package com.example.Stopi.social;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.Stopi.R;
import com.example.Stopi.tools.App;
import com.example.Stopi.profile.User;
import com.example.Stopi.dataBase.Refs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;

public class ChatsFragment extends Fragment {

    private View                    view;
    private RecyclerView            recyclerView;
    private HashMap<String,User>    mUsers;

    //=============================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view            = inflater.inflate(R.layout.fragment_users,container,false);

        mUsers          = new HashMap<>();

        recyclerView    = view.findViewById(R.id.users_recycler_view);
        recyclerView    .setHasFixedSize(true);
        recyclerView    .setLayoutManager(new LinearLayoutManager(getContext()));

        readUsers();

        return          view;
    }

    //=============================

    private void readUsers() {
        Refs.getUsersRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    if(!user.getUid().equals(App.getLoggedUser().getUid()))
                        mUsers.put(user.getUid(),user);
                }
                recyclerView.setAdapter(new UserAdapter(getContext(), mUsers));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
}
