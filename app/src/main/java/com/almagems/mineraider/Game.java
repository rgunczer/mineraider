package com.almagems.mineraider;

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
import static com.almagems.mineraider.Constants.*;


public final class Game extends Scene {

    public enum GameState {
        Loading,
        Menu,
        Stats,
        Playing,
    }

    public GameState gameState;

    private boolean initialized;

    // overlays
    private Menu menu;
    private Loading loading;
    private Stats stats;


    private Graphics graphics;
	private Match3 match3;
    private Quad background;
    public HUD hud;
    public ScoreCounter scoreCounter;

    public MineCart cart1;
    public MineCart cart2;

    private final PositionInfo markerPos;
	private float elapsedTimeSelMarkerAnim;

	private boolean editorEnabled;
	private AnimationManager animManager;
	private final ArrayList<RockData> rocks;
	private final PositionInfo pos;


    // ctor
	public Game() {
        initialized = false;
        swipeDir = SwipeDir.SwipeNone;
        gameState = GameState.Loading;
        markerPos = new PositionInfo();
        elapsedTimeSelMarkerAnim = 0f;
        editorEnabled = false;
        pos = new PositionInfo();
        rocks = new ArrayList<RockData>();
    }

    public void init() {
        graphics = Engine.graphics;

        CollisionHandler.game = this;
        ScoreCounter.game = this;

        StatSectionBase.graphics = graphics;
        SingleColoredQuad.graphics = graphics;
        ColoredQuad.graphics = graphics;
        Overlay.graphics = graphics;
        Quad.graphics = graphics;
        ProgressBarControl.graphics = graphics;
        Text.graphics = graphics;
        HUD.graphics = graphics;
        MenuItem.graphics = graphics;
        MenuImage.graphics = graphics;
        MenuGameTitleAnim.graphics = graphics;
        MenuGroup.graphics = graphics;
        EffectAnim.graphics = graphics;

        background = new Quad();
        loading = new Loading();
    }

    public void createObjects() {
        if (initialized) {
            return;
        }

        initialized = true;

        scoreCounter = new ScoreCounter();
		animManager = new AnimationManager();
		match3 = new Match3(8, animManager, scoreCounter);

		initBoardGeometry();		

		//physics.addEdge(-13.5f, -21.5f,  13.5f, -21.5f); // bottom
		Physics.addEdge(-13.0f, -7.0f, -13.5f, 20.0f); // left
		Physics.addEdge(13.0f, -7.0f, 13.5f, 20.0f); // right
		
		Physics.addBoxStatic(8.0f, -5.9f, 24.8f, 12.0f, 0.8f);
		Physics.addBoxStatic(-7.8f, -5.9f, -25.0f, 12.0f, 0.8f);
		Physics.addBoxStatic(3.1f, -8.8f, 0f, 1.4f, 1.4f);
		Physics.addBoxStatic(-3.0f, -8.8f, 0f, 1.4f, 1.4f);
		
		// sin
		Physics.addBoxStatic(0.0f, -19.7f, 0f, 70.0f, 0.5f);

        float cartX = -20f;
        float cartX2nd = -30f;
        float cartY = -15f;

        float x = cartX; //-20f;
		float y = cartY; //-15.7f;
        MineCart.graphics = graphics;
		MineCart mineCart;
		
		mineCart = new MineCart(x, y);
        mineCart.id = 1;
		cart1 = mineCart;

		x = cartX2nd;
		mineCart = new MineCart(x, y);
        mineCart.id = 2;
		cart2 = mineCart;

        menu = new Menu();
        stats = new Stats();

        hud = new HUD();
        hud.init();
        hud.reset();
        hud.updateScore(scoreCounter.getScore());

        ScoreCounter.hud = hud;

        loading.init();
        menu.init();

        final float cartSpeed = -3f;

        cart1.reposition(cartX, cartY);
        cart1.start(cartSpeed);

        cart2.reposition(cartX2nd, cartY);
        cart2.start(cartSpeed);

        Physics.clear();
        animManager.clear();

        match3.placeInitialGems();
        match3.dumpBoardStat();
        match3.createInitialFallAnim();
	}

