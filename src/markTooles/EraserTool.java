package markTooles;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import mark.Mark;
import mark.dataBox;
import mark.toolSchem;
import structures.Frame;
import structures.ParameterBox;
import toolBox.frameCupture;
import toolBox.tb;

public class EraserTool implements toolSchem
{
	private boolean created = false;
	public static float range = 20f;
	public void useTool(Frame f, int x, int y, dataBox db, ParameterBox pb, Graphics2D g, int tool, Mark mark) 
	{
		drawCircle(x, y, g);
		refreshTool(f, x, y, db, pb, g);
	}


	
	private void refreshTool(Frame f, int x, int y, dataBox db, ParameterBox pb, Graphics2D g) 
	{
		range = pb.EraserSize*10;
		float[] t = f.gco().scaleAndRotateValue(x, y);
		ArrayList<Integer> pointList = getPointsInRange(f, t[0], t[1], range);
		if(pointList.size()>0)
		{
		if(!db.mouseClicked)
		{
//		f.gco().removePointMark();
//		for(int i : pointList)
//			f.gco().getNormalPoint(i).MarkedPoint = true;
//		frameCupture.hasFrame = false;
		}
		else
		{
			f.gco().removePointsNotSorted(pointList);
			f.gco().removeSinglePointsNEW();
			frameCupture.hasFrame = false;
		}
		}
		
	}

	private void drawCircle(int x, int y, Graphics2D g) {
		g.setColor(Color.black);
		g.setStroke(new BasicStroke(1));
		g.drawOval((int)(x-range/2), (int)(y-range/2), (int)range, (int)range);
	}

	public ArrayList<Integer> getPointsInRange(Frame f, float x, float y, float range)
	{
		range = f.gco().simplyScaleValue(range, 0)[0];
		ArrayList<Integer> listTab = new ArrayList<Integer>();
		for(int i = 0; i < f.gco().getPointTabSize(); i++)
		{
			if(tb.distance(x, y, f.gco().getNormalPoint(i).x, f.gco().getNormalPoint(i).y) <= range/2)
			{
				listTab.add(i);
			}
		}
		return listTab;
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
