package com.nedap.university.go.game;

//import GUI.*;
//TODO: also insert GUI?

import com.nedap.university.go.GUI.GoGUIIntegrator;

public class Board {

	private int DIM;
	private Stone[] fields;
	private boolean useGUI;
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
			}
		}
		useGUI = GUI;
		if (useGUI) {
			this.GUI = new GoGUIIntegrator(true, true, DIM);
			this.GUI.startGUI();
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
			if (useGUI) {
				stone.remove(GUI);
			} else {
				stone.remove();
			}
		}
		if (useGUI) {
			GUI.addStone(x, y, white);
		}
	}

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
				if (getField(i, j).getState() == StoneState.BLACK) {
					stone = "B";
				} else if (getField(i, j).getState() == StoneState.WHITE){
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
