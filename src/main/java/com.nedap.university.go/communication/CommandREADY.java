package com.nedap.university.go.communication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by claudia.reuvers on 17/02/2017.
 */
public class CommandREADY implements Command {

    private String incomming;
    private boolean color;
    private int size;

    public CommandREADY(String line) {
        incomming = line;
    }

    @Override
    public void execute(Server server, ClientHandler client) {
        if (checkArguments(server)) {

        }

    }

    @Override
    public boolean checkArguments(Server server) {
        String[] args = incomming.split(" ");
        if (checkArgumentsLength(args)) {
            String colorString = args[1];
            if (colorString.equals("white")) {
                color =  true;
            } else {
                color = false;
            }
            size = Integer.parseInt(args[2]); //should be right since comes from server and was checked
            return true;
        } else {
            return false;
        }
    }

    private boolean checkArgumentsLength(String[] args) {
        if (args.length != 3) {
            //TODO: throw WrongNrOfArgumentsException
            return false;
        }
        return true;
    }

    @Override
    public List<String> getArguments() {
        List<String> args = new ArrayList<>();
        args.add(String.valueOf(color));
        args.add(String.valueOf(size));
        return args;
    }
}