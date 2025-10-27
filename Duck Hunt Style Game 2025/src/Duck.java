import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.net.URL;

// The Duck class represents a picture of a duck that can be drawn on the screen.
public class Duck {
    // Instance variables (data that belongs to each Duck object)
    private Image img;               // Stores the picture of the duck
    private AffineTransform tx;      // Used to move (translate) and resize (scale) the image
    private Image normal;
    private Image dead;
	private Tokage tokage = new Tokage("tokage.gif");
	int score = 0; 
	int pass = (int)((Math.random()*8)+1);
	private Background Scoretables = new Background("Scoretables.PNG",0.5,0.5,950,-400);
    // Variables to control the size (scale) of the duck image
    private double scaleX;           
    private double scaleY;           

    // Variables to control the location (x and y position) of the duck
    private double x;                
    private double y;        
    
    //variables for speed
    private int vx;
    private int vy;
    
    //debugging variable
    public boolean debugging = true;
	private Object fishimg;
	private Object image;

    // Constructor: runs when you make a new Duck object
    public Duck() {
        normal = getImage("/imgs/fish+background.gif");
    	dead = getImage("/imgs/caught_fish.png");
    	
    	img = getImage("/imgs/fish+background.gif"); // Load the image file
        
        tx = AffineTransform.getTranslateInstance(0, 0); // Start with image at (0,0)
        
        // Default values
        scaleX = 3.5;
        scaleY = 3.5;
        x = 800;
        y = 500;
        
        //init the vx and vy variables with non-zero integers
        vx=5;
        vy=5;
        
        init(x, y); // Set up the starting location and size
    }
    
    //2nd constructor to initialize location and scale!
    public Duck(int x, int y, int scaleX, int scaleY) {
    
    	
    	this();
    	this.x 		= x;
    	this.y 		= y;
    	this.scaleX = scaleX;
    	this.scaleY = scaleY;
    	init(x,y);
    }
    
    //2nd constructor to initialize location and scale!
    public Duck(int x, int y, int scaleX, int scaleY, int vx, int vy) {
    	this();
    	this.x 		= x;
    	this.y 		= y;
    	this.scaleX = scaleX;
    	this.scaleY = scaleY;
    	this.vx 	= vx; 
    	this.vy 	= vy;
    	init(x,y);
    }
    
    public void setVelocityVariables(int vx, int vy) {
    	this.vx = vx;
    	this.vy = vy;
    }
    
    
    
    // Changes the picture to a new image file
    public void changePicture(String imageFileName) {
        img = getImage("/imgs/"+imageFileName);
        init(x, y); // keep same location when changing image
    }
    
    //update any variables for the object such as x, y, vx, vy
    public void update() {
    	//x position updates based on vx
    	x += vx; 
    	y += vy;
    	if(x>=1750 || x<=0) {
    		vx *= -1; 
    	} 
    	
    //respawn our fish
    	if(vx==0&&vy>=10) {
    		// fish is dead - change to dead sprite
    		img = dead;
    		
    		}
    		if(y>=900) {
    			vy=-(int)(Math.random()*8+3);
    			vx=(int)(Math.random()*8+3);
    			//changing back to normal sprite
    			img = normal; // normal sprite after reset
    			//50% of the time vx is negative
    			if(Math.random()<0.5) {
    				vx *= -1; 
    			}
    		}
    	
    	
    	//regular behavior - regular bouncing from the bottom
    	if(y>=980 || y<=0) {
    		vy *= -1; 
    	}	
    }
     
    
    
    // Draws the duck on the screen
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;   // Graphics2D lets us draw images
        g2.drawImage(img, tx, null);      // Actually draw the duck image
        update();
        init(x,y);
		Scoretables.paint(g);

		tokage.paint(g);
        
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Courier",Font.BOLD,45));
		g.drawString("0"+score, 1720, 100);
		g.setFont(new Font("Courier",Font.BOLD,32));
		
		if(pass>0) {
			g.drawString("0"+pass, 1300, 101);
		} else {
			g.drawString("Complete!", 1300, 101);
		}
        //create a green hitbox
       // g.setColor(Color.GREEN);
       // g.drawRect((int)x+50, (int)y, 150,100);
     
    }
    
    // Setup method: places the duck at (a, b) and scales it
    private void init(double a, double b) {
        tx.setToTranslation(a, b);        // Move the image to position (a, b)
        tx.scale(scaleX, scaleY);         // Resize the image using the scale variables
    }

    // Loads an image from the given file path
    private Image getImage(String path) {
        Image tempImage = null;
        try {
            URL imageURL = Duck.class.getResource(path);
            tempImage = Toolkit.getDefaultToolkit().getImage(imageURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tempImage;
    }

    // NEW: Method to set scale
    public void setScale(double sx, double sy) {
        scaleX = sx;
        scaleY = sy;
        init(x, y);  // Keep current location
    }

    // NEW: Method to set location
    public void setLocation(double newX, double newY) {
        x = newX;
        y = newY;
        init(x, y);  // Keep current scale
    }
    
    
    //Collision and collision logic
    public boolean checkCollision(int mX, int mY) {
    	
    	//represent the mouse as a rectangle
    	Rectangle mouse = new Rectangle(mX,mY,50,50);
    	
    	//represent this object as a Rectangle
    	
    	Rectangle thisObject = new Rectangle((int)x+50,(int) y,150,100);
    	
    	//use built-in method for rectangle to check if they intersect/collide
    	if(mouse.intersects(thisObject)) {
        	Music catch1 = new Music("caught.wav",false);    	
    		//logic if colliding
    		vx=0; //turn off vx to fall from the sky
    		vy=10; //fall y - gravity
    		
    		this.tokage.x = (int)x;
    		this.tokage.y = 800;
    		this.tokage.vy = -3;
    		catch1.play();
    		score+=1;
    		pass-=1;
    		
    		return true;
    	} else {
    		score-=1;
    		pass+=1;
    		return false;
    	}
    }
}
