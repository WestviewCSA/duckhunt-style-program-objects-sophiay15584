import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.net.URL;

class Tokage{
	private Image img; 	
	private AffineTransform tx;
	int width, height;
	int x, y;						//position of the object
	int vx, vy;						//movement variables
	double scaleWidth = 0.5;		 //change to scale image
	double scaleHeight = 0.5; //change to scale image

	public Tokage(String filename) {
		img = getImage("/imgs/"+filename); //load the image for Tree

		//alter these
		width = 0;
		height = 0;
		x = 1500;
		y = 700;
		vx = 0;
		vy = 0;
		
		
		
		tx = AffineTransform.getTranslateInstance(0, 0);
		init(x, y); 				//initialize the location of the image
									//use your variables
	}
	
	public void changePicture(String newFileName) {
		img = getImage(newFileName);
		init(0, 0);
	}
	
	
	public void paint(Graphics g) {
		//these are the 2 lines of code needed draw an image on the screen
		Graphics2D g2 = (Graphics2D) g;
		
		
		x+=vx;
		y+=vy;
		
		
		if(y<=700) {
			vy=5;
		}
		
		
		
		init(x,y);
		g2.drawImage(img, tx, null);

	}
	
	private void init(double a, double b) {
		tx.setToTranslation(a, b);
		tx.scale(scaleWidth, scaleHeight);
	}

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

}
