package polygonEngine;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import polygonEngineStructures.Conn;
import polygonEngineStructures.StabTab;
import polygonEngineStructures.VertexLine;
import renderSource.Connection;
import renderSource.Point;
import structures.Frame;
import toolBox.BinnarySearch;
import toolBox.QuickSort;
import toolBox.tb;

public class FillMap 
{
	private StabTab st = new StabTab();
	public static boolean showMark = true;
	public int reGenerateAllMap(Frame f, ArrayList<Connection> cTab, ArrayList<Point> pTab, Graphics2D g)
	{
		st = new StabTab();
		if(cTab.size()<1)
			return -1;
		g.setStroke(new BasicStroke(1));
		g.setColor(Color.black);
		
		ArrayList<Float> tabX = new ArrayList<Float>();
		regenerateStabTab(f, cTab, pTab, tabX, g);
		ArrayList<Conn> connTab = new ArrayList<Conn>();
		generateConnTab(cTab, pTab, connTab);
		//System.out.println("Przekonwertowano wszystkie odcinki na Conn "+cTab.size()+" "+connTab.size());
		QuickSort.SortConnectionArray(connTab);
		fillReGeneratedMapWithCuttedConnectiones(tabX, connTab);
		sortAllStabs();
		//PrintNumerOfConnInStabs();
		//System.out.println("Zakoñczono wype³nianie struktury");
		return 0;
	}
	public int reGenerateAllMapNoDrawing(Frame f, ArrayList<Connection> cTab, ArrayList<Point> pTab)
	{
		st = new StabTab();
		if(cTab.size()<1)
			return -1;

		
		ArrayList<Float> tabX = new ArrayList<Float>();
		regenerateStabTabNoDrawing(f, cTab, pTab, tabX);
		ArrayList<Conn> connTab = new ArrayList<Conn>();
		generateConnTab(cTab, pTab, connTab);
		//System.out.println("Przekonwertowano wszystkie odcinki na Conn "+cTab.size()+" "+connTab.size());
		QuickSort.SortConnectionArray(connTab);
		fillReGeneratedMapWithCuttedConnectiones(tabX, connTab);
		sortAllStabs();
		//PrintNumerOfConnInStabs();
		//System.out.println("Zakoñczono wype³nianie struktury");
		return 0;
	}
	public int GenerateSmallMapOfPolygonNoDrawing(Frame f, ArrayList<Connection> cTab, ArrayList<Point> pTab, ArrayList<Integer> includedConns)
	{
		st = new StabTab();
		if(cTab.size()<1)
			return -1;

		
		ArrayList<Float> tabX = new ArrayList<Float>();
		regenerateStabTabNoDrawingWithIncludedConns(f, cTab, pTab, tabX, includedConns);
		ArrayList<Conn> connTab = new ArrayList<Conn>();
		generateConnTabFromIncludedConns(cTab, pTab, connTab, includedConns);
		//System.out.println("Przekonwertowano wszystkie odcinki na Conn "+cTab.size()+" "+connTab.size());
		QuickSort.SortConnectionArray(connTab);
		fillReGeneratedMapWithCuttedConnectiones(tabX, connTab);
		sortAllStabs();
		//PrintNumerOfConnInStabs();
		//System.out.println("Zakoñczono wype³nianie struktury");
		return 0;
	}
	private void sortAllStabs() {
		for(int i = 0; i < st.sTab.size(); i++)
		{
			QuickSort.SortConnectionArray(st.sTab.get(i).cTab);
		}
	}

	@SuppressWarnings("unused")
	private void PrintNumerOfConnInStabs() {
		for(int i = 0; i < st.sTab.size(); i++)
		{
			System.out.println("Iloœæ odcinków w sztabie nr."+i+" "+st.sTab.get(i).cTab.size()+" "+st.sTab.get(i).X1+" do "+st.sTab.get(i).X2);
		}
		
		for(int i = 0; i < st.vTab.size(); i++)
		{
			if(st.vTab.get(i).cTab==null)
				System.out.println(st.vTab.get(i).X+" 0");
			else
				System.out.println(st.vTab.get(i).X+" "+st.vTab.get(i).cTab.size());
		}
	}

	public float raundTo0_001(float a)
	{
		a*=1000;
		a = Math.round(a);
		a/=1000;
		return a;
	}
	
