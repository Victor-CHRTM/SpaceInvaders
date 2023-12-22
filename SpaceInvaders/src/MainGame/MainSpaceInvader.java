package MainGame;

import game.Cube;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
	
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.Animator;
	
public class MainSpaceInvader extends GLCanvas implements GLEventListener {
	
//////////////////////////////////////////////////////////////////////////////
	private Cube starship; // Our starship
	private ArrayList<Cube> enemies; // List of enemies to destroy
	
	private ArrayList<Cube> bullets; // List of bullets fired by the starship
	
	private float starshipX;
	
	private float minX;
	private float maxX;
	private float maxZ;
	
	private float gameSpeed;
	
	private boolean EnemyGoesRight;
	private boolean EnemyGoesLeft;
//////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args) {
		GLCanvas canvas = new MainSpaceInvader();
		canvas.setPreferredSize(new Dimension(1500, 800));
		
		final JFrame frame = new JFrame();
		frame.getContentPane().add(canvas);
		frame.setTitle("Space Invader");
		
		frame.pack();
		frame.setVisible(true);
		
		canvas.requestFocus();
		
		Animator animator = new Animator(canvas);
		animator.start();
	}
//////////////////////////////////////////////////////////////////////////////
	public MainSpaceInvader() { 
		this.addGLEventListener(this);
		
		this.starshipX = 0.0f;
		this.minX = -45.0f;
		this.maxX = 45.0f;
		this.maxZ = -70.0f;
		this.gameSpeed = 5.0f;
		
		this.EnemyGoesRight = true;
		this.EnemyGoesLeft = false;
		
		this.enemies = new ArrayList<Cube>();
		this.bullets = new ArrayList<Cube>();
	}
//////////////////////////////////////////////////////////////////////////////
	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT );
		
		gl.glLoadIdentity();
		
			// Draw objects 
			gl.glPushMatrix();
				
				this.starship.display(gl);
				
			gl.glPopMatrix();
		
		for (int i = 0; i < bullets.size(); i++) {
	        Cube bullet = bullets.get(i);
	        bullet.display(gl);
	        bullet.translate(0, 0.1f, 0);  // Bullet going to the top

	        // Check collision with each enemy
	        for (int j = 0; j < enemies.size(); j++) {
	            Cube enemy = enemies.get(j);

	            // Check if the bullet hits the enemy
	            if (checkCollision(bullet, enemy)) {
	                // Collision detected, blow up the enemy
	                enemies.remove(j);
	                bullets.remove(i);
	            }
	        }
	    }
		
		//Enemies can move
		moveEnemies();
		
		// Display enemies
		for (Cube obj: enemies) {
			obj.display(gl);
		}
		
		// Check if the player has lost
	    checkPlayerLost();

	    // Check if the player has won
	    checkPlayerWon();
	}
//////////////////////////////////////////////////////////////////////////////
	@Override
	public void dispose(GLAutoDrawable arg0) { }
//////////////////////////////////////////////////////////////////////////////
	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		// Background
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Color clear
		gl.glClearDepth(1.0f);
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glDepthFunc(GL2.GL_LEQUAL);
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
		
		// Initialize all graphical objects
		// 1 starship
		this.starship = new Cube(starshipX, -25.0f, this.maxZ, 0, 0, 0, 1f, 0.8f, 0.8f, 0);// The Starship
		
		// 10 enemies
		this.enemies.add(new Cube(-20.0f, 25.0f, this.maxZ, 0, 0, 0, 1.5f, 0.7f, 0.2f, 0)); // Enemy 1
		this.enemies.add(new Cube(-10.0f, 25.0f, this.maxZ, 0, 0, 0, 1.5f, 0.3f, 0.2f, 0)); // Enemy 2
		this.enemies.add(new Cube(0.0f, 25.0f, this.maxZ, 0, 0, 0, 1.5f, 0.4f, 0.1f, 0)); // Enemy 3
		this.enemies.add(new Cube(10.0f, 25.0f, this.maxZ, 0, 0, 0, 1.5f, 0.9f, 0.7f, 0)); // Enemy 4
		this.enemies.add(new Cube(20.0f, 25.0f, this.maxZ, 0, 0, 0, 1.5f, 0.7f, 0.7f, 0)); // Enemy 5
		
		this.enemies.add(new Cube(-20.0f, 20.0f, this.maxZ, 0, 0, 0, 1.5f, 0.6f, 0.9f, 0)); // Enemy 6
		this.enemies.add(new Cube(-10.0f, 20.0f, this.maxZ, 0, 0, 0, 1.5f, 0.1f, 0.5f, 0)); // Enemy 7
		this.enemies.add(new Cube(0.0f, 20.0f, this.maxZ, 0, 0, 0, 1.5f, 0.9f, 0.6f, 0)); // Enemy 8
		this.enemies.add(new Cube(10.0f, 20.0f, this.maxZ, 0, 0, 0, 1.5f, 0.4f, 0.4f, 0)); // Enemy 9
		this.enemies.add(new Cube(20.0f, 20.0f, this.maxZ, 0, 0, 0, 1.5f, 0.1f, 0.5f, 0)); // Enemy 10
		
		MyKeyListener keyListener = new MyKeyListener();
		this.addKeyListener(keyListener);
	}
