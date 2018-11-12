package renderSource;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.QuadCurve2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import polygonEngine.FilledPolygonWithCurves;
import polygonEngineStructures.shapeConnector;
import supportingStructures.TransformationBox;

public class ObjectRenderer 
{
	public static void drawObject(Graphics2D g, Point[] raundPointTab, ArrayList<Point> PointTab, ArrayList<Connection> ConnectionTab, ArrayList<FilledPolygon> PolygonTab, ArrayList<Integer> polygonToDraw, TransformationBox t)
	{

		
		if(Object.RenderPolygonsWithAntiAliasing)
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		else
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		if(PolygonTab.size()!=0)
		{
			//System.out.println("Wyœwietlanie Polygona ");
			drawPolygons(g, raundPointTab, PolygonTab, polygonToDraw, t);
		}

		if(Object.RenderLinesWithAntiAliasing)
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		else
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		
		DrawConnectiones(g, raundPointTab, ConnectionTab, t);
		
		
		if(!Object.drawWithOutMark)
		{
		if(Object.showPoints)
			DrawAllPoints(g, raundPointTab, PointTab, t);
		else
			DrawMarkedPoints(g, raundPointTab, PointTab, t);
		}
		
	}
	
	public static void drawObjectBETA(Graphics2D g, Point[] raundPointTab, ArrayList<Point> PointTab, ArrayList<Connection> ConnectionTab, ArrayList<FilledPolygonWithCurves> PolygonTab, ArrayList<Integer> polygonToDraw, TransformationBox t)
	{

		//System.out.println("ODŒW");
		if(Object.RenderPolygonsWithAntiAliasing)
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		else
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		if(PolygonTab.size()!=0)
		{
			//System.out.println("Wyœwietlanie Polygona ");
			drawPolygonsWithCurves(g, raundPointTab, PolygonTab, polygonToDraw, t);
		}
		
		
		if(Object.RenderLinesWithAntiAliasing)
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		else
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		
		DrawConnectiones(g, raundPointTab, ConnectionTab, t);
		
		
//		try {
			drawPolygonsWithCurvesFromList(g, raundPointTab, PolygonTab, polygonToDraw, t);
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
		
		
		if(!Object.drawWithOutMark)
		{
		if(Object.showPoints)
			DrawAllPoints(g, raundPointTab, PointTab, t);
		else
			DrawMarkedPoints(g, raundPointTab, PointTab, t);
		}
		
	}
	
	
	
