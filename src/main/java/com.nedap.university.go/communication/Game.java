package com.nedap.university.go.communication;

import com.nedap.university.go.game.Board;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by claudia.reuvers on 24/02/2017.
 */
public class Game {

    private ClientHandler client1;
    private ClientHandler client2;
    private List<ClientHandler> listClients = new LinkedList<>();
    private Board board;
    private boolean turn; //true: turn of white; false; turn of black
    private boolean pass;
    private List<String> previousBoards;

    //Constructor
    public Game(ClientHandler client1, ClientHandler client2, int dimension) {
        this.client1 = client1;
        this.client2 = client2;
        listClients.add(client1);
        listClients.add(client2);
        board = new Board(dimension);
        this.turn = false;
        this.pass = false;
        previousBoards = new LinkedList<>();
    }

    //Methods
    public void broadcast(String msg) {
        for (ClientHandler clients : listClients) {
            clients.sendMessage(msg);
        }
    }

    public void alternateTurn() {
        turn = !turn;
    }

    public void addStoneToBoard(int x, int y, boolean white) {
        board.addStone(x, y ,white);
    }

    public void setPass() {
        pass = true;
    }

    public void resetPass() {
        pass = false;
    }

    public void endGame() {
        board.clearBoard();
        for (ClientHandler clients : listClients) {
            clients.setStatus(CHState.GOTNAME);
        }
        client1.getServer().log("The game between " + client1.getClientName() + " and " + client2.getClientName() + " has ended.");
        broadcast(Protocol.CHAT + " You can be put back on the waitinglist by using the command GO <size>.");
    }

    //Queries
    public Board getBoard() {
        return board;
    }

    public String toString() {
        return board.toString();
    }

    public boolean isTurn(boolean white) {
        return (turn == white);
    }

    public boolean isPassed() {
        return pass;
    }

    public List<Integer> getScore() {
        return board.getScore();
    }

    public boolean saveGameState() {
        return previousBoards.add(toString());
    }

    public ClientHandler getOpponent(ClientHandler client) {
        if (client1 == client) {
            return client2;
        } else if (client2 == client) {
            return client1;
        } else {
            return null;
        }
    }
}