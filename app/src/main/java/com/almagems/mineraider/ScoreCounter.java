package com.almagems.mineraider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.almagems.mineraider.Constants.*;


public final class ScoreCounter {

    public static Game game;

    private enum MatchType {
        Complex,

        Horizontal3,
        Vertical3,

        Horizontal4,
        Vertical4,

        Horizontal5,
        Vertical5
    }

    private static final int bonusForPerfectSwap = 20; // valid match in both place
    private static final int bonusFor4Match = 3;
    private static final int bonusFor5Match = 5;
    private static final int bonusFor6Match = 7;
    private static final int bonusFor6PlusMatch = 15;
    private static final int bonusForCombo = 1;

    private int score;

    public final ArrayList<ScoreByGemType> scoreByGemTypes;
    public int match3CountHorizontal;
    public int match3CountVertical;

    public int match4CountHorizontal;
    public int match4CountVertical;

    public int match5CountHorizontal;
    public int match5CountVertical;

    // collected vs wasted
    public int collectedGems;
    public int wastedGems;

    // extras
    public int hintCounter;
    public int perfectSwapCounter;
    public int highestComboCounter;
    public int sharedMatchesCounter;

    // ctor
    public ScoreCounter() {
        ScoreByGemType score;
        scoreByGemTypes = new ArrayList<ScoreByGemType>(MAX_GEM_TYPES);

        for(int i = 0; i < MAX_GEM_TYPES; ++i) {
            score = new ScoreByGemType();
            score.type = i;
            score.value = 0;

            switch (i) {
                case 0: score.color = new Color(188, 38, 38, 255); break;
                case 1: score.color = new Color(197, 94, 124, 255); break;
                case 2: score.color = new Color(194, 150, 76, 255); break;
                case 3: score.color = new Color(26, 61, 186, 255); break;
                case 4: score.color = new Color(193, 102, 193, 255); break;
                case 5: score.color = new Color(196, 123, 99, 255); break;
                case 6: score.color = new Color(172, 152, 158, 255); break;
            }

            scoreByGemTypes.add(score);
        }
        reset();
    }

