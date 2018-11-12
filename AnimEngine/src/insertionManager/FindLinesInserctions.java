package insertionManager;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import polygonEngine.FillManager;
import renderSource.Connection;
import renderSource.Point;
import structures.Frame;
import supportingStructures.ListOfPointsToAdd;
import supportingStructures.PointWithIndex;
import toolBox.QuickSort;
import toolBox.TextBox;
import toolBox.frameCupture;
import toolBox.tb;

public class FindLinesInserctions 
{
	private int crossCounter = 0;
	private static float r = 0.05f;
	
	public void FindInserctionSimpleCall(Frame f)
	{
		FindInserctiones(f, f.gco().getPointTab(), f.gco().getConnectionTab(), f.gco().getScaleX(), f.gco().getScaleY(), f.gco().getX(), f.gco().getY());
//		f.gco().getFm().reGenerateAllMapNoDrawing(f, f.gco().getConnectionTab(), f.gco().getPointTab());
//		FillManager.needToRefresh = false;
	}
	public void FindInserctionSimpleCall(Frame f, float[] minMax)
	{
		//System.out.println("Specjalne przeciêcia "+minMax[0]+" "+minMax[1]+" "+minMax[2]+" "+minMax[3]);
		FindInserctionesWithBoundingBox(f, f.gco().getPointTab(), f.gco().getConnectionTab(), f.gco().getScaleX(), f.gco().getScaleY(), f.gco().getX(), f.gco().getY(), minMax);
//		f.gco().getFm().reGenerateAllMapNoDrawing(f, f.gco().getConnectionTab(), f.gco().getPointTab());
//		FillManager.needToRefresh = false;
	}
	public void FindInserctiones(Frame f,ArrayList<Point> points, ArrayList<Connection> conections, float scaleX, float scaleY, float X, float Y)
	{
		System.out.println("Odnajwywanie przeciêæ "+conections.size());
		crossCounter = 0;
		frameCupture.hasFrame = false;
		FillManager.needToRefresh = true;
		double time = System.nanoTime();
		ArrayList<PointWithIndex> sortedTabIndexX = new ArrayList<PointWithIndex>(conections.size());
		
		addConnXToList(points, conections, sortedTabIndexX);
		
		//drawListOfX(sortedTabIndexX);
		
		ListOfPointsToAdd listOfPoints = new ListOfPointsToAdd(conections.size());
		
		
		ArrayList<Integer> actuallyCheck = new ArrayList<Integer>();
		//ArrayList<Integer> potencialPoints = new ArrayList<Integer>();
		for(int i = 0; i < sortedTabIndexX.size(); i++)
		{
			boolean isOnList = false;
			
			isOnList = removeUsedLine(sortedTabIndexX, actuallyCheck, i, isOnList);
			
			if(!isOnList)
			{
				
				actuallyCheck.add(sortedTabIndexX.get(i).index);
				int indexOdAdded = actuallyCheck.size()-1;
				Connection c = conections.get(actuallyCheck.get(indexOdAdded));
				float[] cp1 = new float[]{f.gco().getPointX(c.getP1()), f.gco().getPointY(c.getP1()), f.gco().getPointX(c.getP2()), f.gco().getPointY(c.getP2())};
				float[] range = new float[4];
				findMaxMin(points, c, range);
				
				for(int j = 0; j < actuallyCheck.size()-1; j++)
				{
					Connection c2 = conections.get(actuallyCheck.get(j));
					checkLinesInsertion(f, points, listOfPoints, actuallyCheck, indexOdAdded, c, cp1, range, j, c2);
				}
				
			}
		}
		
//		System.out.print("Potencjalne punkty ");
//		for(int i:potencialPoints)
//		{
//			System.out.print(i+" ");
//		}
//		System.out.println();
		
		//drawAllCroosPoints(listOfPoints);
		ArrayList<Integer> listOfAddedConn = new ArrayList<Integer>();
		for(int i = 0; i < listOfPoints.tab.length; i++)
		{
			if(listOfPoints.tab[i].listOfPoints!=null)
			{
				if(listOfPoints.tab[i].listOfPoints.size()==1)
				{
				
					f.gco().addPoinToConnectionByIndex(i, listOfPoints.tab[i].listOfPoints.get(0), listOfAddedConn);
				}else{
				ArrayList<Integer> pointsToAdd = new ArrayList<Integer>();

				for(int j = 0; j < listOfPoints.tab[i].listOfPoints.size(); j++)
				{
					pointsToAdd.add(listOfPoints.tab[i].listOfPoints.get(j));
				}
				pointsToAdd.add(conections.get(listOfPoints.tab[i].indexOfConnection).getP1());
				pointsToAdd.add(conections.get(listOfPoints.tab[i].indexOfConnection).getP2());
				
				if(points.get(conections.get(listOfPoints.tab[i].indexOfConnection).getP1()).x!=points.get(conections.get(listOfPoints.tab[i].indexOfConnection).getP2()).x)
				{
					//Zwyk³a
					QuickSort.SortPointIndexes(pointsToAdd, points);

					f.gco().addPoinToConnectionByIndex(listOfPoints.tab[i].indexOfConnection, pointsToAdd, listOfAddedConn);
					
				}
				else
				{
					//Pionowa
					QuickSort.SortPointIndexesY(pointsToAdd, points);
//					System.out.println("Posortowne puntky pion");
//					for( int k:pointsToAdd)
//					{
//						System.out.print(k+" ");
//					}
//					System.out.println();
					f.gco().addPoinToConnectionByIndex(listOfPoints.tab[i].indexOfConnection, pointsToAdd, listOfAddedConn);
				}
				}
			}
		}
//		for(int i = 0; i < listOfAddedConn.size(); i++)
//		{
//			System.out.print(listOfAddedConn.get(i)+" ");
//		}
//		System.out.println();
		//TODO
		//f.gco().mergeLineWithLenght(listOfAddedConn, 0.5f);
		//System.out.println();
		//if(crossCounter>0)
		TextBox.addMessage(("Zakoñczono odnajdywanie przeciêæ "+conections.size()+" odcinków w "+((System.nanoTime()-time)/1000000)+"ms i odnaleziono "+crossCounter+" przeciêæ"), 500);
		//System.out.println("Zakoñczono Sortowanie "+conections.size()+" odcinków w "+((System.nanoTime()-time)/1000000)+"ms");
	}

