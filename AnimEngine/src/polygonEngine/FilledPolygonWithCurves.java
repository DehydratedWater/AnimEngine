package polygonEngine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import polygonEngineStructures.shapeConnector;
import renderSource.Connection;
import renderSource.Point;
import structures.Frame;
import supportingStructures.TransformationBox;
import toolBox.frameCupture;
import toolBox.tb;

public class FilledPolygonWithCurves 
{
	public ArrayList<shapeConnector> shapeTab;
	public ArrayList<Integer> connectionIndexTab;
	public float[] minMax = null;
	public Color c;
	private boolean usedPointRemoved = false;
	public FilledPolygonWithCurves(ArrayList<shapeConnector> tab, ArrayList<Integer> connectionIndexTab) 
	{
		this.shapeTab = tab;
		this.connectionIndexTab = connectionIndexTab;
	}
	
	public FilledPolygonWithCurves(ArrayList<shapeConnector> tab, ArrayList<Integer> connectionIndexTab, Color col) 
	{
		this.shapeTab = tab;
		this.connectionIndexTab = connectionIndexTab;
		this.c = col;
	}
	
	
	public int isConnIncluded(int i)
	{
		for(int j : connectionIndexTab)
		{
			if(j == i)
				return j;
		}
		return -10;
	}
	public void addNewConnToList(int i)
	{
		connectionIndexTab.add(i);
	}
	
	public ArrayList<Integer> getIndexesOfConnsInsidePolygonToRemove(Frame f, FillMap fm)
	{
		//System.out.println("Rozpoczynanie szukania odcinków wewn¹trz polygona ");
		ArrayList<Integer> tabToRem = new ArrayList<Integer>();
		boolean usedConnsTab[] = new boolean[f.gco().getConnectionTabSize()];
		for(int i : connectionIndexTab)
		{
			usedConnsTab[i] = true;
		}
		boolean usedPointsTab[] = new boolean[f.gco().getPointTabSize()];
		for(int i : connectionIndexTab)
		{
			usedPointsTab[f.gco().getCon(i).getP1()] = true;
			usedPointsTab[f.gco().getCon(i).getP2()] = true;
		}
		
		for(int i = 0; i < f.gco().getConnectionTabSize(); i++)
		{
			if(usedPointsTab[f.gco().getCon(i).getP1()]&&usedPointsTab[f.gco().getCon(i).getP2()]&&!usedConnsTab[i])
			{
				tabToRem.add(i);
			}
		}
		float x, y;
		Connection C;
		int tab[];
		PolygonShapeGenerator psg = new PolygonShapeGenerator();
		for(int i = 0; i < tabToRem.size(); i++)
		{
			C = f.gco().getCon((int)tabToRem.get(i));
			//System.out.println("Odcinek "+C.getP1()+" "+C.getP2());
			x = (f.gco().getNormalPoint(C.getP2()).x + f.gco().getNormalPoint(C.getP1()).x)/2;
			y = (f.gco().getNormalPoint(C.getP2()).y + f.gco().getNormalPoint(C.getP1()).y)/2;
			tab = psg.getIndexOdStabAndIndexOfLines(fm.getSt(), x, y);
			if(tab==null)
			{
				//System.out.println("Usuwanie odcinka "+i);
				tabToRem.remove(i);
				i--;
				continue;
			}
			if(tab[1]%2==1)
			{
				//System.out.println("Usuwanie odcinka "+i);
				tabToRem.remove(i);
				i--;
			}
		}
		//System.out.println("Koñczenie szukania odcinków wewn¹trz polygona ");
		return tabToRem;
	}
	
