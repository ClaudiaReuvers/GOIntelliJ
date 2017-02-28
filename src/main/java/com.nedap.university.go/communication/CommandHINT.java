package com.nedap.university.go.communication;

import com.nedap.university.go.game.Board;

/**
 * Created by claudia.reuvers on 28/02/2017.
 */
public class CommandHINT implements Command {

    private String[] args;

    public CommandHINT(String line) {
        args = line.split(" ");
    }

    @Override
    public void execute(ClientHandler client) throws InvalidCommandException {
        checkUse(client);
        checkArguments();
        if (!client.getGame().isTurn(client.getColor())) {
            client.sendWARNING("It is not your turn.");
            return;
        }
        Board board = client.getGame().getBoard();
        client.getClientSize();
        for (int x = 0; x < client.getClientSize(); x++) {
            for (int y = 0; y < client.getClientSize(); y++) {
                if (board.getField(x, y).isEmpty()) {
                    client.sendMessage(Protocol.CHAT + " Possible move: " + x + " " + y);
                    return;
                }
            }
        }
    }

    private void checkUse(ClientHandler client) throws InvalidCommandException {
        if (client.getStatus() != CHState.INGAME) {
            throw new InvalidCommandException("You  may not use the " + Protocol.HINT + " command at this time.");
        }
    }

    private void checkArguments() throws InvalidCommandException {
        Protocol.checkArguments(args, Protocol.HINT);
    }
}