package insertionManager;

import java.util.ArrayList;

import polygonEngine.FillMap;
import polygonEngine.FilledPolygonWithCurves;
import polygonEngine.PolygonShapeGenerator;
import printOutSchems.ListsPrinter;
import renderSource.Connection;
import structures.Frame;
import supportingStructures.PolygonInfoBackup;
import toolBox.TextBox;

public class PolygonInsertion 
{
	public static void polygonInsertionStructureModyfication(Frame f, int polygonIndex)
	{
		//BitmapCapturer.renderBitmap(f);
		System.out.println("Rozpocz�to nak�adanie polygon�w pocz�tkowa liczba punkt�w "+f.gco().getPointTabSize());
		double time = System.nanoTime();
		System.out.println("SZUKANIE INYCH POLYGON�W");
		
		FillMap fm = new FillMap();
		ArrayList<Integer> includedConns = f.gco().getPolygon(polygonIndex).getShapeConnIndexes();
		fm.GenerateSmallMapOfPolygonNoDrawing(f, f.gco().getConnectionTab(), f.gco().getPointTab(), includedConns);
		polygonInOnOtherPolygons(f, polygonIndex, fm);
		
		if(isOnTop(f, polygonIndex))
			polygonIsOnTop(f, polygonIndex, fm);
		else
			polygonIsUnder(f, polygonIndex, fm);
		
		TextBox.addMessage(("Zako�czono nak�adanie polygona "+((System.nanoTime()-time)/1000000)+"ms "), 500);
		System.out.println("Zako�czono nak�adanie polygon�w ko�cowa liczba punkt�w "+f.gco().getPointTabSize());
	}

	

	private static boolean isOnTop(Frame f, int polygonIndex) {
		for(int i : f.gco().getPolygonToDraw())
		{
			if(i == polygonIndex)
				return true;
		}
		return false;
	}

	private static void polygonIsOnTop(Frame f, int polygonIndex, FillMap fm) {
		FilledPolygonWithCurves p = f.gco().getPolygon(polygonIndex);
		p.writePolygon();
		//ArrayList<Integer> conIndex = p.connectionIndexTab;
		ArrayList<Integer> ponIndex = p.getPoints(f.gco().getPointTab(), f.gco().getConnectionTab());
		
		boolean usedTab[] = new boolean[f.gco().getPointTabSize()];
		
		for(int i : ponIndex)
			usedTab[i] = true;
		
		f.gco().removeConnectionMark();
		f.gco().removePointMark();
		System.out.println("Zako�czono onajdywanie urzywanych punkt�w");
		//ArrayList<Integer> pointsToRemove = p.getPointsInPolygon(f, usedTab);

		PolygonShapeGenerator psg = new PolygonShapeGenerator();
		ArrayList<Integer> pointsToRemove = new ArrayList<Integer>();
		//System.out.println("Wygenerowano struktur�");
		for(int i = 0; i < f.gco().getPointTabSize(); i++)
		{
			if(psg.isPointIndide(fm.getSt(), f.gco().getNormalPoint(i).x, f.gco().getNormalPoint(i).y))
			{
				if(usedTab[i]==false)
				{
					pointsToRemove.add(i);
				}
			}
		}
		//System.out.println("Zako�cono");
		for(int i : pointsToRemove)
		{
			//System.out.print(i+" ");
			f.gco().getNormalPoint(i).MarkedPoint = true;
		}
		//System.out.println();
		f.gco().removePointsNotSorted(pointsToRemove);
		
		p.writePolygon();
		
		ArrayList<Integer> connsToRem = p.getIndexesOfConnsInsidePolygonToRemove(f, fm);
		
//		for(int i : connsToRem)
//			f.gco().getCon(i).setMarked(true);
		f.gco().removeListOfConnsSorted(connsToRem);
		
		f.gco().setPolygon(polygonIndex, p);
	}
	
	
	
