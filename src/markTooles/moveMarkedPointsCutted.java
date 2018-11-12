package markTooles;

import java.awt.Graphics2D;
import java.util.ArrayList;

import mark.Mark;
import mark.dataBox;
import mark.toolSchem;
import pastManager.PastBox;
import pastManager.StateBox;
import renderSource.Connection;
import renderSource.Point;
import structures.Frame;
import structures.ParameterBox;
import supportingStructures.PointsToAddToConn;
import toolBox.frameCupture;
import toolBox.tb;

public class moveMarkedPointsCutted implements toolSchem
{
	private boolean created = false;
	private float StartX, StartY;
	private float lastX, lastY;
	public static boolean isReadyToCut = false;
	public static float range = 0.001f;
	
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
		if(mark.isMarkedArea)
		{
		if(mark.range[0]<=x&&mark.range[1]<=y&&mark.range[2]>=x&&mark.range[3]>=y)
		{
			//System.out.println("Rozpoczêto");
			created = true;
			PastBox.addStepStart();
			
			PastBox.eventAllObjectDeform(StateBox.frameIndex, StateBox.objIndex, f.gco());
			
			StartX = x;
			StartY = y;
			lastX = x;
			lastY = y;
			
			if(isReadyToCut)
			{
				cutConnectiones(f, mark);
				isReadyToCut = false;
			}
		}
		}
	}


	/**
	 * Tnie odcinki które znajduj¹ sie wewn¹trz zaznaczenia
	 * dziel¹c je na dwie czeœci
	 * @param f
	 * @param mark
	 */
	private void cutConnectiones(Frame f, Mark mark) 
	{
		float[] r = mark.range.clone();
		float t1[] = f.gco().scaleAndRotateValue(r[0], r[1]);
		float t2[] = f.gco().scaleAndRotateValue(r[2], r[3]);
		r[0] = t1[0]; //xMin
		r[1] = t1[1]; //yMin
		r[2] = t2[0]; //xMax
		r[3] = t2[1]; //yMax
		ArrayList<Point> p = f.gco().getPointTab();
		ArrayList<Connection> c = f.gco().getConnectionTab();
		
		ArrayList<Integer> potencialLines = new ArrayList<Integer>();
		for(int i = 0; i < c.size(); i++)
		{
			float maxX = Math.max(p.get(c.get(i).getP1()).x, p.get(c.get(i).getP2()).x);
			float minX = Math.min(p.get(c.get(i).getP1()).x, p.get(c.get(i).getP2()).x);
			float maxY = Math.max(p.get(c.get(i).getP1()).y, p.get(c.get(i).getP2()).y);
			float minY = Math.min(p.get(c.get(i).getP1()).y, p.get(c.get(i).getP2()).y);
			if(!(r[0]>=maxX||r[2]<=minX||r[1]>=maxY||r[3]<=minY))
			{
				//System.out.println("Po³¹czenie pod indexem "+i+" dodawane do listy potencjalnych" );
				potencialLines.add(i);
			}
		}
		
		float[] a1 = tb.generatePatternABC(r[0], r[1], r[0], r[3]); //xMin pionowa min x
		float[] a2 = tb.generatePatternABC(r[2], r[1], r[2], r[3]); //xMax pionowa max x
		float[] a3 = tb.generatePatternABC(r[0], r[1], r[2], r[1]); //yMin pozioma min y
		float[] a4 = tb.generatePatternABC(r[0], r[3], r[2], r[3]); //yMax pozioma max y

		
		ArrayList<PointsToAddToConn> pointsToAdd = new ArrayList<>();
		
		for(int i = 0; i < potencialLines.size(); i++)
		{
			Connection C = c.get((int)potencialLines.get(i));
			float[] tab = tb.generatePatternABC(p.get(C.getP1()).x, p.get(C.getP1()).y, p.get(C.getP2()).x, p.get(C.getP2()).y);
			float maxX = Math.max(p.get(C.getP1()).x, p.get(C.getP2()).x);
			float minX = Math.min(p.get(C.getP1()).x, p.get(C.getP2()).x);
			float maxY = Math.max(p.get(C.getP1()).y, p.get(C.getP2()).y);
			float minY = Math.min(p.get(C.getP1()).y, p.get(C.getP2()).y);
			
			ArrayList<Point> locPointTab = new ArrayList<Point>(2);
			if(!(r[0]>=maxX||r[0]<=minX||r[1]>=maxY||r[3]<=minY))
			{
				//Poziomie minimalne
				float XY[] = tb.corossPointOfTwoLines(tab, a1);
				if(tb.isPointBetwenOrOnR(r, XY[0], XY[1], range))
				locPointTab.add(new Point(XY[0], XY[1]));
			}
			if(!(r[2]>=maxX||r[2]<=minX||r[1]>=maxY||r[3]<=minY))
			{
				//Poziomie maksymalne
				float XY[] = tb.corossPointOfTwoLines(tab, a2);
				if(tb.isPointBetwenOrOnR(r, XY[0], XY[1], range))
				locPointTab.add(new Point(XY[0], XY[1]));
			}
			if(!(r[0]>=maxX||r[2]<=minX||r[1]>=maxY||r[1]<=minY))
			{
				//Pionowe minimalne
				float XY[] = tb.corossPointOfTwoLines(tab, a3);
				if(tb.isPointBetwenOrOnR(r, XY[0], XY[1], range))
				locPointTab.add(new Point(XY[0], XY[1]));
			}
			if(!(r[0]>=maxX||r[2]<=minX||r[3]>=maxY||r[3]<=minY))
			{
				//Pionowe maksymalne
				float XY[] = tb.corossPointOfTwoLines(tab, a4);
				if(tb.isPointBetwenOrOnR(r, XY[0], XY[1], range))
				locPointTab.add(new Point(XY[0], XY[1]));
			}
			
			if(locPointTab.size()>0)
			{
				pointsToAdd.add(new PointsToAddToConn((int) potencialLines.get(i), locPointTab));
			}
		}
		
//		for(PointsToAddToConn pta : pointsToAdd)
//		{
//			System.out.print(pta.connIndex+" ");
//			for(Point point : pta.pointTab)
//			{
//				System.out.print(point.x+" "+point.y+" // ");
//			}
//			System.out.println();
//		}
		//System.out.println("Start");
		for(PointsToAddToConn pta : pointsToAdd)
		{
			for(Point point : pta.pointTab)
			{
				f.gco().addPoint(point);
			}
		}
		for(PointsToAddToConn pta : pointsToAdd)
		{
			if(pta.pointTab.size()==1)
				f.gco().catConnectionNoScaleMarked(pta.connIndex, pta.pointTab.get(0));
			else
				f.gco().catConnectionMarked(pta.connIndex, pta.pointTab);
		}
		f.gco().removeSinglePointsNEW();
		
		markOnlyIncludedConnectiones(f, mark, p);
		
		frameCupture.hasFrame = false;
	}







	private void markOnlyIncludedConnectiones(Frame f, Mark mark, ArrayList<Point> p) {
		mark.tabOfConnIndex = new ArrayList<>(mark.tabOfConnIndex.size());
		mark.tabOfPointIndex = new ArrayList<>(mark.tabOfPointIndex.size());
		
		boolean tabOfUsed[] = new boolean[f.gco().getPointTabSize()];
		for(int i = 0; i < f.gco().getConnectionTabSize(); i++)
		{
			Connection C = f.gco().getCon(i);
			if(f.gco().getPointTab().get(C.getP1()).MarkedPoint&&f.gco().getPointTab().get(C.getP2()).MarkedPoint)
			{
				//System.out.println("Odcinek zaakceptowany "+i);
				C.setMarked(true);
				mark.tabOfConnIndex.add(i);
				if(!tabOfUsed[C.getP1()])
				{
					mark.tabOfPointIndex.add(C.getP1());
					tabOfUsed[C.getP1()] = true;
				}
				if(!tabOfUsed[C.getP2()])
				{
					mark.tabOfPointIndex.add(C.getP2());
					tabOfUsed[C.getP2()] = true;
				}
			}
			else
			{
				//System.out.println("Odcinek odrzucony "+i);
				C.setMarked(false);
				p.get(C.getP1()).MarkedPoint = false;
				p.get(C.getP2()).MarkedPoint = false;
			}
		}
	}


	public float[] getRageOfMarked(Frame f)
	{
		float tab[] = null;
		boolean isThereAnyMark = false;
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
				isThereAnyMark = true;
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
		tab = new float[4];
		tab[0] = MaxX+10;
		tab[1] = MaxY+10;
		tab[2] = MinX-10;
		tab[3] = MinY-10;
		//System.out.println("Wspó³¿êdne zaznaczenia "+MaxX+" "+MaxY+" "+MinX+" "+MinY);
		}
		if(isThereAnyMark==false)
			tab = null;
//		System.out.println("Brak jakiego kolwiek odcinka zaznaczonegoHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
		return tab;
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