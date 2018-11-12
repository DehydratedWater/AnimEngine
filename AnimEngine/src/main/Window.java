package main;
//Klasa głównego edytora, obsługuje myszkę i edytuje obraz oraz go wyświetla
//This is class of main editor, it can show and edit vector graphic on screen

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import bitmapEngine.textureManager;
import markManagerPac.MarkManager;
import openSave.ImageExport;
import openSave.Open;
import openSave.Save;
import openSave.SaveAs;
import openSave.SizeSetter;
import paintManager.PaintManager;
import paintManagerTools.LineSegmentTool;
import pastManager.PastBox;
import polygonEngine.FillManager;
import polygonEngine.FillMap;
import polygonEngine.PolygonShapeGenerator;
import rasterMarkManager.RasterMarkManager;
import rasterPaintManager.RasterPaintManager;
import renderSource.Object;
import structures.Animation;
import structures.Frame;
import structures.ParameterBox;
import toolBox.TextBox;
import toolBox.frameCupture;


public class Window extends Canvas implements ActionListener, MouseListener, MouseMotionListener, MouseWheelListener, KeyListener
{
	private static final long serialVersionUID = 1L;
	public static String VERSION = "AnimEngine BETA 1.3";
	private String title = VERSION;
	public Animation a;
	public JInternalFrame window;
	private JPanel panel;
	private PaintManager paintManagerNew;
	private FillManager fillManager;
	private MarkManager markManagerNew;
	private ParameterBox parameterBox;
	private Rectangle rec;
	private int AnimationResolutionX, AnimationResolutionY;
	private float zoomScale = 1;
	public static float Scale = 1;
	private BufferStrategy bufor;
	private float moveX = 0, moveY = 0;
	private int ScreenSizeX, ScreenSizeY;
	private long time;
	private int tool = 0, linetool = 0;
	private int frameRate = 60, wait = 0, fps = 0;
	private int mX = 0, mY = 0;
	private boolean mouseClicked,leftClicked,remove,CTRL,PLAY, M;
	
	
	public static float SizeX, SizeY;
	public static boolean ShowPoints;
	private boolean DataToUbdate;
	public boolean needToRevalidate = false;
	
	public boolean alwaysTopBoxBool = false; 
	public boolean timeLineBool = false;
	public boolean toolBoxBool = false;
	public boolean toolBox2Bool = false;
	public boolean timeLineBarBoxBool = false;
	public boolean stateChange = false;
	public boolean saveSet;
	private boolean loading;
	private Menu menu;
	private RasterPaintManager rasterPaintManager;
	private RasterMarkManager rasterMarkManager;
	public static int windowsWidth;
	public static int windowHeight;
	public static textureManager tm = new textureManager();
	
	/**
	 * Tworzy obiekt Window przyjmując rozdzielczość Animacji i ustnawia rozmiar okna na taki sam
	 * @param x
	 * @param y
	 */
	public Window(int x, int y)
	{
	a = new Animation(x,y);
	AnimationResolutionX = x;
	AnimationResolutionY = y;
	window = new JInternalFrame(title, true, false, true, false);
	window.setSize(x, y);
	window.setLocation(50, 30);
	SizeX = x;
	SizeY = y;
	window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	window.setVisible(true);
	window.add(this);
	
	Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	ScreenSizeX = d.width;
	ScreenSizeY = d.height;
	addListeners();
	createDrawingPanel();
	setUpAllManagers();

	}

	public void setUpMenuBar() {
		menu = new Menu();
		window.setJMenuBar(menu.createmenubarBar());
	}

	/**
	 * Tworzy obiekt Window przyjmując obiekt Animacji z którego pobiera rozdzielczość animacji i na taką ustala rozdzielczość okna
	 * @param anim
	 */
	public Window(Animation anim)
	{
	a = anim;
	AnimationResolutionX = anim.getX();
	AnimationResolutionY = anim.getY();
	window = new JInternalFrame(title, true, false, true, false);
	window.setSize(anim.getX(), anim.getY());
	window.setLocation(50, 30);
	SizeX = anim.getX();
	SizeY = anim.getY();
	setSizeY(anim.getY());
	window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	window.setVisible(true);

	Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	ScreenSizeX = d.width;
	ScreenSizeY = d.height;
	addListeners();
	createDrawingPanel();
	setUpAllManagers();
	}
	
	
	/**
	 * Rysuje aktualną klatkę w buforze klatek w zależności on aktualnego trybu
	 * albo do edycji 
	 * albo jako oddtwarzanie animacji
	 */
	public void paint()
	{
		if(!PLAY)
			PaintFrameWithEdit();
		else 
			PaintAnimation();		
	}
	
