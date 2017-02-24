package com.nedap.university.go.newCommunication;

/**
 * Created by claudia.reuvers on 24/02/2017.
 */
public class CommandUnknownKeyword implements Command {

    public CommandUnknownKeyword() {}


    @Override
    public void execute(newClientHandler client) throws InvalidCommandException {
        throw new InvalidCommandException("Unknown keyword.");
    }
}
