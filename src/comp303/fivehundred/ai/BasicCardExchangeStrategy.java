package comp303.fivehundred.ai;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.Suit;
import comp303.fivehundred.util.CardList;

/**
 * Selects the 6 lowest cards in the hand to discard.
 * 
 * @author Ashley Kyung Min Kim
 *
 */
public class BasicCardExchangeStrategy implements ICardExchangeStrategy
{
	@Override
	public CardList selectCardsToDiscard(Bid[] pBids, int pIndex, Hand pHand)
	{
		final int lDiscardAmount = 6;
		
		CardList lList = new CardList();
		Suit aTrump = Bid.max(pBids).getSuit();
		// Use a clone of pHand to avoid side effects.
		Hand aHand = pHand.clone();
		
		for(int i = 0; i < lDiscardAmount; i++)
		{
			Card aCard = aHand.selectLowest(aTrump);
			lList.add(aCard);
			aHand.remove(aCard);
		}
		
		return lList;
	}
}
