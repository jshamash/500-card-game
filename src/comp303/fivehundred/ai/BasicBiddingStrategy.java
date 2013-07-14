package comp303.fivehundred.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.Joker;
import comp303.fivehundred.util.Card.Rank;
import comp303.fivehundred.util.Card.Suit;

/**
 * Goes through a variety of criteria to decide
 * on a good bid.
 * 
 * @author Ashley Kyung Min Kim
 *
 */
public class BasicBiddingStrategy implements IBiddingStrategy
{
	private Map<Rank, Integer> aMap = new HashMap<Rank, Integer>();
	private int aHPoints = 0;
	private int aSPoints = 0;
	private int aDPoints = 0;
	private int aCPoints = 0;
	private int aHCards = 0;
	private int aSCards = 0;
	private int aDCards = 0;
	private int aCCards = 0;
	
	/**
	 * Create a new BasicBiddingStrategy and set up a map that describes how points should be assigned to different cards.
	 */
	public BasicBiddingStrategy()
	{
		ArrayList<Rank> aList = new ArrayList<Rank>();
		
		final int lZeroSet = 7;
		final int lJackPosition = 7;
		final int lQueenPosition = 8;
		final int lKingPosition = 9;
		final int lAcePosition = 10;
		
		// Create a list of ranks
		for (Rank lRank : Rank.values())
		{
			aList.add(lRank);	
		}
		
		// Assign 0 to all ranks from 4 to 10
		for (int i = 0; i < lZeroSet; i++)
		{
			aMap.put(aList.get(i), 0);
		}
		
		// Assign 2 for jacks
		aMap.put(aList.get(lJackPosition), 2);
		
		// Assign 1 for queens
		aMap.put(aList.get(lQueenPosition), 1);
		
		// Assign 2 for kings
		aMap.put(aList.get(lKingPosition), 2);
		
		// Assign 3 for aces
		aMap.put(aList.get(lAcePosition), 3);
	}
	
	@Override
	public Bid selectBid(Bid[] pPreviousBids, Hand pHand)
	{
		aHPoints = 0;
		aSPoints = 0;
		aDPoints = 0;
		aCPoints = 0;
		aHCards = 0;
		aSCards = 0;
		aDCards = 0;
		aCCards = 0;
		
		Suit bidSuit = null;
		int bidStrength = 0;
		Bid lBid;
		
		// Determine number of cards of each suit in pHand.
		suitCounts(pHand);
		// Calculate amount of points to assign to each suit.
		calculateSuitPoints(pHand, pPreviousBids);
		
		// Get the highest biddable suit.
		if(trumpExists())
		{
			bidSuit = selectBiddableSuit();
		}
		
		// Determine the strength of the bid.
		bidStrength = contractStrength(bidSuit);
		
		lBid = new Bid(bidStrength, bidSuit);
		
		// If proposed bid is greater than the highest current bid, make a bid.
		if(lBid.compareTo(Bid.max(pPreviousBids))>0)
		{
			return lBid;
		}
		
		// If proposed bid is not greater than the highest current bid, pass.
		return new Bid();
	}
	
	/**
	 * Calculates the number of points that should be assigned to each suit in pHand.
	 * @param pHand The player's hand.
	 * @param pPreviousBids The array of previous bids.
	 */
	public void calculateSuitPoints(Hand pHand, Bid[] pPreviousBids)
	{
		final int lHighJokerVal = 5;
		final int lLowJokerVal = 4;
		
		int lIndex = getIndex(pPreviousBids);
		int jokerPoints = 0;
		
		for(Card card : pHand)
		{
			if (card.isJoker()) // Establish points by jokers
			{
				if (card.getJokerValue().equals(Joker.HIGH))
				{
					jokerPoints += lHighJokerVal;
				}
				else 
				{
					jokerPoints += lLowJokerVal;
				}
			}
			else // Calculate points for each suit
			{
				if (card.getEffectiveSuit(Suit.HEARTS).equals(Suit.HEARTS))
				{
					aHPoints += aMap.get(card.getRank());
				}
				
				if (card.getEffectiveSuit(Suit.SPADES).equals(Suit.SPADES))
				{
					aSPoints += aMap.get(card.getRank());
				}
				
				if (card.getEffectiveSuit(Suit.DIAMONDS).equals(Suit.DIAMONDS))
				{
					aDPoints += aMap.get(card.getRank());
				}
				
				if (card.getEffectiveSuit(Suit.CLUBS).equals(Suit.CLUBS))
				{
					aCPoints += aMap.get(card.getRank());
				}
			}
		}
		
		// Add joker points to all suits
		aHPoints += jokerPoints;
		aSPoints += jokerPoints;
		aDPoints += jokerPoints;
		aCPoints += jokerPoints;
		
		// Add points if there are many cards of a suit
		suitIsLong();
		
		// Add points if partner has bid a suit
		partnerBid(pPreviousBids, lIndex);
		
		// Remove points if opponent to the right has bid a suit
		rightOpponentBid(pPreviousBids, lIndex);
		
		// Remove points if opponent to the left has bid a suit
		leftOpponentBid(pPreviousBids, lIndex);
	}
	
