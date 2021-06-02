package com.kuan.common.eventbus;

/**
 * Created by hongkuan on 2021-05-12 0012.
 */
public class LoginEvent {
    private final String userName;

    public LoginEvent(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
}
