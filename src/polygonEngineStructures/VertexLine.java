package polygonEngineStructures;

public class VertexLine 
{
	public DynamicList<Conn> cTab;
	public float X;
	public VertexLine(float x)
	{
		this.X = x;
	}
	
	public void addVertexConn(Conn c)
	{
		if(cTab==null)
			cTab = new DynamicList<Conn>();
		
		cTab.add(c);
	}
}
