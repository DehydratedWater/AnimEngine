package supportingStructures;

import renderSource.Point;

public class QuickConn 
{
	public Point p1, p2;
	public QuickConn(float x1, float y1, float x2, float y2)
	{
		p1 = new Point(x1, y1);
		p2 = new Point(x2, y2);
	}
}
