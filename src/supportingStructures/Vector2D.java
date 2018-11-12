package supportingStructures;

public class Vector2D 
{
	public float a;
	public float b;
	
	public Vector2D(float A,float B) 
	{
		this.a = A;
		this.b = B;
	}
	
	public void normalise()
	{
		float lenght = (float) Math.sqrt(a*a+b*b);
		a = a/lenght;
		b = b/lenght;
	}
	public void multiply(float value)
	{
		a*=value;
		b*=value;
	}
	public float getLenght()
	{
		return (float) Math.sqrt(a*a+b*b);
	}
}
