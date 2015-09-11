package com.almagems.mineraider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.almagems.mineraider.Constants.*;


public final class ScoreCounter {

    private static final int bonusForPerfectSwap = 20; // valid match in both place
    private static final int bonusFor4Match = 3;
    private static final int bonusFor5Match = 5;
    private static final int bonusFor6Match = 7;
    private static final int bonusFor6PlusMatch = 15;
    private static final int bonusForCombo = 1;

    private int score;
    private final Game game;
    public final ArrayList<ScoreByGemType> scoreByGemTypes;

    // ctor
    public ScoreCounter(Game game) {
        this.game = game;
        scoreByGemTypes = new ArrayList<ScoreByGemType>(MAX_GEM_TYPES);
        ScoreByGemType score;
        for(int i = 0; i < MAX_GEM_TYPES; ++i) {
            score = new ScoreByGemType();
            score.type = i;
            score.value = 0;

            switch (i) {
                case 0: score.color = new Color(194, 150, 76, 255); break;
                case 1: score.color = new Color(197, 94, 124, 255); break;
                case 2: score.color = new Color(193, 102, 193, 255); break;
                case 3: score.color = new Color(172, 152, 158, 255); break;
                case 4: score.color = new Color(26, 61, 186, 255); break;
                case 5: score.color = new Color(196, 123, 99, 255); break;
                case 6: score.color = new Color(188, 38, 38, 255); break;
            }

            scoreByGemTypes.add(score);
        }
        reset();
    }

    public void handleGemsFromCart(int[] gemTypes) {
        int numberOfGems = 0;
        for (int i = 0; i < MAX_GEM_TYPES; ++i) {
            numberOfGems += gemTypes[i];
        }

        game.hud.showBonusCartGems(numberOfGems);
        score += numberOfGems;
        game.hud.updateScore(score);
    }

    private void sortScoresByGemTypes() {
        Collections.sort(scoreByGemTypes, new Comparator<ScoreByGemType>() {
            @Override
            public int compare(ScoreByGemType lhs, ScoreByGemType rhs) {
                if (lhs.value < rhs.value) {
                    return 1;
                } else if (lhs.value == rhs.value) {
                    return 0;
                } else if (lhs.value > rhs.value) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
    }

    public int[] getScoreByGemTypesAsIntArray() {
        sortScoresByGemTypes();

        int[] arr = new int[MAX_GEM_TYPES];

        ScoreByGemType scoreByGemType;
        for (int i = 0; i < MAX_GEM_TYPES; ++i) {
            scoreByGemType = scoreByGemTypes.get(i);
            arr[i] = scoreByGemType.value;
        }
        return arr;
    }

    public void reset() {
        score = 0;
    }

    public void addScore(PopAnimation anim) {
        System.out.println("Add Score is:" + anim.count());
        score += anim.count(); // * 3;
        calcBonusForScore(anim);

        ScoreByGemType scoreByGemType;
        GemPosition gp;
        int size = anim.count();
        for(int i = 0; i < size; ++i) {
            gp = anim.getAt(i);
            scoreByGemType = scoreByGemTypes.get(gp.type);
            
            if (scoreByGemType.type != gp.type) {
                System.out.println("Wrong Type!!!");
            }
            
            ++scoreByGemType.value;
        }
        dumpScoreByGemTypes();
    }

    public void addBonusForCombo() {
        score += bonusForCombo;
        game.hud.showCombo();
    }

    public void addBonusForPerfectSwap() {
        score += bonusForPerfectSwap;
        game.hud.showPerfectSwap();
    }

    private void calcBonusForScore(PopAnimation anim) {
        int count = anim.count();
        System.out.println("calcBonusForScore... [" + score + "]");
        switch (count) {
            case 3:
                // no bonus for plain match3
                System.out.println("Plain match3, you can do better!");
                break;

            case 4: {
                System.out.println("Great match4, let's do more like this!");

                int x = anim.getAt(0).boardX;
                int y = anim.getAt(0).boardY;

                if ( anim.getAt(1).boardY == y &&
                        anim.getAt(2).boardY == y &&
                        anim.getAt(3).boardY == y ) {
                    game.hud.showMatch4InARowBonus();
                } else if ( anim.getAt(1).boardX == x &&
                     anim.getAt(2).boardX == x &&
                     anim.getAt(3).boardX == x ) {
                    game.hud.showMatch4InAColBonus();
                } else {
                    System.out.println("Something is wrong! Match4 without same column or same row");
                }

                score += bonusFor4Match;
            }
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
        ScoreByGemType scoreByGemType;
        for (int i = 0; i < MAX_GEM_TYPES; ++i) {
            scoreByGemType = scoreByGemTypes.get(i);
            System.out.println("GemType" + scoreByGemType.type + ": " + scoreByGemType.value);
        }
        System.out.println("-------------");
    }

    public void setScoreByGemTypes(int[] arr) {
/*
        ScoreByGemType scoreByGemType;
        for (int i = 0; i < arr.length; ++i) {
            scoreByGemType = scoreByGemTypes.get(i);
            scoreByGemType.value = arr[i];
        }
*/
        ScoreByGemType scoreByGemType;
        int i;

        i = 0;
        scoreByGemType = scoreByGemTypes.get(i);
        scoreByGemType.value = 0;

        i = 1;
        scoreByGemType = scoreByGemTypes.get(i);
        scoreByGemType.value = 0;

        i = 2;
        scoreByGemType = scoreByGemTypes.get(i);
        scoreByGemType.value = 0;

        i = 3;
        scoreByGemType = scoreByGemTypes.get(i);
        scoreByGemType.value = 0;

        i = 4;
        scoreByGemType = scoreByGemTypes.get(i);
        scoreByGemType.value = 0;

        i = 5;
        scoreByGemType = scoreByGemTypes.get(i);
        scoreByGemType.value = 0;

        i = 6;
        scoreByGemType = scoreByGemTypes.get(i);
        scoreByGemType.value = 0;


    }

    public int getComboCount() {
        return 42;
    }

    public int getHintsShownCount() {
        return 12;
    }

    public int getLongestComboNumber() {
        return 90;
    }

    public int getCollectedCount() {
        return 1002;
    }

    public int getWastedCount() {
        return 345;
    }


}
