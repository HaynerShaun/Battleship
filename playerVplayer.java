import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Random;

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

	public playerVplayer(){
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

	private class newGameListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			startGame();
		}
	}

	private class board1Attack implements MouseListener{
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
					player2shots.get(player2shotCount).setLocation(player2shots.get(player2shotCount).returnXCord(),player2shots.get(player2shotCount).returnYCord());
					player2shots.get(player2shotCount).setSize(player2shots.get(player2shotCount).returnWidth(),player2shots.get(player2shotCount).returnHeight());
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
					player2shots.get(player2shotCount).setIcon(player2shots.get(player2shotCount).getPic());

					player2shotCount++;
				}
			}
			boardOne.removeMouseListener(p2AttackHandler);
			repaint();
			e.consume();
		}
		public void mousePressed(MouseEvent e){e.consume();}
		public void mouseReleased(MouseEvent e){e.consume();}
		public void mouseEntered(MouseEvent e){e.consume();}
		public void mouseExited(MouseEvent e){e.consume();}
	}
	
	private class board2Attack implements MouseListener{
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
					player1shots.get(player1shotCount).setLocation(player1shots.get(player1shotCount).returnXCord(),player1shots.get(player1shotCount).returnYCord());
					player1shots.get(player1shotCount).setSize(player1shots.get(player1shotCount).returnWidth(),player1shots.get(player1shotCount).returnHeight());
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
					player1shots.get(player1shotCount).setIcon(player1shots.get(player1shotCount).getPic());

					player1shotCount++;
				}
			}
			boardTwo.removeMouseListener(p1AttackHandler);
			repaint();
			e.consume();
		}
		public void mousePressed(MouseEvent e){e.consume();}
		public void mouseReleased(MouseEvent e){e.consume();}
		public void mouseEntered(MouseEvent e){e.consume();}
		public void mouseExited(MouseEvent e){e.consume();}
	}

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
			ships[x].setLocation(ships[x].returnXCord(), ships[x].returnYCord());
			ships[x].setSize(ships[x].returnWidth(), ships[x].returnHeight());
			if(ships[x].getPlayer() == 1){
				boardOne.add(ships[x]);
			}
			else{
				boardTwo.add(ships[x]);
			}
			ships[x].setVisible(false);
		}

		repaint();
	}

	private boolean placeShip(int num, int xCord, int yCord){
		boolean placed = true;
		int x;
		ships[num] = new Ship(num, shipNames[num], 'H', shipPlayer[num], shipWidth[num], shipHeight[num], xCord, yCord, shipSize[num]);

		if(xCord + ships[num].returnWidth() < boardOne.getWidth() && yCord + ships[num].returnHeight() < boardOne.getHeight()){
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
		buttonArea.requestFocus();
	}

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
		buttonArea.requestFocus();
	}

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
	}

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
	}

	private void changeOrientation(int ship, MouseEvent e){
		boolean intersects = false;
		ships[ship].changeOrientation();
		ships[ship].setSize(ships[ship].returnWidth(), ships[ship].returnHeight());

		if(ships[ship].returnXCord() + ships[ship].returnWidth() > boardOne.getWidth() || ships[ship].returnYCord() + ships[ship].returnHeight() > boardTwo.getHeight()){
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
			ships[ship].setSize(ships[ship].returnWidth(), ships[ship].returnHeight());
		}
		repaint();
	}

	private class ship0Listener implements MouseListener, MouseMotionListener{
		boolean check = false;
		int c1=0;
		int c2=0;
		public void mouseClicked(MouseEvent e){
			int ship = 0;

			if(e.getButton() == 3){
				changeOrientation(ship, e);
			}

			e.consume();
		}
		public void mousePressed(MouseEvent e){e.consume();}
		public void mouseReleased(MouseEvent e){
			check = false;
			e.consume();
		}
		public void mouseEntered(MouseEvent e){e.consume();}
		public void mouseExited(MouseEvent e){e.consume();}
		public void mouseDragged(MouseEvent e){ 

			System.out.println("Made it into mouseDragged");
			Component C=e.getComponent();
			System.out.println(C);

			if(check==false){check=true;Point p=new  
					Point(e.getPoint());c1=p.x;c2=p.y;}

			Point z=new Point(e.getPoint());
			Point q=new Point(C.getLocation());  

			C.setBounds(q.x+(z.x-c1),q.y+(z.y-c2),C.getSize().  
					width,C.getSize().height);  
		} 
		public void mouseMoved(MouseEvent e)
		{
		}
	}

	private class ship1Listener implements MouseListener{
		public void mouseClicked(MouseEvent e){
			int ship = 1;

			if(e.getButton() == 3){
				changeOrientation(ship, e);
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
			int ship = 2;

			if(e.getButton() == 3){
				changeOrientation(ship, e);
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
			int ship = 3;

			if(e.getButton() == 3){
				changeOrientation(ship, e);
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
			int ship = 4;

			if(e.getButton() == 3){
				changeOrientation(ship, e);
			}

			e.consume();
		}
		public void mousePressed(MouseEvent e){e.consume();}
		public void mouseReleased(MouseEvent e){e.consume();}
		public void mouseEntered(MouseEvent e){e.consume();}
		public void mouseExited(MouseEvent e){e.consume();}
	}

	private class ship5Listener implements MouseListener{
		public void mouseClicked(MouseEvent e){
			int ship = 5;

			if(e.getButton() == 3){
				changeOrientation(ship, e);
			}

			e.consume();
		}
		public void mousePressed(MouseEvent e){e.consume();}
		public void mouseReleased(MouseEvent e){e.consume();}
		public void mouseEntered(MouseEvent e){e.consume();}
		public void mouseExited(MouseEvent e){e.consume();}
	}

	private class ship6Listener implements MouseListener{
		public void mouseClicked(MouseEvent e){
			int ship = 6;

			if(e.getButton() == 3){
				changeOrientation(ship, e);
			}

			e.consume();
		}
		public void mousePressed(MouseEvent e){e.consume();}
		public void mouseReleased(MouseEvent e){e.consume();}
		public void mouseEntered(MouseEvent e){e.consume();}
		public void mouseExited(MouseEvent e){e.consume();}
	}

	private class ship7Listener implements MouseListener{
		public void mouseClicked(MouseEvent e){
			int ship = 7;

			if(e.getButton() == 3){
				changeOrientation(ship, e);
			}

			e.consume();
		}
		public void mousePressed(MouseEvent e){e.consume();}
		public void mouseReleased(MouseEvent e){e.consume();}
		public void mouseEntered(MouseEvent e){e.consume();}
		public void mouseExited(MouseEvent e){e.consume();}
	}

	private class ship8Listener implements MouseListener{
		public void mouseClicked(MouseEvent e){
			int ship = 8;

			if(e.getButton() == 3){
				changeOrientation(ship, e);
			}

			e.consume();
		}
		public void mousePressed(MouseEvent e){e.consume();}
		public void mouseReleased(MouseEvent e){e.consume();}
		public void mouseEntered(MouseEvent e){e.consume();}
		public void mouseExited(MouseEvent e){e.consume();}
	}

	private class ship9Listener implements MouseListener{
		public void mouseClicked(MouseEvent e){
			int ship = 9;

			if(e.getButton() == 3){
				changeOrientation(ship, e);
			}

			e.consume();
		}
		public void mousePressed(MouseEvent e){e.consume();}
		public void mouseReleased(MouseEvent e){e.consume();}
		public void mouseEntered(MouseEvent e){e.consume();}
		public void mouseExited(MouseEvent e){e.consume();}
	}
}