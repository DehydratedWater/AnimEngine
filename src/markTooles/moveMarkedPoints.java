package markTooles;

import java.awt.Graphics2D;

import mark.Mark;
import mark.dataBox;
import mark.toolSchem;
import pastManager.PastBox;
import pastManager.StateBox;
import structures.Frame;
import structures.ParameterBox;
import toolBox.frameCupture;

public class moveMarkedPoints implements toolSchem
{
	private boolean created = false;
	private float StartX, StartY;
	private float lastX, lastY;
	public void useTool(Frame f, int x, int y, dataBox db, ParameterBox pb, Graphics2D g, int tool, Mark mark)
	{
		if(created==false)
		{
			if(db.mouseClicked&&!db.mouseRightClicked)
				initTool(f, x, y, mark);
		}
		else
		{
			if(db.mouseRightClicked)
			{
				cancelTool(f, x, y, mark);
				resetTool();
			}
			else
				if(db.mouseClicked)
				{
					refreshTool(f, x, y, mark);

				}
				else
				{
					resetTool();
				}
		}
	}

	
	



	
	private void cancelTool(Frame f, float x, float y, Mark mark) 
	{
		frameCupture.hasFrame = false;
		mark.range[0]-=(x-StartX);
		mark.range[1]-=(y-StartY);
		mark.range[2]-=(x-StartX);
		mark.range[3]-=(y-StartY);
		float tab[] = f.gco().scaleAndRotateValue(x, y);
		float tab2[] = f.gco().scaleAndRotateValue(StartX, StartY);
		float vecX = tab[0]-tab2[0];
		float vecY = tab[1]-tab2[1];

		for(int i = 0; i < mark.tabOfPointIndex.size(); i++)
		{
			f.gco().getNormalPoint(mark.tabOfPointIndex.get(i)).x-=vecX;
			f.gco().getNormalPoint(mark.tabOfPointIndex.get(i)).y-=vecY;
		}
	}



	private void initTool(Frame f, float x, float y, Mark mark)
	{
		if(mark.isMarkedArea&&mark.tabOfPointIndex.size()>0)
		{
		if(mark.range[0]<=x&&mark.range[1]<=y&&mark.range[2]>=x&&mark.range[3]>=y)
		{
			//System.out.println(mark.range[0]+" "+mark.range[1]+" "+mark.range[2]+" "+mark.range[3]);
			created = true;
			PastBox.addStepStart();
			for(int i : mark.tabOfPointIndex)
			{
				PastBox.eventMovPoint(StateBox.frameIndex, StateBox.objIndex, i, f.gco().getNormalPoint(i));
			}
			StartX = x;
			StartY = y;
			lastX = x;
			lastY = y;
		}
		}
	}
	private void refreshTool(Frame f, float x, float y, Mark mark)
	{
		frameCupture.hasFrame = false;
		float tab[] = f.gco().scaleAndRotateValue(x, y);
		float tab2[] = f.gco().scaleAndRotateValue(lastX, lastY);
		float vecX = tab[0]-tab2[0];
		float vecY = tab[1]-tab2[1];

		for(int i = 0; i < mark.tabOfPointIndex.size(); i++)
		{
			f.gco().getNormalPoint(mark.tabOfPointIndex.get(i)).x+=vecX;
			f.gco().getNormalPoint(mark.tabOfPointIndex.get(i)).y+=vecY;
		}
			mark.range[0]+=(x-lastX);
			mark.range[1]+=(y-lastY);
			mark.range[2]+=(x-lastX);
			mark.range[3]+=(y-lastY);
			
			lastX = x;
			lastY = y;
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