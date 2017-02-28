package com.nedap.university.go.communication;

import com.nedap.university.go.ClientCommand.*;
import com.nedap.university.go.GUI.GoGUIIntegrator;
import com.nedap.university.go.game.Board;
import com.nedap.university.go.game.StoneState;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread {

	private  static final String USAGE = "usage: Client <address> <port>";

	//Main-method
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
			client.print("You are successfully connected with the server.");

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
	private boolean color;
	private Board board;
	private GoGUIIntegrator GUI;

	//Constructor
	public Client(InetAddress host, int port) throws IOException {
		this.sock = new Socket(host, port);
		this.in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		this.out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
	}

	//Methods
	public void run() {
		while (sock.isConnected()) {
			readSocketInput();
		}
		print("Socket no longer connected");
		sendMessage("Socket of client no longer connected");
		//TODO: !sock.isConnected()
	}

	private void readSocketInput() {
		String txt;
		try {
			while ((txt = in.readLine()) != null) {
				String[] words = txt.split(" ");
				String keyword = words[0];
				ClientCommand command;
				switch (keyword) {
					case Protocol.WAITING :
						command = new ClientCommandWAITING();
						break;
					case Protocol.READY :
						command = new ClientCommandREADY(txt);
						break;
					case Protocol.VALID :
						command = new ClientCommandVALID(txt);
						break;
					case Protocol.INVALID :
						command = new ClientCommandINVALID(txt);
						break;
					case Protocol.PASSED :
						command = new ClientCommandPASSED(txt);
						break;
					case Protocol.TABLEFLIPPED :
						command = new ClientCommandTABLEFLIPPED(txt);
						break;
					case Protocol.CHAT :
						command = new ClientCommandCHAT(txt);
						break;
					case Protocol.WARNING :
						command = new ClientCommandWARNING(txt);
						break;
					case Protocol.END :
						command = new ClientCommandEND(txt);
						break;
					default :
						command = new ClientCommandUnknownKeyword(txt);
				}
				command.execute(this);
			}
			shutdown();
		} catch (IOException e) {
			//TODO: IOException at readSocketInput() at Client
		}
	}

	public void print(String msg) {
		System.out.println(msg);

	}

	public void sendMessage(String msg) {
		try {
			out.write(msg);
			out.newLine();
			out.flush();
		} catch (IOException e) {
			print("IOException at sendMessage");
			//TODO: IOException at sendMessage in Client
		}
	}

	private void shutdown() {
		print("Server is down.\nClosing the connection.");
		try {
			out.flush();
			out.close();
			in.close();
			sock.close();
		} catch (IOException e) {
		}
		print("Connection closed.");
	}

	public void setColor(boolean white) {
		this.color = white;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public void setGUI(int size) {
		if (GUI == null) {
			this.GUI = new GoGUIIntegrator(true, true, size);
		} else {
			this.GUI.setBoardSize(size);
			this.GUI.clearBoard();

		}
		this.GUI.startGUI();
	}

	public void updateGUI() {
		GUI.clearBoard();
		for (int yGUI = 0; yGUI < board.getDimension(); yGUI++) {
			for (int xGUI = 0; xGUI < board.getDimension(); xGUI++) {
				if (board.getField(xGUI, yGUI).getState() == StoneState.BLACK) {
					GUI.addStone(xGUI, yGUI, false);
				} else if (board.getField(xGUI, yGUI).getState() == StoneState.WHITE) {
					GUI.addStone(xGUI, yGUI, true);
				}
			}
		}
	}

	//Queries
	public boolean getColor() {
		return color;
	}

	public Board getBoard() {
		return board;
	}

	public static String readString() {
		String antw = null;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			antw = in.readLine();
		} catch (IOException e) {
			System.out.println("IOException at readString");
			//TODO: IOException at readString in Client
		}
		return (antw == null) ? "" : antw;
	}
}
