package renderSource;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import structures.Frame;
import toolBox.frameCupture;

public class FilledPolygon 
{
	public ArrayList<PolygonConn> connPointTab;
	public ArrayList<Integer> connectionIndexTab;
	public boolean needToCheck = false;
	public ArrayList<Integer> pointsAddedToLines;
	
	
	public ArrayList<Integer> addedPoints;
	
	public boolean usedPoints[];
	public float[] minMax = null;
	public Color c;
	public boolean usedPointRemoved = false;
	public FilledPolygon(ArrayList<PolygonConn> connTab, ArrayList<Integer> connIndex)
	{
		connPointTab = connTab;
		connectionIndexTab = connIndex;
		addedPoints = new ArrayList<Integer>();
	}
	public FilledPolygon(ArrayList<PolygonConn> connTab, ArrayList<Integer> connIndex, Color c)
	{
		connPointTab = connTab;
		connectionIndexTab = connIndex;
		this.c = c;
		addedPoints = new ArrayList<Integer>();
	}
	
	public void generateMinMax(ArrayList<Point> pTab)
	{
		
		minMax = new float[4];
		Point p1 = pTab.get(connPointTab.get(0).P1), p2, p3;
		minMax[0] = p1.x;
		minMax[1] = p1.y;
		minMax[2] = p1.x;
		minMax[3] = p1.y;
		for(int i = 0; i < connPointTab.size(); i++)
		{
			if(connPointTab.get(i).P1==-10)
				continue;
			p1 = pTab.get(connPointTab.get(i).P1);
			if(p1.x<minMax[0])
				minMax[0] = p1.x;
			else if(p1.x>minMax[2])
				minMax[2] = p1.x;
			if(p1.y<minMax[1])
				minMax[1] = p1.y;
			else if(p1.y>minMax[3])
				minMax[3] = p1.y;
			if(connPointTab.get(i).isDoubleArc)
			{
				p2 = pTab.get(connPointTab.get(i).P2);
				p3 = pTab.get(connPointTab.get(i).P3);
				if(p2.x<minMax[0])
					minMax[0] = p2.x;
				else if(p2.x>minMax[2])
					minMax[2] = p2.x;
				if(p2.y<minMax[1])
					minMax[1] = p2.y;
				else if(p2.y>minMax[3])
					minMax[3] = p2.y;
				if(p3.x<minMax[0])
					minMax[0] = p3.x;
				else if(p3.x>minMax[2])
					minMax[2] = p3.x;
				if(p3.y<minMax[1])
					minMax[1] = p3.y;
				else if(p3.y>minMax[3])
					minMax[3] = p3.y;
			}
		}
		
	}
	
	public void generateBooleanTab()
	{
		int max = connPointTab.get(0).P1;
		for(int i =0; i < connPointTab.size(); i++)
		{
			if(max<connPointTab.get(i).P1)
				max = connPointTab.get(i).P1;
		}
		usedPoints = new boolean[max+1];
		
		for(int i =0; i < connPointTab.size(); i++)
		{
			if(connPointTab.get(i).P1==-10)
				continue;
			usedPoints[connPointTab.get(i).P1] = true;
		}
	}
	public void removePoint(int index)
	{

		for(int i = 0; i < connPointTab.size(); i++)
		{
			PolygonConn p = connPointTab.get(i);
			if(p.P1>=index)
			{
				if(p.P1==index)
				{
					connPointTab = new ArrayList<PolygonConn>();
					usedPointRemoved = true;
					p.P1--;
				}
				else
				p.P1--;
			}
			if(p.isArc)
			{
				if(p.P2>=index)
				{
					if(p.P2==index)
					{
						connPointTab = new ArrayList<PolygonConn>();
						usedPointRemoved = true;
						p.P2--;
					}
					else
					p.P2--;
				}
			}
			if(p.isDoubleArc)
			{
				if(p.P3>=index&&p.P3!=-10)
				{
					if(p.P3==index)
					{
						connPointTab = new ArrayList<PolygonConn>();
						usedPointRemoved = true;
						p.P3--;
					}
					else
					p.P3--;
				}
			}

		}

	}
	
	public boolean checkIfPointIsInBox(float x, float y)
	{
		if(x>=minMax[0]&&x<=minMax[2]&&y>=minMax[1]&&y<=minMax[3])
		{
			return true;
		}
		return false;
	}
	public boolean checkIfPointIsInBox(Point p)
	{
		if(p.x>=minMax[0]&&p.x<=minMax[2]&&p.y>=minMax[1]&&p.y<=minMax[3])
		{
			return true;
		}
		return false;
	}
	
