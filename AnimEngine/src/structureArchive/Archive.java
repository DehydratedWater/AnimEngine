package structureArchive;

import java.util.ArrayList;

import renderSource.Connection;

public class Archive 
{
	public ArrayList<PointConn> pointConn; //Dodawaæ nowy obiekt przy ka¿dym dodanym punkcie a aktualizowaæ przy ka¿dym utworzonym odcinku
	public ArrayList<ConnPolygon> connPolygon; //Dodawaæ przy ka¿dym dodanym odcinku a aktualizowac przy ka¿dym utworzonym polygonie
	
	public Archive()
	{
		pointConn = new ArrayList<PointConn>();
		connPolygon = new ArrayList<ConnPolygon>();
	}
	
	public void addPoint()
	{
		pointConn.add(new PointConn());
	}
	public void addConnToPoint(int pointIndex, int connIndex)
	{
		pointConn.get(pointIndex).connIndexTab.add(connIndex);
	}
	public void removePoint(int pointIndex)
	{
		pointConn.remove(pointIndex);
	}
	public void addConnection()
	{
		connPolygon.add(new ConnPolygon());
	}
	public void addPolygonToConn(int connIndex, int polygonIndex)
	{
		connPolygon.get(connIndex).polygonIndexTab.add(polygonIndex);
	}
	public void removeConn(int connIndex, Connection c)
	{
		pointConn.get(c.getP1()).removeConnIndex(connIndex);
		pointConn.get(c.getP2()).removeConnIndex(connIndex);
		if(c.isDoubleArc())
		{
		pointConn.get(c.getP3()).removeConnIndex(connIndex);
		pointConn.get(c.getP4()).removeConnIndex(connIndex);
		}
	}
}
