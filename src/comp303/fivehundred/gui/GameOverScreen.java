package comp303.fivehundred.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import comp303.fivehundred.util.CardImages;

/**
 * Screen that is displayed at the end of a game.
 * 
 * @author Ashley Kyung Min Kim
 * 
 */
@SuppressWarnings("serial")
public class GameOverScreen extends JPanel implements ActionListener
{
	public static final int WIN = 0;
	public static final int LOSE = 1;

	private ImageIcon winIcon; // Win image
	private ImageIcon loseIcon; // Lose image

	private JComboBox ai1; // Level selector for the first ai.
	private JComboBox ai2; // Level selector for the second ai.
	private JComboBox ai3; // Level selector for the third ai.
	
	JLabel[] stats = new JLabel[7]; // Labels which display the statistics.
	
	/**
	 * Constructor.
	 * 
	 * @param pResult Whether the user won or lost the game.
	 */
	public GameOverScreen(int pResult)
	{
		super();
		
		// Selection types for AI level.
		String[] aiLevel = { "Random", "Basic", "Advanced" };

		// Load images.
		winIcon = new ImageIcon(CardImages.class.getClassLoader().getResource("images/win.gif"));
		loseIcon = new ImageIcon(CardImages.class.getClassLoader().getResource("images/lose.gif"));
		
		// Set the size of the lose icon.
		Image img = loseIcon.getImage();
		Image newImg = img.getScaledInstance(225, 75, java.awt.Image.SCALE_SMOOTH);
		loseIcon = new ImageIcon(newImg);
		
		// Set the size of the win icon.
		img = winIcon.getImage();
		newImg = img.getScaledInstance(225, 75, java.awt.Image.SCALE_SMOOTH);
		winIcon = new ImageIcon(newImg);
		
		JLabel result = new JLabel();

		result.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
		
		// Display win or lose icon as appropriate.
		if (pResult == WIN)
		{
			result.setIcon(winIcon);
		}
		else if (pResult == LOSE)
		{
			result.setIcon(loseIcon);
		}

		JButton newGame = new JButton("New Game");

		newGame.addActionListener(this);

		// Get ai names.
		JLabel aiName1 = new JLabel(FHGUI.getInstance().getPlayer(1));
		JLabel aiName2 = new JLabel(FHGUI.getInstance().getPlayer(2));
		JLabel aiName3 = new JLabel(FHGUI.getInstance().getPlayer(3));

		ai1 = new JComboBox(aiLevel);
		ai2 = new JComboBox(aiLevel);
		ai3 = new JComboBox(aiLevel);
		
		// Sets ai selectors to current levels of each ai.
		ai1.setSelectedItem(FHGUI.getInstance().getAI(1));
		ai2.setSelectedItem(FHGUI.getInstance().getAI(2));
		ai3.setSelectedItem(FHGUI.getInstance().getAI(3));
		
		GridBagConstraints c = new GridBagConstraints();
		
		JPanel aiPanel = new JPanel();
		aiPanel.setLayout(new GridBagLayout());
		aiPanel.setBackground(new Color(238, 154, 0));
		
		c.anchor = GridBagConstraints.CENTER;
		
		c.gridy = 0;
		aiPanel.add(aiName1, c);
		aiPanel.add(aiName2, c);
		aiPanel.add(aiName3, c);
		
		c.gridy++;
		aiPanel.add(ai1, c);
		aiPanel.add(ai2, c);
		aiPanel.add(ai3, c);
		
		for (int i = 0; i<7; i++)
		{
			stats[i] = new JLabel();
		}
		
		JButton resetStat = new JButton("Reset Statistics");
		
		resetStat.addActionListener(this);

		JButton exit = new JButton("Exit Game");

		exit.addActionListener(this);

		setBorder(new CompoundBorder(new LineBorder(new Color(139, 76, 57), 3), new EmptyBorder(30, 15, 30, 15)));

		setLayout(new GridBagLayout());

		c.gridx = 0;
		c.gridy = 0;
		add(result, c);

		c.gridy++;
		add(new JLabel(" "), c);

		c.gridy++;
		add(new JLabel(" "), c);

		c.gridy++;
		add(newGame, c);

		c.gridy++;
		add(new JLabel(" "), c);

		c.gridy++;
		add(aiPanel,c);

		c.gridy++;
		add(new JLabel(" "), c);
		
		for (int i = 0; i<7; i++)
		{
			c.gridy++;
			add(stats[i], c);
		}

		c.anchor = GridBagConstraints.CENTER;

		c.gridy++;
		add(new JLabel(" "), c);

		c.gridy++;
		add(resetStat, c);

		c.gridy++;
		add(new JLabel(" "), c);

		c.gridy++;
		add(exit, c);

		setBackground(new Color(238, 154, 0));
	}

	/**
	 * Display statistical data.
	 * 
	 * @param pStats The strings containing the stats.
	 */
	public void setStats(String[] pStats)
	{
		for (int i = 0; i<7; i++)
		{
			stats[i].setText(pStats[i]);
			stats[i].setFont(new Font("Consolas", Font.PLAIN, 12));
		}
		
		stats[0].setFont(new Font(null, Font.BOLD, 10));
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		// Exit the program if the Exit Game button is clicked.
		if (e.getActionCommand().equals("Exit Game"))
		{
			FHGUI.end();
		}
		// Resets the statistical data if the Reset Statistics button is clicked.
		else if (e.getActionCommand().equals("Reset Statistics"))
		{
			FHGUI.getInstance().resetStats();
		}
		// Begins a new game if the New Gamebutton is clicked.
		else if (e.getActionCommand().equals("New Game"))
		{
			// Set ai levels based on selections.
			FHGUI.getInstance().setAI(1, FHGUI.aiLevels.get(ai1.getSelectedItem()));
			FHGUI.getInstance().setAI(2, FHGUI.aiLevels.get(ai2.getSelectedItem()));
			FHGUI.getInstance().setAI(3, FHGUI.aiLevels.get(ai3.getSelectedItem()));

			synchronized (FHGUI.aLock)
			{
				// Notify that the user has made a selection.
				FHGUI.getInstance().setPlayerInput(true);
				FHGUI.aLock.notify();
			}
		}
	}

}
