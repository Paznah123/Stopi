package com.example.Stopi.social.chat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.Stopi.R;
import com.example.Stopi.tools.KEYS;
import com.example.Stopi.profile.User;
import com.example.Stopi.dataBase.DBreader;
import com.example.Stopi.tools.KEYS.Status;
import com.example.Stopi.tools.Utils;
import java.util.ArrayList;
import java.util.HashMap;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context                 mContext;
    private HashMap<String,User>    mUsers;
    private ArrayList<String>       keys;

    //=============================

    public UserAdapter(Context context, HashMap<String,User> mUsers){
        this.mUsers     = mUsers;
        this.mContext   = context;
        this.keys       = new ArrayList<>(mUsers.keySet());
    }

    //=============================

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view   = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user,parent,false);
        return      new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User user = mUsers.get(keys.get(position));

        String lastSeen = Utils.get().formatToDate(user.getLastSeen());
        Status status = user.getStatus();

        DBreader.get()  .readPic(KEYS.PROFILE, holder.profile_image, user.getUid());
        holder.status_dot       .setImageResource(Utils.get().getDotByStatus(user.getStatus()));
        holder.username         .setText(user.getName());
        holder.last_seen        .setText("Last seen: " + lastSeen);
        holder.status           .setText(status.name());
        holder.itemView         .setOnClickListener(
                        view -> {
                            Intent intent = new Intent(mContext, MessageActivity.class);
                            intent.putExtra("userid", user.getUid());
                            mContext.startActivity(intent);
                        });

        if (status.equals(Status.Online))
            holder.last_seen.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() { return mUsers.size(); }

    //=============================

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView       profile_image;
        private ImageView       status_dot;
        private TextView        last_seen;
        private TextView        username;
        private TextView        status;

        public ViewHolder(View itemView){
            super(itemView);
            findViews();
        }

        private void findViews(){
            profile_image   = itemView.findViewById(R.id.profile_image);
            status_dot      = itemView.findViewById(R.id.status_dot);
            last_seen       = itemView.findViewById(R.id.user_last_seen);
            username        = itemView.findViewById(R.id.username);
            status          = itemView.findViewById(R.id.user_status);
        }
    }
}
