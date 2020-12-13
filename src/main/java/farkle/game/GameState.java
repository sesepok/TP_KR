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
	
	public boolean myTurn; // For network game, set outside of the class
	
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
	
	@Override
	public String toString()
	{
		StringBuilder result = new StringBuilder();
		
		result.append(players[0].toString());
		for (int i = 1; i < players.length; i++)
			result.append("," + players[i].toString());
		
		result.append(".");
		result.append(hand.toString());
		result.append(".");
		result.append(currentPlayer + " " + selectedScore);
		result.append(".");
		result.append(rollEnabled + " " + bankEnabled + " " + busted + " " + victory + " " + myTurn); 
		
		return result.toString();
	}
	
	
	public static GameState fromString(String str)
	{
		String[] parts = str.split("\\.");
		GameState result = new GameState();
		
		String[] playerStrings = parts[0].split(",");
		Player[] players = new Player[playerStrings.length];
		for (int i = 0; i < players.length; i++)
			players[i] = Player.fromString(playerStrings[i]);
		
		result.players = players;
		result.hand = DiceHand.fromString(parts[1]);
		
		String[] ints = parts[2].split(" ");
		result.currentPlayer = Integer.parseInt(ints[0]);
		result.selectedScore = Integer.parseInt(ints[1]);
		
		String[] bools = parts[3].split(" ");
		result.rollEnabled = Boolean.parseBoolean(bools[0]);
		result.bankEnabled = Boolean.parseBoolean(bools[1]);
		result.busted = Boolean.parseBoolean(bools[2]);
		result.victory = Boolean.parseBoolean(bools[3]);
		result.myTurn = Boolean.parseBoolean(bools[4]);
		
		return result;
	}
}
