package comp303.fivehundred.gui;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.util.Card;

/**
 * Interface for GameObservers.
 * 
 * @author Brandon Hum
 *
 */
public interface GameObserver
{
	/**
	 * Called when a new hand is dealt.
	 * 
	 * @param pNewHand The new hand of the player.
	 * @pre pNewHand != null
	 */
	void newHand(Hand pNewHand);
	
	/**
	 * Called when a new bid is made.
	 * 
	 * @param pName The name of the player who made the bid.
	 * @param pBid The bid that was made.
	 * @pre pName != null
	 * @pre pBid != null
	 */
	void newBid(String pName, Bid pBid);
	
	/**
	 * Called when cards have been exchanged.
	 * 
	 * @param pHand The new hand of the player.
	 * @pre pHand != null
	 */
	void exchangeHand(Hand pHand);
	
	/**
	 * Called when a card is discarded.
	 * 
	 * @param pCard The card that was discarded.
	 * @pre pCard != null
	 */
	void discard(Card pCard);
}
