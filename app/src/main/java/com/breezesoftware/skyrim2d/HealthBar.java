package com.breezesoftware.skyrim2d;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.breezesoftware.skyrim2d.entity.Enemy;

/**
 * This file is part of Test Kotlin Application
 * <p>
 * You can do everything with the code and files
 * <p>
 * Created by popof on 30.08.2018.
 */
public class HealthBar {
    private Enemy actor;
    private Rect fullRect;
    private Rect emptyRect;

    private Paint healthBarEmptyPaint;
    private Paint healthBarFullPaint;

    public HealthBar(Enemy actor, int height) {
        this.actor = actor;

        Bitmap currentBitmap = actor.getCurrentCostume();
        int h = currentBitmap.getHeight();
        int w = currentBitmap.getWidth();

        fullRect = new Rect(actor.getX() - 5,
                actor.getY() + h + 5,
                actor.getX() + w + 5,
                actor.getY() + h + 5 + height);

        emptyRect = new Rect(fullRect);

        healthBarEmptyPaint = new Paint();
        healthBarEmptyPaint.setColor(Color.RED);

        healthBarFullPaint = new Paint();
        healthBarFullPaint.setColor(Color.GREEN);
    }

    public void draw(Canvas canvas) {
        // Translate health bar after move
        fullRect.offsetTo(actor.getX() - 5, fullRect.top);
        emptyRect.offsetTo(actor.getX() - 5, emptyRect.top);

        float healthPercent = this.actor.getHealth() / (float) this.actor.getMaxHealth();
        int healthBarWidth = this.emptyRect.right - this.emptyRect.left;
        healthBarWidth *= healthPercent;

        fullRect.right = fullRect.left + healthBarWidth;

        canvas.drawRect(emptyRect, this.healthBarEmptyPaint);
        canvas.drawRect(fullRect, this.healthBarFullPaint);
    }
}
