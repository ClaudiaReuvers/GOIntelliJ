package com.nedap.university.go.communication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by claudia.reuvers on 13/02/2017.
 */
public class CommandPLAYER implements Command {

    private String incomming;
    private String name;

    public CommandPLAYER(String line) {
        incomming = line;
    }

    @Override
    public void execute(Server server, ClientHandler client) {
        //Check if already in clientHandler list
        if (server.checkClientHandlerInList(client)) {
            client.sendMessage("WARNING You are already logged in with the name " + client.getClientName());
            //TODO: throw AlreadySetException
        }
        //Check arguments: are the arguments valid, is the name within requirements, is the name not already used
        if (!checkArguments(server)) {
            client.sendMessage("WARNING The name does not meet the requirements, must use a maximum of 20 lowercase letters");
            client.sendMessage("WARNING there already is a player with this username, please try another name");
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
           if (server.checkName(args[1])) {
               if (!server.checkNameInList(args[1])) {
                   //TODO: throw NameAlreadyUsedException
                   return false;
               } else {
                   name = args[1];
                   return true;
               }
           } else {
               //TODO: throw NonValidNameException
               return false;
           }
       } else {
           //TODO: throw WrongNrOfArgumentsException
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
