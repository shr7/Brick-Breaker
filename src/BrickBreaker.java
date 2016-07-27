import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BrickBreaker extends JPanel implements KeyListener,
ActionListener, Runnable {
	
	/*
	 * left and right are movement keys for the bar.
	 * Initially the bar is stationary, so they are initialized as false.
	 */
	static boolean right = false;
	static boolean left = false;
	
	/*
	 * barx and bary are initial coordinates of the bar
	 */
	int barx = 360;
	int bary = 565;
	
	/*
	 * ballx and bally are initial coordinates of the ball
	 */
	int ballx = 360;
	int bally = 565;
	
	/*
	 * brickx and bricky are coordinates of the first brick of first layer of the brick. 
	 */
	int brickx = 200;
	int bricky = 70;
	
	/*
	 * Declaring bar and ball at their respective coordinates of appropriate sizes.
	 */
	Rectangle Bar = new Rectangle(barx, bary, 100, 15);
	Rectangle Ball = new Rectangle(ballx, bally, 15, 15);

	/*
	 * Declaring array of bricks.
	 */
	Rectangle[] Brick = new Rectangle[44];

	/*
	 * Stating initial conditions:
	 * The ball is moving from the bar to upward directions
	 * GaveOver conditions, ie, ballFallDown and bricksOver are set false.
	 * Count of broken bricks is set to zero. 
	 */
	int movex = -1;
	int movey = -1;
	boolean ballFallDown = false;
	boolean bricksOver = false;
	int count = 0;
	String status;
	
	
	BrickBreaker() {

	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		BrickBreaker game = new BrickBreaker();
		JButton button = new JButton("restart");
		frame.setSize(750, 640);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(game);
		frame.add(button, BorderLayout.SOUTH);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		button.addActionListener(game);
		game.addKeyListener(game);
		game.setFocusable(true);
		Thread t = new Thread(game);
		t.start();
	}


	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 750, 650);
		g.setColor(Color.YELLOW);
		g.fillOval(Ball.x, Ball.y, Ball.width, Ball.height);
		g.setColor(Color.BLUE);
		g.fill3DRect(Bar.x, Bar.y, Bar.width, Bar.height, true);
		g.setColor(Color.red);
		g.fillRect(0,580,750,20);
		g.drawRect(0, 0, 743, 580);
		for (int i = 0; i < Brick.length; i++) {
			if (Brick[i] != null) {
				g.fill3DRect(Brick[i].x, Brick[i].y, Brick[i].width,
						Brick[i].height, true);
			}
		}		
		if (ballFallDown == true || bricksOver == true) {
			Font f = new Font("Arial", Font.BOLD, 40);
			g.setFont(f);
			g.drawString(status, 230, 320);
			ballFallDown = false;
			bricksOver = false;
		}
	}

	
	public void run() {
		/*
		 * Bricks are created.
		 */
		for (int i = 0; i < Brick.length; i++) {
			Brick[i] = new Rectangle(brickx, bricky, 40, 20);
			if (i == 7) {
				brickx = 200;
				bricky = 92;
			}
			if (i == 13) {
				brickx = 242;
				bricky = 114;
			}
			if(i == 17){
				brickx = 284;
				bricky = 136;
			}
			if(i == 19){
				brickx=20;
				bricky=130;
			}
			if(i == 21){
				brickx=20;
				bricky=152;
			}
			if(i == 23){
				brickx=550;
				bricky=130;
			}
			if(i == 25){
				brickx=550;
				bricky=152;
			}
			if(i==27){
				brickx=0;
				bricky=230;
			}
			brickx += 42;
		}
		
		/*
		 * Environment has been set up.
		 * Ball, Bar and Bricks are ready to use.
		 */
		
		while (true) {
			/*
			 * Intersection of ball and bricks is checked
			 * We make the brick=null to remove it once it has been hit by the ball.
			 * The ball changes direction(x) due to collision.
			 * We could change y-direction x-direction or both
			 * Then we keep count of the bricks that have been broken
			 */
			for (int i = 0; i < Brick.length; i++) {
				if (Brick[i] != null) {
					if (Brick[i].intersects(Ball)) {
						Brick[i] = null;
						movex = -movex;
						count++;
					}
				}
			}
			
			/*
			 * Checking if all the bricks have been hit
			 */
			if (count == Brick.length) {
				bricksOver = true;
				status = "YOU WIN!";
				repaint();
			}
			
			repaint();
			Ball.x += movex;
			Ball.y += movey;

			/*
			 * Movement of bar when left and right keys are pressed
			 */
			if (left == true) {
				Bar.x -= 3;
				right = false;
			}
			if (right == true) {	
				Bar.x += 3;
				left = false;
			}
			
			/*
			 * To refrain the bar from going out of the screen
			 * For right side, we also take into account the bat size.
			 */
			if (Bar.x <= 4) {
				Bar.x = 4;
			} 
			else if (Bar.x >= 641) {
				Bar.x = 641;
			}
			
			/*
			 * Ball goes back up when it strikes the bar
			 */
			if (Ball.intersects(Bar)) {
				movey = -movey;
			}
			
			/*
			 * Ball reverses direction when it strikes the horizontal and vertical borders
			 */
			if (Ball.x <= 0 || Ball.x + Ball.height >= 743) {
				movex = -movex;
			}
			if (Ball.y <= 0) {
				movey = -movey;
			}
			
			/*
			 * if the bar can't save the ball
			 */
			if (Ball.y >= 650) {
				ballFallDown = true;
				status = "GAME OVER!";
				repaint();
			}
			try {
				Thread.sleep(7);
			} catch (Exception ex) {
			}// try catch ends here

		}// while loop ends here

	}//run function loop ends here

	/*
	 * HANDLING KEY EVENTS
	 */
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_LEFT) {
			left = true;
		}

		if (keyCode == KeyEvent.VK_RIGHT) {
			right = true;
		}
	}

	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_LEFT) {
			left = false;
		}

		if (keyCode == KeyEvent.VK_RIGHT) {
			right = false;
		}
	}

	public void keyTyped(KeyEvent arg0) {

	}

	public void actionPerformed(ActionEvent e) {
		String str = e.getActionCommand();
		if (str.equals("restart")) {
			this.restart();

		}
	}

	public void restart() {

		requestFocus(true);
		/*
		 * barx and bary are initial coordinates of the bar
		 */
		barx = 360;
		bary = 565;
		/*
		 * ballx and bally are initial coordinates of the ball
		 */
		ballx = 360;
		bally = 565;
		/*
		 * brickx and bricky are initial coordinates of the first layer of the brick. 
		 */
		brickx = 200;
		bricky = 70;
		/*
		 * Creating and declaring bar and ball at their respective coordinates of appropriate sizes.
		 */
		Bar = new Rectangle(barx, bary, 100, 15);
		Ball = new Rectangle(ballx, bally, 15, 15);
		/*
		 * Declaring array of bricks.
		 */
		Brick = new Rectangle[44];
		
		/*
		 * stating initial conditions again
		 */
		movex = -1;
		movey = -1;
		ballFallDown = false;
		bricksOver = false;
		count = 0;
		status = null;

		/*
		 * Creating bricks again because this for loop is out of while loop in run method
		 */
		for (int i = 0; i < Brick.length; i++) {
			Brick[i] = new Rectangle(brickx, bricky, 40, 20);
			if (i == 7) {
				brickx = 200;
				bricky = 92;
			}
			if (i == 13) {
				brickx = 242;
				bricky = 114;
			}
			if(i == 17){
				brickx = 284;
				bricky = 136;
			}
			if(i == 19){
				brickx=20;
				bricky=130;
			}
			if(i == 21){
				brickx=20;
				bricky=152;
			}
			if(i == 23){
				brickx=550;
				bricky=130;
			}
			if(i == 25){
				brickx=550;
				bricky=152;
			}
			if(i==27){
				brickx=0;
				bricky=230;
			}
			brickx += 42;
		}
		repaint();
	}
}