package rasterPaintTools;

import java.awt.Graphics2D;

import mark.dataBox;
import paintManagerTools.paintToolShem;
import structures.Frame;
import structures.ParameterBox;

public class RasterCurveSegmentTool implements paintToolShem{
public boolean created = false;

	public void useTool(Frame f, int x, int y, dataBox db, ParameterBox pb, Graphics2D g, int tool) 
	{
		if(created==false)
		{
			if(db.mouseClicked&&!db.mouseRightClicked)
				initTool(x, y, db);
		}
		else
		{
			if(db.mouseRightClicked)
			{
				resetTool();
				cancelMarking(f);
			}
			else
				refreshTool(f, x, y, db, g);
		}
	}

	private void initTool(int x, int y, dataBox db) 
	{
		
	}

	private void refreshTool(Frame f, int x, int y, dataBox db, Graphics2D g) 
	{
		
	}

	private void cancelMarking(Frame f) 
	{
		
	}

	public void resetTool() 
	{
		
	}

	public boolean isCreated() 
	{
		return created;
	}

}