    public void renderToFBO() {
        graphics.fboBackground.bind();
        glViewport(0, 0, graphics.fboBackground.getWidth(), graphics.fboBackground.getHeight());

        // regular render
        glClearColor(0f, 0f, 0f, 0f);
        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);

        if (false) { // render test
            glDisable(GL_DEPTH_TEST);
            glDisable(GL_BLEND);

            graphics.setProjectionMatrix2D();
            graphics.updateViewProjMatrix();

            // custom drawing
            graphics.bindNoTexture();
            glDisable(GL_TEXTURE_2D);

            Color color = new Color(0f, 0f, 0f);
            EdgeDrawer edgeDrawer = new EdgeDrawer(10);
            edgeDrawer.begin();

            //System.out.println("box2d " + edge.m_vertex1.x + ", " + edge.m_vertex1.y + ", " + edge.m_vertex2.x + ", " + edge.m_vertex2.y);

            edgeDrawer.addLine(-0.5f, 0.5f, 0f, 0.5f, 0.0f, 0f);
            edgeDrawer.addLine(0.5f, 0f, 0f, -0.5f, -0.5f, 0f);
            edgeDrawer.addLine(-0.5f, -0.5f, 0f, -0.5f, 0.5f, 0f);

            setIdentityM(graphics.modelMatrix, 0);
            multiplyMM(graphics.mvpMatrix, 0, graphics.viewProjectionMatrix, 0, graphics.modelMatrix, 0);

            graphics.singleColorShader.useProgram();
            graphics.singleColorShader.setUniforms(graphics.mvpMatrix, color);
            edgeDrawer.bindData(graphics.singleColorShader);
            edgeDrawer.draw();
        }

        if (true) {
            graphics.setProjectionMatrix3D();
            graphics.updateViewProjMatrix();

            glDisable(GL_BLEND);
            glEnable(GL_DEPTH_TEST);

            drawBackgroundAndDecoration();
        }

        graphics.fboBackground.unbind();
        background.initWithFBOTexture(graphics.fboBackground.getTextureId());

