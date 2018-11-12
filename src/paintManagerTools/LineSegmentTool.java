package paintManagerTools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import insertionManager.FindLinesInserctions;
import mark.dataBox;
import pastManager.PastBox;
import polygonEngine.FillManager;
import renderSource.Connection;
import renderSource.Point;
import structures.Frame;
import structures.ParameterBox;
import toolBox.TextBox;
import toolBox.frameCupture;
import toolBox.tb;

public class LineSegmentTool implements paintToolShem
{
	private boolean created = false;
	private ArrayList<Point> pointTab;
	private int startPoint;
	private int endPoint;
	public static boolean drawPoints = false;
	
	public void useTool(Frame f, int x, int y, dataBox db, ParameterBox pb, Graphics2D g,
			int tool) {
		
		if(created==false)
		{
			if(db.mouseClicked)
				initTool(f, x, y);
		}
		else
		{
			if(db.mouseRightClicked)
			{
				resetTool();
				cancelPainting();
			}
			else
				if(db.mouseClicked)
					refreshTool(x, y, pb, g);
				else
				{
					simplyfication(10f);
					addLines(f, pb, x, y);
					resetTool();
				}
		}
	}

	private void simplyfication(float range)
	{
		if(pointTab.size()>2)
		{
			int startPointCount = pointTab.size();
			float patt[] = tb.generatePatternABC(pointTab.get(0).x, pointTab.get(0).y, pointTab.get(1).x, pointTab.get(1).y);
			for(int i = 2; i < pointTab.size()-1; i++)
			{
				float dist = tb.getDistToLine(patt[0], patt[1], patt[2], pointTab.get(i).x, pointTab.get(i).y);
				if(dist < range)
				{
					i--;
					pointTab.remove(i);
				}
				else
				{
					
					patt = tb.generatePatternABC(pointTab.get(i).x, pointTab.get(i).y, pointTab.get(i+1).x, pointTab.get(i+1).y);
				}
			}
			float dist = tb.distanceF(pointTab.get(0).x, pointTab.get(0).y, pointTab.get(1).x, pointTab.get(1).y);
			if(dist<range)
			{
				pointTab.remove(1);
			}
			if(pointTab.size()>1)
			{
			for(int i = 1; i < pointTab.size()-1; i++)
			{
				dist = tb.distanceF(pointTab.get(i).x, pointTab.get(i).y, pointTab.get(i+1).x, pointTab.get(i+1).y);
				if(dist<range)
				{
					pointTab.remove(i);
					i--;
				}
			}
			}
			TextBox.addMessage("Uproszczono prost¹ sk³adaj¹c¹ siê z "+startPointCount+" do "+pointTab.size()+" punktów symplifikacja do "+((float)pointTab.size()/(float)startPointCount*100)+"% pocz¹tkowej watoœci", 200);
		}
	}

	private void addLines(Frame f, ParameterBox pb, float x, float y) 
	{
		
		if(15>tb.distance(pointTab.get(0).x, pointTab.get(0).y, x, y)&&pointTab.size()>2)
		{
			PastBox.addStepStart();
			int startPointSize = f.gco().getPointTab().size();
			for(int i = 0; i < pointTab.size()-1; i++)
			{
				float tab[] =  f.gco().scaleAndRotateValue(pointTab.get(i).x, pointTab.get(i).y);
				f.gco().addPoint(new Point(tab[0], tab[1]));
			}
			f.gco().addConnection(new Connection(startPointSize, f.gco().getPointTab().size()-1, pb.LineSize, pb.LineColor));
			for(int i = startPointSize; i < f.gco().getPointTab().size()-1; i++)
			{
				f.gco().addConnection(new Connection(i, i+1, pb.LineSize, pb.LineColor));
			}
			reAddLines(f);
			frameCupture.hasFrame = false;
			FillManager.needToRefresh = true;
		}
		else
		{
		PastBox.addStepStart();
		endPoint = f.gco().getNearestPoint(x, y);
		if(endPoint>-10)
		{
			x = f.gco().getScaledPoint(endPoint)[0];
			y = f.gco().getScaledPoint(endPoint)[1];
		}
		if(pointTab.size()>2)
		{
		if(startPoint<0&&endPoint<0)
		{
			addLines(f, pb);
		}
		else if(startPoint>-10&&endPoint<0)
		{
			addLinesWithStart(f, pb);
		}
		else if(startPoint<0&&endPoint>-10)
		{
			addLinesWithEnd(f, pb);
		}
		else 
		{
			addLinesWhitStartAndEnd(f, pb);
		}
		reAddLines(f);
		frameCupture.hasFrame = false;
		FillManager.needToRefresh = true;
		}
		else
		{
			PastBox.addStepStart();
			if(startPoint>-10&&endPoint>-10)
			{
				f.gco().addConnection(new Connection(startPoint, endPoint, pb.LineSize, pb.LineColor));
			}
			reAddLines(f);
			frameCupture.hasFrame = false;
			FillManager.needToRefresh = true;
		}
		}
	}