	private static void drawPolygonsWithCurvesFromList(Graphics2D g2, Point[] raundPointTab, ArrayList<FilledPolygonWithCurves> PolygonTab, ArrayList<Integer> polygonToDraw, TransformationBox t) {
		for(int i : polygonToDraw)
		{
		if(PolygonTab.get(i).c!=null&&PolygonTab.get(i).shapeTab.size()>0&&PolygonTab.get(i).isUsedPointRemoved()==false)
		{
		ArrayList<shapeConnector> tab = PolygonTab.get(i).shapeTab;

		Path2D p=new Path2D.Float();
		p.moveTo((raundPointTab[tab.get(0).getPoint()].x*t.scaleX +t.X),(raundPointTab[tab.get(0).getPoint()].y*t.scaleY +t.Y));
		for(int j = 0; j< tab.size()-1; j++)
		{
			if(!tab.get(j).polygonEnd)
			{
				//System.out.println(tab[j].getPoint1());
			if(tab.get(j).isArc==false)
			{
			p.lineTo((raundPointTab[tab.get(j).getPoint()].x*t.scaleX +t.X), (raundPointTab[tab.get(j).getPoint()].y*t.scaleY +t.Y));
			}
			else if(tab.get(j).isArc==true)
			{
				int s[] = tab.get(j).getPoints();
				if(tab.get(j).isDoubleArc==true)
				{
					p.curveTo((raundPointTab[s[2]].x*t.scaleX +t.X), (raundPointTab[s[2]].y*t.scaleY +t.Y),
							(raundPointTab[s[1]].x*t.scaleX +t.X), (raundPointTab[s[1]].y*t.scaleY +t.Y),
							(raundPointTab[s[0]].x*t.scaleX +t.X), (raundPointTab[s[0]].y*t.scaleY +t.Y));
				}
				else
				{
					p.quadTo((raundPointTab[s[1]].x*t.scaleX +t.X), (raundPointTab[s[1]].y*t.scaleY +t.Y), 
							(raundPointTab[s[0]].x*t.scaleX +t.X), (raundPointTab[s[0]].y*t.scaleY +t.Y));
				}
			}
			}
			else
			{
				j++;
				p.moveTo((raundPointTab[tab.get(j).getPoint()].x*t.scaleX +t.X), (raundPointTab[tab.get(j).getPoint()].y*t.scaleY +t.Y));
				continue;
			}
		}

		Color colN = new Color(PolygonTab.get(i).c.getRGB());
		BufferedImage buffImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
		Graphics2D gg = buffImage.createGraphics();   
//		gg.setColor(colN);
//		gg.drawRect(4, 4, 2, 2);
		gg.setColor(colN);
		gg.fillRect(0, 0, 10, 10);
		gg.setColor(Color.GRAY);
		gg.fillRect(4, 4, 2, 2);
		g2.setPaint(new TexturePaint(buffImage, new Rectangle(10, 10) ) );
		//g2.setColor(colN);
		g2.fill(p);
		}
		else
		{
			System.out.println("Usuwanie polygona "+i);
			PolygonTab.remove(i);
			i--;
		}
		}
		
	}
	private static void drawPolygonsWithCurves(Graphics2D g2, Point[] raundPointTab, ArrayList<FilledPolygonWithCurves> PolygonTab, ArrayList<Integer> polygonToDraw, TransformationBox t) {
		boolean tabB[] = new boolean[PolygonTab.size()];
		
		System.out.println("Rysowanie polygonów "+PolygonTab.size());
		for(int i = 0; i < polygonToDraw.size(); i++)
		{
			//System.out.println("Polygon na liœcie nr "+i);
			tabB[polygonToDraw.get(i)] = true;
		}
		
		for(int i = 0; i < PolygonTab.size(); i++)
		{
		if(tabB[i])
		{
			//System.out.println("Pomijanie Polygona "+i);
			continue;
		}
		if(PolygonTab.get(i).c!=null&&PolygonTab.get(i).shapeTab.size()>0&&PolygonTab.get(i).isUsedPointRemoved()==false)
		{
		ArrayList<shapeConnector> tab = PolygonTab.get(i).shapeTab;

		Path2D p=new Path2D.Float();
		p.moveTo((raundPointTab[tab.get(0).getPoint()].x*t.scaleX +t.X),(raundPointTab[tab.get(0).getPoint()].y*t.scaleY +t.Y));
		for(int j = 0; j< tab.size()-1; j++)
		{
			if(!tab.get(j).polygonEnd)
			{
				//System.out.println(tab[j].getPoint1());
			if(tab.get(j).isArc==false)
			{
			p.lineTo((raundPointTab[tab.get(j).getPoint()].x*t.scaleX +t.X), (raundPointTab[tab.get(j).getPoint()].y*t.scaleY +t.Y));
			}
			else if(tab.get(j).isArc==true)
			{
				int s[] = tab.get(j).getPoints();
				if(tab.get(j).isDoubleArc==true)
				{
					p.curveTo((raundPointTab[s[2]].x*t.scaleX +t.X), (raundPointTab[s[2]].y*t.scaleY +t.Y),
							(raundPointTab[s[1]].x*t.scaleX +t.X), (raundPointTab[s[1]].y*t.scaleY +t.Y),
							(raundPointTab[s[0]].x*t.scaleX +t.X), (raundPointTab[s[0]].y*t.scaleY +t.Y));
				}
				else
				{
					p.quadTo((raundPointTab[s[1]].x*t.scaleX +t.X), (raundPointTab[s[1]].y*t.scaleY +t.Y), 
							(raundPointTab[s[0]].x*t.scaleX +t.X), (raundPointTab[s[0]].y*t.scaleY +t.Y));
				}
			}
			}
			else
			{
				j++;
				p.moveTo((raundPointTab[tab.get(j).getPoint()].x*t.scaleX +t.X), (raundPointTab[tab.get(j).getPoint()].y*t.scaleY +t.Y));
				continue;
			}
		}

		
		g2.setColor(PolygonTab.get(i).c);
		g2.fill(p);
		}
		else
		{
			System.out.println("Usuwanie polygona "+i);
			PolygonTab.remove(i);
			i--;
		}
		}
	}
	private static void drawPolygons(Graphics2D g2, Point[] raundPointTab, ArrayList<FilledPolygon> PolygonTab, ArrayList<Integer> polygonToDraw, TransformationBox t) {
		boolean tabB[] = new boolean[polygonToDraw.size()];
		for(int i = 0; i < polygonToDraw.size(); i++)
		{
			//System.out.println("Polygon na liœcie nr "+i);
			tabB[i] = true;
		}
		
		for(int i = 0; i < PolygonTab.size(); i++)
		{	
		if(tabB[i])
		{
			//System.out.println("Pomijanie Polygona "+i);
			continue;
		}
		if(PolygonTab.get(i).c!=null&&PolygonTab.get(i).connPointTab.size()>0)
		{
		ArrayList<PolygonConn> tab = PolygonTab.get(i).connPointTab;

		Path2D p=new Path2D.Float();
		p.moveTo((raundPointTab[tab.get(0).P1].x*t.scaleX +t.X),(raundPointTab[tab.get(0).P1].y*t.scaleY +t.Y));
		for(int j = 0; j< tab.size()-1; j++)
		{
			if(tab.get(j).P1!=-10)
			{
				//System.out.println(tab[j].getPoint1());
			if(tab.get(j).isArc==false)
			{
			p.lineTo((raundPointTab[tab.get(j).P1].x*t.scaleX +t.X), (raundPointTab[tab.get(j).P1].y*t.scaleY +t.Y));
			}
			else if(tab.get(j).isArc==true)
			{
				if(tab.get(j).isDoubleArc==true)
				{
					p.curveTo((raundPointTab[tab.get(j).P2].x*t.scaleX +t.X), (raundPointTab[tab.get(j).P2].y*t.scaleY +t.Y),
							(raundPointTab[tab.get(j).P3].x*t.scaleX +t.X), (raundPointTab[tab.get(j).P3].y*t.scaleY +t.Y),
							(raundPointTab[tab.get(j).P1].x*t.scaleX +t.X), (raundPointTab[tab.get(j).P1].y*t.scaleY +t.Y));
				}
				else
				{
					p.quadTo((raundPointTab[tab.get(j).P2].x*t.scaleX +t.X), (raundPointTab[tab.get(j).P2].y*t.scaleY +t.Y), 
							(raundPointTab[tab.get(j).P1].x*t.scaleX +t.X), (raundPointTab[tab.get(j).P1].y*t.scaleY +t.Y));
				}
			}
			}
			else
			{
				j++;
				p.moveTo((raundPointTab[tab.get(j).P1].x*t.scaleX +t.X), (raundPointTab[tab.get(j).P1].y*t.scaleY +t.Y));
				continue;
			}
		}

		
		g2.setColor(PolygonTab.get(i).c);
		g2.fill(p);
		}
		else
		{
			PolygonTab.remove(i);
			i--;
		}
		}
	}
	private static void DrawMarkedPoints(Graphics2D g2, Point[] raundPointTab, ArrayList<Point> PointTab, TransformationBox t) {
		BasicStroke s = new BasicStroke(3f*t.WindowScale, BasicStroke.CAP_ROUND,  BasicStroke.JOIN_ROUND);
		g2.setStroke(s);
		for(int i = 0; i < raundPointTab.length;i++)
		{	
			if(PointTab.get(i).MarkedPoint)
			{
				g2.setColor(Color.BLACK);
				g2.drawOval((int)(raundPointTab[i].x*t.scaleX +t.X-5*t.scaleX/t.WindowScale), (int)(raundPointTab[i].y*t.scaleY +t.Y-5*t.scaleX/t.WindowScale), (int)(10), (int)(10));
				g2.setColor(Color.RED);
				g2.fillOval((int)(raundPointTab[i].x*t.scaleX +t.X-5*t.scaleX/t.WindowScale), (int)(raundPointTab[i].y*t.scaleY +t.Y-5*t.scaleX/t.WindowScale), (int)(10), (int)(10));
			}
		}
	}

