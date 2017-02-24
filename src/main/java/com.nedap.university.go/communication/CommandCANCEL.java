package com.nedap.university.go.communication;

/**
 * Created by claudia.reuvers on 24/02/2017.
 */
public class CommandCANCEL implements Command {

    private String[] args;

    public CommandCANCEL(String line) {
        args= line.split(" ");
    }


    @Override
    public void execute(ClientHandler client) throws InvalidCommandException {
        checkUse(client);
        checkArguments();
        client.getServer().removeFromWaitingList(client.getClientSize());
        client.getServer().removeFromClientHandlerList(client);
        client.sendMessage("CHAT Bye!");
        client.getServer().log(client.getClientName() + " canceled his/her log in.");
    }

    private void checkArguments() throws InvalidCommandException {
        Protocol.checkArguments(args, "CANCEL");
    }

    private void checkUse(ClientHandler client) throws InvalidCommandException {
        if (client.getStatus() == CHState.INGAME) {
            throw new InvalidCommandException("You may not use the MOVE command at this moment.");
        }
    }
}
