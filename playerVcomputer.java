import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Random;

/**
 * playerVcomputer class loads GUI and handles gameplay if the player vs computer mode is selected
 */
public class playerVcomputer extends JFrame{
	private JLabel boardOne = new JLabel();
	private JLabel boardTwo = new JLabel();
	private JPanel buttonArea = new JPanel();

	private JButton newGame = new JButton("New Game");
	private JButton resetShips = new JButton("Randomize ships");
	private JButton done = new JButton("Set Ships");
	private JLabel p1shipLabel = new JLabel("Player 1 ships sunk: ");
	private JLabel computerShipLabel = new JLabel("Player 2 ships sunk: ");

	private final int FRAME_WIDTH = 1216;
	private final int FRAME_HEIGHT = 678;

	private ImageIcon aircraftCarrierH = new ImageIcon("AircraftCarrierH.png");
	private ImageIcon battleshipH = new ImageIcon("BattleshipH.png");
	private ImageIcon destroyerH = new ImageIcon("DestroyerH.png");
	private ImageIcon submarineH = new ImageIcon("SubmarineH.png");
	private ImageIcon patrolBoatH = new ImageIcon("PatrolBoatH.png");

	private ArrayList<Shot> playerShots = new ArrayList<Shot>();
	private ArrayList<Shot> computerShots = new ArrayList<Shot>();

	private ImageIcon background = new ImageIcon("Board.png");

	private playerAttack playerAttackHandler = new playerAttack();
	private ship0Listener ship0handler = new ship0Listener();
	private ship1Listener ship1handler = new ship1Listener();
	private ship2Listener ship2handler = new ship2Listener();
	private ship3Listener ship3handler = new ship3Listener();
	private ship4Listener ship4handler = new ship4Listener();

	private Ship[] ships;

	private int playerShotCount,playerBoatsSunk,computerBoatsSunk;
	private int computerShotCount,count = 0;

	private boolean[][] computerAttacks = new boolean[30][30];

	private Random ranNum = new Random();
	private String[] shipNames = {"aircraftCarrier","battleship","destroyer","submarine","patrolBoat","aircraftCarrier","battleship","destroyer","submarine","patrolBoat"};
	private int[] shipPlayer = {1,1,1,1,1,2,2,2,2,2};
	private int[] shipWidth = {aircraftCarrierH.getIconWidth(), battleshipH.getIconWidth(),destroyerH.getIconWidth(),
			submarineH.getIconWidth(),patrolBoatH.getIconWidth(),aircraftCarrierH.getIconWidth(), battleshipH.getIconWidth(),
			destroyerH.getIconWidth(),submarineH.getIconWidth(),patrolBoatH.getIconWidth()};
	private int[] shipHeight = {aircraftCarrierH.getIconHeight(), battleshipH.getIconHeight(),destroyerH.getIconHeight(),
			submarineH.getIconHeight(),patrolBoatH.getIconHeight(),aircraftCarrierH.getIconHeight(), battleshipH.getIconHeight(),
			destroyerH.getIconHeight(),submarineH.getIconHeight(),patrolBoatH.getIconHeight()};
	private int[] shipSize = {5,4,3,3,2,5,4,3,3,2};
	private int ranX = 0, ranY = 0;

	/**
	 * playerVcomputer is the constructor for the playerVcomputer class
	 */
	public playerVcomputer(){
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setLayout(null);

		boardOne.setLayout(null);
		boardTwo.setLayout(null);
		buttonArea.setLayout(null);
		
		setLocations();
		
		super.add(buttonArea);
		super.add(boardOne);
		super.add(boardTwo);

		newGame.addActionListener(new newGameListener());
		resetShips.addActionListener(new resetShips());
		done.addActionListener(new doneListener());
		buttonArea.add(newGame);
		buttonArea.add(resetShips);
		buttonArea.add(done);
		buttonArea.add(p1shipLabel);
		buttonArea.add(computerShipLabel);

		super.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		super.setResizable(false);
		super.setVisible(true);
		super.setLocationRelativeTo(null);
	}
	
