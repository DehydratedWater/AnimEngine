package supportingStructures;

import java.util.ArrayList;

import renderSource.Point;

public class PointsToAddToConn 
{
	public int connIndex;
	public ArrayList<Point> pointTab;
	
	public PointsToAddToConn(int connIndex, ArrayList<Point> pointTab) 
	{
		this.connIndex = connIndex;
		this.pointTab = pointTab;
	}
}
