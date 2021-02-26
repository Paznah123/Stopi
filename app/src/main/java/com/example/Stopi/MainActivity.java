package com.example.Stopi;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.example.Stopi.store.OnCoinsChanged;
import com.example.Stopi.tools.App;
import com.example.Stopi.tools.OnFragmentTransaction;
import com.example.Stopi.profile.OnProfileUpdate;
import com.example.Stopi.dataBase.DBreader;
import com.example.Stopi.dataBase.DBupdater;
import com.example.Stopi.tools.KEYS;
import com.example.Stopi.tools.KEYS.Status;
import com.example.Stopi.profile.CreateProfileActivity;
import com.example.Stopi.profile.User;
import com.example.Stopi.tools.Dialogs;
import com.example.Stopi.profile.login.SharedPrefs;
import com.example.Stopi.tools.Utils;
import com.google.android.material.navigation.NavigationView;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements
                                                OnProfileUpdate, OnCoinsChanged,
                                                            OnFragmentTransaction {
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
    protected void onStop() {
        super.onStop();
        DBupdater.get().updateStatus(Status.Offline);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Dialogs.get().addContext(this);
        DBupdater.get().updateStatus(Status.Online);
    }

    //===========================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.get().onActivityCreateSetTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbReader = DBreader.get();
        user = dbReader.getUser();
        DBupdater.get().updateStatus(Status.Online);
        Dialogs.get().addContext(this);

        if(SharedPrefs.get().isFirstLogin()) {
            Utils.get().myStartActivity(this, CreateProfileActivity.class);
            return;
        }

        if(!initServerConnection()) return;

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
        drawer_lbl_userName .setText(user.getName());
        user_coins          .setText("Coins - "+ user.getCoins());
        dbReader.get()      .readPicNoCache(KEYS.PROFILE, drawer_user_pic, user.getUid());
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
        updateWallet();
        Dialogs.get().rewardDialog().show();
        DBupdater.get().updateUser(user);
    }

    //===========================================

    private boolean initServerConnection() {
        DBreader dbReader = DBreader.get();
        if(!App.isNetworkAvailable()){
            Dialogs.get().noInternetDialog().show();
            return false;
        } else if (dbReader.getUser() == null) { // if data hasn't arrived from db yet
            Utils.get().myStartActivity(this, ActivitySplash.class);
            return false;
        }
        return true;
    }

    //=========================================== call backs

    @Override
    public void updateWallet() { // called when item is bought in store or when daily login performed
        user_coins.setText("Coins - " + dbReader.getUser().getCoins());
    }

    @Override
    public void updateProfile(User user) {
        dbReader.get().readPicNoCache(KEYS.PROFILE, drawer_user_pic,user.getUid());
        drawer_lbl_userName.setText(user.getName());
    }

    @Override
    public void setFragmentToView(Fragment fragment, int layout_id){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(layout_id, fragment).commit();
    }

}