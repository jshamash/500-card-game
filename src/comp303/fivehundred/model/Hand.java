package comp303.fivehundred.model;

import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.Joker;
import comp303.fivehundred.util.Card.Rank;
import comp303.fivehundred.util.Card.Suit;
import comp303.fivehundred.util.CardList;

/**
 * Additional services to manage a card list that corresponds to
 * the cards in a player's hand.
 * @author Abigail White
 */
public class Hand extends CardList
{
	/**
	 * @see java.lang.Object#clone()
	 * {@inheritDoc}
	 */
	@Override
	public Hand clone()
	{
		return (Hand)super.clone();
	}
	
	/**
	 * @param pNoTrump If the contract is in no-trump
	 * @return A list of cards that can be used to lead a trick.
	 */
	public CardList canLead(boolean pNoTrump)
	{
		if (pNoTrump) 
		{ 
			if (this.getNonJokers().size() == 0)
			{
				return this.getJokers();
			}
			return this.getNonJokers();
		}	
		return this;
	}
	
	/**
	 * @return The cards that are jokers.
	 */
	public CardList getJokers()
	{
		CardList hJokers = new CardList();
		
		for (Card lCard : this) 
		{
			if(lCard.isJoker())
			{
				hJokers.add(lCard);
			}
		}
		return hJokers; 
	}
	
	/**
	 * @return The cards that are not jokers.
	 */
	public CardList getNonJokers()
	{
		CardList nJokers = new CardList(); 
		
		for (Card lCard : this)
		{
			if(!lCard.isJoker())
			{
				nJokers.add(lCard);
			}
		}
		return nJokers; 
	}
	
	/**
	 * Returns all the trump cards in the hand, including jokers.
	 * Takes jack swaps into account.
	 * @param pTrump The trump to check for. Cannot be null.
	 * @return All the trump cards and jokers.
	 * @pre pTrump != null
	 */
	public CardList getTrumpCards(Suit pTrump)
	{
		CardList tCards = new CardList();
		
		for(Card lCard : this)
		{
			if(lCard.isJoker())
			{
				tCards.add(lCard);
			}
			else
			{
				Suit tSuit = lCard.getEffectiveSuit(pTrump);
				if(tSuit.equals(pTrump))
				{
					tCards.add(lCard);
				}
			}
		}
		return tCards;
	}
	
	/**
	 * Returns all the cards in the hand that are not trumps or jokers.
	 * Takes jack swaps into account.
	 * @param pTrump The trump to check for. Cannot be null.
	 * @return All the cards in the hand that are not trump cards.
	 * @pre pTrump != null
	 */
	public CardList getNonTrumpCards(Suit pTrump)
	{	
		assert pTrump != null;
		
		CardList tCards = new CardList();
		
		for (Card lCard: this)
		{
			if(!lCard.isJoker())
			{
				Suit tSuit = lCard.getEffectiveSuit(pTrump);
				if(!tSuit.equals(pTrump))
				{
					tCards.add(lCard);
				}
			}
		}
		return tCards;
	}
	
	
	/**
	 * Selects the least valuable card in the hand, if pTrump is the trump.
	 * @param pTrump The trump suit. Can be null to indicate no-trump.
	 * @return The least valuable card in the hand.
	 */
	public Card selectLowest(Suit pTrump)
	{
		CardList sortedList;
		
		if (pTrump != null)
		{
			CardList nonTrumps = this.getNonTrumpCards(pTrump);
			if (nonTrumps.size() == 0)
			{
				sortedList = this.sort(new Card.ByRankComparator());
				
				CardList check = sortedList.clone();
				
				//Reposition left and right bowers appropriately
				for (Card lCard : check)
				{
					if (!lCard.isJoker() && lCard.getRank().equals(Rank.JACK) && lCard.getSuit().equals(pTrump.getConverse()))
					{
						sortedList.remove(lCard);
						sortedList.add(lCard);
					}
				}
				for (Card lCard : check)
				{
					if (!lCard.isJoker() && lCard.getRank().equals(Rank.JACK) && lCard.getSuit().equals(pTrump))
					{
						sortedList.remove(lCard);
						sortedList.add(lCard);
					}
				}
				for (Card lCard : check)
				{
					if (lCard.isJoker() && lCard.getJokerValue().equals(Joker.LOW))
					{
						sortedList.remove(lCard);
						sortedList.add(lCard);
					}
				}
				for (Card lCard : check)
				{
					if (lCard.isJoker() && lCard.getJokerValue().equals(Joker.HIGH))
					{
						sortedList.remove(lCard);
						sortedList.add(lCard);
					}
				}
			}
			else
			{
				sortedList = nonTrumps.sort(new Card.ByRankComparator());
			}
		}
		else
		{
			sortedList = this.sort(new Card.ByRankComparator());
		}
		return sortedList.getFirst();
	}
	
	/**
	 * @param pLed The suit led.
	 * @param pTrump Can be null for no-trump
	 * @return All cards that can legally be played given a lead and a trump.
	 */
	public CardList playableCards( Suit pLed, Suit pTrump )
	{
		CardList rPlay = new CardList();
		
		for (Card lCard : this.getNonJokers())
		{
			if (pTrump == null)
			{
				if (lCard.getSuit().equals(pLed))
				{
					rPlay.add(lCard);
				}
			}
			else
			{
				if(lCard.getEffectiveSuit(pTrump).equals(pLed))
				{
					rPlay.add(lCard);
				}
			}
		}
		
		if (pTrump != null && pLed != null && pLed.equals(pTrump) && this.getJokers().size() != 0)
		{
			for (Card lCard : this.getJokers())
			{
				rPlay.add(lCard);
			}
		}
	
		if(rPlay.size() == 0)
		{
			rPlay = this.clone();
		}
		return rPlay;
	}
	
	/**
	 * Returns the number of cards of a certain suit 
	 * in the hand, taking jack swaps into account.
	 * Excludes jokers.
	 * @param pSuit Cannot be null.
	 * @param pTrump Cannot be null.
	 * @return pSuit Can be null.
	 */
	public int numberOfCards(Suit pSuit, Suit pTrump)
	{
		int num = 0;
		
		for(Card lCard : this)
		{
			if(!lCard.isJoker() && lCard.getEffectiveSuit(pTrump).equals(pSuit))
			{
				num++;
			}
		}
		return num;
	}
}
