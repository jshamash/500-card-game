package comp303.fivehundred.util;

import java.util.Comparator;

/**
 * An immutable description of a playing card.
 * 
 * @author Brandon Hum
 */
public final class Card implements Comparable<Card>
{
	/**
	 * Represents the rank of the card.
	 */
	public enum Rank
	{
		FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE
	}

	/**
	 * Represents the suit of the card.
	 */
	public enum Suit
	{
		SPADES, CLUBS, DIAMONDS, HEARTS;

		/**
		 * @return the other suit of the same color.
		 */
		public Suit getConverse()
		{
			Suit lReturn = this;
			switch (this)
			{
			case SPADES:
				lReturn = CLUBS;
				break;
			case CLUBS:
				lReturn = SPADES;
				break;
			case DIAMONDS:
				lReturn = HEARTS;
				break;
			case HEARTS:
				lReturn = DIAMONDS;
				break;
			default:
				lReturn = this;
			}
			return lReturn;
		}
	}

	/**
	 * Represents the value of the card, if the card is a joker.
	 */
	public enum Joker
	{
		LOW, HIGH
	}

	// If this field is null, it means the card is a joker, and vice-versa.
	private final Rank aRank;

	// If this field is null, it means the card is a joker, and vice-versa.
	private final Suit aSuit;

	// If this field is null, it means the card is not a joker, and vice-versa.
	private final Joker aJoker;

	/**
	 * Create a new card object that is not a joker.
	 * 
	 * @param pRank
	 *            The rank of the card.
	 * @param pSuit
	 *            The suit of the card.
	 * @pre pRank != null
	 * @pre pSuit != null
	 */
	public Card(Rank pRank, Suit pSuit)
	{
		assert pRank != null;
		assert pSuit != null;
		aRank = pRank;
		aSuit = pSuit;
		aJoker = null;
	}

	/**
	 * Creates a new joker card.
	 * 
	 * @param pValue
	 *            Whether this is the low or high joker.
	 * @pre pValue != null
	 */
	public Card(Joker pValue)
	{
		assert pValue != null;
		aRank = null;
		aSuit = null;
		aJoker = pValue;
	}

	/**
	 * @return True if this Card is a joker, false otherwise.
	 */
	public boolean isJoker()
	{
		return aJoker != null;
	}

	/**
	 * @return Whether this is the High or Low joker.
	 */
	public Joker getJokerValue()
	{
		assert isJoker();
		return aJoker;
	}

	/**
	 * Obtain the rank of the card.
	 * 
	 * @return An object representing the rank of the card. Can be null if the card is a joker.
	 * @pre !isJoker();
	 */
	public Rank getRank()
	{
		assert !isJoker();
		return aRank;
	}

	/**
	 * Obtain the suit of the card.
	 * 
	 * @return An object representing the suit of the card
	 * @pre !isJoker();
	 */
	public Suit getSuit()
	{
		assert !isJoker();
		return aSuit;
	}

