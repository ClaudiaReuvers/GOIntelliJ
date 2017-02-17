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
//                server.broadcast(txt);
                String[] words = line.split(" ");
                String keyword = words[0];
                switch(keyword) {
                    case PLAYER :
                        if (name.equals("")) {
                            Command player = new CommandPLAYER(line);
                            player.execute(server, this);
//                            if (checkLength(words, 2)) {
//                                keywordPlayer(words);
//                            } else {
//                                sendMessage("WARNING Not a valid commando, to log onto the server use: PLAYER <name>");
//                                //TODO: not valid commando
//                            }
//                        } else {
//                            sendMessage("Player name is already set.");
                        } else {
                            sendMessage(WARNING + " Player name already set");
                        }
                        break;
                    case GO :
                        if (size == 0) {
//                            if (checkLength(words, 2)) {
//                                keywordGO(words);
//                            } else {
//                                sendMessage("Not a valid commando, to add your boardsize use: GO <size>");
//                                //TODO: not valid commando
//                            }
                            Command GO = new CommandGO(line);
                            GO.execute(server, this);
                        } else {
                            sendMessage(WARNING + " Already put in a waiting list for a game of GO on boardsize " + size);
                        }
                        break;
                    case CANCEL :
                        Command cancel = new CommandCANCEL(line);
                        cancel.execute(server, this);
//                        keywordCancel();
                        break;
                    case MOVE :
//                        if (checkLength(words, 3)) {
//                            keywordMove(words);
//                        } else {
//                            //TODO: not valid commando
//                        }
                        Command move = new CommandMOVE(line);
                        move.execute(server, this);
                        break;
                    case PASS :
                        keywordPass();
                        break;
                    case TABLEFLIP :
                        keywordTableflip();
                        break;
                    case EXIT :
                        keywordExit();
                        break;
                    case CHAT :
                        keywordChat(line);
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

    private boolean checkLength(String[] line, int length) {
        return (line.length == length);
        //TODO: add exception if length is incorrect
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

//    private void keywordPlayer(String[] line) {
//        String name = line[1];
//        if (server.checkName(name)) {
////            sendMessage("Add player");
//            setClientName(name);
////            server.addToClientHandlerList(this);
//            server.broadcastToAll("[Player " + name + " has entered]");
//            sendMessage("Current players:");
//            sendMessage(server.getClientList());
//        } else {
//            sendMessage("Not a valid commando: the name must consist only of lowercase letters");
//            //TODO: commando not valid (exception?)
//        }
//    }

    private void startGame(ClientHandler client1, ClientHandler client2, int size) {
        this.game = new Game(client1, client2, size);
        server.addToGamesList(game);
        client1.sendMessage(READY + " black " + client2.getClientName() + size);
        client2.sendMessage(READY + " white " + client1.getClientName() + size);
    }

//    private void keywordGO(String[] line) {
//        //Check if the second is a integer
//        int size = 0;
//        try {
//            size = Integer.parseInt(line[1]);
//            //Set size for this player
//            if (server.checkSize(size)) {
//                setSize(size);
//                if (server.isMatch(size)) {
//                    server.removeFromWaitingList(size);
//                    ClientHandler opponent = server.getMatch(size);
//                    sendMessage("You start a game with " + opponent.getClientName());
//                    opponent.sendMessage("You start a game with " + this.getClientName());
//                    startGame(this, opponent, size);
//
//
//                } else {
//                    server.addToWaitingList(size, this);
//                    sendMessage(WAITING);
//                }
//            } else {
//                //TODO
//                sendMessage("Not a valid size: use an uneven integer between 5 and 131");
//            }
//
//        } catch (NumberFormatException e) {
//            //TODO
//            sendMessage("Not a valid commando, use an uneven integer between 5 an 131");
//        }
//
//    }

    private void keywordCancel() {
        server.removeFromClientHandlerList(this);
        //TODO: close connection properly
    }

    private void keywordMove(String[] line) {
        int x; int y;
        try {
            x = Integer.parseInt(line[1]);
            y = Integer.parseInt(line[2]);
            for (ClientHandler clients : game.getClients()) {
                clients.sendMessage("MOVE " + x + " " + y);
            }
        } catch (NumberFormatException e) {
            //TODO
        }

        //TODO: check if move is valid
        //TODO: if valid -> VALID + <color> + <x> + <y> to both players
        //TODO: if invalid -> INVALID + <color> + <msg> to both players
    }

    private void keywordPass() {
        //TODO: send PASSED + <color> to both players
    }

    private void keywordTableflip() {
        //TODO: send TABLEFLIPPED + <color> to both players
    }

    private void keywordExit() {
        server.removeFromClientHandlerList(this);
        //TODO: exit
    }

    private void keywordChat(String line) {
        String msg = line.substring(line.indexOf(' ') + 1);
        //TODO: send to all players in Game
    }
}
