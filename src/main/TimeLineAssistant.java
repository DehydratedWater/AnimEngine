package main;
//To jest klasa która tworzy oœ czasu do edycji poszczególnych klatek filmu, edycji, usuwania, kopiowania klatek filmu
//This class is responsible for creating Timeline of movie with all option to edit, remove, copy this fames

//import java.awt.Canvas;
//import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;



public class TimeLineAssistant extends JInternalFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private JButton left, right, rightCopyLastFrame, start, end, play, del;
	JLabel frameInfo;
	boolean leftButton, rightButton, rightCopyLastFrameButton, startButton, endButton, playButton, delButton;
	int currentFrame = 1;
	int frameTabSize = 1;
	private Rectangle rec;
	/**
	 * Podstwowy konstruktor który nie robi nic poza ustawieniem podstawowych wartoœci
	 */
	public TimeLineAssistant() 
	{
		setClosable(false);
		setMaximizable(true);
		setMaximizable(false);
		setResizable(true);
		setLayout(new GridLayout());
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setSize(550, 70);
		setLocation(50, 560);
		setUpButtons();
		setVisible(true);
	}
	
	/**
	 * Ustawia poczatkowe nazwy wszystkich przycisków
	 */
	private void setUpButtons()
	{
		left = new JButton(" <--- ");
		start = new JButton(" << ");
		end = new JButton(" >> ");
		right = new JButton(" ---> ");
		rightCopyLastFrame = new JButton(" c--> ");
		play = new JButton("PLAY");
		del = new JButton("DEL");
		frameInfo = new JLabel("      "+(currentFrame+1)+" / "+(frameTabSize+1));
		left.addActionListener(this);
		start.addActionListener(this);
		end.addActionListener(this);
		right.addActionListener(this);
		rightCopyLastFrame.addActionListener(this);
		play.addActionListener(this);
		del.addActionListener(this);
		add(left);
		add(right);
		add(rightCopyLastFrame);
		add(start);
		add(end);
		add(play);
		add(del);
		add(frameInfo);
	}
	/**
	 * Aktualizuje opis aktualnej klatki wyœwietlany w oknie jako napis
	 */
	public void actualizeCurrentFrameNumber()
	{
		frameInfo.setText("      "+(currentFrame+1)+" / "+(frameTabSize+1));
	}
	public void actionPerformed(ActionEvent e) 
	{
		Object wej = e.getSource();
		
		if(wej == left)
			leftButton = true;
		else if(wej == right)
			rightButton = true;
		else if(wej == rightCopyLastFrame)
			rightCopyLastFrameButton = true;
		else if(wej == start)
			startButton = true;
		else if(wej == end)
			endButton = true;
		else if(wej == del)
			delButton = true;
		else if(wej == play)
		{	
			if(playButton)
			{
				setAllButtonsEnnable();
			}
			else
			{
				setAllButtonsNotEnnable();
			}
			playButton = !playButton;
		}
	}
	
	
	private void setAllButtonsEnnable()
	{
		left.setEnabled(true);
		right.setEnabled(true);
		rightCopyLastFrame.setEnabled(true);
		end.setEnabled(true);
		start.setEnabled(true);
		del.setEnabled(true);
	}
	
	private void setAllButtonsNotEnnable()
	{
		left.setEnabled(false);
		right.setEnabled(false);
		rightCopyLastFrame.setEnabled(false);
		end.setEnabled(false);
		start.setEnabled(false);
		del.setEnabled(false);
	}
	/**
	 * Ustawia wszystkie pszyciski na false
	 */
	public void resetButtonState()
	{
		rightButton = false;
		rightCopyLastFrameButton = false;
		leftButton = false;
		endButton = false;
		startButton = false;
		delButton = false;
	}
	
	public void setWindowVisible(boolean state)
	{
		setVisible(state);
	}

	public Rectangle getRec() {
		return rec;
	}

	public void setRec(Rectangle rec) {
		this.rec = rec;
	}
}
