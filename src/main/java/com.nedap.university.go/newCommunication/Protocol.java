package com.nedap.university.go.newCommunication;

import com.nedap.university.go.game.Board;
import com.nedap.university.go.game.Game;

/**
 * Created by claudia.reuvers on 22/02/2017.
 */
public class Protocol {

    public static boolean checkName1(String name) {
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

    public static void checkName(String name) throws InvalidCommandException {
        String useName = "The name should consist of 20 lowercase letters.";
        if (name.length() >= 20) {
            throw new InvalidCommandException(useName);
        }
        for(char c : name.toCharArray()) {
            if (!(Character.isLetter(c) && Character.isLowerCase(c))) {
                throw new InvalidCommandException(useName);
            }
        }
    }

    public static boolean checkSize(int size) {
        return (size >= 5 && size <= 131 && size % 2 == 1);
    }

    public static boolean isOnBoard(Game game, int x, int y) {
        Board board = game.getBoard();
        int size = board.getDimension();
        //Check if this field exists
        if (x >= size || x < 0 || y >= size || y < 0) {
            return false;
        }
        return true;
    }

    public static boolean isEmptyField(Game game, int x, int y) {
        Board board = game.getBoard();
        if (!board.getField(x, y).isEmpty()) {
            return false;
        }
        return true;
        //TODO: could be return (!board.getField(x,y).isEmpty())??
    }

    public static boolean isKo(Game game, int x, int y, boolean white) {
        //TODO
        return game.testNextMove(x, y, white);
    }

    public static boolean isValidMove(Game game, int x, int y, boolean white) {
        return (isOnBoard(game, x, y) && isEmptyField(game, x, y) && isKo(game, x, y, white));
    }

//    public static boolean checkArgumentsLength(String[] args, int length) {
//        return (args.length == length);
//    }

    public static void checkArguments(String[] args, int length) throws InvalidCommandException{
        if (args.length != length) {
            throw new InvalidCommandException("Invalid argument length, command should have " + length + " arguments.");
        }
    }
}
