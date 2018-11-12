package toolBox;

import java.util.ArrayList;

import polygonEngineStructures.Conn;
import polygonEngineStructures.PointLight;
import polygonEngineStructures.Stab;
import polygonEngineStructures.VertexLine;

public class BinnarySearch 
{
	public static int getIndexOfValueFloat(ArrayList<Float> tab, float value)
	{
		return FindIndexOf(tab, 0, tab.size()-1, value);
	}
	public static int getIndexOfUpperVertexConnection(VertexLine tab, float value)
	{
		return FindIndexOfUpperVertexConn(tab, 0, tab.cTab.size()-1, value);
	}
	public static int getIndexOfDownVertexConnection(VertexLine tab, float value)
	{
		return FindIndexOfDownVertexConn(tab, 0, tab.cTab.size()-1, value);
	}
	public static int getIndexOfIndexOfVertexConn(ArrayList<VertexLine> tab, float value)
	{
		return FindIndexOfVertexConn(tab, 0, tab.size()-1, value);
	}
	public static int getIndexOfIndexOfUpperConn(ArrayList<Conn> tab, float value)
	{
		return FindIndexOfConnUpperPoint(tab, 0, tab.size()-1, value);
	}
	public static int getIndexOfIndexOfDownConn(ArrayList<Conn> tab, float value)
	{
		return FindIndexOfConnDownPoint(tab, 0, tab.size()-1, value);
	}
	public static int getIndexOfValueStab(ArrayList<Stab> tab, float value)
	{
		return FindIndexOfStab(tab, 0, tab.size()-1, value);
	}
	public static int getIndexOfValueConn(ArrayList<Conn> tab, float x, float y)
	{
		return FindIndexOfConn(tab, 0, tab.size()-1, x, y);
	}
	
	
	public static int getIndexOfPointLight(ArrayList<PointLight> tab, float value)
	{
		return FindIndexOfPointLight(tab, 0, tab.size()-1, value);
	}
	private static int FindIndexOfPointLight(ArrayList<PointLight> tab, int k, int l, float value)
	{
		if(k>l)
		{
			return -10;
		}
		else
		{
		int q = findMedian(k, l);
		//System.out.println("Znaleziono medianê  "+k+" "+q+" "+l);
		if(tab.get(q).y==value)
		{
			//System.out.println("Znaleziono wynik "+q);
			return q;
		}
		else
		{
			if(tab.get(q).y>value)
			{
				return FindIndexOfPointLight(tab, k, q-1, value);
			}
			if(tab.get(q).y<value)
			{
				return FindIndexOfPointLight(tab, q+1, l, value);
			}
		}
		}
		return -10;
		
	}
	public static int getIndexOfValueConnWithPoint(ArrayList<Conn> tab, float x, float y)
	{
		if(tab.size()>0)
		{
			//System.out.println("szukanie "+x+" "+y+" sztaba :  lewy:"+tab.get(0).P1.x+"  prawy:"+tab.get(0).P2.x);
			if(tab.get(0).P1.x==x)
			{
				//System.out.println("Szukanie punktów po lewej stronie sztaby");
				return FindIndexOfConnPointLeft(tab, 0, tab.size()-1, x, y);
			}
			else if(tab.get(0).P2.x==x)
			{
				//System.out.println("Szukanie punktów po prawej stronie sztaby");
				return FindIndexOfConnPointRight(tab, 0, tab.size()-1, x, y);
			}
		}
		return -10;
	}
	