	private void addLinesWithStart(Frame f, ParameterBox pb) {
		int startPointSize = f.gco().getPointTab().size();
		f.gco().addConnection(new Connection(startPoint, startPointSize, pb.LineSize, pb.LineColor));
		for(int i = 1; i < pointTab.size(); i++)
		{
			float tab[] =  f.gco().scaleAndRotateValue(pointTab.get(i).x, pointTab.get(i).y);
			f.gco().addPoint(new Point(tab[0], tab[1]));
		}
		
		for(int i = startPointSize; i < f.gco().getPointTab().size()-1; i++)
		{
			f.gco().addConnection(new Connection(i, i+1, pb.LineSize, pb.LineColor));
		}
	}

	private void addLinesWithEnd(Frame f, ParameterBox pb) {
		int startPointSize = f.gco().getPointTab().size();
		for(int i = 0; i < pointTab.size()-1; i++)
		{
			float tab[] =  f.gco().scaleAndRotateValue(pointTab.get(i).x, pointTab.get(i).y);
			f.gco().addPoint(new Point(tab[0], tab[1]));
		}
		f.gco().addConnection(new Connection(f.gco().getPointTab().size()-1, endPoint, pb.LineSize, pb.LineColor));
		for(int i = startPointSize; i < f.gco().getPointTab().size()-1; i++)
		{
			f.gco().addConnection(new Connection(i, i+1, pb.LineSize, pb.LineColor));
		}
	}

	private void addLinesWhitStartAndEnd(Frame f, ParameterBox pb) {
		int startPointSize = f.gco().getPointTab().size();
		f.gco().addConnection(new Connection(startPoint, startPointSize, pb.LineSize, pb.LineColor));
		for(int i = 1; i < pointTab.size()-1; i++)
		{
			float tab[] =  f.gco().scaleAndRotateValue(pointTab.get(i).x, pointTab.get(i).y);
			f.gco().addPoint(new Point(tab[0], tab[1]));
		}
		f.gco().addConnection(new Connection(f.gco().getPointTab().size()-1, endPoint, pb.LineSize, pb.LineColor));
		for(int i = startPointSize; i < f.gco().getPointTab().size()-1; i++)
		{
			f.gco().addConnection(new Connection(i, i+1, pb.LineSize, pb.LineColor));
		}
	}

	private void addLines(Frame f, ParameterBox pb) {
		int startPointSize = f.gco().getPointTab().size();
		for(int i = 0; i < pointTab.size(); i++)
		{
			float tab[] =  f.gco().scaleAndRotateValue(pointTab.get(i).x, pointTab.get(i).y);
			f.gco().addPoint(new Point(tab[0], tab[1]));
		}
		for(int i = startPointSize; i < f.gco().getPointTab().size()-1; i++)
		{
			f.gco().addConnection(new Connection(i, i+1, pb.LineSize, pb.LineColor));
		}
	}

	private void reAddLines(Frame f) {
		FindLinesInserctions fli = new FindLinesInserctions();
		fli.FindInserctionSimpleCall(f);
		frameCupture.hasFrame = false;
	}
	private void refreshTool(float x, float y, ParameterBox pb, Graphics2D g) 
	{
		double time = System.nanoTime();
		if(tb.distance(pointTab.get(pointTab.size()-1).x, pointTab.get(pointTab.size()-1).y, x, y)>3)
		pointTab.add(new Point(x, y));
		g.setColor(pb.LineColor);
		g.setStroke(new BasicStroke(pb.LineSize*main.Window.Scale, BasicStroke.CAP_ROUND,  BasicStroke.JOIN_ROUND));
		for(int i = 0; i < pointTab.size()-1; i++)
		{
			g.draw(new Line2D.Float(pointTab.get(i).x, pointTab.get(i).y, pointTab.get(i+1).x, pointTab.get(i+1).y));
		}
		if(drawPoints)
		{
		g.setColor(Color.green);
		for(int i = 0; i < pointTab.size()-1; i++)
		{
			g.drawOval((int)pointTab.get(i).x-3, (int)pointTab.get(i).y-3, 6, 6);
		}
		}
		TextBox.addMessage("Zakoñczono rysowanie pokazwywanej lini "+((System.nanoTime()-time)/1000000)+"ms iloœæ punktów w lini "+pointTab.size(), 30);
		//System.out.println("Zakoñczono rysowanie pokazwywanej lini "+((System.nanoTime()-time)/1000000)+"ms rozmiar tablicy "+pointTab.size());
	}


	private void cancelPainting() 
	{
		
	}


	private void initTool(Frame f, float x, float y) 
	{
		startPoint = f.gco().getNearestPoint(x, y);
		if(startPoint>-10)
		{
			x = f.gco().getScaledPoint(startPoint)[0];
			y = f.gco().getScaledPoint(startPoint)[1];
		}
		System.out.println("Rysowanie segmentów lini");
		pointTab = new ArrayList<Point>();
		pointTab.add(new Point(x, y));
		created = true;
	}


	public void resetTool() {
		
		created = false;
	}

	public boolean isCreated() {
		return created;
	}

}
