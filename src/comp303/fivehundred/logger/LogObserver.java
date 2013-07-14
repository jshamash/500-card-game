package comp303.fivehundred.logger;

import java.util.Observable;
import java.util.Observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import comp303.fivehundred.engine.GameEngine;

/**
 * Observes the game engine and logs the game's events.
 * @author Abigail White
 */
public class LogObserver implements Observer
{
	private static final int NUM_PLAYERS = 4;
	
	private final Logger aLog;
	
	/**
	 * Constructor: creates a new LogObserver instance.
	 */
	public LogObserver()
	{
		System.setProperty("org.slf4j.simplelogger.defaultLogLevel", "debug");
		System.setProperty("org.slf4j.simpleLogger.showLogName", "false");
		aLog = LoggerFactory.getLogger(LogObserver.class);
	}

	@Override
	public void update(Observable pObservable, Object pObject)
	{		
		GameEngine engine = (GameEngine) pObservable;
		Integer eventType = (Integer) pObject;
		
		switch (eventType)
		{
			case GameEngine.NEW_GAME_EVENT:
				logNewGame(engine);
				break;
			case GameEngine.DEAL_EVENT:
				logDeal(engine);
				break;
			case GameEngine.BID_EVENT:
				logBid(engine);
				break;
			case GameEngine.EXCHANGE_EVENT:
				logExchange(engine);
				break;
			case GameEngine.PLAY_EVENT:
				logPlay(engine);
				break;
			case GameEngine.SCORE_EVENT:
				logScore(engine);
				break;				
			default: // Do Nothing					
		}
	}
	
	private void logScore(GameEngine pEngine)
	{
		int contractor1 = pEngine.getContractHolder();
		int contractor2 = (contractor1 + 2) % 4;
		int defender1 = (contractor1 + 1) % 4;
		int defender2 = (defender1 + 2) % 4;
		int contractorTricksWon = pEngine.getTricksWon(contractor1) + pEngine.getTricksWon(contractor2);
		int defenderTricksWon = pEngine.getTricksWon(defender1) + pEngine.getTricksWon(defender2);
		int contractorsRoundScore = pEngine.getRoundScore(contractor1);
		int contractorsTotalScore = pEngine.getGameScore(contractor1);
		int defendersRoundScore = pEngine.getRoundScore(defender1);
		int defendersTotalScore = pEngine.getGameScore(defender1);
		
		aLog.info("{} and {} won {} tricks", new Object[] {pEngine.getPlayer(contractor1),
				pEngine.getPlayer(contractor2), new Integer(contractorTricksWon)});
		aLog.info("{} and {} won {} tricks", new Object[] {pEngine.getPlayer(defender1),
				pEngine.getPlayer(defender2), new Integer(defenderTricksWon)});
		if (pEngine.isContractMade())
		{
			aLog.info("{} and {} make their contract!", pEngine.getPlayer(contractor1), pEngine.getPlayer(contractor2));
		}
		else
		{
			aLog.info("{} and {} didn't make their contract!", pEngine.getPlayer(contractor1), pEngine.getPlayer(contractor2));
		}
		
		aLog.info("Contractors round score: {} Total score: {}", contractorsRoundScore,
				contractorsTotalScore);
		aLog.info("Defenders round score: {} Total score: {}", defendersRoundScore,
				defendersTotalScore);		
	}

	private void logPlay(GameEngine pEngine)
	{
		int iFirst = pEngine.getTurn();
		String lLog = "";
		int iTrick = pEngine.getTricksPlayed();
		String aWinner = pEngine.getPlayer(pEngine.getTrickWinner()); // A string of the winner of the trick
			
		aLog.info("---- TRICK {}----", iTrick);
		
		for(int i = 0; i < NUM_PLAYERS; i++)
		{
			int iPlayer = (iFirst + i ) %4;
			String aHand = pEngine.getHand(iPlayer) +  pEngine.getCardInTrick(i);
			lLog = pEngine.getPlayer(iPlayer) + " cards: " + aHand + " plays:  " + pEngine.getCardInTrick(i);
			aLog.info(lLog);
		}
		
		aLog.info("{} wins the trick.", aWinner);
		
	}

	private void logExchange(GameEngine pEngine)
	{
		int iPlayer = pEngine.getContractHolder();
		String aPlayer = pEngine.getPlayer(iPlayer);
		String aWidow = pEngine.getWidow();
		String aHand = pEngine.getHand(iPlayer);
		
		aLog.info("{} discards {}", aPlayer, aWidow);
		aLog.info("{} cards: {}", aPlayer, aHand);		
	}

	private void logBid(GameEngine pEngine)
	{
		int iFirst = pEngine.getTurn();
		int iContract = pEngine.getContractHolder();
		String aHand = "";
		String aBid = "";
		
		for(int i = 0; i < NUM_PLAYERS; i++)
		{
			int iPlayer = (iFirst + i ) %4;
			aHand = pEngine.getPlayer(iPlayer) + " cards: " + pEngine.getHand(iPlayer);
			aBid = pEngine.getBid(iPlayer);
			aLog.info("{} bids: {}", aHand, aBid);
		}
		
		if (pEngine.allPasses())
		{
			aLog.info("Everyone has passed, cards will now be redealt.");
		}
		else
		{
			aLog.info("{} has the contract of {}", pEngine.getPlayer(iContract), pEngine.getContract());
		}
		
	}

	private void logDeal(GameEngine pEngine)
	{
		String aHand = "";
		String aName = "";
		String aWidow = pEngine.getWidow();
		
		aLog.info("**************NEW DEAL**************");
		aLog.info("{} is dealing", pEngine.getDealer());
		aLog.info("Players dealt cards");
		
		for(int i = 0; i<NUM_PLAYERS; i++)
		{
			aHand = pEngine.getHand(i);
			aName = pEngine.getPlayer(i);
			aLog.info("{} cards: {}", aName, aHand);
		}
		
		aLog.info("The widow contains: {}", aWidow);
		
	}

	private void logNewGame(GameEngine pEngine)
	{
		String iDealer = pEngine.getDealer();
		
		aLog.info("Game initialized. Initial dealer: {}", iDealer);
		aLog.info("================NEW GAME================)");
	}

}
