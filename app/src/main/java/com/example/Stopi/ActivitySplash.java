package com.example.Stopi;

import android.animation.Animator;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import com.example.Stopi.tools.Utils;
import com.example.Stopi.profile.login.LoginActivity;

public class ActivitySplash extends AppCompatActivity {

    private final int ANIMATION_DURATION = 5000;
    private ImageView splash_IMG_logo;

    private ProgressBar progressBar;

    //====================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.get().onActivityCreateSetTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splash_IMG_logo = findViewById(R.id.splash_IMG_logo);
        progressBar = findViewById(R.id.loading_bar);
        progressBar.getIndeterminateDrawable()
                .setColorFilter(Color.parseColor("#FFFFFFFF"),
                                android.graphics.PorterDuff.Mode.SRC_ATOP);

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
                        Utils.get().myStartActivity(ActivitySplash.this, LoginActivity.class);
                        overridePendingTransition(0, 0);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) { }

                    @Override
                    public void onAnimationRepeat(Animator animator) { }
                });
    }


}