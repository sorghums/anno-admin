package site.sorghum.anno.method.resource;

import cn.hutool.core.collection.EnumerationIter;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.FileResource;
import cn.hutool.core.io.resource.MultiResource;
import cn.hutool.core.io.resource.Resource;
import cn.hutool.core.text.AntPathMatcher;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.ClassLoaderUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipException;

/**
 * 资源查找器<br>
 * <p>
 * 参考Spring的PathMatchingResourcePatternResolver，实现classpath资源查找，利用{@link AntPathMatcher}筛选资源。
 *
 * @author Spring, Looly
 */
public class ResourceFinder {

    /**
     * 构建新的ResourceFinder，使用当前环境的类加载器
     *
     * @return ResourceFinder
     */
    public static ResourceFinder of() {
        return of(ClassLoaderUtil.getClassLoader());
    }

    /**
     * 构建新的ResourceFinder
     *
     * @param classLoader 类加载器，用于限定查找范围
     * @return ResourceFinder
     */
    public static ResourceFinder of(final ClassLoader classLoader) {
        return new ResourceFinder(classLoader);
    }

    private final ClassLoader classLoader;
    private final AntPathMatcher pathMatcher;

    /**
     * 构造
     *
     * @param classLoader 类加载器，用于定义查找资源的范围
     */
    public ResourceFinder(final ClassLoader classLoader) {
        this.classLoader = classLoader;
        this.pathMatcher = new AntPathMatcher();
    }

    /**
     * 查找给定表达式对应的资源
     *
     * @param locationPattern 路径表达式
     * @return {@link MultiResource}
     */
    public MultiResource find(final String locationPattern) {
        // 根目录，如 "/WEB-INF/*.xml" 返回 "/WEB-INF/"
        final String rootDirPath = determineRootDir(locationPattern);
        // 子表达式，如"/WEB-INF/*.xml" 返回 "*.xml"
        final String subPattern = locationPattern.substring(rootDirPath.length());

        final MultiResource result = new MultiResource();
        // 遍历根目录下所有资源，并过滤保留符合条件的资源
        for (final Resource rootResource : site.sorghum.anno.method.resource.ResourceUtil.getResources(rootDirPath, classLoader)) {
            if (rootResource instanceof JarResource) {
                // 在jar包中
                try {
                    MultiResource inJar = findInJar((JarResource) rootResource, subPattern);
                    for (Resource resource : inJar) {
                        result.add(resource);
                    }
                } catch (final IOException e) {
                    throw new IORuntimeException(e);
                }
            } else if (rootResource instanceof FileResource) {
                // 文件夹中
                MultiResource inDir = findInDir((FileResource) rootResource, subPattern);
                for (Resource resource : inDir) {
                    result.add(resource);
                }
            } else {
                throw new IORuntimeException("Unsupported resource type: {}", rootResource.getClass().getName());
            }
        }

        return result;
    }

