package farkle.game;

public class GameState 
{
	public Player[] players;
	public DiceHand hand;
	public int currentPlayer;
	public boolean rollEnabled;
	public boolean bankEnabled;
	
	public GameState(String[] names)
	{
		players = new Player[names.length];
		for (int i = 0; i < players.length; i++)
			players[i] = new Player(names[i]);
		hand = new DiceHand();
		currentPlayer = 0;	
	}
	
	public GameState()
	{
	}
	
}
