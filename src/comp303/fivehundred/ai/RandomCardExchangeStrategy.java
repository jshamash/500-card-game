package comp303.fivehundred.ai;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.util.CardList;

/**
* Picks six cards at random. 
* 
* @author Ashley Kyung Min Kim
*/
public class RandomCardExchangeStrategy implements ICardExchangeStrategy
{
	@Override
	public CardList selectCardsToDiscard(Bid[] pBids, int pIndex, Hand pHand)
	{	
		CardList lList = new CardList();
		final int lSize = 6;
		
		//Keeps selecting cards until 6 are chosen, ignoring duplicates
		while (!(lList.size() == lSize))
		{
			lList.add(pHand.random());
		}

		return lList;
	}

}