	/**
	 * Returns the actual suit of the card if pTrump is the trump suit. Takes care of the suit swapping of jacks.
	 * 
	 * @param pTrump
	 *            The current trump. Null if no trump.
	 * @return The suit of the card, except if the card is a Jack and its converse suit is trump. Then, returns the
	 *         trump. If the card is a joker, it returns the trump suit, or null if no trump.
	 */
	public Suit getEffectiveSuit(Suit pTrump)
	{
		if (pTrump == null)
		{
			return aSuit;
		}
		else if (aRank == Rank.JACK && aSuit == pTrump.getConverse())
		{
			return pTrump;
		}
		else if (this.isJoker())
		{
			return pTrump;
		}
		else
		{
			return aSuit;
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 * @return See above.
	 */
	public String toString()
	{
		String lSuit = "S";
		String lRank = "4";
		if (!isJoker())
		{
			if (aRank.equals(Rank.FIVE))
			{
				lRank = "5";
			}
			if (aRank.equals(Rank.SIX))
			{
				lRank = "6";
			}
			if (aRank.equals(Rank.SEVEN))
			{
				lRank = "7";
			}
			if (aRank.equals(Rank.EIGHT))
			{
				lRank = "8";
			}
			if (aRank.equals(Rank.NINE))
			{
				lRank = "9";
			}
			if (aRank.equals(Rank.TEN))
			{
				lRank = "T";
			}
			if (aRank.equals(Rank.JACK))
			{
				lRank = "J";
			}
			if (aRank.equals(Rank.QUEEN))
			{
				lRank = "Q";
			}
			if (aRank.equals(Rank.KING))
			{
				lRank = "K";
			}
			if (aRank.equals(Rank.ACE))
			{
				lRank = "A";
			}

			if (aSuit.equals(Suit.CLUBS))
			{
				lSuit = "C";
			}
			if (aSuit.equals(Suit.DIAMONDS))
			{
				lSuit = "D";
			}
			if (aSuit.equals(Suit.HEARTS))
			{
				lSuit = "H";
			}
		}
		else
		{
			if (aJoker.equals(Joker.LOW))
			{
				lRank = "L";
			}
			else
			{
				lRank = "H";
			}
			lSuit = "J";
		}
		return lRank + lSuit;
	}

	/**
	 * @return A short textual representation of the card
	 */
	public String toShortString()
	{
		String lReturn = "";
		if (isJoker())
		{
			lReturn = aJoker.toString().charAt(0) + "J";
		}
		else
		{
			if (aRank.ordinal() <= Rank.NINE.ordinal())
			{
				lReturn += new Integer(aRank.ordinal() + 4).toString();
			}
			else
			{
				lReturn += aRank.toString().charAt(0);
			}
			lReturn += aSuit.toString().charAt(0);
		}
		return lReturn;
	}

	/**
	 * Converts a string representation of a card to a Card object.
	 * 
	 * @param pCard
	 *            The string to be converted.
	 * @return The Card that is represented by pCard.
	 * @pre pCard is a valid string representation of a card.
	 */
	public static Card stringToCard(String pCard)
	{
		Rank lRank = null;
		Suit lSuit = null;

		if (pCard.equals("HJ"))
		{
			return new Card(Joker.HIGH);
		}
		else if (pCard.equals("LJ"))
		{
			return new Card(Joker.LOW);
		}

		switch (pCard.charAt(0))
		{
		case 'A':
			lRank = Rank.ACE;
			break;
		case 'K':
			lRank = Rank.KING;
			break;
		case 'Q':
			lRank = Rank.QUEEN;
			break;
		case 'J':
			lRank = Rank.JACK;
			break;
		case 'T':
			lRank = Rank.TEN;
			break;
		default:
			lRank = Rank.values()[Integer.parseInt(pCard.charAt(0) + "") - 4];
		}

		switch (pCard.charAt(1))
		{
		case 'S':
			lSuit = Suit.SPADES;
			break;
		case 'C':
			lSuit = Suit.CLUBS;
			break;
		case 'D':
			lSuit = Suit.DIAMONDS;
			break;
		case 'H':
			lSuit = Suit.HEARTS;
			break;
		default:
			break;
		}

		return new Card(lRank, lSuit);
	}

	/**
	 * Compares two cards according to their rank.
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 * @param pCard
	 *            The card to compare to
	 * @return Returns a negative integer, zero, or a positive integer as this object is less than, equal to, or greater
	 *         than pCard
	 * @pre pCard != null
	 */
	public int compareTo(Card pCard)
	{
		assert pCard != null;
		// if pCard is a Joker, return 0 if this is also a Joker or -1 otherwise
		if (pCard.isJoker())
		{
			if (this.isJoker())
			{
				return this.aJoker.ordinal() - pCard.aJoker.ordinal();
			}
			return -1;
		}
		// if this card is a Joker and pCard is not, return 1
		if (this.isJoker() && !pCard.isJoker())
		{
			return 1;
		}
		// if this card and pCard are of different rank, compare based on rank
		if (!this.aRank.equals(pCard.aRank))
		{
			return this.aRank.ordinal() - pCard.aRank.ordinal();
		}
		// if this card and pCard are of same rank, compare based on suit
		return this.aSuit.ordinal() - pCard.aSuit.ordinal();
	}

	/**
	 * Two cards are equal if they have the same suit and rank or if they are two jokers of the same value.
	 * 
	 * @param pCard
	 *            The card to test.
	 * @return true if the two cards are equal
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @pre pCard != null
	 */
	@Override
	public boolean equals(Object pCard)
	{
		assert pCard != null;

		if (this.compareTo((Card) pCard) == 0)
		{
			return true;
		}
		return false;
	}

	/**
	 * The hashcode for a card is the suit*number of ranks + that of the rank (perfect hash).
	 * 
	 * @return the hashcode
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int lLowJoker = 44;
		final int lHighJoker = 45;

		// assign unique hash codes for jokers
		if (this.isJoker())
		{
			if (this.aJoker == Joker.LOW)
			{
				return lLowJoker;
			}
			return lHighJoker;
		}
		// if this card is not a joker, calculate its hash code and return it
		return this.aSuit.ordinal() * Rank.values().length + this.aRank.ordinal();
	}

	/**
	 * Compares cards using their rank as primary key, then suit. Jacks rank between 10 and queens of their suit.
	 */
	public static class ByRankComparator implements Comparator<Card>
	{
		/**
		 * Compares two cards based on rank and then suit.
		 * 
		 * @param pCard1
		 *            is a card that will be compared with pCard2
		 * @param pCard2
		 *            is the card with which pCard1 will be compared
		 * @return Returns a negative integer, zero, or a positive integer if pCard1 is less than, equal to, or greater
		 *         than pCard2
		 * @pre pCard1 != null
		 * @pre pCard2 != null
		 * @pre pCard1 != pCard2
		 */
		@Override
		public int compare(Card pCard1, Card pCard2)
		{
			assert pCard1 != null;
			assert pCard2 != null;
			assert !pCard1.equals(pCard2);

			return pCard1.compareTo(pCard2);
		}
	}

	/**
	 * Compares cards using their suit as primary key, then rank. Jacks rank between 10 and queens of their suit.
	 * 
	 * @pre !pCard1.equals(pCard2)
	 */
	public static class BySuitNoTrumpComparator implements Comparator<Card>
	{
		/**
		 * Compares two cards based on suit and then rank.
		 * 
		 * @param pCard1
		 *            is a card that will be compared with pCard2
		 * @param pCard2
		 *            is the card with which pCard1 will be compared
		 * @return Returns a negative integer, zero, or a positive integer if pCard1 is less than, equal to, or greater
		 *         than pCard2
		 * @pre pCard1 != null
		 * @pre pCard2 != null
		 * @pre pCard1 != pCard2
		 */
		@Override
		public int compare(Card pCard1, Card pCard2)
		{
			assert pCard1 != null;
			assert pCard2 != null;
			assert !pCard1.equals(pCard2);
			// Give High Joker highest priority followed by Low Joker, and then everything else based on suit first then
			// rank
			if (pCard1.isJoker() && pCard2.isJoker())
			{
				return pCard1.aJoker.ordinal() - pCard2.aJoker.ordinal();
			}
			if (pCard1.isJoker())
			{
				return 1;
			}
			if (pCard2.isJoker())
			{
				return -1;
			}
			if (pCard1.aSuit.ordinal() == pCard2.aSuit.ordinal())
			{
				return pCard1.aRank.ordinal() - pCard2.aRank.ordinal();
			}
			return pCard1.aSuit.ordinal() - pCard2.aSuit.ordinal();
		}
	}

	/**
	 * Compares cards using their suit as primary key, then rank. Jacks rank above aces if they are in the trump suit.
	 * The trump suit becomes the highest suit.
	 */
	public static class BySuitComparator implements Comparator<Card>
	{

		private Suit aTrump;

		/**
		 * Constructs a BySuitComparator.
		 * 
		 * @param pTrump
		 *            The trump suit to use for comparison. Can be null for no trump.
		 */
		public BySuitComparator(Suit pTrump)
		{
			this.aTrump = pTrump;
		}

		/**
		 * Compares two cards based on suit and then rank. Jacks in the trump suit or its converse rank higher than
		 * aces.
		 * 
		 * @param pCard1
		 *            is a card that will be compared with pCard2
		 * @param pCard2
		 *            is the card with which pCard1 will be compared
		 * @return Returns a negative integer, zero, or a positive integer if pCard1 is less than, equal to, or greater
		 *         than pCard2
		 * @pre pCard1 != null
		 * @pre pCard2 != null
		 * @pre pCard1 != pCard2
		 */
		@Override
		public int compare(Card pCard1, Card pCard2)
		{
			assert pCard1 != null;
			assert pCard2 != null;
			assert !pCard1.equals(pCard2);

			int lReturn;
			// Give High Joker highest priority followed by Low Joker,
			// then Jack of trump followed by Jack of the converse of the trump,
			// and then everything else based on suit first with trump suit becoming the highest, then rank
			if (pCard1.isJoker() && pCard2.isJoker())
			{
				lReturn = pCard1.aJoker.ordinal() - pCard2.aJoker.ordinal();
			}
			else if (pCard1.isJoker()
					|| (pCard1.aRank.equals(Rank.JACK) && pCard1.aSuit.equals(aTrump) && !pCard2.isJoker()))
			{
				lReturn = 1;
			}
			else if (pCard2.isJoker()
					|| (!pCard1.isJoker() && pCard2.aRank.equals(Rank.JACK) && pCard2.aSuit.equals(aTrump)))
			{
				lReturn = -1;
			}
			else if (pCard1.aRank.equals(Rank.JACK) && pCard1.aSuit.equals(aTrump.getConverse()))
			{
				lReturn = 1;
			}
			else if (pCard2.aRank.equals(Rank.JACK) && pCard2.aSuit.equals(aTrump.getConverse()))
			{
				lReturn = -1;
			}
			else if (pCard1.aSuit.ordinal() == pCard2.aSuit.ordinal())
			{
				lReturn = pCard1.aRank.ordinal() - pCard2.aRank.ordinal();
			}
			else if (pCard1.aSuit.equals(aTrump))
			{
				lReturn = 1;
			}
			else if (pCard2.aSuit.equals(aTrump))
			{
				lReturn = -1;
			}
			else
			{
				lReturn = pCard1.aSuit.ordinal() - pCard2.aSuit.ordinal();
			}
			return lReturn;
		}
	}
}
