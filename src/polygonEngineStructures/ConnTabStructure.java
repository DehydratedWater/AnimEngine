package polygonEngineStructures;

import java.util.ArrayList;

import renderSource.Connection;

public class ConnTabStructure 
{
	public ArrayList<shapeConnector> shapeTab;
	
	public ConnTabStructure() 
	{
		shapeTab = new ArrayList<shapeConnector>(2);
	}
	public ConnTabStructure(int s) 
	{
		shapeTab = new ArrayList<shapeConnector>(s);
	}
	public void addConn(Connection c, boolean fromLeft, int connIndex)
	{
		shapeTab.add(new shapeConnector(c, fromLeft, connIndex));
	}
	public int size()
	{
		return shapeTab.size();
	}
	public int getPoint(int i)
	{
		return shapeTab.get(i).getPoint();
	}
	public void remPoint(int i)
	{
		shapeTab.remove(i);
	}
	public shapeConnector getShape(int i)
	{
		return shapeTab.get(i);
	}
	
	public void drawStructure()
	{
		for(shapeConnector s:shapeTab)
			System.out.print(s.getPoint()+" ");
		System.out.println();
	}
}
