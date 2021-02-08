package com.example.Stopi;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.example.Stopi.objects.dataManage.DBupdater;
import com.example.Stopi.objects.dataManage.DBreader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import static com.example.Stopi.objects.dataManage.KEYS.LOG_TAG;

public class App extends Application {

    private static Context context;

    private static Toast myToast;

    //====================================================

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        context = getApplicationContext();
        myToast = Toast.makeText(App.getAppContext(), "", Toast.LENGTH_SHORT);
        Utils.initUtils();
        DBreader.initDBreader();
        DBupdater.initDBwriter();
    }

    //====================================================

    public static Context getAppContext(){ return context; }

    public static void log(String msg){ Log.d(LOG_TAG, msg); }

    public static FirebaseUser getLoggedUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }

    public static void toast(String msg){
        myToast.setText(msg);
        myToast.show();
    }

}
