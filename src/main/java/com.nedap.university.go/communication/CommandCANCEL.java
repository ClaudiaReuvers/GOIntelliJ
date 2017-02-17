package com.nedap.university.go.communication;

import java.util.List;

/**
 * Created by claudia.reuvers on 13/02/2017.
 */
public class CommandCANCEL implements Command {

    private String incomming;

    public CommandCANCEL(String line) {
        incomming = line;
    }

    @Override
    public void execute(Server server, ClientHandler client) {
        if (checkArguments(server)) {
            server.removeFromWaitingList(client.getClientSize());
            server.removeFromClientHandlerList(client);
            server.broadcastToAll("CHAT [Player " + client.getClientName() + " has left]");
        }
    }

    @Override
    public boolean checkArguments(Server server) {
        String[] args = incomming.split(" ");
        return (args.length == 1);
    }

    @Override
    public List<String> getArguments() {
        return null;
    }
}
