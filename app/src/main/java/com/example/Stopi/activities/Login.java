package com.example.Stopi.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import com.example.Stopi.App;
import com.example.Stopi.R;
import com.example.Stopi.Utils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity {

    private enum LOGIN_STATE {
        ENTERING_NUMBER,
        ENTERING_CODE
    }

    private LOGIN_STATE login_state = LOGIN_STATE.ENTERING_NUMBER;

    private CountryCodePicker country_code_picker;
    private String phoneNumber = "";

    private String mVerificationId;

    private TextInputLayout login_EDT_phone;
    private MaterialButton login_btn_login;

    private FirebaseAuth firebaseAuth;

    // =============================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();

        findViews();
        setListeners();

        if(App.getLoggedUser() != null)
            Utils.myStartActivity(this,MainActivity.class);
    }

    // =============================================================

    private void findViews() {
        login_EDT_phone = findViewById(R.id.login_EDT_phone);
        country_code_picker = findViewById(R.id.country_code_picker);
        login_btn_login = findViewById(R.id.login_btn_login);
    }

    private void setListeners() {
        login_btn_login.setOnClickListener(v -> {
            loginClicked();
        });
    }

    private void updateUI() {
        if (login_state == LOGIN_STATE.ENTERING_NUMBER) {
            login_EDT_phone.setHint(getString(R.string.phone_number));
            login_btn_login.setText("Send Code");
        } else if (login_state == LOGIN_STATE.ENTERING_CODE){
            login_EDT_phone.setHint(getString(R.string.enter_code));
            login_EDT_phone.getEditText().setText("");
            login_btn_login.setText("Login");
        }
    }

    // =============================================================

    private void loginClicked(){
        if(login_state == LOGIN_STATE.ENTERING_NUMBER)
            startLoginProcess();
        else if(login_state == LOGIN_STATE.ENTERING_CODE)
            codeEntered();
    }

    private void startLoginProcess() {
        phoneNumber = country_code_picker.getSelectedCountryCodeWithPlus() + login_EDT_phone.getEditText().getText().toString();
        validateLoginInput(login_EDT_phone.getEditText(), phoneNumber);

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(phoneAuthCallBack)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void codeEntered() {
        String verificationCode = login_EDT_phone.getEditText().getText().toString();

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode);
        signInWithPhoneAuthCredential(credential);
    }

    // =============================================================

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Utils.myStartActivity(this,MainActivity.class);
                App.toast("Login Successful");
            } else {
                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                    App.toast("Incorrect Verification Code");
            }
        });
    }

    // =============================================================

    private void validateLoginInput(EditText editText, String phoneNumber){
        if(phoneNumber.isEmpty()) {
            editText.setError("Phone Number is Required");
            editText.requestFocus();
            return;
        }

        if(phoneNumber.length() < 10){
            editText.setError("Enter a Valid Number");
            editText.requestFocus();
            return;
        }
    }

    // =============================================================

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks phoneAuthCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
            App.toast("Code Sent!");
            login_state = LOGIN_STATE.ENTERING_CODE;
            mVerificationId = verificationId;
            updateUI();
        }

        @Override
        public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
            super.onCodeAutoRetrievalTimeOut(s);
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) { }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            App.toast("Verification Failed");
            login_state = LOGIN_STATE.ENTERING_NUMBER;
            updateUI();
        }
    };
}