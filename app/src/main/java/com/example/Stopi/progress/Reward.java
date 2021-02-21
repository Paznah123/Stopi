package com.example.Stopi.progress;

import java.time.LocalDate;

public class Reward {

    private String rewardName;
    private LocalDate unlockDate;
    private int maximum;

    private boolean unlocked;

    //============================

    public Reward() { }

    //============================

    public Reward setRewardName(String rewardName) { this.rewardName = rewardName; return this; }

    public Reward setUnlockDate(LocalDate unlockDate) { this.unlockDate = unlockDate; return this; }

    public Reward setMax(int maximum) { this.maximum = maximum; return this; }

    public Reward setUnlocked(boolean unlocked) { this.unlocked = unlocked; return this; }

    //============================

    public String getRewardName() { return rewardName; }

    public LocalDate getUnlockDate() { return unlockDate; }

    public int getMax() { return maximum; }

    public boolean isUnlocked() { return unlocked; }
}
