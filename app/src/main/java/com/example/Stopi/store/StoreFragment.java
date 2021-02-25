package com.example.Stopi.store;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.Stopi.R;
import java.util.List;

public class StoreFragment extends Fragment {

    private View view;

    private RecyclerView store_list;
    private ItemsAdapter storeAdapter;

    private List<StoreItem> store_items;

    private OnCoinsChanged onCoinsChanged;

    //====================================================

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onCoinsChanged = (OnCoinsChanged) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_store, container, false);

        findViews();
        initViews();

        return view;
    }

    //====================================================

    private void findViews() {
        store_list = view.findViewById(R.id.store_list);
    }

    private void initViews() {
        store_items = Store.get().getItems();
        store_list.setLayoutManager(new GridLayoutManager(getContext(),2));
        storeAdapter = new ItemsAdapter(store_items);
        storeAdapter.setClickListener(onCoinsChanged);
        store_list.setAdapter(storeAdapter);
    }

    //====================================================

}