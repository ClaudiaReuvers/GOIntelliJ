package com.nedap.university.go.communication;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread {

	private  static final String USAGE = "usage: Client <address> <port>";

	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println(USAGE);
			System.exit(0);
		}
		InetAddress host = null;
		int port = 0;

		try {
			host = InetAddress.getByName(args[0]);
		} catch (UnknownHostException e) {
			System.out.println("ERROR: no valid hostname");
			System.exit(0);
		}

		try {
			port = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			System.out.println("ERROR: no valid portnumber");
			System.exit(0);
		}

		try {
			Client client = new Client(host, port);
			client.start();

			while(true) {
				String input = readString();
				client.sendMessage(input);
			}
		} catch (IOException e) {
			System.out.println("ERROR: couldn't construct a client object");
			System.exit(0);
		}
	}

	private Socket sock;
	private BufferedReader in;
	private BufferedWriter out;

	private static final String PLAYER = "PLAYER";
	private static final String GO = "GO";
	private static final String WAITING = "WAITING";
	private static final String READY = "READY";
	private static final String CANCEL = "CANCEL";
	private static final String MOVE = "MOVE";
	private static final String VALID = "VALID";
	private static final String INVALID = "INVALID";
	private static final String TABLEFLIP = "TABLEFLIP";
	private static final String TABLEFLIPPED = "TABLEFLIPPED";
	private static final String PASS = "PASS";
	private static final String PASSED = "PASSED";
	private static final String EXIT = "EXIT";
	private static final String CHAT = "CHAT";
	private static final String WARNING = "WARNING";
	private static final String END = "END";

	public Client(InetAddress host, int port) throws IOException {
		this.sock = new Socket(host, port);
		this.in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		this.out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
	}
	
	public void run() {
		String txt;
		try {
			while ((txt = in.readLine()) != null) {
				String[] words = txt.split(" ");
				String keyword = words[0];
				switch (keyword) {
					case WAITING :
						//TODO: add WAITING command
						break;
					case READY :
						//TODO: add READY method
						break;
					case VALID :
						//TODO: add VALID command
						break;
					case INVALID :
						//TODO: add INVALID command
						break;
					case PASSED :
						//TODO: add PASSED command
						break;
					case TABLEFLIPPED :
						//TODO: add TABLEFLIPPED command
						break;
					case WARNING :
						//TODO: add WARNING command
						break;
					case END :
						//TODO: add END command
						break;
					default :
						//TODO?
				}
//				System.out.println(txt);
			}
		} catch (IOException e) {
			//TODO
		}
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

	public static String readString() {
		String antw = null;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			antw = in.readLine();
		} catch (IOException e) {
			//TODO
		}
		return (antw == null) ? "" : antw;
	}
}
