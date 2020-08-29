
/*
 * Student Name: Yucong Yin
 * Student Number: 040792791
 * Lab Section: CST8221-302
 * Assignment 2-2
 * Instructor: Svillen Ranev, Daniel Cormier
 */

import java.awt.EventQueue;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;

/**
 * This class is used to launch the ServerChatUI
 * 
 * @author Yucong Yin
 *
 */
public class Server {

	/**
	 * @param args - set for command line arguments
	 */
	public static void main(String[] args) {
		int portNum = 65535;
		ServerSocket serverSocket = null;
		int friend = 0;

		// if command line string is applied at launch
		if (args.length != 0) {
			portNum = Integer.parseInt(args[0]);

		} else
			System.out.println("Using default port: " + portNum);
		try {
			serverSocket = new ServerSocket(portNum);

			while (true) {

				Socket socket;
				socket = serverSocket.accept();
				if (socket.getSoLinger() != -1)
					socket.setSoLinger(true, 5);
				if (!socket.getTcpNoDelay())
					socket.setTcpNoDelay(true);
				System.out.printf("Connecting to a client Socket[addr=%s,port=%d,localport=%d]",
						socket.getInetAddress(), socket.getPort(), socket.getLocalPort());
				friend++;
				final String title = String.format("Yin's Friend %d", friend);
				launchClient(socket, title);

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

	/**
	 * This method creates the GUI in the event-dispatch thread.
	 * 
	 * @param socket - will be implemented in the future
	 * @param title  - pass in title to set the title of Jframe
	 */
	public static void launchClient(Socket socket, String title) {

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				ServerChatUI serverChatUI = new ServerChatUI(socket);
				serverChatUI.setTitle(title);
				serverChatUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				serverChatUI.setVisible(true);

			}
		});

	}

}
