package xyz.fz.springBootVertx.provider;

public class RestException extends RuntimeException {
    public RestException(String message) {
        super(message);
    }
}
