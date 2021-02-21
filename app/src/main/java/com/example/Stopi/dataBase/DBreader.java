package com.example.Stopi.dataBase;

import android.widget.ImageView;
import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.example.Stopi.R;
import com.example.Stopi.tools.App;
import com.example.Stopi.profile.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class DBreader {

    private static  DBreader    instance;

    private         User        user;
    private         List        tips = new ArrayList<>();
    private         List        rewards_info = new ArrayList<>();

    //=============================

    public static void initDBreader(){
        if(instance == null) {
            instance = new DBreader();
            instance.readData();
        }
    }

    public static DBreader getInstance() { return instance; }

    //============================= initial reads from server

    private void readData() {
        if (App.getLoggedUser() != null)
            getInstance().readUserData();
        getInstance().readListData(KEYS.TIPS_REF, tips, String.class);
        getInstance().readListData(KEYS.REWARDS_INFO_REF, rewards_info, String.class);
    }

    public void readListData(String Ref, List list, Class ObjectClass){
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

    //=========================================

    /**
     * @param key STORE = 1 , PROFILE = 2 found in KEYS
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
                .error(R.drawable.img_default_pic)
                .centerInside()
                .into(imageView);
    }

    //=========================================

    public User getUser(){ return user; }

    public List getTips(){ return tips; }

    public List getRewardsInfo(){ return rewards_info; }

}
