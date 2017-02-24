package com.nedap.university.go.newCommunication;

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
    private List<Game> listGames;
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
        //TODO: add check for double names
    }

    public void removeFromClientHandlerList(newClientHandler client) {
        listClientHandlers.remove(client);
        log(client.getClientName() + " removed from CH list");
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
//                addToClientHandlerList(client);
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
}
