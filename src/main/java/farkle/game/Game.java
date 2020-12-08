package farkle.game;

public class Game 
{
	private Player[] players;
	private DiceHand hand;
	private int currentPlayer;
	private boolean busted = false;
	private boolean victory = false;
	private boolean blankHand = true;
	
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
		result.selectedScore = hand.getSelectedScore();
		result.busted = busted;
		result.victory = victory;
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
		
		players[currentPlayer].addScore(hand.getSelectedScore());
		
		for (int i = 0; i < 6; i++)
		{
			
			if (hand.isSelected(i))
				hand.lock(i);
		}
		
		boolean allLocked = true;
		for (int i = 0; i < 6; i++)
		{
			if (!hand.isLocked(i)) allLocked = false;
		}
		
		if (allLocked) hand.reset();
		
		hand.roll();
		blankHand = false;
		
		if (!Rules.hasAnyCombination(hand.getUnlockedValues()))
			busted = true;
	}
	
	
	public void bank()
	{
		assert isBankEnabled();
		
		players[currentPlayer].addScore(hand.getSelectedScore());
		players[currentPlayer].bankScore();
		if (players[currentPlayer].getBank() >= 10000)
			victory = true;
		else
			nextPlayer();
	}
	
	public void nextPlayer()
	{
		players[currentPlayer].setScore(0);
		if (currentPlayer < players.length - 1)
			currentPlayer++;
		else
			currentPlayer = 0;
		hand.reset();
		blankHand = true;
		busted = false;
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
		return !victory && (blankHand || hand.hasSelectedOnlyCombinations());
	}
	
	public boolean isBankEnabled()
	{
		return !victory && !busted && players[currentPlayer].getScore() + hand.getSelectedScore() >= 300;
	}
	
	
}
