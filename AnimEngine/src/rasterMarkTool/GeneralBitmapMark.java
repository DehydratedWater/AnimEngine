package rasterMarkTool;

import java.awt.Graphics2D;

import bitmapEngine.Bitmap;
import mark.Mark;
import mark.dataBox;
import mark.toolSchem;
import structures.Frame;
import structures.ParameterBox;
import toolBox.frameCupture;

public class GeneralBitmapMark implements toolSchem
{
	private boolean create = false;
	private int StartX, StartY;
	private int lastX, lastY;
	public void useTool(Frame f, int x, int y, dataBox db, ParameterBox pb, Graphics2D g, int tool, Mark mark) {
		if(create==false)
		{
			if(db.mouseClicked&&!db.mouseRightClicked)
			init(f, x, y);
		}
		else
		{
			if(db.mouseClicked)
			{
				refresh(f, x, y);
			}
			else if(!db.mouseClicked)
			{
				refresh(f, x, y);
				resetTool();
			}
			else if(db.mouseRightClicked)
			{
				cancel(f, x, y);
			}
				
		}
	}

	private void cancel(Frame f, int x, int y) 
	{
		Bitmap b = f.gcb();
		float xy[] = b.onlyScaleValue(x, y);
		float sxy[] = b.onlyScaleValue(StartX, StartY);
		int vx = (int) (xy[0]-sxy[0]);
		int vy = (int) (xy[1]-sxy[1]);
		
		b.modX(vx);
		b.modY(vy);
		
		lastX = x;
		lastY = y;
		resetTool();
	}

	private void refresh(Frame f, int x, int y) {
		Bitmap b = f.gcb();
		float xy[] = b.onlyScaleValue(x, y);
		float sxy[] = b.onlyScaleValue(lastX, lastY);
		int vx = (int) (xy[0]-sxy[0]);
		int vy = (int) (xy[1]-sxy[1]);
		b.modX(vx);
		b.modY(vy);
		
		lastX = x;
		lastY = y;
		frameCupture.hasFrame = false;
	}

	private void init(Frame f, int x, int y) {
		if(f.gcb().isBitmapClicked(x, y))
		{
		StartX = x;
		StartY = y;
		lastX = x;
		lastY = y;
		create = true;
		}
	}

	public void resetTool() {
		create = false;
	}

	public boolean isCreated() {
		return create;
	}

}
