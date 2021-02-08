package com.example.Stopi.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.Stopi.R;
import com.example.Stopi.Utils;
import com.example.Stopi.objects.dataManage.DBreader;
import com.example.Stopi.objects.User;
import com.example.Stopi.objects.dataManage.KEYS;
import java.util.concurrent.TimeUnit;

public class SmokerDataFragment extends Fragment {

    private Utils utils;
    private View view;

    public enum Section {
        Before,
        After
    }

    private Section mSection;

    private TextView progress_title;
    private TextView progress_cigs;
    private TextView progress_money;
    private TextView progress_life;

    //====================================================

    public SmokerDataFragment(Section section){ mSection = section; }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_smoker_data, container, false);

        utils = Utils.getInstance();

        findViews();
        progress_title.setText(mSection.name() + " Stopi");

        updateViewData();

        return view;
    }

    //====================================================

    private void findViews() {
        progress_title = view.findViewById(R.id.progress_title);
        progress_cigs = view.findViewById(R.id.progress_cigs);
        progress_money = view.findViewById(R.id.progress_money);
        progress_life = view.findViewById(R.id.progress_life);
    }

    //====================================================

    public void updateViewData(){
        if(mSection.equals(Section.Before))
            calculateSmokerData();
        else if(mSection.equals(Section.After))
            calculateStoppedSmokingData();
    }

    //====================================================

    private void calculateStoppedSmokingData() {
        User user = DBreader.getInstance().getUser();
        double hoursPassed = TimeUnit.MILLISECONDS.toHours(user.getRehabDuration());
        double cigsNotSmoked = hoursPassed/24 * user.getCigsPerDay();
        double moneySaved = cigsNotSmoked/user.getCigsPerPack() * user.getPricePerPack();
        double lifeGained = (KEYS.MINUTES_LOST_PER_CIG * cigsNotSmoked) / 60 / 24;

        progress_cigs.setText("Cigarettes not smoked: " + utils.formatNumber(cigsNotSmoked, "##.#"));
        progress_money.setText("Money saved: " + utils.formatNumber(moneySaved, "##.#") + " $");
        progress_life.setText("Life gained: " + utils.formatNumber(lifeGained, "##.#") + " days");
    }

    private void calculateSmokerData(){
        User user = DBreader.getInstance().getUser();

        progress_cigs.setText("Cigarettes smoked: " + utils.formatNumber(user.totalCigsSmoked(), "##.#"));
        progress_money.setText("Money wasted: " + utils.formatNumber(user.moneyWasted(), "##.#") + " $");
        progress_life.setText("Life lost: " + utils.formatNumber(user.lifeLost(), "##.#") + " days");
    }

    //====================================================

}