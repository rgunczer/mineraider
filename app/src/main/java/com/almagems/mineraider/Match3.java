package com.almagems.mineraider;

import static com.almagems.mineraider.Constants.*;

import com.almagems.mineraider.anims.AnimationManager;
import com.almagems.mineraider.anims.BaseAnimation;
import com.almagems.mineraider.anims.FallAnimation;
import com.almagems.mineraider.anims.FallGroupAnimation;
import com.almagems.mineraider.anims.PopAnimation;
import com.almagems.mineraider.anims.SwapAnimation;
import com.almagems.mineraider.anims.SwapHintManager;
import com.almagems.mineraider.util.MyUtils;

public class Match3 {

    public int boardSize;
	public boolean isAnimating;
    private int[] gemTypesArray = new int[MAX_GEM_TYPES];
	public GemPosition[][] board;
    private GemPosition[][] tempBoard;
	public GemPosition firstSelected;
	public GemPosition secondSelected;
    public final SwapHintManager swapHintManager;
	private AnimationManager animManager;
    private final SwapAnimation swapAnim;
    private final FallGroupAnimation pooledFallGroupAnim;
    private final PopAnimation pooledPopAnimation;
    public final ScoreCounter scoreCounter;

    // ctor
	public Match3(int boardSize, AnimationManager animManager, ScoreCounter scoreCounter) {
        this.boardSize = boardSize;
        isAnimating = true;
        this.animManager = animManager;
        this.scoreCounter = scoreCounter;
        swapHintManager = new SwapHintManager();
        swapAnim = new SwapAnimation();
        pooledFallGroupAnim = new FallGroupAnimation();
        pooledPopAnimation = new PopAnimation();
        createBoards();
    }

    private void createBoards() {
        board = new GemPosition[boardSize][boardSize*2];
        tempBoard = new GemPosition[boardSize][boardSize];

        for (int y = 0; y < boardSize * 2; ++y) {
            for (int x = 0; x < boardSize; ++x) {
                board[x][y] = new GemPosition(x, y);
            }
        }

        for (int x = 0; x < boardSize; ++x) {
            for (int y = 0; y < boardSize; ++y) {
                tempBoard[x][y] = new GemPosition(x, y);
            }
        }
    }

	private void placeTestGems() {		
		board[0][2].type = GEM_TYPE_2;
		board[1][2].type = GEM_TYPE_1;
		board[2][2].type = GEM_TYPE_1;
		
		board[0][1].type = GEM_TYPE_1;
				
		board[0][0].type = GEM_TYPE_2;
				
		board[0][3].type = GEM_TYPE_2;
		board[1][3].type = GEM_TYPE_3;
		board[2][3].type = GEM_TYPE_3;
		
		board[3][2].type = GEM_TYPE_3;
				
		board[0][5].type = GEM_TYPE_2;
		board[1][5].type = GEM_TYPE_1;
		board[2][5].type = GEM_TYPE_1;
		
		board[3][4].type = GEM_TYPE_1;
	}
		
	private void emptyBoard() {
		for(int y = 0; y < boardSize; ++y) {
			for (int x = 0; x < boardSize; ++x) {
				board[x][y].type = GEM_TYPE_NONE;
			}
		}
	}
	
	public void placeInitialGems() {
		//System.out.println("Match3 placeInitialGems...");
		int hintCount;
		
		do 
		{			
			emptyBoard();
			placeTestGems();
			
			for(int y = 0; y < boardSize; ++y) {
				for (int x = 0; x < boardSize; ++x) {
					if (board[x][y].type == GEM_TYPE_NONE) {
						do {
							board[x][y].type = randomGemType();
						} while ( isMatch(x, y) );
					}				
				}
			}
			
			hintCount = countHints();
			//System.out.println("Match3 hint count is: " + hintCount);
		} while (hintCount == 0);		
	}
	
	private int randomGemType() {
		return MyUtils.randInt(0, MAX_GEM_TYPES - 1);
	}
	
	private boolean isSameGemAtPosition(GemPosition gp, int x, int y) {
		//System.out.println("isSameGemAtPosition " + x + "," + y);
		if (gp == null)
			return false;
				
		if ( (x < 0 || x > boardSize - 1) || (y < 0 || y > boardSize - 1) ) {
			//System.out.println("out of board...");
			return false;
		}

        return board[x][y].type != GEM_TYPE_NONE && board[x][y].type == gp.type;
    }
	