	private static void polygonIsUnder(Frame f, int polygonIndex, FillMap fm) 
	{
		FilledPolygonWithCurves p = f.gco().getPolygon(polygonIndex);
		p.writePolygon();

		//ArrayList<Integer> conIndex = p.connectionIndexTab;
		ArrayList<Integer> ponIndex = p.getPoints(f.gco().getPointTab(), f.gco().getConnectionTab());
		
		boolean usedTab[] = new boolean[f.gco().getPointTabSize()];
		
		for(int i : ponIndex)
			usedTab[i] = true;
		
		f.gco().removeConnectionMark();
		f.gco().removePointMark();
		System.out.println("Zako�czono onajdywanie urzywanych punkt�w");
		//ArrayList<Integer> pointsToRemove = p.getPointsInPolygon(f, usedTab);
		
		PolygonShapeGenerator psg = new PolygonShapeGenerator();
		ArrayList<Integer> pointsToAdd = new ArrayList<Integer>();
		//System.out.println("Wygenerowano struktur�");
		for(int i = 0; i < f.gco().getPointTabSize(); i++)
		{
			if(psg.isPointIndide(fm.getSt(), f.gco().getNormalPoint(i).x, f.gco().getNormalPoint(i).y))
			{
				if(usedTab[i]==false)
				{
					pointsToAdd.add(i);
				}
			}
		}
		//System.out.println("Zako�cono");
		for(int i : pointsToAdd)
		{
			//System.out.print(i+" ");
			f.gco().getNormalPoint(i).MarkedPoint = true;
		}
		//System.out.println();
		//f.gco().removePointsNotSorted(pointsToAdd);
		
		ArrayList<Integer> connsToRem = p.getIndexesOfConnsInsidePolygonToRemove(f, fm);
		
		
		for(int i = 0; i < f.gco().getConnectionTabSize(); i++)
		{
			if(f.gco().getNormalPoint(f.gco().getCon(i).getP1()).MarkedPoint||f.gco().getNormalPoint(f.gco().getCon(i).getP2()).MarkedPoint)
				connsToRem.add(i);
		}
		p.writePolygon();
		
		for(int i : connsToRem)
			p.addNewConnToList(i);
		
//		for(int i : connsToRem)
//			f.gco().getCon(i).setMarked(true);
		
		for(int i : pointsToAdd)
		{
			//System.out.print(i+" ");
			f.gco().getNormalPoint(i).MarkedPoint = false;
		}
		f.gco().setPolygon(polygonIndex, p);
	}
	//TODO
	
	
	//TODO
	private static void polygonInOnOtherPolygons(Frame f, int polygonIndex, FillMap polygonFM)
	{
		f.gco().removeConnectionMark();
		f.gco().removePointMark();
		FilledPolygonWithCurves topPolygon = f.gco().getPolygon(polygonIndex);
		topPolygon.generateMinMax(f.gco().getPointTab());
		ArrayList<Integer> pointsInTopPolygon = topPolygon.getPoints(f.gco().getPointTab(), f.gco().getConnectionTab());
		boolean usedPointsInTopPolygon[] = new boolean[f.gco().getPointTabSize()];
		for(int i : pointsInTopPolygon)
			usedPointsInTopPolygon[i] = true;
		
		ArrayList<Integer> insertedOtherPolygons = new ArrayList<Integer>();
		ArrayList<PolygonInfoBackup> editedPolygonsInfoBackup = new ArrayList<PolygonInfoBackup>();
		
		for(int i = 0; i < f.gco().getPolygonTab().size(); i++)
		{
			if(i == polygonIndex)
				continue;
			if(f.gco().getPolygon(i).checkIfThisPolygonInsertsWithSecond(f, topPolygon))
				insertedOtherPolygons.add(i);
		}
		
		System.out.println("Lista indeks�w polygon�w kt�re nak�adaj� si� na topowy polygon");
		ListsPrinter.printArrayListWithInt(insertedOtherPolygons);
		
		PolygonShapeGenerator psg = new PolygonShapeGenerator();
		
		boolean tabOfConnsInTopPolygon[] = new boolean[f.gco().getConnectionTabSize()];
		
		for(int i : topPolygon.connectionIndexTab)
			tabOfConnsInTopPolygon[i] = true;
		
		for(int k = insertedOtherPolygons.size()-1; k > -1; k--)
		{
			otherPolygonInsertion(f, polygonFM, pointsInTopPolygon, usedPointsInTopPolygon, insertedOtherPolygons, editedPolygonsInfoBackup, psg, k, tabOfConnsInTopPolygon);
		}
		PolygonGeneratorFromPolygonBackUp pg = new PolygonGeneratorFromPolygonBackUp();
		editInsertedPolygons(f, editedPolygonsInfoBackup, pg);
	}



