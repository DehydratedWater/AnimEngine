package printOutSchems;

import java.util.ArrayList;

public class ListsPrinter 
{
	public static void printArrayListWithInt(ArrayList<Integer> tab)
	{
		for(int i : tab)
			System.out.print(i+" ");
		System.out.println();
	}
	
	public static void printArrayListWithFloat(ArrayList<Float> tab)
	{
		for(float i : tab)
			System.out.print(i+" ");
		System.out.println();
	}
}
