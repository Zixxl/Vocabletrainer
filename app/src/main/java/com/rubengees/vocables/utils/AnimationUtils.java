package com.rubengees.vocables.utils;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;

/**
 * Created by Ruben on 10.05.2015.
 */
public class AnimationUtils {

    public static void translateY(View view, int y, int duration, Interpolator interpolator) {
        ViewPropertyAnimator animator = view.animate().translationYBy(y).setDuration(duration).setInterpolator(interpolator);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            animator.withLayer();
        }
        animator.start();
    }

    public static void animate(@NonNull View view, @NonNull Techniques techniques, int duration, int delay, @Nullable final AnimationEndListener listener) {
        YoYo.with(techniques).duration(duration).delay(delay).withListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (listener != null) {
                    listener.onAnimationEnd();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).playOn(view);
    }

    public interface AnimationEndListener {
        void onAnimationEnd();
    }

}