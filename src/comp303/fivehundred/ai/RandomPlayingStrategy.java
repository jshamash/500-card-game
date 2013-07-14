package comp303.fivehundred.ai;

import comp303.fivehundred.model.Hand;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.util.Card;

/**
 * If leading, picks a card at random except a joker if the contract is in no trump.
 * If not leading and the hand contains cards that can follow suit, pick a suit-following 
 * card at random. If not leading and the hand does not contain cards that can follow suit,
 * pick a card at random (including trumps, if available).
 * 
 * @author Brandon Hum
 */
public class RandomPlayingStrategy implements IPlayingStrategy
{
	@Override
	public Card play(Trick pTrick, Hand pHand)
	{
		//check if leading
		if (pTrick.size() == 0)
		{
			if (pTrick.getTrumpSuit() == null)
			{
				return pHand.canLead(true).random();  //select a legal lead card in no trump at random
			}
			return pHand.canLead(false).random();  //select a legal lead card in trump at random
		}
		return pHand.playableCards(pTrick.getSuitLed(), pTrick.getTrumpSuit()).random();  //select a legal card to play at random
	}
}
