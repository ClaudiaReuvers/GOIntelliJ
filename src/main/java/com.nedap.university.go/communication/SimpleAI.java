package com.nedap.university.go.communication;

import com.nedap.university.go.game.Board;
import com.nedap.university.go.game.Chain;
import com.nedap.university.go.game.Stone;
import com.nedap.university.go.game.StoneState;

import java.util.*;

/**
 * Created by claudia.reuvers on 28/02/2017.
 */
public class SimpleAI implements AI {

    private StoneState ownState;
    private Set<Chain> ownChain = new HashSet<>();
    private Board copy;

    @Override
    public String determineMove(Board board, boolean white) {
        copy = board.deepCopy();
        setOwnState(white);
        setOwnChains();
        String move = increaseLiberty(board, white);
        if (move.equals(Protocol.PASS)) {
            return randomMove(board, white);
        } else {
            return move;
        }
    }

    private String increaseLiberty(Board board, boolean white) {
        for (Chain chains : ownChain) {
            for (Stone stone : chains.getChainNeighbours()) {
                if (stone.isEmpty()) {
                    int previousLiberty = stone.liberty();
                    stone.setColor(white);
                    copy.makeChainWithNewStone(stone);
                    int newLiberty = stone.liberty();
                    if (newLiberty > previousLiberty) {
                        Stone[] allFields = copy.getAllFields();
                        for (int i = 0; i < copy.getDimension() * copy.getDimension(); i++) {
                            if (allFields[i] == stone) {
                                int x = i % copy.getDimension();
                                int y = i / copy.getDimension();
                                System.out.println("Try " + x + "," + y);
                                if (Protocol.isValidMove(board, x, y, white)) {
                                    return move(x, y);
                                }
                            }
                        }
                    }
                }
            }
        }
        return Protocol.PASS;
    }

    private void setOwnChains() {
        for (int y = 0; y < copy.getDimension(); y++) {
            for (int x = 0; x < copy.getDimension(); x++) {
                Stone stone = copy.getField(x, y);
                if (stone.getState() == ownState) {
                    boolean added = ownChain.add(stone.getChain());
//                    System.out.println("Chain added: " + added);
                }
            }
        }
    }

    private String randomMove(Board board, boolean white) {
        int max = board.getDimension();
        int x;
        int y;
        Random random = new Random();
        int count = 0;
        do {
            x = random.nextInt(max);
            y = random.nextInt(max);
            count++;
            System.out.println("Try " + x + "," + y);
        } while (count < 5 && !Protocol.isValidMove(board, x, y, white));
        if (count == 5) {
            return Protocol.PASS;
        } else {
            return move(x, y);
        }
    }

    private String move(int x, int y) {
        return Protocol.MOVE + " " + x + " " + y;
    }

    private void setOwnState(boolean white) {
        if (white) {
            ownState = StoneState.WHITE;
        } else {
            ownState = StoneState.BLACK;
        }
    }


}