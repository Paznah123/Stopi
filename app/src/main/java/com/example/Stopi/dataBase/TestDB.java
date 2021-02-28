package com.example.Stopi.dataBase;

import com.example.Stopi.profile.User;
import com.example.Stopi.store.StoreItem;
import com.example.Stopi.tools.KEYS;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class  TestDB {

    public static void addFakeUsers(){
        ArrayList<User> users = generateUsers();
        for (User user : users) {
            String uId = Refs.getUsersRef().push().getKey();
            user.setUid(uId);
            DBupdater.get().updateUser(user);
        }
    }

    //============================================

    public static void generateAppLists(){
        pushValues(generateTips(), KEYS.TIPS_REF);
        pushValues(generateRewards_info(),KEYS.REWARDS_INFO_REF);
        pushValues(generateStoreItems(), KEYS.STORE_REF);
    }

    private static void pushValues(List list, String ref){
        for (Object element : list)
            Refs.getDBref(ref).push().setValue(element);
    }

    //============================================

    private static ArrayList<User> generateUsers(){
        ArrayList<User> users = new ArrayList<>();

        users.add(new User()
                    .setName("Shlomo Artzi")
                    .setYearsSmoked(20)
                    .setCigsPerDay(10)
                    .setPricePerPack(33.9)
                    .setCurrencySymbol("ILS ₪")
                    .setCigsPerPack(20)
                    .setGoal("Improving my health")
                    .setDateStoppedSmoking(new Date(115,8,15,12,30,25).getTime())
                    .setBoughtItems(new HashMap<>()));

        users.add(new User()
                    .setName("Maddona")
                    .setYearsSmoked(4)
                    .setCigsPerDay(7)
                    .setPricePerPack(40.2)
                    .setCurrencySymbol("US $")
                    .setCigsPerPack(20)
                    .setGoal("Lowering your risk of disease")
                    .setDateStoppedSmoking(new Date(107,3,22,17,00,00).getTime())
                    .setBoughtItems(new HashMap<>()));

        users.add(new User()
                    .setName("Jimi Alberto")
                    .setYearsSmoked(5)
                    .setCigsPerDay(6)
                    .setPricePerPack(25)
                    .setCurrencySymbol("MX $")
                    .setCigsPerPack(20)
                   .setGoal("Not exposing family or friends to secondhand smoke")
                    .setDateStoppedSmoking(new Date(103,8,24,17,10,3).getTime())
                    .setBoughtItems(new HashMap<>()));

        users.add(new User()
                    .setName("Keanu Reeves")
                    .setYearsSmoked(10)
                    .setCigsPerDay(3)
                    .setPricePerPack(32.7)
                    .setCurrencySymbol("US $")
                    .setCigsPerPack(22)
                    .setGoal("Setting a good example for my children")
                    .setDateStoppedSmoking(new Date(113,5,7,22,45,12).getTime())
                    .setBoughtItems(new HashMap<>()));

        users.add(new User()
                    .setName("Sasson Cohen")
                    .setYearsSmoked(1)
                    .setCigsPerDay(3)
                    .setPricePerPack(30.2)
                    .setCurrencySymbol("ILS ₪")
                    .setCigsPerPack(20)
                    .setGoal("Saving money")
                    .setDateStoppedSmoking(new Date(119,3,10,23,20,35).getTime())
                    .setBoughtItems(new HashMap<>()));

        users.add(new User()
                    .setName("Cristiano Ronaldo")
                    .setYearsSmoked(2)
                    .setCigsPerDay(3)
                    .setPricePerPack(55.5)
                    .setCurrencySymbol(" €")
                    .setCigsPerPack(25)
                    .setGoal("Getting rid of the lingering smell of tobacco smoke")
                    .setDateStoppedSmoking(new Date(118,2,15,10,20,35).getTime())
                   .setBoughtItems(new HashMap<>()));

        users.add(new User()
                    .setName("Benjamin Netanyahu")
                    .setYearsSmoked(7)
                    .setCigsPerDay(10)
                    .setPricePerPack(25)
                    .setCurrencySymbol("ILS ₪")
                    .setCigsPerPack(20)
                    .setGoal("Control the world")
                    .setDateStoppedSmoking(new Date(109,10,22,15,25,5).getTime())
                    .setBoughtItems(new HashMap<>()));

        users.add(new User()
                    .setName("Jennifer Adams")
                    .setYearsSmoked(8)
                    .setCigsPerDay(6)
                    .setPricePerPack(35)
                    .setCurrencySymbol("US $")
                    .setCigsPerPack(25)
                    .setGoal("I want to live longer ")
                    .setDateStoppedSmoking(new Date(115,9,6,5,30,00).getTime())
                    .setBoughtItems(new HashMap<>()));

        return users;
    }

    //============================================

    private static ArrayList<StoreItem> generateStoreItems(){
        ArrayList<StoreItem> storeItems = new ArrayList<>();

        storeItems.add(new StoreItem()
                .setTitle("Bamba")
                .setPrice(10));

        storeItems.add(new StoreItem()
                .setTitle("Nike Sneakers")
                .setPrice(1500));

        storeItems.add(new StoreItem()
                .setTitle("basketball")
                .setPrice(25));

        storeItems.add(new StoreItem()
                .setTitle("Watch")
                .setPrice(5000));

        storeItems.add(new StoreItem()
                .setTitle("Luxury Car")
                .setPrice(150000));

        storeItems.add(new StoreItem()
                .setTitle("Movie Tickets")
                .setPrice(15));

        storeItems.add(new StoreItem()
                .setTitle("Iphone")
                .setPrice(3000));

        storeItems.add(new StoreItem()
                .setTitle("Laptop")
                .setPrice(5500));

        storeItems.add(new StoreItem()
                .setTitle("Sky dive")
                .setPrice(20000));

        storeItems.add(new StoreItem()
                .setTitle("Guitar")
                .setPrice(9500));

        return storeItems;
    }

    private static ArrayList<String> generateTips(){
        ArrayList<String> tips = new ArrayList<>();

        tips.add("Picture yourself as a non-smoker");
        tips.add("Ask former smokers you know how they quit");
        tips.add("Read everything you can on the effects of smoking and how other people have quit");
        tips.add("Write your reasons to quit smoking.  When you feel the urge to smoke, review these reasons");
        tips.add("Keep a log of how much better yo ufeel and how your health is improving after the first week or so");
        tips.add("Think of all the money you will be saving by not buying cigarettes. Plan to spend it on something special");
        tips.add("Quit smoking with a friend and support one another");
        tips.add("Ask your friends not to be judgmental and to support your quitting. Ask them to not smoke around you");
        tips.add("Have plenty of raw, crunchy fruits and vegetables on hand to munch");
        tips.add("Avoid caffeine and alchohol if they increase your craving for nicotine");
        tips.add("During the first few weeks after you stop smoking, avoid social situations where there will be drinking of alcoholic beverages and smoking");
        tips.add("Throw away your ashtrays, lighters and other smoking paraphernalia");
        tips.add("Have your car cleand, Wash out the ashtray and fill it with something");
        tips.add("Have your house cleand, including the carpets, drapes and furniture");
        tips.add("Have your clothes laundered or dry-cleaned");
        tips.add("Take relaxing warm baths and showers");
        tips.add("Take a long walk");
        tips.add("Listen to music");
        tips.add("If you slip, don't berate yourself for being weak. A slip or two as you approach success is not a failure. Giving up is the only failure ");
        tips.add("Enjoy your freedom. Time is on your side");

        return tips;
    }

    private static ArrayList<String> generateRewards_info(){
        ArrayList<String> rewards_info = new ArrayList<>();

        rewards_info.add("After 20 Minutes: Your Blood Pressure and heart rate decrease");
        rewards_info.add("After 8 Hours: The carbon monoxide level in your blood returns to normal");
        rewards_info.add("After 24 Hours: Your change of heart attack decrease");
        rewards_info.add("After 48 Hours: Your ability to taste and smell starts to return");
        rewards_info.add("After 72 Hours: Your airways start to relax");
        rewards_info.add("After 5-8 Days: The average number of cravings is reduced to 3 per day");
        rewards_info.add("After 10-14 Days: Blood circulation in your gums and teeth returns to normal. cravings are reduced to 2 per day (average)");
        rewards_info.add("After 2-12 Weeks: Your circulation improves and your lung function increases");
        rewards_info.add("After 3-4 Weeks: Cessation related anger, anxiety restlessness and depression have ended");
        rewards_info.add("After 2 Months: Insulin resistance has normalized despite average weight gain of 2.7 kg");
        rewards_info.add("After 1-9 Months: Coughing and shortness of breath decrease");
        rewards_info.add("After 1-5 Years: Your risk of dying from heart attack is about half of a smoker's");
        rewards_info.add("After 10 Years: Your risk of lung cancer falls to about half of a smoker's");

        return rewards_info;
    }


}
