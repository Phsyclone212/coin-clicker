package com.forgedinpixl.coinclicker;

import java.util.ArrayDeque;

import com.badlogic.gdx.Preferences;

public class StatsTracker {
    // handling math + history

    private int points = 0;

    private int totalFlips = 0;
    private int  headsCount = 0;
    private int  tailsCount = 0;
    private int currentStreak = 0;
    private int longestStreak = 0;

    private String longestStreakSide = "";
    private Boolean lastFlipWasHeads = null;

    private ArrayDeque<String> flipHistory = new ArrayDeque<>();

    public void recordFlip(boolean wasHeads, boolean animated){
        // update counts respectively
        totalFlips++;

        points += animated ? 2 : 1;

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

    public void saveToPrefs(Preferences prefs) {
        prefs.putInteger("points", points);
        prefs.putInteger("totalFlips", totalFlips);
        prefs.putInteger("headsCount", headsCount);
        prefs.putInteger("tailsCount", tailsCount);
        prefs.putInteger("currentStreak", currentStreak);
        prefs.putInteger("longestStreak", longestStreak);
        prefs.putString("longestStreakSide", longestStreakSide);
        prefs.putBoolean("hasLastFlip", lastFlipWasHeads != null);
        prefs.putBoolean("lastFlipWasHeads", lastFlipWasHeads != null && lastFlipWasHeads);
        prefs.putString("flipHistory", String.join(",", flipHistory));
        prefs.flush(); // libGDX equivalent of apply()
    }

    public void loadFromPrefs(Preferences prefs) {
        points = prefs.getInteger("points", prefs.getInteger("flipDollars", 0));
        totalFlips = prefs.getInteger("totalFlips", 0);
        headsCount = prefs.getInteger("headsCount", 0);
        tailsCount = prefs.getInteger("tailsCount", 0);
        currentStreak = prefs.getInteger("currentStreak", 0);
        longestStreak = prefs.getInteger("longestStreak", 0);
        longestStreakSide = prefs.getString("longestStreakSide", "");

        boolean hasLastFlip = prefs.getBoolean("hasLastFlip", false);
        lastFlipWasHeads = hasLastFlip ? prefs.getBoolean("lastFlipWasHeads", false) : null;

        String history = prefs.getString("flipHistory", "");
        flipHistory.clear();
        if (!history.isEmpty()) {
            for (String entry : history.split(",")) {
                flipHistory.addLast(entry);
            }
        }
    }

    public int getPoints(){
        return points;
    }

    public void spendPoints(int amount) {
        points = Math.max(0, points - amount);
    }
}
