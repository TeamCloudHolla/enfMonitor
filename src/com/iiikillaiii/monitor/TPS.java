package com.iiikillaiii.monitor;

public class TPS implements Runnable
{
    public static int TICK_COUNT;
    public static long[] TICKS;
    public static long LAST_TICK;
    
    static {
        TPS.TICK_COUNT = 0;
        TPS.TICKS = new long[600];
        TPS.LAST_TICK = 0L;
    }
    
    public static double getTPS() {
        return getTPS(100);
    }
    
    public static double getTPS(final int ticks) {
        try {
            if (TPS.TICK_COUNT < ticks) {
                return 20.0;
            }
            final int target = (TPS.TICK_COUNT - 1 - ticks) % TPS.TICKS.length;
            final long elapsed = System.currentTimeMillis() - TPS.TICKS[target];
            return ticks / (elapsed / 1000.0);
        }
        catch (Exception ex) {
            return 20.0;
        }
    }
    
    public static long getElapsed(final int tickID) {
        final int length = TPS.TICKS.length;
        final long time = TPS.TICKS[tickID % TPS.TICKS.length];
        return System.currentTimeMillis() - time;
    }
    
    @Override
    public void run() {
        TPS.TICKS[TPS.TICK_COUNT % TPS.TICKS.length] = System.currentTimeMillis();
        ++TPS.TICK_COUNT;
    }
}
