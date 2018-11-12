package markTooles;

import java.awt.Graphics2D;
import java.util.ArrayList;

import mark.Mark;
import mark.dataBox;
import mark.toolSchem;
import markManagerPac.MarkManager;
import pastManager.PastBox;
import pastManager.StateBox;
import polygonEngine.FillManager;
import structures.Frame;
import structures.ParameterBox;
import toolBox.QuickSort;
import toolBox.frameCupture;

public class movePolygon implements toolSchem
{
	private boolean created = false;
	private ArrayList<Integer> tab;
	public static int polygonIndex;
	private float lastX, lastY, startX, startY;
	//private float StartX, StartY;
	public void useTool(Frame f, int x, int y, dataBox db, ParameterBox pb, Graphics2D g,
			int tool, Mark mark) 
	{
	
		if(created==false)
		{
			if(db.mouseClicked&&!db.mouseRightClicked)
				initTool(f,x,y, mark, g);
		}
		else
		{
			if(db.mouseRightClicked)
			{
				cancelToolWork(f, mark);
				resetTool();
			}
			else
			{
				if(db.mouseClicked)
				refreshTool(f, x, y, db, mark, g);
				else
				{
				finalizeTool(f, x, y, db, mark);
				resetTool();
				}
			}
			
		}
	}



	private void initTool(Frame f, int x, int y, Mark mark, Graphics2D g)
	{	
		tab = new ArrayList<Integer>();
		
		f.gco().removePointMark();
		f.gco().removeConnectionMark();
		lastX = x;
		lastY = y;
		startX = x;
		startY = y;
		PastBox.addStepStart();
		
		PastBox.eventAllObjectDeform(StateBox.frameIndex, StateBox.objIndex, f.gco());
		tab = f.gco().getPolygon(polygonIndex).markPolygon(f.gco().getPointTab(), f.gco().getConnectionTab());
		QuickSort.SortIntArrayWithRemoveDoubles(tab);
		mark.indexOfMovedPolygon = polygonIndex;
		f.gco().getPolygonToDraw().add(polygonIndex);
		mark.isMarkedPolygon = true;
		created = true;
	}
	private void cancelToolWork(Frame f, Mark mark) {
		float mx = startX-lastX;
		float my = startY-lastY;
		float m[] = f.gco().simplyScaleValue(mx, my);
		moveTabOfPoints(f, m[0], m[1]);
		resetTool();
	}

	private void finalizeTool(Frame f, int x, int y, dataBox db, Mark mark) {
		// TODO Auto-generated method stub
		
	}

	private void refreshTool(Frame f, int x, int y, dataBox db, Mark mark, Graphics2D g) {
		if(!db.DELETE)
		{
		float mx = x-lastX;
		float my = y-lastY;
		float m[] = f.gco().simplyScaleValue(mx, my);
		lastX = x;
		lastY = y;
		//System.out.println(m[0]+" "+m[1]);
		moveTabOfPoints(f, m[0], m[1]);
		//f.gco().getPolygon(polygonIndex).drawPolygon(f, g);
		//f.gco().getPolygon(polygonIndex).movePolygon(f.gco().getPointTab(), m[0], m[1]);
		}
		else
		{
			mark.isMarkedPolygon = false;
			f.gco().removePolygon(mark.indexOfMovedPolygon);
			f.gco().removePointMark();
			f.gco().removeConnectionMark();
			MarkManager.tool = 0;
			resetTool();
		}
		frameCupture.hasFrame = false;
		
		
	}
	
	public void moveTabOfPoints(Frame f, float x, float y)
	{
		for(int i = 0; i < tab.size(); i++)
		{
			f.gco().getNormalPoint(tab.get(i)).x+=x;
			f.gco().getNormalPoint(tab.get(i)).y+=y;
		}
	}
	public void resetTool() 
	{
		FillManager.needToRefresh = true;
		frameCupture.hasFrame = false;
		created = false;
	}

	public boolean isCreated() 
	{
		
		return false;
	}

}
