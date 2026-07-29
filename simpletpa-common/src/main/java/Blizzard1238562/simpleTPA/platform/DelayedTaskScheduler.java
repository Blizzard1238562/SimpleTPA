package Blizzard1238562.simpleTPA.platform;

public interface DelayedTaskScheduler {

    void runDelayed(Runnable task, long delayTicks);
}