	/**
	 * Obtain player index based on contents of pPreviousBids.
	 * @param pPreviousBids The array of previous bids.
	 * @return The index of the player.
	 */
	public int getIndex(Bid[] pPreviousBids)
	{
		boolean lCheck = false;
		
		for (int i = 0; i<pPreviousBids.length; i++)
		{
			if (pPreviousBids[i] != null)
			{
				lCheck = true;
			}
			// Check for first null value after a bid. This is the current player's index
			if (pPreviousBids[i] == null && lCheck)
			{
				return i;
			}
		}
		return 0;
	}
	
	/**
	 *  Uses the spread of points across all suits to determine if a trump should be selected.
	 * @return true if possible trump, false if no trump should be bid
	 */
	public Boolean trumpExists()
	{
		final double lThreshold = 0.33;
		
		double sum = aHPoints + aSPoints + aDPoints + aCPoints;
		
		// If no suit is biddable, return false
		if (!isBiddable(Suit.SPADES) && !isBiddable(Suit.CLUBS) && !isBiddable(Suit.DIAMONDS) && !isBiddable(Suit.HEARTS))
		{
			return false;
		}
		
		// If any suit accounts for more than lThreshold of total points, then there can be a trump selected
		if(((aHPoints/sum)>=lThreshold)||((aSPoints/sum)>=lThreshold)||((aDPoints/sum)>=lThreshold)||((aCPoints/sum)>=lThreshold))
		{
			return true;
		}
		
		// If the points are spread across all suits similarly, bid with no trump
		return false;
	}
	
	/**
	 * Count the number of cards of each suit in pHand.
	 * @param pHand The player's hand.
	 */
	public void suitCounts(Hand pHand)
	{
		int jokerCards = 0;
		
		for(Card card:pHand)
		{
			if(card.isJoker()) // Count how many jokers are in the hand.
			{
				jokerCards++;
			}
			else
			{
				if(card.getEffectiveSuit(Suit.HEARTS).equals(Suit.HEARTS))
				{
					aHCards++;
				}
				
				if(card.getEffectiveSuit(Suit.CLUBS).equals(Suit.CLUBS))
				{
					aCCards++;
				}
				
				if(card.getEffectiveSuit(Suit.DIAMONDS).equals(Suit.DIAMONDS))
				{
					aDCards++;
				}
				
				if(card.getEffectiveSuit(Suit.SPADES).equals(Suit.SPADES))
				{
					aSCards++;
				}
			}
		}
		
		aHCards += jokerCards;
		aCCards += jokerCards;
		aDCards += jokerCards;
		aSCards += jokerCards;
	}
	
	/**
	 * Add extra points to a suit if the hand contains more than 5 cards of that suit.
	 */
	public void suitIsLong()
	{
		final int lMany = 5;
		
		if(aHCards>lMany)
		{
			aHPoints += aHCards - lMany;
		}
		
		if(aCCards>lMany)
		{
			aCPoints += aCCards - lMany;
		}
		
		if(aDCards>lMany)
		{
			aDPoints += aDCards - lMany;
		}
		
		if(aSCards>lMany)
		{
			aSPoints += aSCards - lMany;
		}
	}
	
