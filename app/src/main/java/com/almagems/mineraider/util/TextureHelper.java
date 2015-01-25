package com.almagems.mineraider.util;

import static android.opengl.GLES20.*;
import static android.opengl.GLUtils.texImage2D;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

public class TextureHelper {

	private static final String TAG = "TextureHelper";
	
	public static Texture loadTexture(Context context, int resourceId) {
		
		final int[] textureObjectIds = new int[1];
		glGenTextures(1, textureObjectIds, 0);
		
		if (textureObjectIds[0] == 0) {
			if (LoggerConfig.ON) {
				Log.w(TAG, "Could not generate new OpenGL texture object.");
			}
			return null;
		}
	
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inScaled = false;

        Texture texture = new Texture();

		final Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
        final int w = bmp.getWidth();
        final int h = bmp.getHeight();
        final String resourceName = context.getResources().getResourceEntryName(resourceId);
        System.out.println("Texture info[" + resourceName + "], w:" + w + " h:" + h);

        texture.name = resourceName;
        texture.width = w;
        texture.height = h;

		Matrix flip = new Matrix();
		flip.postScale(1f, -1f);
		
		Bitmap bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), flip, true);
		
		if (bitmap == null) {
			if (LoggerConfig.ON) {
				Log.w(TAG, "Resource ID " + resourceId + " could not be decoded.");
			}
			glDeleteTextures(1, textureObjectIds, 0);
			return null;
		}
		
		glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		
		texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
		
		bitmap.recycle();
		
		glGenerateMipmap(GL_TEXTURE_2D);
		
		glBindTexture(GL_TEXTURE_2D, 0);

        texture.id = textureObjectIds[0];
		
		//return textureObjectIds[0];
        return texture;
	}
	
	
}
