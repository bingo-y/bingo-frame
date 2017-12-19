package com.bingo.common.rx.exception;

/**
 * @author bingo.
 * @date Create on 2017/12/7.
 * @Description 网络请求异常包装
 */

public class ApiException extends RuntimeException {

    @ApiErrorType
    private int type;

    private int code;

    private String message;

    public ApiException(int type, int code, String message) {
        super(message);
        this.type = type;
        this.code = code;
        this.message = message;
    }

    public ApiException(int type, int code, Throwable cause) {
        super(cause);
        this.type = type;
        this.code = code;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
