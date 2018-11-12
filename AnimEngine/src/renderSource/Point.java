package renderSource;

public class Point 
{
	public boolean TechPoint = false;
	public boolean MarkedPoint = false;
	public float x = 0;
	public float y = 0;
	public curvesPointsConnector cpc = null;

public Point(float X, float Y)
{
	this.x = X;
	this.y = Y;
}
public Point(float X, float Y, boolean techPoint)
{
	this.x = X;
	this.y = Y;
	this.TechPoint = techPoint;

}
public Point(Point P)
{
	this.x = P.x;
	this.y = P.y;
	this.cpc = P.cpc;
	this.TechPoint = P.TechPoint;
}
}
