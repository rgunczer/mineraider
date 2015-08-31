package com.almagems.mineraider.scenes;

// opengl
import static android.opengl.GLES20.*;
import static android.opengl.Matrix.*;

// java
import java.util.ArrayList;

// box2d
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;

// mine
import com.almagems.mineraider.singletons.ClassicSingleton;
import com.almagems.mineraider.GemPosition;
import com.almagems.mineraider.HUD;
import com.almagems.mineraider.Match3.Match3;
import com.almagems.mineraider.PositionInfo;
import com.almagems.mineraider.Physics;
import com.almagems.mineraider.RockData;
import com.almagems.mineraider.visuals.Visuals;
import com.almagems.mineraider.anims.AnimationManager;
import com.almagems.mineraider.anims.PopAnimation;
import com.almagems.mineraider.loading.Loading;
import com.almagems.mineraider.menu.Menu;
import com.almagems.mineraider.objects.EdgeDrawer;
import com.almagems.mineraider.objects.MineCart;
import com.almagems.mineraider.objects.Model;
import com.almagems.mineraider.util.Geometry;
import com.almagems.mineraider.util.MyColor;
import com.almagems.mineraider.util.Ray;

import static com.almagems.mineraider.Constants.*;


public class Level {

    public enum GameState {
        Loading,
        Menu,
        Playing,
    }

	private enum SwipeDir {
		SwipeNone,
		SwipeLeft,
		SwipeRight,
		SwipeUp,
		SwipeDown
	}

    public GameState gameState;

    private boolean initialized;

    private float touchDownX;
    private float touchDownY;
    private SwipeDir swipeDir;

    private Visuals visuals;
    private ClassicSingleton singleton;
    private Menu menu;
    private Loading loading;
	private Physics physics;
	private Match3 match3;

    private final PositionInfo markerPos;
	private float elapsedTimeSelMarkerAnim;

	private boolean editorEnabled;
	private AnimationManager animManager;
	private final ArrayList<RockData> rocks;
	private final PositionInfo pos;


    // ctor
	public Level() {
        initialized = false;
        swipeDir = SwipeDir.SwipeNone;
        gameState = GameState.Loading;
        markerPos = new PositionInfo();
        elapsedTimeSelMarkerAnim = 0f;
        editorEnabled = false;
        pos = new PositionInfo();
        rocks = new ArrayList<RockData>();
    }

    public void init(ClassicSingleton singleton) {
        this.singleton = singleton;
        visuals = singleton.visuals;

        loading = new Loading(visuals);
    }

    public void createObjects() {
        if (initialized) {
            return;
        }

		physics = new Physics();
		animManager = new AnimationManager();
		match3 = new Match3(8, animManager, singleton.scoreCounter);

		initBoardGeometry();		

		singleton.particleManager.init();
				
		//physics.addEdge(-13.5f, -21.5f,  13.5f, -21.5f); // bottom
		physics.addEdge(-13.0f, -7.0f, -13.5f, 20.0f); // left
		physics.addEdge(13.0f, -7.0f, 13.5f, 20.0f); // right
		
		physics.addBoxStatic(8.0f, -5.9f, 24.8f, 12.0f, 0.8f);
		physics.addBoxStatic(-7.8f, -5.9f, -25.0f, 12.0f, 0.8f);
		physics.addBoxStatic(3.1f, -8.8f, 0f, 1.4f, 1.4f);
		physics.addBoxStatic(-3.0f, -8.8f, 0f, 1.4f, 1.4f);
		
		// sin
		physics.addBoxStatic(0.0f, -19.7f, 0f, 70.0f, 0.5f);

        float cartX = -20f;
        float cartX2nd = -30f;
        float cartY = -15f;

        float x = cartX; //-20f;
		float y = cartY; //-15.7f;
		MineCart mineCart;
		
		mineCart = new MineCart(physics, x, y, visuals);
        mineCart.z = 1f;
		singleton.cart1 = mineCart;

		x = cartX2nd;
		mineCart = new MineCart(physics, x, y, visuals);
        mineCart.z = 1f;
		singleton.cart2 = mineCart;

        PopAnimation.physics = physics;


        menu = new Menu(visuals);



        HUD hud = singleton.hud;
        hud.init();
        hud.updateScore(singleton.getScore());

        loading.init();
        menu.init();

        initialized = true;

        //initFadeIn();


        final float cartSpeed = -3f;
        MineCart cart;

        cart = singleton.cart1;
        cart.reposition(cartX, cartY);
        cart.start(cartSpeed);

        cart = singleton.cart2;
        cart.reposition(cartX2nd, cartY);
        cart.start(cartSpeed);

        physics.clear();
        animManager.clear();

        match3.placeInitialGems();
        match3.dumpBoardStat();
        match3.createInitialFallAnim();

        singleton.hud.reset();

        visuals.setProjectionMatrix3D();
	}

