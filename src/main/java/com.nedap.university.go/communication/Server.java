package com.nedap.university.go.communication;

import com.nedap.university.go.game.Board;
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
    private static final String CHAT = "CHAT";
    private static final String READY = "READY";

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
        //TODO: add check for double names
    }

    public List<ClientHandler> getCHList() {
        return listClientHandlers;
    }
    public String getClientList() {
        String clientNames = "";
        for (ClientHandler clients : listClientHandlers) {
            clientNames += "\nCHAT " + clients.getClientName();
        }
        return clientNames;
    }

    public void removeFromClientHandlerList(ClientHandler client) {
        listClientHandlers.remove(client);
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
//                addToClientHandlerList(client);
                client.start();
            } catch(IOException e) {

            }
        }
    }

    public boolean checkName(String name) {
        if (name.length() >= 20) {
            return false;
        }
        for(char c : name.toCharArray()) {
            if (!(Character.isLetter(c) && Character.isLowerCase(c))) {
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

    public void setGame(ClientHandler CH1, ClientHandler CH2, int size) {
        CH1.setOpponent(CH2);
        CH2.setOpponent(CH1);
        CH1.setColor(false);
        CH2.setColor(true);
//        CH1.sendMessage(CHAT + " You start a game with " + CH1.getClientName());
//        CH2.sendMessage(CHAT + " You start a game with " + CH2.getClientName());
        CH1.sendMessage(READY + " black " + CH2.getClientName() + " " + size);
        CH2.sendMessage(READY + " white " + CH1.getClientName() + " " + size);
        Game game = new Game(CH1, CH2, size);
        CH1.setGame(game);
        CH2.setGame(game);
        addToGamesList(game);
//        game.start();
    }

    public boolean isOnBoard(Game game, int x, int y) {
        Board board = game.getBoard();
        int size = board.getDimension();
        //Check if this field exists
        if (x >= size || x < 0 || y >= size || y < 0) {
            return false;
        }
        return true;
    }

    public boolean isEmptyField(Game game, int x, int y) {
        Board board = game.getBoard();
        if (!board.getField(x, y).isEmpty()) {
            return false;
        }
        return true;
    }

    public boolean isKo(Game game, int x, int y) {
        //TODO
        return true;
    }

    public boolean isValidMove(Game game, int x, int y) {
        return (isOnBoard(game, x, y) && isEmptyField(game, x, y) && isKo(game, x, y));
    }

    public boolean checkClientHandlerInList(ClientHandler client) {
        return (listClientHandlers.contains(client));
    }

    public boolean checkNameInList(String name) {
        for (ClientHandler clients : listClientHandlers) {
            if (clients.getClientName().equals(name)) {
                return false;
            }
        }
        return true;
    }
}
