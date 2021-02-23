package com.example.Stopi.tools;

import android.content.Context;
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
import com.example.Stopi.store.StoreItem;
import com.example.Stopi.profile.User;
import com.scrounger.countrycurrencypicker.library.CountryCurrencyPicker;
import com.scrounger.countrycurrencypicker.library.Listener.CountryCurrencyPickerListener;
import com.scrounger.countrycurrencypicker.library.PickerType;
import java.util.HashMap;

public class Dialogs {

    private static  Dialogs         instance;

    private         LayoutInflater  inflater;
    private         Context         ctx;

    //=============================

    public static Dialogs getInstance() { return instance; }

    /**
     * must call addContext for using this object methods!
     */
    public static void initDialogs(){
        if(instance == null)
            instance = new Dialogs();
    }

    /**
     * adds context for layout inflater
     * needs activity context not app context
     */
    public void addContext(Context activityContext){
        if(ctx == null) {
            ctx = activityContext;
            inflater = LayoutInflater.from(ctx);
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
     */
    public DialogView createResetDialog(View.OnClickListener listener){
        DialogView  dialogView  = new DialogView(createDialogView(R.layout.dialog_reset))
                                .findConfirmButtonById(R.id.reset_confirm)
                                .addEditTexts(new int[] {R.id.reset_amount})
                                .setConfirmListener(listener);
        return dialogView;
    }

    //====================================================

    /**
     * creates AlertDialog with user gift bag for sending gift
     * @param itemsList logged user gift bag items
     */
    public DialogView createGiftDialog(HashMap<String, StoreItem> itemsList, User userToGift){
        GiftListAdapter giftsAdapter    = new GiftListAdapter(itemsList, userToGift);
        DialogView      dialogView      = new DialogView(createDialogView(R.layout.dialog_gift))
                                        .findRecyclerViewById(R.id.gift_recycler_view)
                                        .setRecyclerViewAdapter(giftsAdapter);
        return dialogView;
    }

    //====================================================

    /**
     * creates AlertDialog for daily login reward
     */
    public DialogView createRewardDialog() {
        DialogView dialogView  = new DialogView(createDialogView(R.layout.dialog_login_reward))
                                .addTextViews(new int[] {R.id.reward_login_text});
        return dialogView;
    }

    //====================================================

    /**
     * creates AlertDialog when settings new high score in distraction game
     */
    public DialogView createHighScoreDialog() {
        DialogView  dialogView  = new DialogView(createDialogView(R.layout.dialog_high_score))
                                .addTextViews(new int[] {R.id.high_score_text});
        return dialogView;
    }

    //====================================================

    /**
     * creates AlertDialog for attaching message to gift
     * @param receiverKey email receiving userId
     */
    public DialogView createEmailDialog(String receiverKey){
        DialogView dialogView   = new DialogView(createDialogView(R.layout.dialog_email))
                .addEditTexts(new int[] {R.id.email_message})
                .findConfirmButtonById(R.id.email_attach)
                .findCancelButtonById(R.id.email_cancel);

        dialogView              .setConfirmListener(
                v -> {
                    String msg = dialogView.getETtext(R.id.email_message);
                    if(msg.isEmpty()) {
                        dialogView.setETerror(R.id.email_message,"Enter a Message");
                        return;
                    }
                    User sender = DBreader.getInstance().getUser();
                    Message email = new Message()
                            .setSender(sender.getUid())
                            .setReceiver(receiverKey)
                            .setMessage(msg);
                    String chatId = Utils.getInstance().chatIdHash(receiverKey);
                    Refs.getChatsRef().child(chatId).push().setValue(email);
                    App.toast("Message Sent!");
                    dialogView.dismiss();
                })
                .setCancelListener(v -> dialogView.dismiss());

        return dialogView;
    }

    //====================================================

    /**
     *   creates AlertDialog for editing user goal
     */
    public DialogView createGoalDialog(TextView tvGoal) {
        DialogView dialogView   = new DialogView(createDialogView(R.layout.dialog_goal))
                                .addEditTexts(new int[] {R.id.goal_text})
                                .findConfirmButtonById(R.id.goal_confirm)
                                .findCancelButtonById(R.id.goal_cancel);

        dialogView  .setConfirmListener(
                        v -> {
                            String userGoal = dialogView.getETtext(R.id.goal_text);
                            tvGoal.setText(userGoal);
                            DBupdater.getInstance().updateUserGoal(userGoal);
                            dialogView.dismiss();
                        }
        ).setCancelListener(v -> dialogView.dismiss());

        return dialogView;
    }

    //====================================================

    /**
     *   creates AlertDialog with user smoker history
     */
    public DialogView createFeedDialog(User user) {
        int[] layout_id_arr     = {R.id.feed_cigs_smoked, R.id.feed_money_wasted, R.id.feed_life_lost};
        DialogView dialogView   = new DialogView(createDialogView(R.layout.dialog_feed)).addTextViews(layout_id_arr);

        dialogView.setTVtext(R.id.feed_cigs_smoked,"Cigarettes not smoked: "+ Utils.getInstance().formatNumber(user.cigsNotSmoked(),"##.#"));
        dialogView.setTVtext(R.id.feed_money_wasted,"Money saved: "+ Utils.getInstance().formatNumber(user.moneySaved(),"##.#") +" "+ user.getCurrencySymbol());
        dialogView.setTVtext(R.id.feed_life_lost,"life gained: "+ Utils.getInstance().formatNumber(user.lifeGained(),"##.#") + " days");

        return dialogView;
    }

    //====================================================

}
