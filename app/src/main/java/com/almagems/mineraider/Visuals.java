package com.almagems.mineraider;

import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.Matrix.invertM;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.multiplyMV;
import static android.opengl.Matrix.scaleM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.setLookAtM;
import static android.opengl.Matrix.translateM;
import static android.opengl.Matrix.invertM;
import static android.opengl.Matrix.transposeM;
import static android.opengl.Matrix.orthoM;
import static com.almagems.mineraider.Constants.*;

import android.content.Context;
import android.graphics.Color;

import com.almagems.mineraider.objects.Model;
import com.almagems.mineraider.shaders.ColorShader;
import com.almagems.mineraider.shaders.NormalColorShader;
import com.almagems.mineraider.shaders.ParticleShader;
import com.almagems.mineraider.shaders.PixelLightShader;
import com.almagems.mineraider.shaders.DirLightShader;
import com.almagems.mineraider.shaders.PointLightShader;
import com.almagems.mineraider.shaders.TextureShader;
import com.almagems.mineraider.util.MatrixHelper;
import com.almagems.mineraider.util.ModelLoader;
import com.almagems.mineraider.util.TextureHelper;
import com.almagems.mineraider.util.Vector;
import com.almagems.mineraider.util.MyColor;

public class Visuals {	
	private static Visuals instance = null;
	
	public Context context;
		
	public MyColor color = new  MyColor(1f, 1f, 1f);
	
	// matrices
	public final float[] invertedViewProjectionMatrix = new float[16];
	
	public final float[] modelMatrix = new float[16];
	public final float[] viewMatrix = new float[16];
	public final float[] projectionMatrix = new float[16];		
	
	public final float[] modelProjMatrix = new float[16];
	public final float[] mvMatrix = new float[16];
	public final float[] mvpMatrix = new float[16];
	public final float[] viewProjectionMatrix = new float[16];
	
	public final float[] normalMatrix = new float[16];
	
		
	// textures	
	public int textureGems;	
	public int textureHintArrow;
	public int textureCart;
	public int textureRailRoad;
	public int textureParticle;
	public int textureFloor;
	public int textureWall;
	public int texturePillar;
	public int textureCrate;
	public int textureSoil;
	public int textureWheel;
	public int textureBeam;
	public int textureCliff142;
	public int textureMenu;
	public int textureMenuItems;
	public int texturePickAxe;
	public int textureHelmet;
    public int textureFonts;
	
	// models
	public Model[] gems = new Model[MAX_GEM_TYPES];
	public Model marker;
	public Model hintMarker;
	public Model mineCart;
	public Model mineCartWheel;
	public Model railroad;	
	public Model mine;
	public Model floor;
	public Model wall;
	public Model pillar;
	public Model crate;
	public Model soil;
	public Model wheel;
	public Model beam;
	public Model rock0;
	public Model rock1;
	public Model rock2;
	public Model rock3;
	public Model rock4;
	public Model rock5;
	public Model rock6;
	public Model rock7;
	public Model rock8;
	public Model pickAxe;
	public Model helmet;
	
	// shaders
	public TextureShader textureShader;
	public ColorShader colorShader;
	public DirLightShader dirLightShader;
	public ParticleShader particleShader;
	public PixelLightShader pixelLightingShader;
	public NormalColorShader normalColorShader;
	public PointLightShader pointLightShader;
	
	//public final Vector lightDir = new Vector(0.0f, 0.0f, 3.0f);
	//public final Vector lightDir = new Vector(0.3f, 0.4f, 1.0f);
	public final Vector lightDir = new Vector(0.0f, 6.0f, 12.0f);
	//public final Vector lightDir = new Vector(-1.0f, 3.0f, 1.0f);
	public final Vector lightNorm = lightDir.normalize(lightDir);	
	public final MyColor lightColor = new MyColor(1.0f, 1.0f, 1.0f);

