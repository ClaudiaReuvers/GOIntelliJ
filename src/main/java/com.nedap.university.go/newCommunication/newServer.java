package com.nedap.university.go.newCommunication;

import com.nedap.university.go.communication.ClientHandler;
import com.nedap.university.go.game.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by claudia.reuvers on 22/02/2017.
 */
public class newServer {

    private static final String USAGE
            = "usage: " + newServer.class.getName() + " <port>";
    private static final String CHAT = "CHAT";
    private static final String READY = "READY";

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println(USAGE);
            System.exit(0);
        }
        newServer server = new newServer(Integer.parseInt(args[0]));
        server.run();
    }

    private int port;
    private List<newClientHandler> listClientHandlers;
    private List<tempGame> listGames;
    private List<newClientHandler> listPreGame;
    private Map<Integer, newClientHandler> listWaiting;

    public newServer(int port) {
        this.port = port;
        listClientHandlers = new LinkedList<>();
        listPreGame = new LinkedList<>();
        listGames = new LinkedList<>();
        listWaiting = new HashMap<>();
    }

    public void addToClientHandlerList(newClientHandler client) {
        listClientHandlers.add(client);
    }

    public void removeFromClientHandlerList(newClientHandler client) {
        listClientHandlers.remove(client);
        log(client.getClientName() + " removed from CH list");
    }

    public void addToWaitingList(int size, newClientHandler client) {
        listWaiting.put(size, client);
        log(client.getClientName() + " add to the waiting list for size " + size + ".");
    }

    public void removeFromWaitingList(int size) {
        listWaiting.remove(size);
    }

    public void run() {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Could not create a ServerSocket");
        }

        while(!ss.isClosed()) {
            try {
                Socket sock = ss.accept();
                newClientHandler client = new newClientHandler(this, sock);
                addToClientHandlerList(client);
                client.start();
                log("A client has logged in.");
            } catch(IOException e) {
                System.out.println("IOException at run from server");

            }
        }
        broadcastToAll("Server is down.");
        log("Server stopped...=(");
    }

    public void log(String msg) {
        System.out.println(msg);
    }

    public void broadcastToAll(String msg) {
        for (newClientHandler clients : listClientHandlers) {
            clients.sendMessage(msg);
        }
    }

    public boolean isMatch(int size) {
        return listWaiting.containsKey(size);
    }

    public newClientHandler getMatch(int size) {
        newClientHandler client = listWaiting.get(size);
        removeFromWaitingList(size);
        return client;
    }

    public void setGame(newClientHandler CH1, newClientHandler CH2, int size) {
        tempGame game = new tempGame(CH1, CH2, size);
        CH1.setGame(CH2, false, game);
        CH2.setGame(CH1, true, game);
        CH1.sendMessage(READY + " black " + CH2.getClientName() + " " + size);
        CH2.sendMessage(READY + " white " + CH1.getClientName() + " " + size);
        addToGamesList(game);
        log(CH1.getClientName() + " and " + CH2.getClientName() + " start a game at boardsize " + size + ".");
    }

    public void addToGamesList(tempGame game) {
        listGames.add(game);
    }



}
