package com.nedap.university.go.communication;

import com.nedap.university.go.game.Board;

import java.util.List;

/**
 * Created by claudia.reuvers on 28/02/2017.
 */
public interface AI {

    public String determineMove(Board board, boolean white);
}
