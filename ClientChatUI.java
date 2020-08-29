
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
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

/**
 * This class is set to create the content Pane of ClientChatUI and handle all
 * the events in the future
 * 
 * @author Yucong Yin
 *
 */
public class ClientChatUI extends JFrame implements Accessible {

	/**
	 * generated serial UID
	 */
	private static final long serialVersionUID = -4565937347069524176L;
	private JTextField hostText;
	private JTextField messageArea;
	private JTextArea display;
	private JComboBox<String> comboBox;
	private JButton connectButton;
	private JButton sendButton;
	private ObjectOutputStream outputStream;
	private Socket socket;
	private ConnectionWrapper connection;

	/**
	 * It must have a constructor that takes the frame title. In the constructor you
	 * must set the frame title and call a method runClient().
	 *
	 * @param frameTitle - pass in a title to set to the Jframe
	 */
	public ClientChatUI(String frameTitle) {

		setTitle(frameTitle);

		runClient();

	}

	/**
	 * The entire GUI is created in this method in a Jpanel
	 * 
	 * @return mainPanel - return the fully created Jpanel for future use
	 */
	public JPanel createClientUI() {
		Controller controller = new Controller(this);
		JPanel mainPanel = new JPanel();// will be returned by this method
		JPanel connectionPanel = new JPanel();// connection panel,contains host panel and port panel
		JPanel messagePanel = new JPanel();// message panel
		JPanel chatDisplayPanel = new JPanel();
		JPanel hostPanel = new JPanel();// panel for host label and textfield
		JPanel portPanel = new JPanel();// panel for port label, combobox and connect button
		JPanel northPanel = new JPanel();// panel for connection panel and message panel
		JLabel hostLabel = new JLabel("Host:");
		JLabel portLabel = new JLabel("Port:");
		String ports[] = { "", "8088", "65001", "65525" };// define an array to pass into combobox
		comboBox = new JComboBox<String>(ports);
		display = new JTextArea(30, 45);
		display.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(display);

		// connection panel - components
		// host panel
		hostText = new JTextField();
		hostLabel.setPreferredSize(new Dimension(35, 30));
		hostLabel.setDisplayedMnemonic('H');
		hostLabel.setLabelFor(hostText);

		hostText.setBackground(Color.WHITE);
		hostText.setHorizontalAlignment(SwingConstants.LEFT);
		hostText.setText("localhost");
		hostText.requestFocus();
		hostText.setCaretPosition(0);

		hostText.setPreferredSize(new Dimension(508, 20));
		hostPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		hostPanel.add(hostLabel);
		hostPanel.add(hostText);

		// post panel
		portLabel.setPreferredSize(new Dimension(35, 30));
		portLabel.setDisplayedMnemonic('P');
		portLabel.setLabelFor(comboBox);
		comboBox.setBackground(Color.WHITE);
		comboBox.setPreferredSize(new Dimension(100, 20));
		comboBox.setEditable(true);
		comboBox.addActionListener(controller);
		connectButton = new JButton("Connect");
		connectButton.setActionCommand("Connect");
		connectButton.setMnemonic('C');
		connectButton.setBackground(Color.RED);
		connectButton.setPreferredSize(new Dimension(100, 20));
		connectButton.addActionListener(controller);
		// connectButton.setFont(new Font("Arial",Font.BOLD,10));
		// connectButton.setForeground(Color.BLACK);
		portPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		portPanel.add(portLabel);
		portPanel.add(comboBox);
		portPanel.add(connectButton);

		// add to connection panel
		connectionPanel.setLayout(new BorderLayout());
		connectionPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.RED, 10),
				"CONNECTION", TitledBorder.CENTER, TitledBorder.CENTER));
		connectionPanel.add(hostPanel, BorderLayout.NORTH);
		connectionPanel.add(portPanel, BorderLayout.SOUTH);

		// message panel
		// components
		messageArea = new JTextField();
		messageArea.setText("Type message");
		messageArea.setPreferredSize(new Dimension(442, 20));
		messageArea.setBackground(Color.WHITE);
		messageArea.setEditable(true);

		sendButton = new JButton("Send");
		sendButton.setActionCommand("Send");
		sendButton.setEnabled(false);
		sendButton.setMnemonic('S');
		sendButton.setPreferredSize(new Dimension(100, 20));
		sendButton.addActionListener(controller);

		// set panel

		messagePanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		messagePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE, 10),
				"MESSAGE", TitledBorder.LEFT, TitledBorder.TOP));
		messagePanel.add(messageArea);
		messagePanel.add(sendButton);

		// north panel

		northPanel.setLayout(new BorderLayout());
		northPanel.add(connectionPanel, BorderLayout.NORTH);
		northPanel.add(messagePanel, BorderLayout.SOUTH);

		// chat display panel

		chatDisplayPanel.setLayout(new BorderLayout());
		chatDisplayPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 10),
				"CHAT DISPLAY", TitledBorder.CENTER, TitledBorder.CENTER));
		chatDisplayPanel.add(scrollPane);

		// add all together
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(northPanel, BorderLayout.NORTH);
		mainPanel.add(chatDisplayPanel, BorderLayout.CENTER);

		return mainPanel;
	}

	private void runClient() {
		WindowController windowListener = new WindowController();
		addWindowListener(windowListener);
		this.add(createClientUI());

	}

	private class WindowController extends WindowAdapter {

		@Override
		public void windowClosing(WindowEvent e) {
			super.windowClosing(e);

			try {
				outputStream.writeObject(ChatProtocolConstants.CHAT_TERMINATOR);
				System.exit(0);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				System.exit(0);
			}
		}

	}

	private class Controller implements ActionListener {

		private ClientChatUI ui;

		public Controller(ClientChatUI ui) {
			this.ui = ui;// TODO Auto-generated constructor stub
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			boolean connected = false;
			int port = 65535;
			String actionCommand = event.getActionCommand();
			if (actionCommand.equals("Connect")) {
				String host = hostText.getText();

				port = Integer.parseInt((String) comboBox.getSelectedItem());
				connected = connect(host, port);
				System.out.println(port);
				if (connected) {
					connectButton.setEnabled(false);
					connectButton.setBackground(Color.BLUE);
					sendButton.setEnabled(true);
					messageArea.requestFocus();
					Runnable runnable = new ChatRunnable<ClientChatUI>(ui, connection);
					Thread thread = new Thread(runnable);
					thread.start();
				} else {
					return;
				}

			}

			if (actionCommand.equals("Send")) {
				send();
			}

		}

		public boolean connect(String host, int port) {

			try {
				Socket timeout = new Socket();

				timeout.connect(new InetSocketAddress(InetAddress.getByName(host), port), 100000);
				timeout.setSoTimeout(100000);
				socket = timeout;

				if (socket.getSoLinger() != -1)
					socket.setSoLinger(true, 5);
				if (!socket.getTcpNoDelay())
					socket.setTcpNoDelay(true);
				String text = String.format("Connected to Socket[addr=%s,port=%d,localport=%d]",
						socket.getInetAddress(), socket.getPort(), socket.getLocalPort());
				display.append(text);
				connection = new ConnectionWrapper(socket);
				connection.createStreams();
				outputStream = connection.getOutputStream();

				return true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				display.setText(e.getMessage());
				return false;
			}

		}

		private void send() {
			try {
				String sendMessage = messageArea.getText();
				display.append(sendMessage + ChatProtocolConstants.LINE_TERMINATOR);

				outputStream.writeObject(
						ChatProtocolConstants.DISPLACMENT + sendMessage + ChatProtocolConstants.LINE_TERMINATOR);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				enableConnectButton();
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
		if (!socket.isClosed()) {
			try {
				connection.closeConnection();
				enableConnectButton();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private void enableConnectButton() {
		connectButton.setEnabled(true);
		connectButton.setBackground(Color.RED);
		sendButton.setEnabled(false);
		hostText.requestFocus();

	}

}
