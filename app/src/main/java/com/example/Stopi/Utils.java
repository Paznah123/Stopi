package com.example.Stopi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;
import com.example.Stopi.objects.Email;
import com.example.Stopi.objects.StoreItem;
import com.example.Stopi.objects.User;
import com.example.Stopi.objects.dataManage.DBreader;
import com.example.Stopi.objects.dataManage.DBupdater;
import com.furkanakdemir.surroundcardview.SurroundCardView;
import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.material.button.MaterialButton;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Utils {

    /**
     *   formats a double by pattern - #.##
     *   (number of # as the number of digits to show after . )
     */
    public static String formatNumber(double number, String pattern){ return new DecimalFormat(pattern).format(number); }

    public static void onCardClick(SurroundCardView svc){
        if(svc.isCardSurrounded())
            svc.release();
        else
            svc.surround();
    }

    //====================================================

    /**
     *   creates AlertDialog with user gift bag for sending gift
     *   with click listener for each item
     */
    public static AlertDialog.Builder createGiftDialog(Activity activity, ArrayList<String> list, DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.AlertDialogCustom));
        builder.setTitle("Gift Bag");
        builder.setIcon(R.drawable.ic_gift_bag);
        builder.setItems(list.toArray(new String[0]), listener);

        return builder;
    }

    //====================================================

    /**
     *   creates AlertDialog for attaching message to gift
     */
    public static AlertDialog createEmailDialog(LayoutInflater inflater, String receiverKey, StoreItem storeItem){
        AlertDialog.Builder builder = new AlertDialog.Builder(inflater.getContext());
        View dialogView = inflater.inflate(R.layout.dialog_email, null);

        EditText message = dialogView.findViewById(R.id.email_message);
        MaterialButton attach = dialogView.findViewById(R.id.email_attach);
        MaterialButton cancel = dialogView.findViewById(R.id.email_cancel);

        final AlertDialog dialog = builder.create();
        dialog.setView(dialogView);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        attach.setOnClickListener(v -> {
            User sender = DBreader.getInstance().getUser();
            String title = storeItem != null ?
                    sender.getName() +" sent you a "+ storeItem.getTitle()
                    : sender.getName() + " replied";
            Email email = new Email()
                    .setKey(sender.getUid())
                    .setTitle(title)
                    .setMsg(message.getText().toString())
                    .setItemPhotoUrl(sender.getProfilePicFilePath());
            DBupdater.getInstance().sendEmail(receiverKey,email);
            App.toast("Gift Sent!");
            dialog.dismiss();
        });
        cancel.setOnClickListener(v -> {
            dialog.dismiss();
            App.toast("Gift Sent!");
        });

        return dialog;
    }
    //====================================================

    /**
     *   creates AlertDialog with user smoker history
     */
    public static AlertDialog createFeedDialog(LayoutInflater mInflater, User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mInflater.getContext());
        View dialogView = mInflater.inflate(R.layout.dialog_feed, null);

        TextView cigsSmoked = dialogView.findViewById(R.id.feed_cigs_smoked);
        TextView moneyWasted = dialogView.findViewById(R.id.feed_money_wasted);
        TextView lifeLost = dialogView.findViewById(R.id.feed_life_lost);

        cigsSmoked.setText("Cigarettes smoked: "+ formatNumber(user.totalCigsSmoked(),"##.#"));
        moneyWasted.setText("Money wasted: "+ formatNumber(user.moneyWasted(),"##.#") +" $");
        lifeLost.setText("life lost: "+ formatNumber(user.lifeLost(),"##.#") + " days");

        final AlertDialog dialog = builder.create();
        dialog.setView(dialogView);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return dialog;
    }

    //====================================================

    /**
     *   creates AlertDialog for editing user goal
     */
    public static AlertDialog createGoalDialog(Activity activity, TextView tvGoal) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        View dialogView = activity.getLayoutInflater().inflate(R.layout.dialog_goal, null);
        EditText edittext = dialogView.findViewById(R.id.goal_text);
        MaterialButton confirm = dialogView.findViewById(R.id.goal_confirm);
        MaterialButton cancel = dialogView.findViewById(R.id.goal_cancel);

        final AlertDialog dialog = builder.create();
        dialog.setView(dialogView);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        confirm.setOnClickListener(v -> {
            String userGoal = edittext.getText().toString();
            tvGoal.setText(userGoal);
            DBupdater.getInstance().updateUserGoal(userGoal);
            dialog.dismiss();
        });
        cancel.setOnClickListener(v -> dialog.dismiss());

        return dialog;
    }

    //====================================================

    /**
     *   starts activity
     */
    public static void myStartActivity(Activity activity, Class activityClass){
        Intent intent = new Intent(activity, activityClass);
        activity.startActivity(intent);
        activity.finish();
    }

    //====================================================

    /**
     *   gets image from imagePicker in fragment
     */
    public static void getImage(Fragment fragment) {
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
    public static void getImage(Activity activity) {
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
