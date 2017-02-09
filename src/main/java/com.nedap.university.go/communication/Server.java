package com.nedap.university.go.communication;

import com.nedap.university.go.game.Game;

import java.io.CharArrayReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Server {

    private static final String USAGE
            = "usage: " + Server.class.getName() + " <port>";

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println(USAGE);
            System.exit(0);
        }
        Server server = new Server(Integer.parseInt(args[0]));
        server.run();
    }

    private int port;
    private List<ClientHandler> listClientHandlers;
    private List<Game> listGames;
    private Map<Integer, ClientHandler> listWaiting;

    public Server(int port) {
        this.port = port;
        listClientHandlers = new LinkedList<>();
        listGames = new LinkedList<>();
        listWaiting = new HashMap<>();
    }

    public void addToClientHandlerList(ClientHandler client) {
        listClientHandlers.add(client);
    }

    public void removeFromClientHandlerList(ClientHandler client) {
        listClientHandlers.remove(client);
    }

    public List<ClientHandler> getClientList() {
        return listClientHandlers;
    }

    public void addToWaitingList(int size, ClientHandler client) {
        listWaiting.put(size, client);
    }

    public void removeFromWaitingList(int size) {
        listWaiting.remove(size);
    }

    public boolean isMatch(int size) {
        return listWaiting.containsKey(size);
    }

    public ClientHandler getMatch(int size) {
        ClientHandler client = listWaiting.get(size);
        removeFromWaitingList(size);
        return client;
    }

    public void addToGamesList(Game game) {
        listGames.add(game);
    }

    public void removeFromGamesList(Game game) {
        listGames.remove(game);
    }

    public void run() {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Could not create a ServerSocket");
        }

        while(true) {
            try {
                Socket sock = ss.accept();
                ClientHandler client = new ClientHandler(this, sock);
                addToClientHandlerList(client);
                client.start();
            } catch(IOException e) {

            }

        }

    }

    public boolean checkName(String name) {
        if (name.length() >= 20) {
            return false;
        }
        for (char c : name.toCharArray()) {
            if (!Character.isLetter(c) || Character.isLowerCase(c)) {
                return false;
            }
        }
        return true;
    }

    public boolean checkSize(int size) {
        return (size >= 5 && size <= 131 && size % 2 == 1);
    }

    public void broadcastToAll(String msg) {
        for (ClientHandler clients : listClientHandlers) {
            clients.sendMessage(msg);
        }
    }

    public void broadcastToGame(Game game, String msg) {
        for (ClientHandler clients : game.getClients()) {
            clients.sendMessage(msg);
        }
    }

//    public boolean checkRules(Stone[] fields, int x, int y) {
//        //TODO: implement checking of rules: 1) is (x,y) on the board?; 2) is there not already a Stone at that location; 3) does it result in KO?
//    }
}