	public boolean checkIfPointIsInBoxWithIndex(Point p, int index)
	{
		if(p.x>=minMax[0]&&p.x<=minMax[2]&&p.y>=minMax[1]&&p.y<=minMax[3])
		{
			addedPoints.add(index);
			return true;
		}
		return false;
	}
	public boolean isClicked(Frame f, float x, float y)
	{
		generateMinMax(f.gco().getPointTab());
		
		System.out.println("Genrownie minMax "+minMax[0]+" "+minMax[1]+" "+minMax[2]+" "+minMax[3]+" "+x+" "+y);
		float t[] = f.gco().scaleAndRotateValue(x, y);
		if(t[0]>=minMax[0]&&t[0]<=minMax[2]&&t[1]>=minMax[1]&&t[1]<=minMax[3])
		{
			BufferedImage memory_image = new BufferedImage(frameCupture.memory_frame.getWidth(), frameCupture.memory_frame.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = memory_image.createGraphics();
			g.setColor(Color.BLACK);
			drawPolygonForTestsBLACK(f, g);
			System.out.println(memory_image.getRGB((int)x, (int)y));
			if(memory_image.getRGB((int)x, (int)y) == Color.black.getRGB())
			{
			
			memory_image = null;
			System.out.println("Bounding Box Naruszony");
			return true;
			}
		}
		
		return false;
	}
	
	public ArrayList<Integer> getAllPointsOnThisImage(Frame f, BufferedImage memory_image)
	{
		ArrayList<Integer> pointsOnPolygon = new ArrayList<Integer>();
		for(int i = 0; i < addedPoints.size(); i++)
		{
			float[] tab = f.gco().getNormalPointTab(addedPoints.get(i));
			if(memory_image.getRGB((int)tab[0], (int)tab[1]) == Color.black.getRGB())
			{
				pointsOnPolygon.add(addedPoints.get(i));
			}
		}
		return pointsOnPolygon;
	}
	
	public void checkChoosenPointsIfAreOnImage(Frame f, BufferedImage memory_image)
	{
		generateBooleanTab();
		for(int i = 0; i < addedPoints.size(); i++)
		{
			if(addedPoints.get(i)<usedPoints.length)
			{
			if(usedPoints[addedPoints.get(i)])
			{
				continue;
			}
			}
			float[] tab = f.gco().getNormalPointTab(addedPoints.get(i));
			if(!(memory_image.getRGB((int)tab[0], (int)tab[1]) == Color.black.getRGB()||
					memory_image.getRGB((int)tab[0], (int)tab[1]+1) == Color.black.getRGB()||
					memory_image.getRGB((int)tab[0], (int)tab[1]-1) == Color.black.getRGB()||
					memory_image.getRGB((int)tab[0]+1, (int)tab[1]+1) == Color.black.getRGB()||
					memory_image.getRGB((int)tab[0]-1, (int)tab[1]-1) == Color.black.getRGB()||
					memory_image.getRGB((int)tab[0]+1, (int)tab[1]-1) == Color.black.getRGB()||
					memory_image.getRGB((int)tab[0]-1, (int)tab[1]+1) == Color.black.getRGB()||
					memory_image.getRGB((int)tab[0]+1, (int)tab[1]) == Color.black.getRGB()||
					memory_image.getRGB((int)tab[0]-1, (int)tab[1]) == Color.black.getRGB()))
			{
				addedPoints.remove(i);
				i--;
			}
		}
	}
	public FilledPolygon clone()
	{
		ArrayList<PolygonConn> pc = new ArrayList<PolygonConn>();
		ArrayList<Integer> in = new ArrayList<Integer>();
		for(int i: connectionIndexTab)
		{
			in.add(i);
		}
		for(PolygonConn i: connPointTab)
		{
			if(!i.isArc&&!i.isDoubleArc)
				pc.add(new PolygonConn(i.P1));
			else if(i.isArc&&!i.isDoubleArc)
				pc.add(new PolygonConn(i.P1, i.P2));
			else if(i.isArc&&i.isDoubleArc)
				pc.add(new PolygonConn(i.P1, i.P2, i.P3));
		}
		FilledPolygon f = new FilledPolygon(pc, in);
		f.c = c;
		return f;
	}
	
	public void movePolygon(ArrayList<Point> pointTab, float x, float y)
	{
		boolean usedP[] = new boolean[pointTab.size()];
		for(int i = 0; i < connPointTab.size(); i++)
		{
			if(connPointTab.get(i).P1==-10||usedP[connPointTab.get(i).P1])
				continue;
			
			usedP[connPointTab.get(i).P1] = true;
			pointTab.get(connPointTab.get(i).P1).x += x;
			pointTab.get(connPointTab.get(i).P1).y += y;
			if(connPointTab.get(i).isDoubleArc)
			{
			pointTab.get(connPointTab.get(i).P2).x += x;
			pointTab.get(connPointTab.get(i).P3).x += x;
			pointTab.get(connPointTab.get(i).P2).y += y;
			pointTab.get(connPointTab.get(i).P3).y += y;
			}
		}
		
	}
	
	public ArrayList<Integer> markPolygon(ArrayList<Point> pointTab, ArrayList<Connection> connTab)
	{
		ArrayList<Integer> tab = new ArrayList<Integer>();
		for(int i = 0; i < connPointTab.size(); i++)
		{
			if(connPointTab.get(i).P1==-10)
				continue;
			pointTab.get(connPointTab.get(i).P1).MarkedPoint = true;
			tab.add(connPointTab.get(i).P1);
			if(connPointTab.get(i).isDoubleArc)
			{
			pointTab.get(connPointTab.get(i).P2).MarkedPoint = true;
			pointTab.get(connPointTab.get(i).P3).MarkedPoint = true;
			tab.add(connPointTab.get(i).P2);
			tab.add(connPointTab.get(i).P3);
			}
		}
		for(int i = 0; i < connectionIndexTab.size(); i++)
		{
			connTab.get(connectionIndexTab.get(i)).setMarked(true);
			Connection c = connTab.get(connectionIndexTab.get(i));
			pointTab.get(c.getP1()).MarkedPoint = true;
			pointTab.get(c.getP2()).MarkedPoint = true;
			tab.add(c.getP1());
			tab.add(c.getP2());
			if(c.isDoubleArc())
			{
				pointTab.get(c.getP3()).MarkedPoint = true;
				pointTab.get(c.getP4()).MarkedPoint = true;
				tab.add(c.getP3());
				tab.add(c.getP4());
			}
		}
		return tab;
	}
	
	public void drawPolygon(Frame f, Graphics2D g)
	{
			f.gco().CalculateScale();
	
			if(c!=null&&connPointTab.size()>0)
			{
			ArrayList<PolygonConn> tab = connPointTab;

			Path2D p=new Path2D.Float();
			float P1[] = f.gco().getScaledPoint(tab.get(0).P1);
			float P2[];
			float P3[];
			p.moveTo(P1[0],P1[1]);
			for(int j = 0; j< tab.size()-1; j++)
			{
				if(tab.get(j).P1!=-10)
				{
					//System.out.println(tab[j].getPoint1());
				if(tab.get(j).isArc==false)
				{
				P1 = f.gco().getScaledPoint(tab.get(j).P1);
				p.lineTo(P1[0],P1[1]);
				}
				else if(tab.get(j).isArc==true)
				{
					if(tab.get(j).isDoubleArc==true)
					{
						P1 = f.gco().getScaledPoint(tab.get(j).P1);
						P2 = f.gco().getScaledPoint(tab.get(j).P2);
						P3 = f.gco().getScaledPoint(tab.get(j).P3);
						p.curveTo(P2[0],P2[1],P3[0],P3[1],P1[0],P1[1]);
					}
					else
					{
						P1 = f.gco().getScaledPoint(tab.get(j).P1);
						P2 = f.gco().getScaledPoint(tab.get(j).P2);
						p.quadTo(P2[0],P2[1], P1[0],P1[1]);
					}
				}
				}
				else
				{
					j++;
					P1 = f.gco().getScaledPoint(tab.get(j).P1);
					p.moveTo(P1[0],P1[1]);
					continue;
				}
			}

			
			g.setColor(c);
			g.fill(p);
			}
	}
	
	
	public void drawPolygonNoScale(Frame f, Graphics2D g)
	{
			f.gco().CalculateScale();
	
			if(c!=null&&connPointTab.size()>0)
			{
				ArrayList<PolygonConn> tab = connPointTab;

			Path2D p=new Path2D.Float();
			float P1[] = f.gco().getNormalPointTab(tab.get(0).P1);
			g.drawRect((int)P1[0]-1,(int)P1[1]-1, 1, 1);
			float P2[];
			float P3[];
			p.moveTo(P1[0],P1[1]);
			for(int j = 0; j< tab.size()-1; j++)
			{
				if(tab.get(j).P1!=-10)
				{
					//System.out.println(tab[j].getPoint1());
				if(tab.get(j).isArc==false)
				{
				P1 = f.gco().getNormalPointTab(tab.get(j).P1);
				p.lineTo(P1[0],P1[1]);
				}
				else if(tab.get(j).isArc==true)
				{
					if(tab.get(j).isDoubleArc==true)
					{
						P1 = f.gco().getNormalPointTab(tab.get(j).P1);
						P2 = f.gco().getNormalPointTab(tab.get(j).P2);
						P3 = f.gco().getNormalPointTab(tab.get(j).P3);
						p.curveTo(P2[0],P2[1],P3[0],P3[1],P1[0],P1[1]);
					}
					else
					{
						P1 = f.gco().getNormalPointTab(tab.get(j).P1);
						P2 = f.gco().getNormalPointTab(tab.get(j).P2);
						p.quadTo(P2[0],P2[1], P1[0],P1[1]);
					}
				}
				}
				else
				{
					j++;
					P1 = f.gco().getNormalPointTab(tab.get(j).P1);
					p.moveTo(P1[0],P1[1]);
					continue;
				}
			}

			
			g.setColor(Color.BLACK);
			g.fill(p);
			
			
			
			}
	}
	public Path2D getPonygonPath2D(Frame f)
	{
			f.gco().CalculateScale();
	
			if(c!=null&&connPointTab.size()>0)
			{
				ArrayList<PolygonConn> tab = connPointTab;

			Path2D p=new Path2D.Float();
			float P1[] = f.gco().getNormalPointTab(tab.get(0).P1);
			
			float P2[];
			float P3[];
			p.moveTo(P1[0],P1[1]);
			for(int j = 0; j< tab.size()-1; j++)
			{
				if(tab.get(j).P1!=-10)
				{
					//System.out.println(tab[j].getPoint1());
				if(tab.get(j).isArc==false)
				{
				P1 = f.gco().getNormalPointTab(tab.get(j).P1);
				p.lineTo(P1[0],P1[1]);
				}
				else if(tab.get(j).isArc==true)
				{
					if(tab.get(j).isDoubleArc==true)
					{
						P1 = f.gco().getNormalPointTab(tab.get(j).P1);
						P2 = f.gco().getNormalPointTab(tab.get(j).P2);
						P3 = f.gco().getNormalPointTab(tab.get(j).P3);
						p.curveTo(P2[0],P2[1],P3[0],P3[1],P1[0],P1[1]);
					}
					else
					{
						P1 = f.gco().getNormalPointTab(tab.get(j).P1);
						P2 = f.gco().getNormalPointTab(tab.get(j).P2);
						p.quadTo(P2[0],P2[1], P1[0],P1[1]);
					}
				}
				}
				else
				{
					j++;
					P1 = f.gco().getNormalPointTab(tab.get(j).P1);
					p.moveTo(P1[0],P1[1]);
					continue;
				}
			}

			
			return p;
			}
		return null;
	}
	public void drawPolygonForTestsBLACK(Frame f, Graphics2D g)
	{
			f.gco().CalculateScale();
	
			if(c!=null&&connPointTab.size()>0)
			{
			ArrayList<PolygonConn> tab = connPointTab;

			Path2D p=new Path2D.Float();
			float P1[] = f.gco().getScaledPoint(tab.get(0).P1);
			float P2[];
			float P3[];

			p.moveTo(P1[0],P1[1]);
			for(int j = 0; j< tab.size()-1; j++)
			{
				if(tab.get(j).P1!=-10)
				{
					//System.out.println(tab[j].getPoint1());
				if(tab.get(j).isArc==false)
				{
				P1 = f.gco().getScaledPoint(tab.get(j).P1);
				p.lineTo(P1[0],P1[1]);
				}
				else if(tab.get(j).isArc==true)
				{
					if(tab.get(j).isDoubleArc==true)
					{
						P1 = f.gco().getScaledPoint(tab.get(j).P1);
						P2 = f.gco().getScaledPoint(tab.get(j).P2);
						P3 = f.gco().getScaledPoint(tab.get(j).P3);
						p.curveTo(P2[0],P2[1],P3[0],P3[1],P1[0],P1[1]);
					}
					else
					{
						P1 = f.gco().getScaledPoint(tab.get(j).P1);
						P2 = f.gco().getScaledPoint(tab.get(j).P2);
						p.quadTo(P2[0],P2[1], P1[0],P1[1]);
					}
				}
				}
				else
				{
					j++;
					P1 = f.gco().getScaledPoint(tab.get(j).P1);
					p.moveTo(P1[0],P1[1]);
					continue;
				}
			}

			
			g.setColor(Color.BLACK);
			g.fill(p);
			}
	}
	
	
	
	public ArrayList<PolygonConn> getPolygonConn()
	{
		return connPointTab;
	}
	
}
