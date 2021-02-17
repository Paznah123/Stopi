package com.example.Stopi.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import com.example.Stopi.R;
import com.example.Stopi.dataBase.DBreader;
import com.example.Stopi.objects.Reward;
import com.wajahatkarim3.easyflipview.EasyFlipView;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiresApi(api = Build.VERSION_CODES.O)
public class RewardsAdapter extends RecyclerView.Adapter<RewardsAdapter.RewardsViewHolder> {

    private View view;
    private LayoutInflater inflater;

    private List<Reward> rewards;

    private MyItemClickListener mClickListener;

    //====================================================

    public RewardsAdapter(Context context, List<Reward> rewards){
        this.rewards = rewards;
        this.inflater = LayoutInflater.from(context);
    }

    //====================================================

    @NonNull
    @Override
    public RewardsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.item_reward,parent,false);
        return new RewardsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RewardsViewHolder holder, int position) {
        Reward reward = rewards.get(position);
        setHolderData(holder,position, reward);
    }

    //====================================================

    private void setHolderData(RewardsViewHolder holder, int position, Reward reward){
        int time = (int)TimeUnit.MILLISECONDS.toDays(DBreader.getInstance().getUser().getRehabDuration());
        LocalDate date = reward.getUnlockDate();
        String text = "";

        if(reward.isUnlocked())
            text = "Unlocked!";
        else
            text = date.getMonth() +" - "+ date.getDayOfMonth() +" - "+ date.getYear();

        holder.progressBar.setMax(reward.getMax());
        holder.progressBar.setProgress(time);
        holder.rewardText.setText(reward.getRewardName());
        holder.reward_info.setText((String) DBreader.getInstance().getRewardsInfo().get(position));
        holder.unlock_date.setText(text);
    }

    //====================================================

    @Override
    public int getItemCount() {
        return rewards.size();
    }

    public void setClickListener(MyItemClickListener itemClickListener) { this.mClickListener = itemClickListener; }

    public interface MyItemClickListener { default void onItemClick(EasyFlipView flipView){ flipView.flipTheView(); }}

    //====================================================

    public class RewardsViewHolder extends RecyclerView.ViewHolder {

        TextView rewardText;
        ProgressBar progressBar;
        TextView unlock_date;
        TextView reward_info;
        EasyFlipView flipView;

        RewardsViewHolder(View itemView) {
            super(itemView);
            findViews();
            setViewHolderData();
            flipView.flipTheView();
        }

        //====================================================

        void findViews(){
            rewardText = itemView.findViewById(R.id.reward_text);
            progressBar = itemView.findViewById(R.id.reward_progressBar);
            unlock_date = itemView.findViewById(R.id.unlock_date);
            reward_info = itemView.findViewById(R.id.reward_info);
            flipView = itemView.findViewById(R.id.flip_view);
        }

        private void setViewHolderData(){
            itemView.setOnClickListener(v -> {
                if (mClickListener != null)
                    mClickListener.onItemClick(flipView);
            });
        }

        //====================================================

    }
}
