package polygonEngine;

import java.util.ArrayList;

import polygonEngineStructures.ConnTabStructure;
import polygonEngineStructures.PolygonWithCurve;
import polygonEngineStructures.shapeConnector;
import polygonEngineStructures.specialPoint;
import renderSource.Connection;
import renderSource.Point;
import structures.Frame;

public class PolygonFillSercherNEW 
{
	private int lastPoint;
	private int PointCounter;
	
	public FilledPolygonWithCurves findFillInPolygon(Frame f, ArrayList<Point> points, ArrayList<Connection> conections, ArrayList<Integer> indexTab)
	{
		drawUsedPoints(indexTab);
		
		int[] countTab = new int[points.size()];
		ConnTabStructure[] connTab = new ConnTabStructure[points.size()];
		fillConnTab(conections, indexTab, countTab, connTab);
		

		drawTab(connTab);
		ArrayList<PolygonWithCurve> polygonTab = new ArrayList<PolygonWithCurve>();
		
		removeSinglePoints(countTab, connTab);
		System.out.println("Zakoñczono usuwanie");
		drawTab(connTab);
		checkForMultiplePoints(countTab, connTab, polygonTab);
		
		checkForHoles(countTab, connTab, polygonTab);
		
		RaundingPolygonTool.rotateInsidePolygons(f, polygonTab);
		drawPolygonTab(polygonTab);
		
		
		ArrayList<shapeConnector> connIdexTab = new ArrayList<shapeConnector>();
		generatePolygonShape(polygonTab, connIdexTab);
		
		for(shapeConnector s : connIdexTab)
		{
			s.drawPoint();
		}
		FilledPolygonWithCurves fp = new FilledPolygonWithCurves(connIdexTab, indexTab);
		//drawPolygonTabCurve(polygonTab);
		return fp;
	}

	private void generatePolygonShape(ArrayList<PolygonWithCurve> polygonTab, ArrayList<shapeConnector> connIdexTab) {
		for(PolygonWithCurve p:polygonTab)
		{
			for(shapeConnector i:p.polygonTab)
			{
				connIdexTab.add(i);
			}
			connIdexTab.add(new shapeConnector());
		}
	}

	private void drawUsedPoints(ArrayList<Integer> indexTab) {
		for(int i : indexTab)
			System.out.print(i+" ");
		System.out.println();
	}

	private void drawTab(ConnTabStructure[] connTab) {
		for(int i = 0; i < connTab.length; i++)
		{
			
			if(connTab[i]!=null)
			{
			System.out.print(i+": ");	
			connTab[i].drawStructure();
			}
		}
	}
	
	private void fillConnTab(ArrayList<Connection> conections, ArrayList<Integer> indexTab, int[] countTab,	ConnTabStructure[] connTab) {
		for(int i = 0; i < indexTab.size(); i++)
		{
			Connection c = conections.get(indexTab.get(i));
			countTab[c.getP1()]++;
			countTab[c.getP2()]++;
			if(connTab[c.getP1()]==null)
				connTab[c.getP1()] = new ConnTabStructure();
			if(connTab[c.getP2()]==null)
				connTab[c.getP2()] = new ConnTabStructure();
			connTab[c.getP1()].addConn(c, true, indexTab.get(i));
			connTab[c.getP2()].addConn(c, false, indexTab.get(i));
		}
	}
	private void checkForHoles(int[] countTab, ConnTabStructure[] connTab, ArrayList<PolygonWithCurve> polygonTab) {
		specialPoint sp = getNearestDoublePoint(countTab);
		while(sp.specialPoint)
		{
			ArrayList<shapeConnector> simplePolygon = getSimpleFigure(countTab, connTab, sp.index);
			polygonTab.add(new PolygonWithCurve(simplePolygon));
			sp = getNearestDoublePoint(countTab);
		}
	}
	private ArrayList<shapeConnector> getSimpleFigure(int[] countTab, ConnTabStructure[] connTab, int index) 
	{
		ArrayList<shapeConnector> visitedPoints = new ArrayList<shapeConnector>();
		
		int startPoint = index;
		//visitedPoints.add(connTab[startPoint].getShape(0));
		countTab[startPoint]--;
		int lastPoint = startPoint;
		int actualPoint = connTab[startPoint].getPoint(0);
		visitedPoints.add(connTab[startPoint].getShape(1));
		connTab[startPoint].remPoint(0);
		
		if(actualPoint==startPoint)
		{
			for(int i = 0; i < countTab[startPoint]; i++)
			{
				if(connTab[startPoint].getPoint(i)==lastPoint)
				{
					visitedPoints.add(connTab[startPoint].getShape(i));
					connTab[startPoint].remPoint(i);
					break;
				}
			}
				
				countTab[startPoint]--;
			//System.out.println("Powrócono do puntku wyjœcia na Starcie");
		}
		else
		{
		while(true)
		{
			if(actualPoint==startPoint)
			{
				
				for(int i = 0; i < countTab[startPoint]; i++)
				{
					if(connTab[startPoint].getPoint(i)==lastPoint)
					{
						connTab[startPoint].remPoint(i);
						break;
					}
				}
					
					countTab[startPoint]--;
				//System.out.println("Powrócono do puntku wyjœcia");
				break;
			}
			int p1 = connTab[actualPoint].getPoint(0);
			int p2 = connTab[actualPoint].getPoint(1);
			int nextPoint = 0;
			countTab[actualPoint]-=2;
			connTab[actualPoint].getPoint(0);
			connTab[actualPoint].getPoint(0);
			if(p1==lastPoint)
			{
				visitedPoints.add(connTab[actualPoint].getShape(0));
				nextPoint = p2;
			}
			else if(p2 == lastPoint)
			{
				visitedPoints.add(connTab[actualPoint].getShape(1));
				nextPoint = p1;
			}
			
			lastPoint = actualPoint;
			actualPoint = nextPoint;
			
			
		}
		}
		return visitedPoints;
	}