        glViewport(0, 0, (int) Graphics.screenWidth, (int) Graphics.screenHeight);
        glClearColor(0f, 0f, 0f, 1f);
    }

	public void onSurfaceChanged(int width, int height) {
        loading.init();
    }

	public void update() {
        graphics.updateViewProjMatrix();

        if (gameState == GameState.Loading) {
            loading.update();
            if (loading.done) {
                menu.reset();
                gameState = GameState.Menu;
                update();
            }
        } else {
            Physics.update();
            match3.update();

            cart1.update();
            cart2.update();

            hud.update();
            hud.updateScore(scoreCounter.getScore());

            switch (gameState) {
                case Menu:
                    menu.update();
                    switch (menu.getSelectedMenuOption()) {
                        case Play:
                            gameState = GameState.Playing;
                            break;

                        case Stats:
                            gameState = GameState.Stats;
                            menu.resetSelectedMenuOption();
                            stats.init();
                            stats.update();
                            break;
                    }
                    break;

                case Stats:
                    stats.update();
                    if (stats.done) {
                        menu.resetBackground();
                        gameState = GameState.Menu;
                    }
                    break;

                case Playing:
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
            // 2d drawing
            graphics.setProjectionMatrix2D();
            graphics.updateViewProjMatrix();

            glDisable(GL_BLEND);
            glDepthMask(false);
            background.draw();
            glDepthMask(true);

            // 3d drawing
            graphics.setProjectionMatrix3D();
            graphics.updateViewProjMatrix();

            glEnable(GL_DEPTH_TEST);

            // draw gems
            graphics.dirLightShader.setTexture(Graphics.textureGems);
            graphics.batchGemDrawer.begin();
            graphics.batchGemDrawer.add(match3);
            graphics.batchGemDrawer.add(animManager);
            graphics.batchGemDrawer.addPhysics();
            graphics.batchGemDrawer.drawAll();

            if (match3.firstSelected != null) {
                elapsedTimeSelMarkerAnim += 0.3f;
                float d = (((float)Math.sin(elapsedTimeSelMarkerAnim) + 1.0f) / 2f) * 0.08f;

                markerPos.init(match3.firstSelected.pos);
                markerPos.tz -= 0.2f;
                markerPos.scale(1.2f + d, 1.2f + d, 1.0f);

                graphics.calcMatricesForObject(markerPos);
                graphics.pointLightShader.setUniforms();
                graphics.marker.bindData(graphics.pointLightShader);
                graphics.marker.draw();
            }

            if (!match3.swapHintManager.isEmpty()) {
                glDisable(GL_DEPTH_TEST);
                graphics.pointLightShader.useProgram();
                graphics.hint.bindData(graphics.pointLightShader);
                match3.swapHintManager.draw();
                glEnable(GL_DEPTH_TEST);
            }

            // draw minecarts
            graphics.dirLightShader.useProgram();
//            cart1.draw();
//            cart2.draw();

            graphics.dirLightShader.setTexture(Graphics.textureCart);
            graphics.mineCart.bindData(graphics.dirLightShader);
            cart1.drawCart();
            cart2.drawCart();

            graphics.dirLightShader.setTexture(Graphics.textureWheel);
            graphics.wheel.bindData(graphics.dirLightShader);
            cart1.drawWheels();
            cart2.drawWheels();


            // particle system
            glEnable(GL_BLEND);
            glDisable(GL_DEPTH_TEST);
            glBlendFunc(GL_ONE, GL_ONE);

            graphics.particleManager.draw();

            // 2d drawing (HUD and Menu)
            graphics.setProjectionMatrix2D();
            graphics.updateViewProjMatrix();

            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            hud.draw();

            if (gameState == GameState.Menu) {
                menu.draw();
            } else if (gameState == GameState.Stats) {
                stats.draw();
            } else {
                graphics.setProjectionMatrix3D();
                graphics.updateViewProjMatrix();
            }
        }
	}

    private void drawBackgroundAndDecoration() {
        graphics.dirLightShader.useProgram();

        graphics.dirLightShader.setTexture(Graphics.textureFloor);
        drawFloor();

        graphics.dirLightShader.setTexture(Graphics.textureWall);
        drawWall();

        graphics.dirLightShader.setTexture(Graphics.textureSoil);
        drawSoil();

        graphics.dirLightShader.setTexture(Graphics.textureCliff142);
        drawRocks();

        graphics.dirLightShader.setTexture(Graphics.texturePillar);
        drawPillars();

        graphics.dirLightShader.setTexture(Graphics.textureBeam);
        drawBeam();

        graphics.dirLightShader.setTexture(Graphics.textureRailRoad);
        drawRailRoad();

        graphics.dirLightShader.setTexture(Graphics.textureCrate);
        drawCrates();
    }

	public void handleTouchPress(float normalizedX, float normalizedY) {
        switch (gameState) {
            case Playing:
                handleTouchPressOnPlaying(normalizedX, normalizedY);
                break;

            case Menu:
                menu.handleTouchPress(normalizedX, normalizedY);
                break;

            case Stats:
                stats.handleTouchPress(normalizedX, normalizedY);
                break;

            case Loading:
                // do nothing
                break;
        }
    }

    private void handleTouchPressOnPlaying(float normalizedX, float normalizedY) {
        if (normalizedY < -0.86f ) {
            if (normalizedX > 0.86f) {
                gameState = GameState.Menu;
                menu.reset();
            } else if (normalizedX < -0.65f) {
                //gameState = GameState.Stats;
                //menu.reset();
            }
        } else {
            swipeDir = SwipeDir.SwipeNone;
            touchDownX = normalizedX;
            touchDownY = normalizedY;

            if (!match3.isAnimating) {
                Ray ray = Geometry.convertNormalized2DPointToRay(touchDownX, touchDownY, graphics.invertedViewProjectionMatrix);
                GemPosition selectedGem = getSelectedGemFromRay(ray);

                if (selectedGem != null) {
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

            case Stats:
                stats.handleTouchDrag(normalizedX, normalizedY);
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
        switch (gameState) {
            case Playing:
                handleTouchReleseOnPlaying(normalizedX, normalizedY);
                break;

            case Stats:
                stats.handleTouchRelease(normalizedX, normalizedY);
                break;
        }
    }

    private void handleTouchReleseOnPlaying(float normalizedX, float normalizedY) {
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

    private void drawPhysics() {
        glDisable(GL_DEPTH_TEST);
        drawPhysicsStatics();
        drawPhysicsGemsFixtures();
        drawPhysicsEdges();
        glEnable(GL_DEPTH_TEST);
    }

	private void drawPhysicsEdges() {
		Color color = new Color(1.0f, 1.0f, 1.0f);
		EdgeDrawer edgeDrawer = new EdgeDrawer(10);		
		edgeDrawer.begin();
		
		for(Body body : Physics.edges) {
			Fixture fixture = body.getFixtureList();
			while(fixture != null) {
				EdgeShape edge = (EdgeShape)fixture.getShape();
				//System.out.println("box2d " + edge.m_vertex1.x + ", " + edge.m_vertex1.y + ", " + edge.m_vertex2.x + ", " + edge.m_vertex2.y);										
				edgeDrawer.addLine(	edge.m_vertex1.x, edge.m_vertex1.y, 0.0f,
									edge.m_vertex2.x, edge.m_vertex2.y, 0.0f);
				
				fixture = fixture.getNext();
			}
		}
		
		setIdentityM(graphics.modelMatrix, 0);
		multiplyMM(graphics.mvpMatrix, 0, graphics.viewProjectionMatrix, 0, graphics.modelMatrix, 0);
		
		graphics.singleColorShader.useProgram();
		graphics.singleColorShader.setUniforms(graphics.mvpMatrix, color);
		edgeDrawer.bindData(graphics.singleColorShader);
		edgeDrawer.draw();
	}

    private void drawPhysicsStatics() {
        graphics.singleColorShader.useProgram();

        Body body;
        Vec2 pos;
        float angle;
        float degree;
        Fixture fixture;
        PolygonShape polygon;
        EdgeDrawer edgeDrawer = new EdgeDrawer(100);
        int size = Physics.statics.size();
        for(int i = 0; i < size; ++i) {
            body = Physics.statics.get(i);
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

                    setIdentityM(graphics.modelMatrix, 0);
                    translateM(graphics.modelMatrix, 0, pos.x, pos.y, 1.0f);
                    rotateM(graphics.modelMatrix, 0, degree, 0.0f, 0.0f, 1.0f);
                    multiplyMM(graphics.mvpMatrix, 0, graphics.viewProjectionMatrix, 0, graphics.modelMatrix, 0);

                    graphics.singleColorShader.setUniforms(graphics.mvpMatrix, Color.WHITE);
                    edgeDrawer.bindData(graphics.singleColorShader);
                    edgeDrawer.draw();
                }
                fixture = fixture.getNext();
            }
        }
    }

	private void drawPhysicsGemsFixtures() {
        graphics.singleColorShader.useProgram();

		EdgeDrawer edgeDrawer = new EdgeDrawer(30);
		int size = Physics.fragments.size();
		Body body;
        Vec2 pos;
        float degree;
        float angle;
        Fixture fixture;
        PolygonShape polygon;
        Vec2 v0;
        Vec2 v1;
		for(int i = 0; i < size; ++i) {
			body = Physics.fragments.get(i);
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

                setIdentityM(graphics.modelMatrix, 0);
                translateM(graphics.modelMatrix, 0, pos.x, pos.y, 1f);
                rotateM(graphics.modelMatrix, 0, degree, 0.0f, 0.0f, 1.0f);
                multiplyMM(graphics.mvpMatrix, 0, graphics.viewProjectionMatrix, 0, graphics.modelMatrix, 0);

                graphics.singleColorShader.setUniforms(graphics.mvpMatrix, Color.WHITE);
                edgeDrawer.bindData(graphics.singleColorShader);
                edgeDrawer.draw();

                fixture = fixture.getNext();
			}
		}
	}

	private void drawRailRoad() {
        graphics.railroad.bindData(graphics.dirLightShader);

		float x = -10f;
		float y = -20.0f;
		float z =  1.0f;
        float tempZ;
		for(int i = 0; i < 3; ++i) {
            tempZ = z;
			pos.trans(x, y, z);
			pos.rot(0f, 0f, 0f);
			pos.scale(1f, 1f, 1f);

			graphics.calcMatricesForObject(pos);
			graphics.dirLightShader.setUniforms();
			graphics.railroad.draw();

			z = tempZ;
			x += 10.0f;
		}		
	}	
	
	private void drawRock(Model rock, float x, float y, float z, float degree) {
		pos.trans(x, y, z);
        pos.rot(0f, 0f, degree); // ???
        pos.scale(1f, 1f, 1f);
		
		graphics.calcMatricesForObject(pos);
		graphics.dirLightShader.setUniforms();
		rock.bindData(graphics.dirLightShader);
		rock.draw();		
	}

	private void drawPickAxes() {
		Model pickAxe = graphics.pickAxe;

		pos.trans(9f, -20f, 4.0f);
        pos.rot(90f, 30f, 50f);
        pos.scale(1f, 1f, 1f);

		graphics.calcMatricesForObject(pos);
		graphics.dirLightShader.setUniforms();
		pickAxe.bindData(graphics.dirLightShader);
		pickAxe.draw();				
	}
			
	private void drawRocks() {
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
				case 0: drawRock(graphics.rock0, rock.x, rock.y, rock.z, rock.degree); break;
				case 1: drawRock(graphics.rock1, rock.x, rock.y, rock.z, rock.degree); break;
				case 2: drawRock(graphics.rock2,	rock.x, rock.y, rock.z, rock.degree); break;
				case 3: drawRock(graphics.rock3,	rock.x, rock.y, rock.z, rock.degree); break;
				case 4: drawRock(graphics.rock4,	rock.x, rock.y, rock.z, rock.degree); break;
				case 5: drawRock(graphics.rock5,	rock.x, rock.y, rock.z, rock.degree); break;
				case 6: drawRock(graphics.rock6, rock.x, rock.y, rock.z, rock.degree); break;
				case 7: drawRock(graphics.rock7, rock.x, rock.y, rock.z, rock.degree); break;
				case 8: drawRock(graphics.rock8, rock.x, rock.y, rock.z, rock.degree); break;
			}
		}
		
		float rock_z = -1.75f;
				
		// left
		drawRock(graphics.rock0, -10.5f, -7.0f, rock_z, 0.0f);
		drawRock(graphics.rock3,  -8.0f, -8.0f, rock_z, -14.0f);
		drawRock(graphics.rock1, -13.5f, -6.5f, -1.75f, -50.0f);
		drawRock(graphics.rock6, -5.5f, -9.0f, -1.75f, 22.0f);
		drawRock(graphics.rock6, -3.5f, -9.5f, -1.75f, 62.0f);
		drawRock(graphics.rock0, -12.0f, -8.0f, -2.25f, 6.0f);
		drawRock(graphics.rock0, -14.0f, -4.0f, -4.25f, -90.0f);
		drawRock(graphics.rock0, -4.0f, -9.5f, -3.75f, -8.0f);
		
		// right
		drawRock(graphics.rock1,  9.0f, -8.0f, rock_z, 70.0f);
		drawRock(graphics.rock0,  5.8f, -9.0f, rock_z, -70.0f);
		drawRock(graphics.rock6,  3.5f, -9.7f, rock_z, 0.0f);
		drawRock(graphics.rock1, 13.0f, -6.5f, rock_z, -30.0f);
		drawRock(graphics.rock0, 4.5f, -9.5f, -3.75f, 46.0f);
		drawRock(graphics.rock2, 11.5f, -8.5f, -1.25f, 16.0f);
		drawRock(graphics.rock5, 14.5f, -7.5f, -1.25f, 8.0f);
		drawRock(graphics.rock0, 11.5f, -7.0f, -3.75f, 0.0f);
		
		//decor
		drawRock(graphics.rock7, 1.0f, -6.0f, -5.0f, 0.0f);
		drawRock(graphics.rock0, 12.5f, 14.5f, -5.0f, -4.0f);
		drawRock(graphics.rock5, 11.5f, -22.0f, 3.5f, -32.0f);
		drawRock(graphics.rock1, 1.5f, -22.0f, 3.5f, 0.0f);
		drawRock(graphics.rock0, 12.0f, 0.0f, -4.5f, 0.0f);
		drawRock(graphics.rock1, 12.0f, 7.5f, -5.0f, -26.0f);
		drawRock(graphics.rock5, 15.5f, -21.5f, -3.5f, 0.0f);
		drawRock(graphics.rock4, 14.5f, 3.0f, -4.5f, 0.0f);
		drawRock(graphics.rock7, 5.0f, -0.5f, -5.0f, -144.0f);
				
		drawRock(graphics.rock3, -12.0f, 11.5f, -5.0f, 0.0f);
		drawRock(graphics.rock5, -4.0f, -21.5f, -3.5f, 14.0f);
		drawRock(graphics.rock3, -12.0f, -21.5f, 4.0f, 8.0f);
		drawRock(graphics.rock8, -14.0f, 8.0f, -5.0f, -158.0f);
		drawRock(graphics.rock3, -0.5f, 2.5f, -5.0f, -16.0f);
		
		drawRock(graphics.rock2, -1.5f, -9.5f, -4.25f, -12.0f);
		drawRock(graphics.rock3, 1.5f, -9.5f, -4.25f, 6.0f);
		drawRock(graphics.rock5, -11.5f, 0.0f, -4.75f, 8.0f);
	}
	
	private void drawFloor() {
        pos.trans(0f, -20.5f, 0f);
        pos.rot(0f, 0f, 0f);
        pos.scale(1.0f, 1.0f, 1.0f);
        graphics.calcMatricesForObject(pos);
        graphics.dirLightShader.setUniforms();
        graphics.floor.bindData(graphics.dirLightShader);
        graphics.floor.bind();
        graphics.floor.draw();
        graphics.floor.unbind();
    }

    private void drawWall() {
        pos.trans(0f, -15f, -4f);
        pos.rot(0f, 0f, 0f);
        pos.scale(1f, 1f, 1f);
        graphics.calcMatricesForObject(pos);
        graphics.dirLightShader.setUniforms();
        graphics.wall.bindData(graphics.dirLightShader);
        graphics.wall.bind();
        graphics.wall.draw();
        graphics.wall.unbind();
    }

    private void drawSoil() {
        pos.trans(0f, 6f, -3.5f);
        pos.rot(0f, 0f, 0f);
        pos.scale(1.9f, 1.6f, 1.0f);
		graphics.calcMatricesForObject(pos);
		graphics.dirLightShader.setUniforms();
		graphics.soil.bindData(graphics.dirLightShader);
		graphics.soil.draw();
	}
	
	private void drawCrates() {
        graphics.crate.bindData(graphics.dirLightShader);

		pos.trans(-12.5f, -18.6f, -2.0f);
		pos.rot(0.0f, -20.0f, 0.0f);
		pos.scale(1.0f, 1.0f, 1.0f);
		graphics.calcMatricesForObject(pos);
		graphics.dirLightShader.setUniforms();
		graphics.crate.draw();

		pos.trans(12.0f, -18.6f, -2.0f);
		pos.rot(0.0f, 2.0f, 0.0f);
		pos.scale(1.0f, 1.0f, 1.0f);
		graphics.calcMatricesForObject(pos);
		graphics.dirLightShader.setUniforms();
		graphics.crate.draw();
	}

	private void drawBeam() {
		pos.trans(0f, -16f, -2.5f);
		pos.rot(0f, 0f, 0f);
		pos.scale(1.0f, 1.0f, 1.0f);
		graphics.calcMatricesForObject(pos);
		graphics.dirLightShader.setUniforms();
		graphics.beam.bindData(graphics.dirLightShader);
		graphics.beam.draw();
	}
	
	private void drawPillars() {
        graphics.pillar.bindData(graphics.dirLightShader);

		pos.trans(-9.5f, -16f, -2.5f);
		pos.rot(0f, 0f, 0f);
		pos.scale(1.0f, 1.0f, 1.0f);
		graphics.calcMatricesForObject(pos);
		graphics.dirLightShader.setUniforms();
		graphics.pillar.draw();

		pos.trans(9.5f, -16f, -2.5f);
		pos.rot(0f, 0f, 0f);
		pos.scale(1.0f, 1.0f, 1.0f);
		graphics.calcMatricesForObject(pos);
		graphics.dirLightShader.setUniforms();
		graphics.pillar.draw();
	}

    public void notifyOtherMinecartToStart() {
//        Vec2 pos1 = cart1.cart.getPosition();
//        Vec2 pos2 = cart2.cart.getPosition();
//
//        if (pos1.x < pos2.x) {
//            cart1.restartCart();
//        } else {
//            cart2.restartCart();
//        }

        cart1.restartCart();
        cart2.restartCart();
    }

}
