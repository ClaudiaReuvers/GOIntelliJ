package com.nedap.university.go.game;

import com.nedap.university.go.GUI.GoGUIIntegrator;

import java.util.ArrayList;

public class Stone {

	private StoneState color;
	private Chain chain;
	private Chain emptyChain;
	private ArrayList<Stone> neighbour;
	
	//Constructor
	public Stone() {
		this.color = StoneState.EMPTY;
		this.chain = new Chain();
		this.emptyChain = new Chain();
		this.neighbour = new ArrayList<>();
	}

	//Methods
	public void addThisToChain() {
		chain.addStone(this);
	}

	public void addChain(Chain chain) {
		this.chain = chain;
	}

	public void addNeighbour(Stone stone) {
		if (!neighbour.contains(stone)) {
			neighbour.add(stone);
		}
	}

	public void remove() {
		for (Stone stones : chain.getChain()) {
			stones.setEmpty();
		}
	}

	public void setEmpty() {
		this.color = StoneState.EMPTY;
		this.chain = new Chain();
		this.emptyChain = new Chain();
		addThisToChain();
		addThisToEmptyChain();
	}

	public void setColor(boolean white) {
		if (white) {
			this.color = StoneState.WHITE;
		} else {
			this.color = StoneState.BLACK;
		}
	}

	public void join(Stone stone) {
		chain.join(stone.getChain());
		stone.addChain(chain);
	}

	public void addThisToEmptyChain() {
		emptyChain.addStone(this);
	}

	public void addEmptyChain(Chain emptyChain) {
		this.emptyChain = emptyChain;
	}

	public void joinEmptyChain(Stone stone) {
		emptyChain.join(stone.getEmptyChain());
		stone.addEmptyChain(emptyChain);
	}

	//Queries
	public StoneState getState() {
		return color;
	}
	
	public StoneState otherColor() {
		if (color == StoneState.WHITE) {
			return StoneState.BLACK;
		} else if (color == StoneState.BLACK) {
			return StoneState.WHITE;
		} else {
			return StoneState.EMPTY;
		}
	}
	
	public Chain getChain() {
		return chain;
	}

	public Chain getEmptyChain() {
		return emptyChain;
	}
	
	public ArrayList<Stone> getNeighbour() {
		return neighbour;
	}
	
	public boolean isEmpty() {
		return (color == StoneState.EMPTY);
	}
	
	public int liberty() {
		return chain.calculateLibertyChain();
	}

}