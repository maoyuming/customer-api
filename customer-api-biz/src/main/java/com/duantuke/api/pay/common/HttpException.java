package com.duantuke.api.pay.common;

public class HttpException  extends RuntimeException{

    private static final long serialVersionUID = -7838382099833587167L;
    private final Integer code;

    public HttpException(Integer code) {
        super();
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

}
