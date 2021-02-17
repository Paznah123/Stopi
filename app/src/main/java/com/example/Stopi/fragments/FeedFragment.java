package com.example.Stopi.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.Stopi.App;
import com.example.Stopi.R;
import com.example.Stopi.Utils;
import com.example.Stopi.callBacks.OnSendGift;
import com.example.Stopi.objects.StoreItem;
import com.example.Stopi.dataBase.DBreader;
import com.example.Stopi.adapters.FeedAdapter;
import com.example.Stopi.objects.User;
import com.example.Stopi.dataBase.DBupdater;
import com.example.Stopi.dataBase.Refs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class FeedFragment extends Fragment {

    private View view;
    private Context context;

    private RecyclerView main_LST_names;
    private FeedAdapter adapter_post;

    private String giftUserId = "";

    public interface OnFeedRefresh { void updateFeed(HashMap<String,User> usersMap); }

    //====================================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_feed, container, false);
        context = this.getContext();

        main_LST_names = view.findViewById(R.id.main_LST_names);

        main_LST_names.setLayoutManager(new LinearLayoutManager(context));
        adapter_post = new FeedAdapter(context, onSendGift);
        adapter_post.setUsers(new HashMap<>());
        main_LST_names.setAdapter(adapter_post);

        refreshUsersFeed(onFeedRefresh);

        return view;
    }

    //====================================================

    private OnFeedRefresh onFeedRefresh = new OnFeedRefresh() {
        @Override
        public void updateFeed(HashMap<String,User> usersMap) {
            adapter_post.setUsers(usersMap);
            adapter_post.notifyDataSetChanged();
            User user = usersMap.get(giftUserId);
            ArrayList<User> users = new ArrayList<>(usersMap.values());
            for (int i = 0; i < users.size(); i++) {
                if(user != null && users.get(i).getUid().equals(user.getUid()))
                    adapter_post.notifyItemChanged(i);
            }
        }
    };

    private void refreshUsersFeed(@NonNull OnFeedRefresh onFeedRefresh){
        Refs.getUsersRef()
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String,User> usersMap = new HashMap<>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if(!user.getName().equals(DBreader.getInstance().getUser().getName()))
                        usersMap.put(user.getUid(),user);
                }
                onFeedRefresh.updateFeed(usersMap);
            }

            @Override
            public void onCancelled(DatabaseError error) { }
        });
    }

    //====================================================

    private OnSendGift onSendGift = user -> {
        giftUserId = user.getUid();
        HashMap<String, StoreItem> boughtItems = DBreader.getInstance().getUser().getBoughtItems();
        if(boughtItems.size() > 0) {
            Utils.getInstance().createGiftDialog(boughtItems, (parent, view, position, id) -> {
                String itemId = new ArrayList<>(boughtItems.keySet()).get(position);
                StoreItem storeItem = boughtItems.get(itemId);
                DBupdater.getInstance().sendGift(user, storeItem);
                App.toast("Gift Sent!");
            }).show();
        } else
            App.toast("You have no items to send!");
    };
}