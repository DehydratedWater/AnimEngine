package pastManager;

import java.util.ArrayList;

import renderSource.Point;

public class PointTabKeeper 
{
	public ArrayList<Point> pointTab;
	public PointTabKeeper(ArrayList<Point> pTab)
	{
		pointTab = new ArrayList<Point>(pTab.size());
		for(Point p : pTab)
		{
			pointTab.add(new Point(p));
		}
	}
}
