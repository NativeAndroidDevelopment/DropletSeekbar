package com.devomb.dropletseekbar.Graphics;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;

import com.devomb.dropletseekbar.Model.Indicator;

/**
 * Created by Ombrax on 5/10/2015.
 */
public class GraphicsManager {

    //region variable
    private Indicator indicator;
    private int minValue, maxValue, barValue = -1, indicatorValue;
    private float screenWidth, barWidth;
    private Paint barPaint, barBackgroundPaint, textPaint;
    private float textSize;
    private RectF scaleBounds = new RectF();
    private Matrix scaleMatrix = new Matrix();
    //endregion

    //region constructor
    public GraphicsManager() {
        indicator = new Indicator();
        barPaint = newPaint();
        barBackgroundPaint = newPaint();
        textPaint = newPaint();
        textPaint.setTextAlign(Paint.Align.CENTER);
    }
    //endregion

    //region getter
    public Paint getBarPaint() {
        return barPaint;
    }

    public Paint getBarBackgroundPaint() {
        return barBackgroundPaint;
    }

    public Paint getTextPaint() {
        return textPaint;
    }
    //endregion

    //region setter
    public void setRange(int minValue, int maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        recalculate();
    }

    public void setBarValue(int barValue, boolean withIndicator) {
        if (this.barValue == -1 || withIndicator) {
            this.indicatorValue = barValue;
        }
        this.barValue = barValue;
    }

    public void setIndicatorValue(int indicatorValue) {
        this.indicatorValue = indicatorValue;
    }

    public float setScreenWidth(float screenWidth) {
        this.screenWidth = screenWidth;
        return indicator.getHeight() + indicator.getBottomPadding() + barWidth;
    }

    public void setBarWidth(float barWidth) {
        this.barWidth = barWidth;
    }

    public void setTextPaintSize(float textSize) {
        this.textSize = textSize;
        textPaint.setTextSize(textSize);
        recalculate();
    }

    public void setTextPaintStyle(int textStyle) {
        textPaint.setTypeface(Typeface.defaultFromStyle(textStyle));
        recalculate();
    }
    //endregion

    //region method
    public boolean isInBounds(float x){
        float left = getLeftBound(indicatorValue);
        return x > left && x < (left + indicator.getWidth());
    }

    public int getNewBarValue(float x) {
        float progress = (x - indicator.getRadius()) / (screenWidth - indicator.getWidth());
        if (progress > 1f) {
            progress = 1f;
        } else if (progress < 0f) {
            progress = 0f;
        }
        return Math.round(((maxValue - minValue) * progress) + minValue);
    }

    public void draw(Canvas canvas, float deltaTransform) {
        //Draw droplet           /*            Full Track           */
        float dropletLeftBound = getLeftBound(indicatorValue);

        Path path = new Path();
        path.arcTo(new RectF(dropletLeftBound, 0, dropletLeftBound + indicator.getWidth(), indicator.getWidth()), 150, 240);
        path.lineTo(dropletLeftBound + indicator.getWidth() - indicator.getXOffset(), indicator.getYOffset());
        path.lineTo(dropletLeftBound + indicator.getRadius(), indicator.getHeight());
        if (deltaTransform < 1f) {
            path.computeBounds(scaleBounds, true);
            scaleMatrix.setScale(deltaTransform, deltaTransform, scaleBounds.centerX(), scaleBounds.bottom);
            path.transform(scaleMatrix);
        }
        canvas.drawPath(path, barPaint);

        //Draw text
        textPaint.setTextSize(deltaTransform * textSize);
        canvas.drawText(String.valueOf(indicatorValue), dropletLeftBound + indicator.getTextXOffset(), indicator.getTextYOffset() * deltaTransform + (indicator.getHeight() - (deltaTransform * indicator.getHeight())), textPaint);

        //Draw bar
        float barTopBound = indicator.getHeight() + indicator.getBottomPadding();
        float barXOffset = indicator.getRadius();

        float barStartingPoint = dropletLeftBound + barXOffset;
        float barDeltaMove = (getLeftBound(barValue) - dropletLeftBound) * (1 - deltaTransform);

        canvas.drawRect(barXOffset, barTopBound, barStartingPoint + barDeltaMove, barTopBound + barWidth, barPaint);
        canvas.drawRect(barStartingPoint + barDeltaMove, barTopBound, screenWidth - barXOffset, barTopBound + barWidth, barBackgroundPaint);
    }
    //endregion

    //region helper
    private float getLeftBound(float value) {
        return (screenWidth - indicator.getWidth()) * ((value - minValue) / (maxValue - minValue));
    }

    private Paint newPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        return paint;
    }

    private void recalculate() {
        indicator.recalculate(maxValue, textPaint);
    }
    //endregion
}
