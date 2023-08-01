package site.sorghum.anno.spring.filter;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.Setter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Request 包装
 * 流可重置
 *
 * @author sorghum
 * @since 2023/04/22
 */
public class RequestWrapper extends HttpServletRequestWrapper {

    ServletInputStreamWrapper servletInputStreamWrapper;

    Map<String, String[]> paramMap = new HashMap<>();


    public RequestWrapper(ServletRequest request) throws IOException {
        super((HttpServletRequest) request);
        servletInputStreamWrapper = new ServletInputStreamWrapper(super.getInputStream());
        if(super.getParameterMap().size() > 0){
            paramMap = new HashMap<>(super.getParameterMap());
        }
    }

    @Override
    public ServletInputStream getInputStream(){
        return servletInputStreamWrapper;
    }

    public String getBody() throws IOException {
        String body = StrUtil.str(servletInputStreamWrapper.readAllBytes(), getCharacterEncoding());
        getInputStream().reset();
        return body;
    }

    public String setBody(String body) throws IOException {
        this.servletInputStreamWrapper = new ServletInputStreamWrapper(body.getBytes());
        getInputStream().reset();
        return body;
    }


    @Override
    public Map<String, String[]> getParameterMap() {
        return paramMap;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return new Enumeration<>() {
            private int index = 0;
            private final String[] keys = paramMap.keySet().toArray(new String[0]);

            @Override
            public boolean hasMoreElements() {
                return index < keys.length;
            }

            @Override
            public String nextElement() {
                return keys[index++];
            }
        };
    }

    @Override
    public String[] getParameterValues(String name) {
        return paramMap.get(name);
    }
}
