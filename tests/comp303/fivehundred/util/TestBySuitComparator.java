package comp303.fivehundred.util;

import static comp303.fivehundred.util.AllCards.aHJo;
import static comp303.fivehundred.util.AllCards.aLJo;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Test;

import comp303.fivehundred.util.Card.Rank;
import comp303.fivehundred.util.Card.Suit;

/**
 * 
 * @author Brandon Hum
 *
 */
public class TestBySuitComparator
{
	@Test
	public void testBySuitComparator()
	{
		ArrayList<Card> aList;
		ArrayList<Card> sortedList = new ArrayList<Card>();
		Suit lTrump = Suit.DIAMONDS;

		//create a deck of 46 cards sorted based on rankings with trump
		for (Suit lSuit : Suit.values())
		{
			for (Rank lRank : Rank.values())
			{
				if (!lSuit.equals(lTrump))
				{
					if (!lRank.equals(Rank.JACK) || (!lSuit.equals(lTrump.getConverse())))
					{
						sortedList.add(new Card(lRank, lSuit));
					}
				}
			}
		}
		//add trump cards to the end of the list
		for (Rank lRank : Rank.values())
		{
			if (!lRank.equals(Rank.JACK))
			{
				sortedList.add(new Card(lRank, lTrump));
			}
		}
		sortedList.add(new Card(Rank.JACK, lTrump.getConverse()));
		sortedList.add(new Card(Rank.JACK, lTrump));
		sortedList.add(aLJo);
		sortedList.add(aHJo);
		
		//create a shuffled list and sort it, then compare it to sortedList
		aList = TestComparators.shuffledList();
		Collections.sort(aList, new Card.BySuitComparator(lTrump));
		assertTrue(TestComparators.compareLists(sortedList, aList));
		
		aList = TestComparators.shuffledList();
		Collections.sort(aList, new Card.BySuitComparator(lTrump));
		assertTrue(TestComparators.compareLists(sortedList, aList));
		
		aList = TestComparators.shuffledList();
		Collections.sort(aList, new Card.BySuitComparator(lTrump));
		assertTrue(TestComparators.compareLists(sortedList, aList));
	}
}
