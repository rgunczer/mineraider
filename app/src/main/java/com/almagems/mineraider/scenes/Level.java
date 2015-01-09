package com.almagems.mineraider.scenes;

import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
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
import com.almagems.mineraider.Constants;
import com.almagems.mineraider.GemPosition;
import com.almagems.mineraider.HUD;
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
import com.almagems.mineraider.util.Geometry.Point;
import com.almagems.mineraider.util.Geometry.Ray;
import com.almagems.mineraider.ScoreCounter;

public class Level extends Scene {

	private enum SwipeDir {
		SwipeNone,
		SwipeLeft,
		SwipeRight,
		SwipeUp,
		SwipeDown
	}
	
	private SwipeDir swipeDir = SwipeDir.SwipeNone;

    private final HUD hud;

	private Physics physics;
	private Match3 match3;

	private float elapsed = 0f;
	
	private boolean editorEnabled = false;
	
	public ArrayList<MineCart> mineCarts = new ArrayList<MineCart>();
	
	private AnimationManager animManager;
    private ScoreCounter scoreCounter;
	private ParticleManager particleManager;

	private ArrayList<RockData> rocks = new ArrayList<RockData>();
	
	private ObjectPosition _op = new ObjectPosition();
	
	public Level() {
		visuals = Visuals.getInstance();
		physics = Physics.getInstance();

        hud = new HUD();

		animManager = new AnimationManager();
        scoreCounter = new ScoreCounter();
		match3 = new Match3(6, animManager, scoreCounter);

		initBoardGeometry();		
				
		match3.placeInitialGems();
		match3.dumpBoardStat();
		match3.createInitialFallAnim();
	
		particleManager = ParticleManager.getInstance();
		particleManager.init();
				
		//physics.addEdge(-13.5f, -21.5f,  13.5f, -21.5f); // bottom
		physics.addEdge(-13.5f, -7.0f, -13.5f,  20.0f); // left
		physics.addEdge( 13.5f, -7.0f,  13.5f,  20.0f); // right
		
		physics.addBoxStatic( 7.8f, -5.9f,  24.8f, 12.0f, 0.8f);
		physics.addBoxStatic(-7.8f, -5.9f, -25.0f, 12.0f, 0.8f);
		physics.addBoxStatic( 3.0f, -8.8f, 0f, 1.4f, 1.4f);
		physics.addBoxStatic(-3.0f, -8.8f, 0f, 1.4f, 1.4f);
		
		// sin
		physics.addBoxStatic(0.0f, -19.7f, 0f, 120.0f, 0.5f);
		
		
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
		visuals.setProjectionMatrix3D(width, height);
	}
	
	@Override
	public void update() {
		Visuals visuals = Visuals.getInstance();
		visuals.updateViewProjMatrix();
		
		physics.update();
		match3.update();
        hud.update();
	}
	
	@Override
	public void draw() {
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		animManager.draw();

		drawFloorWallSoil();
		
		drawSelectionMarker();
		MyColor color; 
		color = new MyColor(0f, 0f, 0f, 1f);
		drawBoardGems(color);
		glDisable(GL_DEPTH_TEST);
		color = new MyColor(1f, 1f, 1f, 1f);
		drawBoardGems(color);
		glEnable(GL_DEPTH_TEST);
		drawHints();
		drawFallingGems();
					
		drawRocks();			
		drawPillars();
		drawBeam();
		
		drawRailRoad();
		drawMineCarts();	
		drawCrates();
		drawPickAxes();
		drawHelmets();
		
		//glDisable(GL_DEPTH_TEST);
		//drawPhysicsGemsFixtures();
		//drawPhysicsEdges();
		
		particleManager.draw();

        hud.draw();
	}
	
