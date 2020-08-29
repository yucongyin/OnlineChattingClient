
/*
 * Student Name: Yucong Yin
 * Student Number: 040792791
 * Lab Section: CST8221-302
 * Assignment 2-2
 * Instructor: Svillen Ranev, Daniel Cormier
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/**
 * This class is set to create the Server Chat UI GUI and handle all the events
 * in the future
 * 
 * @author Yucong Yin
 *
 */
public class ServerChatUI extends JFrame implements Accessible {

	/**
	 * auto generated serial UID
	 */
	private static final long serialVersionUID = -6873899177468790677L;

	private Socket socket;
	private JTextField messageText;
	private JTextArea display;
	private JButton sendButton;
	private ObjectOutputStream outputStream;
	private ConnectionWrapper connection;

	/**
	 * set the class field socket, call methods to create GUI
	 * 
	 * @param socket - pass in the socket parameter as a initial value of local
	 *               socket reference
	 */
	public ServerChatUI(Socket socket) {

		this.socket = socket;
		setFrame(createUI());
		runClient();

	}

	/**
	 * The entire GUI is created in this method in a Jpanel
	 * 
	 * @return mainPanel - the Jpanel has the entire GUI is returned to be used
	 */
	public JPanel createUI() {
		JPanel mainPanel = new JPanel();
		Controller controller = new Controller();
		JPanel messagePanel = new JPanel();
		JPanel chatDisplayPanel = new JPanel();
		display = new JTextArea(30, 45);
		display.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(display);

		// message panel components
		// message text field
		messageText = new JTextField("Type message");
		messageText.setBackground(Color.WHITE);
		messageText.setEditable(true);
		messageText.requestFocus();
		messageText.setCaretPosition(0);
		messageText.setPreferredSize(new Dimension(442, 20));

		// send button
		sendButton = new JButton("Send");
		sendButton.setActionCommand("Send");
		sendButton.setMnemonic('S');
		sendButton.setEnabled(true);
		sendButton.setPreferredSize(new Dimension(100, 20));
		sendButton.addActionListener(controller);

		// message panel
		messagePanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		messagePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE, 10),
				"MESSAGE", TitledBorder.LEFT, TitledBorder.TOP));
		messagePanel.add(messageText);
		messagePanel.add(sendButton);

		// chat display panel
		chatDisplayPanel.setLayout(new BorderLayout());
		chatDisplayPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 10),
				"CHAT DISPLAY", TitledBorder.CENTER, TitledBorder.CENTER));
		chatDisplayPanel.add(scrollPane);

		// put them together
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(messagePanel, BorderLayout.NORTH);
		mainPanel.add(chatDisplayPanel, BorderLayout.CENTER);

		return mainPanel;
	}

	/**
	 * Take in a Jpanel and add to the main Jframe which is this class,set size
	 * related porperties
	 * 
	 * @param panel - pass in panel to be added to the Jframe
	 */
	public final void setFrame(JPanel panel) {
		WindowController windowListener = new WindowController();
		setResizable(false);
		setSize(new Dimension(588, 500));
		setLocationRelativeTo(null);
		addWindowListener(windowListener);
		add(panel);

	}

	private void runClient() {
		connection = new ConnectionWrapper(socket);
		try {
			connection.createStreams();
			outputStream = connection.getOutputStream();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Runnable runnable = new ChatRunnable<ServerChatUI>(this, connection);
		Thread thread = new Thread(runnable);
		thread.start();

	}

	private class WindowController extends WindowAdapter {

		@Override
		public void windowClosing(WindowEvent e) {
			// super.windowClosing(e);
			System.out.println("ServerUI Window closing!");
			try {
				outputStream.writeObject(ChatProtocolConstants.DISPLACMENT + ChatProtocolConstants.CHAT_TERMINATOR
						+ ChatProtocolConstants.LINE_TERMINATOR);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} finally {
				dispose();
			}

			System.out.println("Closing Chat!");
			try {
				connection.closeConnection();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} finally {
				dispose();
			}

			dispose();
			System.out.println("Chat closed!");

			System.exit(0);
		}

		@Override
		public void windowClosed(WindowEvent e) {
			// TODO Auto-generated method stub
			// super.windowClosed(e);
			System.out.println("Server UI Closed!");
		}

	}

	private class Controller implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			String actionCommand = event.getActionCommand();
			if (actionCommand.equals("Send")) {
				this.send();

			}

		}

		private void send() {
			String sendMessage = messageText.getText();
			display.append(sendMessage + ChatProtocolConstants.LINE_TERMINATOR);
			try {
				outputStream.writeObject(
						ChatProtocolConstants.DISPLACMENT + sendMessage + ChatProtocolConstants.LINE_TERMINATOR);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				display.setText(e.getMessage());
			}
		}
	}

	@Override
	public JTextArea getdisplay() {
		// TODO Auto-generated method stub
		return display;
	}

	@Override
	public void closeChat() {
		// TODO Auto-generated method stub
		try {
			connection.closeConnection();
			dispose();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
