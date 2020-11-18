package farkle.game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DiceHandTest {

	@Test
	void testRangeAndEquality() 
	{
		DiceHand hand = new DiceHand();
		boolean[] happened = new boolean[6];
		for (int i = 0; i < 100; i++)
		{
			hand.roll();
			int[] values = hand.getValues();
			if (values.length != 6) fail("getValues must return 6 values, got: " + values.length);
			for (int value : values)
			{
				if (value < 1 || value > 6) fail("values must be between 1 and 6, got: " + value);
				happened[value-1] = true;
			}
		}
		
		for (int i = 0; i < 6; i++)
		{
			if (!happened[i]) fail("all values must be possible, failed to get: " + i);
		}
	}
	
	@Test
	void testSelectAndLock()
	{
		DiceHand hand = new DiceHand();
		hand.select(0);
		hand.select(5);
		hand.select(3);
		hand.select(0);
		hand.deselect(0);
		hand.select(0);
		hand.select(5);
		hand.deselect(5);
		hand.select(4);
		
		boolean[] expected = {true, false, false, true, true, false};
		
		assertArrayEquals(expected,  hand.areSelected());
		
		hand.lock(0);
		hand.lock(5);
		hand.lock(3);
		hand.lock(0);
		hand.unlock(0);
		hand.lock(0);
		hand.lock(5);
		hand.unlock(5);
		hand.lock(4);
		
		assertArrayEquals(expected,  hand.areLocked());
		
		hand.reset();
		
		hand.select(0);
		hand.select(5);
		hand.deselect(0);
		hand.select(0);
		hand.deselect(5);
		hand.select(4);
		hand.lock(0);
		hand.unlock(0);
		hand.unlock(5);
		hand.lock(4);
		hand.lock(2);
		hand.lock(2);
		
		boolean[] expectedSelected = {true, false, false, false, true, false};
		boolean[] expectedLocked = {false, false, true, false, true, false};
		assertArrayEquals(expectedSelected, hand.areSelected());
		assertArrayEquals(expectedLocked, hand.areLocked());
	}

}
