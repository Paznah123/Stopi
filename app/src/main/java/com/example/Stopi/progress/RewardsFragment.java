package com.example.Stopi.progress;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.Stopi.R;
import com.example.Stopi.dataBase.DBreader;
import com.example.Stopi.profile.User;
import com.example.Stopi.tools.App;
import com.example.Stopi.tools.KEYS;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiresApi(api = Build.VERSION_CODES.O)
public class RewardsFragment extends Fragment {

    private View view;

    private RecyclerView rewards_list;
    private RewardsAdapter rewardsAdapter;

    private List<Reward> rewards;

    //====================================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_rewards, container, false);

        init_views();

        rewardsAdapter  = new RewardsAdapter(rewards);
        rewards_list    .setLayoutManager(new LinearLayoutManager(getContext()));
        rewards_list    .setAdapter(rewardsAdapter);

        if(App.isNetworkAvailable()) createRewards(rewards);

        return view;
    }

    //====================================================

    void init_views(){
        rewards_list = view.findViewById(R.id.rewards_list);
        rewards = new ArrayList<>();
    }

    //====================================================

    void createRewards(List<Reward> rewards){
        User user = DBreader.get().getUser();
        if(user == null) return;
        int time = (int) TimeUnit.MILLISECONDS.toDays(user.getRehabDuration());
        for (int i = 0; i < KEYS.REWARDS_AMOUNT; i++) {
            int max = (int)(Math.pow(i,3)+1);
            LocalDate newDate = LocalDate.now().plusDays(max);

            Reward reward = new Reward()
                    .setRewardName(max + " Days Clean!")
                    .setMax(max)
                    .setUnlockDate(newDate);
            if(time > reward.getMax())
                reward.setUnlocked(true);
            rewards.add(reward);
        }
    }
}