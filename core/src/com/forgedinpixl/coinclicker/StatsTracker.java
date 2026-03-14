package com.forgedinpixl.coinclicker;

public class StatsTracker {
    // handling math + history

    private int totalFlips = 0;
    private int  headsCount = 0;
    private int  tailsCount = 0;
    private int currentStreak = 0;
    private int longestStreak = 0;
    private int recentHistory = 0;
    private double headsPercentage = 0;

    public int getTotalFlips() {
        return totalFlips;
    }

    public void setTotalFlips(int totalFlips) {
        this.totalFlips = totalFlips;
    }

    public int getHeadsCount() {
        return headsCount;
    }

    public void setHeadsCount(int headsCount) {
        this.headsCount = headsCount;
    }

    public int getTailsCount() {
        return tailsCount;
    }

    public void setTailsCount(int tailsCount) {
        this.tailsCount = tailsCount;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    public int getLongestStreak() {
        return longestStreak;
    }

    public void setLongestStreak(int longestStreak) {
        this.longestStreak = longestStreak;
    }

    public int getRecentHistory() {
        return recentHistory;
    }

    public void setRecentHistory(int recentHistory) {
        this.recentHistory = recentHistory;
    }

    public double getHeadsPercentage() {
        return headsPercentage;
    }

    public void setHeadsPercentage(double headsPercentage) {
        this.headsPercentage = headsPercentage;
    }
}
