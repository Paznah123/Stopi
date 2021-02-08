package com.example.Stopi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;

import com.example.Stopi.callBacks.OnProfileUpdate;
import com.example.Stopi.objects.DialogView;
import com.example.Stopi.objects.Email;
import com.example.Stopi.objects.StoreItem;
import com.example.Stopi.objects.User;
import com.example.Stopi.objects.adapters.GiftListAdapter;
import com.example.Stopi.objects.dataManage.DBreader;
import com.example.Stopi.objects.dataManage.DBupdater;
import com.furkanakdemir.surroundcardview.SurroundCardView;
import com.github.drjacky.imagepicker.ImagePicker;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class Utils {

    private static Utils instance;

    //=============================

    public static void initUtils(){
        if(instance == null)
            instance = new Utils();
    }

    public static Utils getInstance() { return instance; }

    //=============================

    /**
     * formats a double by pattern
     * @param pattern #.## (number of # as the number of digits to show after . )
     */
    public String formatNumber(double number, String pattern){ return new DecimalFormat(pattern).format(number); }

    public void onCardClick(SurroundCardView svc){
        if(svc.isCardSurrounded())
            svc.release();
        else
            svc.surround();
    }

    //====================================================

    /**
     * creates AlertDialog for reset button click
     */
    public DialogView createResetDialog(LayoutInflater inflater, View.OnClickListener listener){
        View view = inflater.inflate(R.layout.dialog_reset, null);
        DialogView dialogView = new DialogView(view)
                .findConfirmButtonById(R.id.reset_confirm)
                .addEditTexts(1, new int[] {R.id.reset_amount})
                .setConfirmListener(listener);

        return dialogView;
    }

    /**
     * creates AlertDialog with user gift bag for sending gift
     * with click listener for each item
     * @param itemsList user gift bag titles
     */
    public AlertDialog createGiftDialog(LayoutInflater inflater, HashMap<String, StoreItem> itemsList, AdapterView.OnItemClickListener listener){
        View view = inflater.inflate(R.layout.dialog_gift, null);
        GiftListAdapter giftListAdapter = new GiftListAdapter(inflater.getContext(), itemsList);
        DialogView dialogView = new DialogView(view)
                .findListViewById(R.id.gift_listView)
                .setListAdapter(giftListAdapter)
                .setListItemsClickListener(listener);

        return dialogView.getDialog();
    }

    //====================================================

    /**
     * creates AlertDialog for attaching message to gift
     * @param receiverKey email receiving userId
     */

    public AlertDialog createEmailDialog(LayoutInflater inflater, String receiverKey, StoreItem storeItem){
        View view = inflater.inflate(R.layout.dialog_email, null);
        DialogView dialogView = new DialogView(view)
                .addEditTexts(1, new int[] {R.id.email_message})
                .findConfirmButtonById(R.id.email_attach)
                .findCancelButtonById(R.id.email_cancel);

        dialogView.setConfirmListener(v -> {
            User sender = DBreader.getInstance().getUser();
            String title = storeItem != null ? sender.getName() +" sent you a "+ storeItem.getTitle()
                                                : sender.getName() + " replied";
            Email email = new Email().setKey(sender.getUid())
                                    .setTitle(title)
                                    .setMsg(dialogView.getText(0))
                                    .setItemPhotoUrl(sender.getProfilePicFilePath());
            DBupdater.getInstance().sendEmail(receiverKey,email);
            App.toast("Gift Sent!");
            dialogView.getDialog().dismiss();
        }).setCancelListener(v -> {
            dialogView.getDialog().dismiss();
            App.toast("Gift Sent!");
        });

        return dialogView.getDialog();
    }

    //====================================================

    /**
     *   creates AlertDialog for editing user goal
     */
    public AlertDialog createGoalDialog(LayoutInflater inflater, TextView tvGoal) {
        View view = inflater.inflate(R.layout.dialog_goal, null);

        DialogView dialogView = new DialogView(view)
                .addEditTexts(1, new int[] {R.id.goal_text})
                .findConfirmButtonById(R.id.goal_confirm)
                .findCancelButtonById(R.id.goal_cancel);

        dialogView.setConfirmListener(v -> {
            String userGoal = dialogView.getText(0);
            tvGoal.setText(userGoal);
            DBupdater.getInstance().updateUserGoal(userGoal);
            dialogView.getDialog().dismiss();
        }).setCancelListener(v -> dialogView.getDialog().dismiss());

        return dialogView.getDialog();
    }

    //====================================================

    /**
     *   creates AlertDialog with user smoker history
     */
    public AlertDialog createFeedDialog(LayoutInflater mInflater, User user) {
        View view = mInflater.inflate(R.layout.dialog_feed, null);

        int[] layout_id_arr = {R.id.feed_cigs_smoked, R.id.feed_money_wasted, R.id.feed_life_lost};
        DialogView dialogView = new DialogView(view).addTextViews(3,layout_id_arr);

        dialogView.setText(0,"Cigarettes smoked: "+ formatNumber(user.totalCigsSmoked(),"##.#"));
        dialogView.setText(1,"Money wasted: "+ formatNumber(user.moneyWasted(),"##.#") +" $");
        dialogView.setText(2,"life lost: "+ formatNumber(user.lifeLost(),"##.#") + " days");

        return dialogView.getDialog();
    }

    //====================================================

    /**
     *   starts activity
     */
    public void myStartActivity(Activity activity, Class activityClass){
        Intent intent = new Intent(activity, activityClass);
        activity.startActivity(intent);
        activity.finish();
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
