package main;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;

import openSave.SizeSetter;
import toolBox.TextBox;
import toolBox.frameCupture;

//Tak klasa trzyma w pami�ci wszystkier elementy interfejsu i komunikuje je wzajemnie
//This class keep all interface element like(ToolBox, PaintArea, ...) and communicate it together


public class AnimEngine 
{
	static JFrame mainFrame;
	static JDesktopPane jdp;
	public static float sizeX, sizeY, lastX, lastY;
	public static void main(String args[])
	{
		sizeX = 1780;
		sizeY = 980;
		lastX = sizeX;
		lastY = sizeY;
		mainFrame = new JFrame("AnimEngine 1.3 BETA");
		mainFrame.setSize((int)sizeX, (int)sizeY);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jdp = new JDesktopPane();
		jdp.setVisible(true);

		
		TimeLine TimeLine = new TimeLine();
		
		TimeLineAssistant TimeLineAssistant = new TimeLineAssistant();
		ToolBar ToolBar = new ToolBar();
		ToolAssistant ToolAssistant = new ToolAssistant();
		Window Window =  new Window(SizeSetter.getStartSizeX(), SizeSetter.getStartSizeY());
		LayerManager layerManager = new LayerManager();
		
		setWindowsFromFile(new File("WindowSet.txt"), Window, TimeLineAssistant, ToolBar, ToolAssistant, TimeLine, layerManager, mainFrame.getWidth(), mainFrame.getHeight());
		jdp.add(layerManager);
		jdp.add(TimeLine.window);
		jdp.add(Window.window);
		jdp.add(ToolBar);
		jdp.add(TimeLineAssistant);
		jdp.add(ToolAssistant);
		
		mainFrame.add(jdp);
		mainFrame.setVisible(true);
		
		
		TimeLine.generateBufferStrategy();
		Window.generateBufferStrategy();
		Window.setUpMenuBar();
		while(SizeSetter.getifReady() == false)
		{
			SizeSetter.setAnimationResolution();
			TextBox.addMessage("Rozpoczynanie nowej animacji o parametrach "+SizeSetter.getStartSizeX()+"x"+SizeSetter.getStartSizeY()+" fps: "+SizeSetter.getFrameRate(), 300);
		}
		
		scaleInsideWindows(Window, TimeLineAssistant, ToolBar, ToolAssistant, TimeLine, layerManager);
		
		MainLoop(Window, TimeLineAssistant, ToolBar, ToolAssistant, TimeLine, layerManager);
	}

	
	private static void MainLoop(Window Window, TimeLineAssistant TimeLineAssistant, ToolBar ToolBar, ToolAssistant ToolAssistant, TimeLine TimeLine, LayerManager layerManager )
	{
		while(true)
		{
			scaleInsideWindows(Window, TimeLineAssistant, ToolBar, ToolAssistant, TimeLine, layerManager);
			changeFrame(TimeLineAssistant, Window);
			if(Window.getDataToUbdate())
			{
				System.out.println("Aktualizowanie koloru");
				ToolAssistant.setParameterBox(Window.getParameterBox());
				Window.setDataToUbdate(false);
			}
			Window.setTool(ToolBar.getTool());
			Window.setLinetool(ToolAssistant.getLinetool());
			ToolAssistant.setTool(Window.getTool());
			ToolAssistant.UbdateAssistanceForActualTool();
			Window.setParameterBox(ToolAssistant.getParameterBox());

			Window.revalidate();
			
			Window.paint();
			if(Window.saveSet == true)
			{
				TextBox.addMessage("Zapisano nowy układ okien na ekranie", 300);
				System.out.println("ZAPISYWANIE POZYCJI OKIEN");
				saveSet(Window, ToolBar, ToolAssistant, TimeLineAssistant, TimeLine, layerManager, mainFrame.getWidth(), mainFrame.getHeight());
				Window.saveSet = false;
			}
			TimeLine.paint(Window.a);
			layerManager.ubdateTab(Window.a);
		 if(Window.stateChange == true){
			 if(Window.alwaysTopBoxBool == true){
				 AnimEngine.mainFrame.setAlwaysOnTop(true);
			 }
			 else{
				 AnimEngine.mainFrame.setAlwaysOnTop(false);				 
			 }
			 
			 if(Window.timeLineBool == true){

				 TimeLine.window.setVisible(false);
			 }
			 else{
				 TimeLine.window.setVisible(true); 
			 }
			 
			 if(Window.timeLineBarBoxBool == true){
				 TimeLineAssistant.setVisible(false);
			 }
			 else{
				 TimeLineAssistant.setVisible(true); 
			 }
			 
			 if(Window.toolBoxBool == true){
				 ToolAssistant.setVisible(false);
			 }
			 else{
				 ToolAssistant.setVisible(true);
			 }
			 
			 if(Window.toolBox2Bool == true){
				 ToolBar.setVisible(false);
			 }
			 else{
				 ToolBar.setVisible(true); 
			 }
			 
			 Window.stateChange = false; 
		 }
		
		}
			 
		
	}
	
