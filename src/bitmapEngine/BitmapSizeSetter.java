package bitmapEngine;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BitmapSizeSetter 
{
	public TextureSizeNameBox startGetter()
	{
		 JTextField x = new JTextField(3);
         JTextField y = new JTextField(3);
         JTextField name = new JTextField(10);
         JPanel message = new JPanel();
         message.add(x);
         message.add(new JLabel(" x "));
         message.add(y);
         message.add(new JLabel(" name "));
         message.add(name);
         int result = JOptionPane.showConfirmDialog(null, message, "Enter map size x y", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
         if (result == JOptionPane.OK_OPTION) {
             String sX = x.getText();
             String sY = y.getText();
             

             try {
                 int X = Integer.parseInt(sX);
                 int Y = Integer.parseInt(sY);
                 JOptionPane.showMessageDialog(null, "You enetered " + X + " x " + Y);
                 return new TextureSizeNameBox(X, Y, name.getText());
             } catch (Exception e) {
                 JOptionPane.showMessageDialog(null, "The values you entered are invalid");
             }
         }
         return null;
	}
}