	public void surfaceChanged(int width, int height) {
        loading.init();
    }

	public void update() {
        visuals.updateViewProjMatrix();

        if (gameState == GameState.Loading) {
            loading.update();
            if (loading.done) {
                gameState = GameState.Menu;
                update();
            }
        } else {
            physics.update();
            match3.update();

            singleton.hud.update();
            singleton.hud.updateScore(singleton.scoreCounter.getScore());

            if (gameState == GameState.Menu) {
                menu.update();
                if (menu.getSelectedMenuOption() == Menu.MenuOptions.Play) {
                    gameState = GameState.Playing;
                }
            } else if (gameState == GameState.Playing) {
                updateInPlaying();
            }
        }
    }

    private void updateInPlaying() {

    }

	public void draw() {
        if (gameState == GameState.Loading) {
            loading.draw();
        } else {
            drawCommon();
            drawPlaying();

            if (gameState == GameState.Menu) {
                menu.draw();
            }
        }
	}

    private void drawCommon() {
        visuals.setProjectionMatrix3D();
        visuals.updateViewProjMatrix();

        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
        visuals.dirLightShader.useProgram();

        visuals.dirLightShader.setTexture(visuals.textureFloor);
        drawFloor();

        visuals.dirLightShader.setTexture(visuals.textureWall);
        drawWall();

        visuals.dirLightShader.setTexture(visuals.textureSoil);
        drawSoil();

        visuals.dirLightShader.setTexture(visuals.textureCliff142);
        drawRocks();

        visuals.dirLightShader.setTexture(visuals.texturePillar);
        drawPillars();

        visuals.dirLightShader.setTexture(visuals.textureBeam);
        drawBeam();

        visuals.dirLightShader.setTexture(visuals.textureRailRoad);
        drawRailRoad();

        drawMineCarts();

        visuals.dirLightShader.setTexture(visuals.textureCrate);
        drawCrates();
    }

	private void drawPlaying() {
        visuals.setProjectionMatrix3D();
        visuals.updateViewProjMatrix();

        glEnable(GL_DEPTH_TEST);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        visuals.pointLightShader.useProgram();
        visuals.dirLightShader.setTexture(visuals.textureGems);

        glDisable(GL_BLEND);
		drawGems();
		glEnable(GL_DEPTH_TEST);

        drawSelectionMarker();

        match3.swapHintManager.draw();

        //visuals.dirLightShader.setTexture(visuals.texturePickAxe);
		//drawPickAxes();

//        drawPhysics();
		singleton.particleManager.draw();
        singleton.hud.draw();

        //visuals.setProjectionMatrix2D();
        //super.drawFade();

        // needs to be set for picking to work correctly
        visuals.setProjectionMatrix3D();
        visuals.updateViewProjMatrix();
	}

	public void handleTouchPress(float normalizedX, float normalizedY) {
        switch (gameState) {
            case Playing:
                handleTouchPressOnPlaying(normalizedX, normalizedY);
                break;

            case Menu:
                menu.handleTouchPress(normalizedX, normalizedY);
                break;

            case Loading:
                gameState = GameState.Menu; // TODO: remove later
                break;
        }
    }

