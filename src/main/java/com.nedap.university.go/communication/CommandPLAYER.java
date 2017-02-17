package com.nedap.university.go.communication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by claudia.reuvers on 13/02/2017.
 */
public class CommandPLAYER implements Command {

    //private Server server;
    private String incomming;
    private String name;

    public CommandPLAYER(String line) {
//        this.server = server;
        incomming = line;
    }

    @Override
    public void execute(Server server, ClientHandler client) {
        if (!checkArguments(server)) {
            client.sendMessage("WARNING The name does not meet the requirements, must use a maximum of 20 lowercase letters");
            //TODO: adapt checkName: s.t. checks if already in list (now only checks if length <= 20 & lowercase)
            //TODO: sendMessage(WARNING there already is a player with this username, please try another name)
            return;
        }
        client.setClientName(name);
        server.broadcastToAll("CHAT [Player " + name + " has entered]");
        client.sendMessage("CHAT Current players:\n" + server.getClientList());
    }

    @Override
    public boolean checkArguments(Server server) {
//        String[] args = incomming.split(" ");
//        if (args.length != 2) {
//            //TODO: throw WrongNrOfArgumentsException
//        } else {
//            name = args[1];
//        }
        String args[] = incomming.split(" ");
       if (checkArgumentsLength(args)) {
           name = args[1];
           return (server.checkName(name));
       } else {
           return false;
       }
    }

    private boolean checkArgumentsLength(String[] args) {
        if (args.length != 2) {
            //TODO: throw WrongNrOfArgumentsException
            return false;
        }
        return true;
    }

    @Override
    public List<String> getArguments() {
        List<String> args = new ArrayList<>();
        args.add(name);
        return args;
    }
}
