
/*
 * Student Name: Yucong Yin
 * Student Number: 040792791
 * Lab Section: CST8221-302
 * Assignment 2-2
 * Instructor: Svillen Ranev, Daniel Cormier
 */

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JFrame;
import javax.swing.JTextArea;

/**
 * This class is used to create a thread which can run for both client and
 * server
 * 
 * @author OWNER
 *
 * @param <T> generic type extends Jframe and Accessible
 */
public class ChatRunnable<T extends JFrame & Accessible> implements Runnable {

	final T ui;
	final Socket socket;
	final ObjectInputStream inputStream;
	final ObjectOutputStream outputStream;
	final JTextArea display;

	/**
	 * constructor to initialize all the variables with parameters get from the
	 * connected socket.
	 * 
	 * @param ui         - whatever object extends Jframe and Accesible
	 * @param connection - Connection Wrapper which contains all required
	 *                   information including socket, streams
	 */
	public ChatRunnable(T ui, ConnectionWrapper connection) {
		// TODO Auto-generated constructor stub
		this.ui = ui;
		this.socket = connection.getSocket();
		this.inputStream = connection.getInputStream();
		this.outputStream = connection.getOutputStream();
		this.display = ui.getdisplay();

	}

	@Override
	public void run() {
		String strin = "";
		LocalDateTime times;
		DateTimeFormatter formatter;
		times = LocalDateTime.now();
		formatter = DateTimeFormatter.ofPattern("MMMM dd,hh:mm a");
		String timeDate = times.format(formatter);
		while (true) {
			try {
				if (!socket.isClosed()) {
					// System.out.println("Strin: "+strin);
					strin = (String) inputStream.readObject();

					if (strin.trim().equals(ChatProtocolConstants.CHAT_TERMINATOR)) {
						final String terminate;

						terminate = String.format("%s%s%s%s\n", ChatProtocolConstants.DISPLACMENT, timeDate,
								ChatProtocolConstants.LINE_TERMINATOR, strin);
						display.append(terminate);
						break;
					} else {

						final String append = String.format("%s%s%s%s\n", ChatProtocolConstants.DISPLACMENT, timeDate,
								ChatProtocolConstants.LINE_TERMINATOR, strin);
						display.append(append);

					}

				} else {

					break;
				}

			} catch (EOFException e2) {
				System.out.println("EOF");
				break;
			} catch (SocketTimeoutException e3) {
				System.out.println("SocketEx");
				break;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				break;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;

			}

		}

		if (!socket.isClosed()) {
			try {
				outputStream.writeObject(ChatProtocolConstants.DISPLACMENT + ChatProtocolConstants.CHAT_TERMINATOR
						+ ChatProtocolConstants.LINE_TERMINATOR);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		ui.closeChat();

	}

}
