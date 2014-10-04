import java.util.Scanner;
import java.util.Random;
public class Battleship
{
    private static Scanner scan = new Scanner(System.in);
    private static Random randomNum = new Random();
    private static int dimensions, attackCount = 0;
    
    private static startGame()
    {
        String colorSelection = setPlayerColor();
        dimensions = 10;
        
        System.out.println("Player is " + colorSelection);
        System.out.println("Computer is " + (colorSelection.equals("Red") ? "Blue" : "Red"));
        System.out.println();
        singleMode(colorSelection);
    }
    
    private static String setPlayerColor()
    {
        System.out.print("Player Color Selection (1 for Red, 2 for Blue): ");
        String input = scan.next();
        while(!input.equals("1") && !input.equals("2"))
        {
            System.out.print("Invalid color selection. Enter 1 for Red, 2 for Blue: ");
            input = scan.next();
        }
        String colorSelection = (input.equals("1") ? "Red" : "Blue");
        System.out.println();
        
        return colorSelection;
    }
    
    private static void singleMode(String color)
    {
        char[][] playerBoard = new char[dimensions][dimensions];
        char[][] computerBoard = new char[dimensions][dimensions];
        String playerColor = color;
        String computerColor = color.equals("Red") ? "Blue" : "Red";
        Ship[] ships = new Ship[10];
        int count = 0, shotCount = 0, sinkCount;
        int playerSinkCount = 0, compShotCount = 0;
        int[][] computerHits = new int[2][100];
        boolean[] computerTurns = new boolean[100];
        int[][] directionCheck = {{2,2,2,2},{2,2,2,2}};
        boolean attack = false;
        
        //Starts initial game automatically
        create(playerBoard, computerBoard, color, ships);
        
        for (int x = 0; x < computerHits[0].length; x++)
        {
            computerHits[0][x] = -1;
            computerHits[1][x] = -1;
        }
        
        System.out.print("Command: ");
        String command = scan.next();
        
        while (!command.equals("quit"))
        {
            switch (command)
            {
                case "new":
                    color = setPlayerColor();
                    create(playerBoard, computerBoard, color, ships);
                    count = 0;
                    
                    computerHits = new int[2][100];
                    for (int x = 0; x < computerHits[0].length; x++)
                    {
                        computerHits[0][x] = -1;
                        computerHits[1][x] = -1;
                    }
                    computerTurns = new boolean[100];
                    break;
                case "print":
                    print(playerBoard, computerBoard);
                    break;
                case "help":
                    System.out.println("Commands are:" + 
                                    "\n\tnew    - create new game. First game started automatically." + 
                                    "\n\tprint  - print play board" +  
                                    "\n\tscore  - print score" +
                                    "\n\thship  - place ship horizontally" + 
                                    "\n\tvship  - place ship vertically" + 
                                    "\n\tshoot  - shoot at grid location" + 
                                    "\n\thelp   - print command list" + 
                                    "\n\tquit   - Stop game");
                    break;
                case "shoot":
                    playerSinkCount = 0;
                    if (count <= 4)
                    {
                        System.out.println("You haven't placed all your ships.");
                        break;
                    }
                    shotCount++;
                    sinkCount = shoot(computerBoard, color, ships);
                    if (sinkCount == 5)
                    {
                        System.out.println();
                        System.out.println("You have sunk all enemy ships! Game over.");
                        System.out.println("Start New Game.");
                        System.out.println();
                        break;
                    }
                    for (int x = 0; x < ships.length; x++)
                    {
                        if (ships[x].getShipColor() == color && ships[x].getSunk() == true)
                            playerSinkCount++;
                    }
                    if (playerSinkCount == 5)
                    {
                        System.out.println();
                        System.out.println("Computer has sunk all your ships! Game over.");
                        System.out.println("Start New Game.");
                        System.out.println();
                        break;
                    }
                    if (attack == false)
                        attack = computerShoot(playerBoard, color, ships, computerHits, computerTurns, compShotCount, directionCheck, attack);
                    else
                        attack = compAttackMode(playerBoard, color, ships, computerHits, computerTurns, compShotCount, directionCheck, attack);
                    compShotCount++;
                    break;
                case "hship":
                    if (count > 4)
                    {
                        System.out.println("All ships placed. Start shooting.");
                        break;
                    }
                    
                    count = hship(playerBoard, count, ships);
                    break;
                case "vship":
                    if (count > 4)
                    {
                        System.out.println("All ships placed. Start shooting.");
                        break;
                    }
                    
                    count = vship(playerBoard, count, ships);
                    break;
                case "score":
                    score(ships, color);
                    break;
                case "printShips":
                    printShips(ships);
                    break;
                default:
                    System.out.println("Invalid command");
                    break;
            }
            
            if (count > 5)
            {
                System.out.println("All ships placed. Start shooting.");
            }
            
            System.out.print("Command: ");
            command = scan.next();
        }
        scan.close();
    }
    
