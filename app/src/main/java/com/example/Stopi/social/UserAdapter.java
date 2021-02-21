package com.example.Stopi.social;

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
import com.example.Stopi.profile.User;
import com.example.Stopi.dataBase.DBreader;
import com.example.Stopi.dataBase.KEYS;
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
        final User user         = mUsers.get(keys.get(position));

        DBreader.getInstance()  .readPic(KEYS.PROFILE,holder.profile_image,user.getUid());

        holder.username         .setText(user.getName());
        holder.status           .setText(user.getStatus().name());
        holder.itemView         .setOnClickListener(
                        view -> {
                                    Intent intent=new Intent(mContext, MessageActivity.class);
                                    intent.putExtra("userid",user.getUid());
                                    mContext.startActivity(intent);
                                });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    //=============================

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView        username;
        private TextView        status;
        private ImageView       profile_image;

        public ViewHolder(View itemView){
            super(itemView);
            findViews();
        }

        private void findViews(){
            username        = itemView.findViewById(R.id.username);
            profile_image   = itemView.findViewById(R.id.profile_image);
            status          = itemView.findViewById(R.id.user_status);
        }
    }
}
