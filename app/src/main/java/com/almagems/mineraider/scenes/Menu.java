package com.almagems.mineraider.scenes;

import static android.opengl.GLES20.*;
import static android.opengl.Matrix.*;

import static com.almagems.mineraider.Constants.*;

import com.almagems.mineraider.ClassicSingleton;
import com.almagems.mineraider.EffectAnims.Fade;
import com.almagems.mineraider.Physics;
import com.almagems.mineraider.PositionInfo;
import com.almagems.mineraider.RockData;
import com.almagems.mineraider.Visuals;
import com.almagems.mineraider.data.IndexBuffer;
import com.almagems.mineraider.data.VertexBuffer;
import com.almagems.mineraider.objects.EdgeDrawer;
import com.almagems.mineraider.objects.MineCart;
import com.almagems.mineraider.objects.Model;
import com.almagems.mineraider.objects.Quad;
import com.almagems.mineraider.util.Geometry;
import com.almagems.mineraider.util.MyColor;
import com.almagems.mineraider.util.Rectangle;
import com.almagems.mineraider.util.Text;
import com.almagems.mineraider.util.Texture;
import com.almagems.mineraider.util.Vector;

import java.util.ArrayList;

public class Menu extends Scene {

    // helmet select vars
    private boolean _showNextHelmetArrow;
    private boolean _showPrevHelmetArrow;

    private int _currentHelmetIndex;

    private final Quad _leftArrow;
    private final Quad _rightArrow;
    private Quad _helmetInFocus;
    private final Quad[] _helmets;

    private final Text _textScore;
    private final Text _textSubTitle;
    private final Text _textContinue;
    private final Text _textBack;






    private Fade _fadeSub = new Fade();


    private Physics physics;

    MineCart mineCart;

    private ArrayList<RockData> rocks = new ArrayList<RockData>();

    private boolean _animatePlayButton = false;
    private boolean _animateOptionsButton = false;
    private boolean _animateAboutButton = false;

    private final EdgeDrawer edgeDrawer;

    private final boolean editorEnabled = false;

    private final float menuAnimSpeed = 0.05f;
    private float menuAnimCurrentValue;
    private float menuAnimEndValue;

    private VertexBuffer vbBg;
    private IndexBuffer ibBg;

    private final Quad title;
    private final Quad play;
    private final Quad options;
    private final Quad about;

    private final Quad editorTXPlus;
    private final Quad editorTXMinus;
    private final Quad editorTYPlus;
    private final Quad editorTYMinus;
    private final Quad editorTZPlus;
    private final Quad editorTZMinus;

    private final Quad editorRXPlus;
    private final Quad editorRXMinus;
    private final Quad editorRYPlus;
    private final Quad editorRYMinus;
    private final Quad editorRZPlus;
    private final Quad editorRZMinus;

    private final Quad editorSXPlus;
    private final Quad editorSXMinus;
    private final Quad editorSYPlus;
    private final Quad editorSYMinus;
    private final Quad editorSZPlus;
    private final Quad editorSZMinus;

    private final Quad editorAddNew;
    private final Quad editorChangeType;
    private final Quad editorDump;

    private final PositionInfo _pos;

    private RockData currentRock;

    private enum MenuState {
        MainMenu,
        HelmetSelect,
        Options,
        About
    }

    private MenuState _state = MenuState.MainMenu;

    // ctor
    public Menu() {
        _pos = new PositionInfo();

        title = new Quad();
        play = new Quad();
        options = new Quad();
        about = new Quad();


        editorTXPlus = new Quad();
        editorTXMinus = new Quad();
        editorTYPlus = new Quad();
        editorTYMinus = new Quad();
        editorTZPlus = new Quad();
        editorTZMinus = new Quad();

        editorRXPlus = new Quad();
        editorRXMinus = new Quad();
        editorRYPlus = new Quad();
        editorRYMinus = new Quad();
        editorRZPlus = new Quad();
        editorRZMinus = new Quad();

        editorSXPlus = new Quad();
        editorSXMinus = new Quad();
        editorSYPlus = new Quad();
        editorSYMinus = new Quad();
        editorSZPlus = new Quad();
        editorSZMinus = new Quad();

        editorAddNew = new Quad();
        editorChangeType = new Quad();
        editorDump = new Quad();

        edgeDrawer = new EdgeDrawer(32);

        physics = new Physics();

        // sin
        physics.addBoxStatic(0.0f, -19.7f, 0f, 70.0f, 0.5f);

        // minecart
        mineCart = new MineCart(physics, -1f, -15.7f);
        mineCart.z = 1f;
        mineCart._sceneType = ScenesEnum.Menu;
        mineCart.start(-3f);


        _helmets = new Quad[MAX_HELMET_TYPES];
        for (int i = 0; i < MAX_HELMET_TYPES; ++i) {
            _helmets[i] = new Quad();
        }

        _leftArrow = new Quad();
        _rightArrow = new Quad();

        _textScore = new Text();
        _textSubTitle = new Text();
        _textContinue = new Text();
        _textBack = new Text();

    }

