package com.almagems.mineraider;

import static com.almagems.mineraider.Constants.*;

import com.almagems.mineraider.anims.PopAnimation;

public class ScoreCounter {

    private static final int bonusForPerfectSwap = 20; // valid match in both place
    private static final int bonusFor4Match = 3;
    private static final int bonusFor5Match = 5;
    private static final int bonusFor6Match = 7;
    private static final int bonusFor6PlusMatch = 15;
    private static final int bonusForCombo = 1;

    private int score;

    public int[] scoreByGemTypes = new int[MAX_GEM_TYPES];

    // ctor
    public ScoreCounter() {
        reset();
    }

    public void handleGemsFromCart(int[] gemTypes) {
        int numberOfGems = 0;
        for (int i = 0; i < MAX_GEM_TYPES; ++i) {
            numberOfGems += gemTypes[i];
        }
        ClassicSingleton.getInstance().sendGemsFromCartNotification(numberOfGems);
        score += numberOfGems;
        ClassicSingleton.getInstance().hud.updateScore(score);
    }

    public void reset() {
        score = 0;
    }

    public void addScore(PopAnimation anim) {
        System.out.println("Add Score is:" + anim.count());
        score += anim.count(); // * 3;
        calcBonusForScore(anim.count());

        GemPosition gp;
        int size = anim.count();
        for(int i = 0; i < size; ++i) {
            gp = anim.getAt(i);
            ++scoreByGemTypes[ gp.type ];
        }
        dumpScoreByGemTypes();
    }

    public void addBonusForCombo() {
        score += bonusForCombo;
        ClassicSingleton.getInstance().sendComboNotification();
    }

    public void addBonusForPerfectSwap() {
        score += bonusForPerfectSwap;
        ClassicSingleton.getInstance().sendPerfectSwapNotification();
    }

    private void calcBonusForScore(int count) {
        System.out.println("calcBonusForScore... [" + score + "]");
        switch (count) {
            case 3:
                // no bonus for plain match3
                System.out.println("Plain match3, you can do better!");
                break;

            case 4:
                System.out.println("Great match4, let's do more like this!");
                score += bonusFor4Match;
                break;

            case 5:
                System.out.println("Excellent match5, getting professional!");
                score += bonusFor5Match;
                break;

            case 6:
                System.out.println("Buster match6, this is something!");
                score += bonusFor6Match;
                break;

            default: // more than 6 match?
                if (count > 6) {
                    System.out.println("Unbelievable match" + count + ", this is also some kind of Uber match, right?!");
                    score += bonusFor6PlusMatch;
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


    public void dumpScore() {
        System.out.println(this.toString());
    }

    public void dumpScoreByGemTypes() {
        System.out.println("Score by Gem Types");
        int size = scoreByGemTypes.length;
        for (int i = 0; i < size; ++i) {
            System.out.println("GemType" + i + ": " + scoreByGemTypes[i]);
        }
        System.out.println("-------------");
    }

}
