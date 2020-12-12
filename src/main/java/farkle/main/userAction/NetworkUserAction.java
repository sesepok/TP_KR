package farkle.main.userAction;

public class NetworkUserAction
{
	public UserAction action;
	public int sourcePlayerIndex;
	
	public NetworkUserAction(UserAction action, int sourcePlayerIndex)
	{
		this.action = action;
		this.sourcePlayerIndex = sourcePlayerIndex;
	}
}
