package com.nedap.university.go.newCommunication;

import com.nedap.university.go.game.Board;

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

    public static int checkSize(String arg) throws InvalidCommandException {
        int size;
        try {
            size = Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            throw new InvalidCommandException("The argument of the GO command should be an integer.");
        }
        String useSize = "The size should be an uneven number between 5 and 131.";
        if (size < 5 || size > 131 || size % 2 != 1) {
            throw new InvalidCommandException(useSize);
        }
        return size;
    }

    public static boolean isOnBoard(tempGame game, int x, int y) {
        Board board = game.getBoard();
        int size = board.getDimension();
        //Check if this field exists
        if (x >= size || x < 0 || y >= size || y < 0) {
            return false;
        }
        return true;
    }

    public static boolean isEmptyField(tempGame game, int x, int y) {
        Board board = game.getBoard();
        if (!board.getField(x, y).isEmpty()) {
            return false;
        }
        return true;
        //TODO: could be return (!board.getField(x,y).isEmpty())??
    }

    public static boolean isKo(tempGame game, int x, int y, boolean white) {
        //TODO
        return game.testNextMove(x, y, white);
    }

    public static boolean isValidMove(tempGame game, int x, int y, boolean white) {
        return (isOnBoard(game, x, y) && isEmptyField(game, x, y) && isKo(game, x, y, white));
    }

//    public static boolean checkArgumentsLength(String[] args, int length) {
//        return (args.length == length);
//    }

    public static void checkArgumentLength(String[] args, int length) throws InvalidCommandException{
        if (args.length != length) {
            throw new InvalidCommandException("Invalid argument length, command should have " + (length - 1) + " argument(s).");
        }
    }

    public static void checkArguments(String[] args, String keyword) throws InvalidCommandException {
        switch (keyword) {
            case "PLAYER" :
                checkArgumentLength(args, 2);
                break;
            case "GO" :
                checkArgumentLength(args, 2);
                break;
            case "MOVE" :
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
        }
    }



}
