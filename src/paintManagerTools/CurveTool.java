package paintManagerTools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Line2D;
import java.awt.geom.QuadCurve2D;

import insertionManager.FindLinesInserctions;
import mark.dataBox;
import pastManager.PastBox;
import polygonEngine.FillManager;
import renderSource.Connection;
import renderSource.Point;
import renderSource.curvesPointsConnector;
import structures.Frame;
import structures.ParameterBox;
import toolBox.frameCupture;
import toolBox.tb;

public class CurveTool implements paintToolShem
{
	private boolean created = false;
	private int step = 0;
	private float StartX, StartY, EndX, EndY;
	private float FirstPX, FirstPY;
	private float SecondPX, SecondPY;
	private int typeOfStartFilling = 0, typeOfEndFilling = 0;
	private int startIndex, endIndex;
	private int startConnIndex, endConnIndex;
	private boolean back = false;
	public void useTool(Frame f, int x, int y, dataBox db, ParameterBox pb, Graphics2D g,
			int tool) {
		
		if(created==false)
		{
			if(db.mouseClicked&&!db.mouseRightClicked)
			{

				if(step == 0)
					initTool(f, x, y);
				else if(step ==1)
					initSecondPoint(x, y);
				else if(step ==2)
					initThirdPoint(x, y);
				else if(step ==3)
					waitForAprobate();
			}
			else
			{
				if(step ==1)
					drawLine(g, pb);
				else if(step ==2)
					drawCure(g, pb);
				else if(step == 3)
					drawFinalCure(g, pb);
			}
			if(db.mouseRightClicked)
			{
				if(!back)
				{
					step--;
					back = true;
				}
				if(step<0)
					step = 0;
			}
			else if(!db.mouseRightClicked)
			{
				back = false;
			}
			
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
				{
					if(step == 0)
						refreshToolFirstStep(f, x, y, pb, g);
					else if(step ==1)
						refreshToolSecondStep(f, x, y, pb, g);
					else if(step ==2)
						refreshToolThirdStep(f, x, y, pb, g);	
					if(step == 3)
						drawFinalCure(g, pb);
				}
				else
				{
					
					if(step == 0)
						finalizeFirstStep(f, x, y);
					else if(step == 1)
						finalizeSecondStep(x, y);
					else if(step == 2)
						finalizeThirdStep(x, y);
					changeStep();
					addCure(f, pb);
				}
		}
	}


	private void drawFinalCure(Graphics2D g, ParameterBox pb) {
		g.setColor(Color.green);
		g.setStroke(new BasicStroke(1));
		g.draw(new Line2D.Float(EndX, EndY, FirstPX, FirstPY));
		g.draw(new Line2D.Float(StartX, StartY, SecondPX, SecondPY));
		g.fillOval((int)FirstPX-5, (int)FirstPY-5, 10, 10);
		g.fillOval((int)SecondPX-5, (int)SecondPY-5, 10, 10);
		g.setStroke(new BasicStroke(pb.LineSize*main.Window.Scale));
		g.setColor(pb.LineColor);
		CubicCurve2D cd = new CubicCurve2D.Float();
		cd.setCurve(StartX, StartY, SecondPX, SecondPY, FirstPX, FirstPY, EndX, EndY);
		g.draw(cd);
	}


	private void waitForAprobate() {
		created = true;
	}


	private void drawCure(Graphics2D g, ParameterBox pb) {
		g.setColor(Color.green);
		g.setStroke(new BasicStroke(1));
		g.draw(new Line2D.Float(EndX, EndY, FirstPX, FirstPY));
		g.fillOval((int)FirstPX-5, (int)FirstPY-5, 10, 10);
		g.setStroke(new BasicStroke(pb.LineSize*main.Window.Scale));
		g.setColor(pb.LineColor);
		QuadCurve2D cd = new QuadCurve2D.Float();
		cd.setCurve(StartX, StartY, FirstPX, FirstPY, EndX, EndY);
		g.draw(cd);
		
	}


	private void drawLine(Graphics2D g, ParameterBox pb)
	{
		g.setStroke(new BasicStroke(pb.LineSize*main.Window.Scale));
		g.setColor(pb.LineColor);
		g.draw(new Line2D.Float(StartX, StartY, EndX, EndY));
	}


	private void finalizeThirdStep(int x, int y) {
		SecondPX = x;
		SecondPY = y;
		
	}


	private void finalizeSecondStep(int x, int y) {
		FirstPX = x;
		FirstPY = y;	
	}


