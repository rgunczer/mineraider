package com.almagems.mineraider.scenes;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_ONE;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glEnable;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.multiplyMV;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;
import static com.almagems.mineraider.Constants.*;

import java.util.ArrayList;

// box2d
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;

import com.almagems.mineraider.ClassicSingleton;
import com.almagems.mineraider.GemPosition;
import com.almagems.mineraider.Match3;
import com.almagems.mineraider.ObjectPosition;
import com.almagems.mineraider.Physics;
import com.almagems.mineraider.RockData;
import com.almagems.mineraider.Visuals;
import com.almagems.mineraider.anims.AnimationManager;
import com.almagems.mineraider.anims.PopAnimation;
import com.almagems.mineraider.objects.EdgeDrawer;
import com.almagems.mineraider.objects.MineCart;
import com.almagems.mineraider.objects.Model;
import com.almagems.mineraider.particlesystem.ParticleManager;
import com.almagems.mineraider.util.Geometry;
import com.almagems.mineraider.util.MyColor;
import com.almagems.mineraider.util.Vector;
import com.almagems.mineraider.util.Ray;

public class Level extends Scene {

	private enum SwipeDir {
		SwipeNone,
		SwipeLeft,
		SwipeRight,
		SwipeUp,
		SwipeDown
	}
	
	private SwipeDir swipeDir = SwipeDir.SwipeNone;

	private Physics physics;
	private Match3 match3;

	private float elapsed = 0f;

    private final MyColor colorBlack;
    private final MyColor colorWhite;

    private ObjectPosition markerPos = new ObjectPosition();

	private boolean editorEnabled = false;
	
	public ArrayList<MineCart> mineCarts = new ArrayList<MineCart>();
	
	private AnimationManager animManager;
	private ParticleManager particleManager;

	private ArrayList<RockData> rocks = new ArrayList<RockData>();
	
	private ObjectPosition _op = new ObjectPosition();
	
	public Level() {
        colorWhite = new MyColor(1f, 1f, 1f, 1f);
        colorBlack = new MyColor(0f, 0f, 0f, 1f);

		visuals = Visuals.getInstance();
		physics = Physics.getInstance();

		animManager = new AnimationManager();
		match3 = new Match3(8, animManager, ClassicSingleton.getInstance().scoreCounter);

		initBoardGeometry();		
				
		match3.placeInitialGems();
		match3.dumpBoardStat();
		match3.createInitialFallAnim();
	
		particleManager = ParticleManager.getInstance();
		particleManager.init();
				
		//physics.addEdge(-13.5f, -21.5f,  13.5f, -21.5f); // bottom
		physics.addEdge(-13.0f, -7.0f, -13.5f,  20.0f); // left
		physics.addEdge( 13.0f, -7.0f,  13.5f,  20.0f); // right
		
		physics.addBoxStatic(8.0f, -5.9f, 24.8f, 12.0f, 0.8f);
		physics.addBoxStatic(-7.8f, -5.9f, -25.0f, 12.0f, 0.8f);
		physics.addBoxStatic( 3.1f, -8.8f, 0f, 1.4f, 1.4f);
		physics.addBoxStatic(-3.0f, -8.8f, 0f, 1.4f, 1.4f);
		
		// sin
		physics.addBoxStatic(0.0f, -19.7f, 0f, 70.0f, 0.5f);

		float x = -20f;
		float y = -15.7f;
		MineCart mineCart;
		
		mineCart = new MineCart(x, y);
		mineCarts.add(mineCart);
		ClassicSingleton.getInstance().cart1 = mineCart;

		x = -30f;
		mineCart = new MineCart(x, y);
		mineCarts.add(mineCart);
		ClassicSingleton.getInstance().cart2 = mineCart;

        PopAnimation.physics = physics;
	}
	
	@Override
	public void surfaceChanged(int width, int height) {
		visuals.setProjectionMatrix3D();
        ClassicSingleton.getInstance().hud.init();
        ClassicSingleton.getInstance().hud.updateScore(ClassicSingleton.getInstance().getScore());
	}
	