	private int rowMatchCount(int x, int y) {
		//System.out.println("Match3 rowMatchCount from " + x + "," + y);
		GemPosition current = board[x][y];
		
		if (current == null) {
			//System.out.println("gem at position is null!");
			return 0;
		}
		
		//System.out.println("Gem Type is: " + getGemTypeString(board[x][y].gemType) );
		
		int count = 1;
		int step = 1;
		//System.out.println("x plus...");
		while( isSameGemAtPosition(current, x + step, y) ) {
			++step;
			++count;
		}
		
		step = 1;
		//System.out.println("x minus...");
		while( isSameGemAtPosition(current, x - step, y) ) {
			++step;
			++count;
		}
		
		return count;
	}
	
	private int colMatchCount(int x, int y) {
		//System.out.println("Match3 colMatchCount from " + x + "," + y);
		
		GemPosition current = board[x][y];
		
		if (current == null) {
			//System.out.println("gem at position is null!");
			return 0;
		}
		
		//System.out.println("Gem Type is: " + getGemTypeString(board[x][y].gemType) );
		
		int count = 1;
		int step = 1;
		//System.out.println("y plus...");
		while( isSameGemAtPosition(current, x, y + step) ) {
			++step;
			++count;
		}
		
		step = 1;
		//System.out.println("y minus...");
		while( isSameGemAtPosition(current, x, y - step) ) {
			++step;
			++count;
		}
		
		return count;
	}
	
	private boolean isMatch(int x, int y) {
		return rowMatchCount(x, y) > 2 || colMatchCount(x, y) > 2;
	}
	
	private void removeGems(PopAnimation anim, int x, int y, boolean combo) {
		GemPosition current = board[x][y];
		int step;
		
		if (rowMatchCount(x, y) > 2) {
			step = 0;
			while ( isSameGemAtPosition(current, x + step, y) ) {
				anim.add(board[x + step][y]);
				++step;
			}
			
			step = 0;
			while( isSameGemAtPosition(current, x - step, y) ) {
				anim.add(board[x - step][y]);
				++step;
			}
		}
		
		if (colMatchCount(x, y) > 2) {
			step = 0;
			while( isSameGemAtPosition(current, x, y + step) ) {
				anim.add(board[x][y + step]);
				++step;
			}
			
			step = 0;
			while( isSameGemAtPosition(current, x, y - step) ) {
				anim.add(board[x][y - step]);
				++step;
			}
		}
	}
		
	private void fillBufferBoard() {
		for(int y = boardSize; y < boardSize * 2; ++y) {
			for(int x = 0; x < boardSize; ++x) {
				board[x][y].type = randomGemType();
				board[x][y].visible = true;				
			}
		}
	}
	
	public void dumpBoardStat() {
		//System.out.println("Match3 board stat...");
		countGemTypesOnBoard();
		//int hintCount = countHints();
		//System.out.println("hint count: " + hintCount);
		//hintList.clear();
        swapHintManager.reset();
	}

    private void saveBoardToTemp() {
        for (int x = 0; x < boardSize; ++x) {
            for(int y = 0; y < boardSize; ++y) {
                tempBoard[x][y].type = board[x][y].type;
                tempBoard[x][y].visible = board[x][y].visible;
            }
        }
    }

    private void restoreBoardFromTemp() {
        for (int x = 0; x < boardSize; ++x) {
            for(int y = 0; y < boardSize; ++y) {
                board[x][y].visible = tempBoard[x][y].visible;
                board[x][y].type = tempBoard[x][y].type;
            }
        }
    }

	public void countGemTypesOnBoard() {
		//System.out.println("Match countGemTypesOnBoard...");
		for(int i = 0; i < MAX_GEM_TYPES; ++i) {
            gemTypesArray[i] = 0;
        }
		
		for (int x = 0; x < boardSize; ++x) {
			for(int y = 0; y < boardSize; ++y) {
				if (board[x][y].type != -1) {
					++gemTypesArray[ board[x][y].type ];
				}
			}
		}
		
		//System.out.println("Board Stat:");
		//for (int i = 0; i < MAX_GEM_TYPES; ++i) {
			//System.out.println(getGemTypeString(i) + ": " + gemCounter[i]);
		//}		
	}	

