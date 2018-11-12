package structureArchive;

import java.util.ArrayList;

public class PointConn 
{
	public ArrayList<Integer> connIndexTab;
	public PointConn() {
		connIndexTab = new ArrayList<Integer>(5);
	}
	
	public void removeConnIndex(int index)
	{
		for(int i = 0; i < connIndexTab.size(); i++)
		{
			if(connIndexTab.get(i)==index)
			{
				connIndexTab.remove(i);
				break;
			}
		}
	}
}
