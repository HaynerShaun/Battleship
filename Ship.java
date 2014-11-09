import javax.swing.ImageIcon;
import javax.swing.JLabel;

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
    }
    
    public boolean sunk(){
    	boolean check = false;
    	
    	if(hitsTaken >= size)
    		check = true;
    	
    	return check;
    }
    
    public String getName(){
    	return name;
    }
    
    public int getShipNumber(){
    	return shipNumber;
    }
    
    public int getPlayer(){
    	return player;
    }
    
    public void hit(){
    	hitsTaken++;
    }
    
    private ImageIcon getImage(){
    	return ship;
    }
    
    private void setImage(ImageIcon ship){
        this.ship = ship;
        super.setIcon(this.ship);
    }

    private void setWidth(int width){
    	this.width = width;
    }

    private void setHeight(int height){
    	this.height = height;
    }

    public void setXCord(int xCord){
    	this.xCord = (xCord / 20) * 20;
    }

    public void setYCord(int yCord){
    	this.yCord = (yCord / 20) * 20;;
    }
    
    public int returnWidth(){
    	return width;
    }
    
    public int returnHeight(){
    	return height;
    }
    
    public int returnXCord(){
    	return xCord;
    }
    
    public int returnYCord(){
    	return yCord;
    }
    
    public boolean checkBounds(int x, int y){
    	boolean result = false;
    	//System.out.print("xCord: " + xCord + " - x: " +  x + " - xCord + width: ");
    	//System.out.println(xCord + width);
    	//System.out.print("yCord: " + yCord + " - y: " +  y + " - yCord + height: ");
    	//System.out.println(yCord + height);
    	if(xCord < x && x < xCord + width && yCord < y && y < yCord + height){
    		result = true;
    	}
    	//System.out.println("result: " + result);
    	
    	return result;
    }
    
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
    }
}
