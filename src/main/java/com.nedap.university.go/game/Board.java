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
//	private boolean useGUI;
	private GoGUIIntegrator GUI;
	
	public Board(int boardSize, boolean GUI) {
		DIM = boardSize;
		fields = new Stone[DIM * DIM];
		for (int i = 0; i < DIM * DIM; i++) {
			fields[i] = new Stone();
		}
		for (int x = 0; x < DIM; x++) {
			for (int y = 0; y < DIM; y++) {
				Stone stone = getField(x, y);
				stone.setCoordinates(x, y);
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
//		useGUI = GUI;
		this.GUI = new GoGUIIntegrator(true, true, DIM);
//			this.GUI.setBoardSize(DIM);
		this.GUI.startGUI();
	}

	public Board(int boardSize) {
		DIM = boardSize;
		fields = new Stone[DIM * DIM];
		for (int i = 0; i < DIM * DIM; i++) {
			fields[i] = new Stone();
		}
		for (int x = 0; x < DIM; x++) {
			for (int y = 0; y < DIM; y++) {
				Stone stone = getField(x, y);
				stone.setCoordinates(x, y);
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
//		if (useGUI) {
//			buildGUI();
//		}
	}

//	private void buildGUI() {
//		GUI.clearBoard();
//		for (int yGUI = 0; yGUI < DIM; yGUI++) {
//            for (int xGUI = 0; xGUI < DIM; xGUI++) {
//                if (getField(xGUI, yGUI).getState() == StoneState.BLACK) {
//                    GUI.addStone(xGUI, yGUI, false);
//                } else if (getField(xGUI, yGUI).getState() == StoneState.WHITE) {
//                    GUI.addStone(xGUI, yGUI, true);
//                }
//            }
//        }
//	}

	public Stone getField(int x, int y) {
		return fields[coordinatesToIndex(x, y)];
	}
	
	public int coordinatesToIndex(int x, int y) {
		return (x + DIM * y);
	}
	
//	private boolean isOnCorner(int x, int y) {
//		if (x == 0 || x == DIM - 1) {
//			if (y == 0 || y == DIM - 1) {
//				return true;
//			}
//		}
//		return false;
//	}
	
//	private boolean isOnBorder(int x, int y) {
//		return (x == 0 || x == DIM - 1 || y == 0 || y == DIM - 1);
//	}
	
	public void clearBoard() {
		for (int x = 0; x < DIM; x++) {
			for (int y = 0; y < DIM; y++) {
				this.getField(x, y).setEmpty();
			}
		}
//		if (useGUI) {
//			GUI.clearBoard();
//		}
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
		if (isEmpty()) {
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

	private boolean isEmpty() {
		for (int x = 0; x < DIM; x++) {
			for (int y = 0; y < DIM; y++) {
				if (!getField(x, y).isEmpty()) {
					return false;
				}
			}
		}
		return true;
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
//			System.out.println("Chain black: " + black + " (max. " + empty.getChainNeighbours().size() + ")");
//			System.out.println("Chain white: " + white + " (max. " + empty.getChainNeighbours().size() + ")");
			if (!black) {
//				System.out.print(black + " added to blackscore " + scoreBlack);
				scoreBlack += empty.getChain().size();
//				System.out.println(". Score is now " + scoreBlack);
			} else if (!white) {
//				System.out.print(white + " added to whitescore " + scoreWhite);
				scoreBlack += empty.getChain().size();
//				System.out.println(". Score is now " + scoreWhite);
			}
		}
		List<Integer> score = new LinkedList<>();
		score.add(scoreBlack);
		score.add(scoreWhite);
		return score;
	}

	public Board deepCopy() {
//		Board boardCopy = new Board(DIM, useGUI);
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

	public void setDimension(int dimension) {
		clearBoard();
		this.DIM = dimension;
	}

//	public static void main(String[] args) {
//		Board board = new Board(9);
//		board.addStone(1, 1, true);
//		board.addStone(1, 2, true);
//		board.addStone(1, 0, false);
//		board.addStone(0, 1, true);
//		board.addStone(2, 1, true);
//		System.out.println(board.toString());
//	}
}
