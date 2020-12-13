package farkle.gui;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class RulesDialog extends JDialog implements ActionListener
{
	//private JPanel imagePanel = new JPanel();
	private JPanel buttonPanel = new JPanel();
	
	private ImageIcon image = new ImageIcon(getClass().getResource("/rules.png"));
	private JLabel imageLabel = new JLabel(image);
	
	private JButton button = new JButton("OK");
	
	public RulesDialog(GUI parent)
	{
		super(parent, "Rules", Dialog.ModalityType.APPLICATION_MODAL);
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		add(imageLabel);
		add(buttonPanel);
		buttonPanel.setLayout(new GridBagLayout());		
		button.addActionListener(this);
		button.setPreferredSize(new Dimension(100, 50));
		buttonPanel.add(button);
		pack();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		dispose();
	}

}