    private void handleTouchPressOnPlaying(float normalizedX, float normalizedY) {
        // temp solution to be able to go back
        final float y = -0.9f;
        if (normalizedY < y) {
            gameState = GameState.Menu;
            menu.reset();
            //goNextScene = true;
            //nextSceneId = ScenesEnum.Shaft;
            //super.initFadeOut();
        } else {
            swipeDir = SwipeDir.SwipeNone;
            touchDownX = normalizedX;
            touchDownY = normalizedY;

            if (!match3.isAnimating) {
                Ray ray = Geometry.convertNormalized2DPointToRay(touchDownX, touchDownY, visuals.invertedViewProjectionMatrix);
                GemPosition selectedGem = getSelectedGemFromRay(ray);

                //doEditorStuff(selectedGem);

                if (selectedGem == null) {
                    match3.showOrHideHints();
                } else {
                    if (match3.firstSelected == null) {
                        match3.firstSelected = selectedGem;
                    } else {
                        match3.secondSelected = selectedGem;
                        if (match3.firstSelected == match3.secondSelected) { // Same Gems are selected
                            match3.firstSelected = null;
                            match3.secondSelected = null;
                        } else {
                            match3.handle();
                        }
                    }
                }
            }
        }
	}

	public void handleTouchDrag(float normalizedX, float normalizedY) {

        switch (gameState) {
            case Menu:
                handleTouchDragOnMenu(normalizedX, normalizedY);
                break;

            case Playing:
                handleTouchDragOnPlaying(normalizedX, normalizedY);
                break;
        }
    }

    private void handleTouchDragOnMenu(float normalizedX, float normalizedY) {
        menu.handleTouchDrag(normalizedX, normalizedY);
    }

    private void handleTouchDragOnPlaying(float normalizedX, float normalizedY) {
		if (match3.firstSelected != null) {
			final float minDiff = 0.15f; 
			
			float diffX = Math.abs(touchDownX - normalizedX);
			float diffY = Math.abs(touchDownY - normalizedY);
			
			//System.out.println("DiffX: " + diffX);
			//System.out.println("DiffY: " + diffY);
			
			if (diffX > diffY) { // move on X axis
				if (diffX > minDiff) {
					if (touchDownX > normalizedX) {
						//System.out.println("Swipe left");
						swipeDir = SwipeDir.SwipeLeft;
					} else {
						//System.out.println("Swipe right");
						swipeDir = SwipeDir.SwipeRight;
					}
				}
			} else { // move on Y axis
				if (diffY > minDiff) {
					if (touchDownY > normalizedY) {
						//System.out.println("Swipe down");
						swipeDir = SwipeDir.SwipeDown;
					} else {
						//System.out.println("Swipe up");
						swipeDir = SwipeDir.SwipeUp;
					}
				}
			}
		}
	}

	public void handleTouchRelease(float normalizedX, float normalizedY) {
		if (!match3.isAnimating) {
			if (match3.firstSelected != null && swipeDir != SwipeDir.SwipeNone) {
				int x = match3.firstSelected.boardX;
				int y = match3.firstSelected.boardY;
				
				switch(swipeDir) {
				case SwipeDown:
					if ( (y - 1) >= 0 ) {
						match3.secondSelected = match3.board[x][y-1];
					}
					break;
					
				case SwipeUp:
					if ( (y + 1) < match3.boardSize) {
						match3.secondSelected = match3.board[x][y+1];
					}
					break;
					
				case SwipeLeft:
					if ( (x - 1) >= 0 ) {
						match3.secondSelected = match3.board[x-1][y];
					}
					break;
					
				case SwipeRight:
					if ( (x + 1) < match3.boardSize) {
						match3.secondSelected = match3.board[x+1][y];
					}
					break;
					
				default:
					//System.out.println("No swipe!");
					break;
				}
				
				if (match3.secondSelected != null) {
					match3.handle();
				}
			}
		}
	}

