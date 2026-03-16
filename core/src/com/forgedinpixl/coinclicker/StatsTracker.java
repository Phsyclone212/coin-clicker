package com.forgedinpixl.coinclicker;

public class StatsTracker {
    // handling math + history

    private int totalFlips = 0;
    private int  headsCount = 0;
    private int  tailsCount = 0;
    private int currentStreak = 0;
    private int longestStreak = 0;
    private int recentHistory = 0;

    public void recordFlip(boolean wasHeads){
        // update counts respectively
        totalFlips++;

        if(wasHeads){
            headsCount++;
        } else {
            tailsCount++;
        }
    }

    public int getTotalFlips() {
        return totalFlips;
    }

    public int getHeadsCount() {
        return headsCount;
    }

    public int getTailsCount() {
        return tailsCount;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public int getLongestStreak() {
        return longestStreak;
    }

    public int getRecentHistory() {
        return recentHistory;
    }

    public double getHeadsPercentage() {
        if (totalFlips == 0){
            return 0;
        }
        return (double) headsCount / totalFlips * 100;
    }

    public double getTailsPercentage() {
        if (totalFlips == 0){
            return 0;
        }
        return (double) tailsCount / totalFlips * 100;
    }
}