	/**
	 * setLocations method set the sizes and locations of items on the GUI
	 */
	private void setLocations(){
		buttonArea.setSize(FRAME_WIDTH, 50);
		buttonArea.setLocation(0, 0);
		boardOne.setSize(600,600);
		boardOne.setLocation(0,buttonArea.getHeight());
		boardTwo.setSize(600,600);
		boardTwo.setLocation(boardOne.getWidth() + 10,buttonArea.getHeight());
		boardOne.setIcon(background);
		boardTwo.setIcon(background);
		newGame.setSize(100,20);
		newGame.setLocation(5,5);
		resetShips.setSize(150, 20);
		resetShips.setLocation(newGame.getX() + newGame.getWidth() + 10, newGame.getY());
		resetShips.setVisible(false);
		done.setSize(100, 20);
		done.setLocation(FRAME_WIDTH / 2 - done.getWidth() / 2, newGame.getY());
		done.setVisible(false);
		p1shipLabel.setSize(150,15);
		p1shipLabel.setLocation((boardOne.getWidth() / 2) - (p1shipLabel.getWidth() / 2),buttonArea.getHeight() - p1shipLabel.getHeight() - 5);
		p1shipLabel.setVisible(false);
		computerShipLabel.setSize(150,15);
		computerShipLabel.setLocation(boardTwo.getX() + (boardTwo.getWidth() / 2) - (computerShipLabel.getWidth() / 2),buttonArea.getHeight() - computerShipLabel.getHeight() - 5);
		computerShipLabel.setVisible(false);
	}

	/**
	 * placePlayerShips method randomly places player ships around the screen
	 */
	private void placePlayerShips(){
		int start = 0, stop = 4;
		boolean placed;

		for(int x = start; x <= stop; x++){
			placed = false;

			while(!placed)
			{
				ranX = ranNum.nextInt(600);
				ranY = ranNum.nextInt(600);
				placed = true;

				ships[x] = new Ship(x, shipNames[x], 'H', shipPlayer[x], shipWidth[x], shipHeight[x], ranX, ranY, shipSize[x]);
				ships[x].setLocation(ships[x].getX(), ships[x].getY());
				ships[x].setSize(ships[x].getWidth(), ships[x].getHeight());

				if(ships[x].getX() + ships[x].getWidth() < boardOne.getWidth() && ships[x].getY() + ships[x].getHeight() < boardOne.getHeight()){
					for(int y = start; y < x; y++){
						if(ships[x].getBounds().intersects(ships[y].getBounds())){
							placed = false;
						}
					}
				}else{
					placed = false;
				}
			}
			boardOne.add(ships[x]);
		}
		ships[0].addMouseListener(ship0handler);
		ships[1].addMouseListener(ship1handler);
		ships[2].addMouseListener(ship2handler);
		ships[3].addMouseListener(ship3handler);
		ships[4].addMouseListener(ship4handler);
	}

	/**
	 * placeComputerShips method randomly places computer ships around the screen
	 */
	private void placeComputerShips(){
		int start = 5, stop = 9, orientation;
		boolean placed;

		for(int x = start; x <= stop; x++){
			placed = false;

			while(!placed)
			{
				ranX = ranNum.nextInt(600);
				ranY = ranNum.nextInt(600);
				orientation = ranNum.nextInt(2);
				placed = true;

				ships[x] = new Ship(x, shipNames[x], 'H', shipPlayer[x], shipWidth[x], shipHeight[x], ranX, ranY, shipSize[x]);
				ships[x].setLocation(ships[x].getX(), ships[x].getY());
				ships[x].setSize(ships[x].getWidth(), ships[x].getHeight());

				if(orientation == 1){
					changeOrientation(x);
				}

				if(ships[x].getX() + ships[x].getWidth() < boardTwo.getWidth() && ships[x].getY() + ships[x].getHeight() < boardTwo.getHeight()){
					for(int y = start; y < x; y++){
						if(ships[x].getBounds().intersects(ships[y].getBounds())){
							placed = false;
						}
					}
				}else{
					placed = false;
				}
			}
			boardTwo.add(ships[x]);
			ships[x].setVisible(false);
		}
	}

	/**
	 * computerTurn method selects a random location around the player board and places a shot there
	 */
	private void computerTurn(){
		boolean attacked = false;
		while(!attacked){
			ranX = ranNum.nextInt(30);
			ranY = ranNum.nextInt(30);

			if(computerAttacks[ranX][ranY] == false){
				computerShots.add(new Shot(ranX * 20,ranY * 20));
				boardOne.add(computerShots.get(computerShotCount));
				
				boardOne.setComponentZOrder(computerShots.get(computerShotCount), 0);

				for(int x = 0; x < ships.length; x++){
					if(ships[x].getPlayer() == 1){
						if(ships[x].checkBounds(ranX * 20 + 5, ranY * 20 + 5)){
							computerShots.get(computerShotCount).hit();
							ships[x].hit();
							if(ships[x].sunk()){
								playerBoatsSunk++;
								p1shipLabel.setText("Player 1 ships sunk: " + playerBoatsSunk);
							}
						}
					}
				}

				computerShotCount++;
				attacked = true;
			}
		}
		boardTwo.addMouseListener(playerAttackHandler);
		boardTwo.requestFocus();

		repaint();
	}

