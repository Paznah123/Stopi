package com.example.Stopi.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import com.example.Stopi.MainActivity;
import com.example.Stopi.R;
import com.example.Stopi.tools.App;
import com.example.Stopi.tools.KEYS;
import com.example.Stopi.tools.Utils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private enum LOGIN_STATE {
        ENTERING_NUMBER,
        ENTERING_CODE
    }

    private LOGIN_STATE login_state = LOGIN_STATE.ENTERING_NUMBER;

    private CountryCodePicker country_code_picker;
    private String phoneNumber = "";

    private String mVerificationId;

    private TextInputLayout login_EDT_phone;
    private MaterialButton login_btn_phone;
    private MaterialButton login_btn_google;

    private FirebaseAuth firebaseAuth;

    private GoogleSignInClient signInClient;

    // =============================================================

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == KEYS.GOOGLE_SIGN_IN_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                signInWithGoogleAccount(account);
            } catch (ApiException e) { e.printStackTrace(); }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();

        createRequest();

        findViews();
        setListeners();

        if(App.getLoggedUser() != null)
            Utils.get().myStartActivity(this, MainActivity.class);
    }

    // =============================================================

    private void findViews() {
        login_EDT_phone = findViewById(R.id.login_EDT_phone);
        country_code_picker = findViewById(R.id.country_code_picker);
        login_btn_phone = findViewById(R.id.login_btn_login);
        login_btn_google = findViewById(R.id.google_sign_in);
    }

    private void setListeners() {
        login_btn_phone.setOnClickListener(v -> loginClicked());

        login_btn_google.setOnClickListener(v -> googleSignIn());
    }

    private void updateUI() {
        if (login_state == LOGIN_STATE.ENTERING_NUMBER) {
            login_EDT_phone.setHint(getString(R.string.phone_number));
            login_btn_phone.setText("Send Code");
        } else if (login_state == LOGIN_STATE.ENTERING_CODE){
            login_EDT_phone.setHint(getString(R.string.enter_code));
            login_EDT_phone.getEditText().setText("");
            login_btn_phone.setText("Login");
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
        if(!validateLoginInput(phoneNumber)) return;

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
        if(!validateLoginInput(verificationCode)) return;

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode);
        signInWithPhoneAuthCredential(credential);
    }

    // =============================================================

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Utils.get().myStartActivity(this,MainActivity.class);
                App.toast("Login Successful");
            } else {
                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                    App.toast("Incorrect Verification Code");
            }
        });
    }

    // =============================================================

    private boolean validateLoginInput(String input){
        if(input.isEmpty()) {
            login_EDT_phone.setError("Phone Number is Required");
            login_EDT_phone.requestFocus();
            return false;
        }

        if((input.length() < 10 && login_state.equals(LOGIN_STATE.ENTERING_NUMBER))
                || (input.length() < 6 && login_state.equals(LOGIN_STATE.ENTERING_CODE))){
            login_EDT_phone.setError("Enter a Valid Number");
            login_EDT_phone.requestFocus();
            return false;
        }

        return true;
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
        public void onCodeAutoRetrievalTimeOut(@NonNull String s) { super.onCodeAutoRetrievalTimeOut(s); }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) { }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            App.toast("Verification Failed");
            e.printStackTrace();
            login_state = LOGIN_STATE.ENTERING_NUMBER;
            updateUI();
        }
    };

    // ============================================================= google sign in

    private void createRequest() {
        GoogleSignInOptions gsio = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();
        signInClient = GoogleSignIn.getClient(this, gsio);
    }

    private void googleSignIn(){
        Intent signInIntent = signInClient.getSignInIntent();
        startActivityForResult(signInIntent,KEYS.GOOGLE_SIGN_IN_CODE);
    }

    private void signInWithGoogleAccount(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Utils.get().myStartActivity(LoginActivity.this,MainActivity.class);
                        App.toast("Login Successful!");
                    } else
                        App.toast("Login Failed!");
                });
    }
}