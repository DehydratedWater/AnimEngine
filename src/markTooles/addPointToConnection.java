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

public class addPointToConnection implements toolSchem
{
	private boolean created = false;
	public void useTool(Frame f, int x, int y, dataBox db, ParameterBox pb, Graphics2D g, int tool, Mark mark)
	{
		if(created==false)
		{
			if(db.mouseClicked)
				initTool(f,x,y, tool, db, mark);
		}
		else
		{
			if(db.mouseRightClicked)
			{
				cancelTool();
				resetTool();
			}
			else
				refreshTool(f,x,y,tool,db,mark, g);
		}
	}

	private void initTool(Frame f, int x, int y, int tool, dataBox db, Mark mark)
	{
		created = true;
		int k = f.getObj(f.getObject()).getNearestConnection(x, y);
		if(k>-1)
		{
		f.getObj(f.getObject()).addPoinToConnection(k, x, y);
		mark.isMarkedConn = false;
		}
		frameCupture.hasFrame = false;
		FillManager.needToRefresh = true;
	}
	private void refreshTool(Frame f, int x, int y, int tool, dataBox db, Mark mark, Graphics2D g)
	{
		frameCupture.hasFrame = false;
		MarkManager.tool = ContectTool.setTool(f, x, y, db, tool, mark, g);
	}
	public void cancelTool()
	{
		
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