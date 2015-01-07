package com.almagems.mineraider;

import static com.almagems.mineraider.Constants.*;

import java.util.ArrayList;

import com.almagems.mineraider.anims.AnimationManager;
import com.almagems.mineraider.anims.BaseAnimation;
import com.almagems.mineraider.anims.FallAnimation;
import com.almagems.mineraider.anims.FallGroupAnimation;
import com.almagems.mineraider.anims.PopAnimation;
import com.almagems.mineraider.anims.SwapAnimation;
import com.almagems.mineraider.anims.SwapHint;
import com.almagems.mineraider.anims.SwapHintManager;
import com.almagems.mineraider.util.MyUtils;

public class Match3 {
	
	public enum State {
		Idle,
		WaitForAnimToComplete
	};
	
	public State state;	
	public GemPosition[][] board = new GemPosition[MAX_BOARD_SIZE][MAX_BOARD_SIZE*2];	
	public GemPosition firstSelectedGem;
	public GemPosition secondSelectedGem;
	//public ArrayList<SwapHint> hintList = new ArrayList<SwapHint>();
    public final SwapHintManager swapHintManager;
		
	private String[] gemNames = new String[MAX_GEM_TYPES];	
	private AnimationManager animManager;
	
	public Match3(AnimationManager animManager) {
		//System.out.println("Match3 ctor...");

		state = State.Idle;
		this.animManager = animManager;
        swapHintManager = new SwapHintManager();
		
		for(int y = 0; y < MAX_BOARD_SIZE*2; ++y) {
			for (int x = 0; x < MAX_BOARD_SIZE; ++x) {
				board[x][y] = new GemPosition(x, y);
			}
		}
	}
	
	public void registerGemTypeName(int id, String name) {
		if (gemNames[id] == null) {
			gemNames[id] = new String(name);
		}
	}
	
	public String getGemTypeString(int index) {
		if (index >= 0 && index <= 7)
			return gemNames[index];
		else
			return "no name index: " + index;
	}
	
	private void placeTestGems() {		
		board[0][2].gemType = GEM_TYPE_2;
		board[1][2].gemType = GEM_TYPE_1;
		board[2][2].gemType = GEM_TYPE_1;
		
		board[0][1].gemType = GEM_TYPE_1;
				
		board[0][0].gemType = GEM_TYPE_2;
				
		board[0][3].gemType = GEM_TYPE_2;
		board[1][3].gemType = GEM_TYPE_3;
		board[2][3].gemType = GEM_TYPE_3;
		
		board[3][2].gemType = GEM_TYPE_3;
				
		board[0][5].gemType = GEM_TYPE_2;
		board[1][5].gemType = GEM_TYPE_1;
		board[2][5].gemType = GEM_TYPE_1;
		
		board[3][4].gemType = GEM_TYPE_1;
	}
		
	private void emptyBoard() {
		for(int y = 0; y < MAX_BOARD_SIZE; ++y) {
			for (int x = 0; x < MAX_BOARD_SIZE; ++x) {
				board[x][y].gemType = GEM_TYPE_NONE;
			}
		}
	}
	
	public void placeInitialGems() {
		//System.out.println("Match3 placeInitialGems...");
		
		int hintCount = 0;
		
		do 
		{			
			emptyBoard();
			placeTestGems();
			
			for(int y = 0; y < MAX_BOARD_SIZE; ++y) {
				for (int x = 0; x < MAX_BOARD_SIZE; ++x) {
					if (board[x][y].gemType == GEM_TYPE_NONE) {
						do {
							board[x][y].gemType = getRandomGemType();
						} while ( isMatch(x, y) );
					}				
				}
			}
			
			hintCount = countHints();
			//System.out.println("Match3 hint count is: " + hintCount);
		
		} while (hintCount == 0);		
	}
	
	private int getRandomGemType() {
		int index = MyUtils.randInt(0, MAX_GEM_TYPES - 1);
		return index;
	}
	
