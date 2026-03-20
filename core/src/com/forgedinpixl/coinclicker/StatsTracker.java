package com.forgedinpixl.coinclicker;

public class StatsTracker {
    // handling math + history

    private int totalFlips = 0;
    private int  headsCount = 0;
    private int  tailsCount = 0;
    private int currentStreak = 0;
    private int longestStreak = 0;
    private Boolean lastFlipWasHeads = null;

    public void recordFlip(boolean wasHeads){
        // update counts respectively
        totalFlips++;

        if(wasHeads){
            headsCount++;
        } else {
            tailsCount++;
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
}
