package com.almagems.mineraider.shaders;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniform3f;
import static android.opengl.GLES20.glUniformMatrix4fv;

import android.content.Context;
import com.almagems.mineraider.R;
import com.almagems.mineraider.Visuals;
import com.almagems.mineraider.util.MyColor;
import com.almagems.mineraider.util.Vector;


public class PointLightShader extends BaseShader {
	
	// uniform locations	
	private final int uModelViewLocation;	
	private final int uProjectionLocation;
	private final int uColorLocation;
				
	// attribute locations
	private final int aPositionLocation;
	private final int aNormalLocation;
	private final int aTextureCoordinatesLocation;		
	
	public PointLightShader(Context context) throws Exception {
		super(context, R.raw.point_light_vertex_shader, R.raw.point_light_fragment_shader);
		
		// retrieve uniform locations for the shader program
		uModelViewLocation = glGetUniformLocation(program, "modelView");
		uProjectionLocation = glGetUniformLocation(program, "projection");
		uColorLocation = glGetUniformLocation(program, "u_Color");
					
		uTextureUnitLocation = glGetUniformLocation(program, "u_TextureUnit");
						
		// retrieve attribute locations for the shader program
		aPositionLocation = glGetAttribLocation(program, "vertexPosition");
		aNormalLocation = glGetAttribLocation(program, "vertexNormal");
		aTextureCoordinatesLocation = glGetAttribLocation(program, "textureCoordinates");		
	}
	
	public void setUniforms(MyColor color, MyColor lightColor, Vector lightPos) {
		Visuals visuals = Visuals.getInstance();
				
		glUniformMatrix4fv(uModelViewLocation, 1, false, visuals.mvMatrix, 0);		
		glUniformMatrix4fv(uProjectionLocation, 1, false, visuals.projectionMatrix, 0);		
		
		glUniform4f(uColorLocation, color.r, color.g, color.b, color.a);
	}	
	
	public int getPositionAttributeLocation() {
		return aPositionLocation;
	}
	
	public int getNormalAttributeLocation() {
		return aNormalLocation;
	}
	
	public int getTextureAttributeLocation() {
		return aTextureCoordinatesLocation;
	}		
}
