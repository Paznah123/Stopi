package com.example.Stopi.objects.dataManage;

import android.widget.ImageView;
import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.Stopi.App;
import com.example.Stopi.callBacks.OnEmailReceived;
import com.example.Stopi.objects.StoreItem;
import com.example.Stopi.objects.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class DBreader {

    private static  DBreader    instance;
    private static  User        user;
    private static  List        tips = new ArrayList<>();
    private static  List        rewards_info = new ArrayList<>();
    private static  List        store_items = new ArrayList<>();

    //=============================

    public static void initDBreader(){
        if(instance == null) {
            instance = new DBreader();
            readData();
        }
    }

    public static DBreader getInstance() { return instance; }

    //============================= initial reads from server

    private static void readData() {
        if (App.getLoggedUser() != null)
            getInstance().readUserData();
        getInstance().readListData(KEYS.TIPS_REF, tips, String.class);
        getInstance().readListData(KEYS.REWARDS_INFO_REF, rewards_info, String.class);
        getInstance().readListData(KEYS.STORE_REF, store_items, StoreItem.class);
    }

    //============================= initial reads from server

    private void readListData(String Ref, List list, Class ObjectClass){
        Refs.getDBref(Ref).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren())
                    list.add(snapshot.getValue(ObjectClass));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    public void readUserData() {
        Refs.getUsersRef().child(App.getLoggedUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        user = dataSnapshot.getValue(User.class);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) { }
                });
    }

    public void readEmailsAmount(OnEmailReceived onEmailReceived){
        Refs.getEmailsRef()
                .child(App.getLoggedUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int emailsNum = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren())
                            emailsNum++;
                        onEmailReceived.updateEmailCounter(emailsNum);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
    }

    //=========================================

    /**
     * @param key STORE-1 AND PROFILE-2 KEYS
     * @param imageView imageView to load into
     * @param fileName name of the file to load
     */
    public void readPic(int key, ImageView imageView, String fileName){
        String ref = "";
        switch (key){
            case KEYS.STORE:
                ref = Refs.getStorePicStorageRef(fileName);
                break;
            case KEYS.PROFILE:
                ref = Refs.getProfilePicStorageRef(fileName);
                break;
        }
        Glide.with(App.getAppContext())
                .load(Refs.getStorageRef(ref))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .centerInside()
                .into(imageView);
    }

    //=========================================
    
    public User getUser(){ return user; }

    public List getTips(){ return tips; }

    public List getRewardsInfo(){ return rewards_info; }

    public List getStoreItems(){ return store_items; }


}
