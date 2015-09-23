package com.almagems.mineraider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.almagems.mineraider.Constants.*;


public final class ScoreCounter {

    public static HUD hud;

    private enum MatchType {
        Complex,

        Horizontal3,
        Vertical3,

        Horizontal4,
        Vertical4,

        Horizontal5,
        Vertical5
    }

    // bonus
    private static final int bonusForPerfectSwap = 20;
    private static final int bonusFor4Match = 3;
    private static final int bonusFor5Match = 5;
    private static final int bonusForCombo = 1;

    // score
    public int score;

    // gem types
    public final ArrayList<ScoreByGemType> scoreByGemTypes;    

    // match types
    public int match3CountHorizontal;
    public int match3CountVertical;
    public int match4CountHorizontal;
    public int match4CountVertical;
    public int match5CountHorizontal;
    public int match5CountVertical;

    // extras
    public int sharedMatchesCounter;
    public int highestComboCounter;
    public int perfectSwapCounter;
    public int hintCounter;
    
    // collected vs wasted
    public int collectedGems;
    public int wastedGems;


    // ctor
    public ScoreCounter() {
        ScoreByGemType scoreObj;
        scoreByGemTypes = new ArrayList<ScoreByGemType>(MAX_GEM_TYPES);

        for(int i = 0; i < MAX_GEM_TYPES; ++i) {
            scoreObj = new ScoreByGemType();
            scoreObj.type = i;
            scoreObj.value = 0;

            scoreByGemTypes.add(scoreObj);
        }
        reset();
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
        
        ScoreByGemType scoreByGemType;
        for(int i = 0; i < MAX_GEM_TYPES; ++i) {
            scoreByGemType = scoreByGemTypes.get(i);
            scoreByGemType.value = 0;
        }
    }

    public void addScoreForMatch(PopAnimation anim) {
        score += anim.count();        

        ScoreByGemType scoreObj;
        GemPosition gp;
        int size = anim.count();
        for (int i = 0; i < size; ++i) {
            gp = anim.getAt(i);
            scoreObj = getScoreObjForGemType(gp.type);
            ++scoreObj.value;
        }

        calcMatchTypes(anim.list);
        hud.updateScore(score);
    }

    public void updateHighestComboCount(int comboCount) {
        hud.hideCombo();

        if (comboCount > highestComboCounter) {
            highestComboCounter = comboCount;
        }
    }

    public void addScoreForCombo(PopAnimation anim, int comboCount) {
        score += (anim.count() + bonusForCombo);
        hud.showCombo(comboCount);
    }

    public void addBonusForPerfectSwap() {
        score += bonusForPerfectSwap;
        ++perfectSwapCounter;
        hud.showPerfectSwap();
    }


    public void handleGemsFromCart(int number) {
        score += number;
        hud.showBonusCartGems(number);
        hud.updateScore(score);
        collectedGems += number;
    }

    public ArrayList<ScoreByGemType> getScoreByGemTypesAsIntArray() {
        sortScoresByGemTypes();
        return scoreByGemTypes;
    }

    public ScoreByGemType getScoreObjForGemType(int type) {
        ScoreByGemType scoreObj;
        for(int i = 0; i < MAX_GEM_TYPES; ++i) {
            scoreObj = scoreByGemTypes.get(i);
            if (scoreObj.type == type) {
                return scoreObj;
            }
        }
        return null;
    }

    public void setScoreByGemTypes(int[] arr) {
        ScoreByGemType scoreByGemType;
        for (int i = 0; i < arr.length; ++i) {
            scoreByGemType = scoreByGemTypes.get(i);
            scoreByGemType.value = arr[i];
        }
    }

    public void dump() {
        System.out.println(this.toString());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("\nScore: " + score);
        ScoreByGemType scoreByGemType;
        for (int i = 0; i < MAX_GEM_TYPES; ++i) {
            scoreByGemType = scoreByGemTypes.get(i);
            sb.append("\nGemType" + scoreByGemType.type + ": " + scoreByGemType.value);
        }
        
        sb.append("\nMatch3 Count Horizontal: " + match3CountHorizontal);
        sb.append("\nMatch3 Count Vertical: " + match3CountVertical);

        sb.append("\nMatch4 Count Horizotal: " + match4CountHorizontal);
        sb.append("\nMatch4 Count Vertical: " + match4CountVertical);

        sb.append("\nMatch5 Count Horizontal: " + match5CountHorizontal);
        sb.append("\nMatch5 Count Vertical: " + match5CountVertical);

        sb.append("\nHint Counter: " + hintCounter);
        sb.append("\nPerfect Swap Counter: " + perfectSwapCounter);
        sb.append("\nHighest Combo Counter: " + highestComboCounter);
        sb.append("\nShared Matches Counter: " + sharedMatchesCounter);

        sb.append("\nCollected Gems: " + collectedGems);
        sb.append("\nWasted Gems: " + wastedGems);
        sb.append("\n-------------\n");

        return sb.toString();
    }

// private
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

    private MatchType determineMatchType(ArrayList<GemPosition> list) {
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

    private void calcMatchTypes(ArrayList<GemPosition> list) {
        MatchType matchType = determineMatchType(list);
        switch (matchType) {
            case Horizontal3:
                ++match3CountHorizontal;
                break;

            case Vertical3:
                ++match3CountVertical;
                break;

            case Horizontal4:
                hud.showFourInARow();
                ++match4CountHorizontal;
                break;

            case Vertical4:
                hud.showFourInACol();
                ++match4CountVertical;
                break;

            case Horizontal5:
                hud.showFiveInARow();
                ++match5CountHorizontal;
                break;

            case Vertical5:
                hud.showFiveInACol();
                ++match5CountVertical;
                break;

            case Complex:
                //hud.showMessage("MATCH COMPLEX");
                handleComplexMatch(list);                
                break;
        }
    }

    private void handleComplexMatch(ArrayList<GemPosition> list) {

        if (list.size() == 6) { // after a perfect swap
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

        if (list.size() == 5) {
            ++sharedMatchesCounter;
        }

    }


}
