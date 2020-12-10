package farkle.main.userAction;

public class HostGameUserAction extends UserAction 
{
	public String playerName;
	public int port;
	
	public HostGameUserAction(String playerName, int port)
	{
		super(Type.HOST_GAME);
		this.playerName = playerName;
		this.port = port;
	}
}
