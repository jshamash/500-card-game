package comp303.fivehundred.ai;

import java.util.Comparator;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.BySuitComparator;
import comp303.fivehundred.util.Card.Joker;
import comp303.fivehundred.util.Card.Suit;
import comp303.fivehundred.util.CardList;
import comp303.fivehundred.util.Deck;

/**
 * Advanced bid selection.
 * 
 * @author Jake Shamash
 * 
 */
public class AdvancedBiddingStrategy implements IBiddingStrategy
{

	@Override
	public Bid selectBid(Bid[] pPreviousBids, Hand pHand)
	{
		final int lMinBid = 6;
		final int lMaxBid = 10;
		final int lNoTrumpThreshold = 10;
		int handScore = getListScore(pHand);

		if (handScore < lNoTrumpThreshold)
		{
			return new Bid();
		}

		if (shouldBidNoTrump(pHand))
		{
			int bidVal = getHighCardCount(pHand) + 1;
			if (bidVal < lMinBid)
			{
				return new Bid();
			}
			return new Bid(bidVal, null);
		}
		else
		{
			Suit trumpSuit = getStrongestSuit(pHand);
			int trumpCards = pHand.cardsOf(trumpSuit, trumpSuit).size() + 1;
			if (trumpCards < lMinBid)
			{
				return new Bid();
			}
			if (trumpCards > lMaxBid)
			{
				trumpCards = lMaxBid;
			}
			return new Bid(trumpCards, trumpSuit);
		}
	}

	private Suit getStrongestSuit(Hand pHand)
	{
		final int lMinStartVal = 1000;
		int[] scores = { 0, 0, 0, 0 };
		Suit[] suits = { Suit.SPADES, Suit.CLUBS, Suit.DIAMONDS, Suit.HEARTS };
		Deck deck = new Deck();
		CardList clist = new CardList();

		while (deck.size() > 0)
		{
			Card c = deck.draw();
			if (!pHand.contains(c))
			{
				clist.add(c);
			}
		}

		for (int i = 0; i < scores.length; i++)
		{
			Suit trump = suits[i];
			for (Card c : pHand)
			{
				scores[i] += cardsCanBeat(c, clist, trump);
			}
		}

		// Get the min index
		int minIndex = -1;
		int minScore = lMinStartVal;
		for (int i = 0; i < scores.length; i++)
		{
			if (scores[i] < minScore)
			{
				minScore = scores[i];
				minIndex = i;
			}
		}
		return suits[minIndex];
	}

	private int cardsCanBeat(Card pCard, CardList pCardsLeft, Suit pTrump)
	{
		Comparator<Card> comp = new BySuitComparator(pTrump);
		int lReturn = 0;
		for (Card c : pCardsLeft)
		{
			if (comp.compare(c, pCard) > 0)
			{
				lReturn++;
			}
		}
		return lReturn;
	}

	private boolean shouldBidNoTrump(Hand pHand)
	{
		final int lMinHandScore = 18;
		final int lHighCountThreshold = 1;
		int handScore = getListScore(pHand);

		CardList[] suits = { pHand.cardsOf(Suit.SPADES, null), pHand.cardsOf(Suit.CLUBS, null),
				pHand.cardsOf(Suit.DIAMONDS, null), pHand.cardsOf(Suit.HEARTS, null) };

		if (handScore < lMinHandScore)
		{
			return false;
		}
		else
		{
			/* Should have a fairly even distribution of suits for no trump. */
			for (int i = 0; i < 4; i++)
			{
				if (getHighCardCount(suits[i]) < lHighCountThreshold)
				{
					return false;
				}
			}
		}
		return true;

	}

	private int getHighCardCount(CardList pCardList)
	{
		int count = 0;
		for (Card c : pCardList)
		{
			if (getCardScore(c) > 0)
			{
				count++;
			}
		}
		return count;
	}

	private int getCardScore(Card pCard)
	{
		final int lLowJoScore = 5;
		final int lHiJoScore = 6;
		int score;

		if (pCard.isJoker() && pCard.getJokerValue() == Joker.LOW)
		{
			score = lLowJoScore;
		}
		else if (pCard.isJoker() && pCard.getJokerValue() == Joker.HIGH)
		{
			score = lHiJoScore;
		}
		else
		{
			switch (pCard.getRank())
			{
			case JACK:
				score = 1;
				break;
			case QUEEN:
				score = 2;
				break;
			case KING:
				score = 3;
				break;
			case ACE:
				score = 4;
				break;
			default:
				score = 0;
			}
		}

		return score;
	}

	private int getListScore(CardList pList)
	{
		int score = 0;
		for (Card c : pList)
		{
			score += getCardScore(c);
		}
		return score;
	}

}
