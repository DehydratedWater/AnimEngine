package markManagerPac;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import insertionManager.FindLinesInserctions;
import insertionManager.PolygonInsertion;
import mark.Mark;
import mark.dataBox;
import markTooles.EraserTool;
import markTooles.addPointToConnection;
import markTooles.createMark;
import markTooles.createMarkCutted;
import markTooles.cutConnection;
import markTooles.deleteMarked;
import markTooles.getLineParameters;
import markTooles.moveConnection;
import markTooles.moveMarkedPoints;
import markTooles.moveMarkedPointsCutted;
import markTooles.movePoint;
import markTooles.movePolygon;
import markTooles.rotatePoints;
import markTooles.scalePoints;
import markTooles.separateConnection;
import markTooles.setLineParameters;
import structures.Frame;
import structures.ParameterBox;
import toolBox.frameCupture;
import toolBox.tb;

public class MarkManager 
{
	public static int tool;
	private Mark mark;
	private int lineTool = 0;
	public static boolean isTransformMode = false;
	
	public static boolean toUbdate;
	private static boolean lastToolChanged;
	private deleteMarked delMarked;
	private createMark creMarked;
	private createMarkCutted creMarkedCut;
	private movePoint movPoint;
	private moveConnection movConn;
	private moveMarkedPoints movMarkedPoints;
	private moveMarkedPointsCutted movMarkedPointsCut;
	private cutConnection cutConn;
	private addPointToConnection addPointToConn;
	private separateConnection separateConn;
	private setLineParameters setLineParm;
	private getLineParameters getLineParm;
	private scalePoints scalePoint;
	private rotatePoints rotPoints;
	private movePolygon movPolygon;
	private EraserTool eraserTool;
	private boolean toolToCheck;
	
	
	public MarkManager() 
	{
		delMarked = new deleteMarked();
		creMarked = new createMark();
		creMarkedCut = new createMarkCutted();
		movPoint = new movePoint();
		movConn = new moveConnection();
		movMarkedPoints = new moveMarkedPoints();
		cutConn = new cutConnection();
		addPointToConn = new addPointToConnection();
		separateConn = new separateConnection();
		setLineParm = new setLineParameters();
		getLineParm = new getLineParameters();
		scalePoint = new scalePoints();
		rotPoints = new rotatePoints();
		movPolygon = new movePolygon();
		eraserTool = new EraserTool();
		movMarkedPointsCut = new moveMarkedPointsCutted();
		mark = new Mark();
	}
	
