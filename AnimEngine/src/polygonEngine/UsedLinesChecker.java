package polygonEngine;

import java.util.ArrayList;

public class UsedLinesChecker 
{
	public ArrayList<connIndexKeeper> kTab;
	public UsedLinesChecker(int lenght)
	{
		kTab = new ArrayList<connIndexKeeper>(lenght);
		for(int i = 0; i < lenght; i++)
		{
			kTab.add(new connIndexKeeper());
		}
	}
	public void addConnIndex(int XStabIndex, int index)
	{
		kTab.get(XStabIndex).iTab.add(index);
	}
	public int getConnIndex(int XStabIndex, int index)
	{
		return kTab.get(XStabIndex).iTab.get(index);
	}
}
