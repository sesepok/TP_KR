package farkle.game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DieTest {

	@Test
	void testRange() 
	{
		Die die = new Die();
		for (int i = 0; i < 100; i++)
		{
			die.roll();
			if (die.getValue() < 1 || die.getValue() > 6)
				fail("Die value should be between 1 and 6. Got: " + die.getValue());
		}
	}
	
	@Test
	void testEquality()
	{
		Die die = new Die();
		boolean[] happened = new boolean[6];
		
		for (int i = 0; i < 100; i++)
		{
			die.roll();
			happened[die.getValue() - 1] = true;
		}
		
		for (boolean value : happened)
			if (!value) fail("Every value should be possible");
	}

}