	private final float[] mLightPosInModelSpace = new float[] { 0.0f, 0.0f, 0.0f, 1.0f };
	private final float[] mLightPosInWorldSpace = new float[4];
	public final float[] mLightPosInEyeSpace = new float[4];
	private float[] mLightModelMatrix = new float[16];

    // ctor
	private Visuals() {
		System.out.println("Visuals ctor...");		
	}	
	
	public static Visuals getInstance() {
		if (instance == null) {
			instance = new Visuals(); 
		}
		return instance;
	}
	
	public void init(Context context) {
		this.context = context;
	}
	
	public void loadAssets() throws Exception {
		loadModels();
		loadShaders();
		loadTextures();
	}	
	
	private void loadShaders() throws Exception {
		System.out.println("loadShaders - BEGIN");
					
		textureShader = new TextureShader(context);
		colorShader = new ColorShader(context);
		dirLightShader = new DirLightShader(context);	
		particleShader = new ParticleShader(context);
		normalColorShader = new NormalColorShader(context);		
		pointLightShader = new PointLightShader(context);		

		System.out.println("loadShaders - END");
	}
	
	private void loadModels() {	
		ModelLoader ml;
		
		ml = new ModelLoader();			
		ml.init(context, R.drawable.gem0, "Gem0");
		gems[GEM_TYPE_0] = new Model(ml);
		
		ml = new ModelLoader(); 
		ml.init(context, R.drawable.gem1, "Gem1");
		gems[GEM_TYPE_1] = new Model(ml);
		
		ml = new ModelLoader(); 
		ml.init(context, R.drawable.gem2, "Gem2");
		gems[GEM_TYPE_2] = new Model(ml);
		
		ml = new ModelLoader(); 
		ml.init(context, R.drawable.gem3, "Gem3");
		gems[GEM_TYPE_3] = new Model(ml);
		
		ml = new ModelLoader(); 
		ml.init(context, R.drawable.gem4, "Gem4");
		gems[GEM_TYPE_4] = new Model(ml);
		
		ml = new ModelLoader(); 
		ml.init(context, R.drawable.gem5, "Gem5");
		gems[GEM_TYPE_5] = new Model(ml);
		
		ml = new ModelLoader(); 
		ml.init(context, R.drawable.gem6, "Gem6");
		gems[GEM_TYPE_6] = new Model(ml);
		
		ml = new ModelLoader();
		ml.init(context, R.drawable.marker, "Marker");
		marker = new Model(ml);

		ml = new ModelLoader();
		ml.init(context, R.drawable.hint_arrow, "HintArrow");
		hintMarker = new Model(ml);
		
		
		ml = new ModelLoader();
		ml.init(context, R.drawable.cart, "MineCart");
		mineCart = new Model(ml);
				
		ml = new ModelLoader();
		ml.init(context, R.drawable.cart_wheel, "MineCartWheel");
		mineCartWheel = new Model(ml);
		
		ml = new ModelLoader();
		ml.init(context, R.drawable.railroad, "RailRoad");
		railroad = new Model(ml);
				
		ml = new ModelLoader();
		ml.init(context, R.drawable.mine, "Mine");
		mine = new Model(ml);
		
		ml = new ModelLoader();
		ml.init(context, R.drawable.floor, "Floor");
		floor = new Model(ml);
		
		ml = new ModelLoader();
		ml.init(context, R.drawable.wall, "Wall");
		wall = new Model(ml);
		
		ml = new ModelLoader();
		ml.init(context, R.drawable.pillar, "Pillar");
		pillar = new Model(ml);		
		
		ml = new ModelLoader();
		ml.init(context, R.drawable.crate, "Crate");
		crate = new Model(ml);
		
		ml = new ModelLoader();
		ml.init(context, R.drawable.soil, "Soil");
		soil = new Model(ml);
		
		ml = new ModelLoader();
		ml.init(context, R.drawable.wheel, "Wheel");
		wheel = new Model(ml);

		ml = new ModelLoader();
		ml.init(context, R.drawable.beam, "Beam");
		beam = new Model(ml);
				
		ml = new ModelLoader();
		ml.init(context, R.drawable.rock0, "Rock0");
		rock0 = new Model(ml);
		
		ml = new ModelLoader();
		ml.init(context, R.drawable.rock1, "Rock1");
		rock1 = new Model(ml);
		
		ml = new ModelLoader();
		ml.init(context, R.drawable.rock2, "Rock2");
		rock2 = new Model(ml);		
		
		ml = new ModelLoader();
		ml.init(context, R.drawable.rock3, "Rock3");
		rock3 = new Model(ml);		
				
		ml = new ModelLoader();
		ml.init(context, R.drawable.rock4, "Rock4");
		rock4 = new Model(ml);		
		
		ml = new ModelLoader();
		ml.init(context, R.drawable.rock5, "Rock5");
		rock5 = new Model(ml);		

		ml = new ModelLoader();
		ml.init(context, R.drawable.rock6, "Rock6");
		rock6 = new Model(ml);		

		ml = new ModelLoader();
		ml.init(context, R.drawable.rock7, "Rock7");
		rock7 = new Model(ml);		

		ml = new ModelLoader();
		ml.init(context, R.drawable.rock8, "Rock8");
		rock8 = new Model(ml);		
		
		ml = new ModelLoader();
		ml.init(context, R.drawable.pickaxe, "PickAxe");
		pickAxe = new Model(ml);		
		
		ml = new ModelLoader();
		ml.init(context, R.drawable.helmet, "Helmet");
		helmet = new Model(ml);		
	}	
	
