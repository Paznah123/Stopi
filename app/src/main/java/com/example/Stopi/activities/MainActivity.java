package com.example.Stopi.activities;

import android.app.Activity;
import android.os.Bundle;
import com.example.Stopi.App;
import com.example.Stopi.callBacks.OnCoinsChanged;
import com.example.Stopi.callBacks.OnEmailReceived;
import com.example.Stopi.callBacks.OnFragmentTransaction;
import com.example.Stopi.callBacks.OnProfileUpdate;
import com.example.Stopi.callBacks.OnSendGift;
import com.example.Stopi.Utils;
import com.example.Stopi.objects.StoreItem;
import com.example.Stopi.objects.dataManage.DBreader;
import com.example.Stopi.objects.User;
import com.example.Stopi.objects.dataManage.DBupdater;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.Stopi.R;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity
        implements OnProfileUpdate, OnCoinsChanged, OnSendGift, OnEmailReceived, OnFragmentTransaction {

    private DrawerLayout drawerLayout;
    private NavigationView nav_view;
    private NavController navController;

    private ImageView main_drawer_btn;
    private ImageView drawer_user_pic;

    private TextView main_lbl_title;
    private TextView drawer_lbl_userName;
    private TextView user_coins;

    private User user;
    private DBreader DBreader;

    private TextView email_counter_tv;

    private FloatingActionButton distraction_btn;

    //===========================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        DBreader = DBreader.getInstance();
        DBreader.readUserData();
        DBreader.readEmailsAmount(this);
        user = DBreader.getUser();

        if(user == null) { // need to fix first login activity loop
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
        drawerLayout = findViewById(R.id.drawerLayout);
        nav_view = findViewById(R.id.nav_view);
        main_drawer_btn = findViewById(R.id.main_drawer_btn);
        main_lbl_title = findViewById(R.id.main_lbl_title);
        user_coins = findViewById(R.id.user_coins);
        drawer_lbl_userName = nav_view.getHeaderView(0).findViewById(R.id.drawer_lbl_userName);
        drawer_user_pic = nav_view.getHeaderView(0).findViewById(R.id.drawer_user_pic);
        distraction_btn = findViewById(R.id.floationg_btn_distract);
    }

    //===========================================

    private void initViews() {
        drawer_lbl_userName.setText(user.getName());
        user_coins.setText("Coins - "+ user.getCoins());
        DBreader.getInstance().readProfilePic(drawer_user_pic, user.getProfilePicFilePath());

        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        email_counter_tv = (TextView)inflater.inflate(R.layout.emails_counter,null);
        nav_view.getMenu().findItem(R.id.inbox_item).setActionView(email_counter_tv);

        Activity activity = this;
        distraction_btn.setOnClickListener(v ->
                Utils.getInstance().myStartActivity(activity,DistractionActivity.class)
        );
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
        onCoinsChanged();
        App.toast("+1500 Coins!");
        DBupdater.getInstance().updateUser(user);
    }

    //=========================================== call backs

    /**
     * called when item is bought in store
     */
    @Override
    public void onCoinsChanged() { // called when item is bought in store or when daily login performed
        user_coins.setText("Coins - " + DBreader.getUser().getCoins());
    }

    /**
     * called when data is updated in settings
     */
    @Override
    public void onProfileUpdate(User user) {
        DBreader.getInstance().readProfilePic(drawer_user_pic,user.getProfilePicFilePath());
        drawer_lbl_userName.setText(user.getName());
    }

    /**
     * @param user the user who receives the gift
     * called when send gift button is clicked (in feed fragment)
     */
    @Override
    public void onSendGift(LayoutInflater inflater, User user) {
        HashMap<String, StoreItem> boughtItems = DBreader.getInstance().getUser().getBoughtItems();
        if(boughtItems.size() > 0) {
                Utils.getInstance().createGiftDialog(inflater, boughtItems, (parent, view, position, id) -> {
                String itemId = new ArrayList<>(boughtItems.keySet()).get(position);
                StoreItem storeItem = boughtItems.get(itemId);
                DBupdater.getInstance().sendGift(inflater, user, storeItem);
            }).show();
        } else
            App.toast("You have no items to send!");
    }

    /**
     * sets fragment in view by layout id
     */
    @Override
    public void setFragmentToView(Fragment fragment, int layout_id){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(layout_id, fragment).setCustomAnimations(R.anim.slide_in, R.anim.slide_out).commit();
    }

    /**
     * updates inbox fragment badge
     */
    @Override
    public void updateEmailCounter(int pending_emails) {
        if(pending_emails > 0)
            email_counter_tv.setText(""+ pending_emails);
        else
            email_counter_tv.setText("");
    }
}