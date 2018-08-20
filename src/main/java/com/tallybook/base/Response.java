package com.tallybook.base;


public class Response<T> {

    public enum Status {
        //正常返回
        OK(200, "ok"),
        //权限不足
        ERR_PERMISSION_DENIED(301,"ERROR_PERMISSION_DENIED"),
        //无效格式
        ERR_INVALID_FORMAT(302, "ERR_INVALID_FORMAT"),
        //无效参数
        ERR_INVALID_PARAMS(303, "ERR_INVALID_PARAMS"),
        ERR_LIMIT_EXCEEDS(304, "ERR_LIMIT_EXCEEDS"),
        ERR_IS_EXIST(305, "ERR_IS_EXIST"),
        ERR_NOT_EXIST(306, "ERR_NOT_EXIST"),
        ERR_LIMIT_MSG(307, "ERR_LIMIT_EXCEEDS"),
        //运行异常(代码错误)
        ERR_RUNTIME_EXCEPTION(308, "ERR_RUNTIME_EXCEPTION"),
        //用户未开通存管账户
        ERR_UNAUTH_DENIED(309, "ERR_UNAUTH_DENIED"),
        //用户未签约
        ERR_UNSIGN_DENIED(310, "ERR_UNDESIGN_DENIED"),
        //用户余额不足
        ERR_NOT_SUFF_FUNDS(311, "ERR_NOT_SUFF_FUNDS"),
        //预注册失败
        ERR_PREREGISTER_FAIL(312, "ERR_PREREGISTER_FAIL"),
        //预注册失败
        ERR_COUPON_FAIL(313, "ERR_COUPON_FAIL"),
        //用户已预注册
        ERR_IS_PRE_REGISTER(314, "ERR_IS_PRE_REGISTER"),
        ERR_TIMEOUT_THIRDP(401, "ERR_TIMEOUT_THIRDP"),
        ERR_UNABLE_SMS(402, "ERR_UNABLE_SMS"),
        ERR_LIMIT_THIRDP(403, "ERR_LIMIT_THIRDP"),
        //服务器不知名错误
        ERR_RUNTIME(500, "ERR_RUNTIME"),
        ERR_AUTH_FAIL(502,"ERR_AUTH_FAIL"),
        //不支持版本等级
        ERR_VERSION_LOWEST(1403, "ERR_VERSION_LOWEST"),
        ERR_UNKNOWN(9999, "ERR_UNKNOWN");

        Status(int code, String message) {
            this.errCode = code;
            this.errMessage = message;
        }

        private int errCode;
        private String errMessage;

        public int getErrCode() {
            return errCode;
        }

        public String getErrMessage() {
            return errMessage;
        }
    }

    public Response(Status status, T data) {
        this.code = status.getErrCode();
        this.errMessage = status.getErrMessage();
        this.data = data;
    }
    public Response(Status status,String message, T data) {
        this.code = status.getErrCode();
        this.message = message;
        this.data = data;
    }
    public Response(Status status,String message) {
        this.code = status.getErrCode();
        this.message = message;
    }
    public Response(Status status) {
        this.code = status.getErrCode();
        this.errMessage = status.getErrMessage();
    }

    private int code;
    private String errMessage;
    private String message;
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

}