	public void loadTextures() {
		textureGems = TextureHelper.loadTexture(context, R.drawable.gems_textures);		
		textureHintArrow = TextureHelper.loadTexture(context, R.drawable.hint_arrow_texture);		
		textureCart = TextureHelper.loadTexture(context, R.drawable.cart_texture);
		textureRailRoad = TextureHelper.loadTexture(context, R.drawable.railroad_texture);		
		textureParticle = TextureHelper.loadTexture(context, R.drawable.smokeparticle);
		textureFloor = TextureHelper.loadTexture(context, R.drawable.floor_texture);
		textureWall = TextureHelper.loadTexture(context, R.drawable.wall_texture);
		texturePillar = TextureHelper.loadTexture(context, R.drawable.pillar_texture);
		textureCrate = TextureHelper.loadTexture(context, R.drawable.crate_texture);
		textureSoil = TextureHelper.loadTexture(context, R.drawable.soil_texture);
		textureWheel = TextureHelper.loadTexture(context, R.drawable.wheel_texture);
		textureBeam = TextureHelper.loadTexture(context, R.drawable.beam_texture);
		textureCliff142 = TextureHelper.loadTexture(context, R.drawable.cliffs0142);
		textureMenu = TextureHelper.loadTexture(context, R.drawable.main_menu_bg);
		textureMenuItems = TextureHelper.loadTexture(context, R.drawable.menu_items);
		texturePickAxe = TextureHelper.loadTexture(context, R.drawable.pickaxe_texture);
		textureHelmet = TextureHelper.loadTexture(context, R.drawable.helmet_texture);
        textureFonts = TextureHelper.loadTexture(context, R.drawable.fontsandroid);
	}
	
