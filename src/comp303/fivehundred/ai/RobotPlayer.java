package comp303.fivehundred.ai;

import java.util.Observer;

import comp303.fivehundred.engine.GameEngine;
import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.model.Player;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardList;

/**
 * Robot player whose intelligence level can be modified.
 * 
 * @author Brandon Hum
 *
 */
public class RobotPlayer extends Player implements IRobotPlayer
{

	private IBiddingStrategy aBiddingStrategy;
	private ICardExchangeStrategy aCardExchangeStrategy;
	private IPlayingStrategy aPlayingStrategy;
	private boolean aIsAdvanced;
	
	/**
	 * Creates a new Robot player.
	 * @param pName The name of the robot.
	 * @pre pName != null
	 */
	public RobotPlayer(String pName)
	{
		super(pName);
		aIsAdvanced = false;
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

	@Override
	public void setAiLevel(int pLevel, GameEngine pEngine)
	{
		if (aIsAdvanced)
		{
			pEngine.deleteObserver((Observer) aPlayingStrategy);
			aIsAdvanced = false;
		}
		switch (pLevel)
		{
			case GameEngine.RANDOM_AI:
				aBiddingStrategy = new RandomBiddingStrategy();
				aCardExchangeStrategy = new RandomCardExchangeStrategy();
				aPlayingStrategy = new RandomPlayingStrategy();
				break;
			case GameEngine.BASIC_AI:
				aBiddingStrategy = new BasicBiddingStrategy();
				aCardExchangeStrategy = new BasicCardExchangeStrategy();
				aPlayingStrategy = new BasicPlayingStrategy();
				break;
			case GameEngine.ADVANCED_AI:
				aIsAdvanced = true;
				aBiddingStrategy = new AdvancedBiddingStrategy();
				aCardExchangeStrategy = new AdvancedCardExchangeStrategy();
				aPlayingStrategy = new AdvancedPlayingStrategy();
				
				pEngine.addObserver((Observer) aPlayingStrategy);
				break;
			default: assert pLevel > 0 && pLevel < 4;
		}
		
	}
}
