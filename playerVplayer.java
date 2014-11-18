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
 * playerVplayer class loads GUI and handles gameplay if the player vs player mode is selected
 */
public class playerVplayer extends JFrame{
	private JLabel boardOne = new JLabel();
	private JLabel boardTwo = new JLabel();
	private JPanel buttonArea = new JPanel();

	private JButton newGame = new JButton("New Game");
	private JButton doneTurn = new JButton("Place Ships");
	private JLabel p1shipLabel = new JLabel("Player 1 ships sunk: ");
	private JLabel p2shipLabel = new JLabel("Player 2 ships sunk: ");

	private final int FRAME_WIDTH = 1216;
	private final int FRAME_HEIGHT = 678;

	private ImageIcon aircraftCarrierH = new ImageIcon("AircraftCarrierH.png");
	private ImageIcon battleshipH = new ImageIcon("BattleshipH.png");
	private ImageIcon destroyerH = new ImageIcon("DestroyerH.png");
	private ImageIcon submarineH = new ImageIcon("SubmarineH.png");
	private ImageIcon patrolBoatH = new ImageIcon("PatrolBoatH.png");

	private ArrayList<Shot> player1shots = new ArrayList<Shot>();
	private ArrayList<Shot> player2shots = new ArrayList<Shot>();

	private ImageIcon background = new ImageIcon("Board.png");

	private board1Attack p2AttackHandler = new board1Attack();
	private board2Attack p1AttackHandler = new board2Attack();
	private ship0Listener ship0 = new ship0Listener();
	private ship1Listener ship1 = new ship1Listener();
	private ship2Listener ship2 = new ship2Listener();
	private ship3Listener ship3 = new ship3Listener();
	private ship4Listener ship4 = new ship4Listener();
	private ship5Listener ship5 = new ship5Listener();
	private ship6Listener ship6 = new ship6Listener();
	private ship7Listener ship7 = new ship7Listener();
	private ship8Listener ship8 = new ship8Listener();
	private ship9Listener ship9 = new ship9Listener();

	private Ship[] ships;

	private int player1shotCount,player2shotCount,player1sunkBoats,player2sunkBoats,count = 0;

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
	 * playerVplayer is the constructor for the playerVplayer class
	 */
	public playerVplayer(){
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
		doneTurn.addActionListener(new doneTurn());
		buttonArea.add(newGame);
		buttonArea.add(doneTurn);
		buttonArea.add(p1shipLabel);
		buttonArea.add(p2shipLabel);

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
		newGame.setLocation(10,10);
		doneTurn.setSize(150,20);
		doneTurn.setLocation(FRAME_WIDTH / 2 - doneTurn.getWidth() / 2 ,newGame.getY());
		doneTurn.setVisible(false);
		p1shipLabel.setSize(150,15);
		p1shipLabel.setLocation((boardOne.getWidth() / 2) - (p1shipLabel.getWidth() / 2),buttonArea.getHeight() - p1shipLabel.getHeight() - 5);
		p1shipLabel.setVisible(false);
		p2shipLabel.setSize(150,15);
		p2shipLabel.setLocation(boardTwo.getX() + (boardTwo.getWidth() / 2) - (p2shipLabel.getWidth() / 2),buttonArea.getHeight() - p2shipLabel.getHeight() - 5);
		p2shipLabel.setVisible(false);
	}

	/**
	 * newGameListener is a private inner class to handle the pressing of the new game button
	 */
	private class newGameListener implements ActionListener{
		/**
		 * actionPerformed method handles setting up a new game of battleship
		 */
		public void actionPerformed(ActionEvent e){
			startGame();
		}
	}

