package comp303.fivehundred.engine;

import java.util.Formatter;
import java.util.Observable;
import java.util.Observer;

/**
 * GameStatistics implements Observer; keeps track of stats for each player. Includes a print of the players' stats.
 * 
 * @author Abigail White
 * 
 */
public class GameStatistics implements Observer
{
	private static final int NUM_PLAYERS = 4;

	private GameEngine aEngine;
	private int aGamesPlayed; // Number of total games played
	private int aTricksPlayed; // Number of total tricks played
	private int aRoundsPlayed; // Number of total rounds played
	private int[] aTricksWon; // Number of tricks won for each player
	private int[] aContractsWon; // Number of contracts won for each player
	private int[] aContractsMade; // Number of Contracts fulfilled for each player
	private int[] aGamesWon; // Number of games won for each player
	private int[] aTotalScore; // Accumulated score for each player

	/**
	 * Constructor. Initializes all player statistics.
	 */
	public GameStatistics()
	{
		aGamesPlayed = 0;
		aTricksPlayed = 0;
		aRoundsPlayed = 0;
		aTricksWon = new int[NUM_PLAYERS];
		aContractsWon = new int[NUM_PLAYERS];
		aContractsMade = new int[NUM_PLAYERS];
		aGamesWon = new int[NUM_PLAYERS];
		aTotalScore = new int[NUM_PLAYERS];

		for (int i = 0; i < NUM_PLAYERS; i++)
		{
			aTricksWon[i] = 0;
			aContractsWon[i] = 0;
			aContractsMade[i] = 0;
			aGamesWon[i] = 0;
			aTotalScore[i] = 0;
		}
	}

	@Override
	public void update(Observable pObservable, Object pObject)
	{
		aEngine = (GameEngine) pObservable;
		Integer eventType = (Integer) pObject;

		switch (eventType)
		{
		case GameEngine.NEW_GAME_EVENT:
			handleNewGame();
			break;
		case GameEngine.BID_EVENT:
			handleBid();
			break;
		case GameEngine.PLAY_EVENT:
			handlePlay();
			break;
		case GameEngine.SCORE_EVENT:
			handleScore();
			break;
		case GameEngine.GAME_OVER_EVENT:
			handleGameOver();
			break;
		default: // Do Nothing
		}

	}

	private void handleNewGame()
	{
		aGamesPlayed++;
	}

	private void handleBid()
	{
		int contractHolder = aEngine.getContractHolder();
		aContractsWon[contractHolder]++;
		aRoundsPlayed++;
	}

	private void handlePlay()
	{
		aTricksPlayed++;
		aTricksWon[aEngine.getTrickWinner()]++;
	}

	private void handleScore()
	{
		// Update contracts made only if player was the one who won it
		if (aEngine.isContractMade())
		{
			aContractsMade[aEngine.getContractHolder()]++;
		}

		for (int i = 0; i < NUM_PLAYERS; i++)
		{
			aTotalScore[i] += aEngine.getRoundScore(i);
		}
	}

	private void handleGameOver()
	{
		// Update games won
		for (int i = 0; i < NUM_PLAYERS; i++)
		{
			if (aEngine.isGameWinner(i))
			{
				aGamesWon[i]++;
			}
		}
	}

	/**
	 * Resets all statistical values to 0.
	 */
	public void resetStats()
	{
		aGamesPlayed = 0;
		aTricksPlayed = 0;
		aRoundsPlayed = 0;
		aTricksWon = new int[NUM_PLAYERS];
		aContractsWon = new int[NUM_PLAYERS];
		aContractsMade = new int[NUM_PLAYERS];
		aGamesWon = new int[NUM_PLAYERS];
		aTotalScore = new int[NUM_PLAYERS];

		for (int i = 0; i < NUM_PLAYERS; i++)
		{
			aTricksWon[i] = 0;
			aContractsWon[i] = 0;
			aContractsMade[i] = 0;
			aGamesWon[i] = 0;
			aTotalScore[i] = 0;
		}
	}

