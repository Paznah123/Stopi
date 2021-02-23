package com.example.Stopi.progress;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.Stopi.R;
import com.example.Stopi.tools.App;
import com.example.Stopi.tools.DialogView;
import com.example.Stopi.tools.Dialogs;
import com.example.Stopi.tools.OnFragmentTransaction;
import com.example.Stopi.profile.User;
import com.example.Stopi.dataBase.DBreader;
import com.example.Stopi.progress.SmokerDataFragment.Section;
import com.example.Stopi.dataBase.DBupdater;
import com.google.android.material.button.MaterialButton;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ProgressFragment extends Fragment {

    private View                    view;
    private TextView                time_lbl_passed;
    private TextView                random_lbl_tip;
    private TextView                user_main_goal;
    private MaterialButton          reset_progress;
    private SmokerDataFragment      pastData;
    private SmokerDataFragment      futureData;
    private OnFragmentTransaction   onFragmentTransaction;
    private DBreader                dbReader;
    private List                    tips;

    private DialogView dialogView;
    private View.OnClickListener dialogListener = v -> {
        try {
            User user = DBreader.getInstance().getUser();
            int cigsSmoked = Integer.parseInt(dialogView.getETtext(R.id.reset_amount));
            if (user.updateTotalCigs(cigsSmoked))
                DBupdater.getInstance().updateUser(user);
            pastData.updateViewData();
            dialogView.dismiss();
        } catch (NumberFormatException e){
            dialogView.setETerror(R.id.reset_amount, "Please enter a number");
        }
    };

    //====================================================

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onFragmentTransaction = (OnFragmentTransaction) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view            = inflater.inflate(R.layout.fragment_progress, container, false);

        pastData        = SmokerDataFragment.newInstance(Section.Before);
        futureData      = SmokerDataFragment.newInstance(Section.After);

        findViews();
        setListeners();

        dbReader        = DBreader.getInstance();
        tips            = dbReader.getTips();

        user_main_goal  .setText(dbReader.getUser().getGoal());

        handler         .postDelayed(runnable, 200);

        return          view;
    }

    //====================================================

    private void findViews() {
        time_lbl_passed         = view.findViewById(R.id.time_passed);
        random_lbl_tip          = view.findViewById(R.id.random_lbl_tip);
        user_main_goal          = view.findViewById(R.id.user_lbl_goal);
        reset_progress          = view.findViewById(R.id.reset_progress);

        onFragmentTransaction   .setFragmentToView(pastData,R.id.smoker_past_data);
        onFragmentTransaction   .setFragmentToView(futureData,R.id.smoker_future_data);
    }

    private void setListeners() {
        random_lbl_tip.setOnClickListener(v -> loadRandomTip());

        user_main_goal.setOnClickListener(v ->
                Dialogs.getInstance().createGoalDialog(user_main_goal).show()
        );

        reset_progress.setOnClickListener(v -> {
            dialogView = Dialogs.getInstance().createResetDialog(dialogListener);
            dialogView.show();
        });
    }

    //====================================================

    private void loadRandomTip(){
        int rand        = new Random().nextInt(tips.size());
        String tip      = (String) tips.get(rand);
        random_lbl_tip  .setText(tip);
    }

    //====================================================

    private void updateProgressClock(){ time_lbl_passed.setText(formatDuration()); }

    private String formatDuration() {
        long duration   = DBreader.getInstance().getUser().getRehabDuration();
        long years      = TimeUnit.MILLISECONDS.toDays(duration) / 365;
        long months     = (TimeUnit.MILLISECONDS.toDays(duration) % 365) / 30;
        long days       = TimeUnit.MILLISECONDS.toDays(duration) % 30;
        long hours      = TimeUnit.MILLISECONDS.toHours(duration) % 24;
        long minutes    = TimeUnit.MILLISECONDS.toMinutes(duration) % 60;
        long seconds    = TimeUnit.MILLISECONDS.toSeconds(duration) % 60;

        return String   .format("%2d-y %2d-m %2d-d %02d:%02d:%02d", years, months, days, hours, minutes, seconds);
    }

    //====================================================

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(App.getLoggedUser() == null) return;
            handler     .postDelayed(runnable, 1000);
            futureData  .updateViewData();
            updateProgressClock();
        }
    };
}