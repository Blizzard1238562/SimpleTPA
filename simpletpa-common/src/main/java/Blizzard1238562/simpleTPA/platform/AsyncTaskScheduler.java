package Blizzard1238562.simpleTPA.platform;

public interface AsyncTaskScheduler {

    CancellableTask scheduleRepeatingAsync(Runnable task, long periodSeconds);

    void runOnMainContext(Runnable task);
}
