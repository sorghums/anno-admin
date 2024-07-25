package site.sorghum.anno.pf4j;

import cn.hutool.core.util.ClassLoaderUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * AnnoAdmin全类加载器
 *
 * @author Sorghum
 * @since 2024/07/25
 */
public class Pf4jWholeClassLoader extends ClassLoader {

    public static List<ClassLoader> classLoaders = new ArrayList<ClassLoader>();

    static {
        classLoaders.add(ClassLoaderUtil.getContextClassLoader());
    }

    public static void addClassLoader(ClassLoader pluginClassLoader) {
        classLoaders.add(pluginClassLoader);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        for (ClassLoader classLoader : classLoaders) {
            try {
                if (classLoader.loadClass(name) != null) {
                    return classLoader.loadClass(name);
                }
            }catch (ClassNotFoundException ignore){}
        }
        throw new ClassNotFoundException(name);
    }


    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        for (ClassLoader classLoader : classLoaders) {
            try {
                if (classLoader.loadClass(name) != null) {
                    return classLoader.loadClass(name);
                }
            }catch (ClassNotFoundException ignore){}
        }
        throw new ClassNotFoundException(name);
    }
}
