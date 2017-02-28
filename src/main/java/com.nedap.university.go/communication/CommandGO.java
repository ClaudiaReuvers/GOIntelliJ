package com.nedap.university.go.communication;

/**
 * Created by claudia.reuvers on 24/02/2017.
 */
public class CommandGO implements Command {

    private String[] args;

    public CommandGO(String line) {
        args = line.split(" ");
    }

    @Override
    public void execute(ClientHandler client) throws InvalidCommandException {
        checkUse(client.getStatus());
        Protocol.checkArguments(args, Protocol.GO);
        int size = Integer.parseInt(args[1]);
        client.setClientSize(size);
        client.setStatus(CHState.WAITING);
        client.sendMessage(Protocol.WAITING);
        if (client.getServer().isMatch(size)) {
            ClientHandler opponent = client.getServer().getMatch(size);
            client.getServer().setGame(opponent, client, size);
        } else {
            client.getServer().addToWaitingList(size, client);
        }
    }

    public void checkUse(CHState state) throws InvalidCommandException {
        if (state != CHState.GOTNAME) {
            throw new InvalidCommandException("You may not use the " + Protocol.GO + " command at this moment.");
        }
    }

}
