package comp303.fivehundred.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

/**
 * Test methods for GameEngine.
 * @author Brandon Hum
 *
 */
public class TestGameEngine
{
	String[] aNames = {"Martin", "Alice", "Dorothy", "Richard"};
	int[] aTypes = {1,2,1,2};
	GameEngine aEngine = new GameEngine(aNames, aTypes);
	
	@Test
	public void testConstructor()
	{
		int[] allTypes = {0,1,2,1};
		// Try instantiating a GameEngine with all three types of players.
		GameEngine testEngine = new GameEngine(aNames, allTypes);
	}
	
	@Test
	public void testDeal()
	{
		aEngine.newGame();
		aEngine.deal();
		
		boolean dealerCheck = false;
		
		// Make sure dealer is actually one of the players.
		for (int i=0;i<4;i++)
		{
			if (aEngine.getDealer().equals(aEngine.getPlayer(i)))
			{
				dealerCheck = true;
			}
		}
		
		assertTrue(dealerCheck);
		
		// Check that each player has 10 cards.
		for (int i = 0; i<4; i++)
		{
			assertEquals(30,aEngine.getHand(i).length());
		}
		
		// Check that the widow has 6 cards.
		assertEquals(18, aEngine.getWidow().length());
	}
	
	@Test
	public void testBid()
	{
		aEngine.newGame();
		aEngine.deal();
		aEngine.bid();
		
		// Regex expression representing all valid bid strings.
		Pattern pattern = Pattern.compile("^PASS$|(^([6-9]|10)\\s(SPADES|CLUBS|DIAMONDS|HEARTS|NO TRUMP)$)");
		boolean[] check = {false, false, false, false};
		
		// Check if each player has made a valid bid.
		for (int i = 0; i<4; i++)
		{
			// Match the bid of each player to pattern.
			Matcher matcher = pattern.matcher(aEngine.getBid(i));
			while(matcher.find())
			{
				check[i] = true;
			}
		}
		
		// Check that all 4 bids are valid.
		for (int i=0; i<4; i++)
		{
			assertTrue(check[i]);
		}
	}
	
	@Test
	public void testExchange()
	{
		aEngine.newGame();
		aEngine.deal();
		aEngine.bid();
		aEngine.exchange();
		
		// Check that 6 cards were discarded.
		assertEquals(18, aEngine.getWidow().length());
		// Check that the contract holder still has 10 cards after the exchange.
		assertEquals(30, aEngine.getHand(aEngine.getContractHolder()).length());
	}
	
	@Test
	public void testPlayTrick()
	{
		aEngine.newGame();
		aEngine.deal();
		aEngine.bid();
		while (aEngine.allPasses())
		{
			aEngine.deal();
			aEngine.bid();
		}
		aEngine.exchange();
		
		int handLength;
		int totalTricksWon = 0;
		
		// Check that the contract holder leads the first trick.
		assertEquals(aEngine.getTurn(), aEngine.getContractHolder());
		
		// Check that a card is played by every play every time playTrick() is called.
		for (int i=0;i<10;i++)
		{
			handLength = 30 - i*3;
			for (int j=0;j<4;j++)
			{
				assertEquals(handLength, aEngine.getHand(j).length());
			}
			aEngine.playTrick();
			
			// Check to see that the engine is keeping track of tricks played.
			assertEquals(1+i, aEngine.getTricksPlayed());
		}
		for (int i=0;i<4;i++)
		{
			assertEquals(0, aEngine.getHand(i).length());
			// Check that all players win between 0 and 10 tricks.
			assertTrue(aEngine.getTricksWon(i) >= 0 && aEngine.getTricksWon(i) <= 10);
			
			totalTricksWon += aEngine.getTricksWon(i);
		}
		
		// Check that the total tricks won by all players adds up to 10.
		assertEquals(10, totalTricksWon);
	}
	
	@Test
	public void testAllPasses()
	{
		aEngine.newGame();
		aEngine.deal();
		aEngine.bid();
		
		// Cycle through deal() and bid() until the engine detects that all players have passed
		while (!aEngine.allPasses())
		{
			aEngine.deal();
			aEngine.bid();
		}
		
		// Check that all players have passed.
		for (int i=0;i<4;i++)
		{
			assertEquals("PASS", aEngine.getBid(i));
		}
	}
	
	@Test
	public void testComputeScore()
	{
		for (int i=0; i<1000; i++)
		{
			aEngine.newGame();
			while (!aEngine.isGameOver())
			{
				aEngine.deal();
				aEngine.bid();	
				while (aEngine.allPasses())
				{
					aEngine.deal();
					aEngine.bid();
				}
				aEngine.exchange();
				for( int j = 0; j < 10; j++ )
				{
					aEngine.playTrick();
				}
				aEngine.computeScore();
				
				// Check that each player is awarded or deducted points that are valid values according to the rules of the game.
				for (int j = 0; j<4; j++)
				{
					int score = aEngine.getRoundScore(j);
			
					// Opponents may score anywhere from 0 - 100 in multiples of 10.  Contractors may score anywhere between -520 and -40
					// in multiples of 20 if they do not make their contract, or between 40 and 520 in multiples of 20 if they do make their
					// contract. They may also score 250 if they achieve a slam and their contract was worth less than 250 points.
					assertTrue((score <= 100 && score % 10 == 0) || score == 250 || score % 20 == 0);
			
					assertTrue(score >= -520 && score <= 520);
				}
			}
			// Determine the strength of the contract.
			int contractStrength = Integer.parseInt(aEngine.getContract().replaceAll("[a-zA-Z\\s]", ""));
			// If the contract was made, check that the number of tricks won by both contractors are together at least as much as the contract strength.
			if (aEngine.isContractMade())
			{
				int winnerIndex = aEngine.getContractHolder();
				int tricksWon = aEngine.getTricksWon(winnerIndex) + aEngine.getTricksWon((winnerIndex + 2) % 4);
	
				assertTrue(tricksWon >= contractStrength);
			}
		}
	}
	
	@Test
	public void testIsGameOver()
	{
		// Try with 1000 games played.
		for (int i=0;i<1000;i++)
		{
			aEngine.newGame();
			while (!aEngine.isGameOver())
			{
				aEngine.deal();
				aEngine.bid();
				while (aEngine.allPasses())
				{
					aEngine.deal();
					aEngine.bid();
				}
				aEngine.exchange();
				for (int j=0;j<10;j++)
				{
					aEngine.playTrick();
				}
				aEngine.computeScore();
			}
	
			
			// Check to see that a team has either accumulated at least 500 points or less than or equal to -500 points.
			assertTrue(aEngine.getGameScore(0) <= -500 || aEngine.getGameScore(0) >= 500 || aEngine.getGameScore(1) <= -500 || aEngine.getGameScore(1) >= 500);
		}
	}
	
}
