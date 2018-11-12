package main;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import bitmapEngine.BitmapSizeSetter;
import bitmapEngine.TextureSizeNameBox;
import structures.Animation;
import toolBox.frameCupture;

public class LayerManager  extends JInternalFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private JTable objectList;
	private int lastFrameIndex = 0;
	private JButton addNewObject, addNewBitmap, removeChoosen, addNewBitmapLayer;
	private JScrollPane scrollpane;
	private final String[] tabHeader = {"Index", "Name", "Type"};
	private final String[] obiectTypes = {"Vector", "Bitmap"};
	private boolean toAddObject, toAddBitmap, toRemove, toGenerateBitmap;
	private boolean refresh;
	private String name; 
	private String path;
	private Rectangle rec;
	
	public LayerManager() 
	{
		setClosable(false);
		setResizable(true);
		setMaximizable(false);
		setMaximizable(false);
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setSize(300, 400);
		setLocation(1500, 600);
		setVisible(true);
		String[][] actTab = new String[1][3];
		TableModel tm = new DefaultTableModel(actTab, tabHeader);
		objectList = new JTable(tm);
		objectList.setFillsViewportHeight(true);
		scrollpane = new JScrollPane(objectList);
		scrollpane.setPreferredSize(new Dimension(200, 300));
		scrollpane.createHorizontalScrollBar();
		add(scrollpane);
		addNewObject = new JButton(new ImageIcon("img/vector.png"));
		addNewBitmap = new JButton(new ImageIcon("img/bitmap.png"));
		removeChoosen = new JButton(new ImageIcon("img/bin.png"));
		addNewBitmapLayer = new JButton(new ImageIcon("img/bitmapSet.png"));
		addNewBitmap.addActionListener(this);
		addNewObject.addActionListener(this);
		removeChoosen.addActionListener(this);
		addNewBitmapLayer.addActionListener(this);
		addNewObject.setPreferredSize(new Dimension(32, 32));
		addNewBitmap.setPreferredSize(new Dimension(32, 32));
		removeChoosen.setPreferredSize(new Dimension(32, 32));
		addNewBitmapLayer.setPreferredSize(new Dimension(32, 32));
		add(addNewObject);
		add(addNewBitmap);
		add(addNewBitmapLayer);
		add(removeChoosen);
		setRec(getBounds());
		refreshWindowSetSize();
		
	}
	
	public void ubdateTab(Animation a)
	{
		if(toAddObject)
		{
			toAddObject = false;
			a.getFrame().addObj(new renderSource.Object());
			refresh = true;
			frameCupture.hasFrame=false;
		}
		if(toAddBitmap)
		{
			toAddBitmap = false;
			
			try {
				if(choosePath())
				{
					System.out.println("Dodawanie bitmapy "+path);
					a.getFrame().addBitmap(name, path, 50, 60, 1, 1, 0);
					refresh = true;
					frameCupture.hasFrame = false;
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			
		}
		if(toGenerateBitmap)
		{
			toGenerateBitmap = false;
			try {
				BitmapSizeSetter bss = new BitmapSizeSetter();
				TextureSizeNameBox tnb = bss.startGetter();
				if(tnb != null)
				{
					System.out.println("Dodawanie wygenerowanej bitmapy o rozmiarze "+tnb.x+" "+tnb.y+" oraz nazwie "+tnb.name);
					a.getFrame().addBitmap(tnb.name, tnb.x, tnb.y);
					refresh = true;
					frameCupture.hasFrame = false;
				}
			} catch (Exception e) {
			// TODO: handle exception
			}	
			
		}
		if(a.getCurrentFrame()!=lastFrameIndex||refresh)
		{
			if(refresh)
				refresh = false;
			System.out.println("Odœwierzanie tablicy layer managera "+a.getFrame().getObjTabSize());
			lastFrameIndex = a.getCurrentFrame();
			int rowNum = objectList.getModel().getRowCount();
			System.out.println("iloœæ w kolumnie "+ rowNum);
			DefaultTableModel dtm = (DefaultTableModel) objectList.getModel();
			for(int i = 0; i < rowNum; i++)
			{
				dtm.removeRow(0);
			}
			System.out.println("Ilosæ warstw w nowej klatce "+a.getFrame().getQueneTabSize());
			for(int i = 0; i < a.getFrame().getQueneTabSize(); i++)
			{
				//System.out.println(obiectTypes[a.getFrame().getType(i)]);
				if(a.getFrame().getType(i)==0)
					dtm.addRow(new String[]{""+(i+1), "Layer"+(i+1), ""+obiectTypes[a.getFrame().getType(i)]});
				else
					dtm.addRow(new String[]{""+(i+1), Window.tm.getTexture(a.getFrame().getBitmap(a.getFrame().getQueneTab().get(i).index).getBitmapIndex()).getName(), ""+obiectTypes[a.getFrame().getType(i)]});
			}
		}
		//System.out.println("Koniec");
	}
	
	private boolean choosePath(){
		 JFrame parentFrame = new JFrame();
		 
		 JFileChooser chooser = new JFileChooser();
		 chooser.setDialogTitle("Specify a file to save");  
		 chooser.setAcceptAllFileFilterUsed(true);
		 
		 int userSelection = chooser.showSaveDialog(parentFrame);
		  
		 if (userSelection == JFileChooser.APPROVE_OPTION) {
			 name = chooser.getSelectedFile().getName();
			 path = chooser.getSelectedFile().getAbsolutePath();
			 System.out.println(path);
			 return true;
		 }
		 else
			 return false;
		 
		 }		
	
	public void refreshWindowSetSize()
	{
		int w = getWidth();
		int h = getHeight();
		scrollpane.setPreferredSize(new Dimension((int) (w*0.95f), (int) (h*0.8f)));
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object in = arg0.getSource();
		
		if(in==addNewBitmap&&!toAddBitmap)
		{
			toAddBitmap = true;
		}else if(in==addNewObject&&!toAddObject)
		{
			toAddObject = true;
		}else if(in==removeChoosen&&!toRemove)
		{
			toRemove = true;
		}else if(in==addNewBitmapLayer&&!toGenerateBitmap)
		{
			toGenerateBitmap = true;
		}
		
	}

	public Rectangle getRec() {
		return rec;
	}

	public void setRec(Rectangle rec) {
		this.rec = rec;
	}

}
