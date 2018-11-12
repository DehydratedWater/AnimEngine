package supportingStructures;
import java.util.ArrayList;
import polygonEngine.FillMap;
import polygonEngine.FilledPolygonWithCurves;
import structures.Frame;

public class PolygonInfoBackup 
{
	public FillMap fm;
	public ArrayList<Integer> listOfPoints;
	public ArrayList<Integer> listOfConns;
	public int polygonIndex;
	
	public PolygonInfoBackup(Frame f, FilledPolygonWithCurves p, int polygonIndex)
	{
		fm = new FillMap();
		ArrayList<Integer> includedConns = p.getShapeConnIndexes();
		
//		for(int i : includedConns)
//			System.out.print(i+" ");
//		System.out.println();
		this.polygonIndex = polygonIndex;
		fm.GenerateSmallMapOfPolygonNoDrawing(f, f.gco().getConnectionTab(), f.gco().getPointTab(), includedConns);
		listOfConns = new ArrayList<Integer>(p.connectionIndexTab.size());
		//listOfConns = p.connectionIndexTab;
		for(int i : p.connectionIndexTab)
			listOfConns.add(i);
		
		listOfPoints = p.getPoints(f.gco().getPointTab(), f.gco().getConnectionTab());
	}
}
