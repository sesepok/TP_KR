package farkle.game;

public class Player 
{
	private String name;
	private int bank = 0;
	private int score = 0;
	
	public Player(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getBank()
	{
		return bank;
	}
	
	public void setScore(int score)
	{
		this.score = score;
	}
	
	public void addScore(int addedScore)
	{
		this.score += addedScore;
	}
	
	public int getScore()
	{
		return score;
	}
	
	public void resetScore()
	{
		score = 0;
	}
	
	public void bankScore()
	{
		bank += getScore();
		resetScore();
	}
	
	public Player copy()
	{
		Player copy = new Player(this.name);
		copy.bank = this.bank;
		copy.score = this.score;
		return copy;
	}
	
}
