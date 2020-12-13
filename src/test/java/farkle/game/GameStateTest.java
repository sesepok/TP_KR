package farkle.game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class GameStateTest {

	@Test
	void testToString()
	{
		GameState state = new GameState();
		Player[] players = {new Player("asdf", 358, 153),
							new Player("asd", 9357, 10),
							new Player("fds385", 1384, 384),
							new Player("asdf83e", 4834, 4384)};
		Die[] dice = {	new Die(1, true, false),
						new Die(2, false, true),
						new Die(4, true, true),
						new Die(3, false, false),
						new Die(2, false, true),
						new Die(6, true, false)
					};
		state.players = players;
		state.hand = new DiceHand(dice);
		
		state.currentPlayer = 3;
		state.selectedScore = 168;
		
		state.rollEnabled = true;
		state.bankEnabled = false;
		state.busted = true;
		state.victory = false;
		
		assertEquals(state.toString(), GameState.fromString(state.toString()).toString());
	}

}
