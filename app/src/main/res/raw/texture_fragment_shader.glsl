precision mediump float;

uniform sampler2D u_TextureUnit;
varying vec2 v_TextureCoordinates;
varying vec4 v_Color;

void main()
{
    vec4 color = texture2D(u_TextureUnit, v_TextureCoordinates) * v_Color;
    gl_FragColor = v_Color;
}