package comp303.fivehundred.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import comp303.fivehundred.gui.external.OverlapLayout;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardList;

/**
 * Panel which represents the hand of a player.
 * 
 * @author Brandon Hum
 * 
 */
@SuppressWarnings("serial")
public class HandPanel extends JPanel
{
	public final static int TOP = 0;
	public final static int LEFT_RIGHT = 1;
	public final static int BOTTOM = 2;

	private int side; // The position of the hand.
	private boolean aExchange; // Flag used to determine if the game is in the exchange state.
	private boolean aPlay; // Flag used to determine if the game is in the play state.
	private boolean aSelectable; // Flag used to determine if it is the player's turn.
	private int aSelected; // Count for how many cards have been selected by the player.
	List<CardPanel> aPlayerHand; // List of cards in the hand.

	/**
	 * Constructor for a HandPanel.
	 * 
	 * @param pSide The position of the hand.
	 * @pre pSide >= 0 && pSide <= 2
	 */
	public HandPanel(int pSide)
	{
		super();

		aExchange = false;
		aPlay = false;
		aSelectable = false;

		side = pSide;

		TitledBorder b = new TitledBorder("");

		// Remove the LineBorder
		b.setBorder(new EmptyBorder(0, 0, 0, 0));

		b.setTitleJustification(TitledBorder.CENTER);

		setBorder(b);

		setBackground(FHGUI.bgColor);

		// Set how the cards will be overlayed.
		if (pSide == LEFT_RIGHT)
		{
			setLayout(new OverlapLayout(new Point(0, 30)));
			setMinimumSize(new Dimension(107, 366));
		}
		else
		{
			setLayout(new OverlapLayout(new Point(30, 0)));
			setMinimumSize(new Dimension(353, 120));
		}

		// Set insets for the human player's hand.
		if (pSide == BOTTOM)
		{
			Insets ins = new Insets(10, 0, 0, 0);
			((OverlapLayout) getLayout()).setPopupInsets(ins);
		}
	}

	/**
	 * Sets the name of the player associated with the hand.
	 * 
	 * @param pName The name of the player.
	 */
	public void setName(String pName)
	{
		((TitledBorder) (getBorder())).setTitle(pName);
	}

	/**
	 * Sets the color of the player's name to blue, indicating that it is their turn.
	 */
	public void setTurn()
	{
		((TitledBorder) (getBorder())).setTitleColor(Color.BLUE);
		repaint();
		validate();
	}

	/**
	 * Resets the color of the name of the player to black.
	 */
	public void deselect()
	{
		((TitledBorder) (getBorder())).setTitleColor(Color.BLACK);
		repaint();
		validate();
	}

	/**
	 * Sets the cards to be displayed by the HandPanel.
	 * 
	 * @param pHand The hand containing the cards of the player.
	 */
	public void setHand(Hand pHand)
	{
		aPlayerHand = new ArrayList<CardPanel>();

		// Only show face down cards for computer players.
		if (pHand == null)
		{
			for (int i = 0; i < 10; i++)
			{
				addCard(null);
			}
		}
		else
		{
			for (Card c : pHand)
			{
				addCard(c);
			}
		}
	}

