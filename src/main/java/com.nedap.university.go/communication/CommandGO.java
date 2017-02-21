package com.nedap.university.go.communication;

import com.nedap.university.go.game.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by claudia.reuvers on 13/02/2017.
 */
public class CommandGO implements Command{

    private String incomming;
    private int size;
    private ClientHandler client;

    public CommandGO(String line) {
        incomming = line;
    }

    @Override
    public void execute(Server server, ClientHandler client) {
        this.client = client;
        if (!checkArguments(server)) {
            client.sendMessage("WARNING Wrong arguments"); //TODO: adapt
            return;
        }
        client.setSize(size);
        if (server.isMatch(size)) {
            ClientHandler opponent = server.getMatch(size);
//            client.sendMessage("READY black " + opponent.getClientName() + " " + size);
//            opponent.sendMessage("READY white " + client.getClientName() + " " + size);
            server.setGame(opponent, client, size);
            server.log("Player " + opponent.getClientName() + " and " + client.getClientName() + " start a game at boardsize " + size + ".");
        } else {
            server.addToWaitingList(size, client);
            client.sendMessage("WAITING");
            server.log("Player " + client.getClientName() + " is put on a waitinglist for a game on boardize " + size + ".");
        }

    }

    @Override
    //TODO: add warning for wrong arguments
    public boolean checkArguments(Server server) {
        String args[] = incomming.split(" ");
        if (checkArgumentsLength(args)) {
            try {
                size = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                client.sendMessage("WARNING Not an integer");
                return false;
            }
            return server.checkSize(size);
        } else {
            client.sendMessage("WARNING Wrong length");
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
        args.add(Integer.toString(size));
        return args;
    }

    public void startGame(ClientHandler client1, ClientHandler client2, int size) {
        Game game = new Game(client1, client2, size);
    }


}
