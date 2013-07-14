package comp303.fivehundred.model;

import static org.junit.Assert.*;

import org.junit.Test;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.ModelException;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.util.AllCards;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.Suit;

/**
 * Unit tests for Trick.java
 * @author Jake Shamash
 *
 */
public class TestTrick
{
	@Test
	public void testGetTrumpSuit()
	{
		Bid bPass = new Bid();
		Bid b6H = new Bid(6, Suit.HEARTS);
		Bid b9NT = new Bid (9, null);
		
		// Hearts are trump
		Trick t = new Trick(b6H);
		assertEquals(t.getTrumpSuit(), Suit.HEARTS);
		
		// No Trump
		t = new Trick(b9NT);
		assertNull(t.getTrumpSuit());
		
		// Constructing a trick from a Pass
		try
		{
			t = new Trick(bPass);
			
			fail();
		}
		catch (ModelException e)
		{
		}
	}
	
	@Test
	public void testGetSuitLed()
	{
		// Lead with clubs.
		Trick tClubs = new Trick(new Bid(7, null));
		tClubs.add(AllCards.a7C);
		tClubs.add(AllCards.a5H);
		
		assertEquals(tClubs.getSuitLed(), Suit.CLUBS);
		
		// Empty Trick should throw exception.
		Trick t = new Trick(new Bid(10, null));
		try
		{
			t.getSuitLed();
			fail();
		}
		catch (ModelException e)
		{			
		}
		
		// Leading with Joker should return null.
		t.add(AllCards.aHJo);
		assertNull(t.getSuitLed());
	}
	
	@Test
	public void testJokerLed()
	{
		Trick t = new Trick(new Bid(8, Suit.SPADES));
		t.add(AllCards.aLJo);
		assertTrue(t.jokerLed());
		
		t.remove(AllCards.aLJo);
		t.add(AllCards.aHJo);
		assertTrue(t.jokerLed());
		
		t.remove(AllCards.aHJo);
		t.add(AllCards.a5D);
		assertFalse(t.jokerLed());
		
		t.remove(AllCards.a5D);
		try
		{
			t.jokerLed();
			fail();
		}
		catch (ModelException e)
		{
		}
	}
	
	@Test
	public void testCardLed()
	{
		Trick t = new Trick(new Bid(17));
		
		// Sample trick
		t.add(AllCards.a8C);
		t.add(AllCards.a5S);
		t.add(AllCards.aKC);
		t.add(AllCards.aLJo);
		
		Card cardLed;
		
		while (t.size() > 0)
		{
			cardLed = t.getFirst();
			assertEquals(t.cardLed(), cardLed);
			t.remove(cardLed);
		}
	}
	
	@Test
	public void testHighest()
	{
		Trick tNoTrump = new Trick(new Bid(7, null));
		
		// Sample trick, Low Joker takes the trick.
		tNoTrump.add(AllCards.a6D);
		tNoTrump.add(AllCards.aAD);
		tNoTrump.add(AllCards.a6S);
		tNoTrump.add(AllCards.aLJo);
		
		assertEquals(tNoTrump.highest(), AllCards.aLJo);
		
		// Same trick, but Ace of Diamonds takes the trick.
		tNoTrump.remove(AllCards.aLJo);
		tNoTrump.add(AllCards.a5H);
		
		assertEquals(tNoTrump.highest(), AllCards.a5H);
		
		Trick tSpadesTrump = new Trick(new Bid(8, Suit.SPADES));
		
		// Sample Trick, Jack of Clubs takes the trick.
		tSpadesTrump.add(AllCards.a8S);
		tSpadesTrump.add(AllCards.a9S);
		tSpadesTrump.add(AllCards.aTS);
		tSpadesTrump.add(AllCards.aJC);
		
		assertEquals(tSpadesTrump.highest(), AllCards.aJC);
		
		// Change some cards. High Joker takes the trick.
		tSpadesTrump.remove(AllCards.a8S);
		tSpadesTrump.add(AllCards.a6H);
		tSpadesTrump.remove(AllCards.a9S);
		tSpadesTrump.add(AllCards.aJS);
		tSpadesTrump.remove(AllCards.aTS);
		tSpadesTrump.add(AllCards.aHJo);
		
		assertEquals(tSpadesTrump.highest(), AllCards.aHJo);
	}
	
	@Test
	public void testWinnerIndex()
	{
		Trick tNoTrump = new Trick(new Bid(7, null));
		
		// Sample trick, Low Joker takes the trick.
		tNoTrump.add(AllCards.a6D);
		tNoTrump.add(AllCards.aAD);
		tNoTrump.add(AllCards.a6S);
		tNoTrump.add(AllCards.aLJo);
		
		assertEquals(tNoTrump.winnerIndex(), 3);
		
		Trick tSpadesTrump = new Trick(new Bid(8, Suit.SPADES));
		
		// Sample Trick, Jack of Clubs takes the trick.
		tSpadesTrump.add(AllCards.a8S);
		tSpadesTrump.add(AllCards.a9S);
		tSpadesTrump.add(AllCards.aJC);
		tSpadesTrump.add(AllCards.aTH);
		
		assertEquals(tSpadesTrump.winnerIndex(), 2);
	}
}
