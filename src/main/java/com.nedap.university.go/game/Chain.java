package com.nedap.university.go.game;

import java.util.*;

public class Chain {

	private ArrayList<Stone> chain;

	//Constructor
	public Chain() {
		chain = new ArrayList<>();
	}

	//Methods
	public void addStone(Stone stone) {
		if (!chain.contains(stone)) {
			chain.add(stone);
			stone.addChain(this);
		}
	}
	
	public void join(Chain otherChain) {
		for (Stone stones : otherChain.getChain()) {
			if (!chain.contains(stones)) {
				chain.add(stones);
			}
		}
		for (Stone stones : chain) {
			stones.addChain(this);
		}
	}

	//Queries
	public int calculateLibertyChain() {
		ArrayList<Stone> empty = new ArrayList<>();

		for (Stone stones : chain) {
			for (Stone emptyStones : stones.getNeighbour()) {
				if (emptyStones.isEmpty() && !empty.contains(emptyStones)) {
					empty.add(emptyStones);
				}
			}
		}
		return empty.size();
	}

	public Set<Stone> getChainNeighbours() {
		Set<Stone> neighbours = new HashSet<>();
		for (Stone stones : chain) {
			for (Stone neighbour : stones.getNeighbour()) {
				neighbours.add(neighbour);
			}
		}
		neighbours.removeAll(chain);
		return neighbours;
	}

	public ArrayList<Stone> getChain() {
		return chain;
	}
}
