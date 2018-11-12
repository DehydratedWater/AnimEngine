package renderSource;

import java.awt.Color;
import java.util.ArrayList;

public class Connection 
{
private int P1;
private int P2;
private int P3;
private int P4;
private float size;
private Color c;
private Color fillC;
private boolean marked = false;
private boolean ARC, DoubleArc, OnlyShape;
//data about line
private float maxX, maxY, minX, minY, Cx, Cy, distance, r;

public void printCon()
{
	System.out.println(P1+" "+P2);
}

public Connection clone()
{
	return new Connection(ARC, DoubleArc, P1, P2, P3, P4, size, c);
}
public Connection(Connection c) 
{
	setP1(c.P1);
	setP2(c.P2);
	setP3(c.P3);
	setP4(c.P4);
	setC(c.getC());
	setSize(c.size);
	setARC(c.ARC);
	setDoubleArc(c.DoubleArc);
}
public Connection(int point1, int point2, float Size, Color C) 
{
	setP1(point1);
	setP2(point2);
	this.setSize(Size);
	this.setC(C);
}
public Connection(int point1, int point2, int point3, float Size, Color C) 
{
	setP1(point1);
	setP2(point2);
	setP3(point3);
	this.setSize(Size);
	this.setC(C);
	setARC(true);
}
public Connection(int point1, int point2, int point3, int point4, float Size, Color C) 
{
	setP1(point1);
	setP2(point2);
	setP3(point3);
	setP4(point4);
	setARC(true);
	setDoubleArc(true);
	this.setSize(Size);
	this.setC(C);
}
public Connection(boolean isA, boolean isDA, int point1, int point2, int point3, int point4, float Size) 
{
	setARC(isA);
	setDoubleArc(isDA);
	setP1(point1);
	setP2(point2);
	setP3(point3);
	setP4(point4);
	this.setSize(Size);

}

public Connection(boolean isA, boolean isDA ,int point1, int point2, int point3, int point4, float Size, Color C) 
{
	setARC(isA);
	setDoubleArc(isDA);
	setP1(point1);
	setP2(point2);
	setP3(point3);
	setP4(point4);

	this.setSize(Size);
	this.setC(C);
}

public Connection(int point1, int point2, int point3, int point4, boolean isA, boolean isDA, float Size, Color C) 
{
	setARC(isA);
	setDoubleArc(isDA);
	setP1(point1);
	setP2(point2);
	setP3(point3);
	setP4(point4);

	this.setSize(Size);
	this.setC(C);
}

public Connection(int point1, int point2, int point3, int point4) 
{
	setP1(point1);
	setP2(point2);
	setP3(point3);
	setP3(point4);
	setARC(true);
	setDoubleArc(true);
	setOnlyShape(true);
}
public Connection(int point1, int point2, int point3) 
{
	setP1(point1);
	setP2(point2);
	setP3(point3);
	setARC(true);
	setOnlyShape(true);
}
public Connection(int point1, int point2) 
{
	setP1(point1);
	setP2(point2);
	setOnlyShape(true);
}

public boolean isConnected(int point)
{
	if(point==P2)
	{
		return true;
	}
	if(point==P1)
	{
		return true;
	}
	else
	{
		return false;
	}
}



public double distance(float x1, float y1, float x2, float y2)
{
	return Math.sqrt(Math.pow((x2-x1), 2)+Math.pow((y2-y1), 2));
}

public void generateLineData(ArrayList<renderSource.Point> pointTab)
{
	if(pointTab.get(P1).x>pointTab.get(P2).x)
	{
		maxX = pointTab.get(P1).x;
		minX = pointTab.get(P2).x;
	}
	else
	{
		maxX = pointTab.get(P2).x;
		minX = pointTab.get(P1).x;
	}
	if(pointTab.get(P1).y>pointTab.get(P2).y)
	{
		maxY = pointTab.get(P1).y;
		minY = pointTab.get(P2).y;
	}
	else
	{
		maxY = pointTab.get(P2).y;
		minY = pointTab.get(P1).y;
	}
	
	Cx = (maxX+minX)/2;
	Cy = (maxY+minY)/2;
	
	distance = (float) distance(pointTab.get(P1).x, pointTab.get(P1).y, pointTab.get(P2).x, pointTab.get(P2).y);
	
	r=distance/2;
}

public int getSecondPoint(int point)
{
	if(point==P1)
	{
		return P2;
	}
	if(point==P2)
	{
		return P1;
	}
	else
	{
		System.out.println("Err: Point not connected");
		return 0;
	}
}


public void decreseP1(int i)
{
	P1+=i;
}
public void decreseP2(int i)
{
	P2+=i;
}
public void decreseP3(int i)
{
	P4+=i;
}
public void decreseP4(int i)
{
	P4+=i;
}
public int getP1() {
	return P1;
}

public float getSize() {
	return size;
}
public int getP2() {
	return P2;
}
public void setP1(int p1) {
	P1 = p1;
}
public void setP2(int p2) {
	P2 = p2;
}
public void setSize(float size) {
	this.size = size;
}
public Color getC() {
	return c;
}
public void setC(Color c) {
	this.c = c;
}
public boolean isARC() {
	return ARC;
}
public void setARC(boolean aRC) {
	ARC = aRC;
}

public Color getFillC() {
	return fillC;
}
public void setFillC(Color fillC) {
	this.fillC = fillC;
}
public int getP3() {
	return P3;
}
public void setP3(int p3) {
	P3 = p3;
}
public boolean isOnlyShape() {
	return OnlyShape;
}
public void setOnlyShape(boolean onlyShape) {
	OnlyShape = onlyShape;
}
public int getP4() {
	return P4;
}
public void setP4(int p4) {
	P4 = p4;
}
public boolean isDoubleArc() {
	return DoubleArc;
}
public void setDoubleArc(boolean doubleArc) {
	DoubleArc = doubleArc;
}
public boolean isMarked() {
	return marked;
}
public void setMarked(boolean marked) {
	this.marked = marked;
}

public float getMaxX() {
	return maxX;
}
public void setMaxX(float maxX) {
	this.maxX = maxX;
}
public float getMaxY() {
	return maxY;
}
public void setMaxY(float maxY) {
	this.maxY = maxY;
}
public float getMinX() {
	return minX;
}
public void setMinX(float minX) {
	this.minX = minX;
}
public float getMinY() {
	return minY;
}
public void setMinY(float minY) {
	this.minY = minY;
}
public float getCx() {
	return Cx;
}
public void setCx(float cx) {
	Cx = cx;
}
public float getCy() {
	return Cy;
}
public void setCy(float cy) {
	Cy = cy;
}
public float getDistance() {
	return distance;
}
public void setDistance(float distance) {
	this.distance = distance;
}
public float getR() {
	return r;
}
public void setR(float r) {
	this.r = r;
}


}
