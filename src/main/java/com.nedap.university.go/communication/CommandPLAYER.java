package com.nedap.university.go.communication;

/**
 * Created by claudia.reuvers on 22/02/2017.
 */
public class CommandPLAYER implements Command {

    private String[] args;

    public CommandPLAYER(String line) {
        args = line.split(" ");
    }

    @Override
    public void execute(ClientHandler client) throws InvalidCommandException{
        checkUse(client);
        Protocol.checkArguments(args, "PLAYER");
        //TODO: check if valid command (double name)
        client.setClientName(args[1]);
        client.setStatus(CHState.GOTNAME);
    }

    private void checkUse(ClientHandler client) throws InvalidCommandException {
        if (client.getStatus() != CHState.LOGGEDIN) {
            throw new InvalidCommandException("You may not use the PLAYER command at this moment.");
        }
    }

}
