package com.nedap.university.go.communication;

import com.nedap.university.go.game.Game;

import java.io.*;
import java.net.Socket;
import java.util.List;

/**
 * Created by claudia.reuvers on 07/02/2017.
 */
public class ClientHandler extends Thread {
    private Server server;
    private BufferedReader in;
    private BufferedWriter out;
    private String name = "";
    private int size;
    private Game game;
    private ClientHandler opponent;
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

    public ClientHandler(Server server, Socket sock) throws IOException {
        this.server = server;
        this.in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
    }

    public void sendMessage(String msg) {
        try {
            out.write(msg);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            //TODO
        }
    }

    public void run() {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                String[] words = line.split(" ");
                String keyword = words[0];
                switch(keyword) {
                    case PLAYER :
                        if (name.equals("")) {
                            Command player = new CommandPLAYER(line);
                            player.execute(server, this);
                        } else {
                            sendMessage(WARNING + " Player name already set to " + name);
                        }
                        break;
                    case GO :
                        if (size == 0) {
                            Command GO = new CommandGO(line);
                            GO.execute(server, this);
                        } else {
                            sendMessage(WARNING + " Already put in a waiting list for a game of GO on boardsize " + size);
                        }
                        break;
                    case CANCEL :
                        Command cancel = new CommandCANCEL(line);
                        cancel.execute(server, this);
                        break;
                    case MOVE :
                        Command move = new CommandMOVE(line);
                        move.execute(server, this);
                        break;
                    case PASS :
                        Command pass = new CommandPASS(line);
                        pass.execute(server, this);
                        break;
                    case TABLEFLIP :
                        Command tableflip = new CommandTABLEFLIP(line);
                        tableflip.execute(server, this);
                        break;
                    case EXIT :
                        keywordExit();
                        break;
                    case CHAT :
                        Command chat = new CommandCHAT(line);
                        chat.execute(server, this);
                        break;
                    default :
                        sendMessage("Not a valid commando");
                        //TODO: invalid commando; show help menu?
                }
            }
        } catch (IOException e) {
                //TODO
        }
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setClientName(String name) {
        this.name = name;
    }

    public String getClientName() {
        return name;
    }

    public int getClientSize() {
        return size;
    }

    public void setColor(boolean white) {
        color = white;
    }

    public boolean getColor() {
        return color;
    }

    public void setOpponent(ClientHandler opponent) {
        this.opponent = opponent;
    }

    public ClientHandler getOpponent() {
        return opponent;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    private void keywordExit() {
        server.removeFromClientHandlerList(this);
        //TODO: exit
    }
}
