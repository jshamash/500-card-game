package comp303.fivehundred.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import comp303.fivehundred.gui.external.JTextFieldLimit;

import comp303.fivehundred.engine.GameEngine;

/**
 * The inital screen which allows names to be changed and the playing level of the AIs to be changed.
 * 
 *  The level selection and entered name for each AI is named by their player number so player 2 is AI2
 * 
 * @author Abigail White
 * 
 */
@SuppressWarnings("serial")
public class StartUpScreen extends JPanel implements ActionListener
{
	// Selection types for AI level.
	private String[] aiLevel = { "Random", "Basic", "Advanced" };
	
	private JLabel title = new JLabel("FIVE HUNDRED");
	
	private JLabel team1 = new JLabel("TEAM 1");
	private JLabel team2 = new JLabel("TEAM 2");
	
	private JLabel level = new JLabel("Select Robot Level:  ");
	private JLabel level1 = new JLabel("Select Robot Level:  ");
	private JLabel level2 = new JLabel("Select Robot Level:  ");
	
	private JLabel name = new JLabel("Enter your name:");
	private JLabel name1 = new JLabel("Enter a name:");
	private JLabel name2 = new JLabel("Enter a name:");
	private JLabel name3 = new JLabel("Enter a name:");
	
	// Limit name length to 10 chars and initialize with default names.
	private JTextField nPlayer1 = new JTextField(new JTextFieldLimit(10), "Player 1", 7);
	private JTextField nPlayer2 = new JTextField(new JTextFieldLimit(10), "Player 2", 7);
	private JTextField nPlayer3 = new JTextField(new JTextFieldLimit(10), "Player 3", 7);
	private JTextField nPlayer4 = new JTextField(new JTextFieldLimit(10), "Player 4", 7);
	
	private JComboBox cAI2 = new JComboBox(aiLevel); // Level of Player 2 AI
	private JComboBox cAI3 = new JComboBox(aiLevel); // Level of Player 3 AI
	private JComboBox cAI4 = new JComboBox(aiLevel); // Level of Player4 AI
	
	private JButton bStart = new JButton("Start Game");
	private JButton bQuit = new JButton("Quit");

	/**
	 * Constructor.
	 */
	public StartUpScreen()
	{
		bStart.addActionListener(this);
		bQuit.addActionListener(this);
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		title.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
		team1.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
		team2.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
		
		// row 0 "New Game"
		c.gridx = 4;
		c.gridy = 0;
		add(title, c);
		
		c.gridy++;
		add(new JLabel(" "), c);
		
		c.gridy++;
		add(new JLabel(" "), c);
		
		// row 1 "Team 1:" |"Team 2:"
		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 2;
		add(team1, c);
		
		c.gridx = 5;
		c.gridwidth = 2;
		add(team2, c);
		
		// row 2 "enter your name" player1 "enter aI name" player 2
		c.gridx = 0;
		c.gridy++;
		add(name, c);
		
		c.gridx = 2;
		add(nPlayer1, c);
		
		c.gridx = 5;
		add(name1, c);
		
		c.gridx = 7;
		add(nPlayer2, c);
		
		// row 3 "robot level" ailevels
		c.gridx = 5;
		c.gridy++;
		add(level, c);
		
		c.gridx = 7;
		add(cAI2, c);
		
		c.gridy++;
		add(new JLabel(" "), c);
		
		// row 4 "enter a name" player3 "enter AI name" player4
		c.gridx = 0;
		c.gridy++;
		add(name2, c);
		
		c.gridx = 2;
		add(nPlayer3, c);
		
		c.gridx = 5;
		add(name3, c);
		
		c.gridx = 7;
		add(nPlayer4, c);
		
		// row 5 "robot level" aiLevels "robot level" aiLevels
		c.gridx = 0;
		c.gridy++;
		add(level1, c);
		
		c.gridx = 2;
		add(cAI3, c);
		
		c.gridx = 5;
		add(level2, c);
		
		c.gridx = 7;
		add(cAI4, c); 
		
		c.gridy++;
		add(new JLabel(" "), c);
		
		c.gridy++;
		add(new JLabel(" "), c);
		
		// row 8 start button	
		c.gridx = 4;
		c.gridy++;
		add(bStart, c);
		
		c.gridy++;
		add(new JLabel(" "), c);
		
		// row 7 quit button
		c.gridy++;
		add(bQuit, c);
		
		setBorder(new CompoundBorder(new LineBorder(new Color(139, 76, 57), 3), new EmptyBorder(30, 15, 30, 15)));
		setBackground(new Color(238, 154, 0));
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		String[] lNames = new String[4];
		int[] lTypes = new int[4];
		
		if (e.getActionCommand().equals("Quit"))
		{
			FHGUI.end();
		}
		
		// Get chosen names for all players.
		lNames[0] = nPlayer1.getText();
		lNames[1] = nPlayer2.getText();
		lNames[2] = nPlayer3.getText();
		lNames[3] = nPlayer4.getText();
		
		// Get ai level for all computer players.
		lTypes[0] = 0;
		lTypes[1] = FHGUI.aiLevels.get(cAI2.getSelectedItem());
		lTypes[2] = FHGUI.aiLevels.get(cAI3.getSelectedItem());
		lTypes[3] = FHGUI.aiLevels.get(cAI4.getSelectedItem());
		
		FHGUI.getInstance().setEngine(new GameEngine(lNames, lTypes));
		
		synchronized(FHGUI.aLock)
		{
			// Notify the game engine that the user has made a selection.
			FHGUI.getInstance().setPlayerInput(true);
			FHGUI.aLock.notify();
		}

	}
}
