package com.nedap.university.go.communication;

import java.util.List;

/**
 * Created by claudia.reuvers on 17/02/2017.
 */
public class CommandCHAT implements Command {

    private String incomming;

    public CommandCHAT(String line) {
        incomming = line;
    }
    @Override
    public void execute(Server server, ClientHandler client) {
        ClientHandler opponent = client.getOpponent();
        opponent.sendMessage(incomming);
    }

    @Override
    public boolean checkArguments(Server server) {
        return false;
    }

    @Override
    public List<String> getArguments() {
        return null;
    }
}
