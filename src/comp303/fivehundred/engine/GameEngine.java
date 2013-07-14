package comp303.fivehundred.engine;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

import comp303.fivehundred.ai.RobotPlayer;
import comp303.fivehundred.gui.GameObserver;
import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.model.HumanPlayer;
import comp303.fivehundred.model.Player;
import comp303.fivehundred.model.Trick;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.Card.Suit;
import comp303.fivehundred.util.CardList;
import comp303.fivehundred.util.Deck;

/**
 * An engine for holding game state and running high-level game methods.
 * 
 * @author Jake Shamash
 */
public class GameEngine extends Observable
{
	/* Constants used to indicate to observers which type of event is taking place */
	public static final int NEW_GAME_EVENT = 0;
	public static final int DEAL_EVENT = 1;
	public static final int BID_EVENT = 2;
	public static final int EXCHANGE_EVENT = 3;
	public static final int PLAY_EVENT = 4;
	public static final int SCORE_EVENT = 5;
	public static final int GAME_OVER_EVENT = 6;
	public static final int SINGLE_PLAY_EVENT = 7;

	public static final int HUMAN_PLAYER = 0;
	public static final int RANDOM_AI = 1;
	public static final int BASIC_AI = 2;
	public static final int ADVANCED_AI = 3;

	private static final int NUM_PLAYERS = 4;

	private Player[] aPlayers; // Players in this game
	private String[] aAiLevels;
	private Trick aCurrentTrick; // Current trick
	private Deck aDeck; // Deck for this game
	private int aTurn; // Index of player taking turn
	private int aTrickWinner; // Index of last player to win a trick
	private int aDealer; // Index of current dealer
	private Bid[] aBids; // Bids made in a round
	private Bid aContract; // Contract for the current trick
	private int aContractHolder; // Index of player holding the contract (highest bidder)
	private CardList aWidow; // Becomes discarded cards after a card exchange
	private int aTricksPlayed; // # Tricks played in this game
	private boolean aContractMade; // Set to true when contractors make their contract.
	private Card aLastCardPlayed; // The last card played

	private ArrayList<GameObserver> aObservers = new ArrayList<GameObserver>();

	/**
	 * Constructs a game engine from data about the players playing the game. Each integer in pTypes can have one of the
	 * following values:
	 * 0 - Human player
	 * 1 - Random AI
	 * 2 - Basic AI
	 * 3 - Advanced AI
	 * 
	 * @param pNames
	 *            An array containing the 4 player names. Players 0 and 2 form one team, while players 1 and 3 form the
	 *            other.
	 * @param pTypes
	 *            An array containing the types of the 4 players.
	 * @pre pNames contains exactly 4 elements.
	 * @pre pTypes contains exactly 4 elements.
	 * @pre Every integer in pTypes is a value between 0 and 2.
	 */
	public GameEngine(String[] pNames, int[] pTypes)
	{
		assert pNames.length == NUM_PLAYERS;
		assert pTypes.length == NUM_PLAYERS;

		aPlayers = new Player[NUM_PLAYERS];
		aAiLevels = new String[NUM_PLAYERS];

		for (int i = 0; i < NUM_PLAYERS; i++)
		{
			if (pTypes[i] == HUMAN_PLAYER)
			{
				aPlayers[i] = new HumanPlayer(pNames[i]);
			}
			else
			{
				aPlayers[i] = new RobotPlayer(pNames[i]);
				setAI(i, pTypes[i]);
			}
		}
	}

	/**
	 * Sets up a new game: Clears all data from previous game.
	 */
	public void newGame()
	{
		for (int i = 0; i < NUM_PLAYERS; i++)
		{
			aPlayers[i].reset();
			aPlayers[i].resetTricksWon();
		}

		aCurrentTrick = null;
		aDealer = (new Random()).nextInt(3); // Pick a random dealer to start
		aContract = null;
		aContractHolder = -1;
		aTricksPlayed = 0;

		setChanged();
		notifyObservers(NEW_GAME_EVENT);
	}

