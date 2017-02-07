package com.nedap.university.go.communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

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

    public Server(int port) {
        this.port = port;
        listClientHandlers = new LinkedList<>();
    }

    public void addToClientHandlerList(ClientHandler client) {
        listClientHandlers.add(client);
    }

//    public void removeFromClientHandlerList(ClientHandler client) {
//        listClientHandlers.remove(client);
//    }

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

    public void broadcast(String msg) {
        for (ClientHandler clients : listClientHandlers) {
            clients.sendMessage(msg);
        }
    }
}