	/**
	 * Wywo³uje narzêdznie które jest aktualnie wybrane poprzez wartosæ tool
	 * Wartoœæ tool okreœla siê na pomoc¹ ContectTool które okreœla jakie narzêdnie powinno byæ wybrane na podstawie po³o¿enia myszy i wartoœci linetool
	 * @param f
	 * @param x
	 * @param y
	 * @param mouseClicked
	 * @param mouseRightClicked
	 * @param CTRL
	 * @param DELETE
	 * @param M
	 * @param pb
	 * @param g
	 */
	public void useTool(Frame f, int x, int y, boolean mouseClicked, boolean mouseRightClicked, boolean CTRL, boolean DELETE, boolean M, ParameterBox pb, Graphics2D g)
	{
		//System.out.println("LineTool 1 to "+lineTool+" "+tool );
		dataBox db = new dataBox(mouseClicked, mouseRightClicked, DELETE, CTRL, lineTool, M);
		checkCancelingMark(f, mouseClicked, mouseRightClicked, g);
		if(mouseClicked)
		{
		if(mark.isMarkedArea)
			checkIfClickedOnMarkArea(x, y, mark, tool);
		
		if(mark.isMarkedArea==false&&toolToCheck==true&&lineTool!=5)
		{
			System.out.println("Sprawdznie Narzêdzia");
			int newTool = ContectTool.setTool(f, x, y, db, tool, mark, g);
			if(newTool!=tool)
				resetLastUsedTool(newTool);
			tool = newTool;
			System.out.println("Tool: "+tool);
			toolToCheck = false;
		}
		}
		else
		{
			toolToCheck = true;
		}
		if(lineTool == 5)
		{
			tool = 12;
			lastToolChanged = true;
		}
		else if(lineTool!=5&&lastToolChanged)
		{
			lastToolChanged = false;
			tool = ContectTool.setTool(f, x, y, db, tool, mark, g);
		}
		
		//System.out.println("LineTool 1 to "+lineTool+" "+tool );
		//System.out.println("BETA ZAZNACZANIE tool "+tool);
		if(!db.DELETE)
		{
			delMarked.resetTool();
		}
		if(db.DELETE&&db.mouseClicked==false)
			delMarked.useTool(f, x, y, db, pb,  g, tool, mark);
		else if(tool == 0)
		{
			if(isTransformMode)
				creMarked.useTool(f, x, y, db, pb, g, tool, mark); //OK
			else
				creMarkedCut.useTool(f, x, y, db, pb, g, y, mark);
		}
		else if(tool == 1)
			movPoint.useTool(f, x, y, db, pb, g, tool, mark); //OK
		else if(tool == 2)
			movConn.useTool(f, x, y, db, pb, g, tool, mark);
		else if(tool == 3)
		{
			if(isTransformMode)
				movMarkedPoints.useTool(f, x, y, db, pb, g, tool, mark);
			else
				movMarkedPointsCut.useTool(f, x, y, db, pb, g, tool, mark);
		}
		else if(tool == 4)
			cutConn.useTool(f, x, y, db, pb, g, tool, mark); //OK
		else if(tool == 5)
			addPointToConn.useTool(f, x, y, db, pb, g, tool, mark); //OK
		else if(tool == 6)
			separateConn.useTool(f, x, y, db, pb, g, tool, mark); //OK
		else if(tool == 7)
			setLineParm.useTool(f, x, y, db, pb, g, tool, mark); //OK
		else if(tool == 8)
			getLineParm.useTool(f, x, y, db, pb, g, tool, mark);
		else if(tool == 9)
			scalePoint.useTool(f, x, y, db, pb, g, tool, mark);
		else if(tool == 10)
			rotPoints.useTool(f, x, y, db, pb, g, tool, mark);
		else if(tool == 11)
			movPolygon.useTool(f, x, y, db, pb, g, y, mark);
		else if(tool == 12)
			eraserTool.useTool(f, x, y, db, pb, g, y, mark);
			//System.out.println("s");
		if(mark.isMarkedArea)
			drawMark(g);
		
	//	System.out.println(tool);
	}
	/**
	 * Sprawdza wyniki odznaczania i odznacza zaznaczone elementy
	 * np wywo³uje sprawdzenie przeicêæ odcinków gdy odznacza sie zaznaczony obszar
	 * @param f
	 * @param mouseClicked
	 * @param mouseRightClicked
	 * @param g
	 */
	private void checkCancelingMark(Frame f, boolean mouseClicked, boolean mouseRightClicked, Graphics2D g) {
		if(mouseClicked==false&&mouseRightClicked)
		{
			//System.out.println("Naciœnieto lewy przycisk");
			if(mark.isMarkedPoint)
			{
//				f.gco().reAddAllConnectionesConnectedToPoint(mark.indexOfMovedPoint);
				f.gco().getNormalPoint(mark.indexOfMovedPoint).MarkedPoint = false;
				findLineInsertiones(f);
				mark.isMarkedPoint = false;
				toolToCheck = true;
				frameCupture.hasFrame = false;
			}
			else if(mark.isMarkedConn)
			{
				f.gco().getCon(mark.indexOfMovedConn).setMarked(false);
				findLineInsertiones(f);
				//f.gco().reAddAllConnectionesConnectedToConnection(f.gco().getCon(mark.indexOfMovedConn).getP1(), f.gco().getCon(mark.indexOfMovedConn).getP2() );
				mark.isMarkedConn = false;
				frameCupture.hasFrame = false;
			}
			else if(mark.isMarkedArea&&movMarkedPoints.isCreated()==false)
			{
				//System.out.println("Znaleziono "+mark.tabOfPointIndex.size()+" punktów i "+mark.tabOfConnIndex.size()+" odcinków");
				
				findLineInsertiones(f);
				createMark.cancelMarking(f, mark);
				//System.out.println("Znaleziono "+mark.tabOfPointIndex.size()+" punktów i "+mark.tabOfConnIndex.size()+" odcinków");
				frameCupture.hasFrame = false;
				mark.isMarkedArea = false;
			}
			else if(mark.isMarkedPolygon&&movPolygon.isCreated()==false)
			{
				findLineInsertiones(f);
				mark.isMarkedPolygon = false;
				f.gco().removeConnectionMark();
				f.gco().removeConnectionMark();
				System.out.println("Polygon "+mark.indexOfMovedPolygon);
				PolygonInsertion.polygonInsertionStructureModyfication(f, mark.indexOfMovedPolygon);
				f.gco().setPolygonToDraw(new ArrayList<Integer>());
				//TODO Funkcja która sprawdza przeciêcia z przeci¹ganym polygonem
//				PolygonInsertion.checkInsertionPolygon(f, mark.indexOfMovedPolygon);
				frameCupture.hasFrame = false;
			}
		}
	}
	/**
	 * Odnajduje wszyskie przeciêcia lini w klatce f w endytowanej warstwie
	 * @param f
	 */
	private void findLineInsertiones(Frame f) {
		FindLinesInserctions fli = new FindLinesInserctions();
		fli.FindInserctionSimpleCall(f);
		frameCupture.hasFrame = false;
	}

	
	/**
	 * Zwraca tablicê z indeksami zaznaczonych odcinków
	 * @param f
	 * @return
	 */
	public int[] getMarkedLines(Frame f)
	{
		ArrayList<Integer> tab = new ArrayList<Integer>();
		//System.out.println("OTO ODCINKI");
		for(int i = 0; i < f.getObj(f.getObject()).getConnectionTab().size();i++)
		{
			if(f.gco().getCon(i).isMarked()==true)
			{
				f.gco().getCon(i).setMarked(false);
				tab.add(i);
			}
		}
		//System.out.println("OTO ODCINKI");
		int TAB[] = new int[tab.size()];
		for(int i = 0; i < TAB.length; i++)
		{
			TAB[i] = tab.get(i);
		}
		return TAB;
	}

