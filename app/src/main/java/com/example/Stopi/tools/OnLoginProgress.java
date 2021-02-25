package com.example.Stopi.tools;

public interface OnLoginProgress {

    void updateUI(LoginAPIs.LOGIN_STATE loginState);

    void updateError(String error);
}
