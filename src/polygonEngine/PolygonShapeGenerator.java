package polygonEngine;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import polygonEngineStructures.Conn;
import polygonEngineStructures.PointLight;
import polygonEngineStructures.StabTab;
import polygonEngineStructures.searchConn;
import structures.Frame;
import toolBox.BinnarySearch;
import toolBox.QuickSort;


public class PolygonShapeGenerator 
{
	public static boolean showMark = true;
	public static boolean onlyMainMark = true;
	private float range = 0.05f;
	public ArrayList<Integer> getIncludedConnectiones(Frame f, StabTab st,  float x, float y, Graphics2D g)
	{
		
		int ind = BinnarySearch.getIndexOfValueStab(st.sTab, x);
		if(ind<0)
			return null;
		int connIndex = BinnarySearch.getIndexOfValueConn(st.sTab.get(ind).cTab, x, y);
		if(connIndex<0)
			return null;
		
			
			
			ArrayList<Conn> conIndex = new ArrayList<Conn>();
			ArrayList<searchConn> sTab = new ArrayList<searchConn>();
			UsedLinesChecker ulc = new UsedLinesChecker(st.vTab.size());
			
			initFirstStab(st, ind, connIndex, conIndex, sTab);
			findShape(st, x, y, sTab, conIndex, ulc);
			
			drawResolts(f, st, g, ind, connIndex, conIndex, sTab);
			ArrayList<Integer> finalTab = convertForListOfConnectiones(conIndex);
		return finalTab;
		
	}
	public boolean isPointIndide(StabTab st,  float x, float y)
	{
		
		int ind = BinnarySearch.getIndexOfValueStab(st.sTab, x);
		if(ind<0)
			return false;
		int connIndex = BinnarySearch.getIndexOfValueConn(st.sTab.get(ind).cTab, x, y);
		if(connIndex<0)
			return false;
		
		if(connIndex%2==0)
			return true;
		else
			return false;
		
	}
	public int[] getIndexOdStabAndIndexOfLines(StabTab st,  float x, float y)
	{
		
		int ind = BinnarySearch.getIndexOfValueStab(st.sTab, x);
		if(ind<0)
			return null;
		int connIndex = BinnarySearch.getIndexOfValueConn(st.sTab.get(ind).cTab, x, y);
		if(connIndex<0)
			return null;
		
		int[] tab = new int[]{ind, connIndex};
		return tab;
		
	}
	private ArrayList<Integer> convertForListOfConnectiones(ArrayList<Conn> conIndex) {
		ArrayList<Integer> finalTab = new ArrayList<Integer>(conIndex.size());
		for(int i = 0; i < conIndex.size(); i++)
		{
			finalTab.add(conIndex.get(i).ConnectionNum);
		}
		QuickSort.SortIntArrayWithRemoveDoubles(finalTab);
//		for(int i = 0; i < finalTab.size(); i++)
//		{
//			System.out.print(finalTab.get(i)+" ");
//		}
//		System.out.println();
		return finalTab;
	}


	private void drawResolts(Frame f, StabTab st, Graphics2D g, int ind, int connIndex, ArrayList<Conn> conIndex,
			ArrayList<searchConn> sTab) {
		if(showMark)
			drawClickedStab(f, st, g, ind);
		if(showMark)
			drawStartConnectiones(f, st, g, ind, connIndex);
		if(showMark)
		{
			drawAddedLines(g, conIndex, f);
			drawSearchLines(g, sTab, f);
		}
		if(onlyMainMark&&!showMark)
			drawAddedLines(g, conIndex, f);
	}