    @Override
	protected void surfaceChanged(int width, int height) {
        final float r = 1f;
        final float g = 1f;
        final float b = 1f;
        final float a = 1f;
        final float aspect = Visuals.aspectRatio;

        final float x = 1.0f;
        final float y = aspect;

		float[] vertices = {
				// x, y, z, 			        u, v,
				-x, -y, 0.0f,   r, g, b, a,     0.0f, 0.0f, // 0
				 x, -y, 0.0f,	r, g, b, a,     1.0f, 0.0f, // 1
				 x,  y, 0.0f,	r, g, b, a,     1.0f, 1.0f, // 2
				 
				-x, -y, 0.0f,	r, g, b, a,     0.0f, 0.0f, // 3
			 	 x,  y, 0.0f,	r, g, b, a,     1.0f, 1.0f, // 4
				-x,  y, 0.0f,	r, g, b, a,     0.0f, 1.0f  // 5
		};

        short[] indices = {
            // for gl_lines
            //0, 1,
            //1, 2,
            //2, 5,
            //5, 0

            0, 1, 2,
            3, 4, 5
        };

		vbBg = new VertexBuffer(vertices);
        ibBg = new IndexBuffer(indices);

        Rectangle rect;
        MyColor whiteColor = new MyColor(1f, 1f, 1f, 1f);
        final boolean flipUTextureCoordinate = false;



        rect = new Rectangle(0, 573+306, 1080, 306);
        title.init(visuals.textureMenuItems, whiteColor, rect, flipUTextureCoordinate);
        title.pos.trans(0f, aspect * 0.65f, 0f);
        title.pos.rot(0f, 0f, 0f);
        title.pos.scale(rect.w / Visuals.referenceScreenWidth, rect.h / Visuals.referenceScreenWidth, 1.0f);


        rect = new Rectangle(0, 382+191, 1080, 191);
        play.init(visuals.textureMenuItems, whiteColor, rect, flipUTextureCoordinate);
        play.pos.trans(0.0f, aspect * 0.1f, 0f);
        play.pos.rot(0f, 0f, 0f);
        play.pos.scale(rect.w / Visuals.referenceScreenWidth, rect.h / Visuals.referenceScreenWidth, 1.0f);

        rect = new Rectangle(0, 191+191, 1080, 191);
        options.init(visuals.textureMenuItems, whiteColor, rect, flipUTextureCoordinate);
        options.pos.trans(0.0f, -aspect * 0.2f, 0f);
        options.pos.rot(0f, 0f, 0f);
        options.pos.scale(rect.w / Visuals.referenceScreenWidth, rect.h / Visuals.referenceScreenWidth, 1.0f);

        rect = new Rectangle(0, 0+191, 1080, 191);
        about.init(visuals.textureMenuItems, whiteColor, rect, flipUTextureCoordinate);
        about.pos.trans(0.0f, -aspect * 0.5f, 0f);
        about.pos.rot(0f, 0f, 0f);
        about.pos.scale(rect.w / Visuals.referenceScreenWidth, rect.h / Visuals.referenceScreenWidth, 1.0f);



        // setup editor buttons
        float tx = -0.9f;
        float ty = 1.0f;

        // tx
        rect = new Rectangle(0f, 0+85f, 85f, 85f);
        editorTXPlus.init(visuals.textureEditorButtons, whiteColor, rect, flipUTextureCoordinate);
        editorTXPlus.pos.trans(tx, ty, 0f);
        editorTXPlus.pos.rot(0f, 0f, 0f);
        editorTXPlus.pos.scale(rect.w / Visuals.referenceScreenWidth, rect.h / Visuals.referenceScreenWidth, 1.0f);

        rect = new Rectangle(85f, 0+85f, 85f, 85f);
        editorTXMinus.init(visuals.textureEditorButtons, whiteColor, rect, flipUTextureCoordinate);
        editorTXMinus.pos.trans(tx + 0.2f * 1f, ty, 0f);
        editorTXMinus.pos.rot(0f, 0f, 0f);
        editorTXMinus.pos.scale(rect.w / Visuals.referenceScreenWidth, rect.h / Visuals.referenceScreenWidth, 1.0f);

        // ty
        rect = new Rectangle(85f * 2f, 0+85f, 85f, 85f);
        editorTYPlus.init(visuals.textureEditorButtons, whiteColor, rect, flipUTextureCoordinate);
        editorTYPlus.pos.trans(tx + 0.2f * 2f, ty, 0f);
        editorTYPlus.pos.rot(0f, 0f, 0f);
        editorTYPlus.pos.scale(rect.w / Visuals.referenceScreenWidth, rect.h / Visuals.referenceScreenWidth, 1.0f);

        rect = new Rectangle(85f * 3f, 0+85f, 85f, 85f);
        editorTYMinus.init(visuals.textureEditorButtons, whiteColor, rect, flipUTextureCoordinate);
        editorTYMinus.pos.trans(tx + 0.2f * 3f, ty, 0f);
        editorTYMinus.pos.rot(0f, 0f, 0f);
        editorTYMinus.pos.scale(rect.w / Visuals.referenceScreenWidth, rect.h / Visuals.referenceScreenWidth, 1.0f);

        // tz
        rect = new Rectangle(85f * 4f, 0+85f, 85f, 85f);
        editorTZPlus.init(visuals.textureEditorButtons, whiteColor, rect, flipUTextureCoordinate);
        editorTZPlus.pos.trans(tx + 0.2f * 4f, ty, 0f);
        editorTZPlus.pos.rot(0f, 0f, 0f);
        editorTZPlus.pos.scale(rect.w / Visuals.referenceScreenWidth, rect.h / Visuals.referenceScreenWidth, 1.0f);

        rect = new Rectangle(85f * 5f, 0+85f, 85f, 85f);
        editorTZMinus.init(visuals.textureEditorButtons, whiteColor, rect, flipUTextureCoordinate);
        editorTZMinus.pos.trans(tx + 0.2f * 5f, ty, 0f);
        editorTZMinus.pos.rot(0f, 0f, 0f);
        editorTZMinus.pos.scale(rect.w / Visuals.referenceScreenWidth, rect.h / Visuals.referenceScreenWidth, 1.0f);


        ty = 0.75f;
        // rx
        rect = new Rectangle(0f, 0+85f*2f, 85f, 85f);
        editorRXPlus.init(visuals.textureEditorButtons, whiteColor, rect, flipUTextureCoordinate);
        editorRXPlus.pos.trans(tx, ty, 0f);
        editorRXPlus.pos.rot(0f, 0f, 0f);
        editorRXPlus.pos.scale(rect.w / Visuals.referenceScreenWidth, rect.h / Visuals.referenceScreenWidth, 1.0f);

        rect = new Rectangle(85f, 0+85f*2f, 85f, 85f);
        editorRXMinus.init(visuals.textureEditorButtons, whiteColor, rect, flipUTextureCoordinate);
        editorRXMinus.pos.trans(tx + 0.2f * 1f, ty, 0f);
        editorRXMinus.pos.rot(0f, 0f, 0f);
        editorRXMinus.pos.scale(rect.w / Visuals.referenceScreenWidth, rect.h / Visuals.referenceScreenWidth, 1.0f);

        // ry
        rect = new Rectangle(85f * 2f, 0+85f*2f, 85f, 85f);
        editorRYPlus.init(visuals.textureEditorButtons, whiteColor, rect, flipUTextureCoordinate);
        editorRYPlus.pos.trans(tx + 0.2f * 2f, ty, 0f);
        editorRYPlus.pos.rot(0f, 0f, 0f);
        editorRYPlus.pos.scale(rect.w / Visuals.referenceScreenWidth, rect.h / Visuals.referenceScreenWidth, 1.0f);

        rect = new Rectangle(85f * 3f, 0+85f*2f, 85f, 85f);
        editorRYMinus.init(visuals.textureEditorButtons, whiteColor, rect, flipUTextureCoordinate);
        editorRYMinus.pos.trans(tx + 0.2f * 3f, ty, 0f);
        editorRYMinus.pos.rot(0f, 0f, 0f);
        editorRYMinus.pos.scale(rect.w / Visuals.referenceScreenWidth, rect.h / Visuals.referenceScreenWidth, 1.0f);

        // rz
        rect = new Rectangle(85f * 4f, 0+85f*2f, 85f, 85f);
        editorRZPlus.init(visuals.textureEditorButtons, whiteColor, rect, flipUTextureCoordinate);
        editorRZPlus.pos.trans(tx + 0.2f * 4f, ty, 0f);
        editorRZPlus.pos.rot(0f, 0f, 0f);
        editorRZPlus.pos.scale(rect.w / Visuals.referenceScreenWidth, rect.h / Visuals.referenceScreenWidth, 1.0f);

        rect = new Rectangle(85f * 5f, 0+85f*2f, 85f, 85f);
        editorRZMinus.init(visuals.textureEditorButtons, whiteColor, rect, flipUTextureCoordinate);
        editorRZMinus.pos.trans(tx + 0.2f * 5f, ty, 0f);
        editorRZMinus.pos.rot(0f, 0f, 0f);
        editorRZMinus.pos.scale(rect.w / Visuals.referenceScreenWidth, rect.h / Visuals.referenceScreenWidth, 1.0f);



        ty = 0.5f;
        // sx
        rect = new Rectangle(0f, 0+85f*3f, 85f, 85f);
        editorSXPlus.init(visuals.textureEditorButtons, whiteColor, rect, flipUTextureCoordinate);
        editorSXPlus.pos.trans(tx, ty, 0f);
        editorSXPlus.pos.rot(0f, 0f, 0f);
        editorSXPlus.pos.scale(rect.w / Visuals.referenceScreenWidth, rect.h / Visuals.referenceScreenWidth, 1.0f);

        rect = new Rectangle(85f, 0+85f*3f, 85f, 85f);
        editorSXMinus.init(visuals.textureEditorButtons, whiteColor, rect, flipUTextureCoordinate);
        editorSXMinus.pos.trans(tx + 0.2f * 1f, ty, 0f);
        editorSXMinus.pos.rot(0f, 0f, 0f);
        editorSXMinus.pos.scale(rect.w / Visuals.referenceScreenWidth, rect.h / Visuals.referenceScreenWidth, 1.0f);

        // sy
        rect = new Rectangle(85f * 2f, 0+85f*3f, 85f, 85f);
        editorSYPlus.init(visuals.textureEditorButtons, whiteColor, rect, flipUTextureCoordinate);
        editorSYPlus.pos.trans(tx + 0.2f * 2f, ty, 0f);
        editorSYPlus.pos.rot(0f, 0f, 0f);
        editorSYPlus.pos.scale(rect.w / Visuals.referenceScreenWidth, rect.h / Visuals.referenceScreenWidth, 1.0f);

        rect = new Rectangle(85f * 3f, 0+85f*3f, 85f, 85f);
        editorSYMinus.init(visuals.textureEditorButtons, whiteColor, rect, flipUTextureCoordinate);
        editorSYMinus.pos.trans(tx + 0.2f * 3f, ty, 0f);
        editorSYMinus.pos.rot(0f, 0f, 0f);
        editorSYMinus.pos.scale(rect.w / Visuals.referenceScreenWidth, rect.h / Visuals.referenceScreenWidth, 1.0f);

        // sz
        rect = new Rectangle(85f * 4f, 0+85f*3f, 85f, 85f);
        editorSZPlus.init(visuals.textureEditorButtons, whiteColor, rect, flipUTextureCoordinate);
        editorSZPlus.pos.trans(tx + 0.2f * 4f, ty, 0f);
        editorSZPlus.pos.rot(0f, 0f, 0f);
        editorSZPlus.pos.scale(rect.w / Visuals.referenceScreenWidth, rect.h / Visuals.referenceScreenWidth, 1.0f);

        rect = new Rectangle(85f * 5f, 0+85f*3f, 85f, 85f);
        editorSZMinus.init(visuals.textureEditorButtons, whiteColor, rect, flipUTextureCoordinate);
        editorSZMinus.pos.trans(tx + 0.2f * 5f, ty, 0f);
        editorSZMinus.pos.rot(0f, 0f, 0f);
        editorSZMinus.pos.scale(rect.w / Visuals.referenceScreenWidth, rect.h / Visuals.referenceScreenWidth, 1.0f);



        ty = 0.25f;
        // sx
        rect = new Rectangle(0f, 0+85f*4f, 85f, 85f);
        editorAddNew.init(visuals.textureEditorButtons, whiteColor, rect, flipUTextureCoordinate);
        editorAddNew.pos.trans(tx, ty, 0f);
        editorAddNew.pos.rot(0f, 0f, 0f);
        editorAddNew.pos.scale(rect.w / Visuals.referenceScreenWidth, rect.h / Visuals.referenceScreenWidth, 1.0f);

        rect = new Rectangle(85f, 0+85f*4f, 85f, 85f);
        editorChangeType.init(visuals.textureEditorButtons, whiteColor, rect, flipUTextureCoordinate);
        editorChangeType.pos.trans(tx + 0.2f * 1f, ty, 0f);
        editorChangeType.pos.rot(0f, 0f, 0f);
        editorChangeType.pos.scale(rect.w / Visuals.referenceScreenWidth, rect.h / Visuals.referenceScreenWidth, 1.0f);

        rect = new Rectangle(85f * 2f, 0+85f*4f, 85f, 85f);
        editorDump.init(visuals.textureEditorButtons, whiteColor, rect, flipUTextureCoordinate);
        editorDump.pos.trans(tx + 0.2f * 2f, ty, 0f);
        editorDump.pos.rot(0f, 0f, 0f);
        editorDump.pos.scale(rect.w / Visuals.referenceScreenWidth, rect.h / Visuals.referenceScreenWidth, 1.0f);




        rocks.add( new RockData(3, -1.5f, -21.0f, -8.25f, 0.0f, 0.0f, 0.0f, 2.1000001f, 1.8000002f, 1.7000002f) );
        rocks.add( new RockData(0, -13.0f, -19.0f, -7.75f, 0.0f, 0.0f, 0.0f, 2.1000001f, 1.9000002f, 1.6000001f) );
        rocks.add( new RockData(2, -7.0f, -20.5f, -9.25f, 0.0f, 0.0f, 0.0f, 2.3999999f, 1.9000002f, 2.1000001f) );
        rocks.add( new RockData(3, -7.5f, -18.0f, -11.75f, 0.0f, -18.0f, 0.0f, 2.0000002f, 1.9000002f, 2.0000002f) );
        rocks.add( new RockData(7, 0.5f, -18.0f, -11.75f, 24.0f, 0.0f, 0.0f, 2.75f, 2.5f, 2.25f) );
        rocks.add( new RockData(4, 2.0f, -20.0f, 7.25f, 0.0f, 0.0f, 0.0f, 1.75f, 1.75f, 2.0f) );
        rocks.add( new RockData(0, 3.0f, -19.0f, 11.75f, 0.0f, 0.0f, 0.0f, 2.0f, 1.75f, 2.0f) );
        rocks.add( new RockData(8, 2.0f, -16.0f, 6.25f, 0.0f, 222.0f, 0.0f, 1.5f, 1.75f, 1.5f) );
        rocks.add( new RockData(4, 3.0f, -15.5f, 10.25f, 0.0f, 102.0f, 0.0f, 1.75f, 2.25f, 1.75f) );
        rocks.add( new RockData(0, 0.0f, -21.0f, 15.75f, -54.0f, 0.0f, 0.0f, 1.75f, 1.75f, 2.25f) );
        rocks.add( new RockData(5, 3.5f, -15.0f, 15.25f, 6.0f, -51.0f, -60.0f, 1.5f, 1.5f, 1.5f) );
        rocks.add( new RockData(8, 2.5f, -14.5f, -8.25f, 0.0f, 0.0f, 0.0f, 1.75f, 1.5f, 2.0f) );
        rocks.add( new RockData(4, 0.0f, -21.5f, 11.25f, 0.0f, 0.0f, 0.0f, 1.5f, 1.5f, 1.5f) );
        rocks.add( new RockData(3, 2.0f, -9.5f, -4.25f, 0.0f, 0.0f, 0.0f, 1.25f, 1.25f, 1.25f) );
        rocks.add( new RockData(2, 2.0f, -10.5f, 4.75f, 0.0f, 0.0f, -33.0f, 1.25f, 1.25f, 1.25f) );
        rocks.add( new RockData(4, 1.5f, -9.5f, -1.25f, 0.0f, 0.0f, -24.0f, 1.25f, 1.25f, 1.5f) );
        rocks.add( new RockData(6, 1.0f, -10.0f, 2.25f, 0.0f, 0.0f, 0.0f, 1.5f, 1.5f, 1.75f) );
        rocks.add( new RockData(8, 3.5f, -10.0f, 7.25f, 0.0f, 0.0f, -27.0f, 1.25f, 1.25f, 1.25f) );
        rocks.add( new RockData(1, 6.5f, -10.5f, 11.75f, 0.0f, 0.0f, 0.0f, 1.75f, 1.5f, 1.75f) );
        rocks.add( new RockData(1, 7.5f, -8.0f, 9.25f, 0.0f, 0.0f, -60.0f, 1.25f, 1.25f, 1.25f) );
        rocks.add( new RockData(3, 4.5f, -9.0f, 2.25f, 0.0f, 0.0f, 15.0f, 1.5f, 1.5f, 1.5f) );
        rocks.add( new RockData(7, 5.0f, -8.0f, -1.75f, -15.0f, 0.0f, 0.0f, 1.25f, 1.25f, 1.25f) );
        rocks.add( new RockData(1, 4.5f, -18.5f, -8.75f, 0.0f, 0.0f, 0.0f, 1.75f, 1.75f, 2.25f) );
        rocks.add( new RockData(1, 10.5f, -18.0f, -7.25f, 0.0f, 0.0f, 0.0f, 1.5f, 2.25f, 1.5f) );
        rocks.add( new RockData(8, -3.0f, -15.5f, -16.25f, 0.0f, 0.0f, -54.0f, 1.25f, 1.25f, 1.75f) );
        rocks.add( new RockData(6, -5.5f, -20.5f, -7.25f, 0.0f, 0.0f, 0.0f, 1.25f, 1.25f, 2.25f) );
        rocks.add( new RockData(0, 7.5f, -8.5f, 4.25f, 0.0f, 0.0f, 0.0f, 1.75f, 1.75f, 1.75f) );
        rocks.add( new RockData(7, 9.5f, -7.0f, -0.75f, 0.0f, 0.0f, 0.0f, 1.5f, 1.5f, 1.5f) );
        rocks.add( new RockData(1, 17.5f, -7.0f, -0.25f, 0.0f, 0.0f, 0.0f, 2.0f, 1.25f, 1.75f) );
        rocks.add( new RockData(0, 9.5f, -7.0f, 12.75f, 0.0f, -39.0f, -33.0f, 1.5f, 1.5f, 1.75f) );
        rocks.add( new RockData(0, 15.5f, -6.0f, 6.25f, -39.0f, 0.0f, 0.0f, 1.5f, 1.5f, 1.5f) );
        rocks.add( new RockData(1, -22.0f, -20.5f, -9.25f, 0.0f, 39.0f, 0.0f, 2.25f, 2.25f, 2.25f) );
        rocks.add( new RockData(7, -17.0f, -20.0f, -9.25f, 18.0f, 93.0f, 0.0f, 2.5f, 2.25f, 2.75f) );
        rocks.add( new RockData(3, 2.5f, -13.0f, -14.25f, 57.0f, 6.0f, 57.0f, 2.0f, 2.0f, 1.75f) );
        rocks.add( new RockData(0, 3.5f, -6.5f, -1.25f, -30.0f, 0.0f, 0.0f, 1.25f, 1.25f, 1.5f) );
        rocks.add( new RockData(6, 1.0f, -11.0f, -7.75f, 0.0f, -39.0f, 15.0f, 1.5f, 1.5f, 1.75f) );
	}