	public void FindInserctionesWithBoundingBox(Frame f,ArrayList<Point> points, ArrayList<Connection> conections, float scaleX, float scaleY, float X, float Y, float[] maxMin)
	{
		System.out.println("Odnajwywanie przeciêæ "+conections.size());
		//System.out.println("ROZPOCZÊTO ODNAJDYWANIE PRZECIÊÆ "+f.gco().getConnectionTab().size());
		crossCounter = 0;
		frameCupture.hasFrame = false;
		FillManager.needToRefresh = true;
		double time = System.nanoTime();
		ArrayList<PointWithIndex> sortedTabIndexX = new ArrayList<PointWithIndex>(conections.size());
		
		addConnXToListWithBB(points, conections, sortedTabIndexX, maxMin);
		
		//drawListOfX(sortedTabIndexX);
		
		ListOfPointsToAdd listOfPoints = new ListOfPointsToAdd(conections.size());

		/*
		 * actualyCheck to aktualna lista int sprawdzanych odcinków
		 * pattern to pierwszy z wzorów odcinaka w wercji ABC
		 * pattern2 to wzór drugiego odcinka
		 * indexOfAdded to indeks ostatnio dodanego do sprawdzania odcinka
		 * range to MinMax aktualnie sprawdzanego nowo dodanego odcinka
		 * pod J przekazuje indeks drugiego odcinka w liscie actuallyChek która przechowuje ideksy
		 * listOfPoints to obiekt przechowuj¹cy punkty które s¹ posegregowane zgodnie z indeksami odcinków
		 * przy dodawaniu nowego punktu do nie utworzonej listy trzeba wywao³aæ funkcjê init pod danym indexem odcinka
		*/
		ArrayList<Integer> actuallyCheck = new ArrayList<Integer>();

		for(int i = 0; i < sortedTabIndexX.size(); i++)
		{
			boolean isOnList = false;
			
			isOnList = removeUsedLine(sortedTabIndexX, actuallyCheck, i, isOnList);
			
			if(!isOnList)
			{
				
				actuallyCheck.add(sortedTabIndexX.get(i).index);
				int indexOdAdded = actuallyCheck.size()-1;
				Connection c = conections.get(actuallyCheck.get(indexOdAdded));
				float[] cp1 = new float[]{f.gco().getPointX(c.getP1()), f.gco().getPointY(c.getP1()), f.gco().getPointX(c.getP2()), f.gco().getPointY(c.getP2())};
				float[] range = new float[4];
				findMaxMin(points, c, range);
				
				for(int j = 0; j < actuallyCheck.size()-1; j++)
				{
					Connection c2 = conections.get(actuallyCheck.get(j));
					checkLinesInsertion(f, points, listOfPoints, actuallyCheck, indexOdAdded, c, cp1, range, j, c2);
				}
				
			}
		}
		
//		System.out.print("Potencjalne punkty ");
//		for(int i:potencialPoints)
//		{
//			System.out.print(i+" ");
//		}
//		System.out.println();
		
		//drawAllCroosPoints(listOfPoints);
		//System.out.println("DODAWANIE EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
		ArrayList<Integer> listOfAddedConn = new ArrayList<Integer>();
//		for(int i = 0; i < listOfPoints.tab.length; i++)
//		{
//			if(listOfPoints.tab[i].listOfPoints!=null)
//			{
//				System.out.println(listOfPoints.tab[i].indexOfConnection);
//			}
//		}
		for(int i = 0; i < listOfPoints.tab.length; i++)
		{
			if(listOfPoints.tab[i].listOfPoints!=null)
			{
				if(listOfPoints.tab[i].listOfPoints.size()==1)
				{
//					System.out.println("11111111111111111111111111111111111111111111111111111111111111111111111111");
//					System.out.println(f.gco().getCon(i).getP1()+" "+listOfPoints.tab[i].listOfPoints.get(0)+" "+f.gco().getCon(i).getP2());
					f.gco().addPoinToConnectionByIndex(i, listOfPoints.tab[i].listOfPoints.get(0), listOfAddedConn);
				}else{
				ArrayList<Integer> pointsToAdd = new ArrayList<Integer>();
				for(int j = 0; j < listOfPoints.tab[i].listOfPoints.size(); j++)
				{
					pointsToAdd.add(listOfPoints.tab[i].listOfPoints.get(j));
				}

				
				pointsToAdd.add(conections.get(listOfPoints.tab[i].indexOfConnection).getP1());
				pointsToAdd.add(conections.get(listOfPoints.tab[i].indexOfConnection).getP2());
				
				if(points.get(conections.get(listOfPoints.tab[i].indexOfConnection).getP1()).x!=points.get(conections.get(listOfPoints.tab[i].indexOfConnection).getP2()).x)
				{
					//Zwyk³a
					QuickSort.SortPointIndexes(pointsToAdd, points);
//					System.out.println("222222222222222222222222222222222222222222222222222222222222222222222");
//					System.out.println("Dodawanie takiej listy punktów do odcinka "+listOfPoints.tab[i].indexOfConnection);
//					for(int k: pointsToAdd)
//					{
//						System.out.print(k+" ");
//					}
//					System.out.println();
					f.gco().addPoinToConnectionByIndex(listOfPoints.tab[i].indexOfConnection, pointsToAdd, listOfAddedConn);
					
				}
				else
				{
					//Pionowa
					QuickSort.SortPointIndexesY(pointsToAdd, points);
					f.gco().addPoinToConnectionByIndex(listOfPoints.tab[i].indexOfConnection, pointsToAdd, listOfAddedConn);
				}
				}
			}
		}
		
//		for(int i = 0; i < listOfAddedConn.size(); i++)
//		{
//			System.out.print(listOfAddedConn.get(i)+" ");
//		}
//		System.out.println();
//		
		//f.gco().mergeLineWithLenght(listOfAddedConn, 0.005f);
		//TODO
		//if(crossCounter>0)
		TextBox.addMessage(("Zakoñczono odnajdywanie przeciêæ "+sortedTabIndexX.size()/2+" odcinków w "+((System.nanoTime()-time)/1000000)+"ms i odnaleziono "+crossCounter+" przeciêæ"), 500);
		//System.out.println("Zakoñczono Sortowanie "+conections.size()+" odcinków w "+((System.nanoTime()-time)/1000000)+"ms");
		//System.out.println("ZAKOÑCZONO ODNAJDYWANIE PRZECIÊÆ "+f.gco().getConnectionTab().size());
	}
	private void checkLinesInsertion(Frame f, ArrayList<Point> points, ListOfPointsToAdd listOfPoints,
			ArrayList<Integer> actuallyCheck, int indexOdAdded, Connection c, float[] cp1, float[] range, int j,
			Connection c2) {
		float[] cp2 = new float[]{f.gco().getPointX(c2.getP1()), f.gco().getPointY(c2.getP1()), f.gco().getPointX(c2.getP2()), f.gco().getPointY(c2.getP2())};
		if((c2.isConnected(c.getP1())||c2.isConnected(c.getP2()))==false)
		{
			
		float[] range2 = new float[4];
		findMaxMin(points, c2, range2);	
		
		float[] XY = tb.linesInsertionSimple(cp1[0], cp1[1], cp1[2], cp1[3], cp2[0], cp2[1], cp2[2], cp2[3]);
		if(XY!=null)
		{
			
		boolean vh = false;	
		if(isHorizontal(range))
		{
			vh = true;
			XY[0] = range[0];
		}	
		if(isHorizontal(range2))
		{	
			vh = true;
			XY[0] = range2[0];
		}
		if(isVertical(range))
		{
			vh = true;
			XY[1] = range[1];
		}
		if(isVertical(range2))
		{
			vh = true;
			XY[1] = range2[1];
		}
		//TODO je¿eli tu jest isPointBetwenOrOn to powstaja b³êdne przeciêcia, a je¿eli nie ma to ni wykrywa pionowych i poziomych
		//nale¿y nadaæ tu jakieœ rozró¿nienia
		if(!vh)
		{
		if(isPointBetwen(range, XY[0], XY[1])&&isPointBetwen(range2, XY[0], XY[1]))
		{
			//System.out.println(XY[0]+" "+XY[1]);
			addPointToPointList(f, points, listOfPoints, actuallyCheck, j, indexOdAdded, XY);
			crossCounter++;
		}
		}
		else
		{
			if(isPointBetwenOrOn(range, XY[0], XY[1])&&isPointBetwenOrOn(range2, XY[0], XY[1]))
			{
				//System.out.println(XY[0]+" "+XY[1]);
				addPointToPointList(f, points, listOfPoints, actuallyCheck, j, indexOdAdded, XY);
				crossCounter++;
			}
		}
		}
		}
	}
	private void addConnXToList(ArrayList<Point> points, ArrayList<Connection> conections,
			ArrayList<PointWithIndex> sortedTabIndexX) {
		for(int i = 0; i < conections.size(); i++)
		{
			sortedTabIndexX.add(new PointWithIndex(points.get(conections.get(i).getP1()).x, i));
			sortedTabIndexX.add(new PointWithIndex(points.get(conections.get(i).getP2()).x, i));
		}
		QuickSort.SortPointWithIndex(sortedTabIndexX);
	}
//	if((c.getMinX()>c2.getMaxX()||c.getMaxX()<c2.getMinX()||c.getMinY()>c2.getMaxY()||c.getMaxY()<c2.getMinY())==false)
//	{
	private void addConnXToListWithBB(ArrayList<Point> points, ArrayList<Connection> conections,
			ArrayList<PointWithIndex> sortedTabIndexX, float[] minMax) {
		for(int i = 0; i < conections.size(); i++)
		{
			float minX = Math.min(points.get(conections.get(i).getP1()).x, points.get(conections.get(i).getP2()).x);
			float maxX = Math.max(points.get(conections.get(i).getP1()).x, points.get(conections.get(i).getP2()).x);
			float minY = Math.min(points.get(conections.get(i).getP1()).y, points.get(conections.get(i).getP2()).y);
			float maxY = Math.max(points.get(conections.get(i).getP1()).y, points.get(conections.get(i).getP2()).y);
			if(!(minMax[0]>=maxX+r||minMax[2]<=minX-r||minMax[1]>=maxY+r||minMax[3]<=minY-r))
			{
				sortedTabIndexX.add(new PointWithIndex(points.get(conections.get(i).getP1()).x, i));
				sortedTabIndexX.add(new PointWithIndex(points.get(conections.get(i).getP2()).x, i));
			}
		}
		QuickSort.SortPointWithIndex(sortedTabIndexX);
	}

