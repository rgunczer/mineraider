package com.almagems.mineraider;

import static android.opengl.GLES20.*;
import static android.opengl.Matrix.*;
import static com.almagems.mineraider.Constants.*;

import android.content.Context;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Graphics {

    public static float screenWidth;
    public static float screenHeight;
    public static float aspectRatio;
    public static final float referenceScreenWidth = 1080f;
    public static float scaleFactor;

	public Context context;

    public final ParticleManager particleManager;
    public final BatchDrawer batchDrawer;
    public final FBO fbo;

    // custom
	public MyColor whiteColor = new  MyColor(1f, 1f, 1f);
    public MyColor blackColor = new  MyColor(0f, 0f, 0f);

    public Map<String, TexturedQuad> fonts = new HashMap<String, TexturedQuad>();
    private ArrayList<Texture> textures = new ArrayList<Texture>(20);
	
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
	public int textureLoading;
	public int textureGems;
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
	public int textureMenuItems;
	public int texturePickAxe;
    public int textureFonts;
    public int textureEditorButtons;
    public int textureHudPauseButton;


    // models
	public Model[] gems = new Model[MAX_GEM_TYPES];
    public Model[] gemsPlates = new Model[MAX_GEM_TYPES];
	public Model marker;
	public Model hint;
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
	public Model mineInterior;

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
	public Graphics(Context context) {
        System.out.println("Visuals ctor...");
		this.context = context;
        fbo = new FBO();

        BatchDrawer.graphics = this;
        batchDrawer = new BatchDrawer();
        ParticleManager.graphics = this;
        particleManager = new ParticleManager();
	}

    public void initialSetup() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        //glClearColor(1.0f, 0.3f, 0.3f, 0.0f);
        //glClearColor(0.1f, 0.1f, 0.1f, 0.0f);

        glDisable(GL_CULL_FACE);
        glDepthFunc(GL_LESS);
        //glDepthMask(true);

        //glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        //glBlendFunc(GL_ONE_MINUS_DST_ALPHA,GL_DST_ALPHA);
        //glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        //glBlendFunc(GL_SRC_ALPHA, GL_ONE);
        glBlendFunc(GL_ONE, GL_ONE);
    }

    public void loadStartupAssets() throws Exception {
        System.out.println("Load startup Assets...");

        // shader
        BaseShader.graphics = this;
        textureShader = new TextureShader();
        colorShader = new ColorShader();

        // texture
        textureLoading = loadTexture(R.drawable.almagems_android_loading);
    }

	public void loadShaders() { //throws Exception {
		System.out.println("loadShaders - BEGIN");


		dirLightShader = new DirLightShader();
		particleShader = new ParticleShader();
		normalColorShader = new NormalColorShader();
		pointLightShader = new PointLightShader();

		System.out.println("loadShaders - END");
	}
	
	public void loadModelsPart01() {
        ModelLoader ml;

        //  gems
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

        // plates
        ml = new ModelLoader();
        ml.init(context, R.drawable.gem0_plate, "Gem0Plate");
        gemsPlates[GEM_TYPE_0] = new Model(ml);

        ml = new ModelLoader();
        ml.init(context, R.drawable.gem1_plate, "Gem1Plate");
        gemsPlates[GEM_TYPE_1] = new Model(ml);

        ml = new ModelLoader();
        ml.init(context, R.drawable.gem2_plate, "Gem2Plate");
        gemsPlates[GEM_TYPE_2] = new Model(ml);

        ml = new ModelLoader();
        ml.init(context, R.drawable.gem3_plate, "Gem3Plate");
        gemsPlates[GEM_TYPE_3] = new Model(ml);

        ml = new ModelLoader();
        ml.init(context, R.drawable.gem4_plate, "Gem4Plate");
        gemsPlates[GEM_TYPE_4] = new Model(ml);

        ml = new ModelLoader();
        ml.init(context, R.drawable.gem5_plate, "Gem5Plate");
        gemsPlates[GEM_TYPE_5] = new Model(ml);

        ml = new ModelLoader();
        ml.init(context, R.drawable.gem6_plate, "Gem6Plate");
        gemsPlates[GEM_TYPE_6] = new Model(ml);
    }

    public void loadModelsPart02() {
        ModelLoader ml;

        ml = new ModelLoader();
        ml.init(context, R.drawable.marker, "Marker");
        marker = new Model(ml);

        ml = new ModelLoader();
        ml.init(context, R.drawable.hint, "Hint");
        hint = new Model(ml);

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
    }

    public void loadModelsPart03() {
        ModelLoader ml;

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
        ml.init(context, R.drawable.mine_interior, "MineInterior");
        mineInterior = new Model(ml);
	}	

    private int loadTexture(int resourceId) {
        Texture texture = TextureHelper.loadTexture(context, resourceId);
        textures.add(texture);
        return texture.id;
    }

    public Texture getTextureObj(int textureId) {
        Texture texture;
        int size = textures.size();
        for(int i = 0; i < size; ++i) {
            texture = textures.get(i);
            if (texture.id == textureId) {
                return texture;
            }
        }
        return null;
    }

	public void loadTexturesPart01() {
        textureGems = loadTexture(R.drawable.gems_textures);
        textureCart = loadTexture(R.drawable.cart_texture);
        textureRailRoad = loadTexture(R.drawable.railroad_texture);
        textureParticle = loadTexture(R.drawable.smokeparticle);
        textureFloor = loadTexture(R.drawable.floor_texture);
        textureWall = loadTexture(R.drawable.wall_texture);
    }

    public void loadTexturesPart02() {
        texturePillar = loadTexture(R.drawable.pillar_texture);
        textureCrate = loadTexture(R.drawable.crate_texture);
        textureSoil = loadTexture(R.drawable.soil_texture);
        textureWheel = loadTexture(R.drawable.wheel_texture);
        textureBeam = loadTexture(R.drawable.beam_texture);
        textureCliff142 = loadTexture(R.drawable.cliffs0142);
    }

    public void loadTexturesPart03() {
		texturePickAxe = loadTexture(R.drawable.pickaxe_texture);
        textureFonts = loadTexture(R.drawable.fontsandroid);
        textureEditorButtons = loadTexture(R.drawable.editor_buttons);
        textureHudPauseButton = loadTexture(R.drawable.hud_pause_button);
		textureMenuItems = loadTexture(R.drawable.menu_items);
	}

	public void bindNoTexture() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}


    public void setProjectionMatrix2D() {
        setProjectionMatrix2D((int) screenWidth, (int) screenHeight);
    }

	private void setProjectionMatrix2D(int width, int height) {

//		final float aspectRatio = width > height ? (float)width / (float)height : (float)height / (float)width;

		if (width > height) {
            // landscape
			orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
		} else {
		    // portrait
			orthoM(projectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
		}

        setIdentityM(viewMatrix, 0);

//		orthoM(projectionMatrix, 0, 0f, width, 0f, height, 0f, 100f);
		//setLookAtM(viewMatrix, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f);
	}

    public void setProjectionMatrix3dForMenu()
    {
        MatrixHelper.perspectiveM(projectionMatrix, 45, screenWidth / screenHeight, 1f, 100f);

        setLookAtM(viewMatrix, 0,
                -40f,  0f, 50f,	// eye
                //-40f, 0f, 0f,
                -10f,  1f, 0f,    // at
                //0f, -10f, 0f,
                0f,  1f, 0f);   // up
    }

    public void setProjectionMatrix3dForShaft()
    {
        MatrixHelper.perspectiveM(projectionMatrix, 45, screenWidth / screenHeight, 1f, 100f);

        setLookAtM(viewMatrix, 0,
                0f,  0f, 68f,	// eye
                0f,  0f, 0f,  // at
                0f,  1f, 0f);   // up


/*
        // debug
        setLookAtM(viewMatrix, 0,
                0f,  0f, 0f,	// eye
                10f,  0f, -6f,  // at
                0f,  1f, 0f);   // up

*/




    }

	public void setProjectionMatrix3D() {
		float eyeZ = 49.0f;
		//float eyeZ = 90.0f;
		
		if (Constants.DRAW_BUFFER_BOARD) {
			eyeZ = 70.0f;
		}
		
		MatrixHelper.perspectiveM(projectionMatrix, 45, screenWidth / screenHeight, 1f, 100f);
		
		setLookAtM(viewMatrix, 0,		
				// real
				0f,  12f, eyeZ,	// eye
				0f,  1.2f, 0f,    // at
				0f,  1f, 0f);   // up
					
//				// debug railway upclose
//				-13f, -19f, 3f, // eye				
//				0f, -22f, 0f, 	// at
//				0f, 1f, 0f);	// up

				// debug 
//				-20f, 5f,  1f, 	// eye
//				  0f, 5f, -1f, 	// at
//				  0f, 1f,   0f);	// up

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

	public void calcMatricesForObject(PositionInfo op, float tx, float ty) {
        setIdentityM(modelMatrix, 0);

        translateM(modelMatrix, 0, 0f, ty, 0f);

        // rotation
        if (Math.abs(op.rx) > Constants.EPSILON) {
            rotateM(modelMatrix, 0, op.rx, 1f, 0f, 0f);
        }

        if (Math.abs(op.ry) > Constants.EPSILON) {
            rotateM(modelMatrix, 0, op.ry, 0f, 1f, 0f);
        }

        if (Math.abs(op.rz) > Constants.EPSILON) {
            rotateM(modelMatrix, 0, op.rz, 0f, 0, 1f);
        }



        // translation
        translateM(modelMatrix, 0, op.tx, op.ty, op.tz);

        translateM(modelMatrix, 0, 0f, -ty, 0f);

//        rotateM(modelMatrix, 0, op.rx, 1f, 0f, 0f);
//        rotateM(modelMatrix, 0, op.ry, 0f, 1f, 0f);
//        rotateM(modelMatrix, 0, op.rz, 0f, 0f, 1f);

        // scale
        if ( (Math.abs(1f - op.sx) > Constants.EPSILON) ||
                (Math.abs(1f - op.sy) > Constants.EPSILON) ||
                (Math.abs(1f - op.sz) > Constants.EPSILON) ) {
            scaleM(modelMatrix, 0, op.sx, op.sy, op.sz);
        }

        //scaleM(modelMatrix, 0, op.sx, op.sy, op.sz);

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

    public void calcMatricesForObject(PositionInfo op) {
		setIdentityM(modelMatrix, 0);

        // translation
		translateM(modelMatrix, 0, op.tx, op.ty, op.tz);

        // rotation
		if (Math.abs(op.rx) > Constants.EPSILON) {
			rotateM(modelMatrix, 0, op.rx, 1f, 0f, 0f);
		}

		if (Math.abs(op.ry) > Constants.EPSILON) {
			rotateM(modelMatrix, 0, op.ry, 0f, 1f, 0f);
		}

		if (Math.abs(op.rz) > Constants.EPSILON) {
			rotateM(modelMatrix, 0, op.rz, 0f, 0, 1f);
		}

//        rotateM(modelMatrix, 0, op.rx, 1f, 0f, 0f);
//        rotateM(modelMatrix, 0, op.ry, 0f, 1f, 0f);
//        rotateM(modelMatrix, 0, op.rz, 0f, 0f, 1f);

        // scale
		if ( (Math.abs(1f - op.sx) > Constants.EPSILON) ||
             (Math.abs(1f - op.sy) > Constants.EPSILON) ||
             (Math.abs(1f - op.sz) > Constants.EPSILON) ) {
			scaleM(modelMatrix, 0, op.sx, op.sy, op.sz);
		}

        //scaleM(modelMatrix, 0, op.sx, op.sy, op.sz);

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

    public VertexBuffer createFullScreenVertexBuffer(Rectangle rc, Texture texture) {
        float width = screenWidth;
        float scale = width / referenceScreenWidth;

        float tw = texture.width;
        float th = texture.height;

        float r = 1f;
        float g = 1f;
        float b = 1f;
        float a = 1f;

        float tx0 = rc.x / tw;
        float tx1 = (rc.x + rc.w) / tw;
        float ty0 = ((th - rc.y) - rc.h) / th;
        float ty1 = ((th - rc.y)) / th;

        float x = (rc.w / width) * scale;
        float y = (rc.h / width) * scale;
        float[] vertexData = {
                // x, y, z, 	                u, v,
                -x, -y, 0.0f,   r, g, b, a,     tx0, ty0,
                 x, -y, 0.0f,   r, g, b, a,     tx1, ty0,
                 x,  y, 0.0f,   r, g, b, a,     tx1, ty1,

                -x, -y, 0.0f,   r, g, b, a,     tx0, ty0,
                 x,  y, 0.0f,   r, g, b, a,     tx1, ty1,
                -x,  y, 0.0f,   r, g, b, a,     tx0, ty1
        };

        VertexBuffer vb = new VertexBuffer(vertexData);
        return vb;
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

    public void loadFonts() {
        // irrlicht font maker control points to text format:
        // rawFonts.add( new FontData('%char%', new Rectangle(%left%f, %bottom%f, %width%f, %height%f) ) );
        final ArrayList<FontData> rawFonts = new ArrayList<FontData>(100);
        rawFonts.add( new FontData(' ', new Rectangle(1f, 100f, 10f, 100f) ) );
        rawFonts.add( new FontData('!', new Rectangle(11f, 100f, 39f, 100f) ) );
        rawFonts.add( new FontData('"', new Rectangle(50f, 100f, 47f, 100f) ) );
        rawFonts.add( new FontData('#', new Rectangle(97f, 100f, 75f, 100f) ) );
        rawFonts.add( new FontData('$', new Rectangle(172f, 100f, 51f, 100f) ) );
        rawFonts.add( new FontData('%', new Rectangle(223f, 100f, 64f, 100f) ) );
        rawFonts.add( new FontData('&', new Rectangle(287f, 100f, 58f, 100f) ) );
        rawFonts.add( new FontData('\'', new Rectangle(345f, 100f, 38f, 100f) ) );
        rawFonts.add( new FontData('(', new Rectangle(383f, 100f, 45f, 100f) ) );
        rawFonts.add( new FontData(')', new Rectangle(428f, 100f, 45f, 100f) ) );
        rawFonts.add( new FontData('*', new Rectangle(473f, 100f, 49f, 100f) ) );
        rawFonts.add( new FontData('+', new Rectangle(522f, 100f, 75f, 100f) ) );
        rawFonts.add( new FontData(',', new Rectangle(597f, 100f, 35f, 100f) ) );
        rawFonts.add( new FontData('-', new Rectangle(632f, 100f, 42f, 100f) ) );
        rawFonts.add( new FontData('.', new Rectangle(674f, 100f, 35f, 100f) ) );
        rawFonts.add( new FontData('/', new Rectangle(709f, 100f, 45f, 100f) ) );
        rawFonts.add( new FontData('0', new Rectangle(754f, 100f, 51f, 100f) ) );
        rawFonts.add( new FontData('1', new Rectangle(805f, 100f, 42f, 100f) ) );
        rawFonts.add( new FontData('2', new Rectangle(847f, 100f, 48f, 100f) ) );
        rawFonts.add( new FontData('3', new Rectangle(895f, 100f, 49f, 100f) ) );
        rawFonts.add( new FontData('4', new Rectangle(944f, 100f, 50f, 100f) ) );
        rawFonts.add( new FontData('5', new Rectangle(0f, 201f, 49f, 100f) ) );
        rawFonts.add( new FontData('6', new Rectangle(49f, 201f, 51f, 100f) ) );
        rawFonts.add( new FontData('7', new Rectangle(100f, 201f, 47f, 100f) ) );
        rawFonts.add( new FontData('8', new Rectangle(147f, 201f, 51f, 100f) ) );
        rawFonts.add( new FontData('9', new Rectangle(198f, 201f, 51f, 100f) ) );
        rawFonts.add( new FontData(':', new Rectangle(249f, 201f, 35f, 100f) ) );
        rawFonts.add( new FontData(';', new Rectangle(284f, 201f, 35f, 100f) ) );
        rawFonts.add( new FontData('<', new Rectangle(319f, 201f, 75f, 100f) ) );
        rawFonts.add( new FontData('=', new Rectangle(394f, 201f, 75f, 100f) ) );
        rawFonts.add( new FontData('>', new Rectangle(469f, 201f, 75f, 100f) ) );
        rawFonts.add( new FontData('?', new Rectangle(544f, 201f, 49f, 100f) ) );
        rawFonts.add( new FontData('@', new Rectangle(593f, 201f, 91f, 100f) ) );
        rawFonts.add( new FontData('A', new Rectangle(684f, 201f, 53f, 100f) ) );
        rawFonts.add( new FontData('B', new Rectangle(737f, 201f, 53f, 100f) ) );
        rawFonts.add( new FontData('C', new Rectangle(790f, 201f, 50f, 100f) ) );
        rawFonts.add( new FontData('D', new Rectangle(840f, 201f, 54f, 100f) ) );
        rawFonts.add( new FontData('E', new Rectangle(894f, 201f, 51f, 100f) ) );
        rawFonts.add( new FontData('F', new Rectangle(945f, 201f, 51f, 100f) ) );
        rawFonts.add( new FontData('G', new Rectangle(0f, 302f, 53f, 100f) ) );
        rawFonts.add( new FontData('H', new Rectangle(53f, 302f, 57f, 100f) ) );
        rawFonts.add( new FontData('I', new Rectangle(110f, 302f, 42f, 100f) ) );
        rawFonts.add( new FontData('J', new Rectangle(152f, 302f, 49f, 100f) ) );
        rawFonts.add( new FontData('K', new Rectangle(201f, 302f, 55f, 100f) ) );
        rawFonts.add( new FontData('L', new Rectangle(256f, 302f, 51f, 100f) ) );
        rawFonts.add( new FontData('M', new Rectangle(307f, 302f, 62f, 100f) ) );
        rawFonts.add( new FontData('N', new Rectangle(369f, 302f, 55f, 100f) ) );
        rawFonts.add( new FontData('O', new Rectangle(424f, 302f, 53f, 100f) ) );
        rawFonts.add( new FontData('P', new Rectangle(477f, 302f, 53f, 100f) ) );
        rawFonts.add( new FontData('Q', new Rectangle(530f, 302f, 54f, 100f) ) );
        rawFonts.add( new FontData('R', new Rectangle(584f, 302f, 54f, 100f) ) );
        rawFonts.add( new FontData('S', new Rectangle(638f, 302f, 49f, 100f) ) );
        rawFonts.add( new FontData('T', new Rectangle(687f, 302f, 53f, 100f) ) );
        rawFonts.add( new FontData('U', new Rectangle(740f, 302f, 53f, 100f) ) );
        rawFonts.add( new FontData('V', new Rectangle(793f, 302f, 54f, 100f) ) );
        rawFonts.add( new FontData('W', new Rectangle(847f, 302f, 64f, 100f) ) );
        rawFonts.add( new FontData('X', new Rectangle(911f, 302f, 55f, 100f) ) );
        rawFonts.add( new FontData('Y', new Rectangle(966f, 302f, 53f, 100f) ) );
        rawFonts.add( new FontData('Z', new Rectangle(0f, 403f, 52f, 100f) ) );
        rawFonts.add( new FontData('[', new Rectangle(52f, 403f, 40f, 100f) ) );
        rawFonts.add( new FontData('\\', new Rectangle(92f, 403f, 49f, 100f) ) );
        rawFonts.add( new FontData(']', new Rectangle(141f, 403f, 40f, 100f) ) );
        rawFonts.add( new FontData('^', new Rectangle(181f, 403f, 75f, 100f) ) );
        rawFonts.add( new FontData('_', new Rectangle(256f, 403f, 67f, 100f) ) );
        rawFonts.add( new FontData('`', new Rectangle(323f, 403f, 44f, 100f) ) );
        rawFonts.add( new FontData('a', new Rectangle(367f, 403f, 50f, 100f) ) );
        rawFonts.add( new FontData('b', new Rectangle(417f, 403f, 51f, 100f) ) );
        rawFonts.add( new FontData('c', new Rectangle(468f, 403f, 47f, 100f) ) );
        rawFonts.add( new FontData('d', new Rectangle(515f, 403f, 51f, 100f) ) );
        rawFonts.add( new FontData('e', new Rectangle(566f, 403f, 49f, 100f) ) );
        rawFonts.add( new FontData('f', new Rectangle(615f, 403f, 40f, 100f) ) );
        rawFonts.add( new FontData('g', new Rectangle(655f, 403f, 51f, 100f) ) );
        rawFonts.add( new FontData('h', new Rectangle(706f, 403f, 51f, 100f) ) );
        rawFonts.add( new FontData('i', new Rectangle(757f, 403f, 39f, 100f) ) );
        rawFonts.add( new FontData('j', new Rectangle(796f, 403f, 39f, 100f) ) );
        rawFonts.add( new FontData('k', new Rectangle(835f, 403f, 51f, 100f) ) );
        rawFonts.add( new FontData('l', new Rectangle(886f, 403f, 39f, 100f) ) );
        rawFonts.add( new FontData('m', new Rectangle(925f, 403f, 62f, 100f) ) );
        rawFonts.add( new FontData('n', new Rectangle(0f, 504f, 51f, 100f) ) );
        rawFonts.add( new FontData('o', new Rectangle(51f, 504f, 50f, 100f) ) );
        rawFonts.add( new FontData('p', new Rectangle(101f, 504f, 51f, 100f) ) );
        rawFonts.add( new FontData('q', new Rectangle(152f, 504f, 51f, 100f) ) );
        rawFonts.add( new FontData('r', new Rectangle(203f, 504f, 45f, 100f) ) );
        rawFonts.add( new FontData('s', new Rectangle(248f, 504f, 46f, 100f) ) );
        rawFonts.add( new FontData('t', new Rectangle(294f, 504f, 41f, 100f) ) );
        rawFonts.add( new FontData('u', new Rectangle(335f, 504f, 51f, 100f) ) );
        rawFonts.add( new FontData('v', new Rectangle(386f, 504f, 48f, 100f) ) );
        rawFonts.add( new FontData('w', new Rectangle(434f, 504f, 58f, 100f) ) );
        rawFonts.add( new FontData('x', new Rectangle(492f, 504f, 50f, 100f) ) );
        rawFonts.add( new FontData('y', new Rectangle(542f, 504f, 47f, 100f) ) );
        rawFonts.add( new FontData('z', new Rectangle(589f, 504f, 48f, 100f) ) );
        rawFonts.add( new FontData('{', new Rectangle(637f, 504f, 43f, 100f) ) );
        rawFonts.add( new FontData('|', new Rectangle(680f, 504f, 45f, 100f) ) );
        rawFonts.add( new FontData('}', new Rectangle(725f, 504f, 43f, 100f) ) );
        rawFonts.add( new FontData('~', new Rectangle(768f, 504f, 75f, 100f) ) );
        rawFonts.add( new FontData('', new Rectangle(843f, 506f, 25f, 102f) ) );

        float tw = 1024f;
        float th = 512f;

        Texture texture = getTextureObj(textureFonts);
        if (texture != null) {
            tw = texture.width;
            th = texture.height;
        }

        FontData fontData;
        TexturedQuad pFont;
        float x, y, w, h;

        int arrSize = rawFonts.size();
        for (int i = 0; i < arrSize; ++i) {
            fontData = rawFonts.get(i);

            x = fontData.rect.x;
            y = fontData.rect.y;
            w = fontData.rect.w;
            h = fontData.rect.h;

            pFont = new TexturedQuad();
            pFont.ch = fontData.ch;
            pFont.w = w;
            pFont.h = h;

            // x								// y
            pFont.tx_lo_left.x = x / tw;        pFont.tx_lo_left.y = (th - (y - h)) / th;  // 0
            pFont.tx_lo_right.x = (x + w) / tw; pFont.tx_lo_right.y = (th - (y - h)) / th; // 1
            pFont.tx_up_right.x = (x + w) / tw; pFont.tx_up_right.y = (th - y) / th;       // 2
            pFont.tx_up_left.x = x / tw;        pFont.tx_up_left.y =  (th - y) / th;       // 3

            fonts.put(String.valueOf(pFont.ch), pFont);


//            float tx0 = pFont.tx_lo_left.x;
//            float tx1 = pFont.tx_up_right.x;
//            float ty0 = pFont.tx_lo_left.y;
//            float ty1 = pFont.tx_up_right.y;

        }
    }

    public static int createTexture(int w, int h) {
        int[] temp = new int[1];
        glGenTextures(1, temp, 0);

        glBindTexture(GL_TEXTURE_2D, temp[0]);

//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        //glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, null);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, w, h, 0, GL_RGB, GL_UNSIGNED_SHORT_5_6_5, null);
        return temp[0];
    }

    public void drawAxes() {
        EdgeDrawer edgeDrawer = new EdgeDrawer(2);
        MyColor color = new MyColor(1f, 1f, 0f, 1f);

        float x = 1f;
        float y = Graphics.aspectRatio; //1f;

        edgeDrawer.begin();
        edgeDrawer.addLine(-x, 0f, 0f, x, 0f, 0f);

        edgeDrawer.addLine(0f, -y, 0f, 0f, y, 0f);

        setIdentityM(modelMatrix, 0);
        multiplyMM(mvpMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);

        colorShader.useProgram();
        colorShader.setUniforms(mvpMatrix, color);
        edgeDrawer.bindData(colorShader);
        edgeDrawer.draw();
    }

    public void prepareFrame() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void onSurfaceChanged(int width, int height) {
        glViewport(0, 0, width, height);

        Graphics.screenWidth = width;
        Graphics.screenHeight = height;
        Graphics.aspectRatio = width > height ? (float)width / (float)height : (float)height / (float)width;
        Graphics.scaleFactor = Graphics.screenWidth / Graphics.referenceScreenWidth;

        ParticleShader.pointSize = (float)width * 0.12f;

        fbo.create(width, height);
    }

}
