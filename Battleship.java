import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.lang.Object;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.*;

public class Battleship extends JFrame{
	private JPanel player1Board = new JPanel();
	private JPanel player2Board = new JPanel();
	private JPanel player1Start = new JPanel();
	private JPanel player2Start = new JPanel();

	private JButton[][] player1Squares = new JButton[20][20];
	private JButton[][] player2Squares = new JButton[20][20];
	                                                    
	private final int FRAME_WIDTH = 1206;
	private final int FRAME_HEIGHT = 628;

	private ImageIcon water = new ImageIcon("water.png");
	private ImageIcon hit = new ImageIcon("hit.png");
	private ImageIcon ship = new ImageIcon("ship.png");
	private ImageIcon miss = new ImageIcon("miss.png");
	
	private Timer gameTimer;

	public Battleship(){
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setLayout(null);
		
		player1Board.setLayout(null);
		player2Board.setLayout(null);
		
		player1Board.setSize(600,600);
		player2Board.setSize(600,600);
		
		player1Board.setLocation(0,0);
		player2Board.setLocation(player1Board.getWidth(),0);
		
		gameTimer = new Timer(100, new TimerListener());
		
		super.add(player1Board);
		super.add(player2Board);
		player1Board.setBackground(Color.black);
		
		for(int x = 0; x < player1Squares.length; x++){
			for(int y = 0; y < player1Squares[x].length; y++){
				if(x < 19 && y < 19){
				player1Board.add(player1Squares[x][y] = new JButton(water));
				player2Board.add(player2Squares[x][y] = new JButton(water));
				}else{
					player1Board.add(player1Squares[x][y] = new JButton(hit));
					player2Board.add(player2Squares[x][y] = new JButton(hit));
				}

				player1Squares[x][y].setSize(30,30);
				player2Squares[x][y].setSize(30,30);
				player1Squares[x][y].setLocation(x * 30, y * 30);
				player2Squares[x][y].setLocation(x * 30, y * 30);
			}
		}
		super.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		super.setResizable(false);
		super.setVisible(true);
		super.setLocationRelativeTo(null);
		//startGame();
	}
	
	private void startGame(){
		player1setup();
		player2setup();
	}
	
	private void player1setup(){
		player2Board.setVisible(false);
		super.add(player1Start);
		
		super.remove(player1Start);
		player2Board.setVisible(true);
	}
	
	private void player2setup(){
		player1Board.setVisible(false);
		super.add(player2Start);
		
		super.remove(player2Start);
		player1Board.setVisible(true);
	}
	
	private class TimerListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			startGame();
		}
	}
}