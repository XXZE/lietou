package com.admin.common;

import java.io.Serializable;

/**
 * 统一响应结果类
 * @param <T> 数据类型
 */
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 提示信息
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    /**
     * 成功码
     */
    public static final int SUCCESS = 200;

    /**
     * 失败码
     */
    public static final int ERROR = 500;

    /**
     * 未授权码
     */
    public static final int UNAUTHORIZED = 401;

    /**
     * 资源未找到
     */
    public static final int NOT_FOUND = 404;

    /**
     * 禁止访问
     */
    public static final int FORBIDDEN = 403;

    private Result() {
    }

    private Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功返回（无数据）
     */
    public static <T> Result<T> success() {
        return new Result<>(SUCCESS, "操作成功", null);
    }

    /**
     * 成功返回（带数据）
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(SUCCESS, "操作成功", data);
    }

    /**
     * 成功返回（自定义消息）
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(SUCCESS, message, data);
    }

    /**
     * 失败返回（无数据）
     */
    public static <T> Result<T> error() {
        return new Result<>(ERROR, "操作失败", null);
    }

    /**
     * 失败返回（自定义消息）
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(ERROR, message, null);
    }

    /**
     * 失败返回（带状态码）
     */
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null);
    }

    /**
     * 未授权返回
     */
    public static <T> Result<T> unauthorized() {
        return new Result<>(UNAUTHORIZED, "未授权，请重新登录", null);
    }

    /**
     * 未授权返回（自定义消息）
     */
    public static <T> Result<T> unauthorized(String message) {
        return new Result<>(UNAUTHORIZED, message, null);
    }

    /**
     * 资源未找到
     */
    public static <T> Result<T> notFound() {
        return new Result<>(NOT_FOUND, "资源不存在", null);
    }

    /**
     * 资源未找到（自定义消息）
     */
    public static <T> Result<T> notFound(String message) {
        return new Result<>(NOT_FOUND, message, null);
    }

    // Getter and Setter
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
