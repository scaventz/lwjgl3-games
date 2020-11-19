#version 330 core
out vec4 FragColor;

in vec2 TexCoords;
uniform sampler2D playerTexture;

void main()
{
    FragColor = texture(playerTexture, TexCoords);
}