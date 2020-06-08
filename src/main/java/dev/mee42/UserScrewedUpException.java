package dev.mee42;

public class UserScrewedUpException extends RuntimeException {
    public UserScrewedUpException(String s) {
        super(s);
    }
}
