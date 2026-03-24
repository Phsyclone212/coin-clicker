package com.forgedinpixl.coinclicker;

import java.util.ArrayDeque;

public class StatsTracker {
    // handling math + history

    private int totalFlips = 0;
    private int  headsCount = 0;
    private int  tailsCount = 0;
    private int currentStreak = 0;
    private int longestStreak = 0;

    private String longestStreakSide = "";
    private Boolean lastFlipWasHeads = null;

    private ArrayDeque<String> flipHistory = new ArrayDeque<>();

    public void recordFlip(boolean wasHeads){
        // update counts respectively
        totalFlips++;

        if(wasHeads){
            headsCount++;
        } else {
            tailsCount++;
        }
        flipHistory.addFirst(wasHeads ? "H" : "T");
        if(flipHistory.size() > 10){
            flipHistory.removeLast();
        }

        //streak logic
        if(lastFlipWasHeads == null){
            currentStreak = 1;
        } else if(lastFlipWasHeads == wasHeads){
            currentStreak++;
        } else {
            currentStreak = 1;
        }

        if(currentStreak > longestStreak){
            longestStreak = currentStreak;
            longestStreakSide = wasHeads ? "Heads" : "Tails";
        }

        lastFlipWasHeads = wasHeads;
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

    public String getLongestStreakSide() {
        return longestStreakSide;
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

    public String getSide(){
            if(lastFlipWasHeads == null){
                return "";
            }
            if (lastFlipWasHeads) {
                return "Heads.";
            } else {
                return "Tails.";
            }
    }

    public double getOddsNum(){
        return Math.pow(2, longestStreak);
    }

    public double getOddsPercent(){
        double probability = Math.pow(0.5,longestStreak); // raw num
        return (double) probability * 100; // percentage ver of num
    }

    public String getHistoryText(){
        StringBuilder sb = new StringBuilder("Recent Flips: ");

        for(String flip : flipHistory){
            sb.append(flip);
        }

        return sb.toString().trim();
    }
}
