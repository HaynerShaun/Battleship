import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class Battleship extends JFrame{
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

	private board1Ship p1ShipHandler = new board1Ship();
	private board1Attack p2AttackHandler = new board1Attack();
	private board2Ship p2ShipHandler = new board2Ship();
	private board2Attack p1AttackHandler = new board2Attack();

	private Ship[] ships;

	private int player1shotCount;
	private int player2shotCount;
	private int player1sunkBoats;
	private int player2sunkBoats;
	private int count = 0;

	public Battleship(){
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

	private class board1Ship implements MouseListener, MouseMotionListener{
		public void mouseClicked(MouseEvent e){
			if(e.getButton() == 3){
				for(int x = 0; x < ships.length; x++){
					if(ships[x].checkBounds(e.getX(), e.getY()) && ships[x].getPlayer() == 1)
					{
						ships[x].changeOrientation();
						ships[x].setSize(ships[x].returnWidth(), ships[x].returnHeight());
					}
				}
			}
			repaint();
			e.consume();
		}
		public void mousePressed(MouseEvent e){e.consume();}
		public void mouseReleased(MouseEvent e){e.consume();}
		public void mouseEntered(MouseEvent e){e.consume();}
		public void mouseExited(MouseEvent e){e.consume();}

		public void mouseMoved(MouseEvent e){e.consume();}

		public void mouseDragged(MouseEvent e){
			System.out.println("Mouse is dragged to Location:"+e.getX()+","+e.getY());
			e.consume();
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

	private class board2Ship implements MouseListener{
		public void mouseClicked(MouseEvent e){
			if(e.getButton() == 3){
				for(int x = 0; x < ships.length; x++){
					if(ships[x].checkBounds(e.getX(), e.getY()) && ships[x].getPlayer() == 2)
					{
						ships[x].changeOrientation();
						ships[x].setSize(ships[x].returnWidth(), ships[x].returnHeight());
					}
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
				boardOne.addMouseListener(p1ShipHandler);
				doneTurn.setVisible(false);
				doneTurn.setText("Done Placing Ships");
				player1setup();
			}else if (count  == 1){
				boardOne.removeMouseListener(p1ShipHandler);
				boardTwo.addMouseListener(p2ShipHandler);
				doneTurn.setVisible(false);
				player2setup();
			}
			else if (count % 2 == 0 ){
				boardTwo.removeMouseListener(p2ShipHandler);
				boardTwo.addMouseListener(p1AttackHandler);
				doneTurn.setVisible(false);
				doneTurn.setText("Done Turn");
				player1turn();
			}
			else if (count % 2 == 1){
				boardOne.addMouseListener(p2AttackHandler);
				doneTurn.setVisible(false);
				player2turn();
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
		ships[0] = new Ship(0, "aircraftCarrier", 'H', 1, aircraftCarrierH.getIconWidth(), aircraftCarrierH.getIconHeight(), 50, 50, 5);
		ships[1] = new Ship(1, "battleship", 'H', 1, battleshipH.getIconWidth(), battleshipH.getIconHeight(), 150, 150, 4);
		ships[2] = new Ship(2, "destroyer", 'H', 1, destroyerH.getIconWidth(), destroyerH.getIconHeight(), 250, 250, 3);
		ships[3] = new Ship(3, "submarine", 'H', 1, submarineH.getIconWidth(), submarineH.getIconHeight(), 350, 350, 3);
		ships[4] = new Ship(4, "patrolBoat", 'H', 1, patrolBoatH.getIconWidth(), patrolBoatH.getIconHeight(), 450, 450, 2);
		ships[5] = new Ship(5, "aircraftCarrier", 'H', 2, aircraftCarrierH.getIconWidth(), aircraftCarrierH.getIconHeight(), 50, 50, 5);
		ships[6] = new Ship(6, "battleship", 'H', 2, battleshipH.getIconWidth(), battleshipH.getIconHeight(), 150, 150, 4);
		ships[7] = new Ship(7, "destroyer", 'H', 2, destroyerH.getIconWidth(), destroyerH.getIconHeight(), 250, 250, 3);
		ships[8] = new Ship(8, "submarine", 'H', 2, submarineH.getIconWidth(), submarineH.getIconHeight(), 350, 350, 3);
		ships[9] = new Ship(9, "patrolBoat", 'H', 2, patrolBoatH.getIconWidth(), patrolBoatH.getIconHeight(), 450, 450, 2);

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

		//gameTimer.start();

		repaint();
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
}