package site.sorghum.anno.response;

import lombok.Data;
import org.noear.solon.annotation.Note;
import org.noear.solon.core.handle.Result;

/**
 * Anno结果
 *
 * @author sorghum
 * @since 2023/05/20
 */
@Data
public class AnnoResult<T> {
    /**
     * 状态码
     */
    int status;

    /**
     * 数据
     */
    T data;

    /**
     * 消息
     */
    String msg;

    public static<T> AnnoResult<T> from(Result<T> result){
        AnnoResult<T> annoResult = new AnnoResult<>();
        annoResult.status = result.getCode();
        if (annoResult.status == 200){
            // 200 为成功
            annoResult.status = 0;
        }
        annoResult.data = result.getData();
        annoResult.msg = result.getDescription();
        return annoResult;
    }

    public AnnoResult(){
        this.status = Result.SUCCEED_CODE;
        this.msg = "";
    }

    public AnnoResult(T data) {
        this.status = Result.SUCCEED_CODE;
        this.msg = "";
        this.data = data;
    }

    public AnnoResult(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public AnnoResult(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 成功的空结果
     */
    @Note("成功的空结果")
    public static <T> AnnoResult<T> succeed() {
        return new AnnoResult<>(Result.SUCCEED_CODE, "");
    }

    /**
     * 成功的结果
     */
    @Note("成功的结果")
    public static <T> AnnoResult<T> succeed(T data) {
        return new AnnoResult<>(data);
    }

    @Note("成功的结果")
    public static <T> AnnoResult<T> succeed(T data, String status) {
        return new AnnoResult<>(Result.SUCCEED_CODE, status, data);
    }

    @Note("成功的结果")
    public static <T> AnnoResult<T> succeed(T data, int status) {
        return new AnnoResult<>(status, "", data);
    }

    /**
     * 成功的空结果
     */
    @Note("失败的空结果")
    public static <T> AnnoResult<T> failure() {
        return new AnnoResult<>(Result.FAILURE_CODE, "");
    }

    /**
     * 失败的结果
     */
    @Note("失败的结果")
    public static <T> AnnoResult<T> failure(int status) {
        return failure(status, "");
    }

    /**
     * 失败的结果
     */
    @Note("失败的结果")
    public static <T> AnnoResult<T> failure(int status, String msg) {
        return new AnnoResult<>(status, msg);
    }

    /**
     * 失败的结果
     */
    @Note("失败的结果")
    public static <T> AnnoResult<T> failure(int status, String msg, T data) {
        return new AnnoResult<>(status, msg, data);
    }

    @Note("失败的结果")
    public static <T> AnnoResult<T> failure(String status) {
        return new AnnoResult<>(Result.FAILURE_CODE, status);
    }
    
}
