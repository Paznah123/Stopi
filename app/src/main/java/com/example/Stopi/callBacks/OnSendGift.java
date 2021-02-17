package com.example.Stopi.callBacks;

import com.example.Stopi.objects.User;

public interface OnSendGift {

    /**
     * @param user the user who receives the gift
     * called when send gift button is clicked (in feed fragment)
     */
    void chooseGift(User user);
}
