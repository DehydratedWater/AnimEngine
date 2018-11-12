package toolBox;

import java.util.ArrayList;

import polygonEngineStructures.Conn;
import polygonEngineStructures.PointLight;
import renderSource.Connection;
import renderSource.Point;
import supportingStructures.PointWithIndex;



public class QuickSort 
{

	public static void SortPointIndexes(ArrayList<Integer> tab, ArrayList<Point> pointTab)
	{
		QuickSortPointIndexes(tab, pointTab, 0, tab.size()-1);
	}
	public static void SortPointIndexesY(ArrayList<Integer> tab, ArrayList<Point> pointTab)
	{
		QuickSortPointIndexesY(tab, pointTab, 0, tab.size()-1);
	}
	public static void SortFloatArray(ArrayList<Float> tab)
	{
		QuickSortFloat(tab, 0, tab.size()-1);
	}
	public static void SortPointWithIndex(ArrayList<PointWithIndex> tab)
	{
		QuickSortPointWithIndex(tab, 0, tab.size()-1);
	}

	public static void SortConnIndexes(ArrayList<Integer> indexTab, ArrayList<Connection> connTab, ArrayList<Point> pointTab)
	{
		QuickSortIndexsesOfConnectiones(indexTab, connTab, pointTab, 0, indexTab.size()-1);
	}
	public static void SortIntArray(ArrayList<Integer> tab)
	{
		QuickSortInt(tab, 0, tab.size()-1);
	}
	public static void SortIntArrayWithRemoveDoubles(ArrayList<Integer> tab)
	{
		QuickSortInt(tab, 0, tab.size()-1);
		removeDoublesInt(tab);
	}
	public static void SortLightPoint(ArrayList<PointLight> tab)
	{
		QuickSortLightPoint(tab, 0, tab.size()-1);
	}
	public static void SortPoint(ArrayList<Point> tab)
	{
		QuickSortPoint(tab, 0, tab.size()-1);
	}
	public static void SortFloatArrayWithRemoveDoubles(ArrayList<Float> tab)
	{
		QuickSortFloat(tab, 0, tab.size()-1);
		removeDoubles(tab);
	}
	private static void QuickSortPointWithIndex(ArrayList<PointWithIndex> tab, int l, int r) 
	{
		if(r>l)
		{
			int i = reorganiseDataPointWithIndex(tab, l, r);
			QuickSortPointWithIndex(tab, l, i-1);
			QuickSortPointWithIndex(tab, i+1, r);
		}
	}