	/**
	 * Creates a new deck, deals a hand to each of the players, and sets aside the widow.
	 */
	public void deal()
	{
		final int lCardsInHand = 10;

		// aTurn is set to the first player bidding.
		aTurn = (aDealer + 1) % NUM_PLAYERS;
		aDeck = new Deck();
		aWidow = new CardList();

		Hand playerHand = new Hand();

		for (int i = 0; i < NUM_PLAYERS; i++)
		{
			/* Give each player a ten card hand */
			Hand h = new Hand();
			for (int j = 0; j < lCardsInHand; j++)
			{
				h.add(aDeck.draw());
			}
			aPlayers[i].setHand(h);

			if (i == 0)
			{
				playerHand = h;
			}
		}

		while (aDeck.size() > 0)
		{
			aWidow.add(aDeck.draw());
		}

		setChanged();
		notifyObservers(DEAL_EVENT);

		notifyDeal(playerHand.clone());
	}

	/**
	 * Asks each player to bid according to their bidding strategy, and updates game state accordingly.
	 */
	public void bid()
	{
		bidStart();
		for (int i = 0; i < NUM_PLAYERS; i++)
		{
			nextBid();
		}
		bidEnd();
	}

	/**
	 * Start a new round a bidding.
	 */
	public void bidStart()
	{
		aBids = new Bid[NUM_PLAYERS];
	}

	/**
	 * Gets the bid of the next player in order.
	 */
	public void nextBid()
	{
		Player lPlayer;

		lPlayer = aPlayers[aTurn];

		aBids[aTurn] = lPlayer.selectBid(aBids, lPlayer.getHand());

		notifyBid(aPlayers[aTurn].getName(), aBids[aTurn]);

		aTurn = (aTurn + 1) % 4;
	}

	/**
	 * Called when all four players have made their bids to set the contract holder.
	 */
	public void bidEnd()
	{
		aContract = Bid.max(aBids);

		/* Get the index of the player who won the contract */
		for (int i = 0; i < NUM_PLAYERS; i++)
		{
			if (aBids[i].equals(aContract))
			{
				aContractHolder = i;
				break;
			}
		}

		// Prepare for start of round
		aTricksPlayed = 0;
		aContractMade = false;
		for (Player p : aPlayers)
		{
			p.resetTricksWon();
		}

		setChanged();
		notifyObservers(BID_EVENT);

		if (!allPasses())
		{
			aCurrentTrick = new Trick(aContract);
		}
	}

	/**
	 * Highest bidder does card exchange.
	 * 
	 * @pre !allPasses()
	 */
	public void exchange()
	{
		assert !allPasses();
		exchangeStart();
		exchangeDone();
	}

	/**
	 * Begin the exchange event.
	 */
	public void exchangeStart()
	{
		Player curPlayer = aPlayers[aContractHolder];

		Hand h = curPlayer.getHand();

		for (Card c : aWidow)
		{
			h.add(c);
		}

		notifyExchange(h);
	}

	/**
	 * Called when the exchange is ready to be made and sets the new hand of the player.
	 */
	public void exchangeDone()
	{
		Player curPlayer = aPlayers[aContractHolder];

		Hand h = curPlayer.getHand();

		aWidow = curPlayer.selectCardsToDiscard(aBids, aContractHolder, h);

		for (Card c : aWidow)
		{
			h.remove(c);
		}

		curPlayer.setHand(h);

		setChanged();
		notifyObservers(EXCHANGE_EVENT);
		notifyExchange(h);

		aTurn = aContractHolder;
	}

	/**
	 * Each player plays according to their playing strategy, and the winner of the trick is evaluated.
	 */
	public void playTrick()
	{
		for (int i = 0; i < NUM_PLAYERS; i++)
		{
			nextPlay();
		}
		trickEnd();
	}

	/**
	 * Gets the play of the next player in order.
	 */
	public void nextPlay()
	{
		Player curPlayer = aPlayers[aTurn];
		Hand curHand = curPlayer.getHand();

		Card cardPlayed = curPlayer.play(aCurrentTrick, curHand);

		aLastCardPlayed = cardPlayed;
		aCurrentTrick.add(cardPlayed);
		curHand.remove(cardPlayed);
		curPlayer.setHand(curHand);

		notifyPlay(cardPlayed);
		setChanged();
		notifyObservers(SINGLE_PLAY_EVENT);

		aTurn = (aTurn + 1) % 4;
	}

