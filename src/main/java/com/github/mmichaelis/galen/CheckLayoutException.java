package com.github.mmichaelis.galen;

public class CheckLayoutException extends RuntimeException {
    public CheckLayoutException() {
        super();
    }

    public CheckLayoutException(String message) {
        super(message);
    }

    public CheckLayoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public CheckLayoutException(Throwable cause) {
        super(cause);
    }

    protected CheckLayoutException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
