package main;
//Ta klasa zawiera w sobie wszystkie przyciski s³u¿¹ce do zmiany narzêdzi
//This class keep all buttons responsible for changing tools

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;


public class ToolBar extends JInternalFrame implements ActionListener
{

	private static final long serialVersionUID = 1L;
	private JButton a1, a2, a3, a4, a5, a6, a7, a8;
	private int tool = 0;
	private boolean ok = true;
	private Rectangle rec;
	/**
	 * Konstruktor ustalaj¹cy podstawowe wartoœci
	 */
	public ToolBar() 
	{
		setClosable(false);
		setResizable(true);
		setMaximizable(false);
		setMaximizable(false);
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setSize(40, 400);
		setLocation(800, 30);
		setUpButtons();
		setVisible(true);
	}
	/**
	 * Tworzy obiekty przycisków i przypisuje im ikony z folderu img/
	 */
	private void setUpButtons() {
		a1 =  new JButton(new ImageIcon("img/line.png"));
		a1.setPreferredSize(new Dimension(32, 32));
		add(a1);
		a2 =  new JButton(new ImageIcon("img/toolBox.png"));
		a2.setPreferredSize(new Dimension(32, 32));
		add(a2);
		a3 =  new JButton(new ImageIcon("img/paint.png"));
		a3.setPreferredSize(new Dimension(32, 32));
		add(a3);
		a4 =  new JButton(new ImageIcon("img/pen.png"));
		a4.setPreferredSize(new Dimension(32, 32));
		add(a4);
		a5 =  new JButton(new ImageIcon("img/cuv.png"));
		a5.setPreferredSize(new Dimension(32, 32));
		add(a5);
		a6 =  new JButton(new ImageIcon("img/rect.png"));
		a6.setPreferredSize(new Dimension(32, 32));
		add(a6);
		a7 =  new JButton("7");
		a7.setPreferredSize(new Dimension(32, 32));
		add(a7);
		a8 =  new JButton("8");
		a8.setPreferredSize(new Dimension(32, 32));
		add(a8);
		a1.addActionListener(this);
		a2.addActionListener(this);
		a3.addActionListener(this);
		a4.addActionListener(this);
		a5.addActionListener(this);
		a6.addActionListener(this);
		a7.addActionListener(this);
		a8.addActionListener(this);
	}
	public boolean isOk() {
		return ok;
	}
	public void setOk(boolean ok) {
		this.ok = ok;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		Object wej = e.getSource();
		if(wej == a1)
			setTool(0);
		else if(wej == a2)
			setTool(1);
		else if(wej == a3)
			setTool(2);
		else if(wej == a4)
			setTool(3);
		else if(wej == a5)
			setTool(4);
		else if(wej == a6)
			setTool(5);
		else if(wej == a7)
			setTool(6);
		else if(wej == a8)
			setTool(7);
	}
	public int getTool() {
		return tool;
	}
	public void setTool(int tool) {
		this.tool = tool;
	}
	//Tym mo¿esz zmieniæ widzialnoœæ okna analogine funkcje s¹ w pozosta³ych przybornikach 
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
