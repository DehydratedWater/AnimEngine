package markTooles;

import java.awt.Graphics2D;

import mark.Mark;
import mark.dataBox;
import mark.toolSchem;
import polygonEngine.FillManager;
import renderSource.Connection;
import structures.Frame;
import structures.ParameterBox;
import toolBox.frameCupture;

public class moveConnection implements toolSchem
{
	private int connIndex;
	private float startP1X, startP1Y, startP2X, startP2Y;
	private float vectorX, vectorY, startVectorX, startVectorY;
	private Connection conn;
	private renderSource.Point P1;
	private renderSource.Point P2;
    
	private boolean created = false;
	
	public void useTool(Frame f, int x, int y, dataBox db, ParameterBox pb, Graphics2D g, int tool, Mark mark)
	{
		//System.out.println("CONNNNNNNNN");
		if(created == false)
		{
			if(db.mouseClicked&&!db.mouseRightClicked)
				initTool(f, x, y, db, pb, tool, mark);
		}
		else
		{
			if(db.mouseRightClicked)
			{
				cancelTool(f, mark);
				resetTool();
			}
			else
			{
				if(db.mouseClicked)
				refreshTool(f, x, y, db, tool, mark);
				else
				{
				finalizeTool(f, x, y, db, pb, tool, mark);
				resetTool();
				}
			}
			
		}
	}
		

	private void initTool(Frame f, int x, int y, dataBox db, ParameterBox pb, int tool, Mark mark)
	{
		System.out.println("inicjalizacja");
		connIndex =  f.gco().getNearestConnection(x, y);

		if(connIndex > -1){
		conn =  f.gco().getConnectionTab().get(connIndex);
		float t[] = f.gco().scaleAndRotateValue(x, y);
		startVectorX = t[0];
		startVectorY = t[1];
		P1 = f.gco().getNormalPoint(conn.getP1());
		P2 = f.gco().getNormalPoint(conn.getP2());
		startP1X = P1.x;
		startP1Y = P1.y;
		startP2X = P2.x;
		startP2Y = P2.y;
		mark.isMarkedConn = true;
		mark.indexOfMovedConn = connIndex;
		created = true;	
		}
	}
	private void refreshTool(Frame f, int x, int y, dataBox db, int tool, Mark mark)
	{

		
			float t[] = f.gco().scaleAndRotateValue(x, y);
			vectorX= t[0] - startVectorX;
			vectorY= t[1] - startVectorY; 
			f.gco().moveConnection(connIndex, vectorX, vectorY, startP1X, startP1Y, startP2X, startP2Y, 0, 0, 0, 0);
			frameCupture.hasFrame = false;

		
	}
	
	public void finalizeTool(Frame f, int x, int y, dataBox db, ParameterBox pb, int tool, Mark mark)
	{
		frameCupture.hasFrame = false;
		f.gco().moveConnection(connIndex, vectorX, vectorY, startP1X, startP1Y, startP2X, startP2Y, 0, 0, 0, 0);
		//f.gco().reAddAllConnectionesConnectedToConnection(f.gco().getCon(connIndex).getP1(), f.gco().getCon(connIndex).getP2() );
		FillManager.needToRefresh = true;
	}
	
	
	
	public boolean isCreated() 
	{
		return created;
	}


	
	public void cancelTool (Frame f, Mark mark) {
		mark.isMarkedConn = false; 	
		f.gco().moveConnection(connIndex, 0, 0, startP1X, startP1Y, startP2X, startP2Y, 0, 0, 0, 0);
		f.gco().getCon(mark.indexOfMovedConn).setMarked(false);
	}


	@Override
	public void resetTool() {
		created = false;
		
	}


	
	
}