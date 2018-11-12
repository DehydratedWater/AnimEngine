package markTooles;

import java.awt.Graphics2D;

import mark.Mark;
import mark.dataBox;
import mark.toolSchem;
import markManagerPac.ContectTool;
import markManagerPac.MarkManager;
import polygonEngine.FillManager;
import structures.Frame;
import structures.ParameterBox;
import toolBox.frameCupture;

public class cutConnection implements toolSchem
{
	private boolean created = false;
	public void useTool(Frame f, int x, int y, dataBox db, ParameterBox pb, Graphics2D g, int tool, Mark mark)
	{
		if(created==false)
		{
			if(db.mouseClicked&&!db.mouseRightClicked)
				initTool(f, x, y, db, mark, g);
		}
		else
		{
			if(db.mouseRightClicked)
				resetTool();
			else
				refreshTool();
		}
	}

	private void initTool(Frame f, int x, int y, dataBox db, Mark mark, Graphics2D g)
	{
	int k = f.getObj(f.getObject()).getNearestConnection(x, y);
	if(k>-1)
	{
	f.getObj(f.getObject()).catConnection(k, x, y);
	created = true;
	frameCupture.hasFrame = false;
	}
	else
	{
		MarkManager.tool = ContectTool.setTool(f, x, y, db, MarkManager.tool, mark, g);
	}
	FillManager.needToRefresh = true;
	}
	private void refreshTool()
	{
		MarkManager.tool = 1;
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