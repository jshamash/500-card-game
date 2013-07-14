package comp303.fivehundred.ai;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.util.AllCards;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.Suit;
import comp303.fivehundred.util.CardList;

/**
 * Test method for BasicCardExchangeStrategy
 * @author Brandon Hum
 *
 */
public class TestBasicCardExchangeStrategy
{
	BasicCardExchangeStrategy aStrat = new BasicCardExchangeStrategy();
	Bid[] aSpadesBid = {new Bid(), new Bid(), new Bid(), new Bid(8, Suit.SPADES)};
	Bid[] aClubsBid = {new Bid(), new Bid(), new Bid(), new Bid(8, Suit.CLUBS)};
	Bid[] aDiamondsBid = {new Bid(), new Bid(), new Bid(), new Bid(8, Suit.DIAMONDS)};
	Bid[] aHeartsBid = {new Bid(), new Bid(), new Bid(), new Bid(8, Suit.HEARTS)};
	Bid[] aNoTrumpBid = {new Bid(), new Bid(), new Bid(), new Bid(8, null)};
	Hand aHand = new Hand();
	CardList aSpadesList = new CardList();
	CardList aClubsList = new CardList();
	CardList aDiamondsList = new CardList();
	CardList aHeartsList = new CardList();
	CardList aNoTrumpList = new CardList();
	
	@Before
	public void setUp()
	{
		aHand.add(AllCards.a5S);
		aHand.add(AllCards.a4D);
		aHand.add(AllCards.a7C);
		aHand.add(AllCards.aJH);
		aHand.add(AllCards.aAS);
		aHand.add(AllCards.aHJo);
		aHand.add(AllCards.aLJo);
		aHand.add(AllCards.aKC);
		aHand.add(AllCards.a8D);
		aHand.add(AllCards.aQS);
		aHand.add(AllCards.aTH);
		aHand.add(AllCards.a5C);
		aHand.add(AllCards.aAH);
		aHand.add(AllCards.aJC);
		aHand.add(AllCards.aTD);
		aHand.add(AllCards.aKH);	
		
		// Check to make sure that it selects the 6 lowest valued cards.
		aSpadesList.add(AllCards.a4D);
		aSpadesList.add(AllCards.a5C);
		aSpadesList.add(AllCards.a7C);
		aSpadesList.add(AllCards.a8D);
		aSpadesList.add(AllCards.aTD);
		aSpadesList.add(AllCards.aTH);
		
		aClubsList.add(AllCards.a4D);
		aClubsList.add(AllCards.a5S);
		aClubsList.add(AllCards.a8D);
		aClubsList.add(AllCards.aTD);
		aClubsList.add(AllCards.aTH);
		aClubsList.add(AllCards.aJH);
		
		aDiamondsList.add(AllCards.a5S);
		aDiamondsList.add(AllCards.a5C);
		aDiamondsList.add(AllCards.a7C);
		aDiamondsList.add(AllCards.aTH);
		aDiamondsList.add(AllCards.aJC);
		aDiamondsList.add(AllCards.aQS);
		
		aHeartsList.add(AllCards.a4D);
		aHeartsList.add(AllCards.a5S);
		aHeartsList.add(AllCards.a5C);
		aHeartsList.add(AllCards.a7C);
		aHeartsList.add(AllCards.a8D);
		aHeartsList.add(AllCards.aTD);
		
		aNoTrumpList.add(AllCards.a4D);
		aNoTrumpList.add(AllCards.a5S);
		aNoTrumpList.add(AllCards.a5C);
		aNoTrumpList.add(AllCards.a7C);
		aNoTrumpList.add(AllCards.a8D);
		aNoTrumpList.add(AllCards.aTD);	
	}
	
	@Test
	public void testSelectCardsToDiscard()
	{
		// Test with spades as trump.
		CardList selected = aStrat.selectCardsToDiscard(aSpadesBid, 0, aHand);
		assertEquals(aSpadesList.toString(), selected.sort(new Card.ByRankComparator()).toString());
		
		// Test with clubs as trump.
		selected = aStrat.selectCardsToDiscard(aClubsBid, 0, aHand);
		assertEquals(aClubsList.toString(), selected.sort(new Card.ByRankComparator()).toString());
		
		// Test with diamonds as trump.
		selected = aStrat.selectCardsToDiscard(aDiamondsBid, 0, aHand);
		assertEquals(aDiamondsList.toString(), selected.sort(new Card.ByRankComparator()).toString());
		
		// Test with hearts as trump.
		selected = aStrat.selectCardsToDiscard(aHeartsBid, 0, aHand);
		assertEquals(aHeartsList.toString(), selected.sort(new Card.ByRankComparator()).toString());
		
		// Test with no trump.
		selected = aStrat.selectCardsToDiscard(aNoTrumpBid, 0, aHand);
		assertEquals(aNoTrumpList.toString(), selected.sort(new Card.ByRankComparator()).toString());
	}
}
