precision mediump float;

uniform vec3 u_LightPos;
uniform sampler2D u_TextureUnit;

varying vec4 v_Color;
varying vec2 v_TextureCoordinates;
varying vec3 v_Position;
varying vec3 v_Normal;

void main()
{
	//gl_FragColor = texture2D(u_TextureUnit, v_TextureCoordinates) *  v_Color;
	//gl_FragColor = v_Color;
	//gl_FragColor = texture2D(u_TextureUnit, v_TextureCoordinates);
	//gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0);
	
	vec3 lightVector = normalize(u_LightPos - v_Position);
	
	float diffuse = max(dot(v_Normal, lightVector), 0.5);
	
	//gl_FragColor = v_Color * diffuse;
	gl_FragColor = texture2D(u_TextureUnit, v_TextureCoordinates) * v_Color* diffuse;
	
}