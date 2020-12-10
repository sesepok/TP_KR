package farkle.gui;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class HostGameDialog  extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;
	GUI parent;
	
	private JPanel playerNamePanel = new JPanel();
	private JPanel portPanel = new JPanel();
	private JPanel errorPanel = new JPanel();
	private JPanel buttonPanel = new JPanel();
	
	
	private JTextField playerNameTextField = new JTextField("PlayerHost");
	private JTextField portTextField = new JTextField("12345");
	
	private JButton cancelButton = new JButton("Cancel");
	private JButton okButton = new JButton("OK");
	
	private JLabel errorLabel = new JLabel();
	
	public HostGameDialog(GUI parent)
	{
		super(parent, "Host a game", Dialog.ModalityType.APPLICATION_MODAL);
		this.parent = parent;
		
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		add(playerNamePanel);
		add(portPanel);
		add(errorPanel);
		add(buttonPanel);
		
		playerNamePanel.add(new JLabel("Player name: "));
		playerNameTextField.setPreferredSize(new Dimension(100, 20));
		playerNamePanel.add(playerNameTextField);
		
		portPanel.add(new JLabel("Port:"));
		portPanel.add(portTextField);
		
		errorLabel.setForeground(Color.red);
		errorPanel.add(errorLabel);
		
		cancelButton.addActionListener(this);
		buttonPanel.add(cancelButton);
		
		okButton.addActionListener(this);
		buttonPanel.add(okButton);
		
		pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == cancelButton)
			dispose();
		else if (e.getSource() == okButton)
		{
			try
			{
				String playerName = playerNameTextField.getText();
				int port = Integer.parseInt(portTextField.getText());
				parent.confirmHostGame(playerName, port);
				dispose();
			}
			catch (Exception ex)
			{
				errorLabel.setText("Could not host a game!");
				ex.printStackTrace();
				pack();
			}
		}
	}
	
	
}
