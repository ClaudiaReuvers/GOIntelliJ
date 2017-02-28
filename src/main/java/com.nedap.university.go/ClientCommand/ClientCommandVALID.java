package com.nedap.university.go.ClientCommand;

import com.nedap.university.go.communication.Client;
import com.nedap.university.go.communication.Protocol;
import com.nedap.university.go.game.Board;

import java.util.List;

/**
 * Created by claudia.reuvers on 20/02/2017.
 */
public class ClientCommandVALID implements ClientCommand {

    String incomming;

    public ClientCommandVALID(String line) {
        incomming = line;
    }

    @Override
    public void execute(Client client) {
        String[] args = incomming.split(" ");
        boolean color = colorToBoolean(args[1]);
        int x = Integer.parseInt(args[2]);
        int y = Integer.parseInt(args[3]);
        Board board = client.getBoard();
        board.addStone(x, y, color);
        client.updateGUI();
        client.print(args[1] + " stone placed at (" + x + "," + y + ")");
        client.print("Current state:\n" + board.toString());
        if (client.isComputer() && color != client.getColor()) {
            client.print("Computer and clients turn.");
            String move = client.determineMove();
            client.sendMessage(move);
        }
        board.saveGameState();
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
