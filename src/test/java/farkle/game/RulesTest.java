package farkle.game;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

class RulesTest {
	
	private static ArrayList<Integer> arrayToList(int[] arr)
	{
		ArrayList<Integer> intList = new ArrayList<Integer>(arr.length);
		for (int i : arr)
		{
		    intList.add(i);
		}
		return intList;
	}
	
	private static int[] listToArray(ArrayList<Integer> list)
	{
		int[] arr = new int[list.size()];
		for (int i = 0; i < list.size(); i++)
		{
			arr[i] = list.get(i);
		}
		return arr;
	}
	
	private static int[] shuffle(int[] arr)
	{
		ArrayList<Integer> list = arrayToList(arr);
		Collections.shuffle(list);
		return listToArray(list);
	}
	
	@Test
	//Test my shuffle and converters
	void testUtils()
	{
		DiceHand hand = new DiceHand();
		for (int i = 0; i < 100; i++)
		{
			hand.roll();
			int[] values = hand.getValues();
			assertArrayEquals(values, listToArray(arrayToList(values)));
			
			for (int j = 0; j < 20; j++)
			{
				int[] shuffled = shuffle(values);
				Arrays.sort(values);
				Arrays.sort(shuffled);
				assertArrayEquals(values, shuffled);
			}
		}
	}

	@Test
	void testOrderIndependence()
	{
		DiceHand hand = new DiceHand();
		
		int[] values;
		for (int i = 0; i < 100; i++)
		{
			hand.roll();
			values = hand.getValues();
				
			boolean result = Rules.hasAnyCombination(values);
			for (int j = 0; j < 10; j++)
			{
				assertEquals(result, Rules.hasAnyCombination(shuffle(values)));
			}
		}
	}
	
	@Test
	void testValues()
	{
		int[] values;
		
		//ones
		values = new int[] {6,2,6,1,1,1};
		assertEquals(true, Rules.hasAnyCombination(values));
		values = new int[] {3,6,5,4,6,1};
		assertEquals(true, Rules.hasAnyCombination(values));
		values = new int[] {1,2,6,1,4,4};
		assertEquals(true, Rules.hasAnyCombination(values));
		values = new int[] {4,2,6,1};
		assertEquals(true, Rules.hasAnyCombination(values));
		values = new int[] {2,3,2,4,1};
		assertEquals(true, Rules.hasAnyCombination(values));
		values = new int[] {1};
		assertEquals(true, Rules.hasAnyCombination(values));
		
		//fives
		values = new int[] {5,4,2,3,6,6};
		assertEquals(true, Rules.hasAnyCombination(values));
		values = new int[] {3,2,6,6,4,5};
		assertEquals(true, Rules.hasAnyCombination(values));
		values = new int[] {2,5,1};
		assertEquals(true, Rules.hasAnyCombination(values));
		values = new int[] {5};
		assertEquals(true, Rules.hasAnyCombination(values));
		
		//twos
		values = new int[] {2,4,5,2,2,4};
		assertEquals(true, Rules.hasAnyCombination(values));
		values = new int[] {2,3,2,6,2,2};
		assertEquals(true, Rules.hasAnyCombination(values));
		values = new int[] {2,2,2,6,2,2};
		assertEquals(true, Rules.hasAnyCombination(values));
		values = new int[] {2,2,2,2,2,2};
		assertEquals(true, Rules.hasAnyCombination(values));
		values = new int[] {2,2,2};
		assertEquals(true, Rules.hasAnyCombination(values));
		values = new int[] {2,3,2,3,4};
		assertEquals(false, Rules.hasAnyCombination(values));
		values = new int[] {2,2};
		assertEquals(false, Rules.hasAnyCombination(values));
		
		//threes
		values = new int[] {4,3,3,4,3};
		assertEquals(true, Rules.hasAnyCombination(values));
		values = new int[] {4,3,3,4,3,3};
		assertEquals(true, Rules.hasAnyCombination(values));
		values = new int[] {3,3,3};
		assertEquals(true, Rules.hasAnyCombination(values));
		values = new int[] {3,3};
		assertEquals(false, Rules.hasAnyCombination(values));
		values = new int[] {4,3,6,4,3};
		assertEquals(false, Rules.hasAnyCombination(values));
		
		//fours
		values = new int[] {4,2,4,6,4,2};
		assertEquals(true, Rules.hasAnyCombination(values));
		values = new int[] {4,4,4,2,4,4};
		assertEquals(true, Rules.hasAnyCombination(values));
		values = new int[] {4,4,4};
		assertEquals(true, Rules.hasAnyCombination(values));
		values = new int[] {4,4};
		assertEquals(false, Rules.hasAnyCombination(values));
		values = new int[] {4,2,4,6,6,2};
		assertEquals(false, Rules.hasAnyCombination(values));
		
		//sixes
		values = new int[] {6,4,5,6,6,4};
		assertEquals(true, Rules.hasAnyCombination(values));
		values = new int[] {6,3,6,6,6,6};
		assertEquals(true, Rules.hasAnyCombination(values));
		values = new int[] {6,6,6,6,6,6};
		assertEquals(true, Rules.hasAnyCombination(values));
		values = new int[] {6,6,6,6,6,6};
		assertEquals(true, Rules.hasAnyCombination(values));
		values = new int[] {6,6,6};
		assertEquals(true, Rules.hasAnyCombination(values));
		values = new int[] {6,3,6,3,4};
		assertEquals(false, Rules.hasAnyCombination(values));
		values = new int[] {6,6};
		assertEquals(false, Rules.hasAnyCombination(values));
		
		//misc
		values = new int[] {2,2,3,3,4,4};
		assertEquals(false, Rules.hasAnyCombination(values));
		values = new int[] {2,3,4,6};
		assertEquals(false, Rules.hasAnyCombination(values));
		values = new int[] {1,2,3,4,5,6};
		assertEquals(true, Rules.hasAnyCombination(values));
		values = new int[] {};
		assertEquals(false, Rules.hasAnyCombination(values));
		
		
		
	}

}
