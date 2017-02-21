package com.nedap.university.go.communication;

import com.nedap.university.go.game.Game;

import java.util.List;

/**
 * Created by claudia.reuvers on 17/02/2017.
 */
public class CommandTABLEFLIP implements Command {

    private String incomming;

    public CommandTABLEFLIP(String line) {
        incomming = line;
    }
    @Override
    public void execute(Server server, ClientHandler client) {
        Game game = client.getGame();
        boolean white = client.getColor();
        if (!checkArguments(server)) {
            client.sendMessage("WARNING Invalid command");
            return;
        }
        server.broadcastToGame(game, "TABLEFLIPPED " + booleanToColor(white));
        server.broadcastToGame(game, "END -1 -1");
//        game.endGame();
    }

    @Override
    public boolean checkArguments(Server server) {
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