	private void drawPolygonTab(ArrayList<PolygonWithCurve> polygonTab) {
		for(PolygonWithCurve p:polygonTab)
		{
			for(shapeConnector i:p.polygonTab)
			{
				System.out.print(i.getPoint()+" ");
			}
			System.out.println();
		}
	}
	@SuppressWarnings("unused")
	private void drawPolygonTabConn(ArrayList<PolygonWithCurve> polygonTab) {
		for(PolygonWithCurve p:polygonTab)
		{
			for(shapeConnector i:p.polygonTab)
			{
				System.out.print(i.getPoint()+"-"+i.getSecondPoint()+" ");
			}
			System.out.println();
		}
	}
	@SuppressWarnings("unused")
	private void drawPolygonTabCurve(ArrayList<PolygonWithCurve> polygonTab) {
		for(PolygonWithCurve p:polygonTab)
		{
			for(shapeConnector i:p.polygonTab)
			{
				i.drawConnector();
			}
			System.out.println();
		}
	}
	private specialPoint getNearestDoublePoint(int[] countTab)
	{
		int index = -10;
		boolean isSpecialPoint = false;
		for(int i = 0; i < countTab.length; i++)
		{
			if(countTab[i]==2)
			{
				index = i;
				isSpecialPoint = true;
				break;
			}
		}
		return new specialPoint(index, isSpecialPoint);
	}
	private void removeSinglePoints(int[] countTab, ConnTabStructure[] connTab) {
		specialPoint sp = isSinglePoint(countTab);
		//System.out.println("Znaleniono1 "+sp.index+" "+sp.specialPoint);
		while(sp.specialPoint)
		{
			for(int i = sp.index; i < countTab.length; i++)
			{
				if(countTab[i]==1)
				{
					
					int pIndex = connTab[i].getPoint(0);
					//System.out.println("Znaleziono pojedyñczy punkt "+i+" po³¹czony z "+pIndex);
					countTab[i] = 0;
					connTab[i] = null;
					for(int j = 0; j < connTab[pIndex].size(); j++)
					{
						if(connTab[pIndex].getPoint(j)==i)
						{
							connTab[pIndex].remPoint(j);
							
							if(connTab[pIndex].size()==0)
							{
								countTab[pIndex] = 0;
								connTab[pIndex] = null;
							}
							else
							{
								countTab[pIndex]--;
							}
							break;
						}
						
					}
					if(countTab[pIndex]==1)
						i = pIndex;
				}
			}
			sp = isSinglePoint(countTab);
			if(!sp.specialPoint)
				break;
			//System.out.println("Znaleniono2 "+sp.index+" "+sp.specialPoint);
		}
	}
	
	private specialPoint isSinglePoint(int[] countTab) {
		boolean singlePoint = false;
		int index = -10;
		for(int i = 0; i < countTab.length; i++)
		{
			if(countTab[i]==1)
			{
				singlePoint = true;
				index = i;
				break;
			}
		}
		return new specialPoint(index, singlePoint);
	}
	
	private void checkForMultiplePoints(int[] countTab, ConnTabStructure[] connTab, ArrayList<PolygonWithCurve> polygonList) 
	{
		ArrayList<Integer> multiplePointsList = getListOfAllMltiplePoints(countTab);
		boolean check = true;
		while(check)
		{
		check = false;
		
		for(int i = 0; i < multiplePointsList.size(); i++)
		{
			for(int j = 0; j < countTab[multiplePointsList.get(i)]; j++)
			{
				int end = checkHowItsEnds(countTab, connTab, j, multiplePointsList.get(i));
				if(end==1)
				{
					ArrayList<shapeConnector> figure = getFigure(countTab, connTab, j, multiplePointsList.get(i));
					polygonList.add(new PolygonWithCurve(figure));
				}
			}
			//System.out.println("Sprawdznie punktów multi");
			
			removeSinglePoints(countTab, connTab);
		}
		//drawTab(countTab, connTab);
		check = isThereAnyMultiplePoint(countTab);
		if(PointCounter>100)
		{
			System.out.println("Awaryjne przerywanie");
			PointCounter = 0;
			break;
		}
		System.out.println("Czy s¹ jakieœ punkty wielokrotne "+check);
		}
	}
	
