package comp303.fivehundred.model;

import comp303.fivehundred.ai.AdvancedBiddingStrategy;
import comp303.fivehundred.ai.AdvancedCardExchangeStrategy;
import comp303.fivehundred.ai.BasicPlayingStrategy;
import comp303.fivehundred.gui.FHGUI;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardList;

/**
 * Class containing functionality for human players.
 * 
 * @author Jake Shamash
 */
public class HumanPlayer extends Player
{
	/**
	 * Constructor.
	 * 
	 * @param pName
	 *            The name of the player
	 */
	public HumanPlayer(String pName)
	{
		super(pName);
	}

	@Override
	public Bid selectBid(Bid[] pPreviousBids, Hand pHand)
	{
		if (!FHGUI.getInstance().isAutoPlayOn())
		{
			synchronized (FHGUI.aLock)
			{
				// Tell the GUI that it's the human player's turn.
				FHGUI.getInstance().setTurn();
				FHGUI.getInstance().setPlayerInput(false);
				// Wait until the player has made a move.
				while (!FHGUI.getInstance().hasPlayerInput())
				{
					try
					{
						FHGUI.aLock.wait();
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		if (!FHGUI.getInstance().isAutoPlayOn())
		{
			// Get the choice of the player which is stored in the GUI.
			return FHGUI.getInstance().getBid();
		}

		// If autoplay is on, use the AdvancedBiddingStrategy to select a bid.
		return new AdvancedBiddingStrategy().selectBid(pPreviousBids, pHand);
	}

	@Override
	public CardList selectCardsToDiscard(Bid[] pBids, int pIndex, Hand pHand)
	{
		if (!FHGUI.getInstance().isAutoPlayOn())
		{
			synchronized (FHGUI.aLock)
			{
				FHGUI.getInstance().setPlayerInput(false);
				// Wait until the player has made a move.
				while (!FHGUI.getInstance().hasPlayerInput())
				{
					try
					{
						FHGUI.aLock.wait();
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		if (!FHGUI.getInstance().isAutoPlayOn())
		{
			// Get the choice of the player which is stored in the GUI.
			return FHGUI.getInstance().getCardsToDiscard();
		}

		// If autoplay is on, use the AdvancedCardExchangeStrategy to select a bid.
		return new AdvancedCardExchangeStrategy().selectCardsToDiscard(pBids, pIndex, pHand);
	}

	@Override
	public Card play(Trick pTrick, Hand pHand)
	{
		if (!FHGUI.getInstance().isAutoPlayOn())
		{
			synchronized (FHGUI.aLock)
			{
				// Tell the GUI that it's the human player's turn.
				FHGUI.getInstance().setTurn();
				FHGUI.getInstance().setPlayerInput(false);
				// Wait until the player has made a move.
				while (!FHGUI.getInstance().hasPlayerInput())
				{
					try
					{
						FHGUI.aLock.wait();
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		}

		if (!FHGUI.getInstance().isAutoPlayOn())
		{
			// Get the choice of the player which is stored in the GUI.
			return FHGUI.getInstance().getPlay();
		}

		// If autoplay is on, use the BasicPlayingStrategy to play a card.
		// Could not implement this using advanced playing strategy, since this would require adding an observer to the
		// game engine.
		return new BasicPlayingStrategy().play(pTrick, pHand);
	}
}
