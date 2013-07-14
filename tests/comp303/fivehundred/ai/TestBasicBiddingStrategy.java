package comp303.fivehundred.ai;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.util.AllCards;
import comp303.fivehundred.util.Card.Suit;

/**
 * Test method for BasicBiddingStrategy
 * @author Brandon Hum
 *
 */
public class TestBasicBiddingStrategy
{
	BasicBiddingStrategy aStrat = new BasicBiddingStrategy();
	
	Hand perfSHand = new Hand();
	Hand perfCHand = new Hand();
	Hand perfDHand = new Hand();
	Hand perfHHand = new Hand();
	Hand perfNoTrumpHand = new Hand();
	Hand weakNoTrumpHand1 = new Hand();
	Hand weakNoTrumpHand2 = new Hand();
	Hand sixBidHand = new Hand();
	Bid[] emptyBid = {null, null, null, null};
	Bid[] partnerBid = {new Bid(), null, null, new Bid(8, Suit.SPADES)};
	Bid[] partnerPassBid = {new Bid(), null, null, new Bid()};
	Bid[] partnerNoTrumpBid = {new Bid(), null, null, new Bid(8, null)};
	Bid[] opponentBid = {null, null, new Bid(6, Suit.SPADES), null};
	Bid[] twoOpponentBid = {new Bid(), new Bid(6, Suit.SPADES), null, new Bid(6, Suit.SPADES)};
	Bid[] allPass = {null, new Bid(), new Bid(), new Bid()};
	
	@Before
	public void setUp()
	{
		perfSHand.add(AllCards.aHJo);
		perfSHand.add(AllCards.aLJo);
		perfSHand.add(AllCards.aAS);
		perfSHand.add(AllCards.aKS);
		perfSHand.add(AllCards.aQS);
		perfSHand.add(AllCards.aJS);
		perfSHand.add(AllCards.aJC);
		perfSHand.add(AllCards.aTS);
		perfSHand.add(AllCards.a9S);
		perfSHand.add(AllCards.a8S);
		
		perfCHand.add(AllCards.aHJo);
		perfCHand.add(AllCards.aLJo);
		perfCHand.add(AllCards.aAC);
		perfCHand.add(AllCards.aKC);
		perfCHand.add(AllCards.aQC);
		perfCHand.add(AllCards.aJC);
		perfCHand.add(AllCards.aJS);
		perfCHand.add(AllCards.aTC);
		perfCHand.add(AllCards.a9C);
		perfCHand.add(AllCards.a8C);
		
		perfDHand.add(AllCards.aHJo);
		perfDHand.add(AllCards.aLJo);
		perfDHand.add(AllCards.aAD);
		perfDHand.add(AllCards.aKD);
		perfDHand.add(AllCards.aQD);
		perfDHand.add(AllCards.aJD);
		perfDHand.add(AllCards.aJH);
		perfDHand.add(AllCards.aTD);
		perfDHand.add(AllCards.a9D);
		perfDHand.add(AllCards.a8D);
		
		perfHHand.add(AllCards.aHJo);
		perfHHand.add(AllCards.aLJo);
		perfHHand.add(AllCards.aAH);
		perfHHand.add(AllCards.aKH);
		perfHHand.add(AllCards.aQH);
		perfHHand.add(AllCards.aJH);
		perfHHand.add(AllCards.aJD);
		perfHHand.add(AllCards.aTH);
		perfHHand.add(AllCards.a9H);
		perfHHand.add(AllCards.a8H);
		
		perfNoTrumpHand.add(AllCards.aHJo);
		perfNoTrumpHand.add(AllCards.aLJo);
		perfNoTrumpHand.add(AllCards.aAS);
		perfNoTrumpHand.add(AllCards.aAC);
		perfNoTrumpHand.add(AllCards.aAD);
		perfNoTrumpHand.add(AllCards.aAH);
		perfNoTrumpHand.add(AllCards.aKS);
		perfNoTrumpHand.add(AllCards.aKC);
		perfNoTrumpHand.add(AllCards.aKD);
		perfNoTrumpHand.add(AllCards.aJS);
		
		weakNoTrumpHand1.add(AllCards.a4S);
		weakNoTrumpHand1.add(AllCards.a4C);
		weakNoTrumpHand1.add(AllCards.a5C);
		weakNoTrumpHand1.add(AllCards.a5S);
		weakNoTrumpHand1.add(AllCards.a6S);
		weakNoTrumpHand1.add(AllCards.a6C);
		weakNoTrumpHand1.add(AllCards.a7C);
		weakNoTrumpHand1.add(AllCards.a7S);
		weakNoTrumpHand1.add(AllCards.a8S);
		weakNoTrumpHand1.add(AllCards.a8C);
		
		weakNoTrumpHand2.add(AllCards.a4D);
		weakNoTrumpHand2.add(AllCards.a4H);
		weakNoTrumpHand2.add(AllCards.a5D);
		weakNoTrumpHand2.add(AllCards.a5H);
		weakNoTrumpHand2.add(AllCards.a6D);
		weakNoTrumpHand2.add(AllCards.a6H);
		weakNoTrumpHand2.add(AllCards.a7H);
		weakNoTrumpHand2.add(AllCards.a7D);
		weakNoTrumpHand2.add(AllCards.a8D);
		weakNoTrumpHand2.add(AllCards.a8H);
		
		sixBidHand.add(AllCards.a4D);
		sixBidHand.add(AllCards.a4H);
		sixBidHand.add(AllCards.a5C);
		sixBidHand.add(AllCards.aJH);
		sixBidHand.add(AllCards.aJD);
		sixBidHand.add(AllCards.aQH);
		sixBidHand.add(AllCards.aKH);
		sixBidHand.add(AllCards.aKD);
		sixBidHand.add(AllCards.aHJo);
		sixBidHand.add(AllCards.aAH);
	}
	