	private ArrayList<shapeConnector> getFigure(int[] countTab, ConnTabStructure[] connTab, int pointStart, int index) {
		ArrayList<shapeConnector> visitedPoints = new ArrayList<shapeConnector>();
		
		int startPoint = index;
		//visitedPoints.add(connTab[startPoint].getShape(0));
		countTab[startPoint]--;
		int lastPoint = startPoint;
		int actualPoint = connTab[startPoint].getPoint(pointStart);
		//visitedPoints.add(connTab[startPoint].getShape(pointStart));
		connTab[startPoint].remPoint(pointStart);
		//visitedPoints.add(connTab[actualPoint].getShape(0));
		if(actualPoint==startPoint)
		{
			for(int i = 0; i < countTab[startPoint]; i++)
			{
				if(connTab[startPoint].getPoint(i)==lastPoint)
				{
					visitedPoints.add(connTab[startPoint].getShape(i));
					connTab[startPoint].remPoint(i);
					break;
				}
			}
				
				countTab[startPoint]--;
			//System.out.println("Powrócono do puntku wyjœcia na Starcie");
		}
		else if(countTab[actualPoint]>2)
		{
			//System.out.println("Znaleziono inny du¿y punkt na Starcie");
			
		}
		else
		{
		while(true)
		{
			int p1 = connTab[actualPoint].getPoint(0);
			int p2 = connTab[actualPoint].getPoint(1);
			int nextPoint = 0;
			countTab[actualPoint]-=2;
			
			if(p1==lastPoint)
			{
				visitedPoints.add(connTab[actualPoint].getShape(0));
				nextPoint = p2;
			}
			else if(p2 == lastPoint)
			{
				visitedPoints.add(connTab[actualPoint].getShape(1));
				nextPoint = p1;
			}
			connTab[actualPoint].remPoint(0);
			connTab[actualPoint].remPoint(0);
			lastPoint = actualPoint;
			actualPoint = nextPoint;
			
			if(actualPoint==startPoint)
			{
				
				for(int i = 0; i < countTab[startPoint]; i++)
				{
					if(connTab[startPoint].getPoint(i)==lastPoint)
					{
						visitedPoints.add(connTab[actualPoint].getShape(i));
						connTab[startPoint].remPoint(i);
						break;
					}
				}
					
					countTab[startPoint]--;
				//System.out.println("Powrócono do puntku wyjœcia");
				break;
			}
			else if(countTab[actualPoint]>2)
			{
				//System.out.println("Znaleziono inny du¿y punkt");
				break;
			}
		}
		}
		return visitedPoints;
	}
	
	
	private boolean isThereAnyMultiplePoint(int[] countTab)
	{
		for(int i = 0; i < countTab.length; i++)
		{
			if(countTab[i]>2)
			{
				System.out.println("Znaleziono punkt wielkokrotny "+i);
				if(lastPoint == i)
				{
					PointCounter++;
				}
				else
				{
					lastPoint = i;
					PointCounter = 0;
				}
				return true;
			}
		}
		return false;
	}
	
	private ArrayList<Integer> getListOfAllMltiplePoints(int[] countTab)
	{
		ArrayList<Integer> tabOfPoints = new ArrayList<Integer>();
		for(int i = 0; i < countTab.length; i++)
		{
			if(countTab[i]>2)
			{
				tabOfPoints.add(i);
			}
		}
		return tabOfPoints;
	}
	
	
private int checkHowItsEnds(int[] countTab, ConnTabStructure[] connTab, int pointStart, int index) {
		
		int startPoint = index;
		int lastPoint = startPoint;
		int actualPoint = connTab[startPoint].getPoint(pointStart);
		if(actualPoint==startPoint)
		{
			//System.out.println("Powrócono do puntku wyjœcia na Starcie");
			return 1;
			
		}
		else if(countTab[actualPoint]>2)
		{
			//System.out.println("Znaleziono inny du¿y punkt na Starcie");
			return 2;
			
			
		}
		else
		{
		while(true)
		{
			int p1 = connTab[actualPoint].getPoint(0);
			int p2 = connTab[actualPoint].getPoint(1);
			int nextPoint = 0;

			if(p1==lastPoint)
			{
				nextPoint = p2;
			}
			else if(p2 == lastPoint)
			{
				nextPoint = p1;
			}
			
			lastPoint = actualPoint;
			actualPoint = nextPoint;
			if(actualPoint==startPoint)
			{
				//System.out.println("Powrócono do puntku wyjœcia");
				return 1;
			}
			else if(countTab[actualPoint]>2)
			{
				//System.out.println("Znaleziono inny du¿y punkt");
				return 2;
			}
			
		}
		}
		
	}
}
