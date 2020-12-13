package farkle.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;

import farkle.game.Game;
import farkle.game.GameState;
import farkle.gui.GUI;
import farkle.main.userAction.BankUserAction;
import farkle.main.userAction.ConfirmLobbyUserAction;
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
	
	private boolean myTurn = false; // for clients in network game
	
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
	
	// FOR LOCAL USER ONLY
	public void dispatchUserAction(UserAction action)
	{
		switch(action.type)
		{
		case LOCAL_GAME:
			clearState();
			game = new Game(((CreateLocalGameUserAction)action).names);
			gui.createGameScreen(((CreateLocalGameUserAction)action).names);
			showGameState(game.getGameState());
			changeState(AppState.LOCAL_GAME);
			break;
		
		case HOST_GAME:
			dispatchHostGameUserAction((HostGameUserAction)action);
			break;
			
		case CONFIRM_LOBBY:
			game = new Game(lobbyPlayerList.toArray(new String[0]));
			try {
				server.broadcastStartGame();
				server.startGame();
				gui.createGameScreen(lobbyPlayerList.toArray(new String[0]));
				showGameState(game.getGameState());
				server.broadcastGameState(game.getGameState());
				changeState(AppState.HOSTED_GAME);
				lobby = false;
			} catch (IOException e) {
				e.printStackTrace();
			}
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
			dispatchBankUserAction((BankUserAction)action);
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
		if (currentAppState == AppState.LOCAL_GAME)
		{
			clickDie(action.n);
			showGameState(game.getGameState());
		}
		else if (currentAppState == AppState.HOSTED_GAME)
		{
			if (game.getGameState().currentPlayer == 0)
			{
				clickDie(action.n);
				showGameState(game.getGameState());
				broadcastGameState(game.getGameState());
			}
		}
		else if (currentAppState == AppState.JOINED_GAME)
		{
			if (myTurn)
			{
				client.sendUserActionToServer(new DieUserAction(action.n));
			}
		}
	}
	
	private void clickDie(int n)
	{
		if (game.getGameState().hand.getValue(n) == 0 
				|| game.getGameState().hand.isLocked(n)) return;
		
		if (!game.getGameState().hand.isSelected(n))
			game.select(n);
		else
			game.deselect(n);
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
					showGameState(game.getGameState());
					if (currentAppState == AppState.HOSTED_GAME)
						broadcastGameState(game.getGameState());
				}
				catch(InterruptedException e)
				{
					game.nextPlayer();
					showGameState(game.getGameState());
					if (currentAppState == AppState.HOSTED_GAME)
						broadcastGameState(game.getGameState());
					this.interrupt();
				}
			}
		}
		
		Thread tmp = new WaitingThread(sleepTime);
		tmp.start();
	}
	
	private void dispatchRollUserAction(RollUserAction action)
	{
		if (currentAppState == AppState.JOINED_GAME)
		{
			client.sendUserActionToServer(new RollUserAction());
		}
		else
		{
			game.roll();
			System.out.println(game.getGameState());
			showGameState(game.getGameState());
			if (currentAppState == AppState.HOSTED_GAME)
				broadcastGameState(game.getGameState());
			if (game.getGameState().busted)
			{
				continueAfterSleeping(2000);
			}
		}
	}
	
	private void dispatchBankUserAction(BankUserAction action)
	{
		if (currentAppState == AppState.JOINED_GAME)
		{
			client.sendUserActionToServer(new BankUserAction());
		}
		else
		{
			game.bank();
			showGameState(game.getGameState());
			if (currentAppState == AppState.HOSTED_GAME)
				broadcastGameState(game.getGameState());
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
	
	private void broadcastGameState(GameState state)
	{
		try {
			server.broadcastGameState(state);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void showGameState(GameState state)
	{
		if (currentAppState == AppState.HOSTED_GAME && state.currentPlayer != 0)
		{
			state.rollEnabled = false;
			state.bankEnabled = false;
		}
		gui.setGameState(state);
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
			else
			{
				game.removePlayer(networkAction.sourcePlayerIndex);
				showGameState(game.getGameState());
				broadcastGameState(game.getGameState());
			}
			break;
		
		case ROLL:
			if (game.getGameState().rollEnabled
					&& game.getGameState().currentPlayer == networkAction.sourcePlayerIndex)
			{
				game.roll();
				showGameState(game.getGameState());
				broadcastGameState(game.getGameState());
				if (game.getGameState().busted)
				{
					continueAfterSleeping(2000);
				}
			}
			break;
			
		case BANK:
			if (game.getGameState().bankEnabled
					&& game.getGameState().currentPlayer == networkAction.sourcePlayerIndex)
			{
				game.bank();
				showGameState(game.getGameState());
				broadcastGameState(game.getGameState());
			}
			break;
			
		case DIE:
			if (game.getGameState().currentPlayer == networkAction.sourcePlayerIndex)
			{
				clickDie(((DieUserAction)networkAction.action).n);
				showGameState(game.getGameState());
				broadcastGameState(game.getGameState());
			}
		}
	}
	
	// =================== Following are for clients =====================
	
	public void setLobbyPlayerNames(String[] names)
	{
		gui.setLobbyPlayerNames(names);
	}
	
	public void serverStartedGame(String[] players)
	{
		gui.createGameScreen(players);
		changeState(AppState.JOINED_GAME);
	}
	
	public void updateGameState(GameState state)
	{
		myTurn = state.myTurn;
		if (!myTurn)
		{
			state.rollEnabled = false;
			state.bankEnabled = false;
		}
		showGameState(state);
	}
	
}