	public void bindNoTexture() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public void setProjectionMatrix2D(int width, int height) {
		
		final float aspectRatio = width > height ? (float)width / (float)height : (float)height / (float)width;
		
		if (width > height) {
			orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f); // landscape
		} else {
			orthoM(projectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f); // portrait
		}
				
//		orthoM(projectionMatrix, 0, 0f, width, 0f, height, 0f, 100f);
		//setLookAtM(viewMatrix, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f);
	}
	
	public void setProjectionMatrix3D(int width, int height) {
				
		float eyeZ = 49.0f;
		//float eyeZ = 190.0f;
		
		
		if (Constants.DRAW_BUFFER_BOARD) {
			eyeZ = 70.0f;
		}
		
		MatrixHelper.perspectiveM(projectionMatrix, 45, (float)width / (float)height, 1f, 1000f);
		
		setLookAtM(viewMatrix, 0,		
				// real
				0f,  11f, eyeZ,	// eye
				0f,  0f, 0f,    // at
				0f,  1f, 0f);   // up
					
//				// debug railway upclose
//				-13f, -19f, 3f, // eye				
//				0f, -22f, 0f, 	// at
//				0f, 1f, 0f);	// up

				// debug 
//				-60f, -10f, 13f, 	// eye				
//				0f, -6f, -10f, 	// at
//				0f, 1f, 0f);		// up
				
				
	/*	
        glViewport(0, 0, width, height);        

        MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width
            / (float) height, 1f, 10f);
        
        setIdentityM(viewMatrix, 0);
        translateM(viewMatrix, 0, 0f, -1.5f, -5f);   
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0,
            viewMatrix, 0);
    */
	}
	
	public void updateViewProjMatrix() {
		multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
		invertM(invertedViewProjectionMatrix, 0, viewProjectionMatrix, 0);		
	}
	
	public void calcMatricesForObject(float tx, float ty, float tz,
									  float rx, float ry, float rz,
									  float sx, float sy, float sz) {

		setIdentityM(modelMatrix, 0);
		
		translateM(modelMatrix, 0, tx, ty, tz);
		
		if (Math.abs(rx) > Constants.EPSILON) {
			rotateM(modelMatrix, 0, rx, 1f, 0f, 0f);
		}
		
		if (Math.abs(ry) > Constants.EPSILON) {
			rotateM(modelMatrix, 0, ry, 0f, 1f, 0f);
		}
		
		if (Math.abs(rz) > Constants.EPSILON) {
			rotateM(modelMatrix, 0, rz, 0f, 0, 1f);
		}
		
		if ( (Math.abs(sx) > Constants.EPSILON) || (Math.abs(sy) > Constants.EPSILON) || (Math.abs(sz) > Constants.EPSILON) ) { 
			scaleM(modelMatrix, 0, sx, sy, sz);			
		}
	
		setIdentityM(mLightModelMatrix, 0);
		translateM(mLightModelMatrix, 0, lightDir.x, lightDir.y, lightDir.z);
		
		multiplyMV(mLightPosInWorldSpace, 0, mLightModelMatrix, 0, mLightPosInModelSpace, 0);
		multiplyMV(mLightPosInEyeSpace, 0, viewMatrix, 0, mLightPosInWorldSpace, 0);
				
		multiplyMM(mvpMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);
						
		multiplyMM(mvMatrix, 0, viewMatrix, 0, modelMatrix, 0);
		//multiplyMM(mvMatrix, 0, modelMatrix, 0, viewMatrix, 0);
		
		//multiplyMV()
		
		multiplyMM(modelProjMatrix, 0, projectionMatrix, 0, modelMatrix, 0);
		
		// calc matrix to transform normal based on the model matrix
		invertM(normalMatrix, 0, modelMatrix, 0);
		transposeM(normalMatrix, 0, normalMatrix, 0);				
	}
	
	public void calcMatricesForObject(ObjectPosition op) {
		calcMatricesForObject( op.tx, op.ty, op.tz, 
							   op.rx, op.ry, op.rz, 
							   op.sx, op.sy, op.sz );
	}
	
	public int colorFromGemType(int type) {		
		switch (type) {
			case GEM_TYPE_0: return Color.rgb(255, 6, 0);	
			case GEM_TYPE_1: return Color.rgb(255, 35, 99);						
			case GEM_TYPE_2: return Color.rgb(0, 101, 30);			
			case GEM_TYPE_3: return Color.rgb(0, 0, 169);
			case GEM_TYPE_4: return Color.rgb(255, 79, 0);
			case GEM_TYPE_5: return Color.rgb(255, 255, 0);
			case GEM_TYPE_6: return Color.rgb(255, 222, 247);
			default: break;
		}
		return Color.rgb(255, 255, 255);		
	}
}
