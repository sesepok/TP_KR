package farkle.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

class MainMenu extends JPanel 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JFrame parent;
	
	JButton localGameButton = new JButton("Local Game");
	JButton hostGameButton = new JButton("Host Game");
	JButton joinGameButton = new JButton("Join Game");
	JButton rulesButton = new JButton("Rules");
	JButton quitButton = new JButton("Quit");
	
	JPanel inner = new JPanel();
	JPanel central = new JPanel();
	
	public static final String LOCAL_GAME = "LOCAL_GAME";
	public static final String HOST_GAME = "HOST_GAME";
	public static final String JOIN_GAME = "JOIN_GAME";
	public static final String RULES = "RULES";
	public static final String QUIT = "QUIT";
	 
	
	public MainMenu(JFrame parent)
	{
		super();
		this.parent = parent;
		
		this.setLayout(new GridLayout(1, 3, 0, 0));
		this.add(new JPanel());
		this.add(central);
		this.add(new JPanel());
		
		BorderLayout layout = new BorderLayout();
		central.setLayout(layout);
		central.add(new JPanel(), BorderLayout.NORTH);
		
		central.add(inner, BorderLayout.CENTER);
		central.add(new JPanel(), BorderLayout.SOUTH);
		
		 
		inner.setBorder(new EmptyBorder(50, 0, 50, 0));
		inner.setLayout(new GridLayout(0, 1, 0, 20));
		
		localGameButton.setName(LOCAL_GAME);
		localGameButton.addActionListener((ActionListener) parent);
		inner.add(localGameButton);
		
		hostGameButton.setName(HOST_GAME);
		hostGameButton.addActionListener((ActionListener) parent);
		inner.add(hostGameButton); 
		
		joinGameButton.setName(JOIN_GAME);
		joinGameButton.addActionListener((ActionListener) parent);
		inner.add(joinGameButton); 
		
		rulesButton.setName(RULES);
		rulesButton.addActionListener((ActionListener) parent);
		inner.add(rulesButton);
		
		quitButton.setName(QUIT);
		quitButton.addActionListener((ActionListener) parent);
		inner.add(quitButton);
		
	}
}