	private void drawListOfX(ArrayList<PointWithIndex> sortedTabIndexX) {
		for(int i = 0; i < sortedTabIndexX.size(); i++)
		{
			System.out.print(sortedTabIndexX.get(i).index+" ");
		}
		System.out.println();
	}


	@SuppressWarnings("unused")
	private void drawAllCroosPoints(ListOfPointsToAdd listOfPoints) {
		for(int i = 0; i < listOfPoints.tab.length; i++)
		{
			if(listOfPoints.tab[i].listOfPoints!=null)
			{
				System.out.print("Odcinek "+listOfPoints.tab[i].indexOfConnection+" jest przeciêty punktami: ");
				for(int j = 0; j < listOfPoints.tab[i].listOfPoints.size(); j++)
				{
					System.out.print(listOfPoints.tab[i].listOfPoints.get(j)+" ");
				}
				System.out.println();
			}
		}
	}


	private boolean removeUsedLine(ArrayList<PointWithIndex> sortedTabIndexX, ArrayList<Integer> actuallyCheck, int i,
			boolean isOnList) {
		for(int j = 0; j < actuallyCheck.size(); j++)
		{
			if(actuallyCheck.get(j)==sortedTabIndexX.get(i).index)
			{
				actuallyCheck.remove(j);
				isOnList = true;
				break;
			}
		}
		return isOnList;
	}


