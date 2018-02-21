package com.github.mmichaelis.galen;

@SuppressWarnings({"unused", "WeakerAccess"})
public class CheckLayoutException extends GalenExtensionException {
    private static final long serialVersionUID = 3082184860732869799L;

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