	private static void editInsertedPolygons(Frame f, ArrayList<PolygonInfoBackup> editedPolygonsInfoBackup,
			PolygonGeneratorFromPolygonBackUp pg) {
		for(PolygonInfoBackup pb : editedPolygonsInfoBackup)
		{
			FilledPolygonWithCurves tp = pg.generateFilledPolygon(f, pb);
			System.out.println("Wygenerowano nowy kszta�t polygona nr "+pb.polygonIndex);
			tp.writePolygon();
			tp.c = f.gco().getPolygon(pb.polygonIndex).c;
			f.gco().setPolygon(pb.polygonIndex, tp);
		}
	}

	
	/**
	 * Kroki algorytmu
	 * (Polygon 1 - ten na wierzchu, Polygon 2 - ten kt�ry zostanie zakryty)
	 * 1.Odnajdz wszystkie punkty nale��ce do Polygona 2 kt�re znajduj� si� wewn�trz Polygona 1 i za razem nie nale�� do niego zapisz je jako PUNKTY WEWN�TRZE
	 * 2.Odnajdz wszystkie odcinki kt�re nale�� do Polygona 2 i s� po��czone z przynajmniej jednym punktem PUNKTEM WEWN�TRZNYM i zapisz je jako ODCINKI WEWN�TRZNE
	 * 3.Usu� ODCINKI WEWN�TRZNE
	 * 4.Usu� PUNKTY WEWN�TRZNE
	 * 5.Do listy punkt�w nowego kszta�tu trzeba doda� wszystkie punkty kt�re nale�� do Polygona 1 ale nie s� oznaczone jako PUNKTY WEWN�TRZNE
	 * 6.Sprawdz czy w Polygonie 2 nie wyst�puj� odcinki wewn�trz polygona ale nie zawierajace punkt�w wewn�trz niego
	 * @param f
	 * @param polygonFM
	 * @param pointsInTopPolygon
	 * @param usedPointsInTopPolygon
	 * @param insertedOtherPolygons
	 * @param editedPolygonsInfoBackup
	 * @param psg
	 * @param k
	 */
	private static void otherPolygonInsertion(Frame f, FillMap polygonFM, ArrayList<Integer> pointsInTopPolygon,
			boolean[] usedPointsInTopPolygon, ArrayList<Integer> insertedOtherPolygons,
			ArrayList<PolygonInfoBackup> editedPolygonsInfoBackup, PolygonShapeGenerator psg, int k, boolean tabOfConnsInTopPolygon[]) {
		
		boolean needToedit = false;
		
		f.gco().removeConnectionMark();
		f.gco().removePointMark();
	
		PolygonInfoBackup editedPolygonBackUp = new PolygonInfoBackup(f, f.gco().getPolygon(insertedOtherPolygons.get(k)), insertedOtherPolygons.get(k));
		ArrayList<Integer> editedPolygonPoints = editedPolygonBackUp.listOfPoints;
		ArrayList<Integer> editedPolygonConns = editedPolygonBackUp.listOfConns;
		
		
		ArrayList<Integer> pointsInsideTopPolygon = new ArrayList<Integer>();
		boolean pointsInsideTopPolygonControlTab[] = new boolean[f.gco().getPointTabSize()];
		findAllPointsInsideTopPolygon(f, polygonFM, usedPointsInTopPolygon, psg, editedPolygonPoints, pointsInsideTopPolygon, pointsInsideTopPolygonControlTab);
		System.out.println("Punkty wewn�trz topowego polygona");
		ListsPrinter.printArrayListWithInt(pointsInsideTopPolygon);
		
		if(pointsInsideTopPolygon.size()>0)
			needToedit = true;
		
		ArrayList<Integer> connsConectedToPointsInsideTopPolygon = new ArrayList<Integer>();
		findAndRemoveAllConectionecConnectedToPointsInsideTopPolygon(f, editedPolygonConns, pointsInsideTopPolygonControlTab,	connsConectedToPointsInsideTopPolygon);
		System.out.println("Odcinki po��czone z punktami zakrytymi przez topowy polygon");
		ListsPrinter.printArrayListWithInt(connsConectedToPointsInsideTopPolygon);
		
		removeInsidePoints(editedPolygonPoints, pointsInsideTopPolygonControlTab);
		
		FillMap editedPolygonFillMap = editedPolygonBackUp.fm;
		addPointsFromTopPolygon(f, pointsInTopPolygon, psg, editedPolygonPoints, pointsInsideTopPolygonControlTab, editedPolygonFillMap);
		
		
		boolean tabOfPointsInEditedPolygon[] = new boolean[f.gco().getPointTabSize()];
		boolean tabOfConnsInEditedPolygon[] = new boolean[f.gco().getConnectionTabSize()];
		fillControlTabs(editedPolygonPoints, editedPolygonConns, tabOfPointsInEditedPolygon, tabOfConnsInEditedPolygon);
		
		boolean areThereConns1 = checkForConnsWithOutPointsInPolygon(f, psg, editedPolygonConns, editedPolygonFillMap,
				tabOfPointsInEditedPolygon, tabOfConnsInEditedPolygon);
		
		
		//ListsPrinter.printArrayListWithInt(editedPolygonConns);
		
		boolean areThereConns2 = checkForConnsIndedeTopAndEditedPolygon(f, polygonFM, psg, tabOfConnsInTopPolygon, editedPolygonConns,
				tabOfPointsInEditedPolygon);
		
		//ListsPrinter.printArrayListWithInt(editedPolygonConns);
		
		if(areThereConns1)
			needToedit = true;
		if(areThereConns2)
			needToedit = true;
		
		if(needToedit)
		{
			markShape(f, editedPolygonPoints, editedPolygonConns);
			editedPolygonsInfoBackup.add(editedPolygonBackUp);
		}
		
		
	}