    /**
     * 查找jar包中的资源
     *
     * @param rootResource 根资源，为jar包文件
     * @param subPattern   子表达式，如 *.xml
     * @return 符合条件的资源
     * @throws IOException IO异常
     */
    protected MultiResource findInJar(final JarResource rootResource, final String subPattern) throws IOException {
        final URL rootDirURL = rootResource.getUrl();
        final URLConnection conn = rootDirURL.openConnection();

        final JarFile jarFile;
        String rootEntryPath;
        final boolean closeJarFile;

        if (conn instanceof JarURLConnection) {
            final JarURLConnection jarCon = (JarURLConnection) conn;
            URLUtil.useCachesIfNecessary(jarCon);
            jarFile = jarCon.getJarFile();
            final JarEntry jarEntry = jarCon.getJarEntry();
            rootEntryPath = (jarEntry != null ? jarEntry.getName() : StrUtil.EMPTY);
            closeJarFile = !jarCon.getUseCaches();
        } else {
            // 去除子路径后重新获取jar文件
            final String urlFile = rootDirURL.getFile();
            try {
                int separatorIndex = urlFile.indexOf(URLUtil.WAR_URL_SEPARATOR);
                if (separatorIndex == -1) {
                    separatorIndex = urlFile.indexOf(URLUtil.JAR_URL_SEPARATOR);
                }
                if (separatorIndex != -1) {
                    final String jarFileUrl = urlFile.substring(0, separatorIndex);
                    rootEntryPath = urlFile.substring(separatorIndex + 2);  // both separators are 2 chars
                    jarFile = JarResource.ofJar(jarFileUrl);
                } else {
                    jarFile = new JarFile(urlFile);
                    rootEntryPath = StrUtil.EMPTY;
                }
                closeJarFile = true;
            } catch (final ZipException ex) {
                return new MultiResource();
            }
        }

        rootEntryPath = StrUtil.addSuffixIfNot(rootEntryPath, StrUtil.SLASH);
        // 遍历jar中的entry，筛选之
        final MultiResource result = new MultiResource();

        try {
            String entryPath;
            for (final JarEntry entry : new EnumerationIter<>(jarFile.entries())) {
                entryPath = entry.getName();
                if (entryPath.startsWith(rootEntryPath)) {
                    final String relativePath = entryPath.substring(rootEntryPath.length());
                    if (pathMatcher.match(subPattern, relativePath)) {
                        result.add(ResourceUtil.getResource(ResourceUtil.getURL(rootDirURL, relativePath)));
                    }
                }
            }
        } finally {
            if (closeJarFile) {
                IoUtil.close(jarFile);
            }
        }

        return result;
    }

    /**
     * 遍历目录查找指定表达式匹配的文件列表
     *
     * @param resource   文件资源
     * @param subPattern 子表达式
     * @return 满足条件的文件
     */
    protected MultiResource findInDir(final FileResource resource, final String subPattern) {
        final MultiResource result = new MultiResource();
        final File rootDir = resource.getFile();
        if (!rootDir.exists() || !rootDir.isDirectory() || !rootDir.canRead()) {
            // 保证给定文件存在、为目录且可读
            return result;
        }

        final String fullPattern = replaceBackSlash(rootDir.getAbsolutePath() + StrUtil.SLASH + subPattern);

        ResourceUtil.walkFiles(rootDir, (file -> {
            final String currentPath = replaceBackSlash(file.getAbsolutePath());
            if (file.isDirectory()) {
                // 检查目录是否满足表达式开始规则，满足则继续向下查找，否则跳过
                return pathMatcher.matchStart(fullPattern, StrUtil.addSuffixIfNot(currentPath, StrUtil.SLASH));
            }

            if (pathMatcher.match(fullPattern, currentPath)) {
                result.add(new FileResource(file));
                return true;
            }

            return false;
        }));

        return result;
    }

    /**
     * 根据给定的路径表达式，找到跟路径<br>
     * 根路径即不包含表达式的路径，如 "/WEB-INF/*.xml" 返回 "/WEB-INF/"
     *
     * @param location 路径表达式
     * @return root dir
     */
    protected String determineRootDir(final String location) {
        final int prefixEnd = location.indexOf(':') + 1;
        int rootDirEnd = location.length();
        while (rootDirEnd > prefixEnd && pathMatcher.isPattern(location.substring(prefixEnd, rootDirEnd))) {
            rootDirEnd = location.lastIndexOf(CharUtil.SLASH, rootDirEnd - 2) + 1;
        }
        if (rootDirEnd == 0) {
            rootDirEnd = prefixEnd;
        }
        return location.substring(0, rootDirEnd);
    }

    /**
     * 替换'\'为'/'
     *
     * @param path 路径
     * @return 替换后的路径
     */
    private static String replaceBackSlash(final String path) {
        return StrUtil.isEmpty(path) ? path : path.replace(CharUtil.BACKSLASH, CharUtil.SLASH);
    }
}