	/**
	 * Add a card to the hand as a CardPanel and add a mouse listener to it if it belongs to the human player.
	 * 
	 * @param pCard The card to be added to the hand.
	 */
	private void addCard(Card pCard)
	{
		if (side == LEFT_RIGHT)
		{
			// Add a horizontal face down card.
			add(new CardPanel(true));
		}
		if (side == TOP)
		{
			// Add a vertical face down card.
			add(new CardPanel(false));
		}
		if (side == BOTTOM)
		{
			CardPanel lPanel = new CardPanel(pCard);
			aPlayerHand.add(lPanel);

			// REUSED
			// Based on some code provided by Martin Robillard
			// in his CardPanel class.
			// Add ways to select cards when appropriate.
			lPanel.addMouseListener(new MouseAdapter()
			{
				// During a trick, the card will pop up when the user places their mouse cursor
				// over it, and it will pop back down once the cursor leaves the card.
				public void mouseEntered(MouseEvent e)
				{
					if (!aPlay)
					{
						return;
					}

					Component c = e.getComponent();

					((OverlapLayout) getLayout()).addLayoutComponent(c, OverlapLayout.POP_UP);

					c.getParent().invalidate();
					c.getParent().validate();
				}

				public void mouseExited(MouseEvent e)
				{
					if (!aPlay)
					{
						return;
					}

					Component c = e.getComponent();

					((OverlapLayout) getLayout()).addLayoutComponent(c, OverlapLayout.POP_DOWN);

					c.getParent().invalidate();
					c.getParent().validate();
				}

				public void mousePressed(MouseEvent e)
				{
					Component c = e.getComponent();
					Boolean constraint = ((OverlapLayout) getLayout()).getConstraints(c);

					// If the game is in the exchange state, the player can select cards by clicking
					// on them, causing them to pop up. Only up to six cards may be selected at any
					// time.
					if (aExchange)
					{
						if ((constraint == null || constraint == OverlapLayout.POP_DOWN) && aSelected < 6)
						{
							((OverlapLayout) getLayout()).addLayoutComponent(c, OverlapLayout.POP_UP);
							aSelected++;
						}
						else if (constraint == OverlapLayout.POP_UP)
						{
							((OverlapLayout) getLayout()).addLayoutComponent(c, OverlapLayout.POP_DOWN);
							aSelected--;

						}

						c.getParent().invalidate();
						c.getParent().validate();
					}
					// If the game is in the play state and it is the player's turn, clicking on a card
					// will set that card as the play of the player.
					else if (aSelectable)
					{
						if (!FHGUI.getInstance().getPlayableCards().contains(Card.stringToCard(c.getName())))
						{
							// If the user tries to play a card that cannot be played based on the rules of
							// the game, a warning will be displayed.
							FHGUI.getInstance().invalidPlay();
							return;
						}

						synchronized (FHGUI.aLock)
						{
							aSelectable = false;
							FHGUI.getInstance().setPlay(Card.stringToCard(c.getName()));
							FHGUI.getInstance().setPlayerInput(true);
							FHGUI.aLock.notify();
						}
					}
				}
			});

			add(lPanel);
		}
	}

	/**
	 * Clears the HandPanel.
	 */
	public void reset()
	{
		removeAll();
		validate();
	}

	/**
	 * Removes a card from the hand.
	 * 
	 * @param pCard The card to be discarded.
	 */
	public void discard(Card pCard)
	{
		// If the hand belongs to a computer player, simply remove the first card.
		if (side != BOTTOM)
		{
			remove(0);
		}
		else
		{
			Hand h = new Hand();
			// Create a new hand containing the same cards except for the one to be discarded.
			for (CardPanel c : aPlayerHand)
			{
				if (!c.getName().equals(pCard.toShortString()))
				{
					h.add(Card.stringToCard(c.getName()));
				}
			}
			// Set the new hand.
			reset();
			setHand(h);
		}
		// If there are no more cards in the hand, add an empty card panel as a place holder.
		if (getComponentCount() == 0)
		{
			add(new CardPanel(CardPanel.HAND_BUFFER));
		}
	}

	/**
	 * Signals that the game is in the exchange state.
	 */
	public void setExchange()
	{
		aExchange = true;
	}

	/**
	 * Signals that the game has left the exchange state.
	 */
	public void resetExchange()
	{
		aExchange = false;
	}

	/**
	 * Signals that the game is in the play state.
	 */
	public void setPlay()
	{
		aPlay = true;
	}

	/**
	 * Signals that the game has left the play state.
	 */
	public void resetPlay()
	{
		aPlay = false;
	}

	/**
	 * Returns true if a card can be selected (i.e. it is the player's turn).
	 * 
	 * @return True if a card can be selected, false otherwise.
	 */
	public boolean isSelectable()
	{
		return aSelectable;
	}

	/**
	 * Signals that a card may be selected by the player.
	 */
	public void setSelectable()
	{
		aSelectable = true;
	}

	/**
	 * Signals that a card may no longer be selected by the player.
	 */
	public void resetSelectable()
	{
		aSelectable = false;
	}

	/**
	 * Gathers all the cards that have been selected during exchange as a CardList and
	 * returns it.
	 * 
	 * @return The list of cards to be discarded.
	 */
	public CardList cardsToDiscard()
	{
		CardList lList = new CardList();

		for (CardPanel card : aPlayerHand)
		{
			Boolean constraint = ((OverlapLayout) getLayout()).getConstraints(card);

			// Adds each popped up card to the list.
			if (constraint == OverlapLayout.POP_UP)
			{
				lList.add(Card.stringToCard(card.getName()));
			}
		}

		// Do nothing if less than 6 cards have been selected.
		if (lList.size() == 6)
		{
			aExchange = false;
			aSelected = 0;
			return lList;
		}

		return null;
	}
}
