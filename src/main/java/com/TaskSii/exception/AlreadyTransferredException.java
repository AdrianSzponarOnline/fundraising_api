package com.TaskSii.exception;

public class AlreadyTransferredException extends RuntimeException {
    public AlreadyTransferredException() {
        super();
    }

    public AlreadyTransferredException(String message) {
        super(message);
    }

    public AlreadyTransferredException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyTransferredException(Throwable cause) {
        super(cause);
    }
}
