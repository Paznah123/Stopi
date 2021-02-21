package com.example.Stopi.store;

import android.widget.AdapterView;
import com.example.Stopi.dataBase.DBreader;
import com.example.Stopi.dataBase.DBupdater;
import com.example.Stopi.dataBase.KEYS;
import com.example.Stopi.tools.App;
import com.example.Stopi.tools.DialogView;
import com.example.Stopi.tools.Dialogs;
import com.example.Stopi.profile.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Store {

    private static Store instance;

    private  List<StoreItem> itemsList = new ArrayList<>();

    private HashMap<String, StoreItem> itemsMap = new HashMap<>();

    //=============================

    public static void initStore(){
        if(instance == null) {
            instance = new Store();
            DBreader.getInstance().readListData(KEYS.STORE_REF, instance.itemsList, StoreItem.class);
        }
    }

    public static Store getInstance() { return instance; }

    //=============================

    public List<StoreItem> getItems() { return itemsList; }

    public OnGiftSent giftCallBack(){ return onGiftSent; }

    //=============================

    private OnGiftSent onGiftSent = user -> {
        HashMap<String, StoreItem> boughtItems = DBreader.getInstance().getUser().getBoughtItems();
        if(boughtItems.size() > 0) {
            displayAvailableGifts(user, boughtItems);
        } else
            App.toast("You have no items to send!");
    };

    private void displayAvailableGifts(User giftedUser,HashMap<String, StoreItem> boughtItems){
        DialogView dialogView = Dialogs.getInstance().createGiftDialog(boughtItems, (parent, view, position, id) -> { });
        AdapterView.OnItemClickListener listener = (parent, view, position, id) -> {
            String itemId = new ArrayList<>(boughtItems.keySet()).get(position);
            getInstance().sendGift(giftedUser,boughtItems.get(itemId));
            dialogView.notifyDataSetChanged();
        };
        dialogView.setListItemsClickListener(listener);
        dialogView.show();
    }

    //=============================

    private void sendGift(User userToGift, StoreItem itemToGift){
        addStoreItem(userToGift.getBoughtItems(), itemToGift);
        DBupdater dbUpdate =  DBupdater.getInstance();
        dbUpdate.updateUser(userToGift);

        if(itemToGift.getPrice() > 1)
            itemToGift.reduceAmount();
        else
            DBreader.getInstance().getUser().getBoughtItems().remove(itemToGift.getTitle());
        dbUpdate.saveLoggedUser();
        App.toast("Gift Sent!");
    }

    //=============================

    private void addStoreItem(HashMap<String, StoreItem> userItems, StoreItem item) {
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
        HashMap<String, StoreItem> userItems = user.getBoughtItems();
        if(item.getPrice() > user.getCoins()){
            App.toast("Not Enough Coins!");
            return;
        }
        addStoreItem(userItems, item);
        user.reduceCoins(item.getPrice());
        App.toast(item.getTitle() + " Bought!");
    }

}


