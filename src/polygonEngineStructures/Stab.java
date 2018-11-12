package polygonEngineStructures;

public class Stab 
{
	public DynamicList<Conn> cTab;
	public float X1, X2;
	public Stab(float x1, float x2)
	{
		cTab = new DynamicList<Conn>();
		this.X1 = x1;
		this.X2 = x2;
	}
}
