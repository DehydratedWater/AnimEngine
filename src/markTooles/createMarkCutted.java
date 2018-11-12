package markTooles;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import mark.Mark;
import mark.dataBox;
import mark.toolSchem;
import renderSource.Connection;
import structures.Frame;
import structures.ParameterBox;
import toolBox.frameCupture;

public class createMarkCutted implements toolSchem
{
	private boolean created = false;
	private float StartX, StartY, EndX, EndY;
	public static boolean showMarked = false;

	public void useTool(Frame f, int x, int y, dataBox db, ParameterBox pb, Graphics2D g, int tool, Mark mark)
	{
		if(created==false)
		{
			if(db.mouseClicked&&!db.mouseRightClicked)
				initTool(f, x, y, mark, db);
		}
		else
		{
			if(db.mouseRightClicked)
			{
				resetTool();
				cancelMarking(f, mark);
			}
			else
				refreshTool(f, x, y, db, g, mark);
		}
	}

	private void initTool(Frame f, int x, int y, Mark mark, dataBox db)
	{
		System.out.println("Tworzenie zaznaczenia");

		
		if(!db.CTRL)
		{
			f.gco().removeConnectionMark();
			f.gco().removePointMark();
		}
		mark.tabOfConnIndex = new ArrayList<Integer>();
		mark.tabOfPointIndex = new ArrayList<Integer>();
		created = true;
		StartX = x;
		StartY = y;
		
	}
	private void refreshTool(Frame f, int x, int y, dataBox db, Graphics2D g, Mark mark)
	{
		if(db.mouseClicked)
		{
			
			EndX = x;
			EndY = y;
			if(showMarked)
			{
				frameCupture.hasFrame = false;
			float range[] = generateRangeTab();
			findMarkedPoints(f, mark, db, range);
			}
			drawMark(g);
		}
		else
		{
			frameCupture.hasFrame = false;
			float range[] = null;
			
			range = generateRangeTab();
			findMarkedPoints(f, mark, db, range);
				
			
			if(range!=null)
			{
			StartX = range[0];
			StartY = range[1];
			EndX = range[2];
			EndY = range[3];
			}
			
			mark.isMarkedArea = true;
			mark.range = generateRangeTab();
			moveMarkedPointsCutted.isReadyToCut = true;
			System.out.println("Zakres Koñcowy "+mark.range[0]+" "+mark.range[1]+" "+mark.range[2]+" "+mark.range[3]);
			drawMark(g);
			resetTool();
		}
	}
	

	private void findMarkedPoints(Frame f, Mark mark, dataBox db, float range[])
	{
		mark.tabOfPointIndex = new ArrayList<Integer>();
		mark.tabOfConnIndex = new ArrayList<Integer>();
		boolean[] checkTab = new boolean[f.gco().getPointTab().size()];
		for(int i = 0; i < f.gco().getPointTab().size(); i++)
		{
			float[] t = f.gco().getScaledPoint(i);
			if(t[0]>=range[0]&&t[0]<=range[2]&&t[1]>=range[1]&&t[1]<=range[3])
			{
				f.gco().getNormalPoint(i).MarkedPoint = true;
				mark.tabOfPointIndex.add(i);
				checkTab[i] = true;
			}
			else
			{
				if(db.CTRL==false)
				{
					f.gco().getNormalPoint(i).MarkedPoint = false;
				}
				else
				{
					if(f.gco().getNormalPoint(i).MarkedPoint)
						mark.tabOfPointIndex.add(i);
				}
			}
		}
		
		for(int i = 0; i < f.gco().getConnectionTab().size(); i++)
		{
			Connection c = f.gco().getCon(i);
			if(checkTab[c.getP1()]==true||checkTab[c.getP2()]==true)
			{
				f.gco().getCon(i).setMarked(true);
				mark.tabOfConnIndex.add(i);
			}
			else
			{
				if(db.CTRL==false)
				{
					f.gco().getCon(i).setMarked(false);
				}
				else
				{
					if(f.gco().getCon(i).isMarked())
						mark.tabOfConnIndex.add(i);
				}
			}
		}
		//System.out.println("Znaleziono "+mark.tabOfPointIndex.size()+" punktów i "+mark.tabOfConnIndex.size()+" odcinków");
	}
	
	private void drawMark(Graphics2D g)
	{
		float dash1[] = {10.0f};

		BasicStroke s = new BasicStroke(1.5f, BasicStroke.CAP_ROUND,  BasicStroke.JOIN_ROUND, 10.0f, dash1, 0.0f);
		g.setStroke(s);
		g.setColor(Color.BLACK);
		g.draw(new Line2D.Float(StartX,StartY,StartX,EndY));
		g.draw(new Line2D.Float(StartX,StartY,EndX,StartY));
		g.draw(new Line2D.Float(StartX,EndY,EndX,EndY));
		g.draw(new Line2D.Float(EndX,StartY,EndX,EndY));
		g.setColor(Color.yellow);
		BasicStroke s2 = new BasicStroke(1.5f, BasicStroke.CAP_ROUND,  BasicStroke.JOIN_ROUND);
		g.setStroke(s2);
		g.fillOval((int)(StartX)-5, (int)(StartY)-5, 10, 10);
		g.fillOval((int)(EndX-5), (int)(EndY-5), 10, 10);
		g.fillOval((int)(StartX)-5, (int)(EndY-5), 10, 10);
		g.fillOval((int)(EndX-5), (int)(StartY)-5, 10, 10);
		g.setColor(Color.BLACK);
		g.drawOval((int)(StartX)-5, (int)(StartY)-5, 10, 10);
		g.drawOval((int)(EndX-5), (int)(EndY-5), 10, 10);
		g.drawOval((int)(StartX)-5, (int)(EndY-5), 10, 10);
		g.drawOval((int)(EndX-5), (int)(StartY)-5, 10, 10);
	}
	static public void cancelMarking(Frame f, Mark mark)
	{
		
		f.gco().removeConnectionMark();
		f.gco().removePointMark();
		mark.isMarkedArea = false;
		mark.tabOfConnIndex = new ArrayList<Integer>();
		mark.tabOfPointIndex = new ArrayList<Integer>();
		
	}
	
	
	
	public void resetTool() 
	{
		created = false;
	}

	private float[] generateRangeTab()
	{
		float minX, minY, maxX, maxY;
		if(StartX > EndX)
		{
			minX = EndX;
			maxX = StartX;
		}
		else
		{
			minX = StartX;
			maxX = EndX;
		}
		
		if(StartY > EndY)
		{
			minY = EndY;
			maxY = StartY;
		}
		else
		{
			minY = StartY;
			maxY = EndY;
		}
		float[] tab = {minX, minY, maxX, maxY};
		return tab;
	}
	
	public boolean isCreated() 
	{
		return created;
	}
	
}