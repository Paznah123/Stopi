package com.example.Stopi.tools;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;
import com.example.Stopi.dataBase.DBupdater;
import com.example.Stopi.dataBase.DBreader;
import com.example.Stopi.store.Store;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class App extends Application {

    private static Context context;

    private static Toast myToast;


    //====================================================

    @Override
    public void onCreate() {
        super.onCreate();

        context         = getApplicationContext();
        myToast         = Toast.makeText(context, "", Toast.LENGTH_SHORT);

        SharedPrefs     .initPrefs();
        DBupdater       .initUpdater();
        DBreader        .initReader();
        Utils           .initUtils();
        Store           .initStore();
        Dialogs         .initDialogs();
    }

    //====================================================

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static Toast toast(String msg){
        myToast     .setText(msg);
        myToast     .show();
        return      myToast;
    }

    public static void log(String msg)          { Log.d(KEYS.LOG_TAG, msg); }

    public static Context getAppContext()       { return context; }

    public static FirebaseUser getLoggedUser()  { return FirebaseAuth.getInstance().getCurrentUser(); }

}
