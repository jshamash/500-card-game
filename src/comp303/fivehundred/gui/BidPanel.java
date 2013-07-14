package comp303.fivehundred.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import comp303.fivehundred.model.Bid;

/**
 * Panel that allows the user to select a bid.
 * 
 * @author Brandon Hum
 *
 */
@SuppressWarnings("serial")
public class BidPanel extends JPanel implements ActionListener
{
	/**
	 * Constructor.
	 */
	public BidPanel()
	{
		super();
	
		setLayout(new GridLayout(6,5));
		
		JButton passButton = new JButton("Pass");
		passButton.addActionListener(this);
		
		TitledBorder b = new TitledBorder("Select a bid:");
		b.setTitleFont(new Font("Comic Sans MS", Font.BOLD, 18));
		b.setBorder(new EmptyBorder(50,7,30,7));

		setBorder(b);
	
		setBackground(new Color(238, 154, 0));
		
		// Set up the buttons used to select bids.
		for (int i = 0; i<25; i++)
		{
			int strength = i / 5 + 6;
			String suit;
			
			// Set the suit value of the bid.
			switch(i%5)
			{
				case 0: suit = " S";
						break;
				case 1: suit = " C";
						break;
				case 2: suit = " D";
						break;
				case 3: suit = " H";
						break;
				default: suit = " NT";
			}
			
			JButton lButton = new JButton(strength + suit);
			lButton.setActionCommand(i + "");
			lButton.addActionListener(this);
			
			add(lButton);
		}
		
		add(passButton);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// Do nothing if the player tries to make a bid while it is not their turn.
		if (!FHGUI.getInstance().isSelectable())
		{
			return;
		}
		
		synchronized(FHGUI.aLock)
		{	
			// Creates a new bid for the user based on their choice.
			if (e.getActionCommand().equals("Pass"))
			{
				FHGUI.getInstance().setBid(new Bid());
			}
			else
			{
				Bid lBid = new Bid(Integer.parseInt(e.getActionCommand()));
				
				// Display a warning message if the player tries to make an invalid bid.
				if (lBid.compareTo(FHGUI.getInstance().highestBid()) <= 0)
				{
					FHGUI.getInstance().setTurn();
					FHGUI.getInstance().invalidBid();
					return;
				}
				
				FHGUI.getInstance().setBid(lBid);
			}
			
			// Prevent the player from making any more selections until appropriate.
			FHGUI.getInstance().resetSelectable();
			
			// Notifies the thread waiting on aLock that the user has made their selection.
			FHGUI.getInstance().setPlayerInput(true);
			FHGUI.aLock.notify();
		}		
	}
}