	private static void QuickSortIndexsesOfConnectiones(ArrayList<Integer> indexTab, ArrayList<Connection> connTab, ArrayList<Point> pointTab, int l, int r)
	{
		if(r>l)
		{
			int i = reorganiseDataOfConnectionIndexes(indexTab, connTab, pointTab, l, r);
			QuickSortIndexsesOfConnectiones(indexTab, connTab, pointTab, l, i-1);
			QuickSortIndexsesOfConnectiones(indexTab, connTab, pointTab, i+1, r);
		}
	}
	private static void QuickSortPointIndexesY(ArrayList<Integer> tab, ArrayList<Point> pointTab, int l, int r)
	{
		if(r>l)
		{
			int i = reorganiseDataPointIndexesY(tab, pointTab, l, r);
			QuickSortPointIndexesY(tab, pointTab, l, i-1);
			QuickSortPointIndexesY(tab, pointTab, i+1, r);
		}
	}
	private static void QuickSortPointIndexes(ArrayList<Integer> tab, ArrayList<Point> pointTab, int l, int r)
	{
		if(r>l)
		{
			int i = reorganiseDataPointIndexes(tab, pointTab, l, r);
			QuickSortPointIndexes(tab, pointTab, l, i-1);
			QuickSortPointIndexes(tab, pointTab, i+1, r);
		}
	}
	private static void QuickSortFloat(ArrayList<Float> tab, int l, int r)
	{
		if(r>l)
		{
			int i = reorganiseData(tab, l, r);
			QuickSortFloat(tab, l, i-1);
			QuickSortFloat(tab, i+1, r);
		}
	}
	private static void QuickSortInt(ArrayList<Integer> tab, int l, int r)
	{
		if(r>l)
		{
			int i = reorganiseDataInt(tab, l, r);
			QuickSortInt(tab, l, i-1);
			QuickSortInt(tab, i+1, r);
		}
	}
	private static void QuickSortLightPoint(ArrayList<PointLight> tab, int l, int r)
	{
		if(r>l)
		{
			int i = reorganiseDataLightPoint(tab, l, r);
			QuickSortLightPoint(tab, l, i-1);
			QuickSortLightPoint(tab, i+1, r);
		}
	}
	private static void QuickSortPoint(ArrayList<Point> tab, int l, int r)
	{
		if(r>l)
		{
			int i = reorganiseDataPoint(tab, l, r);
			QuickSortPoint(tab, l, i-1);
			QuickSortPoint(tab, i+1, r);
		}
	}
	private static int reorganiseDataOfConnectionIndexes(ArrayList<Integer> indexTab,ArrayList<Connection> connTab, ArrayList<Point> pointTab, int l, int r)
	{
		int divIndex = getDivIndex(l, r);
		float divValue = Math.min(pointTab.get(connTab.get(indexTab.get(divIndex)).getP1()).x, pointTab.get(connTab.get(indexTab.get(divIndex)).getP2()).x);
		SwitchInt(indexTab, divIndex, r);
		int actualPos = l;
		for(int i = l; i <= r; i++)
		{
			if(Math.min(pointTab.get(connTab.get(indexTab.get(i)).getP1()).x, pointTab.get(connTab.get(indexTab.get(i)).getP2()).x) < divValue)
			{
				SwitchInt(indexTab, i, actualPos);
				actualPos+=1;
			}
		}
		SwitchInt(indexTab, actualPos, r);
		return actualPos;
	}
	private static int reorganiseDataPointWithIndex(ArrayList<PointWithIndex> tab, int l, int r) {
		int divIndex = getDivIndex(l, r);
		float divValue = tab.get(divIndex).x;
		SwitchPointWithIndex(tab, divIndex, r);
		int actualPos = l;
		for(int i = l; i <= r; i++)
		{
			if(tab.get(i).x < divValue)
			{
				SwitchPointWithIndex(tab, i, actualPos);
				actualPos+=1;
			}
		}
		SwitchPointWithIndex(tab, actualPos, r);
		return actualPos;
	}
	private static int reorganiseDataPointIndexesY(ArrayList<Integer> tab, ArrayList<Point> pointTab, int l, int r)
	{
		int divIndex = getDivIndex(l, r);
		float divValue = pointTab.get(tab.get(divIndex)).y;
		SwitchInt(tab, divIndex, r);
		int actualPos = l;
		for(int i = l; i <= r; i++)
		{
			if( pointTab.get(tab.get(i)).y < divValue)
			{
				SwitchInt(tab, i, actualPos);
				actualPos+=1;
			}
		}
		SwitchInt(tab, actualPos, r);
		return actualPos;
	}
	private static int reorganiseDataPointIndexes(ArrayList<Integer> tab, ArrayList<Point> pointTab, int l, int r)
	{
		int divIndex = getDivIndex(l, r);
		float divValue = pointTab.get(tab.get(divIndex)).x;
		SwitchInt(tab, divIndex, r);
		int actualPos = l;
		for(int i = l; i <= r; i++)
		{
			if( pointTab.get(tab.get(i)).x < divValue)
			{
				SwitchInt(tab, i, actualPos);
				actualPos+=1;
			}
		}
		SwitchInt(tab, actualPos, r);
		return actualPos;
	}
	private static int reorganiseData(ArrayList<Float> tab, int l, int r)
	{
		int divIndex = getDivIndex(l, r);
		float divValue = tab.get(divIndex);
		Switch(tab, divIndex, r);
		int actualPos = l;
		for(int i = l; i <= r; i++)
		{
			if(tab.get(i) < divValue)
			{
				Switch(tab, i, actualPos);
				actualPos+=1;
			}
		}
		Switch(tab, actualPos, r);
		return actualPos;
	}
	private static int reorganiseDataInt(ArrayList<Integer> tab, int l, int r)
	{
		int divIndex = getDivIndex(l, r);
		float divValue = tab.get(divIndex);
		SwitchInt(tab, divIndex, r);
		int actualPos = l;
		for(int i = l; i <= r; i++)
		{
			if(tab.get(i) < divValue)
			{
				SwitchInt(tab, i, actualPos);
				actualPos+=1;
			}
		}
		SwitchInt(tab, actualPos, r);
		return actualPos;
	}
	private static int reorganiseDataPoint(ArrayList<Point> tab, int l, int r)
	{
		int divIndex = getDivIndex(l, r);
		float divValue = tab.get(divIndex).y;
		SwitchPoints(tab, divIndex, r);
		int actualPos = l;
		for(int i = l; i <= r; i++)
		{
			if(tab.get(i).y < divValue)
			{
				SwitchPoints(tab, i, actualPos);
				actualPos+=1;
			}
		}
		SwitchPoints(tab, actualPos, r);
		return actualPos;
	}
	private static int reorganiseDataLightPoint(ArrayList<PointLight> tab, int l, int r)
	{
		int divIndex = getDivIndex(l, r);
		float divValue = tab.get(divIndex).y;
		SwitchPointLights(tab, divIndex, r);
		int actualPos = l;
		for(int i = l; i <= r; i++)
		{
			if(tab.get(i).y < divValue)
			{
				SwitchPointLights(tab, i, actualPos);
				actualPos+=1;
			}
		}
		SwitchPointLights(tab, actualPos, r);
		return actualPos;
	}
	private static int getDivIndex(int l, int r)
	{
		return (l+r)/2;
	}
	
