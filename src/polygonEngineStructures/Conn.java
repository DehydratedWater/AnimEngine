package polygonEngineStructures;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import structures.Frame;

public class Conn 
{
	public PointLight P1, P2;
	public int ConnectionNum;
	public float medY;
	public Conn(float x1, float y1, float x2, float y2, int ConnIndex) 
	{
		P1 = new PointLight(x1, y1);
		P2 = new PointLight(x2, y2);
		ConnectionNum = ConnIndex;
	}
	
	public Conn(Conn c) 
	{
		P1 = new PointLight(c.P2.x, c.P2.y);
		P2 = new PointLight(c.P1.x, c.P1.y);
		ConnectionNum = c.ConnectionNum;
	}
	public void generateMedY()
	{
		medY = (P1.y+P2.y)/2;
	}
	
	public void draw(Frame f, Graphics2D g)
	{
		g.setColor(Color.RED);
		g.draw(new Line2D.Float(f.gco().scaleValue(P1.x, P1.y)[0], f.gco().scaleValue(P1.x, P1.y)[1], f.gco().scaleValue(P2.x, P2.y)[0], f.gco().scaleValue(P2.x, P2.y)[1]));
	}
}