	/**
	 * Rysuje zaznaczenie na ekranie (to obramowanie z punktami i liniami przerywanymi)
	 * @param g
	 */
	private void drawMark(Graphics2D g)
	{
		float dash1[] = {10.0f};
		BasicStroke s = new BasicStroke(1.5f, BasicStroke.CAP_ROUND,  BasicStroke.JOIN_ROUND, 10.0f, dash1, 0.0f);
		g.setStroke(s);
		g.setColor(Color.BLACK);
		g.draw(new Line2D.Float(mark.range[0],mark.range[1],mark.range[0],mark.range[3]));
		g.draw(new Line2D.Float(mark.range[0],mark.range[1],mark.range[2],mark.range[1]));
		g.draw(new Line2D.Float(mark.range[0],mark.range[3],mark.range[2],mark.range[3]));
		g.draw(new Line2D.Float(mark.range[2],mark.range[1],mark.range[2],mark.range[3]));
		BasicStroke s2 = new BasicStroke(1.5f, BasicStroke.CAP_ROUND,  BasicStroke.JOIN_ROUND);
		g.setStroke(s2);
		g.setColor(Color.yellow);
		g.fillOval((int)(mark.range[0])-5, (int)(mark.range[1])-5, 10, 10);
		g.fillOval((int)(mark.range[2]-5), (int)(mark.range[3]-5), 10, 10);
		g.fillOval((int)(mark.range[0])-5, (int)(mark.range[3]-5), 10, 10);
		g.fillOval((int)(mark.range[2]-5), (int)(mark.range[1])-5, 10, 10);
		g.setColor(Color.BLACK);
		g.drawOval((int)(mark.range[0])-5, (int)(mark.range[1])-5, 10, 10);
		g.drawOval((int)(mark.range[2]-5), (int)(mark.range[3]-5), 10, 10);
		g.drawOval((int)(mark.range[0])-5, (int)(mark.range[3]-5), 10, 10);
		g.drawOval((int)(mark.range[2]-5), (int)(mark.range[1])-5, 10, 10);

		g.fillRect((int)(mark.range[0])-10, (int)(mark.range[1])-10, 5, 5);
		g.fillRect((int)(mark.range[2]+5), (int)(mark.range[3]+5), 5, 5);
		g.fillRect((int)(mark.range[0])-10, (int)(mark.range[3]+5), 5, 5);
		g.fillRect((int)(mark.range[2]+5), (int)(mark.range[1])-10, 5, 5);
		
	}
	/**
	 * Wywo³uje reset ostatnio urzywanego narzêdzia
	 * @param temp
	 */
	private void resetLastUsedTool(int temp) 
	{
		System.out.println("Resetowanie narzêdzia "+temp);
		
		
		if(temp == 0)
			creMarked.resetTool();
		else if(temp == 1)
			movPoint.resetTool();
		else if(temp == 2)
			movConn.resetTool();
		else if(temp == 3)
			movMarkedPoints.resetTool();
		else if(temp == 4)
			cutConn.resetTool();
		else if(temp == 5)
			addPointToConn.resetTool();
		else if(temp == 6)
			separateConn.resetTool();
		else if(temp == 7)
			setLineParm.resetTool();
		else if(temp == 8)
			getLineParm.resetTool();
		else if(temp == 9)
			scalePoint.resetTool();
		else if(temp == 10)
			rotPoints.resetTool();
			
			
	}
	/**
	 * Sprawdza czy i gdzie naciœniêto na Zaznaczenie (obramówka) i wybiera odpowiednie narzêdzia w zale¿noœci gdzie naciœniêto
	 * np 
	 * gdy naciœnieto na punkt do skalowania wywoa³uje narzêdzie do skalowaia punktów
	 * @param x
	 * @param y
	 * @param mark
	 * @param tool
	 */
	private void checkIfClickedOnMarkArea(float x, float y, Mark mark, int tool) 
	{
		int indexOfClickedPoint = clickedOnPoints(x, y, mark);
		int indexOfClickedRotatingPoint = clickedOnRotatingPoints(x, y, mark);
		if(movMarkedPoints.isCreated()||movMarkedPointsCut.isCreated())
		{
			//System.out.println("Utworzone");
			MarkManager.tool = 3;
		}
		else if((indexOfClickedRotatingPoint>-10||(rotPoints.isCreated()))&&!(scalePoint.isCreated()))
		{
			//System.out.println("Wybrano obracanie "+indexOfClickedRotatingPoint);
			MarkManager.tool = 10;
		}
		else if(indexOfClickedPoint>-10||(scalePoint.isCreated()))
		{
			//System.out.println("Naciœniêto skalowanie"+(scalePoint.isCreated()));
			MarkManager.tool = 9;
		}
		else if((mark.range[0]<=x&&mark.range[1]<=y&&mark.range[2]>=x&&mark.range[3]>=y))
		{
			//System.out.println("Naciœniêto na zaznacznie "+tool);
			MarkManager.tool = 3;
		}
		else
		{
			System.out.println("anulowanie");
			mark.isMarkedArea=false;
		}
	}

