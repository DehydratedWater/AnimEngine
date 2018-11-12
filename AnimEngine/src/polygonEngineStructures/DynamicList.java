package polygonEngineStructures;

import java.util.ArrayList;

public class DynamicList<E> extends ArrayList<E>
{
	private static final long serialVersionUID = 1L;
	
	public void addInPosition(E e, int i)
	{
		ArrayList<E> tempTab = new ArrayList<E>();
		if(i<size()&&i>-1)
		{
			for(int j = i; j < size(); j++)
			{
				tempTab.add(get(j));
				remove(j);
				j--;
			}
			add(e);
			for(int j = 0; j < tempTab.size(); j++)
			{
				add(tempTab.get(j));
			}
		}
		else
		{
			if(i==size())
			{
				add(e);
			}
			else
			System.out.println("Errr Index out of rage");
		}
	}
}