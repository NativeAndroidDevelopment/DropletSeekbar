package com.devomb.dropletseekbar.Widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.devomb.dropletseekbar.Graphics.AnimationManager;
import com.devomb.dropletseekbar.Graphics.GraphicsManager;
import com.devomb.dropletseekbar.R;

/**
 * Created by Ombrax on 17/09/2015.
 */
public class DropletSeekBar extends View implements AnimationManager.OnAnimationListener {

    //region declaration
    //region constant
    private final int TOUCH_DOWN_THRESHOLD = 100;
    private final int defaultBarColor = 0xffde571d;
    private final int defaultBarBackgroundColor = 0x80000000;
    private final int defaultTextColor = 0xaa000000;
    //endregion

    //region inner field
    private GraphicsManager graphicsManager;
    private AnimationManager animationManager;
    private boolean isDown;
    private float deltaTransform = 1f;
    private long startTouchDown;
    //endregion

    //region variable
    private int minValue;
    private int maxValue;
    private int barValue;
    private DropletSeekBarListener dropletSeekBarListener;
    //endregion
    //endregion

    //region constructor
    public DropletSeekBar(Context context) {
        super(context);
        init(null, 0);
    }

    public DropletSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public DropletSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }
    //endregion

    //region setup
    private void init(AttributeSet attrs, int defStyleAttr) {
        graphicsManager = new GraphicsManager();
        animationManager = new AnimationManager();
        getAttributes(attrs, defStyleAttr);
    }
    //endregion

    //region getter setter
    //region getter
    public void setDropletSeekBarListener(DropletSeekBarListener dropletSeekBarListener) {
        this.dropletSeekBarListener = dropletSeekBarListener;
    }

    public int getBarColor() {
        return graphicsManager.getBarPaint().getColor();
    }

    public int getBarBackgroundColor() {
        return graphicsManager.getBarBackgroundPaint().getColor();
    }

    public int getMinValue() {
        return minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public int getBarValue() {
        return barValue;
    }

    public int getDropletTextColor() {
        return graphicsManager.getTextPaint().getColor();
    }
    //endregion


    //region setter
    public void setBarColor(int barColor) {
        graphicsManager.getBarPaint().setColor(barColor);
    }

    public void setBarBackgroundColor(int barBackgroundColor) {
        graphicsManager.getBarBackgroundPaint().setColor(barBackgroundColor);
    }

    public void setRange(int minValue, int maxValue) {
        if (minValue < 0 || maxValue < 0) {
            throw new IllegalArgumentException("Negative integers are not allowed");
        }
        if (minValue >= maxValue) {
            throw new IllegalArgumentException(String.format("maxValue (%d) must be larger than minValue (%d)", maxValue, minValue));
        }
        this.minValue = minValue;
        this.maxValue = maxValue;
        graphicsManager.setRange(minValue, maxValue);
    }

    public void setBarValue(int barValue) {
        setBarValue(barValue, true);
    }

    public void setBarValue(int barValue, boolean animate) {
        setBarValue(barValue, 0);
        if(animate && !animationManager.isAnimating()){
            animationManager.animate(this);
        }
    }

    public void setBarWidth(float barWidth) {
        graphicsManager.setBarWidth(barWidth);
    }

    public void setDropletTextColor(int textColor) {
        graphicsManager.getTextPaint().setColor(textColor);
    }

    public void setDropletTextSize(float textSize) {
        graphicsManager.setTextPaintSize(textSize);
    }

    public void setDropletTextStyle(int textStyle) {
        graphicsManager.setTextPaintStyle(textStyle);
    }
    //endregion
    //endregion

    //region helper
    private void getAttributes(AttributeSet set, int defStyleAttr) {
        TypedArray attrs = getContext().obtainStyledAttributes(set, R.styleable.DropletSeekBar, defStyleAttr, 0);

        setBarColor(attrs.getColor(R.styleable.DropletSeekBar_seekBarColor, defaultBarColor));
        setBarBackgroundColor(attrs.getColor(R.styleable.DropletSeekBar_seekBarBackgroundColor, defaultBarBackgroundColor));
        setRange(attrs.getInt(R.styleable.DropletSeekBar_minValue, 0), attrs.getInt(R.styleable.DropletSeekBar_maxValue, 100));
        setBarValue(attrs.getInt(R.styleable.DropletSeekBar_defaultValue, 0), false);
        setBarWidth(attrs.getDimensionPixelSize(R.styleable.DropletSeekBar_seekBarWidth, getDimensionPixelSize(TypedValue.COMPLEX_UNIT_DIP, 7)));
        setDropletTextColor(attrs.getColor(R.styleable.DropletSeekBar_android_textColor, defaultTextColor));
        setDropletTextSize(attrs.getDimensionPixelSize(R.styleable.DropletSeekBar_android_textSize, getDimensionPixelSize(TypedValue.COMPLEX_UNIT_SP, 12)));
        setDropletTextStyle(attrs.getInt(R.styleable.DropletSeekBar_android_textStyle, Typeface.NORMAL));
    }

    private int getDimensionPixelSize(int unit, float value){
        return (int) TypedValue.applyDimension(unit, value, getResources().getDisplayMetrics());
    }

    private void setBarValue(int barValue, int indicatorValue) {
        if (barValue < minValue || barValue > maxValue) {
            throw new IllegalArgumentException(String.format("defaultValue must be in the range of minValue (%d) and maxValue (%d)", minValue, maxValue));
        }
        if(dropletSeekBarListener != null && indicatorValue != 0){
            dropletSeekBarListener.onValueChanged(this.barValue, barValue);
        }
        this.barValue = barValue;
        graphicsManager.setBarValue(barValue, indicatorValue != 0);
        invalidate();
    }

    private boolean isClick(){
        return System.currentTimeMillis() - startTouchDown < TOUCH_DOWN_THRESHOLD;
    }
    //endregion

    //region touch
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDown = true;
                startTouchDown = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                float xDown = event.getX();
                if (isDown && !isClick() && graphicsManager.isInBounds(xDown)) {
                    setBarValue(graphicsManager.getNewBarValue(xDown), 1);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                isDown = false;
                if(isClick()) {
                    if(!animationManager.isAnimating()) {
                        setBarValue(graphicsManager.getNewBarValue(event.getX()), 0);
                        animationManager.animate(this);
                    }
                }
                break;
        }
        return true;
    }
    //endregion

    //region draw
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        graphicsManager.draw(canvas, deltaTransform);
    }
    //endregion

    //region measure
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), (int) graphicsManager.setScreenWidth(getMeasuredWidth()));
    }
    //endregion

    //region interface implementation
    @Override
    public void onAnimationFrame(float progress) {
        deltaTransform = progress;
        invalidate();
    }

    @Override
    public void onAnimationFinished(boolean isFirstRun) {
        if(isFirstRun) {
            graphicsManager.setIndicatorValue(barValue);
        }else if(dropletSeekBarListener != null){
            dropletSeekBarListener.onAnimationFinished();
        }
    }
    //endregion

    //region interface
    public interface DropletSeekBarListener{
        void onAnimationFinished();
        void onValueChanged(int oldValue, int newValue);
    }
    //endregion
}
