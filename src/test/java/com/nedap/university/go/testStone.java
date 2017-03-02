package com.nedap.university.go;

import com.nedap.university.go.game.Stone;
import com.nedap.university.go.game.StoneState;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class testStone {

	private Stone stone1;
	private Stone stone2;
	
	@Before
	public void setUp() {
		stone1 = new Stone();
		stone1.addNeighbour(new Stone());
		stone1.addNeighbour(new Stone());
		stone1.addNeighbour(new Stone());
		stone1.addNeighbour(new Stone());
		stone2 = new Stone();
		stone2.addNeighbour(new Stone());
		stone2.addNeighbour(new Stone());
		stone2.addNeighbour(new Stone());
		stone2.addNeighbour(new Stone());
	}
	
	@Test
	public void testSetUp() {
		assertEquals(StoneState.EMPTY, stone1.getState());
		assertTrue(stone1.isEmpty());
		assertTrue(stone1.getChain().getChain().isEmpty());
		assertEquals(4, stone1.getNeighbour().size());
	}
	
	@Test
	public void testAddThisToChain() {
		stone1.addThisToChain();
		assertEquals(stone1, stone1.getChain().getChain().get(0));
	}
	
	@Test
	public void testSetColor() {
		stone1.setColor(true);
		assertEquals(StoneState.WHITE, stone1.getState());
		assertFalse(stone1.isEmpty());
		stone1.setColor(false);
		assertEquals(StoneState.BLACK, stone1.getState());
		assertFalse(stone1.isEmpty());
	}
}
