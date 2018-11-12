package markTooles;

import java.awt.Graphics2D;

import mark.Mark;
import mark.dataBox;
import mark.toolSchem;
import polygonEngine.FillManager;
import renderSource.Connection;
import structures.Frame;
import structures.ParameterBox;
import toolBox.frameCupture;

public class separateConnection implements toolSchem
{
	private boolean created = false;
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
				resetTool();
			else
				refreshTool(db, f);
		}
	}

	private void initTool(Frame f, int x, int y, Mark mark)
	{
		frameCupture.hasFrame = false;
		created = true;
		int k = f.gco().getNearestConnection(x, y);
		if(k>-1)
		{
			if(mark.isMarkedConn)
			{
				System.out.println("resetowanie zaznaczenia dla "+mark.indexOfMovedConn);
				f.gco().getCon(mark.indexOfMovedConn).setMarked(false);
				mark.isMarkedConn=false;
			}
			
			Connection c = f.gco().getCon(k);
			int p1 = c.getP1();
			int p2 = c.getP2();
			
			int connInd = f.gco().separateConection(k);
			mark.indexOfMovedConn = connInd;
			mark.isMarkedConn=true;
			f.gco().removePointsIfTheyAreSingle(p1, p2);
			FillManager.needToRefresh = true;
		}
	}
	private void refreshTool(dataBox db, Frame f)
	{
		frameCupture.hasFrame = false;
		if(!db.mouseClicked)
		{
			f.gco().removeConnectionMark();
			created = false;
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