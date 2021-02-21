package com.example.Stopi.social;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.Stopi.dataBase.DBupdater;
import com.example.Stopi.R;
import com.example.Stopi.tools.Utils;
import com.example.Stopi.profile.User;
import com.example.Stopi.dataBase.DBreader;
import com.example.Stopi.dataBase.KEYS;
import com.example.Stopi.dataBase.Refs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    private CircleImageView         profile_image;
    private TextView                username;
    private TextView                userStatus;

    private ImageButton             btn_send;
    private EditText                text_send;

    private RecyclerView            recyclerView;
    private LinearLayoutManager     linearLayoutManager;
    private MessageAdapter          messageAdapter;
    private List<Message>           mChat;

    private String                  chatId = "";

    //=============================

    private Handler handler = new Handler();
    private Runnable runnable = () -> DBupdater.getInstance().updateStatus(KEYS.Status.Online);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        handler.postDelayed(runnable,350);

        final String userId     = getIntent().getStringExtra("userid");
        chatId                  = Utils.getInstance().chatIdHash(userId);
        mChat                   = new ArrayList<>();

        findViews();
        initRecyclerView();
        setListeners(userId);

        Refs.getUsersRef().child(userId)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user               = dataSnapshot.getValue(User.class);
                username                .setText(user.getName());
                userStatus              .setText(user.getStatus().name());
                DBreader.getInstance()  .readPic(KEYS.PROFILE,profile_image, user.getUid());

                readMessage();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

    }

    //=============================

    private void findViews() {
        recyclerView        = findViewById(R.id.recycler_view);
        profile_image       = findViewById(R.id.profile_image);
        username            = findViewById(R.id.username);
        userStatus          = findViewById(R.id.user_status);
        btn_send            = findViewById(R.id.btn_send);
        text_send           = findViewById(R.id.text_send);
    }

    private void initRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager .setStackFromEnd(true);
        recyclerView        .setHasFixedSize(true);
        recyclerView        .setLayoutManager(linearLayoutManager);
    }

    private void setListeners(String userId){
        btn_send.setOnClickListener(view -> {
            String msg = text_send.getText().toString();
            if(!msg.equals(""))
                sendMessage(DBreader.getInstance().getUser().getUid(),userId,msg);
            else
                text_send.setError("Enter a message");
            text_send.setText("");
        });
    }

//=============================

    private void sendMessage(String sender,String receiver,String text){
        Message message = new Message() .setSender(sender)
                                        .setReceiver(receiver)
                                        .setMessage(text);
        Refs.getChatsRef().child(chatId).push().setValue(message);
    }

    private void readMessage(){
        Refs.getChatsRef().child(chatId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                    mChat.add(snapshot.getValue(Message.class));
                messageAdapter = new MessageAdapter(mChat);
                recyclerView.setAdapter(messageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
}
