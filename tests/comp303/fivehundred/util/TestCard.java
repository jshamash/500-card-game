package comp303.fivehundred.util;

import static comp303.fivehundred.util.AllCards.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import comp303.fivehundred.util.Card.*;

/**
 * 
 * @author Brandon Hum
 *
 */
public class TestCard
{
	
	@Test
	public void testGetConverse()
	{
		assertEquals(Suit.SPADES, Suit.CLUBS.getConverse());
		assertEquals(Suit.CLUBS, Suit.SPADES.getConverse());
		assertEquals(Suit.HEARTS, Suit.DIAMONDS.getConverse());
		assertEquals(Suit.DIAMONDS, Suit.HEARTS.getConverse());
	}
	
	@Test
	public void testGetJokerValue()
	{
		assertEquals(Joker.LOW, aLJo.getJokerValue());
		assertEquals(Joker.HIGH, aHJo.getJokerValue());
	}
	
	@Test
	public void testGetRank()
	{
		assertEquals(Rank.FOUR, a4H.getRank());
		assertEquals(Rank.FOUR, a4S.getRank());
		assertEquals(Rank.QUEEN, aQC.getRank());
		assertEquals(Rank.ACE, aAD.getRank());
	}
	
	@Test
	public void testGetSuit()
	{
		assertEquals(Suit.HEARTS, a7H.getSuit());
		assertEquals(Suit.DIAMONDS, a7D.getSuit());
		assertEquals(Suit.CLUBS, aKC.getSuit());
		assertEquals(Suit.SPADES, aAS.getSuit());
	}
	
	@Test
	public void testGetEffectiveSuit()
	{
		assertEquals(Suit.HEARTS, a8H.getEffectiveSuit(null));
		assertEquals(Suit.CLUBS, aJC.getEffectiveSuit(null));
		assertEquals(Suit.HEARTS, aJD.getEffectiveSuit(Suit.HEARTS));
		assertEquals(Suit.HEARTS, aJH.getEffectiveSuit(Suit.HEARTS));
		assertEquals(Suit.SPADES, aJS.getEffectiveSuit(Suit.HEARTS));
		assertEquals(Suit.CLUBS, aAC.getEffectiveSuit(Suit.SPADES));
	}
	
	@Test
	public void testToString()
	{
		assertEquals( "ACE of CLUBS", aAC.toString());
		assertEquals( "TEN of CLUBS", aTC.toString());
		assertEquals( "JACK of CLUBS", aJC.toString());
		assertEquals( "QUEEN of HEARTS", aQH.toString());
		assertEquals( "KING of SPADES", aKS.toString());
		assertEquals( "QUEEN of DIAMONDS", aQD.toString());
		assertEquals( "HIGH Joker", aHJo.toString());
		assertEquals( "LOW Joker", aLJo.toString());
	}
	
	@Test
	public void testToShortString()
	{
		assertEquals( "HJ", aHJo.toShortString());
		assertEquals( "LJ", aLJo.toShortString());
		assertEquals( "5S", a5S.toShortString());
		assertEquals( "JH", aJH.toShortString());
		assertEquals( "KC", aKC.toShortString());
		assertEquals( "AD", aAD.toShortString());
		assertEquals( "JS", aJS.toShortString());
		assertEquals( "TD", aTD.toShortString());
	}
	
	@Test
	public void testCompareTo()
	{
		assertTrue(a8H.compareTo(aQD) < 0);
		assertTrue(aQD.compareTo(a8H) > 0);
		assertTrue(aKD.compareTo(aKC) > 0);
		assertTrue(aKS.compareTo(aKH) < 0);
		assertTrue(aJD.compareTo(aJD) == 0);
		assertTrue(aHJo.compareTo(aAC) > 0);
		assertTrue(a5S.compareTo(aLJo) < 0);
		assertTrue(aLJo.compareTo(aHJo) < 0);
		assertTrue(aLJo.compareTo(aLJo) == 0);
		assertTrue(aJS.compareTo(aLJo) < 0);
	}
	
	@Test
	public void testEquals()
	{
		assertTrue(aAC.equals(aAC2));
		assertTrue(a6H.equals(a6H));
		assertTrue(aHJo.equals(aHJo));
		assertFalse(aAH.equals(aAC));
		assertFalse(a5S.equals(aTS));
		assertFalse(aLJo.equals(aHJo));
	}
	
	@Test
	public void testHashCode()
	{
		ArrayList<Integer> hashList = new ArrayList<Integer>();
		Deck lDeck = new Deck();

		for (int i=0; i<46; i++)
		{
			int code = lDeck.draw().hashCode();
			//only add code to list if it is not already there
			if (!hashList.contains(code))
			{
				hashList.add(code);
			}
		}
		//checks if each different card is assigned a unique hash code
		assertEquals(46,hashList.size());
	}
	
	
}
