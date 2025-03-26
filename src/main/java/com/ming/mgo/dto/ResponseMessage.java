package com.ming.mgo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMessage<T> {
    private int code;
    private String message;
    private T data;

    public static <T> ResponseMessage<T> success(T data) {
        return new ResponseMessage<>(200, "success", data);
    }

    public static <T> ResponseMessage<T> success(String message, T data) {
        return new ResponseMessage<>(200, message, data);
    }

    public static <T> ResponseMessage<T> success(String message) {
        return new ResponseMessage<>(200, message, null);
    }

    public static <T> ResponseMessage<T> error(int code, String message) {
        return new ResponseMessage<>(code, message, null);
    }

    public static <T> ResponseMessage<T> error(int code, String message, T data) {
        return new ResponseMessage<>(code, message, data);
    }

    @Override
    public String toString() {
        return "ResponseMessage{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(T data) {
        this.data = data;
    }
}
