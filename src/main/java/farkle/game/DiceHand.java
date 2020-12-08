package farkle.game;

public class DiceHand 
{
	private Die[] dice = new Die[6];
	
	
	public DiceHand()
	{
		for (int i = 0; i < 6; i++)
		{
			dice[i] = new Die();
		}
	}
	
	public void reset()
	{
		for (Die die : dice)
		{
			die.reset();
		}
	}
	
	public void roll()
	{
		for (Die die: dice)
			if (!die.isLocked()) die.roll();
	}
	
	public void roll(int[] diceNums)
	{
		for (int number : diceNums)
			dice[number].roll();
	}
	
	public Die getDie(int n)
	{
		return dice[n].copy();
	}
	
	public int getValue(int n)
	{
		return dice[n].getValue();
	}
	
	public int[] getValues()
	{
		int[] values = new int[6];
		for (int i = 0; i < 6; i++)
			values[i] = dice[i].getValue();
		return values;
	}
	
	public void select(int n)
	{
		dice[n].select();
	}
	
	public void deselect(int n)
	{
		dice[n].deselect();
	}
	
	public boolean isSelected(int n)
	{
		return dice[n].isSelected();
	}
	
	public boolean[] areSelected()
	{
		boolean[] selectedInfo = new boolean[6];
		for (int i = 0; i < 6; i++)
			selectedInfo[i] = dice[i].isSelected();
		return selectedInfo;
	}
	
	public void lock(int n)
	{
		dice[n].deselect();
		dice[n].lock();
	}
	
	public void unlock(int n)
	{
		dice[n].unlock();
	}
	
	public boolean isLocked(int n)
	{
		return dice[n].isLocked();
	}
	
	public boolean[] areLocked()
	{
		boolean[] lockedInfo = new boolean[6];
		for (int i = 0; i < 6; i++)
			lockedInfo[i] = dice[i].isLocked();
		return lockedInfo;
	}
	
	public boolean isSelectedCombination()
	{
		return Rules.hasAnyCombination(getValues());
	}
	
	public int getSelectedScore()
	{
		return Rules.getScore(getSelectedValues());
	}
	
	public int[] getSelectedValues()
	{	
		int size = 0;
		for (Die die: dice)
			if (die.isSelected()) size++;
		int[] result = new int[size];
		int index = 0;
		for (Die die: dice)
			if (die.isSelected()) result[index++] = die.getValue();
		return result;
	}
	
	public int[] getUnlockedValues()
	{
		int size = 0;
		for (Die die: dice)
			if (!die.isLocked()) size++;
		int[] result = new int[size];
		int index = 0;
		for (Die die: dice)
			if (!die.isLocked()) result[index++] = die.getValue();
		return result;
	}
	
	public boolean hasSelectedOnlyCombinations()
	{
		return Rules.hasOnlyCombinations(getSelectedValues());
	}
	
	public void lockSelected()
	{
		for (Die die: dice)
		{
			if (die.isSelected()) die.lock();
		}
	}
	
	public DiceHand copy()
	{
		DiceHand copy = new DiceHand();
		for (int i = 0; i < 6; i++)
			copy.dice[i] = this.dice[i].copy();
		return copy;
	}

}
