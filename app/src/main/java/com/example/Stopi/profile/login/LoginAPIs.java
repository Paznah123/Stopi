package com.example.Stopi.profile.login;

import android.app.Activity;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.example.Stopi.MainActivity;
import com.example.Stopi.R;
import com.example.Stopi.tools.App;
import com.example.Stopi.tools.KEYS;
import com.example.Stopi.tools.Utils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import java.util.concurrent.TimeUnit;

public class LoginAPIs {

    public enum LOGIN_STATE {
        ENTERING_NUMBER,
        ENTERING_CODE
    }

    private LOGIN_STATE login_state = LOGIN_STATE.ENTERING_NUMBER;

    private Activity            activity;
    private FirebaseAuth        firebaseAuth;
    private GoogleSignInClient  signInClient;

    private String              mVerificationId;

    private LoginListener loginListener;

    public LoginAPIs(Activity activity){
        this.activity           = activity;
        this.loginListener = (LoginListener) activity;
        this.firebaseAuth       = FirebaseAuth.getInstance();

        createRequest();
    }

    private void linkWithCredential(AuthCredential credential){
        firebaseAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        Utils.get().myStartActivity(activity,MainActivity.class);
                        App.toast("Login Successful!");
                    } else
                        App.toast("Login Failed!");
                });
    }

    // ============================================================= google sign in

    private void createRequest() {
        GoogleSignInOptions gsio = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail().build();
        signInClient = GoogleSignIn.getClient(activity, gsio);
    }

    public void googleSignIn(){
        Intent signInIntent = signInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, KEYS.GOOGLE_SIGN_IN_CODE);
    }

    public void signInWithGoogleAccount(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        Utils.get().myStartActivity(activity,MainActivity.class);
                        App.toast("Login Successful!");
                    } else
                        App.toast("Login Failed!");
                });
    }

    // ============================================================= phone sign in

    public void phoneSignIn(String countryCode, String input) {
        if(login_state == LOGIN_STATE.ENTERING_NUMBER)
            startLoginProcess(countryCode + input);
        else if(login_state == LOGIN_STATE.ENTERING_CODE)
            codeEntered(input);
    }

    private void startLoginProcess(String phoneNumber) {
        if(!validateLoginInput(phoneNumber)) return;

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(activity)
                        .setCallbacks(phoneAuthCallBack)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void codeEntered(String verificationCode) {
        if(!validateLoginInput(verificationCode)) return;

        AuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(AuthCredential credential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(activity, task -> {
            if (task.isSuccessful()) {
                Utils.get().myStartActivity(activity, MainActivity.class);
                App.toast("Login Successful");
            } else {
                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                    App.toast("Incorrect Verification Code");
            }
        });
    }

    // ============================================================= phone call back

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks phoneAuthCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
            App.toast("Code Sent!");
            mVerificationId = verificationId;
            login_state = LOGIN_STATE.ENTERING_CODE;
            loginListener.onLoginStatusChange(login_state);
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
            loginListener.onLoginStatusChange(login_state);
        }
    };

    // ============================================================= validation

    private boolean validateLoginInput(String input){
        if(input.isEmpty()) {
            loginListener.onInputError("Phone Number is Required");
            return false;
        }

        if((input.length() < 10 && login_state.equals(LOGIN_STATE.ENTERING_NUMBER))
                || (input.length() < 6 && login_state.equals(LOGIN_STATE.ENTERING_CODE))){
            loginListener.onInputError("Enter a Valid Number");
            return false;
        }

        return true;
    }
}
