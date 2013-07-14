package comp303.fivehundred.gui;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Panel used to display information and error messages to the user.
 * 
 * @author Brandon Hum
 *
 */
@SuppressWarnings("serial")
public class InfoPanel extends JPanel
{
	private JLabel[] aInfo = new JLabel[5];
	
	public InfoPanel()
	{
		super();
		
		setLayout(new GridLayout(5,1));
		
		setBackground(FHGUI.bgColor);
		
		for (int i = 0; i<5; i++)
		{
			aInfo[i] = new JLabel(" ");
			add(aInfo[i]);
		}
	}
	
	/**
	 * Displays a message on the info panel at the specified line.
	 * 
	 * @param pLabel The line at which the message will be displayed.
	 * @param pInfo The message to be displayed.
	 * @pre pLabel >= 0 && pLabel <= 4
	 */
	public void setInfo(int pLabel, String pInfo)
	{
		aInfo[pLabel].setText(pInfo);
		aInfo[pLabel].setHorizontalAlignment(JLabel.CENTER);
	}
	
	/**
	 * Displays an error message at the bottom of the info panel in red.
	 * 
	 * @param pError The error message to be displayed.
	 */
	public void setError(String pError)
	{
		aInfo[4].setText(pError);
		aInfo[4].setForeground(Color.RED);
	}
	
	/**
	 * Clears the info panel.
	 */
	public void clearInfo()
	{
		for (JLabel l : aInfo)
		{
			l.setText(" ");
		}
		validate();
	}
}
