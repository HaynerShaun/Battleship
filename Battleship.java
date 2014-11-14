import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Random;

public class Battleship extends JFrame{

	private int FRAME_WIDTH = 300;
	private int FRAME_HEIGHT = 300;
	
	private JLabel mode = new JLabel("Select game mode:");

	private JButton pvp = new JButton("Player vs. Player");
	private JButton pvc = new JButton("Player vs. Computer");
	private JButton cvc = new JButton("Computer vs. Computer");
	
	public Battleship(){
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setLayout(null);
		super.setTitle("Battle Ship");
		
		mode.setSize(110, 20);
		mode.setLocation(FRAME_WIDTH / 2 - mode.getWidth() / 2, 20);
		
		pvp.setSize(200, 20);
		pvp.setLocation(FRAME_WIDTH / 2 - pvp.getWidth() / 2, mode.getY() + mode.getHeight() + 50);
		
		pvc.setSize(200, 20);
		pvc.setLocation(FRAME_WIDTH / 2 - pvc.getWidth() / 2, pvp.getY() + pvp.getHeight() + 50);
		
		cvc.setSize(200, 20);
		cvc.setLocation(FRAME_WIDTH / 2 - cvc.getWidth() / 2, pvc.getY() + pvc.getHeight() + 50);

		pvp.addActionListener(new buttonListener());
		pvc.addActionListener(new buttonListener());
		cvc.addActionListener(new buttonListener());
		
		super.add(mode);
		super.add(pvp);
		super.add(pvc);
		super.add(cvc);

		super.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		super.setResizable(false);
		super.setVisible(true);
		super.setLocationRelativeTo(null);
	}
	
	private void close(){
		super.setVisible(false);
	}
	
	private class buttonListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if(e.getSource() == pvp){
				playerVplayer pvp = new playerVplayer();
			}
			if(e.getSource() == pvc){
				playerVcomputer pvc = new playerVcomputer();
			}
			if(e.getSource() == cvc){
				computerVcomputer cvc = new computerVcomputer();
			}
			close();
		}
	}
}