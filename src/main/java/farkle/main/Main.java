package farkle.main;

import farkle.game.Game;
import farkle.game.GameState;
import farkle.gui.GUI;
import farkle.main.userAction.CreateLocalGameUserAction;
import farkle.main.userAction.DieUserAction;
import farkle.main.userAction.HostGameUserAction;
import farkle.main.userAction.JoinGameUserAction;
import farkle.main.userAction.RollUserAction;
import farkle.main.userAction.UserAction;

public class Main 
{
	private GUI gui;
	private Game game;
	
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
			game = new Game(((CreateLocalGameUserAction)action).names);
			gui.createLocalGameScreen(((CreateLocalGameUserAction)action).names);
			gui.setGameState(game.getGameState());
			currentAppState = AppState.LOCAL_GAME;
			break;
		
		case HOST_GAME:
			dispatchHostGameUserAction((HostGameUserAction)action);
			break;
			
		case JOIN_GAME:
			dispatchJoinGameUserAction((JoinGameUserAction)action);
			break;
			
		case LEAVE_GAME:
			gui.mainMenu();
			
			currentAppState = AppState.MAIN_MENU;
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
		gui.createLobbyScreen(action.playerName, true);
		currentAppState = AppState.HOSTED_LOBBY;
	}
	
	private void dispatchJoinGameUserAction(JoinGameUserAction action)
	{
		gui.createLobbyScreen(action.playerName, false);
		currentAppState = AppState.JOINED_LOBBY;
	}
}
