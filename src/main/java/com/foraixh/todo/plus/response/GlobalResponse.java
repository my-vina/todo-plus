package com.foraixh.todo.plus.response;

/**
 * @author myvina@qq.com
 * @date 2020/8/24  17:01
 * @usage
 */

public class GlobalResponse<T> {
    private static final String SUCCESS = "操作成功";
    private static final String FAILURE = "操作失败";
    private static final String SUCCESS_CODE = "0";
    private static final String FAILURE_CODE = "500";
    private String code;
    private String msg;
    private T data;

    public static<T> GlobalResponse<T> success(T data) {
        GlobalResponse<T> response = new GlobalResponse<>();
        response.setCode(SUCCESS_CODE);
        response.setMsg(SUCCESS);
        response.setData(data);
        return response;
    }

    public static<T> GlobalResponse<T> success(String msg, T data) {
        GlobalResponse<T> response = new GlobalResponse<>();
        response.setCode(SUCCESS_CODE);
        response.setMsg(msg);
        response.setData(data);
        return response;
    }

    public static<T> GlobalResponse<T> failure(T data) {
        GlobalResponse<T> response = new GlobalResponse<>();
        response.setCode(FAILURE_CODE);
        response.setMsg(FAILURE);
        response.setData(data);
        return response;
    }

    public static<T> GlobalResponse<T> failure(String msg, T data) {
        GlobalResponse<T> response = new GlobalResponse<>();
        response.setCode(FAILURE_CODE);
        response.setMsg(msg);
        response.setData(data);
        return response;
    }

    @Override
    public String toString() {
        return "GlobalResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static final class GlobalResponseBuilder<T> {
        private GlobalResponse globalResponse;

        private GlobalResponseBuilder() {
            globalResponse = new GlobalResponse();
        }

        public static GlobalResponseBuilder builder() {
            return new GlobalResponseBuilder();
        }

        public GlobalResponseBuilder code(String code) {
            globalResponse.setCode(code);
            return this;
        }

        public GlobalResponseBuilder msg(String msg) {
            globalResponse.setMsg(msg);
            return this;
        }

        public GlobalResponseBuilder data(T data) {
            globalResponse.setData(data);
            return this;
        }

        public GlobalResponse build() {
            return globalResponse;
        }
    }
}
