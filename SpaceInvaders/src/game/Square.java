package game;

import com.jogamp.opengl.GL2;

public class Square extends GraphicalObject
{
	
	public Square(float pX, float pY, float pZ,
			float angX, float angY, float angZ,
			float scale,
			float r, float g, float b)
	{
		super(pX, pY, pZ, angX, angY, angZ, scale, r, g, b);
	}
	
	public void display_normalized(GL2 gl)
	{
		gl.glBegin(GL2.GL_QUADS);
			gl.glVertex3f(-1f, 1f, 0f);
			gl.glVertex3f(1f, 1f, 0f);
			gl.glVertex3f(1f, -1f, 0f);
			gl.glVertex3f(-1f, -1f, 0f);
		gl.glEnd();
	}
}