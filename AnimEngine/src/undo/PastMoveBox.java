package undo;

import java.util.ArrayList;

import polygonEngine.FillManager;
import structures.Animation;

public class PastMoveBox 
{
	private ArrayList<Animation> movieTab = new ArrayList<Animation>();
	private ArrayList<Step> stepTab = new ArrayList<Step>();
	
	private ArrayList<Animation> FutureMovieTab = new ArrayList<Animation>();
	private ArrayList<Step> FutureStepTab = new ArrayList<Step>();
	
	public void addStep(Animation a)
	{
			if(CheckIfDuplicated(a)==false)
			{
			FutureMovieTab = new ArrayList<Animation>();
			FutureStepTab = new ArrayList<Step>();
			stepTab.add(new Step(true, false, false));
			movieTab.add(a.clone());
			//System.out.println("Dodawnie kroku z filmem: "+stepTab.size());
			}
//			else
//				System.out.println("Krok zosta³ by powielony");
		
		//System.out.println("Dodano nowy krok, aktualna iloœæ kroków: "+stepTab.size());
	}
	
	
	private boolean CheckIfDuplicated(Animation a1)
	{
		Animation a2;
		if(movieTab.size()>0)
		a2 = movieTab.get(movieTab.size()-1);
		else
			return false;
		if(a1.getFrameTabSize()!=a2.getFrameTabSize())
		{
			return false;
		}
		else
		{
			for(int i = 0 ; i < a1.getFrameTabSize();i++)
			{
				if(a1.getFrameNum(i).getObjTab()!=a2.getFrameNum(i).getObjTab())
				{
					return false;
				}
				else
				{
					
					//TODO sparawdzanie tylko bierz¹cych klatek Current frame
					
					
					for(int j = 0; j < a1.getFrameNum(i).getObjTabSize();j++)
					{
						if(a1.getFrameNum(i).getObj(j).getPointTab().size()!=a2.getFrameNum(i).getObj(j).getPointTab().size())
						{
							return false;
						}
						else
						{
							for(int k = 0; k < a1.getFrameNum(i).getObj(j).getPointTab().size();k++)
							{
								if(a1.getFrameNum(i).getObj(j).getNormalPoint(k).x!=a2.getFrameNum(i).getObj(j).getNormalPoint(k).x&&a1.getFrameNum(i).getObj(j).getNormalPoint(k).y!=a2.getFrameNum(i).getObj(j).getNormalPoint(k).y)
								{
									return false;
								}
							}
						}
						if(a1.getFrameNum(i).getObj(j).getConnectionTab().size()!=a2.getFrameNum(i).getObj(j).getConnectionTab().size())
						{
							return false;
						}
						if(a1.getFrameNum(i).getObj(j).getPolygonTab().size()!=a2.getFrameNum(i).getObj(j).getPolygonTab().size())
						{
							return false;
						}
						
					}
				}
			}
		}
		return true;
	}
	
	

	
	
	public Animation stepForewad(Animation a)
	{
	
		if(FutureStepTab.size()>0)
		{
			
			a = FutureMovieTab.get(FutureMovieTab.size()-1).clone();
			FillManager.needToRefresh = true;
			movieTab.add(FutureMovieTab.get(FutureMovieTab.size()-1));
			stepTab.add(FutureStepTab.get(FutureStepTab.size()-1));	
			FutureMovieTab.remove(FutureMovieTab.size()-1);
			FutureStepTab.remove(FutureStepTab.size()-1);
			System.out.println("Iloœæ cofniêtych kroków "+FutureStepTab.size());
			System.out.println("Iloœæ pozosta³ych kroków: "+stepTab.size());
		}
		
		return a;
	}
	
	public Animation stepBack(Animation a)
	{
		if(stepTab.size()>0)
		{
			if(FutureMovieTab.size()==0)
			{
				FutureMovieTab.add(a.clone());
				FutureStepTab.add(new Step(true, false, false));
			}
			FillManager.needToRefresh = true;
			//Step s = stepTab.get(stepTab.size()-1);
			//System.out.println("Wykonywanie kroku nr."+(stepTab.size()-1)+" isMovie "+s.isMovie()+" isFrame "+s.isFrame()+" isObject "+s.isObject());
			//System.out.println("Odczytywanie animacji pod numerem");
			a = movieTab.get(movieTab.size()-1).clone();
			//System.out.println("Cofanie kroku "+movieTab.size());

			FutureMovieTab.add(movieTab.get(movieTab.size()-1).clone());
			FutureStepTab.add(stepTab.get(stepTab.size()-1).clone());
			movieTab.remove(movieTab.size()-1);
			stepTab.remove(stepTab.size()-1);	
			System.out.println("Iloœæ cofniêtych kroków "+FutureStepTab.size());
			System.out.println("Iloœæ pozosta³ych kroków: "+stepTab.size());
		}
		
		//System.out.println("Zwracanie animacji objTab: "+a.getFrame().getObjTabSize()+" PointTab: "+a.getFrame().gco().getPointTab().size());
		return a;
	}
	

	public void removeLastStep()
	{
		if(stepTab.size()>0)
		stepTab.remove(stepTab.size()-1);	
	}

	public ArrayList<Step> getStepTab() {
		return stepTab;
	}

	public void setStepTab(ArrayList<Step> stepTab) {
		this.stepTab = stepTab;
	}

	public ArrayList<Animation> getMovieTab() {
		return movieTab;
	}

	public void setMovieTab(ArrayList<Animation> movieTab) {
		this.movieTab = movieTab;
	}


}
