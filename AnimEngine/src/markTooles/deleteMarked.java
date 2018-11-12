package markTooles;

import java.awt.Graphics2D;

import mark.Mark;
import mark.dataBox;
import mark.toolSchem;
import pastManager.PastBox;
import pastManager.StateBox;
import polygonEngine.FillManager;
import structures.Frame;
import structures.ParameterBox;
import toolBox.frameCupture;

public class deleteMarked implements toolSchem
{
	private boolean created = false;
	public void useTool(Frame f, int x, int y, dataBox db, ParameterBox pb, Graphics2D g, int tool, Mark mark)
	{
		if(created==false)
		{
			if(db.DELETE)
				initTool(f, x, y, mark);
		}
		
	}

	private void initTool(Frame f, int x, int y, Mark mark)
	{
		created = true;
		
		PastBox.addStepStart();
		PastBox.eventHugeStructureChange(StateBox.frameIndex, StateBox.objIndex, f.gco());
		
		for(int i = 0; i < f.getObj(f.getObject()).getConnectionTab().size(); i++)
		{
			if(f.getObj(f.getObject()).getCon(i).isMarked())
			{
				
				f.gco().removeCon(i);
				i--;
			}
		}
		for(int i = 0; i < f.getObj(f.getObject()).getPointTab().size(); i++)
		{
			if(f.getObj(f.getObject()).getNormalPoint(i).MarkedPoint)
			{
				f.gco().removePoint(i);
				//f.gco().removePointFormPolygons(i);
				i--;
			}
		}
		f.gco().removeSinglePointsNEW();
		frameCupture.hasFrame = false;
		FillManager.needToRefresh = true;
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