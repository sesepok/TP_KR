package farkle.game;

public class Rules 
{
	private static int getLongestSequence(int[] values, int targetValue)
	{
		int foundSoFar = 0;
		for (int value: values)
		{
			if (value == targetValue) foundSoFar++;
		}
		return foundSoFar;
	}
	
	public static boolean hasAnyCombination(int[] values)
	{
		if (getLongestSequence(values, 1) > 0) return true;
		if (getLongestSequence(values, 5) > 0) return true;
		if (getLongestSequence(values, 2) > 2) return true;
		if (getLongestSequence(values, 3) > 2) return true;
		if (getLongestSequence(values, 4) > 2) return true;
		if (getLongestSequence(values, 6) > 2) return true;
		
		return false;
	}
}
