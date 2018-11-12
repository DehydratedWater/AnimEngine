package rasterPaintTools;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;

import main.Window;
import mark.dataBox;
import paintManagerTools.paintToolShem;
import structures.Frame;
import structures.ParameterBox;
import toolBox.frameCupture;

public class RasterLineTool implements paintToolShem{
public boolean created = false;
private float startX, startY;
	public void useTool(Frame f, int x, int y, dataBox db, ParameterBox pb, Graphics2D g, int tool) 
	{

		if(created==false)
		{
			if(db.mouseClicked&&!db.mouseRightClicked)
				initTool(x, y);
		}
		else
		{
			if(db.mouseRightClicked)
			{
				resetTool();
				cancelMarking(f);
			}
			else
				refreshTool(f, x, y, db, pb, g);
		}
	}

	private void initTool(int x, int y) 
	{
		System.out.println("Inicjowanie narzêdzia do rysowania rastrowego");
		startX = x;
		startY = y;
		created = true;
	}

	private void refreshTool(Frame f, int x, int y, dataBox db, ParameterBox pb, Graphics2D g) 
	{
		if(db.mouseRightClicked)
		{
			cancelMarking(f);
			return;
			
		}
		
		if(db.mouseClicked)
		{

			g.setColor(pb.LineColor);
			g.setStroke(new BasicStroke(pb.LineSize*Window.Scale));
			g.draw(new Line2D.Float(startX, startY, x, y));
		}
		else
		{
			System.out.println("Rysowanie na bitmapie");
			Graphics2D g2 = (Graphics2D) Window.tm.getTexture(f.gcb().getBitmapIndex()).texture.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(pb.LineColor);
			g2.setStroke(new BasicStroke(pb.LineSize));
			float t1[] = f.gcb().scaleAndRotateValue(x, y);
			float t2[] = f.gcb().scaleAndRotateValue(startX, startY);
			g2.draw(new Line2D.Float(t2[0], t2[1], t1[0], t1[1]));
			
			cancelMarking(f);
			frameCupture.hasFrame = false;
			return;
		}
	}

	private void cancelMarking(Frame f) 
	{
		resetTool();
	}

	public void resetTool() 
	{
		created = false;
	}

	public boolean isCreated() 
	{
		return created;
	}

}
