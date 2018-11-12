package main;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import structures.Animation;

public class TimeLine extends Canvas implements MouseListener, KeyListener
{
	private static final long serialVersionUID = 1L;
	private JPanel panel;
	public JInternalFrame window;
	private BufferStrategy bufor;
	private int currentFrame = 0;
	private int currentLayer = 0;
	private int bigestObjectIndex = 0;
	private Rectangle rec;
	/**
	 * Tworzy nowy TimeLine i ustawia jego pozycje na domyœlne, 
	 * Ustawia równie¿ obiekt Graphics2D do rysowania w oknie
	 */
	public TimeLine() 
	{
		window = new JInternalFrame("TimeLine", true, false, true);
		window.setLocation(6, 517);
		window.setSize(748, 297);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		window.add(this);
		Container container = window.getContentPane();
		container.setLayout(new BorderLayout());
		panel = (JPanel) window.getContentPane();
		addKeyListener(this);
		
		container.add(panel.add(this),BorderLayout.CENTER);
		
	}
	
	public void generateBufferStrategy()
	{
		createBufferStrategy(2);
		
		bufor = getBufferStrategy();
		requestFocus();
	}
	/**
	 * Odœwierza obraz wyœwietlany w oknie na zgadzajacy siê z aktualnym
	 * @param a
	 */
	public void paint(Animation a)
	{
		Graphics2D g = (Graphics2D) bufor.getDrawGraphics();	
		//g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		drawFrameTab(a, currentFrame, currentLayer, window.getWidth(), window.getHeight(), g);
		drawScrolls(window.getWidth(), window.getHeight(), g,a);
		bufor.show();
	}
	
	/**
	 * Ustawia widocznoœæ okna TimeLine
	 * @param state
	 */
	public void setWindowVisible(boolean state)
	{
		setVisible(state);
	}
	
	/**
	 * Rysuje tabele klatek dostosowan¹ do rozmiaru wyœwietlanego okna
	 * @param a -> obiekt animacji którego strukturê wiualizuje time line
	 * @param startFrame -> klatka od której ma siê zacz¹æ rysowanie od lewej
	 * @param startLayer -> warstwa która jest aktualnie na samej górze od góry
	 * @param width -> szerokoœæ okna
	 * @param height -> wyskokoœæ okna
	 * @param g -> obiekt Graphisc2D
	 */
	private void drawFrameTab(Animation a, int startFrame,int startLayer, int width, int height, Graphics2D g)
	{
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		
		int inX = 0;
		int inY = 30;
		int sizX = 20;
		int sizY = 35;
		generateFrameVisualRepresention(a, startFrame, width, height, g, inX,
				inY, sizX, sizY);
		generateFrameNumbersIndex(startFrame, width, g, sizX);
	}

	/**
	 * Rysuje wizualizacje klatek jako prostok¹tów
	 * @param a
	 * @param startFrame
	 * @param width
	 * @param height
	 * @param g
	 * @param inX
	 * @param inY
	 * @param sizX
	 * @param sizY
	 */
	private void generateFrameVisualRepresention(Animation a, int startFrame,
			int width, int height, Graphics2D g, int inX, int inY, int sizX,
			int sizY) {
		g.setColor(Color.BLACK);
		for(int j = 0; j < height/sizY; j++)
		{
			
			for(int i = 0; i < width/sizX; i++)
			{
				
				g.drawRect(inX, inY, sizX, sizY);
				
				g.drawRect(inX+5, inY+10, sizX-10, sizY-17);
				g.setColor(Color.black);
				
				if(i+startFrame<a.getFrameTabSize())
				{
					if(a.getCurrentFrame()==i)
					{
						g.setColor(Color.gray);
						g.fillRect(inX, inY, sizX, sizY);
						g.setColor(Color.black);
						g.drawRect(inX, inY, sizX, sizY);
					}
					if(j+currentLayer<a.getFrameNum(i+startFrame).getObjTabSize())
					{
					if(j+currentLayer>bigestObjectIndex)
						bigestObjectIndex = j+currentLayer;
					g.fillRect(inX+5, inY+10, sizX-10, sizY-17);
					}
				}
				inX+=sizX;
			}
			inY+=sizY;
			inX = 0;
		}
	}
	/**
	 * Rysuje opisy indeksów klatek
	 * @param startFrame
	 * @param width
	 * @param g
	 * @param sizX
	 */
	private void generateFrameNumbersIndex(int startFrame, int width, Graphics2D g, int sizX) {
		int inX = 0;
		for(int i = 0; i < width/sizX; i++)
		{
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(inX, 0, 20, 30);
		g.setColor(Color.black);
		g.drawRect(inX, 0, 20, 30);
		Font font = new Font ("SansSerif", Font.BOLD, 11);
		g.setFont(font);
		if((i+startFrame)<1000)
		{
			g.drawString(""+(i+startFrame), inX+2, 15);
		}
		else
		{
			int w = (i+startFrame);
			if(w<10000)
			{
				int t = w%10;
				w=w-t;
				g.drawString(w+"", inX+2, 15);
				g.drawString(t+"", inX+2, 25);
			}
			else if(w<100000)
			{
				int t = w%100;
				w=w-t;
				g.drawString(w+"", inX+2, 15);
				g.drawString(t+"", inX+2, 25);
			}
			else if(w<1000000)
			{
				int t = w%1000;
				w=w-t;
				g.drawString(w+"", inX+2, 15);
				g.drawString(t+"", inX+2, 25);
			}
		}
		inX+=sizX;
		}
	}
	/**
	 * Rysuje scrole na dole i po prawej stronie okna
	 * @param width
	 * @param height
	 * @param g
	 * @param a
	 */
	private void drawScrolls(int width, int height, Graphics2D g,Animation a)
	{
		
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, height-60, width, height);	
		g.fillRect(width-40, 0, width, height);
		g.setColor(Color.black);
		g.fillRect(width-40, height-60, width, height);
		g.setColor(Color.DARK_GRAY);
		if(a.getFrameTabSize()>currentFrame)
		{
			float prop = (width-80)/a.getFrameTabSize();
			g.fillRect((int)(prop*currentFrame), height-60, 40, height);
		}
		else
		{
			g.fillRect((int)(width-80), height-60, 40, height);
		}
		
		if(bigestObjectIndex>=currentLayer)
		{
			float prop = (height-100)/(bigestObjectIndex+1);
			g.fillRect(width-40, (int)(prop*currentLayer), width, 40);
		}
		else
		{
			g.fillRect(width-40, (int)(height-100), width, 40);
		}
	}

	public int getCurrentFrame() {
		return currentFrame;
	}

	public void setCurrentFrame(int currentFrame) {
		this.currentFrame = currentFrame;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_LEFT)
		{
			System.out.println("Przycisk lewo");
			currentFrame--;
			if(currentFrame<0)
			{
				currentFrame=0;
			}
		}
		else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			System.out.println("Przycisk prawo");
			currentFrame++;
		}
		else if(e.getKeyCode() == KeyEvent.VK_UP)
		{
			System.out.println("Przycisk prawo");
			currentLayer--;
			if(currentLayer<0)
			{
				currentLayer=0;
			}
		}
		else if(e.getKeyCode() == KeyEvent.VK_DOWN)
		{
			System.out.println("Przycisk prawo");
			currentLayer++;
		}
		else if(e.getKeyCode() == KeyEvent.VK_R)
		{
			currentLayer=0;
			currentFrame=0;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public Rectangle getRec() {
		return rec;
	}

	public void setRec(Rectangle rec) {
		this.rec = rec;
	}
}
