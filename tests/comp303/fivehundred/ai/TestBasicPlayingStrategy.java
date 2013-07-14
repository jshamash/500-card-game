package comp303.fivehundred.ai;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.util.AllCards;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.Suit;

/**
 * Test method for BasicPlayingStrategy
 * @author Brandon Hum
 *
 */
public class TestBasicPlayingStrategy
{
	Hand h, h2;
	Trick emptyTrumpTrick;
	Trick emptyNoTrumpTrick;
	Trick trumpTrick1;
	Trick trumpTrick2;
	Trick trumpTrick3;
	Trick noTrumpTrick1;
	Trick noTrumpTrick2;
	Trick jokerLead;
	BasicPlayingStrategy aStrat;
	
	@Before
	public void setUp()
	{
		aStrat = new BasicPlayingStrategy();
		
		h = new Hand();
		h.add(AllCards.aJS);
		h.add(AllCards.aKS);
		h.add(AllCards.a4C);
		h.add(AllCards.a5C);
		h.add(AllCards.a7C);
		h.add(AllCards.aQC);
		h.add(AllCards.a5D);
		h.add(AllCards.aTD);
		h.add(AllCards.aAD);
		h.add(AllCards.aHJo);
		
		h2 = new Hand();
		h2.add(AllCards.a6S);
		h2.add(AllCards.aTD);
		h2.add(AllCards.aKD);
		
		emptyTrumpTrick = new Trick(new Bid(6, Suit.HEARTS));
		emptyNoTrumpTrick = new Trick(new Bid(7, null));
		trumpTrick1 = new Trick (new Bid(7, Suit.CLUBS));
		trumpTrick1.add(AllCards.a5S);
		trumpTrick2 = new Trick (new Bid(9, Suit.CLUBS));
		trumpTrick2.add(AllCards.a6H);
		trumpTrick3 = new Trick (new Bid(8, Suit.HEARTS));
		trumpTrick3.add(AllCards.aAD);
		noTrumpTrick1 = new Trick (new Bid(9, null));
		noTrumpTrick1.add(AllCards.a7S);
		noTrumpTrick2 = new Trick(new Bid(8, null));
		noTrumpTrick2.add(AllCards.a8C);
		jokerLead = new Trick(new Bid(9, null));
		jokerLead.add(AllCards.aLJo);
	}
	
	@Test
	public void testPlay()
	{
		ArrayList<Card> playsList;
		ArrayList<Card> validPlays;
		
		//try selecting legal cards at random over 1000 iterations in a leading, hearts as trump situation
		playsList = new ArrayList<Card>();
				
		for (int i=0; i<1000; i++)
		{
			Card lCard = aStrat.play(emptyTrumpTrick, h);
			if (!playsList.contains(lCard))
			{
				playsList.add(lCard);
			}
		}
				
		validPlays = new ArrayList<Card>();
		for (Card lCard : h)
		{
			validPlays.add(lCard);
		}
				
		//check if only and all valid cards can be selected
		assertEquals(10, playsList.size());
		for (Card lCard : validPlays)
		{
			assertTrue(playsList.contains(lCard));
		}
		
		//try selecting legal cards at random over 1000 iterations in a leading, no trump situation
		playsList = new ArrayList<Card>();
		
		for (int i=0; i<1000; i++)
		{
			Card lCard = aStrat.play(emptyNoTrumpTrick, h);
			if (!playsList.contains(lCard))
			{
				playsList.add(lCard);
			}
		}
				
		validPlays = new ArrayList<Card>();
		for (Card lCard : h)
		{
			if (!lCard.isJoker())
			{
				validPlays.add(lCard);
			}
		}
		
		//check if only and all valid cards can be selected
		assertEquals(9, playsList.size());
		for (Card lCard : validPlays)
		{
			assertTrue(playsList.contains(lCard));
		}
		
		//Select a card to play in a five of spades led, clubs as trump situation.
		assertEquals(AllCards.aKS,aStrat.play(trumpTrick1, h));
		
		//Select a card to play in a six of hearts led, clubs as trump situation.
		assertEquals(AllCards.a4C,aStrat.play(trumpTrick2, h));
		
		//Try with a trump already played.
		trumpTrick2.add(AllCards.aTC);
		assertEquals(AllCards.aQC,aStrat.play(trumpTrick2, h));
		
		//Try with a suit following card in hand on a trumped trick.
		h.add(AllCards.a8H);
		assertEquals(AllCards.a8H,aStrat.play(trumpTrick2, h));
		h.remove(AllCards.a8H);
		
		h.remove(AllCards.aHJo);
		h.add(AllCards.aLJo);
		
		//Try with a joker already played.
		trumpTrick2.add(AllCards.aHJo);
		assertEquals(AllCards.a5D,aStrat.play(trumpTrick2, h));
		
		//Select a card to play in a seven of spades led, no trump situation.
		assertEquals(AllCards.aJS, aStrat.play(noTrumpTrick1, h));
		
		//Select a card to play in a eight of clubs led, no trump situation.	
		assertEquals(AllCards.aQC,aStrat.play(noTrumpTrick2, h));
		
		//Select a card to play in a joker led, no trump situation.
		assertEquals(AllCards.a4C,aStrat.play(jokerLead, h));
		
		//Try with no suit following cards or trumps in hand.
		assertEquals(AllCards.a6S,aStrat.play(trumpTrick2, h2));
		trumpTrick2.remove(AllCards.aHJo);
		trumpTrick2.add(AllCards.aJS);
		assertEquals(AllCards.a6S,aStrat.play(trumpTrick2, h2));
		
		//Try with no suit following cards that can win in a non-trumped trick.
		assertEquals(AllCards.aTD,aStrat.play(trumpTrick3, h2));
		
		//Try with no suit following cards or trump cards in a non-trumped trick.
		trumpTrick3.remove(AllCards.aAD);
		trumpTrick3.add(AllCards.aAC);
		assertEquals(AllCards.a6S,aStrat.play(trumpTrick3, h2));
		
		//Try with a low joker led trick with high joker in hand.
		h2.add(AllCards.aHJo);
		assertEquals(AllCards.aHJo,aStrat.play(jokerLead, h2));
		
		//Try with a low joker played trick with high joker in hand.
		trumpTrick2.add(AllCards.aLJo);
		assertEquals(AllCards.aHJo,aStrat.play(trumpTrick2, h2));
	}
}
