package farkle.game;

public class GameState 
{
	public Player[] players;
	public DiceHand hand;
	public int currentPlayer;
	public int selectedScore;
	public boolean rollEnabled;
	public boolean bankEnabled;
	public boolean busted;
	public boolean victory;
	
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
	
	public String toString()
	{
		String result = "GameState(\nplayers:\n[\n";
		for (int i = 0; i < players.length; i++)
		{
			result += "\t{name: " + players[i].getName() + ", bank: " + players[i].getBank() +
					", score: " + players[i].getScore() + "}\n";
		}
		result += "],\ncurrentPlayer: " + currentPlayer + ",\nselectedValue: " + selectedScore +
				",\nrollEnabled: " + rollEnabled + ",\nbankEnabled: " +
				bankEnabled + ",\nbusted: " + busted + ",\nvictory: " + victory + ")";
		return result;
	}
	
}
