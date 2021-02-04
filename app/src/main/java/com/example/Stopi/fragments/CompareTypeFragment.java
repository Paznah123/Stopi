package com.example.Stopi.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.Stopi.R;
import com.example.Stopi.Utils;
import com.example.Stopi.objects.dataManage.DBreader;
import com.example.Stopi.objects.User;
import com.example.Stopi.objects.dataManage.KEYS;
import com.furkanakdemir.surroundcardview.SurroundCardView;

public class CompareTypeFragment extends Fragment {

    private View view;

    private TextView textView;
    private ImageView imageView;

    private SurroundCardView surroundCardView;

    private User user;

    //================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_compare_type, container, false);

        user = DBreader.getInstance().getUser();

        findViews();

        surroundCardView.setOnClickListener(v ->{ Utils.onCardClick(surroundCardView); });

        return view;
    }

    //================================

    private void findViews() {
        textView = view.findViewById(R.id.compare_textView);
        imageView = view.findViewById(R.id.compare_imageView);
        surroundCardView = view.findViewById(R.id.compare_card_view);
    }

    public void setData(CompareFragment.compareType compareType) {
        switch(compareType){
            case COST:
                calculateData(compareType.name().toLowerCase(), "Dollars",KEYS.CIGARETTE_COST);
                break;
            case LENGTH:
                calculateData(compareType.name().toLowerCase(), "Meters",KEYS.CIGARETTE_AVG_LENGTH);
                break;
            case WEIGHT:
                calculateData(compareType.name().toLowerCase(), "Kilograms",KEYS.CIGARETTE_AVG_WEIGHT);
                break;
        }
    }

    //================================

    private void calculateData(String measureType, String measureUnit, double constant){
        double allCigsData = user.totalCigsSmoked() * constant;
        textView.setText("All cigarettes \n" + measureType + ": "
                                + Utils.formatNumber(allCigsData, "##.##")
                                +"\n(" + measureUnit +")");
    }

    //================================
}