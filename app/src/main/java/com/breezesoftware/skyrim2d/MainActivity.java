package com.breezesoftware.skyrim2d;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
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
    public static Point SCREEN_SIZE;

    private GameView gameView;
    private Button playAgainButton;

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
    public boolean onTouchEvent(MotionEvent event) {
        gameView.getPlayer().fire(new PointF(event.getX(), event.getY()));
        return super.onTouchEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        gameView = this.findViewById(R.id.game_view);
        gameView.setGameOverOverlay((ConstraintLayout) findViewById(R.id.fader));

        playAgainButton = this.findViewById(R.id.againButton);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.againButton) {
                    Log.d("mainActivity", "AGAIN");
                    startNewGame();
                    runGame();
                }
            }
        };

        playAgainButton.setOnClickListener(listener);

        Display display = getWindowManager().getDefaultDisplay();
        SCREEN_SIZE = new Point();
        display.getSize(SCREEN_SIZE);

        gameView.setLevelLabel(findViewById(R.id.levelLabel));
        gameView.setMonstersLabel(findViewById(R.id.monstersLabel));
        gameView.setGoldLabel(findViewById(R.id.goldLabel));

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

}
