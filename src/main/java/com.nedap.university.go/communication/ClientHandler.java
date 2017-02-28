package com.nedap.university.go.communication;

import java.io.*;
import java.net.Socket;
import java.util.List;

/**
 * Created by claudia.reuvers on 22/02/2017.
 */
public class ClientHandler extends Thread {

    private Server server;
    private Socket sock;
    private BufferedReader in;
    private BufferedWriter out;
    private CHState status;
    private String clientName = "";
    private int size = -1;
    private Game game;
//    private ClientHandler opponent;
    private boolean color;

//    private static final String PLAYER = "PLAYER";
//    private static final String GO = "GO";
//    private static final String WAITING = "WAITING";
//    private static final String READY = "READY";
//    private static final String CANCEL = "CANCEL";
//    private static final String MOVE = "MOVE";
//    private static final String INVALID = "INVALID";
//    private static final String TABLEFLIP = "TABLEFLIP";
//    private static final String TABLEFLIPPED = "TABLEFLIPPED";
//    private static final String PASS = "PASS";
//    private static final String PASSED = "PASSED";
//    private static final String EXIT = "EXIT";
//    private static final String CHAT = "CHAT";
//    private static final String WARNING = "WARNING";

    public ClientHandler(Server server, Socket sock) throws IOException {
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
            System.out.println("IOException at sendMessage of client " + getClientName());
            //TODO: IOException at sendMessage of CH
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
                    case Protocol.PLAYER :
                        command = new CommandPLAYER(line);
                        break;
                    case Protocol.GO :
                        command = new CommandGO(line);
                        break;
                    case Protocol.CANCEL :
                        command = new CommandCANCEL(line);
                        break;
                    case Protocol.MOVE :
                        command = new CommandMOVE(line);
                        break;
                    case Protocol.PASS :
                        command = new CommandPASS(line);
                        break;
                    case Protocol.TABLEFLIP :
                        command = new CommandTABLEFLIP(line);
                        break;
                    case Protocol.CHAT :
                        command = new CommandCHAT(line);
                        break;
                    case Protocol.HINT :
                        command = new CommandHINT(line);
                        break;
                    default :
                        command = new CommandUnknownKeyword();
                        break;
                }
                try {
                    command.execute(this);
                } catch (InvalidCommandException e) {
                    sendWARNING(e.getMessage());
                    sendHelpMenu();
                }
            }
            shutdown();

        } catch (IOException e) {
            System.out.println("IOException at run of CH");
            //TODO: IOException at run of CH
        }
    }

    public void sendWARNING(String msg) {
        sendMessage(Protocol.WARNING + " " + msg);
    }

    private void shutdown() {
        server.log("Client " + clientName + ": connection lost...");
        server.removeFromClientHandlerList(this);
//        server.removeFromPreGameList(this);
        server.removeFromWaitingList(size);
        if (game != null) {
            game.getOpponent(this).sendMessage(Protocol.CHAT + " The connection of your opponent was lost.");
            List<Integer> score = game.getScore();
            game.getOpponent(this).sendMessage(Protocol.END + " " + score.get(0) + " " + score.get(1));
            game.endGame();
        }
        try {
            out.flush();
            out.close();
            in.close();
            sock.close();
        } catch (IOException e) {
        }
        server.log("Client " + clientName + " removed.");
    }

    public Server getServer() {
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

//    public void setOpponent(newClientHandler opponent) {
//        this.opponent = opponent;
//    }

//    public newClientHandler getOpponent() {
//        return opponent;
//    }

    public void setColor(boolean color) {
        this.color = color;
    }

    public boolean getColor() {
        return color;
    }

    public void setGame(ClientHandler opponent, boolean white, Game game) {
//        setOpponent(opponent);
        setColor(white);
        this.game = game;
        this.status = CHState.INGAME;
    }

    public Game getGame() {
        return game;
    }

    private void sendHelpMenu() {
        String helpMenu = Protocol.CHAT + " Possible keywords at this moment:\n";
        String player    = String.format(Protocol.CHAT + " %-9s %-10s sets your name. name must be a String of less than 20 lowercase letters.", Protocol.PLAYER, "<name>");
        String go        = String.format(Protocol.CHAT + " %-9s %-10s sets the board size on which you want to play. size must be an uneven integer between 5 and 131.", Protocol.GO, "<size>");
        String cancel    = String.format(Protocol.CHAT + " %-9s %-10s log out of the server. If you are waiting for a game, you will be removed from the waiting list.", Protocol.CANCEL, "");
        String move      = String.format(Protocol.CHAT + " %-9s %-10s puts a stone on the board if the move is valid. x,y must be an integer.", Protocol.MOVE, "<x> <y>");
        String tableflip = String.format(Protocol.CHAT + " %-9s %-10s give up. Your opponent wins.", Protocol.TABLEFLIP, "");
        String pass      = String.format(Protocol.CHAT + " %-9s %-10s pass, no stone is placed on the board. If you pass after your opponent has passed, the game will be ended.", Protocol.PASS, "");
        String chat      = String.format(Protocol.CHAT + " %-9s %-10s ", Protocol.CHAT, "<message>");
        String hint      = String.format(Protocol.CHAT + " %-9s %-10s gives a suggestion for a next move. Does not yet check if this move results in a KO.", Protocol.HINT, "");
        if (status == CHState.LOGGEDIN) {
            helpMenu += player + "\n" + cancel;
        } else if (status == CHState.GOTNAME) {
            helpMenu += go + "\n" + cancel + "\n" + chat + "is sent to all player currently waiting to play a game.";
        } else if (status == CHState.WAITING) {
            helpMenu += cancel + "\n" + chat + "is sent to all player currently waiting to play a game.";
        } else {
            helpMenu += move + "\n" + pass + "\n" + tableflip + "\n" + hint + "\n" + chat + "is sent to all players in the game.";
        }
        sendMessage(helpMenu);
    }
}
