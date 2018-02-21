package com.github.mmichaelis.galen;

@SuppressWarnings({"unused", "WeakerAccess"})
public class GalenExtensionException extends RuntimeException {
    private static final long serialVersionUID = -8999808417481402928L;

    public GalenExtensionException() {
        super();
    }

    public GalenExtensionException(String message) {
        super(message);
    }

    public GalenExtensionException(String message, Throwable cause) {
        super(message, cause);
    }

    public GalenExtensionException(Throwable cause) {
        super(cause);
    }

    protected GalenExtensionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