	@SuppressWarnings("unused")
	private void noVerticalLines(Frame f, ArrayList<Point> points, float scaleX, float scaleY, float X, float Y,
			ListOfPointsToAdd listOfPoints, ArrayList<Integer> actuallyCheck, float[] pattern, float[] range, int j,
			Connection c1,Connection c2, float[] pattern2, int indexOdAdded, ArrayList<Integer> potencialPoints) {
		
		float[] range2 = new float[4];
		findMaxMin(points, c2, range2);
		float XY[] = tb.corossPointOfTwoLines(pattern, pattern2);
		
		if(XY!=null)
		{
			
				
		if(isPointBetwenOrOn(range, XY[0], XY[1])&&isPointBetwenOrOn(range2, XY[0], XY[1]))
		{
			addPointToPointList(f, points, listOfPoints, actuallyCheck, j, indexOdAdded, XY);
			crossCounter++;
			//System.out.println("Znaleziono przeciêcie pomiêdzy "+actuallyCheck.get(j)+" i "+actuallyCheck.get(indexOdAdded)+" w punkcie "+XY[0]+" "+XY[1]);
		}
//		else
//		{
//				checkIfPointsAreOnLine(points, listOfPoints, actuallyCheck, pattern, j, c1, c2, pattern2, indexOdAdded, potencialPoints);
//		}
		//TODO TU Dodaæ sprawdzanie najblirzszych lini punktów krañcowych
		}
	}

