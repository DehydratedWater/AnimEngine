package structureArchive;

import java.util.ArrayList;

public class ConnPolygon 
{
	public ArrayList<Integer> polygonIndexTab;
	public ConnPolygon()
	{
		polygonIndexTab = new ArrayList<Integer>(2);
	}
	
	public void removePolygonIndex(int index)
	{
		for(int i = 0; i < polygonIndexTab.size(); i++)
		{
			if(polygonIndexTab.get(i)==index)
			{
				polygonIndexTab.remove(i);
				break;
			}
		}
	}
}
