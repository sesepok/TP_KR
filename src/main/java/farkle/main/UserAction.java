package farkle.main;

public class UserAction 
{
	public enum Type
	{
		LOCAL_GAME,
		HOST_GAME,
		JOIN_GAME,
		LEAVE_GAME,
		ROLL,
		BANK,
		DIE,
		QUIT
	}
	
	public Type type;
	
	public UserAction(Type type)
	{
		this.type = type;
	}
	

}
