package com.devomb.dropletseekbar.Graphics;


import android.os.Handler;

/**
 * Created by Ombrax on 5/10/2015.
 */
public class AnimationManager {

    private static final int FPS = 20;

    private final Handler indicatorHandler = new Handler();
    private final Runnable indicatorRunnable = new Runnable() {
        @Override
        public void run() {
            if (animFrameCount < maxFrameCount) {
                animFrameCount++;
                float progress = isFirstRun ? (1 - ((float)animFrameCount / maxFrameCount)) : ((float)animFrameCount / maxFrameCount);
                onAnimationListener.onAnimationFrame(progress);
                indicatorHandler.postDelayed(this, FPS);
            } else {
                animFrameCount = 0;
                onAnimationListener.onAnimationFinished(isFirstRun);
                if(isFirstRun){
                    animate(false);
                }
            }
        }
    };

    private boolean isFirstRun;
    private int animFrameCount;
    private int maxFrameCount = 300 / FPS;
    private OnAnimationListener onAnimationListener;

    public void setAnimDuration(int animDuration) {
        maxFrameCount = animDuration / FPS;
    }

    public boolean isAnimating(){
        return animFrameCount != 0;
    }

    public void animate(OnAnimationListener onAnimationListener){
        this.onAnimationListener = onAnimationListener;
        animate(true);
    }

    private void animate(boolean isFirstRun){
        this.isFirstRun = isFirstRun;
        indicatorHandler.postDelayed(indicatorRunnable, FPS);
    }

    public interface OnAnimationListener {
        void onAnimationFrame(float progress);
        void onAnimationFinished(boolean isFirstRun);
    }
}
