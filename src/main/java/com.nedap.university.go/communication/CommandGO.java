package com.nedap.university.go.newCommunication;

/**
 * Created by claudia.reuvers on 24/02/2017.
 */
public class CommandGO implements Command {

    private String[] args;

    public CommandGO(String line) {
        args = line.split(" ");
    }

    @Override
    public void execute(newClientHandler client) throws InvalidCommandException {
        checkUse(client);
        Protocol.checkArguments(args, "GO");
        int size = Integer.parseInt(args[1]);
        client.setClientSize(size);
        client.setStatus(CHState.WAITING);
        client.sendMessage("WAITING");
        if (client.getServer().isMatch(size)) {
            newClientHandler opponent = client.getServer().getMatch(size);
            client.getServer().setGame(opponent, client, size);
        } else {
            client.getServer().addToWaitingList(size, client);
        }
    }

    public void checkUse(newClientHandler client) throws InvalidCommandException {
        if (client.getStatus() != CHState.GOTNAME) {
            throw new InvalidCommandException("You may not use the GO command at this moment.");
        }
    }

}
