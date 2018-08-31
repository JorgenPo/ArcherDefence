package com.breezesoftware.skyrim2d.util;

import android.graphics.Point;

/**
 * This file is part of Test Kotlin Application
 * <p>
 * You can do everything with the code and files
 * <p>
 * Created by popof on 31.08.2018.
 */
public class VectorUtil {
    public static int getDistanceBetween(Point p1, Point p2) {
        int x1 = p1.x;
        int y1 = p1.y;
        int x2 = p2.x;
        int y2 = p2.y;

        return (int) Math.sqrt(Math.pow(x2 - x1, 2) - Math.pow(y2 - y1, 2));
    }

    public static Point getVectorBetween(Point source, Point dest) {
        return new Point(dest.x - source.x, dest.y - source.y);
    }

    public static int getVectorLength(Point vector) {
        return (int) Math.sqrt(Math.pow(vector.x, 2) + Math.pow(vector.y, 2));
    }

    public static Point normalize(Point vector) {
        int length = getVectorLength(vector);
        return new Point(vector.x / length, vector.y / length);
    }

    public static Point multiplyVector(Point vector, int multiplier) {
        return new Point(vector.x * multiplier, vector.y * multiplier);
    }

    public static Point translateByVector(Point position, Point vector) {
        return new Point(position.x + vector.x, position.y + vector.y);
    }
}
