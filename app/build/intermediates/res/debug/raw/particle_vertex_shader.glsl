uniform mat4 u_MvpMatrix;
uniform float u_Time;

attribute vec3 a_Position;
attribute vec3 a_Color;
attribute vec3 a_DirectionVector;
attribute float a_ParticleStartTime;

varying vec3 v_Color;
varying float v_ElapsedTime;

void main()
{
	v_Color = a_Color;
	v_ElapsedTime = u_Time - a_ParticleStartTime;
	float gravityFactor = v_ElapsedTime * v_ElapsedTime;  // 2.0;
	vec3 currentPosition = a_Position + (a_DirectionVector * v_ElapsedTime);
	currentPosition.y += gravityFactor;
	gl_Position = u_MvpMatrix * vec4(currentPosition, 1.0);
	gl_PointSize = 90.0; //30.0 + 90 * (1.0 - v_ElapsedTime * 0.5); 
}