	private void fallGems(FallGroupAnimation anim) {
		//System.out.println("Match3 fallGems...");
        FallAnimation fallAnim;
        int boardX, fromY, toY, size;
        int hintCount;
        int gap;

        saveBoardToTemp();

        do {
			anim.reset();
			fillBufferBoard();
            restoreBoardFromTemp();

			for (int x = 0; x < boardSize; ++x) {
				gap = 0;
				for(int y = 0; y < boardSize * 2; ++y) {
					if (board[x][y].type == GEM_TYPE_NONE) {
						++gap;
					} else { 
						if ( (gap > 0) &&  (y - gap < boardSize) ) {
							anim.add(/*from*/board[x][y], /*to*/board[x][y - gap]);
						}
					}
				}
			}
			
			// simulate board state after fall anim has completed
            size = anim.count();
			for(int i = 0; i < size; ++i) {
                fallAnim = anim.getAnimAt(i);
				boardX = fallAnim.animGemFrom.boardX;
				fromY = fallAnim.animGemFrom.boardY;
				toY = fallAnim.animGemTo.boardY;
				
				board[boardX][toY].type = board[boardX][fromY].type;
			}
			
			hintCount = countHints();
			//System.out.println("hint count: " + hintCount);
			
		} while (hintCount == 0); // prevent dead end (no match swaps)

        restoreBoardFromTemp();
        swapHintManager.reset();

        for(int i = 0; i < size; ++i) {
            fallAnim = anim.getAnimAt(i);
			boardX = fallAnim.animGemFrom.boardX;
			fromY = fallAnim.animGemFrom.boardY;
			
			board[boardX][fromY].visible = false;
		}
	}
	
	public void handle() {
		//System.out.println("Match3 handle...");
		// check if first and second selected gems are neighbors
		// two gems are neighbors if distance between them is 1
		int distanceX = Math.abs(firstSelected.boardX - secondSelected.boardX);
		int distanceY = Math.abs(firstSelected.boardY - secondSelected.boardY);
		
		if (distanceX + distanceY > 1) { // The selected two gems are not neighbors 
			//System.out.println("Selected gems are NOT neighbors");
			firstSelected = secondSelected;
			secondSelected = null;
		} else {
			//System.out.println("Swapping 1st " + getGemTypeString(firstSelectedGem.gemType)  + " (" + firstSelectedGem.boardX  + "," + firstSelectedGem.boardY + ")" 
			//				 + " and 2nd " + getGemTypeString(secondSelectedGem.gemType) + " (" + secondSelectedGem.boardX + "," + secondSelectedGem.boardY + ")");
		
			swapAnim.init(firstSelected, secondSelected, /*undo*/false);
			addAnimToManager(swapAnim);

            swapHintManager.reset();
		}
	}
	
	private void swapGems(GemPosition first, GemPosition second) {
		int temp = first.type;
		first.type = second.type;
		second.type = temp;
	}
	
	public void showOrHideHints() {
		//System.out.println("Match3 calcHints...");
        if (swapHintManager.isEmpty()) {
            countHints();
        } else {
            swapHintManager.reset();
        }
	}
	
	public int countHints() {
		GemPosition first, second;
        swapHintManager.reset();
		
		for(int y = 0; y < boardSize; ++y) {
			for(int x = 0; x < boardSize; ++x) {
				
				if (x < boardSize - 1) {
					first = board[x][y];
					second = board[x+1][y];
					swapGems(first, second);

					if ( isMatch(x, y) || isMatch(x + 1, y) ) {
                        swapHintManager.add(first, second);
					}
					swapGems(first, second);
				}
				
				if (y < boardSize - 1) {
					first = board[x][y];
					second = board[x][y+1];
					swapGems(first, second);

					if ( isMatch(x,y) || isMatch(x, y + 1) ) {
                        swapHintManager.add(first, second);
					}
					swapGems(first, second);
				}
			}
		}
        return swapHintManager.count();
	}
	
	private void addAnimToManager(BaseAnimation anim) {
		animManager.add(anim);
		isAnimating = true;
	}
	
	public void update() {
		if (isAnimating) {
            animManager.update();
            if (animManager.isDone()) {
				handleAnimCompleted();
			}
		}
	}
	
