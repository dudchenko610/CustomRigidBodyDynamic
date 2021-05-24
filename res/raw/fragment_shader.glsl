precision mediump float;

uniform bool u_HasColor;
uniform float u_Alpha;
uniform sampler2D u_TextureUnit;

varying vec4 v_Color;
varying vec2 v_TextureCoordinates;

void main() {

	if (u_HasColor) {
		gl_FragColor = v_Color;
	} else {
		gl_FragColor =  vec4(vec3(texture2D(u_TextureUnit, v_TextureCoordinates)), u_Alpha);
	}

}
