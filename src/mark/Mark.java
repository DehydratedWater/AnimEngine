package mark;

import java.util.ArrayList;

public class Mark 
{
	public float range[] = new float[4]; //minx, miny, maxx, maxy
	public ArrayList<Integer> tabOfPointIndex = new ArrayList<Integer>();
	public ArrayList<Integer> tabOfConnIndex = new ArrayList<Integer>();
	public int indexOfMovedPoint;
	public int indexOfMovedConn;
	public int indexOfMovedPolygon;
	public boolean isMarkedArea, isMarkedPoint, isMarkedConn, isMarkedPolygon;
}
