package comp303.fivehundred.model;

import comp303.fivehundred.util.Card.Suit;

/**
 * Represents a bid or a contract for a player. Immutable.
 * @author Jake Shamash
 */
public class Bid implements Comparable<Bid>
{
	private int aTricks;
	private Suit aSuit;			// null if no trump
	private boolean aPass;
	
	/**
	 * Constructs a new standard bid (not a pass) in a trump.
	 * @param pTricks Number of tricks bid. Must be between 6 and 10 inclusive.
	 * @param pSuit Suit bid. A null value indicates no trump.
	 * @pre pTricks >= 6 && pTricks <= 10
	 */
	public Bid(int pTricks, Suit pSuit)
	{
		this.aTricks = pTricks;
		this.aSuit = pSuit;
		this.aPass = false;
	}
	
	/**
	 * Constructs a new passing bid.
	 */
	public Bid()
	{
		this.aPass = true;
	}
	
	/**
	 * Creates a bid from an index value between 0 and 24 representing all possible
	 * bids in order of strength.
	 * @param pIndex 0 is the weakest bid (6 spades), 24 is the highest (10 no trump),
	 * and everything in between.
	 * @pre pIndex >= 0 && pIndex <= 24
	 */
	public Bid(int pIndex)
	{
		final int lMaxIndex = 24;
		final int lNumSuits = 5;
		final int lMinTrick = 6;
		assert pIndex >= 0 && pIndex <= lMaxIndex;
		
		switch(pIndex % lNumSuits)
		{
			case 0:	this.aSuit = Suit.SPADES;
					break;
			case 1:	this.aSuit = Suit.CLUBS;
					break;
			case 2:	this.aSuit = Suit.DIAMONDS;
					break;
			case 3: this.aSuit = Suit.HEARTS;
					break;
			case 4:	this.aSuit = null;
					break;
			default: this.aSuit = null;
		}
		
		this.aTricks = (pIndex / lNumSuits) + lMinTrick;
		this.aPass = false;
	}
	
	/**
	 * @return The suit the bid is in, or null if it is in no-trump.
	 * @throws ModelException if the bid is a pass.
	 */
	public Suit getSuit()
	{
		if (this.aPass)
		{
			throw new ModelException("Cannot retrieve suit: This bid is a pass.");
		}
		
		return this.aSuit;
	}
	
	/**
	 * @return The number of tricks bid.
	 * @throws ModelException if the bid is a pass.
	 */
	public int getTricksBid()
	{
		if (this.aPass)
		{
			throw new ModelException("Cannot retrieve tricks: This bid is a pass.");
		}
		
		return this.aTricks;
	}
	
	/**
	 * @return True if this is a passing bid.
	 */
	public boolean isPass()
	{
		return this.aPass;
	}
	
	/**
	 * @return True if the bid is in no trump.
	 */
	public boolean isNoTrump()
	{
		if (this.isPass())
		{
			return false;
		}
		
		if (this.aSuit == null)
		{
			return true;
		}
		
		return false;
	}

	@Override
	public int compareTo(Bid pBid)
	{
		 int rank1;			// Rank for this bid
		 int rank2;			// Rank for pBid
		 
		 if (this.isPass())
		 {
			 rank1 = -1;
		 }
		 else
		 {
			 rank1 = this.toIndex();
		 }
		 
		 if (pBid.isPass())
		 {
			 rank2 = -1;
		 }
		 else
		 {
			 rank2 = pBid.toIndex();
		 }
		 
		 return rank1 - rank2;		 
	}
	
	/**
	 * @see java.lang.Object#toString()
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		if (this.aPass)
		{
			return "PASS";
		}
		
		String suitName;
		if (this.aSuit == null)
		{
			suitName = "NO TRUMP";
		}
		else
		{
			suitName = this.aSuit.toString();
		}
		
		return this.aTricks + " " + suitName;
	}

	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object pBid)
	{
		if (pBid instanceof Bid)
		{
			if (this.compareTo((Bid) pBid) == 0)
			{
				return true;
			}
		}
		
		return false;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		final int lPass = -1;
		if (this.aPass)
		{
			return lPass;
		}
		
		return this.toIndex();
	}

	/**
	 * Converts this bid to an index in the 0-24 range.
	 * @return 0 for a bid of 6 spades, 24 for a bid of 10 no-trump,
	 * and everything in between.
	 * @throws ModelException if this is a passing bid.
	 */
	public int toIndex()
	{
		if (this.isPass())
		{
			throw new ModelException("This is a passing bid.");
		}
		
		final int lNumSuits = 4;
		final int lMinTrick = 6;
		final int lNumTricks = 5;
		
		int lReturn;
		int suitRank;
		
		if (this.aSuit == null)
		{
			suitRank = lNumSuits;
		}
		else
		{
			suitRank = this.aSuit.ordinal();
		}
		
		lReturn = (this.getTricksBid() - lMinTrick) * lNumTricks + suitRank;
	
		return lReturn;
	}
	
	/**
	 * Returns the highest bid in pBids. If they are all passing
	 * bids, returns pass.
	 * @param pBids The bids to compare.
	 * @return the highest bid.
	 */
	public static Bid max(Bid[] pBids)
	{
		Bid maxBid = null;
		for (Bid b : pBids)
		{
			if (b == null)
			{
				continue;
			}
			
			if (b.isPass())
			{
				continue;
			}
			else if (maxBid == null || b.toIndex() > maxBid.toIndex())
			{
				maxBid = b;
			}
		}
		
		// If they are all passing bids, return pass.
		if (maxBid == null)
		{
			return new Bid();
		}
		
		// Otherwise, return the maximum.
		return maxBid;
	}
	
	/**
	 * @return The score associated with this bid.
	 * @throws ModelException if the bid is a pass.
	 */
	public int getScore()
	{
		if (this.isPass())
		{
			throw new ModelException("This bid is a pass.");
		}
		
		final int lNoTrumpInit = 120;
		final int lColRate = 20;
		final int lColInit = 40;
		final int lRowRate = 100;
		final int lMinTrick = 6;
		
		int initialVal;
		
		/* The initial score value for a given suit is: suitrank * 20 + 40 */
		if (this.aSuit == null)
		{
			initialVal = lNoTrumpInit;
		}
		else
		{
			initialVal = this.aSuit.ordinal() * lColRate + lColInit;
		}
		
		return (this.aTricks - lMinTrick) * lRowRate + initialVal;
	}
}