    public void handleGemsFromCart(int number) {
        score += number;
        game.hud.showBonusCartGems(number);
        game.hud.updateScore(score);
        collectedGems += number;
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

    public ArrayList<ScoreByGemType> getScoreByGemTypesAsIntArray() {
        sortScoresByGemTypes();
        return scoreByGemTypes;
    }

    public void reset() {
        score = 0;

        match3CountHorizontal = 0;
        match3CountVertical = 0;

        match4CountHorizontal = 0;
        match4CountVertical = 0;

        match5CountHorizontal = 0;
        match5CountVertical = 0;

        hintCounter = 0;
        perfectSwapCounter = 0;
        highestComboCounter = 0;
        sharedMatchesCounter = 0;

        collectedGems = 0;
        wastedGems = 0;
    }

    public ScoreByGemType getScoreByGemType(int type) {
        ScoreByGemType scoreByGemType;
        for(int i = 0; i < MAX_GEM_TYPES; ++i) {
            scoreByGemType = scoreByGemTypes.get(i);
            if (scoreByGemType.type == type) {
                return scoreByGemType;
            }
        }
        return null;
    }

    private boolean isVertical(ArrayList<GemPosition> list) {
        int x = list.get(0).boardX;

        for (int i = 1; i < list.size(); ++i) {
            if (x != list.get(i).boardX) {
                return false;
            }
        }

        return true;
    }

    private boolean isHorizontal(ArrayList<GemPosition> list) {
        int y = list.get(0).boardY;

        for (int i = 1; i < list.size(); ++i) {
            if (y != list.get(i).boardY) {
                return false;
            }
        }

        return true;
    }

    private MatchType determinaMatchType(ArrayList<GemPosition> list) {
        switch (list.size()) {
            case 3:
                if (isHorizontal(list)) {
                    return MatchType.Horizontal3;
                } else if (isVertical(list)) {
                    return MatchType.Vertical3;
                }
            break;

            case 4:
                if (isHorizontal(list)) {
                    return MatchType.Horizontal4;
                } else if (isVertical(list)) {
                    return MatchType.Vertical4;
                }
                break;

            case 5:
                if (isHorizontal(list)) {
                    return MatchType.Horizontal5;
                } else if (isVertical(list)) {
                    return MatchType.Vertical5;
                }
                break;
        }
        return MatchType.Complex;
    }

    public void addScoreCombo(PopAnimation anim) {
        score += anim.count();
    }

    public void addScore(PopAnimation anim) {
        score += anim.count();
        calcBonusForScore(anim);

        ScoreByGemType scoreByGemType;
        GemPosition gp;
        int size = anim.count();
        for (int i = 0; i < size; ++i) {
            gp = anim.getAt(i);
            scoreByGemType = getScoreByGemType(gp.type);
            ++scoreByGemType.value;
        }
        dumpScoreByGemTypes();

        calcMatchTypes(anim.list);
    }

    private void calcMatchTypes(ArrayList<GemPosition> list) {
        MatchType matchType = determinaMatchType(list);
        switch (matchType) {
            case Horizontal3:
                game.hud.showMessage("MATCH3 HORIZONTAL");
                ++match3CountHorizontal;
                break;

            case Vertical3:
                game.hud.showMessage("MATCH3 VERTICAL");
                ++match3CountVertical;
                break;

            case Horizontal4:
                game.hud.showMessage("MATCH4 HORIZONTAL");
                ++match4CountHorizontal;
                break;

            case Vertical4:
                game.hud.showMessage("MATCH4 VERTICAL");
                ++match4CountVertical;
                break;

            case Horizontal5:
                game.hud.showMessage("MATCH5 HORIZONTAL");
                ++match5CountHorizontal;
                break;

            case Vertical5:
                game.hud.showMessage("MATCH5 VERTICAL");
                ++match5CountVertical;
                break;

            case Complex:
                game.hud.showMessage("MATCH COMPLEX");
                handleComplexMatch(list);
                break;
        }
    }

    private void handleComplexMatch(ArrayList<GemPosition> list) {

        if (list.size() == 6) {
            ArrayList<GemPosition> listA = new ArrayList<GemPosition>(12);
            ArrayList<GemPosition> listB = new ArrayList<GemPosition>(12);

            GemPosition gp = list.get(0);
            listA.add(gp);
            for(int i = 1; i < list.size(); ++i) {
                gp = list.get(i);
                if (gp.type == listA.get(0).type) {
                    listA.add(gp);
                } else {
                    listB.add(gp);
                }
            }

            if (listA.size() > 0 && listB.size() > 0) {
                calcMatchTypes(listA);
                calcMatchTypes(listB);
            }
        }
    }

    public void addBonusForCombo() {
        score += bonusForCombo;
        int comboCounter = game.hud.showCombo();

        if (comboCounter > highestComboCounter) {
            highestComboCounter  = comboCounter;
        }
    }

    public void addBonusForPerfectSwap() {
        score += bonusForPerfectSwap;
        ++perfectSwapCounter;
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
        ScoreByGemType scoreByGemType;
        for (int i = 0; i < arr.length; ++i) {
            scoreByGemType = scoreByGemTypes.get(i);
            scoreByGemType.value = arr[i];
        }
    }

    public int getSharedMatchCount() {
        return sharedMatchesCounter;
    }

    public int getHighestComboCount() {
        return highestComboCounter;
    }

    public int getPerfectSwapCount() {
        return perfectSwapCounter;
    }

    public int getHintCount() {
        return hintCounter;
    }

    public int getCollectedCount() {
        return collectedGems;
    }

    public int getWastedCount() {
        return wastedGems;
    }

    @Override
    public String toString() {
        return "Score is: [" + this.score + "]";
    }

}
