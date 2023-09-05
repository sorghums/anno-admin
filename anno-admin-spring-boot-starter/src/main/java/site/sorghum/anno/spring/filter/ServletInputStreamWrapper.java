package site.sorghum.anno.spring.filter;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * servlet输入流包装器
 *
 * @author sorghum
 * @since 2023/04/22
 */
@Slf4j
public class ServletInputStreamWrapper extends ServletInputStream {
    private ByteArrayInputStream byteArrayInputStream;

    public ServletInputStreamWrapper(ServletInputStream inputStream) {
        try {
            byteArrayInputStream = new ByteArrayInputStream(inputStream.readAllBytes());
        } catch (IOException e) {
            log.error("异常信息：",e);
        }
    }

    public ServletInputStreamWrapper(byte[] bytes) {
        byteArrayInputStream = new ByteArrayInputStream(bytes);
    }
    @Override
    public boolean isFinished() {
        return byteArrayInputStream.available() == 0;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(ReadListener listener) {
    }

    @Override
    public int read(){
        return byteArrayInputStream.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        return byteArrayInputStream.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return byteArrayInputStream.read(b, off, len);
    }

    @Override
    public byte[] readAllBytes() throws IOException {
        return byteArrayInputStream.readAllBytes();
    }

    @Override
    public byte[] readNBytes(int len) throws IOException {
        return byteArrayInputStream.readNBytes(len);
    }

    @Override
    public int readNBytes(byte[] b, int off, int len) throws IOException {
        return byteArrayInputStream.readNBytes(b, off, len);
    }

    @Override
    public long skip(long n) throws IOException {
        return byteArrayInputStream.skip(n);
    }

    @Override
    public void skipNBytes(long n) throws IOException {
        byteArrayInputStream.skipNBytes(n);
    }

    @Override
    public int available() throws IOException {
        return byteArrayInputStream.available();
    }

    @Override
    public void close() throws IOException {
        byteArrayInputStream.close();
    }

    @Override
    public synchronized void mark(int readlimit) {
        byteArrayInputStream.mark(readlimit);
    }

    @Override
    public synchronized void reset() throws IOException {
        byteArrayInputStream.reset();
    }

    @Override
    public boolean markSupported() {
        return byteArrayInputStream.markSupported();
    }

    @Override
    public long transferTo(OutputStream out) throws IOException {
        return byteArrayInputStream.transferTo(out);
    }
}