//////////////////////////////////////////////////////////////////////////////
	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL2 gl = drawable.getGL().getGL2();
		
		// Set the view area
		gl.glViewport(0, 0, width, height);
		
		// Setup perspective projection
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU glu = new GLU();
		glu.gluPerspective(50.0, (float)width/height, 0.5, 100.0);
		
		// Enable the model view
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
//////////////////////////////////////////////////////////////////////////////
	public void moveEnemies() {
        for (Cube enemy : enemies) {
        	if (enemy.getPosX() >= maxX) { // if the enemy reach the right limit -> move this enemy to the bottom
        		EnemyGoesRight = false;
        		EnemyGoesLeft = true;
        		for (Cube enemy2 : enemies) {
        			enemy2.translate(-gameSpeed/500,-5f, 0);
        		}
        	}
        	
        	else if (EnemyGoesRight == true) { // if EnemyGoesRight is true -> move the enemy to the right
        		enemy.translate(gameSpeed/500, 0, 0);
        	}
        	
        	else if (enemy.getPosX() <= minX) { // if the enemy reach the left limit -> move this enemy to the bottom
        		EnemyGoesLeft = false;
        		EnemyGoesRight = true;
        		for (Cube enemy2 : enemies) {
        			enemy2.translate(gameSpeed/500,-5f, 0);
        		}
        	}
        	
        	else if (EnemyGoesLeft == true) { // if EnemyGoesLeft is true -> move the enemy to the left
        		enemy.translate(-gameSpeed/500, 0, 0);
        	}
        }
    }
//////////////////////////////////////////////////////////////////////////////
	public void checkPlayerLost() {
        float lowestEnemyY = Float.MAX_VALUE;

        for (Cube enemy : enemies) {
            float enemyY = enemy.getPosY();
            if (enemyY < lowestEnemyY) {
                lowestEnemyY = enemyY;
            }
        }

        if (lowestEnemyY <= starship.getPosY()) {
            System.out.println("Game Over");
            endGame();
        }
    }
//////////////////////////////////////////////////////////////////////////////
	public void checkPlayerWon() {
        if (enemies.isEmpty()) {
            System.out.println("You won"); 
            endGame();
        }
    }
//////////////////////////////////////////////////////////////////////////////
	public void endGame() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        System.exit(0);
    }
//////////////////////////////////////////////////////////////////////////////
	private boolean checkCollision(Cube obj1, Cube obj2) {
		// Get object 1 - X,Y,Z and scale
	    float obj1X = obj1.getPosX();
	    float obj1Y = obj1.getPosY();
	    float obj1Z = obj1.getPosZ();
	    float obj1Size = obj1.getScale();
	    
	    // Get object 2 - X,Y,Z and scale
	    float obj2X = obj2.getPosX();
	    float obj2Y = obj2.getPosY();
	    float obj2Z = obj2.getPosZ();
	    float obj2Size = obj2.getScale();

	    // Check collision in 3D
	    return obj1X < obj2X + obj2Size && obj1X + obj1Size > obj2X && // X
	            obj1Y < obj2Y + obj2Size && obj1Y + obj1Size > obj2Y && // Y
	            obj1Z < obj2Z + obj2Size && obj1Z + obj1Size > obj2Z; // Z
	}
//////////////////////////////////////////////////////////////////////////////
	class MyKeyListener extends java.awt.event.KeyAdapter {
	    public void keyPressed(java.awt.event.KeyEvent e) {
	        handleKeyPress(e.getKeyCode());
	    }

	    public void keyReleased(java.awt.event.KeyEvent e) {
	        handleKeyRelease(e.getKeyCode());
	    }

	    private void handleKeyPress(int keyCode) {
	        if (keyCode == java.awt.event.KeyEvent.VK_LEFT) {
	            if (starshipX > minX) {
	            	starship.translate(-gameSpeed, 0, 0);
	                starshipX -= gameSpeed;
	                // System.out.println("Moving left: " + starshipX);
	            } else {
	                System.out.println("STOPPP - You reached the left limit");
	            }
	        } else if (keyCode == java.awt.event.KeyEvent.VK_RIGHT) {
	            if (starshipX < maxX) {
	            	starship.translate(gameSpeed, 0, 0);
	                starshipX += gameSpeed;
	                // System.out.println("Moving right: " + starshipX);
	            } else {
	                System.out.println("STOPPP - You reached the right limit");
	            }
	        }
	        
	        
	        
	        if (keyCode == java.awt.event.KeyEvent.VK_SPACE) {
	            // Create a cube to represent a bullet
	            Cube bullet = new Cube(starship.getPosX(), starship.getPosY() + 1.0f, starship.getPosZ(), 0, 0, 0, 0.2f, 0.7f, 0.5f, 0);
	            bullets.add(bullet);  // Add the bullet to the List of bullets
	        }
	    }

	    private void handleKeyRelease(int keyCode) {
	        
	    }
	}
}
