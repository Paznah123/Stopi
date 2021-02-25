package com.example.Stopi.social;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.Stopi.R;
import com.example.Stopi.profile.User;
import com.example.Stopi.tools.Dialogs;
import com.example.Stopi.store.Store;
import com.example.Stopi.tools.Utils;
import com.example.Stopi.dataBase.DBreader;
import com.example.Stopi.tools.KEYS;
import com.furkanakdemir.surroundcardview.SurroundCardView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.MyViewHolder> {

    private View                    view;
    private HashMap<String,User>    usersMap;
    private ArrayList<String>       keys;

    //====================================================

    @Override
    public int getItemCount() {
        return usersMap.size();
    }

    public FeedAdapter(HashMap<String,User> usersMap) {
        this.usersMap   = usersMap;
        this.keys       = new ArrayList<>(usersMap.keySet());
    }

    //====================================================

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String Uid                      = keys.get(position);
        User user                       = usersMap.get(Uid);
        long daysNotSmoked              = TimeUnit.MILLISECONDS.toDays(user.getRehabDuration());
        long yearsNotSmoked             = daysNotSmoked / 365;
        String clean                    = daysNotSmoked > 365 ?
                                                yearsNotSmoked + " years , " + daysNotSmoked%365 + " days clean!"
                                                    : daysNotSmoked%365 + " days clean!";

        holder.user_LBL_clean_period    .setText(clean);
        holder.user_LBL_name            .setText(user.getName());
        holder.user_goal                .setText("\""+ user.getGoal() +"\"");
        holder.user_score               .setText("High Score: "+ user.getHighScore());

        DBreader.get()          .readPic(KEYS.PROFILE, holder.user_IMG, user.getUid());

        setViewHolderListeners(holder, user);
    }

    private void setViewHolderListeners(MyViewHolder holder, User user) {
        holder.user_send_msg .setOnClickListener(v -> Dialogs   .get()     .emailDialog(user).show());
        holder.user_history  .setOnClickListener(v -> Dialogs   .get()     .feedDialog(user).show());
        holder.user_gift     .setOnClickListener(v -> Store     .get()     .chooseGift(user));
        holder.svc           .setOnClickListener(v -> Utils     .get()     .onCardClick(holder.svc));
    }
    
    //====================================================

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView            user_LBL_clean_period;
        TextView            user_LBL_name;
        TextView            user_score;
        TextView            user_goal;

        ImageView           user_send_msg;
        ImageView           user_history;
        ImageView           user_gift;
        ImageView           user_IMG;

        SurroundCardView    svc;

        MyViewHolder(View itemView) {
            super(itemView);
            findViews();
        }

        //====================================================

        private void findViews() {
            user_LBL_clean_period   = itemView.findViewById(R.id.user_clean_period);
            user_LBL_name           = itemView.findViewById(R.id.user_LBL_name);
            user_score              = itemView.findViewById(R.id.user_score);
            user_goal               = itemView.findViewById(R.id.user_goal);

            user_send_msg           = itemView.findViewById(R.id.user_send);
            user_history            = itemView.findViewById(R.id.user_history);
            user_gift               = itemView.findViewById(R.id.user_gift);
            user_IMG                = itemView.findViewById(R.id.user_IMG);

            svc                     = itemView.findViewById(R.id.surround_card_view);
        }

    }
}