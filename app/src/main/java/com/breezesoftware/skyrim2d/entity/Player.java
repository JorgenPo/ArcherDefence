package com.breezesoftware.skyrim2d.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;

import com.breezesoftware.skyrim2d.R;

/**
 * This file is part of Test Kotlin Application
 * <p>
 * You can do everything with the code and files
 * <p>
 * Created by popof on 31.08.2018.
 */
public class Player extends Actor {
    private static final int PLAYER_DRAWABLE = R.drawable.archer;

    public Player(Context context, Point position) {
        super(context, position.x, position.y, "Player", PLAYER_DRAWABLE);

        Bitmap playerDrawable = this.getBitmap();
        int w = playerDrawable.getWidth();
        int h = playerDrawable.getHeight();

        Point arrowOffset = new Point(w / 2, (int) (h / 2.75f));

        // Create an arrow on bow
        Arrow arrow = new Arrow(context, arrowOffset , this.getPosition());
        // No moving, just on bow arrow
        arrow.setStatic(true);

        this.addChild(arrow);
    }

    /**
     * Fire an arrow to the destination point
     *
     * @param destination Arrow destination point
     */
    public void fire(Point destination) {

    }
}