	private void doEditorStuff(GemPosition selectedGem) {
        if (match3.boardSize != 8) {
            throw new RuntimeException("Invalid board size for doEditorStuff must be 8, now is:" + match3.boardSize);
        }

		if (null == selectedGem) {
			return;
		}
		
		if (!editorEnabled) {
			return;
		}
					
		if ((selectedGem.boardX == 0) && (selectedGem.boardY == 0)) {
			rocks.add( new RockData() );
			return;
		}

		// dump
		if ((selectedGem.boardX == 7) && (selectedGem.boardY == 7)) {
			//for (RockData rd : rocks) {
			int size = rocks.size();
			for(int i = 0; i < size; ++i) {
				System.out.println("[ROCK]" + rocks.get(i).toString());
			}
			return;
		}		
				
		if (rocks.size() > 0) {
			float step = 0.5f;
			RockData rd = rocks.get(rocks.size() - 1);
			
			// gemtype
			if ((selectedGem.boardX == 1) && (selectedGem.boardY == 1)) {
				++rd.rockId;
				if (rd.rockId > 8) {
					rd.rockId = 0;
				}			
			}
		
			// degree
			if ((selectedGem.boardX == 2) && (selectedGem.boardY == 2)) {
				rd.degree -= 2.0f;						
			}			

			if ((selectedGem.boardX == 0) && (selectedGem.boardY == 2)) {
				rd.degree += 2.0f;
			}
			
			// z
			if ((selectedGem.boardX == 0) && (selectedGem.boardY == 7)) {
				rd.z -= step;
			}
				
			if ((selectedGem.boardX == 0) && (selectedGem.boardY == 6)) {
				rd.z += step;		
			}
		
			// 	x
			if ((selectedGem.boardX == 0) && (selectedGem.boardY == 1)) {
				rd.x -= step;			
			}
		
			if ((selectedGem.boardX == 2) && (selectedGem.boardY == 1)) {
				rd.x += step;			
			}
				
			// y
			if ((selectedGem.boardX == 1) && (selectedGem.boardY == 2)) {
				rd.y += step;			
			}
		
			if ((selectedGem.boardX == 1) && (selectedGem.boardY == 0)) {
				rd.y -= step;			
			}
		}
	}	

	private GemPosition getSelectedGemFromRay(Ray ray) {		
		for(int y = 0; y < match3.boardSize; ++y) {
			for (int x = 0; x < match3.boardSize; ++x) {
				GemPosition gp = match3.board[x][y];				
				if ( Geometry.intersects(gp.boundingSphere, ray) ) {
					return gp;
				}
			}
		}
		return null;
	}
		
	private void initBoardGeometry() {
		float posX;
		float posY = -2.7f;
		
		if (DRAW_BUFFER_BOARD) {
			posY = -20.0f;
		}
		
		final float gemDistance = 2.66f;
		
		for(int y = 0; y < match3.boardSize*2; ++y) {
			posX = -(((match3.boardSize - 1) * gemDistance) / 2.0f);
			for (int x = 0; x < match3.boardSize; ++x) {
				match3.board[x][y].init(posX, posY, -0.5f,	1.05f, 1.05f, 1.0f);
				posX += gemDistance;
			}
			posY += gemDistance;
			if (y == match3.boardSize - 1)
				posY += 1.0f;
		}			
	}

    void drawPhysics() {
        glDisable(GL_DEPTH_TEST);
        drawPhysicsStatics();
        drawPhysicsGemsFixtures();
        drawPhysicsEdges();
        glEnable(GL_DEPTH_TEST);
    }

