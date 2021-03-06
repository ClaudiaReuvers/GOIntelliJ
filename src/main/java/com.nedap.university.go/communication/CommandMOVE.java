package com.nedap.university.go.communication;

import java.util.List;

/**
 * Created by claudia.reuvers on 24/02/2017.
 */
public class CommandMOVE implements Command {

    private String args[];

    public CommandMOVE(String line) {
        args = line.split(" ");
    }

    @Override
    public void execute(ClientHandler client) throws InvalidCommandException {
        checkUse(client);
        Protocol.checkArguments(args, Protocol.MOVE);
        int x = Integer.parseInt(args[1]); // should work since it is checked in checkArguments
        int y = Integer.parseInt(args[2]);
        Game game = client.getGame();
        if (!game.isTurn(client.getColor())) {
            client.sendWARNING("It is not your turn.");
            return;
        }
        if (Protocol.isValidMove(game.getBoard(), x, y, client.getColor())) {
            game.addStoneToBoard(x, y, client.getColor());
            game.broadcast(Protocol.VALID + " " + booleanToColor(client.getColor()) + " " + x + " " + y);
            game.resetPass();
            game.alternateTurn();
            game.saveGameState();
        } else {
            String reason;
            if (!Protocol.isOnBoard(game.getBoard(), x, y)) {
                reason = "This field is not on the board.";
            } else if (!Protocol.isEmptyField(game.getBoard(), x, y)) {
                reason = "There is already a stone placed at this field.";
            } else {
                reason = "This move results in KO.";
            }
            game.broadcast(Protocol.INVALID + " " + booleanToColor(client.getColor()) + " " + reason);
            List<Integer> score = game.getScore();
            game.broadcast(Protocol.END + " " + score.get(0) + " " + score.get(1));
            game.endGame();
        }
    }

    private void checkUse(ClientHandler client) throws InvalidCommandException {
        if (client.getStatus() != CHState.INGAME) {
            throw new InvalidCommandException("You may not use the " + Protocol.MOVE + " command at this moment.");
        }
    }

    private String booleanToColor(boolean white) {
        if (white) {
            return "white";
        } else {
            return "black";
        }
    }

}