	@Override
	public void handleTouchPress(float normalizedX, float normalizedY) {

        scoreCounter.dump();

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
		
		Point nearPointRay = new Point(nearPointWorld[0], nearPointWorld[1], nearPointWorld[2]);
		Point farPointRay = new Point(farPointWorld[0], farPointWorld[1], farPointWorld[2]);
		
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
		float posY = -2f;
		
		if (Constants.DRAW_BUFFER_BOARD) {
			posY = -20.0f;
		}
		
		float gemDistance = 2.4f;
		
		for(int y = 0; y < match3.boardSize*2; ++y) {
			posX = -(((match3.boardSize - 1) * gemDistance) / 2.0f);
			for (int x = 0; x < match3.boardSize; ++x) {
				match3.board[x][y].init(posX, posY, -2.0f,	1.0f, 1.0f, 1.0f);
				posX += gemDistance;
			}
			posY += gemDistance;
			if (y == match3.boardSize - 1)
				posY += 1.0f;
		}			
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
				edgeDrawer.addLine(	new Point(edge.m_vertex1.x, edge.m_vertex1.y, 0.0f), 
									new Point(edge.m_vertex2.x, edge.m_vertex2.y, 0.0f));
				
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
				float d = Constants.GEM_FRAGMENT_SIZE + (0.15f * Constants.GEM_FRAGMENT_SIZE); 
				
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

		glDisable(GL_DEPTH_TEST);
		*/
		visuals.pointLightShader.setTexture(visuals.textureGems);
		Body body;
		int size = physics.boxes.size();
		for(int i = 0; i < size; ++i) {
			body = physics.boxes.get(i);
			if ( body.m_type == BodyType.DYNAMIC && body.m_userData != null ) {
				Vec2 pos = body.getPosition();
				float angle = body.getAngle();
				float degree = (float) Math.toDegrees(angle);			
				float d = Constants.GEM_FRAGMENT_SIZE;
				
				Integer integer = (Integer)body.m_userData;
				int gemType = integer;
				Model gem = visuals.gems[gemType];				
				_op.setPosition(pos.x, pos.y, 1.0f);							
				_op.setRot(0f, 0f, degree);
				_op.setScale(d, d, 1f);

				visuals.calcMatricesForObject(_op);				
				visuals.pointLightShader.useProgram();				
				visuals.pointLightShader.setUniforms(gem.color, visuals.lightColor, visuals.lightNorm);						
				gem.bindData(visuals.pointLightShader);					
				gem.draw();
			}
		}		
		glEnable(GL_DEPTH_TEST);
	}
		
