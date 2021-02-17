package com.example.Stopi.fragments;

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
import com.example.Stopi.objects.StoreItem;
import com.example.Stopi.adapters.ItemsAdapter;
import com.example.Stopi.dataBase.KEYS;
import com.example.Stopi.dataBase.Refs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class GiftBagFragment extends Fragment {

    private View                view;
    private Context             context;
    private RecyclerView        bought_list;
    private ItemsAdapter        boughtAdapter;

    public interface OnItemBought { void refreshItemList(ArrayList<StoreItem> boughtItems); }

    //===================================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view            = inflater.inflate(R.layout.fragment_gift_bag, container, false);
        context         = getContext();
        bought_list     = view.findViewById(R.id.bought_list);
        bought_list     .setLayoutManager(new GridLayoutManager(context,2));

        updateBoughtItems(onItemBought);

        return view;
    }

    //====================================================

    private OnItemBought onItemBought = new OnItemBought() {

        @Override
        public void refreshItemList(ArrayList<StoreItem> boughtItems) {
            boughtAdapter   = new ItemsAdapter(context, boughtItems);
            bought_list     .setAdapter(boughtAdapter);
        }
    };

    private void updateBoughtItems(@NonNull OnItemBought onItemBought) {
        Refs.getLoggedUserRef() .child(KEYS.GIFT_BAG_REF)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        ArrayList<StoreItem> itemsList = new ArrayList<>();
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren())
                                            itemsList.add(snapshot.getValue(StoreItem.class));
                                        onItemBought.refreshItemList(itemsList);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) { }
                                });
    }
}