import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * Shot class creates shot objects to show where shots have already been taken and whether they were hits or misses.
 * @author Hayner
 */
public class Shot extends JLabel
{
	private int xCord, yCord;
	private int width = 20;
	private int height = 20;
	
	private ImageIcon hitPic = new ImageIcon("hit.png");
	private ImageIcon missPic = new ImageIcon("miss.png");
	private ImageIcon image;
	
	/**
	 * shot is the constructor for the shot class
	 * @param xCord
	 * @param yCord
	 */
	public Shot(int xCord, int yCord){
        this.xCord = xCord/20*20 + 1;
        this.yCord = yCord/20*20 + 1;
        image = missPic;
        super.setIcon(missPic);
        super.setSize(width, height);
        super.setLocation(this.xCord, this.yCord);
    }
	
	/**
	 * hit method changes the image to represent that there was a hit in that location
	 */
	public void hit(){
		super.setIcon(hitPic);
	}
    
	/**
	 * checkBounds method checks to see if a shot has already been taken in that location
	 * @param x
	 * @param y
	 * @return true if shot was already taken there, false otherwise
	 */
    public boolean checkBounds(int x, int y){
    	boolean result = false;
    	
    	if(xCord <= x && x <= xCord + width && yCord <= y && y <= yCord + height){
    		result = true;
    	}
    	
    	return result;
    }
}
