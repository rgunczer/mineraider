package com.almagems.mineraider;

public class ScoreCounter {

    private static int bonusForPerfectSwap = 20; // valid match in both place
    private static int bonusForMoreThan3Match = 10;
    //private static int bonusFor

    private int score;
    private int bonus;

    // ctor
    public ScoreCounter() {
        reset();
    }

    public void reset() {
        score = 0;
        bonus = 0;
    }

    public void addScore(int count, boolean combo) { // gems popped
        int multiplier = combo ? 1 : 2;
        score += count * multiplier;
    }

    public void addBonusForPerfectSwap() { // perfect swap is: swap resulting 6 match in both places
        score += bonusForPerfectSwap;
        bonus += bonusForPerfectSwap;
    }

    public void addBonusForMoreThan3Match() {
        score += bonusForMoreThan3Match;
        bonus += bonusForMoreThan3Match;
    }

    public String toString() {
        return "Score is: [" + this.score + "], bonus is: [" + this.bonus + "]";
    }

    public void dump() {
        System.out.println(this.toString());
    }
}