    private void initOptions(int width, int height) {
        float textWidth;

        _textSubTitle.init("OPTIONS", new MyColor(1f, 1f, 1f, 1f), new MyColor(1f, 1f, 1f, 1f), 1.3f);
        textWidth = _textSubTitle.getTextWidth();
        _textSubTitle.pos.trans(-textWidth / 2f, Visuals.aspectRatio * 0.4f, 0f);
        _textSubTitle.pos.rot(0f, 0f, 0f);
        _textSubTitle.pos.scale(1f, 1f, 1f);

        _textBack.init("BACK", new MyColor(1f, 1f, 1f, 1f), new MyColor(1f, 1f, 1f, 1f), 0.9f);
        textWidth = _textBack.getTextWidth();
        _textBack.pos.trans(-0.5f - (textWidth / 2f), -Visuals.aspectRatio * 0.9f, 0f);
        _textBack.pos.rot(0f, 0f, 0f);
        _textBack.pos.scale(1f, 1f, 1f);

    }

    private void initAbout(int width, int height) {
        float textWidth;

        _textSubTitle.init("ABOUT", new MyColor(1f, 1f, 1f, 1f), new MyColor(1f, 1f, 1f, 1f), 1.3f);
        textWidth = _textSubTitle.getTextWidth();
        _textSubTitle.pos.trans(-textWidth / 2f, Visuals.aspectRatio * 0.4f, 0f);
        _textSubTitle.pos.rot(0f, 0f, 0f);
        _textSubTitle.pos.scale(1f, 1f, 1f);

        _textBack.init("BACK", new MyColor(1f, 1f, 1f, 1f), new MyColor(1f, 1f, 1f, 1f), 0.9f);
        textWidth = _textBack.getTextWidth();
        _textBack.pos.trans(-0.5f - (textWidth / 2f), -Visuals.aspectRatio * 0.9f, 0f);
        _textBack.pos.rot(0f, 0f, 0f);
        _textBack.pos.scale(1f, 1f, 1f);

    }