	private void drawPhysicsEdges() {
		MyColor color = new MyColor(1.0f, 1.0f, 1.0f);
		EdgeDrawer edgeDrawer = new EdgeDrawer(10);		
		edgeDrawer.begin();
		
		for(Body body : physics.edges) {
			Fixture fixture = body.getFixtureList();
			while(fixture != null) {
				EdgeShape edge = (EdgeShape)fixture.getShape();
				//System.out.println("box2d " + edge.m_vertex1.x + ", " + edge.m_vertex1.y + ", " + edge.m_vertex2.x + ", " + edge.m_vertex2.y);										
				edgeDrawer.addLine(	edge.m_vertex1.x, edge.m_vertex1.y, 0.0f,
									edge.m_vertex2.x, edge.m_vertex2.y, 0.0f);
				
				fixture = fixture.getNext();
			}
		}
		
		setIdentityM(visuals.modelMatrix, 0);
		multiplyMM(visuals.mvpMatrix, 0, visuals.viewProjectionMatrix, 0, visuals.modelMatrix, 0);
		
		visuals.colorShader.useProgram();
		visuals.colorShader.setUniforms(visuals.mvpMatrix, color);
		edgeDrawer.bindData(visuals.colorShader);
		edgeDrawer.draw();
	}

    private void drawPhysicsStatics() {
        visuals.colorShader.useProgram();

        Body body;
        Vec2 pos;
        float angle;
        float degree;
        Fixture fixture;
        PolygonShape polygon;
        EdgeDrawer edgeDrawer = new EdgeDrawer(100);
        int size = physics.statics.size();
        for(int i = 0; i < size; ++i) {
            body = physics.statics.get(i);
            pos = body.getPosition();
            angle = body.getAngle();
            degree = (float) Math.toDegrees(angle);

            fixture = body.getFixtureList();
            while(fixture != null) {
                polygon = (PolygonShape)fixture.getShape();
                if (polygon.m_count == 4) { // box
                    edgeDrawer.begin();
                    Vec2 v0 = polygon.m_vertices[0];
                    Vec2 v1 = polygon.m_vertices[1];
                    Vec2 v2 = polygon.m_vertices[2];
                    Vec2 v3 = polygon.m_vertices[3];

                    edgeDrawer.addLine(	v0.x, v0.y, 0.0f,   v1.x, v1.y, 0.0f);
                    edgeDrawer.addLine(	v1.x, v1.y, 0.0f,   v2.x, v2.y, 0.0f);
                    edgeDrawer.addLine(	v2.x, v2.y, 0.0f,   v3.x, v3.y, 0.0f);
                    edgeDrawer.addLine(	v3.x, v3.y, 0.0f,   v0.x, v0.y, 0.0f);

                    setIdentityM(visuals.modelMatrix, 0);
                    translateM(visuals.modelMatrix, 0, pos.x, pos.y, 1.0f);
                    rotateM(visuals.modelMatrix, 0, degree, 0.0f, 0.0f, 1.0f);
                    multiplyMM(visuals.mvpMatrix, 0, visuals.viewProjectionMatrix, 0, visuals.modelMatrix, 0);

                    visuals.colorShader.setUniforms(visuals.mvpMatrix, visuals.whiteColor);
                    edgeDrawer.bindData(visuals.colorShader);
                    edgeDrawer.draw();
                }
                fixture = fixture.getNext();
            }
        }
    }

