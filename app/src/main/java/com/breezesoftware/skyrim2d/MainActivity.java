package com.breezesoftware.skyrim2d;

import android.graphics.Point;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final int FRAME_RATE = 50;
    private static final int PLAYER_SPEED = 4;

    private GameView gameView;
    private ImageButton fireButton;
    private ImageButton upButton;
    private ImageButton downButton;
    private Button playAgainButton;

    private boolean isMovingDown = false;
    private boolean isMovingUp = false;

    public static Point screenSize;

    class GameThread extends Thread {
        private boolean isRunning = false;

        public void setRunning(boolean running) {
            this.isRunning = running;
        }

        @Override
        public void run() {
            while (isRunning) {
                // Update
                if (gameView.isGameOver()) {
                    pauseGame();
                }

                updateMovement();

                gameView.update();
                gameView.postInvalidate();

                try {
                    Thread.sleep(1000 / FRAME_RATE);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    };

    // Game thread
    private GameThread gameThread;


    private void updateMovement() {
        if (isMovingDown) {
            movePlayer(true);
        } else if (isMovingUp) {
            movePlayer(false);
        }
    }

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                switch (v.getId()) {
                    case R.id.upButton:
                        isMovingUp = true;
                        break;
                    case R.id.downButton:
                        isMovingDown = true;
                        break;
                    default:
                        return true;
                }
            }

            if (event.getAction() == MotionEvent.ACTION_UP) {
                switch (v.getId()) {
                    case R.id.upButton:
                        isMovingUp = false;
                        break;
                    case R.id.downButton:
                        isMovingDown = false;
                        break;
                    default:
                        return true;
                }
            }

            return true;
        }
    };

    private void startNewGame() {
        gameView.startGame();
        gameThread = new GameThread();
    }

    private void runGame() {
        gameThread.setRunning(true);
        gameThread.start();
    }

    // Trying to pause game thread
    private void pauseGame() {
        boolean retry = true;
        gameThread.setRunning(false);

        while (retry) {
            try {
                gameThread.join();
                retry = false;
            } catch (InterruptedException e) {
                // retry
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        gameView = this.findViewById(R.id.game_view);
        gameView.setGameOverOverlay((ConstraintLayout) findViewById(R.id.fader));

        upButton = this.findViewById(R.id.upButton);
        downButton = this.findViewById(R.id.downButton);
        playAgainButton = this.findViewById(R.id.againButton);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.upButton) {
                    movePlayer(false);
                } else if (v.getId() == R.id.againButton) {
                    Log.d("mainActivity", "AGAIN");
                    startNewGame();
                    runGame();
                } else {
                    movePlayer(true);
                }
            }
        };

        upButton.setOnTouchListener(touchListener);
        downButton.setOnTouchListener(touchListener);

        playAgainButton.setOnClickListener(listener);

        fireButton = this.findViewById(R.id.fireButton);
        fireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.getPlayer().fire(new Point(300, 150));
            }
        });

        Display display = getWindowManager().getDefaultDisplay();
        screenSize = new Point();
        display.getSize(screenSize);

        gameView.setLevelLabel((TextView) findViewById(R.id.levelLabel));
        gameView.setMonstersLabel((TextView) findViewById(R.id.monstersLabel));

        // Resets all game state
        startNewGame();

        gameView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                runGame();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                // Nothing now
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                pauseGame();
            }
        });
    }

    private void movePlayer(boolean down) {
        int speed = down ? PLAYER_SPEED : -PLAYER_SPEED;

        gameView.elf.goTo(gameView.elf.getX(), gameView.elf.getY() + speed);
    }

}