	/**
	 * Zwraca indeks klikniêtego punktu skaluj¹cego z zazaczenia
	 * @param x
	 * @param y
	 * @param mark
	 * @return
	 */
	private int clickedOnPoints(float x, float y, Mark mark) 
	{
		if(checkIfClickedOnPoint(x, y, mark.range[0], mark.range[1],10))
			return 1;
		if(checkIfClickedOnPoint(x, y, mark.range[2], mark.range[1],10))
			return 2;
		if(checkIfClickedOnPoint(x, y, mark.range[2], mark.range[3],10))
			return 3;
		if(checkIfClickedOnPoint(x, y, mark.range[0], mark.range[3],10))
			return 4;
		return -10;
	}
	/**
	 * Zwraca indeks klikniêtefo punktu do obracania z zaznaczenia
	 * @param x
	 * @param y
	 * @param mark
	 * @return
	 */
	private int clickedOnRotatingPoints(float x, float y, Mark mark) 
	{
		if(checkIfClickedOnPoint(x, y, mark.range[0]-10, mark.range[1]-10,5))
			return 1;
		if(checkIfClickedOnPoint(x, y, mark.range[2]+5, mark.range[1]-10,5))
			return 2;
		if(checkIfClickedOnPoint(x, y, mark.range[2]+5, mark.range[3]+5,5))
			return 3;
		if(checkIfClickedOnPoint(x, y, mark.range[0]-10, mark.range[3]+5,5))
			return 4;
		return -10;
	}
	
	/**
	 * Sprawdza czy naciœniêto na punkt o indeksach px, py, przez mysz o kordynatach x, y
	 * @param x
	 * @param y
	 * @param px
	 * @param py
	 * @param range
	 * @return
	 */
	private boolean checkIfClickedOnPoint(float x, float y, float px, float py, float range)
	{
		if(tb.distance(x, y, px, py)<range)
			return true;
		else
			return false;
	}
	public int getLineTool() {
		return lineTool;
	}
	public void setLineTool(int lineTool) {
		this.lineTool = lineTool;
	}
}
