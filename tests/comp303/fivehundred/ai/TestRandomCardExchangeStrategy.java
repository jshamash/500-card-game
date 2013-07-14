package comp303.fivehundred.ai;

import java.util.ArrayList;

import org.junit.Test;

import comp303.fivehundred.model.Hand;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardList;

import static comp303.fivehundred.util.AllCards.*;
import static org.junit.Assert.*;

/**
 * 
 * @author Ashley Kyung Min Kim
 *
 */
public class TestRandomCardExchangeStrategy
{
	@Test
	public void testRandomCardExchangeStrategy()
	{
		RandomCardExchangeStrategy exCard = new RandomCardExchangeStrategy();
		
		Hand aHand = new Hand();
		
		// Add 16 cards.
		aHand.add(a4C);
		aHand.add(a4S);
		aHand.add(a5H);
		aHand.add(a6D);
		aHand.add(a7C);
		aHand.add(a7H);
		aHand.add(a8D);
		aHand.add(a8S);
		aHand.add(aAC);
		aHand.add(aHJo);
		aHand.add(aAH);
		aHand.add(aLJo);
		aHand.add(aKS);
		aHand.add(aTC);
		aHand.add(aQD);
		aHand.add(aJH);
		
		CardList picked = new CardList();
		ArrayList<Card> totalPicked = new ArrayList<Card>();
		
		// Make sure to iterate enough so all cards can be selected
		for (int i = 0; i < 1000; i++)
		{
			picked = exCard.selectCardsToDiscard(null, 0, aHand);
			
			for (Card lCard : picked)
			{
				if (!(totalPicked.contains(lCard)))
				{
					totalPicked.add(lCard);
				}
			}
			
		}
		
		for (Card lCard : aHand)
		{
			assertTrue(totalPicked.contains(lCard));
		}
		
		assertEquals(6, picked.size());
		assertEquals(16, aHand.size());
		
	}
}