	public void cutConnWithPoint(int connIndex, int pointIndex, int secondConnIndex)
	{
		//System.out.println("Ciêcie odcinków w polygonie odcinkiem "+connIndex+" STTTTTTTTTTTTTTTTTTTTTTAAAAAAAAAAAAAAAAARRRRRRRT");
		//writePolygon();
		//writePolygonConns();
		shapeConnector s1 = null;
		int shapeIndex = -10;
		for(int i = 0; i < shapeTab.size(); i++)
		{
			if(shapeTab.get(i).polygonEnd)
				continue;
			if(shapeTab.get(i).connIndex==connIndex)
			{
				//System.out.println("Znaleziono odcinek odpowiadaj¹cy poszukiwanemu "+connIndex);
				s1 = new shapeConnector(shapeTab.get(i));
				shapeTab.get(i).editReversedPoint(pointIndex);
			
				s1.editPoint(pointIndex);
				s1.connIndex = secondConnIndex;
				shapeIndex = i;
				break;
			}
		}
		
		if(s1==null)
		{
			System.out.println("ERRRRRR Brak szukanego odcinka");
			return;
		}
			
		//System.out.println("Dodawanie odcinka "+s1.getPoint());
		//System.out.println("ciêcie odcinka nr "+shapeIndex);
		shapeIndex+=1;
		shapeConnector tempShape = shapeTab.get(shapeIndex);
		//System.out.println("Rozpoczyanianie od odcinka "+tempShape.getPoint());
		shapeTab.set(shapeIndex, s1); //B£¥D W DODAWANIU PUNKTU I ICH PRZEK£ADANIU
		for(int i = (shapeIndex+1); i < shapeTab.size(); i++)
		{
			shapeConnector tempShape2 = shapeTab.get(i);
			shapeTab.set(i, tempShape);
			tempShape = tempShape2;
		}
		shapeTab.add(tempShape);
		connectionIndexTab.add(secondConnIndex);
		//writePolygon();
		//System.out.println("Zakoñczono ciêcie EEEEEEEEENNNNNNNNNNNNNNNNNNNNDDDDDDDDDDDDDDDDDDDDDDDDDD");
	}
	
	public void cutConnWithPointNEW(int connIndex, int pointIndex, int secondConnIndex, int indexOfPointInNonChangingConn)
	{
		//System.out.println("Ciêcie odcinków w polygonie odcinkiem "+connIndex+" STTTTTTTTTTTTTTTTTTTTTTAAAAAAAAAAAAAAAAARRRRRRRT");
		//writePolygon();
		//writePolygonConns();
		shapeConnector s1 = null;
		int shapeIndex = -10;
		for(int i = 0; i < shapeTab.size(); i++)
		{
			if(shapeTab.get(i).polygonEnd)
				continue;
			if(shapeTab.get(i).connIndex==connIndex)
			{
				//System.out.println("Znaleziono odcinek odpowiadaj¹cy poszukiwanemu "+connIndex);
				s1 = new shapeConnector(shapeTab.get(i));
				shapeIndex = i;
				break;
			}
		}
		
		if(s1==null)
		{
			System.out.println("ERRRRRR Brak szukanego odcinka");
			return;
		}
		ArrayList<shapeConnector> editedTab = new ArrayList<>(shapeTab.size()+1);
		for(int i = 0; i < shapeIndex; i++)
		{
			editedTab.add(shapeTab.get(i));
		}

		shapeConnector S1 = new shapeConnector(s1);
		shapeConnector S2 = new shapeConnector(s1);

		

		S1.editReversedPoint(pointIndex);
		S2.editPoint(pointIndex);
		
		if(s1.getPoint() == S2.getPoint())
		{
			S1.fromLeft = !S1.fromLeft;
			S2.fromLeft = !S2.fromLeft;
			editedTab.add(S2);
			editedTab.add(S1);
		}
		else
		{
			editedTab.add(S1);
			editedTab.add(S2);
		}
		
		if(S1.P1==indexOfPointInNonChangingConn&&S1.P2==pointIndex||S1.P2==indexOfPointInNonChangingConn&&S1.P1==pointIndex)
		{
			S1.connIndex = s1.connIndex;
			S2.connIndex = secondConnIndex;
		}
		else
		{
			S1.connIndex = secondConnIndex;
			S2.connIndex = s1.connIndex;
		}
		connectionIndexTab.add(secondConnIndex);
		//System.out.println("S1 "+S1.getSecondPoint()+" -> "+S1.getPoint()+"  S2 "+S2.getSecondPoint()+" -> "+S2.getPoint());

		for(int i = shapeIndex+1; i < shapeTab.size(); i++)
		{
			editedTab.add(shapeTab.get(i));
		}
		shapeTab = editedTab;
		//writePolygon();
		//writePolygonConns();
		//System.out.println("Zakoñczono ciêcie EEEEEEEEENNNNNNNNNNNNNNNNNNNNDDDDDDDDDDDDDDDDDDDDDDDDDD");
	}
	/**
	 * Tnie po³¹czenie tak zgodnie z dostarczonymi indeksami po³aczeñ stworzonymi na barzie punktow w pointTab
	 * @param connList -> lista Odcinków
	 * @param ConIndex -> index po³aczenia
	 * @param testPoint -> punkt pod indeksem 0 w pointTab który s³u¿y do przetestowania od której strony zaczyna siê po³aæzenia
	 * @param startPoint -> punkt pod indexem 1 w pointTab
	 * @param endPoint -> punkt pod indekxem pointTab.size()-1 w pointTab
	 * @param newConnTab -> lista indeksów odcinków które zosta³y utworzone podczas ciêcia
	 */
	public void cutPolygonWithPoints(ArrayList<Connection> connList, int ConIndex, int testPoint, int startPoint, int endPoint, ArrayList<Integer> newConnTab)
	{
		int connIndex = -10;
		for(int i = 0; i < shapeTab.size(); i++)
		{
			if(shapeTab.get(i).polygonEnd)
				continue;
			if(shapeTab.get(i).connIndex==ConIndex)
			{
				connIndex = i;
			}
		}
		if(connIndex==-10)
		return;
		
		if(shapeTab.get(connIndex).P1 == testPoint)
		{
			shapeTab.get(connIndex).P2 = startPoint;
		}
		boolean direction = shapeTab.get(connIndex).fromLeft;
		ArrayList<shapeConnector> cutedConns = new ArrayList<shapeConnector>(newConnTab.size()-1);
		for(int i : newConnTab)
		{
			cutedConns.add(new shapeConnector(connList.get(i), direction, i));
		}
		ArrayList<shapeConnector> temList = new ArrayList<shapeConnector>(shapeTab.size()-connIndex);
		
		for(int i = connIndex+1; i < shapeTab.size(); i++)
		{
			temList.add(shapeTab.get(i));
			shapeTab.remove(i);
			i--;
		}
		for(int i = 0; i < cutedConns.size(); i++)
		{
			shapeTab.add(cutedConns.get(i));
		}
		for(int i = 0; i < temList.size(); i++)
		{
			shapeTab.add(temList.get(i));
		}
		temList = null;
		for(int i : newConnTab)
			connectionIndexTab.add(i);
	}
	
