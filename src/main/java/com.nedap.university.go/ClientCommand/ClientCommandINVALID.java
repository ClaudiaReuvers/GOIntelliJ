package com.nedap.university.go.ClientCommand;

import com.nedap.university.go.communication.Client;

import java.util.List;

/**
 * Created by claudia.reuvers on 20/02/2017.
 */
public class ClientCommandINVALID implements ClientCommand {

    String incomming;

    public ClientCommandINVALID(String line) {
        incomming = line;
    }

    @Override
    public void execute(Client client) {
        String args[] = incomming.split(" ", 3);
        boolean color = colorToBoolean(args[1]);
        if (color == client.getColor()) {
            client.print("You have made an invalid move.");
        } else {
            client.print("Your opponent has made an invalid move.");
        }
        client.print("Reason: " + args[2]);
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
