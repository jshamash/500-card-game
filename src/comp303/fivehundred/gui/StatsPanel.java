package comp303.fivehundred.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import comp303.fivehundred.engine.GameEngine;

/**
 * Panel which displays various information about the state of the current game.
 * 
 * @author Brandon Hum
 *
 */
@SuppressWarnings("serial")
public class StatsPanel extends JPanel implements ChangeListener
{
	private GameEngine aEngine; // The game engine from which information will be retrieved.
	private JLabel aScoreTitle = new JLabel("SCORES"); // Title label for the scores.
	private JLabel aTeam1 = new JLabel("Team 1:   0"); // Label which displays the score of team 1.
	private JLabel aTeam2 = new JLabel("Team 2:   0"); // Label which displays the score of team 2.
	private JLabel aTricksTitle = new JLabel("TRICKS WON"); // Title label for the trick win count.
	private JLabel[] aTricks = new JLabel[4]; // Labels which display the number of tricks won by each player.
	private JLabel aContractTitle = new JLabel("CONTRACT HOLDER"); // Title label for the contract holder.
	private JLabel aContractHolder = new JLabel("???"); // Label which displays the contract holder and their contract.
	private JLabel aTrumpTitle = new JLabel("TRUMP"); // Title label for the trump.
	private JLabel aTrump = new JLabel("???"); // Label which displays the trump suit.
	private JLabel aAiLevelTitle = new JLabel("AI INTELLIGENCE"); // Title label for ai levels.
	private JLabel[] aAiLevels = new JLabel[3];
	private JLabel aSpeedTitle = new JLabel("GAME SPEED"); // Title label for the game speed selector.
	private JSlider aGameSpeed; // Slider used to select the game speed.
	private JLabel aAutoplayTitle = new JLabel("AUTOPLAY"); // Title label for the autoplay indicator.
	private JLabel aAutoplay = new JLabel("OFF"); // Label which will indicate whether autoplay is on or off.
	private JLabel aAutoplayInfo = new JLabel("(Press spacebar to toggle)"); // Label which explains how to toggle autoplay.

	
	public StatsPanel(GameEngine pEngine)
	{
		super();
		
		aEngine = pEngine;
		
		setBorder(new CompoundBorder(new LineBorder(new Color(139, 76, 57), 3), new EmptyBorder(30, 10, 30, 10)));
		
		setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		// Set title fonts.
		aScoreTitle.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
		aTricksTitle.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
		aContractTitle.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
		aTrumpTitle.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
		aAiLevelTitle.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
		aSpeedTitle.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
		aAutoplayTitle.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
		
		aAutoplay.setForeground(Color.RED);
		
		// Set slider parameters.
		aGameSpeed = new JSlider();
		aGameSpeed.setMinimum(0);
		aGameSpeed.setMaximum(5);
		aGameSpeed.setPaintTicks(true);
		aGameSpeed.setMajorTickSpacing(1);
		aGameSpeed.setSnapToTicks(true);
		aGameSpeed.setValue(1);
		aGameSpeed.setPaintLabels(true);
		aGameSpeed.addChangeListener(this);
		aGameSpeed.setPreferredSize(new Dimension(150, 50));
		
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		
		add(aScoreTitle, c);
		c.gridy++;
		add(aTeam1, c);
		c.gridy++;
		add(aTeam2, c);
		
		c.gridy++;
		add(new JLabel(" "), c);
		
		c.gridy++;
		add(aTricksTitle, c);
		
		// Initialize trick labels, setting number of tricks won to 0.
		for (int i = 0; i<4; i++)
		{
			aTricks[i] = new JLabel(aEngine.getPlayer(i) + ":   0");
			c.gridy++;
			add(aTricks[i], c);
		}
		
		c.gridy++;
		add(new JLabel(" "), c);
		
		c.gridy++;
		add(aContractTitle, c);
		c.gridy++;
		add(aContractHolder, c);
		
		c.gridy++;
		add(new JLabel(" "), c);
		
		c.gridy++;
		add(aTrumpTitle, c);
		c.gridy++;
		add(aTrump, c);
		
		for (int i = 0; i<4; i++)
		{
			c.gridy++;
			add(new JLabel(" "), c);
		}
		
		c.gridy++;
		add(aAiLevelTitle, c);
		
		// Initialize AI level labels.
		for (int i = 0; i<3; i++)
		{
			aAiLevels[i] = new JLabel(aEngine.getPlayer(i + 1) + ":   " + aEngine.getAI(i+1));
			c.gridy++;
			add(aAiLevels[i], c);
		}
		
		c.gridy++;
		add(new JLabel(" "), c);

		c.gridy++;
		add(aSpeedTitle, c);
		c.gridy++;
		add(aGameSpeed, c);
		
		c.gridy++;
		add(aAutoplayTitle, c);
		c.gridy++;
		add(aAutoplay, c);	
		c.gridy++;
		add(aAutoplayInfo, c);
	}

	/**
	 *  Set contract holder and trump suit.
	 */
	public void updateContract()
	{
		aContractHolder.setText(aEngine.getPlayer(aEngine.getContractHolder()) + ":   " + aEngine.getContract());
		
		aTrump.setText(aEngine.getTrump());
	}
	
	/**
	 * Updates the trick labels.
	 */
	public void updateTricks()
	{
		for (int i = 0; i<4; i++)
		{
			aTricks[i].setText(aEngine.getPlayer(i) + ":   " + aEngine.getTricksWon(i));
		}
	}
	
	/**
	 * Updates the score labels.
	 */
	public void updateScores()
	{
		aTeam1.setText("Team 1:   " + aEngine.getGameScore(0));
		aTeam2.setText("Team 2:   " + aEngine.getGameScore(1));
	}
	
	/**
	 * Resets labels to begin a new round.
	 */
	public void resetRound()
	{
		aContractHolder.setText("???");
		
		aTrump.setText("???");
		
		for (int i = 0; i<4; i++)
		{
			aTricks[i].setText(aEngine.getPlayer(i) + ":   0");
		}
		
		for (int i = 0; i<3; i++)
		{
			aAiLevels[i] = new JLabel(aEngine.getPlayer(i + 1) + ":   " + aEngine.getAI(i+1));
		}
	}

	/**
	 * Resets scores to begin a new game.
	 */
	public void resetScores()
	{
		aTeam1.setText("Team 1:   0");
		aTeam2.setText("Team 2:   0");
	}
	
	/**
	 * Sets autoplay indicator to "ON".
	 */
	public void startAutoplay()
	{
		aAutoplay.setText("ON");
		aAutoplay.setForeground(new Color(44,151,11));
	}
	
	/**
	 * Sets autoplay indicator to "OFF".
	 */
	public void stopAutoplay()
	{
		aAutoplay.setText("OFF");
		aAutoplay.setForeground(Color.RED);
	}

	@Override
	public void stateChanged(ChangeEvent e)
	{
		// Changes the game speed when the slider is moved.
		FHGUI.getInstance().setPlaySpeed(aGameSpeed.getValue());	
	}
}
