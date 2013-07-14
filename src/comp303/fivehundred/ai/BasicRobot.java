package comp303.fivehundred.ai;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.model.Player;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardList;

/**
 * Plays correctly using basic intelligence.
 * @author Brandon Hum
 *
 */
public class BasicRobot extends Player
{
	private IBiddingStrategy aBiddingStrategy = new BasicBiddingStrategy();
	private ICardExchangeStrategy aCardExchangeStrategy = new BasicCardExchangeStrategy();
	private IPlayingStrategy aPlayingStrategy = new BasicPlayingStrategy();
	
	/**
	 * Creates a BasicRobot player.
	 * @param pName The name of the robot.
	 */
	public BasicRobot(String pName)
	{
		super(pName);
	}

	@Override
	public Bid selectBid(Bid[] pPreviousBids, Hand pHand)
	{
		aBiddingStrategy = new BasicBiddingStrategy();
		return aBiddingStrategy.selectBid(pPreviousBids, pHand);
	}

	@Override
	public CardList selectCardsToDiscard(Bid[] pBids, int pIndex, Hand pHand)
	{
		return aCardExchangeStrategy.selectCardsToDiscard(pBids, pIndex, pHand);
	}

	@Override
	public Card play(Trick pTrick, Hand pHand)
	{
		return aPlayingStrategy.play(pTrick, pHand);
	}
}
