package com.example.Stopi.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.fragment.app.FragmentManager;
import com.example.Stopi.R;
import com.example.Stopi.dataBase.DBreader;
import com.example.Stopi.dataBase.DBupdater;
import com.example.Stopi.dataBase.Refs;
import com.example.Stopi.social.Message;
import com.example.Stopi.store.GiftListAdapter;
import com.example.Stopi.profile.User;
import com.scrounger.countrycurrencypicker.library.CountryCurrencyPicker;
import com.scrounger.countrycurrencypicker.library.Listener.CountryCurrencyPickerListener;
import com.scrounger.countrycurrencypicker.library.PickerType;

public class Dialogs {

    private static  Dialogs         instance;

    private         LayoutInflater  inflater;
    private         Context         ctx;
    private         Utils           utils;

    //=============================

    /**
     * gets the singleton
     */
    public static Dialogs get() { return instance; }

    /**
     * must call addContext for using this object methods!
     */
    public static void initDialogs(){
        if(instance == null){
            instance = new Dialogs();
            instance.utils = Utils.get();
        }
    }

    /**
     * adds context for layout inflater
     * needs activity context not app context
     */
/*    public void addContext(Context activityContext){
        if(ctx == null) {
            ctx = activityContext;
            inflater = LayoutInflater.from(activityContext);
        }
    }*/

    public void addContext(Context activityContext){
        if(ctx == null || ((Activity)ctx).isFinishing()) {
            ctx = activityContext;
            inflater = LayoutInflater.from(activityContext);
        }
    }
    private View createDialogView(int layoutId){ return inflater.inflate(layoutId, null); }

    //====================================================

    public void createCurrencyDialog(FragmentManager fragmentManager, CountryCurrencyPickerListener listener){
        CountryCurrencyPicker pickerDialog = CountryCurrencyPicker.newInstance(PickerType.COUNTRYandCURRENCY,listener);
        pickerDialog.show(fragmentManager, CountryCurrencyPicker.DIALOG_NAME);
    }

    //====================================================

    /**
     * creates AlertDialog for reset button click
     * @param listener OnClickListener for button
     */
    public GenericDialog resetDialog(View.OnClickListener listener){
        GenericDialog genericDialog = new GenericDialog(createDialogView(R.layout.dialog_reset))
                                .findConfirmButtonById(R.id.reset_confirm)
                                .addEditTexts(new int[] {R.id.reset_amount})
                                .setConfirmListener(listener);
        return genericDialog;
    }

    //====================================================

    /**
     * creates AlertDialog with user gift bag for sending gift
     * @param loggedUser logged user
     * @param userToGift gift receiver
     */
    public GenericDialog giftDialog(User loggedUser, User userToGift){
        GiftListAdapter giftsAdapter    = new GiftListAdapter(loggedUser.getBoughtItems(), userToGift);
        GenericDialog genericDialog     = new GenericDialog(createDialogView(R.layout.dialog_gift))
                                        .findRecyclerViewById(R.id.gift_recycler_view)
                                        .setRecyclerViewAdapter(giftsAdapter);
        return genericDialog;
    }

    //====================================================

    /**
     * creates AlertDialog for daily login reward
     */
    public GenericDialog rewardDialog() {
        GenericDialog genericDialog = new GenericDialog(createDialogView(R.layout.dialog_login_reward))
                                .addTextViews(new int[] {R.id.reward_login_text});
        return genericDialog;
    }

    //====================================================

    /**
     * creates AlertDialog when settings new high score in distraction game
     */
    public GenericDialog highScoreDialog() {
        GenericDialog genericDialog = new GenericDialog(createDialogView(R.layout.dialog_high_score))
                                .addTextViews(new int[] {R.id.high_score_text});
        return genericDialog;
    }

    //====================================================

    /**
     * creates AlertDialog for attaching message to gift
     * @param receiver email receiving user
     */
    public GenericDialog emailDialog(User receiver){
        GenericDialog genericDialog = new GenericDialog(createDialogView(R.layout.dialog_email))
                        .addEditTexts(new int[] {R.id.email_message})
                        .findConfirmButtonById(R.id.email_attach)
                        .findCancelButtonById(R.id.email_cancel);

        genericDialog.setConfirmListener(
                v -> {
                    String msg = genericDialog.getETtext(R.id.email_message);
                    if(msg.isEmpty()) {
                        genericDialog.setETerror(R.id.email_message,"Enter a Message");
                        return;
                    }
                    User sender = DBreader.get().getUser();
                    Message email = new Message()
                            .setSender(sender.getUid())
                            .setReceiver(receiver.getUid())
                            .setMessage(msg);
                    String chatId = utils.chatIdHash(receiver.getUid());
                    Refs.getChatsRef().child(chatId).push().setValue(email);
                    App.toast("Message Sent!");
                    genericDialog.dismiss();
                })
                .setCancelListener(v -> genericDialog.dismiss());

        return genericDialog;
    }

    //====================================================

    /**
     *   creates AlertDialog for editing user goal
     */
    public GenericDialog goalDialog(TextView tvGoal) {
        GenericDialog genericDialog = new GenericDialog(createDialogView(R.layout.dialog_goal))
                        .addEditTexts(new int[] {R.id.goal_text})
                        .findConfirmButtonById(R.id.goal_confirm)
                        .findCancelButtonById(R.id.goal_cancel);

        genericDialog.setConfirmListener(
                        v -> {
                            String userGoal = genericDialog.getETtext(R.id.goal_text);
                            tvGoal.setText(userGoal);
                            DBupdater.get().updateUserGoal(userGoal);
                            genericDialog.dismiss();
                        }
        ).setCancelListener(v -> genericDialog.dismiss());

        return genericDialog;
    }

    //====================================================

    /**
     *   creates AlertDialog with user smoker history
     */
    public GenericDialog feedDialog(User user) {
        int[] layout_id_arr     = {R.id.feed_cigs_smoked, R.id.feed_money_wasted, R.id.feed_life_lost};
        GenericDialog genericDialog = new GenericDialog(createDialogView(R.layout.dialog_feed))
                                    .addTextViews(layout_id_arr);

        genericDialog.setTVtext(R.id.feed_cigs_smoked,"Cigarettes not smoked: "+
                                utils.formatNumber(user.cigsNotSmoked(),"##.#"));

        genericDialog.setTVtext(R.id.feed_money_wasted,"Money saved: "+
                                utils.formatNumber(user.moneySaved(),"##.#") +" "+ user.getCurrencySymbol());

        genericDialog.setTVtext(R.id.feed_life_lost,"life gained: "+
                                utils.formatNumber(user.lifeGained(),"##.#") + " days");
        return genericDialog;
    }

    //====================================================

    /**
     *   creates AlertDialog with user smoker history
     */
    public GenericDialog noInternetDialog() {
        int[] layout_id_arr     = {R.id.internet_title, R.id.internet_text};
        GenericDialog genericDialog = new GenericDialog(createDialogView(R.layout.dialog_no_internet))
                .findConfirmButtonById(R.id.internet_btn_settings)
                .addTextViews(layout_id_arr)
                .setConfirmListener(v -> {
                    Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                    ctx.startActivity(intent);
                });

        return genericDialog;
    }

    //====================================================

}
