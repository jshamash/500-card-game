package comp303.fivehundred.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardImages;

/**
 * A panel that will hold a card image or a place holder for a card.
 * 
 * @author Brandon Hum
 *
 */
@SuppressWarnings("serial")
public class CardPanel extends JPanel
{	
	public static final int TRICK_BUFFER = 0;
	public static final int HAND_BUFFER = 1;
	
	private JLabel card = new JLabel();
	
	/**
	 * A place holder panel either for the trick panel or hand panels.
	 * 
	 * @param pBufferType The type of place holder.
	 * @pre pBufferType == 0 || pBufferType == 1
	 */
	public CardPanel(int pBufferType)
	{
		super();
		
		if (pBufferType == TRICK_BUFFER)
		{
			card.setPreferredSize(new Dimension(73,97));
		}
		else if (pBufferType == HAND_BUFFER)
		{
			card.setPreferredSize(new Dimension(87,87));
		}
		setBackground(FHGUI.bgColor);
		add(card);
	}
	
	/**
	 * CardPanel used to represent the cards of the AI players.
	 * Only displays the back of a card.
	 * 
	 * @param isHorizontal True if a horizontal card is needed, false otherwise.
	 */
	public CardPanel(boolean isHorizontal)
	{
		super();
		
		setLayout(new GridBagLayout());
		
		if (isHorizontal)
		{
			card.setIcon(CardImages.getBackHorizontal());
		}
		else
		{
			card.setIcon(CardImages.getBack());	
		}
		add(card);
		
	}
	
	/**
	 * CardPanel used to represent the cards of the user.
	 * 
	 * @param pCard The card to be displayed by the panel.
	 * @pre pCard != null.
	 */
	public CardPanel(Card pCard)
	{
		super();
		
		setBackground(new Color(164, 56, 56));
		setLayout(new GridBagLayout());

		add(card);
		
		card.setIcon(CardImages.getCard(pCard));
		
		// Set the name of the panel to identify what card it represents.
		setName(pCard.toShortString());
	}
	
	/**
	 * Sets the image of the CardPanel.
	 * 
	 * @param pCard The card to be displayed by the CardPanel.
	 * @pre pCard != null
	 */
	public void setCard(Card pCard)
	{
		card.setIcon(CardImages.getCard(pCard));
	}
	
	/**
	 * Removes the image from the CardPanel.
	 */
	public void setBlank()
	{
		card.setIcon(null);
	}
}