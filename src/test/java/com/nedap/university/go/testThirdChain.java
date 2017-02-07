package com.nedap.university.go;

import com.nedap.university.go.game.Chain;
import com.nedap.university.go.game.Stone;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class testThirdChain {

	private Stone stone1;
	private Stone stone2;
	private Chain chain;
	
	@Before
	public void setUp() {
		stone1 = new Stone();
		stone1.addNeighbour(new Stone());
		stone1.addNeighbour(new Stone());
		stone1.addNeighbour(new Stone());
		stone1.addNeighbour(new Stone());
		stone1.addThisToChain();
		stone2 = new Stone();
		stone2.addNeighbour(new Stone());
		stone2.addNeighbour(new Stone());
		stone2.addNeighbour(new Stone());
		stone2.addNeighbour(new Stone());
		stone2.addThisToChain();
		chain = new Chain();
	}
	
	@Test
	public void testSetUp() {
		assertTrue(chain.getChain().isEmpty());
//		assertTrue(chain.getEmptyStones().isEmpty());
	}
	
	@Test
	public void testAddStone() {
		chain.addStone(stone1);
		assertEquals(stone1, chain.getChain().get(0));
		assertEquals(chain, stone1.getChain());
		assertEquals(4, chain.calculateLibertyChain());
		chain.addStone(stone2);
		assertEquals(stone2, chain.getChain().get(1));
		assertEquals(chain, stone2.getChain());
		assertEquals(chain, stone1.getChain());
		assertEquals(8, chain.calculateLibertyChain());
	}



	@Test
	public void testGetLibertiesIf() {
		Stone stone3 = new Stone();
		stone3.addNeighbour(new Stone());
		stone3.addNeighbour(new Stone());
		stone3.addNeighbour(new Stone());
		Stone stone4 = new Stone();
		stone4.setColor(true);
		stone3.addNeighbour(stone4);
		chain.addStone(stone3);
		assertEquals(3, chain.calculateLibertyChain());
		chain.addStone(stone2);
		assertEquals(7, chain.calculateLibertyChain());
	}
	
	@Test
	public void testJoin() {
		stone1.join(stone2);
		assertEquals(stone1.getChain(), stone2.getChain());
		System.out.println(stone1.getChain().getChain().size());
		assertEquals(8, stone1.getChain().calculateLibertyChain());
	}

}
