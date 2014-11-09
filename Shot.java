import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Shot extends JLabel
{
	private int xCord, yCord;
	private int width = 20;
	private int height = 20;
	
	private ImageIcon hitPic = new ImageIcon("hit.png");
	private ImageIcon missPic = new ImageIcon("miss.png");
	private ImageIcon image;
	
	public Shot(int xCord, int yCord){
        this.xCord = xCord/20*20 + 1;
        this.yCord = yCord/20*20 + 1;
        image = missPic;
    }
	
	public ImageIcon getPic(){
		return image;
	}
	
	public void hit(){
		image = hitPic;
	}
	
	public int returnXCord(){
		return xCord;
	}
	
	public int returnYCord(){
		return yCord;
	}
	
	public int returnWidth(){
		return width;
	}
	
	public int returnHeight(){
		return height;
	}
    
    public boolean checkBounds(int x, int y){
    	boolean result = false;
    	
    	if(xCord <= x && x <= xCord + width && yCord <= y && y <= yCord + height){
    		result = true;
    	}
    	
    	return result;
    }
}
