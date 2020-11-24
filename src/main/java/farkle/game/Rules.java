package farkle.game;

public class Rules 
{
	public static final int ONE_SCORE = 100;
	public static final int FIVE_SCORE = 50;
	public static final int TRIPLE_ONE_SCORE = 1000;
	public static final int TRIPLE_TWO_SCORE = 200;
	public static final int TRIPLE_THREE_SCORE = 300;
	public static final int TRIPLE_FOUR_SCORE = 400;
	public static final int TRIPLE_FIVE_SCORE = 500;
	public static final int TRIPLE_SIX_SCORE = 600;
	
	/*private static int getLongestSequence(int[] values, int targetValue)
	{
		int foundSoFar = 0;
		for (int value: values)
		{
			if (value == targetValue) foundSoFar++;
		}
		return foundSoFar;
	}*/
	
	private static int[] getEntries(int[] values)
	{
		int[] result = new int[7];
		for (int value : values)
		{
			result[value]++;
		}
		return result;
	}
	
	public static boolean hasAnyCombination(int[] values)
	{
		/*if (getLongestSequence(values, 1) > 0) return true;
		if (getLongestSequence(values, 5) > 0) return true;
		if (getLongestSequence(values, 2) > 2) return true;
		if (getLongestSequence(values, 3) > 2) return true;
		if (getLongestSequence(values, 4) > 2) return true;
		if (getLongestSequence(values, 6) > 2) return true;*/
		
		int[] entries = getEntries(values);
		if (entries[1] > 0) return true;
		if (entries[5] > 0) return true;
		if (entries[2] >= 3) return true;
		if (entries[3] >= 3) return true;
		if (entries[4] >= 3) return true;
		if (entries[6] >= 3) return true;
		
		return false;
	}
	
	public static int getScore(int[] values)
	{
		int result = 0;
		
		int[] entries = getEntries(values);
		
		if (entries[1] >= 3 )
			result += TRIPLE_ONE_SCORE * (entries[1] - 2);
		else if (entries[1] > 0)
			result += ONE_SCORE * entries[1];
		
		if (entries[5] >= 3)
			result += TRIPLE_FIVE_SCORE * (entries[5] - 2);
		else if (entries[5] > 0)
			result += FIVE_SCORE * entries[5];
		
		if (entries[2] >= 3) result += TRIPLE_TWO_SCORE * (entries[2] - 2);
		if (entries[3] >= 3) result += TRIPLE_THREE_SCORE * (entries[3] - 2);
		if (entries[4] >= 3) result += TRIPLE_FOUR_SCORE * (entries[4] - 2);
		if (entries[6] >= 3) result += TRIPLE_SIX_SCORE * (entries[6] - 2);
		
		return result;
	}
	
	public static boolean hasOnlyCombinations(int[] values)
	{
		if (values.length == 0) return false;
		
		int[] entries = getEntries(values);
		
		if (entries[2] > 0 && entries[2] < 3) return false;
		if (entries[3] > 0 && entries[3] < 3) return false;
		if (entries[4] > 0 && entries[4] < 3) return false;
		if (entries[6] > 0 && entries[6] < 3) return false;
		return true;
	}
}
