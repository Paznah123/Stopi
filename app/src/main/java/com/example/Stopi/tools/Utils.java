package com.example.Stopi.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.Stopi.R;
import com.example.Stopi.dataBase.DBreader;
import com.example.Stopi.dataBase.DBupdater;
import com.example.Stopi.profile.login.SharedPrefs;
import com.furkanakdemir.surroundcardview.SurroundCardView;
import com.github.drjacky.imagepicker.ImagePicker;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    private static  Utils           instance;

    private         MediaPlayer     mp;

    private         int             sTheme = 1;

    //=============================

    /**
     * gets the singleton
     */
    public static Utils get() { return instance; }

    /**
     * must call addContext for using this object methods!
     */
    public static void initUtils(){
        if(instance == null)
            instance = new Utils();
    }

    //====================================================

    /**
     * generates uniqueChatId for user chat
     * @param userId second userId for the chat (not logged firebase user)
     */
    public String chatIdHash(String userId){
        String chatKey = userId + DBreader.get().getUser().getUid();
        int chatId = 0;
        for (int i = 0; i < chatKey.length(); i++)
            chatId += chatKey.charAt(i)*31 % 2349;

        return String.valueOf(chatId);
    }

    //====================================================

    /**
     * formats a double by pattern
     * @param pattern #.## (number of # as the number of digits to show after . )
     */
    public String formatNumber(double number, String pattern){ return new DecimalFormat(pattern).format(number); }

    public String formatToDate(long time){
        SimpleDateFormat formatter= new SimpleDateFormat("MM-dd 'at' HH:mm");
        Date date = new Date(time);
        return formatter.format(date);
    }

    //====================================================

    public void onCardClick(SurroundCardView svc){
        if(svc.isCardSurrounded())
            svc.release();
        else
            svc.surround();
    }

    //====================================================

    public int getDotByStatus(KEYS.Status status){
        if (status.equals(KEYS.Status.Online)) {
            return R.drawable.ic_online_dot;
        } else
            return R.drawable.ic_offline_dot;
    }

    //====================================================

    /**
     * plays sound
     * @param rawSound sound id
     */
    public void playSound(Context context, int rawSound) {
        if(mp != null) mp.reset();
        mp = MediaPlayer.create(context,rawSound);
        mp.start();
    }

    //====================================================

    /**
     *   starts activity
     */
    public void myStartActivity(Activity activity, Class activityClass){
        Intent intent   = new Intent(activity, activityClass);
        activity        .startActivity(intent);
        activity        .finish();
    }

    //====================================================

    /**
     *   gets image from imagePicker in fragment
     */
    public void getImage(Fragment fragment) {
        ImagePicker.Companion
                    .with(fragment)
                    .crop()
                    .cropOval()
                    .cropSquare()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start();
    }

    /**
     *   gets image from imagePicker in activity
     */
    public void getImage(Activity activity) {
        ImagePicker.Companion
                    .with(activity)
                    .crop()
                    .cropOval()
                    .cropSquare()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start();
    }

    //====================================================

    public void changeToTheme(Activity activity, int theme) {
        sTheme = theme;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
        activity.overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
        SharedPrefs.get().saveTheme(theme);
       /* DBreader.get().getUser().setTheme(theme);
        DBupdater.get().saveLoggedUser();*/
    }

    public void onActivityCreateSetTheme(Activity activity) {
        switch (SharedPrefs.get().getTheme()) {
            default:
            case KEYS.THEME_WARM:
                activity.setTheme(R.style.Theme_Stopi_App);
                break;
            case KEYS.THEME_COLD:
                activity.setTheme(R.style.Theme_Stopi_App_Second);
                break;
            case KEYS.THEME_COOL:
                activity.setTheme(R.style.Theme_Stopi_App_Third);
                break;
        }
        ((AppCompatActivity)activity).getSupportActionBar().hide();
    }
}
