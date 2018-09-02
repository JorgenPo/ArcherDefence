package com.breezesoftware.skyrim2d.entity;

import android.content.Context;
import android.graphics.Canvas;
import android.media.MediaPlayer;

import com.breezesoftware.skyrim2d.HealthBar;
import com.breezesoftware.skyrim2d.util.SoundUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * This file is part of Test Kotlin Application
 * <p>
 * You can do everything with the code and files
 * <p>
 * Created by popof on 21.08.2018.
 */
public class Enemy extends Actor {
    private int speed;
    private int maxHealth;
    private int health;
    private boolean isDead;

    private Random rand = new Random(new Date().getTime());

    private static List<MediaPlayer> diedSounds = new ArrayList<>();
    private static List<MediaPlayer> hurtSounds = new ArrayList<>();

    public Enemy(Context context, int xPos, int yPos, String name, int outfit, int speed, int maxHealth) {
        super(context, xPos, yPos, name, outfit);

        this.speed = speed;
        this.maxHealth = maxHealth;
        this.health = maxHealth;

        HealthBar healthBar = new HealthBar(context, this, 10);
        this.addChild(healthBar);
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
        this.setCurrentCostume(dead ? 1 : 0);
        this.playDiedSound();
    }

    public void hurt(int damage) {
        this.health -= damage;
        this.playHurtSound();

        if (this.health <= 0) {
            this.setDead(true);
        }


    }

    private void playDiedSound() {
        SoundUtil.playRandomSound(diedSounds);
    }

    private void playHurtSound() {
        SoundUtil.playRandomSound(hurtSounds);
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void move() {
        if (!this.isDead) {
            this.goTo(this.getX() - this.speed, this.getY());
        }
    }

    public void addDiedSound(int resId) {
        diedSounds.add(MediaPlayer.create(getContext(), resId));
    }

    public void addHurtSound(int resId) {
        hurtSounds.add(MediaPlayer.create(getContext(), resId));
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getHealth() {
        return health;
    }

    @Override
    public boolean intersectsWith(Actor another) {
        return !this.isDead && super.intersectsWith(another);
    }
}
