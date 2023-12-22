package game;

import com.jogamp.opengl.GL2;

public abstract class GraphicalObject 
{
	private float posX, posY, posZ;
	private float angX, angY, angZ;
	private float r, g, b;
	private float scale;
	
	public GraphicalObject(float pX, float pY, float pZ,
			float angX, float angY, float angZ,
			float scale,
			float r, float g, float b)
	{
		this.posX = pX;
		this.posY = pY;
		this.posZ = pZ;
		this.angX = angX;
		this.angY = angY;
		this.angZ = angZ;
		this.scale = scale;
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public abstract void display_normalized(GL2 gl);
	
	public void display(GL2 gl)
	{
		gl.glPushMatrix();
			gl.glTranslatef(this.posX, this.posY, this.posZ);
			gl.glRotatef(this.angX, 1.0f, 0.0f, 0.0f);
			gl.glRotatef(this.angY, 0.0f, 1.0f, 0.0f);
			gl.glRotatef(this.angZ, 0.0f, 0.0f, 1.0f);
			gl.glScalef(this.scale, this.scale, this.scale);
			gl.glColor3f(this.r, this.g, this.g);
			this.display_normalized(gl);
		gl.glPopMatrix();
	}
	
	public void rotate(float aX,float aY,float aZ)
	{
		this.angX += aX;
		this.angY += aY;
		this.angZ += aZ;
	}
	
	public void translate(float pX, float pY, float pZ)
	{
	    this.posX += pX;
	    this.posY += pY;
	    this.posZ += pZ;
	}
	
	
	public float getPosX() {
        return this.posX;
    }

    public float getPosY() {
        return this.posY;
    }

    public float getPosZ() {
        return this.posZ;
    }
    
    public float getScale() {
        return scale;
    }
	
}