	private void initFirstStab(StabTab st, int ind, int connIndex, ArrayList<Conn> conIndex,
			ArrayList<searchConn> sTab) {
		if(!comparePointLigts(st.sTab.get(ind).cTab.get(connIndex).P1, st.sTab.get(ind).cTab.get(connIndex+1).P1))
			sTab.add(new searchConn(st.sTab.get(ind).cTab.get(connIndex).P1, st.sTab.get(ind).cTab.get(connIndex+1).P1, true, ind));
		if(!comparePointLigts(st.sTab.get(ind).cTab.get(connIndex).P2, st.sTab.get(ind).cTab.get(connIndex+1).P2))
			sTab.add(new searchConn(st.sTab.get(ind).cTab.get(connIndex).P2, st.sTab.get(ind).cTab.get(connIndex+1).P2, false, ind));

		
		conIndex.add(st.sTab.get(ind).cTab.get(connIndex));
		conIndex.add(st.sTab.get(ind).cTab.get(connIndex+1));
	}


	private void findShape(StabTab st, float x, float y, ArrayList<searchConn> sTab, ArrayList<Conn> conIndex, UsedLinesChecker ulc) {
		for(int i = 0; i < sTab.size(); i++)
		{
			searchConn sCon = sTab.get(i);
			if(sCon.P1.y>sCon.P2.y)
			{
				PointLight p1 = new PointLight(sCon.P1.x, sCon.P1.y);
				sCon.P1 = new PointLight(sCon.P2.x, sCon.P2.y);
				sCon.P2 = p1;
			}

			if((Math.abs(sCon.P1.y-sCon.P2.y)>range))
			{
			//System.out.println("Rozpoczynanie odcinka œledzenia nr."+i+" o wspó³êdnych y "+sCon.P1.x+" "+sCon.P1.y+" "+sCon.P2.x+" "+sCon.P2.y);
			if(sCon.left)
			{
				leftCheck(st, conIndex, sCon, sTab, ulc);
			}
			else
			{
				rightCheck(st, conIndex, sCon, sTab, ulc);
			}
			}
			
		}
	}


