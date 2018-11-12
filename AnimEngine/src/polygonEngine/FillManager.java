package polygonEngine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import pastManager.PastBox;
import pastManager.StateBox;
import structures.Frame;
import structures.ParameterBox;
import toolBox.QuickSort;
import toolBox.TextBox;
import toolBox.frameCupture;

public class FillManager 
{
	public static int tool =  0;
	private static boolean clicked;
	private static double stabTime;
	private static double shapeTime;
	private static double fillTime;
	public static boolean needToRefresh = true;
	private static int lastGenerated = 0;
	private static double compareTime;
	public static boolean diagnose = false;
	public void setTool(Frame f, int x, int y, boolean MouseClicked, boolean MouseleftClicked)
	{
		if(MouseClicked)
		{
			
		}
	}
	
	public void useTool(Frame f, int x, int y, boolean MouseClicked, boolean MouseleftClicked, ParameterBox pb, Graphics2D g)
	{
		if(tool == 0)
		{
			fillTest(f, x, y, MouseClicked, MouseleftClicked, pb, g);
			if(diagnose)
			{
				diagnosticTool(f, x, y, MouseClicked, pb, g);
			}
		}
		
	}
	public void removePolygon(Frame f, float x, float y)
	{
		int polIndex = getIndexOfFilledPolygon(f, x, y);
		if(polIndex>-10)
		{
			PastBox.addStepStart();
			System.out.println("Usuwanie polygona "+polIndex);
			PastBox.eventRemPolygon(StateBox.frameIndex, StateBox.objIndex, polIndex, f.gco().getPolygon(polIndex));
			f.gco().removePolygon(polIndex);
		}
	}
	public void diagnosticTool(Frame f, float x, float y, boolean MouseClicked, ParameterBox pb, Graphics2D g)
	{

		float[] XY = f.gco().scaleAndRotateValue(x, y);
		x = XY[0];
		y = XY[1];
		double time = System.nanoTime();

		if(needToRefresh)
		{
			f.getObj(lastGenerated).setFm(null);	
			System.gc();
			f.getObj(lastGenerated).setFm(new FillMap());	
			f.gco().getFm().reGenerateAllMap(f, f.gco().getConnectionTab(), f.gco().getPointTab(), g);
			needToRefresh = false;
			System.out.println("Zakoñczono generowanie sztab "+((System.nanoTime()-time)/1000000)+"ms");
			stabTime = ((System.nanoTime()-time)/1000000);
		}
		//stabTime = 0;
		
		time = System.nanoTime();
		PolygonShapeGenerator psg = new PolygonShapeGenerator();
		ArrayList<Integer> connTab = psg.getIncludedConnectiones(f, f.gco().getFm().getSt(), x, y, g);
		
		System.out.println("Zakoñczono Wyszukiwanie kszta³tu figury "+((System.nanoTime()-time)/1000000)+"ms");
		shapeTime = ((System.nanoTime()-time)/1000000);
		time = System.nanoTime();
		PolygonFillSercherNEW pfs = new PolygonFillSercherNEW();
		System.out.println("ROZPOCZYNANIE WYSZUKIWANIA KSZTA£TU");
		if(connTab!=null)
			pfs.findFillInPolygon(f, f.gco().getPointTab(), f.gco().getConnectionTab(), connTab);
		
		System.out.println("Zakoñczono Wyszukiwanie Wype³nienia "+((System.nanoTime()-time)/1000000)+"ms");
		fillTime = ((System.nanoTime()-time)/1000000);
		
		time = System.nanoTime();
		g.setColor(Color.black);
	}
	public void fillTest(Frame f, float x, float y, boolean MouseClicked, boolean MouseRightClicked, ParameterBox pb, Graphics2D g)
	{
		//TODO
		double time = System.nanoTime();
		//System.out.println("Naciœniêto na "+chceckIfClickedOnPolygon(f, x, y));
		
		if(lastGenerated != f.getObject())
		{
			System.out.println("Resetowanie FillMapy "+lastGenerated);
			needToRefresh = true;
			f.getObj(lastGenerated).setFm(new FillMap());	
		}
		if(needToRefresh)
		{
			lastGenerated = f.getObject();
			f.gco().getFm().reGenerateAllMap(f, f.gco().getConnectionTab(), f.gco().getPointTab(), g);
			needToRefresh = false;
			//System.out.println("Zakoñczono generowanie sztab "+((System.nanoTime()-time)/1000000)+"ms");
			stabTime = ((System.nanoTime()-time)/1000000);
			TextBox.addMessage("Zakoñczono generowanie sztab w czasie "+stabTime+"ms", 300);
		}
		if(MouseRightClicked&&clicked==false)
		{
			
			removePolygon(f, x, y);
			frameCupture.hasFrame = false;
		}
		else if(MouseClicked&&clicked==false)
		{
			
		
		int t = chceckIfClickedOnPolygon(f, x, y);
		//System.out.println("Naciœniêto na "+t);

		clicked = true;
		if(t==-10)
		{

		float[] XY = f.gco().scaleAndRotateValue(x, y);
		x = XY[0];
		y = XY[1];
		//stabTime = 0;
		System.out.println("Wyszukiwanie i dodawanie polygona");
		time = System.nanoTime();
		PolygonShapeGenerator psg = new PolygonShapeGenerator();
		ArrayList<Integer> connTab = psg.getIncludedConnectiones(f, f.gco().getFm().getSt(), x, y, g);
		
		System.out.println("Zakoñczono Wyszukiwanie kszta³tu figury "+((System.nanoTime()-time)/1000000)+"ms");
		shapeTime = ((System.nanoTime()-time)/1000000);
		time = System.nanoTime();
		PolygonFillSercherNEW pfs = new PolygonFillSercherNEW();
		System.out.println("ROZPOCZYNANIE WYSZUKIWANIA KSZTA£TU");
		FilledPolygonWithCurves fp = null;
		if(connTab!=null)
			fp = pfs.findFillInPolygon(f, f.gco().getPointTab(), f.gco().getConnectionTab(), connTab);
		System.out.println("Zakoñczono Wyszukiwanie Wype³nienia "+((System.nanoTime()-time)/1000000)+"ms");
		fillTime = ((System.nanoTime()-time)/1000000);
		
//		if(fp!=null)
//		{
//			fp.c = pb.fillColor;
//			PastBox.addStepStart();
//			fp.writePolygon();
//			f.gco().addPolygon(fp);
//			frameCupture.hasFrame = false;
//		}
		
		if(fp!=null)
		{
			time = System.nanoTime();

			fp.c = pb.fillColor;

				PastBox.addStepStart();
				PastBox.eventAddPolygon(StateBox.frameIndex, StateBox.objIndex, f.gco().getPolygonTab().size());
				f.gco().getPolygonTab().add(fp);

				
			frameCupture.hasFrame = false;
			System.out.println("Dodawnie wype³nienia "+ f.gco().getPolygonTab().size());
			compareTime = ((System.nanoTime()-time)/1000000);
		}
		
		}
		else
		{
			System.out.println("Zmiana koloru");
			f.gco().getPolygon(t).c = pb.fillColor;
			frameCupture.hasFrame = false;
			System.out.println("Zmiana koloru "+ f.gco().getPolygonTab().size());
			compareTime = ((System.nanoTime()-time)/1000000);
		}
		time = System.nanoTime();
		g.setColor(Color.black);
		TextBox.addMessage("Zakoñczono generowanie wype³nianie w czasie "+(shapeTime+fillTime)+"ms (jednorazowo) sztaby: "+stabTime+"ms  kszta³t: "+shapeTime+"ms  wype³nienia: "+fillTime+"ms  porównania: "+compareTime+"ms ", 500);
		}
		
		if(!MouseClicked&&!MouseRightClicked)
		{
			clicked = false;
		}
	}
	
