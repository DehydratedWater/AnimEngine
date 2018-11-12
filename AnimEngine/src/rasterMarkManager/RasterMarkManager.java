package rasterMarkManager;

import java.awt.Graphics2D;

import mark.Mark;
import mark.dataBox;
import rasterMarkTool.GeneralBitmapMark;
import structures.Frame;
import structures.ParameterBox;

public class RasterMarkManager 
{
	public static int internalTool = 0;
	public static int tool = 0;
	private GeneralBitmapMark generalBitmapMark;
	
	public RasterMarkManager()
	{
		generalBitmapMark = new GeneralBitmapMark();
	}
	public void useTool(Frame f, int x, int y, boolean mouseClicked, boolean mouseRightClicked, boolean CTRL, boolean DELETE, boolean M, ParameterBox pb, Graphics2D g)
	{
		//System.out.println("LineTool 1 to "+lineTool+" "+tool );
		dataBox db = new dataBox(mouseClicked, mouseRightClicked, DELETE, CTRL, internalTool, M);
		Mark mark = new Mark();
		if(internalTool == 0)
		{
			if(tool == 0)
			{
				generalBitmapMark.useTool(f, x, y, db, pb, g, tool, mark);
			}
		}else if(internalTool == 1)
		{
			
		}
	}
}