	private int rightCheck(StabTab st, ArrayList<Conn> conIndex, searchConn sCon, ArrayList<searchConn> sTab, UsedLinesChecker ulc) {
//		System.out.println("PRAWY");
		//TODO
		int stabIndex = sCon.stabIndex+1;
		int indexOfVertex = BinnarySearch.getIndexOfIndexOfVertexConn(st.vTab, sCon.P1.x);
		if(st.vTab.get(indexOfVertex).cTab!=null)
		{
			//System.out.println("Znaleziono Pion");
			ArrayList<Integer> vTab = new ArrayList<Integer>();
			for(int i = 0; i < st.vTab.get(indexOfVertex).cTab.size(); i++)
			{
				Conn vl = st.vTab.get(indexOfVertex).cTab.get(i);
				if(vl.P1.y==sCon.P1.y&&vl.P2.y==sCon.P2.y||vl.P1.y==sCon.P2.y&&vl.P2.y==sCon.P1.y)
				{
					conIndex.add(vl);
					return -10;
				}
				else
				{
					if(vl.P1.y>=sCon.P1.y&&vl.P2.y<=sCon.P2.y)
					{
						conIndex.add(vl);
						vTab.add(i);
					}
				}
			}
			if(vTab.size()>0)
			{
//				System.out.println("Znaleziono "+vTab.size()+" odcinki pod wspó³rzêdn¹ "+st.vTab.get(indexOfVertex).X+" pod indeksem "+indexOfVertex);
//				System.out.print("Odcinek szkuaj¹cy "+sCon.P1.y+" "+sCon.P2.y+" zawiera "+vTab.size()+": ");
//				for(int i = 0; i < vTab.size(); i++)
//				{
//					System.out.print(st.vTab.get(indexOfVertex).cTab.get(vTab.get(i)).P1.y+" "+st.vTab.get(indexOfVertex).cTab.get(vTab.get(i)).P2.y+" / ");
//				}
//				System.out.println();
//				System.out.println("Sortowanie puntków");
				ArrayList<PointLight> LightPointTab = new ArrayList<PointLight>();
				LightPointTab.add(sCon.P1);
				
				for(int i = 0; i < vTab.size(); i++)
				{
					LightPointTab.add(st.vTab.get(indexOfVertex).cTab.get(vTab.get(i)).P1);
					LightPointTab.add(st.vTab.get(indexOfVertex).cTab.get(vTab.get(i)).P2);
				}
				LightPointTab.add(sCon.P2);
				
				QuickSort.SortLightPoint(LightPointTab);
				removeDoubles(LightPointTab);
//				for(int i = 0; i < LightPointTab.size(); i++)
//				{
//					System.out.print(LightPointTab.get(i).y+" ");
//				}
//				System.out.println();
				
				
				if(LightPointTab.size()>2)
				{
				for(int i = 0; i < LightPointTab.size()-1; i++)
				{
					searchConn sc = new searchConn(LightPointTab.get(i), LightPointTab.get(i+1), sCon.left, sCon.stabIndex);
					//System.out.println("Testowanie Ciêtego odcinka odcinka 1 "+sc.P1.y+" "+sc.P2.y);
					for(int j = 0; j < vTab.size(); j++)
					{
						if(st.vTab.get(indexOfVertex).cTab.get(vTab.get(j)).P1.y>st.vTab.get(indexOfVertex).cTab.get(vTab.get(j)).P2.y)
						{
							PointLight p1 = new PointLight(st.vTab.get(indexOfVertex).cTab.get(vTab.get(j)).P1.x, st.vTab.get(indexOfVertex).cTab.get(vTab.get(j)).P1.y);
							st.vTab.get(indexOfVertex).cTab.get(vTab.get(j)).P1 = new PointLight(st.vTab.get(indexOfVertex).cTab.get(vTab.get(j)).P2.x, st.vTab.get(indexOfVertex).cTab.get(vTab.get(j)).P2.y);
							st.vTab.get(indexOfVertex).cTab.get(vTab.get(j)).P2 = p1;
						}
						//System.out.println("Testowanie Ciêtego odcinka odcinka 2 "+sc.P1.y+" "+sc.P2.y+" z pionowym "+st.vTab.get(indexOfVertex).cTab.get(vTab.get(j)).P1.y+" "+st.vTab.get(indexOfVertex).cTab.get(vTab.get(j)).P2.y);
						if(sc.P1.y!=st.vTab.get(indexOfVertex).cTab.get(vTab.get(j)).P1.y&&sc.P2.y!=st.vTab.get(indexOfVertex).cTab.get(vTab.get(j)).P2.y)
						{
							//System.out.println("Testowanie Ciêtego odcinka odcinka 3 "+sc.P1.y+" "+sc.P2.y);
							if(chceckIfLeftRepet(sc, sTab, ulc)==0);
							{
								//System.out.println("Testowanie Ciêtego odcinka odcinka 4 "+sc.P1.y+" "+sc.P2.y);
								//System.out.println("Dodawnie pociêtego odcinka "+sc.P1.y+" "+sc.P2.y);
								sTab.add(sc);
								ulc.addConnIndex(stabIndex, sTab.size()-1);
							}
						}
					}
				}

					return -10;
				}
			}

		}
		
		if(stabIndex<st.sTab.size())
		{
		int indexOfUpperLine = BinnarySearch.getIndexOfValueConnWithPoint(st.sTab.get(stabIndex).cTab, sCon.P1.x, sCon.P1.y);
		int indexOfDownLine = BinnarySearch.getIndexOfValueConnWithPoint(st.sTab.get(stabIndex).cTab, sCon.P2.x, sCon.P2.y);

		if((indexOfDownLine==-10||indexOfUpperLine==-10)==false)
		{	
		if(indexOfDownLine-indexOfUpperLine>1)
		{
			for(int i = 0; i < indexOfDownLine-indexOfUpperLine; i++)
			{
				if(st.sTab.get(stabIndex).cTab.get(indexOfDownLine-i).P1.y==st.sTab.get(stabIndex).cTab.get(indexOfUpperLine).P1.y)
				{
					indexOfUpperLine = indexOfDownLine-i;
				}
			}
			if(indexOfDownLine-indexOfUpperLine>1)
			{
				for(int i = 1; i <= indexOfDownLine-indexOfUpperLine; i++)
				{
					if(st.sTab.get(stabIndex).cTab.get(indexOfUpperLine+i).P1.y==st.sTab.get(stabIndex).cTab.get(indexOfDownLine).P1.y)
					{
						indexOfDownLine = indexOfUpperLine+i;
					}
				}
			}
			
		}
	
		
		if(indexOfDownLine-indexOfUpperLine==1)
		{
			conIndex.add(st.sTab.get(stabIndex).cTab.get(indexOfUpperLine));
			conIndex.add(st.sTab.get(stabIndex).cTab.get(indexOfDownLine));

				if(conIndex.get(conIndex.size()-2).P2.y!= conIndex.get(conIndex.size()-1).P2.y)
				{
					searchConn sc = new searchConn(conIndex.get(conIndex.size()-2).P2, conIndex.get(conIndex.size()-1).P2, false, stabIndex);
					if(checkIfRightRepet(sc, sTab, ulc)==0)
					{
						sTab.add(sc);
						ulc.addConnIndex(stabIndex+1, sTab.size()-1);
					}
				}
				
		}
		else
		{
			
			ArrayList<PointLight> yTab = new ArrayList<PointLight>();
			for(int i = indexOfUpperLine; i <= indexOfDownLine; i++)
			{
				yTab.add(st.sTab.get(stabIndex).cTab.get(i).P1);
			}

		
			
			for(int i = 0; i < yTab.size()-1; i++)
			{
				searchConn sc = new searchConn(yTab.get(i), yTab.get(i+1), sCon.left, sCon.stabIndex);
				sc.isSpecial=true;
				//System.out.println("Dodano odcinek: "+sc.P1.y+" "+sc.P2.y);
				if((Math.abs(sc.P1.y-sc.P2.y)>range))
				{
				if(checkIfRightRepet(sc, sTab, ulc)==0)
				{
					sTab.add(sc);
					ulc.addConnIndex(stabIndex, sTab.size()-1);
				}
				}
			}
		}
		}
		else
		{
			//System.out.println("Procedura odnajdywania specjalnych lini Prawa "+indexOfUpperLine+" "+indexOfDownLine);
			int check = ifBigerSerchLineAutOfRange(st, conIndex, sTab, stabIndex, indexOfUpperLine, indexOfDownLine, ulc);
			if(check<0)
				return -10;
		}	
	}
		return 0;
	}


