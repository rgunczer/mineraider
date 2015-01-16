precision mediump float;

uniform sampler2D u_TextureUnit;

varying vec2 v_TextureCoordinates;
varying vec3 fragColour;


void main()
{

    vec4 colorFromTexture = texture2D(u_TextureUnit, v_TextureCoordinates);
	vec4 colorFromVertexShader = vec4(fragColour, colorFromTexture.a);
	gl_FragColor = colorFromTexture * colorFromVertexShader;

	//vec4 combined = colorFromTexture * colorFromVertexShader;
	//gl_FragColor = vec4(1.0, 0.0, 0.0, colorFromTexture.a);
	//gl_FragColor = combined; //vec4(colorFromVertexShader.r, colorFromVertexShader.g, colorFromVertexShader.b, colorFromTexture.a);
}