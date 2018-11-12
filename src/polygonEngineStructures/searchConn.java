package polygonEngineStructures;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import structures.Frame;

public class searchConn 
{
	public PointLight P1, P2;
	public int stabIndex;
	public boolean left = false;
	public boolean isSpecial = false;
	public searchConn(PointLight p1, PointLight p2, boolean isLeft, int stabIndex)
	{
		this.P1 = p1;
		this.P2 = p2;
		this.left = isLeft;
		this.stabIndex = stabIndex;
	}
	
	public void draw(Frame f, Graphics2D g)
	{
		if(!isSpecial)
		{
		if(left)
			g.setColor(Color.yellow);
		else
			g.setColor(Color.ORANGE);
		}
		else
		{
			
			if(left)
				g.setColor(Color.green);
			else
				g.setColor(Color.blue);
			g.drawString(""+stabIndex, P1.x, 400);
			
		}
		g.draw(new Line2D.Float(f.gco().scaleValue(P1.x, P1.y)[0], f.gco().scaleValue(P1.x, P1.y)[1], f.gco().scaleValue(P2.x, P2.y)[0], f.gco().scaleValue(P2.x, P2.y)[1]));
	}
}
