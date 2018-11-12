package renderSource;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

import pastManager.PastBox;
import pastManager.StateBox;
import polygonEngine.FilledPolygonWithCurves;
import supportingStructures.TransformationBox;
import toolBox.QuickSort;
import toolBox.frameCupture;
import toolBox.tb;


public class Object 
{
private polygonEngine.FillMap fm = new polygonEngine.FillMap();
private ArrayList<Point> PointTab;
private ArrayList<Connection> ConnectionTab;
private ArrayList<FilledPolygonWithCurves> PolygonTab;
private ArrayList<Integer> polygonToDraw;
private float scaleX = 1, scaleY = 1;
private float SX = 1, SY = 1, WindowScale = 1;
private float align = 0;
private int sizeX;
private int sizeY;
private float MaxX, MaxY, MinX, MinY;
private float X , sX;
private float Y , sY;
private float RotateX;
private float RotateY;
private boolean rotate3D = false;
private Point RaundPointTab[];


public static boolean showPoints = false;
public static boolean RenderPolygonsWithAntiAliasing = true;
public static boolean RenderLinesWithAntiAliasing = true;
public static boolean drawWithOutMark = false;
public static boolean drawConnNumber = false;

private boolean isChanged = false;
/**
 * Create empty object and initialize FillMap, PointTab, ConnectinTab, ColorPointTab
 */
public Object() 
{
		PointTab = new ArrayList<Point>();
		ConnectionTab = new ArrayList<Connection>();
		PolygonTab = new ArrayList<FilledPolygonWithCurves>();
		polygonToDraw = new ArrayList<Integer>();
}

public Object(ArrayList<Point> points, ArrayList<Connection> conections, ArrayList<FilledPolygonWithCurves> filledPolygons) 
{
	if(points!=null)
	{
	PointTab = points;
	ConnectionTab = conections;
	PolygonTab = filledPolygons;
	polygonToDraw = new ArrayList<Integer>();
	FindImageCenter(PointTab);
	}
	else
	{
		PointTab = new ArrayList<Point>();
		ConnectionTab = new ArrayList<Connection>();
		PolygonTab = new ArrayList<FilledPolygonWithCurves>();
		polygonToDraw = new ArrayList<Integer>();
	}
}

public Object clone()
{
	Object o = new Object();
	if(PointTab.size()!=0)
	{
	ArrayList<Point> pt = new ArrayList<Point>();
	for(int i = 0; i < PointTab.size(); i++)
	{
		pt.add(new Point(PointTab.get(i)));
	}
	ArrayList<Connection> ct = new ArrayList<Connection>();
	for(int i = 0; i < ConnectionTab.size(); i++)
	{
		ct.add(new Connection(ConnectionTab.get(i)));
	}

	ArrayList<FilledPolygonWithCurves> fp = new ArrayList<FilledPolygonWithCurves>();
	for(int i = 0; i < PolygonTab.size(); i++)
	{
		fp.add(PolygonTab.get(i).clone());
	}
	
	o = new Object(pt, ct, fp);
	return o;
	}
	else
	{
		return new Object();
	}
}


public void renderObjectAsVector(Graphics2D g2)
{
	g2.setFont(new Font("Arial", 1, 15));
	CalculateScale();
	if(PointTab.size()!=0)
	{

	Point[] raundPointTab = genrateRotatedPointTab();
	ObjectRenderer.drawObjectBETA(g2, raundPointTab, PointTab, ConnectionTab, PolygonTab, polygonToDraw, new TransformationBox(X, Y, scaleX, scaleY, WindowScale));
	}

	//FindLinesInserctions fli = new FindLinesInserctions();
	//fli.FindInserctionesONLYVIZUALIZATION(PointTab, ConnectionTab, g2, scaleX, scaleY, X, Y);
}

public void mergeLineWithLenght(ArrayList<Integer> tabOfConn, float range)
{
	//TODO
	QuickSort.SortIntArray(tabOfConn);
	ArrayList<Integer> potencialPoints = new ArrayList<Integer>();
	for(int i = 0; i < tabOfConn.size(); i++)
	{
		int index = tabOfConn.get(tabOfConn.size()-1-i);
		Connection c = ConnectionTab.get(index);
		//System.out.println("index "+index+" "+tb.distanceF(PointTab.get(c.getP1()).x, PointTab.get(c.getP1()).y, PointTab.get(c.getP2()).x, PointTab.get(c.getP2()).y));
		if(tb.distanceF(PointTab.get(c.getP1()).x, PointTab.get(c.getP1()).y, PointTab.get(c.getP2()).x, PointTab.get(c.getP2()).y)<range)
		{
			//System.out.println("Usuwanie "+index);
			potencialPoints.add(c.getP1());
			potencialPoints.add(c.getP2());
			ConnectionTab.remove(index);
			mergePointsWithoutCheckingSinglePoints(c.getP1(), c.getP2());
			
		}
	}
	//TODO zamiast usuwaæ po³¹czenie trzeba zrobiæ wywo³anie merge points
	//removeSinglePoints();
}
public void mergePointsWithoutCheckingSinglePoints(int P1, int P2)
{
	if(P1!=P2)
	{
		mergePointsInPolygon(P1, P2);
		//System.out.println("Scalanie punktów "+P1+" "+P2);
	for(int i = 0; i < ConnectionTab.size(); i++)
	{
		if(ConnectionTab.get(i).isARC()==false)
		{
			if(ConnectionTab.get(i).getP1()==P1)
			{
				ConnectionTab.get(i).setP1(P2);
			}
			if(ConnectionTab.get(i).getP2()==P1)
			{
				ConnectionTab.get(i).setP2(P2);
			}
		}
		else
		{
			if(ConnectionTab.get(i).isDoubleArc())
			{
				if(ConnectionTab.get(i).getP1()==P1)
				{
					ConnectionTab.get(i).setP1(P2);
				}
				if(ConnectionTab.get(i).getP2()==P1)
				{
					ConnectionTab.get(i).setP2(P2);
				}
				if(ConnectionTab.get(i).getP3()==P1)
				{
					ConnectionTab.get(i).setP3(P2);
				}
				if(ConnectionTab.get(i).getP4()==P1)
				{
					ConnectionTab.get(i).setP4(P2);
				}
			}
			else
			{
				if(ConnectionTab.get(i).getP1()==P1)
				{
					ConnectionTab.get(i).setP1(P2);
				}
				if(ConnectionTab.get(i).getP2()==P1)
				{
					ConnectionTab.get(i).setP2(P2);
				}
				if(ConnectionTab.get(i).getP3()==P1)
				{
					ConnectionTab.get(i).setP3(P2);
				}
			}
		}
		
	}
	removePointWithoutGeneratingTab(P1);
	}
	generateRaundPointTab();
	checkLineParadox();

}
public void mergePoints(int P1, int P2)
{
	if(P1!=P2)
	{
		mergePointsInPolygon(P1, P2);
		//System.out.println("Scalanie punktów "+P1+" "+P2);
	for(int i = 0; i < ConnectionTab.size(); i++)
	{
		if(ConnectionTab.get(i).isARC()==false)
		{
			if(ConnectionTab.get(i).getP1()==P1)
			{
				ConnectionTab.get(i).setP1(P2);
			}
			if(ConnectionTab.get(i).getP2()==P1)
			{
				ConnectionTab.get(i).setP2(P2);
			}
		}
		else
		{
			if(ConnectionTab.get(i).isDoubleArc())
			{
				if(ConnectionTab.get(i).getP1()==P1)
				{
					ConnectionTab.get(i).setP1(P2);
				}
				if(ConnectionTab.get(i).getP2()==P1)
				{
					ConnectionTab.get(i).setP2(P2);
				}
				if(ConnectionTab.get(i).getP3()==P1)
				{
					ConnectionTab.get(i).setP3(P2);
				}
				if(ConnectionTab.get(i).getP4()==P1)
				{
					ConnectionTab.get(i).setP4(P2);
				}
			}
			else
			{
				if(ConnectionTab.get(i).getP1()==P1)
				{
					ConnectionTab.get(i).setP1(P2);
				}
				if(ConnectionTab.get(i).getP2()==P1)
				{
					ConnectionTab.get(i).setP2(P2);
				}
				if(ConnectionTab.get(i).getP3()==P1)
				{
					ConnectionTab.get(i).setP3(P2);
				}
			}
		}
		
	}
	removePoint(P1);
	}
	checkLineParadox();
}





public void reAddAllConnectionesConnectedToPoint(int pointIndex)
{
	ArrayList<Connection> tab = new ArrayList<Connection>();
	for(int i = 0; i < ConnectionTab.size(); i++)
	{
		if(ConnectionTab.get(i).isConnected(pointIndex))
		{
			tab.add(ConnectionTab.get(i));
			ConnectionTab.remove(i);
			i--;
		}
	}
	for(int i = 0; i < tab.size(); i++)
	{
		OLDaddConnectionOLD(tab.get(i));
	}
}


public void reAddConnectiones(ArrayList<Integer> intTab)
{
	System.out.println("Sprawdzanie przeciêæ "+intTab.size()+" odcinków");
	QuickSort.SortIntArray(intTab);
	//System.out.println("Posortowno");
	for(int i = 0; i < PointTab.size(); i++)
	{
		reAddAllConnectionesConnectedToPoint(i);
	}
	//System.out.println("Wytypowano");

	System.out.println("Zakoñczono sprawdznie przeicêæ");
}

public void reAddAllConnectionesConnectedToConnection(int pointIndexA,int pointIndexB)
{
	ArrayList<Connection> tab = new ArrayList<Connection>();
	for(int i = 0; i < ConnectionTab.size(); i++)
	{
		if(ConnectionTab.get(i).isConnected(pointIndexA)||ConnectionTab.get(i).isConnected(pointIndexB))
		{
			tab.add(ConnectionTab.get(i));
			ConnectionTab.remove(i);
			i--;
		}
	}
	for(int i = 0; i < tab.size(); i++)
	{
		OLDaddConnectionOLD(tab.get(i));
	}
}

public ArrayList<Integer> getListOfAllConectedPoints(int pointIndex)
{
	ArrayList<Integer> tab = new ArrayList<Integer>();
	for(int i = 0 ; i < ConnectionTab.size(); i++)
	{
		if(ConnectionTab.get(i).isConnected(pointIndex))
		{
			tab.add(i);
		}
	}
	return tab;
}

private Point[] genrateRotatedPointTab() {
	Point raundPointTab[] = new Point[PointTab.size()];
	for(int i = 0; i< PointTab.size(); i++)
	{
		float x = PointTab.get(i).x;
		float y = PointTab.get(i).y;
		float nx = 0;
		float ny = 0;
		nx = (float) ((x-RotateX)*Math.cos(Math.toRadians(align)) - (y-RotateY)*Math.sin(Math.toRadians(align)) + RotateX);
		ny = (float) ((x-RotateX)*Math.sin(Math.toRadians(align)) + (y-RotateY)*Math.cos(Math.toRadians(align)) + RotateY);
		raundPointTab[i] = new Point(nx , ny);
	}
	RaundPointTab = raundPointTab;
	return raundPointTab;
}

//TODO
public void CalculateScale()
{
	scaleX = SX * WindowScale;
	scaleY = SY * WindowScale;
	X = sX;
	Y = sY;
}

public void generateRaundPointTab()
{
	genrateRotatedPointTab();
}


public void FindImageCenter(ArrayList<Point> tab)
{
	float x = tab.get(0).x;
	float y = tab.get(0).y;
	float mx = tab.get(0).x;
	float my = tab.get(0).y;
	for(int i = 0; i < tab.size(); i++)
	{
		if(tab.get(i).x < x)
		{
			x = tab.get(i).x;
		}
		if(tab.get(i).y < y)
		{
			y = tab.get(i).y;
		}
		if(tab.get(i).x > mx)
		{
			mx = tab.get(i).x;
		}
		if(tab.get(i).y > my)
		{
			my = tab.get(i).y;
		}
	}
	MinX = x;
	MinY = y;
	MaxX = mx;
	MaxY = my;
	setRotateX((x+mx)/2);
	setRotateY((y+my)/2);
}

public void FindMax(Point[] tab)
{
	float x = tab[0].x;
	float y = tab[0].y;
	float mx = tab[0].x;
	float my = tab[0].y;
	for(int i = 0; i < tab.length; i++)
	{
		if(tab[i].x < x)
		{
			x = tab[i].x;
		}
		if(tab[i].y < y)
		{
			y = tab[i].y;
		}
		if(tab[i].x > mx)
		{
			mx = tab[i].x;
		}
		if(tab[i].y > my)
		{
			my = tab[i].y;
		}
	}
	MinX = x;
	MinY = y;
	MaxX = mx;
	MaxY = my;
}



/**
 * Informuje czy klikniêto na obiekt
 * @param Mx
 * @param My
 * @return prawda/fa³sz
 */
public boolean isClicked(int Mx, int My)
{
	Mx = (int)  (Mx/scaleX-X/scaleX);
	My = (int) (My/scaleY-Y/scaleY);
	if(Mx>=MinX-3&&Mx<=MaxX+3&&My>=MinY-3&&My<=MaxY+3)
	{
		return true;
	}
	return false;
}




/**
 * Obraca punkt zgodnie z obrotem objektu
 * @param x
 * @param y
 * @return obrócony punkt zgodnie ze skal¹
 */
public float[] RotatePoint(float x, float y)
{
	float nx = (float) ((x-RotateX)*Math.cos(Math.toRadians(-align)) - (y-RotateY)*Math.sin(Math.toRadians(-align)) + RotateX);
	float ny = (float) ((x-RotateX)*Math.sin(Math.toRadians(-align)) + (y-RotateY)*Math.cos(Math.toRadians(-align)) + RotateY);
	float[] t = {nx, ny};
	return t;
}


/**
 * Ekran --> Objekt
 * @param x
 * @param y
 * @return przeskalowany i obrócony punkt z Ekranu na skale objektu
 */
public float[] scaleAndRotateValue(float x, float y)
{
	return RotatePoint((x/WindowScale-X/WindowScale), (y/WindowScale-Y/WindowScale));
}


/**
 * Ekran --> Objekt
 * @param x
 * @param y
 * @return przeskalowany i obrócony punkt z Ekranu na skale objektu
 */
public float[] simplyScaleValue(float x, float y)
{
	return RotatePoint(x/WindowScale, y/WindowScale);
}
//TODO
/**
 * Objekt --> Ekran
 * @param x
 * @param y
 * @return przeskalowany i obrócony punkt z objektu na skale ekranu
 */
public float[] getScaledPoint(int i)
{
	float[] t = new float[2];
	float nx = (float) ((PointTab.get(i).x-RotateX)*Math.cos(Math.toRadians(align)) - (PointTab.get(i).y-RotateY)*Math.sin(Math.toRadians(align)) + RotateX);
	float ny = (float) ((PointTab.get(i).x-RotateX)*Math.sin(Math.toRadians(align)) + (PointTab.get(i).y-RotateY)*Math.cos(Math.toRadians(align)) + RotateY);
	t[0] = nx*scaleX+X;
	t[1] = ny*scaleY+Y;
	return t;
}
/**
 * Objekt --> Ekran
 * @param x
 * @param y
 * @return przeskalowany punkt z objektu na skale ekranu
 * Jedynie do podg¹du dzia³ania funkcji
 */
public float[] scaleValue(float x, float y)
{
	float[] t = new float[2];
	t[0] = x*scaleX+X;
	t[1] = y*scaleY+Y;
	return t;
}
// Zwraca najproœciej punkt
public Point getNormalPoint(int i)
{
	return PointTab.get(i);
}

public float getPointX(int i)
{
	return PointTab.get(i).x;
}

public float getPointY(int i)
{
	return PointTab.get(i).y;
}
public float[] getNormalPointTab(int i)
{
	return new float[]{PointTab.get(i).x, PointTab.get(i).y};
}
public void removePointMark()
{
	for(int i = 0; i < PointTab.size(); i ++)
	{
		if(PointTab.get(i).MarkedPoint)
		PointTab.get(i).MarkedPoint = false;
	}
}

public void removeConnectionMark()
{
	for(int i = 0; i < ConnectionTab.size(); i ++)
	{
		if(ConnectionTab.get(i).isMarked())
			ConnectionTab.get(i).setMarked(false);
	}
}




public void moveConnection(int ConIndex, float x, float y,float sx, float sy,float sx2, float sy2,float sx3, float sy3,float sx4, float sy4)
{
	if(ConnectionTab.get(ConIndex).isARC()==false)
	{
		int P1 = ConnectionTab.get(ConIndex).getP1();
		int P2 = ConnectionTab.get(ConIndex).getP2();
		PointTab.get(P1).x=x+sx;
		PointTab.get(P1).y=y+sy;
		PointTab.get(P2).x=x+sx2;
		PointTab.get(P2).y=y+sy2;
	}
	else
	{
		if(ConnectionTab.get(ConIndex).isDoubleArc())
		{
			int P1 = ConnectionTab.get(ConIndex).getP1();
			int P2 = ConnectionTab.get(ConIndex).getP2();
			int P3 = ConnectionTab.get(ConIndex).getP3();
			int P4 = ConnectionTab.get(ConIndex).getP4();
			PointTab.get(P1).x=x+sx;
			PointTab.get(P1).y=y+sy;
			PointTab.get(P2).x=x+sx2;
			PointTab.get(P2).y=y+sy2;
			PointTab.get(P3).x=x+sx3;
			PointTab.get(P3).y=y+sy3;
			PointTab.get(P4).x=x+sx4;
			PointTab.get(P4).y=y+sy4;
		}
		else
		{
			int P1 = ConnectionTab.get(ConIndex).getP1();
			int P2 = ConnectionTab.get(ConIndex).getP2();
			int P3 = ConnectionTab.get(ConIndex).getP3();
			PointTab.get(P1).x=x+sx;
			PointTab.get(P1).y=y+sy;
			PointTab.get(P2).x=x+sx2;
			PointTab.get(P2).y=y+sy2;
			PointTab.get(P3).x=x+sx3;
			PointTab.get(P3).y=y+sy3;
		}
	}
}



public static float[] generateLine(float x1, float y1, float x2, float y2)
{
	float tab[] = new float[2];
	tab[0] = (y1-y2)/(x1-x2);
	tab[1] = y1 - x1*tab[0];
	return tab;
}
public static float countY(float[] num, float x)
{
	return num[0]*x+num[1];
}
public static float countX(float[] num, float y)
{
	return (y-num[1])/num[0];
}
public int getNearestPointReversed(float x, float y)
{
	//System.out.println("Szukanie najbli¿szego punktu "+x+" "+y);
	if(PointTab.size()!=0)
	{
	x =  (x/scaleX-X/scaleX);
	y =  (y/scaleY-Y/scaleY);
	int r = 0;


	float dist = (float) tb.distance(x, y, RaundPointTab[0].x, RaundPointTab[0].y);
	//System.out.println("Odleg³oœæ "+dist);
	for (int j = 0; j < PointTab.size(); j++) 
	{
		float dist2 = (float) tb.distance(x, y, RaundPointTab[j].x, RaundPointTab[j].y);
		if(dist2<dist)
		{
			dist = dist2;
			r = j;
		}
		//System.out.println("Odleg³oœæ  "+j+" "+dist+" "+dist2);
	}
	float d = 5*(1/scaleX);
	
	//System.out.println("D: ="+d+" "+WindowScale);
	if(dist > d)	{
		//System.out.println(dist);
		r = -10;
	}
	//System.out.println("nabli¿szy punkt to: "+r+ " "+dist);
	return r;
	}
	else
	{
		return -10;
	}
}
//Mierzy odleg³oœæ sklauj¹c punkty z listy na ekran
public int getNearestPoint(float x, float y)//22000
{
	//double time = System.nanoTime();
	//System.out.println("Szukanie najbli¿szego punktu "+x+" "+y);
	if(PointTab.size()!=0)
	{
	int r = 0;

	float t[] = getScaledPoint(0);
	float dist = (float) tb.distance(x, y, t[0], t[1]);
	//System.out.println("Odleg³oœæ "+dist);
	for (int j = 0; j < PointTab.size(); j++) 
	{
		t = getScaledPoint(j);
		float dist2 = (float) tb.distance(x, y, t[0], t[1]);
		if(dist2<dist)
		{
			dist = dist2;
			r = j;
		}
		//System.out.println("Odleg³oœæ  "+j+" "+dist+" "+dist2);
	}
	float d = 15;
	//System.out.println("Odleg³oœæ od punktu to "+dist+" linit to: "+d);
	//System.out.println("D: ="+d+" "+WindowScale);
	if(dist > d)	{
		//System.out.println(dist);
		r = -10;
	}
	//System.out.println("nabli¿szy punkt w skali to: "+r+ " "+dist);
	//System.out.println(System.nanoTime()-time);
	return r;
	}
	else
	{
		return -10;
	}
}

public ArrayList<Integer> getNearestPointList(float x, float y, float range)
{

	if(PointTab.size()!=0)
	{
	ArrayList<Integer> pointTab = new ArrayList<Integer>();

	float dist = range;
	for (int j = 0; j < PointTab.size(); j++) 
	{
		float[] t = getScaledPoint(j);
		float dist2 = (float) tb.distance(x, y, t[0], t[1]);
		if(dist2<dist)
		{
			pointTab.add(j);
		}
	}

	return pointTab;
	}
	else
	{
		return null;
	}
}

public ArrayList<Integer> getNearestPointListExceptPoint(int pointIndex, float range)
{
	//System.out.println("Szukanie najbli¿szego punktu "+x+" "+y);
	
	if(PointTab.size()!=0)
	{
	ArrayList<Integer> pointTab = new ArrayList<Integer>();

	float dist = range;
	float[] t2 = getScaledPoint(pointIndex);
	for (int j = 0; j < PointTab.size(); j++) 
	{
		if(pointIndex==j)
		{
			continue;
		}
		float[] t = getScaledPoint(j);
		float dist2 = (float) tb.distance(t2[0], t2[1], t[0], t[1]);
		if(dist2<dist)
		{
			pointTab.add(j);
		}
		//System.out.println("Odleg³oœæ  "+j+" "+dist+" "+dist2);
	}

	//System.out.println("nabli¿szy punkt w skali to: "+r+ " "+dist);
	if(pointTab.size()>0)
	return pointTab;
	else
	return null;
	}
	else
	{
		return null;
	}
}
public int getNearestPointExceptPointReversed(float x, float y, int firstPoint)
{

	if(PointTab.size()!=0)
	{
	x =  (x/scaleX-X/scaleX);
	y =  (y/scaleY-Y/scaleY);
	int r = 0;


	float dist = (float) tb.distance(x, y, RaundPointTab[0].x, RaundPointTab[0].y);
	
	for (int j = 0; j < PointTab.size(); j++) 
	{
		if(j==firstPoint)
		{
			continue;
		}
		float dist2 = (float) tb.distance(x, y, RaundPointTab[j].x, RaundPointTab[j].y);
		if(dist2<dist)
		{
			dist = dist2;
			r = j;
		}
		//System.out.println("Odleg³oœæ  "+j+" "+dist+" "+dist2);
	}
	float d = 5*(1/scaleX);
	
	//System.out.println("D: ="+d+" "+WindowScale);
	if(dist > d)	{
		System.out.println(dist);
		r = -10;
	}
	//System.out.println("nabli¿szy punkt to: "+r+ " "+dist);
	return r;
	}
	else
	{
		return -10;
	}
}

public int getNearestPointExceptPoint(float x, float y, int firstPoint)
{

	if(PointTab.size()!=0)
	{

	int r = -10;
	float t[];
	if(firstPoint!=0)
	{
		t = getScaledPoint(0);
		r = 0;
	}
	else
	{
		t = getScaledPoint(1);
		r = 1;
	}
	float dist = (float) tb.distance(x, y, t[0], t[1]);
	
	for (int j = 0; j < PointTab.size(); j++) 
	{
		//System.out.println("Sprawdzanie "+firstPoint+" i "+j);
		if(j==firstPoint)
		{
			//System.out.println("Przewijanie punktu "+j+" "+firstPoint);
			continue;
		}
		t = getScaledPoint(j);
		float dist2 = (float) tb.distance(x, y, t[0], t[1]);
		if(dist2<dist)
		{
			dist = dist2;
			r = j;
		}
		//System.out.println("Odleg³oœæ  "+j+" "+dist+" "+dist2);
	}
	float d = 15;
	
	//System.out.println("D: ="+d+" "+WindowScale);
	if(dist > d)	{
		//System.out.println(dist);
		r = -10;
	}
	//System.out.println("nabli¿szy punkt to: "+r+ " "+dist);
	return r;
	}
	else
	{
		return -10;
	}
}
public int getNearestConnection(int x, int y)
{
	//System.out.println("Rozpoczêto sprawdzanie najbli¿szego odcinka");
	if(ConnectionTab.size()!=0)
	{
	float StartX = 0;
	float StartY = 0;
	float EndX = 0;
	float EndY = 0;
	
	int connection = -10;
	float distance = 2147483647;

	for(int i = 0; i < ConnectionTab.size();i++)
	{
		// System.out.println("Istniej¹ po³¹czenia");
		StartX = (RaundPointTab[ConnectionTab.get(i).getP1()].x*scaleX + X);
		StartY = (RaundPointTab[ConnectionTab.get(i).getP1()].y*scaleY +Y);
		EndX = (RaundPointTab[ConnectionTab.get(i).getP2()].x*scaleX + X);
		EndY = (RaundPointTab[ConnectionTab.get(i).getP2()].y*scaleY +Y);

		float maxX = 0;
		float maxY = 0;
		float minX = 0;
		float minY = 0;
		if(StartX>EndX)
		{
			maxX = StartX;
			minX = EndX;
		}
		else
		{
			maxX = EndX;
			minX = StartX;
		}
		if(StartY>EndY)
		{
			maxY = StartY;
			minY = EndY;
		}
		else
		{
			maxY = EndY;
			minY = StartY;
		}	
		float dd = 15+ConnectionTab.get(i).getSize()*(1/scaleX);//(5+ConnectionTab.get(i).getSize())*(1/scaleX);

		
		if(x>=minX-dd&&x<=maxX+dd&&y>=minY-dd&&y<=maxY+dd)
		{
			//System.out.println("Po³¹cznie numer: "+i);
			if(maxX!=minX)
			{
			float t[] = tb.generateLine(StartX, StartY, EndX, EndY);
			float d = tb.getDistToLine(t[0], -1, t[1], x, y);
			//System.out.println("Odlegoœæ do lini "+i+" : "+d);
			
			if(distance>d)
			{
				distance = d;
				connection = i;
			}
			}
			else
			{
				float d =  Math.abs(maxX-x);
				if(distance>d)
				{
					distance = d;
					connection = i;
				}
			}
		}
	}
	
	float dd = 15;
	//System.out.println("D: ="+dd+" "+WindowScale);
	if(distance > dd)	{
		//System.out.println(distance);
		connection = -10;
	}
	//System.out.println("ZWRACANIE ODCINKA "+ connection);
	return connection;
}
	else
	{
		//System.out.println("Brak po³¹czeñ");
		return -10;
	}
}

public int getNearestConnectionExceptPoint(int x, int y, int pointIndex)
{
	//System.out.println("Rozpoczêto sprawdzanie najbli¿szego odcinka");
	if(ConnectionTab.size()!=0)
	{
	float StartX = 0;
	float StartY = 0;
	float EndX = 0;
	float EndY = 0;
	
	int connection = -10;
	float distance = 2147483647;

	for(int i = 0; i < ConnectionTab.size();i++)
	{
		if(ConnectionTab.get(i).isConnected(pointIndex))
		{
			continue;
		}
		// System.out.println("Istniej¹ po³¹czenia");
		StartX = (RaundPointTab[ConnectionTab.get(i).getP1()].x*scaleX + X);
		StartY = (RaundPointTab[ConnectionTab.get(i).getP1()].y*scaleY +Y);
		EndX = (RaundPointTab[ConnectionTab.get(i).getP2()].x*scaleX + X);
		EndY = (RaundPointTab[ConnectionTab.get(i).getP2()].y*scaleY +Y);

		float maxX = 0;
		float maxY = 0;
		float minX = 0;
		float minY = 0;
		if(StartX>EndX)
		{
			maxX = StartX;
			minX = EndX;
		}
		else
		{
			maxX = EndX;
			minX = StartX;
		}
		if(StartY>EndY)
		{
			maxY = StartY;
			minY = EndY;
		}
		else
		{
			maxY = EndY;
			minY = StartY;
		}	
		float dd = 15+ConnectionTab.get(i).getSize()*(1/scaleX);//(5+ConnectionTab.get(i).getSize())*(1/scaleX);

		
		if(x>=minX-dd&&x<=maxX+dd&&y>=minY-dd&&y<=maxY+dd)
		{
			//System.out.println("Po³¹cznie numer: "+i);
			if(maxX!=minX)
			{
			float t[] = tb.generateLine(StartX, StartY, EndX, EndY);
			float d = tb.getDistToLine(t[0], -1, t[1], x, y);
			//System.out.println("Odlegoœæ do lini "+i+" : "+d);
			
			if(distance>d)
			{
				distance = d;
				connection = i;
			}
			}
			else
			{
				float d =  Math.abs(maxX-x);
				if(distance>d)
				{
					distance = d;
					connection = i;
				}
			}
		}
	}
	
	float dd = 15;
	//System.out.println("D: ="+dd+" "+WindowScale);
	if(distance > dd)	{
		//System.out.println(distance);
		connection = -10;
	}
	//System.out.println("ZWRACANIE ODCINKA "+ connection);
	return connection;
}
	else
	{
		//System.out.println("Brak po³¹czeñ");
		return -10;
	}
}

public void addPoinToConnection(int ConIndex, float x, float y)
{
	System.out.println("Pocz¹tkowa d³ugoœæ tabP1 "+PointTab.size());
	x =  (x/scaleX-X/scaleX);
	y =  (y/scaleY-Y/scaleY);
	addPoint(new Point(x, y));
	
	int index = (PointTab.size()-1);
	System.out.println("Pocz¹tkowa d³ugoœæ tabP2 "+PointTab.size());
	System.out.println("Dodawanie punktu "+x+" "+y+" pod indexem: "+index);
	int P1 = ConnectionTab.get(ConIndex).getP1();
	int P2 = ConnectionTab.get(ConIndex).getP2();
	float size = ConnectionTab.get(ConIndex).getSize();
	Color c = ConnectionTab.get(ConIndex).getC();
	System.out.println(P1+" "+index+" "+P2);
	PastBox.eventChangeConn(StateBox.frameIndex, StateBox.objIndex, ConIndex, ConnectionTab.get(ConIndex));
	ConnectionTab.set(ConIndex, new Connection(P1, index, size,c));
	addConnection(new Connection(index, P2, size,c));
	cutConnectionInPolygons(ConIndex, PointTab.size()-1, ConnectionTab.size()-1, ConnectionTab.get(ConIndex).getP1());
}

public void addPoinToConnectionByIndex(int ConIndex, int pointIndex)
{
	int P1 = ConnectionTab.get(ConIndex).getP1();
	int P2 = ConnectionTab.get(ConIndex).getP2();
	float size = ConnectionTab.get(ConIndex).getSize();
	Color c = ConnectionTab.get(ConIndex).getC();
	//System.out.println(P1+" "+pointIndex+" "+P2);
	Connection C1 = new Connection(P1, pointIndex, size,c);
	Connection C2 = new Connection(pointIndex, P2, size,c);
	PastBox.eventChangeConn(StateBox.frameIndex, StateBox.objIndex, ConIndex, ConnectionTab.get(ConIndex));
	ConnectionTab.set(ConIndex, C1);
	addConnection(C2);
}

public void addPoinToConnectionByIndex(int ConIndex, int pointIndex, ArrayList<Integer> tabOfConn) //POJEDYÑCZE PRZECIÊCIA --> TO Z TEGO SIÊ KORZYSTA 
{
	System.out.println("1");
	PastBox.eventChangeConn(StateBox.frameIndex, StateBox.objIndex, ConIndex, ConnectionTab.get(ConIndex));
	System.out.println("Rozpoczêcie ciêcia odcinka");
	
	int P1 = ConnectionTab.get(ConIndex).getP1();
	int P2 = ConnectionTab.get(ConIndex).getP2();
	float size = ConnectionTab.get(ConIndex).getSize();
	Color c = ConnectionTab.get(ConIndex).getC();
	//System.out.println(P1+" "+pointIndex+" "+P2);
	Connection C1 = new Connection(P1, pointIndex, size,c);
	Connection C2 = new Connection(pointIndex, P2, size,c);
	tabOfConn.add(ConIndex);
	ConnectionTab.set(ConIndex, C1);
	addConnection(C2);

	tabOfConn.add(ConnectionTab.size()-1);
	cutConnectionInPolygons(ConIndex, pointIndex, ConnectionTab.size()-1, ConnectionTab.get(ConIndex).getP1());
}

/**
 * Dodaje wiele przeciêæ przeciêcia do odcinka
 * @param ConIndex -> numer odcinka
 * @param pointTab 
 * @param tabOfConn -> tu s¹ zapisywane indexy dodanych odcinków
 * @return Dod
 */
public void addPoinToConnectionByIndex(int ConIndex, ArrayList<Integer> pointTab, ArrayList<Integer> tabOfConn) //WIELOKROTNE PRZECIÊCIA --> TO Z TEGO SIÊ KORZYSTA 
{
	System.out.println("Wielokrotne przeciêcia");
	if(pointTab.size()>1)
	{
	
	float size = ConnectionTab.get(ConIndex).getSize();
	Color c = ConnectionTab.get(ConIndex).getC();
	PastBox.eventChangeConn(StateBox.frameIndex, StateBox.objIndex, ConIndex, ConnectionTab.get(ConIndex));
		ConnectionTab.set(ConIndex, new Connection(pointTab.get(0), pointTab.get(1), size,c));
		tabOfConn.add(ConIndex);
		ArrayList<Integer> connIndexTab = new ArrayList<Integer>();
		for(int i = 1; i < pointTab.size()-1; i++)
		{
			addConnection(new Connection(pointTab.get(i), pointTab.get(i+1), size,c));
			tabOfConn.add(ConnectionTab.size()-1);
			connIndexTab.add(ConnectionTab.size()-1);
		}
		cutConnectionInPolygonsMultiple(ConIndex, pointTab.get(0), pointTab.get(1), pointTab.get(pointTab.size()-1), connIndexTab);
	}
}

private void cutConnectionInPolygonsMultiple(int connIndex, int testPoint, int startPoint, int endPoint, ArrayList<Integer> newConnTab)
{
	System.out.println("Rozpoczynanie odnajdywania przciêæ z polygonem");
	ArrayList<Integer> tab = new ArrayList<Integer>(2);

	for(int i = 0; i < PolygonTab.size(); i++)
	{
		int j = PolygonTab.get(i).isConnIncluded(connIndex);
		if(j!=-10)
		{
			tab.add(i);
		}	
	}
	for(int i = 0; i < tab.size(); i++)
	{
		PolygonTab.get(tab.get(i)).cutPolygonWithPointsNEW(ConnectionTab, connIndex, testPoint, startPoint, endPoint, newConnTab);
	}
	System.out.println("Zakoñczono odnajdywanie przciêæ z polygonem");
}


private void addPoinToConnectionWithlastPoint(int ConIndex, int pointindex)
{
	int index = (pointindex);
	int P2 = ConnectionTab.get(ConIndex).getP2();
	float size = ConnectionTab.get(ConIndex).getSize();
	Color c = ConnectionTab.get(ConIndex).getC();
	PastBox.eventChangeConn(StateBox.frameIndex, StateBox.objIndex, ConIndex, ConnectionTab.get(ConIndex));
	ConnectionTab.get(ConIndex).setP2(index);
	addConnection(new Connection(index, P2, size,c));
}
public void catConnection(int ConIndex, float x, float y)
{
	x =  (x/scaleX-X/scaleX);
	y =  (y/scaleY-Y/scaleY);
	int P1 = ConnectionTab.get(ConIndex).getP1();
	int P2 = ConnectionTab.get(ConIndex).getP2();
	float size = ConnectionTab.get(ConIndex).getSize();
	Color c = ConnectionTab.get(ConIndex).getC();
	addPoint(new Point(x, y));
	ConnectionTab.set(ConIndex, new Connection(P1, PointTab.size()-1, size,c));
	addPoint(new Point(x, y));
	addConnection(new Connection(PointTab.size()-1, P2, size,c));
}
public void catConnectionNoScale(int ConIndex, float x, float y)
{
	int P1 = ConnectionTab.get(ConIndex).getP1();
	int P2 = ConnectionTab.get(ConIndex).getP2();
	float size = ConnectionTab.get(ConIndex).getSize();
	Color c = ConnectionTab.get(ConIndex).getC();
	addPoint(new Point(x, y));
	ConnectionTab.set(ConIndex, new Connection(P1, PointTab.size()-1, size,c));
	addPoint(new Point(x, y));
	addConnection(new Connection(PointTab.size()-1, P2, size,c));
}
public void catConnectionNoScaleMarked(int ConIndex, Point p)
{
	int P1 = ConnectionTab.get(ConIndex).getP1();
	int P2 = ConnectionTab.get(ConIndex).getP2();

	float size = ConnectionTab.get(ConIndex).getSize();
	Color c = ConnectionTab.get(ConIndex).getC();
	addPoint(new Point(p));
	PointTab.get(PointTab.size()-1).MarkedPoint = true;
	ConnectionTab.set(ConIndex, new Connection(P1, PointTab.size()-1, size,c));
	addPoint(new Point(p));
	PointTab.get(PointTab.size()-1).MarkedPoint = true;
	addConnection(new Connection(PointTab.size()-1, P2, size,c));
}
public void catConnectionNoScale(int ConIndex, Point p)
{
	int P1 = ConnectionTab.get(ConIndex).getP1();
	int P2 = ConnectionTab.get(ConIndex).getP2();
	float size = ConnectionTab.get(ConIndex).getSize();
	Color c = ConnectionTab.get(ConIndex).getC();
	addPoint(new Point(p));
	PointTab.get(PointTab.size()-1).MarkedPoint = true;
	ConnectionTab.set(ConIndex, new Connection(P1, PointTab.size()-1, size,c));
	addPoint(new Point(p));
	PointTab.get(PointTab.size()-1).MarkedPoint = true;
	addConnection(new Connection(PointTab.size()-1, P2, size,c));
}
public void catConnection(int ConIndex, ArrayList<Point> listOfPoints)
{

	int P1 = ConnectionTab.get(ConIndex).getP1();
	int P2 = ConnectionTab.get(ConIndex).getP2();
	float size = ConnectionTab.get(ConIndex).getSize();
	Color c = ConnectionTab.get(ConIndex).getC();
	QuickSort.SortPoint(listOfPoints);
	if(PointTab.get(P1).y<PointTab.get(P2).y)
	{
		addPoint(new Point(listOfPoints.get(0)));
		ConnectionTab.set(ConIndex, new Connection(P1, PointTab.size()-1, size,c));
		for(int i = 0; i < listOfPoints.size()-1; i++)
		{
			addPoint(new Point(listOfPoints.get(i)));
			addPoint(new Point(listOfPoints.get(i+1)));
			addConnection(new Connection(PointTab.size()-1, PointTab.size()-2, size,c));
		}
		addPoint(new Point(listOfPoints.get(listOfPoints.size()-1)));
		addConnection(new Connection(PointTab.size()-1, P2, size,c));
	}
	else
	{
		addPoint(new Point(listOfPoints.get(0)));
		ConnectionTab.set(ConIndex, new Connection(P2, PointTab.size()-1, size,c));
		for(int i = 0; i < listOfPoints.size()-1; i++)
		{
			addPoint(new Point(listOfPoints.get(i)));
			addPoint(new Point(listOfPoints.get(i+1)));
			addConnection(new Connection(PointTab.size()-1, PointTab.size()-2, size,c));
		}
		addPoint(new Point(listOfPoints.get(listOfPoints.size()-1)));
		addConnection(new Connection(PointTab.size()-1, P1, size,c));
	}
}
public void catConnectionMarked(int ConIndex, ArrayList<Point> listOfPoints)
{

	int P1 = ConnectionTab.get(ConIndex).getP1();
	int P2 = ConnectionTab.get(ConIndex).getP2();
	float size = ConnectionTab.get(ConIndex).getSize();
	Color c = ConnectionTab.get(ConIndex).getC();
	QuickSort.SortPoint(listOfPoints);
	
	for(Point p : listOfPoints)
		p.MarkedPoint = true;
	
	if(PointTab.get(P1).y<PointTab.get(P2).y)
	{
		addPoint(new Point(listOfPoints.get(0)));
		PointTab.get(PointTab.size()-1).MarkedPoint = true;
		ConnectionTab.set(ConIndex, new Connection(P1, PointTab.size()-1, size,c));
		for(int i = 0; i < listOfPoints.size()-1; i++)
		{
			addPoint(new Point(listOfPoints.get(i)));
			addPoint(new Point(listOfPoints.get(i+1)));
			PointTab.get(PointTab.size()-1).MarkedPoint = true;
			PointTab.get(PointTab.size()-2).MarkedPoint = true;
			addConnection(new Connection(PointTab.size()-1, PointTab.size()-2, size,c));
		}
		addPoint(new Point(listOfPoints.get(listOfPoints.size()-1)));
		PointTab.get(PointTab.size()-1).MarkedPoint = true;
		addConnection(new Connection(PointTab.size()-1, P2, size,c));
	}
	else
	{
		addPoint(new Point(listOfPoints.get(0)));
		PointTab.get(PointTab.size()-1).MarkedPoint = true;
		ConnectionTab.set(ConIndex, new Connection(P2, PointTab.size()-1, size,c));
		for(int i = 0; i < listOfPoints.size()-1; i++)
		{
			addPoint(new Point(listOfPoints.get(i)));
			addPoint(new Point(listOfPoints.get(i+1)));
			PointTab.get(PointTab.size()-1).MarkedPoint = true;
			PointTab.get(PointTab.size()-2).MarkedPoint = true;
			addConnection(new Connection(PointTab.size()-1, PointTab.size()-2, size,c));
		}
		addPoint(new Point(listOfPoints.get(listOfPoints.size()-1)));
		PointTab.get(PointTab.size()-1).MarkedPoint = true;
		addConnection(new Connection(PointTab.size()-1, P1, size,c));
	}
}
public int separateConection(int ConIndex)
{
	int P1 = ConnectionTab.get(ConIndex).getP1();
	int P2 = ConnectionTab.get(ConIndex).getP2();
	float size = ConnectionTab.get(ConIndex).getSize();
	Color c = ConnectionTab.get(ConIndex).getC();
	ConnectionTab.remove(ConIndex);
	addPoint(new Point(PointTab.get(P1).x, PointTab.get(P1).y));
	addPoint(new Point(PointTab.get(P2).x, PointTab.get(P2).y));
	addConnection(new Connection(PointTab.size()-1, PointTab.size()-2, size, c));
	ConnectionTab.get(ConnectionTab.size()-1).setMarked(true);
	return (ConnectionTab.size()-1);
}

public Point[] getConPoints(int ConIndex)
{
	float maxX = PointTab.get(ConnectionTab.get(ConIndex).getP1()).x*scaleX+X;
	float maxY = PointTab.get(ConnectionTab.get(ConIndex).getP1()).y*scaleY+Y;
	float minX = PointTab.get(ConnectionTab.get(ConIndex).getP2()).x*scaleX+X;
	float minY = PointTab.get(ConnectionTab.get(ConIndex).getP2()).y*scaleY+Y;
	
	Point[] t = {new Point(maxX, maxY),new Point(minX, minY)};
	return t;
}
public void cutConnectionInPolygons(int connIndex, int pointIndex, int secConnIndex, int indexOfPointInNonChangingConn)
{
	System.out.println("Rozpoczynanie odnajdywania przciêæ z polygonem");
	ArrayList<Integer> tab = new ArrayList<Integer>(2);

	for(int i = 0; i < PolygonTab.size(); i++)
	{
		int j = PolygonTab.get(i).isConnIncluded(connIndex);
		if(j!=-10)
		{
			tab.add(i);
		}	
	}
	System.out.print("Znaleziono polygony: ");
	for(int i :tab)
		System.out.print(i+" ");
	System.out.println();
	
	for(int i :tab)
	{
		System.out.println("Ciêcie w polygonie nr. "+i);
		PolygonTab.get(i).cutConnWithPointNEW(connIndex, pointIndex, secConnIndex, indexOfPointInNonChangingConn);
	}
	System.out.println("Zakoñczono odnajdywanie przciêæ z polygonem");
}

public ArrayList<Integer> getPolygonsWithConnIncluded(int connIndex)
{
	ArrayList<Integer> tab = new ArrayList<Integer>(2);
	for(FilledPolygonWithCurves p : PolygonTab)
	{
		int i = p.isConnIncluded(connIndex);
		if(i!=-10)
		{
			tab.add(i);
		}	
	}
	return tab;
}
public void mergePointsWithList(int P1, int P2, ArrayList<Integer> tab)
{
	if(P1!=P2)
	{
	for(int i = 0; i < ConnectionTab.size(); i++)
	{
		if(ConnectionTab.get(i).isARC()==false)
		{
			if(ConnectionTab.get(i).getP1()==P1)
			{
				ConnectionTab.get(i).setP1(P2);
			}
			if(ConnectionTab.get(i).getP2()==P1)
			{
				ConnectionTab.get(i).setP2(P2);
			}
		}
		else
		{
			if(ConnectionTab.get(i).isDoubleArc())
			{
				if(ConnectionTab.get(i).getP1()==P1)
				{
					ConnectionTab.get(i).setP1(P2);
				}
				if(ConnectionTab.get(i).getP2()==P1)
				{
					ConnectionTab.get(i).setP2(P2);
				}
				if(ConnectionTab.get(i).getP3()==P1)
				{
					ConnectionTab.get(i).setP3(P2);
				}
				if(ConnectionTab.get(i).getP4()==P1)
				{
					ConnectionTab.get(i).setP4(P2);
				}
			}
			else
			{
				if(ConnectionTab.get(i).getP1()==P1)
				{
					ConnectionTab.get(i).setP1(P2);
				}
				if(ConnectionTab.get(i).getP2()==P1)
				{
					ConnectionTab.get(i).setP2(P2);
				}
				if(ConnectionTab.get(i).getP3()==P1)
				{
					ConnectionTab.get(i).setP3(P2);
				}
			}
		}
		
	}
	removePoint(P1);
	for(int i = 0; i < tab.size(); i++)
	{
		if(tab.get(i)>=P1)
		{
			tab.set(i, tab.get(i)-1);
		}
	}
	}
	checkLineParadox();
}

public void mergePointList(int P1, ArrayList<Integer> pointTab)
{
	
	for(int i = 0; i < ConnectionTab.size(); i++)
	{
		for(int index:pointTab)
		{
		if(ConnectionTab.get(i).isARC()==false)
		{
			if(ConnectionTab.get(i).getP1()==index)
			{
				ConnectionTab.get(i).setP1(P1);
			}
			if(ConnectionTab.get(i).getP2()==index)
			{
				ConnectionTab.get(i).setP2(P1);
			}
		}
		else
		{
			if(ConnectionTab.get(i).isDoubleArc())
			{
				if(ConnectionTab.get(i).getP1()==index)
				{
					ConnectionTab.get(i).setP1(P1);
				}
				if(ConnectionTab.get(i).getP2()==index)
				{
					ConnectionTab.get(i).setP2(P1);
				}
				if(ConnectionTab.get(i).getP3()==index)
				{
					ConnectionTab.get(i).setP3(P1);
				}
				if(ConnectionTab.get(i).getP4()==index)
				{
					ConnectionTab.get(i).setP4(P1);
				}
			}
			else
			{
				if(ConnectionTab.get(i).getP1()==index)
				{
					ConnectionTab.get(i).setP1(P1);
				}
				if(ConnectionTab.get(i).getP2()==index)
				{
					ConnectionTab.get(i).setP2(P1);
				}
				if(ConnectionTab.get(i).getP3()==index)
				{
					ConnectionTab.get(i).setP3(P1);
				}
			}
		}
		}
	}
	QuickSort.SortIntArray(pointTab);
	for(int i = 0; i < pointTab.size(); i++)
	{
		removePointWithoutGeneratingTab(pointTab.get(pointTab.size()-1-i));
	}
	generateRaundPointTab();
	checkLineParadox();
	//checkSinglePoints();
}
public void removePointWithoutGeneratingTab(int P)
{
	PointTab.remove(P);
	removePointFormPolygons(P);
	for(int i = 0; i < ConnectionTab.size(); i++)
	{
		if(ConnectionTab.get(i).isConnected(P))
		{
			ConnectionTab.remove(i);
			i--;
		}
		else
		{
			if(ConnectionTab.get(i).isARC()==false)
			{
				if(ConnectionTab.get(i).getP1()>P)
				{
					ConnectionTab.get(i).setP1(ConnectionTab.get(i).getP1()-1);
				}
				if(ConnectionTab.get(i).getP2()>P)
				{
					ConnectionTab.get(i).setP2(ConnectionTab.get(i).getP2()-1);
				}
			}
			else
			{
				if(ConnectionTab.get(i).isDoubleArc())
				{
					if(ConnectionTab.get(i).getP1()>P)
					{
						ConnectionTab.get(i).setP1(ConnectionTab.get(i).getP1()-1);
					}
					if(ConnectionTab.get(i).getP2()>P)
					{
						ConnectionTab.get(i).setP2(ConnectionTab.get(i).getP2()-1);
					}
					if(ConnectionTab.get(i).getP3()>P)
					{
						ConnectionTab.get(i).setP3(ConnectionTab.get(i).getP3()-1);
					}
					if(ConnectionTab.get(i).getP4()>P)
					{
						ConnectionTab.get(i).setP4(ConnectionTab.get(i).getP4()-1);
					}
				}
				else
				{
					if(ConnectionTab.get(i).getP1()>P)
					{
						ConnectionTab.get(i).setP1(ConnectionTab.get(i).getP1()-1);
					}
					if(ConnectionTab.get(i).getP2()>P)
					{
						ConnectionTab.get(i).setP2(ConnectionTab.get(i).getP2()-1);
					}
					if(ConnectionTab.get(i).getP3()>P)
					{
						ConnectionTab.get(i).setP3(ConnectionTab.get(i).getP3()-1);
					}
				}
			}
		}
	}

}
public void removePoint(int P)
{
	//PastBox.eventRemPoint(StateBox.frameIndex, StateBox.objIndex, P, PointTab.get(P));
	PointTab.remove(P);
	System.out.println("Usuwanie punktu "+P);
	removePointFormPolygons(P);
	for(int i = 0; i < ConnectionTab.size(); i++)
	{
		if(ConnectionTab.get(i).isConnected(P))
		{
			removeCon(i);
			i--;
		}
		else
		{
			if(ConnectionTab.get(i).isARC()==false)
			{
				if(ConnectionTab.get(i).getP1()>P)
				{
					
					ConnectionTab.get(i).setP1(ConnectionTab.get(i).getP1()-1);
				}
				if(ConnectionTab.get(i).getP2()>P)
				{
					
					ConnectionTab.get(i).setP2(ConnectionTab.get(i).getP2()-1);
				}
			}
			else
			{
				if(ConnectionTab.get(i).isDoubleArc())
				{
					if(ConnectionTab.get(i).getP1()>P)
					{
						
						ConnectionTab.get(i).setP1(ConnectionTab.get(i).getP1()-1);
					}
					if(ConnectionTab.get(i).getP2()>P)
					{
						
						ConnectionTab.get(i).setP2(ConnectionTab.get(i).getP2()-1);
					}
					if(ConnectionTab.get(i).getP3()>P)
					{
						
						ConnectionTab.get(i).setP3(ConnectionTab.get(i).getP3()-1);
					}
					if(ConnectionTab.get(i).getP4()>P)
					{
						
						ConnectionTab.get(i).setP4(ConnectionTab.get(i).getP4()-1);
					}
				}
				else
				{
					if(ConnectionTab.get(i).getP1()>P)
					{
						
						ConnectionTab.get(i).setP1(ConnectionTab.get(i).getP1()-1);
					}
					if(ConnectionTab.get(i).getP2()>P)
					{
						
						ConnectionTab.get(i).setP2(ConnectionTab.get(i).getP2()-1);
					}
					if(ConnectionTab.get(i).getP3()>P)
					{
						
						ConnectionTab.get(i).setP3(ConnectionTab.get(i).getP3()-1);
					}
				}
			}
		}
	}

	generateRaundPointTab();
}

public void removePointFormPolygons(int P) {
	
	//System.out.println("Usuwanie punktów z polygonów "+PolygonTab.size());
	for(FilledPolygonWithCurves f: PolygonTab)
	{
		f.removePoint(P);
	}
	
}


public void removePointsSorted(ArrayList<Integer> pointList)
{
	System.out.println("USUWANIE "+pointList.size()+" PUNKTÓW "+PointTab.size());
	
	for(int i = pointList.size()-1; i > -1; i--)
	{
		//System.out.println("Usuwanie "+pointList.get(i));
		PointTab.remove((int)pointList.get(i));
		removePointFormPolygons(pointList.get(i));
	}
	
	for(int i : pointList)
		System.out.print(i+" ");
	
	System.out.println();
	//System.out.println("ENDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD "+PointTab.size());
	for(int i = 0; i < ConnectionTab.size(); i++)
	{
		Connection c = ConnectionTab.get(i);

		for(int j = pointList.size()-1; j > -1; j--)
		{
			//System.out.println("Rozpoczêto sprawdzanie CONN nr "+i+" i punkt nr "+pointList.get(j));
			if(c.getP1() == pointList.get(j))
			{
				//System.out.println("Usuwanie CONN "+i+" bo zawiera punkt "+pointList.get(j));
				removeCon(i);
				//ConnectionTab.remove(i);
				i--;
				break;
			}
			else if(c.getP2() == pointList.get(j))
			{
				//System.out.println("Usuwanie CONN "+i+" bo zawiera punkt "+pointList.get(j));
				removeCon(i);
				//ConnectionTab.remove(i);
				i--;
				break;
			}
			if(c.isARC()&&c.isDoubleArc())
			{
				if(c.getP3() == pointList.get(j))
				{
					//ConnectionTab.remove(i);
					removeCon(i);
					i--;
					break;
				}
				else if(c.getP4() == pointList.get(j))
				{
					//ConnectionTab.remove(i);
					removeCon(i);
					i--;
					break;
				}
			}
			
			
			if(c.getP1() > pointList.get(j))
			{
				//System.out.println("Zmniejszanie punktu p1 "+c.getP1()+" > "+pointList.get(i));
				ConnectionTab.get(i).decreseP1(-1);
				//System.out.println("Punkt "+(ConnectionTab.get(i).getP1()+1)+" Zmniejszono do "+ConnectionTab.get(i).getP1());
			}
			if(c.getP2() > pointList.get(j))
			{
				//System.out.println("Zmniejszanie punktu p2 "+c.getP2()+" > "+pointList.get(i));
				ConnectionTab.get(i).decreseP2(-1);
				//System.out.println("Punkt "+(ConnectionTab.get(i).getP2()+1)+" Zmniejszono do "+ConnectionTab.get(i).getP2());
			}
			if(c.isARC()&&c.isDoubleArc())
			{
				if(c.getP3() > pointList.get(j))
				{
					ConnectionTab.get(i).decreseP3(-1);
				}
				if(c.getP4() > pointList.get(j))
				{
					ConnectionTab.get(i).decreseP4(-1);
				}
			}
			//System.out.println("Zakoñczono petle "+j);
		}
	}
	generateRaundPointTab();
	frameCupture.hasFrame = false;
//	for(Connection c : ConnectionTab)
//		c.printCon();
}
public void removePointsNotSorted(ArrayList<Integer> pointList)
{
	QuickSort.SortIntArray(pointList);
	removePointsSorted(pointList);
}


public void removeConn(int index)
{
	for(FilledPolygonWithCurves fp : PolygonTab)
	{
		for(int i = 0; i < fp.connectionIndexTab.size(); i++)
		{
			if(fp.connectionIndexTab.get(i)==index)
			{
				fp.connectionIndexTab.remove(i);
				i--;
			}
			else if(fp.connectionIndexTab.get(i)>index)
			{
				fp.connectionIndexTab.set(i, fp.connectionIndexTab.get(i)-1);
			}
		}
	}
	for(FilledPolygonWithCurves fp : PolygonTab)
	{
		for(int i = 0; i < fp.shapeTab.size(); i++)
		{
			if(fp.shapeTab.get(i).polygonEnd)
				continue;
			if(fp.shapeTab.get(i).connIndex==index)
			{
				fp.shapeTab.remove(i);
				i--;
			}
			else if(fp.shapeTab.get(i).connIndex>index)
			{
				fp.shapeTab.get(i).connIndex = fp.shapeTab.get(i).connIndex-1;
			}
		}
	}
}

public void mergePointsInPolygon(int P1, int P2)
{
	for(FilledPolygonWithCurves p : PolygonTab)
		p.mengePoints(P1, P2);
}

public void removeListOfConnsNotSorted(ArrayList<Integer> tab)
{
	QuickSort.SortIntArray(tab);
	for(int i = tab.size()-1; i > -1; i--)
	{
		removeConn(tab.get(i));
	}
}
public void removeListOfConnsSortedFromPolygons(ArrayList<Integer> tab)
{
	System.out.println("Usuwanie listy Odcinków "+tab.size());
	for(int i = tab.size()-1; i > -1; i--)
	{
		System.out.println("Usuwanie odcinka "+tab.get(i));
		removeConn(tab.get(i));
	}
}
public void removeListOfConnsSorted(ArrayList<Integer> tab)
{
	System.out.println("Usuwanie listy Odcinków "+tab.size());
	for(int i = tab.size()-1; i > -1; i--)
	{
		System.out.println("Usuwanie odcinka "+tab.get(i));
		removeCon(tab.get(i));
	}
}
public void OLDaddConnectionOLD(Connection c)
{
	//Sprawdzenie czy linia nie przecina innej lini, a je¿eli to siê dzieje linia jest dzielona
	checkCollisionWithNewLine(c);
	//System.out.print("Dodano po³acznie: ");
	//c.printCon();
}

//Wersja z lini¹ jako punktem pocz¹tek i koniec w tym samym punkcie
public void checkLineParadox()
{
	for(int i = 0; i < ConnectionTab.size(); i++)
	{
		if(PointTab.get(ConnectionTab.get(i).getP1())==PointTab.get(ConnectionTab.get(i).getP2()))
		{
			ConnectionTab.remove(i);
			i--;
		}
	}
}
//TODO DOKOÑÆZYÆ FUNKCJÊ
public void removePointsIfTheyAreSingle(int p1, int p2)
{
	boolean p1IsConnected = false;
	boolean p2IsConnected = false;
	for(int i = 0; i < ConnectionTab.size(); i++)
	{
		if(ConnectionTab.get(i).isConnected(p1))
		{
			p1IsConnected = true;
			if(p1IsConnected&&p2IsConnected)
				break;
		}
		if(ConnectionTab.get(i).isConnected(p2))
		{
			p2IsConnected = true;
			if(p1IsConnected&&p2IsConnected)
				break;
		}
	}
	
	
	if(!p1IsConnected)
	{
		System.out.println("Usuwanie P1 bo nie pod³¹czone "+p1);
		if(p1<p2)
		{
			p2--;
		}
		removePoint(p1);
	}
	if(!p2IsConnected)
	{
		System.out.println("Usuwanie P2 bo nie pod³¹czone "+p2);
		removePoint(p2);
	}
//	int[] tabTabToRemove = null;
//	if(!p1IsConnected&&!p2IsConnected)
//	{
//		tabTabToRemove = new int[2];
//		tabTabToRemove[0] = p1;
//		tabTabToRemove[1] = p2;
//	}
//	else if(!p1IsConnected&&p2IsConnected)
//	{
//		tabTabToRemove = new int[1];
//		tabTabToRemove[0] = p1;
//	}
//	else if(p1IsConnected&&!p2IsConnected)
//	{
//		tabTabToRemove = new int[1];
//		tabTabToRemove[0] = p2;
//	}
//	
//	if(tabTabToRemove!=null)
//	{
//		System.out.println();
//		removePoints(tabTabToRemove);
//	}
}



//TODO Dodanie ciêcia nak³adj¹cych siê lini na zasadzie ¿e jedna le¿y dok³adnie na drugiej Oraz zmiana przeszukiwania potencjalnych lini tak by by³o w stanie odnaleŸæ potencjalne linie nak³¹dj¹ce siê
public int checkCollisionWithNewLine(Connection c)
{
	//System.out.println("Rozpoczêto dodawanie po³¹czenia c o punktach "+c.getP1()+" "+c.getP2()+" --> "+PointTab.get(c.getP1()).x+" "+PointTab.get(c.getP1()).y+" / "+PointTab.get(c.getP2()).x+" "+PointTab.get(c.getP2()).y);
	//double time = System.nanoTime();
	int countOfCross = 0;
	c.generateLineData(PointTab);
	ArrayList<Integer> potencialLines = new ArrayList<Integer>();

	for(int i = 0; i < ConnectionTab.size();i++)
	{
		ConnectionTab.get(i).generateLineData(PointTab);
		Connection c2 = ConnectionTab.get(i);
		if((c.getMinX()>c2.getMaxX()||c.getMaxX()<c2.getMinX()||c.getMinY()>c2.getMaxY()||c.getMaxY()<c2.getMinY())==false)
		{	
			if((ConnectionTab.get(i).isConnected(c.getP1())||ConnectionTab.get(i).isConnected(c.getP2()))==false)
			{
				potencialLines.add(i);
			}
		}
	}

	//System.out.println("Wygenerowano potencjalnie przecinaj¹ce siê linie iloœæ lini to "+potencialLines.size());
	
	
	if(potencialLines.size()==0)
	{
		//System.out.println("Dodawanie po³aczenia wariant bez ciêcia");
		ConnectionTab.add(c);
		return countOfCross;
	}
	
	else
	{
		//System.out.println("Wariant z ciêciem i liniami potencjalnymi");
		ArrayList<Connection> baseLines = new ArrayList<Connection>(); //odcinki na które zostanie pociêty dodawany odcinek
		baseLines.add(c);
		float ABC[] = tb.generatePatternABC(PointTab.get(c.getP1()).x, PointTab.get(c.getP1()).y, PointTab.get(c.getP2()).x, PointTab.get(c.getP2()).y);//Oblicznie wzoru ogólnego dla podstawowego odcinka
			for(int j = 0; j < baseLines.size(); j++)
			{
			for(int i = 0; i < potencialLines.size(); i++)
			{
				ConnectionTab.get(potencialLines.get(i)).generateLineData(PointTab);
				Connection c2 = ConnectionTab.get(potencialLines.get(i));
				float ABC2[] = tb.generatePatternABC(PointTab.get(c2.getP1()).x, PointTab.get(c2.getP1()).y,PointTab.get(c2.getP2()).x, PointTab.get(c2.getP2()).y);
				float XY[] = tb.corossPointOfTwoLines(ABC, ABC2);
				if(XY!=null)
				{
					if((tb.isVertical(ABC)||tb.isVertical(ABC2))==false)
					{
					if(tb.isPointInside(XY[0], XY[1], baseLines.get(j).getMaxX(), baseLines.get(j).getMaxY(), baseLines.get(j).getMinX(), baseLines.get(j).getMinY())
						&&tb.isPointInside(XY[0], XY[1], c2.getMaxX(), 	c2.getMaxY(), c2.getMinX(), c2.getMinY()))
						{
							countOfCross++;
							addCrossPoint(XY, potencialLines, baseLines, j, i);
							j--;
							potencialLines.remove(i);
							break;
							
						}
					}
					else
					{
						if(tb.isVertical(ABC)&&tb.isVertical(ABC2)==false)
						{
							//System.out.println("Wariant 1");
							if(tb.isPointInsideVertical(XY[0], XY[1], baseLines.get(j).getMaxX(), baseLines.get(j).getMaxY(), baseLines.get(j).getMinX(), baseLines.get(j).getMinY())
									&&tb.isPointInside(XY[0], XY[1], c2.getMaxX(), 	c2.getMaxY(), c2.getMinX(), c2.getMinY()))
							{
								countOfCross++;
								addCrossPoint(XY, potencialLines, baseLines, j, i);
								j--;
								potencialLines.remove(i);
								break;
							}
						}else if(tb.isVertical(ABC2)&&tb.isVertical(ABC)==false)
						{
							if(tb.isPointInside(XY[0], XY[1], baseLines.get(j).getMaxX(), baseLines.get(j).getMaxY(), baseLines.get(j).getMinX(), baseLines.get(j).getMinY())
									&&tb.isPointInsideVertical(XY[0], XY[1], c2.getMaxX(), 	c2.getMaxY(), c2.getMinX(), c2.getMinY()))
							{
								countOfCross++;
								addCrossPoint(XY, potencialLines, baseLines, j, i);
								j--;
								potencialLines.remove(i);
								break;
							}
						}
						else
						{
							System.out.println("Wariant 3 LINIA PIONOWA NA PIONOWEJ LINI");
						}
					}
				}
				}
				
				
			}
//			System.out.println("Koñczenie obiegu pêtli z "+baseLines.size());
			
		
		//System.out.println("Odnaleziono "+countOfCross+" przeciêæ naniesiono je i przyst¹piono do dodawania pociêtych odcinków których jest "+baseLines.size());
		for(int i = 0; i < baseLines.size(); i++)
		{
			ConnectionTab.add(baseLines.get(i));
		}
		//System.out.println("Wykonano w "+((System.nanoTime()-time)/1000000)+"ms");
		return countOfCross;
		}
	
}

public void addConnection(Connection c)
{
	PastBox.eventAddConn(StateBox.frameIndex, StateBox.objIndex, ConnectionTab.size());
	ConnectionTab.add(c);
}
public void addConnectionWithoutStep(Connection c)
{
	ConnectionTab.add(c);
}
private void addCrossPoint(float XY[], ArrayList<Integer> potencialLines, ArrayList<Connection> baseLines, int j, int i)
{
	addPoint(new Point(XY[0],XY[1]));
	int lastPointIndex = PointTab.size()-1;
	addPoinToConnectionWithlastPoint(potencialLines.get(i), lastPointIndex);
	Connection C1 = new Connection(baseLines.get(j).getP1(), lastPointIndex, baseLines.get(j).getSize(), baseLines.get(j).getC());
	Connection C2 = new Connection(lastPointIndex, baseLines.get(j).getP2(), baseLines.get(j).getSize(), baseLines.get(j).getC());		
	C1.generateLineData(PointTab);
	C2.generateLineData(PointTab);
	baseLines.remove(j);
	baseLines.add(C1);
	baseLines.add(C2);
}

public void removeSinglePointsNEW()
{
	System.out.println("Usuwanie samotnych punktów");
	boolean[] usedPoints = new boolean[PointTab.size()];
	for(Connection c : ConnectionTab)
	{
		usedPoints[c.getP1()] = true;
		usedPoints[c.getP2()] = true;
		if(c.isDoubleArc())
		{
			usedPoints[c.getP3()] = true;
			usedPoints[c.getP4()] = true;
		}
	}
	
	ArrayList<Integer> tab = new ArrayList<Integer>();
	
	for(int i = 0; i < usedPoints.length; i++)
	{
		if(!usedPoints[i])
			tab.add(i);
	}
//	for(int i : tab)
//		System.out.print(i+" ");
//	System.out.println();
	removePointsSorted(tab);
}

//TODO
public void removeCon(int ConIndex)
{
	removeConn(ConIndex);
	ConnectionTab.remove(ConIndex);
}
public Connection getCon(int i)
{
	return ConnectionTab.get(i);
}

public void addPoint(Point p)
{
	PastBox.eventAddPoint(StateBox.frameIndex, StateBox.objIndex, PointTab.size());
	PointTab.add(p);
}

public void removePolygon(int i)
{
	//TODO
	PolygonTab.remove(i);
	
}
public FilledPolygonWithCurves getPolygon(int i)
{
	return PolygonTab.get(i);
}
public void setPolygon(int i, FilledPolygonWithCurves fp)
{
	PolygonTab.set(i, fp);
}

public boolean isShowPoints() {
	return showPoints;
}

public void setShowPoints(boolean showPoints) {
	Object.showPoints = showPoints;
}

public boolean isShowMarkPoints() {
	return showMarkPoints;
}

public void setShowMarkPoints(boolean showMarkPoints) {
	this.showMarkPoints = showMarkPoints;
}

private boolean showMarkPoints = false;
public boolean isRotate3D() {
	return rotate3D;
}

public void setRotate3D(boolean rotate3d) {
	rotate3D = rotate3d;
}
public float[] getBounds()
{
	float[] tab = {MinX, MinY, MaxX, MaxY};
	return tab;
}

public ArrayList<Point> ConvertPointTabToArray(Point tab[])
{
	ArrayList<Point> array = new ArrayList<Point>();
	for(int i  = 0; i < tab.length; i++)
	{
		array.add(tab[i]);
	}
	return array;
}

public ArrayList<Connection> ConvertConnectionTabToArray(Connection tab[])
{
	ArrayList<Connection> array = new ArrayList<Connection>();
	for(int i  = 0; i < tab.length; i++)
	{
		array.add(tab[i]);
	}
	return array;
}

public void setScaleXY(float scaleX2, float scaleY2) {
setScaleX(scaleX2);
setScaleY(scaleY2);
}

public void setXY(float x2, float y2) {
	setX(x2);
	setY(y2);
}
//GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG

public int getPointTabSize()
{
	return PointTab.size();
}

public Point getLastPoint()
{
	return PointTab.get(PointTab.size()-1);
}
public Point getPreLastPoint()
{
	return PointTab.get(PointTab.size()-2);
}
public ArrayList<Point> getPointTab() {
	return PointTab;
}
public void setPointTab(ArrayList<Point> pointTab) {
	PointTab = pointTab;
}
//TODO
public int getConnectionTabSize() {
	return ConnectionTab.size();
}
public ArrayList<Connection> getConnectionTab() {
	return ConnectionTab;
}



public void setConnectionTab(ArrayList<Connection> connectionTab) {

	ConnectionTab = connectionTab;
	for(Connection c : connectionTab)
	{
		System.out.println(c.getP1()+" "+c.getP2()+" "+c.getSize()+" "+c.getC().toString());
	}
	frameCupture.hasFrame = false;
}
public float getAlign() {
	return align;
}
public void setAlign(float align) {
	this.align = align;
}

public float getfX()
{
	return X;
}
public float getfY()
{
	return Y;
}
public float getScaleX() {
	return SX;
}
public void setScaleX(float scaleX) {
	this.SX = scaleX;
}
public float getX() {
	return sX;
}
public void setX(float x) {
	sX = x;
}
public float getY() {
	return sY;
}
public void setY(float y) {
	sY = y;
}
public int getSizeY() {
	return sizeY;
}
public void setSizeY(int sizeY) {
	this.sizeY = sizeY;
}
public int getSizeX() {
	return sizeX;
}
public void setSizeX(int sizeX) {
	this.sizeX = sizeX;
}
public float getScaleY() {
	return SY;
}
public void setScaleY(float scaleY) {
	this.SY = scaleY;
}
public float getRotateX() {
	return RotateX;
}

public void setRotateX(float rotateX) {
	RotateX = rotateX;
}

public float getRotateY() {
	return RotateY;
}

public void setRotateY(float rotateY) {
	RotateY = rotateY;
}

public float getWindowScale(){
	return WindowScale;
}

public void setWindowsScale(float Scale){
	this.WindowScale = Scale;
}

public polygonEngine.FillMap getFm() {
	return fm;
}

public void setFm(polygonEngine.FillMap fm) {
	this.fm = fm;
}

public boolean isChanged() {
	return isChanged;
}

public void setChanged(boolean isChanged) {
	this.isChanged = isChanged;
}

public boolean isDrawWithOutMark() {
	return drawWithOutMark;
}

public void setDrawWithOutMark(boolean drawWithOutMark) {
	Object.drawWithOutMark = drawWithOutMark;
}

public ArrayList<FilledPolygonWithCurves> getPolygonTab() {
	return PolygonTab;
}

public void addPolygon(FilledPolygonWithCurves fp)
{
	//TODO
	System.out.println("Dodawanie Polygona");
	PolygonTab.add(fp);
}
public void setPolygonTab(ArrayList<FilledPolygonWithCurves> polygonTab) {
	PolygonTab = polygonTab;
}

public Point[] getRaundPointTab() {
	return RaundPointTab;
}

public void setRaundPointTab(Point[] raundPointTab) {
	RaundPointTab = raundPointTab;
}
public TransformationBox getActualTransformation()
{
	return new TransformationBox(X, Y, scaleX, scaleY, WindowScale);
}

public ArrayList<Integer> getPolygonToDraw() {
	return polygonToDraw;
}

public void setPolygonToDraw(ArrayList<Integer> polygonToDraw) {
	this.polygonToDraw = polygonToDraw;
}


}
