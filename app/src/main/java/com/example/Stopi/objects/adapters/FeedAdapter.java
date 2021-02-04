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
import com.furkanakdemir.surroundcardview.SurroundCardView;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.MyViewHolder> {

    private View view;
    private HashMap<String,User> mData;
    private LayoutInflater mInflater;

    private OnSendGift onSendGift;

    //====================================================

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public FeedAdapter(Context context, OnSendGift onSendGift) {
        this.mInflater = LayoutInflater.from(context);
        this.onSendGift = onSendGift;
    }

    public void setUsers(HashMap<String,User> usersMap){
        this.mData = usersMap;
    }

    //====================================================

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = mInflater.inflate(R.layout.item_feed, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String Uid = new ArrayList<>(mData.keySet()).get(position);
        User user = mData.get(Uid);
        long daysNotSmoked = TimeUnit.MILLISECONDS.toDays(user.getRehabDuration());
        long yearsNotSmoked = daysNotSmoked / 365;

        holder.post_LBL_author.setText(user.getName());
        holder.post_LBL_content.setText(yearsNotSmoked + " years , " + daysNotSmoked%365 + " days clean!");
        holder.user_goal.setText("\""+user.getGoal()+"\"");

        DBreader.getInstance().readProfilePic(holder.post_IMG_user,user.getProfilePicFilePath());
        setViewHolderListeners(holder, user);
    }

    private void setViewHolderListeners(MyViewHolder holder, User user) {
        holder.send_gift.setOnClickListener(v -> onSendGift.onSendGift(mInflater, user));
        holder.smoker_history.setOnClickListener(v -> Utils.createFeedDialog(mInflater,user).show());
        holder.svc.setOnClickListener(v -> Utils.onCardClick(holder.svc));
    }

    //====================================================

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView post_IMG_user;
        TextView post_LBL_author;
        TextView post_LBL_content;
        TextView user_goal;
        MaterialButton send_gift;
        MaterialButton smoker_history;
        SurroundCardView svc;

        MyViewHolder(View itemView) {
            super(itemView);
            findViews();
        }

        //====================================================

        private void findViews() {
            post_IMG_user = itemView.findViewById(R.id.post_IMG_user);
            post_LBL_author = itemView.findViewById(R.id.post_LBL_author);
            post_LBL_content = itemView.findViewById(R.id.post_LBL_content);
            user_goal = itemView.findViewById(R.id.user_goal);
            send_gift = itemView.findViewById(R.id.feed_send_gift);
            smoker_history = itemView.findViewById(R.id.feed_smoker_history);
            svc = itemView.findViewById(R.id.surround_card_view);
        }

    }
}