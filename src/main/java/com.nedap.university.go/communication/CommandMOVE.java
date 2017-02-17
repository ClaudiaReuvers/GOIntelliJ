package com.nedap.university.go.communication;

import com.nedap.university.go.game.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by claudia.reuvers on 13/02/2017.
 */
public class CommandMOVE implements Command {

    private String incomming;
    private int x;
    private int y;

    public CommandMOVE(String line) {
        incomming = line;
    }

    @Override
    public void execute(Server server, ClientHandler client) {
        Game game = client.getGame();
        //Check if it's the clients turn
        if (!game.isTurn(client.getColor())) {
            client.sendMessage("WARNING It is not your turn");
            return;
        }
        //Check if the command is valid
        if (!checkArguments(server)) {
            client.sendMessage("WARNING Invalid command");
            return;
        }
        //Check if the move is valid
        boolean white = client.getColor();
        if (server.isValidMove(game, x, y)) {
            game.addStoneToBoard(x, y, white);
            String msg = "VALID " + booleanToColor(white) + " " + x + " " + y;
            server.broadcastToGame(game, msg);
            game.resetPass();
            game.alternateTurn();
        } else {
            String msg = "INVALID " + booleanToColor(white) + " not a valid move";
            server.broadcastToGame(game, msg);
            //TODO: add why invalid move
        }
    }

    @Override
    public boolean checkArguments(Server server) {
        String[] args = incomming.split(" ");
        if (args.length != 3) {
            return false;
        }
        try {
            x = Integer.parseInt(args[1]);
            y = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private String booleanToColor(boolean white) {
        if (white) {
            return "white";
        } else {
            return "black";
        }
    }

    @Override
    public List<String> getArguments() {
        List<String> args = new ArrayList<>();
        args.add(Integer.toString(x));
        args.add(Integer.toString(y));
        return args;
    }
}
