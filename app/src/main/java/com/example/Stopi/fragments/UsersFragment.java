package com.example.Stopi.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.Stopi.R;
import com.example.Stopi.objects.User;
import com.example.Stopi.adapters.UserAdapter;
import com.example.Stopi.dataBase.Refs;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;

public class UsersFragment extends Fragment {

    private View                    view;
    private RecyclerView            recyclerView;
    private UserAdapter             userAdapter;
    private HashMap<String,User>    mUsers;

    //=============================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view            = inflater.inflate(R.layout.fragment_users,container,false);

        mUsers          = new HashMap<>();

        recyclerView    = view.findViewById(R.id.recycler_view);
        recyclerView    .setHasFixedSize(true);
        recyclerView    .setLayoutManager(new LinearLayoutManager(getContext()));

        readUsers();

        return          view;
    }

    //=============================

    private void readUsers() {
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        Refs.getUsersRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    assert user != null;
                    assert firebaseUser != null;
                    if(!user.getUid().equals(firebaseUser.getUid())){
                        mUsers.put(user.getUid(),user);
                    }
                }
                userAdapter = new UserAdapter(getContext(),mUsers);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
}
