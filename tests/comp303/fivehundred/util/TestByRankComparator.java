package comp303.fivehundred.util;

import static comp303.fivehundred.util.AllCards.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Test;

import comp303.fivehundred.util.Card.*;

/**
 * 
 * @author Brandon Hum
 *
 */
public class TestByRankComparator
{
	@Test
	public void testByRankComparator()
	{
		ArrayList<Card> aList;
		ArrayList<Card> sortedList = new ArrayList<Card>();
		
		//create a list of 46 cards sorted by rank then suit followed by the low and high jokers
		for (Rank lRank : Rank.values())
		{
			for (Suit lSuit : Suit.values())
			{
				sortedList.add(new Card(lRank, lSuit));
			}
		}
		sortedList.add(aLJo);
		sortedList.add(aHJo);
		
		//create a shuffled list and sort it, then compare it to sortedList
		aList = TestComparators.shuffledList();
		Collections.sort(aList, new Card.ByRankComparator());
		assertTrue(TestComparators.compareLists(sortedList, aList));
		
		aList = TestComparators.shuffledList();
		Collections.sort(aList, new Card.ByRankComparator());		
		assertTrue(TestComparators.compareLists(sortedList, aList));
		
		aList = TestComparators.shuffledList();
		Collections.sort(aList, new Card.ByRankComparator());		
		assertTrue(TestComparators.compareLists(sortedList, aList));	
	}
}
