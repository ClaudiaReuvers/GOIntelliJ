package com.nedap.university.go.game;

import com.nedap.university.go.communication.Client;

public class Game {

	private Client[] clients;
	private Board board;
	
	public Game(Client[] clients, int dimension) {
		this.clients = clients;
		board = new Board(dimension);
	}
	
	public String[] getClientNames() {
		String[] names = new String[clients.length];
		for (int i = 0; i < clients.length; i++) {
			names[i] = clients[i].getName();
		}
		return names;
	}
	
	public int getNrClients() {
		return clients.length;
	}
	
	public Client[] getClients() {
		return clients;
	}
	
	public Board getBoard() {
		return board;
	}
	
}
