package comp303.fivehundred.model;

import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.BySuitNoTrumpComparator;
import comp303.fivehundred.util.Card.BySuitComparator;
import comp303.fivehundred.util.Card.Suit;
import comp303.fivehundred.util.CardList;


/**
 * A card list specialized for handling cards discarded
 * as part of the play of a trick.
 * @author Jake Shamash
 */
public class Trick extends CardList
{
	private Bid aContract;
	
	/**
	 * Constructs a new empty trick for the specified contract.
	 * @param pContract The contract that this trick is played for.
	 * @throws ModelException if pContract is a passing Bid.
	 */
	public Trick(Bid pContract)
	{
		if (pContract.isPass())
		{
			throw new ModelException("A Trick's contract cannot be a pass");
		}
		this.aContract = pContract;
	}
	
	/**
	 * @return Can be null for no-trump.
	 */
	public Suit getTrumpSuit()
	{
		return aContract.getSuit();
	}
	
	
	/**
	 * @return The effective suit led. Returns null if led with Joker.
	 * @throws ModelException if the trick is empty.
	 */
	public Suit getSuitLed()
	{
		if (this.size() == 0)
		{
			throw new ModelException("This trick is empty!");
		}
		return this.getFirst().getEffectiveSuit(aContract.getSuit());
	}
	
	/**
	 * @return True if a joker led this trick
	 * @throws ModelException if the trick is empty.
	 */
	public boolean jokerLed()
	{
		if (this.size() == 0)
		{
			throw new ModelException("This trick is empty!");
		}
		return this.getFirst().isJoker();
	}
	
	/**
	 * @return The card that led this trick
	 * @pre size() > 0
	 */
	public Card cardLed()
	{
		assert this.size() > 0;
		return this.getFirst();
	}

	/**
	 * @return Highest card that actually follows suit (or trumps it).
	 * I.e., the card currently winning the trick.
	 * @pre size() > 0
	 */
	public Card highest()
	{
		assert size() > 0;
		CardList leadOrTrump = new CardList();
		Suit trump = this.getTrumpSuit();
		Suit lead = null;
		if (!this.getFirst().isJoker())
		{
			lead = this.getFirst().getSuit();
		}
		
		for (Card lCard : this)
		{
			if (lCard.isJoker() || (trump != null && lCard.getEffectiveSuit(trump).equals(trump)) || lCard.getSuit().equals(lead))
			{
				leadOrTrump.add(lCard);
			}
		}
		
		// Case 1: No trump
		if (this.aContract.getSuit() == null)
		{
			return leadOrTrump.sort(new BySuitNoTrumpComparator()).getLast();
		}
		
		// Case 2: Trump
		return leadOrTrump.sort(new BySuitComparator(aContract.getSuit())).getLast();
	}
	
	/**
	 * @return The index of the card that wins the trick.
	 * @pre size() > 0
	 */
	public int winnerIndex()
	{
		assert this.size() > 0;
		
		Card winner = this.highest();
		int index = -1;
		
		// Stop when we reach the winner
		for (Card c : this)
		{
			index++;
			if (c.equals(winner))
			{
				break;
			}
		}
		
		return index;
	}
	
	/**
	 * Determines whether a card could win this trick.
	 * @param pCard The card in question
	 * @return True if pCard could win the trick.
	 */
	public boolean wouldWin(Card pCard)
	{
		Trick t = (Trick) this.clone();
		t.add(pCard);
		if (t.highest().equals(pCard))
		{
			return true;
		}
		
		return false;
	}
}
