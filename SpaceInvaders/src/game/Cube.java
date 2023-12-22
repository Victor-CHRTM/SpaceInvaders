package game;

import java.util.ArrayList;

import com.jogamp.opengl.GL2;



public class Cube extends GraphicalObject
{
	
	private ArrayList<Square> faces; 
	
	public Cube(float pX, float pY, float pZ,
			float angX, float angY, float angZ,
			float scale,
			float r, float g, float b)
	{
		super(pX, pY, pZ, angX, angY, angZ, scale, r, g, b);
		faces = new ArrayList<Square>();
		// Front face
		faces.add(new Square(0, 0, 1, 0, 0, 0, 1, r, g, b));
		// Back face
		faces.add(new Square(0, 0, -1, 0, 0, 0, 1, r, g, b));
		// Right face
		faces.add(new Square(1, 0, 0, 0, 90, 0, 1, r, g, b));
		// Left face
		faces.add(new Square(-1, 0, 0, 0, -90, 0, 1, r, g, b));
		// Top face
		faces.add(new Square(0, 1, 0, 90, 0, 0, 1, r, g, b));
		// Bottom face
		faces.add(new Square(0, -1, 0, 90, 0, 0, 1, r, g, b));
	}
	
	
	public void display_normalized(GL2 gl)
	{
		for (Square face: faces)
			face.display(gl);
	}

}
