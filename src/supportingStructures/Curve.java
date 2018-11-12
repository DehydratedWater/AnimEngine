package supportingStructures;

import java.util.ArrayList;

import renderSource.Connection;
import renderSource.Point;

public class Curve 
{
	public float Ax, Ay, Bx, By, Cx, Cy, Dx, Dy;
	public int Ai, Bi, Ci, Di;
	public Curve(float ax, float ay, float bx, float by, float cx, float cy, float dx, float dy, int ai, int bi, int ci, int di) 
	{
		Ax = ax;
		Ay = ay;
		Bx = bx;
		By = by;
		Cx = cx;
		Cy = cy;
		Dx = dx;
		Dy = dy;
		Ai = ai;
		Bi = bi;
		Ci = ci;
		Di = di;
	}
	
	public Curve(int ai, int bi, int ci, int di, ArrayList<Point> points) 
	{
		Ax = points.get(ai).x;
		Ay = points.get(ai).y;
		Bx = points.get(bi).x;
		By = points.get(bi).y;
		Cx = points.get(ci).x;
		Cy = points.get(ci).y;
		Dx = points.get(di).x;
		Dy = points.get(di).y;
		Ai = ai;
		Bi = bi;
		Ci = ci;
		Di = di;
	}
	
	public Curve(Connection c, ArrayList<Point> points) 
	{
		Ax = points.get(c.getP1()).x;
		Ay = points.get(c.getP1()).y;
		Bx = points.get(c.getP4()).x;
		By = points.get(c.getP4()).y;
		Cx = points.get(c.getP3()).x;
		Cy = points.get(c.getP3()).y;
		Dx = points.get(c.getP2()).x;
		Dy = points.get(c.getP2()).y;
		Ai = c.getP1();
		Bi = c.getP4();
		Ci = c.getP3();
		Di = c.getP2();
	}
	
	public float[] getPointOnCurve(float t) //œrednio 1300ns
	{
		float[] xy = new float[2];
		xy[0] = (float) (Ax*Math.pow((1-t), 3)+3*Bx*t*Math.pow((1-t), 2)+3*Cx*t*t*(1-t)+Dx*t*t*t);
		xy[1] = (float) (Ay*Math.pow((1-t), 3)+3*By*t*Math.pow((1-t), 2)+3*Cy*t*t*(1-t)+Dy*t*t*t);
		return xy;
	}
	
	public float[] getPointOnCurveDeCasteljau(float t) //1000 nano sekund wolniejszy od getPointOnCurve ale liczy równie¿ punkty kontrolne œrednio 2200ns
	{
		Vector2D v1 = new Vector2D(Bx-Ax, By-Ay);
		Vector2D v2 = new Vector2D(Cx-Bx, Cy-By);
		Vector2D v3 = new Vector2D(Dx-Cx, Dy-Cy);
		v1.multiply(t);
		v2.multiply(t);
		v3.multiply(t);
		
		SimplePoint p1 = new SimplePoint(Ax+v1.a, Ay+v1.b);
		SimplePoint p2 = new SimplePoint(Bx+v2.a, By+v2.b);
		SimplePoint p3 = new SimplePoint(Cx+v3.a, Cy+v3.b);
		
		Vector2D w1 = new Vector2D(p2.x-p1.x, p2.y-p1.y);
		Vector2D w2 = new Vector2D(p3.x-p2.x, p3.y-p2.y);
		
		w1.multiply(t);
		w2.multiply(t);
		
		SimplePoint p4 = new SimplePoint(p1.x+w1.a, p1.y+w1.b);
		SimplePoint p5 = new SimplePoint(p2.x+w2.a, p2.y+w2.b);
		
		Vector2D v = new Vector2D(p5.x-p4.x, p5.y-p4.y);
		
		v.multiply(t);
		
		SimplePoint p = new SimplePoint(p4.x+v.a, p4.y+v.b);
		//p1 p3 p4 p5
		return new float[]{p.x, p.y};
	}
	
	public SimplePoint[] getPointOnCurveDeCasteljauPoints(float t) //1000 nano sekund wolniejszy od getPointOnCurve ale liczy równie¿ punkty kontrolne œrednio 2200ns
	{
		Vector2D v1 = new Vector2D(Bx-Ax, By-Ay);
		Vector2D v2 = new Vector2D(Cx-Bx, Cy-By);
		Vector2D v3 = new Vector2D(Dx-Cx, Dy-Cy);
		v1.multiply(t);
		v2.multiply(t);
		v3.multiply(t);
		
		SimplePoint p1 = new SimplePoint(Ax+v1.a, Ay+v1.b);
		SimplePoint p2 = new SimplePoint(Bx+v2.a, By+v2.b);
		SimplePoint p3 = new SimplePoint(Cx+v3.a, Cy+v3.b);
		
		Vector2D w1 = new Vector2D(p2.x-p1.x, p2.y-p1.y);
		Vector2D w2 = new Vector2D(p3.x-p2.x, p3.y-p2.y);
		
		w1.multiply(t);
		w2.multiply(t);
		
		SimplePoint p4 = new SimplePoint(p1.x+w1.a, p1.y+w1.b);
		SimplePoint p5 = new SimplePoint(p2.x+w2.a, p2.y+w2.b);
		
		Vector2D v = new Vector2D(p5.x-p4.x, p5.y-p4.y);
		
		v.multiply(t);
		
		SimplePoint p = new SimplePoint(p4.x+v.a, p4.y+v.b);
		//p1 p3 p4 p5
		return new SimplePoint[]{p1, p4, p, p5, p3};
	}
}