	private void drawPhysicsGemsFixtures() {
        visuals.colorShader.useProgram();

		EdgeDrawer edgeDrawer = new EdgeDrawer(30);
		int size = physics.fragments.size();
		Body body;
        Vec2 pos;
        float degree;
        float angle;
        Fixture fixture;
        PolygonShape polygon;
        Vec2 v0;
        Vec2 v1;
		for(int i = 0; i < size; ++i) {
			body = physics.fragments.get(i);
			pos = body.getPosition();
			angle = body.getAngle();
			degree = (float)Math.toDegrees(angle);
			fixture = body.getFixtureList();

			while(fixture != null) {
				polygon = (PolygonShape)fixture.getShape();
                edgeDrawer.begin();
				for(int j = 0; j < polygon.m_count-1; ++j) {
                    v0 = polygon.m_vertices[j];
                    v1 = polygon.m_vertices[j + 1];

                    edgeDrawer.addLine(v0.x, v0.y, 0.0f,
                                       v1.x, v1.y, 0.0f);
                }

                v0 = polygon.m_vertices[0];
                v1 = polygon.m_vertices[ polygon.m_count - 1];

                edgeDrawer.addLine(v0.x, v0.y, 0.0f,
                        v1.x, v1.y, 0.0f);

                setIdentityM(visuals.modelMatrix, 0);
                translateM(visuals.modelMatrix, 0, pos.x, pos.y, 1f);
                rotateM(visuals.modelMatrix, 0, degree, 0.0f, 0.0f, 1.0f);
                multiplyMM(visuals.mvpMatrix, 0, visuals.viewProjectionMatrix, 0, visuals.modelMatrix, 0);

                visuals.colorShader.setUniforms(visuals.mvpMatrix, visuals.whiteColor);
                edgeDrawer.bindData(visuals.colorShader);
                edgeDrawer.draw();

                fixture = fixture.getNext();
			}
		}
	}

	void drawSelectionMarker() {				
		if (match3.firstSelected != null) {
			elapsedTimeSelMarkerAnim += 0.3f;
			float d = (((float)Math.sin(elapsedTimeSelMarkerAnim) + 1.0f) / 2f) * 0.08f;
			//System.out.println("d is: " + d);

			markerPos.init(match3.firstSelected.pos);
			markerPos.tz -= 0.2f;
            markerPos.scale(1.2f + d, 1.2f + d, 1.0f);

			visuals.calcMatricesForObject(markerPos);
			visuals.pointLightShader.setUniforms();
			visuals.marker.bindData(visuals.pointLightShader);
			visuals.marker.draw();
		}
	}

    void drawGems() {
        singleton.batchDrawer.begin();
        singleton.batchDrawer.add(match3);
        singleton.batchDrawer.add(animManager);
        singleton.batchDrawer.add(physics);
        singleton.batchDrawer.drawAll();
    }

	void drawRailRoad() {
        visuals.railroad.bindData(visuals.dirLightShader);

		float x = -10f;
		float y = -20.0f;
		float z =  1.0f;
        float tempZ;
		for(int i = 0; i < 3; ++i) {
            tempZ = z;
			pos.trans(x, y, z);
			pos.rot(0f, 0f, 0f);
			pos.scale(1f, 1f, 1f);

			visuals.calcMatricesForObject(pos);
			visuals.dirLightShader.setUniforms();
			visuals.railroad.draw();

			z = tempZ;
			x += 10.0f;
		}		
	}	
	
	void drawRock(Model rock, float x, float y, float z, float degree) {
		pos.trans(x, y, z);
        pos.rot(0f, 0f, degree); // ???
        pos.scale(1f, 1f, 1f);
		
		visuals.calcMatricesForObject(pos);
		visuals.dirLightShader.setUniforms();
		rock.bindData(visuals.dirLightShader);
		rock.draw();		
	}

	void drawPickAxes() {
		Model pickAxe = visuals.pickAxe;

		pos.trans(9f, -20f, 4.0f);
        pos.rot(90f, 30f, 50f);
        pos.scale(1f, 1f, 1f);

		visuals.calcMatricesForObject(pos);
		visuals.dirLightShader.setUniforms();
		pickAxe.bindData(visuals.dirLightShader);
		pickAxe.draw();				
	}
			
