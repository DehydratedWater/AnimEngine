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
import renderSource.curvesPointsConnector;
import structures.Frame;
import structures.ParameterBox;
import toolBox.TextBox;
import toolBox.frameCupture;
import toolBox.tb;

public class CurveSegmentTool implements paintToolShem
{
	private boolean created = false;
	private ArrayList<Point> pointTab;
	private ArrayList<Point> controlPointTab;
	public static boolean drawPoints = false;
	public static float drawingPrecision = 20f;
	public static float curvingPrecision = 0.3f;
	public void useTool(Frame f, int x, int y, dataBox db, ParameterBox pb, Graphics2D g,
			int tool) {
		
		if(created==false)
		{
			if(db.mouseClicked)
				initTool(x, y);
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
					simplyfication(drawingPrecision);
					addSpecialPoints(f, curvingPrecision);
					addLines(f, pb);
					resetTool();
				}
		}
	}

	private void addSpecialPoints(Frame f, float range) 
	{
		if(pointTab.size()>=3)
		{
		controlPointTab = new ArrayList<Point>();
		for(int i = 1; i < pointTab.size()-1; i++)
		{
			Point P1 = pointTab.get(i-1);
			Point P2 = pointTab.get(i);
			Point P3 = pointTab.get(i+1);
			vector2D v1 = new vector2D(P1.x-P2.x, P1.y-P2.y);
			vector2D v2 = new vector2D(P3.x-P2.x, P3.y-P2.y);
			float lenght1 = v1.getLenght()*range;
			float lenght2 = v2.getLenght()*range;
			v1.normalise();
			v2.normalise();
			Point T1 = new Point(P2.x+v1.x, P2.y+v1.y);
			Point T2 = new Point(P2.x+v2.x, P2.y+v2.y);
			v1 = new vector2D(T1.x-T2.x, T1.y-T2.y);
			v2 = new vector2D(T2.x-T1.x, T2.y-T1.y);
			v1.normalise();
			v2.normalise();
			v1.multiply(lenght1);
			v2.multiply(lenght2);
			T1 = new Point(P2.x+v1.x, P2.y+v1.y);
			T2 = new Point(P2.x+v2.x, P2.y+v2.y);
			float[] tab1 = f.gco().scaleAndRotateValue(T1.x, T1.y);
			float[] tab2 = f.gco().scaleAndRotateValue(T2.x, T2.y);
			controlPointTab.add(new Point(tab1[0], tab1[1]));
			controlPointTab.add(new Point(tab2[0], tab2[1]));
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

	private void addLines(Frame f, ParameterBox pb) 
	{
		if(pointTab.size()>1)
		{
		PastBox.addStepStart();
		int startPointSize = f.gco().getPointTab().size();
		for(int i = 0; i < pointTab.size(); i++)
		{
			float tab[] =  f.gco().scaleAndRotateValue(pointTab.get(i).x, pointTab.get(i).y);
			f.gco().addPoint(new Point(tab[0], tab[1]));
		}
		int firstPointSize = f.gco().getPointTab().size();
		int index = 0;
		f.gco().addPoint(controlPointTab.get(index));
		index++;
		f.gco().addConnection(new Connection(startPointSize, startPointSize+1, f.gco().getPointTabSize()-1, f.gco().getPointTabSize()-1,  pb.LineSize, pb.LineColor));
		f.gco().getLastPoint().cpc = new curvesPointsConnector(startPointSize+1);
		f.gco().getLastPoint().TechPoint = true;
		for(int i = startPointSize+1; i < firstPointSize-2; i++)
		{
			f.gco().addPoint(controlPointTab.get(index));
			index++;
			f.gco().addPoint(controlPointTab.get(index));
			index++;
			f.gco().addConnection(new Connection(i, i+1, f.gco().getPointTabSize()-1, f.gco().getPointTabSize()-2, pb.LineSize, pb.LineColor));
			f.gco().getLastPoint().cpc = new curvesPointsConnector(i+1);
			f.gco().getPreLastPoint().cpc = new curvesPointsConnector(i);
			f.gco().getLastPoint().TechPoint = true;
			f.gco().getPreLastPoint().TechPoint = true;
		}
		f.gco().addPoint(controlPointTab.get(index));
		index++;
		f.gco().addConnection(new Connection(firstPointSize-2, firstPointSize-1, f.gco().getPointTabSize()-1, f.gco().getPointTabSize()-1,  pb.LineSize, pb.LineColor));
		f.gco().getLastPoint().cpc = new curvesPointsConnector(firstPointSize-2);
		f.gco().getLastPoint().TechPoint = true;
		reAddLines(f);
		frameCupture.hasFrame = false;
		FillManager.needToRefresh = true;
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


	private void initTool(float x, float y) 
	{
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
