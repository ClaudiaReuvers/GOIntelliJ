package com.nedap.university.go;

//import game.thirdStone;

import com.nedap.university.go.game.Board;
import com.nedap.university.go.game.Stone;
import com.nedap.university.go.game.StoneState;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class testThirdBoard {

	private Board board;
	
	@Before
	public void setUp() {
		board = new Board(9);
	}
	
	@Test
	public void testSetUp() {
		assertEquals(9, board.getDimension());
		assertEquals(9 * 9, board.getAllFields().length);
		assertEquals(StoneState.EMPTY, board.getField(0, 0).getState());
		assertEquals(StoneState.EMPTY, board.getField(8, 8).getState());
		assertEquals(1, board.getField(0, 0).getChain().getChain().size());
		assertEquals(2, board.getField(0, 0).liberty());
		assertEquals(3, board.getField(5, 0).liberty());
		assertEquals(4, board.getField(1, 1).liberty());
		Board board19 = new Board(19);
		assertEquals(19, board19.getDimension());
		assertEquals(StoneState.EMPTY, board19.getField(0, 0).getState());
		assertEquals(StoneState.EMPTY, board19.getField(18, 18).getState());
	}
	
	@Test
	public void testAddStoneOne() {
		board.addStone(0, 0, true);
		assertFalse(board.getField(0, 0).isEmpty());
		assertEquals(StoneState.WHITE, board.getField(0, 0).getState());
		assertEquals(2, board.getField(0, 0).liberty());
		assertEquals(1, board.getField(0, 0).getChain().getChain().size());
		board.addStone(3, 3, false);
		assertEquals(StoneState.BLACK, board.getField(3, 3).getState());
		assertEquals(4, board.getField(3, 3).liberty());
	}
	
	@Test
	public void testAddStoneTwoOtherColor() {
		board.addStone(1, 1, true);
		board.addStone(1, 2, false);
		assertEquals(3, board.getField(1, 1).liberty());
		assertEquals(3, board.getField(1, 2).liberty());
		assertEquals(1, board.getField(1, 1).getChain().getChain().size());
		assertEquals(1, board.getField(1, 2).getChain().getChain().size());
		board.addStone(1, 0, false);
		assertEquals(2, board.getField(1, 0).liberty());
		assertEquals(2, board.getField(1, 1).liberty());
		board.addStone(0, 0, true);
		assertEquals(1, board.getField(0, 0).liberty());
		assertEquals(1, board.getField(1, 0).liberty());
	}
	
	@Test
	public void testAddStoneTwoSameColor() {
		board.addStone(1, 1, true);
		board.addStone(1, 2, true);
		assertEquals(board.getField(1, 1).getChain(), board.getField(1, 2).getChain());
		assertEquals(6, board.getField(1, 1).liberty());
//		assertEquals(6, board.getField(1, 2).liberty());
		board.addStone(2, 2, false);
		assertEquals(board.getField(1, 1).getChain(), board.getField(1, 2).getChain());
		assertEquals(5, board.getField(1, 1).liberty());
//		assertEquals(5, board.getField(1, 2).liberty());
		assertEquals(3, board.getField(2, 2).liberty());
	}
	
	@Test
	public void testAddStoneThreeSameColorStraight() {
		board.addStone(1, 1, true);
		board.addStone(1, 2, true);
		board.addStone(1, 3, true);
		assertEquals(board.getField(1, 1).getChain(), board.getField(1, 2).getChain());
		assertEquals(board.getField(1, 1).getChain(), board.getField(1, 3).getChain());
		assertEquals(8, board.getField(1, 1).liberty());
	}
	
	@Test
	public void testAddStoneThreeSameColorBend() {
		board.addStone(1, 1, true);
		board.addStone(1, 2, true);
		board.addStone(2, 2, true);
		assertEquals(board.getField(1, 1).getChain(), board.getField(1, 2).getChain());
		assertEquals(board.getField(1, 1).getChain(), board.getField(2, 2).getChain());
		assertEquals(7, board.getField(1, 1).liberty());
	}
	
	@Test
	public void testAddStoneBlock() {
		board.addStone(1, 1, true);
		board.addStone(1, 2, true);
		board.addStone(2, 2, true);
		board.addStone(2, 1, true);
		assertEquals(board.getField(1, 1).getChain(), board.getField(1, 2).getChain());
		assertEquals(board.getField(1, 1).getChain(), board.getField(2, 2).getChain());
		assertEquals(board.getField(1, 1).getChain(), board.getField(2, 1).getChain());
		assertEquals(8, board.getField(1, 1).liberty());
	}
	
	@Test
	public void testJoinTwoStraight() {
		board.addStone(1, 0, true);
		board.addStone(1, 1, true);
		board.addStone(1, 3, true);
		board.addStone(1, 4, true);
		board.addStone(1, 2, true);
		assertEquals(board.getField(1, 0).getChain(), board.getField(1, 1).getChain());
		assertEquals(board.getField(1, 0).getChain(), board.getField(1, 3).getChain());
		assertEquals(board.getField(1, 0).getChain(), board.getField(1, 4).getChain());
		assertEquals(board.getField(1, 0).getChain(), board.getField(1, 2).getChain());
		assertEquals(11, board.getField(1, 0).liberty());
	}
	
	@Test
	public void testJoinTwoBend() {
		board.addStone(1, 0, true);
		board.addStone(1, 1, true);
		board.addStone(2, 1, true);
		board.addStone(2, 3, true);
		board.addStone(2, 4, true);
		board.addStone(3, 4, true);
		board.addStone(2, 2, true);
		assertEquals(board.getField(1, 0).getChain(), board.getField(1, 1).getChain());
		assertEquals(board.getField(1, 0).getChain(), board.getField(2, 1).getChain());
		assertEquals(board.getField(1, 0).getChain(), board.getField(2, 3).getChain());
		assertEquals(board.getField(1, 0).getChain(), board.getField(2, 4).getChain());
		assertEquals(board.getField(1, 0).getChain(), board.getField(3, 4).getChain());
		assertEquals(board.getField(1, 0).getChain(), board.getField(2, 2).getChain());
		assertEquals(12, board.getField(1, 0).liberty());
	}
	
	@Test
	public void testJoinStraighBend() {
		board.addStone(1, 0, true);
		board.addStone(1, 1, true);
		board.addStone(2, 1, true);
		board.addStone(2, 3, true);
		board.addStone(3, 3, true);
		board.addStone(2, 2, true);
		assertEquals(board.getField(1, 0).getChain(), board.getField(1, 1).getChain());
		assertEquals(board.getField(1, 0).getChain(), board.getField(2, 1).getChain());
		assertEquals(board.getField(1, 0).getChain(), board.getField(2, 3).getChain());
		assertEquals(board.getField(1, 0).getChain(), board.getField(3, 3).getChain());
		assertEquals(board.getField(1, 0).getChain(), board.getField(2, 2).getChain());
		assertEquals(10, board.getField(1, 0).liberty());
	}
	
	@Test
	public void testClearBoard() {
		board.addStone(0, 0, true);
		board.addStone(8, 8, false);
		board.addStone(2, 2, false);
		board.clearBoard();
		assertEquals(StoneState.EMPTY, board.getField(0, 0).getState());
		assertEquals(StoneState.EMPTY, board.getField(8, 8).getState());
		assertEquals(StoneState.EMPTY, board.getField(2, 2).getState());
		assertEquals(1, board.getField(0, 0).getChain().getChain().size());
	}
	
	@Test
	public void testRemoveIfOneSurroundedByOwnColor() {
		board.addStone(1, 1, true);
		board.addStone(1, 2, true);
		board.addStone(1, 0, true);
		board.addStone(0, 1, true);
		board.addStone(2, 1, true);
		assertFalse(board.getField(1, 1).isEmpty());
		assertEquals(6, board.getField(1, 1).liberty());
	}
	
	@Test
	public void testRemoveIfOneSurroundedByOtherColor() {
		board.addStone(1, 1, false);
		board.addStone(1, 2, true);
		board.addStone(1, 0, true);
		board.addStone(0, 1, true);
		board.addStone(2, 1, true);
		assertTrue(board.getField(1, 1).isEmpty());
		assertEquals(4, board.getField(1, 2).liberty());
	}
	
	@Test
	public void testRemoveChain() {
		board.addStone(1, 2, true);
		board.addStone(1, 3, true);
		board.addStone(2, 1, true);
		board.addStone(2, 2, false);
		board.addStone(2, 3, false);
		board.addStone(3, 2, true);
		board.addStone(3, 3, true);
		board.addStone(2, 4, true);
		assertTrue(board.getField(2, 2).isEmpty());
		assertTrue(board.getField(2, 3).isEmpty());
	}
	
	@Test
	public void testRemoveOrder() {
		board.addStone(1, 1, true);
		board.addStone(1, 2, true);
		board.addStone(1, 3, true);
		board.addStone(1, 4, true);
		board.addStone(1, 5, true);
		board.addStone(2, 1, true);
		board.addStone(2, 5, true);
		board.addStone(3, 1, true);
		board.addStone(3, 5, true);
		board.addStone(4, 1, true);
		board.addStone(4, 5, true);
		board.addStone(5, 1, true);
		board.addStone(5, 2, true);
		board.addStone(5, 3, true);
		board.addStone(5, 4, true);
		board.addStone(2, 2, false);
		board.addStone(2, 3, false);
		board.addStone(2, 4, false);
		board.addStone(3, 2, false);
		board.addStone(3, 4, false);
		board.addStone(4, 2, false);
		board.addStone(4, 3, false);
		board.addStone(4, 4, false);
		board.addStone(3, 3, true);
		assertTrue(board.getField(2, 2).isEmpty());
	}
	
	@Test
	public void testRemoveOrder2() {
		board.addStone(1, 1, true);
		board.addStone(1, 2, true);
		board.addStone(1, 3, true);
		board.addStone(1, 4, true);
		board.addStone(1, 5, true);
		board.addStone(2, 1, true);
		board.addStone(2, 5, true);
		board.addStone(3, 1, true);
		board.addStone(3, 5, true);
		board.addStone(4, 1, true);
		board.addStone(4, 5, true);
		board.addStone(5, 1, true);
		board.addStone(5, 2, true);
		board.addStone(5, 3, true);
		board.addStone(5, 4, true);
		board.addStone(3, 3, true);
		board.addStone(2, 2, false);
		board.addStone(2, 3, false);
		board.addStone(2, 4, false);
		board.addStone(3, 2, false);
		board.addStone(3, 4, false);
		board.addStone(4, 2, false);
		board.addStone(4, 3, false);
		board.addStone(4, 4, false);
		assertTrue(board.getField(3, 3).isEmpty());
	}

	@Test
	public void testScore() {
		board.addStone(0, 0, true);
		board.addStone(1, 0, false);
		board.addStone(0, 1, true);
		board.addStone(1, 1, false);
		board.addStone(4, 4, true);
		board.addStone(4, 0, true);
		board.addStone(0, 2, false);
		List<Integer> score = board.getScore();
		assertEquals(5, score.get(0).intValue());
		assertEquals(2, score.get(1).intValue());
		board.addStone(1, 2, false);
		List<Integer> score2 = board.getScore();
		assertEquals(6, score2.get(0).intValue());
		assertEquals(2, score2.get(1).intValue());
	}
}
