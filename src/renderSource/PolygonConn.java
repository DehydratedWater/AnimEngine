package renderSource;

public class PolygonConn 
{
	public int P1, P2, P3, P4;
	public boolean isArc, isDoubleArc;
	public PolygonConn(int p1) 
	{
		this.P1 = p1;
		isArc = false;
		isDoubleArc = false;
	}
	public PolygonConn(int p1, int p2) 
	{
		this.P1 = p1;
		this.P2 = p2;
		isArc = true;
		isDoubleArc = false;
	}
	public PolygonConn(int p1, int p2, int p3) 
	{
		this.P1 = p1;
		this.P2 = p2;
		this.P3 = p3;
		isArc = true;
		isDoubleArc = true;
	}
	
	public PolygonConn(boolean arc, boolean doubleArc,int p1, int p2, int p3) 
	{
		this.P1 = p1;
		this.P2 = p2;
		this.P3 = p3;
		isArc = arc;
		isDoubleArc = doubleArc;
	}
}
