package structures;

public class frameQuene 
{
	public boolean isObject = true;
	public int index = 0;
	public frameQuene(boolean isObject, int index) {
		this.isObject = isObject; // m�wi czy korzysta� z tablicy bitmap czy tablicy obiekt�w
		this.index = index; //index w tablicy
	}
	
	public frameQuene clone()
	{
		return new frameQuene(isObject, index);
	}
}
