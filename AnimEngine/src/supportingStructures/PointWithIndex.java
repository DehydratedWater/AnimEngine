package supportingStructures;

public class PointWithIndex 
{
	public float x;
	public int index;
	public PointWithIndex(float x, int index) {
		this.x = x;
		this.index = index;
	}
	public PointWithIndex clone()
	{
		return new PointWithIndex(this.x, this.index);
	}
}