	/**
	 * Prints statistical data about each player, in table format.
	 */
	public void printStatistics()
	{
		final int lFiveHundred = 500;
		final float lOneHundred = 100;

		System.out.println("Games played: " + aGamesPlayed);
		System.out.println();

		Formatter aFormat = new Formatter(System.out);
		aFormat.format("%-12s %6s %7s %7s %7s %6s\n", "", "Trick", "Cont", "Made", "Game", "Score");

		for (int i = 0; i < NUM_PLAYERS; i++)
		{
			float trick = ((float) aTricksWon[i] / (float) aTricksPlayed) * lOneHundred;
			float cont = ((float) aContractsWon[i] / (float) aRoundsPlayed) * lOneHundred;
			float made;
			if (aContractsWon[i] == 0)
			{
				made = 0;
			}
			else
			{
				made = ((float) aContractsMade[i] / (float) aContractsWon[i]) * lOneHundred;
			}
			float game = ((float) aGamesWon[i] / (float) aGamesPlayed) * lOneHundred;
			float score = (float) aTotalScore[i] / (float) (aGamesPlayed * lFiveHundred);

			aFormat.format("%-11s %6.1f%% %6.1f%% %6.1f%% %6.1f%%  %-6.2f\n", aEngine.getPlayer(i), trick, cont, made,
					game, score);
		}

		aFormat.close();
	}

	/**
	 * Returns statistical data about each player as an array of strings.
	 * 
	 * @return The array of strings containing the statistics.
	 */
	public String[] getStatistics()
	{
		final int lFiveHundred = 500;
		final float lOneHundred = 100;
		final int lSeven = 7;

		String[] stats = new String[lSeven];

		stats[0] = "Games played: " + aGamesPlayed;
		stats[1] = " ";
		stats[2] = "           Trick    Cont    Made    Game   Score";
		for (int i = 0; i < 4; i++)
		{
			float trick;
			float cont;
			float made;
			float game;
			float score;
			if (aTricksPlayed == 0)
			{
				trick = 0.0f;
			}
			else
			{
				trick = ((float) aTricksWon[i] / (float) aTricksPlayed) * lOneHundred;
			}
			if (aRoundsPlayed == 0)
			{
				cont = 0.0f;
			}
			else
			{
				cont = ((float) aContractsWon[i] / (float) aRoundsPlayed) * lOneHundred;
			}
			if (aContractsWon[i] == 0)
			{
				made = 0.0f;
			}
			else
			{
				made = ((float) aContractsMade[i] / (float) aContractsWon[i]) * lOneHundred;
			}
			if (aGamesPlayed == 0)
			{
				game = 0.0f;
				score = 0.0f;
			}
			else
			{
				game = ((float) aGamesWon[i] / (float) aGamesPlayed) * lOneHundred;
				score = (float) aTotalScore[i] / (float) (aGamesPlayed * lFiveHundred);
			}

			stats[i + 3] = setNameLength(aEngine.getPlayer(i)) + setStatLength(String.format("%.1f", trick) + "%") +
					"  " + setStatLength(String.format("%.1f", cont) + "%") + "  " +
					setStatLength(String.format("%.1f", made) + "%") + "  " +
					setStatLength(String.format("%.1f", game) + "%") + "  " + setStatLength(Float.toString(score));
		}

		return stats;
	}

	/**
	 * Sets name length to 10, adding spaces if needed.
	 * 
	 * @param pName The name of the player.
	 * @return The modified name of the player.
	 */
	private String setNameLength(String pName)
	{
		final int lTen = 10;

		String lName = pName;

		while (lName.length() < lTen)
		{
			lName = lName + " ";
		}

		return lName;
	}

	/**
	 * Sets stat length to 6, adding spaces if needed.
	 * 
	 * @param pStat The stat to be modified.
	 * @return The modified stat.
	 */
	private String setStatLength(String pStat)
	{
		final int lSix = 6;

		String lStat = pStat;

		while (lStat.length() < lSix)
		{
			lStat = " " + lStat;
		}

		return lStat;
	}
}
