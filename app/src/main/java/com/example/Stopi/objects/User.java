package com.example.Stopi.objects;

import com.example.Stopi.objects.dataManage.KEYS;

import java.util.Calendar;
import java.util.HashMap;

public class User {

    private String uid = "";
    private String name = "";
    private String goal = "";

    private double yearsSmoked = 0;
    private double pricePerPack = 0;
    private int cigsPerDay = 0;
    private int cigsPerPack = 1;

    private int coins = 0;

    private long dateStoppedSmoking;
    private long loggedToday = -1;

    private String profilePicFilePath = "";
    private HashMap<String,StoreItem> boughtItems = new HashMap<>();

    //=========================================

    public User(){ }

    /**
     *  added storeItem to user gift bag ,
     *  if storeItem exists only increments amount
     */
    public void addStoreItem(StoreItem storeItem) {
        StoreItem boughtItem = new StoreItem()
                .setTitle(storeItem.getTitle())
                .setPhotoUrl(storeItem.getPhotoUrl())
                .setPrice(0);
        if(!boughtItems.containsKey(boughtItem.getTitle())) {
            boughtItem.setPrice(1);
            boughtItems.put(boughtItem.getTitle(), boughtItem);
        } else {
            boughtItems.get(storeItem.getTitle()).incrementAmount();
        }
    }

    //=========================================

    public double totalCigsSmoked(){ return yearsSmoked * cigsPerDay * KEYS.DAYS_IN_YEAR; }

    public double moneyWasted(){ return totalCigsSmoked()/cigsPerPack * pricePerPack; }

    public double lifeLost(){ return KEYS.MINUTES_LOST_PER_CIG * totalCigsSmoked() / 60 / 24; }

    //=========================================

    public void incrementCoins(int amount) { this.coins += amount; }

    public void reduceCoins(int amount) { this.coins -= amount; }

    //=========================================

    public User setUid(String Uid) { uid = Uid; return this; }

    public User setName(String name) { this.name = name; return this; }

    public User setGoal(String goal) { this.goal = goal; return this; }

    public User setYearsSmoked(double yearsSmoked) { this.yearsSmoked = yearsSmoked; return this; }

    public User setPricePerPack(double pricePerPack) { this.pricePerPack = pricePerPack; return this; }

    public User setCigsPerDay(int cigsPerDay) { this.cigsPerDay = cigsPerDay; return this; }

    public User setCigsPerPack(int cigsPerPack) { this.cigsPerPack = cigsPerPack; return this; }

    public User setCoins(int coins) { this.coins = coins; return this; }

    public User setDateStoppedSmoking(long dateStoppedSmoking) { this.dateStoppedSmoking = dateStoppedSmoking; return this; }

    public User setLoggedToday(long loggedToday) { this.loggedToday = loggedToday; return this;}

    public User setProfilePicFilePath(String profilePicFilePath) { this.profilePicFilePath = profilePicFilePath; return this; }

    public User setBoughtItems(HashMap<String,StoreItem> boughtItems) { this.boughtItems = boughtItems; return this; }

    //=========================================

    public String getUid() { return uid; }

    public String getName() { return name; }

    public String getGoal() { return goal; }

    public double getYearsSmoked() { return yearsSmoked; }

    public double getPricePerPack() { return pricePerPack; }

    public int getCigsPerDay() { return cigsPerDay; }

    public int getCigsPerPack() { return cigsPerPack; }

    public int getCoins() { return coins; }

    public long getDateStoppedSmoking() { return dateStoppedSmoking; }

    public long getLoggedToday() { return loggedToday; }

    public long getRehabDuration(){ return Calendar.getInstance().getTimeInMillis() - dateStoppedSmoking; }

    public String getProfilePicFilePath() { return profilePicFilePath == null ? " " : profilePicFilePath; }

    public HashMap<String,StoreItem> getBoughtItems() { return boughtItems; }

}