	@SuppressWarnings("unused")
	private void checkIfPointsAreOnLine(ArrayList<Point> points, ListOfPointsToAdd listOfPoints,
			ArrayList<Integer> actuallyCheck, float[] pattern, int j, Connection c1, Connection c2, float[] pattern2,
			int indexOdAdded, ArrayList<Integer> potencialPoints) {
		float RANGE = 2f;
		float d1 = tb.getDistToLine(pattern2[0], pattern2[1], pattern2[2], points.get(c1.getP1()).x, points.get(c1.getP1()).y);
		float d2 = tb.getDistToLine(pattern2[0], pattern2[1], pattern2[2], points.get(c1.getP2()).x, points.get(c1.getP2()).y);
		float d3 = tb.getDistToLine(pattern[0], pattern[1], pattern[2], points.get(c2.getP1()).x, points.get(c2.getP1()).y);
		float d4 = tb.getDistToLine(pattern[0], pattern[1], pattern[2], points.get(c2.getP2()).x, points.get(c2.getP2()).y);
		
		
		
		
		System.out.println("INDEXY PUNKTOW "+c1.getP1()+" "+c1.getP2()+" "+c2.getP1()+" "+c2.getP2());
		if(!c2.isConnected(c1.getP1())&&!c2.isConnected(c1.getP2())&&!c1.isConnected(c2.getP1())&&!c1.isConnected(c2.getP2()))
		{
			System.out.println("ODLEG£OŒCI "+d1+" "+d2+" "+d3+" "+d4);
		if(d1 < RANGE)
		{
			System.out.println("1");
			potencialPoints.add(c1.getP1());
		}
		else if(d2 < RANGE)
		{
			System.out.println("2");
			potencialPoints.add(c1.getP2());
		}
		else if(d3 < RANGE)
		{
			System.out.println("3");
			potencialPoints.add(c2.getP1());
		}
		else if(d4 < RANGE)
		{
			System.out.println("4");
			potencialPoints.add(c2.getP2());
		}
		}
	}


	public boolean isVertical(float[] range)
	{
		if(range[1]==range[3])
			return true;
		else
			return false;
	}
	public boolean isHorizontal(float[] range)
	{
		if(range[0]==range[2])
			return true;
		else
			return false;
	}

