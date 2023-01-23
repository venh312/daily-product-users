package com.daily.product.users.msg;

public enum Login {
    SUCCESS("Successful."),
    EMPTY("Empty Data."),
    LOCK("Account is locked."),
    WITHDRAWAL("Withdrawn user."),
    NOT_MATCH_PASSWORD("Invalid password."),
    REISSUE("Successful reissue."),
    EXPIRES_REISSUE("Expires reissue."),
    NOT_MATCH_REISSUE("Token does not match.");

    private final String value;
    Login(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
