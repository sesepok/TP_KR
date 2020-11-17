package farkle.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

class GameScreen extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	public static final String LEAVE = "LEAVE";
	
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
		//bottomPanel.add(testLabel);
		
		leftPanel.setBackground(Color.green);
		leftPanel.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.gray));
		leftPanel.setPreferredSize(new Dimension(350, 1));
		add(leftPanel, BorderLayout.WEST);
		
		mainPanel.setBackground(Color.blue);
		add(mainPanel);
		
		bottomPanel.setLayout(new BorderLayout());
		leaveButton.setMaximumSize(new Dimension(300, 50));
		leaveButton.setMinimumSize(new Dimension(300, 50));
		leaveButton.setPreferredSize(new Dimension(300, 50));
		leaveButton.setName(LEAVE);
		leaveButton.addActionListener(parent);
		//bottomPanel.add(Box.createHorizontalStrut(25));
		leaveButtonPanel.setBackground(Color.cyan);
		leaveButtonPanel.setPreferredSize(new Dimension(350, 1));
		leaveButtonPanel.setLayout(new GridBagLayout());
		bottomPanel.add(leaveButtonPanel, BorderLayout.WEST);
		leaveButtonPanel.add(leaveButton);
		bottomPanel.add(generalInfoPanel);
		generalInfoPanel.setLayout(new GridBagLayout());
		generalInfoPanel.add(generalInfoLabel);
		
		mainPanel.setLayout(new GridLayout(5, 1));
		JPanel dummy1 = new JPanel();
		dummy1.setOpaque(false);
		mainPanel.add(dummy1);
		JPanel dummy2 = new JPanel();
		dummy2.setOpaque(false);
		mainPanel.add(dummy2);
		dicePanel.setBackground(Color.cyan);
		mainPanel.add(dicePanel);
		selectedInfoPanel.setBackground(Color.orange);
		mainPanel.add(selectedInfoPanel);
		playerActionsPanel.setBackground(Color.yellow);
		mainPanel.add(playerActionsPanel);
		
		playerActionsPanel.setLayout(new GridLayout(1, 2));
		rollButtonPanel.setBackground(Color.pink);
		playerActionsPanel.add(rollButtonPanel);
		bankButtonPanel.setBackground(Color.magenta);
		playerActionsPanel.add(bankButtonPanel);
		
		selectedInfoPanel.setLayout(new GridBagLayout());
		selectedInfoPanel.add(selectedInfoLabel);
		
		rollButtonPanel.setLayout(new GridBagLayout());
		rollButton.setPreferredSize(new Dimension(300, 100));
		rollButtonPanel.add(rollButton);
		bankButtonPanel.setLayout(new GridBagLayout());
		bankButton.setPreferredSize(new Dimension(300, 100));
		bankButtonPanel.add(bankButton);
		
	}

}