	@SuppressWarnings("unused")
	private void secondLineVertical(Frame f, ArrayList<Point> points, float scaleX, float scaleY, float X, float Y,
			ListOfPointsToAdd listOfPoints, ArrayList<Integer> actuallyCheck, float[] pattern, float[] range, int j,
			Connection c2, float[] pattern2, int indexOdAdded) {
		float[] range2 = new float[4];
		findMaxMin(points, c2, range2);
		float XY[] = tb.corossPointOfTwoLines(pattern, pattern2);
		if(XY!=null)
		{
			//System.out.println("Znaleziono Pionow¹ liniêêê LLLLLLLLLLLL" +XY[0]+" "+XY[1]);
		if(isPointBetwenOrOn(range, XY[0], XY[1])&&isVerticalLineOrOn(range2, XY[0], XY[1]))
		{
			addPointToPointList(f, points, listOfPoints, actuallyCheck, j, indexOdAdded, XY);
			crossCounter++;
			//System.out.println("Znaleziono przeciêcie pomiêdzy "+actuallyCheck.get(j)+" i "+actuallyCheck.get(indexOdAdded)+" w punkcie "+XY[0]+" "+XY[1]);
		}
		}
	}


	@SuppressWarnings("unused")
	private void firstLineVertical(Frame f, ArrayList<Point> points, float scaleX, float scaleY, float X, float Y,
			ListOfPointsToAdd listOfPoints, ArrayList<Integer> actuallyCheck, float[] pattern, float[] range, int j,
			Connection c2, float[] pattern2, int indexOdAdded) {
		float[] range2 = new float[4];
		findMaxMin(points, c2, range2);
		float XY[] = tb.corossPointOfTwoLines(pattern, pattern2);
		if(XY!=null)
		{
			//System.out.println("Znaleziono Pionow¹ liniêêê LLLLLLLLLLLL" +XY[0]+" "+XY[1]);
		if(isVerticalLineOrOn(range, XY[0], XY[1])&&isPointBetwenOrOn(range2, XY[0], XY[1]))
		{
			addPointToPointList(f, points, listOfPoints, actuallyCheck, j, indexOdAdded, XY);
			crossCounter++;
			//System.out.println("Znaleziono przeciêcie pomiêdzy "+actuallyCheck.get(j)+" i "+actuallyCheck.get(indexOdAdded)+" w punkcie "+XY[0]+" "+XY[1]);
		}
		}
	}

	private void addPointToPointList(Frame f, ArrayList<Point> points, ListOfPointsToAdd listOfPoints,
			ArrayList<Integer> actuallyCheck, int j, int indexOdAdded, float[] XY) {
		
		//TODO

		//PastBox.eventAddPoint(StateBox.frameIndex, StateBox.objIndex, points.size());
		f.gco().addPoint(new Point(XY[0], XY[1]));

		listOfPoints.tab[actuallyCheck.get(j)].indexOfConnection = actuallyCheck.get(j);
		
		if(listOfPoints.tab[actuallyCheck.get(j)].listOfPoints==null)
			listOfPoints.tab[actuallyCheck.get(j)].init();
		
		listOfPoints.tab[actuallyCheck.get(indexOdAdded)].indexOfConnection = actuallyCheck.get(indexOdAdded);
		
		if(listOfPoints.tab[actuallyCheck.get(indexOdAdded)].listOfPoints==null)
			listOfPoints.tab[actuallyCheck.get(indexOdAdded)].init();
		
		listOfPoints.tab[actuallyCheck.get(j)].listOfPoints.add(points.size()-1);
		listOfPoints.tab[actuallyCheck.get(indexOdAdded)].listOfPoints.add(points.size()-1);
	}
	//TODO
	@SuppressWarnings("unused")
	private void addPointToPointListFromPointsList(ArrayList<Point> points, ListOfPointsToAdd listOfPoints,
			ArrayList<Integer> actuallyCheck, int j, int indexOdAdded, int indexOdPoint) {
		//belongsToNew ustawia czy dany punkt nale¿y do nowego odcinka czy do starego
		//Ta czêœæ dodaje punkt do odcinka sprawdzanego
		
		listOfPoints.tab[actuallyCheck.get(j)].indexOfConnection = actuallyCheck.get(j);
		
		if(listOfPoints.tab[actuallyCheck.get(j)].listOfPoints==null)
			listOfPoints.tab[actuallyCheck.get(j)].init();
		
		listOfPoints.tab[actuallyCheck.get(indexOdAdded)].indexOfConnection = actuallyCheck.get(indexOdAdded);
		
		if(listOfPoints.tab[actuallyCheck.get(indexOdAdded)].listOfPoints==null)
			listOfPoints.tab[actuallyCheck.get(indexOdAdded)].init();
		
		listOfPoints.tab[actuallyCheck.get(j)].listOfPoints.add(indexOdPoint);
		listOfPoints.tab[actuallyCheck.get(indexOdAdded)].listOfPoints.add(indexOdPoint);
	}
	//Funkcja do dodawania punktó z listy do odcinków
	
