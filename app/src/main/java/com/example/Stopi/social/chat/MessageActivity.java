package com.example.Stopi.social.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.Stopi.dataBase.DBupdater;
import com.example.Stopi.R;
import com.example.Stopi.tools.App;
import com.example.Stopi.tools.Utils;
import com.example.Stopi.profile.User;
import com.example.Stopi.dataBase.DBreader;
import com.example.Stopi.tools.KEYS.Status;
import com.example.Stopi.dataBase.Refs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.Stopi.tools.KEYS.PROFILE;

public class MessageActivity extends AppCompatActivity {

    private CircleImageView         profile_image;
    private ImageView               userStatus;
    private ImageView               btn_back;

    private EditText                text_send;
    private ImageButton             btn_send;
    private TextView                username;
    private TextView                last_seen;

    private RecyclerView            recyclerView;
    private LinearLayoutManager     linearLayoutManager;
    private List<Message>           mChat;

    private String                  chatId = "";

    //=============================

    private Handler handler = new Handler();
    private Runnable runnable = () -> DBupdater.get().updateStatus(Status.Online);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.get().onActivityCreateSetTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        final String userId     = getIntent().getStringExtra("userid");
        mChat                   = new ArrayList<>();
        chatId                  = Utils.get().chatIdHash(userId);

        handler                 .postDelayed(runnable,500);

        findViews();
        initRecyclerView();
        setListeners(userId);
        setProfileChangesListener(userId);
    }

    //=============================

    private void findViews() {
        recyclerView        = findViewById(R.id.messages_recycler_view);
        profile_image       = findViewById(R.id.profile_image);
        username            = findViewById(R.id.username);
        last_seen           = findViewById(R.id.user_last_seen);
        userStatus          = findViewById(R.id.status_dot);
        btn_send            = findViewById(R.id.btn_send);
        btn_back            = findViewById(R.id.chat_back);
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
                sendMessage(App.getLoggedUser().getUid(), userId, msg);
            else
                text_send.setError("Enter a message");
            text_send.setText("");
        });

        btn_back.setOnClickListener(v -> MessageActivity.this.onBackPressed());
    }

    private void setProfileChangesListener(String userId) {
        Refs.getUsersRef().child(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user               = dataSnapshot.getValue(User.class);
                        String lastSeen         = Utils.get().formatToDate(user.getLastSeen());
                        Status status           = user.getStatus();

                        username                .setText(user.getName());
                        userStatus              .setImageResource(Utils.get().getDotByStatus(status));
                        last_seen               .setText("Last seen: " + lastSeen);

                        DBreader.get()  .readPic(PROFILE,profile_image, user.getUid());

                        if (status.equals(Status.Online))
                            last_seen.setVisibility(View.INVISIBLE);
                        else
                            last_seen.setVisibility(View.VISIBLE);

                        readMessages();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });
    }

    //=============================

    private void sendMessage(String sender,String receiver,String text){
        Message message = new Message() .setSender(sender)
                                        .setReceiver(receiver)
                                        .setMessage(text);
        Refs.getChatsRef().child(chatId).push().setValue(message);
    }

    private void readMessages(){
        Refs.getChatsRef().child(chatId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                    mChat.add(snapshot.getValue(Message.class));
                recyclerView.setAdapter(new MessageAdapter(mChat));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
}