	private static boolean checkForConnsIndedeTopAndEditedPolygon(Frame f, FillMap polygonFM, PolygonShapeGenerator psg,
			boolean[] tabOfConnsInTopPolygon, ArrayList<Integer> editedPolygonConns,
			boolean[] tabOfPointsInEditedPolygon) {
		
		boolean toEdit = false;
		for(int i = 0; i < editedPolygonConns.size(); i++)
		{
			if(!tabOfConnsInTopPolygon[(int)editedPolygonConns.get(i)])
			{
				//System.out.println("Sprawdzanie odzinka nr "+editedPolygonConns.get(i));
				Connection c = f.gco().getCon((int)editedPolygonConns.get(i));
				if(tabOfPointsInEditedPolygon[c.getP1()]&&tabOfPointsInEditedPolygon[c.getP2()])
				{
					float x = (f.gco().getNormalPoint(c.getP1()).x+f.gco().getNormalPoint(c.getP2()).x)/2;
					float y = (f.gco().getNormalPoint(c.getP1()).y+f.gco().getNormalPoint(c.getP2()).y)/2;
					if(psg.isPointIndide(polygonFM.getSt(), x, y))
					{
						//System.out.println("Znaleziono odcinek wewn�trzny "+editedPolygonConns.get(i));
						toEdit = true;
						editedPolygonConns.remove(i);
						i--;
					}
				}
			}
		}
		return toEdit;
	}



	private static boolean checkForConnsWithOutPointsInPolygon(Frame f, PolygonShapeGenerator psg,
			ArrayList<Integer> editedPolygonConns, FillMap editedPolygonFillMap, boolean[] tabOfPointsInEditedPolygon,
			boolean[] tabOfConnsInEditedPolygon) {
		boolean needEdit = false;
		for(int i = 0; i < f.gco().getConnectionTabSize(); i++)
		{
			if(!tabOfConnsInEditedPolygon[i])
			{
				Connection c = f.gco().getCon(i);
				if(tabOfPointsInEditedPolygon[c.getP1()]&&tabOfPointsInEditedPolygon[c.getP2()])
				{
					float x = (f.gco().getNormalPoint(c.getP1()).x+f.gco().getNormalPoint(c.getP2()).x)/2;
					float y = (f.gco().getNormalPoint(c.getP1()).y+f.gco().getNormalPoint(c.getP2()).y)/2;
					if(psg.isPointIndide(editedPolygonFillMap.getSt(), x, y))
					{
						needEdit = true;
						editedPolygonConns.add(i);
					}
				}
			}
		}
		return needEdit;
	}