    private static void create(char[][] playerBoard, char[][] computerBoard, String color, Ship[] ships)
    {
        ships[0] = new Ship(color, "Aircraft Carrier", 5);
        ships[1] = new Ship(color, "Battleship", 4);
        ships[2] = new Ship(color, "Submarine", 3);
        ships[3] = new Ship(color, "Destroyer", 3);
        ships[4] = new Ship(color, "Patrol Boat", 2);
        ships[5] = new Ship(color.equals("Red") ? "Blue" : "Red", "Aircraft Carrier", 5);
        ships[6] = new Ship(color.equals("Red") ? "Blue" : "Red", "Battleship", 4);
        ships[7] = new Ship(color.equals("Red") ? "Blue" : "Red", "Submarine", 3);
        ships[8] = new Ship(color.equals("Red") ? "Blue" : "Red", "Destroyer", 3);
        ships[9] = new Ship(color.equals("Red") ? "Blue" : "Red", "Patrol Boat", 2);
        
        for (int x = 0; x < playerBoard.length; x++)
        {
            for (int y = 0; y < playerBoard[x].length; y++)
            {
                playerBoard[x][y] = '~';
            }
        }
        
        // Adding ships to computer board
        boolean restart;
        int count = 1;
        restart = placeComputerShips(ships, computerBoard);
        while(restart)
        { 
            restart = placeComputerShips(ships, computerBoard);
        }
    }
    
    private static void print(char[][] playerBoard, char[][] computerBoard)
    {
        System.out.println();
        System.out.println("Player Board\t\t\t Computer Board");
        System.out.println();
        for (int x = -1; x < playerBoard.length; x++)
        {
            if (x == -1)
            {
                System.out.print("   ");
                
                for (int y = 1; y <= playerBoard.length; y++)
                {
                    System.out.print(y + " ");
                }
                System.out.print("\t    ");
            
                for (int y = 1; y <= playerBoard.length; y++)
                {
                    System.out.print(y + " ");
                }
                
                System.out.println();
            }
            else
            {
                for (int y = 0; y < playerBoard[x].length; y++)
                {
                    if (y == 0)
                        System.out.print((x == 9 ? (x + 1) + " " : " " + (x + 1) + " ") + playerBoard[x][y] + " ");
                    else
                        System.out.print(playerBoard[x][y] + " ");
                }
                System.out.print("\t\t ");
            
                for (int y = 0; y < playerBoard[x].length; y++)
                {
                    if (y == 0)
                        System.out.print((x == 9 ? (x + 1) + " " : " " + (x + 1) + " ") + 
                                         (computerBoard[x][y] == 'R' || computerBoard[x][y] == 'B' ? '~' : computerBoard[x][y]) + " ");
                    else
                        System.out.print((computerBoard[x][y] == 'R' || computerBoard[x][y] == 'B' ? '~' : computerBoard[x][y]) + " ");
                }
                
                System.out.println();
            }
        }
        System.out.println();
    }
    
    private static int shoot(char[][] computerBoard, String color, Ship[] ships)
    {
        int row = getInput(1); // 1 is passed in for row
        int column = getInput(2);  // 2 is passed in for colum
        int sinkCount = 0;
        char location = computerBoard[row][column];
                
        switch(location)
        {
            case '~':
                System.out.println("You have hit water.");
                break;
            case 'B':
                System.out.println("You have hit a ship!");
                playerHit(computerBoard, row, column, color, ships);
                break;
            case 'R':
                System.out.println("You have hit a ship!");
                playerHit(computerBoard, row, column, color, ships);
                break;
            case 'H':
                System.out.println("You have already hit a shit in this location.");
                break;
        }
        for(int x = 0; x < ships.length; x++)
        {
            if (ships[x].getShipColor() != color && ships[x].getSunk() == true)
                sinkCount++;
        }
        return sinkCount;
    }
    
