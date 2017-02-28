package com.nedap.university.go.communication;

import com.nedap.university.go.game.Board;

/**
 * Created by claudia.reuvers on 22/02/2017.
 */
public class Protocol {

    public static final String PLAYER = "PLAYER";
    public static final String GO = "GO";
    public static final String WAITING = "WAITING";
    public static final String READY = "READY";
    public static final String CANCEL = "CANCEL";
    public static final String MOVE = "MOVE";
    public static final String VALID = "VALID";
    public static final String INVALID = "INVALID";
    public static final String TABLEFLIP = "TABLEFLIP";
    public static final String TABLEFLIPPED = "TABLEFLIPPED";
    public static final String PASS = "PASS";
    public static final String PASSED = "PASSED";
    public static final String END = "END";
    public static final String CHAT = "CHAT";
    public static final String WARNING = "WARNING";
    public static final String HINT = "HINT";

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

    public static void checkSize(String arg) throws InvalidCommandException {
        int size;
        try {
            size = Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            throw new InvalidCommandException("The argument of the " + Protocol.GO + " command should be an integer.");
        }
        String useSize = "The size should be an uneven number between 5 and 131.";
        if (size < 5 || size > 131 || size % 2 != 1) {
            throw new InvalidCommandException(useSize);
        }
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
        return game.testNextMove(x, y, white);
    }

    public static boolean isValidMove(Game game, int x, int y, boolean white) {
        return (isOnBoard(game, x, y) && isEmptyField(game, x, y) && isKo(game, x, y, white));
    }

    public static void checkArgumentLength(String[] args, int length) throws InvalidCommandException{
        if (args.length != length) {
            throw new InvalidCommandException("Invalid argument length, command should have " + (length - 1) + " argument(s).");
        }
    }

    public static void checkArguments(String[] args, String keyword) throws InvalidCommandException {
        switch (keyword) {
            case PLAYER :
                checkArgumentLength(args, 2);
                checkName(args[1]);
                break;
            case GO :
                checkArgumentLength(args, 2);
                checkSize(args[1]);
                break;
            case CANCEL :
                checkArgumentLength(args, 1);
                break;
            case MOVE :
                checkArgumentLength(args, 3);
                int x;
                int y;
                try {
                    x = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    throw new InvalidCommandException("The second argument should be an integer.");
                }
                try {
                    y = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    throw new InvalidCommandException("The third argument should be an integer.");
                }
                break;
            case PASS :
                checkArgumentLength(args, 1);
                break;
            case TABLEFLIP :
                checkArgumentLength(args, 1);
                break;
            case CHAT :
                checkArgumentLength(args, 2);
                break;
            case HINT :
                checkArgumentLength(args, 1);
                break;
        }
    }

}
