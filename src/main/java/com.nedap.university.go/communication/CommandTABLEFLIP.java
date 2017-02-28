package com.nedap.university.go.communication;

/**
 * Created by claudia.reuvers on 24/02/2017.
 */
public class CommandTABLEFLIP implements Command {

    private String args[];

    public CommandTABLEFLIP(String line) {
        args = line.split(" ");
    }

    @Override
    public void execute(ClientHandler client) throws InvalidCommandException {
        checkUse(client.getStatus());
        checkArguments();
        if (!client.getGame().isTurn(client.getColor())) {
            client.sendWARNING("It is not you turn.");
            return;
        }
        client.getGame().broadcast(Protocol.TABLEFLIPPED + " " + booleanToColor(client.getColor()));
        client.getGame().broadcast(Protocol.END + " -1 -1");
        client.getGame().endGame();
    }

    private void checkUse(CHState state) throws InvalidCommandException {
        if (state != CHState.INGAME) {
            throw new InvalidCommandException("You may not use the " + Protocol.TABLEFLIP + " command at this moment.");
        }
    }

    private void checkArguments() throws InvalidCommandException {
        Protocol.checkArguments(args, Protocol.TABLEFLIP);
    }

    private String booleanToColor(boolean white) {
        if (white) {
            return "white";
        } else {
            return "black";
        }
    }
}