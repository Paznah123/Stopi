package com.example.Stopi.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.Stopi.R;
import com.example.Stopi.profile.login.LoginActivity;
import com.example.Stopi.tools.App;
import com.example.Stopi.tools.Dialogs;
import com.example.Stopi.profile.login.SharedPrefs;
import com.example.Stopi.tools.Utils;
import com.example.Stopi.dataBase.DBreader;
import com.example.Stopi.dataBase.DBupdater;
import com.example.Stopi.tools.KEYS;
import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.scrounger.countrycurrencypicker.library.Country;
import com.scrounger.countrycurrencypicker.library.Currency;
import com.scrounger.countrycurrencypicker.library.Listener.CountryCurrencyPickerListener;

public class SettingsFragment extends Fragment {

    private View            view;

    private ImageView       user_profile_pic;
    private Uri             filePathUri;

    private String          currencySymbol;

    private TextInputLayout user_name;
    private TextInputLayout years_smoked;
    private TextInputLayout cigs_per_day;
    private TextInputLayout price_per_pack;
    private TextInputLayout cigs_per_pack;

    private MaterialButton  update_btn;
    private MaterialButton  logout;
    private MaterialButton  delete_account;
    private MaterialButton  currency;
    private MaterialButton  theme_btn;

    private OnProfileUpdate onProfileUpdate;

    private User            user;

    //============================================

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onProfileUpdate = (OnProfileUpdate) context;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            filePathUri = data.getData();
            DBupdater.get().uploadImage(filePathUri, user -> {
                user_profile_pic.setImageURI(filePathUri);
                onProfileUpdate.updateProfile(user);
            });
        } else if (resultCode == ImagePicker.RESULT_ERROR)
            App.toast(new ImagePicker().Companion.getError(data));
    }

    //============================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        findViews();
        user = DBreader.get().getUser();

        if(!App.isNetworkAvailable() || user == null) return view;

        setCurrentValues();
        setListeners();

        return view;
    }

    //============================================

    private void findViews() {
        user_profile_pic    = view.findViewById(R.id.user_profile_pic);
        user_name           = view.findViewById(R.id.settings_user_name);
        years_smoked        = view.findViewById(R.id.settings_years_smoked);
        cigs_per_day        = view.findViewById(R.id.settings_cigs_day);
        price_per_pack      = view.findViewById(R.id.settings_price_pack);
        cigs_per_pack       = view.findViewById(R.id.settings_cigs_per_pack);
        update_btn          = view.findViewById(R.id.settings_update);
        logout              = view.findViewById(R.id.settings_logout);
        delete_account      = view.findViewById(R.id.settings_delete);
        currency            = view.findViewById(R.id.settings_btn_currency);
        theme_btn           = view.findViewById(R.id.settings_btn_theme);
    }

    private void setListeners() {
        user_profile_pic    .setOnClickListener(v -> Utils.get().getImage(this));

        update_btn          .setOnClickListener(v -> updateUserData() );

        logout              .setOnClickListener(v -> {
            Utils           .get()     .myStartActivity(getActivity(), LoginActivity.class);
            FirebaseAuth    .getInstance()     .signOut();
        });

        delete_account      .setOnClickListener(v -> {
            DBupdater       .get()      .deleteUserData(user.getUid());
            SharedPrefs     .get()      .deleteFirstLogin();
            SharedPrefs     .get()      .deleteSelectedTheme();
            Utils           .get()      .myStartActivity(getActivity(), LoginActivity.class);
            FirebaseAuth    .getInstance()      .signOut();
        });

        currency.setOnClickListener(v -> Dialogs.get()
                .createCurrencyDialog(getParentFragmentManager(), new CountryCurrencyPickerListener() {
                    @Override
                    public void onSelectCountry(Country country) {
                        currencySymbol = country.getCurrency().getCode() +" "+ country.getCurrency().getSymbol();
                        currency.setText(currencySymbol);
                    }

                    @Override
                    public void onSelectCurrency(Currency currency) { }
                }));

        theme_btn.setOnClickListener(v -> Dialogs.get().themeDialog().show());
    }

    //============================================

    private void setCurrentValues(){
        DBreader.get()          .readPicNoCache(KEYS.PROFILE, user_profile_pic, user.getUid());
        onProfileUpdate         .updateProfile(user);

        user_name       .getEditText()  .setText(""+ user.getName());
        years_smoked    .getEditText()  .setText(""+ user.getYearsSmoked());
        cigs_per_day    .getEditText()  .setText(""+ user.getCigsPerDay());
        price_per_pack  .getEditText()  .setText(""+ user.getPricePerPack());
        cigs_per_pack   .getEditText()  .setText(""+ user.getCigsPerPack());
        currency                        .setText(""+ user.getCurrencySymbol());
    }

    //============================================

    private String getInputLayoutText(TextInputLayout et){ return et.getEditText().getText().toString(); }

    private void updateUserData() {
        try {
            String name         = getInputLayoutText(user_name);
            int cigsPerDay      = Integer.parseInt(getInputLayoutText(cigs_per_day));
            int cigsPerPack     = Integer.parseInt(getInputLayoutText(cigs_per_pack));
            double yearsSmoked  = Double.parseDouble(getInputLayoutText(years_smoked));
            double pricePerPack = Double.parseDouble(getInputLayoutText(price_per_pack));

            user                        .setName(name)
                                        .setYearsSmoked(yearsSmoked)
                                        .setCigsPerDay(cigsPerDay)
                                        .setPricePerPack(pricePerPack)
                                        .setCigsPerPack(cigsPerPack)
                                        .setCurrencySymbol(currencySymbol == null ?
                                                 user.getCurrencySymbol() : currencySymbol);

            DBupdater.get()     .updateUser(user);
            onProfileUpdate             .updateProfile(user.setName(name));

            if(App.isNetworkAvailable()) App.toast("Data Updated!");
            else                         App.toast("Data Not Updated!");

        } catch (NumberFormatException e) { App.toast("Error in input!"); }
    }

}