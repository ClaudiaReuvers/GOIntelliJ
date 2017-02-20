package com.nedap.university.go.communication;

import java.util.List;

/**
 * Created by claudia.reuvers on 20/02/2017.
 */
public class ClientCommandWARNING implements ClientCommand {

    String incomming;

    public ClientCommandWARNING(String line) {
        incomming = line;
    }

    @Override
    public void execute(Client client) {
        String args[] = incomming.split(" ", 2);
        client.print("WARNING: " + args[1]);
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
