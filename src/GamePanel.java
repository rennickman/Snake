import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
	
	// Set screen dimensions
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	
	// Set object size
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	
	// Create delay for timer - higher number = slower game
	static final int DELAY = 75;
	
	// Arrays to hold all the x and y coordinates of the snake using game units as maximum size
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	
	// Direction of snake - starting right
	char direction = 'R';
	
	// Set initial size of snake
	int bodyParts = 6;
	
	// Declare variables to keep track of apple coordinates and the amount eaten
	int applesEaten;
	int appleX;
	int appleY;
	
	boolean running = false;
	Timer timer;
	Random random;
	
	
	GamePanel() {
		
		random = new Random();
		// Set dimension of screen
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		// Set background color
		this.setBackground(Color.black);
		// Set to listen for key strokes
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		// Call start game method
		startGame();
		
	}
	
	public void startGame() {
		// Call new apple method
		newApple();
		// Set running as true
		running = true;
		// Create and start the timer
		timer = new Timer(DELAY, this);
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		
		// Check if game is running
		if (running) {
			
			
			// Create grid - uncomment to see
			/*
			for (int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++) {
				// Draw vertical lines
				g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
				// Draw horizontal lines
				g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
			}
			*/
			
			// Set color of ball to red
			g.setColor(Color.red);
			// Make ball a circle the size of a unit
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
		
			// Loop through all body parts of snake
			for (int i = 0; i < bodyParts; i++) {
				if (i == 0) {
					// Draw head of snake
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				} else {
					// Draw body of snake a different shade of green
					g.setColor(new Color(45, 180, 0));
					// Make snake multicolored - uncomment to use
					//g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			
			// Set score text color, font and metrics
			g.setColor(Color.red);
			g.setFont(new Font("Ink Free", Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			
			// Draw score text in top of screen
			g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
			
		} else {
			gameOver(g);
		}
	}
	
	public void newApple() {
		// Create random position for apple x and y  and place somewhere inside grid
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE)) * UNIT_SIZE;
	}
	
	public void move() {
		// Loop through every part of the snake
		for (int i = bodyParts; i > 0; i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		// Check direction based on button pressed using unit size to move x or y
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
	}

	public void checkApple() {
		
		// Check to see if apple and head of snake touch
		if ((x[0] == appleX) && (y[0] == appleY)) {
			// Add a body part and update apples eaten
			bodyParts ++;
			applesEaten ++;
			// Create new apple
			newApple();
		}
	}
	
	public void checkCollisions() {
		
		// Check if head collides with body
		for(int i = bodyParts; i > 0; i--) {
			if ((x[0] == x[i]) && y[0] == y[i]) {
				running = false;
			}
		}
		
		// Check if head hits left border
		if (x[0] < 0) {
			running = false;
		}
		
		// Check if head hits right border
		if (x[0] > SCREEN_WIDTH) {
			running = false;
		}
			
		// Check if head hits top border
		if (y[0] < 0) {
			running = false;
		}
				
		// Check if head hits bottom border
		if (y[0] > SCREEN_HEIGHT) {
			running = false;
		}
		
		// Stop the timer if game stops running
		if (!running) {
			timer.stop();		}
	}
	
	public void gameOver(Graphics g) {
		
		// Set score text color, font and metrics
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
					
		// Draw score text in top of screen
		g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
					
		// Set game over text color, font and metrics
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		
		// Draw game over text in center of screen
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		// Check if game is running and perform actions or repaint scene if not
		if (running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
		
	}
	
	public class MyKeyAdapter extends KeyAdapter {
		@Override 
		public void keyPressed(KeyEvent e) {
			
			// Check if key is pressed - change direction but only allow 45 degrees
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if (direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if (direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if (direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if (direction != 'U') {
					direction = 'D';
				}
				break;
			}
		}
	}

}
