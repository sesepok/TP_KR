package farkle.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LobbyScreen  extends JPanel
{
	private static final long serialVersionUID = 1L;
	private GUI parent;
	
	private JPanel leftPanel = new JPanel();
	private JPanel centralPanel = new JPanel();
	private JPanel rightPanel = new JPanel();
	
	private JButton leaveButton = new JButton("Leave");
	
	private JButton startButton = new JButton("Start");
	
	private JLabel titleLabel = new JLabel("Connected players:");
	
	private JLabel[] playerNameLabels = new JLabel[6];
	

	public static final String LEAVE = "LOBBY_LEAVE";
	public static final String START = "LOBBY_START";
	
	public LobbyScreen(GUI parent, boolean host)
	{
		super();
		
		this.parent = parent;
		
		this.setLayout(new GridLayout(1, 3, 0, 0));
		this.add(leftPanel);
		this.add(centralPanel);
		this.add(rightPanel);
		
		titleLabel.setFont(titleLabel.getFont().deriveFont(28f));
		
		leftPanel.setLayout(new BorderLayout());
		leaveButton.setPreferredSize(new Dimension(1, 60));
		leaveButton.setName(LEAVE);
		leaveButton.addActionListener(parent);
		leftPanel.add(leaveButton, BorderLayout.SOUTH);
		
		centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.Y_AXIS));
		centralPanel.add(titleLabel);
		
		
		if (host)
		{
			rightPanel.setLayout(new BorderLayout());
			startButton.setPreferredSize(new Dimension(1, 60));
			startButton.setName(START);
			startButton.addActionListener(parent);
			rightPanel.add(startButton, BorderLayout.SOUTH);
		}
	}
	
	
	
	private void update()
	{
		centralPanel.removeAll();
		centralPanel.add(titleLabel);
		centralPanel.add(new JLabel("   "));
		for (int i = 0; i < 6; i++)
			if (playerNameLabels[i] != null)
				centralPanel.add(playerNameLabels[i]);
		
		repaint();
		revalidate();
	}
	
	
	public void addPlayerName(String name, int index)
	{
		playerNameLabels[index] = new JLabel(name);
		playerNameLabels[index].setFont(playerNameLabels[index].getFont().deriveFont(28f));
		update();
	}
	
	public void removePlayerName(int index)
	{
		playerNameLabels[index] = null;
		update();
	}
}