	private int leftCheck(StabTab st, ArrayList<Conn> conIndex, searchConn sCon, ArrayList<searchConn> sTab, UsedLinesChecker ulc) {
		//TODO
//		System.out.println("LEWY");
		int stabIndex = sCon.stabIndex-1;
		
		int indexOfVertex = BinnarySearch.getIndexOfIndexOfVertexConn(st.vTab, sCon.P1.x);
		if(st.vTab.get(indexOfVertex).cTab!=null)
		{
			//System.out.println("Znaleziono Pion");
			ArrayList<Integer> vTab = new ArrayList<Integer>();
			for(int i = 0; i < st.vTab.get(indexOfVertex).cTab.size(); i++)
			{
				Conn vl = st.vTab.get(indexOfVertex).cTab.get(i);
				if(vl.P1.y==sCon.P1.y&&vl.P2.y==sCon.P2.y||vl.P1.y==sCon.P2.y&&vl.P2.y==sCon.P1.y)
				{
					conIndex.add(vl);
					return -10;
				}
				else
				{
					if(vl.P1.y>=sCon.P1.y&&vl.P2.y<=sCon.P2.y)
					{
						conIndex.add(vl);
						vTab.add(i);
					}
				}
			}
			if(vTab.size()>0)
			{
				//System.out.println("Znaleziono "+vTab.size()+" odcinki pod wspó³rzêdn¹ "+st.vTab.get(indexOfVertex).X+" pod indeksem "+indexOfVertex);
				//System.out.print("Odcinek szkuaj¹cy "+sCon.P1.y+" "+sCon.P2.y+" zawiera "+vTab.size()+": ");
//				for(int i = 0; i < vTab.size(); i++)
//				{
//					System.out.print(st.vTab.get(indexOfVertex).cTab.get(vTab.get(i)).P1.y+" "+st.vTab.get(indexOfVertex).cTab.get(vTab.get(i)).P2.y+" / ");
//				}
//				System.out.println();
//				System.out.println("Sortowanie puntków");
				ArrayList<PointLight> LightPointTab = new ArrayList<PointLight>();
				LightPointTab.add(sCon.P1);
				
				for(int i = 0; i < vTab.size(); i++)
				{
					LightPointTab.add(st.vTab.get(indexOfVertex).cTab.get(vTab.get(i)).P1);
					LightPointTab.add(st.vTab.get(indexOfVertex).cTab.get(vTab.get(i)).P2);
				}
				LightPointTab.add(sCon.P2);
				
				QuickSort.SortLightPoint(LightPointTab);
				removeDoubles(LightPointTab);
//				for(int i = 0; i < LightPointTab.size(); i++)
//				{
//					System.out.print(LightPointTab.get(i).y+" ");
//				}
//				System.out.println();
//				
				if(LightPointTab.size()>2)
				{
				for(int i = 0; i < LightPointTab.size()-1; i++)
				{
					searchConn sc = new searchConn(LightPointTab.get(i), LightPointTab.get(i+1), sCon.left, sCon.stabIndex);
					for(int j = 0; j < vTab.size(); j++)
					{
						if(st.vTab.get(indexOfVertex).cTab.get(vTab.get(j)).P1.y>st.vTab.get(indexOfVertex).cTab.get(vTab.get(j)).P2.y)
						{
							PointLight p1 = new PointLight(st.vTab.get(indexOfVertex).cTab.get(vTab.get(j)).P1.x, st.vTab.get(indexOfVertex).cTab.get(vTab.get(j)).P1.y);
							st.vTab.get(indexOfVertex).cTab.get(vTab.get(j)).P1 = new PointLight(st.vTab.get(indexOfVertex).cTab.get(vTab.get(j)).P2.x, st.vTab.get(indexOfVertex).cTab.get(vTab.get(j)).P2.y);
							st.vTab.get(indexOfVertex).cTab.get(vTab.get(j)).P2 = p1;
						}
						if(sc.P1.y!=st.vTab.get(indexOfVertex).cTab.get(vTab.get(j)).P1.y&&sc.P2.y!=st.vTab.get(indexOfVertex).cTab.get(vTab.get(j)).P2.y)
						{
							if(chceckIfLeftRepet(sc, sTab, ulc)==0);
							{
								//System.out.println("Dodawnie pociêtego odcinka "+sc.P1.y+" "+sc.P2.y);
								sTab.add(sc);
								ulc.addConnIndex(stabIndex, sTab.size()-1);
							}
						}
					}
				}
					return -10;
				}
			}

		}
		
		if(stabIndex>-1)
		{
		int indexOfUpperLine = BinnarySearch.getIndexOfValueConnWithPoint(st.sTab.get(stabIndex).cTab, sCon.P1.x, sCon.P1.y);
		int indexOfDownLine = BinnarySearch.getIndexOfValueConnWithPoint(st.sTab.get(stabIndex).cTab, sCon.P2.x, sCon.P2.y);

		if((indexOfDownLine==-10||indexOfUpperLine==-10)==false)
		{
		if(indexOfDownLine-indexOfUpperLine>1)
		{
			for(int i = 1; i <= indexOfDownLine-indexOfUpperLine; i++)
			{
				if(st.sTab.get(stabIndex).cTab.get(indexOfDownLine-i).P2.y==st.sTab.get(stabIndex).cTab.get(indexOfUpperLine).P2.y)
				{
					indexOfUpperLine = indexOfDownLine-i;
				}
			}
			if(indexOfDownLine-indexOfUpperLine>1)
			{
				for(int i = 1; i <= indexOfDownLine-indexOfUpperLine; i++)
				{
					if(st.sTab.get(stabIndex).cTab.get(indexOfUpperLine+i).P2.y==st.sTab.get(stabIndex).cTab.get(indexOfDownLine).P2.y)
					{
						indexOfDownLine = indexOfUpperLine+i;
					}
				}
			}
		}
		
		if(indexOfDownLine-indexOfUpperLine==1)
		{
			conIndex.add(st.sTab.get(stabIndex).cTab.get(indexOfUpperLine));
			conIndex.add(st.sTab.get(stabIndex).cTab.get(indexOfDownLine));

				if(conIndex.get(conIndex.size()-2).P1.y!= conIndex.get(conIndex.size()-1).P1.y)
				{
					searchConn sc = new searchConn(conIndex.get(conIndex.size()-2).P1, conIndex.get(conIndex.size()-1).P1, true, stabIndex);

					if(chceckIfLeftRepet(sc, sTab, ulc)==0);
					{
						sTab.add(sc);
						ulc.addConnIndex(stabIndex, sTab.size()-1);
					}
					
				}

			
		}
		else
		{
			//System.out.println(x);
			ArrayList<PointLight> yTab = new ArrayList<PointLight>();
			for(int i = indexOfUpperLine; i <= indexOfDownLine; i++)
			{
				yTab.add(st.sTab.get(stabIndex).cTab.get(i).P2);
			}

			for(int i = 0; i < yTab.size()-1; i++)
			{
				searchConn sc = new searchConn(yTab.get(i), yTab.get(i+1), sCon.left, sCon.stabIndex);
				if((Math.abs(sc.P1.y-sc.P2.y)>range))
				{
				if(chceckIfLeftRepet(sc, sTab, ulc)==0)
				{
					sTab.add(sc);
					ulc.addConnIndex(stabIndex, sTab.size()-1);
				}
				}
			}
		}
		}
		else
		{
			//System.out.println("Procedura odnajdywania specjalnych lini Lewa "+indexOfUpperLine+" "+indexOfDownLine);
			int check = ifBigerSerchLineAutOfRange(st, conIndex, sTab, stabIndex, indexOfUpperLine, indexOfDownLine, ulc);
			if(check<0)
				return -10;
		}
		}
		
		return 0;
	}

