package com.example.Stopi.progress;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.Stopi.R;
import com.example.Stopi.tools.Utils;
import com.example.Stopi.dataBase.DBreader;
import com.example.Stopi.profile.User;

public class SmokerDataFragment extends Fragment {

    private static final String Section_TAG = "Section";

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

    public static SmokerDataFragment newInstance(Section section) {
        SmokerDataFragment myFragment = new SmokerDataFragment();

        Bundle args = new Bundle();
        args.putString(Section_TAG, section.name());
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_smoker_data, container, false);

        utils = Utils.get();

        findViews();
        if(getArguments() == null) mSection = Section.After;
        mSection = Section.valueOf(getArguments().getString(Section_TAG));
        progress_title.setText(mSection + " Stopi");

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
        try {
            if (mSection.equals(Section.After))
                calculateStoppedSmokingData();
            else
                calculateSmokerData();
        } catch (NullPointerException e) {}
    }

    //====================================================

    private void calculateStoppedSmokingData() {
        User user = DBreader.get().getUser();

        progress_cigs.setText("Cigarettes not smoked: " + utils.formatNumber(user.cigsNotSmoked(), "##.#"));
        progress_money.setText("Money saved: " + utils.formatNumber(user.moneySaved(), "##.#") + " "+ user.getCurrencySymbol());
        progress_life.setText("Life gained: " + utils.formatNumber(user.lifeGained(), "##.#") + " days");
    }

    private void calculateSmokerData(){
        User user = DBreader.get().getUser();

        progress_cigs.setText("Cigarettes smoked: " + utils.formatNumber(user.totalCigsSmoked(), "##.#"));
        progress_money.setText("Money wasted: " + utils.formatNumber(user.moneyWasted(), "##.#") + " "+ user.getCurrencySymbol());
        progress_life.setText("Life lost: " + utils.formatNumber(user.lifeLost(), "##.#") + " days");
    }

    //====================================================

}