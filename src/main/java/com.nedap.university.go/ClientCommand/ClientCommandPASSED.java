package com.nedap.university.go.ClientCommand;

import com.nedap.university.go.communication.Client;
import com.nedap.university.go.communication.InvalidCommandException;

import java.util.List;

/**
 * Created by claudia.reuvers on 20/02/2017.
 */
public class ClientCommandPASSED implements ClientCommand{

    String incomming;

    public ClientCommandPASSED(String line) {
        incomming = line;
    }

    @Override
    public void execute(Client client) {
        String[] args = incomming.split(" ");
        boolean color  = colorToBoolean(args[1]);
        if (color == client.getColor()) {
            client.print("You have passed.");
        } else {
            client.print("Your opponent has passed.");
        }
        if (client.isComputer() && color != client.getColor()) {
            String move = client.determineMove();
            try {
                client.sendMessage(move);
            } catch (InvalidCommandException e) {
                client.print("The computer failed. Choose your own move and beat the computer.");
            }
        }
    }

    private boolean colorToBoolean(String color) {
        if (color.equals("white")) {
            return true;
        } else {
            return false;
        }
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