	private void removeDoubles(ArrayList<PointLight> tab)
	{
		for(int i = 0; i < tab.size()-1; i++)
		{
			if(tab.get(i).y==tab.get(i+1).y)
			{
				tab.remove(i);
				i--;
			}
		}
	}
	
	private int checkIfRightRepet(searchConn sCon, ArrayList<searchConn> sTab, UsedLinesChecker ulc) {
		for(int i = 0; i < ulc.kTab.get(sCon.stabIndex+1).iTab.size(); i++)
		{
		if(sTab.get(ulc.getConnIndex(sCon.stabIndex+1, i)).P1.y==sCon.P1.y&&sTab.get(ulc.getConnIndex(sCon.stabIndex+1, i)).P2.y==sCon.P2.y)
		{
			//System.out.println("Znaleziono powtarzaj¹c¹ siê liniê");
			return -10;
		}
		}
		return 0;
	}

	private int chceckIfLeftRepet(searchConn sCon, ArrayList<searchConn> sTab, UsedLinesChecker ulc) {
		for(int i = 0; i < ulc.kTab.get(sCon.stabIndex).iTab.size(); i++)
		{
		if(sTab.get(ulc.getConnIndex(sCon.stabIndex, i)).P1.y==sCon.P1.y&&sTab.get(ulc.getConnIndex(sCon.stabIndex, i)).P2.y==sCon.P2.y)
		{
			//System.out.println("Znaleziono powtarzaj¹c¹ siê liniê");
			return -10;
		}
		}
		return 0;
	}


