package mark;

import java.awt.Graphics2D;

import structures.Frame;
import structures.ParameterBox;

public interface toolSchem 
{
	/**
	 * Wywo³uje dzia³ania narzêdzia
	 * @param f
	 * @param x
	 * @param y
	 * @param db
	 * @param pb
	 * @param g
	 * @param tool
	 * @param mark
	 */
	public void useTool(Frame f, int x, int y, dataBox db, ParameterBox pb, Graphics2D g, int tool, Mark mark);
	/**
	 * resetuje wartoœæ zmiennej "created" na false
	 */
	public void resetTool();
	/**
	 * Ma zwracaæ wartoœc zmiennej "created" która powinno zawierac ka¿de narzêdzie
	 * @return
	 */
	public boolean isCreated();
}
