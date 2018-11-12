package markTooles;

import java.awt.Graphics2D;
import java.util.ArrayList;

import mark.Mark;
import mark.dataBox;
import mark.toolSchem;
import pastManager.PastBox;
import pastManager.StateBox;
import polygonEngine.FillManager;
import renderSource.Point;
import structures.Frame;
import structures.ParameterBox;
import toolBox.frameCupture;
import toolBox.tb;

public class movePoint implements toolSchem
{
	private boolean created = false;
	private float StartX, StartY;
	private int pointIndex;
	public void useTool(Frame f, int x, int y, dataBox db, ParameterBox pb, Graphics2D g, int tool, Mark mark)
	{
		//System.out.println("Narzêdzie do przesuwania punktu");
		if(created==false)
		{
			if(db.mouseClicked&&!db.mouseRightClicked)
				initTool(f,x,y, mark);
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
				refreshTool(f, x, y, db);
				else
				{
				finalizeTool(f, x, y, db, mark);
				resetTool();
				}
			}
			
		}
	}

	private void cancelToolWork(Frame f, Mark mark) 
	{
		f.gco().getNormalPoint(pointIndex).MarkedPoint=false;
		f.gco().getNormalPoint(pointIndex).x = StartX;
		f.gco().getNormalPoint(pointIndex).y = StartY;
		mark.isMarkedPoint = false;
	}

	private void finalizeTool(Frame f, int x, int y, dataBox db, Mark mark) 
	{
		if(!(db.CTRL||db.M))
		{
		int nearestPointIndex = f.gco().getNearestPointExceptPoint(x, y, pointIndex);
		int nearestConIndex = f.gco().getNearestConnectionExceptPoint(x, y, pointIndex);
		System.out.println("Koñczenie ³¹czeniem punktów "+pointIndex+" "+nearestPointIndex);
		if(nearestPointIndex>-1)
		{
			f.gco().mergePoints(pointIndex, nearestPointIndex);
			if(pointIndex>nearestPointIndex)
				mark.indexOfMovedPoint = nearestPointIndex;
			else
				mark.indexOfMovedPoint = nearestPointIndex-1;
			f.gco().reAddAllConnectionesConnectedToPoint(mark.indexOfMovedPoint);
		}
		else if(nearestConIndex>-10)
		{
			float t1[] = f.gco().getScaledPoint(f.gco().getCon(nearestConIndex).getP1());
			float t2[] = f.gco().getScaledPoint(f.gco().getCon(nearestConIndex).getP2());
			float t3[] = tb.getPoinOfCrossABC(tb.generatePatternABC(t1[0], t1[1], t2[0], t2[1]), x, y);
			t3 = f.gco().scaleAndRotateValue(t3[0], t3[1]);
			f.gco().getNormalPoint(pointIndex).x = t3[0];
			f.gco().getNormalPoint(pointIndex).y = t3[1];
			f.gco().addPoinToConnectionByIndex(nearestConIndex, pointIndex);
		}
		}
		else if(db.M)
		{
			System.out.println("£¹czenie punktów");
			mergeMovedPoint(f, 15, pointIndex);
			f.gco().reAddAllConnectionesConnectedToPoint(mark.indexOfMovedPoint);
		}
		FillManager.needToRefresh = true;
	}

	private void initTool(Frame f, int x, int y, Mark mark)
	{	
		pointIndex = f.gco().getNearestPoint(x, y);
		
		if(pointIndex>-1)
		{
			PastBox.addStepStart();
			PastBox.eventMovPoint(StateBox.frameIndex, StateBox.objIndex, pointIndex, f.gco().getNormalPoint(pointIndex));
			mark.indexOfMovedPoint = pointIndex;
			mark.isMarkedPoint = true;
			created = true;
			Point p = f.gco().getNormalPoint(pointIndex);
			StartX = p.x;
			StartY = p.y;
		}
		frameCupture.hasFrame = false;
	}
	
	private void refreshTool(Frame f, int x, int y, dataBox db)
	{
		frameCupture.hasFrame = false;
		if(db.mouseClicked)
		{
			
			int nearestPointIndex = f.gco().getNearestPointExceptPoint(x, y, pointIndex);
			
			int nearestConIndex = f.gco().getNearestConnectionExceptPoint(x, y, pointIndex);

			if(!db.CTRL&&nearestPointIndex>-10)
			{
				Point tab = f.gco().getNormalPoint(nearestPointIndex);
				f.gco().getNormalPoint(pointIndex).x = tab.x;
				f.gco().getNormalPoint(pointIndex).y = tab.y;
			}
			else if(!db.CTRL&&nearestConIndex>-10)
			{
				float t1[] = f.gco().getScaledPoint(f.gco().getCon(nearestConIndex).getP1());
				float t2[] = f.gco().getScaledPoint(f.gco().getCon(nearestConIndex).getP2());
				float t3[] = tb.getPoinOfCrossABC(tb.generatePatternABC(t1[0], t1[1], t2[0], t2[1]), x, y);
				t3 = f.gco().scaleAndRotateValue(t3[0], t3[1]);
				f.gco().getNormalPoint(pointIndex).x = t3[0];
				f.gco().getNormalPoint(pointIndex).y = t3[1];
			}
			else
			{
				//System.out.println("WYKONANO STD");
				float t[] = f.gco().scaleAndRotateValue(x, y);
				f.gco().getNormalPoint(pointIndex).x = t[0];
				f.gco().getNormalPoint(pointIndex).y = t[1];
			}
		}
		else
		{
			resetTool();
		}
	}
	
	public void mergeMovedPoint(Frame f, float range, int pointIndex)
	{
		ArrayList<Integer> pointTab = f.gco().getNearestPointListExceptPoint(pointIndex, range);
		if(pointTab!=null)
		{
		System.out.println("index "+pointIndex);
		for(int index:pointTab)
		{
			System.out.print(index+" ");
		}
		f.gco().mergePointList(pointIndex, pointTab);
		System.out.println();
		}
	}
	
	public void resetTool() 
	{
		frameCupture.hasFrame = false;
		created = false;
	}

	public boolean isCreated() 
	{
		return created;
	}
	
}