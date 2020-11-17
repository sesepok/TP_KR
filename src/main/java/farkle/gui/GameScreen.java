package farkle.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
	
	private JButton leaveButton = new JButton("Leave");
	
	public GameScreen(GUI parent)
	{
		super();
		
		this.parent = parent;
		
		setLayout(new BorderLayout());
		setBackground(Color.cyan);
		
		bottomPanel.setBackground(Color.red);
		bottomPanel.setPreferredSize(new Dimension(1, 100));
		add(bottomPanel, BorderLayout.SOUTH);
		//bottomPanel.add(testLabel);
		
		leftPanel.setBackground(Color.green);
		leftPanel.setPreferredSize(new Dimension(350, 1));
		add(leftPanel, BorderLayout.WEST);
		
		mainPanel.setBackground(Color.blue);
		add(mainPanel);
		
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
		leaveButton.setMaximumSize(new Dimension(300, 50));
		leaveButton.setName(LEAVE);
		leaveButton.addActionListener(parent);
		bottomPanel.add(Box.createHorizontalStrut(25));
		bottomPanel.add(leaveButton);
		
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
	}

}
