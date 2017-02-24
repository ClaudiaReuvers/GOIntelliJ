package com.nedap.university.go.newCommunication;

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
    public void execute(newClientHandler client) throws InvalidCommandException {
        checkUse(client);
        Protocol.checkArguments(args, "MOVE");
        int x = Integer.parseInt(args[1]); // should work since it is checked in checkArguments
        int y = Integer.parseInt(args[2]);
        tempGame game = client.getGame();
        if (!game.isTurn(client.getColor())) {
            client.sendWARNING("It is not your turn.");
            return;
        }
        if (Protocol.isValidMove(game, x, y, client.getColor())) {
            game.addStoneToBoard(x, y, client.getColor());
            game.broadcast("VALID " + booleanToColor(client.getColor()) + " " + x + " " + y);
            game.resetPass();
            game.alternateTurn();
            game.saveGameState();
        } else {
            String reason;
            if (!Protocol.isOnBoard(game, x, y)) {
                reason = "This field is not on the board.";
            } else if (!Protocol.isEmptyField(game, x, y)) {
                reason = "There is already a stone placed at this field.";
            } else {
                reason = "This move results in KO.";
            }
            game.broadcast("INVALID " + booleanToColor(client.getColor()) + " " + reason);
            List<Integer> score = game.endGame();
            game.broadcast("END " + score.get(0) + " " + score.get(1));
        }

    }

    private void checkUse(newClientHandler client) throws InvalidCommandException {
        if (client.getStatus() != CHState.INGAME) {
            throw new InvalidCommandException("You may not use the MOVE command at this moment.");
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
