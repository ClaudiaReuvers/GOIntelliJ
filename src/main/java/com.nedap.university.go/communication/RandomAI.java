package com.nedap.university.go.communication;

import com.nedap.university.go.game.Board;

import java.util.Random;

/**
 * Created by claudia.reuvers on 28/02/2017.
 * This AI places a random stone. If it can't find a valid move within 5 tries it passes.
 */
public class RandomAI implements AI {
    @Override
    public String determineMove(Board board, boolean white) {
        int max = board.getDimension();
        int x;
        int y;
        Random random = new Random();
        int count = 0;
        do {
            x = random.nextInt(max);
            y = random.nextInt(max);
            System.out.println("Try move " + x + "," + y + ".");
            count++;
        } while (count < 5 && !Protocol.isValidMove(board, x, y, white));
        if (count == 5) {
            return Protocol.PASS;
        } else {
            return Protocol.MOVE + " " + x + " " + y;
        }
    }
}
