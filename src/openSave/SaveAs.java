package openSave;

import java.awt.Component;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import bitmapEngine.Bitmap;
import main.Window;
import polygonEngine.FilledPolygonWithCurves;
import polygonEngineStructures.shapeConnector;
import renderSource.Connection;
import renderSource.Object;
import structures.Frame;
import structures.frameQuene;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SaveAs {
	
	private static boolean ifBeenSaved = false;
	private static String name; 
	private static String path;
	
	public static void saveAs(ArrayList<Frame> frames, int x, int y){
	File f;
	 boolean rightChoice = false;	
		do{
		choosePath();
		
		 f = new File(path+".ae");
		 try {
			f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		 
		if(f.exists() == false){
			rightChoice = true;
			Component frame = null;
			JOptionPane.showMessageDialog(frame,
				    "File exists, make another one.",
				    "Inane error",
				    JOptionPane.ERROR_MESSAGE);
		}
		else {
			rightChoice = false;
		}
		
		}while(rightChoice == true);
		
		try {
			f.createNewFile();
		} catch (IOException e) {
			System.out.println("Nie mo�na zapisa� pliku");
			e.printStackTrace();
		}
		PrintWriter pw = null;
		
		try {
			pw = new PrintWriter(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Nag��wek pliku");
		pw.println("ANIMATION");
		pw.println(name);
		pw.println("RESOLUTION");
		pw.println(x+" "+y);
		pw.println("LENGHT");
		pw.println(frames.size());
		
		System.out.println("poszczeg�lne klatki");
		for(int i = 0; i < frames.size();i++)
		{
			pw.println("FRAME");
			
			pw.println("QUENETAB");
			pw.println("LENGHT");
			pw.println(frames.get(i).getQueneTabSize());
			for(int j = 0; j < frames.get(i).getQueneTabSize();j++)
			{
				writeFrameQuene(pw, frames.get(i).getFrameQuene(j));
			}
			
			pw.println("OBJTAB");
			pw.println("LENGHT");
			pw.println(frames.get(i).getObjTabSize());
			for(int j = 0; j < frames.get(i).getObjTabSize();j++)
			{
				pw.println("OBJ");
				writeObject(pw, frames.get(i).getObj(j));
			}
			//TODO
			pw.println("BITMAPTAB");
			pw.println("LENGHT");
			pw.println(frames.get(i).getTexTabSize());
			for(int j = 0; j < frames.get(i).getTexTabSize();j++)
			{
				writeBitmap(pw, frames.get(i).getBitmap(j));
			}
		}
		
		pw.println("BITMAPS");
		pw.println("LENGHT");
		pw.println(Window.tm.getTextureTabSize());
		for(int i = 0; i < Window.tm.getTextureTabSize(); i++)
		{
			pw.println(Window.tm.getTexture(i).getBitmap());
		}
		pw.println();
		pw.println(Window.VERSION);
		pw.close();
		ifBeenSaved = true;
		System.out.println("Zako�czono zapis pliku JAKO");
		
		}
	
	private static void writeBitmap(PrintWriter pw, Bitmap b)
	{
		pw.println("BITMAP "+b.getBitmapIndex()+" "+b.getX()+" "+b.getY()+" "+b.getScaleX()+" "+b.getScaleY()+" "+b.getAngle());
	}
	
	private static void writeFrameQuene(PrintWriter pw, frameQuene fq)
	{
		pw.println("Q "+fq.isObject+" "+fq.index);
	}
	
	
	private static void choosePath(){
	 JFrame parentFrame = new JFrame();
	 
	 JFileChooser chooser = new JFileChooser();
	 chooser.setDialogTitle("Specify a file to save");  
	 FileFilter filter = new FileNameExtensionFilter("AnimEngine animation files","ae");
	 chooser.addChoosableFileFilter(filter);
	 chooser.setAcceptAllFileFilterUsed(true);
	 
	 int userSelection = chooser.showSaveDialog(parentFrame);
	  
	 if (userSelection == JFileChooser.APPROVE_OPTION) {
		 name = chooser.getSelectedFile().getName();
		 path = chooser.getSelectedFile().getAbsolutePath();
		 System.out.println(path);
	 }
	 
	 }		 

	private static void writeObject(PrintWriter pw, Object o)
	{
		pw.println("POINTS");
		pw.println(o.getPointTab().size());
		for(int i = 0 ; i < o.getPointTab().size(); i++)
		{
			writePoint(pw, o.getNormalPoint(i));
		}
		pw.println("CONNECTIONES");
		pw.println(o.getConnectionTab().size());
		for(int i = 0 ; i < o.getConnectionTab().size(); i++)
		{
			writeConnection(pw, o.getCon(i));
		}
		pw.println("POLYGONS");
		pw.println(o.getPolygonTab().size());
		for(int i = 0 ; i < o.getPolygonTab().size(); i++)
		{
			writePolygon(pw, o.getPolygonTab().get(i));
		}
	}
	private static void writePolygon(PrintWriter pw, FilledPolygonWithCurves p)
	{
		pw.print("COLOR "+p.c.getRed()+" "+p.c.getGreen()+" "+p.c.getBlue()+" "+p.c.getAlpha()+" ");
		pw.print("POLYGON "+p.shapeTab.size()+" ");
		for(int i = 0; i < p.shapeTab.size(); i++)
		{
			writePolygonConn(pw, p.shapeTab.get(i));
		}
		pw.print("CONN "+p.connectionIndexTab.size()+" ");
		for(int i = 0; i < p.connectionIndexTab.size(); i++)
		{
			pw.print(p.connectionIndexTab.get(i)+" ");
		}
		pw.println();
	}
	private static void writePolygonConn(PrintWriter pw, shapeConnector s)
	{
		pw.print(s.isArc+" "+s.isDoubleArc+" "+s.fromLeft+" "+s.polygonEnd+" "+s.connIndex+" "+s.P1+" "+s.P2+" "+s.P3+" "+s.P4+" ");
	}
	private static void writePoint(PrintWriter pw, renderSource.Point p)
	{
		pw.println(p.x+" "+p.y);
	}

	private static void writeConnection(PrintWriter pw, Connection c)
	{
		if(c.getC()!=null)
		pw.println(c.isARC()+" "+c.isDoubleArc()+" "+c.getP1()+" "+c.getP2()+" "+c.getP3()+" "+c.getP4()+" "+c.getSize()+" COLOR "+c.getC().getRed()+" "+c.getC().getGreen()+" "+c.getC().getBlue()+" "+c.getC().getAlpha());
		else
		pw.println(c.isARC()+" "+c.isDoubleArc()+" "+c.getP1()+" "+c.getP2()+" "+c.getP3()+" "+c.getP4()+" "+c.getSize()+" NOCOLOR");	
	}
	

	public static String getPath(){
		return path;
	}
	
	static String getName(){
		return name;
	}
	  static public void setName(String x){
	    	name = x;
	    }
	static public boolean getIfBeenSaved(){
		return ifBeenSaved;
	}
	
    static public void setIfBeenSaved(boolean x){
    	ifBeenSaved = x;
    }
    
    static public void setPath(String x){
    	path = x;
    }
    
  
    
}

