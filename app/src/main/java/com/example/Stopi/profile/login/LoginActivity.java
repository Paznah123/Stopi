package com.example.Stopi.profile.login;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import com.example.Stopi.MainActivity;
import com.example.Stopi.R;
import com.example.Stopi.tools.App;
import com.example.Stopi.tools.KEYS;
import com.example.Stopi.profile.login.LoginAPIs.LOGIN_STATE;
import com.example.Stopi.tools.Utils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.hbb20.CountryCodePicker;

public class LoginActivity extends AppCompatActivity implements LoginListener {

    private CountryCodePicker   country_code_picker;

    private TextInputLayout     login_EDT_phone;
    private MaterialButton      login_btn_phone;
    private MaterialButton      login_btn_google;

    private LoginAPIs           loginAPIs;

    // =============================================================

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == KEYS.GOOGLE_SIGN_IN_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                loginAPIs.signInWithGoogleAccount(account);
            } catch (ApiException e) { e.printStackTrace(); }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        loginAPIs = new LoginAPIs(this);

        findViews();
        setListeners();

        if(App.getLoggedUser() != null)
            Utils.get().myStartActivity(this, MainActivity.class);
    }

    // =============================================================

    private void findViews() {
        login_EDT_phone     = findViewById(R.id.login_EDT_phone);
        country_code_picker = findViewById(R.id.country_code_picker);
        login_btn_phone     = findViewById(R.id.login_btn_login);
        login_btn_google    = findViewById(R.id.google_sign_in);
    }

    private void setListeners() {
        login_btn_google.setOnClickListener(v -> loginAPIs.googleSignIn());

        login_btn_phone.setOnClickListener(v -> {
            String ccp = country_code_picker.getSelectedCountryCodeWithPlus();
            String input = login_EDT_phone.getEditText().getText().toString();
            loginAPIs.phoneSignIn(ccp, input);
        });
    }

    // ============================================================= call backs

    @Override
    public void onLoginStatusChange(LOGIN_STATE loginState) {
        if (loginState == LOGIN_STATE.ENTERING_NUMBER) {
            login_EDT_phone.setHint(getString(R.string.phone_number));
            login_btn_phone.setText("Send Code");
        } else if (loginState == LOGIN_STATE.ENTERING_CODE){
            login_EDT_phone.setHint(getString(R.string.enter_code));
            login_EDT_phone.getEditText().setText("");
            login_btn_phone.setText("Login");
        }
    }

    @Override
    public void onInputError(String error) {
        login_EDT_phone.setError(error);
        login_EDT_phone.requestFocus();
    }

}