	private static void SwitchPointWithIndex(ArrayList<PointWithIndex> tab, int i1, int i2)
	{
		PointWithIndex tem = tab.get(i2).clone();
		tab.set(i2, tab.get(i1));
		tab.set(i1, tem);
	}
	private static void Switch(ArrayList<Float> tab, int i1, int i2)
	{
		float tem = tab.get(i2);
		tab.set(i2, tab.get(i1));
		tab.set(i1, tem);
	}
	private static void SwitchInt(ArrayList<Integer> tab, int i1, int i2)
	{
		int tem = tab.get(i2);
		tab.set(i2, tab.get(i1));
		tab.set(i1, tem);
	}
	private static void SwitchPointLights(ArrayList<PointLight> tab, int i1, int i2)
	{
		PointLight tem = new PointLight(tab.get(i2).x, tab.get(i2).y);
		tab.set(i2, new PointLight(tab.get(i1).x, tab.get(i1).y));
		tab.set(i1, tem);
	}
	private static void SwitchPoints(ArrayList<Point> tab, int i1, int i2)
	{
		Point tem = new Point(tab.get(i2).x, tab.get(i2).y);
		tab.set(i2, new Point(tab.get(i1).x, tab.get(i1).y));
		tab.set(i1, tem);
	}
	private static void removeDoubles(ArrayList<Float> tab)
	{
		float value = tab.get(0);
		for(int i = 1 ; i < tab.size(); i++)
		{
			if(tab.get(i)==value)
			{
				tab.remove(i);
				i--;
			}
			else
			{
				value = tab.get(i);
			}
		}
	}
	
	private static void removeDoublesInt(ArrayList<Integer> tab)
	{
		int value = tab.get(0);
		for(int i = 1 ; i < tab.size(); i++)
		{
			if(tab.get(i)==value)
			{
				tab.remove(i);
				i--;
			}
			else
			{
				value = tab.get(i);
			}
		}
	}
	
	
	
	
	
	
	
	
	public static void SortConnectionArray(ArrayList<Conn> tab)
	{
		//System.out.println("Sortowanie po³¹czeñ");
		QuickSortConnection(tab, 0, tab.size()-1);
	}
	

	private static void QuickSortConnection(ArrayList<Conn> tab, int l, int r)
	{
		if(r>l)
		{
			int i = reorganiseDataConnection(tab, l, r);
			QuickSortConnection(tab, l, i-1);
			QuickSortConnection(tab, i+1, r);
		}
	}

	private static int reorganiseDataConnection(ArrayList<Conn> tab, int l, int r)
	{
		int divIndex = getDivIndex(l, r);
		float divValue = tab.get(divIndex).medY;
		SwitchConnection(tab, divIndex, r);
		int actualPos = l;
		for(int i = l; i <= r; i++)
		{
			if(tab.get(i).medY < divValue)
			{
				SwitchConnection(tab, i, actualPos);
				actualPos+=1;
			}
		}
		SwitchConnection(tab, actualPos, r);
		return actualPos;
	}
	
	private static void SwitchConnection(ArrayList<Conn> tab, int i1, int i2)
	{
		Conn tem = tab.get(i2);
		tab.set(i2, tab.get(i1));
		tab.set(i1, tem);
	}

	
}
