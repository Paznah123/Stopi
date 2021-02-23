package com.example.Stopi.store;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.Stopi.R;
import com.example.Stopi.dataBase.Refs;
import com.example.Stopi.tools.App;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class GiftBagFragment extends Fragment {

    private View                view;
    private RecyclerView        bought_list;
    private ItemsAdapter        boughtAdapter;

    //===================================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view            = inflater.inflate(R.layout.fragment_gift_bag, container, false);

        bought_list     = view.findViewById(R.id.bought_list);
        bought_list     .setLayoutManager(new GridLayoutManager(getContext(),2));

        updateBoughtItems(onItemBought);

        return view;
    }

    //====================================================

    public interface OnItemBought { void refreshItemList(ArrayList<StoreItem> boughtItems); }

    private OnItemBought onItemBought = boughtItems -> {
            boughtAdapter   = new ItemsAdapter(boughtItems);
            bought_list     .setAdapter(boughtAdapter);
    };

    private void updateBoughtItems(OnItemBought onItemBought) {
        Refs.getGiftBagsRef()
                .child(App.getLoggedUser().getUid())
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