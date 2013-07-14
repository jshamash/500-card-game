package comp303.fivehundred.ai;

import comp303.fivehundred.model.Hand;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.Joker;
import comp303.fivehundred.util.Card.Suit;
import comp303.fivehundred.util.CardList;

/**
 * If leading, picks a card at random except jokers if playing in no trump.
 * If following, select the lowest playable card that can win either by
 * following suit or with a trump if possible.  If no cards can win, pick
 * the lowest overall.
 * 
 * @author Ashley Kyung Min Kim
 *
 */
public class BasicPlayingStrategy implements IPlayingStrategy
{

	@Override
	public Card play(Trick pTrick, Hand pHand)
	{
		Suit leading;
		Suit trump = pTrick.getTrumpSuit();
		Card highest;
		
		// Select a card to play in the case of lead.
		if (pTrick.size() == 0)
		{
			return leadingPlay(pHand, trump);
		}
		
		leading = pTrick.getSuitLed();
		highest = pTrick.highest();
		
		// Select a card to play in the case where a joker was led.
		if (pTrick.jokerLed())
		{
			return jokerLeadPlay(pHand, trump);
		}
		
		// Select a card to play in the case where a trump or joker has been played.
		if (highest.isJoker() || highest.getEffectiveSuit(trump).equals(trump))
		{
			return trumpPlay(pHand, highest, leading, trump);
		}
		
		// Select a card to play in the case where no trump or jokers have been played.
		return suitFollowPlay(highest, pHand, leading, trump);
	}
	
	/**
	 * Select a card to play in a lead situation.
	 * @param pHand The player's hand.
	 * @param pTrump The trump suit, can be null for no trump.
	 * @return The card to be played.
	 */
	public Card leadingPlay(Hand pHand, Suit pTrump)
	{
		CardList leadable;
		
		// Select a card at random except jokers if in no trump.
		if (pTrump == null)
		{
			leadable = pHand.canLead(true);
		}
		else
		{
			leadable = pHand.canLead(false);
		}
		return leadable.random();
	}
	
	/**
	 * Select a card to play when a joker has led.
	 * @param pHand The player's hand.
	 * @param pTrump The trump suit, can be null for no trump.
	 * @return The card to be played.
	 */
	public Card jokerLeadPlay(Hand pHand, Suit pTrump)
	{
		// Play a high joker if possible.
		for (Card lCard : pHand)
		{
			if (lCard.isJoker() && lCard.getJokerValue().equals(Joker.HIGH))
			{
				return lCard;
			}
		}
		// Play the lowest card in the hand.
		return pHand.selectLowest(pTrump);
	}
	
	/**
	 * Select a card to play when a trump has been played.
	 * @param pHand The player's hand.
	 * @param pHighest The highest card currently in the trick.
	 * @param pLed The leading suit.
	 * @param pTrump The trump suit, can be null for no trump.
	 * @return The card to be played.
	 */
	public Card trumpPlay(Hand pHand, Card pHighest, Suit pLed, Suit pTrump)
	{
		Hand playable = new Hand();
		Hand ofSuit = new Hand();
		CardList ofTrump = new CardList();
		
		for (Card lCard : pHand)
		{
			if (!lCard.isJoker() && lCard.getEffectiveSuit(pTrump).equals(pLed))
			{
				ofSuit.add(lCard);
			}
		}
		
		ofTrump = pHand.getTrumpCards(pTrump);
		
		// If there are cards that follow suit, choose the lowest.
		if (ofSuit.size() != 0)
		{
			return ofSuit.selectLowest(pTrump);
		}
		
		// If a joker was played, play a high joker if possible, else select the lowest playable card.
		if (pHighest.isJoker())
		{
			for (Card lCard : pHand)
			{
				if (lCard.isJoker() && lCard.getJokerValue().equals(Joker.HIGH))
				{
					return lCard;
				}
			}
			
			for (Card lCard : pHand.playableCards(pLed, pTrump))
			{
				playable.add(lCard);
			}
			return playable.selectLowest(pTrump);
		}
		
		// If no cards that follow suit, play lowest trump available that can win
		Card lCard = getLowestWinningCard(pHighest, ofTrump, pTrump);
		if (lCard != null)
		{
			return lCard;
		}
		
		// If no cards that follow suit or trump cards that can win, select lowest card in hand.
		return pHand.selectLowest(pTrump);
	}
	
	/**
	 * Select a card to play when no jokers or trumps have been played.
	 * @param pHighest The current highest card in the trick.
	 * @param pHand The player's hand.
	 * @param pLed The leading suit.
	 * @param pTrump The trump suit, can be null for no trump.
	 * @return The card to be played.
	 */
	public Card suitFollowPlay(Card pHighest, Hand pHand, Suit pLed, Suit pTrump)
	{
		CardList ofSuit = new CardList();
		CardList ofTrump = pHand.getTrumpCards(pTrump);
		
		for (Card lCard : pHand)
		{
			if (!lCard.isJoker() && lCard.getEffectiveSuit(pTrump).equals(pLed))
			{
				ofSuit.add(lCard);
			}
		}
		
		// Play the lowest suit following card that can win, otherwise play the lowest suit following card
		if (ofSuit.size() != 0)
		{
			Card lCard = getLowestWinningCard(pHighest, ofSuit, pTrump);
			if (lCard == null)
			{
				return ofSuit.sort(new Card.ByRankComparator()).getFirst();
			}
			return lCard;
		}
		
		ofTrump = pHand.getTrumpCards(pTrump);
		
		// Play the lowest trump if available
		if (ofTrump.size() != 0)
		{
			return ofTrump.sort(new Card.ByRankComparator()).getFirst();
		}
		
		// If no suit following cards or trump cards, play the lowest valued card in hand.
		return pHand.selectLowest(pTrump);
	}
	
	/**
	 * Get the lowest card in pList that beats pHighest.
	 * @param pHighest The card to be compared to.
	 * @param pList The cards to be checked.
	 * @param pTrump The current trump suit, can be null for no trump.
	 * @return The lowest card in the list that beats pHighest, or null if none exists.
	 */
	public Card getLowestWinningCard(Card pHighest, CardList pList, Suit pTrump)
	{
		Hand winnable = new Hand();
		for (Card lCard : pList)
		{
			if (lCard.compareTo(pHighest) > 0)
			{
				winnable.add(lCard);
			}
		}
		
		if (winnable.size() == 0)
		{
			return null;
		}
		
		return winnable.selectLowest(pTrump);
	}
}