	/**
	 * board1Attack is a private inner class to handle attacking player 1 ships by clicking where you want to attack on the board
	 */
	private class board1Attack implements MouseListener{
		/**
		 * mouseClicked method makes sure a shot wasn't already taken in that location and adds a new hit or miss icon 
		 * depending on layout of ships.
		 */
		public void mouseClicked(MouseEvent e){
			if(e.getButton() == 1){
				boolean found = false;

				for(int x = 0; x < player2shots.size(); x ++){
					if(player2shots.get(x).checkBounds(e.getX(), e.getY())){
						found = true;
					}
				}

				if(!found)
				{
					player2shots.add(new Shot(e.getX(),e.getY()));
					boardOne.add(player2shots.get(player2shotCount));

					boardOne.setComponentZOrder(player2shots.get(player2shotCount), 0);

					for(int x = 0; x < ships.length; x++){
						if(ships[x].getPlayer() == 1){
							if(ships[x].checkBounds(e.getX(), e.getY())){
								player2shots.get(player2shotCount).hit();
								ships[x].hit();
								if(ships[x].sunk()){
									player1sunkBoats++;
									p1shipLabel.setText("Player 1 ships sunk: " + player1sunkBoats);
								}
							}
						}
					}

					player2shotCount++;
					boardOne.removeMouseListener(p2AttackHandler);
					buttonArea.requestFocus();
				}
			}
			repaint();
			e.consume();
		}
		public void mousePressed(MouseEvent e){e.consume();}
		public void mouseReleased(MouseEvent e){e.consume();}
		public void mouseEntered(MouseEvent e){e.consume();}
		public void mouseExited(MouseEvent e){e.consume();}
	}

	/**
	 * board2Attack is a private inner class to handle attacking player 2 ships by clicking where you want to attack on the board
	 */
	private class board2Attack implements MouseListener{
		/**
		 * mouseClicked method makes sure a shot wasn't already taken in that location and adds a new hit or miss icon 
		 * depending on layout of ships.
		 */
		public void mouseClicked(MouseEvent e){
			if(e.getButton() == 1){
				boolean found = false;

				for(int x = 0; x < player1shots.size(); x ++){
					if(player1shots.get(x).checkBounds(e.getX(), e.getY())){
						found = true;
					}
				}

				if(!found)
				{
					player1shots.add(new Shot(e.getX(),e.getY()));
					boardTwo.add(player1shots.get(player1shotCount));

					boardTwo.setComponentZOrder(player1shots.get(player1shotCount), 0);

					for(int x = 0; x < ships.length; x++){
						if(ships[x].getPlayer() == 2){
							if(ships[x].checkBounds(e.getX(), e.getY())){
								player1shots.get(player1shotCount).hit();
								ships[x].hit();
								if(ships[x].sunk()){
									player2sunkBoats++;
									p2shipLabel.setText("Player 2 ships sunk: " + player2sunkBoats);
								}
							}
						}
					}

					player1shotCount++;
					boardTwo.removeMouseListener(p1AttackHandler);
					buttonArea.requestFocus();
				}
			}
			repaint();
			e.consume();
		}
		public void mousePressed(MouseEvent e){e.consume();}
		public void mouseReleased(MouseEvent e){e.consume();}
		public void mouseEntered(MouseEvent e){e.consume();}
		public void mouseExited(MouseEvent e){e.consume();}
	}