	private static void DrawAllPoints(Graphics2D g2, Point[] raundPointTab, ArrayList<Point> PointTab, TransformationBox t) {
		BasicStroke s = new BasicStroke(3f, BasicStroke.CAP_ROUND,  BasicStroke.JOIN_ROUND);
		g2.setStroke(s);
		for(int i = 0; i < raundPointTab.length;i++)
		{
			g2.setColor(Color.BLACK);
			g2.drawOval((int)(raundPointTab[i].x*t.scaleX +t.X-5*t.scaleX/t.WindowScale), (int)(raundPointTab[i].y*t.scaleY +t.Y-5*t.scaleX/t.WindowScale), (int)(10), (int)(10));
			if(PointTab.get(i).MarkedPoint)
			{
				
				if(PointTab.get(i).TechPoint)
				{
					g2.setColor(Color.green);
					g2.setStroke(new BasicStroke(1));
					g2.draw(new Line2D.Float(raundPointTab[i].x*t.scaleX +t.X, raundPointTab[i].y*t.scaleY +t.Y, raundPointTab[PointTab.get(i).cpc.p1].x*t.scaleX +t.X, raundPointTab[PointTab.get(i).cpc.p1].y*t.scaleY +t.Y));
					g2.setStroke(s);
				}
				g2.setColor(Color.RED);
			}
			else
			{
				if(!PointTab.get(i).TechPoint)
					g2.setColor(Color.YELLOW);
				else
				{
					g2.setColor(Color.green);
					g2.setStroke(new BasicStroke(1));
					g2.draw(new Line2D.Float(raundPointTab[i].x*t.scaleX +t.X, raundPointTab[i].y*t.scaleY +t.Y, raundPointTab[PointTab.get(i).cpc.p1].x*t.scaleX +t.X, raundPointTab[PointTab.get(i).cpc.p1].y*t.scaleY +t.Y));
					g2.setStroke(s);
				}
			}
			g2.fillOval((int)(raundPointTab[i].x*t.scaleX +t.X-5*t.scaleX/t.WindowScale), (int)(raundPointTab[i].y*t.scaleY +t.Y-5*t.scaleX/t.WindowScale), (int)(10), (int)(10));
			g2.setColor(Color.black);
			g2.drawString(""+i, (int)(raundPointTab[i].x*t.scaleX +t.X-5*t.scaleX/t.WindowScale), (int)(raundPointTab[i].y*t.scaleY +t.Y-6*t.scaleX/t.WindowScale));
		}
	}

