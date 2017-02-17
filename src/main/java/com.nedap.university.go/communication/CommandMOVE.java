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
        if (!checkArguments(server)) {
            client.sendMessage("WARNING invalid command");
            return;
        }
        Game game = client.getGame();
        if (server.isValidMove(game, x, y)) {
            boolean white = true; //TODO: get clientcolor
            game.getBoard().addStone(x, y, white);
            //TODO: check if end of game
            String msg = "MOVED " + " " + booleanToColor(white) + "" + x + " " + y;
            server.broadcastToGame(game, msg);
        } else {

        }
        //TODO: [somewhere] check if it is his/her turn
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
