package com.breezesoftware.skyrim2d;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.breezesoftware.skyrim2d.entity.Actor;
import com.breezesoftware.skyrim2d.entity.Enemy;

import java.util.List;

/**
 * This file is part of Test Kotlin Application
 * <p>
 * You can do everything with the code and files
 * <p>
 * Created by popof on 21.08.2018.
 */
public class GameView extends SurfaceView {
    public Actor elf;
    public Actor arrow;
    public List<Enemy> enemies;
    private static final int ARROW_SPEED = 20;
    private static final int ARROW_FIRE_DELAY = 400;

    private int enemyHalfHeight;
    private int enemyHalfWidth;

    public boolean isArrowFired = false;
    public boolean canFire = true;
    private boolean isGameOver = false;

    private ConstraintLayout gameOverOverlay;
    private TextView levelLabel;
    private TextView monstersLabel;

    private int killCount;

    private LevelManager levelManager;

    private int canvasWidth = 0;
    private int canvasHeight = 0;

    private Handler arrowHandler = new Handler();

    public void updateView() {
        if (this.levelLabel != null) {
            this.levelLabel.setText(String.format("Level %d", levelManager.getCurrentLevel()));
        }

        if (this.monstersLabel != null) {
            this.monstersLabel.setText(String.format("Monsters: %d", levelManager.getCurrentLevelMonsterCount()));
        }
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setMonstersLabel(TextView monstersLabel) {
        this.monstersLabel = monstersLabel;
    }

    public void setLevelLabel(TextView levelLabel) {
        this.levelLabel = levelLabel;
    }

    public void setGameOverOverlay(ConstraintLayout gameOverOverlay) {
        this.gameOverOverlay = gameOverOverlay;
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        BitmapDrawable enemyBmp = (BitmapDrawable) context.getResources().getDrawable(R.drawable.monster);
        this.enemyHalfWidth = enemyBmp.getBitmap().getWidth() / 2;
        this.enemyHalfHeight = enemyBmp.getBitmap().getHeight() / 2;

        this.initSounds();

        levelManager = new LevelManager(context);
    }

    public void initSounds() {
        // BAD API HACK
        // TODO: Rework this with a factory
        Enemy enemy = new Enemy(getContext(), 0, 0, "Monster", R.drawable.monster, 0, 0);
        enemy.addDiedSound(R.raw.orc_dead);
        enemy.addHurtSound(R.raw.orc_damaged_1);
        enemy.addHurtSound(R.raw.orc_damaged_2);
    }

    public void startGame() {
        this.levelManager.setLevel(0);
        this.spawnPlayer();
        resetGame();
    }

    public void spawnEnemies() {
        this.enemies = levelManager.getEnemies();
    }

    public void resetGame() {
        Log.d("GameView", "resetGame");
        this.isArrowFired = false;
        this.isGameOver = false;

        if (this.gameOverOverlay != null) {
            this.gameOverOverlay.setVisibility(INVISIBLE);
        }

        this.killCount = 0;

        this.spawnEnemies();
    }

    public void update() {
        Log.d("GameView", "update");
        if (isGameOver) {
            return;
        }

        updateArrow();
        checkGameOver();
    }

    private void updateArrow() {
        if (this.isArrowFired) {
            arrow.goTo(arrow.getX() + ARROW_SPEED, arrow.getY());

            // Spawn arrow on bow
            if (arrow.getX() > canvasWidth) {
                this.spawnArrow(this.arrow.getContext());
                this.isArrowFired = false;
            }

            this.checkHit();
        }
    }

    private void checkGameOver() {
        if (killCount == levelManager.getCurrentLevelMonsterCount()) {
            levelManager.nextLevel();

            if (levelManager.isLastLevel()) {
                gameOver();
            } else {
                this.resetGame();
            }
        }
    }

    public void updateEnemies(Canvas canvas) {
        for (Enemy enemy : this.enemies) {
            enemy.move();

            if (enemy.getX() < elf.getX()) {
                this.gameOver();
            }

            enemy.draw(canvas);
        }
    }

    private void gameOver() {
        this.isGameOver = true;
        if (this.gameOverOverlay != null) {
            this.gameOverOverlay.setVisibility(View.VISIBLE);
        }
    }

    public void spawnPlayer() {
        this.elf = new Actor(getContext(), 150, 150, "Player", R.drawable.archer);
        this.spawnArrow(getContext());
    }

    public void spawnArrow(Context context) {
        if (this.arrow != null) {
            this.arrow.goTo(this.elf.getX() + 35, this.elf.getY() + 46);
            return;
        }

        this.arrow = new Actor(context, this.elf.getX() + 35, this.elf.getY() + 46, "Arrow", R.drawable.arrow);
    }

    // This function is not for you and not for alike
    private boolean checkEnemyHit(Enemy enemy) {
        return !enemy.isDead() && Math.abs(enemy.getY() + enemyHalfHeight - arrow.getY()) < enemyHalfHeight + 5 &&
                Math.abs(enemy.getX() + enemyHalfWidth - arrow.getX()) < enemyHalfWidth + 5;

    }

    private void checkHit() {
        for (Enemy enemy : this.enemies) {
            if (this.checkEnemyHit(enemy)) {
                enemy.hurt(1);

                if (enemy.getHealth() <= 0) {
                    enemy.setDead(true);
                    this.killCount++;
                }

                this.isArrowFired = false;
                this.spawnArrow(this.arrow.getContext());
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("GameView", "onDraw");

        updateView();

        if (canvasWidth == 0) {
            canvasWidth = canvas.getWidth();
        }

        if (canvasHeight == 0) {
            canvasHeight = canvas.getHeight();
        }

        this.updateEnemies(canvas);

        this.elf.draw(canvas);
        this.arrow.draw(canvas);
    }

    public void fireArrow() {
        if (!canFire) {
            return;
        }

        canFire = false;
        arrowHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                canFire = true;
            }
        }, ARROW_FIRE_DELAY);

        isArrowFired = true;
    }
}
