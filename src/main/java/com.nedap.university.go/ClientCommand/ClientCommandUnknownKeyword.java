package com.nedap.university.go.ClientCommand;

import com.nedap.university.go.communication.Client;

import java.util.List;

/**
 * Created by claudia.reuvers on 20/02/2017.
 */
public class ClientCommandUnknownKeyword implements ClientCommand {

    String incomming;

    public ClientCommandUnknownKeyword(String line) {
        incomming = line;
    }

    @Override
    public void execute(Client client) {
        client.print("Keyword is not recognized. " + incomming);
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