	private void drawPhysicsGemsFixtures() {		
		EdgeDrawer edgeDrawer = new EdgeDrawer(100);
		int size = physics.boxes.size();
		Body body;
		for(int i = 0; i < size; ++i) {
			body = physics.boxes.get(i);
			Vec2 pos = body.getPosition();
			float angle = body.getAngle();
			float degree = (float) Math.toDegrees(angle);
			
			if (body.m_type == BodyType.STATIC) {
				Fixture fixture = body.getFixtureList();
				while(fixture != null) {
					PolygonShape polygon = (PolygonShape)fixture.getShape();				
					if (polygon.m_count == 4) { // box
						edgeDrawer.begin();
						Vec2 v0 = polygon.m_vertices[0];
						Vec2 v1 = polygon.m_vertices[1];
						Vec2 v2 = polygon.m_vertices[2];
						Vec2 v3 = polygon.m_vertices[3];
						
						edgeDrawer.addLine(	new Point(v0.x, v0.y, 0.0f), 
											new Point(v1.x, v1.y, 0.0f));
		
						edgeDrawer.addLine(	new Point(v1.x, v1.y, 0.0f), 
											new Point(v2.x, v2.y, 0.0f));
		
						edgeDrawer.addLine(	new Point(v2.x, v2.y, 0.0f), 
											new Point(v3.x, v3.y, 0.0f));
						
						edgeDrawer.addLine(	new Point(v3.x, v3.y, 0.0f), 
											new Point(v0.x, v0.y, 0.0f));
																											
						Vector position = new Vector(pos.x,	pos.y, 1.0f);
						
						setIdentityM(visuals.modelMatrix, 0);						
						translateM(visuals.modelMatrix, 0, position.x, position.y, position.z);
						rotateM(visuals.modelMatrix, 0, degree, 0.0f, 0.0f, 1.0f);											
						multiplyMM(visuals.mvpMatrix, 0, visuals.viewProjectionMatrix, 0, visuals.modelMatrix, 0);
						
						visuals.colorShader.useProgram();
						visuals.colorShader.setUniforms(visuals.mvpMatrix, visuals.color);
						edgeDrawer.bindData(visuals.colorShader);
						edgeDrawer.draw();
					}
					fixture = fixture.getNext();
				}
			}
			
			if (body.m_type == BodyType.DYNAMIC) {
				if (body.m_userData != null) {
					Fixture fixture = body.getFixtureList();
					while(fixture != null) {
						PolygonShape polygon = (PolygonShape)fixture.getShape();

						if (polygon.m_count == 3) { // triangle
							edgeDrawer.begin();

							Vec2 v0 = polygon.m_vertices[0];
							Vec2 v1 = polygon.m_vertices[1];
							Vec2 v2 = polygon.m_vertices[2];

							edgeDrawer.addLine(	new Point(v0.x, v0.y, 0.0f), 
												new Point(v1.x, v1.y, 0.0f));

							edgeDrawer.addLine(	new Point(v1.x, v1.y, 0.0f), 
												new Point(v2.x, v2.y, 0.0f));
							
							edgeDrawer.addLine(	new Point(v2.x, v2.y, 0.0f), 
												new Point(v0.x, v0.y, 0.0f));

							Vector position = new Vector(pos.x,	pos.y, 1.0f);
							
							setIdentityM(visuals.modelMatrix, 0);							
							translateM(visuals.modelMatrix, 0, position.x, position.y, position.z);
							rotateM(visuals.modelMatrix, 0, degree, 0.0f, 0.0f, 1.0f);													
							multiplyMM(visuals.mvpMatrix, 0, visuals.viewProjectionMatrix, 0, visuals.modelMatrix, 0);
							
							visuals.colorShader.useProgram();
							visuals.colorShader.setUniforms(visuals.mvpMatrix, visuals.color);
							edgeDrawer.bindData(visuals.colorShader);
							edgeDrawer.draw();
						}
						
						if (polygon.m_count == 5) {
							edgeDrawer.begin();
							
							Vec2 v0 = polygon.m_vertices[0];
							Vec2 v1 = polygon.m_vertices[1];
							Vec2 v2 = polygon.m_vertices[2];
							Vec2 v3 = polygon.m_vertices[3];
							Vec2 v4 = polygon.m_vertices[4];							
							
							edgeDrawer.addLine(	new Point(v0.x, v0.y, 0.0f), 
												new Point(v1.x, v1.y, 0.0f));
			
							edgeDrawer.addLine(	new Point(v1.x, v1.y, 0.0f), 
												new Point(v2.x, v2.y, 0.0f));
			
							edgeDrawer.addLine(	new Point(v2.x, v2.y, 0.0f), 
												new Point(v3.x, v3.y, 0.0f));
							
							edgeDrawer.addLine(	new Point(v3.x, v3.y, 0.0f), 
												new Point(v4.x, v4.y, 0.0f));
									
							edgeDrawer.addLine(	new Point(v4.x, v4.y, 0.0f), 
												new Point(v0.x, v0.y, 0.0f));
							
							Vector position = new Vector(pos.x,	pos.y, 1.0f);
							
							setIdentityM(visuals.modelMatrix, 0);							
							translateM(visuals.modelMatrix, 0, position.x, position.y, position.z);
							rotateM(visuals.modelMatrix, 0, degree, 0.0f, 0.0f, 1.0f);												
							multiplyMM(visuals.mvpMatrix, 0, visuals.viewProjectionMatrix, 0, visuals.modelMatrix, 0);
							
							visuals.colorShader.useProgram();
							visuals.colorShader.setUniforms(visuals.mvpMatrix, visuals.color);
							edgeDrawer.bindData(visuals.colorShader);
							edgeDrawer.draw();
						}
						
						if (polygon.m_count == 6) {
							edgeDrawer.begin();
							
							Vec2 v0 = polygon.m_vertices[0];
							Vec2 v1 = polygon.m_vertices[1];
							Vec2 v2 = polygon.m_vertices[2];
							Vec2 v3 = polygon.m_vertices[3];
							Vec2 v4 = polygon.m_vertices[4];
							Vec2 v5 = polygon.m_vertices[5];
							
							edgeDrawer.addLine(	new Point(v0.x, v0.y, 0.0f), 
												new Point(v1.x, v1.y, 0.0f));
			
							edgeDrawer.addLine(	new Point(v1.x, v1.y, 0.0f), 
												new Point(v2.x, v2.y, 0.0f));
			
							edgeDrawer.addLine(	new Point(v2.x, v2.y, 0.0f), 
												new Point(v3.x, v3.y, 0.0f));
							
							edgeDrawer.addLine(	new Point(v3.x, v3.y, 0.0f), 
												new Point(v4.x, v4.y, 0.0f));
									
							edgeDrawer.addLine(	new Point(v4.x, v4.y, 0.0f), 
												new Point(v5.x, v5.y, 0.0f));

							edgeDrawer.addLine(	new Point(v5.x, v5.y, 0.0f), 
												new Point(v0.x, v0.y, 0.0f));							
							
							Vector position = new Vector(pos.x,	pos.y, 1.0f);
							
							setIdentityM(visuals.modelMatrix, 0);							
							translateM(visuals.modelMatrix, 0, position.x, position.y, position.z);
							rotateM(visuals.modelMatrix, 0, degree, 0.0f, 0.0f, 1.0f);													
							multiplyMM(visuals.mvpMatrix, 0, visuals.viewProjectionMatrix, 0, visuals.modelMatrix, 0);
							
							visuals.colorShader.useProgram();
							visuals.colorShader.setUniforms(visuals.mvpMatrix, visuals.color);
							edgeDrawer.bindData(visuals.colorShader);
							edgeDrawer.draw();
						}
						
						if (polygon.m_count == 8) {
							edgeDrawer.begin();
							
							Vec2 v0 = polygon.m_vertices[0];
							Vec2 v1 = polygon.m_vertices[1];
							Vec2 v2 = polygon.m_vertices[2];
							Vec2 v3 = polygon.m_vertices[3];
							Vec2 v4 = polygon.m_vertices[4];
							Vec2 v5 = polygon.m_vertices[5];
							Vec2 v6 = polygon.m_vertices[6];
							Vec2 v7 = polygon.m_vertices[7];
							
							edgeDrawer.addLine(	new Point(v0.x, v0.y, 0.0f), 
												new Point(v1.x, v1.y, 0.0f));
			
							edgeDrawer.addLine(	new Point(v1.x, v1.y, 0.0f), 
												new Point(v2.x, v2.y, 0.0f));
			
							edgeDrawer.addLine(	new Point(v2.x, v2.y, 0.0f), 
												new Point(v3.x, v3.y, 0.0f));
							
							edgeDrawer.addLine(	new Point(v3.x, v3.y, 0.0f), 
												new Point(v4.x, v4.y, 0.0f));
									
							edgeDrawer.addLine(	new Point(v4.x, v4.y, 0.0f), 
												new Point(v5.x, v5.y, 0.0f));

							edgeDrawer.addLine(	new Point(v5.x, v5.y, 0.0f), 
												new Point(v6.x, v6.y, 0.0f));
							
							edgeDrawer.addLine(	new Point(v6.x, v6.y, 0.0f), 
												new Point(v7.x, v7.y, 0.0f));

							edgeDrawer.addLine(	new Point(v7.x, v7.y, 0.0f), 
												new Point(v0.x, v0.y, 0.0f));														
							
							Vector position = new Vector(pos.x,	pos.y, 1.0f);
							
							setIdentityM(visuals.modelMatrix, 0);							
							translateM(visuals.modelMatrix, 0, position.x, position.y, position.z);
							rotateM(visuals.modelMatrix, 0, degree, 0.0f, 0.0f, 1.0f);													
							multiplyMM(visuals.mvpMatrix, 0, visuals.viewProjectionMatrix, 0, visuals.modelMatrix, 0);
							
							visuals.colorShader.useProgram();
							visuals.colorShader.setUniforms(visuals.mvpMatrix, visuals.color);
							edgeDrawer.bindData(visuals.colorShader);
							edgeDrawer.draw();
						}
						
						if (polygon.m_count == 4) { // box
							edgeDrawer.begin();
								
							Vec2 v0 = polygon.m_vertices[0];
							Vec2 v1 = polygon.m_vertices[1];
							Vec2 v2 = polygon.m_vertices[2];
							Vec2 v3 = polygon.m_vertices[3];
							
							edgeDrawer.addLine(	new Point(v0.x, v0.y, 0.0f), 
												new Point(v1.x, v1.y, 0.0f));
			
							edgeDrawer.addLine(	new Point(v1.x, v1.y, 0.0f), 
												new Point(v2.x, v2.y, 0.0f));
			
							edgeDrawer.addLine(	new Point(v2.x, v2.y, 0.0f), 
												new Point(v3.x, v3.y, 0.0f));
							
							edgeDrawer.addLine(	new Point(v3.x, v3.y, 0.0f), 
												new Point(v0.x, v0.y, 0.0f));
																												
							Vector position = new Vector(pos.x,	pos.y, 1.0f);
							
							setIdentityM(visuals.modelMatrix, 0);							
							translateM(visuals.modelMatrix, 0, position.x, position.y, position.z);
							rotateM(visuals.modelMatrix, 0, degree, 0.0f, 0.0f, 1.0f);													
							multiplyMM(visuals.mvpMatrix, 0, visuals.viewProjectionMatrix, 0, visuals.modelMatrix, 0);
							
							visuals.colorShader.useProgram();
							visuals.colorShader.setUniforms(visuals.mvpMatrix, visuals.color);
							edgeDrawer.bindData(visuals.colorShader);
							edgeDrawer.draw();
						}
						fixture = fixture.getNext();
					}
				}
			}
		}
	}
	
