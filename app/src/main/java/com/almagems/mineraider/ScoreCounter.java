package com.almagems.mineraider;

public class ScoreCounter {

    private static final int bonusForPerfectSwap = 20; // valid match in both place
    private static final int bonusFor4Match = 10;
    private static final int bonusFor5Match = 13;
    private static final int bonusFor6Match = 16;
    private static final int bonusFor6PlusMatch = 20;
    private static final int bonusForCombo = 1;

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
        System.out.println("Add Score is:" + count + (combo ? "combo" : ""));
        //int multiplier = combo ? 1 : 2;
        score += count; // * multiplier;
        if (combo)
            score += bonusForCombo;

//        if (!combo) {
//            calcBonusForScore(count);
//        }
    }

    public void addBonusForPerfectSwap() {
        //score += bonusForPerfectSwap;
        //bonus += bonusForPerfectSwap;
    }

    private void calcBonusForScore(int count) {
        System.out.println("calcBonusForScore... [" + score + "]");
        switch (count) {
            case 3:
                // no bonus for plain match3
                break;

            case 4:
                score += bonusFor4Match;
                bonus += bonusFor4Match;
                break;

            case 5:
                score += bonusFor5Match;
                bonus += bonusFor5Match;
                break;

            case 6:
                score += bonusFor6Match;
                bonus += bonusFor6Match;
                break;

            default: // more than 6 match?
                if (count > 6) {
                    score += bonusFor6PlusMatch;
                    bonus += bonusFor6PlusMatch;
                } else {
                    //System.out.println("unexpected behaviour in addScore calc bonus");
                    throw new RuntimeException("Unexpected behaviour in addScore calc bonus");
                }
                break;
        }
    }

    public String toString() {
        return "Score is: [" + this.score + "]";
    }

    public int getScore() {
        return score;
    }
    public void setScore(int score) { this.score = score; }

    public void dump() {
        System.out.println(this.toString());
    }
}
