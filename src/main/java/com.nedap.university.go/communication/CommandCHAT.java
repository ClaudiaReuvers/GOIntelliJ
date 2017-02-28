package com.nedap.university.go.communication;

/**
 * Created by claudia.reuvers on 24/02/2017.
 */
public class CommandCHAT implements Command {

    private String[] args;

    public CommandCHAT(String line) {
        args = line.split(" ", 2);
    }

    @Override
    public void execute(ClientHandler client) throws InvalidCommandException {
        Protocol.checkArguments(args, Protocol.CHAT);
        String msg = Protocol.CHAT + " " + client.getClientName() + ": " + args[1];
        if (client.getStatus() == CHState.INGAME) {
            client.getGame().broadcast(msg);
        } else if (client.getStatus() == CHState.LOGGEDIN) {
            throw new InvalidCommandException("You may not use the " + Protocol.CHAT + " command at this moment. You first need to set your name using the " + Protocol.PLAYER + " command.");
        } else {
            client.getServer().broadcastToWaiting(msg);
        }
    }
}
