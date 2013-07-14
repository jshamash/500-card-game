package comp303.fivehundred.engine;

import java.util.Observer;

import comp303.fivehundred.logger.LogObserver;

/**
 * Runs the game in automatic mode 10,000 times.
 * @author Brandon Hum
 *
 */
public final class Driver
{
	/**
	 * Prevent this class from being instantiated.
	 */
	private Driver()
	{		
	}
	
	/**
	 * Instantiates a GameEngine and simulates 10,000 games.
	 * @param pArgs The command line arguments.
	 */
	public static void main(String[] pArgs)
	{
		String[] lNames = {"BasicA",  "AdvancedA", "BasicB", "AdvancedB"};
		int[] lTypes = {2, 3, 2, 3};
		final int lNumberOfGames = 10000;
		boolean lLog = false;   // Set to true to turn on logging.
		
		GameEngine game = new GameEngine(lNames, lTypes);
		simulate(game, lNumberOfGames, lLog);	
	}
	
	/**
	 * Plays pGameNumber games in a row, adding and removing appropriate observers where needed.
	 * Appropriate logging observers are added and removed as needed if logging is turned on.
	 * @param pGame The GameEngine being used.
	 * @param pGameNumber The number of games to be played.
	 * @param pLog If true, logging is turned on.  Logging is off if false.
	 */
	public static void simulate(GameEngine pGame, int pGameNumber, boolean pLog)
	{		
		final int lNumberOfRounds = 10;
		GameStatistics stats = new GameStatistics();
		pGame.addObserver(stats);
		Observer logger = new LogObserver();
		if (pLog)
		{
			pGame.addObserver(logger);
		}
		
		for (int i = 0; i<pGameNumber; i++)
		{
			pGame.newGame();
			
			while(!pGame.isGameOver())
			{
				pGame.deal();
				pGame.bid();
				
				while (pGame.allPasses())
				{
					pGame.deal();
					pGame.bid();
				}
				
				pGame.exchange();
				
				for (int j = 0; j<lNumberOfRounds; j++)
				{
					pGame.playTrick();
				}
				pGame.computeScore();
			}
		}
		pGame.deleteObservers();
		
		stats.printStatistics();
	}
	
}
