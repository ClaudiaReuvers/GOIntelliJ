package com.nedap.university.go.communication;

import com.nedap.university.go.game.Game;

import java.util.List;

/**
 * Created by claudia.reuvers on 17/02/2017.
 */
public class CommandPASS implements Command {

    private String incomming;

    public CommandPASS(String line) {
        incomming = line;
    }

    @Override
    public void execute(Server server, ClientHandler client) {
        Game game = client.getGame();
        boolean white = client.getColor();
        //Check if it's the clients turn
        if (!game.isTurn(white)) {
            client.sendMessage("WARNING It is not your turn");
            return;
        }
        if (!checkArguments(server)) {
            client.sendMessage("WARNING Invalid command");
        }
        server.broadcastToGame(game, "PASSED " + booleanToColor(white));
        if (game.isPassed()) {
            game.endGame();
            server.broadcastToGame(game, "END 1 1");
        }
        game.setPass();
    }

    @Override
    public boolean checkArguments(Server server) {
        String[] args = incomming.split(" ");
        if (args.length != 1) {
            return false;
        }
        return false;
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
        return null;
    }
}