	/**
	 * Zapisuje aktualne ustawienia okien na ekranie do pliku WindowSet.txt
	 * @param window
	 * @param toolBar
	 * @param toolAssistant
	 * @param timeLineAssistent
	 * @param TimeLine
	 */
private static void saveSet(Window window, ToolBar toolBar, ToolAssistant toolAssistant, TimeLineAssistant timeLineAssistent, TimeLine TimeLine, LayerManager layerManager, float x, float y)
{
	File WindowSet = new File("WindowSet.txt");
	if(!WindowSet.exists())
	{
		try {
			WindowSet.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	try {
		PrintWriter pw = new PrintWriter(WindowSet);
		pw.println("WINDOW");
		pw.print(window.getOkno().getX()/x+" "+window.getOkno().getY()/y+" "+window.getOkno().getWidth()/x+" "+window.getOkno().getHeight()/y);
		pw.println();
		pw.println("TOOLBAR");
		pw.print(toolBar.getX()/x+" "+toolBar.getY()/y+" "+toolBar.getWidth()/x+" "+toolBar.getHeight()/y);
		pw.println();
		pw.println("TIMELINEASSISTANT");
		pw.print(timeLineAssistent.getX()/x+" "+timeLineAssistent.getY()/y+" "+timeLineAssistent.getWidth()/x+" "+timeLineAssistent.getHeight()/y);
		pw.println();
		pw.println("TOOLASIST");
		pw.print(toolAssistant.getX()/x+" "+toolAssistant.getY()/y+" "+toolAssistant.getWidth()/x+" "+toolAssistant.getHeight()/y);
		pw.println();
		pw.println("TIMELINE");
		pw.print(TimeLine.window.getX()/x+" "+TimeLine.window.getY()/y+" "+TimeLine.window.getWidth()/x+" "+TimeLine.window.getHeight()/y);
		pw.println();
		pw.println("LAYERMANAGER");
		pw.print(layerManager.getX()/x+" "+layerManager.getY()/y+" "+layerManager.getWidth()/x+" "+layerManager.getHeight()/y);
		pw.println();
		pw.close();
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}
	
}


/**
 * Odczytuje dane z pliku WindowSet.txt i ustawia rozmiary i położenia okien na takie jak w pliku
 * @param WindowSetFile
 * @param Window
 * @param TimeLineAssistant
 * @param ToolBar
 * @param ToolAssistant
 * @param TimeLine
 */
private static void setWindowsFromFile(File WindowSetFile, Window Window, TimeLineAssistant TimeLineAssistant, ToolBar ToolBar, ToolAssistant ToolAssistant, TimeLine TimeLine, LayerManager layerManager, int x, int y)
{
	if(WindowSetFile.exists())
	{
	try 
	{
		System.out.println("Wczytywanie pozycji");
		Scanner s = new Scanner(WindowSetFile);
		if(s.nextLine().equals("WINDOW"))
		{
			Window.getOkno().setBounds((int)(x*Float.parseFloat(s.next())), (int)(y*Float.parseFloat(s.next())), (int)(x*Float.parseFloat(s.next())), (int)(y*Float.parseFloat(s.next())));
			s.nextLine();
		}
		if(s.nextLine().equals("TOOLBAR"))
		{
			ToolBar.setBounds((int)(x*Float.parseFloat(s.next())), (int)(y*Float.parseFloat(s.next())), (int)(x*Float.parseFloat(s.next())), (int)(y*Float.parseFloat(s.next())));
			s.nextLine();
		}
		if(s.nextLine().equals("TIMELINEASSISTANT"))
		{
			TimeLineAssistant.setBounds((int)(x*Float.parseFloat(s.next())), (int)(y*Float.parseFloat(s.next())), (int)(x*Float.parseFloat(s.next())), (int)(y*Float.parseFloat(s.next())));
			s.nextLine();
		}
		if(s.nextLine().equals("TOOLASIST"))
		{
			ToolAssistant.setBounds((int)(x*Float.parseFloat(s.next())), (int)(y*Float.parseFloat(s.next())), (int)(x*Float.parseFloat(s.next())), (int)(y*Float.parseFloat(s.next())));
			s.nextLine();
		}
		if(s.nextLine().equals("TIMELINE"))
		{
			TimeLine.window.setBounds((int)(x*Float.parseFloat(s.next())), (int)(y*Float.parseFloat(s.next())), (int)(x*Float.parseFloat(s.next())), (int)(y*Float.parseFloat(s.next())));
			s.nextLine();
		}
		if(s.nextLine().equals("LAYERMANAGER"))
		{
			layerManager.setBounds((int)(x*Float.parseFloat(s.next())), (int)(y*Float.parseFloat(s.next())), (int)(x*Float.parseFloat(s.next())), (int)(y*Float.parseFloat(s.next())));
			s.nextLine();
		}
		s.close();
		
		Window.setRec(Window.window.getBounds());
		TimeLine.setRec(TimeLine.window.getBounds());
		ToolBar.setRec(ToolBar.getBounds());
		TimeLineAssistant.setRec(TimeLineAssistant.getBounds());
		ToolAssistant.setRec(ToolAssistant.getBounds());
		layerManager.setRec(layerManager.getBounds());
		
	} catch (FileNotFoundException e) 
	{
		System.out.println("BRAK PLIKU");
		e.printStackTrace();
	}
	}
}

public static void scaleInsideWindows(Window Window, TimeLineAssistant TimeLineAssistant, ToolBar ToolBar, ToolAssistant ToolAssistant, TimeLine TimeLine, LayerManager layerManager)
{
	float actSizeX = mainFrame.getWidth();
	float actSizeY = mainFrame.getHeight();
	
	if(lastX != actSizeX||lastY != actSizeY)
	{
		
	frameCupture.hasFrame = false;
	float scaleX = actSizeX/sizeX;
	float scaleY = actSizeY/sizeY;
	
	Rectangle bounds = new Rectangle(Window.getRec());
	bounds.x*=scaleX;
	bounds.y*=scaleY;
	bounds.width*=scaleX;
	bounds.height*=scaleY;
	Window.window.setBounds(bounds);
	
	bounds = new Rectangle(TimeLine.getRec());
	bounds.x*=scaleX;
	bounds.y*=scaleY;
	bounds.width*=scaleX;
	bounds.height*=scaleY;
	TimeLine.window.setBounds(bounds);
	
	bounds = new Rectangle(TimeLineAssistant.getRec());
	bounds.x*=scaleX;
	bounds.y*=scaleY;
	bounds.width*=scaleX;
	bounds.height*=scaleY;
	TimeLineAssistant.setBounds(bounds);
	
	bounds = new Rectangle(ToolBar.getRec());
	bounds.x*=scaleX;
	bounds.y*=scaleY;
	bounds.width*=scaleX;
	bounds.height*=scaleY;
	ToolBar.setBounds(bounds);
	
	bounds = new Rectangle(ToolAssistant.getRec());
	bounds.x*=scaleX;
	bounds.y*=scaleY;
	bounds.width*=scaleX;
	bounds.height*=scaleY;
	ToolAssistant.setBounds(bounds);
	
	
	bounds = new Rectangle(layerManager.getRec());
	bounds.x*=scaleX;
	bounds.y*=scaleY;
	bounds.width*=scaleX;
	bounds.height*=scaleY;
	layerManager.setBounds(bounds);
	
	layerManager.refreshWindowSetSize();
	
	lastX = actSizeX;
	lastY = actSizeY;
	}
	
}
/**
 * Odczytuje wywoałania z TimeLineAssistent-a i wywołuje odpowiednie operacje w klasie Window wymuszając zmianę klatki
 * @param timeLineAssistent
 * @param window
 */
private static void changeFrame(TimeLineAssistant timeLineAssistent, Window window)
{

	if(timeLineAssistent.leftButton)
		window.a.MoveBack();
	else if(timeLineAssistent.rightButton)
		window.a.MoveForeword();
	else if(timeLineAssistent.rightCopyLastFrameButton)
		window.a.MoveForewordWithCopyFrame();
	else if(timeLineAssistent.startButton)
		window.a.MoveToBegining();
	else if(timeLineAssistent.endButton)
		window.a.MoveToEnd();
	else if(timeLineAssistent.delButton)
		window.a.removeCurrentFrame();
	window.setPLAY(timeLineAssistent.playButton);
	timeLineAssistent.resetButtonState();
	timeLineAssistent.currentFrame = window.a.getCurrentFrame();
	timeLineAssistent.frameTabSize = window.a.getFrameTabSize()-1;
	timeLineAssistent.actualizeCurrentFrameNumber();
}
}

