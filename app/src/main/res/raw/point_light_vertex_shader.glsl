attribute vec4 vertexPosition;
attribute vec3 vertexNormal;
attribute vec2 textureCoordinates;
      
uniform mat4 projection;
uniform mat4 modelView;
uniform vec4 u_Color;

varying vec2 v_TextureCoordinates;
varying vec3 fragColour;        
      

void main()
{
	v_TextureCoordinates = textureCoordinates;

	vec3 vertexColour = u_Color.rgb;
 
 	if (vertexColour.r < 0.5)
 	{
 		fragColour = vertexColour;
 	}
 	else
 	{	 
	    vec3 transformedVertexNormal = normalize((modelView * vec4(vertexNormal, 0.0)).xyz);
	    vec3 inverseLightDirection = normalize(vec3(-1.0, -1.0, 1.0));
	 	fragColour = vec3(0.0);
	 
	 
	 
	 	vec3 diffuseLightIntensity = vec3(1.0, 1.0, 1.0);
	    vec3 vertexDiffuseReflectionConstant = vertexColour;
	    float normalDotLight = max(0.0, dot(transformedVertexNormal, inverseLightDirection));
	    fragColour += normalDotLight * vertexDiffuseReflectionConstant * diffuseLightIntensity;
	 
	 
	 
	    vec3 ambientLightIntensity = vec3(0.2, 0.2, 0.2);
	    vec3 vertexAmbientReflectionConstant = vertexColour;
	    fragColour += vertexAmbientReflectionConstant * ambientLightIntensity;
	 
	 
	 
	    vec3 inverseEyeDirection = normalize(vec3(0.0, 11.0, 49.0));
	    //vec3 inverseEyeDirection = normalize(vec3(0.0, 1.0, 5.0));
	    vec3 specularLightIntensity = vec3(1.0, 1.0, 1.0);
	    vec3 vertexSpecularReflectionConstant = vec3(1.0, 1.0, 1.0);
	    float shininess = 100.0;
	    vec3 lightReflectionDirection = reflect(vec3(0) - inverseLightDirection, transformedVertexNormal);
	    float normalDotReflection = max(0.0, dot(inverseEyeDirection, lightReflectionDirection));
	    fragColour += pow(normalDotReflection, shininess) * vertexSpecularReflectionConstant * specularLightIntensity;
	 
		clamp(fragColour, 0.0, 1.0);
 	}
 	
	gl_Position = projection * modelView * vertexPosition;
 }