	void drawHints() {
//		int size = match3.hintList.size();
//		SwapHint hint = null;
//		for (int i = 0; i < size; ++i) {
//			hint = match3.hintList.get(i);
//			hint.update();
//			hint.draw();
//		}
        match3.swapHintManager.draw();
	}	
	
	void drawSelectionMarker() {				
		if (match3.firstSelected != null) {
			elapsed += 0.3f;
			float d = (((float)Math.sin(elapsed) + 1f) / 2f) * 0.075f;
			//System.out.println("d is: " + d);
					
			visuals.dirLightShader.setTexture(visuals.textureGems);
			ObjectPosition op = new ObjectPosition(match3.firstSelected.op);
			op.tz -= 0.1f;
			op.setScale(1.0f+d, 1.0f+d, 1.0f);

			visuals.calcMatricesForObject(op);														
			visuals.dirLightShader.useProgram();
			visuals.dirLightShader.setUniforms(visuals.color, visuals.lightColor, visuals.lightNorm);			
			visuals.marker.bindData(visuals.dirLightShader);
			visuals.marker.draw();
		}
	}
	
	void drawBoardGems(MyColor color) {	
		//r+=0.25f;
		int yMax = match3.boardSize;
		if (Constants.DRAW_BUFFER_BOARD) {
			yMax = match3.boardSize * 2;
		}
		
		visuals.pointLightShader.setTexture(visuals.textureGems);
		
		float scale;
		for(int y = 0; y < yMax; ++y) {
			for (int x = 0; x < match3.boardSize; ++x) {
				GemPosition gp = match3.board[x][y];
				
				if (gp.type != GEM_TYPE_NONE && gp.visible) {
					Model gem = visuals.gems[gp.type];
														
					if (color.r == 0.0f) {
						scale = 0.1f;
					} else {
						scale = 0.0f;
					}
					//op.rotAxis = new Vector(0.0f, 0.0f, 1.0f);
					//op.rotDegree = r;		
					
					visuals.calcMatricesForObject(gp.op.tx, gp.op.ty, gp.op.tz,
												  gp.op.rx, gp.op.ry, gp.op.rz,
												  gp.op.sx + scale, gp.op.sy + scale, gp.op.sz);
					
					visuals.pointLightShader.useProgram();										
					visuals.pointLightShader.setUniforms(color, visuals.lightColor, visuals.lightDir);							 			
					gem.bindData(visuals.pointLightShader);
					gem.bind();
					gem.draw();
					gem.unbind();
				}
			}
		}
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
	
	void drawHelmets() {		
		Model helmet = visuals.helmet;
				
		_op.setPosition(-12.5f, -17f, -2f);
		_op.setScale(1.0f, 1.0f, 1.0f);		
		_op.setRot(0f, 30f, 0f);
				
		visuals.calcMatricesForObject(_op);
		visuals.pointLightShader.useProgram();		
		visuals.pointLightShader.setTexture(visuals.textureHelmet);
		visuals.pointLightShader.setUniforms(visuals.color, visuals.lightColor, visuals.lightNorm);		
		helmet.bindData(visuals.pointLightShader);
		helmet.draw();					
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
		drawRock(visuals.rock0,  6.0f, -9.0f, rock_z, -70.0f);
		drawRock(visuals.rock6,  3.3f, -9.7f, rock_z, 0.0f);
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
		// floor
		_op.setPosition(0f, -20.5f, 0f);
		_op.setRot(0f, 0f, 0f);
		_op.setScale(1.0f, 1.0f, 1.0f);
				
		visuals.calcMatricesForObject(_op);
		visuals.dirLightShader.useProgram();
		visuals.dirLightShader.setTexture(visuals.textureFloor);
		visuals.dirLightShader.useProgram();
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
		visuals.dirLightShader.useProgram();
		visuals.dirLightShader.setTexture(visuals.textureWall);
		visuals.dirLightShader.useProgram();
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
		visuals.dirLightShader.useProgram();
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