    private static boolean computerShoot(char[][] playerBoard, String color, Ship[] ships, int[][] computerHits, 
                                         boolean[] computerTurns, int compShotCount, int[][] directionCheck, boolean attack)
    {
        boolean test = false;
        int count, row = 0, column = 0, dirCount = 0;
        attackCount = 1;
        
        while(test == false)
        {
            row = randomNum.nextInt(10);
            column = randomNum.nextInt(10);
            count = 0;
        
            for (int x = 0; x < computerHits[0].length; x++)
            {
                if (computerHits[0][x] == row && computerHits[1][x] == column)
                    count++;
            }
            if (count == 0)
            {
                test = true;
            }
        }
        
        computerHits[0][compShotCount] = row;
        computerHits[1][compShotCount] = column;
        
        char location = playerBoard[row][column];
        
        switch(location)
        {
            case '~':
                System.out.println("Computer has hit water.");
                computerTurns[compShotCount] = false;
                attack = false;
                break;
            case 'B':
                System.out.println("Computer has hit a ship!");
                playerBoard[row][column] = 'H';
                computerTurns[compShotCount] = true;
                computerHit(playerBoard, row, column, color, ships, computerHits, computerTurns);
                attack = true;
                break;
            case 'R':
                System.out.println("Computer has hit a ship!");
                playerBoard[row][column] = 'H';
                computerTurns[compShotCount] = true;
                computerHit(playerBoard, row, column, color, ships, computerHits, computerTurns);
                attack = true;
                break;
        }
        if(attack == true)
        {
            for(int x = 0; x < directionCheck.length; x++)
            {
                for (int y = 0; y < directionCheck[x].length; y++)
                {
                    directionCheck[x][y] = 2;
                }
            }
            if(computerTurns[compShotCount] == true)
            {
                if(computerHits[0][compShotCount - 1] > 0)
                {
                    directionCheck[0][dirCount] = -1;
                    directionCheck[1][dirCount] = 0;
                    dirCount++;
                }
                if(computerHits[0][compShotCount - 1] < 9)
                {
                    directionCheck[0][dirCount] = 1;
                    directionCheck[1][dirCount] = 0;
                    dirCount++;
                }
                if(computerHits[1][compShotCount - 1] > 0)
                {
                    directionCheck[0][dirCount] = 0;
                    directionCheck[1][dirCount] = -1;
                    dirCount++;
                }
                if(computerHits[1][compShotCount - 1] < 9)
                {
                    directionCheck[0][dirCount] = 0;
                    directionCheck[1][dirCount] = 1;
                }
            }
        }
        return attack;
    }
    private static boolean compAttackMode(char[][] playerBoard, String color, Ship[] ships, int[][] computerHits, 
                                          boolean[] computerTurns, int compShotCount, int[][] directionCheck, boolean attack)
    {
        int dirCount = 0, nextVshot, nextHshot, num, row = 0, column= 0;
        
        for (int x = 0; x < directionCheck[0].length; x++)
        {
            if (directionCheck[0][x] != 2)
                dirCount++;
        }
        
        if(attackCount == 1) //First follow up shot from 
        {
            num = randomNum.nextInt(dirCount);
        
            row = computerHits[0][compShotCount - 1] + directionCheck[0][num];
            column = computerHits[1][compShotCount - 1] + directionCheck[1][num];
        }
        else
        {
            //write code for second follow up shot
        }
        
        char location = playerBoard[row][column];
        
        switch(location)
        {
            case '~':
                System.out.println("Computer has hit water.");
                computerTurns[compShotCount] = false;
                //attack = false;
                break;
            case 'B':
                System.out.println("Computer has hit a ship!");
                playerBoard[row][column] = 'H';
                computerTurns[compShotCount] = true;
                computerHit(playerBoard, row, column, color, ships, computerHits, computerTurns);
                //attack = true;
                break;
            case 'R':
                System.out.println("Computer has hit a ship!");
                playerBoard[row][column] = 'H';
                computerTurns[compShotCount] = true;
                computerHit(playerBoard, row, column, color, ships, computerHits, computerTurns);
                //attack = true;
                break;
        }
        attackCount++;
        //System.out.println("\t\t\t\tfirst shot - (" + computerHits[0][compShotCount - 1] + "," + computerHits[1][compShotCount - 1] + ")");
        //System.out.println("\t\t\t\tfollow up shot -(" + row + "," + column + ") - attack count - " + attackCount);
        int[][] shipCords;
        for(int x = 0; x < ships.length; x++)
        {
            if(ships[x].getShipColor() == color)
            {
                shipCords = new int[2][ships[x].getShipLength()];
                shipCords = ships[x].getShipCords();
                
                for(int y = 0; y < shipCords[0].length; y++)
                {
                    if (shipCords[0][y] == row && shipCords[1][y] == column)
                    {
                        if (ships[x].getSunk() ==  true)
                            attack = false;
                    }
                }
            }
        }
        return attack;
    }
    