    private void initHelmets(int width, int height) {
        _showNextHelmetArrow = true;
        _showPrevHelmetArrow = true;

        float textWidth;

        _textSubTitle.init("SELECT YOUR HELMET", new MyColor(1f, 1f, 1f, 1f), new MyColor(1f, 1f, 1f, 1f), 1.3f);
        textWidth = _textSubTitle.getTextWidth();
        _textSubTitle.pos.trans(-textWidth / 2f, Visuals.aspectRatio * 0.4f, 0f);
        _textSubTitle.pos.rot(0f, 0f, 0f);
        _textSubTitle.pos.scale(1f, 1f, 1f);

        _textContinue.init("CONTINUE", new MyColor(1f, 1f, 1f, 1f), new MyColor(1f, 1f, 1f, 1f), 0.9f);
        textWidth = _textContinue.getTextWidth();
        _textContinue.pos.trans(0.5f - (textWidth / 2f) , -Visuals.aspectRatio * 0.9f, 0f);
        _textContinue.pos.rot(0f, 0f, 0f);
        _textContinue.pos.scale(1f, 1f, 1f);

        _textBack.init("BACK", new MyColor(1f, 1f, 1f, 1f), new MyColor(1f, 1f, 1f, 1f), 0.9f);
        textWidth = _textContinue.getTextWidth();
        _textBack.pos.trans(-0.5f - (textWidth / 2f), -Visuals.aspectRatio * 0.9f, 0f);
        _textBack.pos.rot(0f, 0f, 0f);
        _textBack.pos.scale(1f, 1f, 1f);


        _textScore.init("SCORE", new MyColor(1f, 1f, 1f, 1f), new MyColor(1f, 1f, 1f, 1f), 1.0f);
        textWidth = _textScore.getTextWidth();
        _textScore.pos.trans(-textWidth / 2f, -0.4f, 0f);
        _textScore.pos.rot(0f, 0f, 0f);
        _textScore.pos.scale(1f, 1f, 1f);

        float quadScale = 0.1f;
        final boolean flipUCoordinate = false;
        MyColor colorWhite = new MyColor(1f, 1f, 1f, 1f);


        _leftArrow.init(visuals.textureNextArrow, colorWhite, new Rectangle(0f, 0f, -128f, 128f), flipUCoordinate);
        _leftArrow.pos.trans(-0.75f, 0.0f, 0f);
        _leftArrow.pos.rot(0f, 0f, 0f);
        _leftArrow.pos.scale(quadScale, quadScale, 1f);


        _rightArrow.init(visuals.textureNextArrow, colorWhite, new Rectangle(0f, 0f, 128f, 128f), flipUCoordinate);
        _rightArrow.pos.trans(0.75f, 0.0f, 0f);
        _rightArrow.pos.rot(0f, 0f, 0f);
        _rightArrow.pos.scale(quadScale, quadScale, 1f);


        quadScale = 0.3f;

        // red
        _helmets[RED_HELMET].init(visuals.textureHelmets, colorWhite, rectRedHelmet, flipUCoordinate);
        _helmets[RED_HELMET].pos.trans(0.0f, 0.0f, 0f);
        _helmets[RED_HELMET].pos.rot(0f, 0f, 0f);
        _helmets[RED_HELMET].pos.scale(quadScale, quadScale, 1f);

        // green
        _helmets[GREEN_HELMET].init(visuals.textureHelmets, colorWhite, rectGreenHelmet, flipUCoordinate);
        _helmets[GREEN_HELMET].pos.trans(0.0f, 0.0f, 0f);
        _helmets[GREEN_HELMET].pos.rot(0f, 0f, 0f);
        _helmets[GREEN_HELMET].pos.scale(quadScale, quadScale, 1f);

        // blue
        _helmets[BLUE_HELMET].init(visuals.textureHelmets, colorWhite, rectBlueHelmet, flipUCoordinate);
        _helmets[BLUE_HELMET].pos.trans(0.0f, 0.0f, 0f);
        _helmets[BLUE_HELMET].pos.rot(0f, 0f, 0f);
        _helmets[BLUE_HELMET].pos.scale(quadScale, quadScale, 1f);

        // yellow
        _helmets[YELLOW_HELMET].init(visuals.textureHelmets, colorWhite, rectYellowHelmet, flipUCoordinate);
        _helmets[YELLOW_HELMET].pos.trans(0.0f, 0.0f, 0f);
        _helmets[YELLOW_HELMET].pos.rot(0f, 0f, 0f);
        _helmets[YELLOW_HELMET].pos.scale(quadScale, quadScale, 1f);

        ClassicSingleton singleton = ClassicSingleton.getInstance();
        _currentHelmetIndex = singleton.selectedHelmetIndex;

        setCurrentHelmet(_currentHelmetIndex);
    }

