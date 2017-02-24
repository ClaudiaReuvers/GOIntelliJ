package com.nedap.university.go.newCommunication;

import java.util.List;

/**
 * Created by claudia.reuvers on 24/02/2017.
 */
public class CommandPASS implements Command {

    String args[];

    public CommandPASS(String line) {
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
        client.getGame().broadcast("PASSED " + booleanToColor(client.getColor()));
        if (client.getGame().isPassed()) {
            List<Integer> score = client.getGame().endGame();
            client.getGame().broadcast("END " + score.get(0) + " " + score.get(1));
        }
        client.getGame().setPass();
        client.getGame().alternateTurn();
    }

    private void checkUse(newClientHandler client) throws InvalidCommandException {
        if (client.getStatus() != CHState.INGAME) {
            throw new InvalidCommandException("You may not use the PASS command at this moment.");
        }
    }

    private void checkArguments() throws InvalidCommandException {
        Protocol.checkArguments(args, "PASS");
    }

    private String booleanToColor(boolean white) {
        if (white) {
            return "white";
        } else {
            return "black";
        }
    }
}
