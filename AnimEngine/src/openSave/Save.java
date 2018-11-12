package openSave;

import java.io.File;
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

public class Save 
{

	public static int SaveDataToFile( ArrayList<Frame> frames, int x, int y)
	{
		System.out.println("HAMSKIE ZAPISYWANIE");
		String fileName = SaveAs.getName();
		
		System.out.println("Rozpoczeto nadpisywanie pliku jako: "+ SaveAs.getName());
		File f = new File(fileName);
		
		try {
			f.createNewFile();
		} catch (IOException e) {
			System.out.println("Nie mo�na zapisa� pliku");
			e.printStackTrace();
		}
		PrintWriter pw;
		try {
			pw = new PrintWriter(f);
		} catch (IOException e) {
			System.out.println("Nie mo�na nadpisa� pliku");
			e.printStackTrace();
			return -1;
		}
		System.out.println("Nag��wek pliku");
		pw.println("ANIMATION");
		pw.println(fileName);
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
		System.out.println("Zako�czono CHAMSKI zapis pliku");
		pw.println();
		pw.println(Window.VERSION);
		pw.close();
		return 1;
		
	}
	
	private static void writeBitmap(PrintWriter pw, Bitmap b)
	{
		pw.println("BITMAP "+b.getBitmapIndex()+" "+b.getX()+" "+b.getY()+" "+b.getScaleX()+" "+b.getScaleY()+" "+b.getAngle());
	}
	
	private static void writeFrameQuene(PrintWriter pw, frameQuene fq)
	{
		pw.println("Q "+fq.isObject+" "+fq.index);
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
	
	private static void writePoint(PrintWriter pw, renderSource.Point p)
	{
		pw.println(p.x+" "+p.y);
	}
	private static void writePolygonConn(PrintWriter pw, shapeConnector s)
	{
		pw.print(s.isArc+" "+s.isDoubleArc+" "+s.fromLeft+" "+s.polygonEnd+" "+s.connIndex+" "+s.P1+" "+s.P2+" "+s.P3+" "+s.P4+" ");
	}
	private static void writeConnection(PrintWriter pw, Connection c)
	{
		if(c.getC()!=null)
		pw.println(c.isARC()+" "+c.isDoubleArc()+" "+c.getP1()+" "+c.getP2()+" "+c.getP3()+" "+c.getP4()+" "+c.getSize()+" COLOR "+c.getC().getRed()+" "+c.getC().getGreen()+" "+c.getC().getBlue()+" "+c.getC().getAlpha());
		else
		pw.println(c.isARC()+" "+c.isDoubleArc()+" "+c.getP1()+" "+c.getP2()+" "+c.getP3()+" "+c.getP4()+" "+c.getSize()+" NOCOLOR");	
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
}
