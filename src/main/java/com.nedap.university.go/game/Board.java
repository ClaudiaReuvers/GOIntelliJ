package com.nedap.university.go.game;

//import GUI.*;
//TODO: also insert GUI?

import com.nedap.university.go.GUI.GoGUIIntegrator;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Board {

	private int DIM;
	private Stone[] fields;
	private GoGUIIntegrator GUI;

	//Constructor
//	public Board(int boardSize, boolean GUI) {
//		DIM = boardSize;
//		fields = new Stone[DIM * DIM];
//		for (int i = 0; i < DIM * DIM; i++) {
//			fields[i] = new Stone();
//		}
//		for (int x = 0; x < DIM; x++) {
//			for (int y = 0; y < DIM; y++) {
//				Stone stone = getField(x, y);
//				if (!isOnTopBorder(x, y)) {
//					stone.addNeighbour(getField(x, y -1));
//				}
//				if (!isOnRightBorder(x, y)) {
//					stone.addNeighbour(getField(x + 1, y));
//				}
//				if (!isOnBottomBorder(x, y)) {
//					stone.addNeighbour(getField(x, y + 1));
//				}
//				if (!isOnLeftBorder(x, y)) {
//					stone.addNeighbour(getField(x - 1, y));
//				}
//				stone.addThisToChain();
//				stone.addThisToEmptyChain();
//			}
//		}
//		this.GUI = new GoGUIIntegrator(true, true, DIM);
//		this.GUI.startGUI();
//	}

	public Board(int boardSize) {
		DIM = boardSize;
		fields = new Stone[DIM * DIM];
		for (int i = 0; i < DIM * DIM; i++) {
			fields[i] = new Stone();
		}
		for (int x = 0; x < DIM; x++) {
			for (int y = 0; y < DIM; y++) {
				Stone stone = getField(x, y);
				if (!isOnTopBorder(x, y)) {
					stone.addNeighbour(getField(x, y -1));
				}
				if (!isOnRightBorder(x, y)) {
					stone.addNeighbour(getField(x + 1, y));
				}
				if (!isOnBottomBorder(x, y)) {
					stone.addNeighbour(getField(x, y + 1));
				}
				if (!isOnLeftBorder(x, y)) {
					stone.addNeighbour(getField(x - 1, y));
				}
				stone.addThisToChain();
				stone.addThisToEmptyChain();
			}
		}
	}

	//Methods
	public void addStone(int x, int y, boolean white) {
		Stone stone = getField(x, y);
		stone.setColor(white);
		for (Stone surrounding : stone.getNeighbour()) {
			if (surrounding.getState() == stone.getState()) {
				stone.join(surrounding);
			}
			if (surrounding.getState() == stone.otherColor()) {
				if (surrounding.liberty() == 0) {
					surrounding.remove();
				}
			}
		}
		if (stone.liberty() == 0) {
				stone.remove();
		}
	}

	public void clearBoard() {
		for (int x = 0; x < DIM; x++) {
			for (int y = 0; y < DIM; y++) {
				this.getField(x, y).setEmpty();
			}
		}
	}

	//Queries
	public int coordinatesToIndex(int x, int y) {
		return (x + DIM * y);
	}

	public Stone getField(int x, int y) {
		return fields[coordinatesToIndex(x, y)];
	}

	private boolean isEmptyBoard() {
		for (int x = 0; x < DIM; x++) {
			for (int y = 0; y < DIM; y++) {
				if (!getField(x, y).isEmpty()) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean isOnTopBorder(int x, int y) {
		return (y == 0);
	}
	
	private boolean isOnLeftBorder(int x, int y) {
		return (x == 0);
	}
	
	private boolean isOnRightBorder(int x, int y) {
		return (x == DIM - 1);
	}
	
	private boolean isOnBottomBorder(int x, int y) {
		return (y == DIM -1);
	}
	
	public int getDimension() {
		return DIM;
	}
	
	public Stone[] getAllFields(){
		return fields;
	}

	public String toString() {
		String board = "";
		for (int i = 0; i < DIM; i++) {
			for (int j = 0; j < DIM; j++) {
				String stone = " ";
				if (getField(j, i).getState() == StoneState.BLACK) {
					stone = "B";
				} else if (getField(j, i).getState() == StoneState.WHITE){
					stone = "W";
				} else {
					stone = ".";
				}
				board += stone;// + " - ";
			}
			if (i != DIM -1) {
				board += "\n";
			}
		}
		return board;
	}

	public List<Integer> getScore() {
		List<Integer> score = new LinkedList<>();
		if (isEmptyBoard()) {
			score.add(0);
			score.add(0);
		}
		//Calculate score on taken fields and make chains of empty fields
		int scoreBlack = 0;
		int scoreWhite = 0;
		Set<Chain> emptyChains = new HashSet<>();
		for (int x = 0; x < DIM; x++) {
			for (int y = 0; y < DIM; y++) {
				Stone stone = getField(x, y);
				StoneState state = stone.getState();
				if (state == StoneState.BLACK) {
					scoreBlack++;
				} else if (state == StoneState.WHITE) {
					scoreWhite++;
				} else {
					for (Stone neighbour : stone.getNeighbour()) {
						if (neighbour.getState() == StoneState.EMPTY) {
							stone.joinEmptyChain(neighbour);
						}
					}
					emptyChains.add(stone.getEmptyChain());
				}
			}
		}
		//Calculate score on empty chains
		List<Integer> scoreEmpty = getScoreEmptyChains(emptyChains);

		//Create output
		score.add(scoreBlack + scoreEmpty.get(0));
		score.add(scoreWhite + scoreEmpty.get(1));
		return score;
	}

	

	private List<Integer> getScoreEmptyChains(Set<Chain> emptyChains) {
		int scoreBlack = 0;
		int scoreWhite = 0;
		for (Chain empty : emptyChains) {
			boolean black = false;
			boolean white = false;
			for (Stone neighbours : empty.getChainNeighbours()) {
				if (neighbours.getState() == StoneState.BLACK) {
					black = true;
				} else {
					white = true;
				}
			}
			if (!black) {
				scoreBlack += empty.getChain().size();
			} else if (!white) {
				scoreBlack += empty.getChain().size();
			}
		}
		List<Integer> score = new LinkedList<>();
		score.add(scoreBlack);
		score.add(scoreWhite);
		return score;
	}

	public Board deepCopy() {
		Board boardCopy = new Board(DIM);
		for (int x = 0; x < DIM; x++) {
			for (int y = 0; y < DIM; y++) {
				StoneState state = getField(x, y).getState();
				if (state == StoneState.BLACK) {
					boardCopy.addStone(x, y, false);
				} else if (state == StoneState.WHITE) {
					boardCopy.addStone(x, y, true);
				}
			}
		}
		return boardCopy;
	}
}
