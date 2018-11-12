package markTooles;

import java.awt.Graphics2D;

import mark.Mark;
import mark.dataBox;
import mark.toolSchem;
import markManagerPac.MarkManager;
import structures.Frame;
import structures.ParameterBox;

public class getLineParameters implements toolSchem
{
	private boolean created = false;
	public void useTool(Frame f, int x, int y, dataBox db, ParameterBox pb, Graphics2D g, int tool, Mark mark)
	{
		if(created==false)
		{
			if(db.mouseClicked)
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
		
		int k = f.gco().getNearestConnection(x, y);
		System.out.println("Pobieranie parametrów z odcinka "+k);
		if(k>-1)
		{
			created = true;
			System.out.println("Zmian a koloru ");
			MarkManager.toUbdate = true;
			pb.LineColor=f.gco().getCon(k).getC();
			pb.LineSize=f.gco().getCon(k).getSize();
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