package comp303.fivehundred.ai;

import java.util.ArrayList;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.ByRankComparator;
import comp303.fivehundred.util.Card.Suit;
import comp303.fivehundred.util.CardList;

/**
 * Advanced AI to select cards to exchange.
 * @author Jake Shamash
 *
 */
public class AdvancedCardExchangeStrategy implements ICardExchangeStrategy
{

	@Override
	public CardList selectCardsToDiscard(Bid[] pBids, int pIndex, Hand pHand)
	{
		final int lWidowSize = 6;
		int toDiscard = lWidowSize;
		CardList discard = new CardList();
		Suit trump = Bid.max(pBids).getSuit();

		ArrayList<CardList> suits = new ArrayList<CardList>();
		suits.add(pHand.cardsOf(Suit.SPADES, trump));
		suits.add(pHand.cardsOf(Suit.CLUBS, trump));
		suits.add(pHand.cardsOf(Suit.DIAMONDS, trump));
		suits.add(pHand.cardsOf(Suit.HEARTS, trump));

		if (trump != null)
		{
			final int lLeastCardStart = 100;
			// Don't discard trumps
			suits.remove(trump.ordinal());

			for (int i = 0; i < toDiscard; i++)
			{
				int leastCards = lLeastCardStart;
				CardList minSuit = null;
				int minIndex = -1;

				// Get index of suit with fewest cards.
				for (CardList clist : suits)
				{
					if (clist.size() < leastCards)
					{
						minSuit = clist;
						leastCards = minSuit.size();
					}
				}

				// minIndex is the suit with the fewest cards.
				minIndex = suits.indexOf(minSuit);
				if (leastCards < toDiscard)
				{
					// Throw out the entire suit
					for (Card c : minSuit)
					{
						discard.add(c);
						toDiscard--;
					}
					suits.remove(minIndex);
				}
				else
				{
					CardList sorted = minSuit.sort(new ByRankComparator());
					// Discard just the lowest cards of the suit.
					for (Card c : sorted)
					{
						if (c.getRank().ordinal() <= lWidowSize && toDiscard > 0)
						{
							minSuit.remove(c);
							discard.add(c);
							toDiscard--;
						}
					}
				}
			}
		}
		else
		{
			/* In no trump, we want to remove low cards from each suit. */
			for (int i = 0; i < toDiscard; i++)
			{
				if (toDiscard <= 0)
				{
					break;
				}
				for (CardList clist : suits)
				{
					if (clist.size() > 0)
					{
						CardList sorted = clist.sort(new ByRankComparator());
						Card lowest = sorted.getFirst();
						if (lowest.getRank().ordinal() <= lWidowSize && toDiscard > 0)
						{
							clist.remove(lowest);
							discard.add(lowest);
							toDiscard--;
						}
					}
				}
			}
		}

		while (toDiscard > 0)
		{
			if (trump != null)
			{
				// We may need to discard trump cards
				suits.add(pHand.cardsOf(trump, trump));
			}
			CardList lowest = new CardList();
			// We have to start discarding valuable cards.
			for (CardList clist : suits)
			{
				if (clist.size() > 0)
				{
					clist.sort(new ByRankComparator());
					lowest.add(clist.getFirst());
					clist.remove(clist.getFirst());
				}
			}
			lowest.sort(new ByRankComparator());

			while (toDiscard > 0 && lowest.size() > 0)
			{
				discard.add(lowest.getFirst());
				lowest.remove(lowest.getFirst());
				toDiscard--;
			}
		}

		assert toDiscard == 0;
		return discard;
	}

}
