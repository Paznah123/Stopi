package com.example.Stopi.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.Stopi.objects.Message;
import com.example.Stopi.R;
import com.example.Stopi.objects.User;
import com.example.Stopi.adapters.UserAdapter;
import com.example.Stopi.dataBase.DBreader;
import com.example.Stopi.dataBase.Refs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class ChatsFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;

    private User user;

    private HashMap<String,User> allChats;
    private HashMap<String,Boolean> allUserMessages;

    //=============================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view                = inflater.inflate(R.layout.fragment_chats, container, false);
        user                = DBreader.getInstance().getUser();
        allUserMessages     = new HashMap<>();

        initRecyclerView();
        getUserChats();

        return view;
    }

    //=============================

    private void initRecyclerView() {
        recyclerView    = view.findViewById(R.id.recycler_view);
        recyclerView    .setHasFixedSize(true);
        recyclerView    .setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void getUserChats() {
        Refs.getChatsRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allUserMessages.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Message message =snapshot.getValue(Message.class);
                    if(message.getSender().equals(user.getUid()))
                        allUserMessages.put(message.getReceiver(), true);
                    else if(message.getReceiver().equals(user.getUid()))
                        allUserMessages.put(message.getSender(), true);
                }
                populateChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    //=============================

    private void populateChats(){
        allChats = new HashMap<>();
        Refs.getUsersRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allChats.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    allChats.put(user.getUid(),user);
                }
                userAdapter = new UserAdapter(getContext(), allChats);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    //=============================

}