	/**
	 * Add points to a suit if partner has bid that suit already.
	 * @param pPreviousBids The array of previous bids.
	 * @param pIndex The index of the current player.
	 */
	public void partnerBid(Bid[] pPreviousBids, int pIndex)
	{
		final int lBonus = 5;
		
		int lPartnerIndex = (pIndex + 2) % 4;
		
		// If partner has not bid a suit, return.
		if(pPreviousBids[lPartnerIndex] == null || pPreviousBids[lPartnerIndex].isPass() || (pPreviousBids[lPartnerIndex].isNoTrump()))
		{
			return;
		}
		
		if(pPreviousBids[lPartnerIndex].getSuit().equals(Suit.CLUBS))
		{
			aCPoints += lBonus;
		}
			
		if(pPreviousBids[lPartnerIndex].getSuit().equals(Suit.DIAMONDS))
		{
			aDPoints += lBonus;
		}
			
		if(pPreviousBids[lPartnerIndex].getSuit().equals(Suit.HEARTS))
		{
			aHPoints += lBonus;
		}
			
		if(pPreviousBids[lPartnerIndex].getSuit().equals(Suit.SPADES))
		{
			aSPoints += lBonus;
		}
	}
	
	/**
	 * Remove points from a suit if opponent to the right has bid on that suit.
	 * @param pPreviousBids The array of previous bids.
	 * @param pIndex The index of the current player.
	 */
	public void rightOpponentBid(Bid[] pPreviousBids, int pIndex)
	{
		final int lRemove = 3;
		int lRightIndex = (pIndex + 3) % 4;
		
		if (pPreviousBids[lRightIndex] == null)
		{
			return;
		}
		
		if(!(pPreviousBids[lRightIndex].isPass()) && !(pPreviousBids[lRightIndex].isNoTrump()))
		{
			if(pPreviousBids[lRightIndex].getSuit().equals(Suit.CLUBS))
			{
				aCPoints -= lRemove;
			}
				
			if(pPreviousBids[lRightIndex].getSuit().equals(Suit.DIAMONDS))
			{
				aDPoints -= lRemove;
			}
				
			if(pPreviousBids[lRightIndex].getSuit().equals(Suit.HEARTS))
			{
				aHPoints -= lRemove;
			}
				
			if(pPreviousBids[lRightIndex].getSuit().equals(Suit.SPADES))
			{
				aSPoints -= lRemove;
			}
		}
	}
	
	/**
	 * Remove points from a suit if opponent to the left has bid on that suit.
	 * @param pPreviousBids The array of previous bids.
	 * @param pIndex The index of the current player.
	 */
	public void leftOpponentBid(Bid[] pPreviousBids, int pIndex)
	{
		final int lRemove = 3;
		int lLeftIndex = (pIndex + 1) % 4;
		
		if (pPreviousBids[lLeftIndex] == null)
		{
			return;
		}
		
		if(!(pPreviousBids[lLeftIndex].isPass()) && !(pPreviousBids[lLeftIndex].isNoTrump()))
		{
			if(pPreviousBids[lLeftIndex].getSuit().equals(Suit.CLUBS))
			{
				aCPoints -= lRemove;
			}
						
			if(pPreviousBids[lLeftIndex].getSuit().equals(Suit.DIAMONDS))
			{
				aDPoints -= lRemove;
			}
				
			if(pPreviousBids[lLeftIndex].getSuit().equals(Suit.HEARTS))
			{
				aHPoints -= lRemove;
			}
						
			if(pPreviousBids[lLeftIndex].getSuit().equals(Suit.SPADES))
			{
				aSPoints -= lRemove;
			}
		}
	}

	
	/**
	 * Determine the best suit to bid on by comparing the points of all biddable suits.
	 * @return The best suit to bid on.
	 */
	public Suit selectBiddableSuit()
	{
		Suit lSuit;
		int suitPoints;
		
		// If a suit is not biddable, set its points to 0 so that it will not be chosen.
		if(!isBiddable(Suit.CLUBS))
		{
			aCPoints = 0;
		}
		
		if(!isBiddable(Suit.DIAMONDS))
		{
			aDPoints = 0;
		}
		
		if(!isBiddable(Suit.HEARTS))
		{
			aHPoints = 0;
		}
		
		if(!isBiddable(Suit.SPADES))
		{
			aSPoints = 0;
		}
		
		// Determine which biddable suit has the most points.
		if(aCPoints > aDPoints)
		{
			lSuit = Suit.CLUBS;
			suitPoints = aCPoints;
		}
		else
		{
			lSuit = Suit.DIAMONDS;
			suitPoints = aDPoints;
		}
		
		if(aHPoints > suitPoints)
		{
			lSuit = Suit.HEARTS;
			suitPoints = aHPoints;
		}
		
		if(aSPoints > suitPoints)
		{
			lSuit = Suit.SPADES;
		}
		
		return lSuit;
	}
	
