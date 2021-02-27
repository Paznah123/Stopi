package com.example.Stopi.dataBase;

import android.widget.ImageView;
import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.example.Stopi.R;
import com.example.Stopi.tools.App;
import com.example.Stopi.profile.User;
import com.example.Stopi.tools.KEYS;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.List;

public class DBreader {

    private static  DBreader    instance;

    private         User        user;
    private         List        tips = new ArrayList<>();
    private         List        rewards_info = new ArrayList<>();

    //=============================

    public static void initReader(){
        if(instance == null) {
            instance = new DBreader();
            instance.readData();
        }
    }

    /**
     * gets the singleton
     */
    public static DBreader get() { return instance; }

    //============================= initial reads from server

    private void readData() {
        if (App.getLoggedUser() != null)
            get().readUserData();
        get().readListData(KEYS.TIPS_REF, tips, String.class);
        get().readListData(KEYS.REWARDS_INFO_REF, rewards_info, String.class);
    }

    public void readListData(String Ref, List list, Class ObjectClass){
        Refs.getDBref(Ref).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren())
                    list.add(snapshot.getValue(ObjectClass));
                App.log("readListData() - read list");

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
                        App.log("readUserData() - read user");
                        user = dataSnapshot.getValue(User.class);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) { }
                });
    }

    //=========================================

    private StorageReference photoPathRef(int key, String fileName) {
        String ref = "";
        switch (key){
            case KEYS.STORE:
                ref = Refs.getStorePicStoragePath(fileName);
                break;
            case KEYS.PROFILE:
                ref = Refs.getProfilePicStoragePath(fileName);
                break;
        }
        return Refs.getStorageRef(ref);
    }
    /**
     * @param key STORE = 1 , PROFILE = 2 found in KEYS
     * @param imageView imageView to load into
     * @param fileName name of the file to load
     */
    public void readPic(int key, ImageView imageView, String fileName){
        StorageReference ref = photoPathRef(key,fileName);
        Glide.with(App.getAppContext())
                .load(ref)
                .placeholder(R.drawable.img_default_pic)
                .centerInside()
                .dontAnimate()
                .into(imageView);
    }

    /**
     * read photo from server even if in cache
     * @param key STORE = 1 , PROFILE = 2 found in KEYS
     * @param imageView imageView to load into
     * @param fileName name of the file to load
     */
    public void readPicNoCache(int key, ImageView imageView, String fileName){
        StorageReference ref = photoPathRef(key,fileName);
        Glide.with(App.getAppContext())
                .load(ref)
                .placeholder(R.drawable.img_default_pic)
                .centerInside()
                .dontAnimate()
                .signature(new ObjectKey(System.currentTimeMillis()))
                .into(imageView);
    }

    //=========================================

    public User getUser(){ return user; }

    public List getTips(){ return tips; }

    public List getRewardsInfo(){ return rewards_info; }

}