    private void setCurrentHelmet(int index) {
        _helmetInFocus = _helmets[index];
        _currentHelmetIndex = index;
        int score = ClassicSingleton.getInstance().loadPreferences(_currentHelmetIndex);

        _textScore.init("" + score, new MyColor(1f, 1f, 1f, 1f), new MyColor(1f, 1f, 1f, 1f), 1.0f);
        float textWidth = _textScore.getTextWidth();
        _textScore.pos.trans(-textWidth / 2f, -0.4f, 0f);
        _textScore.pos.rot(0f, 0f, 0f);
        _textScore.pos.scale(1f, 1f, 1f);
    }

    private void stepCurrentHelmet(int step) {
        _showNextHelmetArrow = true;
        _showPrevHelmetArrow = true;

        int nextIndex = _currentHelmetIndex + step;
        if (step > 0) {
            if (nextIndex < MAX_HELMET_TYPES) {
                setCurrentHelmet(nextIndex);
            }
        } else {
            if (nextIndex >= 0) {
                setCurrentHelmet(nextIndex);
            }
        }

        if (_currentHelmetIndex == 0) {
            _showPrevHelmetArrow = false;
        }

        if (_currentHelmetIndex == MAX_HELMET_TYPES - 1) {
            _showNextHelmetArrow = false;
        }
    }


