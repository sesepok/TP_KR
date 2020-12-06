package farkle.main.userAction;

public class CreateLocalGameUserAction extends UserAction
{
	public String[] names;
	
	
	public CreateLocalGameUserAction(String[] names)
	{
		super(Type.LOCAL_GAME);
		
		this.names = names;
	}
}
