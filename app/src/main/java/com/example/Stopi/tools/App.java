package com.example.Stopi.tools;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.example.Stopi.dataBase.DBupdater;
import com.example.Stopi.dataBase.DBreader;
import com.example.Stopi.store.Store;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class App extends Application {

    private static Context context;

    private static Toast myToast;

    //====================================================

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        context         = getApplicationContext();
        myToast         = Toast.makeText(App.getAppContext(), "", Toast.LENGTH_SHORT);
        DBupdater       .initDBwriter();
        DBreader        .initDBreader();
        Dialogs         .initDialogs();
        Utils           .initUtils();
        Store           .initStore();
    }

    //====================================================

    public static Toast toast(String msg){
        myToast     .setText(msg);
        myToast     .show();
        return      myToast;
    }

    public static Context getAppContext()       { return context; }

    public static void log(String msg)          { Log.d(KEYS.LOG_TAG, msg); }

    public static FirebaseUser getLoggedUser()  { return FirebaseAuth.getInstance().getCurrentUser(); }

}
