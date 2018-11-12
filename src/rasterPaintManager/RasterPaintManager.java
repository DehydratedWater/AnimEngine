package rasterPaintManager;

import java.awt.Graphics2D;

import mark.dataBox;
import rasterPaintTools.RasterCurveSegmentTool;
import rasterPaintTools.RasterCurveTool;
import rasterPaintTools.RasterLineSegmentTool;
import rasterPaintTools.RasterLineTool;
import rasterPaintTools.RasterRectTool;
import structures.Frame;
import structures.ParameterBox;

public class RasterPaintManager 
{
	public static int tool = 0;
	public static int toolFigure = 0;
	
	RasterLineTool rasterLineTool;
	RasterCurveTool rasterCurveTool;
	RasterLineSegmentTool rasterLineSegmetTool;
	RasterCurveSegmentTool rasterCurveSegmetTool;
	RasterRectTool rasterRectTool;
	public RasterPaintManager()
	{
		rasterLineTool = new RasterLineTool();
		rasterCurveTool = new RasterCurveTool();
		rasterLineSegmetTool = new RasterLineSegmentTool();
		rasterCurveSegmetTool = new RasterCurveSegmentTool();
		rasterRectTool = new RasterRectTool();
	}
	public void useTool(Frame f, int x, int y, boolean mouseClicked, boolean mouseRightClicked, boolean CTRL, boolean DELETE, boolean M, ParameterBox pb, Graphics2D g)
	{
		dataBox db = new dataBox(mouseClicked, mouseRightClicked, DELETE, CTRL, tool, M);
		
		
		if(tool == 0)
			rasterLineTool.useTool(f, x, y, db, pb, g, tool);
		else if(tool == 1)
			rasterCurveTool.useTool(f, x, y, db, pb, g, tool);
		else if(tool == 2)
			rasterLineSegmetTool.useTool(f, x, y, db, pb, g, tool);
		else if(tool == 3)
			rasterCurveSegmetTool.useTool(f, x, y, db, pb, g, tool);
		else if(tool == 4)
		{
			if(toolFigure == 0)
				rasterRectTool.useTool(f, x, y, db, pb, g, tool);
		}
	}
}