	private int ifBigerSerchLineAutOfRange(StabTab st, ArrayList<Conn> conIndex, ArrayList<searchConn> sTab,
			int stabIndex, int indexOfUpperLine, int indexOfDownLine, UsedLinesChecker ulc) {
		//System.out.println("Odnaleziono przeskok ");
		if(indexOfDownLine>-10||indexOfUpperLine>-10)
		{
		//	System.out.println("Szukanie");
			if(indexOfDownLine>-10)
			{
			
				int indexOfSecondLine = indexOfDownLine-1;
				//System.out.println("TU "+indexOfDownLine+" "+indexOfSecondLine);
				if(indexOfDownLine==0)
				{
					//System.out.println("KOniec");
					return -10;
				}
				conIndex.add(st.sTab.get(stabIndex).cTab.get(indexOfSecondLine));
				conIndex.add(st.sTab.get(stabIndex).cTab.get(indexOfDownLine));
				
				
				
				
				
				if(conIndex.get(conIndex.size()-2).P1.y!= conIndex.get(conIndex.size()-1).P1.y)
				{
					searchConn sc = new searchConn(conIndex.get(conIndex.size()-2).P1, conIndex.get(conIndex.size()-1).P1, true, stabIndex);
					if(chceckIfLeftRepet(sc, sTab, ulc)==0)
					{
					sTab.add(sc);
					ulc.addConnIndex(stabIndex, sTab.size()-1);
					sTab.get(sTab.size()-1).isSpecial = true;
					}
				}
				if(conIndex.get(conIndex.size()-2).P2.y!= conIndex.get(conIndex.size()-1).P2.y)
				{
					searchConn sc2 = new searchConn(conIndex.get(conIndex.size()-2).P2, conIndex.get(conIndex.size()-1).P2, false, stabIndex);
					if(checkIfRightRepet(sc2, sTab, ulc)==0)
					{
					sTab.add(sc2);
					ulc.addConnIndex(stabIndex+1, sTab.size()-1);
					sTab.get(sTab.size()-1).isSpecial = true;
					}
				}
				
				
			}
			if(indexOfUpperLine>-10)
			{
				int indexOfSecondLine = indexOfUpperLine+1;
				if(indexOfSecondLine>=st.sTab.get(stabIndex).cTab.size())
				{
					return -10;
				}
				conIndex.add(st.sTab.get(stabIndex).cTab.get(indexOfUpperLine));
				conIndex.add(st.sTab.get(stabIndex).cTab.get(indexOfSecondLine));

				
				if(conIndex.get(conIndex.size()-2).P1.y!= conIndex.get(conIndex.size()-1).P1.y)
				{
					searchConn sc = new searchConn(conIndex.get(conIndex.size()-2).P1, conIndex.get(conIndex.size()-1).P1, true, stabIndex);
					if(chceckIfLeftRepet(sc, sTab, ulc)==0)
					{
					sTab.add(sc);
					ulc.addConnIndex(stabIndex, sTab.size()-1);
					sTab.get(sTab.size()-1).isSpecial = true;
					}
				}
				if(conIndex.get(conIndex.size()-2).P2.y!= conIndex.get(conIndex.size()-1).P2.y)
				{
					searchConn sc2 = new searchConn(conIndex.get(conIndex.size()-2).P2, conIndex.get(conIndex.size()-1).P2, false, stabIndex);
					if(checkIfRightRepet(sc2, sTab, ulc)==0)
					{
					sTab.add(sc2);
					ulc.addConnIndex(stabIndex+1, sTab.size()-1);
					sTab.get(sTab.size()-1).isSpecial = true;
					}
				}
				
			
			}
		}
		else
		{
		//TODO TU nale¿y dodaæ sprawdzanie na lewo i prawo je¿eli nie 
		return -10;
		}
		return 0;
	}

	
	private void drawAddedLines(Graphics2D g, ArrayList<Conn> conIndex, Frame f) 
	{
		g.setStroke(new BasicStroke(5));
		g.setColor(Color.red);
		for(int i = 0; i < conIndex.size(); i++)
		{
			conIndex.get(i).draw(f, g);
		}
		
	}


