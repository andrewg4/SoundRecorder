package ua.com.prologistic.soundrecorder;

import android.os.Handler;
import android.widget.TextView;

/**
 * Created by Andrew on 09.06.2015.
 */
public class StopWatch implements Runnable {
    public enum TimerState {STOPPED, PAUSED, RUNNING};

    private TextView widget;
    private long updateInterval;
    private long time;
    private long startTime;
    private TimerState state;
    private Handler handler;

    public StopWatch(TextView widget, long updateInterval) {
        this.widget = widget;
        this.updateInterval = updateInterval;
        time = 0;
        startTime = 0;
        state = TimerState.STOPPED;

        handler = new Handler();
    }

    @Override
    public void run() {
        time = System.currentTimeMillis();
        long millis = time - startTime;
        long seconds = (long) (millis / 1000);

        widget.setText(String.format("%02d:%02d.%03d", seconds / 60, seconds % 60, millis % 1000));

        if (state == TimerState.RUNNING) {
            handler.postDelayed(this, updateInterval);
        }
    }

    public void start() {
        startTime = time = System.currentTimeMillis();
        state = TimerState.RUNNING;

        handler.post(this);
    }

    public void reset() {
        start();
    }

    public void pause() {
        handler.removeCallbacks(this);

        state = TimerState.PAUSED;
    }

    public void resume() {
        state = TimerState.RUNNING;

        startTime = System.currentTimeMillis() - (time - startTime);

        handler.post(this);
    }

    public void stop() {
        handler.removeCallbacks(this);

        time = 0;
        startTime = 0;
        state = TimerState.STOPPED;

        widget.setText("00:00.000");
    }

    public long getUpdateInterval() {
        return updateInterval;
    }

    public void setUpdateInterval(long updateInterval) {
        this.updateInterval = updateInterval;
    }

    public TimerState getState() {
        return state;
    }
}
