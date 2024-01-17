/*
 * Copyright (c) 2023. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package site.sorghum.anno.method.resource;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.resource.UrlResource;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.URLUtil;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.jar.JarFile;

/**
 * Jar包资源对象
 *
 * @author looly
 */
public class JarResource extends UrlResource {
    private static final long serialVersionUID = 1L;

    /**
     * 构造
     *
     * @param uri JAR的URI
     */
    public JarResource(final URI uri) {
        super(uri);
    }

    /**
     * 构造
     *
     * @param url JAR的URL
     */
    public JarResource(final URL url) {
        super(url);
    }

    /**
     * 构造
     *
     * @param url  JAR的URL
     * @param name 资源名称
     */
    public JarResource(final URL url, final String name) {
        super(url, name);
    }

    /**
     * 获取URL对应的{@link JarFile}对象
     *
     * @return {@link JarFile}
     * @throws IORuntimeException IO异常
     */
    public JarFile getJarFile() throws IORuntimeException {
        try {
            return doGetJarFile();
        } catch (final IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 获取{@link JarFile}<br>
     * 首席按通过openConnection方式获取，如果得到的不是{@link JarURLConnection}，<br>
     * 则尝试去除WAR、JAR等协议分隔符，裁剪分隔符前段来直接获取{@link JarFile}。
     *
     * @return {@link JarFile}
     * @throws IOException IO异常
     */
    private JarFile doGetJarFile() throws IOException {
        final URLConnection con = getUrl().openConnection();
        if (con instanceof JarURLConnection) {
            final JarURLConnection jarCon = (JarURLConnection) con;
            return jarCon.getJarFile();
        } else {
            final String urlFile = getUrl().getFile();
            int separatorIndex = urlFile.indexOf(URLUtil.WAR_URL_SEPARATOR);
            if (separatorIndex == -1) {
                separatorIndex = urlFile.indexOf(URLUtil.JAR_URL_SEPARATOR);
            }
            if (separatorIndex != -1) {
                return ofJar(urlFile.substring(0, separatorIndex));
            } else {
                return new JarFile(urlFile);
            }
        }
    }

    /**
     * 获取对应URL路径的jar文件，支持包括file://xxx这类路径<br>
     * 来自：org.springframework.core.io.support.PathMatchingResourcePatternResolver#getJarFile
     *
     * @param jarFileUrl jar文件路径
     * @return {@link JarFile}
     * @throws IORuntimeException IO异常
     * @since 6.0.0
     */
    public static JarFile ofJar(String jarFileUrl) throws IORuntimeException {
        Assert.notBlank(jarFileUrl, "Jar file url is blank!");

        if (jarFileUrl.startsWith(URLUtil.FILE_URL_PREFIX)) {
            try {
                jarFileUrl = URLUtil.toURI(jarFileUrl).getSchemeSpecificPart();
            } catch (final UtilException e) {
                jarFileUrl = jarFileUrl.substring(URLUtil.FILE_URL_PREFIX.length());
            }
        }
        try {
            return new JarFile(jarFileUrl);
        } catch (final IOException e) {
            throw new IORuntimeException(e);
        }
    }
}
