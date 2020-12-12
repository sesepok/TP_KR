package farkle.main;

import java.io.IOException;
import java.util.ArrayList;

import farkle.game.Game;
import farkle.game.GameState;
import farkle.gui.GUI;
import farkle.main.userAction.CreateLocalGameUserAction;
import farkle.main.userAction.DieUserAction;
import farkle.main.userAction.HostGameUserAction;
import farkle.main.userAction.JoinGameUserAction;
import farkle.main.userAction.NetworkUserAction;
import farkle.main.userAction.RollUserAction;
import farkle.main.userAction.UserAction;

public class Main 
{
	private GUI gui;
	private Game game;
	private Server server;
	private Client client;
	
	private ArrayList<String> lobbyPlayerList = new ArrayList<String>();
	private boolean lobby = true;
	
	private enum AppState
	{
		MAIN_MENU,
		LOCAL_GAME,
		HOSTED_LOBBY,
		HOSTED_GAME,
		JOINED_LOBBY,
		JOINED_GAME
	}
	
	private AppState currentAppState;
	
	public static void main(String[] args)
	{
		Main main = new Main();
		GUI gui = new GUI(main);
		main.assignGUI(gui);
	}
	
	public Main()
	{
		currentAppState = AppState.MAIN_MENU;
	}
	
	public void assignGUI(GUI gui)
	{
		this.gui = gui;
	}
	
	
	public void dispatchUserAction(UserAction action)
	{
		switch(action.type)
		{
		case LOCAL_GAME:
			clearState();
			game = new Game(((CreateLocalGameUserAction)action).names);
			gui.createLocalGameScreen(((CreateLocalGameUserAction)action).names);
			gui.setGameState(game.getGameState());
			changeState(AppState.LOCAL_GAME);
			break;
		
		case HOST_GAME:
			dispatchHostGameUserAction((HostGameUserAction)action);
			break;
			
		case JOIN_GAME:
			dispatchJoinGameUserAction((JoinGameUserAction)action);
			break;
			
		case LEAVE_GAME:
			clearState();
			gui.mainMenu();
			changeState(AppState.MAIN_MENU);
			
			break;
		case DIE:
			dispatchDieUserAction((DieUserAction)action);
			break;	
			
		case ROLL:
			dispatchRollUserAction((RollUserAction)action);
			break;
			
		case BANK:
			game.bank();
			System.out.println(game.getGameState());
			gui.setGameState(game.getGameState());
			break;
			
		case QUIT:
			System.exit(0);
			break;
		
		default:
			break;
			
		}
	}
	
	private void dispatchDieUserAction(DieUserAction action)
	{
		if (game.getGameState().hand.getValue(action.n) == 0 
				|| game.getGameState().hand.isLocked(action.n)) return;
		
		if (!game.getGameState().hand.isSelected(action.n))
			game.select(action.n);
		else
			game.deselect(action.n);
		
		gui.setGameState(game.getGameState());
	}
	
	private void continueAfterSleeping(int sleepTime)
	{
		class WaitingThread extends Thread
		{
			private int sleepTime;
			public WaitingThread(int sleepTime)
			{
				this.sleepTime = sleepTime;
			}
			
			public void run()
			{
				try
				{
					Thread.sleep(sleepTime);
					game.nextPlayer();
					gui.setGameState(game.getGameState());
				}
				catch(InterruptedException e)
				{
					game.nextPlayer();
					gui.setGameState(game.getGameState());
					this.interrupt();
				}
			}
		}
		
		Thread tmp = new WaitingThread(sleepTime);
		tmp.start();
	}
	
	private void dispatchRollUserAction(RollUserAction action)
	{
		game.roll();
		System.out.println(game.getGameState());
		gui.setGameState(game.getGameState());
		if (game.getGameState().busted)
		{
			continueAfterSleeping(2000);
		}
	}
	
	private void dispatchHostGameUserAction(HostGameUserAction action)
	{
		try 
		{
			clearState();
			gui.createLobbyScreen(action.playerName, true);
			lobbyPlayerList.clear();
			lobbyPlayerList.add(action.playerName);
			server = new Server(this, action.port);
			server.start();
			changeState(AppState.HOSTED_LOBBY);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void dispatchJoinGameUserAction(JoinGameUserAction action)
	{
		try
		{
			clearState();
			gui.createLobbyScreen(action.playerName, false);
			client = new Client(this, action.playerName, action.IP, action.port);
			client.connect();
			changeState(AppState.JOINED_LOBBY);
		} catch (IOException e) {
			System.out.println("Could not connect");
			clearState();
			gui.mainMenu();
			changeState(AppState.MAIN_MENU);
		}
		
	}
	
	private void clearState()
	{
		if (server != null)
		{
			try {
				server.close();
				server = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (client != null)
		{
			try {
				client.close();
				client = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void changeState(AppState newState)
	{
		currentAppState = newState;
	}
	
	//FOR SERVER ONLY!!!
	public void dispatchNetworkUserAction(NetworkUserAction networkAction)
	{
		UserAction action = networkAction.action;
		switch(action.type)
		{
		case JOIN_GAME:
			assert lobby;
			lobbyPlayerList.add(((JoinGameUserAction)action).playerName);
			try {
				server.broadcastPlayerNames(lobbyPlayerList.toArray(new String[0]));
				gui.setLobbyPlayerNames(lobbyPlayerList.toArray(new String[0]));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case LEAVE_GAME:
			if (lobby)
			{
				lobbyPlayerList.remove(networkAction.sourcePlayerIndex);
				try {
					server.broadcastPlayerNames(lobbyPlayerList.toArray(new String[0]));
					gui.setLobbyPlayerNames(lobbyPlayerList.toArray(new String[0]));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			break;
		}
	}
	
	// =================== Following are for clients =====================
	
	public void setLobbyPlayerNames(String[] names)
	{
		gui.setLobbyPlayerNames(names);
	}
	
	
}
