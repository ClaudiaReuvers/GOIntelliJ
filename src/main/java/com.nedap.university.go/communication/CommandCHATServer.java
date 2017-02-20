package com.nedap.university.go.communication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by claudia.reuvers on 17/02/2017.
 */
public class CommandCHATServer implements Command {

    private String incomming;
    private String msg;

    public CommandCHATServer(String line) {
        incomming = line;
    }

    @Override
    public void execute(Server server, ClientHandler client) {
        ClientHandler opponent = client.getOpponent();
        if (checkArguments(server)) {
            String sendMessage = "CHAT " + client.getClientName() + ": " + msg;
            opponent.sendMessage(sendMessage);
        } else {
            //TODO
        }
    }

    @Override
    public boolean checkArguments(Server server) {
        String[] args = incomming.split(" ", 2);
        msg = args[1];
        return (msg != null);
    }

    @Override
    public List<String> getArguments() {
        List<String> args = new ArrayList<>();
        args.add(msg);
        return args;
    }
}