	@Test
	public void testGetIndex()
	{	
		assertEquals(3, aStrat.getIndex(opponentBid));
		assertEquals(1, aStrat.getIndex(partnerBid));
		assertEquals(0, aStrat.getIndex(emptyBid));
		assertEquals(2, aStrat.getIndex(twoOpponentBid));
	} 
	
	@Test
	public void testSelectBid()
	{
		
		// Test for the case of a perfect spades hand.
		assertEquals(new Bid(10, Suit.SPADES), aStrat.selectBid(partnerBid, perfSHand));
		assertEquals(29, aStrat.getSPoints());
		
		// Try with slightly weaker hand.
		aStrat = new BasicBiddingStrategy();
		Hand sHand = perfSHand.clone();
		sHand.remove(AllCards.aKS);
		sHand.add(AllCards.aKD);
		assertEquals(new Bid(9, Suit.SPADES), aStrat.selectBid(partnerBid, sHand));
		assertEquals(26, aStrat.getSPoints());
		
		// Test for the case when one opponent bids on spades.
		aStrat = new BasicBiddingStrategy();
		assertEquals(new Bid(7, Suit.SPADES), aStrat.selectBid(opponentBid, perfSHand));
		assertEquals(21, aStrat.getSPoints());
		
		// Test for the case where both opponents bid on spades.
		aStrat = new BasicBiddingStrategy();
		assertEquals(new Bid(7, Suit.SPADES), aStrat.selectBid(twoOpponentBid, perfSHand));
		assertEquals(18, aStrat.getSPoints());
		
		// Test for the case of a perfect clubs hand.
		aStrat = new BasicBiddingStrategy();
		partnerBid[3] = new Bid(8, Suit.CLUBS);
		assertEquals(new Bid(10, Suit.CLUBS), aStrat.selectBid(partnerBid, perfCHand));
		assertEquals(29, aStrat.getCPoints());
		
		// Test for the case when one opponent bids on clubs.
		aStrat = new BasicBiddingStrategy();
		opponentBid[3] = new Bid(6, Suit.CLUBS);
		assertEquals(new Bid(7, Suit.CLUBS), aStrat.selectBid(opponentBid, perfCHand));
		assertEquals(21, aStrat.getCPoints());
		
		// Test for the case where both opponents bid on clubs.
		aStrat = new BasicBiddingStrategy();
		twoOpponentBid[1] = new Bid(6, Suit.CLUBS);
		twoOpponentBid[3] = new Bid(6, Suit.CLUBS);
		assertEquals(new Bid(7, Suit.CLUBS), aStrat.selectBid(twoOpponentBid, perfCHand));
		assertEquals(18, aStrat.getCPoints());
		
		// Test for the case of a perfect diamonds hand.
		aStrat = new BasicBiddingStrategy();
		partnerBid[3] = new Bid(8, Suit.DIAMONDS);
		assertEquals(new Bid(10, Suit.DIAMONDS), aStrat.selectBid(partnerBid, perfDHand));
		assertEquals(29, aStrat.getDPoints());
		
		// Test for the case when one opponent bids on diamonds.
		aStrat = new BasicBiddingStrategy();
		opponentBid[3] = new Bid(6, Suit.DIAMONDS);
		assertEquals(new Bid(7, Suit.DIAMONDS), aStrat.selectBid(opponentBid, perfDHand));
		assertEquals(21, aStrat.getDPoints());
		
		// Test for the case where both opponents bid on diamonds.
		aStrat = new BasicBiddingStrategy();
		twoOpponentBid[1] = new Bid(6, Suit.DIAMONDS);
		twoOpponentBid[3] = new Bid(6, Suit.DIAMONDS);
		assertEquals(new Bid(7, Suit.DIAMONDS), aStrat.selectBid(twoOpponentBid, perfDHand));
		assertEquals(18, aStrat.getDPoints());
		
		// Test for the case of a perfect hearts hand.
		aStrat = new BasicBiddingStrategy();
		partnerBid[3] = new Bid(8, Suit.HEARTS);
		assertEquals(new Bid(10, Suit.HEARTS), aStrat.selectBid(partnerBid, perfHHand));
		assertEquals(29, aStrat.getHPoints());
		
		// Test for the case when one opponent bids on hearts.
		aStrat = new BasicBiddingStrategy();
		opponentBid[3] = new Bid(6, Suit.HEARTS);
		assertEquals(new Bid(7, Suit.HEARTS), aStrat.selectBid(opponentBid, perfHHand));
		assertEquals(21, aStrat.getHPoints());
		
		// Test for the case where both opponents bid on hearts.
		aStrat = new BasicBiddingStrategy();
		twoOpponentBid[1] = new Bid(6, Suit.HEARTS);
		twoOpponentBid[3] = new Bid(6, Suit.HEARTS);
		assertEquals(new Bid(7, Suit.HEARTS), aStrat.selectBid(twoOpponentBid, perfHHand));
		assertEquals(18, aStrat.getHPoints());
		
		// Test for the case when one opponent bids on no trump.
		aStrat = new BasicBiddingStrategy();
		opponentBid[3] = new Bid(6, null);
		assertEquals(new Bid(8, Suit.HEARTS), aStrat.selectBid(opponentBid, perfHHand));
		assertEquals(24, aStrat.getHPoints());
		
		// Test for the case where both opponents bid on no trump.
		aStrat = new BasicBiddingStrategy();
		twoOpponentBid[1] = new Bid(6, null);
		twoOpponentBid[3] = new Bid(6, null);
		assertEquals(new Bid(8, Suit.HEARTS), aStrat.selectBid(twoOpponentBid, perfHHand));
		assertEquals(24, aStrat.getHPoints());
		
		// Test for the case where both opponents and partner pass.
		aStrat = new BasicBiddingStrategy();
		assertEquals(new Bid(8, Suit.HEARTS), aStrat.selectBid(allPass, perfHHand));
		assertEquals(24, aStrat.getHPoints());
		
		// Test for the case of a perfect no trump hand.
		aStrat = new BasicBiddingStrategy();
		assertEquals(new Bid(10, null), aStrat.selectBid(partnerBid, perfNoTrumpHand));
		// Check if points were computed correctly.
		assertEquals(16, aStrat.getSPoints());
		assertEquals(16, aStrat.getCPoints());
		assertEquals(14, aStrat.getDPoints());
		assertEquals(17, aStrat.getHPoints());
		// Check if card count was computed correctly.
		assertEquals(5, aStrat.getSCards());
		assertEquals(5, aStrat.getCCards());
		assertEquals(4, aStrat.getDCards());
		assertEquals(3, aStrat.getHCards());
		
		// Try with partner pass.
		aStrat = new BasicBiddingStrategy();
		assertEquals(new Bid(10, null), aStrat.selectBid(partnerPassBid, perfNoTrumpHand));
		
		// Try with partner no trump.
		aStrat = new BasicBiddingStrategy();
		assertEquals(new Bid(10, null), aStrat.selectBid(partnerNoTrumpBid, perfNoTrumpHand));
		
		// Try with starting bid.
		aStrat = new BasicBiddingStrategy();
		assertEquals(new Bid(10, null), aStrat.selectBid(emptyBid, perfNoTrumpHand));
		
		// Try with weak spades and clubs.
		aStrat = new BasicBiddingStrategy();
		assertEquals(new Bid(), aStrat.selectBid(emptyBid, weakNoTrumpHand2));
		
		// Try with weak diamonds and hearts.
		aStrat = new BasicBiddingStrategy();
		assertEquals(new Bid(), aStrat.selectBid(emptyBid, weakNoTrumpHand1));
		
		// Try with low hearts hand.
		aStrat = new BasicBiddingStrategy();
		assertEquals(new Bid(6, Suit.HEARTS), aStrat.selectBid(emptyBid, sixBidHand));
	}
	
}