	/**
	 * playerAttack is a private inner class that listens for player attacks on the computer board
	 */
	private class playerAttack implements MouseListener{
		/**
		 * mouseClicked method makes sure a shot wasn't already taken in that location and adds a new hit or miss icon 
		 * depending on layout of ships.
		 */
		public void mouseClicked(MouseEvent e){
			if(e.getButton() == 1){
				boolean found = false;

				for(int x = 0; x < playerShots.size(); x ++){
					if(playerShots.get(x).checkBounds(e.getX(), e.getY())){
						found = true;
					}
				}

				if(!found)
				{
					newPlayerAttack(e);
				}
			}
			repaint();
			e.consume();
			boardTwo.requestFocus();
		}
		public void mousePressed(MouseEvent e){e.consume();}
		public void mouseReleased(MouseEvent e){e.consume();}
		public void mouseEntered(MouseEvent e){e.consume();}
		public void mouseExited(MouseEvent e){e.consume();}
	}
	
	/**
	 * newPlayerAttack performs the player attack when called
	 */
	private void newPlayerAttack(MouseEvent e){
		playerShots.add(new Shot(e.getX(),e.getY()));
		boardTwo.add(playerShots.get(playerShotCount));

		boardTwo.setComponentZOrder(playerShots.get(playerShotCount), 0);

		for(int x = 0; x < ships.length; x++){
			if(ships[x].getPlayer() == 2){
				if(ships[x].checkBounds(e.getX(), e.getY())){
					playerShots.get(playerShotCount).hit();
					ships[x].hit();
					if(ships[x].sunk()){
						computerBoatsSunk++;
						computerShipLabel.setText("Computer ships sunk: " + computerBoatsSunk);
					}
				}
			}
		}

		playerShotCount++;
		boardTwo.removeMouseListener(playerAttackHandler);
		
		if(computerBoatsSunk == 5){
			JOptionPane.showMessageDialog(null,"Game Over\nPlayer has won");
			System.exit(0);
		}
		
		computerTurn();
		
		if(playerBoatsSunk == 5){
			JOptionPane.showMessageDialog(null,"Game Over\nComputer has won");
			System.exit(0);
		}
	}

	/**
	 * startGame method sets visibilities, initializes variables, then calls other methods to place ships
	 */
	private void startGame(){
		newGame.setVisible(false);
		p1shipLabel.setVisible(true);
		computerShipLabel.setVisible(true);
		resetShips.setVisible(true);
		done.setVisible(true);

		playerShotCount = 0;
		computerShotCount = 0;
		playerBoatsSunk = 0;
		computerBoatsSunk = 0;

		p1shipLabel.setText("Player ships sunk: " + playerBoatsSunk);
		computerShipLabel.setText("Computer ships sunk: " + computerBoatsSunk);

		ships = new Ship[10];

		placePlayerShips();
		placeComputerShips();

		for(int x = 0; x < computerAttacks.length; x++){
			for(int y = 0; y < computerAttacks[x].length; y++){
				computerAttacks[x][y] = false;
			}
		}

		repaint();
		boardOne.requestFocus();
	}

	/**
	 * changeOrientation method checks to see if the ship's orientation can be legally made at the player's request
	 * change is made if allowed, nothing visible happens if not allowed
	 * @param ship
	 */
	private void changeOrientation(int ship){
		boolean intersects = false;
		ships[ship].changeOrientation();

		if(ships[ship].getX() + ships[ship].getWidth() > boardOne.getWidth() || ships[ship].getY() + ships[ship].getHeight() > boardTwo.getHeight()){
			intersects = true;
		}
		if(!intersects){
			if(ship <= 4){
				for(int x = 0; x < ship; x++){
					if(x != ship){
						if(ships[ship].getBounds().intersects(ships[x].getBounds())){
							intersects = true;
						}
					}
				}
			}else{
				for(int x = 5; x < ship; x++){
					if(x != ship){
						if(ships[ship].getBounds().intersects(ships[x].getBounds())){
							intersects = true;
						}
					}
				}
			}
		}
		if(intersects){
			ships[ship].changeOrientation();
		}
		repaint();
	}

	/**
	 * resetShips is a private inner class to listen for the resetShips button to be pressed
	 */
	private class resetShips implements ActionListener{
		/**
		 * actionPerformed method removes all ships from the board and calls the methods to randomly place them again
		 */
		public void actionPerformed(ActionEvent e){
			for(int x = 0; x < ships.length; x++){
				if(x <= 4)
					boardOne.remove(ships[x]);
				else
					boardTwo.remove(ships[x]);
			}
			placePlayerShips();
			placeComputerShips();
		}
	}

