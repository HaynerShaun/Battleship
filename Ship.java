import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * Ship class creates the ship objects used throughout gameplay
 *  * @author Hayner
 */
public class Ship extends JLabel
{
	private String name;
	private String ext = ".png";
	
	private char orientation;
	private char h = 'H';
	private char v = 'V';
	
	private int width, height;
	private int xCord, yCord;
	private int player;
	private int shipNumber;
	private int size;
	private int hitsTaken;

	private ImageIcon hit = new ImageIcon("hit.png");
	private ImageIcon ship;
	
	/**
	 * ship is the constructor for the ship class
	 * @param shipNumber
	 * @param name
	 * @param orientation
	 * @param player
	 * @param width
	 * @param height
	 * @param xCord
	 * @param yCord
	 * @param size
	 */
    public Ship(int shipNumber, String name, char orientation, int player, int width, int height, int xCord, int yCord, int size)
    {
        this.shipNumber = shipNumber;
        this.name = name;
        this.orientation = orientation;
        this.player = player;
        this.width = width;
        this.height = height;
        this.xCord = (xCord / 20) * 20 + 1;
        this.yCord = (yCord / 20) * 20 + 1;
        ship = new ImageIcon(name + orientation + ext);
        super.setIcon(ship);
        this.size = size;
        hitsTaken = 0;
        super.setLocation(this.xCord, this.yCord);
        super.setSize(this.width, this.height);
    }
    
    /**
     * the sunk class checks to the if the ship is sunk based on size and hits taken
     * @return true if ship is suck, false otherwise
     */
    public boolean sunk(){
    	boolean check = false;
    	
    	if(hitsTaken >= size)
    		check = true;
    	
    	return check;
    }
    
    /**
     * getName method returns the name of the ship
     * @return a string representing the name of the ship
     */
    public String getName(){
    	return name;
    }
    
    /**
     * getShipNumber returns the ship's index in the array
     * @return the index of the ship in the ships array
     */
    public int getShipNumber(){
    	return shipNumber;
    }
    
    /**
     * getPlayer returns which player the ship belongs to
     * @return an int representing the player number - 1 or 2
     */
    public int getPlayer(){
    	return player;
    }
    
    /**
     * hit increments the number of hits the ship has taken
     */
    public void hit(){
    	hitsTaken++;
    }
    
    /**
     * the checkBounds method checks to see if the x and y coordinate is within the ship
     * @param x
     * @param y
     * @return true if coordinate is within ship, false otherwise
     */
    public boolean checkBounds(int x, int y){
    	boolean result = false;
    	
    	if(xCord < x && x < xCord + width && yCord < y && y < yCord + height){
    		result = true;
    	}
    	
    	return result;
    }
    
    /**
     * changeOrientation switches the ships height and width to change from horizontal to vertical and vuce versa
     */
    public void changeOrientation(){
    	if(orientation == h){
    		orientation = v;
    	}else{
    		orientation = h;
    	}
    	ship = new ImageIcon(name + orientation + ext);
        super.setIcon(ship);
        int temp;
        temp = width;
        width = height;
        height = temp;

        super.setSize(width, height);
    }
}
