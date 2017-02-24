package com.nedap.university.go.newCommunication;

/**
 * Created by claudia.reuvers on 22/02/2017.
 */
public interface Command {

    void execute(newClientHandler client) throws InvalidCommandException;

}