	/**
	 * newGameListener is a private inner class to listen for the newGame button to be pressed
	 */
	private class newGameListener implements ActionListener{
		/**
		 * actionPerformed method calls the startGame method
		 */
		public void actionPerformed(ActionEvent e){
			startGame();
		}
	}

	/**
	 * doneListener is a private inner class to listen for the done button to be pressed
	 */
	private class doneListener implements ActionListener{
		/**
		 * actionPerformed method adds the mouse listener back onto the computer's board, so the player can attack
		 */
		public void actionPerformed(ActionEvent e){
			done.setText("Done Turn");
			resetShips.setVisible(false);
			ships[0].removeMouseListener(ship0handler);
			ships[1].removeMouseListener(ship1handler);
			ships[2].removeMouseListener(ship2handler);
			ships[3].removeMouseListener(ship3handler);
			ships[4].removeMouseListener(ship4handler);
			boardTwo.addMouseListener(playerAttackHandler);
			boardTwo.requestFocus();
			done.setVisible(false);
			count++;
		}
	}

	/**
	 * ship0Listener listens for the player to right click their ship indicating that they want to change it's orientation
	 */
	private class ship0Listener implements MouseListener{
		/**
		 * mouseClicked method sets the value of a ship variable to the ship's number, then sends 
		 * the ship variable, and the mouse click's x,y coordinate
		 */
		public void mouseClicked(MouseEvent e){
			int num = 0;
			if(e.getButton() == 3){
				changeOrientation(num);
			}
			e.consume();
		}
		public void mousePressed(MouseEvent e){e.consume();}
		public void mouseReleased(MouseEvent e){e.consume();}
		public void mouseEntered(MouseEvent e){e.consume();}
		public void mouseExited(MouseEvent e){e.consume();}
	}

	/**
	 * ship1Listener listens for the player to right click their ship indicating that they want to change it's orientation
	 */
	private class ship1Listener implements MouseListener{
		/**
		 * mouseClicked method sets the value of a ship variable to the ship's number, then sends 
		 * the ship variable, and the mouse click's x,y coordinate
		 */
		public void mouseClicked(MouseEvent e){
			int num = 1;
			if(e.getButton() == 3){
				changeOrientation(num);
			}
			e.consume();
		}
		public void mousePressed(MouseEvent e){e.consume();}
		public void mouseReleased(MouseEvent e){e.consume();}
		public void mouseEntered(MouseEvent e){e.consume();}
		public void mouseExited(MouseEvent e){e.consume();}
	}

	/**
	 * ship2Listener listens for the player to right click their ship indicating that they want to change it's orientation
	 */
	private class ship2Listener implements MouseListener{
		/**
		 * mouseClicked method sets the value of a ship variable to the ship's number, then sends 
		 * the ship variable, and the mouse click's x,y coordinate
		 */
		public void mouseClicked(MouseEvent e){
			int num = 2;
			if(e.getButton() == 3){
				changeOrientation(num);
			}
			e.consume();
		}
		public void mousePressed(MouseEvent e){e.consume();}
		public void mouseReleased(MouseEvent e){e.consume();}
		public void mouseEntered(MouseEvent e){e.consume();}
		public void mouseExited(MouseEvent e){e.consume();}
	}

	/**
	 * ship3Listener listens for the player to right click their ship indicating that they want to change it's orientation
	 */
	private class ship3Listener implements MouseListener{
		/**
		 * mouseClicked method sets the value of a ship variable to the ship's number, then sends 
		 * the ship variable, and the mouse click's x,y coordinate
		 */
		public void mouseClicked(MouseEvent e){
			int num = 3;
			if(e.getButton() == 3){
				changeOrientation(num);
			}
			e.consume();
		}
		public void mousePressed(MouseEvent e){e.consume();}
		public void mouseReleased(MouseEvent e){e.consume();}
		public void mouseEntered(MouseEvent e){e.consume();}
		public void mouseExited(MouseEvent e){e.consume();}
	}

	/**
	 * ship4Listener listens for the player to right click their ship indicating that they want to change it's orientation
	 */
	private class ship4Listener implements MouseListener{
		/**
		 * mouseClicked method sets the value of a ship variable to the ship's number, then sends 
		 * the ship variable, and the mouse click's x,y coordinate
		 */
		public void mouseClicked(MouseEvent e){
			int num = 4;
			if(e.getButton() == 3){
				changeOrientation(num);
			}
			e.consume();
		}
		public void mousePressed(MouseEvent e){e.consume();}
		public void mouseReleased(MouseEvent e){e.consume();}
		public void mouseEntered(MouseEvent e){e.consume();}
		public void mouseExited(MouseEvent e){e.consume();}
	}
}