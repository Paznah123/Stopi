package com.example.Stopi.objects.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.Stopi.R;
import com.example.Stopi.callBacks.OnSendGift;
import com.example.Stopi.objects.User;
import com.example.Stopi.Utils;
import com.example.Stopi.objects.dataManage.DBreader;
import com.example.Stopi.objects.dataManage.KEYS;
import com.furkanakdemir.surroundcardview.SurroundCardView;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.MyViewHolder> {

    private View                    view;
    private HashMap<String,User>    usersMap;
    private ArrayList<String>       keys;
    private LayoutInflater          mInflater;
    private OnSendGift              onSendGift;

    //====================================================

    @Override
    public int getItemCount() {
        return usersMap.size();
    }

    public FeedAdapter(Context context, OnSendGift onSendGift) {
        this.mInflater  = LayoutInflater.from(context);
        this.onSendGift = onSendGift;
    }

    public void setUsers(HashMap<String,User> usersMap){
        this.usersMap   = usersMap;
        this.keys       = new ArrayList<>(usersMap.keySet());
    }

    //====================================================

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = mInflater.inflate(R.layout.item_feed, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String Uid = keys.get(position);
        User user = usersMap.get(Uid);
        long daysNotSmoked = TimeUnit.MILLISECONDS.toDays(user.getRehabDuration());
        long yearsNotSmoked = daysNotSmoked / 365;

        holder.user_LBL_name            .setText(user.getName());
        holder.user_LBL_clean_period    .setText(yearsNotSmoked + " years , " + daysNotSmoked%365 + " days clean!");
        holder.user_goal                .setText("\""+ user.getGoal() +"\"");
        holder.user_score               .setText("High Score: "+ user.getHighScore());

        DBreader.getInstance().readPic(KEYS.PROFILE, holder.user_IMG, user.getUid());
        setViewHolderListeners(holder, user);
    }

    private void setViewHolderListeners(MyViewHolder holder, User user) {
        holder.send_gift        .setOnClickListener(v -> onSendGift             .onSendGift(user));
        holder.smoker_history   .setOnClickListener(v -> Utils.getInstance()    .createFeedDialog(mInflater,user).show());
        holder.svc              .setOnClickListener(v -> Utils.getInstance()    .onCardClick(holder.svc));
    }

    //====================================================

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView           user_IMG;
        TextView            user_LBL_name;
        TextView            user_LBL_clean_period;
        TextView            user_goal;
        TextView            user_score;
        MaterialButton      send_gift;
        MaterialButton      smoker_history;
        SurroundCardView    svc;

        MyViewHolder(View itemView) {
            super(itemView);
            findViews();
        }

        //====================================================

        private void findViews() {
            user_IMG                = itemView.findViewById(R.id.user_IMG);
            user_LBL_name           = itemView.findViewById(R.id.user_LBL_name);
            user_LBL_clean_period   = itemView.findViewById(R.id.user_clean_period);
            user_goal               = itemView.findViewById(R.id.user_goal);
            user_score              = itemView.findViewById(R.id.user_score);
            send_gift               = itemView.findViewById(R.id.feed_send_gift);
            smoker_history          = itemView.findViewById(R.id.feed_smoker_history);
            svc                     = itemView.findViewById(R.id.surround_card_view);
        }

    }
}