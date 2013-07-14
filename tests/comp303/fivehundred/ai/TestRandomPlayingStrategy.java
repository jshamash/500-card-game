package comp303.fivehundred.ai;

import static comp303.fivehundred.util.AllCards.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.*;

/**
 * 
 * @author Brandon Hum
 *
 */
public class TestRandomPlayingStrategy
{
	@Test
	public void testRandomPlayingStrategy()
	{
		RandomPlayingStrategy aStrat = new RandomPlayingStrategy();
		Bid aBid1 = new Bid(8, Suit.HEARTS);
		Bid aBid2 = new Bid(7, Suit.CLUBS);
		Bid aBid3 = new Bid(9, Suit.DIAMONDS);
		Bid aBid4 = new Bid(6, null);

		//create 5 different trick cases that will be used to test RandomPlayingStrategy
		Trick aTrickLead = new Trick(aBid1);
		
		Trick aTrickFollow = new Trick(aBid2);
		aTrickFollow.add(aKD);
		
		Trick aTrickJFollow = new Trick(aBid3);
		aTrickJFollow.add(aHJo);
		
		Trick noTrumpTrickLead = new Trick(aBid4);
		
		Trick noTrumpTrickFollow = new Trick(aBid4);
		noTrumpTrickFollow.add(a7H);
		
		ArrayList<Card> validPlays;
		ArrayList<Card> playsList;
		int loopCheck;
		
		//create a hand from which a card will be selected to play in a given trick
		Hand aHand = new Hand();
		aHand.add(a7D);
		aHand.add(aQS);
		aHand.add(aAH);
		aHand.add(aLJo);
		aHand.add(aJS);
		aHand.add(a5C);
		
		//try selecting legal cards at random over 1000 iterations in a leading, hearts as trump situation
		playsList = new ArrayList<Card>();
		
		for (loopCheck=0; loopCheck<1000; loopCheck++)
		{
			Card lCard = aStrat.play(aTrickLead, aHand);
			if (!playsList.contains(lCard))
			{
				playsList.add(lCard);
			}
		}
		
		validPlays = new ArrayList<Card>();
		validPlays.add(a7D);
		validPlays.add(aQS);
		validPlays.add(aAH);
		validPlays.add(aLJo);
		validPlays.add(aJS);
		validPlays.add(a5C);
		
		//check if only and all valid cards can be selected
		assertEquals(6, playsList.size());
		for (Card lCard : validPlays)
		{
			assertTrue(playsList.contains(lCard));
		}
		
		//try selecting legal cards at random over 1000 iterations in a Diamonds led, clubs as trump situation
		playsList = new ArrayList<Card>();
		
		for (loopCheck=0; loopCheck<1000; loopCheck++)
		{
			Card lCard = aStrat.play(aTrickFollow, aHand);
			if (!playsList.contains(lCard))
			{
				playsList.add(lCard);
			}
		}
		
		validPlays = new ArrayList<Card>();
		validPlays.add(a7D);
		validPlays.add(aLJo);
		validPlays.add(aJS);
		validPlays.add(a5C);
		
		//check if only and all valid cards can be selected
		assertEquals(4, playsList.size());
		for (Card lCard : validPlays)
		{
			assertTrue(playsList.contains(lCard));
		}
		
		//try selecting legal cards at random over 1000 iterations in a Joker led, diamonds as trump situation
		playsList = new ArrayList<Card>();
				
		for (loopCheck=0; loopCheck<1000; loopCheck++)
		{
			Card lCard = aStrat.play(aTrickJFollow, aHand);
			if (!playsList.contains(lCard))
			{
				playsList.add(lCard);
			}
		}
				
		validPlays = new ArrayList<Card>();
		validPlays.add(a7D);
		validPlays.add(aLJo);
				
		//check if only and all valid cards can be selected
		assertEquals(2, playsList.size());
		for (Card lCard : validPlays)
		{
			assertTrue(playsList.contains(lCard));
		}
		
		//try selecting legal cards at random over 1000 iterations in a leading, no trump situation
		playsList = new ArrayList<Card>();
		
		for (loopCheck=0; loopCheck<1000; loopCheck++)
		{
			Card lCard = aStrat.play(noTrumpTrickLead, aHand);
			if (!playsList.contains(lCard))
			{
				playsList.add(lCard);
			}
		}
		
		validPlays = new ArrayList<Card>();
		validPlays.add(a7D);
		validPlays.add(aQS);
		validPlays.add(aAH);
		validPlays.add(aJS);
		validPlays.add(a5C);
	
		//check if only and all valid cards can be selected
		assertEquals(5, playsList.size());
		for (Card lCard : validPlays)
		{
			assertTrue(playsList.contains(lCard));
		}
		
		//try selecting legal cards at random over 1000 iterations in a hearts led, no trump situation
		playsList = new ArrayList<Card>();
		
		for (loopCheck=0; loopCheck<1000; loopCheck++)
		{
			Card lCard = aStrat.play(noTrumpTrickFollow, aHand);
			if (!playsList.contains(lCard))
			{
				playsList.add(lCard);
			}
		}
		
		validPlays = new ArrayList<Card>();
		validPlays.add(aAH);
		validPlays.add(aLJo);
		
		//check if only and all valid cards can be selected
		assertEquals(2, playsList.size());
		for (Card lCard : validPlays)
		{
			assertTrue(playsList.contains(lCard));
		}
	}
}