	/**
	 * doneTurn is a private inner class to handle the pressing of the done turn button
	 */
	private class doneTurn implements ActionListener{
		public void actionPerformed(ActionEvent e){

			if(player1sunkBoats == 5){
				JOptionPane.showMessageDialog(null,"Player 2 wins\nGame Over");
				System.exit(0);
			}
			if(player2sunkBoats == 5){
				JOptionPane.showMessageDialog(null,"Player 1 wins\nGame Over");
				System.exit(0);
			}

			if(count  == 0){
				ships[0].addMouseListener(ship0);
				ships[1].addMouseListener(ship1);
				ships[2].addMouseListener(ship2);
				ships[3].addMouseListener(ship3);
				ships[4].addMouseListener(ship4);
				doneTurn.setVisible(false);
				doneTurn.setText("Done Placing Ships");
				player1setup();
				boardOne.requestFocus();
			}else if (count  == 1){
				ships[0].removeMouseListener(ship0);
				ships[1].removeMouseListener(ship1);
				ships[2].removeMouseListener(ship2);
				ships[3].removeMouseListener(ship3);
				ships[4].removeMouseListener(ship4);
				ships[5].addMouseListener(ship5);
				ships[6].addMouseListener(ship6);
				ships[7].addMouseListener(ship7);
				ships[8].addMouseListener(ship8);
				ships[9].addMouseListener(ship9);
				doneTurn.setVisible(false);
				player2setup();
				boardTwo.requestFocus();
			}
			else if (count % 2 == 0 ){
				ships[5].removeMouseListener(ship5);
				ships[6].removeMouseListener(ship6);
				ships[7].removeMouseListener(ship7);
				ships[8].removeMouseListener(ship8);
				ships[9].removeMouseListener(ship9);
				boardTwo.addMouseListener(p1AttackHandler);
				doneTurn.setVisible(false);
				doneTurn.setText("Done Turn");
				player1turn();
				boardOne.requestFocus();
			}
			else if (count % 2 == 1){
				boardOne.addMouseListener(p2AttackHandler);
				doneTurn.setVisible(false);
				player2turn();
				boardTwo.requestFocus();
			}
			count++;
		}
	}

	/**
	 * startGame method sets visibilities, initializes variables, then creates and places the ships randomly around the boards
	 */
	private void startGame(){

		newGame.setVisible(false);
		doneTurn.setVisible(true);
		p1shipLabel.setVisible(true);
		p2shipLabel.setVisible(true);

		player1shotCount = 0;
		player2shotCount = 0;
		player1sunkBoats = 0;
		player2sunkBoats = 0;

		p1shipLabel.setText("Player 1 ships sunk: " + player1sunkBoats);
		p2shipLabel.setText("Player 2 ships sunk: " + player2sunkBoats);

		ships = new Ship[10];

		for(int x = 0; x < ships.length; x++){
			ranX = ranNum.nextInt(600);
			ranY = ranNum.nextInt(600);
			while(!placeShip(x, ranX, ranY)){
				ranX = ranNum.nextInt(600);
				ranY = ranNum.nextInt(600);
			}
		}

		for(int x = 0; x < ships.length; x++){
			if(ships[x].getPlayer() == 1){
				boardOne.add(ships[x]);
			}
			else{
				boardTwo.add(ships[x]);
			}
			ships[x].setVisible(false);
		}
		buttonArea.requestFocus();

		repaint();
	}

	/**
	 * placeShip method creates a new ship at the random location provided by the xCord and yCord
	 * @param num
	 * @param xCord
	 * @param yCord
	 * @return true if the ship was placed without a problem, false if the ship's placement isn't allowed
	 */
	private boolean placeShip(int num, int xCord, int yCord){
		boolean placed = true;
		int x;
		ships[num] = new Ship(num, shipNames[num], 'H', shipPlayer[num], shipWidth[num], shipHeight[num], xCord, yCord, shipSize[num]);

		if(xCord + ships[num].getWidth() < boardOne.getWidth() && yCord + ships[num].getHeight() < boardOne.getHeight()){
			if(num <= 4){
				x = 0;
				while(x <= 4 && placed == true && x <= num){
					if(x != num){
						if(ships[num].getBounds().intersects(ships[x].getBounds())){
							placed = false;
						}
					}
					x++;
				}
			}else{
				x = 5;
				while(x <= 4 && placed == true && x <= num){
					if(x != num){
						if(ships[num].getBounds().intersects(ships[x].getBounds())){
							placed = false;
						}
					}
					x++;
				}
			}
		}else{
			placed = false;
		}
		return placed;
	}

	/**
	 * allows the player 1 to take their turn by attacking player 2's board
	 */
	private void player1turn(){
		for(int x = 0; x < ships.length; x++){
			ships[x].setVisible(false);
		}
		repaint();
		JOptionPane.showMessageDialog(null,"Player 1 turn");
		for(int x = 0; x < ships.length; x++){
			if(ships[x].getPlayer() == 1)
				ships[x].setVisible(true);
		}
		doneTurn.setVisible(true);
		repaint();
		boardOne.requestFocus();
	}

