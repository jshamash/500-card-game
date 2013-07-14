package comp303.fivehundred.ai;

import java.util.Comparator;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import comp303.fivehundred.engine.GameEngine;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.ByRankComparator;
import comp303.fivehundred.util.Card.BySuitComparator;
import comp303.fivehundred.util.Card.BySuitNoTrumpComparator;
import comp303.fivehundred.util.Card.Suit;
import comp303.fivehundred.util.CardList;
import comp303.fivehundred.util.Deck;

/**
 * Advanced AI playing strategy.
 * 
 * @author Jake Shamash
 * 
 */
public class AdvancedPlayingStrategy implements IPlayingStrategy, Observer
{
	private boolean[][] aSuits; // aSuits[player][suit]
	private CardList aCardsLeft;
	private int aTrickLeader;
	private boolean aFirstTrick;

	/**
	 * Constructor.
	 */
	public AdvancedPlayingStrategy()
	{
		aSuits = new boolean[4][4];

		newGame();
	}

	private void newGame()
	{
		/* Assume everyone has every suit */
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				aSuits[i][j] = true;
			}
		}

		Deck deck = new Deck();
		aCardsLeft = new CardList();
		while (deck.size() > 0)
		{
			aCardsLeft.add(deck.draw());
		}

		aTrickLeader = -1;
		aFirstTrick = true;
	}

	@Override
	public Card play(Trick pTrick, Hand pHand)
	{
		CardList playable;
		Card toPlay = null;

		/* If this is the first trick, remove the cards from the player's hand from aCardsLeft. */
		if (aFirstTrick)
		{
			for (Card c : pHand)
			{
				aCardsLeft.remove(c);
			}
			aFirstTrick = false;
		}

		/*
		 * Case 1: leading trick. The strategy here is to play the highest playable card, since this will either take
		 * the trick or force other players to play their high cards, making later tricks easier to win.
		 */
		if (pTrick.size() == 0)
		{
			if (pTrick.getTrumpSuit() == null)
			{
				playable = pHand.canLead(true);
				playable = playable.sort(new ByRankComparator());
				if (aTrickLeader < 0)
				{
					toPlay = playable.getLast();
				}
				else
				{
					for (Card c : playable)
					{
						if (c.isJoker())
						{
							continue;
						}
						if (!canBeBeat(c, aTrickLeader, null, c.getSuit()))
						{
							toPlay = c;
							break;
						}
					}
				}
			}
			else
			{
				playable = pHand.canLead(false);
				if (playable.cardsOf(pTrick.getTrumpSuit(), pTrick.getTrumpSuit()).size() > 0)
				{
					playable = playable.cardsOf(pTrick.getTrumpSuit(), pTrick.getTrumpSuit());
				}
				toPlay = playable.getLast();
			}

			if (toPlay == null)
			{
				// log.info("All my cards could be beaten, so attempting to play highest");
				toPlay = playable.getLast();
			}
		}
		/* I am the last to play */
		else if (pTrick.size() == 3)
		{
			/* If my partner is winning the trick, I can play garbage */
			if (pTrick.winnerIndex() == 1)
			{
				toPlay = playLow(pHand, pTrick, 1.0);
			}
			/* If not, I should try to win */
			else
			{
				/* Play lowest playable card that would win */
				toPlay = playToWin(pHand, pTrick);

				/* If I don't have any winning cards, play low. */
				if (toPlay == null)
				{
					toPlay = playLow(pHand, pTrick, 1.0);
				}
			}
		}
		/* My partner has played, and I am not last to play -> partner led trick */
		else if (pTrick.size() == 2)
		{
			int myIndex = (aTrickLeader + pTrick.size()) % 4;
			Card partnerCard = pTrick.getFirst();

			/* If my partner is winning the trick and cannot be beat, I can play garbage. */
			if (pTrick.winnerIndex() == 0 &&
					!canBeBeat(partnerCard, myIndex, pTrick.getTrumpSuit(), pTrick.getSuitLed()))
			{
				toPlay = playLow(pHand, pTrick, 1.0);
			}
			/* Otherwise, I must try to win the trick */
			else
			{
				final double lProb = 0.5;
				toPlay = playToWin(pHand, pTrick);
				if (toPlay == null)
				{
					toPlay = playLow(pHand, pTrick, lProb);
				}
			}
		}
		/* I am playing second => my partner hasn't played */
		else
		{
			/* Try to win */
			toPlay = playToWin(pHand, pTrick);

			/*
			 * If we can't find a card that is guaranteed to win the trick, play garbage.
			 */
			if (toPlay == null)
			{
				final double lProb = 0.7;
				toPlay = playLow(pHand, pTrick, lProb);
			}
		}

		return toPlay;
	}

	/* Returns the lowest playable card that is guaranteed to win, or null if no such card exists. */
	private Card playToWin(Hand pHand, Trick pTrick)
	{
		int myIndex = (aTrickLeader + pTrick.size()) % 4;
		CardList playable = pHand.playableCards(pTrick.getSuitLed(), pTrick.getTrumpSuit());
		if (pTrick.getTrumpSuit() == null)
		{
			playable = playable.sort(new BySuitNoTrumpComparator());
		}
		else
		{
			playable = playable.sort(new BySuitComparator(pTrick.getTrumpSuit()));
		}

		/* At the end of this loop, toPlay will contain the lowest card that is guaranteed to win the trick. */
		for (Card c : playable)
		{
			if (pTrick.wouldWin(c) && !canBeBeat(c, myIndex, pTrick.getTrumpSuit(), pTrick.getSuitLed()))
			{
				return c;
			}
		}

		return null;
	}

	/* Play low with probability pProbability, but play high with probability (1 - pProbability). */
	private Card playLow(Hand pHand, Trick pTrick, double pProbability)
	{
		// Possible improvements: favour weaker suits.
		double prob = pProbability;
		CardList cList = pHand.playableCards(pTrick.getSuitLed(), pTrick.getTrumpSuit());
		if (pTrick.getTrumpSuit() == null)
		{
			cList = cList.sort(new BySuitNoTrumpComparator());
		}
		else
		{
			cList = cList.sort(new BySuitComparator(pTrick.getTrumpSuit()));
		}

		cList = cList.sort(new ByRankComparator());

		// If I don't have any cards that can win, definitely play lowest!
		boolean mightWin = false;
		for (Card c : cList)
		{
			if (pTrick.wouldWin(c))
			{
				mightWin = true;
			}
		}
		if (!mightWin)
		{
			prob = 1;
		}

		Random r = new Random();
		if (r.nextDouble() <= prob)
		{
			return cList.getFirst();
		}

		return cList.getLast();
	}

	/*
	 * Returns true if there is a possibility that any of the players following the player at pIndex can beat pCard.
	 */
	private boolean canBeBeat(Card pCard, int pCardIndex, Suit pTrump, Suit pSuitLed)
	{
		/* If this is the last card in a trick, no one will follow it, therefore it cannot be beat. */
		if ((pCardIndex + 1) % 4 == aTrickLeader)
		{
			return false;
		}

		/*
		 * If pCard is a high joker, it cannot be followed. If it is a low joker, it can only be beat if there is still
		 * a high joker left.
		 */
		if (pCard.isJoker())
		{
			if (pCard.getJokerValue() == Card.Joker.HIGH)
			{
				return false;
			}
			if (aCardsLeft.contains(new Card(Card.Joker.HIGH)))
			{
				return true;
			}
			return false;
		}

		/*
		 * If there is a trump suit, and one of the remaining players could have a higher trump, this card can be beat.
		 */
		if (pTrump != null)
		{
			CardList remainingTrumps = aCardsLeft.cardsOf(pTrump, pTrump);
			remainingTrumps = remainingTrumps.merge(aCardsLeft.getJokers());
			Comparator<Card> comp = new BySuitComparator(pTrump);
			for (int i = (pCardIndex + 1) % 4; i != aTrickLeader; i = (i + 1) % 4)
			{
				if (aSuits[i][pTrump.ordinal()])
				{
					for (Card c : remainingTrumps)
					{
						if (comp.compare(c, pCard) > 0)
						{
							return true;
						}
					}
				}
			}
		}

		/* In no trump, if there are any jokers left, this card can be beat. */
		if (pTrump == null && aCardsLeft.hasJoker())
		{
			return true;
		}

		/* Lastly, if any player can follow the suit led with a card higher than pCard, pCard can be beat. */
		CardList canFollow = aCardsLeft.cardsOf(pSuitLed, pTrump);
		for (int i = (pCardIndex + 1) % 4; i != aTrickLeader; i = (i + 1) % 4)
		{
			if (aSuits[i][pSuitLed.ordinal()])
			{
				for (Card c : canFollow)
				{
					if (c.compareTo(pCard) > 0)
					{
						return true;
					}
				}
			}
		}

		return false;
	}

	@Override
	public void update(Observable pObservable, Object pObject)
	{
		GameEngine engine = (GameEngine) pObservable;
		Integer eventType = (Integer) pObject;

		if (eventType == GameEngine.SINGLE_PLAY_EVENT)
		{
			aTrickLeader = engine.getTrickLeader();
			int player = engine.getTurn();
			Suit suitLed = engine.getSuitLed(); // null if joker.
			Card cardPlayed = engine.getLastCardPlayed();
			Suit trumpSuit = engine.getTrumpSuit();
			Suit cardSuit;

			aCardsLeft.remove(cardPlayed);

			// Joker was led => counts as trump suit
			if (suitLed == null && trumpSuit != null)
			{
				suitLed = trumpSuit;
			}

			// If the player led the trick, they could have played any card.
			if (player != aTrickLeader)
			{
				// Jokers count as trumps when playing with trump
				if (cardPlayed.isJoker() && trumpSuit != null)
				{
					cardSuit = trumpSuit;
				}
				// Note: cardSuit will be null if joker was played with no trump.
				else
				{
					cardSuit = cardPlayed.getEffectiveSuit(trumpSuit);
				}
				if (suitLed != null && cardSuit != suitLed)
				{
					// Couldn't follow suit
					aSuits[player][suitLed.ordinal()] = false;
				}

			}
		}
		if (eventType == GameEngine.DEAL_EVENT)
		{
			newGame();
		}

	}

}