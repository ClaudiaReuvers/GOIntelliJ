package com.nedap.university.go.communication;

import java.util.List;

/**
 * Created by claudia.reuvers on 13/02/2017.
 */
public interface Command {

    public void execute(Server server, ClientHandler client);

    boolean checkArguments(Server server);

    public List<String> getArguments();
}
