import javax.swing.JTextArea;

/**
 * Interface which should be implemented by UI
 * 
 * @author Yucong Yin
 *
 */
public interface Accessible {

	/**
	 * get Display Console in the GUI
	 * 
	 * @return message area of Server and Client UI
	 */
	JTextArea getdisplay();

	/**
	 * method to close the GUI and socket and streams
	 */
	void closeChat();

}
