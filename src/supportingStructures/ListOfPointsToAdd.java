package supportingStructures;

public class ListOfPointsToAdd 
{
	public PointOnConnection[] tab;
	public ListOfPointsToAdd(int lenght) 
	{
		tab = new PointOnConnection[lenght];
		for(int i = 0; i < lenght; i++)
		{
			tab[i] = new PointOnConnection(i);
		}
	}
}