	private static void fillControlTabs(ArrayList<Integer> editedPolygonPoints, ArrayList<Integer> editedPolygonConns,
			boolean[] tabOfPointsInEditedPolygon, boolean[] tabOfConnsInEditedPolygon) {
		for(int i : editedPolygonPoints)
			tabOfPointsInEditedPolygon[i] = true;
		for(int i : editedPolygonConns)
			tabOfConnsInEditedPolygon[i] = true;
	}



	private static void addPointsFromTopPolygon(Frame f, ArrayList<Integer> pointsInTopPolygon,
			PolygonShapeGenerator psg, ArrayList<Integer> otherPolygonPoints,
			boolean[] pointsInsideTopPolygonControlTab, FillMap otherPolygonFillMap) {
		for(int i = 0; i < pointsInTopPolygon.size(); i++)
		{
			if(!pointsInsideTopPolygonControlTab[pointsInTopPolygon.get(i)])
			{
				if(psg.isPointIndide(otherPolygonFillMap.getSt(), f.gco().getNormalPoint((int)pointsInTopPolygon.get(i)).x, f.gco().getNormalPoint((int)pointsInTopPolygon.get(i)).y))
				{
					otherPolygonPoints.add(pointsInTopPolygon.get(i));
				}
			}
		}
	}



	private static void markShape(Frame f, ArrayList<Integer> otherPolygonPoints,
			ArrayList<Integer> otherPolygonConns) {
		for(int i : otherPolygonPoints)
			f.gco().getNormalPoint(i).MarkedPoint = true;
		for(int i : otherPolygonConns)
			f.gco().getCon(i).setMarked(true);
	}



	private static void removeInsidePoints(ArrayList<Integer> otherPolygonPoints,
			boolean[] pointsInsideTopPolygonControlTab) {
		for(int i = 0; i < otherPolygonPoints.size(); i++)
		{
			if(pointsInsideTopPolygonControlTab[otherPolygonPoints.get(i)])
			{
				otherPolygonPoints.remove(i);
				i--;
			}
		}
	}



	private static void findAndRemoveAllConectionecConnectedToPointsInsideTopPolygon(Frame f,
			ArrayList<Integer> otherPolygonConns, boolean[] pointsInsideTopPolygonControlTab,
			ArrayList<Integer> connsConectedToPointsInsideTopPolygon) {
		for(int i = 0; i < otherPolygonConns.size(); i++)
		{
			Connection c = f.gco().getCon((int)otherPolygonConns.get(i));
			if(pointsInsideTopPolygonControlTab[c.getP1()]||pointsInsideTopPolygonControlTab[c.getP2()])
			{
				connsConectedToPointsInsideTopPolygon.add(otherPolygonConns.get(i));
				otherPolygonConns.remove(i);
				i--;
			}
		}
	}



	private static void findAllPointsInsideTopPolygon(Frame f, FillMap polygonFM, boolean[] usedPointsInTopPolygon,
			PolygonShapeGenerator psg, ArrayList<Integer> otherPolygonPoints, ArrayList<Integer> pointsInsideTopPolygon,
			boolean[] pointsInsideTopPolygonControlTab) {
		for(int i : otherPolygonPoints)
		{
			//Je�eli nie nale�� do g��wnego polygona, ale punkty znajduj� si� wewn�trz, wszystkie punkty kt�re zosta�y zas�oni�te
			if(!usedPointsInTopPolygon[i]&&psg.isPointIndide(polygonFM.getSt(), f.gco().getNormalPoint(i).x, f.gco().getNormalPoint(i).y))
			{
				pointsInsideTopPolygonControlTab[i] = true;
				pointsInsideTopPolygon.add(i);
			}
		}
	}
}
