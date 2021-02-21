package com.example.Stopi.store;

import com.example.Stopi.profile.User;

public interface OnGiftSent {

    /**
     * @param user the user who receives the gift
     * called when send gift button is clicked (in feed fragment)
     */
    void chooseGift(User user);
}
