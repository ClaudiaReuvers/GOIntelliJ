package com.nedap.university.go.newCommunication;

/**
 * Created by claudia.reuvers on 24/02/2017.
 */
public class CommandTABLEFLIP implements Command {

    private String args[];

    public CommandTABLEFLIP(String line) {
        args = line.split(" ");
    }

    @Override
    public void execute(newClientHandler client) throws InvalidCommandException {
        checkUse(client);
        checkArguments();
        if (!client.getGame().isTurn(client.getColor())) {
            client.sendWARNING("It is not you turn.");
            return;
        }
        client.getGame().broadcast("TABLEFLIPPED " + booleanToColor(client.getColor()));
        client.getGame().broadcast("END -1 -1");
        //TODO: end the game
    }

    private void checkUse(newClientHandler client) throws InvalidCommandException {
        if (client.getStatus() != CHState.INGAME) {
            throw new InvalidCommandException("You may not use the MOVE command at this moment.");
        }
    }

    private void checkArguments() throws InvalidCommandException {
        Protocol.checkArguments(args, "TABLEFLIP");
    }

    private String booleanToColor(boolean white) {
        if (white) {
            return "white";
        } else {
            return "black";
        }
    }
}