	/**
	 * Determines if a suit is biddable based on the number of cards in the hand of that suit.
	 * @param pSuit The suit that will be checked.
	 * @return True if pSuit is biddable, false if not.
	 */
	public Boolean isBiddable(Suit pSuit)
	{
		final int lThresholdCard = 4;
		final int lThresholdPoint = 8;
		
		if(pSuit.equals(Suit.CLUBS))
		{
			if((aCCards>lThresholdCard) && (aCPoints>lThresholdPoint))
			{
				return true;
			}
		}
		
		if(pSuit.equals(Suit.DIAMONDS))
		{
			if((aDCards>lThresholdCard) && (aDPoints>lThresholdPoint))
			{
				return true;
			}
		}
		
		if(pSuit.equals(Suit.HEARTS))
		{
			if((aHCards>lThresholdCard) && (aHPoints>lThresholdPoint))
			{
				return true;
			}
		}
		
		if(pSuit.equals(Suit.SPADES))
		{
			if((aSCards>lThresholdCard) && (aSPoints>lThresholdPoint))
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Determines the strength of the bid based on points of the highest biddable suit.
	 * @param pSuit The suit that will be looked at.
	 * @return The strength of the bid.
	 */
	public int contractStrength(Suit pSuit)
	{
		int points = 0;
		
		final int lTenThreshold = 28;
		final int lNineThreshold = 25;
		final int lEightThreshold = 21;
		final int lSevenThreshold = 17;
		final int lSixThreshold = 13;
		
		final int lTenBid = 10;
		final int lNineBid = 9;
		final int lEightBid = 8;
		final int lSevenBid = 7;
		final int lSixBid = 6;
		
		// Determine the points that should be used to decide on the contract strength.
		if (pSuit == null)
		{
			points += pointsOf(Suit.SPADES) + pointsOf(Suit.CLUBS) + pointsOf(Suit.DIAMONDS) + pointsOf(Suit.HEARTS);
			points /= 2;
		}
		else
		{
			points = pointsOf(pSuit);
		}
		
		if(points > lTenThreshold)
		{
			return lTenBid;
		}
		
		if(points > lNineThreshold)
		{
			return lNineBid;
		}
		
		if(points > lEightThreshold)
		{
			return lEightBid;
		}
		
		if(points > lSevenThreshold)
		{
			return lSevenBid;
		}
		
		if (points > lSixThreshold)
		{
			return lSixBid;
		}
		
		return 0;
	}
	
	/**
	 * Obtain the points of a suit.
	 * @param pSuit The suit that will be checked.
	 * @return The points of pSuit.
	 */
	public int pointsOf(Suit pSuit)
	{
		if(pSuit.equals(Suit.CLUBS))
		{
			return aCPoints;
		}
		
		if(pSuit.equals(Suit.DIAMONDS))
		{
			return aDPoints;
		}
		
		if(pSuit.equals(Suit.HEARTS))
		{
			return aHPoints;
		}
			
		return aSPoints;
	}
	
	/**
	 * Getter method for testing purposes.
	 * @return The points currently assigned to hearts.
	 */
	public int getHPoints()
	{
		return aHPoints;
	}

	/**
	 * Getter method for testing purposes.
	 * @return The points currently assigned to spades.
	 */
	public int getSPoints()
	{
		return aSPoints;
	}

	/**
	 * Getter method for testing purposes.
	 * @return The points currently assigned to diamonds.
	 */
	public int getDPoints()
	{
		return aDPoints;
	}

	/**
	 * Getter method for testing purposes.
	 * @return The points currently assigned to clubs.
	 */
	public int getCPoints()
	{
		return aCPoints;
	}

	/**
	 * Getter method for testing purposes.
	 * @return The number of cards that are hearts.
	 */
	public int getHCards()
	{
		return aHCards;
	}

	/**
	 * Getter method for testing purposes.
	 * @return The number of cards that are spades.
	 */
	public int getSCards()
	{
		return aSCards;
	}

	/**
	 * Getter method for testing purposes.
	 * @return The number of cards that are diamonds.
	 */
	public int getDCards()
	{
		return aDCards;
	}

	/**
	 * Getter method for testing purposes.
	 * @return The number of cards that are clubs.
	 */
	public int getCCards()
	{
		return aCCards;
	}
}
