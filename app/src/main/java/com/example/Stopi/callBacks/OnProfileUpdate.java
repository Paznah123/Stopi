package com.example.Stopi.callBacks;

import com.example.Stopi.objects.User;

public interface OnProfileUpdate {

    /**
     * called when data is updated in settings
     */
    void updateProfile(User user);

}
