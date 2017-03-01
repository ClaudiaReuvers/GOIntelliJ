package com.nedap.university.go.ClientCommand;

import com.nedap.university.go.communication.Client;
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
        boolean color = colorToBoolean(args[1]);
        client.setColor(color);
        String opponentName = args[2];
        client.setOpponent(opponentName);
        int size = Integer.parseInt(args[3]);
//        if (client.getBoard() != null) {
//            client.getBoard().setDimension(size);
//            client.setGUI(size);
//        } else {
        client.createNewGame(size);
//            Board board = new Board(size);
//            client.setBoard(board);
//            client.setGUI(size);
//        }
        client.print("You start a game with " + opponentName + " at boardsize " + size + " and play with color " + args[1] + ".");
        if (client.isComputer() && color == client.getColor()) {
            client.print("Computer and clients turn.");
            String move = client.determineMove();
            client.sendMessage(move);
        }
    }

    private boolean colorToBoolean(String color) {
        if (color.equals("white")) {
            return true;
        } else if (color.equals("black")) {
            return false;
        } else {
            //TODO
            return false;
        }
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
