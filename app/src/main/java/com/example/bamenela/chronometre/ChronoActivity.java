package com.example.bamenela.chronometre;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ChronoActivity extends AppCompatActivity {

    private int seconds = 0; // number of seconds since launched
    private boolean running; //is it running or not?
    private boolean wasRunning;
    private final Handler handler = new Handler();
    private TextView timeView;
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (running) {
                seconds++;
                printTime();
            }
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chrono);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.wasRunning = running;
        this.running = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.running = this.wasRunning;
        this.timeView = (TextView)findViewById(R.id.time_view);
        this.printTime();
        if (running) {
            this.runTimer();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    //Start the stopwatch running when the Start button is clicked.
    public void onClickStart(View view) {
        if (!running) {
            running = true;
            runTimer();
        }
    }
    //Stop the stopwatch running when the Stop button is clicked.
    public void onClickStop(View view) {
        this.handler.removeCallbacks(runnable);
        running = false;
    }
    //Reset the stopwatch when the Reset button is clicked.
    public void onClickReset(View view) {
        running = false;
        seconds = 0;
        this.handler.removeCallbacks(runnable);
        printTime();
    }

    //We need this code to keep looping so that it increments the seconds variable
    // and updates the text view every second. We need to do this in such a way
    // that we don’t block the main Android thread.

    //In non-Android Java programs, you can perform tasks like this using a background thread.
    // In Androidville, this is a problem—only the main Android thread can update the user
    // interface, and if any other thread tries to do so, you get a CalledFromWrongThreadException.
    // The solution is to use a Handler.

    private void runTimer() {
        handler.post(runnable);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("seconds", this.seconds);
        outState.putBoolean("running", this.wasRunning);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.seconds = savedInstanceState.getInt("seconds");
        this.wasRunning = savedInstanceState.getBoolean("running");
    }

    private void printTime() {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;
        String time = String.format("%d:%02d:%02d",
                hours, minutes, secs);
        timeView.setText(time);
    }
}
