package com.nedap.university.go.communication;

/**
 * Created by claudia.reuvers on 22/02/2017.
 */
public interface Command {

    void execute(ClientHandler client) throws InvalidCommandException;

}
