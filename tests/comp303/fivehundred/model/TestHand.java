package comp303.fivehundred.model;



import static comp303.fivehundred.util.AllCards.*;
import static org.junit.Assert.*;

import org.junit.Test;

import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.Suit;
import comp303.fivehundred.util.CardList;

/**
 *  @author Brandon Hum
 */
public class TestHand
{
	@Test
	public void testClone()
	{
		Hand testCloned = new Hand();
		
		testCloned.add(a4S);
		testCloned.add(a5H);
		testCloned.add(aHJo);
		testCloned.add(a6C);
		
		// Check if the cloned list has the exact copy
		Hand c = testCloned.clone();
		assertEquals(testCloned.toString(), c.toString());
		
		// Check if it is a deep copy
		testCloned.remove(a6C);
		assertNotSame(testCloned.toString(), c.toString());
	}
	
	@Test
	public void testCanLead(){
		Hand tHand = new Hand();
		CardList validList;
		
		tHand.add(a6C);
		tHand.add(a9H);
		tHand.add(aHJo);
		tHand.add(aJS);
		tHand.add(a4S);
		tHand.add(aJH);
		tHand.add(aLJo);
		tHand.add(aTD);
		tHand.add(aAD);
		
		validList = new CardList();
		validList.add(a6C);
		validList.add(a9H);
		validList.add(aJS);
		validList.add(a4S);
		validList.add(aJH);
		validList.add(aTD);
		validList.add(aAD);
		
		assertEquals(validList.toString(), tHand.canLead(true).toString());
		
		assertEquals(tHand.toString(), tHand.canLead(false).toString());
	}
	
	@Test
	public void testGetJokers()
	{
		Hand h1 = new Hand();
		CardList jokers;
		
		// Empty hand
		jokers = h1.getJokers();
		
		assertTrue(jokers.size() == 0);
		
		// No jokers
		h1.add(a8S);
		h1.add(a6C);
		h1.add(aQC);
		
		jokers = h1.getJokers();
		assertTrue(jokers.size() == 0);
		
		// Add one joker
		h1.add(aHJo);
		
		jokers = h1.getJokers();		
		assertTrue(jokers.size() == 1);
		assertTrue(jokers.getFirst() == aHJo);
		
		// Only jokers
		h1 = new Hand();
		h1.add(aLJo);
		h1.add(aHJo);
		
		jokers = h1.getJokers();		
		assertTrue(jokers.size() == 2);
		for (Card c : jokers)
		{
			assertTrue(c.isJoker());
		}
	}
	
	@Test
	public void testGetNonJokers()
	{
		Hand h = new Hand();
		CardList nonJokers;
		
		// Empty hand
		nonJokers = h.getNonJokers();
		assertTrue(nonJokers.size() == 0);
		
		// Only Jokers
		h.add(aLJo);
		h.add(aHJo);
		h.add(aHJo);
		
		nonJokers = h.getNonJokers();
		assertTrue(nonJokers.size() == 0);
		
		// Add one non-joker
		h.add(a5H);
		
		nonJokers = h.getNonJokers();
		assertTrue(nonJokers.size() == 1);
		assertTrue(nonJokers.getFirst().equals(a5H));
		
		// Only non-jokers
		h = new Hand();
		h.add(a4C);
		h.add(a6S);
		h.add(a8H);
		
		nonJokers = h.getNonJokers();
		assertTrue(nonJokers.size() == 3);
		for (Card c : nonJokers)
		{
			assertFalse(c.isJoker());
		}
	}
	
	@Test
	public void testGetTrumpCards()
	{
		Hand tHand = new Hand();
		CardList validList;
		
		tHand.add(a5C);
		tHand.add(a8H);
		tHand.add(aHJo);
		tHand.add(aJD);
		tHand.add(aJS);
		tHand.add(aTC);
		tHand.add(aAD);
		tHand.add(aKS);
		
		validList = new CardList();
		validList.add(aHJo);
		validList.add(aJS);
		validList.add(aKS);
		
		assertEquals(validList.toString(), tHand.getTrumpCards(Suit.SPADES).toString());
		
		validList = new CardList();
		validList.add(a5C);
		validList.add(aHJo);
		validList.add(aJS);
		validList.add(aTC);
		
		assertEquals(validList.toString(), tHand.getTrumpCards(Suit.CLUBS).toString());
		
		validList = new CardList();
		validList.add(aHJo);
		validList.add(aJD);
		validList.add(aAD);
		
		assertEquals(validList.toString(), tHand.getTrumpCards(Suit.DIAMONDS).toString());
		
		validList = new CardList();
		validList.add(a8H);
		validList.add(aHJo);
		validList.add(aJD);
		
		assertEquals(validList.toString(), tHand.getTrumpCards(Suit.HEARTS).toString());
	}
	