	/**
	 * allows the player 2 to take their turn by attacking player 1's board
	 */
	private void player2turn(){
		for(int x = 0; x < ships.length; x++){
			ships[x].setVisible(false);
		}
		repaint();
		JOptionPane.showMessageDialog(null,"Player 2 turn");
		for(int x = 0; x < ships.length; x++){
			if(ships[x].getPlayer() == 2)
				ships[x].setVisible(true);
		}
		doneTurn.setVisible(true);
		repaint();
		boardTwo.requestFocus();
	}

	/**
	 * player1setup allows the player to see where their ships were randomly placed, and allows them to right click the 
	 * ship to change the orientation of the ship, if allowed
	 */
	private void player1setup(){
		for(int x = 0; x < ships.length; x++){
			ships[x].setVisible(false);
		}
		repaint();
		JOptionPane.showMessageDialog(null,"Player 1 place ships");
		for(int x = 0; x < ships.length; x++){
			if(ships[x].getPlayer() == 1)
				ships[x].setVisible(true);
		}
		doneTurn.setVisible(true);
		repaint();
		boardOne.requestFocus();
	}

	/**
	 * player2setup allows the player to see where their ships were randomly placed, and allows them to right click the 
	 * ship to change the orientation of the ship, if allowed
	 */
	private void player2setup(){
		for(int x = 0; x < ships.length; x++){
			ships[x].setVisible(false);
		}
		repaint();
		JOptionPane.showMessageDialog(null,"Player 2 place ships");
		for(int x = 0; x < ships.length; x++){
			if(ships[x].getPlayer() == 2)
				ships[x].setVisible(true);
		}
		doneTurn.setVisible(true);
		repaint();
		boardTwo.requestFocus();
	}

