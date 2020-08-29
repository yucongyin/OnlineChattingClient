
/*
 * Student Name: Yucong Yin
 * Student Number: 040792791
 * Lab Section: CST8221-302
 * Assignment 2-2
 * Instructor: Svillen Ranev, Daniel Cormier
 */

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;

/**
 * This class is used to launch ChatClientUI
 * 
 * @author Yucong Yin
 *
 */
public class Client {

	/**
	 * @param args - set for command line arguments
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {

			public void run() {
				ClientChatUI clientChatUI = new ClientChatUI("Yucong's ClientChatUI");
				clientChatUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				clientChatUI.setResizable(false);
				clientChatUI.setSize(new Dimension(588, 500));
				clientChatUI.setLocationRelativeTo(null);
				clientChatUI.setVisible(true);

			}
		});

	}

}