	public static int getIndexOfFilledPolygon(Frame f, float x, float y)
	{
		//System.out.println("szukanie");
		for(int i = 0; i < f.gco().getPolygonTab().size(); i++)
		{
			if(f.gco().getPolygon(i).isClicked(f, x, y))
			{
				System.out.println(i);
				return i;
			}
		}
		return -10;
	}
	
	public static int getIndexOfFilledPolygon(Frame f, float x, float y,Graphics2D g)
	{
		double time = System.nanoTime();
		int polNum = -10;
		if(lastGenerated != f.getObject())
		{
			System.out.println("Resetowanie FillMapy "+lastGenerated);
			needToRefresh = true;
			f.getObj(lastGenerated).setFm(new FillMap());	
		}
		if(needToRefresh)
		{
			lastGenerated = f.getObject();
			f.gco().getFm().reGenerateAllMap(f, f.gco().getConnectionTab(), f.gco().getPointTab(), g);
			needToRefresh = false;
			//System.out.println("Zakoñczono generowanie sztab "+((System.nanoTime()-time)/1000000)+"ms");
			stabTime = ((System.nanoTime()-time)/1000000);
			TextBox.addMessage("Zakoñczono generowanie sztab w czasie "+stabTime+"ms", 300);
		}

		float[] XY = f.gco().scaleAndRotateValue(x, y);
		x = XY[0];
		y = XY[1];


		
		//stabTime = 0;
		
		time = System.nanoTime();
		PolygonShapeGenerator psg = new PolygonShapeGenerator();
		ArrayList<Integer> connTab = psg.getIncludedConnectiones(f, f.gco().getFm().getSt(), x, y, g);
		
		System.out.println("Zakoñczono Wyszukiwanie kszta³tu figury "+((System.nanoTime()-time)/1000000)+"ms");
		shapeTime = ((System.nanoTime()-time)/1000000);
		time = System.nanoTime();
		PolygonFillSercherNEW pfs = new PolygonFillSercherNEW();
		System.out.println("ROZPOCZYNANIE WYSZUKIWANIA KSZTA£TU");
		FilledPolygonWithCurves fp = null;
		if(connTab!=null)
			fp = pfs.findFillInPolygon(f, f.gco().getPointTab(), f.gco().getConnectionTab(), connTab);
		System.out.println("Zakoñczono Wyszukiwanie Wype³nienia "+((System.nanoTime()-time)/1000000)+"ms");
		fillTime = ((System.nanoTime()-time)/1000000);
		if(fp!=null)
		{
			time = System.nanoTime();
			polNum = compareToExistingPolygonsWithSort(f, fp);
			frameCupture.hasFrame = false;
			//System.out.println("Dodawnie wype³nienia "+ f.gco().getPolygonTab().size());
			compareTime = ((System.nanoTime()-time)/1000000);
		}
		
		
		time = System.nanoTime();
		g.setColor(Color.black);
		TextBox.addMessage("Zakoñczono generowanie wype³nianie w czasie "+(shapeTime+fillTime)+"ms (jednorazowo) sztaby: "+stabTime+"ms  kszta³t: "+shapeTime+"ms  wype³nienia: "+fillTime+"ms  porównania: "+compareTime+"ms ", 500);

		return polNum;
	}
	public static int chceckIfClickedOnPolygon(Frame f, float x, float y)
	{
		int polIndex = -10;
		for(int i = 0; i < f.gco().getPolygonTab().size(); i++)
		{
			if(f.gco().getPolygon(i).isClicked(f, x, y))
			{
				return i;
			}
		}
		return polIndex;
	}
	public static int compareToExistingPolygonsWithSort(Frame f, FilledPolygonWithCurves fp)
	{
		for(int i = 0; i < f.gco().getPolygonTab().size(); i++)
		{
			if(f.gco().getPolygonTab().get(i).shapeTab.size() == fp.shapeTab.size()&&f.gco().getPolygonTab().get(i).connectionIndexTab.size() == fp.connectionIndexTab.size())
			{
				ArrayList<Integer> points1 = new ArrayList<Integer>();
				for(int j = 0; j < f.gco().getPolygonTab().get(i).shapeTab.size(); j++)
				{
					points1.add(f.gco().getPolygonTab().get(i).shapeTab.get(j).getPoint());
					if(f.gco().getPolygonTab().get(i).shapeTab.get(j).isArc)
						points1.add(f.gco().getPolygonTab().get(i).shapeTab.get(j).P3);
					if(f.gco().getPolygonTab().get(i).shapeTab.get(j).isDoubleArc)
						points1.add(f.gco().getPolygonTab().get(i).shapeTab.get(j).P4);
				}
				
				ArrayList<Integer> points2 = new ArrayList<Integer>();
				for(int j = 0; j < fp.shapeTab.size(); j++)
				{
					points2.add(fp.shapeTab.get(j).getPoint());
					if(fp.shapeTab.get(j).isArc)
						points2.add(fp.shapeTab.get(j).P3);
					if(fp.shapeTab.get(j).isDoubleArc)
						points2.add(fp.shapeTab.get(j).P3);
				}
				
				QuickSort.SortIntArray(points1);
				QuickSort.SortIntArray(points2);
				boolean diffrence = false;
				for(int j = 0; j < points1.size(); j++)
				{
					if(points1.get(j)!=points2.get(j))
					{
						diffrence = true;
						break;
					}
				}
				if(!diffrence)
				{
					return i;
				}
			}
			else
			{
				System.out.println("DifrentSize");
			}
		}
		return -10;
	}
	
}
//Sumowanie 
//Je¿eli naka³adamy figurê na inn¹ figurê musimy poci¹æ linie w miejscach przeciêæ a nastêpnie usun¹æ te powsta³e linie
//których oba punkty zawieraj¹ siê w takowej figurze

//Przeciêcia lini bierzemy liniê, sprawdzamy po kolei ze wszystkimi liniami których BoundingBox zachodzi na nasz¹ liniê
//czy maj¹ punkt przeciêcia. nastêpnie kroimy obie linie wzglêdem tego punktu i sprawdzamy nasze dwie pokrojone linie