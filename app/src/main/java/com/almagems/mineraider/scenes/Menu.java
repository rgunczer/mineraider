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
import com.almagems.mineraider.util.Texture;
import com.almagems.mineraider.util.Vector;

import java.util.ArrayList;

public class Menu extends Scene {

    private Physics physics;

    MineCart mineCart;

    private ArrayList<RockData> rocks = new ArrayList<RockData>();

    private final EdgeDrawer edgeDrawer;

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

    private Fade fade;

    private RockData currentRock;


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

        physics = Physics.getInstance();

        // sin
        physics.addBoxStatic(0.0f, -19.7f, 0f, 70.0f, 0.5f);

        // minecart
        mineCart = new MineCart(-1f, -15.7f);
    }

    private VertexBuffer createVertexBuffer(Rectangle rc, Texture texture) {
        float width = Visuals.screenWidth;
        float scale = width / Visuals.referenceScreenWidth;

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

    @Override
	public void surfaceChanged(int width, int height) {
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












        rect = new Rectangle(0, 308+439, 1080, 439);
        title.init(visuals.textureMenuItems, whiteColor, rect, flipUTextureCoordinate);
        title.pos.trans(0f, aspect * 0.65f, 0f);
        title.pos.rot(0f, 0f, 0f);
        title.pos.scale(rect.w / Visuals.referenceScreenWidth, rect.h / Visuals.referenceScreenWidth, 1.0f);


        rect = new Rectangle(1155, 0+308, 407, 308);
        play.init(visuals.textureMenuItems, whiteColor, rect, flipUTextureCoordinate);
        play.pos.trans(0.0f, aspect * 0.3f, 0f);
        play.pos.rot(0f, 0f, 0f);
        play.pos.scale(rect.w / Visuals.referenceScreenWidth, rect.h / Visuals.referenceScreenWidth, 1.0f);

        rect = new Rectangle(525, 0+296, 630, 296);
        options.init(visuals.textureMenuItems, whiteColor, rect, flipUTextureCoordinate);
        options.pos.trans(0.0f, -aspect * 0.0f, 0f);
        options.pos.rot(0f, 0f, 0f);
        options.pos.scale(rect.w / Visuals.referenceScreenWidth, rect.h / Visuals.referenceScreenWidth, 1.0f);

        rect = new Rectangle(0, 0+288, 525, 288);
        about.init(visuals.textureMenuItems, whiteColor, rect, flipUTextureCoordinate);
        about.pos.trans(0.0f, -aspect * 0.3f, 0f);
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







        fade = new Fade();
        fade.init(new MyColor(0f, 0f, 0f, 1f), new MyColor(0f, 0f, 0f, 0f));




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
        play.draw();
        options.draw();
        about.draw();
        title.draw();


        //drawEditorButtons();


        //visuals.textureShader.setTexture(visuals.textureFonts);
        //text.draw();
       // credits.draw();

        visuals.bindNoTexture();

        //drawEdge();

        fade.update();
        fade.draw();

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

    void drawRocks() {
        RockData rd = new RockData();
/*
        // rock types

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
*/

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

    void drawFloor() {
        visuals.floor.bindData(visuals.dirLightShader);

        _pos.trans(0f, -20.5f, 0f);
        _pos.rot(0f, 0f, 0f);
        _pos.scale(1.0f, 1.0f, 1.0f);
        visuals.calcMatricesForObject(_pos);
        visuals.dirLightShader.setUniforms();
        visuals.floor.draw();

        _pos.trans(0f, -20.5f, 10f);
        _pos.rot(0f, 0f, 0f);
        _pos.scale(1.0f, 1.0f, 1.0f);
        visuals.calcMatricesForObject(_pos);
        visuals.dirLightShader.setUniforms();
        visuals.floor.draw();

        _pos.trans(0f, -20.5f, -10f);
        _pos.rot(0f, 0f, 0f);
        _pos.scale(1.0f, 1.0f, 1.0f);
        visuals.calcMatricesForObject(_pos);
        visuals.dirLightShader.setUniforms();
        visuals.floor.draw();
    }

    void drawRailRoad() {
        visuals.railroad.bindData(visuals.dirLightShader);

        float x = -10f;
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
        _pos.rot(-85f, 0f, 0f);
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
        visuals.textureShader.bindData(vbBg);

        ibBg.bind();
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_SHORT, 0);
        vbBg.unbind();
        ibBg.unbind();
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

    @Override
	public void handleTouchPress(float normalizedX, float normalizedY) {

        Vector pos = Geometry.convertNormalized2DPointToNormalizedDevicePoint2D(normalizedX, normalizedY, visuals.invertedViewProjectionMatrix);

        if (currentRock != null) {

            // trans
            if (editorTXPlus.isHit(pos.x, pos.y)) {
                System.out.println("tx plus");
                currentRock.x += 0.5f;
            }

            if (editorTXMinus.isHit(pos.x, pos.y)) {
                System.out.println("tx minus");
                currentRock.x -= 0.5f;
            }

            if (editorTYPlus.isHit(pos.x, pos.y)) {
                System.out.println("ty plus");
                currentRock.y += 0.5f;
            }

            if (editorTYMinus.isHit(pos.x, pos.y)) {
                System.out.println("ty minus");
                currentRock.y -= 0.5f;
            }

            if (editorTZPlus.isHit(pos.x, pos.y)) {
                System.out.println("tz plus");
                currentRock.z += 0.5f;
            }

            if (editorTZMinus.isHit(pos.x, pos.y)) {
                System.out.println("tz minus");
                currentRock.z -= 0.5f;
            }


            // rot
            if (editorRXPlus.isHit(pos.x, pos.y)) {
                System.out.println("rx plus");
                currentRock.rx += 3.0f;
            }

            if (editorRXMinus.isHit(pos.x, pos.y)) {
                System.out.println("rx minus");
                currentRock.rx -= 3.0f;
            }

            if (editorRYPlus.isHit(pos.x, pos.y)) {
                System.out.println("ry plus");
                currentRock.ry += 3.0f;
            }

            if (editorRYMinus.isHit(pos.x, pos.y)) {
                System.out.println("ry minus");
                currentRock.ry -= 3.0f;
            }

            if (editorRZPlus.isHit(pos.x, pos.y)) {
                System.out.println("rz plus");
                currentRock.rz += 3.0f;
            }

            if (editorRZMinus.isHit(pos.x, pos.y)) {
                System.out.println("rz minus");
                currentRock.rz -= 3.0f;
            }


            // scal
            if (editorSXPlus.isHit(pos.x, pos.y)) {
                System.out.println("sx plus");
                currentRock.sx += 0.25f;
            }

            if (editorSXMinus.isHit(pos.x, pos.y)) {
                System.out.println("sx minus");
                currentRock.sx -= 0.25f;
            }

            if (editorSYPlus.isHit(pos.x, pos.y)) {
                System.out.println("sy plus");
                currentRock.sy += 0.25f;
            }

            if (editorSYMinus.isHit(pos.x, pos.y)) {
                System.out.println("sy minus");
                currentRock.sy -= 0.25f;
            }

            if (editorSZPlus.isHit(pos.x, pos.y)) {
                System.out.println("sz plus");
                currentRock.sz += 0.25f;
            }

            if (editorSZMinus.isHit(pos.x, pos.y)) {
                System.out.println("sz minus");
                currentRock.sz -= 0.25f;
            }

            if (editorChangeType.isHit(pos.x, pos.y)) {
                System.out.println("Change type...");

                ++currentRock.rockId;

                if (currentRock.rockId > 8) {
                    currentRock.rockId = 0;
                }
            }
        }

        if (editorAddNew.isHit(pos.x, pos.y)) {
            System.out.println("Add new...");

            currentRock = new RockData();
            rocks.add(currentRock);
        }

        if (editorDump.isHit(pos.x, pos.y)) {
            System.out.println("Dump rocks...");

            for (RockData rd : rocks) {
                System.out.println("rocks.add( new RockData(" + rd.rockId + ", "
                                                              + rd.x + "f, " + rd.y + "f, " + rd.z + "f, " +
                                                              + rd.rx + "f, " + rd.ry + "f, " + rd.rz + "f, " +
                                                              + rd.sx + "f, " + rd.sy + "f, " + rd.sz + "f) );");
            }
        }

        if ( play.isHit(pos.x, pos.y) ) {
            System.out.println("play is hit...");
            //ClassicSingleton.getInstance().showScene(ScenesEnum.HelmetSelect);

            return;
        }

        if  (options.isHit(pos.x, pos.y) ) {
            System.out.println("options is hit...");

            return;
        }

        if ( about.isHit(pos.x, pos.y) ) {
            System.out.println("about is hit...");

        }

	}

	@Override
	public void handleTouchDrag(float normalizedX, float normalizedY) {

	}

	@Override
	public void handleTouchRelease(float normalizedX, float normalizedY) {

	}
}
