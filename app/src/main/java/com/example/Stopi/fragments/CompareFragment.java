package com.example.Stopi.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.Stopi.R;
import com.example.Stopi.callBacks.OnFragmentTransaction;

public class CompareFragment extends Fragment  {

    public enum compareType {
        COST,
        LENGTH,
        WEIGHT
    }

    private View view;

    private CompareTypeFragment compare_cost;
    private CompareTypeFragment compare_length;
    private CompareTypeFragment compare_weight;

    private Handler handler = new Handler();
    private Runnable runnable = () -> setData();

    private OnFragmentTransaction onFragmentTransaction;

    //====================================================

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onFragmentTransaction = (OnFragmentTransaction) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_compare, container, false);

        initViews();

        handler.postDelayed(runnable, 100);

        return view;
    }

    //====================================================

    private void initViews() {
        compare_cost = initCompareFragment(R.id.compare_cost);
        compare_length = initCompareFragment(R.id.compare_length);
        compare_weight = initCompareFragment(R.id.compare_weight);
    }

    private CompareTypeFragment initCompareFragment(int layoutId){
        CompareTypeFragment fragment = new CompareTypeFragment();
        onFragmentTransaction.setFragmentToView(fragment, layoutId);
        return fragment;
     }

    //====================================================

    private void setData() {
        compare_cost.setData(compareType.COST);
        compare_length.setData(compareType.LENGTH);
        compare_weight.setData(compareType.WEIGHT);
    }

}