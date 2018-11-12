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

public class scalePoints implements toolSchem
{
	private boolean created = false;
	private int clickedPoint;
	private float[] range;
	private ArrayList<Point> markedPoints;
	public void useTool(Frame f, int x, int y, dataBox db, ParameterBox pb, Graphics2D g, int tool, Mark mark)
	{
		//System.out.println("Wykonywanie UseTOOL");
		if(created==false)
		{
			if(db.mouseClicked&&!db.mouseRightClicked)
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
				{
					//getRageOfMarked(f, mark);
					optymalizeMark(f, mark);
					resetTool();
				}
		}
	}

	private void initTool(Frame f, int x, int y, Mark mark)
	{
		frameCupture.hasFrame = false;
		
		int indexOfClickedPoint = clickedOnPoints(x, y, mark);
		//System.out.println("Szukanie punktu "+indexOfClickedPoint);
		if(indexOfClickedPoint>-10)
		{
			PastBox.addStepStart();
			for(int i : mark.tabOfPointIndex)
			{
				PastBox.eventMovPoint(StateBox.frameIndex, StateBox.objIndex, i, f.gco().getNormalPoint(i));
			}
			//System.out.println("Tworznie sklaowania");
			created = true;
			markedPoints = new ArrayList<Point>();
			clickedPoint = indexOfClickedPoint;
			for(int i = 0; i < mark.tabOfPointIndex.size(); i++)
			{
				markedPoints.add(new Point(f.gco().getNormalPoint(mark.tabOfPointIndex.get(i))));
			}
			range = mark.range.clone();			
		}
	}
	private void refreshTool(Frame f, float x, float y, Mark mark, Graphics2D g)
	{
		frameCupture.hasFrame = false;
		//System.out.println("Skalowanie");
		mark.range = range.clone();
		float centerX = (mark.range[0]+mark.range[2])/2;
		float centerY = (mark.range[1]+mark.range[3])/2;
		
		float lenghtX = Math.abs(mark.range[0] - mark.range[2]);
		float lenghtY = Math.abs(mark.range[1] - mark.range[3]);
		
		
		
		if(clickedPoint==1)
		{
			mark.range[0] = x;
			mark.range[1] = y;
		}
		else if(clickedPoint==2)
		{
			mark.range[2] = x;
			mark.range[1] = y;
		}
		else if(clickedPoint==3)
		{
			mark.range[2] = x;
			mark.range[3] = y;
		}
		else if(clickedPoint==4)
		{
			mark.range[0] = x;
			mark.range[3] = y;
		}
		
		
		float newCenterX = (mark.range[0]+mark.range[2])/2;
		float newCenterY = (mark.range[1]+mark.range[3])/2;
		g.setColor(Color.GREEN);
		g.drawOval((int)newCenterX-2, (int)newCenterY-2, 4, 4);
		if(lenghtX<1)
		{
			lenghtX = 1f;
		}
		if(lenghtY<1)
		{
			lenghtY = 1f;
		}
		float scaleX = Math.abs(mark.range[0] - mark.range[2])/lenghtX;
		float scaleY = Math.abs(mark.range[1] - mark.range[3])/lenghtY;
//		if(mark.range[0]==mark.range[2])
//		{
//			scaleX = 1;
//			newCenterX = centerX;
//		}
//		if(mark.range[1]==mark.range[3])
//		{
//			scaleY = 1;
//			newCenterY = centerY;
//		}
		//System.out.println(scaleX+" "+scaleY+" "+centerX+" "+centerY+" "+(newCenterX-centerX)+" "+(newCenterY-centerY));
		scaleMarkedPoints(f, scaleX, scaleY, centerX, centerY, newCenterX-centerX, newCenterY-centerY, mark);
	}
	
	public void resetTool() 
	{
		created = false;
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
	public boolean isCreated() 
	{
		return created;
	}
	private int clickedOnPoints(float x, float y, Mark mark) 
	{
		if(checkIfClickedOnPoint(x, y, mark.range[0], mark.range[1]))
			return 1;
		if(checkIfClickedOnPoint(x, y, mark.range[2], mark.range[1]))
			return 2;
		if(checkIfClickedOnPoint(x, y, mark.range[2], mark.range[3]))
			return 3;
		if(checkIfClickedOnPoint(x, y, mark.range[0], mark.range[3]))
			return 4;
		return -10;
	}
	
	private boolean checkIfClickedOnPoint(float x, float y, float px, float py)
	{
		if(tb.distance(x, y, px, py)<15)
			return true;
		else
			return false;
	}
	//scaleMarkedPoints(f, ScaleX, ScaleY,CenterX,CenterY,pointX-CenterX,pointY-CenterY);
	private void scaleMarkedPoints(Frame f, float ScaleX, float ScaleY, float centerX, float centerY, float newCenterX, float newCenterY, Mark mark)
	{
		//System.out.println("Funakcja skaluj¹ca");
		float scale = f.gco().getWindowScale();
		float X = f.gco().getX();
		float Y = f.gco().getY();
		float mx = (centerX-X)/scale;
		float my = (centerY-Y)/scale;
		float sx = newCenterX/scale;
		float sy = newCenterY/scale;
		for(int i = 0; i < markedPoints.size(); i++)
		{
			f.gco().getNormalPoint(mark.tabOfPointIndex.get(i)).x=ScaleX*(markedPoints.get(i).x-mx)+mx+sx;
			f.gco().getNormalPoint(mark.tabOfPointIndex.get(i)).y=ScaleY*(markedPoints.get(i).y-my)+my+sy;
		}
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

	public void getRageOfMarked(Frame f, Mark mark)
	{

		if(f.gco().getConnectionTab().size()>0)
		{
			
		float MaxX = -100000;
		float MaxY = -100000;
		float MinX = 100000;
		float MinY = 100000;
		for(int i = 0; i < f.gco().getConnectionTab().size(); i ++)
		{
			if(f.gco().getCon(i).isMarked()==true)
			{

				float[] A = f.gco().getScaledPoint(f.gco().getCon(i).getP1());
				float[] B = f.gco().getScaledPoint(f.gco().getCon(i).getP2());
				//System.out.println("Znaleziono nowy odcinek "+i+" "+A[0]+" "+A[1]+" I "+B[0]+" "+B[1]);
				if(A[0]>MaxX)
					MaxX = A[0];
				if(B[0]>MaxX)
					MaxX = B[0];
				if(A[1]>MaxY)
					MaxY = A[1];
				if(B[1]>MaxY)
					MaxY = B[1];
				if(A[0]<MinX)
					MinX = A[0];
				if(B[0]<MinX)
					MinX = B[0];
				if(A[1]<MinY)
					MinY = A[1];
				if(B[1]<MinY)
					MinY = B[1];
			}
		}

		mark.range[2] = MaxX+10;
		mark.range[3] = MaxY+10;
		mark.range[0] = MinX-10;
		mark.range[1] = MinY-10;
		//System.out.println("Wspó³¿êdne zaznaczenia "+MaxX+" "+MaxY+" "+MinX+" "+MinY);
		}

	}
}