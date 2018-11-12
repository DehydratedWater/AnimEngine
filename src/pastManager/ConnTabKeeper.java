package pastManager;

import java.util.ArrayList;

import renderSource.Connection;

public class ConnTabKeeper 
{
	public ArrayList<Connection> connTab;
	public ConnTabKeeper(ArrayList<Connection> cTab)
	{
		connTab = new ArrayList<Connection>(cTab.size());
		for(Connection c:cTab)
		{
			connTab.add(new Connection(c));
		}
	}
}