	private static int FindIndexOfConnPointRight(ArrayList<Conn> tab, int k, int l, float x, float y) {
		if(k>l)
		{
			return -10;
		}
		else
		{
		int q = findMedian(k, l);
		//System.out.println("Znaleziono medianê  "+k+" "+q+" "+l);
		if(tab.get(q).P2.y==y)
		{
			//System.out.println("Znaleziono wynik "+q);
			return q;
		}
		else
		{
			if(tab.get(q).P2.y>y)
			{
				return FindIndexOfConnPointRight(tab, k, q-1, x, y);
			}
			if(tab.get(q).P2.y<y)
			{
				return FindIndexOfConnPointRight(tab, q+1, l, x, y);
			}
		}
		}
		return -10;
	}
	private static int FindIndexOfConnPointLeft(ArrayList<Conn> tab, int k, int l, float x, float y) {
		if(k>l)
		{
			return -10;
		}
		else
		{
		int q = findMedian(k, l);
		//System.out.println("Znaleziono medianê  "+k+" "+q+" "+l);
		if(tab.get(q).P1.y==y)
		{
			//System.out.println("Znaleziono wynik "+q);
			return q;
		}
		else
		{
			if(tab.get(q).P1.y>y)
			{
				return FindIndexOfConnPointLeft(tab, k, q-1, x, y);
			}
			if(tab.get(q).P1.y<y)
			{
				return FindIndexOfConnPointLeft(tab, q+1, l, x, y);
			}
		}
		}
		return -10;
	}
	public static int getIndexOfValueStabN(ArrayList<Stab> tab, float value)
	{
		return FindIndexOfStabN(tab, value);
	}
	private static int FindIndexOf(ArrayList<Float> tab, int k, int l, float value)
	{
		if(k>l)
		{
			return -10;
		}
		else
		{
		int q = findMedian(k, l);
		//System.out.println("Znaleziono medianê  "+k+" "+q+" "+l);
		if(tab.get(q)==value)
		{
			//System.out.println("Znaleziono wynik "+q);
			return q;
		}
		else
		{
			if(tab.get(q)>value)
			{
				return FindIndexOf(tab, k, q-1, value);
			}
			if(tab.get(q)<value)
			{
				return FindIndexOf(tab, q+1, l, value);
			}
		}
		}
		return -10;
		
	}
	
	private static int FindIndexOfVertexConn(ArrayList<VertexLine> tab, int k, int l, float value)
	{
		if(k>l)
		{
			return -10;
		}
		else
		{
		int q = findMedian(k, l);
		//System.out.println("Znaleziono medianê  "+k+" "+q+" "+l);
		if(tab.get(q).X==value)
		{
			//System.out.println("Znaleziono wynik "+q);
			return q;
		}
		else
		{
			if(tab.get(q).X>value)
			{
				return FindIndexOfVertexConn(tab, k, q-1, value);
			}
			if(tab.get(q).X<value)
			{
				return FindIndexOfVertexConn(tab, q+1, l, value);
			}
		}
		}
		return -10;
		
	}
	