	@Override
	public void update() {
		Visuals visuals = Visuals.getInstance();
		visuals.updateViewProjMatrix();
		
		physics.update();
		match3.update();
        ClassicSingleton.getInstance().hud.update();
        ClassicSingleton.getInstance().hud.updateScore(ClassicSingleton.getInstance().scoreCounter.getScore());
	}
	
	@Override
	public void draw() {
        visuals.setProjectionMatrix3D();
        visuals.updateViewProjMatrix();

        drawFloorWallSoil();

        glEnable(GL_DEPTH_TEST);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        visuals.pointLightShader.useProgram();
        visuals.dirLightShader.setTexture(visuals.textureGems);

        glDisable(GL_BLEND);
        animManager.draw();

        //glDisable(GL_BLEND);
		drawBoardGems();

        drawSelectionMarker();

		glEnable(GL_DEPTH_TEST);
        match3.swapHintManager.draw();
		drawFallingGems();

		drawRocks();
		drawPillars();
		drawBeam();

		drawRailRoad();
		drawMineCarts();
		drawCrates();
		drawPickAxes();

        //drawPhysics();
		particleManager.draw();

        ClassicSingleton.getInstance().hud.draw();

        // needs to be set for picking to work correctly
        visuals.setProjectionMatrix3D();
        visuals.updateViewProjMatrix();
	}
	
