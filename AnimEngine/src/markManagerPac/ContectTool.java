package markManagerPac;

import java.awt.Graphics2D;

import insertionManager.FindLinesInserctions;
import mark.Mark;
import mark.dataBox;
import markTooles.movePolygon;
import polygonEngine.FillManager;
import structures.Frame;
import toolBox.frameCupture;

public class ContectTool 
{
	//Ma za pomoc¹ kontekstowego wybierania ustawiæ odpowiednie narzêdzie
	/**
	 * Za pomoc¹ kolejnych testów okreœla które narzêdzie ma zostaæ wybrane
	 * np
	 * je¿eli wspó³rzêdne x,y znajduja sie niedaleko punktu to zostanie wybrane narzêdzie do przesuwania punktu 
	 * @param f
	 * @param x -> myszy
	 * @param y -> myszy
	 * @param db
	 * @param tool
	 * @param mark
	 * @param g
	 * @return
	 */
	public static int setTool(Frame f, int x, int y, dataBox db, int tool, Mark mark, Graphics2D g)
	{
		frameCupture.hasFrame = false;
		System.out.println("Ustawianie narzêdzia");
		if(db.mouseClicked)
		{
			//System.out.println("Naciœniêto przycisk");
		if(mark.isMarkedArea)
		{
			if((x>mark.range[0]&&y>mark.range[1]&&mark.range[2]<x&&mark.range[3]<y))
			{
				System.out.println("Naciœniêto na MarkArenê");
				return 3;
			}
		}
		else
		{
			int nearestPoint = f.gco().getNearestPoint(x, y);
			if(nearestPoint>-1)
			{
				
				if(mark.isMarkedPoint)
				{
					if(mark.indexOfMovedPoint!=nearestPoint)
					{
						System.out.println("Ciêcie");
						f.gco().getNormalPoint(mark.indexOfMovedPoint).MarkedPoint = false;
						FindLinesInserctions fli = new FindLinesInserctions();
						fli.FindInserctionSimpleCall(f);
						//f.gco().reAddAllConnectionesConnectedToPoint(mark.indexOfMovedPoint);
						mark.indexOfMovedPoint=nearestPoint;
					}
				}
				else
				{
					mark.isMarkedPoint = true;
					mark.indexOfMovedPoint = nearestPoint;
				}
				if(mark.isMarkedConn)
				{
					System.out.println("Anulacja lini");
					if(!db.CTRL)
					{
						f.gco().getCon(mark.indexOfMovedConn).setMarked(false);
						FindLinesInserctions fli = new FindLinesInserctions();
						fli.FindInserctionSimpleCall(f);
						//f.gco().reAddAllConnectionesConnectedToConnection(f.gco().getCon(mark.indexOfMovedConn).getP1(), f.gco().getCon(mark.indexOfMovedConn).getP2() );
						mark.isMarkedConn = false;
					}
				}
				if(mark.isMarkedPolygon)
				{
					//TODO nak³adanie figur
					f.gco().removePointMark();
					f.gco().removeConnectionMark();
					mark.isMarkedPolygon= false;
				}
				f.gco().getNormalPoint(nearestPoint).MarkedPoint = true;
				return 1;
			}
			else
			{
				if(!db.CTRL)
				{
				f.gco().getNormalPoint(mark.indexOfMovedPoint).MarkedPoint = false;
				mark.isMarkedPoint = false;
				}
				FindLinesInserctions fli = new FindLinesInserctions();
				fli.FindInserctionSimpleCall(f, mark.range);
				//f.gco().reAddAllConnectionesConnectedToPoint(mark.indexOfMovedPoint);
				
			}
			
			
			int connectionIndex = f.gco().getNearestConnection(x, y);
			if(connectionIndex>-1)
			{
				
				if(mark.isMarkedConn)
				{
					if(mark.indexOfMovedConn!=connectionIndex)
					{
						FindLinesInserctions fli = new FindLinesInserctions();
						fli.FindInserctionSimpleCall(f);
						//f.gco().reAddAllConnectionesConnectedToConnection(f.gco().getCon(mark.indexOfMovedConn).getP1(), f.gco().getCon(mark.indexOfMovedConn).getP2() );
						f.gco().getCon(mark.indexOfMovedConn).setMarked(false);
						mark.indexOfMovedConn=connectionIndex;
					}
				}
				else
				{
					mark.isMarkedConn = true;
					mark.indexOfMovedConn = connectionIndex;
				}
				
				if(mark.isMarkedPoint)
				{
					mark.isMarkedPoint = false;
					FindLinesInserctions fli = new FindLinesInserctions();
					fli.FindInserctionSimpleCall(f);
					//f.gco().reAddAllConnectionesConnectedToPoint(mark.indexOfMovedPoint);
				}
				if(mark.isMarkedPolygon)
				{
					//TODO nak³adanie figur
					f.gco().removePointMark();
					f.gco().removeConnectionMark();
					mark.isMarkedPolygon= false;
				}
				f.gco().getCon(connectionIndex).setMarked(true);	
					if(db.lineTool==0){
					if(db.CTRL)
					{
						return 6;
					}
					else
					{
						return 2;
					}
				} else if(db.lineTool==1){
					return 5;
				} else if(db.lineTool==2){
					if(db.CTRL)
					{
						return 4;
					}
					else
					{
						return 2;
					}
				} else if(db.lineTool==3){
					f.gco().getCon(connectionIndex).setMarked(false);
					return 7;
				} else if(db.lineTool==4){
					f.gco().getCon(connectionIndex).setMarked(false);
					return 8;
				} 
				
			}
			else
			{
				if(!db.CTRL)
				{
				f.gco().getCon(mark.indexOfMovedConn).setMarked(false);
				//f.gco().reAddAllConnectionesConnectedToConnection(f.gco().getCon(mark.indexOfMovedConn).getP1(), f.gco().getCon(mark.indexOfMovedConn).getP2() );
				FindLinesInserctions fli = new FindLinesInserctions();
				fli.FindInserctionSimpleCall(f);
				mark.isMarkedConn = false;
				}
			}
			
			int filledPolygonIndex = FillManager.getIndexOfFilledPolygon(f, x, y);
			if(filledPolygonIndex>-1)
			{
				movePolygon.polygonIndex = filledPolygonIndex;
				System.out.println("Odnaleziono polygon nr "+filledPolygonIndex);
				return 11;
			}
		}
		}

		
		return 0;
	}
}
