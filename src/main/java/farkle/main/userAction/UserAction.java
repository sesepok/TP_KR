package farkle.main.userAction;

public class UserAction 
{
	public enum Type
	{
		LOCAL_GAME,
		HOST_GAME,
		CONFIRM_LOBBY,
		JOIN_GAME,
		LEAVE_GAME,
		ROLL,
		BANK,
		DIE,
		QUIT
	}
	
	public final Type type;
	
	protected UserAction(Type type)
	{
		this.type = type;
	}
}


