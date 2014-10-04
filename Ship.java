public class Ship
{
    private String shipColor;
    private int shipLength, vStart = 0, hStart = 0;
    private int hitCount = 0;
    private String direction = "";
    private String shipName;
    private int[][] shipCords;
    private boolean sunk = false;
    private int[][] hitLocations;
    
    public Ship(String shipColor, String shipName, int shipLength)
    {
        this.shipColor = shipColor;
        this.shipName = shipName;
        this.shipLength = shipLength;
        shipCords = new int[2][shipLength];
        hitLocations = new int[2][shipLength];
        for (int x = 0; x < shipLength; x++)
        {
            hitLocations[0][x] = -1;
            hitLocations[1][x] = -1;
        }
    }
    
    public String getShipColor()
    {
        return shipColor;
    }
    public String getShipName()
    {
        return shipName;
    }
    public int getShipLength()
    {
        return shipLength;
    }
    public int getHitCount()
    {
        return hitCount;
    }
    public String getDirection()
    {
        return direction;
    }
    public int getVStart()
    {
        return vStart;
    }
    public int getHStart()
    {
        return hStart;
    }
    public int[][] getShipCords()
    {
        return shipCords;
    }
    public int[][] getHitLocations()
    {
        return hitLocations;
    }
    public boolean getSunk()
    {
        return sunk;
    }
    
    public void setShipColor(String shipColor)
    {
        this.shipColor = shipColor;
    }
    public void setShipName(String shipName)
    {
        this.shipName = shipName;
    }
    public void setShipLength(int shipLength)
    {
        this.shipLength = shipLength;
    }
    public void setDirection(char direction)
    {
        this.direction = (direction == 'H' ? "Horrizontal" : "Vertical");
    }
    public void setVStart(int vStart)
    {
        this.vStart = vStart;
    }
    public void setHStart(int hStart)
    {
        this.hStart = hStart;
    }
    public void setShipCoords()
    {
        for (int x = 0; x < getShipLength(); x++)
        {
            if(getDirection().equals("Horrizontal"))
            {
                shipCords[0][x] = getVStart();
                shipCords[1][x] = getHStart() + x;
            }
            else
            {
                shipCords[0][x] = getVStart() + x;
                shipCords[1][x] = getHStart();
            }
        }
    }
    public void setHitLocations(int vLocation, int hLocation)
    {
        for (int x = 0; x < getShipLength(); x++)
        {
            if(hitLocations[0][x] == -1)
            {
                hitLocations[0][x] = vLocation;
                hitLocations[1][x] = hLocation;
                break;
            }
        }
    }
    public void setSunk(boolean sunk)
    {
        this.sunk = sunk;
    }
    
    public void hitIncrement()
    {
        hitCount++;
    }
    
    public String toString()
    {
        String description = ("Ship color: " + getShipColor() + 
                              "\nShip Length: " + getShipLength() +
                              "\nShip Direction: " + getDirection() +
                              "\nStarting Coordinates: (" + getVStart() + "," + getHStart() + ")" +
                              "\nHit Count: " + getHitCount() + "\nShip cords: ");
        for (int x = 0; x < getShipLength(); x++)
        {
            description += "(" + shipCords[0][x] + "," + shipCords[1][x] + ") ";
        }
        description += "\n" + "Hit Location: ";
        for (int x = 0; x < getShipLength(); x++)
        {
            description += "(" + hitLocations[0][x] + "," + hitLocations[1][x] + ") ";
        }
        description += "\n" + (getSunk() ? "This ship has been sunk" : "This ship hasn't been sunk") + "\n";
        return description;
    }
}
