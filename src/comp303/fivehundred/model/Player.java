package comp303.fivehundred.model;

import comp303.fivehundred.ai.IBiddingStrategy;
import comp303.fivehundred.ai.ICardExchangeStrategy;
import comp303.fivehundred.ai.IPlayingStrategy;
import comp303.fivehundred.engine.GameEngine;

/**
 * Stores necessary information about a player and describes its behavior.
 * @author Brandon Hum
 */
public abstract class Player implements IBiddingStrategy, ICardExchangeStrategy, IPlayingStrategy
{
	private String aName;
	private Hand aHand;
	private int aScore;					// Score in current game
	private int aRoundScore;			// Score in previous round
	private int aTricksWon;				// Tricks won in current round
	private boolean aGameWinner;		// Set to true when a player wins a game
	
	
	/**
	 * Creates a new Player object.
	 * @param pName The name of the player.
	 * @pre pName != null
	 */
	public Player(String pName)
	{
		aName = pName;
		aScore = 0;
		aTricksWon = 0;
		aRoundScore = 0;
		aGameWinner = false;
	}
	
	/**
	 * Resets the player's score for the start of a new game. Also clears the "isGameWinner" flag.
	 */
	public void reset()
	{
		aScore = 0;
		aGameWinner = false;
	}
	
	/**
	 * Resets the number of tricks won by the player for the start of a new round.
	 */
	public void resetTricksWon()
	{
		aTricksWon = 0;
	}
	
	/**
	 * Adds to the current score of the player.
	 * @param pScore The score to be added.
	 */
	public void addScore(int pScore)
	{
		aScore += pScore;
	}
	
	/**
	 * Obtain the score of the player.
	 * @return The player's score.
	 */
	public int getScore()
	{
		return aScore;
	}
	
	/**
	 * Obtain the hand of the player.
	 * @return The player's hand.
	 */
	public Hand getHand()
	{
		return aHand;
	}
	
	/**
	 * Obtain the contents of the player's hand as a string.
	 * @return A string representing the hand of the player.
	 */
	public String getHandString()
	{
		return aHand.toString();
	}
	
	/**
	 * Sets a new hand for the player.
	 * @param pHand The player's new hand.
	 */
	public void setHand(Hand pHand)
	{
		aHand = pHand;
	}
	
	/**
	 * Obtain the name of the player.
	 * @return The player's name.
	 */
	public String getName()
	{
		return aName;
	}
	
	/**
	 * Increment the number of tricks won by the player for this game and in total.
	 */
	public void incTricksWon()
	{
		aTricksWon++;
	}
	
	/**
	 * Obtain the number of tricks won by the player.
	 * @return The number of tricks won.
	 */
	public int getTricksWon()
	{
		return aTricksWon;
	}
	
	/**
	 * Set the score for the previous round.
	 * @param pScore The score
	 */
	public void setRoundScore(int pScore)
	{
		aRoundScore = pScore;
	}
	
	/**
	 * Get the score of the previous round.
	 * @return The score
	 */
	public int getRoundScore()
	{
		return aRoundScore;
	}

	/**
	 * Is this player the winner of the game?
	 * @return True if the player won the game, false otherwise.
	 */
	public boolean isGameWinner()
	{
		return aGameWinner;
	}

	/**
	 * Set this player to be the winner of the current game.
	 */
	public void setGameWinner()
	{
		this.aGameWinner = true;
	}
	
	/**
	 * Sets the AI level of the player.
	 * 
	 * @param pLevel The value representing the AI level.
	 * @param pEngine The Game engine.
	 */
	public void setAiLevel(int pLevel, GameEngine pEngine)
	{}
}