	private void fillReGeneratedMapWithCuttedConnectiones(
			ArrayList<Float> tabX, ArrayList<Conn> connTab) {
		for(int i = 0; i < connTab.size(); i++)
		{
			if(connTab.get(i).P1.x!=connTab.get(i).P2.x)
			{
			float valueS = connTab.get(i).P1.x;
			int indexS = BinnarySearch.getIndexOfValueFloat(tabX, valueS); //Od pierwszej lewej krawêdzi
			float valueE = connTab.get(i).P2.x;
			int indexE = BinnarySearch.getIndexOfValueFloat(tabX, valueE);//Do drugiej prawej krawêdzi
			if(indexS>indexE)
			{
				int tem = indexE;
				indexE = indexS;
				indexS = tem;
			}
			float pat[] = tb.generatePatternAB(connTab.get(i).P1.x, connTab.get(i).P1.y, connTab.get(i).P2.x, connTab.get(i).P2.y);
			//System.out.println("Poszukiwany odcinek c znajduje siê pomiêdzy "+indexS+" a "+indexE+" czyli wartoœciami "+valueS+" / "+valueE);
			for(int j = indexS; j < indexE; j++)
			{
				float XY1[] = tb.getCrossOfLineX(pat[0], pat[1], st.sTab.get(j).X1);
				float XY2[] = tb.getCrossOfLineX(pat[0], pat[1], st.sTab.get(j).X2);
				//System.out.println("Ciêcie odcinka nr."+i+" "+ XY1[0]+" "+XY1[1]+" / "+XY2[0]+" "+XY2[1]);
				st.sTab.get(j).cTab.add(new Conn(raundTo0_001(XY1[0]), raundTo0_001(XY1[1]), raundTo0_001(XY2[0]), raundTo0_001(XY2[1]), connTab.get(i).ConnectionNum));
				st.sTab.get(j).cTab.get(st.sTab.get(j).cTab.size()-1).generateMedY();
			}
			}
			else
			{
				//System.out.println("Pionowy Odcinek CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC");
				float value = connTab.get(i).P1.x;
				connTab.get(i).P2.x = value;
				int index = BinnarySearch.getIndexOfValueFloat(tabX, value);
				//System.out.println("Pionowy odcinek na ³¹czeniu nr "+index);
				st.addVertexLine(index, connTab.get(i));
			}
			
		}
	}

	private void generateConnTab(ArrayList<Connection> cTab,
			ArrayList<Point> pTab, ArrayList<Conn> connTab) {
		for(int i = 0; i < cTab.size(); i++)
		{
			Connection c = cTab.get(i);
			
			if(pTab.get(c.getP1()).x>pTab.get(c.getP2()).x)
			{
				connTab.add(new Conn(raundTo0_001(pTab.get(c.getP2()).x), raundTo0_001(pTab.get(c.getP2()).y), raundTo0_001(pTab.get(c.getP1()).x), raundTo0_001(pTab.get(c.getP1()).y), i));
				connTab.get(connTab.size()-1).generateMedY();
			}
			else 
			{
				connTab.add(new Conn(raundTo0_001(pTab.get(c.getP1()).x), raundTo0_001(pTab.get(c.getP1()).y), raundTo0_001(pTab.get(c.getP2()).x), raundTo0_001(pTab.get(c.getP2()).y), i));
				connTab.get(connTab.size()-1).generateMedY();
			}
		}
	}
	private void generateConnTabFromIncludedConns(ArrayList<Connection> cTab,
			ArrayList<Point> pTab, ArrayList<Conn> connTab, ArrayList<Integer> includedConns) {
		for(int i = 0; i < includedConns.size(); i++)
		{
			Connection c = cTab.get((int)includedConns.get(i));
			
			if(pTab.get(c.getP1()).x>pTab.get(c.getP2()).x)
			{
				connTab.add(new Conn(raundTo0_001(pTab.get(c.getP2()).x), raundTo0_001(pTab.get(c.getP2()).y), raundTo0_001(pTab.get(c.getP1()).x), raundTo0_001(pTab.get(c.getP1()).y), i));
				connTab.get(connTab.size()-1).generateMedY();
			}
			else 
			{
				connTab.add(new Conn(raundTo0_001(pTab.get(c.getP1()).x), raundTo0_001(pTab.get(c.getP1()).y), raundTo0_001(pTab.get(c.getP2()).x), raundTo0_001(pTab.get(c.getP2()).y), i));
				connTab.get(connTab.size()-1).generateMedY();
			}
		}
	}
	private void regenerateStabTab(Frame f, ArrayList<Connection> cTab,
			ArrayList<Point> pTab, ArrayList<Float> tabX, Graphics2D g) {
		
		for(int i = 0; i < cTab.size(); i++)
		{
			tabX.add(raundTo0_001(pTab.get(cTab.get(i).getP1()).x));
			tabX.add(raundTo0_001(pTab.get(cTab.get(i).getP2()).x));
		}
		QuickSort.SortFloatArrayWithRemoveDoubles(tabX);
		if(showMark)
		drawVerticalLines(g, f, tabX);
		for(int i = 0; i < tabX.size()-1; i++)
		{
			st.addStab(tabX.get(i), tabX.get(i+1));
			st.vTab.add(new VertexLine(tabX.get(i)));
		}
		st.vTab.add(new VertexLine(tabX.get(tabX.size()-1)));
		st.SX = tabX.get(0);
		st.EX = tabX.get(tabX.size()-1);
		//System.out.println("Zakoñczono generowanie listy sztab krawêdzie to "+st.SX+" "+st.EX);
		//System.out.println("Rozpoczêto wype³nianie sisty sztab");
		
//		for(int i = 0; i< tabX.size(); i++)
//		{
//			System.out.print(tabX.get(i)+" ");
//		}
//		System.out.println();
		
	}
	
