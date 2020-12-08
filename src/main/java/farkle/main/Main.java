package farkle.main;

import farkle.game.Game;
import farkle.game.GameState;
import farkle.gui.GUI;
import farkle.main.userAction.CreateLocalGameUserAction;
import farkle.main.userAction.DieUserAction;
import farkle.main.userAction.RollUserAction;
import farkle.main.userAction.UserAction;

public class Main 
{
	private GUI gui;
	private Game game;
	
	public static void main(String[] args)
	{
		Main main = new Main();
		GUI gui = new GUI(main);
		main.assignGUI(gui);
		
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
			//System.out.println("CREATED");
			break;
		
		case HOST_GAME:
			break;
			
		case JOIN_GAME:
			break;
			
		case LEAVE_GAME:
			gui.mainMenu();
			break;
		case DIE:
			//System.out.println("DIE");
			dispatchDieUserAction((DieUserAction)action);
			break;	
			
		case ROLL:
			//System.out.println("ROLL");
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
}
