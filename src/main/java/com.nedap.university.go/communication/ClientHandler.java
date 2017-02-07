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
                server.broadcast(txt);
                txt = in.readLine();
            }
        } catch (IOException e) {
                //TODO
        }
    }
}
