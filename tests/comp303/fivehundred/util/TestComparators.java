package comp303.fivehundred.util;

import java.util.ArrayList;

/**
 * 
 * @author Brandon Hum
 *
 */
public class TestComparators
{	
	
	/**
	 * Method which returns a shuffled ArrayList of all 46 cards
	 * @return an ArrayList of shuffled cards
	 */
	public static ArrayList<Card> shuffledList()
	{
		ArrayList<Card> lList = new ArrayList<Card>();
		Deck aDeck = new Deck();
		aDeck.shuffle();
		
		for (int i=0; i<46; i++)
		{
			lList.add(aDeck.draw());
		}
		
		return lList;
	}
	
	/**
	 * Method which compares two ArrayLists
	 * @return true if both lists are identical, false if they differ
	 * @param expList The expected list
	 * @param actList actual list
	 * @pre expList != null
	 * @pre actList != null
	 */
	public static boolean compareLists(ArrayList<Card> expList, ArrayList<Card> actList)
	{
		for (int i=0; i<46; i++)
		{
			if (!expList.get(i).equals(actList.get(i)))
			{
				return false;
			}
		}
		return true;
	}
}
