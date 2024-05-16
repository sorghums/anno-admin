package site.sorghum.anno._common.response;

import lombok.Data;
import site.sorghum.anno._common.util.JSONUtil;

/**
 * Anno结果
 *
 * @author sorghum
 * @since 2023/05/20
 */
@Data
public class AnnoResult<T> {
    private static final int SUCCEED_CODE = 0;
    private static final int FAILURE_CODE = 400;
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

    public AnnoResult(){
        this.status = AnnoResult.SUCCEED_CODE;
        this.msg = "操作成功";
    }

    public AnnoResult(T data) {
        this.status = AnnoResult.SUCCEED_CODE;
        this.msg = "操作成功";
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
    
    public static <T> AnnoResult<T> succeed() {
        return new AnnoResult<>(AnnoResult.SUCCEED_CODE, "操作成功");
    }

    /**
     * 成功的结果
     */
    
    public static <T> AnnoResult<T> succeed(T data) {
        return new AnnoResult<>(data);
    }

    
    public static <T> AnnoResult<T> succeed(T data, String status) {
        return new AnnoResult<>(AnnoResult.SUCCEED_CODE, status, data);
    }

    
    public static <T> AnnoResult<T> succeed(T data, int status) {
        return new AnnoResult<>(status, "操作成功", data);
    }

    /**
     * 成功的空结果
     */
    
    public static <T> AnnoResult<T> failure() {
        return new AnnoResult<>(AnnoResult.FAILURE_CODE, "操作失败");
    }

    /**
     * 失败的结果
     */
    
    public static <T> AnnoResult<T> failure(int status) {
        return failure(status, "操作失败");
    }

    /**
     * 失败的结果
     */
    
    public static <T> AnnoResult<T> failure(int status, String msg) {
        return new AnnoResult<>(status, msg);
    }

    /**
     * 失败的结果
     */
    
    public static <T> AnnoResult<T> failure(int status, String msg, T data) {
        return new AnnoResult<>(status, msg, data);
    }

    
    public static <T> AnnoResult<T> failure(String msg) {
        return new AnnoResult<>(AnnoResult.FAILURE_CODE, msg);
    }


    public AnnoResult<T> withStatus(int status){
        this.status = status;
        return this;
    }

    public AnnoResult<T> withMsg(String msg){
        this.msg = msg;
        return this;
    }

    public AnnoResult<T> withData(T data){
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return JSONUtil.toJsonString(this);
    }
}
