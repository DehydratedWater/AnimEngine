package polygonEngineStructures;

public class StabTab 
{
	public DynamicList<Stab> sTab;
	public DynamicList<VertexLine> vTab;
	public float SX, EX;
	public StabTab() 
	{
		sTab = new DynamicList<Stab>();
		vTab = new DynamicList<VertexLine>();
	}
	public void addStab(float X1, float X2)
	{
		sTab.add(new Stab(X1, X2));
	}
	public void addVertexLine(int i, Conn c)
	{
		vTab.get(i).addVertexConn(c);
	}
	
	public void destroy()
	{
		sTab.clear();
		vTab.clear();
		System.gc();
	}
}
