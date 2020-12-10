package farkle.main.userAction;


public class JoinGameUserAction extends UserAction
{
	public String playerName;
	public String IP;
	public int port;
	
	public JoinGameUserAction(String playerName, String IP, int port)
	{
		super(Type.JOIN_GAME);
		this.playerName = playerName;
		this.IP = IP;
		this.port = port;
	}
}
