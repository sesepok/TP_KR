package farkle.main.userAction;


public class DieUserAction extends UserAction
{
	public int n;
	
	public DieUserAction(int n)
	{
		super(Type.DIE);
		this.n = n;
	}
}