    @Override
    public void prepare() {
        super.prepare();

        _state = MenuState.MainMenu;

        _animatePlayButton = false;
        _animateOptionsButton = false;
        _animateAboutButton = false;
    }

	@Override
	public void update() {
        physics.update();
        visuals.updateViewProjMatrix();
	}

	@Override
	public void draw() {
        //play.op.tx += 0.01f;

        visuals.setProjectionMatrix2D();
        visuals.updateViewProjMatrix();
        visuals.textureShader.useProgram();

		glDisable(GL_DEPTH_TEST);
        glDisable(GL_BLEND);

        visuals.textureShader.setTexture(visuals.textureMenu);
		drawBg();

        glEnable(GL_DEPTH_TEST);
        visuals.setProjectionMatrix3dForMenu();
        visuals.updateViewProjMatrix();

        visuals.dirLightShader.useProgram();
        visuals.dirLightShader.setTexture(visuals.textureFloor);
        drawFloor();

        visuals.dirLightShader.setTexture(visuals.textureRailRoad);
        drawRailRoad();

        visuals.dirLightShader.setTexture(visuals.textureMineEntranceBeam);
        drawBeams();

        visuals.dirLightShader.setTexture(visuals.textureMineInterior);
        drawMineInterior();


        drawMineCarts();

        visuals.dirLightShader.setTexture(visuals.textureCliff142);
        drawRocks();

        visuals.setProjectionMatrix2D();
        visuals.updateViewProjMatrix();
        visuals.textureShader.useProgram();

        glDisable(GL_DEPTH_TEST);



		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_BLEND);

        visuals.textureShader.setTexture(visuals.textureMenuItems);
        title.draw();


        switch (_state) {
            case MainMenu:
                drawMenuItemes();
                break;

            case Options:
                drawOptions();
                break;

            case About:
                drawAbout();
                break;

            case HelmetSelect:
                drawHelmetSelect();
                break;
        }



        if (editorEnabled) {
            drawEditorButtons();
        }

        //visuals.textureShader.setTexture(visuals.textureFonts);
        //text.draw();
       // credits.draw();

        visuals.bindNoTexture();

