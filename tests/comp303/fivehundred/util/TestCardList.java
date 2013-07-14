package comp303.fivehundred.util;

import static comp303.fivehundred.util.AllCards.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Test;

/**
 * @author Ashley Kyung Min Kim
 */

public class TestCardList
{	
	@Test
	public void testAdd()
	{
		CardList testList = new CardList();
		
		testList.add(a4C);
		assertEquals(a4C, testList.getFirst());
		
		// Check if it doesn't add the same element twice.
		testList.add(a4C);
		assertEquals(1, testList.size());
		
		testList.add(aKC);
		assertEquals(2, testList.size());	
		
		testList.add(aLJo);
		assertEquals(3, testList.size());
	}
	
	@Test
	public void testSize()
	{
		CardList testList = new CardList();
		testList.add(a4C);
		testList.add(aKC);
		testList.add(aLJo);
		
		CardList empty = new CardList();
		
		assertEquals(3, testList.size());
		
		assertEquals(0, empty.size());
	}
	
	@Test (expected = GameUtilException.class)
	public void testGetFirst()
	{
		CardList empty = new CardList();
		
		CardList testList = new CardList();
		testList.add(aLJo);
		testList.add(a4C);
		testList.add(aKC);
		
		empty.getFirst();
		
		assertEquals(aLJo, testList.getFirst());
		
		
	}
	
	@Test (expected = GameUtilException.class)
	public void testGetLast()
	{
		CardList empty = new CardList();
		
		CardList testList = new CardList();
		testList.add(a4C);
		testList.add(aKC);
		
		empty.getLast();
		
		assertEquals(aKC, testList.getLast());
		
		testList.add(aLJo);
		assertEquals(aLJo, testList.getLast());
	}
	
	@Test
	public void testRemove()
	{
		CardList r = new CardList();
		
		r.add(a6D);
		r.add(a8C);
		r.remove(a4H);
		assertEquals(2, r.size());
		
		r.remove(a8C);
		assertEquals(1, r.size());
		
		r.add(aHJo);
		r.remove(aHJo);
		assertEquals(1, r.size());
	}
	
	@Test
	public void testClone()
	{
		CardList testCloned = new CardList();
		
		testCloned.add(a4S);
		testCloned.add(a5H);
		testCloned.add(aHJo);
		testCloned.add(a6C);
		
		// Check if the cloned list has the exact copy
		CardList c = testCloned.clone();
		assertEquals(testCloned.toString(), c.toString());
		
		// Check if it is a deep copy
		testCloned.remove(a6C);
		assertNotSame(testCloned.toString(), c.toString());
	}
	
	@Test
	public void testIterator()
	{
		CardList IList = new CardList();
		IList.add(a4D);
		IList.add(a5H);
		IList.add(aHJo);
		IList.add(aTD);
		
		Iterator<Card> i = IList.iterator();
		
		assertEquals(a4D, i.next());
		assertEquals(a5H, i.next());
		assertEquals(aHJo, i.next());
		assertEquals(aTD, i.next());
	}
	
	@Test
	public void testToString()
	{
		CardList ts = new CardList();
		ts.add(a4D);
		ts.add(a7H);
		ts.add(aAS);
		ts.add(aHJo);
		
		assertEquals("{[FOUR of DIAMONDS][SEVEN of HEARTS][ACE of SPADES][HIGH Joker]}", ts.toString());
	}
	
	@Test
	public void testRandom()
	{
		CardList rn = new CardList();
		rn.add(a5S);
		rn.add(a6C);
		rn.add(aLJo);
		
		ArrayList<Card> picked = new ArrayList<Card>();
		
		// Make sure to iterate enough so all cards can be selected
		for (int i = 0; i < 100; i++)
		{
			Card lCard = rn.random();
			if (!picked.contains(lCard))
			{
				picked.add(rn.random());
			}
		}
		
		for (Card lCard : rn)
		{
			assertTrue(picked.contains(lCard));
		}
		
	}
	
	@Test
	public void testSort()
	{
		CardList c1 = new CardList();
		c1.add(aHJo);
		c1.add(a4C);
		c1.add(aTH);
		c1.add(a5S);
		c1.add(a9H);
		
		CardList c2 = new CardList();
		c2.add(aHJo);
		c2.add(a4C);
		c2.add(aTH);
		c2.add(a5S);
		c2.add(a9H);
		
		CardList c3 = new CardList();
		c3.add(a4C);
		c3.add(a5S);
		c3.add(a9H);
		c3.add(aTH);
		c3.add(aHJo);
		
		// Sort by rank
		CardList c4 = c1.sort(new Card.ByRankComparator());
		
		// To check if it has been properly sorted
		assertEquals(c4.toString(), c3.toString());
		
		// To check there is no side effect
		assertEquals(c1.toString(), c2.toString());
	}
}
