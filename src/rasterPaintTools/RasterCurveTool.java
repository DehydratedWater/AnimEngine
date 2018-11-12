package rasterPaintTools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Line2D;
import java.awt.geom.QuadCurve2D;

import main.Window;
import mark.dataBox;
import paintManagerTools.paintToolShem;
import structures.Frame;
import structures.ParameterBox;
import toolBox.frameCupture;

public class RasterCurveTool implements paintToolShem{
	private boolean created = false;
	private int step = 0;
	private float StartX, StartY, EndX, EndY;
	private float FirstPX, FirstPY;
	private float SecondPX, SecondPY;

	private boolean back = false;
	public void useTool(Frame f, int x, int y, dataBox db, ParameterBox pb, Graphics2D g, int tool) 
	{
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
		
			EndX = x;
			EndY = y;
		
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
			System.out.println("Rysowanie na bitmapie");
			Graphics2D g = (Graphics2D) Window.tm.getTexture(f.gcb().getBitmapIndex()).texture.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			g.setStroke(new BasicStroke(pb.LineSize));
			g.setColor(pb.LineColor);
			CubicCurve2D cd = new CubicCurve2D.Float();
			float t1[] = f.gcb().scaleAndRotateValue(StartX, StartY);
			float t2[] = f.gcb().scaleAndRotateValue(SecondPX, SecondPY);
			float t3[] = f.gcb().scaleAndRotateValue(FirstPX, FirstPY);
			float t4[] = f.gcb().scaleAndRotateValue(EndX, EndY);
			cd.setCurve(t1[0], t1[1], t2[0], t2[1], t3[0], t3[1], t4[0], t4[1]);
			g.draw(cd);
			frameCupture.hasFrame = false;
			step = 0;
			resetTool();
		}
		
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
			StartX = x;
			StartY = y;
			
		created = true;
	}

	public boolean isCreated() {
		return created;
	}

	@Override
	public void resetTool() {
		created = false;
		
	}

}
