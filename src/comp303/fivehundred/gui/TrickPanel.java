package comp303.fivehundred.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import comp303.fivehundred.util.Card;

/**
 * Panel used to display cards discarded by the players during a trick.
 * 
 * @author Brandon Hum
 *
 */
@SuppressWarnings("serial")
public class TrickPanel extends JPanel
{
	private CardPanel[] aPlays = new CardPanel[4]; // Panels where the cards that are played will be displayed.
	private JLabel buffer = new JLabel(); // Space in the middle of the panel used to display a message.
	
	/**
	 * Constructor.
	 */
	public TrickPanel()
	{
		super();
		
		for (int i = 0; i<4; i++)
		{
			// Create empty card panels.
			aPlays[i] = new CardPanel(CardPanel.TRICK_BUFFER);
		}
		
		buffer.setPreferredSize(new Dimension(185,152));
		buffer.setFont(new Font("Comic Sans MS", Font.BOLD, 13));
		
		setBackground(FHGUI.bgColor);
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 1;
		c.gridy = 2;
		add(aPlays[0], c);
		
		c.gridx--;
		c.gridy--;
		add(aPlays[1], c);
		
		c.gridx++;
		add(buffer, c);
		
		c.gridy--;
		add(aPlays[2], c);
		
		c.gridx++;
		c.gridy++;
		add(aPlays[3], c);
	}
	
	/**
	 * Displays the card played by a player.
	 * 
	 * @param pTurn The index of the player.
	 * @param pCard The card that was played.
	 * @pre pTurn >= 0 && pTurn <= 3
	 * @pre pCard != null
	 */
	public void play(int pTurn, Card pCard)
	{
		aPlays[pTurn].setCard(pCard);
	}
	
	/**
	 * Displays a message at the center of the panel saying who won the trick.
	 * 
	 * @param pWinner The name of the winner of the trick.
	 */
	public void winner(String pWinner)
	{
		buffer.setText("  " + pWinner + " wins the trick!");
		buffer.setForeground(Color.BLACK);
	}
	
	/**
	 * Displays a warning message at the center of the panel if the player tries to make an illegal play.
	 */
	public void error()
	{
		buffer.setText(" You cannot play that card");
		buffer.setForeground(Color.RED);
	}
	
	/**
	 * Clears any messages in the panel.
	 */
	public void resetText()
	{
		buffer.setText("");
	}
	
	/**
	 * Clears all cards from the panel.
	 */
	public void reset()
	{
		for (int i = 0; i<4; i++)
		{
			aPlays[i].setBlank();
		}
	}
}
