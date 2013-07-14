package comp303.fivehundred.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import comp303.fivehundred.engine.GameEngine;
import comp303.fivehundred.engine.GameStatistics;
import comp303.fivehundred.logger.LogObserver;
import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import comp303.fivehundred.util.Card;
import comp303.fivehundred.util.CardList;

/**
 * Frame in which all events during the course of the game will be displayed.
 * The user will be able to interact with it in various ways while they play.
 * 
 * @author Brandon Hum
 *
 */
@SuppressWarnings("serial")
public class FHGUI extends JFrame implements ActionListener, GameObserver
{
	public static final Color bgColor = new Color(238, 216, 174);
	
	public static final Map<String, Integer> aiLevels = new HashMap<String, Integer>(); // Map between string and int representations of ai level.
	
	public static Object aLock = new Object(); // Lock used to wait for player input.
	
	private static final FHGUI aGUI = new FHGUI(); // Singleton of the FHGUI.
	
	// Dimensions of the screen.
	private final int WIDTH = 800;
	private final int HEIGHT = 700;
	
	private GameEngine aEngine; // Game engine used for the game.
	
	private GridBagConstraints c = new GridBagConstraints(); // Used to position different panels on the frame.
	
	private JPanel aCenterPanel; // Used to display various panels and information at the center of the frame.
	private HandPanel[] aHands = new HandPanel[4]; // Panels which will display the hands of each player.
	private StartUpScreen aStart = new StartUpScreen(); // Screen shown when the program is first started.
	private BidPanel aBid = new BidPanel(); // Panel shown during bid time.
	private InfoPanel aInfo = new InfoPanel(); // Panel used to display information about the game.
	private TrickPanel aTrick = new TrickPanel(); // Panel used during play time.
	private StatsPanel aStats; // Panel which displays game statistics and information.
	private GameOverScreen aGameOver; // Panel shown after a game finishes.
	private JButton aDiscard = new JButton("Discard"); // Button used during exchange time for discarding cards.
	
	private Hand aHand; // Hand of the user.
	private Bid aPlayerBid; // Bid of the user.
	private CardList aCardsToDiscard; // List of cards to be discarded by the user.
	private Card aPlay; // Card played by the user.
	
	private boolean playerExchange = false; // Flag indicating whether the game is in the exchange state.
	private boolean autoplay = false; // Flag indicating whether the game is in autoplay mode.
	private boolean aPlayerInput = false; // Flag indicating whether the user has made an input.
	
	private int aPlaySpeed = 1; // Speed in seconds between individual plays by the computer players.
	
	private int aBidPosition = 0; // Bid position.
	
	// Used to add KeyListener functionality to the program.
	private KeyboardFocusManager manager;
	private MyDispatcher aDispatcher = new MyDispatcher();
	
	private GameStatistics stats = new GameStatistics(); // Observer which keeps track of global statistical data.

	/**
	 * Private constructor.  Only one instance of the FHGUI can be created.
	 */
	private FHGUI()
	{
		super();

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();

		// Set up the ai level map.
		aiLevels.put("Random", GameEngine.RANDOM_AI);
		aiLevels.put("Basic", GameEngine.BASIC_AI);
		aiLevels.put("Advanced", GameEngine.ADVANCED_AI);

		aCenterPanel = new JPanel();
		aCenterPanel.setLayout(new GridBagLayout());
		aCenterPanel.setBackground(bgColor);

		aDiscard.addActionListener(this);

		setLayout(new GridBagLayout());

		c.gridx = 1;
		c.gridy = 1;
		add(aCenterPanel, c);

		// Initialize HandPanels.
		aHands[0] = new HandPanel(HandPanel.BOTTOM);
		aHands[1] = new HandPanel(HandPanel.LEFT_RIGHT);
		aHands[2] = new HandPanel(HandPanel.TOP);
		aHands[3] = new HandPanel(HandPanel.LEFT_RIGHT);

		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 3;
		add(aHands[0], c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		add(aHands[1], c);

		c.gridx = 1;
		c.gridy = 0;
		add(aHands[2], c);

		c.gridx = 2;
		c.gridy = 1;
		add(aHands[3], c);

		getContentPane().setBackground(bgColor);

		setSize(new Dimension(WIDTH, HEIGHT));
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);
		setFocusable(true);
	}

