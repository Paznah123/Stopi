package com.example.Stopi.profile.login;

public interface LoginListener {

    void onLoginStatusChange(LoginAPIs.LOGIN_STATE loginState);

    void onInputError(String error);
}
