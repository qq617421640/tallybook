package com.tallybook.base;

public class ControllerException extends RuntimeException {

    private Response.Status status;

    public ControllerException(String message) {
        super(message);
    }

    public ControllerException(Response.Status status, String message) {
        super(message);
        this.status = status;
    }

    public Response.Status getStatus() {
        return status;
    }

}
