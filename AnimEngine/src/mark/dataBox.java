package mark;

public class dataBox 
{
	public boolean mouseClicked;
	public boolean mouseRightClicked;
	public boolean DELETE;
	public boolean CTRL;
	public int lineTool;
	public boolean M;
	/**
	 * Tworzy nowy DataBox z podamymi danymi
	 * @param mouseClicked
	 * @param mouseRightClicked
	 * @param DELETE
	 * @param CTRL
	 * @param lineTool
	 * @param M
	 */
	public dataBox(boolean mouseClicked, boolean mouseRightClicked, boolean DELETE, boolean CTRL, int lineTool, boolean M)
	{
		this.lineTool = lineTool;
		this.mouseClicked = mouseClicked;
		this.mouseRightClicked = mouseRightClicked;
		this.CTRL = CTRL;
		this.DELETE = DELETE;
		this.M = M;
	}
}
