package markTooles;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import mark.Mark;
import mark.dataBox;
import mark.toolSchem;
import pastManager.PastBox;
import pastManager.StateBox;
import renderSource.Point;
import structures.Frame;
import structures.ParameterBox;
import toolBox.frameCupture;
import toolBox.tb;

public class rotatePoints implements toolSchem
{
	private boolean created = false;
	private float startX;
	private ArrayList<Point> markedPoints;
	private float range[];
	private float centerX;
	private float centerY;
	public void useTool(Frame f, int x, int y, dataBox db, ParameterBox pb, Graphics2D g, int tool, Mark mark)
	{
		if(created==false&&!db.mouseRightClicked)
		{
			if(db.mouseClicked)
				initTool(f, x, y, mark);
		}
		else
		{
			if(db.mouseRightClicked)
			{
				cancelTool(f, mark);
				resetTool();
			}
			else
				if(db.mouseClicked)
					refreshTool(f, x, y, mark, g);
				else
					resetTool();
		}
	}

	private void initTool(Frame f, int x, int y, Mark mark)
	{
		frameCupture.hasFrame = false;
		System.out.println("Rozpoczynanie");
		if(clickedOnRotatingPoints(x, y, mark)>-10&&mark.tabOfPointIndex.size()>0)
		{
			PastBox.addStepStart();
			for(int i : mark.tabOfPointIndex)
			{
				PastBox.eventMovPoint(StateBox.frameIndex, StateBox.objIndex, i, f.gco().getNormalPoint(i));
			}
			System.out.println("Zainicjowano obracanie");
			created = true;
			range = mark.range.clone();
			startX = x;
			centerX = (mark.range[0]+mark.range[2])/2;
			centerY = (mark.range[1]+mark.range[3])/2;
			markedPoints = new ArrayList<Point>();
			for(int i = 0; i < mark.tabOfPointIndex.size(); i++)
			{
				markedPoints.add(new Point(f.gco().getNormalPoint(mark.tabOfPointIndex.get(i))));
			}
			System.out.println("zakoñczono inicjalizacjê");
		}
	}
	private void refreshTool(Frame f, int x, int y, Mark mark, Graphics2D g)
	{
		System.out.println("Odœwierzanie");
		frameCupture.hasFrame = false;
		g.setColor(Color.GREEN);
		g.drawOval((int)centerX-2, (int)centerY-2, 4, 4);
		float dist = (x-startX)%360;
		rotateMarkedPoints(f, dist, centerX, centerY, mark);
		optymalizeMark(f, mark);
	}
	
	private void optymalizeMark(Frame f, Mark mark)
	{
		
		if(mark.tabOfPointIndex.size()>0)
		{
			float maxX, maxY, minX, minY;
			Point P = f.gco().getNormalPoint(mark.tabOfPointIndex.get(0));
			maxX = P.x;
			maxY = P.y;
			minX = P.x;
			minY = P.y;
			for(int i = 0; i < mark.tabOfPointIndex.size(); i++)
			{
				Point p = f.gco().getNormalPoint(mark.tabOfPointIndex.get(i));
				if(p.x>maxX)
					maxX = p.x;
				if(p.y>maxY)
					maxY = p.y;
				if(p.x<minX)
					minX = p.x;
				if(p.y<minY)
					minY = p.y;
			}
			mark.range[0] = f.gco().scaleValue(minX, 0)[0]-10;
			mark.range[1] = f.gco().scaleValue(0, minY)[1]-10;
			mark.range[2] = f.gco().scaleValue(maxX, 0)[0]+10;
			mark.range[3] = f.gco().scaleValue(0, maxY)[1]+10;
		}
	}
	

	public void cancelTool(Frame f, Mark mark)
	{
		frameCupture.hasFrame = false;
		mark.range = range.clone();
		for(int i = 0; i < markedPoints.size(); i++)
		{
			f.gco().getNormalPoint(mark.tabOfPointIndex.get(i)).x=markedPoints.get(i).x;
			f.gco().getNormalPoint(mark.tabOfPointIndex.get(i)).y=markedPoints.get(i).y;
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
	private int clickedOnRotatingPoints(float x, float y, Mark mark) 
	{
		if(checkIfClickedOnPoint(x, y, mark.range[0]-10, mark.range[1]-10,5))
			return 1;
		if(checkIfClickedOnPoint(x, y, mark.range[2]+5, mark.range[1]-10,5))
			return 2;
		if(checkIfClickedOnPoint(x, y, mark.range[2]+5, mark.range[3]+5,5))
			return 3;
		if(checkIfClickedOnPoint(x, y, mark.range[0]-10, mark.range[3]+5,5))
			return 4;
		return -10;
	}
	private boolean checkIfClickedOnPoint(float x, float y, float px, float py, float range)
	{
		if(tb.distance(x, y, px, py)<range)
			return true;
		else
			return false;
	}
	
	private void rotateMarkedPoints(Frame f, float align, float centerX, float centerY, Mark mark)
	{
		float scale = f.getObj(f.getObject()).getWindowScale();
		float X = f.getObj(f.getObject()).getX();
		float Y = f.getObj(f.getObject()).getY();
		float mx = (centerX-X)/scale;
		float my = (centerY-Y)/scale;
		
		for(int i = 0; i < mark.tabOfPointIndex.size(); i++)
		{
			float nx = (float) ((markedPoints.get(i).x-mx)*Math.cos(Math.toRadians(align)) - (markedPoints.get(i).y-my)*Math.sin(Math.toRadians(align)) + mx);
			float ny = (float) ((markedPoints.get(i).x-mx)*Math.sin(Math.toRadians(align)) + (markedPoints.get(i).y-my)*Math.cos(Math.toRadians(align)) + my);
				f.getObj(f.getObject()).getNormalPoint(mark.tabOfPointIndex.get(i)).x=nx;
				f.getObj(f.getObject()).getNormalPoint(mark.tabOfPointIndex.get(i)).y=ny;
		}
	}
}