	private static void DrawConnectiones(Graphics2D g2, Point[] raundPointTab, ArrayList<Connection> ConnectionTab, TransformationBox t) 
	{
		//System.out.println(ConnectionTab.size());
		for(int i = 0; i < ConnectionTab.size(); i++)
		{
			
			BasicStroke s = null;
			if(ConnectionTab.get(i).isMarked()&&!Object.drawWithOutMark)
			{
				Color newC = ConnectionTab.get(i).getC();
				newC = new Color(newC.getRed(), newC.getGreen(), newC.getBlue(), 128);
				g2.setColor(newC);
				float dash1[] = {10.0f};
				s = new BasicStroke(ConnectionTab.get(i).getSize()*((t.scaleX+t.scaleY)/2), BasicStroke.CAP_ROUND,  BasicStroke.JOIN_ROUND, 10.0f, dash1, 0.0f);
			}
			else
			{
				g2.setColor(ConnectionTab.get(i).getC());
				s = new BasicStroke(ConnectionTab.get(i).getSize()*((t.scaleX+t.scaleY)/2), BasicStroke.CAP_ROUND,  BasicStroke.JOIN_ROUND);
			}
			g2.setStroke(s);	
			if(ConnectionTab.get(i).isARC()==false)
			{
				if(ConnectionTab.get(i).isOnlyShape()==false)
				{
					DrawLine(g2, raundPointTab, i, ConnectionTab, t);
					
					if(Object.drawConnNumber)
					{
					g2.setColor(Color.GREEN);
					g2.drawString(""+i, ((raundPointTab[ConnectionTab.get(i).getP1()].x*t.scaleX + t.X)+(raundPointTab[ConnectionTab.get(i).getP2()].x*t.scaleX + t.X))/2,
							((raundPointTab[ConnectionTab.get(i).getP1()].y*t.scaleY +t.Y)+(raundPointTab[ConnectionTab.get(i).getP2()].y*t.scaleY +t.Y))/2);
					}
				}
			}
			else
			{
				if(ConnectionTab.get(i).isOnlyShape()==false)
				{
					if(ConnectionTab.get(i).isDoubleArc())
					{
						DrawDoubleArc(g2, raundPointTab, i, ConnectionTab, t);
					}
					else
					{
						DrawArc(g2, raundPointTab, i, ConnectionTab, t);
					}
				}
			}
		}
	}
	
