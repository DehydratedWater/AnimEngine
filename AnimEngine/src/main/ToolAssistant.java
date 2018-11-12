package main;
//Ta klasa trzyma wszelkie opcje tycz¹ce siê ustawieñ poszczególnych narzêdzi (np. gruboœæ lini, kolor...)
//This class keep and edit value of tool parameter (like. size of line, color of line/ fill)

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;

import structures.ParameterBox;


public class ToolAssistant extends JInternalFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private int tool = 0;
	private static JColorChooser colorChooser;
	private JLabel toolDescription;
	private ParameterBox parameterBox;
	private JSlider slider;
	private JButton j1, j2, j3, j4, j5, j6;
	private boolean ready;
	private int linetool = 0;
	private Rectangle rec;
	/**
	 * Ustawia podstawowe parametry okna
	 */
	public ToolAssistant() 
	{
		setClosable(false);
		setMaximizable(true);
		setMaximizable(false);
		setResizable(true);
		setLayout(new FlowLayout());
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setSize(400,200);
		setLocation(600, 550);
		setVisible(true);
		toolDescription = new JLabel();
		add(toolDescription);
		colorChooser = new JColorChooser();
		colorChooser.setColor(Color.black);
		colorChooser.putClientProperty("JComponent.sizeVariant", "mini");
		slider = new JSlider(0, 3000);
		slider.setValue(300);
		
		setUpButtons();	
		parameterBox = new ParameterBox();
		add(colorChooser);
		add(slider);
		parameterBox.LineSize = 4f;
		parameterBox.fillColor = Color.BLACK;
	}

	/**
	 * Tworzy obiekty przycisków oraz ³aduje ich tekstury z folderu img/
	 */
	private void setUpButtons() {
		j1 = new JButton();
		j1.setIcon(new ImageIcon("img/MarkAndMove.png"));
		j2 = new JButton();
		j2.setIcon(new ImageIcon("img/addPoint.png"));
		j3 = new JButton();
		j3.setIcon(new ImageIcon("img/cut.png"));
		j4 = new JButton();
		j4.setIcon(new ImageIcon("img/LineParm.png"));
		j5 = new JButton();
		j5.setIcon(new ImageIcon("img/GetParametr.png"));
		j6 = new JButton();
		j6.setIcon(new ImageIcon("img/GetParametr.png"));
		add(j1);
		add(j2);
		add(j3);
		add(j4);
		add(j5);
		add(j6);
		j1.addActionListener(this);
		j2.addActionListener(this);
		j3.addActionListener(this);
		j4.addActionListener(this);
		j5.addActionListener(this);
		j6.addActionListener(this);
		j1.setEnabled(false);
	}


	/**
	 * Ustanawia uk³ad okna na taki który odpoiwiada rysowaniu lini oraz ci¹gów lini (powinien równiez dzia³ac przy rysowaniu krzywych, ci¹gów krzywych oraz figór)
	 */
	public void UbdateAssistanceForActualTool()
	{

		if(tool == 0||tool == 3)
		{
			if(ready==false)
			{
			setButtonsNotVisible();
			toolDescription.setText("Narzêdzie do rysowania lini");
			colorChooser.setColor(parameterBox.LineColor);
			colorChooser.setVisible(true);
			slider.setVisible(true);
			slider.setValue((int)parameterBox.LineSize*100);
			//setSize(700,400);
			ready = true;
			}
			parameterBox.LineColor = colorChooser.getColor();
			parameterBox.LineSize = slider.getValue()/100;
		}
		else if(tool == 1)
		{
			if(ready==false)
			{
			j1.setEnabled(false);
			j2.setEnabled(true);
			j3.setEnabled(true);
			j4.setEnabled(true);
			j5.setEnabled(true);
			j6.setEnabled(true);
			setButtonsVisible();
			//setSize(400,200);
			ready = true;
			colorChooser.setVisible(false);
			slider.setVisible(false);
			}
			parameterBox.EraserSize = slider.getValue()/100;
			setDescriptionForMarkTools();			
		}
		else if(tool == 2)
		{
			if(ready==false)
			{
			System.out.println("Zmiana koloru 2");
			setButtonsNotVisible();
			colorChooser.setColor(parameterBox.fillColor);
			toolDescription.setText("Narzêdzie do wype³niania kszta³tów");
			colorChooser.setVisible(true);
			slider.setVisible(false);
			//setSize(700,400);
			ready = true;
			}
			parameterBox.fillColor = colorChooser.getColor();
		}
	
		
	}

	public void actionPerformed(ActionEvent e) 
	{
		Object in = e.getSource();
		if(in==j1)
		{
			//setSize(400,200);
			Button1Action();
		}
		else if(in==j2)
		{
			//setSize(400,200);
			Button2Action();
		}
		else if(in==j3)
		{
			//setSize(400,200);
			Button3Action();
		}
		else if(in==j4)
		{
			//setSize(700, 500);
			Button4Action();
		}
		else if(in==j5)
		{
			//setSize(700, 500);
			Button5Action();
		}
		else if(in==j6)
		{
			//setSize(700, 500);
			Button6Action();
		}
	}
	
	/**
	 * Zmienia opisy narzêdzi w oknie zgodnie z aktualnie wybranym
	 */
	private void setDescriptionForMarkTools() {
		if(linetool==0)
		{
			toolDescription.setText("Narzêdzie do deformacji i zaznaczeñ (wycinanie odcinka)");
		}else if(linetool==1)
		{
			toolDescription.setText("Narzêdzie do deformacji i zaznaczeñ (dodawanie punktów)");
		}else if(linetool==2)
		{
			toolDescription.setText("Narzêdzie do deformacji i zaznaczeñ (ciêcie odcinka)   ");
		}else if(linetool==3)
		{
			toolDescription.setText("Narzêdzie do parametrów koloru lini");
			parameterBox.LineColor = colorChooser.getColor();
			parameterBox.LineSize = slider.getValue()/100;
		}
		else if(linetool==4)
		{
		toolDescription.setText("Narzêdzie do pobierania parametrów lini");
		}
		else if(linetool==5)
		{
		toolDescription.setText("Gumka");
		}
	}




	private void setButtonsVisible() {
		j1.setVisible(true);
		j2.setVisible(true);
		j3.setVisible(true);
		j4.setVisible(true);
		j5.setVisible(true);
		j6.setVisible(true);
	}



	private void setButtonsNotVisible() {
		j1.setVisible(false);
		j2.setVisible(false);
		j3.setVisible(false);
		j4.setVisible(false);
		j5.setVisible(false);
		j6.setVisible(false);
	}
	

	public ParameterBox getParameterBox()
	{
		return parameterBox;
	}
	
	private void Button6Action() {
		j1.setEnabled(true);
		j2.setEnabled(true);
		j3.setEnabled(true);
		j4.setEnabled(true);
		j5.setEnabled(true);
		j6.setEnabled(false);
		colorChooser.setVisible(false);
		slider.setVisible(true);
		linetool = 5;
	}


	private void Button5Action() {
		j1.setEnabled(true);
		j2.setEnabled(true);
		j3.setEnabled(true);
		j4.setEnabled(true);
		j5.setEnabled(false);
		j6.setEnabled(true);
		colorChooser.setVisible(true);
		slider.setVisible(true);
		linetool = 4;
	}

	private void Button4Action() {
		j1.setEnabled(true);
		j2.setEnabled(true);
		j3.setEnabled(true);
		j4.setEnabled(false);
		j5.setEnabled(true);
		j6.setEnabled(true);
		colorChooser.setVisible(true);
		slider.setVisible(true);
		linetool = 3;
	}

	private void Button3Action() {
		j1.setEnabled(true);
		j2.setEnabled(true);
		j3.setEnabled(false);
		j4.setEnabled(true);
		j5.setEnabled(true);
		j6.setEnabled(true);
		colorChooser.setVisible(false);
		slider.setVisible(false);
		linetool = 2;
	}

	private void Button2Action() {
		j1.setEnabled(true);
		j2.setEnabled(false);
		j3.setEnabled(true);
		j4.setEnabled(true);
		j5.setEnabled(true);
		j6.setEnabled(true);
		colorChooser.setVisible(false);
		slider.setVisible(false);
		linetool = 1;
	}

	private void Button1Action() {
		j1.setEnabled(false);
		j2.setEnabled(true);
		j3.setEnabled(true);
		j4.setEnabled(true);
		j5.setEnabled(true);
		j6.setEnabled(true);
		colorChooser.setVisible(false);
		slider.setVisible(false);
		linetool = 0;
	}
	public int getTool() {
		return tool;
	}
	public void setTool(int tool) {
		if(this.tool!=tool)
		{
			ready = false;
		}
		this.tool = tool;
	}

	public int getLinetool() {
		return linetool;
	}

	public void setLinetool(int linetool) {
		this.linetool = linetool;
	}
	
	/**
	 * Przyjmuje ParameterBox i ustanawia wartosc lokalnego jako wartoœæ przyjêtego
	 * Dostosowyje do niego równie¿ ustawinia kolorów i lini 
	 * @param PB
	 */
	public void setParameterBox(ParameterBox PB)
	{
		this.parameterBox = PB;
		colorChooser.setColor(PB.LineColor);
		slider.setValue((int)(PB.LineSize*100));
	}
	
	public void setWindowVisible(boolean state)
	{
		setVisible(state);
	}

	public Rectangle getRec() {
		return rec;
	}

	public void setRec(Rectangle rec) {
		this.rec = rec;
	}
}