package farkle.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import farkle.game.GameState;
import farkle.main.Main;
import farkle.main.userAction.*;

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
	private LobbyScreen lobbyScreen;
	
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
			PlayerListDialog playerListDialog = new PlayerListDialog(this);
			playerListDialog.setLocationRelativeTo(this);
			playerListDialog.setVisible(true);
			break;
		case MainMenu.HOST_GAME:
			HostGameDialog hostGameDialog = new HostGameDialog(this);
			hostGameDialog.setLocationRelativeTo(this);
			hostGameDialog.setVisible(true);
			break;
		case MainMenu.JOIN_GAME:
			JoinGameDialog joinGameDialog = new JoinGameDialog(this);
			joinGameDialog.setLocationRelativeTo(this);
			joinGameDialog.setVisible(true);
			break;
		case MainMenu.QUIT:
			main.dispatchUserAction(new QuitUserAction());
			break;
			
		case GameScreen.LEAVE:
			main.dispatchUserAction(new LeaveUserAction());
			break;
			
		case GameScreen.ROLL:
			main.dispatchUserAction(new RollUserAction());
			break;
			
		case GameScreen.BANK:
			main.dispatchUserAction(new BankUserAction());
			break;
		
		case LobbyScreen.LEAVE:
			
			main.dispatchUserAction(new LeaveUserAction());
			break;
			
		case LobbyScreen.START:
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
	
	//================ Interface for children =========================================
	
	public void inputPlayerNames(String[] names)
	{
		main.dispatchUserAction(new CreateLocalGameUserAction(names));
	}
	
	public void diePressed(int n)
	{
		main.dispatchUserAction(new DieUserAction(n));
	}
	
	public void confirmHostGame(String playerName, int port)
	{
		main.dispatchUserAction(new HostGameUserAction(playerName, port));
	}
	
	public void confirmJoinGame(String playerName, String IP, int port)
	{
		main.dispatchUserAction(new JoinGameUserAction(playerName, IP, port));
	}
	
	//=================== Interface for main ==================================
	
	//PROTOTYPE
	public void createLocalGameScreen(String[] names)
	{
		
		changeScreenTo(gameScreen);
		
		GameState state = new GameState(names);
		gameScreen.setGameState(state);
		
	}
	
	public void setGameState(GameState state)
	{
		gameScreen.setGameState(state);
	}
	
	public void createLobbyScreen(String name, boolean host)
	{
		lobbyScreen = new LobbyScreen(this, host);
		changeScreenTo(lobbyScreen);
		lobbyScreen.addPlayerName(name, 0);
	}
	
	
}
