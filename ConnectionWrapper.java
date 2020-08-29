
/*
 * Student Name: Yucong Yin
 * Student Number: 040792791
 * Lab Section: CST8221-302
 * Assignment 2-2
 * Instructor: Svillen Ranev, Daniel Cormier
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This class is used to create & close streams and socket
 * 
 * @author Yucong Yin
 *
 */
public class ConnectionWrapper {

	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	private Socket socket;

	ConnectionWrapper(Socket socket) {

		this.socket = socket;

	}

	/**
	 * @return outputStream to be accessed
	 */
	public ObjectOutputStream getOutputStream() {
		return outputStream;
	}

	/**
	 * @return inputStream to be accessed
	 */
	public ObjectInputStream getInputStream() {
		return inputStream;
	}

	/**
	 * @return the socket to be accessed
	 */
	public Socket getSocket() {
		return socket;
	}

	/**
	 * The method instantiates an object of ObjectInputStream using the input stream
	 * of the socket, assigns the reference to the inputStream field and returns the
	 * inputStream reference.
	 * 
	 * @return the input stream assigned with socket input stream
	 * @throws IOException - throws input/output exceptions
	 */
	public ObjectInputStream createObjectIStream() throws IOException {

		inputStream = new ObjectInputStream(socket.getInputStream());
		return inputStream;

	}

	/**
	 * The method instantiates an object of ObjectOutputStream using the output
	 * stream of the socket , assigns the reference to the outputStream field and
	 * returns the outputStream reference.
	 * 
	 * @return the output stream assigned with socket output stream
	 * @throws IOException - throws input/output exceptions
	 */
	public ObjectOutputStream createObjectOStream() throws IOException {

		outputStream = new ObjectOutputStream(socket.getOutputStream());
		return outputStream;

	}

	/**
	 * The method instantiates an object of ObjectOutputStream and assigns the
	 * reference to the outputStream field. Then it instantiates an object of
	 * ObjectInputStream and assigns the reference to the inputStream field.
	 * 
	 * @throws IOException - throws input/output exceptions
	 */
	public void createStreams() throws IOException {
		outputStream = createObjectOStream();
		inputStream = createObjectIStream();

	}

	/**
	 * The method closes the output stream, the input stream, and the socket. Make
	 * sure that you do not call the close() method on null references. Also make
	 * sure that you do not call close() on a closed socket
	 * 
	 * @throws IOException - throws input/output exceptions
	 */
	public void closeConnection() throws IOException {
		if (!socket.isClosed() && inputStream != null)
			inputStream.close();

		if (!socket.isClosed() && outputStream != null)
			outputStream.close();

		if (!socket.isClosed())
			socket.close();

	}

}