	/**
	 * Called when all four players have made their plays and checks who won the trick.
	 */
	public void trickEnd()
	{
		// Index of player who won trick
		aTrickWinner = (aTurn + aCurrentTrick.winnerIndex()) % NUM_PLAYERS;
		aPlayers[aTrickWinner].incTricksWon();

		aTricksPlayed++;

		setChanged();
		notifyObservers(PLAY_EVENT);

		// Winner leads next trick
		aTurn = aTrickWinner;

		aCurrentTrick = new Trick(aContract);
	}

	/**
	 * Check if all players pass on the previous bid.
	 * 
	 * @return true if all players pass, false otherwise.
	 */
	public boolean allPasses()
	{
		for (int i = 0; i < NUM_PLAYERS; i++)
		{
			if (!aBids[i].isPass())
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Computes the scores for each player based on the results of the previous round, and prepares players for the next
	 * round.
	 */
	public void computeScore()
	{
		final int lOppPoints = 10; // Points scored by opposition per trick won
		final int lTricksPerRound = 10;
		final int lSlamPoints = 250;

		int contractorScore;
		int oppositionScore;

		// Contract holding players
		final Player lContractor1 = aPlayers[aContractHolder];
		final Player lContractor2 = aPlayers[(aContractHolder + 2) % NUM_PLAYERS];

		// Players not holding contract (opposition)
		final Player lOpposition1 = aPlayers[(aContractHolder + 1) % NUM_PLAYERS];
		final Player lOpposition2 = aPlayers[(aContractHolder + 3) % NUM_PLAYERS];

		oppositionScore = lOppPoints * (lOpposition1.getTricksWon() + lOpposition2.getTricksWon());

		int contractorTricksWon = lContractor1.getTricksWon() + lContractor2.getTricksWon();

		// If the contractors do not take enough tricks for their contract,
		// they score minus the value of the contract.
		if (contractorTricksWon < aContract.getTricksBid())
		{
			contractorScore = -1 * aContract.getScore();
		}
		else
		{
			aContractMade = true;

			// If the contractors make a slam, and their bid was worth less than 250 points,
			// they score 250 instead of their bid.
			if ((contractorTricksWon == lTricksPerRound) && aContract.getScore() <= lSlamPoints)
			{
				contractorScore = lSlamPoints;
			}
			else
			{
				contractorScore = aContract.getScore();
			}
		}

		lContractor1.addScore(contractorScore);
		lContractor1.setRoundScore(contractorScore);
		lContractor2.addScore(contractorScore);
		lContractor2.setRoundScore(contractorScore);
		lOpposition1.addScore(oppositionScore);
		lOpposition1.setRoundScore(oppositionScore);
		lOpposition2.addScore(oppositionScore);
		lOpposition2.setRoundScore(oppositionScore);

		// Next dealer is chosen
		aDealer = (aDealer + 1) % NUM_PLAYERS;

		setChanged();
		notifyObservers(SCORE_EVENT);
	}

	/**
	 * Checks if game is over. If the game is over, the game state is updated with the winners.
	 * 
	 * @return true if game is over, false otherwise.
	 */
	public boolean isGameOver()
	{
		/*
		 * Note: Players of the same team will always have the same score. Therefore, checking only one player from each
		 * partnership is sufficient.
		 */

		final int lFiveHundred = 500;

		for (int i = 0; i < 2; i++)
		{
			/* The game ends if a team reaches minus 500 points or worse, and thus loses the game. */
			if (aPlayers[i].getScore() <= -lFiveHundred)
			{
				// Other team wins
				aPlayers[(i + 1) % 4].setGameWinner();
				aPlayers[(i + 3) % 4].setGameWinner();

				setChanged();
				notifyObservers(GAME_OVER_EVENT);

				return true;
			}

			/*
			 * The game ends when a team wins by reaching a score of 500 points or more as a result of winning a
			 * contract.
			 */
			if (aPlayers[i].getScore() >= lFiveHundred)
			{
				/*
				 * Reaching 500 points or more as a result of odd tricks won while the other side are playing a contract
				 * is not sufficient to win the game.
				 */
				if (aContractHolder == i || aContractHolder == (i + 2) % NUM_PLAYERS)
				{
					aPlayers[i].setGameWinner();
					aPlayers[(i + 2) % 4].setGameWinner();

					setChanged();
					notifyObservers(GAME_OVER_EVENT);

					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Gets a player name.
	 * 
	 * @param pIndex
	 *            The index of the player
	 * @return The name of the player at index pIndex
	 */
	public String getPlayer(int pIndex)
	{
		return aPlayers[pIndex].getName();
	}

	/**
	 * Gets the current turn.
	 * 
	 * @return The index of the player whose turn it is.
	 */
	public int getTurn()
	{
		return aTurn;
	}

	/**
	 * Gets the dealer's name.
	 * 
	 * @return The name of the dealer.
	 */
	public String getDealer()
	{
		return aPlayers[aDealer].getName();
	}

	/**
	 * Returns the bid of the specified player.
	 * 
	 * @param pIndex
	 *            The index of the player.
	 * @pre pIndex >= 0 && pIndex <= 3.
	 * @return A string representation of player pIndex's bid.
	 */
	public String getBid(int pIndex)
	{
		assert pIndex >= 0 && pIndex <= 3;
		return aBids[pIndex].toString();
	}

	/**
	 * Returns the highest current bid.
	 * 
	 * @return The value of the highest current bid.
	 */
	public Bid highestBid()
	{
		return Bid.max(aBids);
	}

	/**
	 * Gets the contract.
	 * 
	 * @return A string representation of the current contract.
	 */
	public String getContract()
	{
		return aContract.toString();
	}

	/**
	 * Gets the contract holder.
	 * 
	 * @return the index of the player holding the contract.
	 */
	public int getContractHolder()
	{
		return aContractHolder;
	}

	/**
	 * Gets a player's hand as a string.
	 * 
	 * @param pIndex
	 *            The index of the player.
	 * @return A string representation of the player's hand.
	 */
	public String getHand(int pIndex)
	{
		return aPlayers[pIndex].getHandString();
	}

	/**
	 * Gets the widow.
	 * 
	 * @return A string representation of the widow
	 */
	public String getWidow()
	{
		return aWidow.toString();
	}

	/**
	 * Gets the trump.
	 * 
	 * @return A string representation of the trump.
	 */
	public String getTrump()
	{
		if (aContract.getSuit() == null)
		{
			return "NO TRUMP";
		}
		return aContract.getSuit().toString();
	}
	
	/**
	 * Gets the current trump suit.
	 * @return The current trump suit.
	 */
	public Suit getTrumpSuit()
	{
		return aCurrentTrick.getTrumpSuit();
	}

	/**
	 * Gets the number of tricks played.
	 * 
	 * @return The tricks played since the last call to newGame().
	 */
	public int getTricksPlayed()
	{
		return aTricksPlayed;
	}

	/**
	 * Gets a specific card from the most recently played trick.
	 * 
	 * @param pIndex
	 *            Index of the card
	 * @return A string representation of the card.
	 */
	public String getCardInTrick(int pIndex)
	{
		return aCurrentTrick.getCardAtIndex(pIndex);
	}
	
	/**
	 * Gets the last card played.
	 * @return The last card played.
	 */
	public Card getLastCardPlayed()
	{
		return aLastCardPlayed;
	}

	/**
	 * Gets the index of the player who won the most recent trick.
	 * 
	 * @return The index of the player.
	 */
	public int getTrickWinner()
	{
		return aTrickWinner;
	}

	/**
	 * Gets the number of tricks won by a player.
	 * 
	 * @param pPlayer
	 *            Index of the player
	 * @return Tricks won by the player
	 */
	public int getTricksWon(int pPlayer)
	{
		return aPlayers[pPlayer].getTricksWon();
	}

	/**
	 * Get a player's score for the current round.
	 * 
	 * @param pPlayer
	 *            Index of the player.
	 * @return The player's score for this round.
	 */
	public int getRoundScore(int pPlayer)
	{
		return aPlayers[pPlayer].getRoundScore();
	}

	/**
	 * Get a player's accumulated score for the game.
	 * 
	 * @param pPlayer
	 *            Index of the player
	 * @return The player's game score
	 */
	public int getGameScore(int pPlayer)
	{
		return aPlayers[pPlayer].getScore();
	}

	/**
	 * Returns whether or not contractors made their contract for the previous round.
	 * 
	 * @return True if contract was made, false otherwise.
	 */
	public boolean isContractMade()
	{
		return aContractMade;
	}

	/**
	 * Returns the index of the player leading the current trick. Once the trick is finished, this will be the index of
	 * the player about to lead the next trick.
	 * 
	 * @return Index of the player leading the trick.
	 */
	public int getTrickLeader()
	{
		if (aTricksPlayed == 0)
		{
			return aContractHolder;
		}
		return aTrickWinner;
	}
	
	/**
	 * Get the effective suit led. Null if Joker.
	 * @return Effective suit led.
	 */
	public Suit getSuitLed()
	{
		return aCurrentTrick.getSuitLed();
	}

	/**
	 * Returns whether or not a player won the game. Note: this value is set at the end of the game, then cleared after
	 * a call to newGame().
	 * 
	 * @param pPlayer
	 *            The index of the player
	 * @return True if this player won the game, false otherwise.
	 */
	public boolean isGameWinner(int pPlayer)
	{
		return aPlayers[pPlayer].isGameWinner();
	}

	/**
	 * Sets the AI level of a player.
	 * 
	 * @param pPlayer
	 *            The player to be modified.
	 * @param pLevel
	 *            The new AI level of the player.
	 * @pre pPlayer > 0 && pPlayer <= 3
	 * @pre pLevel > 0 && pLevel <= 3
	 */
	public void setAI(int pPlayer, int pLevel)
	{
		aPlayers[pPlayer].setAiLevel(pLevel, this);
		switch (pLevel)
		{
		case RANDOM_AI:
			aAiLevels[pPlayer] = "Random";
			break;
		case BASIC_AI:
			aAiLevels[pPlayer] = "Basic";
			break;
		case ADVANCED_AI:
			aAiLevels[pPlayer] = "Advanced";
			break;
		default:
			assert pLevel > 0 && pLevel <= 3;
		}
	}

	/**
	 * Gets the AI level of a player as a String.
	 * 
	 * @param pPlayer
	 *            The index of the player.
	 * @return The AI level of the player.
	 */
	public String getAI(int pPlayer)
	{
		return aAiLevels[pPlayer];
	}

	/**
	 * Gets the list of playable cards of the human player.
	 * 
	 * @return The list of playable cards.
	 */
	public CardList getPlayableCards()
	{
		if (aCurrentTrick.size() == 0)
		{
			return aPlayers[0].getHand().canLead(aContract.isNoTrump());
		}
		return aPlayers[0].getHand().playableCards(aCurrentTrick.getSuitLed(), aContract.getSuit());
	}

	/**
	 * Method to add a GameObserver.
	 * 
	 * @param pObserver
	 *            The GameObserver to be added.
	 * @pre pObserver != null
	 */
	public void addGameObserver(GameObserver pObserver)
	{
		aObservers.add(pObserver);
	}

	/**
	 * Notifies GameObservers that the cards have been dealt.
	 * 
	 * @param pHand
	 *            The new hand of the human player.
	 * @pre pHand != null
	 */
	private void notifyDeal(Hand pHand)
	{
		for (GameObserver o : aObservers)
		{
			o.newHand(pHand);
		}
	}

	/**
	 * Notifies GameObservers that a bid has been made.
	 * 
	 * @param pName
	 *            The name of the player who made the bid.
	 * @param pBid
	 *            The bid that was made.
	 * @pre pName != null
	 * @pre pBid != null
	 */
	private void notifyBid(String pName, Bid pBid)
	{
		for (GameObserver o : aObservers)
		{
			o.newBid(pName, pBid);
		}
	}

	/**
	 * Notifies GameObservers that cards have been exchanged.
	 * 
	 * @param pHand
	 *            The new hand of the player.
	 * @pre pHand != null
	 */
	private void notifyExchange(Hand pHand)
	{
		for (GameObserver o : aObservers)
		{
			o.exchangeHand(pHand);
		}
	}

	/**
	 * Notifies GameObserverse that a card has been played.
	 * 
	 * @param pCard
	 *            The card that was played.
	 * @pre pCard != null
	 */
	private void notifyPlay(Card pCard)
	{
		for (GameObserver o : aObservers)
		{
			o.discard(pCard);
		}
	}
}
