package com.nedap.university.go.game;

import com.nedap.university.go.communication.Client;
import com.nedap.university.go.communication.ClientHandler;

import java.util.LinkedList;
import java.util.List;

public class Game {

	private ClientHandler client1;
	private ClientHandler client2;
	private List<ClientHandler> listClients = new LinkedList<>();
	private Board board;
	private boolean turn; //true: turn of white; false; turn of black
    private boolean pass;
    private List<String> previousBoards;
	
	public Game(ClientHandler client1, ClientHandler client2, int dimension) {
		this.client1 = client1;
		this.client2 = client2;
		listClients.add(client1);
		listClients.add(client2);
		board = new Board(dimension, false);
		this.turn = false;
		this.pass = false;
		previousBoards = new LinkedList<>();
	}
	
//	public String[] getClientNames() {
//		String[] names = new String[listClients.size()];
//		for (int i = 0; i < listClients.size(); i++) {
//			names[i] = listClients[i].getName();
//		}
//		return names;
//	}
	
	public int getNrClients() {
		return listClients.size();
	}
	
	public List<ClientHandler> getClients() {
		return listClients;
	}
	
	public Board getBoard() {
		return board;
	}

	public String toString() {
		return board.toString();
	}

	public void alternateTurn() {
		turn = !turn;
	}

    public void addStoneToBoard(int x, int y, boolean white) {
	    board.addStone(x, y ,white);
    }

    public boolean isTurn(boolean white) {
	    return (turn == white);
    }

    public void setPass() {
	    pass = true;
    }

    public void resetPass() {
	    pass = false;
    }

    public boolean isPassed() {
	    return pass;
    }

    public List<Integer> endGame() {
		return board.getScore();
	    //TODO
    }

    public boolean testNextMove(int x, int y, boolean white) {
		Board boardCopy = board.deepCopy();
		boardCopy.addStone(x, y, white);
		if (previousBoards.isEmpty()) {
			return true;
		} else {
			return !previousBoards.contains(boardCopy.toString());
		}
	}

	public boolean saveGameState() {
		return previousBoards.add(toString());
	}
}