	private void finalizeFirstStep(Frame f, int x, int y) {
		System.out.println("Koñczenie rysowania lini");
		int pointIndex = f.gco().getNearestPoint(x, y);
		int lineIndex = f.gco().getNearestConnection(x, y);
		
		
		
		if(pointIndex>-10)
		{
			System.out.println("Odnaleziono bliski punkt "+pointIndex);
			float[] t = f.getObj(f.getObject()).getScaledPoint(pointIndex);
			EndX = t[0];
			EndY = t[1];
			typeOfEndFilling = 1;
			endIndex = pointIndex;
		}
		else if(lineIndex>-10)
		{
			System.out.println("Odnaleziono bliski odcinek "+lineIndex);
			float t1[] = f.gco().getScaledPoint(f.gco().getCon(lineIndex).getP1());
			float t2[] = f.gco().getScaledPoint(f.gco().getCon(lineIndex).getP2());
			float t3[] = tb.getPoinOfCrossABC(tb.generatePatternABC(t1[0], t1[1], t2[0], t2[1]), x, y);
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
		System.out.println("Zakoñczono rysowanie lini w punkcie punktu "+StartX+" "+StartY);
		
	}


	private void refreshToolSecondStep(Frame f, int x, int y, ParameterBox pb, Graphics2D g) 
	{
		FirstPX = x;
		FirstPY = y;
		g.setColor(Color.green);
		g.setStroke(new BasicStroke(1));
		g.draw(new Line2D.Float(EndX, EndY, FirstPX, FirstPY));
		g.fillOval((int)FirstPX-5, (int)FirstPY-5, 10, 10);
		g.setStroke(new BasicStroke(pb.LineSize));
		g.setColor(pb.LineColor);
		QuadCurve2D cd = new QuadCurve2D.Float();
		cd.setCurve(StartX, StartY, FirstPX, FirstPY, EndX, EndY);
		g.draw(cd);
	}


	private void refreshToolThirdStep(Frame f, int x, int y, ParameterBox pb, Graphics2D g) 
	{
		SecondPX = x;
		SecondPY = y;
		g.setColor(Color.green);
		g.setStroke(new BasicStroke(1));
		g.draw(new Line2D.Float(EndX, EndY, FirstPX, FirstPY));
		g.draw(new Line2D.Float(StartX, StartY, SecondPX, SecondPY));
		g.fillOval((int)FirstPX-5, (int)FirstPY-5, 10, 10);
		g.fillOval((int)SecondPX-5, (int)SecondPY-5, 10, 10);
		g.setStroke(new BasicStroke(pb.LineSize));
		g.setColor(pb.LineColor);
		CubicCurve2D cd = new CubicCurve2D.Float();
		cd.setCurve(StartX, StartY, SecondPX, SecondPY, FirstPX, FirstPY, EndX, EndY);
		g.draw(cd);
	}


	private void addCure(Frame f, ParameterBox pb) 
	{
		if(step==4)
		{
			PastBox.addStepStart();
			System.out.println("Zakoñczono tworznie krzywej");
			step = 0;
			addLine(f, pb);
			
			float[] t1 = f.gco().scaleAndRotateValue(SecondPX, SecondPY);
			float[] t2 = f.gco().scaleAndRotateValue(FirstPX, FirstPY);
			Point p1 = new Point(t1[0], t1[1]);
			Point p2 = new Point(t2[0], t2[1]);
			p1.TechPoint = true;
			p2.TechPoint = true;
			f.gco().addPoint(p1);
			f.gco().addPoint(p2);
			p1.cpc = new curvesPointsConnector(f.gco().getCon(f.gco().getConnectionTab().size()-1).getP1());
			p2.cpc = new curvesPointsConnector(f.gco().getCon(f.gco().getConnectionTab().size()-1).getP2());
			int l = f.gco().getPointTab().size();
			f.gco().getCon(f.gco().getConnectionTab().size()-1).setP3(l-1);
			f.gco().getCon(f.gco().getConnectionTab().size()-1).setP4(l-2);
			f.gco().getCon(f.gco().getConnectionTab().size()-1).setARC(true);
			f.gco().getCon(f.gco().getConnectionTab().size()-1).setDoubleArc(true);
			
//			Curve curve = new Curve(f.gco().getCon(f.gco().getConnectionTab().size()-1), f.gco().getPointTab());
//			double time = System.nanoTime();
//			float[] t = curve.getPointOnCurve(0.2f);
//			System.out.println(System.nanoTime()-time);
//			f.gco().addPoint(new Point(t[0], t[1]));
//			SimplePoint[] sp = curve.getPointOnCurveDeCasteljauPoints(0.2f);
//			for(int i = 0; i < sp.length; i++)
//			{
//				f.gco().addPoint(new Point(sp[i].x, sp[i].y));
//			}
			resetTool();
		}
		
	}

	public void addLine(Frame f, ParameterBox pb) 
	{
		float[] t1 = f.gco().scaleAndRotateValue(StartX, StartY);
		float[] t2 = f.gco().scaleAndRotateValue(EndX, EndY);
		Point p1 = new Point(t1[0], t1[1]);
		Point p2 = new Point(t2[0], t2[1]);
		if(typeOfStartFilling==0&&typeOfEndFilling==0)
		{
			f.gco().addPoint(p1);
			f.gco().addPoint(p2);
			int l = f.gco().getPointTab().size();
			f.gco().addConnection(new Connection(l-2, l-1,pb.LineSize, pb.LineColor));
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
			f.gco().addPoinToConnection(endConnIndex, EndX, EndY);
			f.gco().addPoint(p1);
			int l = f.gco().getPointTab().size();
			f.gco().addConnection(new Connection(l-2, l-1,pb.LineSize, pb.LineColor));
		}
		else if(typeOfStartFilling==2&&typeOfEndFilling==1)
		{
			System.out.println("1");
			f.gco().addPoinToConnection(startConnIndex, StartX, StartY);
			int l = f.gco().getPointTab().size();
			f.gco().addConnection(new Connection(l-1, endIndex,pb.LineSize, pb.LineColor));
		}
		else if(typeOfStartFilling==1&&typeOfEndFilling==2)
		{
			System.out.println("2");
			f.gco().addPoinToConnection(endConnIndex, EndX, EndY);
			int l = f.gco().getPointTab().size();
			f.gco().addConnection(new Connection(startIndex, l-1,pb.LineSize, pb.LineColor));
		}
		else if(typeOfStartFilling==2&&typeOfEndFilling==2)
		{
			System.out.println("3");
			f.gco().addPoinToConnection(startConnIndex, StartX, StartY);
			f.gco().addPoinToConnection(endConnIndex, EndX, EndY);
			int l = f.gco().getPointTab().size();
			f.gco().addConnection(new Connection(l-2, l-1, pb.LineSize, pb.LineColor));
		}
		FindLinesInserctions fli = new FindLinesInserctions();
		fli.FindInserctionSimpleCall(f);
		frameCupture.hasFrame = false;
		FillManager.needToRefresh = true;
	}
	private void changeStep() {
		step++;
		created = false;
		System.out.println("Krok Tworznia lini: "+step);
	}


	private void initThirdPoint(int x, int y) {
		System.out.println("Inicjowanie kroku 3");
		created = true;
		
		SecondPX = x;
		SecondPY = y;
	}


	private void initSecondPoint(int x, int y) {
		System.out.println("Inicjowanie kroku 2");
		created = true;
		
		FirstPX = x;
		FirstPY = y;
		
	}


	private void refreshToolFirstStep(Frame f, int x, int y, ParameterBox pb, Graphics2D g) 
	{
		int pointIndex = f.gco().getNearestPoint(x, y);
		int lineIndex = f.gco().getNearestConnection(x, y);
		
		if(pointIndex>-10)
		{
			System.out.println("Odnaleziono bliski punkt "+pointIndex);
			float[] t = f.getObj(f.getObject()).getScaledPoint(pointIndex);
			x = (int) t[0];
			y = (int) t[1];
		}
		else if(lineIndex>-10)
		{
			System.out.println("Odnaleziono bliski odcinek "+lineIndex);
			float t1[] = f.gco().getScaledPoint(f.gco().getCon(lineIndex).getP1());
			float t2[] = f.gco().getScaledPoint(f.gco().getCon(lineIndex).getP2());
			float t3[] = tb.getPoinOfCrossABC(tb.generatePatternABC(t1[0], t1[1], t2[0], t2[1]), x, y);
			System.out.println("Punkt przeciêcia z lini¹ w "+t3[0]+" "+t3[1]);
			x = (int) t3[0];
			y = (int) t3[1];
		}
		g.setStroke(new BasicStroke(pb.LineSize));
		g.setColor(pb.LineColor);
		g.draw(new Line2D.Float(StartX, StartY, x, y));
	}


	private void cancelPainting() 
	{
		
	}


	private void initTool(Frame f, int x, int y) 
	{
		System.out.println("Inicjowanie kroku 1");
		int pointIndex = f.gco().getNearestPoint(x, y);
		int lineIndex = f.gco().getNearestConnection(x, y);

		
		if(pointIndex>-10)
		{
			float[] t = f.getObj(f.getObject()).getScaledPoint(pointIndex);
			StartX = t[0];
			StartY = t[1];
			typeOfStartFilling = 1;
			startIndex = pointIndex;
		}
		else if(lineIndex>-10)
		{
			float t1[] = f.gco().getScaledPoint(f.gco().getCon(lineIndex).getP1());
			float t2[] = f.gco().getScaledPoint(f.gco().getCon(lineIndex).getP2());
			float t3[] = tb.getPoinOfCrossABC(tb.generatePatternABC(t1[0], t1[1], t2[0], t2[1]), x, y);
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
		created = true;
	}


	public void resetTool() {
		created = false;
	}

	public boolean isCreated() {
		return created;
	}

}