	private void regenerateStabTabNoDrawing(Frame f, ArrayList<Connection> cTab,
			ArrayList<Point> pTab, ArrayList<Float> tabX) {
		
		for(int i = 0; i < cTab.size(); i++)
		{
			tabX.add(raundTo0_001(pTab.get(cTab.get(i).getP1()).x));
			tabX.add(raundTo0_001(pTab.get(cTab.get(i).getP2()).x));
		}
		QuickSort.SortFloatArrayWithRemoveDoubles(tabX);

		for(int i = 0; i < tabX.size()-1; i++)
		{
			st.addStab(tabX.get(i), tabX.get(i+1));
			st.vTab.add(new VertexLine(tabX.get(i)));
		}
		st.vTab.add(new VertexLine(tabX.get(tabX.size()-1)));
		st.SX = tabX.get(0);
		st.EX = tabX.get(tabX.size()-1);
		//System.out.println("Zakoñczono generowanie listy sztab krawêdzie to "+st.SX+" "+st.EX);
		//System.out.println("Rozpoczêto wype³nianie sisty sztab");
		
//		for(int i = 0; i< tabX.size(); i++)
//		{
//			System.out.print(tabX.get(i)+" ");
//		}
//		System.out.println();
		
	}
	
	
	private void regenerateStabTabNoDrawingWithIncludedConns(Frame f, ArrayList<Connection> cTab,
			ArrayList<Point> pTab, ArrayList<Float> tabX, ArrayList<Integer> includedConns) {
		
		for(int i = 0; i < includedConns.size(); i++)
		{
			tabX.add(raundTo0_001(pTab.get(cTab.get((int)includedConns.get(i)).getP1()).x));
			tabX.add(raundTo0_001(pTab.get(cTab.get((int)includedConns.get(i)).getP2()).x));
		}
		QuickSort.SortFloatArrayWithRemoveDoubles(tabX);

		for(int i = 0; i < tabX.size()-1; i++)
		{
			st.addStab(tabX.get(i), tabX.get(i+1));
			st.vTab.add(new VertexLine(tabX.get(i)));
		}
		st.vTab.add(new VertexLine(tabX.get(tabX.size()-1)));
		st.SX = tabX.get(0);
		st.EX = tabX.get(tabX.size()-1);
		//System.out.println("Zakoñczono generowanie listy sztab krawêdzie to "+st.SX+" "+st.EX);
		//System.out.println("Rozpoczêto wype³nianie sisty sztab");
		
//		for(int i = 0; i< tabX.size(); i++)
//		{
//			System.out.print(tabX.get(i)+" ");
//		}
//		System.out.println();
		
	}
	
	
	private void drawVerticalLines(Graphics2D g, Frame f, ArrayList<Float> tabX)
	{
		for(int i = 0; i < tabX.size(); i++)
		{
			g.draw(new Line2D.Float(f.gco().scaleValue(tabX.get(i), 0)[0], 0, f.gco().scaleValue(tabX.get(i), 0)[0], 6000));
			g.drawString(""+tabX.get(i), f.gco().scaleValue(tabX.get(i), 0)[0], 100+10*i);
		}
	}
	
	
	public StabTab getSt() {
		return st;
	}

	public void setSt(StabTab st) {
		this.st = st;
	}

}
