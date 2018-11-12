package markTooles;

import java.awt.Graphics2D;

import mark.Mark;
import mark.dataBox;
import mark.toolSchem;
import structures.Frame;
import structures.ParameterBox;
import toolBox.frameCupture;

public class setLineParameters implements toolSchem
{
	private boolean created = false;
	public void useTool(Frame f, int x, int y, dataBox db, ParameterBox pb, Graphics2D g, int tool, Mark mark)
	{
		if(created==false)
		{
			if(db.mouseClicked&&!db.mouseRightClicked)
				initTool(f, x, y, pb);
		}
		else
		{
			if(db.mouseRightClicked)
				resetTool();
			else
				refreshTool(db);
		}
	}

	private void initTool(Frame f, int x, int y, ParameterBox pb)
	{
		frameCupture.hasFrame = false;
		int k = f.getObj(f.getObject()).getNearestConnection(x, y);
		System.out.println("Ustawianie parametrów "+k+" "+pb.LineColor+" "+pb.LineSize);
		if(k>-1)
		{
			created = true;
			f.gco().getCon(k).setC(pb.LineColor);
			f.gco().getCon(k).setSize(pb.LineSize);

		}
	}
	private void refreshTool(dataBox db)
	{
		if(!db.mouseClicked)
		{
			resetTool();
		}
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