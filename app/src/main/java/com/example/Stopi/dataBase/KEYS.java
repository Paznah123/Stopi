package com.example.Stopi.dataBase;

public interface KEYS {

    String  LOG_TAG              = "-Stopi-";

    String  USERS_REF            = "https://fumi-app-default-rtdb.firebaseio.com/Users";
    String  TIPS_REF             = "https://fumi-app-default-rtdb.firebaseio.com/Tips";
    String  REWARDS_INFO_REF     = "https://fumi-app-default-rtdb.firebaseio.com/Rewards_Info";
    String  STORE_REF            = "https://fumi-app-default-rtdb.firebaseio.com/Store_items";
    String  EMAILS_REF           = "https://fumi-app-default-rtdb.firebaseio.com/emails";
    String  CHATS_REF           = "https://fumi-app-default-rtdb.firebaseio.com/Chats";

    String  STORE_PICS_REF       = "gs://fumi-app.appspot.com/store_pics/";
    String  FULL_PROFILE_PIC_URL = "gs://fumi-app.appspot.com/profile_pics/";

    String  GIFT_BAG_REF         = "boughtItems";
    String  COMPARE_REF          = "Compare";

    int     STORE                = 1;
    int     PROFILE              = 2;
    int     REWARDS_AMOUNT       = 13;
    int     DAYS_IN_YEAR         = 365;
    int     MINUTES_LOST_PER_CIG = 11;
    int     BALL_RADIUS          = 40;

    double  CIGARETTE_AVG_WEIGHT = 0.0008;
    double  CIGARETTE_AVG_LENGTH = 0.1;

    float   REBOUND              = 0.6f;

}