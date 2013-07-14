package comp303.fivehundred.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import comp303.fivehundred.ai.TestBasicBiddingStrategy;
import comp303.fivehundred.ai.TestBasicCardExchangeStrategy;
import comp303.fivehundred.ai.TestBasicPlayingStrategy;
import comp303.fivehundred.ai.TestRandomBiddingStrategy;
import comp303.fivehundred.ai.TestRandomCardExchangeStrategy;
import comp303.fivehundred.ai.TestRandomPlayingStrategy;
import comp303.fivehundred.engine.TestGameEngine;
import comp303.fivehundred.model.TestBid;
import comp303.fivehundred.model.TestHand;
import comp303.fivehundred.model.TestTrick;
import comp303.fivehundred.util.TestByRankComparator;
import comp303.fivehundred.util.TestBySuitComparator;
import comp303.fivehundred.util.TestBySuitNoTrumpComparator;
import comp303.fivehundred.util.TestCard;
import comp303.fivehundred.util.TestCardList;
import comp303.fivehundred.util.TestDeck;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	/*TestCard.class, 
	TestDeck.class,
	TestBid.class,
	TestTrick.class,
	TestCardList.class,
	TestHand.class,
	TestByRankComparator.class,
	TestBySuitComparator.class,
	TestBySuitNoTrumpComparator.class,
	TestRandomCardExchangeStrategy.class,
	TestRandomPlayingStrategy.class,
	TestRandomBiddingStrategy.class,*/
	TestBasicBiddingStrategy.class,
	TestBasicCardExchangeStrategy.class,
	TestBasicPlayingStrategy.class,
	TestGameEngine.class
	})
public class AllTests
{

}