    private static void computerHit(char[][] gameBoard, int row, int column, String color, Ship[] ships, int[][] computerHits, boolean[] computerTurns)
    {
        
        gameBoard[row][column] = 'H';
        int[][] shipCords;
        boolean sunk = false;
        
        for(int x = 0; x < ships.length; x++)
        {
            if(ships[x].getShipColor() == color)
            {
                shipCords = new int[2][ships[x].getShipLength()];
                shipCords = ships[x].getShipCords();
                
                for(int y = 0; y < shipCords[0].length; y++)
                {
                    if (shipCords[0][y] == row && shipCords[1][y] == column)
                    {
                        ships[x].hitIncrement();
                        ships[x].setHitLocations(row, column);
                        System.out.println("Computer has hit your " + ships[x].getShipName());
                    }
                }
                
                if (ships[x].getHitCount() == ships[x].getShipLength())
                {
                    if(ships[x].getSunk() == false)
                    {
                        System.out.println("Player's " + ships[x].getShipName() + " has been sunk");
                        sunk = true;
                        ships[x].setSunk(sunk);
                    }
                }
            }
        }
    }
    
    private static void playerHit(char[][] gameBoard, int row, int column, String color, Ship[] ships)
    {
        gameBoard[row][column] = 'H';
        int[][] shipCords;
        boolean sunk = false;
        
        for(int x = 0; x < ships.length; x++)
        {
            if(ships[x].getShipColor() != color)
            {
                shipCords = new int[2][ships[x].getShipLength()];
                shipCords = ships[x].getShipCords();
                
                for(int y = 0; y < shipCords[0].length; y++)
                {
                    if (shipCords[0][y] == row && shipCords[1][y] == column)
                    {
                        ships[x].hitIncrement();
                        ships[x].setHitLocations(row, column);
                    }
                }
                
                if (ships[x].getHitCount() == ships[x].getShipLength())
                {
                    if(ships[x].getSunk() == false)
                    {
                        System.out.println("Computer's " + ships[x].getShipName() + " has been sunk");
                        sunk = true;
                        ships[x].setSunk(sunk);
                    }
                }
            }
        }
    }
    
    private static void score(Ship[] ships, String color)
    {
        int playerSinks = 0, computerSinks = 0;
        
        for(int x = 0; x < ships.length; x++)
        {
            if (ships[x].getShipColor().equals(color))
            {
                if(ships[x].getSunk() == true)
                    computerSinks++;
            }
            else
            {
                if(ships[x].getSunk() == true)
                    playerSinks++;
            }
        }
        
        System.out.println("Player has sunk " + playerSinks + " ship(s)");
        System.out.println("Computer has sunk " + computerSinks + " ship(s)");
    }
    
    private static int vship(char[][] playerBoard, int count, Ship[] ships)
    {
        System.out.println("\tPlace " + ships[count].getShipName() + 
                            " - Length: " + ships[count].getShipLength());
        
        int row = getInput(1); // 1 is passed in for row
        int column = getInput(2);  // 2 is passed in for colum
        int locationCheck = 0;
        
        if (row < playerBoard.length - (ships[count].getShipLength() - 1))
        {
            for (int x = row; x < row + ships[count].getShipLength(); x++)
            {
                if (playerBoard[x][column] != '~')
                    locationCheck++;
            }
            
            if (locationCheck == 0)
            {
                for (int x = row; x < row + ships[count].getShipLength(); x++)
                {
                    playerBoard[x][column] = ships[count].getShipColor().equals("Red") ? 'R' : 'B';
                }
                count++;
            }
            else
            {
                System.out.println("Cannot place ship on top of existing ship");
            }
        }
        else
        {
            System.out.println("Invalid Selection - Whole ship cannot be placed on game board from this location");
        }
        
        ships[count - 1].setDirection('V');
        ships[count - 1].setVStart(row);
        ships[count - 1].setHStart(column);
        ships[count - 1].setShipCoords();
        
        return count;
    } 
    
