package com.example.Stopi.fragments;

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
import com.example.Stopi.App;
import com.example.Stopi.R;
import com.example.Stopi.Utils;
import com.example.Stopi.activities.Login;
import com.example.Stopi.callBacks.OnProfileUpdate;
import com.example.Stopi.objects.dataManage.DBreader;
import com.example.Stopi.objects.User;
import com.example.Stopi.objects.dataManage.DBupdater;
import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {

    private View view;

    private ImageView user_profile_pic;
    private Uri filePathUri;

    private TextInputLayout user_name;
    private TextInputLayout years_smoked;
    private TextInputLayout cigs_per_day;
    private TextInputLayout price_per_pack;
    private TextInputLayout cigs_per_pack;

    private MaterialButton update_btn;
    private MaterialButton logout;
    private MaterialButton delete_account;

    private OnProfileUpdate onProfileUpdate;

    private DBreader dbReader;

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
            App.log("on activity result");
            filePathUri = data.getData();
            DBupdater.getInstance().uploadImage(filePathUri, user -> {
                App.toast("Image Uploaded");
                onProfileUpdate.onProfileUpdate(user);
                setCurrentValues();
            });
        } else if (resultCode == ImagePicker.RESULT_ERROR)
            App.toast(new ImagePicker().Companion.getError(data));
        else
            App.toast("Task Cancelled");
    }

    //============================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        dbReader = DBreader.getInstance();

        findViews();
        setCurrentValues();
        setListeners();

        return view;
    }

    //============================================

    private void findViews() {
        user_profile_pic = view.findViewById(R.id.user_profile_pic);
        user_name = view.findViewById(R.id.settings_user_name);
        years_smoked = view.findViewById(R.id.settings_years_smoked);
        cigs_per_day = view.findViewById(R.id.settings_cigs_day);
        price_per_pack = view.findViewById(R.id.settings_price_pack);
        cigs_per_pack = view.findViewById(R.id.settings_cigs_per_pack);
        update_btn = view.findViewById(R.id.settings_update);
        logout = view.findViewById(R.id.settings_logout);
        delete_account = view.findViewById(R.id.settings_delete);
    }

    private void setListeners() {
        user_profile_pic.setOnClickListener(v -> Utils.getImage(this));

        update_btn.setOnClickListener(v -> updateUserData() );

        logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Utils.myStartActivity(getActivity(), Login.class);
        });

        delete_account.setOnClickListener(v -> {
            DBupdater.getUsersRef().child(App.getLoggedUser().getUid()).removeValue();
            FirebaseAuth.getInstance().signOut();
            Utils.myStartActivity(getActivity(), Login.class);
        });
    }

    //============================================

    private void setCurrentValues(){
        User user = dbReader.getUser();
        DBreader.getInstance().readProfilePic(user_profile_pic, user.getProfilePicFilePath());

        user_name.getEditText().setText(""+ user.getName());
        years_smoked.getEditText().setText(""+ user.getYearsSmoked());
        cigs_per_day.getEditText().setText(""+ user.getCigsPerDay());
        price_per_pack.getEditText().setText(""+ user.getPricePerPack());
        cigs_per_pack.getEditText().setText(""+ user.getCigsPerPack());
    }

    //============================================

    private void updateUserData() {
        User user = dbReader.getUser();
        try {
            String name = user_name.getEditText().getText().toString();
            double yearsSmoked = Double.parseDouble(years_smoked.getEditText().getText().toString());
            int cigsPerDay = Integer.parseInt(cigs_per_day.getEditText().getText().toString());
            double pricePerPack = Double.parseDouble(price_per_pack.getEditText().getText().toString());
            int cigsPerPack = Integer.parseInt(cigs_per_pack.getEditText().getText().toString());
            long dateStoppedSmoking = user.getDateStoppedSmoking();

            DBupdater.getInstance().updateUser(user.setUid(App.getLoggedUser().getUid())
                                    .setName(name)
                                    .setYearsSmoked(yearsSmoked)
                                    .setCigsPerDay(cigsPerDay)
                                    .setPricePerPack(pricePerPack)
                                    .setCigsPerPack(cigsPerPack)
                                    .setDateStoppedSmoking(dateStoppedSmoking));

            onProfileUpdate.onProfileUpdate(user);
            App.toast("Data Updated!");
        } catch(NumberFormatException e) {
            App.toast("Error In Input!");
        }
    }

}