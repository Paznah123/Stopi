package com.example.Stopi.objects.dataManage;

import android.net.Uri;
import com.example.Stopi.App;
import com.example.Stopi.Utils;
import com.example.Stopi.callBacks.OnProfileUpdate;
import com.example.Stopi.objects.Email;
import com.example.Stopi.objects.StoreItem;
import com.example.Stopi.objects.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

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
     * deletes all data , emails and profile pic
     * @param Uid user firebase id
     */
    public void deleteUserData(String Uid){
        Refs.getUsersRef().child(Uid).removeValue();
        Refs.getEmailsRef().child(Uid).removeValue();
        DBupdater.getInstance().deleteProfilePic(Uid);
    }

    /**
     * updates user in database
     * @param user user for update (not for logged user)
     */
    public void updateUser(User user){ Refs.getUsersRef().child(user.getUid()).setValue(user); }

    /**
     * saves current logged user in database
     */
    public void saveLoggedUser(){
        User loggedUser = DBreader.getInstance().getUser();
        Refs.getUsersRef().child(loggedUser.getUid()).setValue(loggedUser);
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
            StorageReference ref = Refs.getStorageRef(KEYS.FULL_PROFILE_PIC_URL + App.getLoggedUser().getUid()+".jpg");
            App.toast("Uploading Photo! ");
            ref.putFile(filePathUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        App.toast("Image Uploaded");
                        onProfileUpdate.onProfileUpdate(DBreader.getInstance().getUser());
                    })
                    .addOnFailureListener(e -> App.log("Upload failed"));
        }
    }

    public void deleteProfilePic(String Uid){ Refs.getStorageRef(KEYS.FULL_PROFILE_PIC_URL + Uid +".jpg").delete(); }

    //=============================

    /**
     * added new email by user id
     * to email reference in database
     * @param userId database id of email receiving user
     */
    public void sendEmail(String userId, Email email){
        DatabaseReference userEmailsRef = Refs.getEmailsRef().child(userId);
        email.setSenderKey(Refs.getLoggedUserRef().getKey());
        String emailKey = userEmailsRef.push().getKey();
        userEmailsRef.child(emailKey).setValue(email);
    }

    /**
     * deletes from logged user email reference by key
     * @param key database email id (saved inside email object)
     */
    public void deleteEmail(String key){
        Refs.getEmailsRef().child(App.getLoggedUser().getUid())
                        .child(key).removeValue();
    }

    //=============================

    /**
     * creates dialog for attaching message to gift
     * updates logged user gift bag
     * updates receiver user gift bag
     * @param user gift destination user
     */
    public void sendGift(User user, StoreItem storeItem){
        Utils.getInstance().createEmailDialog(user.getUid(),storeItem).show();

        user.addStoreItem(storeItem);
        updateUser(user);

        if(storeItem.getPrice() > 1)
            storeItem.reduceAmount();
        else
            DBreader.getInstance().getUser().getBoughtItems().remove(storeItem.getTitle());
        saveLoggedUser();
    }

    //=============================

}
