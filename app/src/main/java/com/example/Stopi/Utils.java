package com.example.Stopi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.example.Stopi.objects.DialogView;
import com.example.Stopi.objects.Message;
import com.example.Stopi.objects.StoreItem;
import com.example.Stopi.objects.User;
import com.example.Stopi.adapters.GiftListAdapter;
import com.example.Stopi.dataBase.DBreader;
import com.example.Stopi.dataBase.DBupdater;
import com.example.Stopi.dataBase.Refs;
import com.furkanakdemir.surroundcardview.SurroundCardView;
import com.github.drjacky.imagepicker.ImagePicker;
import com.scrounger.countrycurrencypicker.library.CountryCurrencyPicker;
import com.scrounger.countrycurrencypicker.library.Listener.CountryCurrencyPickerListener;
import com.scrounger.countrycurrencypicker.library.PickerType;
import java.text.DecimalFormat;
import java.util.HashMap;

public class Utils {

    private static Utils    instance;
    private Context         ctx;
    private LayoutInflater  inflater;

    //=============================

    public static Utils getInstance() { return instance; }

    /**
     * must call addContext for using this object methods!
     */
    public static void initUtils(){
        if(instance == null)
            instance = new Utils();
    }

    /**
     * adds context for layout inflater
     * needs activity context not app context
     */
    public void addContext(Context activityContext){
        if(ctx == null) {
            ctx = activityContext;
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
    }

    //=============================

    /**
     * formats a double by pattern
     * @param pattern #.## (number of # as the number of digits to show after . )
     */
    public String formatNumber(double number, String pattern){ return new DecimalFormat(pattern).format(number); }

    private View createDialogView(int layoutId){ return inflater.inflate(layoutId, null); }

    public void onCardClick(SurroundCardView svc){
        if(svc.isCardSurrounded())
            svc.release();
        else
            svc.surround();
    }

    //====================================================

    public void createCurrencyDialog(FragmentManager fragmentManager, CountryCurrencyPickerListener listener){
        CountryCurrencyPicker pickerDialog = CountryCurrencyPicker.newInstance(PickerType.COUNTRYandCURRENCY,listener);
        pickerDialog.show(fragmentManager, CountryCurrencyPicker.DIALOG_NAME);
    }

    //====================================================

    /**
     * creates AlertDialog for daily login reward
     */
    public AlertDialog createRewardDialog() {
        DialogView  dialogView  = new DialogView(createDialogView(R.layout.dialog_login_reward))
                                .addTextViews(new int[] {R.id.reward_login_text});

        return      dialogView.getAlertDialog();
    }

    public AlertDialog createHighScoreDialog() {
        DialogView  dialogView  = new DialogView(createDialogView(R.layout.dialog_high_score))
                                .addTextViews(new int[] {R.id.high_score_text});

        return      dialogView.getAlertDialog();
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
     * with click listener for each item
     * @param itemsList user gift bag titles
     */
    public AlertDialog createGiftDialog(HashMap<String, StoreItem> itemsList, AdapterView.OnItemClickListener listener){
        GiftListAdapter giftsAdapter    = new GiftListAdapter(inflater.getContext(), itemsList);
        DialogView      dialogView      = new DialogView(createDialogView(R.layout.dialog_gift))
                                        .findListViewById(R.id.gift_listView)
                                        .setListAdapter(giftsAdapter)
                                        .setListItemsClickListener(listener);

        return dialogView.getAlertDialog();
    }

    //====================================================

    /**
     * creates AlertDialog for attaching message to gift
     * @param receiverKey email receiving userId
     */
    public AlertDialog createEmailDialog(String receiverKey){
        DialogView dialogView   = new DialogView(createDialogView(R.layout.dialog_email))
                                .addEditTexts(new int[] {R.id.email_message})
                                .findConfirmButtonById(R.id.email_attach)
                                .findCancelButtonById(R.id.email_cancel);

        dialogView              .setConfirmListener(
                        v -> {
                            String msg = dialogView.getTextEditText(R.id.email_message);
                            if(msg.isEmpty()) {
                                dialogView.setEditTextError(R.id.email_message,"Enter a Message");
                                return;
                            }
                            User sender = DBreader.getInstance().getUser();
                            Message email = new Message()
                                    .setSender(sender.getUid())
                                    .setReceiver(receiverKey)
                                    .setMessage(msg);
                            Refs.getChatsRef().push().setValue(email);
                            App.toast("Message Sent!");
                            dialogView.getAlertDialog().dismiss();
                        })
                                .setCancelListener(v -> dialogView.getAlertDialog().dismiss());

        return dialogView.getAlertDialog();
    }

    //====================================================

    /**
     *   creates AlertDialog for editing user goal
     */
    public AlertDialog createGoalDialog(TextView tvGoal) {
        DialogView dialogView   = new DialogView(createDialogView(R.layout.dialog_goal))
                                .addEditTexts(new int[] {R.id.goal_text})
                                .findConfirmButtonById(R.id.goal_confirm)
                                .findCancelButtonById(R.id.goal_cancel);

        dialogView              .setConfirmListener(
                        v -> {
                                String userGoal = dialogView.getTextEditText(R.id.goal_text);
                                tvGoal.setText(userGoal);
                                DBupdater.getInstance().updateUserGoal(userGoal);
                                dialogView.getAlertDialog().dismiss();
                            }
                                ).setCancelListener(
                        v -> dialogView.getAlertDialog().dismiss());

        return dialogView.getAlertDialog();
    }

    //====================================================

    /**
     *   creates AlertDialog with user smoker history
     */
    public AlertDialog createFeedDialog(LayoutInflater mInflater, User user) {
        int[] layout_id_arr     = {R.id.feed_cigs_smoked, R.id.feed_money_wasted, R.id.feed_life_lost};
        DialogView dialogView   = new DialogView(createDialogView(R.layout.dialog_feed)).addTextViews(layout_id_arr);

        dialogView.setTextViewText(R.id.feed_cigs_smoked,"Cigarettes not smoked: "+ formatNumber(user.cigsNotSmoked(),"##.#"));
        dialogView.setTextViewText(R.id.feed_money_wasted,"Money saved: "+ formatNumber(user.moneySaved(),"##.#") +" "+ user.getCurrencySymbol());
        dialogView.setTextViewText(R.id.feed_life_lost,"life gained: "+ formatNumber(user.lifeGained(),"##.#") + " days");

        return dialogView.getAlertDialog();
    }

    //====================================================

    /**
     *   starts activity
     */
    public void myStartActivity(Activity activity, Class activityClass){
        Intent intent   = new Intent(activity, activityClass);
        activity        .startActivity(intent);
        activity        .finish();
    }

    //====================================================

    /**
     *   gets image from imagePicker in fragment
     */
    public void getImage(Fragment fragment) {
        ImagePicker.Companion
                    .with(fragment)
                    .crop()
                    .cropOval()
                    .cropSquare()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start();
    }

    /**
     *   gets image from imagePicker in activity
     */
    public void getImage(Activity activity) {
        ImagePicker.Companion
                    .with(activity)
                    .crop()
                    .cropOval()
                    .cropSquare()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start();
    }

}
