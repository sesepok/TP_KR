package farkle.main;

import farkle.gui.GUI;

public class Main 
{
	GUI gui;
	
	public static void main(String[] args)
	{
		Main main = new Main();
		GUI gui = new GUI(main);
		main.assignGUI(gui);;
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
			break;
		
		case HOST_GAME:
			break;
			
		case JOIN_GAME:
			break;
			
		case LEAVE_GAME:
			gui.mainMenu();
			break;
			
		case ROLL:
			break;
			
		case BANK:
			break;
			
		case QUIT:
			System.exit(0);
			break;
		
		default:
			break;
			
		}
	}
}
