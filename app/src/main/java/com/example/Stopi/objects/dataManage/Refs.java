package com.example.Stopi.objects.dataManage;

import com.example.Stopi.App;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Refs {

    /**
     * get database reference by key
     * @param url database ref (references found in dataManage.KEYS)
     */
    public static DatabaseReference getDBref(String url)    { return FirebaseDatabase.getInstance().getReferenceFromUrl(url); }
    public static DatabaseReference getUsersRef()           { return getDBref(KEYS.USERS_REF); }
    public static DatabaseReference getEmailsRef()          { return getDBref(KEYS.EMAILS_REF); }
    public static DatabaseReference getLoggedUserRef()      { return getUsersRef().child(App.getLoggedUser().getUid()); }

    //=============================

    public static String getProfilePicStorageRef(String fileName)   { return KEYS.FULL_PROFILE_PIC_URL + fileName + ".jpg"; }
    public static String getStorePicStorageRef(String fileName)     { return KEYS.STORE_PICS_REF + fileName.toLowerCase() + ".jpg"; }

    public static StorageReference getStorageRef(String fullFilePath){
        return FirebaseStorage.getInstance().getReference().getStorage().getReferenceFromUrl(fullFilePath);
    }

    //=============================

}
