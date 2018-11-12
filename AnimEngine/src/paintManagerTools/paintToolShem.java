package paintManagerTools;

import java.awt.Graphics2D;

import mark.dataBox;
import structures.Frame;
import structures.ParameterBox;

public interface paintToolShem 
{
	public void useTool(Frame f, int x, int y, dataBox db, ParameterBox pb, Graphics2D g, int tool);
	public void resetTool();
	public boolean isCreated();
}
