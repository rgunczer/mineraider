attribute vec4 a_Position;
attribute vec3 a_Normal;
attribute vec2 a_TextureCoordinates;

uniform mat4 u_MVPMatrix;
uniform mat4 u_MVMatrix;
//uniform vec3 u_LightPos; // position of the light in eye space
uniform vec4 u_Color;

varying vec3 v_Position;
varying vec4 v_Color;
varying vec3 v_Normal;
varying vec2 v_TextureCoordinates;


void main()
{
	vec4 color = u_Color; //vec4(0.0, 0.0, 0.0, 1.0); 

	// per fragment lighting
	v_Position = vec3(u_MVMatrix * a_Position);
	v_Color = color;
	v_Normal = vec3(u_MVMatrix * vec4(a_Normal, 0.0));
	

//	vec3 modelViewVertex = vec3(u_MVMatrix * a_Position);
//	vec3 modelViewNormal = vec3(u_MVMatrix * vec4(a_Normal, 0.0));	
//	float distance = length(u_LightPos - modelViewVertex);
//	vec3 lightVector = normalize(u_LightPos - modelViewVertex);
//	float diffuse = max(dot(modelViewNormal, lightVector), 0.1);
	//diffuse = diffuse * (1.0 / (1.0 + (0.25 * distance * distance)));
//	v_Color = color * diffuse;
	
	v_TextureCoordinates = a_TextureCoordinates;
	gl_Position = u_MVPMatrix * a_Position;	
}