	private static int FindIndexOfDownVertexConn(VertexLine tab, int k, int l, float value)
	{
		if(k>l)
		{
			return -10;
		}
		else
		{
		int q = findMedian(k, l);
		//System.out.println("Znaleziono medianê  "+k+" "+q+" "+l);
		if(tab.cTab.get(q).P1.y==value)
		{
			//System.out.println("Znaleziono wynik "+q);
			return q;
		}
		else
		{
			if(tab.cTab.get(q).P2.y>value)
			{
				return FindIndexOfDownVertexConn(tab, k, q-1, value);
			}
			if(tab.cTab.get(q).P2.y<value)
			{
				return FindIndexOfDownVertexConn(tab, q+1, l, value);
			}
		}
		}
		return -10;
		
	}
	private static int FindIndexOfUpperVertexConn(VertexLine tab, int k, int l, float value)
	{
		if(k>l)
		{
			return -10;
		}
		else
		{
		int q = findMedian(k, l);
		//System.out.println("Znaleziono medianê  "+k+" "+q+" "+l);
		if(tab.cTab.get(q).P1.y==value)
		{
			//System.out.println("Znaleziono wynik "+q);
			return q;
		}
		else
		{
			if(tab.cTab.get(q).P1.y>value)
			{
				return FindIndexOfUpperVertexConn(tab, k, q-1, value);
			}
			if(tab.cTab.get(q).P1.y<value)
			{
				return FindIndexOfUpperVertexConn(tab, q+1, l, value);
			}
		}
		}
		return -10;
		
	}
	private static int FindIndexOfConnUpperPoint(ArrayList<Conn> tab, int k, int l, float value)
	{
		if(k>l)
		{
			return -10;
		}
		else
		{
		int q = findMedian(k, l);
		//System.out.println("Znaleziono medianê  "+k+" "+q+" "+l);
		if(tab.get(q).P1.y==value)
		{
			//System.out.println("Znaleziono wynik "+q);
			return q;
		}
		else
		{
			if(tab.get(q).P1.y>value)
			{
				return FindIndexOfConnUpperPoint(tab, k, q-1, value);
			}
			if(tab.get(q).P1.y<value)
			{
				return FindIndexOfConnUpperPoint(tab, q+1, l, value);
			}
		}
		}
		return -10;
		
	}
	private static int FindIndexOfConnDownPoint(ArrayList<Conn> tab, int k, int l, float value)
	{
		if(k>l)
		{
			return -10;
		}
		else
		{
		int q = findMedian(k, l);
		//System.out.println("Znaleziono medianê  "+k+" "+q+" "+l);
		if(tab.get(q).P2.y==value)
		{
			//System.out.println("Znaleziono wynik "+q);
			return q;
		}
		else
		{
			if(tab.get(q).P2.y>value)
			{
				return FindIndexOfConnDownPoint(tab, k, q-1, value);
			}
			if(tab.get(q).P2.y<value)
			{
				return FindIndexOfConnDownPoint(tab, q+1, l, value);
			}
		}
		}
		return -10;
		
	}
	private static int FindIndexOfStab(ArrayList<Stab> tab, int k, int l, float value)
	{
		if(k>l)
		{
			return -10;
		}
		else
		{
		int q = findMedian(k, l);
		//System.out.println("Znaleziono medianê  "+k+" "+q+" "+l);
		if(tab.get(q).X1<=value&&tab.get(q).X2>=value)
		{
			//System.out.println("Znaleziono wynik "+q);
			return q;
		}
		else
		{
			if(tab.get(q).X1>value&&tab.get(q).X2>value)
			{
				return FindIndexOfStab(tab, k, q-1, value);
			}
			if(tab.get(q).X1<value&&tab.get(q).X2<value)
			{
				return FindIndexOfStab(tab, q+1, l, value);
			}
		}
		}
		return -10;
		
	}
	
	
	private static int FindIndexOfConn(ArrayList<Conn> tab, int k, int l, float x, float y)
	{
		if(k>l)
		{
			return -10;
		}
		else
		{
		int q = findMedian(k, l);
		//System.out.println("Znaleziono medianê  "+k+" "+q+" "+l);
		if(q+1 >= tab.size())
		{
			return -10;
		}
		int a = vectorTools.sideOfPointWithLine(tab.get(q).P1.x, tab.get(q).P1.y, tab.get(q).P2.x, tab.get(q).P2.y, x, y);
		int b = vectorTools.sideOfPointWithLine(tab.get(q+1).P1.x, tab.get(q+1).P1.y, tab.get(q+1).P2.x, tab.get(q+1).P2.y, x, y);
		if((a==1||a==0)&&(b==-1||b==0))
		{
			//System.out.println("Znaleziono wynik "+q);
			return q;
		}
		else
		{
			if(a==-1&&b==-1)
			{
				return FindIndexOfConn(tab, k, q-1, x, y);
			}
			if(a==1&&b==1)
			{
				return FindIndexOfConn(tab, q+1, l, x, y);
			}
		}
		}
		return -10;
		
	}
	
	
	
	private static int FindIndexOfStabN(ArrayList<Stab> tab, float value)
	{
		for(int i = 0; i < tab.size(); i++)
		{
			if(tab.get(i).X1<value&&tab.get(i).X2>=value)
			{
				return i;
			}
		}
		return -10;
	}
	
	private static int findMedian(int k, int l)
	{
		return (k+l)/2;
	}
}

