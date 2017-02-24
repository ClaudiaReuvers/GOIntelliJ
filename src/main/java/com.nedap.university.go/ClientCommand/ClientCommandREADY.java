package com.nedap.university.go.ClientCommand;

import com.nedap.university.go.newCommunication.Client;
import com.nedap.university.go.game.Board;

import java.util.List;

/**
 * Created by claudia.reuvers on 20/02/2017.
 */
public class ClientCommandREADY implements ClientCommand {

    String incomming;

    public ClientCommandREADY(String line) {
        incomming = line;
    }

    @Override
    public void execute(Client client) {
        String args[] = incomming.split(" ");
        String color = args[1];
        if (color.equals("white")) {
            client.setColor(true);
        } else if (color.equals("black")){
            client.setColor(false);
        } else {
            client.print("Does not recognize the color.");
            //TODO
        }
        String opponentName = args[2];
        client.setOpponent(opponentName);
        int size = Integer.parseInt(args[3]);
        Board board = new Board(size, true);
        client.setBoard(board);
        //TODO: is making of a board sufficient?
        client.print("You start a game with " + opponentName + " at boardsize " + size + " and play with color " + color + ".");
    }

    @Override
    public boolean checkArguments() {
        return false;
    }

    @Override
    public List<String> getArguments() {
        return null;
    }
}