        //drawEdge();
        glDisable(GL_DEPTH_TEST);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);

        super.drawFade();
	}

    void drawEditorButtons() {
        editorTXPlus.draw();
        editorTXMinus.draw();
        editorTYPlus.draw();
        editorTYMinus.draw();
        editorTZPlus.draw();
        editorTZMinus.draw();

        editorRXPlus.draw();
        editorRXMinus.draw();
        editorRYPlus.draw();
        editorRYMinus.draw();
        editorRZPlus.draw();
        editorRZMinus.draw();


        editorSXPlus.draw();
        editorSXMinus.draw();
        editorSYPlus.draw();
        editorSYMinus.draw();
        editorSZPlus.draw();
        editorSZMinus.draw();

        editorAddNew.draw();
        editorChangeType.draw();
        editorDump.draw();
    }

    void drawRock(Model rock, RockData rd) {
        _pos.trans(rd.x, rd.y, rd.z);
        _pos.rot(rd.rx, rd.ry, rd.rz);
        _pos.scale(rd.sx, rd.sy, rd.sz);

        visuals.calcMatricesForObject(_pos);
        visuals.dirLightShader.setUniforms();
        rock.bindData(visuals.dirLightShader);
        rock.draw();
    }

    void drawRockTypes() {
        // rock types
        RockData rd = new RockData();
        rd.x = 13.5f;
        rd.y = 10f;
        rd.z = 0f;

        drawRock(visuals.rock0, rd);
        rd.x = 10f;
        drawRock(visuals.rock1, rd);
        rd.x = 7f;
        drawRock(visuals.rock2,	rd);
        rd.x = 4f;
        drawRock(visuals.rock3,	rd);
        rd.x = 1f;
        drawRock(visuals.rock4,	rd);
        rd.x = -3f;
        drawRock(visuals.rock5,	rd);
        rd.x = -7f;
        drawRock(visuals.rock6, rd);
        rd.x = -10f;
        drawRock(visuals.rock7, rd);
        rd.x = -13.5f;
        drawRock(visuals.rock8, rd);
    }

    void drawRocks() {
        RockData rd;
        int size = rocks.size();
        for (int i = 0; i < size; ++i) {
            rd = rocks.get(i);
            switch(rd.rockId) {
                case 0: drawRock(visuals.rock0, rd); break;
                case 1: drawRock(visuals.rock1, rd); break;
                case 2: drawRock(visuals.rock2,	rd); break;
                case 3: drawRock(visuals.rock3,	rd); break;
                case 4: drawRock(visuals.rock4,	rd); break;
                case 5: drawRock(visuals.rock5,	rd); break;
                case 6: drawRock(visuals.rock6, rd); break;
                case 7: drawRock(visuals.rock7, rd); break;
                case 8: drawRock(visuals.rock8, rd); break;
            }
        }
    }

    void drawMineCarts() {
        mineCart.draw();
    }

    void drawMineInterior() {
        visuals.mineInterior.bindData(visuals.dirLightShader);

        _pos.trans(10f, -17.0f, -4f);
        _pos.rot(0f, 0f, 0f);
        _pos.scale(1.0f, 1.0f, 1.0f);
        visuals.calcMatricesForObject(_pos);
        visuals.dirLightShader.setUniforms();
        visuals.mineInterior.draw();
    }

    void drawFloor() {
        visuals.floor.bindData(visuals.dirLightShader);

        _pos.trans(-10f, -20.5f, 0f);
        _pos.rot(0f, 0f, 0f);
        _pos.scale(1.0f, 1.0f, 1.0f);
        visuals.calcMatricesForObject(_pos);
        visuals.dirLightShader.setUniforms();
        visuals.floor.draw();

        _pos.trans(-10f, -20.5f, 10f);
        _pos.rot(0f, 0f, 0f);
        _pos.scale(1.0f, 1.0f, 1.0f);
        visuals.calcMatricesForObject(_pos);
        visuals.dirLightShader.setUniforms();
        visuals.floor.draw();

        _pos.trans(-10f, -20.5f, -10f);
        _pos.rot(0f, 0f, 0f);
        _pos.scale(1.0f, 1.0f, 1.0f);
        visuals.calcMatricesForObject(_pos);
        visuals.dirLightShader.setUniforms();
        visuals.floor.draw();
    }

    void drawRailRoad() {
        visuals.railroad.bindData(visuals.dirLightShader);

        float x = -22f;
        float y = -20.0f;
        float z =  1.0f;
        float tempZ;
        for(int i = 0; i < 3; ++i) {
            tempZ = z;
            _pos.trans(x, y, z);
            _pos.rot(0f, 0f, 0f);
            _pos.scale(1f, 1f, 1f);

            visuals.calcMatricesForObject(_pos);
            visuals.dirLightShader.setUniforms();
            visuals.railroad.draw();

            z = tempZ;
            x += 10.0f;
        }
    }

    void drawBeams() {
        visuals.mineEntranceBeam.bindData(visuals.dirLightShader);

        float x = 0.0f;
        float y = -12.0f;
        float z = 0.0f;

        _pos.trans(x, y, z);
        _pos.rot(0f, 0f, 0f);
        _pos.scale(1.0f, 1f, 1.0f);

        visuals.calcMatricesForObject(_pos);
        visuals.dirLightShader.setUniforms();
        visuals.mineEntranceBeam.draw();


        _pos.trans(x, y-4.0f, z+4.3f);
        _pos.rot(85f, 0f, 0f);
        _pos.scale(1.0f, 1f, 1.0f);

        visuals.calcMatricesForObject(_pos);
        visuals.dirLightShader.setUniforms();
        visuals.mineEntranceBeam.draw();


        _pos.trans(x, y-4.0f, z-4.3f);
        _pos.rot(-85f, 0f, 120f);
        _pos.scale(1.0f, 1f, 1.0f);

        visuals.calcMatricesForObject(_pos);
        visuals.dirLightShader.setUniforms();
        visuals.mineEntranceBeam.draw();
    }

    private void drawBg() {
        _pos.trans(0f, 0f, 0f);
        _pos.rot(0f, 0f, 0f);
        _pos.scale(1.0f, 1.0f, 1.0f);

		visuals.calcMatricesForObject(_pos);
		visuals.textureShader.setUniforms(visuals.mvpMatrix);

        vbBg.bind();
        ibBg.bind();
        visuals.textureShader.bindData(vbBg);
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_SHORT, 0);
        ibBg.unbind();
        vbBg.unbind();
	}

    private void doEditorStuff(float x, float y) {
        if (currentRock != null) {

            // trans
            if (editorTXPlus.isHit(x, y)) {
                System.out.println("tx plus");
                currentRock.x += 0.5f;
            }

            if (editorTXMinus.isHit(x, y)) {
                System.out.println("tx minus");
                currentRock.x -= 0.5f;
            }

            if (editorTYPlus.isHit(x, y)) {
                System.out.println("ty plus");
                currentRock.y += 0.5f;
            }

            if (editorTYMinus.isHit(x, y)) {
                System.out.println("ty minus");
                currentRock.y -= 0.5f;
            }

            if (editorTZPlus.isHit(x, y)) {
                System.out.println("tz plus");
                currentRock.z += 0.5f;
            }

            if (editorTZMinus.isHit(x, y)) {
                System.out.println("tz minus");
                currentRock.z -= 0.5f;
            }


            // rot
            if (editorRXPlus.isHit(x, y)) {
                System.out.println("rx plus");
                currentRock.rx += 3.0f;
            }

            if (editorRXMinus.isHit(x, y)) {
                System.out.println("rx minus");
                currentRock.rx -= 3.0f;
            }

            if (editorRYPlus.isHit(x, y)) {
                System.out.println("ry plus");
                currentRock.ry += 3.0f;
            }

            if (editorRYMinus.isHit(x, y)) {
                System.out.println("ry minus");
                currentRock.ry -= 3.0f;
            }

            if (editorRZPlus.isHit(x, y)) {
                System.out.println("rz plus");
                currentRock.rz += 3.0f;
            }

            if (editorRZMinus.isHit(x, y)) {
                System.out.println("rz minus");
                currentRock.rz -= 3.0f;
            }


            // scal
            if (editorSXPlus.isHit(x, y)) {
                System.out.println("sx plus");
                currentRock.sx += 0.25f;
            }

            if (editorSXMinus.isHit(x, y)) {
                System.out.println("sx minus");
                currentRock.sx -= 0.25f;
            }

            if (editorSYPlus.isHit(x, y)) {
                System.out.println("sy plus");
                currentRock.sy += 0.25f;
            }

            if (editorSYMinus.isHit(x, y)) {
                System.out.println("sy minus");
                currentRock.sy -= 0.25f;
            }

            if (editorSZPlus.isHit(x, y)) {
                System.out.println("sz plus");
                currentRock.sz += 0.25f;
            }

            if (editorSZMinus.isHit(x, y)) {
                System.out.println("sz minus");
                currentRock.sz -= 0.25f;
            }

            if (editorChangeType.isHit(x, y)) {
                System.out.println("Change type...");

                ++currentRock.rockId;

                if (currentRock.rockId > 8) {
                    currentRock.rockId = 0;
                }
            }
        }

        if (editorAddNew.isHit(x, y)) {
            System.out.println("Add new...");

            currentRock = new RockData();
            rocks.add(currentRock);
        }

        if (editorDump.isHit(x, y)) {
            System.out.println("Dump rocks...");

            for (RockData rd : rocks) {
                System.out.println("rocks.add( new RockData(" + rd.rockId + ", "
                        + rd.x + "f, " + rd.y + "f, " + rd.z + "f, " +
                        + rd.rx + "f, " + rd.ry + "f, " + rd.rz + "f, " +
                        + rd.sx + "f, " + rd.sy + "f, " + rd.sz + "f) );");
            }
        }
    }

    private void drawEdge() {
        edgeDrawer.begin();
        edgeDrawer.addLine(	-1f, 0f, 0f,
                             1f, 0f, 0f);

        edgeDrawer.addLine( 0f, -1f, 0f,
                            0f,  1f, 0f);

        setIdentityM(visuals.modelMatrix, 0);
        multiplyMM( visuals.mvpMatrix, 0,
                    visuals.viewProjectionMatrix, 0,
                    visuals.modelMatrix, 0);

        visuals.colorShader.useProgram();
        visuals.colorShader.setUniforms(visuals.mvpMatrix, visuals.blackColor);
        edgeDrawer.bindData(visuals.colorShader);
        edgeDrawer.draw();
    }

    private void drawMenuItemes() {

        if (_animatePlayButton) {
            menuAnimCurrentValue += menuAnimSpeed;
            float temp = play.pos.sy;
            play.pos.sy = menuAnimCurrentValue;
            play.draw();
            play.pos.sy = temp;

            if (menuAnimCurrentValue > menuAnimEndValue) {
                _animatePlayButton = false;
                _state = MenuState.HelmetSelect;
            }
        } else {
            play.draw();
        }

        if (_animateOptionsButton) {
            menuAnimCurrentValue += menuAnimSpeed;
            float temp = options.pos.sy;
            options.pos.sy = menuAnimCurrentValue;
            options.draw();
            options.pos.sy = temp;

            if (menuAnimCurrentValue > menuAnimEndValue) {
                _animateOptionsButton = false;
                _state = MenuState.Options;
            }
        } else {
            options.draw();
        }

        if (_animateAboutButton) {
            menuAnimCurrentValue += menuAnimSpeed;
            float temp = play.pos.sy;
            about.pos.sy = menuAnimCurrentValue;
            about.draw();
            about.pos.sy = temp;

            if (menuAnimCurrentValue > menuAnimEndValue) {
                _animateAboutButton = false;
                _state = MenuState.About;
            }
        } else {
            about.draw();
        }
    }

    private void drawOptions() {
        _fadeSub.draw();

        visuals.textureShader.useProgram();
        visuals.textureShader.setTexture(visuals.textureFonts);

        _textSubTitle.draw();
        _textBack.draw();
    }

    private void drawAbout() {
        _fadeSub.draw();

        visuals.textureShader.useProgram();
        visuals.textureShader.setTexture(visuals.textureFonts);

        _textSubTitle.draw();
        _textBack.draw();
    }

    private void drawHelmetSelect() {
        //visuals.setProjectionMatrix2D();
        //visuals.updateViewProjMatrix();

        //glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        //glEnable(GL_BLEND);
        //glDisable(GL_DEPTH_TEST);

        _fadeSub.draw();

        visuals.textureShader.useProgram();
        visuals.textureShader.setTexture(visuals.textureFonts);

        _textScore.draw();
        _textSubTitle.draw();
        _textContinue.draw();
        _textBack.draw();

        visuals.textureShader.setTexture(visuals.textureHelmets);
        _helmetInFocus.draw();

        if (_showPrevHelmetArrow) {
            _leftArrow.draw();
        }

        if (_showNextHelmetArrow) {
            _rightArrow.draw();
        }
    }

    @Override
	public void handleTouchPress(float normalizedX, float normalizedY) {

        switch(_state) {
            case MainMenu:
                Vector pos = Geometry.convertNormalized2DPointToNormalizedDevicePoint2D(normalizedX, normalizedY, visuals.invertedViewProjectionMatrix);

                if (editorEnabled) {
                    doEditorStuff(pos.x, pos.y);
                    return;
                }

                if (play.isHit(pos.x, pos.y)) {
                    System.out.println("play is hit...");
                    _fadeSub.init(  new MyColor(0f, 0f, 0f, 0.4f),
                                    new MyColor(0f, 0f, 0f, 0.4f),
                                    new Rectangle(0f, -0.35f, 0.85f, 1.1f));

                    initHelmets((int)Visuals.screenWidth, (int)Visuals.screenHeight);

                    _animatePlayButton = true;
                    menuAnimCurrentValue = play.pos.sy;
                    menuAnimEndValue = menuAnimCurrentValue + 0.1f;
                    return;
                }

                if (options.isHit(pos.x, pos.y)) {
                    System.out.println("options is hit...");
                    _animateOptionsButton = true;
                    menuAnimCurrentValue = options.pos.sy;
                    menuAnimEndValue = menuAnimCurrentValue + 0.1f;

                    _fadeSub.init(  new MyColor(0f, 0f, 0f, 0.4f),
                            new MyColor(0f, 0f, 0f, 0.4f),
                            new Rectangle(0f, -0.35f, 0.85f, 1.1f));

                    initOptions((int)Visuals.screenWidth, (int)Visuals.screenHeight);

                    return;
                }

                if (about.isHit(pos.x, pos.y)) {
                    System.out.println("about is hit...");
                    _animateAboutButton = true;
                    menuAnimCurrentValue = about.pos.sy;
                    menuAnimEndValue = menuAnimCurrentValue + 0.1f;

                    _fadeSub.init(  new MyColor(0f, 0f, 0f, 0.4f),
                            new MyColor(0f, 0f, 0f, 0.4f),
                            new Rectangle(0f, -0.35f, 0.85f, 1.1f));

                    initAbout((int)Visuals.screenWidth, (int)Visuals.screenHeight);

                    return;
                }
                break;

            case HelmetSelect:
                if (normalizedY > -0.2f && normalizedY < 0.2f) {
                    if (normalizedX < -0.6f) {
                        //System.out.println("Helmets go left...");
                        stepCurrentHelmet(-1);
                    } else if (normalizedX > 0.6f) {
                        //System.out.println("Helmets go right...");
                        stepCurrentHelmet(1);
                    }
                } else if (normalizedY < -0.75f) {
                    //System.out.println(ClassicSingleton.getInstance().helmetIndexToString(currentHelmetIndex));

                    if (normalizedX > 0.3f) {
                        nextSceneId = ScenesEnum.Shaft;
                    } else if (normalizedX < -0.3f) {
                        _state = MenuState.MainMenu;
                    } else {
                        nextSceneId = ScenesEnum.None;
                    }

                    if (nextSceneId != ScenesEnum.None) {
                        goNextScene = true;
                        ClassicSingleton.getInstance().selectedHelmetIndex = _currentHelmetIndex;
                        super.initFadeOut();
                    }
                }
                break;

            case Options:
                    if (normalizedY < -0.75f && normalizedX < -0.3f) {
                        _state = MenuState.MainMenu;
                    }
                break;

            case About:
                    if (normalizedY < -0.75f && normalizedX < -0.3f) {
                        _state = MenuState.MainMenu;
                    }
                break;
        }
	}

	@Override
	public void handleTouchDrag(float normalizedX, float normalizedY) {

	}

	@Override
	public void handleTouchRelease(float normalizedX, float normalizedY) {

	}
}
