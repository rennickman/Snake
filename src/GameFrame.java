import javax.swing.JFrame;

public class GameFrame extends JFrame {

	GameFrame() {
		
		// Create instance of game panel class
		GamePanel panel = new GamePanel();
		this.add(panel);
		// Set the title
		this.setTitle("Snake");
		// Set to close by clicking x
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		// Fit frame around components
		this.pack();
		this.setVisible(true);
		// Set location to center of screen
		this.setLocationRelativeTo(null);
	}
}