	/**
	 * Rysuje klatę w bufforze klatek jedynie wyświetlajac jej zawartość
	 */
	private void PaintAnimation()
	{
		refreshScale();
		time = System.currentTimeMillis();
		Graphics2D g = (Graphics2D) bufor.getDrawGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, ScreenSizeX, ScreenSizeY);
		if(frameCupture.hasFrame == false&&!frameCupture.isExported)
		{
			//System.out.println("Odświerzanie klatki");
			captureObiectToAnimation(a.getFrame());
			g.drawImage(frameCupture.memory_frame, 0, 0, null);
		}
		else
		{
			g.drawImage(frameCupture.memory_frame, 0, 0, null);
		}
		time = System.currentTimeMillis()-time;
		//System.out.println("Wygenerowano w "+time);

		wait = (int) ((1000/a.getFrameRate())-time);
		if(wait>=0) 
			fps = (int) a.getFrameRate();
		else
			fps = (int) (1000/((1000/a.getFrameRate())-wait));

		g.setColor(Color.BLACK);
		g.drawString("FPS: "+fps+" "+time+"ms", 10, 20);
	
		a.ChangeFrame();
		BasicStroke s = new BasicStroke(1f, BasicStroke.CAP_ROUND,  BasicStroke.JOIN_ROUND);
		g.setStroke(s);
		g.drawRect((int)(moveX), (int)(moveY), (int)((SizeX*Scale)), (int)((SizeY*Scale)));
		bufor.show();
		if(wait > 0)
		{
		try {
			//System.out.println("CZEKANIE "+wait);
			Thread.sleep(wait);//delta
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		
		
	}
	/**
	 * Rysuje klatkę w bufforze klatek wywołując również funkcje do edycji treści w klatce
	 */
	private void PaintFrameWithEdit()
	{
		//System.out.println("Rozpoczeto malowanie");
		refreshScale();
		time = System.currentTimeMillis();
		Graphics2D g = (Graphics2D) bufor.getDrawGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, ScreenSizeX, ScreenSizeY);
		BufferedImage memory_image = new BufferedImage(window.getWidth(), window.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = memory_image.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if(a.getFrame().gcoType()==0)
		{
			//System.out.println("Uruchomiono edycje wektorową");
			markTool(g2);
			paintTool(g2);
			fillTool(g2);
		}
		else
		{
			rasterMarkTool(g2);
			rasterPaintTool(g2);
			rasterFillTool(g2);
			//System.out.println("Uruchomiono edycje bitmapową");
		}
		if(frameCupture.hasFrame == false&&!frameCupture.isExported)
		{
			System.out.println("Odświerzanie klatki");
			captureObiectToEdit(a.getFrame());
			g.drawImage(frameCupture.memory_frame, 0, 0, null);
			System.out.println("Udało się narysować klatkę");
		}
		else
		{
			g.drawImage(frameCupture.memory_frame, 0, 0, null);
		}
		//System.out.println("Wyświetlono");
		g.drawImage(memory_image, 0, 0, null);
		//ubdateFrameShowingSetlings(g, a.getFrame());
		time = System.currentTimeMillis()-time;


		wait = (int) ((1000/frameRate)-time);
		if(wait>=0) 
			fps = frameRate;
		else
			fps = 1000/((1000/frameRate)-wait);

		g.setColor(Color.BLACK);
		g.drawString("FPS: "+fps, 10, 20);
		if(a.getFrame().gcoType()==0)
		{
			String ConnPoints = "P: "+a.getFrame().gco().getPointTabSize()+" C: "+a.getFrame().gco().getConnectionTab().size()+" F: "+a.getFrame().gco().getPolygonTab().size()+" S: "+PastBox.steps;
			g.drawString(ConnPoints, window.getWidth()-ConnPoints.length()*6.5f, 20);
		}
		TextBox.drawMessage(g, 10, window.getHeight()-70);
	//	System.out.println(moveX+" "+moveY+" "+((SizeX*Scale))+" "+((SizeY*Scale))+" s: "+Scale);
		
		BasicStroke s = new BasicStroke(1f, BasicStroke.CAP_ROUND,  BasicStroke.JOIN_ROUND);
		g.setStroke(s);
		g.setColor(Color.black);
		g.drawRect((int)(moveX), (int)(moveY), (int)((SizeX*Scale)), (int)((SizeY*Scale)));
		bufor.show();
		if(wait > 0)
		{
		try {
			
			Thread.sleep(wait);//delta
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		//System.out.println("Zakończono");
	}
	
	private void rasterPaintTool(Graphics2D g) {
		try
		{
		if(tool == 0)
		{
			RasterPaintManager.tool = 0;
			rasterPaintManager.useTool(a.getFrame(), mX, mY, mouseClicked, leftClicked, CTRL, M, remove, parameterBox, g);
		}
		else if(tool == 3)
		{
			RasterPaintManager.tool = 2;
			rasterPaintManager.useTool(a.getFrame(), mX, mY, mouseClicked, leftClicked, CTRL, M, remove, parameterBox, g);
		}
		else if(tool == 4)
		{
			RasterPaintManager.tool = 1;
			rasterPaintManager.useTool(a.getFrame(), mX, mY, mouseClicked, leftClicked, CTRL, M, remove, parameterBox, g);
		}
		else if(tool == 5)
		{
			RasterPaintManager.tool = 4;
			rasterPaintManager.useTool(a.getFrame(), mX, mY, mouseClicked, leftClicked, CTRL, M, remove, parameterBox, g);
		}
		else if(tool == 6)
		{
			RasterPaintManager.tool = 3;
			rasterPaintManager.useTool(a.getFrame(), mX, mY, mouseClicked, leftClicked, CTRL, M, remove, parameterBox, g);
		}
		}catch(Exception e)
		{
			rasterPaintManager = new RasterPaintManager();
			System.out.println("Wystąpił błąd RESETOWANIE NARZĘDZIA DO MALOWANIA");
			System.out.println(e);
		}
		
	}

	private void rasterFillTool(Graphics2D g2) {
		// TODO Auto-generated method stub
		
	}

	private void rasterMarkTool(Graphics2D g2) {
		try
		{
		if(tool == 1)
		{
			rasterMarkManager.useTool(a.getFrame(), mX, mY, mouseClicked, leftClicked, CTRL, remove, M, parameterBox, g2);
		}
		
		
		}catch(Exception e)
		{
			rasterMarkManager = new RasterMarkManager();
			System.out.println("Wystąpił błąd RESETOWANIE NARZĘDZIA DO ZAZNACZEŃ BETA");
			System.out.println(e);
		}
	}

	/**
	 * Wywołuje nrzędzia do zaznaczania z MarkManagera ustalajac wartość linetool na pobraną z ToolAssistanta-a
	 * @param g
	 */
	private void markTool(Graphics2D g)
	{
		try
		{
		if(tool == 1)
		{
			
			//System.out.println("TOOL to "+linetool);
				if(MarkManager.toUbdate)
				{
					MarkManager.toUbdate = false;
					setDataToUbdate(true);
				}
			markManagerNew.setLineTool(linetool);
			markManagerNew.useTool(a.getFrame(), mX, mY, mouseClicked, leftClicked, CTRL, remove, M, parameterBox, g);
		}
		
		
		}catch(Exception e)
		{
			markManagerNew = new markManagerPac.MarkManager();
			System.out.println("Wystąpił błąd RESETOWANIE NARZĘDZIA DO ZAZNACZEŃ BETA");
			System.out.println(e);
		}
		
	}
	/**
	 * Wywołuje narzędzia służące do wypełnień z fillManagera
	 * @param g
	 */
	private void fillTool(Graphics2D g) {
		if(tool == 2)
		{
			try
			{
			
			FillManager.tool = 0;
			fillManager.useTool(a.getFrame(), mX, mY, mouseClicked, leftClicked, parameterBox, g);
			
			
			}catch(Exception e)
			{
				fillManager = new FillManager();
				System.out.println("Wystąpił błąd RESETOWANIE NARZĘDZIA DO WYPEŁNIEŃ");
				System.out.println(e);
			}
		}
		
	}

	/**
	 * Wywołuje odpowiednie narzędzia rysujące w zależnosći od wybranego narzędzia
	 * @param g
	 */
	private void paintTool(Graphics2D g)
	{
		try
		{
		if(tool == 0)
		{
			PaintManager.tool = 0;
			paintManagerNew.useTool(a.getFrame(), mX, mY, mouseClicked, leftClicked, CTRL, M, remove, parameterBox, g);
		}
		else if(tool == 3)
		{
			PaintManager.tool = 2;
			paintManagerNew.useTool(a.getFrame(), mX, mY, mouseClicked, leftClicked, CTRL, M, remove, parameterBox, g);
		}
		else if(tool == 4)
		{
			PaintManager.tool = 1;
			paintManagerNew.useTool(a.getFrame(), mX, mY, mouseClicked, leftClicked, CTRL, M, remove, parameterBox, g);
		}
		else if(tool == 5)
		{
			PaintManager.tool = 4;
			paintManagerNew.useTool(a.getFrame(), mX, mY, mouseClicked, leftClicked, CTRL, M, remove, parameterBox, g);
		}
		else if(tool == 6)
		{
			PaintManager.tool = 3;
			paintManagerNew.useTool(a.getFrame(), mX, mY, mouseClicked, leftClicked, CTRL, M, remove, parameterBox, g);
		}
		}catch(Exception e)
		{
			paintManagerNew = new PaintManager();
			System.out.println("Wystąpił błąd RESETOWANIE NARZĘDZIA DO MALOWANIA");
			System.out.println(e);
		}
	}
	/**
	 * Ustawia predefiniowane ustawiania rysowania w bufforze w trybie edycji a następnie generuje klatkę f i zapisuje wygenerowana klatkę w frameCapture.memoryFrame
	 * @param f
	 */
	public void captureObiectToEdit(Frame f)
	{
		BufferedImage memory_image = new BufferedImage(window.getWidth(), window.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = memory_image.createGraphics();
		f.setDrawWithOutMark(false);
		f.setShowPoints(ShowPoints);
		f.setScale(Scale);
		f.setMoveXY(moveX, moveY);
		f.DrawFrame(g);
		frameCupture.memory_frame = memory_image;
		frameCupture.hasFrame = true;
	}
	/**
	 * Ustawia predefiniowane ustawiania rysowania w bufforze w trybie wyświetlania animacji a następnie generuje klatkę f i zapisuje wygenerowana klatkę w frameCapture.memoryFrame
	 * @param f
	 */
	public void captureObiectToAnimation(Frame f)
	{
		BufferedImage memory_image = new BufferedImage(window.getWidth(), window.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = memory_image.createGraphics();
		f.setDrawWithOutMark(true);
		f.setShowPoints(ShowPoints);
		f.setScale(Scale);
		f.setMoveXY(moveX, moveY);
		f.DrawFrame(g);
		frameCupture.memory_frame = memory_image;
		frameCupture.hasFrame = true;
	}
	/**
	 * Dodaje listenery do okna
	 */
	private void addListeners() {
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		addKeyListener(this);
	}

	/**
	 * Ustawia rysowanie w palnelu i strategię bufforowania
	 */
	private void createDrawingPanel() 
	{
		Container container = window.getContentPane();
		container.setLayout(new BorderLayout());
		panel = (JPanel) window.getContentPane();
		container.add(panel.add(this),BorderLayout.CENTER);
		
	}
	
	public void generateBufferStrategy()
	{
		createBufferStrategy(2);
		bufor = getBufferStrategy();
		requestFocus();
	}
	/**
	 * Tworzy obiekty wszysktich managerów
	 */
	private void setUpAllManagers() 
	{
		markManagerNew = new markManagerPac.MarkManager();
		fillManager = new FillManager();
		parameterBox = new ParameterBox();
		paintManagerNew = new PaintManager();
		rasterPaintManager = new RasterPaintManager();
		rasterMarkManager = new RasterMarkManager();
	}
	
	/**
	 * oblicza skalę okna wzgędem początkowej i ta skala jest później urzywana do skalowania wyświetlanego obrazu
	 */
	public void refreshScale()
	{
		windowsWidth = window.getWidth();
		windowHeight = window.getHeight();
		float ScaleX = window.getWidth()/SizeX;
		float ScaleY = window.getHeight()/SizeY;
		if(ScaleX>ScaleY)
		{
			Scale = ScaleY;
		}
		else
		{
			Scale = ScaleX;
		}
		Scale*=zoomScale;
//		System.out.println(ScaleX+" "+ScaleY);
	}
	
	public void actionPerformed(ActionEvent e) {}
	public void mouseClicked(MouseEvent e) {mX = e.getX();mY = e.getY();}
	public void mouseEntered(MouseEvent e) {mX = e.getX();mY = e.getY();}
	public void mouseExited(MouseEvent e) {mX = e.getX();mY = e.getY();}
	public void mousePressed(MouseEvent e) {
		mX = e.getX();
		mY = e.getY();
		int w = e.getButton();
		if(w==MouseEvent.BUTTON1)
		mouseClicked = true;
		else if(w==MouseEvent.BUTTON3)
		{
			leftClicked = true;
		}
	}
	public void mouseReleased(MouseEvent e) {
		mX = e.getX();
		mY = e.getY();
		int w = e.getButton();
		if(w==MouseEvent.BUTTON1)
		mouseClicked = false;
		else if(w==MouseEvent.BUTTON3)
		leftClicked = false;
	}
	public void mouseDragged(MouseEvent e) {mX = e.getX();mY = e.getY();}
	public void mouseMoved(MouseEvent e) {mX = e.getX();mY = e.getY();}
	public void mouseWheelMoved(MouseWheelEvent e) 
	{
	if(e.getWheelRotation()==-1)
	{	
		frameCupture.hasFrame = false;
		if(zoomScale<10)
		{

		zoomScale+=0.05f;
		moveY-=10f;
		moveX-=10f;
		}
	}
	else if(e.getWheelRotation()==1)
	{
		frameCupture.hasFrame = false;
		if(zoomScale>0.5)
		{
		
		zoomScale-=0.05f;
		moveY+=10f;
		moveX+=10f;
		}
	}
	}
	
	
	public void keyPressed(KeyEvent e) 
	{
		//System.out.println(e.getKeyCode());
		int w = e.getKeyCode();
		if(w == KeyEvent.VK_UP)
		{
			frameCupture.hasFrame = false;
			moveY+=10f;
		}
		else if(w == KeyEvent.VK_DOWN)
		{
			frameCupture.hasFrame = false;
			moveY-=10f;
		}
		else if(w == KeyEvent.VK_LEFT)
		{
			frameCupture.hasFrame = false;
			moveX+=10f;
		}
		else if(w == KeyEvent.VK_RIGHT)
		{
			frameCupture.hasFrame = false;
			moveX-=10f;
		}
		else if(w == KeyEvent.VK_R)
		{
			frameCupture.hasFrame = false;
			moveX=0;
			moveY=0;
			zoomScale = 1;
		}
		else if(w == KeyEvent.VK_P)
		{
			frameCupture.hasFrame = false;
			ShowPoints=!ShowPoints;
			menu.f1.setSelected(!menu.f1.isSelected());
			
		}
		else if(w == KeyEvent.VK_E)
		{
			System.out.println("Eksportowanie do PNG");
			ImageExport.exportToPNG(a);
			
		}
		else if(w == KeyEvent.VK_L)
		{
			polygonEngine.FillMap.showMark=!polygonEngine.FillMap.showMark;
			PolygonShapeGenerator.showMark=!PolygonShapeGenerator.showMark;
		}
		else if(w == KeyEvent.VK_M)
		{
			
			PolygonShapeGenerator.onlyMainMark=!PolygonShapeGenerator.onlyMainMark;
		}
		else if(w == KeyEvent.VK_Q)
		{
			
			LineSegmentTool.drawPoints = !LineSegmentTool.drawPoints;
		}
		else if(w == KeyEvent.VK_DELETE)
		{
			frameCupture.hasFrame = false;
			setRemove(true);
		}
		else if(w == KeyEvent.VK_CONTROL)
		{
			CTRL = true;
		}
		else if(w == KeyEvent.VK_O)
		{
			Save.SaveDataToFile(a.getFrameTab(), AnimationResolutionX, AnimationResolutionY);
		}
		else if(w == KeyEvent.VK_I)
		{
			frameCupture.hasFrame = false;
			if(loading==false)
			{
			double loadingTime = System.currentTimeMillis();
			System.out.println("Rozpoczęto otwieranie pliku");
			loading = true;
			Animation anim = Open.loadFile("ANIMACJA.txt");
			frameCupture.hasFrame = false;
			loading = false;
			System.out.println("Zakończono otwieranie pliku");
			if(anim != null)
			{
			a = anim;
			System.out.println();
			fillManager = new FillManager();
			parameterBox = new ParameterBox();
			markManagerNew = new markManagerPac.MarkManager();
			}
			System.out.println("Zakończono wczytywanie po "+((System.currentTimeMillis()-loadingTime)/1000)+"s");
			}
		}
		else if(w == KeyEvent.VK_U)
		{
			saveSet = true;
		}
		
		if(CTRL)
		{
		if(w == KeyEvent.VK_Z)
		{
			
			a = PastBox.reverseStep(a);
			frameCupture.hasFrame = false;
			System.out.println("Cofanie kroku");
			//a = timeBackspace.stepBack(a);
			
		}
		else if(w == KeyEvent.VK_X)
		{
			frameCupture.hasFrame = false;
			System.out.println("Dodawanie kroku");
		}
		else if(w == KeyEvent.VK_Y)
		{
			frameCupture.hasFrame = false;
			System.out.println("Cofanie cofniętego kroku kroku");
		}
		
		}
		else if(w == KeyEvent.VK_M)
		{
			M=true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int w = e.getKeyCode();
		if(w == KeyEvent.VK_DELETE)
		{
			setRemove(false);
		}
		else if(w == KeyEvent.VK_CONTROL)
		{
			CTRL = false;
		}
		else if(w == KeyEvent.VK_M)
		{
			M=false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@SuppressWarnings("unused")
	private void ubdateFrameShowingSetlings(Graphics2D g, Frame f)
	{
		f.setShowPoints(ShowPoints);
		f.setScale(Scale);
		f.setMoveXY(moveX, moveY);
		f.DrawFrame(g);
	}
	public int getTool() {return tool;}
	public void setTool(int tool) {	this.tool = tool;}
	public boolean isRemove() {	return remove;}
	public void setRemove(boolean remove) {	this.remove = remove;}
	public void setParameterBox(ParameterBox p)	{parameterBox = p;}
	public ParameterBox getParameterBox(){return parameterBox;}
	public int getLinetool() {return linetool;}
	public void setLinetool(int linetool) {this.linetool = linetool;}
	public boolean getDataToUbdate() {return DataToUbdate;}
	public void setDataToUbdate(boolean dataToUbdate) {DataToUbdate = dataToUbdate;}
	public JInternalFrame getOkno(){return window;}
	public boolean isPLAY() {return PLAY;}
	public void setPLAY(boolean pLAY) {	PLAY = pLAY;}
	public float getSizeX() {return SizeX;}
	public void setSizeX(float sizeX) {SizeX = sizeX;}
	public float getSizeY() {return SizeY;}
	public void setSizeY(float sizeY) {	SizeY = sizeY;}

	public Rectangle getRec() {
		return rec;
	}

	public void setRec(Rectangle rec) {
		this.rec = rec;
	}

	public class Menu implements ActionListener {

		

		JMenuBar menuBar;
		JMenu menu, submenu, windowMenu, windowSubmenu, exportMenu, exportSubmenu, advancedFunctions;
		JMenuItem menuItem, saveButton, openButton, saveAsButton, exportToPNGButton, resolutionSetterButton, saveWindowSet;
		JCheckBoxMenuItem alwaysTopBox, timeLineBox, toolBox, toolBox2, timeLineBarBox;
		JCheckBoxMenuItem f1, f2, f3, f4, f5, f6, f7, f8;

        public JMenuBar createmenubarBar() {
			
			menuBar = new JMenuBar();
			
			
			menu = new JMenu("Menu");
			
			menuBar.add(menu);
			
			saveButton = new JMenuItem("Save");
			saveButton.addActionListener(this);
			
			submenu = new JMenu("File");
			menu.add(submenu);
			
			saveButton.addActionListener(this);
			submenu.add(saveButton);
			

			saveAsButton = new JMenuItem("Save As..");
			saveAsButton.addActionListener(this);
			submenu.add(saveAsButton);
			submenu.addSeparator();
			
			
			openButton = new JMenuItem("Open");
			submenu.add(openButton);
			openButton.addActionListener(this);
	
			
			
			menu.addSeparator();
			
			windowMenu = new JMenu("Windows");
			menuBar.add(windowMenu);
			
			alwaysTopBox = new JCheckBoxMenuItem("Windows on Top");
			alwaysTopBox.addActionListener(this);
			windowMenu.add(alwaysTopBox);
			
			windowMenu.addSeparator();
			
			windowSubmenu = new JMenu("Set Invisible");
			windowMenu.add(windowSubmenu);
			
			timeLineBox = new JCheckBoxMenuItem("Timeline");
			timeLineBox.setSelected(true);
			timeLineBox.addActionListener(this);
			windowSubmenu.add(timeLineBox);
			
			windowSubmenu.addSeparator();
			

			timeLineBarBox = new JCheckBoxMenuItem("Timeline Box");
			timeLineBarBox.setSelected(true);
			timeLineBarBox.addActionListener(this);
			windowSubmenu.add(timeLineBarBox);
			
			windowSubmenu.addSeparator();
			
			

			toolBox = new JCheckBoxMenuItem("Draw Box");
			toolBox.setSelected(true);
			toolBox.addActionListener(this);
			windowSubmenu.add(toolBox);
			
		    windowSubmenu.addSeparator();
		    
		    toolBox2 = new JCheckBoxMenuItem("Tool Box");
		    toolBox2.setSelected(true);
			toolBox2.addActionListener(this);
			windowSubmenu.add(toolBox2);
			
		    windowSubmenu.addSeparator();
		    
		    windowMenu.addSeparator();
		    
		    resolutionSetterButton = new JMenuItem("Set Animation resolution");
		    windowMenu.add(resolutionSetterButton);
		    resolutionSetterButton.addActionListener(this);
		    
		    saveWindowSet = new JMenuItem("Save Windows set");
		    windowMenu.add(saveWindowSet);
		    saveWindowSet.addActionListener(this);
		    
		    exportMenu = new JMenu("Export");
		    menuBar.add(exportMenu);
		    exportSubmenu = new JMenu("Export frames to");
		    exportMenu.add(exportSubmenu);
		    exportToPNGButton = new JMenuItem("...PNG format");
		    exportSubmenu.add(exportToPNGButton);
		    exportToPNGButton.addActionListener(this);
			//TODO
			
		    advancedFunctions = new JMenu("Advanced");
		    menuBar.add(advancedFunctions);
		    f1 = new JCheckBoxMenuItem("ShowPoints");
			f1.addActionListener(this);
			advancedFunctions.add(f1);
			f2 = new JCheckBoxMenuItem("ShowLinesNum");
			f2.addActionListener(this);
			advancedFunctions.add(f2);
			f3 = new JCheckBoxMenuItem("AntialiasingPolygons");
			f3.setSelected(true);
			f3.addActionListener(this);
			advancedFunctions.add(f3);
			f4 = new JCheckBoxMenuItem("FillDiagnosticTool");
			f4.addActionListener(this);
			advancedFunctions.add(f4);
			f5 = new JCheckBoxMenuItem("DrawWithOutMark");
			f5.addActionListener(this);
			advancedFunctions.add(f5);
			f6 = new JCheckBoxMenuItem("DrawVerticalFillLines");
			f6.setSelected(true);
			f6.addActionListener(this);
			advancedFunctions.add(f6);
			f7 = new JCheckBoxMenuItem("AntialiasingLines");
			f7.setSelected(true);
			f7.addActionListener(this);
			advancedFunctions.add(f7);
			f8 = new JCheckBoxMenuItem("TransformMode");
			f8.setSelected(false);
			f8.addActionListener(this);
			advancedFunctions.add(f8);
			
			return menuBar;
	}
    	
		@Override
		public void actionPerformed(ActionEvent e) {
			java.lang.Object get =  e.getSource();
			needToRevalidate = true;
			if(get != null)
				stateChange = true;
			
			if(get == timeLineBox){
			 if(timeLineBool == false){
				 TextBox.addMessage("Ustawiono TimeLine na niewidoczny", 300);
			  timeLineBool = true;
			 }
			 else{
				 TextBox.addMessage("Ustawiono TimeLine na widoczny", 300);
			   timeLineBool = false;
			 }
			}
			
			if(get == toolBox){
				 if(toolBoxBool == false){
					 TextBox.addMessage("Ustawiono ToolAssistant na niewidoczny", 300);
				 toolBoxBool = true;
				 }
				 else{
			     toolBoxBool = false;
				 TextBox.addMessage("Ustawiono ToolAssistant na widoczny", 300);
				 }
				}
			
			if(get == timeLineBarBox){
				 if(timeLineBarBoxBool == false){
					 TextBox.addMessage("Ustawiono TimeLineAssistant na niewidoczny", 300);
				  timeLineBarBoxBool = true;
				 }
				 else{
					 TextBox.addMessage("Ustawiono TimeLineAssistant na widoczny", 300);
			      timeLineBarBoxBool = false;
				 }
				}
		
			
			if(get == toolBox2){
				if(toolBox2Bool == false){
					 TextBox.addMessage("Ustawiono ToolBar na niewidoczny", 300);
					toolBox2Bool = true;
				}
				else{
					 TextBox.addMessage("Ustawiono ToolBar na widoczny", 300);
					toolBox2Bool = false;
				}
			}
			
			
			if(get == alwaysTopBox){
				if(alwaysTopBoxBool == false){
					TextBox.addMessage("Ustawiono okno zawsze na wierzchu", 300);
					alwaysTopBoxBool = true;
				}
				else{
					TextBox.addMessage("Wyłączono ustawie okna jako zawsze na wierzchu", 300);
					alwaysTopBoxBool = false;
				}
		}

			if(get == saveButton){
				System.out.println("Zapisywanie");
				if(SaveAs.getIfBeenSaved()) {
					System.out.println("Zapisywanie Klasyczne");
					TextBox.addMessage("Zapisywanie: "+SaveAs.getPath(), 300);
				Save.SaveDataToFile( a.getFrameTab(), a.getX(), a.getY());
				SaveAs.setIfBeenSaved(true);
				}
				else {
					TextBox.addMessage("Zapisywanie jako: "+SaveAs.getPath(), 300);
					System.out.println("Zapisywanie jako");
					SaveAs.setIfBeenSaved(true);
					SaveAs.saveAs( a.getFrameTab(), a.getX(), a.getY());
				}
			}
			
			if(get == saveAsButton){
				System.out.println("Zapisywanie JAKO");
				TextBox.addMessage("Zapisywanie jako: "+SaveAs.getPath(), 300);
					System.out.println("Zapisywanie jako");
					SaveAs.setIfBeenSaved(true);
					SaveAs.saveAs( a.getFrameTab(), a.getX(), a.getY());
					window.setTitle("AnimEngine: "+SaveAs.getPath());
			}
			
			if(get == openButton){
				 if(loading==false)
					{
					 JFileChooser chooser = new JFileChooser();
					    chooser.setDialogTitle("Open File");
					    FileFilter filter = new FileNameExtensionFilter("AnimEngine animation files","ae");
						 chooser.addChoosableFileFilter(filter);
						 chooser.setAcceptAllFileFilterUsed(true);
						 TextBox.addMessage("Otwieranie pliku", 300);

					    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					      double loadingTime = System.currentTimeMillis();
							System.out.println("Rozpocz�to otwieranie pliku");
							loading = true;
							
							Animation anim = Open.loadFile(chooser.getSelectedFile().getPath());
							frameCupture.hasFrame = false;
							TextBox.addMessage("Otwieranie pliku "+SaveAs.getPath(), 300);
							window.setTitle("AnimEngine: "+SaveAs.getPath());
							loading = false;
							System.out.println("Zako�czono otwieranie pliku");
							
							if(anim != null)
							{
							a = anim;
					//System.out.println("ZA�ADOWANO PLIK TUTUTUTUTUTUTUTUTUUTUT");
					System.out.println();
					//markManager = new MarkManager();
					fillManager = new FillManager();
					//paintManager = new OldPaintManager();
					parameterBox = new ParameterBox();
					markManagerNew = new markManagerPac.MarkManager();
					}
					System.out.println("Zako�czono wczytywanie po "+((System.currentTimeMillis()-loadingTime)/1000)+"s");
					}
				
			}

			}
			
			if(get == resolutionSetterButton){
				SizeSetter.setAnimationResolution();
				TextBox.addMessage("Zmienianie parametrów okna na "+SizeSetter.getStartSizeX()+"x"+SizeSetter.getStartSizeY()+" fps: "+SizeSetter.getFrameRate(), 300);
				System.out.println("zmienianie parametrów okna na "+SizeSetter.getStartSizeX()+"x"+SizeSetter.getStartSizeY());
				a.setX(SizeSetter.getStartSizeX());
				a.setY(SizeSetter.getStartSizeY());
				a.setFrameRate(SizeSetter.getFrameRate());
				SizeX = SizeSetter.getStartSizeX();
				SizeY = SizeSetter.getStartSizeY();
			
			}
			
			if(get==saveWindowSet)
			{
				saveSet = true;
			}
			
			if(get == exportToPNGButton){
				if(SaveAs.getIfBeenSaved() == false){
					TextBox.addMessage("Export jako PNG", 300);
					Component frame = null;
					JOptionPane.showMessageDialog(frame,
						    "You have to save project before export",
						    "Save it",
						    JOptionPane.WARNING_MESSAGE);
				}
				else{
				Save.SaveDataToFile( a.getFrameTab(), a.getX(), a.getY());
				ImageExport.exportToPNG(a);
				}
             }
			//TODO
			if(f1.isSelected())
				Window.ShowPoints = true;
			else
				Window.ShowPoints = false;
			if(f2.isSelected())
				Object.drawConnNumber = true;
			else
				Object.drawConnNumber = false;
			if(f3.isSelected())
				Object.RenderPolygonsWithAntiAliasing = true;
			else
				Object.RenderPolygonsWithAntiAliasing = false;
			if(f4.isSelected())
				FillManager.diagnose = true;
			else
				FillManager.diagnose = false;
			if(f5.isSelected())
				Object.drawWithOutMark = true;
			else
				Object.drawWithOutMark = false;
			if(f6.isSelected())
				FillMap.showMark = true;
			else
				FillMap.showMark = false;
			if(f7.isSelected())
				Object.RenderLinesWithAntiAliasing = true;
			else
				Object.RenderLinesWithAntiAliasing = false;
			if(f8.isSelected())
				MarkManager.isTransformMode = true;
			else
				MarkManager.isTransformMode = false;
			if(get==f1||get==f2||get==f3||get==f4||get==f5||get==f6||get==f7)
				frameCupture.hasFrame = false;
	   }

	}

	
}