	@Test
	public void testGetNonTrumpCards()
	{
		Hand tHand = new Hand();
		CardList validList;
		
		tHand.add(a5C);
		tHand.add(a8H);
		tHand.add(aHJo);
		tHand.add(aJD);
		tHand.add(aJS);
		tHand.add(aTC);
		tHand.add(aAD);
		tHand.add(aKS);
		
		validList = new CardList();
		validList.add(a5C);
		validList.add(a8H);
		validList.add(aJD);
		validList.add(aTC);
		validList.add(aAD);
		
		assertEquals(validList.toString(), tHand.getNonTrumpCards(Suit.SPADES).toString());
		
		validList = new CardList();
		validList.add(a8H);
		validList.add(aJD);
		validList.add(aAD);
		validList.add(aKS);
		
		assertEquals(validList.toString(), tHand.getNonTrumpCards(Suit.CLUBS).toString());
		
		validList = new CardList();
		validList.add(a5C);
		validList.add(a8H);
		validList.add(aJS);
		validList.add(aTC);
		validList.add(aKS);
		
		assertEquals(validList.toString(), tHand.getNonTrumpCards(Suit.DIAMONDS).toString());
		
		validList = new CardList();
		validList.add(a5C);
		validList.add(aJS);
		validList.add(aTC);
		validList.add(aAD);
		validList.add(aKS);
		
		assertEquals(validList.toString(), tHand.getNonTrumpCards(Suit.HEARTS).toString());
	}
	
	@Test
	public void testSelectLowest(){
		Hand tHand = new Hand();
		
		tHand.add(a5C);
		tHand.add(a8H);
		tHand.add(aHJo);
		tHand.add(aJH);
		tHand.add(aAD);
		tHand.add(aJC);
		tHand.add(a5D);
		tHand.add(a5S);
		
		assertEquals(a5S, tHand.selectLowest(Suit.HEARTS));
		assertEquals(a5C, tHand.selectLowest(Suit.SPADES));
		assertEquals(a5S, tHand.selectLowest(null));
	}
	
	@Test
	public void testPlayableCards()
	{
		Hand tHand = new Hand();
		CardList validList;
		CardList lList;
		
		tHand.add(a5C);
		tHand.add(a8H);
		tHand.add(aHJo);
		tHand.add(aJC);
		tHand.add(aAD);
		tHand.add(aKH);
		tHand.add(aJS);
		tHand.add(a6D);
		tHand.add(aJH);
		
		validList = new CardList();
		validList.add(a8H);
		validList.add(aHJo);
		validList.add(aJC);
		validList.add(aKH);
		validList.add(aJS);
		validList.add(aJH);
		
		lList = tHand.playableCards(Suit.HEARTS, Suit.SPADES);
		assertEquals(validList.toString(), lList.toString());
		
		validList = new Hand();
		validList.add(a5C);
		validList.add(aHJo);
		validList.add(aJC);
		validList.add(a6D);
		validList.add(aJH);
		
		lList = tHand.playableCards(Suit.CLUBS, Suit.DIAMONDS);
		
		validList = new Hand();
		validList.add(a8H);
		validList.add(aHJo);
		validList.add(aKH);
		validList.add(aJH);
		
		lList = tHand.playableCards(null, Suit.HEARTS);
		assertEquals(validList.toString(), lList.toString());
		
		validList = new Hand();
		validList.add(aHJo);
		validList.add(aJS);
		
		lList = tHand.playableCards(Suit.SPADES, null);
		assertEquals(validList.toString(), lList.toString());
		
		//try with no suit following cards available
		tHand = new Hand();
		tHand.add(a6D);
		tHand.add(aKS);
		tHand.add(aQC);
		
		lList = tHand.playableCards(Suit.HEARTS, Suit.HEARTS);
		assertEquals(tHand.toString(), lList.toString());
	}
	
	@Test
	public void testNumberofCards()
	{
		Hand tHand = new Hand();
		
		tHand.add(a5C);
		tHand.add(a8H);
		tHand.add(aHJo);
		tHand.add(aJD);
		tHand.add(aJS);
		tHand.add(aAS);
		tHand.add(aTH);
		
		assertEquals(3, tHand.numberOfCards(Suit.HEARTS, Suit.HEARTS));
		assertEquals(2, tHand.numberOfCards(Suit.HEARTS, Suit.CLUBS));
		assertEquals(1, tHand.numberOfCards(Suit.CLUBS, Suit.SPADES));
		assertEquals(1, tHand.numberOfCards(Suit.DIAMONDS, Suit.CLUBS));
		assertEquals(2, tHand.numberOfCards(Suit.SPADES, Suit.DIAMONDS));
	}
}