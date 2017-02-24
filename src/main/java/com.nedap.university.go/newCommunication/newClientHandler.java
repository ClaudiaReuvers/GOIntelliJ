package com.nedap.university.go.newCommunication;

import com.nedap.university.go.game.Game;

import java.io.*;
import java.net.Socket;

/**
 * Created by claudia.reuvers on 22/02/2017.
 */
public class newClientHandler extends Thread {

    private newServer server;
    private Socket sock;
    private BufferedReader in;
    private BufferedWriter out;
    private CHState status;
    private String clientName = "";
    private int size = -1;
    private tempGame game;
    private newClientHandler opponent;
    private boolean color;

    private static final String PLAYER = "PLAYER";
    private static final String GO = "GO";
    private static final String WAITING = "WAITING";
    private static final String READY = "READY";
    private static final String CANCEL = "CANCEL";
    private static final String MOVE = "MOVE";
    private static final String INVALID = "INVALID";
    private static final String TABLEFLIP = "TABLEFLIP";
    private static final String TABLEFLIPPED = "TABLEFLIPPED";
    private static final String PASS = "PASS";
    private static final String PASSED = "PASSED";
    private static final String EXIT = "EXIT";
    private static final String CHAT = "CHAT";
    private static final String WARNING = "WARNING";

    public newClientHandler(newServer server, Socket sock) throws IOException {
        this.server = server;
        this.sock = sock;
        this.in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
        status = CHState.LOGGEDIN;
    }

    public void sendMessage(String msg) {
        try {
            out.write(msg);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            System.out.println("IOException at sendMessage");
            //TODO
        }
    }

    public void run() {
//        while(sock.isConnected()) {
        readCommand();
//        }
//        server.log("Client " + name + " logged out");
    }

    private void readCommand() {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                String[] words = line.split(" ");
                String keyword = words[0];
                Command command = null;
                switch(keyword) {
                    case PLAYER :
                        command = new CommandPLAYER(line);
                        break;
                    case GO :
                        command = new CommandGO(line);
                        break;
                    case CANCEL :
                        command = new CommandCANCEL(line);
                        break;
                    case MOVE :
                        command = new CommandMOVE(line);
                        break;
                    case PASS :
                        command = new CommandPASS(line);
                        break;
                    case TABLEFLIP :
                        command = new CommandTABLEFLIP(line);
                        break;
                    case CHAT :
                        command = new CommandCHAT(line);
                        break;
                    default :
                        command = new CommandGO(line);
                        sendWARNING("Not a valid commando");
                        break;
                        //TODO: invalid commando; show help menu?
                }
                try {
                    command.execute(this);
                } catch (InvalidCommandException e) {
                    sendWARNING(e.getMessage());
                }
            }
            shutdown();

        } catch (IOException e) {
            System.out.println("IOException at run of CH");
            //TODO
        }
    }

    public void sendWARNING(String msg) {
        sendMessage(WARNING + " " + msg);
    }

    private void shutdown() {
        server.log("Client " + clientName + ": connection lost...");
        server.removeFromClientHandlerList(this);
//        server.removeFromPreGameList(this);
        server.removeFromWaitingList(size);
        try {
            out.flush();
            out.close();
            in.close();
            sock.close();
        } catch (IOException e) {
        }
        server.log("Client " + clientName + " removed.");
    }

    public newServer getServer() {
        return server;
    }

    public CHState getStatus() {
        return status;
    }

    public void setStatus(CHState status) {
        this.status = status;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientSize(int size) {
        this.size = size;
    }

    public int getClientSize() {
        return size;
    }

    public void setOpponent(newClientHandler opponent) {
        this.opponent = opponent;
    }

    public newClientHandler getOpponent() {
        return opponent;
    }

    public void setColor(boolean color) {
        this.color = color;
    }

    public boolean getColor() {
        return color;
    }

    public void setGame(newClientHandler opponent, boolean white, tempGame game) {
        setOpponent(opponent);
        setColor(white);
        this.game = game;
        this.status = CHState.INGAME;
    }

    public tempGame getGame() {
        return game;
    }

}
