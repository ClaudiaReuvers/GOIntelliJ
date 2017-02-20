package com.nedap.university.go.ClientCommand;

import com.nedap.university.go.communication.Client;

import java.util.List;

/**
 * Created by claudia.reuvers on 20/02/2017.
 */
public class ClientCommandEND implements ClientCommand {

    String incomming;

    public ClientCommandEND(String line) {
        incomming = line;
    }

    @Override
    public void execute(Client client) {
        String[] args = incomming.split(" ");
        int ownScore;
        int opponentScore;
        if (client.getColor()) {
            ownScore = Integer.parseInt(args[2]);
            opponentScore = Integer.parseInt(args[1]);
        } else {
            ownScore = Integer.parseInt(args[1]);
            opponentScore = Integer.parseInt(args[2]);
        }
        if (ownScore == opponentScore) {
            if (ownScore == -1) {
                client.print("There is not a score (-1 vs -1) as a result of a tableflip.");
            } else {
                client.print("There is a tie: " + ownScore + " vs " + opponentScore);
            }
        } else if (ownScore > opponentScore) {
            client.print("You have won!! (" + ownScore + " vs " + opponentScore + ").");
        } else {
            client.print("You have lost. (" + ownScore + " vs " + opponentScore + ").");
        }
        //TODO: end game
    }

    @Override
    public boolean checkArguments() {
        return false;
    }

    @Override
    public List<String> getArguments() {
        return null;
    }
}
