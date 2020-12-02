package farkle.game;

public class Game 
{
	private Player[] players;
	private DiceHand hand;
	private int currentPlayer;
	
	public Game(String[] names)
	{
		players = new Player[names.length];
		for (int i = 0; i < names.length; i++)
			players[i] = new Player(names[i]);
		
		hand = new DiceHand();
		currentPlayer = 0;
	}
	
	public GameState getGameState()
	{
		GameState result = new GameState();
		result.players = new Player[this.players.length];
		for (int i = 0; i < this.players.length; i++)
			result.players[i] = this.players[i].copy();
		
		result.hand = this.hand.copy();
		result.currentPlayer = this.currentPlayer;
		
		result.rollEnabled = isRollEnabled();
		result.bankEnabled = isBankEnabled();
		return result;
	}
	
	public boolean isRollEnabled()
	{
		return hand.hasSelectedOnlyCombinations();
	}
	
	public boolean isBankEnabled()
	{
		return players[currentPlayer].getScore() >= 300;
	}
	
	
}
