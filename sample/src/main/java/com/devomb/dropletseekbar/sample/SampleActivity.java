package com.devomb.dropletseekbar.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.devomb.dropletseekbar.Widget.DropletSeekBar;


public class SampleActivity extends Activity {

    private DropletSeekBar dropletSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        dropletSeekBar = (DropletSeekBar) findViewById(R.id.first_seekbar);
        dropletSeekBar.setDropletSeekBarListener(new DropletSeekBar.DropletSeekBarListener() {
            @Override
            public void onAnimationFinished() {
                System.out.println("Animation done, value " + dropletSeekBar.getBarValue());
            }

            @Override
            public void onValueChanged(int oldValue, int newValue) {
                System.out.println("Transition: " + oldValue + " >>> " + newValue);
            }
        });
    }

    public void animateToHalf(View view) {
        dropletSeekBar.setBarValue(50);
    }

}
