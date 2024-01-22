package site.sorghum.anno.method.resource;

import cn.hutool.core.collection.EnumerationIter;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.resource.FileResource;
import cn.hutool.core.io.resource.MultiResource;
import cn.hutool.core.io.resource.Resource;
import cn.hutool.core.io.resource.UrlResource;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Predicate;

/**
 * @author songyinyin
 * @since 2024/1/16 23:08
 */
public class ResourceUtil {

    /**
     * 获取同名的所有资源<br>
     * 资源的加载顺序是：
     * <ul>
     *     <li>1. 首先在本项目下查找资源文件</li>
     *     <li>2. 按照classpath定义的顺序，去对应路径或jar包下寻找资源文件</li>
     * </ul>
     *
     * @param resource    资源名
     * @param classLoader {@link ClassLoader}，{@code null}表示使用默认的当前上下文ClassLoader
     * @return {@link MultiResource}
     */
    public static MultiResource getResources(final String resource, final ClassLoader classLoader) {
        final EnumerationIter<URL> iter = cn.hutool.core.io.resource.ResourceUtil.getResourceIter(resource, classLoader);
        final MultiResource resources = new MultiResource();
        for (final URL url : iter) {
            resources.add(getResource(url));
        }
        return resources;
    }

    /**
     * 获取{@link UrlResource} 资源对象
     *
     * @param url URL
     * @return {@link Resource} 资源对象
     * @since 6.0.0
     */
    public static Resource getResource(final URL url) {
        if(URLUtil.isJarURL(url)){
            return new JarResource(url);
        } else if(URLUtil.isFileURL(url)){
            return new FileResource(URLUtil.decode(url.getFile()));
        }
        return new UrlResource(url);
    }

    /**
     * 获取相对于给定URL的新的URL<br>
     * 来自：org.springframework.core.io.UrlResource#createRelativeURL
     *
     * @param url 基础URL
     * @param relativePath 相对路径
     * @return 相对于URL的子路径URL
     * @throws IORuntimeException URL格式错误
     * @since 6.0.0
     */
    public static URL getURL(final URL url, String relativePath) {
        // # 在文件路径中合法，但是在URL中非法，此处转义
        relativePath = StrUtil.replace(StrUtil.removePrefix(relativePath, StrUtil.SLASH), "#", "%23");
        try {
            return new URL(url, relativePath);
        } catch (final MalformedURLException e) {
            throw new IORuntimeException(e, "Error occurred when get URL!");
        }
    }

    /**
     * 递归遍历目录并处理目录下的文件，可以处理目录或文件：
     * <ul>
     *     <li>目录和非目录均调用{@link Predicate}处理</li>
     *     <li>目录如果{@link Predicate#test(Object)}为{@code true}则递归调用此方法处理。</li>
     * </ul>
     * 此方法与{@link #loopFiles(File, FileFilter)}不同的是，处理目录判断，可减少无效目录的遍历。
     *
     * @param file     文件或目录，文件直接处理
     * @param predicate 文件处理器，只会处理文件
     * @since 5.5.2
     */
    public static void walkFiles(final File file, final Predicate<File> predicate) {
        if (predicate.test(file) && file.isDirectory()) {
            final File[] subFiles = file.listFiles();
            if (ArrayUtil.isNotEmpty(subFiles)) {
                for (final File tmp : subFiles) {
                    walkFiles(tmp, predicate);
                }
            }
        }
    }
}
