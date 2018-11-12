package polygonEngineStructures;

import java.util.ArrayList;

public class PolygonWithCurve 
{
	public ArrayList<shapeConnector> polygonTab;
	public PolygonWithCurve()
	{
		polygonTab = new ArrayList<shapeConnector>();
	}
	public PolygonWithCurve(int s)
	{
		polygonTab = new ArrayList<shapeConnector>(s);
	}
	public PolygonWithCurve(ArrayList<shapeConnector> shape)
	{
		polygonTab = shape;
	}
	public void writePolygon()
	{
		for(shapeConnector s : polygonTab)
		{
			System.out.print(s.getPoint()+" ");
		}
		System.out.println();
	}
	public void reverse()
	{
		ArrayList<shapeConnector> polygonTabNew = new ArrayList<shapeConnector>(polygonTab.size());
		
		for(int i = 1; i < polygonTab.size(); i++)
		{
			polygonTabNew.add(polygonTab.get(polygonTab.size()-1-i));
			//polygonTabNew.get(polygonTabNew.size()-1).fromLeft = !polygonTabNew.get(polygonTabNew.size()-1).fromLeft;
		}
		polygonTabNew.add(polygonTab.get(polygonTab.size()-1));
		polygonTab = polygonTabNew;
	}
}
