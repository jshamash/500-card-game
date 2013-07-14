package comp303.fivehundred.ai;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.model.Player;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardList;

/**
 * Plays correctly but randomly, i.e., very badly.
 */
public class RandomRobot extends Player
{
	private IBiddingStrategy aBiddingStrategy = new RandomBiddingStrategy();
	private ICardExchangeStrategy aCardExchangeStrategy = new RandomCardExchangeStrategy();
	private IPlayingStrategy aPlayingStrategy = new RandomPlayingStrategy();
	
	/**
	 * Creates a new RandomRobot player.
	 * @param pName The name of the robot.
	 * @pre pName != null
	 */
	public RandomRobot(String pName)
	{
		super(pName);
	}

	@Override
	public CardList selectCardsToDiscard(Bid[] pBids, int pIndex, Hand pHand)
	{
		return aCardExchangeStrategy.selectCardsToDiscard(pBids, pIndex, pHand);
	}

	@Override
	public Bid selectBid(Bid[] pBid, Hand pHand)
	{
		return aBiddingStrategy.selectBid(pBid, pHand);
	}

	@Override
	public Card play(Trick pTrick, Hand pHand)
	{
		return aPlayingStrategy.play(pTrick, pHand);
	}
}
