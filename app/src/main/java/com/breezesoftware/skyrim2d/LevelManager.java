package com.breezesoftware.skyrim2d;

import android.content.Context;

import com.breezesoftware.skyrim2d.entity.Enemy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * This file is part of Test Kotlin Application
 * <p>
 * You can do everything with the code and files
 * <p>
 * Created by popof on 22.08.2018.
 */
public class LevelManager {

    private static final int SPEED_DISPERSION = 4;
    private static final int HEALTH_DISPERSION = 2;
    private static final int LEVEL_COUNT = 6;

    private int currentLevel = 0;
    private int[] levels;

    private Context context;

    public LevelManager(Context context) {
        this.levels = new int[LEVEL_COUNT];

        this.context = context;
        this.initLevels();
    }

    /**
     * Init levels (number of monsters on each)
     */
    private void initLevels() {
        levels[0] = 1;
        levels[1] = 4;
        levels[2] = 8;
        levels[3] = 12;
        levels[4] = 16;
        levels[5] = 20;
    }

    /**
     * Returns enemies for current level
     * @return
     */
    public List<Enemy> getEnemies() {
        return this.spawnEnemies(levels[currentLevel]);
    }

    public int getCurrentLevelMonsterCount() {
        return this.levels[currentLevel];
    }


    private ArrayList<Enemy> spawnEnemies(int count) {
        Random rand = new Random(new Date().getTime());

        ArrayList<Enemy> enemies = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            enemies.add(this.spawnEnemy(rand.nextInt()));
        }

        return enemies;
    }

    private Enemy spawnEnemy(int seed) {
        Random rand = new Random(seed);

        int xOffset = MainActivity.SCREEN_SIZE.x + Math.abs(rand.nextInt() % 200);
        int yOffset = 100 + Math.abs(rand.nextInt()) % (MainActivity.SCREEN_SIZE.y - 300);
        int speed = Math.abs(rand.nextInt()) % SPEED_DISPERSION + 1;
        int health = Math.abs(rand.nextInt(HEALTH_DISPERSION)) + 1;
        int gold = 1;

        Enemy enemy = new Enemy(this.context, xOffset, yOffset, "Monster", R.drawable.monster, speed, health);
        enemy.setGold(gold);
        enemy.setCostume(R.drawable.monster_dead, 1);

        return enemy;
    }

    public int getLevelCount() {
        return this.levels.length;
    }

    public int getCurrentLevel() {
        return this.currentLevel + 1;
    }

    public boolean isLastLevel() {
        return this.currentLevel > this.levels.length - 1;
    }

    public void nextLevel() {
        this.currentLevel++;
    }

    public void setLevel(int level) {
        this.currentLevel = level;
    }
}
