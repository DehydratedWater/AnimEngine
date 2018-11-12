package mark;

import java.awt.Graphics2D;

import structures.Frame;
import structures.ParameterBox;

public interface toolSchem 
{
	/**
	 * Wywo�uje dzia�ania narz�dzia
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
	 * resetuje warto�� zmiennej "created" na false
	 */
	public void resetTool();
	/**
	 * Ma zwraca� warto�c zmiennej "created" kt�ra powinno zawierac ka�de narz�dzie
	 * @return
	 */
	public boolean isCreated();
}
