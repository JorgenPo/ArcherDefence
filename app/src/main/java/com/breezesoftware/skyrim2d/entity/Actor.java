package com.breezesoftware.skyrim2d.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import com.breezesoftware.skyrim2d.Composite;
import com.breezesoftware.skyrim2d.util.BitmapUtil;
import com.breezesoftware.skyrim2d.util.VectorUtil;

/**
 * This file is part of Test Kotlin Application
 * <p>
 * You can do everything with the code and files
 * <p>
 * Created by popof on 21.08.2018.
 */
public class Actor extends Composite<Actor> {
    private static float ACTOR_SCALE = 0.8f;

    protected Context context;

    protected float x;
    protected float y;

    protected String name;

    protected int costume;
    protected int currentCostume;

    protected Bitmap[] graphic = new Bitmap[10];

    public Actor(Context context, float xPos, float yPos, String name, int outfit) {
        this.x = xPos;
        this.y = yPos;
        this.context = context;
        this.name = name;
        this.costume = outfit;
        this.currentCostume = 0;

        if (outfit != 0) {
            BitmapDrawable drawable = (BitmapDrawable) this.context.getResources().getDrawable(costume);
            graphic[0] = BitmapUtil.getBitmapWithTransparentBG(drawable.getBitmap(), Color.WHITE);
            graphic[0] = Bitmap.createScaledBitmap(
                    graphic[0],
                    (int) (graphic[0].getWidth() * ACTOR_SCALE),
                    (int) (graphic[0].getHeight() * ACTOR_SCALE),
                    false);
        }
    }

    public void goTo(float posX, float posY) {
        this.x = posX;
        this.y = posY;
    }

    public void setCostume(int costume, int index) {
        BitmapDrawable drawable = (BitmapDrawable) this.context.getResources().getDrawable(costume);
        graphic[index] = BitmapUtil.getBitmapWithTransparentBG(drawable.getBitmap(), Color.WHITE);
        graphic[index] = Bitmap.createScaledBitmap(
                graphic[index],
                (int) (graphic[index].getWidth() * ACTOR_SCALE),
                (int) (graphic[index].getHeight() * ACTOR_SCALE),
                false);
    }

    public void setCurrentCostume(int index) {
        currentCostume = index;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public String getName() {
        return name;
    }

    public int getCostume() {
        return costume;
    }

    public void setScale(float scale) {
        for (Bitmap bitmap : graphic) {
            bitmap.setWidth(bitmap.getWidth() * (int) scale);
        }
    }

    public Bitmap getBitmap() {
        return this.graphic[currentCostume];
    }

    public Bitmap getBitmapAtIndex(int index) {
        return this.graphic[index];
    }

    public Context getContext() {
        return context;
    }

    public void update() {
        for (int i = 0; i < this.getChildren().size(); i++) {
            Actor actor = this.getChildren().get(i);
            actor.update();

            if (actor.isCanDelete()) {
                Log.d("SActor", "Removed child " + actor.getName());
                this.removeChild(actor);
            }
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(this.getBitmap(), this.getX(), this.getY(), null);

        // Translate canvas to make origin on parent Actor position
        canvas.translate(this.x, this.y);
        for (Actor actor : this.getChildren()) {
            actor.draw(canvas);
        }
        canvas.translate(-this.x, -this.y);
    }

    public Bitmap getCurrentCostume() {
        return this.graphic[this.currentCostume];
    }

    public PointF getPosition() {
        return new PointF(this.x, this.y);
    }

    public RectF getBoundingRect() {
        Bitmap bitmap = this.getBitmap();
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        return new RectF(this.x, this.y, this.x + w, this.y + h);
    }

    public boolean intersectsWith(Actor another) {
        return getBoundingRect().intersect(another.getBoundingRect());
    }
}