	/**
	 * changeOrientation method checks to see if the ship's orientation can be legally made at the player's request
	 * change is made if allowed, nothing visible happens if not allowed
	 * @param ship
	 * @param e
	 */
	private void changeOrientation(int ship, MouseEvent e){
		boolean intersects = false;
		ships[ship].changeOrientation();

		if(ships[ship].getX() + ships[ship].getWidth() > boardOne.getWidth() || ships[ship].getY() + ships[ship].getHeight() > boardTwo.getHeight()){
			intersects = true;
		}
		if(!intersects){
			if(ship <= 4){
				for(int x = 0; x <= 4; x++){
					if(x != ship){
						if(ships[ship].getBounds().intersects(ships[x].getBounds())){
							intersects = true;
						}
					}
				}
			}else{
				for(int x = 5; x <= 9; x++){
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
	 * ship0Listener listens for the player to right click their ship indicating that they want to change it's orientation
	 */
	private class ship0Listener implements MouseListener{
		/**
		 * mouseClicked method sets the value of a ship variable to the ship's number, then sends 
		 * the ship variable, and the mouse click's x,y coordinate
		 */
		public void mouseClicked(MouseEvent e){
			int ship = 0;

			if(e.getButton() == 3){
				changeOrientation(ship, e);
			}

			e.consume();
			boardOne.requestFocus();
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
			int ship = 1;

			if(e.getButton() == 3){
				changeOrientation(ship, e);
			}

			e.consume();
			boardOne.requestFocus();
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
			int ship = 2;

			if(e.getButton() == 3){
				changeOrientation(ship, e);
			}

			e.consume();
			boardOne.requestFocus();
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
			int ship = 3;

			if(e.getButton() == 3){
				changeOrientation(ship, e);
			}

			e.consume();
			boardOne.requestFocus();
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
			int ship = 4;

			if(e.getButton() == 3){
				changeOrientation(ship, e);
			}

			e.consume();
			boardOne.requestFocus();
		}
		public void mousePressed(MouseEvent e){e.consume();}
		public void mouseReleased(MouseEvent e){e.consume();}
		public void mouseEntered(MouseEvent e){e.consume();}
		public void mouseExited(MouseEvent e){e.consume();}
	}

	/**
	 * ship5Listener listens for the player to right click their ship indicating that they want to change it's orientation
	 */
	private class ship5Listener implements MouseListener{
		/**
		 * mouseClicked method sets the value of a ship variable to the ship's number, then sends 
		 * the ship variable, and the mouse click's x,y coordinate
		 */
		public void mouseClicked(MouseEvent e){
			int ship = 5;

			if(e.getButton() == 3){
				changeOrientation(ship, e);
			}

			e.consume();
			boardTwo.requestFocus();
		}
		public void mousePressed(MouseEvent e){e.consume();}
		public void mouseReleased(MouseEvent e){e.consume();}
		public void mouseEntered(MouseEvent e){e.consume();}
		public void mouseExited(MouseEvent e){e.consume();}
	}

	/**
	 * ship6Listener listens for the player to right click their ship indicating that they want to change it's orientation
	 */
	private class ship6Listener implements MouseListener{
		/**
		 * mouseClicked method sets the value of a ship variable to the ship's number, then sends 
		 * the ship variable, and the mouse click's x,y coordinate
		 */
		public void mouseClicked(MouseEvent e){
			int ship = 6;

			if(e.getButton() == 3){
				changeOrientation(ship, e);
			}

			e.consume();
			boardTwo.requestFocus();
		}
		public void mousePressed(MouseEvent e){e.consume();}
		public void mouseReleased(MouseEvent e){e.consume();}
		public void mouseEntered(MouseEvent e){e.consume();}
		public void mouseExited(MouseEvent e){e.consume();}
	}

	/**
	 * ship7Listener listens for the player to right click their ship indicating that they want to change it's orientation
	 */
	private class ship7Listener implements MouseListener{
		/**
		 * mouseClicked method sets the value of a ship variable to the ship's number, then sends 
		 * the ship variable, and the mouse click's x,y coordinate
		 */
		public void mouseClicked(MouseEvent e){
			int ship = 7;

			if(e.getButton() == 3){
				changeOrientation(ship, e);
			}

			e.consume();
			boardTwo.requestFocus();
		}
		public void mousePressed(MouseEvent e){e.consume();}
		public void mouseReleased(MouseEvent e){e.consume();}
		public void mouseEntered(MouseEvent e){e.consume();}
		public void mouseExited(MouseEvent e){e.consume();}
	}

	/**
	 * ship8Listener listens for the player to right click their ship indicating that they want to change it's orientation
	 */
	private class ship8Listener implements MouseListener{
		/**
		 * mouseClicked method sets the value of a ship variable to the ship's number, then sends 
		 * the ship variable, and the mouse click's x,y coordinate
		 */
		public void mouseClicked(MouseEvent e){
			int ship = 8;

			if(e.getButton() == 3){
				changeOrientation(ship, e);
			}

			e.consume();
			boardTwo.requestFocus();
		}
		public void mousePressed(MouseEvent e){e.consume();}
		public void mouseReleased(MouseEvent e){e.consume();}
		public void mouseEntered(MouseEvent e){e.consume();}
		public void mouseExited(MouseEvent e){e.consume();}
	}

	/**
	 * ship9Listener listens for the player to right click their ship indicating that they want to change it's orientation
	 */
	private class ship9Listener implements MouseListener{
		/**
		 * mouseClicked method sets the value of a ship variable to the ship's number, then sends 
		 * the ship variable, and the mouse click's x,y coordinate
		 */
		public void mouseClicked(MouseEvent e){
			int ship = 9;

			if(e.getButton() == 3){
				changeOrientation(ship, e);
			}

			e.consume();
			boardTwo.requestFocus();
		}
		public void mousePressed(MouseEvent e){e.consume();}
		public void mouseReleased(MouseEvent e){e.consume();}
		public void mouseEntered(MouseEvent e){e.consume();}
		public void mouseExited(MouseEvent e){e.consume();}
	}
}