	void drawRocks() {
        // rock types
//		float y = -15f;
//		float z = 0f;
//		float degree = 0f;
//		drawRock(visuals.rock0,  13.5f, y, z, degree);		
//		drawRock(visuals.rock1,  10.0f, y, z, degree);		
//		drawRock(visuals.rock2,	  7.0f, y, z, degree);					
//		drawRock(visuals.rock3,	  4.0f, y, z, degree);		
//		drawRock(visuals.rock4,	  1.0f, y, z, degree);			
//		drawRock(visuals.rock5,	 -3.0f, y, z, degree);			
//		drawRock(visuals.rock6,  -7.0f, y, z, degree);		
//		drawRock(visuals.rock7, -10.0f, y, z, degree);		
//		drawRock(visuals.rock8, -13.5f, y, z, degree);
	
		RockData rock;
		int size = rocks.size();
		for (int i = 0; i < size; ++i) {
			rock = rocks.get(i);
			switch(rock.rockId) {
				case 0: drawRock(visuals.rock0, rock.x, rock.y, rock.z, rock.degree); break;		
				case 1: drawRock(visuals.rock1, rock.x, rock.y, rock.z, rock.degree); break;
				case 2: drawRock(visuals.rock2,	rock.x, rock.y, rock.z, rock.degree); break;			
				case 3: drawRock(visuals.rock3,	rock.x, rock.y, rock.z, rock.degree); break;
				case 4: drawRock(visuals.rock4,	rock.x, rock.y, rock.z, rock.degree); break;	
				case 5: drawRock(visuals.rock5,	rock.x, rock.y, rock.z, rock.degree); break;	
				case 6: drawRock(visuals.rock6, rock.x, rock.y, rock.z, rock.degree); break;
				case 7: drawRock(visuals.rock7, rock.x, rock.y, rock.z, rock.degree); break;
				case 8: drawRock(visuals.rock8, rock.x, rock.y, rock.z, rock.degree); break;
			}
		}
		
		float rock_z = -1.75f;
				
		// left
		drawRock(visuals.rock0, -10.5f, -7.0f, rock_z, 0.0f);
		drawRock(visuals.rock3,  -8.0f, -8.0f, rock_z, -14.0f);				
		drawRock(visuals.rock1, -13.5f, -6.5f, -1.75f, -50.0f);
		drawRock(visuals.rock6, -5.5f, -9.0f, -1.75f, 22.0f);
		drawRock(visuals.rock6, -3.5f, -9.5f, -1.75f, 62.0f);	
		drawRock(visuals.rock0, -12.0f, -8.0f, -2.25f, 6.0f);
		drawRock(visuals.rock0, -14.0f, -4.0f, -4.25f, -90.0f);		
		drawRock(visuals.rock0, -4.0f, -9.5f, -3.75f, -8.0f);
		
		// right
		drawRock(visuals.rock1,  9.0f, -8.0f, rock_z, 70.0f);
		drawRock(visuals.rock0,  5.8f, -9.0f, rock_z, -70.0f);
		drawRock(visuals.rock6,  3.5f, -9.7f, rock_z, 0.0f);
		drawRock(visuals.rock1, 13.0f, -6.5f, rock_z, -30.0f);
		drawRock(visuals.rock0, 4.5f, -9.5f, -3.75f, 46.0f);
		drawRock(visuals.rock2, 11.5f, -8.5f, -1.25f, 16.0f);
		drawRock(visuals.rock5, 14.5f, -7.5f, -1.25f, 8.0f);		
		drawRock(visuals.rock0, 11.5f, -7.0f, -3.75f, 0.0f);
		
		//decor
		drawRock(visuals.rock7, 1.0f, -6.0f, -5.0f, 0.0f);
		drawRock(visuals.rock0, 12.5f, 14.5f, -5.0f, -4.0f);		
		drawRock(visuals.rock5, 11.5f, -22.0f, 3.5f, -32.0f);
		drawRock(visuals.rock1, 1.5f, -22.0f, 3.5f, 0.0f);
		drawRock(visuals.rock0, 12.0f, 0.0f, -4.5f, 0.0f);
		drawRock(visuals.rock1, 12.0f, 7.5f, -5.0f, -26.0f);
		drawRock(visuals.rock5, 15.5f, -21.5f, -3.5f, 0.0f);
		drawRock(visuals.rock4, 14.5f, 3.0f, -4.5f, 0.0f);
		drawRock(visuals.rock7, 5.0f, -0.5f, -5.0f, -144.0f);
				
		drawRock(visuals.rock3, -12.0f, 11.5f, -5.0f, 0.0f);
		drawRock(visuals.rock5, -4.0f, -21.5f, -3.5f, 14.0f);
		drawRock(visuals.rock3, -12.0f, -21.5f, 4.0f, 8.0f);
		drawRock(visuals.rock8, -14.0f, 8.0f, -5.0f, -158.0f);
		drawRock(visuals.rock3, -0.5f, 2.5f, -5.0f, -16.0f);
		
		drawRock(visuals.rock2, -1.5f, -9.5f, -4.25f, -12.0f);
		drawRock(visuals.rock3, 1.5f, -9.5f, -4.25f, 6.0f);
		drawRock(visuals.rock5, -11.5f, 0.0f, -4.75f, 8.0f);		
	}
	
