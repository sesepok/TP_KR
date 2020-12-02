package farkle.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import farkle.game.GameState;
import farkle.main.Main;
import farkle.main.UserAction;

public class GUI extends JFrame implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//private GUIState state = GUIState.MAIN_MENU;
	private JPanel current;
	
	private MainMenu mainMenu = new MainMenu(this);
	private GameScreen gameScreen = new GameScreen(this);
	
	private Main main;
	
	public GUI(Main main)
	{
		super();
		this.main = main;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//getContentPane().add(mainMenu);
		setContentPane(mainMenu);
		current = mainMenu;
		setSize(1280, 720);
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		switch (((JButton)e.getSource()).getName())
		{
		case MainMenu.LOCAL_GAME:
			PlayerListDialog dialog = new PlayerListDialog(this);
			dialog.setLocationRelativeTo(this);
			dialog.setVisible(true);
			break;
		case MainMenu.QUIT:
			main.dispatchUserAction(new UserAction(UserAction.Type.QUIT));
			break;
			
		case GameScreen.LEAVE:
			main.dispatchUserAction(new UserAction(UserAction.Type.LEAVE_GAME));
			break;
		}
		
	}
	
	/*private void hideCurrentScreen()
	{
		remove(current);
	}*/
	
	public void mainMenu()
	{
		changeScreenTo(mainMenu);
	}
	
	private void changeScreenTo(JPanel newScreen)
	{
		setContentPane(newScreen);
		current = newScreen;
		revalidate();
	}
	
	//PROTOTYPE
	public void createLocalGame(String[] names)
	{
		for (String name : names)
		{
			System.out.println(name);
		}
		changeScreenTo(gameScreen);
		
		GameState state = new GameState(names);
		gameScreen.setGameState(state);
		
	}
	
	
}