	public void cutPolygonWithPointsNEW(ArrayList<Connection> connList, int ConIndex, int testPoint, int startPoint, int endPoint, ArrayList<Integer> newConnTab)
	{
		//System.out.println("Rozpoczêto ciêcie polygona wielokrotne "+ConIndex+" STARTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");
		int connIndex = -10;
		for(int i = 0; i < shapeTab.size(); i++)
		{
			if(shapeTab.get(i).polygonEnd)
				continue;
			if(shapeTab.get(i).connIndex==ConIndex)
			{
				connIndex = i;
				break;
			}
		}
		//System.out.println("Znaleziono odcinek "+ConIndex+" pod indeksem "+connIndex);
		//writePolygon();
		//writePolygonConns();
		
		if(connIndex==-10)
			return;
		
//		for(shapeConnector s: shapeTab)
//		{
//			System.out.print(s.getSecondPoint()+" -> "+s.getPoint()+"  \\  ");
//		}
//		System.out.println();
//		

		shapeConnector s1 = shapeTab.get(connIndex);
		int p1 = s1.getPoint();
		//int p2 = s1.getSecondPoint();
		
		boolean mainDirection = shapeTab.get(connIndex).fromLeft;

		ArrayList<shapeConnector> cutedConns = new ArrayList<shapeConnector>(newConnTab.size()-1);
		ArrayList<shapeConnector> temList = new ArrayList<shapeConnector>(shapeTab.size()-connIndex);
		
		
		
		cutedConns.add(new shapeConnector(connList.get(ConIndex), mainDirection, ConIndex));
		
		for(int i : newConnTab)
		{
			cutedConns.add(new shapeConnector(connList.get(i), mainDirection, i));
		}
		
		boolean reversed = false;
		boolean direction = false;
		
		
		
	//	System.out.println("P1 "+p1+"  P2 "+p2);
		//System.out.println("Cuttedconn PP1 "+cutedConns.get(0).getPoint()+"  PR1 "+cutedConns.get(0).getSecondPoint()+"  \\  PP2 "+cutedConns.get(cutedConns.size()-1).getPoint()+" RP2 "+cutedConns.get(cutedConns.size()-1).getSecondPoint());
		if(p1==cutedConns.get(0).getPoint())
		{
			//System.out.println("Wybrano wariant 1 ");
			reversed = false;
			direction = false;
		}
		else if(p1==cutedConns.get(0).getSecondPoint())
		{
			//System.out.println("Wybrano wariant 2");
			reversed = false;
			direction = true;
		}
		else if(p1==cutedConns.get(cutedConns.size()-1).getPoint())
		{
			//System.out.println("Wybrano wariant 3");
			reversed = true;
			direction = false;
		}
		else if(p1==cutedConns.get(cutedConns.size()-1).getSecondPoint())
		{
			//System.out.println("Wybrano wariant 4");
			reversed = true;
			direction = true;
		}
		else
		{
			System.out.println("ERRRRRRRRRRRRRRRRRRRRRRRRRRRRR");
		}
		
//		for(shapeConnector s: cutedConns)
//		{
//			System.out.print(s.getSecondPoint()+" -> "+s.getPoint()+"  \\  ");
//		}
//		System.out.println();
//		
//		for(shapeConnector s: cutedConns)
//		{
//			System.out.print(s.getPoint()+" ");
//		}
//		System.out.println();
//		for(shapeConnector s: cutedConns)
//		{
//			System.out.print(s.getSecondPoint()+" ");
//		}
//		System.out.println();
		
		for(int i = 0; i < connIndex; i++)
		{
			//System.out.println("Dodawanie1 punku "+shapeTab.get(i).getPoint());
			temList.add(shapeTab.get(i));
		}
		
		
		if(reversed&&direction)
		{
			//System.out.println("Odwrócone i skierowane");
			for(int i = 0; i < cutedConns.size(); i++)
			{
				
				cutedConns.get(cutedConns.size()-1-i).fromLeft = !mainDirection;
				temList.add(cutedConns.get(cutedConns.size()-1-i));
				//System.out.println("Dodawanie punku cietego "+cutedConns.get(cutedConns.size()-1-i).getPoint());
			}
		}
		else if(!reversed&&!direction)
		{
			//System.out.println("Nieodwrócone i nieskierowane");
			for(int i = 0; i < cutedConns.size(); i++)
			{
				cutedConns.get(i).fromLeft = mainDirection;
				temList.add(cutedConns.get(i));
				//System.out.println("Dodawanie punku cietego "+cutedConns.get(i).getPoint());
			}
		}
		else if(!reversed&&direction)
		{
			//System.out.println("Nieodwrócone i skierowane");
			for(int i = 0; i < cutedConns.size(); i++)
			{
				cutedConns.get(i).fromLeft = !mainDirection;
				temList.add(cutedConns.get(i));
				//System.out.println("Dodawanie punku cietego "+cutedConns.get(i).getPoint());
			}
		}
		else if(reversed&&!direction)
		{
			//System.out.println("Odwrócone i nieskierowane");
			for(int i = 0; i < cutedConns.size(); i++)
			{
				cutedConns.get(cutedConns.size()-1-i).fromLeft = mainDirection;
				temList.add(cutedConns.get(cutedConns.size()-1-i));
				//System.out.println("Dodawanie punku cietego "+cutedConns.get(cutedConns.size()-1-i).getPoint());
			}
		}
		
		
		for(int i = connIndex+1; i < shapeTab.size(); i++)
		{
			//System.out.println("Dodawanie2 punku "+shapeTab.get(i).getPoint());
			temList.add(shapeTab.get(i));
		}
		
		
		
		//System.out.println();
		for(int i : newConnTab)
			connectionIndexTab.add(i);
		shapeTab = temList;
		
//		for(shapeConnector s: shapeTab)
//		{
//			System.out.print(s.getSecondPoint()+" -> "+s.getPoint()+"  \\  ");
//		}
//		System.out.println();
		//writePolygon();
		//writePolygonConns();
		//System.out.println("Zakoñczono ciêcie polygona wielokrotne ENDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD");
	}
	
	
	public ArrayList<Integer> getShapeConnIndexes()
	{
		ArrayList<Integer> includedConns = new ArrayList<Integer>(shapeTab.size());
		
		for(shapeConnector s : shapeTab)
		{
			if(s.polygonEnd)
				continue;
			includedConns.add(s.connIndex);
		}
		
		return includedConns;
	}
	
