package farkle.game;

public class Game 
{
	private Player[] players;
	private DiceHand hand;
	private int currentPlayer;
	
	private boolean blankHand = true;
	
	public Game(String[] names)
	{
		players = new Player[names.length];
		for (int i = 0; i < names.length; i++)
			players[i] = new Player(names[i]);
		
		hand = new DiceHand();
		currentPlayer = 0;
		System.out.println(isRollEnabled());
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
		System.out.println("getGameState: " + isRollEnabled());
		result.bankEnabled = isBankEnabled();
		return result;
	}
	
	public void resetHand()
	{
		hand.reset();
		blankHand = true;
	}
	
	public void roll()
	{
		assert isRollEnabled();
		
		
		
		hand.roll();
		blankHand = false;
	}
	
	public void select(int n)
	{
		assert n >= 0 && n < 6;
		
		hand.select(n);
	}
	
	public void deselect(int n)
	{
		assert n >= 0 && n < 6;
		
		hand.deselect(n);
	}
	
	
	public boolean isRollEnabled()
	{
		return blankHand || hand.hasSelectedOnlyCombinations();
	}
	
	public boolean isBankEnabled()
	{
		return players[currentPlayer].getScore() >= 300;
	}
	
	
}