	@SuppressWarnings("unused")
	private void addPointToPointListFromPointsList(ArrayList<Point> points, ListOfPointsToAdd listOfPoints,
			ArrayList<Integer> actuallyCheck, int j, int indexOdAdded, int indexOdPoint, boolean belongsToNew) {
		//belongsToNew ustawia czy dany punkt nale¿y do nowego odcinka czy do starego
		//Ta czêœæ dodaje punkt do odcinka sprawdzanego
		if(belongsToNew)
		{
		listOfPoints.tab[actuallyCheck.get(j)].indexOfConnection = actuallyCheck.get(j);
		
		if(listOfPoints.tab[actuallyCheck.get(j)].listOfPoints==null)
			listOfPoints.tab[actuallyCheck.get(j)].init();
		
		}
		else
		{
		//ta czêœæ dodaje punkt do nowo utworzonego
			listOfPoints.tab[actuallyCheck.get(indexOdAdded)].indexOfConnection = actuallyCheck.get(indexOdAdded);
			
			if(listOfPoints.tab[actuallyCheck.get(indexOdAdded)].listOfPoints==null)
				listOfPoints.tab[actuallyCheck.get(indexOdAdded)].init();
		}
		if(belongsToNew)
		{
			System.out.println("1 Dodawnie punktu "+indexOdPoint+" do odcinka "+actuallyCheck.get(j));
		listOfPoints.tab[actuallyCheck.get(j)].listOfPoints.add(indexOdPoint);
		}
		else
		{
			System.out.println("2 Dodawnie punktu "+indexOdPoint+" do odcinka "+indexOdAdded);
			listOfPoints.tab[actuallyCheck.get(indexOdAdded)].listOfPoints.add(indexOdPoint);
		}
	}
	public void FindInserctionesONLYVIZUALIZATION(ArrayList<Point> points, ArrayList<Connection> conections, Graphics2D g, float scaleX, float scaleY, float X, float Y)
	{
		g.setColor(Color.ORANGE);
		double time = System.nanoTime();
		ArrayList<PointWithIndex> sortedTabIndexX = new ArrayList<PointWithIndex>(conections.size());
		addConnXToList(points, conections, sortedTabIndexX);
		drawListOfX(sortedTabIndexX);
		
		
		ArrayList<Integer> actuallyCheck = new ArrayList<Integer>();
		for(int i = 0; i < sortedTabIndexX.size(); i++)
		{
			boolean isOnList = false;
			isOnList = removeUsedLine(sortedTabIndexX, actuallyCheck, i, isOnList);
			if(!isOnList)
			{
				
				actuallyCheck.add(sortedTabIndexX.get(i).index);
				int indexOdAdded = actuallyCheck.size()-1;
				Connection c = conections.get(actuallyCheck.get(indexOdAdded));
				float[] pattern = tb.generatePatternABC(points.get(c.getP1()).x, points.get(c.getP1()).y, points.get(c.getP2()).x, points.get(c.getP2()).y);
				//System.out.println("Wygenerowno Wzór");
				float[] range = new float[4];
				findMaxMin(points, c, range);
				
				for(int j = 0; j < actuallyCheck.size()-1; j++)
				{
					Connection c2 = conections.get(actuallyCheck.get(j));
					if((c2.isConnected(c.getP1())||c2.isConnected(c.getP2()))==false)
					{
					float[] pattern2 = tb.generatePatternABC(points.get(c2.getP1()).x, points.get(c2.getP1()).y, points.get(c2.getP2()).x, points.get(c2.getP2()).y);
				
					if((tb.isVertical(pattern)||tb.isVertical(pattern2))==false)
					{
						float[] range2 = new float[4];
						findMaxMin(points, c2, range2);
						float XY[] = tb.corossPointOfTwoLines(pattern, pattern2);
						if(XY!=null)
						{
						if(isPointBetwenOrOn(range, XY[0], XY[1])&&isPointBetwenOrOn(range2, XY[0], XY[1]))
						{
							
							g.fillOval((int)(XY[0]*scaleX+X)-5, (int)(XY[1]*scaleY+Y)-5, 10, 10);
							//System.out.println("Znaleziono przeciêcie pomiêdzy "+actuallyCheck.get(j)+" i "+actuallyCheck.get(indexOdAdded)+" w punkcie "+XY[0]+" "+XY[1]);
						}
						}
					}
					else if((!tb.isVertical(pattern)&&tb.isVertical(pattern2)))
					{
						float[] range2 = new float[4];
						findMaxMin(points, c2, range2);
						float XY[] = tb.corossPointOfTwoLines(pattern, pattern2);
						if(XY!=null)
						{
						if(isPointBetwenOrOn(range, XY[0], XY[1])&&isVerticalLineOrOn(range2, XY[0], XY[1]))
						{
						
							g.fillOval((int)(XY[0]*scaleX+X)-5, (int)(XY[1]*scaleY+Y)-5, 10, 10);
							//System.out.println("Znaleziono przeciêcie pomiêdzy "+actuallyCheck.get(j)+" i "+actuallyCheck.get(indexOdAdded)+" w punkcie "+XY[0]+" "+XY[1]);
						}
						}
					}
					else if((tb.isVertical(pattern)&&!tb.isVertical(pattern2)))
					{
						float[] range2 = new float[4];
						findMaxMin(points, c2, range2);
						float XY[] = tb.corossPointOfTwoLines(pattern, pattern2);
						if(XY!=null)
						{
						if(isVerticalLineOrOn(range, XY[0], XY[1])&&isPointBetwenOrOn(range2, XY[0], XY[1]))
						{
						
							g.fillOval((int)(XY[0]*scaleX+X)-5, (int)(XY[1]*scaleY+Y)-5, 10, 10);
							//System.out.println("Znaleziono przeciêcie pomiêdzy "+actuallyCheck.get(j)+" i "+actuallyCheck.get(indexOdAdded)+" w punkcie "+XY[0]+" "+XY[1]);
						}
						}
					}	
				}
						
				}
				
			}
//			System.out.println("Aktualnie sprawdzane");
//			for(int j = 0; j < actuallyCheck.size(); j++)
//			{
//				System.out.print(actuallyCheck.get(j)+" ");
//			}
//			System.out.println();
		}
		//System.out.println();
		System.out.println("Zakoñczono Sortowanie "+conections.size()+" odcinków w "+((System.nanoTime()-time)/1000000)+"ms");
		g.setColor(Color.BLACK);
	}

