package paintManager;

import java.awt.Graphics2D;

import mark.dataBox;
import paintManagerTools.CurveSegmentTool;
import paintManagerTools.CurveTool;
import paintManagerTools.LineSegmentTool;
import paintManagerTools.LineTool;
import paintManagerTools.RectTool;
import structures.Frame;
import structures.ParameterBox;

public class PaintManager 
{
	public static int tool;
	public static int toolFigure = 0;
	LineTool lineTool;
	CurveTool cureTool;
	LineSegmentTool lineSegmentTool;
	CurveSegmentTool curveSegmentTool;
	RectTool rectTool;
	public PaintManager()
	{
		lineTool = new LineTool();
		cureTool = new CurveTool();
		lineSegmentTool = new LineSegmentTool();
		curveSegmentTool = new CurveSegmentTool();
		rectTool = new RectTool();
	}
	public void useTool(Frame f, int x, int y, boolean mouseClicked, boolean mouseRightClicked, boolean CTRL, boolean DELETE, boolean M, ParameterBox pb, Graphics2D g)
	{
		dataBox db = new dataBox(mouseClicked, mouseRightClicked, DELETE, CTRL, tool, M);
		if(tool == 0)
			lineTool.useTool(f, x, y, db, pb, g, y);
		else if(tool == 1)
			cureTool.useTool(f, x, y, db, pb, g, y);
		else if(tool == 2)
			lineSegmentTool.useTool(f, x, y, db, pb, g, y);
		else if(tool == 3)
			curveSegmentTool.useTool(f, x, y, db, pb, g, y);
		else if(tool == 4)
		{
			if(toolFigure == 0)
				rectTool.useTool(f, x, y, db, pb, g, y);
		}
	}

}
