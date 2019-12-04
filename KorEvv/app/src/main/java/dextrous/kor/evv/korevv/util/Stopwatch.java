package dextrous.kor.evv.korevv.util;

import java.util.concurrent.TimeUnit;

import static android.media.MediaExtractor.MetricsConstants.FORMAT;

public class Stopwatch {
    private long startTime = 0;
    private long stopTime = 0;
    private boolean running = false;
    public void start() {
        this.startTime = System.currentTimeMillis();
        this.running = true;
    }
    public void start(long startTime ) {
        this.startTime = startTime;
        this.running = true;
    }
    public void stop() {
        this.stopTime = System.currentTimeMillis();
        this.running = false;
    }
   // elaspsed time in milliseconds
    public long getElapsedTime() {
        if (running) {
            return System.currentTimeMillis() - startTime;
        }
        return stopTime - startTime;
    }
    // elaspsed time in seconds
    public long getElapsedTimeSecs() {
        if (running) {
            return ((System.currentTimeMillis() - startTime) / 1000);
        }
        return ((stopTime - startTime) / 1000);
    }
    public static String parseTime(long milliseconds) {
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliseconds),
                TimeUnit.MILLISECONDS.toMinutes(milliseconds) - TimeUnit.HOURS.toMinutes(
                        TimeUnit.MILLISECONDS.toHours(milliseconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }
}