	private static void DrawLine(Graphics2D g2, Point[] raundPointTab, int i, ArrayList<Connection> ConnectionTab, TransformationBox t) {
		g2.draw(new Line2D.Float((raundPointTab[ConnectionTab.get(i).getP1()].x*t.scaleX + t.X), (raundPointTab[ConnectionTab.get(i).getP1()].y*t.scaleY +t.Y), 
				(raundPointTab[ConnectionTab.get(i).getP2()].x*t.scaleX + t.X), (raundPointTab[ConnectionTab.get(i).getP2()].y*t.scaleY +t.Y)));
	}

	private static void DrawArc(Graphics2D g2, Point[] raundPointTab, int i, ArrayList<Connection> ConnectionTab, TransformationBox t) {
		QuadCurve2D cd = new QuadCurve2D.Float();
		cd.setCurve((raundPointTab[ConnectionTab.get(i).getP1()].x*t.scaleX + t.X), (raundPointTab[ConnectionTab.get(i).getP1()].y*t.scaleY +t.Y),
				(raundPointTab[ConnectionTab.get(i).getP3()].x*t.scaleX + t.X), (raundPointTab[ConnectionTab.get(i).getP3()].y*t.scaleY +t.Y),
				(raundPointTab[ConnectionTab.get(i).getP2()].x*t.scaleX + t.X), (raundPointTab[ConnectionTab.get(i).getP2()].y*t.scaleY +t.Y));
		g2.setColor(ConnectionTab.get(i).getC());
		g2.draw(cd);
	}

	private static void DrawDoubleArc(Graphics2D g2, Point[] raundPointTab, int i, ArrayList<Connection> ConnectionTab, TransformationBox t) {
		CubicCurve2D cd = new CubicCurve2D.Float();
		cd.setCurve((raundPointTab[ConnectionTab.get(i).getP1()].x*t.scaleX + t.X), (raundPointTab[ConnectionTab.get(i).getP1()].y*t.scaleY +t.Y),
				(raundPointTab[ConnectionTab.get(i).getP4()].x*t.scaleX + t.X), (raundPointTab[ConnectionTab.get(i).getP4()].y*t.scaleY +t.Y),
				(raundPointTab[ConnectionTab.get(i).getP3()].x*t.scaleX + t.X), (raundPointTab[ConnectionTab.get(i).getP3()].y*t.scaleY +t.Y),
				(raundPointTab[ConnectionTab.get(i).getP2()].x*t.scaleX + t.X), (raundPointTab[ConnectionTab.get(i).getP2()].y*t.scaleY +t.Y));
		g2.setColor(ConnectionTab.get(i).getC());
		g2.draw(cd);
	}
}
