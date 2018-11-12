package paintManagerTools;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import insertionManager.FindLinesInserctions;
import mark.dataBox;
import pastManager.PastBox;
import polygonEngine.FillManager;
import renderSource.Connection;
import renderSource.Point;
import structures.Frame;
import structures.ParameterBox;
import toolBox.frameCupture;
import toolBox.tb;

public class RectTool implements paintToolShem{

	private boolean created = false;
	private float StartX, StartY, EndX, EndY;
	private int typeOfStartFilling = 0, typeOfEndFilling = 0;
	private int startIndex, endIndex;
	private int startConnIndex, endConnIndex;
	public void useTool(Frame f, int x, int y, dataBox db, ParameterBox pb, Graphics2D g,
			int tool) {
		if(created==false)
		{
			if(db.mouseClicked&&!db.mouseRightClicked)
				initTool(f, x, y, db);
		}
		else
		{
			if(db.mouseRightClicked)
			{
				resetTool();
			}
			else
				if(db.mouseClicked)
					refreshTool(f, x, y, pb, db, g);
				else
				{
					addEndLineStatsLine(f, db, x, y);
					addRect(f, pb);
					resetTool();
				}
		}
		
	}

	private void addEndLineStatsLine(Frame f, dataBox db, int x, int y) 
	{

		//System.out.println("Koñczenie rysowania lini");
		int pointIndex = f.gco().getNearestPoint(x, y);
		int lineIndex = f.gco().getNearestConnection(x, y);
		
		
		if(!db.CTRL)
		{
		if(pointIndex>-10)
		{
			//System.out.println("Odnaleziono bliski punkt "+pointIndex);
			float[] t = f.getObj(f.getObject()).getScaledPoint(pointIndex);
			EndX = t[0];
			EndY = t[1];
			typeOfEndFilling = 1;
			endIndex = pointIndex;
		}
		else if(lineIndex>-10)
		{
			//System.out.println("Odnaleziono bliski odcinek "+lineIndex);
			float t1[] = f.gco().getScaledPoint(f.gco().getCon(lineIndex).getP1());
			float t2[] = f.gco().getScaledPoint(f.gco().getCon(lineIndex).getP2());
			float t3[] = tb.getPoinOfCrossABC(tb.generatePatternABC(t1[0], t1[1], t2[0], t2[1]), x, y);
			if(tb.isPointInside(t1, t2, t3[0], t3[1], 0.001f))
			{
			EndX = t3[0];
			EndY = t3[1];
			typeOfEndFilling = 2;
			endConnIndex = lineIndex;
			}
			else
			{
				EndX = x;
				EndY = y;
				typeOfEndFilling = 0;
			}
		}
		else
		{
			EndX = x;
			EndY = y;
			typeOfEndFilling = 0;
		}
		}
		else
		{
			EndX = x;
			EndY = y;
			typeOfEndFilling = 0;
		}
		//System.out.println("Zakoñczono rysowanie lini w punkcie punktu "+StartX+" "+StartY);
	}
	

	private void initTool(Frame f, int x, int y, dataBox db) 
	{
		System.out.println("Inicjalizacja narzêdzia");
		int pointIndex = f.gco().getNearestPoint(x, y);
		int lineIndex = f.gco().getNearestConnection(x, y);
		PastBox.addStepStart();
		if(!db.CTRL)
		{
		if(pointIndex>-10)
		{
			//System.out.println("Odnaleziono bliski punkt "+pointIndex);
			float[] t = f.getObj(f.getObject()).getScaledPoint(pointIndex);
			StartX = t[0];
			StartY = t[1];
			typeOfStartFilling = 1;
			startIndex = pointIndex;
		}
		else if(lineIndex>-10)
		{
			//System.out.println("Odnaleziono bliski odcinek "+lineIndex);
			float t1[] = f.gco().getScaledPoint(f.gco().getCon(lineIndex).getP1());
			float t2[] = f.gco().getScaledPoint(f.gco().getCon(lineIndex).getP2());
			float t3[] = tb.getPoinOfCrossABC(tb.generatePatternABC(t1[0], t1[1], t2[0], t2[1]), x, y);
			if(tb.isPointInside(t1, t2, t3[0], t3[1], 0.001f))
			{
			StartX = t3[0];
			StartY = t3[1];
			typeOfStartFilling = 2;
			startConnIndex = lineIndex;
			}
			else
			{
				StartX = x;
				StartY = y;
				typeOfStartFilling = 0;
			}
		}
		else
		{
			StartX = x;
			StartY = y;
			typeOfStartFilling = 0;
		}
		}
		else
		{
			StartX = x;
			StartY = y;
			typeOfStartFilling = 0;
		}

		created = true;
	}

