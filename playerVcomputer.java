import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Random;

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

	public playerVcomputer(){
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setLayout(null);

		boardOne.setLayout(null);
		boardTwo.setLayout(null);
		buttonArea.setLayout(null);

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

	private void placePlayerShips(){
		int start = 0;
		int stop = 4;
		boolean placed;

		for(int x = start; x <= stop; x++){
			placed = false;

			while(!placed)
			{
				ranX = ranNum.nextInt(600);
				ranY = ranNum.nextInt(600);
				placed = true;

				ships[x] = new Ship(x, shipNames[x], 'H', shipPlayer[x], shipWidth[x], shipHeight[x], ranX, ranY, shipSize[x]);
				ships[x].setLocation(ships[x].returnXCord(), ships[x].returnYCord());
				ships[x].setSize(ships[x].returnWidth(), ships[x].returnHeight());

				if(ships[x].returnXCord() + ships[x].returnWidth() < boardOne.getWidth() && ships[x].returnYCord() + ships[x].returnHeight() < boardOne.getHeight()){
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

	private void placeComputerShips(){
		int start = 5;
		int stop = 9;
		boolean placed;
		int orientation;

		for(int x = start; x <= stop; x++){
			placed = false;

			while(!placed)
			{
				ranX = ranNum.nextInt(600);
				ranY = ranNum.nextInt(600);
				orientation = ranNum.nextInt(2);
				placed = true;

				ships[x] = new Ship(x, shipNames[x], 'H', shipPlayer[x], shipWidth[x], shipHeight[x], ranX, ranY, shipSize[x]);
				ships[x].setLocation(ships[x].returnXCord(), ships[x].returnYCord());
				ships[x].setSize(ships[x].returnWidth(), ships[x].returnHeight());

				if(orientation == 1){
					changeOrientation(x);
				}

				if(ships[x].returnXCord() + ships[x].returnWidth() < boardTwo.getWidth() && ships[x].returnYCord() + ships[x].returnHeight() < boardTwo.getHeight()){
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
		}

		for(int x = 0; x < computerAttacks.length; x++){
			for(int y = 0; y < computerAttacks[x].length; y++){
				computerAttacks[x][y] = false;
			}
		}
	}

	private void computerTurn(){
		boolean attacked = false;
		while(!attacked){
			ranX = ranNum.nextInt(30);
			ranY = ranNum.nextInt(30);

			if(computerAttacks[ranX][ranY] == false){
				computerShots.add(new Shot(ranX * 20,ranY * 20));
				computerShots.get(computerShotCount).setLocation(computerShots.get(computerShotCount).returnXCord(),computerShots.get(computerShotCount).returnYCord());
				computerShots.get(computerShotCount).setSize(computerShots.get(computerShotCount).returnWidth(),computerShots.get(computerShotCount).returnHeight());
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
				computerShots.get(computerShotCount).setIcon(computerShots.get(computerShotCount).getPic());

				computerShotCount++;
				attacked = true;
			}
		}
		boardTwo.addMouseListener(playerAttackHandler);
		boardTwo.requestFocus();

		repaint();
	}

	private class playerAttack implements MouseListener{
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
					playerShots.add(new Shot(e.getX(),e.getY()));
					playerShots.get(playerShotCount).setLocation(playerShots.get(playerShotCount).returnXCord(),playerShots.get(playerShotCount).returnYCord());
					playerShots.get(playerShotCount).setSize(playerShots.get(playerShotCount).returnWidth(),playerShots.get(playerShotCount).returnHeight());
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
					playerShots.get(playerShotCount).setIcon(playerShots.get(playerShotCount).getPic());

					playerShotCount++;
					boardTwo.removeMouseListener(playerAttackHandler);
				}
			}
			repaint();
			e.consume();
			
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
		public void mousePressed(MouseEvent e){e.consume();}
		public void mouseReleased(MouseEvent e){e.consume();}
		public void mouseEntered(MouseEvent e){e.consume();}
		public void mouseExited(MouseEvent e){e.consume();}
	}

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

		repaint();
		boardOne.requestFocus();
	}

	private void changeOrientation(int ship){
		boolean intersects = false;
		ships[ship].changeOrientation();
		ships[ship].setSize(ships[ship].returnWidth(), ships[ship].returnHeight());

		if(ships[ship].returnXCord() + ships[ship].returnWidth() > boardOne.getWidth() || ships[ship].returnYCord() + ships[ship].returnHeight() > boardTwo.getHeight()){
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
			ships[ship].setSize(ships[ship].returnWidth(), ships[ship].returnHeight());
		}
		repaint();
	}

	private class resetShips implements ActionListener{
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

	private class newGameListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			startGame();
		}
	}

	private class doneListener implements ActionListener{
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

	private class ship0Listener implements MouseListener{
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

	private class ship1Listener implements MouseListener{
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

	private class ship2Listener implements MouseListener{
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

	private class ship3Listener implements MouseListener{
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

	private class ship4Listener implements MouseListener{
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