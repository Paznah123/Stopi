package com.example.Stopi.activities;

import android.animation.Animator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import com.example.Stopi.R;
import com.example.Stopi.Utils;

public class ActivitySplash extends AppCompatActivity {

    private final int ANIMATION_DURATION = 5000;
    private ImageView splash_IMG_logo;

    private Activity activity;

    //====================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        activity = this;
        splash_IMG_logo = findViewById(R.id.splash_IMG_logo);

        showView(splash_IMG_logo);
    }

    //====================================================

    public void showView(final View view) {
        view.setScaleX(0.0f);
        view.setScaleY(0.0f);
        view.setAlpha(0.0f);

        view.animate()
                .alpha(1.0f)
                .scaleY(1.0f)
                .scaleX(1.0f)
                .setDuration(ANIMATION_DURATION)
                .setInterpolator(new LinearOutSlowInInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        view.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        Utils.myStartActivity(activity, Login.class);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) { }

                    @Override
                    public void onAnimationRepeat(Animator animator) { }
                });
    }

}