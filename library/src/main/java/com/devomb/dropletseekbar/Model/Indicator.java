package com.devomb.dropletseekbar.Model;

import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by Ombrax on 21/09/2015.
 */
public class Indicator {

    //region constant
    private final float textPadding = 4f;
    private final float bottomPadding = 4f;
    private final float minWidth = 40f;
    //endregion

    //region variable
    private float width;
    private float height;
    private float radius;
    private float yOffset;
    private float xOffset;
    private float textWidth;
    private float textXOffset;
    private float textYOffset;
    //endregion

    //region method
    public void recalculate(int maxValue, Paint paint) {
        paint.setTextAlign(Paint.Align.CENTER);

        Rect textBounds = new Rect();
        String text = String.valueOf(maxValue);
        paint.getTextBounds(text, 0, text.length(), textBounds);

        textWidth = textBounds.width();
        width = textWidth + (textPadding * 2);
        if (width < minWidth) {
            width = minWidth;
        }
        textXOffset = width / 2;
        textYOffset = textXOffset + (textBounds.height() / 2);
        height = width * 1.5f;
        radius = width / 2;
        yOffset = radius * 1.5f;
        xOffset = (width - (radius * (float) Math.sqrt(3.0f))) / 2;
    }
    //endregion

    //region getter
    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getRadius() {
        return radius;
    }

    public float getYOffset() {
        return yOffset;
    }

    public float getXOffset() {
        return xOffset;
    }

    public float getTextWidth() {
        return textWidth;
    }

    public float getTextXOffset() {
        return textXOffset;
    }

    public float getTextYOffset() {
        return textYOffset;
    }

    public float getBottomPadding() {
        return bottomPadding;
    }
    //endregion
}
