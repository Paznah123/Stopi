package com.example.Stopi.objects.dataManage;

import android.net.Uri;
import android.view.LayoutInflater;
import com.example.Stopi.App;
import com.example.Stopi.Utils;
import com.example.Stopi.callBacks.OnProfileUpdate;
import com.example.Stopi.objects.Email;
import com.example.Stopi.objects.StoreItem;
import com.example.Stopi.objects.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import static com.example.Stopi.objects.dataManage.KEYS.EMAILS_REF;
import static com.example.Stopi.objects.dataManage.KEYS.PROFILE_PICS_REF;

public class DBupdater {

    private static DBupdater instance;

    //=============================

    public static void initDBwriter(){
        if(instance == null)
            instance = new DBupdater();
    }

    public static DBupdater getInstance() { return instance; }

    //=============================

    /**
     * get database reference by key
     */
    public static DatabaseReference getDBref(String ref){ return FirebaseDatabase.getInstance().getReference(ref); }

    public static DatabaseReference getUsersRef(){ return getDBref(KEYS.USERS_REF); }

    public static DatabaseReference getEmailsRef(){ return getDBref(EMAILS_REF); }

    public static DatabaseReference getLoggedUserRef(){ return getUsersRef().child(App.getLoggedUser().getUid()); }

    public static StorageReference getStorageRef(){ return FirebaseStorage.getInstance().getReference(); }

    //=============================

    /**
     * updates user in database
     * not for updating logged user
     */
    public void updateUser(User user){ DBupdater.getUsersRef().child(user.getUid()).setValue(user); }

    /**
     * saves current logged user in database
     */
    public void saveLoggedUser(){
        User loggedUser = DBreader.getInstance().getUser();
        DBupdater.getUsersRef().child(loggedUser.getUid()).setValue(loggedUser);
    }

    /**
     * updates user goal in database
     * and saves user object
     */
    public void updateUserGoal(String goal){
        DBreader dbReader = DBreader.getInstance();
        dbReader.getUser().setGoal(goal);
        saveLoggedUser();
    }

    /**
     * saves photo in database storage by user id
     */
    public void uploadImage(Uri filePathUri, OnProfileUpdate onProfileUpdate) {
        App.log("Enter upload");
        if (filePathUri != null) {
            App.log("Start upload");
            StorageReference ref = DBupdater.getStorageRef().child(PROFILE_PICS_REF)
                    .child(App.getLoggedUser().getUid()+".jpg");
            App.toast("Uploading Photo! ");
            ref.putFile(filePathUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        App.toast("Image Uploaded");
                        onProfileUpdate.onProfileUpdate(DBreader.getInstance().getUser());
                    })
                    .addOnFailureListener(e -> App.log("Upload failed"));
        }
    }

    //=============================

    /**
     * added new email by user id
     * to email reference in database
     */
    public void sendEmail(String userId, Email email){
        DatabaseReference userEmailsRef = DBupdater.getEmailsRef().child(userId);
        String key = userEmailsRef.push().getKey();
        email.setKey(key);
        userEmailsRef.child(key).setValue(email);
    }

    /**
     * deletes from logged user email reference by key
     */
    public void deleteEmail(String key){
        DBupdater.getEmailsRef()
                .child(App.getLoggedUser().getUid()).child(key).removeValue();
    }

    //=============================

    /**
     * - creates dialog for attaching message to gift
     * - updates logged user gift bag
     * - updates receiver user gift bag
     * - updates database references
     */
    public void sendGift(LayoutInflater inflater, User user, String itemName){
        StoreItem storeItem = DBreader.getInstance().getUser().getBoughtItems().get(itemName);

        Utils.createEmailDialog(inflater,user.getUid(),storeItem).show();

        user.addStoreItem(storeItem);
        updateUser(user);

        if(storeItem.getPrice() > 1)
            storeItem.reduceAmount();
        else
            DBreader.getInstance().getUser().getBoughtItems().remove(itemName);
        saveLoggedUser();
    }

    //=============================

}
