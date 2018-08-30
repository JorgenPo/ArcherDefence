package com.breezesoftware.skyrim2d;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * This file is part of Test Kotlin Application
 * <p>
 * You can do everything with the code and files
 * <p>
 * Created by popof on 30.08.2018.
 */
public class HealthBar {
    private Actor actor;
    private Rect rect;

    private Paint healthBarEmptyPaint;
    private Paint healthBarFullPaint;

    HealthBar(Actor actor, int height) {
        Bitmap currentBitmap = actor.getCurrentCostume();
        int h = currentBitmap.getHeight();
        int w = currentBitmap.getWidth();

        rect = new Rect(actor.getX() - 5,
                actor.getY() + h + 5,
                actor.getX() + w + 5,
                actor.getY() + h + 5 + height);

        healthBarEmptyPaint = new Paint();
        healthBarEmptyPaint.setColor(Color.RED);

        healthBarFullPaint = new Paint();
        healthBarFullPaint.setColor(Color.GREEN);
    }

    public void draw(Canvas canvas) {
        // Translate health bar after move
        rect.offsetTo(actor.getX() - 5, rect.top);

        canvas.drawRect(rect, this.healthBarEmptyPaint);
        canvas.drawRect(rect, this.healthBarFullPaint);
    }
}
