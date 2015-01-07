attribute vec4 a_Position;
attribute vec4 a_Normal;

uniform mat4 u_MvpMatrix;
uniform mat4 u_NormalMatrix;
uniform vec4 u_Color;
uniform vec3 u_LightColor;
uniform vec3 u_LightDirection;

varying vec4 v_Color;

void main()
{
	vec3 normal = normalize(vec3(u_NormalMatrix * a_Normal));
		
	float nDotL = max( dot(u_LightDirection, normal), 0.0);
	vec3 diffuse = u_LightColor * vec3(u_Color) * nDotL;

	v_Color = vec4(diffuse, u_Color.a);	
	gl_Position = u_MvpMatrix * a_Position;	
}