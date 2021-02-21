package com.example.Stopi.social;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.Stopi.R;
import com.example.Stopi.dataBase.DBreader;
import com.example.Stopi.profile.User;
import com.example.Stopi.dataBase.Refs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class FeedFragment extends Fragment {

    private View            view;

    private RecyclerView    main_LST_names;
    private FeedAdapter     adapter_post;

    public interface OnFeedRefresh { void updateFeed(HashMap<String,User> usersMap); }

    //====================================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view                = inflater.inflate(R.layout.fragment_feed, container, false);

        main_LST_names      = view.findViewById(R.id.main_LST_names);

        adapter_post        = new FeedAdapter(new HashMap<>());
        main_LST_names      .setLayoutManager(new LinearLayoutManager(getContext()));
        main_LST_names      .setAdapter(adapter_post);

        refreshUsersFeed(onFeedRefresh);

        return view;
    }

    //====================================================

    private OnFeedRefresh onFeedRefresh = new OnFeedRefresh() {
        @Override
        public void updateFeed(HashMap<String,User> usersMap) {
            adapter_post    = new FeedAdapter(usersMap);
            main_LST_names  .setAdapter(adapter_post);
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

}