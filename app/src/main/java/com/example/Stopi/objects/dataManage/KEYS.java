package com.example.Stopi.objects.dataManage;

public interface KEYS {

    String LOG_TAG = "-Stopi-";

    String USERS_REF = "Users";

    String TIPS_REF = "Tips";

    String REWARDS_INFO_REF = "Rewards_Info";

    String STORE_REF = "Store_items";

    String COMPARE_REF = "Compare";

    String GIFT_BAG_REF = "boughtItems";

    String EMAILS_REF = "emails";

    String DATE_REF = "dateStoppedSmoking";
    
    String PROFILE_PICS_REF = "profile_pics/";

    int REWARDS_AMOUNT = 13;

    int DAYS_IN_YEAR = 365;

    int MINUTES_LOST_PER_CIG = 11;

    double CIGARETTE_AVG_WEIGHT = 0.0008;

    double CIGARETTE_AVG_LENGTH = 0.1;

    double CIGARETTE_COST = DBreader.getInstance().getUser().getPricePerPack()
                                / DBreader.getInstance().getUser().getCigsPerPack();
}