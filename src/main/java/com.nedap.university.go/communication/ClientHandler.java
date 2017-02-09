package com.nedap.university.go.communication;

import java.io.*;
import java.net.Socket;

/**
 * Created by claudia.reuvers on 07/02/2017.
 */
public class ClientHandler extends Thread {
    private Server server;
    private BufferedReader in;
    private BufferedWriter out;
    private String name;
    private int size;

    private static final String PLAYER = "PLAYER";
    private static final String GO = "GO";
    private static final String WAITING = "WAITING";
    private static final String CANCEL = "CANCEL";
    private static final String MOVE = "MOVE";
    private static final String INVALID = "INVALID";
    private static final String TABLEFLIP = "TABLEFLIP";
    private static final String TABLEFLIPPED = "TABLEFLIPPED";
    private static final String PASS = "PASS";
    private static final String PASSED = "PASSED";
    private static final String EXIT = "EXIT";
    private static final String CHAT = "CHAT";

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
            String txt = in.readLine();
            while (txt != null) {
//                server.broadcast(txt);
                String[] words = txt.split(" ");
                String keyword = words[0];
                switch(keyword) {
                    case PLAYER :
                        if (checkLength(words, 2)) {
                            keywordPlayer(words);
                        } else {
                            //TODO: not valid commando
                        }
                        break;
                    case GO :
                        if (checkLength(words, 2)) {
                            keywordGO(words);
                        } else {
                            //TODO: not valid commando
                        }
                        break;
                    case CANCEL :
                        keywordCancel();
                        break;
                    case MOVE :
                        if (checkLength(words, 3)) {
                            keywordMove(words);
                        } else {
                            //TODO: not valid commando
                        }
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
                        keywordChat(txt);
                        break;
                    default :
                        //TODO: invalid commando; show help menu?
                }
                txt = in.readLine();
            }
        } catch (IOException e) {
                //TODO
        }
    }

    private boolean checkLength(String[] line, int length) {
        return (line.length == length);
        //TODO: add exception if length is incorrect
    }

    private void setSize(int size) {
        this.size = size;
    }

    private void setClientName(String name) {
        this.name = name;
    }

    private void keywordPlayer(String[] line) {
        String name = line[1];
        if (server.checkName(name)) {
            setClientName(name);
            server.broadcastToAll("[Player " + name + " has entered]");
        } else {
            //TODO: commando not valid (exception?)
        }
    }

    private void keywordGO(String[] line) {
        //Check if the second is a integer
        int size = 0;
        try {
            size = Integer.parseInt(line[1]);
        } catch (NumberFormatException e) {
            //TODO
        }
        //Set size for this player
        if (server.checkSize(size)) {
            setSize(size);
        }
        //TODO: check if this player has a match
//        if (isMatch()) {
//            //TODO: pair two players in a Game + send back READY + <color> + <opponentName> + <size>
//        } else {
//            //TODO: add player to waitinglist + send back: WAITING
//        }
    }

    private void keywordCancel() {
        server.removeFromClientHandlerList(this);
        //TODO: close connection properly
    }

    private void keywordMove(String[] line) {
        int x; int y;
        try {
            x = Integer.parseInt(line[1]);
            y = Integer.parseInt(line[2]);
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
        //TODO: exit
    }

    private void keywordChat(String line) {
        String msg = line.substring(line.indexOf(' ') + 1);
        //TODO: send to all players in Game
    }
}
