package com.example.Stopi.store;

import com.example.Stopi.dataBase.DBreader;
import com.example.Stopi.dataBase.DBupdater;
import com.example.Stopi.tools.KEYS;
import com.example.Stopi.tools.App;
import com.example.Stopi.tools.Dialogs;
import com.example.Stopi.profile.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Store {

    private static Store instance;

    private  List<StoreItem> itemsList = new ArrayList<>();

    //=============================

    public static void initStore(){
        if(instance == null) {
            instance = new Store();
            DBreader.getInstance().readListData(KEYS.STORE_REF, instance.itemsList, StoreItem.class);
        }
    }

    public static Store getInstance() { return instance; }

    public List<StoreItem> getItems() { return itemsList; }

    //=============================

    private OnGiftSent onGiftSent = user -> {
        HashMap<String, StoreItem> boughtItems = DBreader.getInstance().getUser().getBoughtItems();
        if(boughtItems.size() > 0) {
            Dialogs.getInstance().createGiftDialog(boughtItems, user).show();
        } else
            App.toast("You have no items to send!");
    };

    //=============================

    public void chooseGift(User user){ onGiftSent.chooseGift(user); }

    public void sendGift(User userToGift, StoreItem itemToGift){
        User loggedUser = DBreader.getInstance().getUser();
        addStoreItem(userToGift, itemToGift);
        DBupdater dbUpdate =  DBupdater.getInstance();
        dbUpdate.updateGiftBag(userToGift);

        if(itemToGift.getPrice() > 1)
            itemToGift.reduceAmount();
        else
            loggedUser.getBoughtItems().remove(itemToGift.getTitle());
        dbUpdate.updateGiftBag(loggedUser);

        App.toast("Gift Sent!");
    }

    //=============================

    private void addStoreItem(User user, StoreItem item) {
        HashMap<String, StoreItem> userItems = user.getBoughtItems();
        if(userItems.containsKey(item.getTitle()))
            userItems.get(item.getTitle()).incrementAmount();
        else {
            userItems.put(item.getTitle() ,
                            new StoreItem()
                            .setTitle(item.getTitle())
                            .setPrice(1));
        }
    }

    //=============================

    public void buyItem(User user, StoreItem item){
        if(item.getPrice() > user.getCoins()){
            App.toast("Not Enough Coins!");
            return;
        }
        addStoreItem(user, item);
        user.reduceCoins(item.getPrice());
        App.toast(item.getTitle() + " Bought!");
    }

}


