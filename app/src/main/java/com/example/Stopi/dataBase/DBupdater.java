package com.example.Stopi.dataBase;

import android.net.Uri;
import com.example.Stopi.tools.App;
import com.example.Stopi.profile.OnProfileUpdate;
import com.example.Stopi.profile.User;
import com.example.Stopi.dataBase.KEYS.Status;
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
        DBupdater.getInstance().deleteProfilePic(Uid);
    }

    private void deleteProfilePic(String Uid){ Refs.getStorageRef(KEYS.FULL_PROFILE_PIC_URL + Uid +".jpg").delete(); }

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
     * updates user status in database
     * on login and logout
     */
    public void updateStatus(Status status){
        DBreader db = DBreader.getInstance();
        if(db.getUser() == null) return;
        db.getUser().setStatus(status);
        DBupdater.getInstance().saveLoggedUser();
    }

    /**
     * saves photo in database storage by user id
     */
    public void uploadImage(Uri filePathUri, OnProfileUpdate onProfileUpdate) {
        if (filePathUri == null) return;
        StorageReference ref = Refs.getStorageRef(KEYS.FULL_PROFILE_PIC_URL + App.getLoggedUser().getUid()+".jpg");
        App.toast("Uploading Photo! ");
        ref.putFile(filePathUri)
                .addOnSuccessListener(taskSnapshot -> {
                    App.toast("Image Uploaded");
                    onProfileUpdate.updateProfile(DBreader.getInstance().getUser());
                }).addOnFailureListener(e -> App.log("Upload failed"));
    }

    //=============================

}
