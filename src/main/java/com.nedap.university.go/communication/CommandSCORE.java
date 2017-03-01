package com.nedap.university.go.communication;

import java.util.List;

/**
 * Created by claudia.reuvers on 28/02/2017.
 */
public class CommandSCORE implements Command {

    private String[] args;

    public CommandSCORE(String line) {
        args = line.split(" ");
    }

    @Override
    public void execute(ClientHandler client) throws InvalidCommandException {
        checkUse(client.getStatus());
        checkArguments();
        List<Integer> score = client.getGame().getScore();
        int black = score.get(0);
        int white = score.get(1);
        client.sendMessage(Protocol.CHAT + " The current score is " + black + " vs " + white + " (black vs white).");
    }

    private void checkArguments() throws InvalidCommandException {
        Protocol.checkArguments(args, Protocol.SCORE);
    }

    private void checkUse(CHState status) throws InvalidCommandException {
        if (status != CHState.INGAME) {
            throw new InvalidCommandException("You may not use the " + Protocol.SCORE + " command at this moment.");
        }
    }
}
