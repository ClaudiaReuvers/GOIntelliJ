package com.nedap.university.go.newCommunication;

/**
 * Created by claudia.reuvers on 22/02/2017.
 */
public class CommandPLAYER implements Command {

    private String[] args;

    public CommandPLAYER(String line) {
        args = line.split(" ");
    }

    @Override
    public void execute(newClientHandler client) throws InvalidCommandException{
        checkUse(client);
        checkArguments();

        //TODO: check if valid command
        client.setStatus(CHState.GOTNAME);
    }

    private void checkUse(newClientHandler client) throws InvalidCommandException {
        if (client.getStatus() != CHState.LOGGEDIN) {
            throw new InvalidCommandException("You may not use this command at this moment.");
        }
    }

    private void checkArguments() throws InvalidCommandException {
        Protocol.checkArguments(args, 2);
        Protocol.checkName(args[1]);
    }
}
