package com.example.Stopi.activities;

import android.os.Bundle;
import com.example.Stopi.Utils;
import com.example.Stopi.callBacks.*;
import com.example.Stopi.objects.User;
import com.example.Stopi.dataBase.KEYS;
import com.example.Stopi.dataBase.DBreader;
import com.example.Stopi.dataBase.DBupdater;
import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.widget.ImageView;
import android.widget.TextView;
import com.example.Stopi.R;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements
                                                OnProfileUpdate, OnCoinsChanged,
                                                            OnFragmentTransaction {

    public enum Status { Online, Offline}

    private DrawerLayout    drawerLayout;
    private NavigationView  nav_view;
    private NavController   navController;

    private ImageView       main_drawer_btn;
    private ImageView       drawer_user_pic;

    private TextView        main_lbl_title;

    private TextView        drawer_lbl_userName;
    private TextView        user_coins;

    private User            user;
    private DBreader        dbReader;

    //===========================================

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DBupdater.getInstance().updateStatus(Status.Offline);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Utils.getInstance().addContext(this);
        dbReader = dbReader.getInstance();
        user = dbReader.getUser();

        DBupdater.getInstance().updateStatus(Status.Online);

        if(user == null) {
            Utils.getInstance().myStartActivity(this, FirstTimeActivity.class);
            return;
        }

        findViews();
        initDrawer();
        initViews();

        checkFirstLogin();
    }

    //===========================================

    private void findViews() {
        drawerLayout        = findViewById(R.id.drawerLayout);
        nav_view            = findViewById(R.id.nav_view);
        main_drawer_btn     = findViewById(R.id.main_drawer_btn);
        main_lbl_title      = findViewById(R.id.main_lbl_title);
        user_coins          = findViewById(R.id.user_coins);
        drawer_lbl_userName = nav_view.getHeaderView(0).findViewById(R.id.drawer_lbl_userName);
        drawer_user_pic     = nav_view.getHeaderView(0).findViewById(R.id.drawer_user_pic);
    }

    //===========================================

    private void initViews() {
        drawer_lbl_userName     .setText(user.getName());
        user_coins              .setText("Coins - "+ user.getCoins());
        dbReader.getInstance()  .readPic(KEYS.PROFILE, drawer_user_pic, user.getUid());
    }

    private void initDrawer() {
        nav_view.setItemIconTintList(null);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHost_fragment);
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(nav_view, navController);

        main_drawer_btn.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> main_lbl_title.setText(destination.getLabel()));
    }

    //===========================================

    private void checkFirstLogin() {
        long daysPassed = TimeUnit.MILLISECONDS.toDays(
                Calendar.getInstance().getTimeInMillis() - user.getLoggedToday());
        if(daysPassed < 1 && user.getLoggedToday() != -1) return;

        rewardDailyLogin();
    }

    private void rewardDailyLogin(){
        user.setLoggedToday(Calendar.getInstance().getTimeInMillis());
        user.incrementCoins(1500);
        changeCoins();
        Utils.getInstance().createRewardDialog().show();
        DBupdater.getInstance().updateUser(user);
    }

    //=========================================== call backs

    @Override
    public void changeCoins() { // called when item is bought in store or when daily login performed
        user_coins.setText("Coins - " + dbReader.getUser().getCoins());
    }

    @Override
    public void updateProfile(User user) {
        dbReader.getInstance().readPic(KEYS.PROFILE, drawer_user_pic,user.getUid());
        drawer_lbl_userName.setText(user.getName());
    }

    @Override
    public void setFragmentToView(Fragment fragment, int layout_id){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(layout_id, fragment).commit();
    }

}