    private static int hship(char[][] playerBoard, int count, Ship[] ships)
    {
        System.out.println("\tPlace " + ships[count].getShipName() + 
                            " - Length: " + ships[count].getShipLength());
        int row = getInput(1); // 1 is passed in for row
        int column = getInput(2);  // 2 is passed in for colum
        int locationCheck = 0;
        
        if (column < playerBoard.length - (ships[count].getShipLength() - 1))
        {
            for (int x = column; x < column + ships[count].getShipLength(); x++)
            {
                if (playerBoard[row][x] != '~')
                    locationCheck++;
            }
            
            if (locationCheck == 0)
            {
                for (int x = column; x < column + ships[count].getShipLength(); x++)
                {
                    playerBoard[row][x] = ships[count].getShipColor().equals("Red") ? 'R' : 'B';
                }
                count++;
            }
            else
            {
                System.out.println("Cannot place ship on top of existing ship");
            }
        }
        else
        {
            System.out.println("Invalid Selection - Whole ship cannot be placed on game board from this location");
        }
        
        ships[count - 1].setDirection('H');
        ships[count - 1].setVStart(row);
        ships[count - 1].setHStart(column);
        ships[count - 1].setShipCoords();
        
        return count;
    } 
    
    private static boolean placeComputerShips(Ship[] ships, char[][] computerBoard)
    {
        int row = -1, column = -1, dirNum;
        boolean shipPlaced;
        int count;
        boolean restart = false;
        char direction = ' ';
        
        for (int x = 0; x < computerBoard.length; x++)
        {
            for (int y = 0; y < computerBoard[x].length; y++)
            {
                computerBoard[x][y] = '~';
            }
        }
        
        for (int y = 5; y < ships.length; y++)
        {
            shipPlaced = false;
            int locationCheck = 0;
            count = 1;
        
            while(!shipPlaced)
            {
                row = randomNum.nextInt(10);
                column = randomNum.nextInt(10);
            
                dirNum = randomNum.nextInt(2);
                
                if (dirNum == 0)
                    direction = 'H';
                else
                    direction = 'V';
                        
                if (direction == 'H') //set ship horizontally
                {
                    if (column < computerBoard.length - (ships[y].getShipLength() - 1))
                    {
                        for (int x = column; x < column + ships[y].getShipLength(); x++)
                        {
                            if (computerBoard[row][x] != '~')
                                locationCheck++;
                        }
                        
                        if (locationCheck == 0)
                        {
                            for (int x = column; x < column + ships[y].getShipLength(); x++)
                            {
                                computerBoard[row][x] = ships[y].getShipColor().equals("Red") ? 'R' : 'B';
                            }
                            shipPlaced = true;
                        }
                        else
                        {
                            count++;
                        }
                    }
                    else
                    {
                        count++;
                    }
                }
                else //set ship vertically
                {
                    if (row < computerBoard.length - (ships[y].getShipLength() - 1))
                    {
                        for (int x = row; x < row + ships[y].getShipLength(); x++)
                        {
                            if (computerBoard[x][column] != '~')
                                locationCheck++;
                        }
                        
                        if (locationCheck == 0)
                        {
                            for (int x = row; x < row + ships[y].getShipLength(); x++)
                            {
                                computerBoard[x][column] = ships[y].getShipColor().equals("Red") ? 'R' : 'B';
                            }
                            shipPlaced = true;
                        }
                        else
                        {
                            count++;
                        }
                    }
                    else
                    {
                        count++;
                    }
                }
                
                if (count > 5)
                {
                    restart = true;
                    return restart;
                }
            }
                
            ships[y].setDirection(direction);
            ships[y].setVStart(row);
            ships[y].setHStart(column);
            ships[y].setShipCoords();
        }
        
        return restart;
    }
    
    private static int getInput(int x)
    {
        int num = 0;
        boolean test = false;
        String input;
        
        while(test == false)
        {
            if (x == 1)
                System.out.print("\trow: ");
            else
                System.out.print("\tcolumn: ");
            input = scan.next();
            
            switch (input)
            {
                case "1":
                    num = 0;
                    test = true;
                    break;
                case "2":
                    num = 1;
                    test = true;
                    break;
                case "3":
                    num = 2;
                    test = true;
                    break;
                case "4":
                    num = 3;
                    test = true;
                    break;
                case "5":
                    num = 4;
                    test = true;
                    break;
                case "6":
                    num = 5;
                    test = true;
                    break;
                case "7":
                    num = 6;
                    test = true;
                    break;
                case "8":
                    num = 7;
                    test = true;
                    break;
                case "9":
                    num = 8;
                    test = true;
                    break;
                case "10":
                    num = 9;
                    test = true;
                    break;
                default:
                    if (x == 1)
                        System.out.println("Invalid row input. Must of between 1 - 10");
                    else
                        System.out.println("Invalid column input. Must of between 1 - 10");
            }
        }
        
        return num;
    }
    
    private static void printShips(Ship[] ships)
    {
        for(int x = 0; x < ships.length; x++)
        {
            System.out.println(ships[x].toString());
        }
    }
}