	private void handleAnimCompleted() {
		BaseAnimation finishedAnim = animManager.finished;
		isAnimating = false;
		
		if (finishedAnim instanceof SwapAnimation) {
			swapAnimFinished((SwapAnimation)finishedAnim);
		}
		
		if (finishedAnim instanceof FallGroupAnimation) {
			fallGroupAnimFinished((FallGroupAnimation)finishedAnim);
		}
		
		if (finishedAnim instanceof PopAnimation) {
			popAnimFinished((PopAnimation)finishedAnim);
		}
		animManager.finished = null;
	}
	
	private void swapAnimFinished(SwapAnimation swapAnim) {
		if (swapAnim.undo) {
			swapGems(secondSelected, firstSelected);
			firstSelected.visible = true;
			secondSelected.visible = true;
			firstSelected = null;
			secondSelected = null;
		} else {
			swapGems(firstSelected, secondSelected);
			firstSelected.visible = true;
			secondSelected.visible = true;
			
			boolean first = isMatch(secondSelected.boardX, secondSelected.boardY);
			boolean second = isMatch(firstSelected.boardX, firstSelected.boardY);
				
			if (first || second) {
				//System.out.println("Gems after swapping form a valid match...");
				PopAnimation anim = getPopAnimation();
				
				if (first) {
					removeGems(anim, secondSelected.boardX, secondSelected.boardY, false);
				}
				
				if (second) {
					removeGems(anim, firstSelected.boardX, firstSelected.boardY, false);
				}

                scoreCounter.addScore(anim.count(), /*combo*/false);

                if (first && second) {
                    scoreCounter.addBonusForPerfectSwap();
                }
				
				firstSelected = null;
				secondSelected = null;
				
				addAnimToManager(anim);
			} else {
				//System.out.println("Gems after swapping does NOT form a valid match...");
				swapAnim.init(secondSelected, firstSelected, /*undo*/true);
				addAnimToManager(swapAnim);
			}
		}
	}

	private void fallGroupAnimFinished(FallGroupAnimation fallGroupAnim) {
        int boardX, fromY, toY;
        FallAnimation fallAnim;
        int size = fallGroupAnim.count();
		for (int i = 0; i < size; ++i) {
            fallAnim = fallGroupAnim.getAnimAt(i);
			boardX = fallAnim.animGemFrom.boardX;
			fromY = fallAnim.animGemFrom.boardY;
			toY = fallAnim.animGemTo.boardY;
			
			board[boardX][toY].type = board[boardX][fromY].type;
			board[boardX][toY].visible = true;
			board[boardX][fromY].type = GEM_TYPE_NONE;
		}

		PopAnimation anim = getPopAnimation();

		for(int y = 0; y < boardSize; ++y) {
			for(int x = 0; x < boardSize; ++x) {
				if (isMatch(x, y)) {
					removeGems(anim, x, y, true);
				}
			}
		}
		
		if (!anim.isEmpty()) {
			//System.out.println("COMBO(S)!!!");
			addAnimToManager(anim);
            scoreCounter.addScore(anim.count(), /*combo*/true);
		}
	}
	
	private void popAnimFinished(PopAnimation popAnim) {
        GemPosition gp;
        int size = popAnim.count();
		for(int i = 0; i < size; ++i) {
            gp = popAnim.getAt(i);
			board[gp.boardX][gp.boardY].type = GEM_TYPE_NONE;
		}
		
		FallGroupAnimation anim = getFallGroupAnimation();
        anim.reset();
		fallGems(anim);
		
		if (!anim.isEmpty()) {
            addAnimToManager(anim);
        }
	}
	
	public void createInitialFallAnim() {
		FallGroupAnimation anim = getFallGroupAnimation();
        anim.reset();
		
		for(int y = 0; y < boardSize*2; ++y) {
			for(int x = 0; x < boardSize; ++x) {
				if (y < boardSize) {
					board[x][y+boardSize].type = board[x][y].type;
                    anim.add(/*from*/board[x][y + boardSize], /*to*/board[x][y]);
				}
				board[x][y].visible = false;
			}
		}
		addAnimToManager(anim);
	}

    private PopAnimation getPopAnimation() {
        pooledPopAnimation.reset();
        return pooledPopAnimation;
    }

    private FallGroupAnimation getFallGroupAnimation() {
        return pooledFallGroupAnim;
    }
}