	private void addRect(Frame f, ParameterBox pb) {
		
		float[] t1 = f.gco().scaleAndRotateValue(StartX, StartY);
		float[] t3 = f.gco().scaleAndRotateValue(EndX, EndY);
		float[] t4 = f.gco().scaleAndRotateValue(StartX, EndY);
		float[] t2 = f.gco().scaleAndRotateValue(EndX, StartY);
		Point p1 = new Point(t1[0], t1[1]);
		Point p2 = new Point(t2[0], t2[1]);
		Point p3 = new Point(t3[0], t3[1]);
		Point p4 = new Point(t4[0], t4[1]);
		
		PastBox.addStepStart();
		if(tb.distance(StartX, StartY, EndX, EndY)>5)
		{
		if(typeOfStartFilling==0&&typeOfEndFilling==0)
		{
			f.gco().addPoint(p1);
			f.gco().addPoint(p2);
			f.gco().addPoint(p3);
			f.gco().addPoint(p4);
			int l = f.gco().getPointTab().size();
			f.gco().addConnection(new Connection(l-4, l-3,pb.LineSize, pb.LineColor));
			f.gco().addConnection(new Connection(l-3, l-2,pb.LineSize, pb.LineColor));
			f.gco().addConnection(new Connection(l-2, l-1,pb.LineSize, pb.LineColor));
			f.gco().addConnection(new Connection(l-1, l-4,pb.LineSize, pb.LineColor));
		}
		else if(typeOfStartFilling==1&&typeOfEndFilling==0)
		{
			f.gco().addPoint(p2);
			int l = f.gco().getPointTab().size();
			f.gco().addConnection(new Connection(startIndex, l-1,pb.LineSize, pb.LineColor));
		}
		else if(typeOfStartFilling==0&&typeOfEndFilling==1)
		{
			f.gco().addPoint(p1);
			int l = f.gco().getPointTab().size();
			f.gco().addConnection(new Connection(l-1, endIndex,pb.LineSize, pb.LineColor));
		}
		else if(typeOfStartFilling==1&&typeOfEndFilling==1)
		{
			f.gco().addConnection(new Connection(startIndex, endIndex,pb.LineSize, pb.LineColor));
		}
		else if(typeOfStartFilling==2&&typeOfEndFilling==0)
		{
			f.gco().addPoinToConnection(startConnIndex, StartX, StartY);
			f.gco().addPoint(p2);
			int l = f.gco().getPointTab().size();
			f.gco().addConnection(new Connection(l-2, l-1,pb.LineSize, pb.LineColor));
		}
		else if(typeOfStartFilling==0&&typeOfEndFilling==2)
		{
//TODO
			f.gco().addPoinToConnection(endConnIndex, EndX, EndY);
			f.gco().addPoint(p1);
			int l = f.gco().getPointTab().size();
			f.gco().addConnection(new Connection(l-2, l-1,pb.LineSize, pb.LineColor));
			
//			f.gco().addPoint(p1);
//			f.gco().addPoint(p2);
//			int l = f.gco().getPointTab().size();
//			f.gco().addNotControledConnection(new Connection(l-2, l-1,pb.LineSize, pb.LineColor));
		}
		else if(typeOfStartFilling==2&&typeOfEndFilling==1)
		{
			//System.out.println("1");
			f.gco().addPoinToConnection(startConnIndex, StartX, StartY);
			int l = f.gco().getPointTab().size();
			f.gco().addConnection(new Connection(l-1, endIndex,pb.LineSize, pb.LineColor));
		}
		else if(typeOfStartFilling==1&&typeOfEndFilling==2)
		{
			//System.out.println("2");
			f.gco().addPoinToConnection(endConnIndex, EndX, EndY);
			int l = f.gco().getPointTab().size();
			f.gco().addConnection(new Connection(startIndex, l-1,pb.LineSize, pb.LineColor));
		}
		else if(typeOfStartFilling==2&&typeOfEndFilling==2)
		{
			//System.out.println("3");
			f.gco().addPoinToConnection(startConnIndex, StartX, StartY);
			f.gco().addPoinToConnection(endConnIndex, EndX, EndY);
			int l = f.gco().getPointTab().size();
			f.gco().addConnection(new Connection(l-2, l-1, pb.LineSize, pb.LineColor));
		}
		
		
		FindLinesInserctions fli = new FindLinesInserctions();
		fli.FindInserctionSimpleCall(f);
		frameCupture.hasFrame = false;
		}
		FillManager.needToRefresh = true;
	}

	private void refreshTool(Frame f, int x, int y, ParameterBox pb, dataBox db, Graphics2D g) {
		//System.out.println("Rysowanie");
		int pointIndex = f.gco().getNearestPoint(x, y);
		int lineIndex = f.gco().getNearestConnection(x, y);
		if(!db.CTRL)
		{
		if(pointIndex>-10)
		{
			//System.out.println("Odnaleziono bliski punkt "+pointIndex);
			float[] t = f.getObj(f.getObject()).getScaledPoint(pointIndex);
			x = (int) t[0];
			y = (int) t[1];
		}
		else if(lineIndex>-10)
		{
			//System.out.println("Odnaleziono bliski odcinek "+lineIndex);
			float t1[] = f.gco().getScaledPoint(f.gco().getCon(lineIndex).getP1());
			float t2[] = f.gco().getScaledPoint(f.gco().getCon(lineIndex).getP2());
			float t3[] = tb.getPoinOfCrossABC(tb.generatePatternABC(t1[0], t1[1], t2[0], t2[1]), x, y);
			//System.out.println("Punkt przeciêcia z lini¹ w "+t3[0]+" "+t3[1]);
			if(tb.isPointInside(t1, t2, t3[0], t3[1], 0.001f))
			{
			x = (int) t3[0];
			y = (int) t3[1];
			}
		}
		}
		EndX = x;
		EndY = y;
		
		g.setStroke(new BasicStroke((pb.LineSize*main.Window.Scale), BasicStroke.CAP_ROUND,  BasicStroke.JOIN_ROUND));
		g.setColor(pb.LineColor);
		g.draw(new Line2D.Float(StartX, StartY, EndX, StartY));
		g.draw(new Line2D.Float(EndX, EndY, StartX, EndY));
		g.draw(new Line2D.Float(StartX, StartY, StartX, EndY));
		g.draw(new Line2D.Float(EndX, EndY, EndX, StartY));
	}
	
	public void resetTool() {
		created = false;
		
	}

	public boolean isCreated() {
		return created;
	}

}
