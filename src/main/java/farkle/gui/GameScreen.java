package farkle.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NoSuchObjectException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import farkle.game.Die;
import farkle.game.GameState;

class GameScreen extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	public static final String LEAVE = "LEAVE";
	public static final String ROLL = "ROLL";
	public static final String BANK = "BANK";
	
	private GUI parent;
	
	private JPanel bottomPanel = new JPanel();
	private JPanel leftPanel = new JPanel();
	private JPanel mainPanel = new JPanel();
	private JPanel dicePanel = new JPanel();
	private JPanel selectedInfoPanel = new JPanel();
	private JPanel playerActionsPanel = new JPanel();
	private JPanel rollButtonPanel = new JPanel();
	private JPanel bankButtonPanel = new JPanel();
	private JPanel leaveButtonPanel = new JPanel();
	private JPanel generalInfoPanel = new JPanel();
	
	private JButton leaveButton = new JButton("Leave");
	private JButton rollButton = new JButton("Roll");
	private JButton bankButton = new JButton("Bank");
	
	private JLabel selectedInfoLabel = new JLabel("SELECTED INFO LABEL");
	private JLabel generalInfoLabel = new JLabel("GENERAL INFO LABEL");
	
	private ImageIcon noneDieIcon = new ImageIcon(getClass().getResource("/none.png"));
	private ImageIcon oneDieIcon = new ImageIcon(getClass().getResource("/one.png"));
	private ImageIcon twoDieIcon = new ImageIcon(getClass().getResource("/two.png"));
	private ImageIcon threeDieIcon = new ImageIcon(getClass().getResource("/three.png"));
	private ImageIcon fourDieIcon = new ImageIcon(getClass().getResource("/four.png"));
	private ImageIcon fiveDieIcon = new ImageIcon(getClass().getResource("/five.png"));
	private ImageIcon sixDieIcon = new ImageIcon(getClass().getResource("/six.png"));
	
	private ImageIcon oneDieIconSelected = new ImageIcon(getClass().getResource("/oneSelected.png"));
	private ImageIcon twoDieIconSelected = new ImageIcon(getClass().getResource("/twoSelected.png"));
	private ImageIcon threeDieIconSelected = new ImageIcon(getClass().getResource("/threeSelected.png"));
	private ImageIcon fourDieIconSelected = new ImageIcon(getClass().getResource("/fourSelected.png"));
	private ImageIcon fiveDieIconSelected = new ImageIcon(getClass().getResource("/fiveSelected.png"));
	private ImageIcon sixDieIconSelected = new ImageIcon(getClass().getResource("/sixSelected.png"));
	
	private ImageIcon oneDieIconLocked = new ImageIcon(getClass().getResource("/oneLocked.png"));
	private ImageIcon twoDieIconLocked = new ImageIcon(getClass().getResource("/twoLocked.png"));
	private ImageIcon threeDieIconLocked = new ImageIcon(getClass().getResource("/threeLocked.png"));
	private ImageIcon fourDieIconLocked = new ImageIcon(getClass().getResource("/fourLocked.png"));
	private ImageIcon fiveDieIconLocked = new ImageIcon(getClass().getResource("/fiveLocked.png"));
	private ImageIcon sixDieIconLocked = new ImageIcon(getClass().getResource("/sixLocked.png"));
	
	
	private JButton[] diceButtons = new JButton[6];
	
	private class PlayerRecord extends JPanel
	{
		public JLabel nameLabel;
		public JLabel bankLabel;
		public JLabel scoreLabel;
		
		public PlayerRecord(String name)
		{
			setBorder(new EmptyBorder(5, 10, 15, 0));
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			setOpaque(false);
			nameLabel = new JLabel(name);
			nameLabel.setFont(nameLabel.getFont().deriveFont(20f));
			add(nameLabel);
			bankLabel = new JLabel();
			bankLabel.setFont(nameLabel.getFont().deriveFont(20f));
			add(bankLabel);
			scoreLabel = new JLabel();
			scoreLabel.setFont(nameLabel.getFont().deriveFont(20f));
			add(scoreLabel);
			
			setBank(0); setScore(0);
		}
		
		public void setBank(int n)
		{
			bankLabel.setText("Bank: " + n);
		}
		public void setScore(int n)
		{
			scoreLabel.setText("Score: " + n);
		}
		
		public void select()
		{
			nameLabel.setForeground(Color.yellow);
			bankLabel.setForeground(Color.yellow);
			scoreLabel.setForeground(Color.yellow);
		}
		
		public void deselect()
		{
			nameLabel.setForeground(Color.black);
			bankLabel.setForeground(Color.black);
			scoreLabel.setForeground(Color.black);
		}
	}
	
	private PlayerRecord[] playerRecords;
	
	public GameScreen(GUI parent)
	{
		super();
		
		this.parent = parent;
		
		setLayout(new BorderLayout());
		setBackground(Color.cyan);
		
		bottomPanel.setBackground(Color.red);
		bottomPanel.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.gray));
		bottomPanel.setPreferredSize(new Dimension(1, 100));
		add(bottomPanel, BorderLayout.SOUTH);
		
		//leftPanel.setBackground(Color.green);
		leftPanel.setBackground(new Color(127, 127, 97));
		leftPanel.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.gray));
		leftPanel.setPreferredSize(new Dimension(350, 1));
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		add(leftPanel, BorderLayout.WEST);
		
		mainPanel.setBackground(new Color(0, 100, 0));
		add(mainPanel);
		
		bottomPanel.setLayout(new BorderLayout());
		bottomPanel.setBackground(new Color(101, 67, 33));
		leaveButton.setMaximumSize(new Dimension(300, 50));
		leaveButton.setMinimumSize(new Dimension(300, 50));
		leaveButton.setPreferredSize(new Dimension(300, 50));
		leaveButton.setName(LEAVE);
		leaveButton.addActionListener(parent);
		//leaveButtonPanel.setBackground(Color.cyan);
		leaveButtonPanel.setPreferredSize(new Dimension(350, 1));
		leaveButtonPanel.setLayout(new GridBagLayout());
		leaveButtonPanel.setOpaque(false);
		bottomPanel.add(leaveButtonPanel, BorderLayout.WEST);
		leaveButtonPanel.add(leaveButton);
		bottomPanel.add(generalInfoPanel);
		generalInfoPanel.setLayout(new GridBagLayout());
		generalInfoLabel.setForeground(Color.yellow);
		generalInfoLabel.setFont(generalInfoLabel.getFont().deriveFont(24f));
		generalInfoPanel.add(generalInfoLabel);
		generalInfoPanel.setOpaque(false);
		
		mainPanel.setLayout(new GridLayout(5, 1));
		JPanel dummy1 = new JPanel();
		dummy1.setOpaque(false);
		mainPanel.add(dummy1);
		JPanel dummy2 = new JPanel();
		dummy2.setOpaque(false);
		mainPanel.add(dummy2);
		//dicePanel.setBackground(Color.cyan);
		dicePanel.setOpaque(false);
		mainPanel.add(dicePanel);
		//selectedInfoPanel.setBackground(Color.orange);
		selectedInfoPanel.setOpaque(false);
		mainPanel.add(selectedInfoPanel);
		//playerActionsPanel.setBackground(Color.yellow);
		playerActionsPanel.setOpaque(false);
		mainPanel.add(playerActionsPanel);
		
		playerActionsPanel.setLayout(new GridLayout(1, 2));
		//rollButtonPanel.setBackground(Color.pink);
		rollButtonPanel.setOpaque(false);
		playerActionsPanel.add(rollButtonPanel);
		//bankButtonPanel.setBackground(Color.magenta);
		bankButtonPanel.setOpaque(false);
		playerActionsPanel.add(bankButtonPanel);
		
		selectedInfoPanel.setLayout(new GridBagLayout());
		selectedInfoLabel.setFont(selectedInfoLabel.getFont().deriveFont(18f));
		selectedInfoLabel.setForeground(Color.yellow);
		selectedInfoPanel.add(selectedInfoLabel);
		
		rollButtonPanel.setLayout(new GridBagLayout());
		rollButton.setPreferredSize(new Dimension(300, 100));
		rollButton.setName(ROLL);
		rollButton.addActionListener(parent);
		rollButtonPanel.add(rollButton);
		bankButtonPanel.setLayout(new GridBagLayout());
		bankButton.setPreferredSize(new Dimension(300, 100));
		bankButton.setName(BANK);
		bankButton.addActionListener(parent);
		bankButtonPanel.add(bankButton);
		
		
		
		initDiceButtons();
		
		dicePanel.setLayout(new GridLayout());
		for (int i = 0; i < 6; i++)
			dicePanel.add(diceButtons[i]);
	}
	
	private void initDiceButtons()
	{
		for (int i = 0; i < 6; i++)
		{
			diceButtons[i] = new JButton(noneDieIcon);
			diceButtons[i].setBorderPainted(false);
			diceButtons[i].setFocusPainted(false);
			diceButtons[i].setContentAreaFilled(false);
			diceButtons[i].setName("die " + i);
			
			diceButtons[i].addActionListener(this);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		System.out.println("pressed!" + ((JButton)e.getSource()).getName().charAt(4));
		parent.diePressed(Integer.parseInt(((JButton)e.getSource()).getName().substring(4)));
	}
	
	
	
	private ImageIcon dieToIcon(Die die)
	{
		if (die.isSelected())
		{
			switch (die.getValue())
			{
			case 1: return oneDieIconSelected;
			case 2: return twoDieIconSelected;
			case 3: return threeDieIconSelected;
			case 4: return fourDieIconSelected;
			case 5: return fiveDieIconSelected;
			case 6: return sixDieIconSelected;
			}
		}
		else if (die.isLocked())
		{
			switch (die.getValue())
			{
			case 1: return oneDieIconLocked;
			case 2: return twoDieIconLocked;
			case 3: return threeDieIconLocked;
			case 4: return fourDieIconLocked;
			case 5: return fiveDieIconLocked;
			case 6: return sixDieIconLocked;
			}
		}
		else
		{
			switch (die.getValue())
			{
			case 1: return oneDieIcon;
			case 2: return twoDieIcon;
			case 3: return threeDieIcon;
			case 4: return fourDieIcon;
			case 5: return fiveDieIcon;
			case 6: return sixDieIcon;
			default: return noneDieIcon;
			}
		}
		return noneDieIcon;
	}
	
	public void setGameState(GameState state)
	{
		leftPanel.removeAll();
		
		playerRecords = new PlayerRecord[state.players.length];
		for (int i = 0; i < playerRecords.length; i++)
		{
			playerRecords[i] = new PlayerRecord(state.players[i].getName());
			playerRecords[i].bankLabel.setText("Bank: " + state.players[i].getBank());
			playerRecords[i].scoreLabel.setText("Score: " + state.players[i].getScore());
			leftPanel.add(playerRecords[i]);
		}
		playerRecords[state.currentPlayer].select();
		
		for (int i = 0; i < 6; i++)
		{
			diceButtons[i].setIcon(dieToIcon(state.hand.getDie(i)));
		}
		
		rollButton.setEnabled(state.rollEnabled);
		bankButton.setEnabled(state.bankEnabled);
		
		selectedInfoLabel.setText("SELECTED SCORE: " + Integer.toString(state.selectedScore));
		
		if (state.busted)
			generalInfoLabel.setText("BUSTED!");
		else if (state.victory)
			generalInfoLabel.setText(state.players[state.currentPlayer].getName() + " WON!!!");
		else
			generalInfoLabel.setText("");
		
		/*diceButtons[0].setIcon(oneDieIcon);
		diceButtons[1].setIcon(twoDieIcon);
		diceButtons[2].setIcon(threeDieIcon);
		diceButtons[3].setIcon(fourDieIcon);
		diceButtons[4].setIcon(fiveDieIcon);
		diceButtons[5].setIcon(sixDieIcon);*/
		
		revalidate();
		repaint();
	}

}
