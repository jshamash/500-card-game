package comp303.fivehundred.model;

import static org.junit.Assert.*;

import org.junit.Test;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.ModelException;
import comp303.fivehundred.util.Card.Suit;

/**
 * Testing class for Bid.java.
 * @author Jake Shamash
 *
 */
public class TestBid
{
	@Test
	public void testGetSuit()
	{
		Bid b = new Bid(7, Suit.DIAMONDS);
		assertEquals(Suit.DIAMONDS, b.getSuit());
		
		b = new Bid(10, null);
		assertEquals(null, b.getSuit());
		
		b = new Bid(0);
		assertEquals(Suit.SPADES, b.getSuit());
		
		b = new Bid();
		try
		{
			b.getSuit();
			fail();
		}
		catch (ModelException e)
		{
			// This is what we want!
		}
	}
	
	@Test
	public void testGetTricksBid()
	{
		// 8 Clubs
		Bid b = new Bid(7);
		assertEquals(7, b.getTricksBid());
		
		b = new Bid();
		try
		{
			b.getTricksBid();
			fail();
		}
		catch (ModelException e)
		{
			// This is what we want!
		}
	}
	
	@Test
	public void testIsPass()
	{
		Bid b = new Bid();
		assertTrue(b.isPass());
		
		b = new Bid(12);
		assertFalse(b.isPass());
	}
	
	@Test
	public void testIsNoTrump()
	{
		// 10 no trump
		Bid b = new Bid(24);
		assertTrue(b.isNoTrump());
		
		b = new Bid(10, Suit.HEARTS);
		assertFalse(b.isNoTrump());
		
		b = new Bid();
		assertFalse(b.isNoTrump());
	}
	
	@Test
	public void testCompareTo()
	{
		// Both are passes
		Bid b1 = new Bid();
		Bid b2 = new Bid();
		assertEquals(b1.compareTo(b2), 0);
		
		// Only one is a pass
		b1 = new Bid(18);
		assertTrue(b1.compareTo(b2) > 0 && b2.compareTo(b1) < 0);
		
		// Both are non-passing
		b2 = new Bid(23);
		assertTrue(b1.compareTo(b2) < 0 && b2.compareTo(b1) > 0);
		
		b2 = new Bid(18);
		assertEquals(b1.compareTo(b2), 0);
		assertEquals(b2.compareTo(b1), 0);
	}
	
	@Test
	public void testToString()
	{
		Bid bPass = new Bid();
		Bid b6S = new Bid(6, Suit.SPADES);
		Bid b7C = new Bid(7, Suit.CLUBS);
		Bid b8D = new Bid(8, Suit.DIAMONDS);
		Bid b9H = new Bid(9, Suit.HEARTS);
		Bid b10NT = new Bid(10, null);
		
		assertEquals(bPass.toString(), "PASS");
		assertEquals(b6S.toString(), "6 SPADES");
		assertEquals(b7C.toString(), "7 CLUBS");
		assertEquals(b8D.toString(), "8 DIAMONDS");
		assertEquals(b9H.toString(), "9 HEARTS");
		assertEquals(b10NT.toString(), "10 NO TRUMP");		
	}
	
	@Test
	public void testEquals()
	{
		// One or both is a pass
		Bid b1 = new Bid();
		Bid b2 = new Bid();
		assertTrue(b1.equals(b2) && b2.equals(b1));
		
		b2 = new Bid(7, Suit.HEARTS);
		assertFalse(b1.equals(b2));
		
		// Both are non-passing
		b1 = new Bid(7, Suit.HEARTS);
		assertTrue(b1.equals(b2) && b2.equals(b1));
		b2 = new Bid(10, Suit.DIAMONDS);
		assertFalse(b1.equals(b2));
		
		// One is not a Bid
		assertFalse(b1.equals(new Object()));
	}
	
	@Test
	public void testHashCode()
	{
		// Different bids must produce different hash codes
		Bid b1 = new Bid();
		Bid b2 = new Bid(9, null);
		Bid b3 = new Bid(7, Suit.HEARTS);
		assertTrue(b1.hashCode() != b2.hashCode());
		assertTrue(b2.hashCode() != b3.hashCode());
		assertTrue(b1.hashCode() != b3.hashCode());
		
		// Identical bids must produce identical hash codes
		b2 = new Bid();
		assertEquals(b1.hashCode(), b2.hashCode());
		b2 = new Bid(7, Suit.HEARTS);
		assertEquals(b2.hashCode(), b3.hashCode());
	}
	
	@Test
	public void testToIndex()
	{
		// Returns correct index for non-passing bids
		for (int i = 0; i <= 24; i++)
		{
			assertEquals((new Bid(i).toIndex()), i);
		}
		
		// Throws exception for passing bids
		Bid b = new Bid();
		try
		{
			b.toIndex();
			fail();
		}
		catch (ModelException e)
		{
			// This is what we want
		}
	}
	
	@Test
	public void testMax()
	{
		Bid[] bids = new Bid[4];
		
		// All passing bids
		for (int i = 0; i < bids.length; i++)
		{
			bids[i] = new Bid();
		}
		assertTrue(Bid.max(bids).isPass());
		
		// All bids are different
		bids[1] = new Bid(12);
		bids[2] = new Bid(21);
		bids[3] = new Bid(5);
		assertTrue(Bid.max(bids).equals(bids[2]));
		
		// More than one maximum
		bids[3] = new Bid(21);
		assertTrue(Bid.max(bids).equals(bids[2]));
	}
	
	@Test
	public void testGetScore()
	{
		int[] expectedScores = new int [25];
		for (int i = 0; i <= 24; i++)
		{
			expectedScores[i] = 40 + 20*i;
		}

		// Test all possible bids
		int[] actualScores = new int[25];
		for (int i = 0; i <= 24; i++)
		{
			actualScores[i] = (new Bid(i)).getScore();
		}
		
		assertArrayEquals(expectedScores, actualScores);
		
		// Throws exception for passing bids
		Bid b = new Bid();
		try
		{
			b.getScore();
			fail();
		}
		catch (ModelException e)
		{
			// This is what we want
		}		
		
	}
}
