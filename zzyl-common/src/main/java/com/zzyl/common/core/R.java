package com.zzyl.common.core;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 统一响应体
 *
 * @param <T> 数据类型
 */
@Data
@Schema(description = "统一响应体")
public class R<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public static final int SUCCESS = 200;
    public static final int FAIL = 500;
    public static final int UNAUTHORIZED = 401;

    @Schema(description = "状态码")
    private int code;

    @Schema(description = "提示信息")
    private String msg;

    @Schema(description = "业务数据")
    private T data;

    public static <T> R<T> ok() {
        return rest(null, SUCCESS, "操作成功");
    }

    public static <T> R<T> ok(T data) {
        return rest(data, SUCCESS, "操作成功");
    }

    public static <T> R<T> ok(String msg, T data) {
        return rest(data, SUCCESS, msg);
    }

    public static <T> R<T> fail() {
        return rest(null, FAIL, "操作失败");
    }

    public static <T> R<T> fail(String msg) {
        return rest(null, FAIL, msg);
    }

    public static <T> R<T> fail(int code, String msg) {
        return rest(null, code, msg);
    }

    private static <T> R<T> rest(T data, int code, String msg) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setData(data);
        r.setMsg(msg);
        return r;
    }
}