	private boolean isSameGemAtPosition(GemPosition gp, int x, int y) {
		//System.out.println("isSameGemAtPosition " + x + "," + y);
		
		if (gp == null)
			return false;
				
		if (x < 0 || x > MAX_BOARD_SIZE - 1) {
			//System.out.println("out of board X axis...");
			return false;
		}
		
		if (y < 0 || y > MAX_BOARD_SIZE - 1) {
			//System.out.println("out of board Y axis...");
			return false;
		}
		
		if (board[x][y].gemType == GEM_TYPE_NONE) {
			//System.out.println("gem type is NONE...");
			return false;
		}
		
		if (board[x][y].gemType == gp.gemType) {
			//System.out.println("same gem type: " + getGemTypeString(gp.gemType) );
			return true;
		} else {
			//System.out.println("different gem type: " + getGemTypeString(board[x][y].gemType) );
		}
		
		return false;
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
		//System.out.println("Match3 isMatch (" + x + "," + y + ")...");
		return rowMatchCount(x, y) > 2 || colMatchCount(x, y) > 2;
	}
	
	private void removeGems(PopAnimation anim, int x, int y) {
		//System.out.println("Match3 removeGems from position (" + x + "," + y + ")");

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
		for(int y = MAX_BOARD_SIZE; y < MAX_BOARD_SIZE * 2; ++y) {
			for(int x = 0; x < MAX_BOARD_SIZE; ++x) {				
				board[x][y].gemType = getRandomGemType();
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
	
	public void countGemTypesOnBoard() {
		//System.out.println("Match countGemTypesOnBoard...");
		
		int[] gemCounter = new int[MAX_GEM_TYPES];
		
		for (int x = 0; x < MAX_BOARD_SIZE; ++x) {			
			for(int y = 0; y < MAX_BOARD_SIZE; ++y) {
				if (board[x][y].gemType != -1) {
					++gemCounter[ board[x][y].gemType ];
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
		GemPosition[][] temp = new GemPosition[MAX_BOARD_SIZE][MAX_BOARD_SIZE];
		
		for (int x = 0; x < MAX_BOARD_SIZE; ++x) {			
			for(int y = 0; y < MAX_BOARD_SIZE; ++y) {
				temp[x][y] = new GemPosition(x, y);
				temp[x][y].gemType = board[x][y].gemType;
				temp[x][y].visible = board[x][y].visible;
			}
		}
				
		int hintCount = 0;
		do {
			anim.list.clear();
						
			fillBufferBoard();			
			
			for (int x = 0; x < MAX_BOARD_SIZE; ++x) {
				for(int y = 0; y < MAX_BOARD_SIZE; ++y) {			
					board[x][y].visible = temp[x][y].visible;
					board[x][y].gemType = temp[x][y].gemType;
				}
			}
			
			for (int x = 0; x < MAX_BOARD_SIZE; ++x) {
				int gap = 0;			
				for(int y = 0; y < MAX_BOARD_SIZE * 2; ++y) {
					if (board[x][y].gemType == GEM_TYPE_NONE) {
						++gap;
					} else { 
						if ( (gap > 0) &&  (y - gap < MAX_BOARD_SIZE) ) {
							FallAnimation fall = new FallAnimation(/*from*/board[x][y], /*to*/board[x][y-gap]);
							anim.list.add(fall);
						}
					}
				}
			}
			
			// simulate board state after fall anim has completed
			for (FallAnimation fallAnim : anim.list) {
				int x = fallAnim.animGemFrom.boardX;
				int fromY = fallAnim.animGemFrom.boardY;
				int toY = fallAnim.animGemTo.boardY;
				
				board[x][toY].gemType = board[x][fromY].gemType;				
			}
			
			hintCount = countHints();
			//System.out.println("hint count: " + hintCount);
			
		} while (hintCount == 0);

		for (int x = 0; x < MAX_BOARD_SIZE; ++x) {
			for(int y = 0; y < MAX_BOARD_SIZE; ++y) {			
				board[x][y].visible = temp[x][y].visible;
				board[x][y].gemType = temp[x][y].gemType;				
			}
		}		
		//hintList.clear();
        swapHintManager.reset();
		
		for (FallAnimation fallAnim : anim.list) {
			int x = fallAnim.animGemFrom.boardX;
			int fromY = fallAnim.animGemFrom.boardY;
			
			board[x][fromY].visible = false;
		}
	}
	
	public void handle() {
		//System.out.println("Match3 handle...");
		// check if first and second selected gems are neighbors
		// two gems are neighbors if distance between them is 1
		int distanceX = Math.abs(firstSelectedGem.boardX - secondSelectedGem.boardX);
		int distanceY = Math.abs(firstSelectedGem.boardY - secondSelectedGem.boardY);
		
		if (distanceX + distanceY > 1) { // The selected two gems are not neighbors 
			//System.out.println("Selected gems are NOT neighbors");
			firstSelectedGem = secondSelectedGem;
			secondSelectedGem = null;
		} else {
			//System.out.println("Swapping 1st " + getGemTypeString(firstSelectedGem.gemType)  + " (" + firstSelectedGem.boardX  + "," + firstSelectedGem.boardY + ")" 
			//				 + " and 2nd " + getGemTypeString(secondSelectedGem.gemType) + " (" + secondSelectedGem.boardX + "," + secondSelectedGem.boardY + ")");
		
			SwapAnimation swapAnim = new SwapAnimation(firstSelectedGem, secondSelectedGem, /*undo*/false);
			addAnimToManager(swapAnim);
			
			//hintList.clear();
            swapHintManager.reset();
		}
	}
	
	private void swapGems(GemPosition first, GemPosition second) {
		int gemType1 = first.gemType;
		int gemType2 = second.gemType;
		
		first.gemType = gemType2;
		second.gemType = gemType1;
	}
	
	public void hints() {
		//System.out.println("Match3 hints...");
		
//		if (!hintList.isEmpty()) {
//			hintList.clear();
//			return;
//		}

        if (!swapHintManager.isEmpty()) {
            swapHintManager.reset();
            return;
        }

		countHints();
	}
	
	public int countHints() {
		GemPosition first;
		GemPosition second;
		
		//hintList.clear();
        swapHintManager.reset();
		
		for(int y = 0; y < MAX_BOARD_SIZE; ++y) {
			for(int x = 0; x < MAX_BOARD_SIZE; ++x) {
				
				if (x < MAX_BOARD_SIZE - 1) {
					first = board[x][y];
					second = board[x+1][y];
					
					swapGems(first, second);
					if ( isMatch(x, y) || isMatch(x + 1, y) ) {
						//int x1 = x + 1;
						//System.out.println("hints Swap (" + x + "," + y + ") -> (" + x1 + "," + y + ")");
						//SwapHint hint = new SwapHint(first, second);
						//hintList.add(hint);
                        swapHintManager.add(first, second);
					}
					swapGems(first, second);
				}
				
				if (y < MAX_BOARD_SIZE - 1) {
					first = board[x][y];
					second = board[x][y+1];
					
					swapGems(first, second);
					if ( isMatch(x,y) || isMatch(x, y + 1) ) {
						//int y1 = y + 1;
						//System.out.println("hints Swap (" + x + "," + y + ") -> (" + x + "," + y1 + ")");
						//SwapHint hint = new SwapHint(first, second);
						//hintList.add(hint);
                        swapHintManager.add(first, second);
					}
					swapGems(first, second);
				}
			}
		}
//		return hintList.size();
        return swapHintManager.count();
	}
	
	private void addAnimToManager(BaseAnimation anim) {
		animManager.add(anim);
		state = State.WaitForAnimToComplete;
	}
	
	public void update() {
		//System.out.println("match3 state is: " + state);
		
		animManager.update();		
		
		if (state == State.WaitForAnimToComplete) {
			if (animManager.isDone()) {
				handleAnimCompleted();
			}
		}
	}
	
	private void handleAnimCompleted() {
		BaseAnimation finishedAnim = animManager.finishedAnim;
		state = State.Idle;
		
		if (finishedAnim instanceof SwapAnimation) {
			SwapAnimation swapAnim = (SwapAnimation)finishedAnim;
			swapAnimFinished(swapAnim);
		}
		
		if (finishedAnim instanceof FallGroupAnimation) {
			FallGroupAnimation fallGroupAnim = (FallGroupAnimation)finishedAnim;
			fallGroupAnimFinished(fallGroupAnim);
		}
		
		if (finishedAnim instanceof PopAnimation) {
			PopAnimation disapAnim = (PopAnimation)finishedAnim;
			disappearAnimFinished(disapAnim);
		}
		animManager.finishedAnim = null;
	}
	
	private void swapAnimFinished(SwapAnimation swapAnim) {
		if (swapAnim.undo) {
			swapGems(secondSelectedGem, firstSelectedGem);
			firstSelectedGem.visible = true;
			secondSelectedGem.visible = true;
			firstSelectedGem = null;
			secondSelectedGem = null;
		} else {
			swapGems(firstSelectedGem, secondSelectedGem);
			firstSelectedGem.visible = true;
			secondSelectedGem.visible = true;
			
			boolean first = isMatch(secondSelectedGem.boardX, secondSelectedGem.boardY);
			boolean second = isMatch(firstSelectedGem.boardX, firstSelectedGem.boardY);
				
			if (first || second) {
				//System.out.println("Gems after swapping form a valid match...");
				
				PopAnimation anim = new PopAnimation();
				
				if (first) {
					removeGems(anim, secondSelectedGem.boardX, secondSelectedGem.boardY);
				}
				
				if (second) {
					removeGems(anim, firstSelectedGem.boardX, firstSelectedGem.boardY);
				}
				
				firstSelectedGem = null;
				secondSelectedGem = null;
				
				addAnimToManager(anim);
			} else {
				//System.out.println("Gems after swapping does NOT form a valid match...");
				SwapAnimation undoSwapAnim = new SwapAnimation(secondSelectedGem, firstSelectedGem, /*undo*/true);
				addAnimToManager(undoSwapAnim);
			}
		}
	}
	
	private void fallGroupAnimFinished(FallGroupAnimation fallGroupAnim) {
		for (FallAnimation fallAnim : fallGroupAnim.list) {
			int x = fallAnim.animGemFrom.boardX;
			int fromY = fallAnim.animGemFrom.boardY;
			int toY = fallAnim.animGemTo.boardY;
			
			board[x][toY].gemType = board[x][fromY].gemType;
			board[x][toY].visible = true;
			board[x][fromY].gemType = GEM_TYPE_NONE;
		}
				
		PopAnimation anim = new PopAnimation();

		for(int y = 0; y < MAX_BOARD_SIZE; ++y) {
			for(int x = 0; x < MAX_BOARD_SIZE; ++x) {
				if (isMatch(x, y)) {
					removeGems(anim, x, y);
				}
			}
		}
		
		if (!anim.list.isEmpty()) {
			//System.out.println("COMBO(S)!!!");
			addAnimToManager(anim);
		}		
	}
	
	private void disappearAnimFinished(PopAnimation disapAnim) {
		if ( !disapAnim.list.isEmpty() ) {
			for (GemPosition gp : disapAnim.list) {
				board[gp.boardX][gp.boardY].gemType = GEM_TYPE_NONE;				
			}
		}
		
		FallGroupAnimation anim = new FallGroupAnimation();
		fallGems(anim);
		
		if (!anim.list.isEmpty()) {
			addAnimToManager(anim);
		}
	}
	
	public void createInitialFallAnim() {
		FallGroupAnimation anim = new FallGroupAnimation();
		
		for(int y = 0; y < MAX_BOARD_SIZE*2; ++y) {
			for(int x = 0; x < MAX_BOARD_SIZE; ++x) {
				if (y < MAX_BOARD_SIZE) {
					board[x][y+MAX_BOARD_SIZE].gemType = board[x][y].gemType;
					
					FallAnimation fall = new FallAnimation(/*from*/board[x][y+MAX_BOARD_SIZE], /*to*/board[x][y]);
					anim.list.add(fall);
				}
				board[x][y].visible = false;
			}
		}				
		addAnimToManager(anim);
	}
	
}