	@Override
	public void handleTouchPress(float normalizedX, float normalizedY) {

        // temp solution to be able to go back
        final float y = -0.9f;
        if (normalizedY < y) {
            ClassicSingleton singleton = ClassicSingleton.getInstance();
            singleton.showSceneMenu();
        } else {
            swipeDir = SwipeDir.SwipeNone;
            touchDownX = normalizedX;
            touchDownY = normalizedY;

            if (!match3.isAnimating) {
                Ray ray = convertNormalized2DPointToRay(touchDownX, touchDownY);
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
	
	@Override
	public void handleTouchDrag(float normalizedX, float normalizedY) {			
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

	@Override
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
	
	private void divideByW(float[] vector) {
		vector[0] /= vector[3];
		vector[1] /= vector[3];
		vector[2] /= vector[3];
	}	
	
	private Ray convertNormalized2DPointToRay(float normalizedX, float normalizedY) {
		// we will convert these normalized device coordinates int world-space
		// coordinates. We will pick a point on the near and far planes, and draw a 
		// line between them. To do this transform, we need to first multiply by
		// the inverse matrix, and then we need to undo the perspective divide
		final float[] nearPointNdc = { normalizedX, normalizedY, -1, 1 };
		final float[] farPointNdc  = { normalizedX, normalizedY,  1, 1 };
		
		final float[] nearPointWorld = new float[4];
		final float[] farPointWorld = new float[4];
		
		multiplyMV(nearPointWorld, 0, visuals.invertedViewProjectionMatrix, 0, nearPointNdc, 0);
		multiplyMV(farPointWorld, 0, visuals.invertedViewProjectionMatrix, 0, farPointNdc, 0);
		
		divideByW(nearPointWorld);
		divideByW(farPointWorld);
		
		Vector nearPointRay = new Vector(nearPointWorld[0], nearPointWorld[1], nearPointWorld[2]);
		Vector farPointRay = new Vector(farPointWorld[0], farPointWorld[1], farPointWorld[2]);
		
		return new Ray(nearPointRay, Geometry.vectorBetween(nearPointRay, farPointRay));
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
		float posY = -2.2f;
		
		if (DRAW_BUFFER_BOARD) {
			posY = -20.0f;
		}
		
		float gemDistance = 2.45f;
		
		for(int y = 0; y < match3.boardSize*2; ++y) {
			posX = -(((match3.boardSize - 1) * gemDistance) / 2.0f);
			for (int x = 0; x < match3.boardSize; ++x) {
				match3.board[x][y].init(posX, posY, -0.5f,	1.0f, 1.0f, 1.0f);
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
	
	private void drawFallingGems() {	
		/*
		MyColor color = new MyColor(0f, 0f, 0f, 1f);
		
		visuals.pointLightShader.setTexture(visuals.textureGems);
		
		for(Body body : physics.boxes) {									
			if ( body.m_type == BodyType.DYNAMIC && body.m_userData != null ) {
				Vec2 pos = body.getPosition();
				float angle = body.getAngle();
				float degree = (float) Math.toDegrees(angle);			
				float d = GEM_FRAGMENT_SIZE + (0.15f * GEM_FRAGMENT_SIZE);
				
				Integer integer = (Integer)body.m_userData;
				int gemType = integer.intValue();
				Model gem = visuals.gems[gemType];				
				_op.setPosition(pos.x, pos.y, 1.0f);
				_op.setScale(d, d, 1f);				
				_op.setRot(0f, 0f, degree);
				
				visuals.calcMatricesForObject(_op);				
				visuals.pointLightShader.useProgram();
				
				visuals.pointLightShader.setUniforms(color, visuals.lightColor, visuals.lightNorm);						
				gem.bindData(visuals.pointLightShader);					
				gem.draw();
			}
		}
*/
		//glDisable(GL_DEPTH_TEST);
        glDisable(GL_BLEND);

		Body body;
        Model gem;
        Vec2 pos;
        Integer integer;
        int gemType;
        float angle;
        float degree;
        float d;
		int size = physics.fragments.size();
		for(int i = 0; i < size; ++i) {
			body = physics.fragments.get(i);
            pos = body.getPosition();
            angle = body.getAngle();
            degree = (float) Math.toDegrees(angle);
            d = GEM_FRAGMENT_SIZE;

            integer = (Integer)body.m_userData;
            gemType = integer;
            gem = visuals.gems[gemType];
            _op.setPosition(pos.x, pos.y, 1.0f);
            _op.setRot(0f, 0f, degree);
            _op.setScale(d, d, 1f);

            visuals.calcMatricesForObject(_op);
            visuals.pointLightShader.setUniforms(colorWhite, visuals.lightColor, visuals.lightNorm);
            gem.bindData(visuals.pointLightShader);
            gem.draw();


            gem = visuals.gemsPlates[gemType];
            _op.setPosition(pos.x, pos.y, 1.0f);
            _op.setRot(0f, 0f, degree);
            _op.setScale(d, d, 1f);

            visuals.calcMatricesForObject(_op);
            visuals.pointLightShader.setUniforms(colorWhite, visuals.lightColor, visuals.lightNorm);
            gem.bindData(visuals.pointLightShader);
            gem.draw();


        }
		//glEnable(GL_DEPTH_TEST);
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

                    visuals.colorShader.setUniforms(visuals.mvpMatrix, visuals.color);
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

                visuals.colorShader.setUniforms(visuals.mvpMatrix, visuals.color);
                edgeDrawer.bindData(visuals.colorShader);
                edgeDrawer.draw();

                fixture = fixture.getNext();
			}
		}
	}

	void drawSelectionMarker() {				
		if (match3.firstSelected != null) {
			elapsed += 0.3f;
			float d = (((float)Math.sin(elapsed) + 1f) / 2f) * 0.08f;
			//System.out.println("d is: " + d);

			markerPos.init(match3.firstSelected.op);
			markerPos.tz -= 0.2f;
            markerPos.setScale(1.0f + d, 1.0f + d, 1.0f);

			visuals.calcMatricesForObject(markerPos);
			visuals.pointLightShader.setUniforms(visuals.color, visuals.lightColor, visuals.lightNorm);
			visuals.marker.bindData(visuals.pointLightShader);
			visuals.marker.draw();
		}
	}

	void drawBoardGems() {
		int yMax = match3.boardSize;
		if (DRAW_BUFFER_BOARD) {
			yMax = match3.boardSize * 2;
		}

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        //glBlendFunc(GL_ONE, GL_ONE);

        Model gem;
        GemPosition gp;

        glDisable(GL_BLEND);
        // draw gems
		for(int y = 0; y < yMax; ++y) {
            for (int x = 0; x < match3.boardSize; ++x) {
                gp = match3.board[x][y];

                if (gp.type != GEM_TYPE_NONE && gp.visible) {
                    gem = visuals.gems[gp.type];

                    visuals.calcMatricesForObject(gp.op);
                    visuals.pointLightShader.setUniforms(colorWhite, visuals.lightColor, visuals.lightDir);
                    gem.bindData(visuals.pointLightShader);
                    gem.draw();
                }
            }
        }

        glEnable(GL_BLEND);
        // draw gem plates
        for(int y = 0; y < yMax; ++y) {
            for (int x = 0; x < match3.boardSize; ++x) {
                gp = match3.board[x][y];

                if (gp.type != GEM_TYPE_NONE && gp.visible) {
                    gem = visuals.gemsPlates[gp.type];

                    markerPos.init(gp.op);
                    markerPos.tz -= 0.11f;

                    visuals.calcMatricesForObject(markerPos);
                    visuals.pointLightShader.setUniforms(colorWhite, visuals.lightColor, visuals.lightDir);
                    gem.bindData(visuals.pointLightShader);
                    gem.draw();
                }
            }
        }
        glDisable(GL_BLEND);
	}
	
	void drawRailRoad() {		
		float x, y, z;
		
		x = -10f;
		y = -20.0f;
		z =  1.0f;		
		for(int i = 0; i < 3; ++i) {			
			float tempZ = z;			
			_op.setPosition(x, y, z);
			_op.setRot(0f, 0f, 0f);
			_op.setScale(1f, 1f, 1f);
							
			visuals.calcMatricesForObject(_op);
			visuals.dirLightShader.useProgram();
			visuals.dirLightShader.setTexture(visuals.textureRailRoad);
			visuals.dirLightShader.setUniforms(visuals.color, visuals.lightColor, visuals.lightNorm);			
			visuals.railroad.bindData(visuals.dirLightShader);
			visuals.railroad.draw();

			z = tempZ;
			x+=10.0f;
		}		
	}	
	
	void drawRock(Model rock, float x, float y, float z, float degree) {
		_op.setPosition(x, y, z);
		_op.setScale(1f, 1f, 1f);		
		_op.setRot(0f, 0f, degree);
		
		visuals.calcMatricesForObject(_op);
		visuals.dirLightShader.useProgram();
		visuals.dirLightShader.setTexture(visuals.textureCliff142);		
		visuals.dirLightShader.setUniforms(visuals.color, visuals.lightColor, visuals.lightNorm);		
		rock.bindData(visuals.dirLightShader);
		rock.draw();		
	}

	void drawPickAxes() {
		Model pickAxe = visuals.pickAxe;
		
		float x = 9f;//-3f;
		float y = -20f;//-19f;
		float z = 4.0f;//-3f;		
		
		_op.setPosition(x, y, z);
		_op.setScale(1f, 1f, 1f);		
		_op.setRot(90f, 30f, 50f);
				
		visuals.calcMatricesForObject(_op);
		visuals.dirLightShader.useProgram();		
		visuals.dirLightShader.setTexture(visuals.texturePickAxe);
		visuals.dirLightShader.setUniforms(visuals.color, visuals.lightColor, visuals.lightNorm);		
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
	
	void drawFloorWallSoil() {
        visuals.dirLightShader.useProgram();

		// floor
		_op.setPosition(0f, -20.5f, 0f);
		_op.setRot(0f, 0f, 0f);
		_op.setScale(1.0f, 1.0f, 1.0f);
		visuals.calcMatricesForObject(_op);
		visuals.dirLightShader.setTexture(visuals.textureFloor);
		visuals.dirLightShader.setUniforms(visuals.color, visuals.lightColor, visuals.lightNorm);		
		visuals.floor.bindData(visuals.dirLightShader);
		visuals.floor.bind();
		visuals.floor.draw();
		visuals.floor.unbind();
		
		// wall
		_op.setPosition(0f, -15f, -4f);
		_op.setRot(0f, 0f, 0f);
		_op.setScale(1f, 1f, 1f);		
		visuals.calcMatricesForObject(_op);
		visuals.dirLightShader.setTexture(visuals.textureWall);
		visuals.dirLightShader.setUniforms(visuals.color, visuals.lightColor, visuals.lightNorm);		
		visuals.wall.bindData(visuals.dirLightShader);
		visuals.wall.bind();
		visuals.wall.draw();
		visuals.wall.unbind();
		
		// soil
		_op.setPosition(0f, 5f, -3.5f);
		_op.setRot(0f, 0f, 0f);
		_op.setScale(1.9f, 1.5f, 1.0f);					
		visuals.calcMatricesForObject(_op);
		visuals.dirLightShader.setTexture(visuals.textureSoil);		
		visuals.dirLightShader.setUniforms(visuals.color, visuals.lightColor, visuals.lightNorm);		
		visuals.soil.bindData(visuals.dirLightShader);
		visuals.soil.bind();
		visuals.soil.draw();
		visuals.soil.unbind();
	}
	
	void drawCrates() {
		float x, y, z;
		
		x = -12.5f;
		y = -18.6f;
		z = -2.0f;					
		_op.setPosition(x, y, z);
		_op.setRot(0.0f, -20.0f, 0.0f);
		_op.setScale(1.0f, 1.0f, 1.0f);
				
		visuals.dirLightShader.setTexture(visuals.textureCrate);
		visuals.calcMatricesForObject(_op);
		visuals.dirLightShader.useProgram();
		visuals.dirLightShader.setUniforms(visuals.color, visuals.lightColor, visuals.lightNorm);		
		visuals.crate.bindData(visuals.dirLightShader);
		visuals.crate.draw();

		
		x = 12.0f;
		y = -18.6f;
		z = -2.0f;				
		_op.setPosition(x, y, z);
		_op.setRot(0.0f, 2.0f, 0.0f);
		_op.setScale(1.0f, 1.0f, 1.0f);		

		visuals.dirLightShader.setTexture(visuals.textureCrate);
		visuals.calcMatricesForObject(_op);
		visuals.dirLightShader.useProgram();
		visuals.dirLightShader.setUniforms(visuals.color, visuals.lightColor, visuals.lightNorm);		
		visuals.crate.bindData(visuals.dirLightShader);
		visuals.crate.draw();	
	}
	
	void drawBeam() {						
		_op.setPosition(0f, -16f, -2.5f);
		_op.setRot(0f, 0f, 0f);
		_op.setScale(1.0f, 1.0f, 1.0f);
		visuals.dirLightShader.setTexture(visuals.textureBeam);
		visuals.calcMatricesForObject(_op);
		visuals.dirLightShader.useProgram();
		visuals.dirLightShader.setUniforms(visuals.color, visuals.lightColor, visuals.lightNorm);		
		visuals.beam.bindData(visuals.dirLightShader);
		visuals.beam.draw();		
	}
	
	void drawPillars() {	
		float x = -9.5f;
		float y = -16f;
		float z = -2.5f;
		
		_op.setPosition(x, y, z);
		_op.setRot(0f, 0f, 0f);
		_op.setScale(1.0f, 1.0f, 1.0f);			
		visuals.dirLightShader.setTexture(visuals.texturePillar);
		visuals.calcMatricesForObject(_op);
		visuals.dirLightShader.useProgram();
		visuals.dirLightShader.setUniforms(visuals.color, visuals.lightColor, visuals.lightNorm);		
		visuals.pillar.bindData(visuals.dirLightShader);
		visuals.pillar.draw();		
		
		x = 9.5f;
		_op.setPosition(x, y, z);
		_op.setRot(0f, 0f, 0f);
		_op.setScale(1.0f, 1.0f, 1.0f);				
		visuals.calcMatricesForObject(_op);		
		visuals.dirLightShader.setUniforms(visuals.color, visuals.lightColor, visuals.lightNorm);			
		visuals.pillar.draw();				
	}
		
	void drawMineCarts() {
		int size = mineCarts.size();
		for (int i = 0; i < size; ++i) {
			mineCarts.get(i).update();
			mineCarts.get(i).draw();
		}
	}	
}