	public void cutCurveWithPoint()
	{
		
	}
	public FilledPolygonWithCurves clone()
	{
		ArrayList<shapeConnector> pc = new ArrayList<shapeConnector>(shapeTab.size());
		ArrayList<Integer> connectionIndex = new ArrayList<Integer>(connectionIndexTab.size());
		for(shapeConnector i: shapeTab)
		{
			pc.add(new shapeConnector(i));
		}
		for(int i: connectionIndexTab)
		{
			connectionIndex.add(i);
		}
		FilledPolygonWithCurves f = new FilledPolygonWithCurves(pc, connectionIndex);
		f.c = c;
		return f;
	}
	public void writePolygon()
	{
		for(shapeConnector s : shapeTab)
		{
			if(!s.polygonEnd)
				System.out.print(s.getPoint()+" ");
			else
				System.out.print("-10 ");
		}
		System.out.println();
	}
	
	public void writePolygonConns()
	{
		for(shapeConnector s : shapeTab)
		{
			if(!s.polygonEnd)
				System.out.print(s.connIndex+" ");
			else
				System.out.print("-10 ");
		}
		System.out.println();
	}
	public void generateMinMax(ArrayList<Point> pTab)
	{
		
		minMax = new float[4];
		Point p1 = pTab.get(shapeTab.get(0).getPoint()), p4, p3;
		minMax[0] = p1.x;
		minMax[1] = p1.y;
		minMax[2] = p1.x;
		minMax[3] = p1.y;
		for(int i = 0; i < shapeTab.size(); i++)
		{
			if(shapeTab.get(i).polygonEnd)
				continue;
			p1 = pTab.get(shapeTab.get(i).getPoint());
			if(p1.x<minMax[0])
				minMax[0] = p1.x;
			else if(p1.x>minMax[2])
				minMax[2] = p1.x;
			if(p1.y<minMax[1])
				minMax[1] = p1.y;
			else if(p1.y>minMax[3])
				minMax[3] = p1.y;
			if(shapeTab.get(i).isDoubleArc)
			{
				p4 = pTab.get(shapeTab.get(i).P4);
				p3 = pTab.get(shapeTab.get(i).P3);
				if(p4.x<minMax[0])
					minMax[0] = p4.x;
				else if(p4.x>minMax[2])
					minMax[2] = p4.x;
				if(p4.y<minMax[1])
					minMax[1] = p4.y;
				else if(p4.y>minMax[3])
					minMax[3] = p4.y;
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
	
	public ArrayList<Integer> getPointsInPolygon(Frame f, boolean usedTab[])
	{
		generateMinMax(f.gco().getPointTab());
		ArrayList<Integer> pTab = new ArrayList<Integer>();
		float x, y;
		for(int i = 0; i < f.gco().getPointTabSize(); i++)
		{
			x = f.gco().getPointX(i);
			y = f.gco().getPointY(i);
			if(x>=minMax[0]&&x<=minMax[2]&&y>=minMax[1]&&y<=minMax[3])
			{
				if(usedTab[i]==false)
				pTab.add(i);
			}
		}
		System.out.println("Znaleziono punkty w BB "+pTab.size());
		BufferedImage memory_image = new BufferedImage(frameCupture.memory_frame.getWidth(), frameCupture.memory_frame.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = memory_image.createGraphics();
		drawPolygonForTestsBLACKNoScale(f, g);
		for(int i = 0; i < pTab.size(); i++)
		{
			x = f.gco().getPointX(pTab.get(i));
			y = f.gco().getPointY(pTab.get(i));
			if(memory_image.getRGB((int)x, (int)y) != Color.black.getRGB())
			{
			pTab.remove(i);
			i--;
			}
		}
		
		return pTab;
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
	
	private void drawPolygonForTestsBLACK(Frame f, Graphics2D g) 
	{
		if(c!=null&&shapeTab.size()>0&&isUsedPointRemoved()==false)
		{
		Point raundPointTab[] = f.gco().getRaundPointTab();
		TransformationBox t = f.gco().getActualTransformation();
		Path2D p=new Path2D.Float();
		p.moveTo((raundPointTab[shapeTab.get(0).getPoint()].x*t.scaleX +t.X),(raundPointTab[shapeTab.get(0).getPoint()].y*t.scaleY +t.Y));
		for(int j = 0; j< shapeTab.size()-1; j++)
		{
			if(!shapeTab.get(j).polygonEnd)
			{
				//System.out.println(tab[j].getPoint1());
			if(shapeTab.get(j).isArc==false)
			{
			p.lineTo((raundPointTab[shapeTab.get(j).getPoint()].x*t.scaleX +t.X), (raundPointTab[shapeTab.get(j).getPoint()].y*t.scaleY +t.Y));
			}
			else if(shapeTab.get(j).isArc==true)
			{
				int s[] = shapeTab.get(j).getPoints();
				if(shapeTab.get(j).isDoubleArc==true)
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
				p.moveTo((raundPointTab[shapeTab.get(j).getPoint()].x*t.scaleX +t.X), (raundPointTab[shapeTab.get(j).getPoint()].y*t.scaleY +t.Y));
				continue;
			}
		}

		
		g.setColor(Color.black);
		g.fill(p);
		}
		
	}
	
	private void drawPolygonForTestsBLACKNoScale(Frame f, Graphics2D g) 
	{
		if(c!=null&&shapeTab.size()>0&&isUsedPointRemoved()==false)
		{
		Point raundPointTab[] = f.gco().getRaundPointTab();
		Path2D p=new Path2D.Float();
		p.moveTo((raundPointTab[shapeTab.get(0).getPoint()].x),(raundPointTab[shapeTab.get(0).getPoint()].y));
		for(int j = 0; j< shapeTab.size()-1; j++)
		{
			if(!shapeTab.get(j).polygonEnd)
			{
				//System.out.println(tab[j].getPoint1());
			if(shapeTab.get(j).isArc==false)
			{
			p.lineTo((raundPointTab[shapeTab.get(j).getPoint()].x), (raundPointTab[shapeTab.get(j).getPoint()].y));
			}
			else if(shapeTab.get(j).isArc==true)
			{
				int s[] = shapeTab.get(j).getPoints();
				if(shapeTab.get(j).isDoubleArc==true)
				{
					p.curveTo((raundPointTab[s[2]].x), (raundPointTab[s[2]].y),
							(raundPointTab[s[1]].x), (raundPointTab[s[1]].y),
							(raundPointTab[s[0]].x), (raundPointTab[s[0]].y));
				}
				else
				{
					p.quadTo((raundPointTab[s[1]].x), (raundPointTab[s[1]].y), 
							(raundPointTab[s[0]].x), (raundPointTab[s[0]].y));
				}
			}
			}
			else
			{
				j++;
				p.moveTo((raundPointTab[shapeTab.get(j).getPoint()].x), (raundPointTab[shapeTab.get(j).getPoint()].y));
				continue;
			}
		}

		
		g.setColor(Color.black);
		g.fill(p);
		}
		
	}
	public void drawPolygon(Frame f, Graphics2D g) 
	{
		if(c!=null&&shapeTab.size()>0&&isUsedPointRemoved()==false)
		{
		Point raundPointTab[] = f.gco().getRaundPointTab();
		TransformationBox t = f.gco().getActualTransformation();
		Path2D p=new Path2D.Float();
		p.moveTo((raundPointTab[shapeTab.get(0).getPoint()].x*t.scaleX +t.X),(raundPointTab[shapeTab.get(0).getPoint()].y*t.scaleY +t.Y));
		for(int j = 0; j< shapeTab.size()-1; j++)
		{
			if(!shapeTab.get(j).polygonEnd)
			{
				//System.out.println(tab[j].getPoint1());
			if(shapeTab.get(j).isArc==false)
			{
			p.lineTo((raundPointTab[shapeTab.get(j).getPoint()].x*t.scaleX +t.X), (raundPointTab[shapeTab.get(j).getPoint()].y*t.scaleY +t.Y));
			}
			else if(shapeTab.get(j).isArc==true)
			{
				int s[] = shapeTab.get(j).getPoints();
				if(shapeTab.get(j).isDoubleArc==true)
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
				p.moveTo((raundPointTab[shapeTab.get(j).getPoint()].x*t.scaleX +t.X), (raundPointTab[shapeTab.get(j).getPoint()].y*t.scaleY +t.Y));
				continue;
			}
		}

		
		g.setColor(c);
		g.fill(p);
		}
		
	}
	
	public void mengePoints(int P1, int P2)
	{
		System.out.println("£¹czenie punktów "+P1+" "+P2);
		
		for(int i = 0; i < shapeTab.size(); i++)
		{
			shapeTab.get(i).drawConnector();
		}
		System.out.println();
		
		for(int i = 0; i < shapeTab.size()-1; i++)
		{
			if(shapeTab.get(i).polygonEnd)
				continue;
			
			if((shapeTab.get(i).getPoint() == P2&&shapeTab.get(i+1).getPoint() == P1))
			{
				System.out.println("1Usuwanie po³aczenia "+i+" "+shapeTab.get(i).getPoint()+" - "+shapeTab.get(i).getSecondPoint());
				
				shapeTab.remove(i);
				break;
			}
			
		}
	}
	public void removePoint(int index)
	{
		//System.out.println("Usuwanie punktu o indexie "+index+" "+shapeTab.size());
		for(int i = 0; i < shapeTab.size(); i++)
		{
			shapeConnector p = shapeTab.get(i);
			if(p.polygonEnd)
				continue;
			//System.out.println("CONN "+p.P1+" "+p.P2);
			if(p.P1>=index&&!p.polygonEnd)
			{
				if(p.P1==index)
				{
					//System.out.println("Usuwanie p1 "+p.P1);
					setUsedPointRemoved(true);
					break;
				}
				//System.out.println("Zmniejszanie p1 "+p.P1);
				p.P1--;
			}
			if(p.P2>=index&&!p.polygonEnd)
			{
				if(p.P2==index)
				{
					//System.out.println("Usuwanie p2 "+p.P2);
					setUsedPointRemoved(true);
					break;
				}
				//System.out.println("Zmniejszanie p2 "+p.P2);
				p.P2--;
			}
			if(p.isArc)
			{
				if(p.P3>=index&&!p.polygonEnd)
				{
					if(p.P3==index)
					{
						//System.out.println("Usuwanie "+p.P3);
						setUsedPointRemoved(true);
						break;
					}
					p.P3--;
				}
			}
			if(p.isDoubleArc)
			{
				if(p.P4>=index&&!p.polygonEnd)
				{
					if(p.P4==index)
					{
						//System.out.println("Usuwanie "+p.P4);
						setUsedPointRemoved(true);
						break;
					}
					p.P4--;
				}
			}

		}
		//System.out.println(usedPointRemoved);
	}
	public ArrayList<Integer> markPolygon(ArrayList<Point> pointTab, ArrayList<Connection> connTab)
	{
		ArrayList<Integer> tab = new ArrayList<Integer>();
		for(int i = 0; i < shapeTab.size(); i++)
		{
			if(shapeTab.get(i).polygonEnd)
				continue;
			pointTab.get(shapeTab.get(i).getPoint()).MarkedPoint = true;
			tab.add(shapeTab.get(i).P1);
			if(shapeTab.get(i).isDoubleArc)
			{
			pointTab.get(shapeTab.get(i).P2).MarkedPoint = true;
			pointTab.get(shapeTab.get(i).P3).MarkedPoint = true;
			tab.add(shapeTab.get(i).P2);
			tab.add(shapeTab.get(i).P3);
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
	public ArrayList<Integer> getPoints(ArrayList<Point> pointTab, ArrayList<Connection> connTab)
	{
		ArrayList<Integer> tab = new ArrayList<Integer>();
		boolean checkTab[] = new boolean[pointTab.size()];
		for(int i = 0; i < connectionIndexTab.size(); i++)
		{
			//connTab.get(connectionIndexTab.get(i)).setMarked(true);
			Connection c = connTab.get(connectionIndexTab.get(i));
			if(!checkTab[c.getP1()])
			{
				tab.add(c.getP1());
				checkTab[c.getP1()]=true;	
			}
			if(!checkTab[c.getP2()])
			{
				tab.add(c.getP2());
				checkTab[c.getP2()]=true;	
			}
			if(c.isDoubleArc())
			{
				if(!checkTab[c.getP3()])
				{
					tab.add(c.getP3());
					checkTab[c.getP3()]=true;	
				}
				if(!checkTab[c.getP4()])
				{
					tab.add(c.getP4());
					checkTab[c.getP4()]=true;	
				}
			}
		}
		return tab;
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
	public boolean isUsedPointRemoved() {
		return usedPointRemoved;
	}
	public void setUsedPointRemoved(boolean usedPointRemoved) {
		System.out.println("Usuwanie Polygona");
		this.usedPointRemoved = usedPointRemoved;
	}
	
	public boolean checkIfThisPolygonInsertsWithSecond(Frame f, FilledPolygonWithCurves fp)
	{
		generateMinMax(f.gco().getPointTab());
		return tb.isBBinsertion(minMax, fp.minMax);
	}
}
