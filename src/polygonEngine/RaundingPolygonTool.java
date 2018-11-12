package polygonEngine;

import java.util.ArrayList;

import polygonEngineStructures.PolygonWithCurve;
import polygonEngineStructures.shapeConnector;
import renderSource.Point;
import structures.Frame;
import toolBox.tb;

public class RaundingPolygonTool 
{
	public static void rotateInsidePolygons(Frame f, ArrayList<PolygonWithCurve> polygonTab)
	{
		if(polygonTab.size()>1)
		{
		System.out.println("Rozpoczêto obracanie polygonów "+polygonTab.size());

		ArrayList<float[]> tabOfMax = new ArrayList<float[]>();
		int indexOfMax = getIndexOfMaxRangePolygon(f, polygonTab, tabOfMax);
		
		float[] maxSize = tabOfMax.get(indexOfMax);

		System.out.println("Znaleziono index najwiêkszego polygona "+indexOfMax+" "+maxSize[0]+" "+maxSize[1]+" "+maxSize[2]+" "+maxSize[3]);
		polygonTab.get(indexOfMax).writePolygon();
		
		for(int i = 0; i < polygonTab.size(); i++)
		{
			System.out.println("Polygon "+i+" isLeft: "+isLeftTurned(polygonTab.get(i),i,tabOfMax, f.gco().getPointTab()));
		}
		boolean mainTurn = isLeftTurned(polygonTab.get(indexOfMax),indexOfMax,tabOfMax, f.gco().getPointTab());
		
		
		for(int i = 0; i < polygonTab.size(); i++)
		{
			if(i==indexOfMax)
				continue;
			if(isLeftTurned(polygonTab.get(i),i,tabOfMax, f.gco().getPointTab())==mainTurn)
			{
				System.out.println("Obracanie polygona "+i);
				reversePolygon(polygonTab.get(i));
			}
		}
		System.out.println("Zakoñczono obracanie polygonów");
		}
	}
	
	private static int getIndexOfMaxRangePolygon(Frame f, ArrayList<PolygonWithCurve> polygonTab, ArrayList<float[]> tabOfMax)
	{
		//System.out.println("Sprawdzanie rozpiaru polygona");
		float[] maxSize = new float[4];
		if(polygonTab.size()>0)
		{
		maxSize = getMaxSize(f, polygonTab.get(0));
		int max = 0;
		tabOfMax.add(maxSize);
		for(int i = 1; i < polygonTab.size(); i++)
		{
			float mm[] = getMaxSize(f, polygonTab.get(i));
			tabOfMax.add(mm);
			if(compareTwoPolygons(mm, maxSize))
			{
				//System.out.println("Podmienianie");
				maxSize = mm;
				max = i;
			}
		}
		return max;
		}
		return -10;
	}
	
	private static float[] getMaxSize(Frame f, PolygonWithCurve fp)
	{
		float[] minMax = new float[4];
		ArrayList<Point> p = f.gco().getPointTab();
		minMax[0] = p.get(fp.polygonTab.get(0).getPoint()).x;
		minMax[1] = p.get(fp.polygonTab.get(0).getPoint()).y;
		minMax[2] = p.get(fp.polygonTab.get(0).getPoint()).x;
		minMax[3] = p.get(fp.polygonTab.get(0).getPoint()).y;
		for(shapeConnector s : fp.polygonTab)
		{
			if(p.get(s.getPoint()).x<minMax[0])
			{
				minMax[0] = p.get(s.getPoint()).x;
			}
			else if(p.get(s.getPoint()).x>minMax[2])
			{
				minMax[2] = p.get(s.getPoint()).x;
			}
			if(p.get(s.getPoint()).y<minMax[1])
			{
				minMax[1] = p.get(s.getPoint()).y;
			}
			else if(p.get(s.getPoint()).y>minMax[3])
			{
				minMax[3] = p.get(s.getPoint()).y;
			}
		}
		//System.out.println("BBox "+minMax[0]+" "+minMax[1]+" "+minMax[2]+" "+minMax[3]);
		return minMax;
	}
	
	
	private static boolean compareTwoPolygons(float mm[], float[] maxSize) // if pole mm > pola maxSize to zwróæ prawdê
	{
		float size1 = (mm[2]-mm[0])*(mm[3]-mm[1]);
		float size2 = (maxSize[2]-maxSize[0])*(maxSize[3]-maxSize[1]);
		//System.out.println("Porównywanie pól "+size1+" "+size2);
		if(size1>size2)
			return true;
		else
			return false;
	}
	private static void reversePolygon(PolygonWithCurve fp)
	{
		fp.reverse();
	}
	
	private static boolean isLeftTurned(PolygonWithCurve fp, int indexOfMax, ArrayList<float[]> tabOfMax, ArrayList<Point> pointTab)
	{
		System.out.println("Sprawdzanie kierunku polygona nr. "+indexOfMax);
		float MedX = (tabOfMax.get(indexOfMax)[0]+tabOfMax.get(indexOfMax)[2])/2;
		float[] ab = new float[2];
		float[] XY = {200000, 200000};
		int index = 0;
		
		for(int i = 0; i < fp.polygonTab.size(); i++)
		{
			if((pointTab.get(fp.polygonTab.get(i).P1).x>=MedX&&pointTab.get(fp.polygonTab.get(i).P2).x<=MedX)||(pointTab.get(fp.polygonTab.get(i).P1).x<=MedX&&pointTab.get(fp.polygonTab.get(i).P2).x>=MedX))
			{
				ab = tb.generatePatternAB(pointTab.get(fp.polygonTab.get(i).P1).x, pointTab.get(fp.polygonTab.get(i).P1).y, pointTab.get(fp.polygonTab.get(i).P2).x, pointTab.get(fp.polygonTab.get(i).P2).y);
				if(ab!=null)
				{
				float[] XY2 = tb.getCrossOfLineX(ab[0], ab[1], MedX);
				if(XY2[1]<XY[1])
				{
					XY = XY2;
					index = i;
				}
				}
			}
		}
		System.out.println("Najwy¿szy odcinek "+fp.polygonTab.get(index).connIndex);
			if(pointTab.get(fp.polygonTab.get(index).getPoint()).x<=MedX&&pointTab.get(fp.polygonTab.get(index).getSecondPoint()).x>=MedX)
			{
				return true;
			}
			else if(pointTab.get(fp.polygonTab.get(index).getSecondPoint()).x<=MedX&&pointTab.get(fp.polygonTab.get(index).getPoint()).x>=MedX)
			{
				return false;
			}
			else
				return false;
		
	}
}
