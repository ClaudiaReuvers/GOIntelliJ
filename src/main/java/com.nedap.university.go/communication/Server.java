package com.nedap.university.go.communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by claudia.reuvers on 22/02/2017.
 */
public class Server {

    private static final String USAGE
            = "usage: " + Server.class.getName() + " <port>";

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println(USAGE);
            System.exit(0);
        }
        Server server = new Server(Integer.parseInt(args[0]));
        InetAddress ip =  null;
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.getMessage();
        }
        server.log("Server was successfully started at IP address " + ip.getHostAddress() + " on port " + args[0] + ".\nReady to accept clients.");
        server.run();
    }

    private int port;
    private List<ClientHandler> listClientHandlers;
//    private List<Game> listGames;
    private List<ClientHandler> listPreGame;
    private Map<Integer, ClientHandler> listWaiting;

    public Server(int port) {
        this.port = port;
        listClientHandlers = new LinkedList<>();
        listPreGame = new LinkedList<>();
//        listGames = new LinkedList<>();
        listWaiting = new HashMap<>();
    }

    public void addToClientHandlerList(ClientHandler client) {
        listClientHandlers.add(client);
    }

    public void removeFromClientHandlerList(ClientHandler client) {
        listClientHandlers.remove(client);
        log(client.getClientName() + " removed from CH list");
    }

    public void addToWaitingList(int size, ClientHandler client) {
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
                ClientHandler client = new ClientHandler(this, sock);
                addToClientHandlerList(client);
                client.start();
                client.sendMessage(Protocol.CHAT + " Welcome!\n" + Protocol.CHAT + " Type " + Protocol.PLAYER + " <name> to set your name.");
                log("A client has logged in.");
            } catch(IOException e) {
                System.out.println("IOException at run from server");
                //TODO: IOException at run of the server
            }
        }
        broadcastToAll("Server is down.");
        log("Server stopped...=(");
    }

    public void log(String msg) {
        System.out.println(msg);
    }

    public void broadcastToAll(String msg) {
        for (ClientHandler clients : listClientHandlers) {
            clients.sendMessage(msg);
        }
    }

    public boolean isMatch(int size) {
        return listWaiting.containsKey(size);
    }

    public ClientHandler getMatch(int size) {
        ClientHandler client = listWaiting.get(size);
        removeFromWaitingList(size);
        return client;
    }

    public void setGame(ClientHandler CH1, ClientHandler CH2, int size) {
        Game game = new Game(CH1, CH2, size);
        CH1.setGame(CH2, false, game);
        CH2.setGame(CH1, true, game);
        CH1.sendMessage(Protocol.READY + " black " + CH2.getClientName() + " " + size);
        CH2.sendMessage(Protocol.READY + " white " + CH1.getClientName() + " " + size);
//        addToGamesList(game);
        log(CH1.getClientName() + " and " + CH2.getClientName() + " start a game at boardsize " + size + ".");
    }

//    public void addToGamesList(Game game) {
//        listGames.add(game);
//    }

    public void broadcastToWaiting(String msg) {
        for (ClientHandler clients : listClientHandlers) {
            if (clients.getStatus() != CHState.INGAME) {
                clients.sendMessage(msg);
            }
        }
    }

    public boolean containsName(String name) {
        for (ClientHandler clients : listClientHandlers) {
            if (clients.getClientName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
