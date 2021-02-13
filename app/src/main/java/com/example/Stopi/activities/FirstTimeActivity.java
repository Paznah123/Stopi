package com.example.Stopi.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.Stopi.App;
import com.example.Stopi.R;
import com.example.Stopi.Utils;
import com.example.Stopi.objects.dataManage.DBreader;
import com.example.Stopi.objects.User;
import com.example.Stopi.objects.dataManage.DBupdater;
import com.example.Stopi.objects.dataManage.KEYS;
import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.scrounger.countrycurrencypicker.library.Country;
import com.scrounger.countrycurrencypicker.library.Currency;
import com.scrounger.countrycurrencypicker.library.Listener.CountryCurrencyPickerListener;
import java.util.Calendar;
import java.util.HashMap;

public class FirstTimeActivity extends AppCompatActivity {

    private String          currencySymbol;

    private Uri             filePathUri;
    private ImageView       user_profile_pic;

    private TextInputLayout user_name;
    private TextInputLayout years_smoked;
    private TextInputLayout cigs_per_day;
    private TextInputLayout price_per_pack;
    private TextInputLayout cigs_per_pack;

    private MaterialButton  continue_btn;
    private MaterialButton  currency_btn;

    //====================================

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            filePathUri = data.getData();
            DBupdater.getInstance().uploadImage(filePathUri, user ->
                    DBreader.getInstance().readPic(KEYS.PROFILE, user_profile_pic, App.getLoggedUser().getUid())
            );
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            App.toast(new ImagePicker().Companion.getError(data));
        } else {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time);

        findViews();
        setListeners();
    }

    //====================================

    private void findViews() {
        user_profile_pic =  findViewById(R.id.user_profile_pic);
        user_name =         findViewById(R.id.user_name);
        years_smoked =      findViewById(R.id.years_smoked);
        cigs_per_day =      findViewById(R.id.cigs_per_day);
        price_per_pack =    findViewById(R.id.price_per_pack);
        cigs_per_pack =     findViewById(R.id.cigs_per_pack);
        continue_btn =      findViewById(R.id.first_time_continue);
        currency_btn =      findViewById(R.id.first_btn_currency);
    }

    private void setListeners() {
        user_profile_pic.setOnClickListener(v -> Utils.getInstance().getImage(this));

        continue_btn.setOnClickListener(v -> {
            User user = createUserData();
            DBupdater.getInstance()     .updateUser(user);
            DBreader.getInstance()      .readUserData();
            Utils.getInstance()         .myStartActivity(FirstTimeActivity.this, ActivitySplash.class);
        });

        currency_btn.setOnClickListener(v -> Utils.getInstance()
                .createCurrencyDialog(getSupportFragmentManager(), new CountryCurrencyPickerListener() {
                    @Override
                    public void onSelectCountry(Country country) {
                        currencySymbol = country.getCurrency().getCode() +" "+ country.getCurrency().getSymbol();
                        currency_btn.setText(currencySymbol);
                    }

                    @Override
                    public void onSelectCurrency(Currency currency) { }
        }));

    }

    //====================================================

    private User createUserData() {
        String  name                = user_name.getEditText().getText().toString();
        long    dateStoppedSmoking  = Calendar.getInstance().getTimeInMillis();
        double  yearsSmoked         = Double.parseDouble(0 + years_smoked.getEditText().getText().toString());
        double  pricePerPack        = Double.parseDouble(0 + price_per_pack.getEditText().getText().toString());
        int     cigsPerDay          = Integer.parseInt(0 + cigs_per_day.getEditText().getText().toString());
        int     cigsPerPack         = Integer.parseInt(0 + cigs_per_pack.getEditText().getText().toString());

        return new User().setUid(App.getLoggedUser().getUid())
                        .setName(name)
                        .setYearsSmoked(yearsSmoked)
                        .setCigsPerDay(cigsPerDay)
                        .setPricePerPack(pricePerPack)
                        .setCurrencySymbol(currencySymbol != null ? currencySymbol : "Currency")
                        .setCigsPerPack(cigsPerPack)
                        .setGoal("What is your Goal?")
                        .setCoins(1000)
                        .setLoggedToday(dateStoppedSmoking)
                        .setDateStoppedSmoking(dateStoppedSmoking)
                        .setBoughtItems(new HashMap<>());
    }
}
