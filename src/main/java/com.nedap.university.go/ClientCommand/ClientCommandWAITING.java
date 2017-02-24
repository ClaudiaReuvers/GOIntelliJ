package com.nedap.university.go.ClientCommand;

import com.nedap.university.go.communication.Client;

import java.util.List;

/**
 * Created by claudia.reuvers on 20/02/2017.
 */
public class ClientCommandWAITING implements ClientCommand {

    @Override
    public void execute(Client client) {
        client.print("Waiting for an opponent.");
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
