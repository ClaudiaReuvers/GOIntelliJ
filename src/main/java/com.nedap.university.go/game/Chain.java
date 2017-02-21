package com.nedap.university.go.game;

import java.util.*;

public class Chain {

	private ArrayList<Stone> chain;
//	private ArrayList<thirdStone> empty;
	
	public Chain() {
		chain = new ArrayList<>();
//		empty = new ArrayList<>();
	}
	
	public void addStone(Stone stone) {
		if (!chain.contains(stone)) {
			chain.add(stone);
			stone.addChain(this);
		}
	}
	
	public void join(Chain otherChain) {
//		System.out.print("Join to stones: " + otherChain.getChain().get(0).getState());
		for (Stone stones : otherChain.getChain()) {
//			System.out.println("Join " + stones.getState());
			if (!chain.contains(stones)) {
				chain.add(stones);
			}
		}
		for (Stone stones : chain) {
			stones.addChain(this);
		}
	}
	
	public int calculateLibertyChain() {
		ArrayList<Stone> empty = new ArrayList<>();
//		for (Stone neighbours : getNeighbours()) {
//			if (neighbours.isEmpty()) {
//				empty.add(neighbours);
//			}
//		}

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
	
//	public int getLiberty() {
//		return empty.size();
//	}

	public ArrayList<Stone> getChain() {
		return chain;
	}
	
	public boolean contains(Stone stone) {
		return chain.contains(stone);
	}
	
//	public ArrayList<thirdStone> getEmptyStones() {
//		return empty;
//	}
}