	private boolean isPointBetwen(float[] range, float x, float y)
	{
		if(x>range[0]&&x<range[2]&&y>range[1]&&y<range[3])
		{
			return true;
		}
		return false;
	}
	@SuppressWarnings("unused")
	private boolean isPointOn(float[] range, float x, float y)
	{
		if(x==range[0]||x==range[2]||y==range[1]||y==range[3])
		{
			float[] t = {12f,23f,34f,45f,3f};
			float[] t2 = new float[]{12f,23f,34f,45f,3f};
			return true;
			
		}
		return false;
	}
	//TODO
	private boolean isPointBetwenOrOn(float[] range, float x, float y)
	{
		if(x>=range[0]&&x<=range[2]&&y>=range[1]&&y<=range[3])
		{
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("unused")
	private boolean isPointBetwenOrOnR(float[] range, float x, float y, float r)
	{
		if(x>=range[0]-r&&x<=range[2]+r&&y>=range[1]-r&&y<=range[3]+r)
		{
			return true;
		}
		return false;
	}
	private boolean isVerticalLineOrOn(float[] range, float x, float y)
	{
		if(x==range[0]&&x==range[2]&&y>=range[1]&&y<=range[3])
		{
			return true;
		}
		return false;
	}

	@SuppressWarnings("unused")
	private boolean isVerticalLine(float[] range, float x, float y)
	{
		if(x==range[0]&&x==range[2]&&y>range[1]&&y<range[3])
		{
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("unused")
	private boolean isHorizontalLine(float[] range, float x, float y)
	{
		if(x>range[0]&&x<range[2]&&y==range[1]&&y==range[3])
		{
			return true;
		}
		return false;
	}
	private void findMaxMin(ArrayList<Point> points, Connection c, float[] range) {
		range[0] = Math.min(points.get(c.getP1()).x, points.get(c.getP2()).x);
		range[1] = Math.min(points.get(c.getP1()).y, points.get(c.getP2()).y);
		range[2] = Math.max(points.get(c.getP1()).x, points.get(c.getP2()).x);
		range[3] = Math.max(points.get(c.getP1()).y, points.get(c.getP2()).y);
	}
}
