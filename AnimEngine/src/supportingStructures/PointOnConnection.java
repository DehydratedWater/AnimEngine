package supportingStructures;

import java.util.ArrayList;

public class PointOnConnection 
{
	public int indexOfConnection = -10;
	public ArrayList<Integer> listOfPoints;
	
	public PointOnConnection(int index) 
	{
		this.indexOfConnection = index;
	} 	
	
	public void init()
	{
		listOfPoints = new ArrayList<Integer>();
	}
}
