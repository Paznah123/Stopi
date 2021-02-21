package com.example.Stopi.profile;

import com.example.Stopi.R;
import com.example.Stopi.dataBase.KEYS;
import com.example.Stopi.dataBase.KEYS.Status;
import com.example.Stopi.tools.App;
import com.example.Stopi.tools.Utils;
import com.example.Stopi.store.StoreItem;
import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class User implements Comparable, Serializable {

    private String  uid = "";
    private String  name = "";
    private String  goal = "";
    private String  currencySymbol = "";

    private int     cigsPerDay = 0;
    private int     cigsPerPack = 1;
    private int     coins = 0;
    private int     highScore = 0;

    private double  cigsSinceQuit = 0;
    private double  yearsSmoked = 0;
    private double  pricePerPack = 0;

    private long    dateStoppedSmoking;
    private long    loggedToday = -1;

    private Status status = Status.Offline;

    private HashMap<String, StoreItem> boughtItems = new HashMap<>();

    //=========================================

    public User(){ }

    @Override
    public int compareTo(Object o) {
        User u = (User) o;
        if(u.getName().equals(this.getName())
                && u.getUid().equals(this.getUid()))
            return 0;
        return 1;
    }

    //=========================================

    public double totalCigsSmoked(){ return (yearsSmoked * KEYS.DAYS_IN_YEAR * cigsPerDay) + cigsSinceQuit; }

    public double moneyWasted(){ return totalCigsSmoked()/cigsPerPack * pricePerPack; }

    public double lifeLost(){ return KEYS.MINUTES_LOST_PER_CIG * totalCigsSmoked() / 60 / 24; }

    //=========================================

    public double moneySaved(){ return cigsNotSmoked()/cigsPerPack * pricePerPack;}

    public double cigsNotSmoked(){ return (TimeUnit.MILLISECONDS.toHours(getRehabDuration())/24) * cigsPerDay; }

    public double lifeGained(){ return (KEYS.MINUTES_LOST_PER_CIG * cigsNotSmoked()) / 60 / 24; }

    //=========================================

    public double cigCost(){ return pricePerPack/cigsPerPack; }

    public void incrementCoins(int amount) { this.coins += amount; }

    public void reduceCoins(int amount) { this.coins -= amount; }

    //=========================================

    /**
     * updates only if bigger than 0
     */
    public boolean updateTotalCigs(double cigsSmoked) {
        if(cigsSmoked > 0){
            Utils.getInstance().playSound(App.getAppContext(), R.raw.smoke_inhale);
            cigsSinceQuit += cigsSmoked;
            this.setDateStoppedSmoking(Calendar.getInstance().getTimeInMillis());
            return true;
        }
        return false;
    }

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

    public User setBoughtItems(HashMap<String,StoreItem> boughtItems) { this.boughtItems = boughtItems; return this; }

    public User setCurrencySymbol(String currencySymbol) { this.currencySymbol = currencySymbol; return this; }

    public User setStatus(Status status) { this.status = status; return this; }

    public boolean setHighScore(int score) { if(score > highScore){ highScore = score; return true; } return false; }

    //=========================================

    public String getUid() { return uid; }

    public String getName() { return name; }

    public String getGoal() { return goal; }

    public double getYearsSmoked() { return yearsSmoked; }

    public double getPricePerPack() { return pricePerPack; }

    public int getCigsPerDay() { return cigsPerDay; }

    public int getCigsPerPack() { return cigsPerPack; }

    public int getCoins() { return coins; }

    public int getHighScore() { return highScore; }

    public Status getStatus() { return status; }

    public long getLoggedToday() { return loggedToday; }

    public long getDateStoppedSmoking() { return dateStoppedSmoking; }

    public long getRehabDuration(){ return Calendar.getInstance().getTimeInMillis() - dateStoppedSmoking; }

    public String getCurrencySymbol() { return currencySymbol; }

    public HashMap<String, StoreItem> getBoughtItems() { return boughtItems; }

}