	void drawFloor() {
        pos.trans(0f, -20.5f, 0f);
        pos.rot(0f, 0f, 0f);
        pos.scale(1.0f, 1.0f, 1.0f);
        visuals.calcMatricesForObject(pos);
        visuals.dirLightShader.setUniforms();
        visuals.floor.bindData(visuals.dirLightShader);
        visuals.floor.bind();
        visuals.floor.draw();
        visuals.floor.unbind();
    }

    void drawWall() {
        pos.trans(0f, -15f, -4f);
        pos.rot(0f, 0f, 0f);
        pos.scale(1f, 1f, 1f);
        visuals.calcMatricesForObject(pos);
        visuals.dirLightShader.setUniforms();
        visuals.wall.bindData(visuals.dirLightShader);
        visuals.wall.bind();
        visuals.wall.draw();
        visuals.wall.unbind();
    }

    void drawSoil() {
        pos.trans(0f, 6f, -3.5f);
        pos.rot(0f, 0f, 0f);
        pos.scale(1.9f, 1.6f, 1.0f);
		visuals.calcMatricesForObject(pos);
		visuals.dirLightShader.setUniforms();
		visuals.soil.bindData(visuals.dirLightShader);
		visuals.soil.draw();
	}
	
	void drawCrates() {
        visuals.crate.bindData(visuals.dirLightShader);

		pos.trans(-12.5f, -18.6f, -2.0f);
		pos.rot(0.0f, -20.0f, 0.0f);
		pos.scale(1.0f, 1.0f, 1.0f);
		visuals.calcMatricesForObject(pos);
		visuals.dirLightShader.setUniforms();
		visuals.crate.draw();

		pos.trans(12.0f, -18.6f, -2.0f);
		pos.rot(0.0f, 2.0f, 0.0f);
		pos.scale(1.0f, 1.0f, 1.0f);
		visuals.calcMatricesForObject(pos);
		visuals.dirLightShader.setUniforms();
		visuals.crate.draw();	
	}

	void drawBeam() {						
		pos.trans(0f, -16f, -2.5f);
		pos.rot(0f, 0f, 0f);
		pos.scale(1.0f, 1.0f, 1.0f);
		visuals.calcMatricesForObject(pos);
		visuals.dirLightShader.setUniforms();
		visuals.beam.bindData(visuals.dirLightShader);
		visuals.beam.draw();		
	}
	
	void drawPillars() {
        visuals.pillar.bindData(visuals.dirLightShader);

		pos.trans(-9.5f, -16f, -2.5f);
		pos.rot(0f, 0f, 0f);
		pos.scale(1.0f, 1.0f, 1.0f);
		visuals.calcMatricesForObject(pos);
		visuals.dirLightShader.setUniforms();
		visuals.pillar.draw();		

		pos.trans(9.5f, -16f, -2.5f);
		pos.rot(0f, 0f, 0f);
		pos.scale(1.0f, 1.0f, 1.0f);
		visuals.calcMatricesForObject(pos);
		visuals.dirLightShader.setUniforms();
		visuals.pillar.draw();				
	}
		
	void drawMineCarts() {
		singleton.cart1.draw();
        singleton.cart2.draw();
	}

}