	/**
	 * Getter for the FHGUI instance.
	 * 
	 * @return Instance of the FHGUI.
	 */
	public static FHGUI getInstance()
	{
		return aGUI;
	}

	/**
	 * Initialize the program.
	 */
	public void init()
	{
		// Display the start up screen.
		aCenterPanel.add(aStart);
		validate();
		
		// Wait for player input.
		synchronized (aLock)
		{
			while (!aPlayerInput)
			{
				try
				{
					aLock.wait();
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Sets the game engine for the game.
	 * 
	 * @param pEngine The game engine used by the game.
	 */
	public void setEngine(GameEngine pEngine)
	{
		aEngine = pEngine;
		// Add this as an observer of the game engine.
		aEngine.addGameObserver(this);
		// Add an observer to keep track of statistics.
		aEngine.addObserver(stats);
		// Add an observer to log the events of the game.
		aEngine.addObserver(new LogObserver());

		aStats = new StatsPanel(aEngine);

		c.gridheight = 3;
		c.gridx = 3;
		c.gridy = 0;
		
		// Display the game statistics panel.
		add(aStats, c);

		c.gridheight = 1;
	}

	/**
	 * Set up the beginning of a new game.
	 */
	public void setUp()
	{
		// Add a KeyListener.
		manager.addKeyEventDispatcher(aDispatcher);

		aEngine.newGame();
		
		// Reset game stats.
		aStats.resetScores();

		// Display names of each player.
		for (int i = 0; i < 4; i++)
		{
			aHands[i].setName(aEngine.getPlayer(i));
		}
	}

	/**
	 * Deal cards to all players.
	 */
	public void deal()
	{
		// Reset tricks won, contract holder and trump.
		aStats.resetRound();

		// Reset all hands.
		for (HandPanel h : aHands)
		{
			h.reset();
		}
		
		// Deal cards.
		aEngine.deal();

		aCenterPanel.removeAll();

		// Add bid panel
		c.gridx = 0;
		c.gridy = 0;
		aCenterPanel.add(aBid, c);
		
		// Add info panel.
		c.gridy++;
		aCenterPanel.add(aInfo, c);

		// Display the hands of all players.
		for (int i = 0; i < 4; i++)
		{
			if (i == 0)
			{
				aHands[i].setHand(aHand);
			}
			else
			{
				// Show cards of other players face down.
				aHands[i].setHand(null);
			}
		}

		validate();
	}

	/**
	 * Begin bidding.
	 */
	public void bidStart()
	{
		aEngine.bidStart();
		aBidPosition = 0;
	}

	/**
	 * As for the bid of the next player.
	 */
	public void nextBid()
	{
		aHands[aEngine.getTurn()].setTurn();
		
		aEngine.nextBid();
		pause(1);
		
		aHands[(aEngine.getTurn() + 3) % 4].deselect();
	}

	/**
	 * End of the bidding round.  Returns false if all bids were passes.
	 * 
	 * @return True if a non-passing bid was made, false otherwise.
	 */
	public boolean bidEnd()
	{
		pause(1);

		// Return false if all bids were passes.
		if (aEngine.allPasses())
		{
			aInfo.clearInfo();
			return false;
		}

		aEngine.bidEnd();

		aInfo.clearInfo();
		aCenterPanel.remove(aBid);
		
		// Display the player with the highest bid and their contract.
		aInfo.setInfo(0,
				aEngine.getPlayer(aEngine.getContractHolder()) + " holds the contract of " + aEngine.getContract());

		// If the human player won the contract, enter exchange state.
		if (aEngine.getContractHolder() == 0)
		{
			playerExchange = true;
		}

		validate();

		// Update stats panel with new contract holder and trump suit.
		aStats.updateContract();

		return true;
	}

	/**
	 * Ask the contract holder to exchange their cards.
	 */
	public void exchange()
	{
		pause(2);

		// If a computer player has the contract, have them exchange cards without displaying to the user.
		if (!playerExchange)
		{
			aEngine.exchangeStart();
			aEngine.exchangeDone();
			
			validate();
			return;
		}

		// If the human player has the contract, set up the UI so that they can select cards to discard.
		aInfo.clearInfo();
		aInfo.setInfo(0, "Select six cards to discard.");
		
		aHands[0].setExchange();
		
		aEngine.exchangeStart();
		
		// Add discard button.
		c.gridx = 1;
		c.gridy = 3;
		add(aDiscard, c);
		
		// Set new hand including widow.
		aHands[0].reset();
		aHands[0].setHand(aHand);
		
		validate();
		
		if (autoplay)
		{
			pause(1);
		}
		
		aEngine.exchangeDone();
		
		remove(aDiscard);
		
		// Set new hand once exchange is done.
		aHands[0].reset();
		aHands[0].setHand(aHand);
		
		playerExchange = false;
		validate();
	}

	/**
	 * Ready the GUI for the start of a trick.
	 */
	public void playStart()
	{
		aInfo.clearInfo();
		aCenterPanel.removeAll();
		
		// Indicate that exchange is done and play state has begun.
		aHands[0].resetExchange();
		aHands[0].setPlay();
		
		// Add the trick panel.
		aCenterPanel.add(aTrick);
		
		repaint();
		validate();
	}

	/**
	 * Asks for the play of the next player.
	 */
	public void nextPlay()
	{
		aTrick.resetText();
		
		aHands[aEngine.getTurn()].setTurn();
		
		// If it is a computer player's turn or autoplay is on, pause between each play.
		if (aEngine.getTurn() != 0 || autoplay)
		{
			pause(aPlaySpeed);
		}
		
		aEngine.nextPlay();
		
		aHands[(aEngine.getTurn() + 3) % 4].deselect();
		
		validate();
	}

	/**
	 * Called at the end of a trick to remove discarded cards and update the stats panel.
	 */
	public void trickEnd()
	{
		pause(aPlaySpeed);
		
		aEngine.trickEnd();
		
		// Display the winner of the trick.
		declareWinner();
		
		// Remove all discarded cards.
		aTrick.reset();
		
		// Update stats panel.
		aStats.updateTricks();
	}

	/**
	 *  Displays the winner of the trick at the center of the frame.
	 */
	public void declareWinner()
	{
		aTrick.winner(aEngine.getPlayer(aEngine.getTrickWinner()));
		pause(2);
	}

	/**
	 * Called at the end of a round of 10 tricks and returns true if the game is over
	 * based on the accumulated points of each team.
	 * 
	 * @return True if the game is over, false otherwise.
	 */
	public boolean roundEnd()
	{
		aHands[0].resetPlay();

		// Computer and update new scores for each team.
		aEngine.computeScore();
		aStats.updateScores();

		return aEngine.isGameOver();
	}

	/**
	 * Called at the end of a game to display the game over screen.
	 */
	public void gameOver()
	{	
		manager.removeKeyEventDispatcher(aDispatcher);

		aCenterPanel.removeAll();

		// Determine whether to display a win screen or a lose screen.
		if (aEngine.isGameWinner(0))
		{
			aGameOver = new GameOverScreen(GameOverScreen.WIN);
			aCenterPanel.add(aGameOver);
			aGameOver.setStats(stats.getStatistics());
		}
		else
		{
			aGameOver = new GameOverScreen(GameOverScreen.LOSE);
			aCenterPanel.add(aGameOver);
			aGameOver.setStats(stats.getStatistics());
		}

		validate();
		
		// Wait for player input.
		synchronized (aLock)
		{
			aPlayerInput = false;
			while (!aPlayerInput)
			{
				try
				{
					aLock.wait();
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Sets the ai of a computer player.
	 * 
	 * @param pPlayer The index of the player.
	 * @param pLevel The ai level of the player.
	 * @pre pPlayer > 0 && pPlaer <= 3
	 * @pre pLevel > 0 && pLevel <= 3
	 */
	public void setAI(int pPlayer, int pLevel)
	{
		aEngine.setAI(pPlayer, pLevel);
	}

	/**
	 * Sets the turn to that of the human player, allowing them to make a bid or a play.
	 */
	public void setTurn()
	{
		aHands[0].setSelectable();
	}

	/**
	 * Returns true if it is the human player's turn.
	 * 
	 * @return True if it is the human player's turn, false otherwise.
	 */
	public boolean isSelectable()
	{
		return aHands[0].isSelectable();
	}

	/**
	 * Indicate that it is no longer the human player's turn.
	 */
	public void resetSelectable()
	{
		aHands[0].resetSelectable();
	}

	/**
	 * Sets the bid made by the human player.
	 * 
	 * @param pBid The bid selected by the human player.
	 * @pre pBid != null
	 */
	public void setBid(Bid pBid)
	{
		aPlayerBid = pBid;
	}

	/**
	 * Gets the bid of the human player.
	 * 
	 * @return The bid made by the human player.
	 */
	public Bid getBid()
	{
		return aPlayerBid;
	}

	/**
	 * Gets the list of cards chosen to be discarded by the human player.
	 * 
	 * @return The list of cards to discard by the human player.
	 */
	public CardList getCardsToDiscard()
	{
		return aCardsToDiscard;
	}

	/**
	 * Returns the list of playable cards in the human player's hand.
	 * 
	 * @return The list of playable cards in the human player's hand.
	 */
	public CardList getPlayableCards()
	{
		return aEngine.getPlayableCards();
	}

	/**
	 * Sets the card played by the human player.
	 * 
	 * @param pCard The card to be discarded by the human player.
	 */
	public void setPlay(Card pCard)
	{
		aPlay = pCard;
		aTrick.resetText();
	}

	/**
	 * Get the card played by the human player.
	 * 
	 * @return The card played by the human player.
	 */
	public Card getPlay()
	{
		return aPlay;
	}

	/**
	 * Get the highest current bid.
	 * 
	 * @return The highest current bid.
	 */
	public Bid highestBid()
	{
		return aEngine.highestBid();
	}

	/**
	 * Display a warning message if the user tried to make an illegal bid.
	 */
	public void invalidBid()
	{
		aInfo.setError("You must either pass or select a bid higher than " + aEngine.highestBid());
	}

	/**
	 * Display a warning message if the user tried to discard less than 6 cards.
	 */
	public void invalidExchange()
	{
		aInfo.setError("You must select six cards to discard.");
	}

	/**
	 * Display a warning message if the user tried to make an illegal play.
	 */
	public void invalidPlay()
	{
		aTrick.error();
	}

	/**
	 * Reset the global statistical data.
	 */
	public void resetStats()
	{
		stats.resetStats();
		aGameOver.setStats(stats.getStatistics());
	}

	/**
	 * Pause execution for a specified number of seconds.
	 * 
	 * @param sec The number of seconds to pause.
	 */
	public static void pause(int pSec)
	{
		long time = System.currentTimeMillis();
		while (System.currentTimeMillis() < time + pSec * 1000)
		{
			// do nothing
		}
	}

	/**
	 * Get the name of a player.
	 * 
	 * @param pPlayer The index of the player.
	 * @return The name of the player.
	 * @pre pPlayer >= 0 && <= 3
	 */
	public String getPlayer(int pPlayer)
	{
		return aEngine.getPlayer(pPlayer);
	}

	/**
	 * Get the ai level of a computer player.
	 * 
	 * @param pPlayer The index of the player.
	 * @return The ai level of the player.
	 * @pre pPlayer > 0 && <= 3
	 */
	public String getAI(int pPlayer)
	{
		return aEngine.getAI(pPlayer);
	}

	/**
	 * @return True if autoplay mode is on.
	 */
	public boolean isAutoPlayOn()
	{
		return autoplay;
	}

	/**
	 * Sets autoplay to on.
	 */
	public void startAutoPlay()
	{
		autoplay = true;
	}

	/**
	 * Sets autoplay to off.
	 */
	public void stopAutoPlay()
	{
		autoplay = false;
	}
	
	/**
	 * Sets the time between computer player plays in seconds.
	 * 
	 * @param pSpeed The number of seconds between plays.
	 */
	public void setPlaySpeed(int pSpeed)
	{
		aPlaySpeed = pSpeed;
	}

	/**
	 * @return True if player has made an input, false otherwise.
	 */
	public boolean hasPlayerInput()
	{
		return aPlayerInput;
	}
	
	/**
	 * @param pInput Sets aPlayerInput.
	 */
	public void setPlayerInput(boolean pInput)
	{
		aPlayerInput = pInput;
	}
	
	/**
	 * REUSED
	 * Snippet from Rob Camick's ExitAction class http://tips4java.wordpress.com/2009/05/01/closing-an-application/
	 * 
	 * Closes the application.
	 */
	public static void end()
	{
		for (Frame frame : Frame.getFrames())
		{
			if (frame.isActive())
			{
				WindowEvent windowClosing = new WindowEvent(frame, WindowEvent.WINDOW_CLOSING);
				frame.dispatchEvent(windowClosing);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("Discard"))
		{
			// Get the list of cards to be discarded.
			aCardsToDiscard = aHands[0].cardsToDiscard();
			if (aCardsToDiscard == null)
			{
				// Display warning message if less than 6 cards were selected.
				invalidExchange();
				return;
			}

			synchronized (aLock)
			{
				// Notify that the user has made a selection.
				aPlayerInput = true;
				aLock.notify();
			}
		}
	}

	@Override
	public void newHand(Hand pNewHand)
	{
		aHand = pNewHand;
	}

	@Override
	public void discard(Card pCard)
	{
		aTrick.play(aEngine.getTurn(), pCard);
		aHands[aEngine.getTurn()].discard(pCard);
	}

	@Override
	public void newBid(String pName, Bid pBid)
	{
		if (pBid.isPass())
		{
			aInfo.setInfo(aBidPosition, pName + " passes.");
		}
		else
		{
			aInfo.setInfo(aBidPosition, pName + " bids " + pBid + ".");
		}
		aBidPosition++;
	}

	@Override
	public void exchangeHand(Hand pHand)
	{
		aHand = pHand;
	}

	/**
	 * REUSED
	 * Code obtained at http://stackoverflow.com/questions/286727/java-keylistener-for-jframe-is-being-unresponsive
	 *
	 * Creates a KeyListener for the GUI.
	 */
	private class MyDispatcher implements KeyEventDispatcher
	{
		@Override
		public boolean dispatchKeyEvent(KeyEvent e)
		{
			if (e.getID() == KeyEvent.KEY_PRESSED)
			{
				// Toggle autoplay when the user presses spacebar.
				if (e.getKeyCode() == KeyEvent.VK_SPACE)
				{
					if (autoplay == true)
					{
						autoplay = false;
						aStats.stopAutoplay();
					}
					else
					{
						autoplay = true;
						aStats.startAutoplay();
					}

					synchronized (aLock)
					{
						aPlayerInput = true;
						aLock.notify();
					}
				}
			}
			return false;
		}
	}
	
	public static void main(String[] args)
	{
		FHGUI lGUI = getInstance();

		lGUI.init();
		
		while (true)
		{
			lGUI.setUp();

			boolean bid = false;
			boolean isGameOver = false;

			while (!isGameOver)
			{
				do
				{
					lGUI.deal();
					lGUI.bidStart();

					for (int i = 0; i < 4; i++)
					{
						lGUI.nextBid();
					}
					
					bid = lGUI.bidEnd();
				} while (!bid);

				lGUI.exchange();

				lGUI.playStart();

				for (int i = 0; i < 10; i++)
				{
					for (int j = 0; j < 4; j++)
					{
						lGUI.nextPlay();
					}
					lGUI.trickEnd();
				}

				isGameOver = lGUI.roundEnd();
			}

			lGUI.gameOver();
		}
	}
}