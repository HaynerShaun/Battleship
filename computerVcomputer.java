import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Random;

public class computerVcomputer extends JFrame{
	private JLabel boardOne = new JLabel();
	private JLabel boardTwo = new JLabel();
	private JPanel buttonArea = new JPanel();

	private JButton newGame = new JButton("New Game");
	private JLabel cp1shipLabel = new JLabel("Computer Player 1 ships sunk: ");
	private JLabel cp2shipLabel = new JLabel("Computer Player 2 ships sunk: ");

	private final int FRAME_WIDTH = 1216;
	private final int FRAME_HEIGHT = 678;

	private ImageIcon aircraftCarrierH = new ImageIcon("AircraftCarrierH.png");
	private ImageIcon battleshipH = new ImageIcon("BattleshipH.png");
	private ImageIcon destroyerH = new ImageIcon("DestroyerH.png");
	private ImageIcon submarineH = new ImageIcon("SubmarineH.png");
	private ImageIcon patrolBoatH = new ImageIcon("PatrolBoatH.png");

	private ArrayList<Shot> cp1Shots = new ArrayList<Shot>();
	private ArrayList<Shot> cp2Shots = new ArrayList<Shot>();

	private ImageIcon background = new ImageIcon("Board.png");

	private Ship[] ships;

	private Timer gameTimer;

	private int cp1ShotCount,cp1BoatsSunk,cp2BoatsSunk, cp2ShotCount, count = 0;

	private boolean[][] cp1Attacks = new boolean[30][30];
	private boolean[][] cp2Attacks = new boolean[30][30];

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

	public computerVcomputer(){
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

		cp1shipLabel.setSize(200,15);
		cp1shipLabel.setLocation((boardOne.getWidth() / 2) - (cp1shipLabel.getWidth() / 2),buttonArea.getHeight() - cp1shipLabel.getHeight() - 5);
		cp1shipLabel.setVisible(false);

		cp2shipLabel.setSize(200,15);
		cp2shipLabel.setLocation(boardTwo.getX() + (boardTwo.getWidth() / 2) - (cp2shipLabel.getWidth() / 2),buttonArea.getHeight() - cp2shipLabel.getHeight() - 5);
		cp2shipLabel.setVisible(false);

		gameTimer = new Timer(10, new TimerListener());

		super.add(buttonArea);
		super.add(boardOne);
		super.add(boardTwo);

		newGame.addActionListener(new newGameListener());
		buttonArea.add(newGame);
		buttonArea.add(cp1shipLabel);
		buttonArea.add(cp2shipLabel);

		super.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		super.setResizable(false);
		super.setVisible(true);
		super.setLocationRelativeTo(null);
	}

	private void cp1PlaceShips(){
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
	}

	private void cp2PlaceShips(){
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
	}

	private void cp1turn(){
		boolean attacked = false;
		while(!attacked){
			ranX = ranNum.nextInt(30);
			ranY = ranNum.nextInt(30);

			if(cp1Attacks[ranX][ranY] == false){
				cp1Shots.add(new Shot(ranX * 20,ranY * 20));
				cp1Shots.get(cp1ShotCount).setLocation(cp1Shots.get(cp1ShotCount).returnXCord(),cp1Shots.get(cp1ShotCount).returnYCord());
				cp1Shots.get(cp1ShotCount).setSize(cp1Shots.get(cp1ShotCount).returnWidth(),cp1Shots.get(cp1ShotCount).returnHeight());
				boardTwo.add(cp1Shots.get(cp1ShotCount));

				boardTwo.setComponentZOrder(cp1Shots.get(cp1ShotCount), 0);

				for(int x = 0; x < ships.length; x++){
					if(ships[x].getPlayer() == 2){
						if(ships[x].checkBounds(ranX * 20 + 5, ranY * 20 + 5)){
							cp1Shots.get(cp1ShotCount).hit();
							ships[x].hit();
							if(ships[x].sunk()){
								cp2BoatsSunk++;
								cp2shipLabel.setText("Computer Player 2 ships sunk: " + cp2BoatsSunk);
							}
						}
					}
				}
				cp1Shots.get(cp1ShotCount).setIcon(cp1Shots.get(cp1ShotCount).getPic());

				cp1ShotCount++;
				attacked = true;
				cp1Attacks[ranX][ranY] = true;
			}
		}

		repaint();
	}

	private void cp2turn(){
		boolean attacked = false;
		while(!attacked){
			ranX = ranNum.nextInt(30);
			ranY = ranNum.nextInt(30);

			if(cp2Attacks[ranX][ranY] == false){
				cp2Shots.add(new Shot(ranX * 20,ranY * 20));
				cp2Shots.get(cp2ShotCount).setLocation(cp2Shots.get(cp2ShotCount).returnXCord(),cp2Shots.get(cp2ShotCount).returnYCord());
				cp2Shots.get(cp2ShotCount).setSize(cp2Shots.get(cp2ShotCount).returnWidth(),cp2Shots.get(cp2ShotCount).returnHeight());
				boardOne.add(cp2Shots.get(cp2ShotCount));

				boardOne.setComponentZOrder(cp2Shots.get(cp2ShotCount), 0);

				for(int x = 0; x < ships.length; x++){
					if(ships[x].getPlayer() == 1){
						if(ships[x].checkBounds(ranX * 20 + 5, ranY * 20 + 5)){
							cp2Shots.get(cp2ShotCount).hit();
							ships[x].hit();
							if(ships[x].sunk()){
								cp1BoatsSunk++;
								cp1shipLabel.setText("Computer Player 1 ships sunk: " + cp1BoatsSunk);
							}
						}
					}
				}
				cp2Shots.get(cp2ShotCount).setIcon(cp2Shots.get(cp2ShotCount).getPic());

				cp2ShotCount++;
				attacked = true;
				cp2Attacks[ranX][ranY] = true;
			}
		}

		repaint();
	}

	private void startGame(){

		newGame.setVisible(false);
		cp1shipLabel.setVisible(true);
		cp2shipLabel.setVisible(true);

		cp1ShotCount = 0;
		cp2ShotCount = 0;
		cp1BoatsSunk = 0;
		cp2BoatsSunk = 0;

		cp1shipLabel.setText("Computer Player 1 ships sunk: " + cp1BoatsSunk);
		cp2shipLabel.setText("Computer Player 2 ships sunk: " + cp2BoatsSunk);

		ships = new Ship[10];

		cp1PlaceShips();
		cp2PlaceShips();

		for(int x = 0; x < cp1Attacks.length; x++){
			for(int y = 0; y < cp1Attacks[x].length; y++){
				cp1Attacks[x][y] = false;
				cp2Attacks[x][y] = false;
			}
		}

		gameTimer.start();

		repaint();
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

	private class TimerListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			
			if(count % 2 == 0){
				cp1turn();
				if(cp2BoatsSunk == 5){
					gameTimer.stop();
					JOptionPane.showMessageDialog(null,"Game Over\nComputer Player 1 has won");
					System.exit(0);
				}
			}else{
				cp2turn();
				if(cp1BoatsSunk == 5){
					gameTimer.stop();
					JOptionPane.showMessageDialog(null,"Game Over\nComputer Player 2 has won");
					System.exit(0);
				}
			}
			count++;
		}
	}

	private class newGameListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			startGame();
		}
	}
}