	private boolean comparePointLigts(PointLight P1, PointLight P2)
	{
		if(P1.x==P2.x)
		{
			if(P1.y==P2.y)
			{
				//System.out.println("IDNETYCZNE PUNKTY");
				return true;
			}
		}
		//System.out.println(P1.x+" "+P1.y+" "+P2.x+" "+P2.y);
		return false;
	}
	
	
	
	private void drawSearchLines(Graphics2D g, ArrayList<searchConn> sTab, Frame f) 
	{
		g.setStroke(new BasicStroke(2));
		for(int i = 0; i < sTab.size(); i++)
		{
			sTab.get(i).draw(f, g);
		}
	}

	private void drawStartConnectiones(Frame f, StabTab st, Graphics2D g, int ind, int connIndex) {
		//System.out.println("Index po³¹czenia to "+connIndex );
		g.setStroke(new BasicStroke(6));
		g.setColor(Color.red);
		g.draw(new Line2D.Float(f.gco().scaleValue(st.sTab.get(ind).cTab.get(connIndex).P1.x, st.sTab.get(ind).cTab.get(connIndex).P1.y)[0],
				f.gco().scaleValue(st.sTab.get(ind).cTab.get(connIndex).P1.x, st.sTab.get(ind).cTab.get(connIndex).P1.y)[1],
				f.gco().scaleValue(st.sTab.get(ind).cTab.get(connIndex).P2.x, st.sTab.get(ind).cTab.get(connIndex).P2.y)[0],
				f.gco().scaleValue(st.sTab.get(ind).cTab.get(connIndex).P2.x, st.sTab.get(ind).cTab.get(connIndex).P2.y)[1]));
		connIndex++;
		g.draw(new Line2D.Float(f.gco().scaleValue(st.sTab.get(ind).cTab.get(connIndex).P1.x, st.sTab.get(ind).cTab.get(connIndex).P1.y)[0],
				f.gco().scaleValue(st.sTab.get(ind).cTab.get(connIndex).P1.x, st.sTab.get(ind).cTab.get(connIndex).P1.y)[1],
				f.gco().scaleValue(st.sTab.get(ind).cTab.get(connIndex).P2.x, st.sTab.get(ind).cTab.get(connIndex).P2.y)[0],
				f.gco().scaleValue(st.sTab.get(ind).cTab.get(connIndex).P2.x, st.sTab.get(ind).cTab.get(connIndex).P2.y)[1]));
		connIndex--;
		
	}

	private void drawClickedStab(Frame f, StabTab st, Graphics2D g, int ind) {
		//System.out.println("Najechano mysz¹ na sztabê"+ind+" która zawiera "+st.sTab.get(ind).cTab.size());
		g.setColor(Color.green);
		g.draw(new Line2D.Float(f.gco().scaleValue(st.sTab.get(ind).X1, 0)[0], 0, f.gco().scaleValue(st.sTab.get(ind).X1, 0)[0], 6000));
		g.draw(new Line2D.Float(f.gco().scaleValue(st.sTab.get(ind).X2, 0)[0], 0, f.gco().scaleValue(st.sTab.get(ind).X2, 0)[0], 6000));
	}
}
