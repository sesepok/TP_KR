package farkle.gui;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

class PlayerListDialog extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;
	GUI parent;
	
	private final int minPlayers = 2;
	private final int maxPlayers = 6;
	
	private int currentPlayers = 2;
	private JButton minusButton = new JButton("-");
	private JButton plusButton = new JButton("+");
	private JLabel numberLabel = new JLabel("2");
	
	private JPanel topPanel = new JPanel();
	private JPanel entryPanel = new JPanel();
	private JPanel bottomPanel = new JPanel();
	
	private JButton cancelButton = new JButton("Cancel");
	private JButton okButton = new JButton("OK");
	
	private class PlayerEntry extends JPanel
	{
		private static final long serialVersionUID = 1L;
		public JLabel label;
		public JTextField textField;
		
		public PlayerEntry(int number)
		{
			label = new JLabel("Player " + number + " name: ");
			textField = new JTextField("Player" + number);
			textField.setPreferredSize(new Dimension(100, 20));
			add(label);
			add(textField);
		}
	}
	
	private LinkedList<PlayerEntry> playerEntryList = new LinkedList<PlayerEntry>();
	
	public PlayerListDialog(GUI parent)
	{
		super(parent, "Name players", Dialog.ModalityType.APPLICATION_MODAL);
		this.parent = parent;
		
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		add(topPanel);
		
		entryPanel.setLayout(new BoxLayout(entryPanel, BoxLayout.Y_AXIS));
		add(entryPanel);
		
		add(bottomPanel);
		
		
		minusButton.addActionListener(this);
		topPanel.add(minusButton);
		
		numberLabel.setBorder(new EmptyBorder(0, 20, 0, 20));
		topPanel.add(numberLabel);
		
		plusButton.addActionListener(this);
		topPanel.add(plusButton);
		
		cancelButton.addActionListener(this);
		bottomPanel.add(cancelButton);
		
		okButton.addActionListener(this);
		bottomPanel.add(okButton);
		
		update();
		pack();
	}
	
	private void update()
	{
		numberLabel.setText(Integer.toString(currentPlayers));
		minusButton.setEnabled(currentPlayers > minPlayers);
		plusButton.setEnabled(currentPlayers < maxPlayers);
		
		
		
		int currentEntries = playerEntryList.size();
		while (currentPlayers > currentEntries)
		{
			PlayerEntry newEntry = new PlayerEntry(++currentEntries);
			playerEntryList.add(newEntry);
			entryPanel.add(newEntry);
		}
		
		while (currentPlayers < currentEntries)
		{
			PlayerEntry oldEntry = playerEntryList.removeLast();
			entryPanel.remove(oldEntry);
			currentEntries--;
		}
		
		pack();
		
	}
	
	public String[] getNames()
	{
		String[] names = new String [playerEntryList.size()];
		int i = 0;
		for (PlayerEntry entry : playerEntryList)
		{
			names[i] = entry.textField.getText();
			i++;
		}
		return names;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == minusButton && currentPlayers > minPlayers)
		{
			currentPlayers--;
		}
		else if (e.getSource() == plusButton && currentPlayers < maxPlayers)
		{
			currentPlayers++;
		}
		update();
		
		if (e.getSource() == cancelButton)
		{
			dispose();
		}
		else if (e.getSource() == okButton)
		{
			parent.createLocalGame(getNames());
			dispose();
		}
	}

}
