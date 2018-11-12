package polygonEngineStructures;

import renderSource.Connection;

public class shapeConnector 
{
	public int P1, P2, P3, P4;
	public int connIndex;
	public boolean isArc = false, isDoubleArc = false;
	public boolean fromLeft = true;
	public boolean polygonEnd = false;
	public shapeConnector(Connection c, boolean fromLeft, int connIndex)
	{
		this.P1 = c.getP1();
		this.P2 = c.getP2();
		this.P3 = c.getP3();
		this.P4 = c.getP4();
		this.isArc = c.isARC();
		this.isDoubleArc = c.isDoubleArc();
		this.fromLeft = fromLeft;
		this.connIndex = connIndex;
	}
	public shapeConnector(shapeConnector s)
	{
		this.P1 = s.P1;
		this.P2 = s.P2;
		this.P3 = s.P3;
		this.P4 = s.P4;
		this.isArc = s.isArc;
		this.isDoubleArc = s.isDoubleArc;
		this.fromLeft = s.fromLeft;;
		this.connIndex = s.connIndex;
		this.polygonEnd = s.polygonEnd;
	}
	
	public shapeConnector(boolean arc, boolean doubleArc, boolean left, boolean end, int index, int p1, int p2, int p3, int p4)
	{
		this.P1 = p1;
		this.P2 = p2;
		this.P3 = p3;
		this.P4 = p4;
		this.isArc = arc;
		this.polygonEnd = end;
		this.isDoubleArc = doubleArc;
		this.fromLeft = left;
		this.connIndex = index;
	}
	public shapeConnector()
	{
		this.polygonEnd = true;
	}
	public int[] getPoints()
	{
		if(fromLeft)
			return new int[]{P1, P4, P3};
		else
			return new int[]{P2, P3, P4};
	}
	
	public int getPoint()
	{
		if(fromLeft)
			return P2;
		else
			return P1;
	}
	public void editPoint(int c)
	{
		if(fromLeft)
			P2 = c;
		else
			P1 = c;
	}
	public void editReversedPoint(int c)
	{
		if(fromLeft)
			P1 = c;
		else
			P2 = c;
	}
	public void decresePointIndex()
	{
		if(fromLeft)
			P2--;
		else
			P1--;
	}
	
	public int getSecondPoint()
	{
		if(!fromLeft)
			return P2;
		else
			return P1;
	}
	
	public void drawConnector()
	{
		if(!polygonEnd)
		{
		if(fromLeft)
			System.out.println(P2+"-"+P4+"-"+P3+"-"+P1);
		else
			System.out.println(P1+"-"+P3+"-"+P4+"-"+P2);
		}
		else
		{
			System.out.println("-10");
		}
	}
	public void drawPoint()
	{
		if(!polygonEnd)
		{
		if(fromLeft)
			System.out.print(P2+" ");
		else
			System.out.print(P1+" ");
		}
		else
		{
			System.out.print("-10 ");
		}
	}
	
}
