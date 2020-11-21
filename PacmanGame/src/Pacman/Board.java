package Pacman;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board implements ActionListener, KeyListener {
	
	private final int WIDTH = 400, HEIGHT = 425; // width and height of the window
	private Dimension d;
	private boolean inGame = false, isDead = false, userWin = false; // if the user is in game or dead or won
	
	private int lives = 3, score = 0; // lives = 3 and score = 0
	
	private final int BLOCK_SIZE = 24; // size of each block (dot, wall, etc.)
    private final int NUM_BLOCKS = 15; // number of blocks
    private final int SCREEN_SIZE = NUM_BLOCKS * BLOCK_SIZE; // screen size (playing size
    private final int MAX_GHOSTS = 6; // max ghosts
    private final int MARGIN_X = 6, MARGIN_Y = 7, SIZE = 15;
    private int PACMAN_SPEED = 6; // speed of pacman
    private final int WIN_SCORE = 194; // score you need to win

    private int NUM_GHOSTS = 4; // number of ghosts
    private int[] x, y; 
    private int[] ghostX, ghostY, ghost2X, ghost2Y, ghostSpeed; // ghost left right up down
    
    private int pacmanX, pacmanY, pacman2X, pacmand2Y; // pacman left right up down
    private int userX, userY; // user arrow keys left right up down
    
    private int currentSpeed = 3; //current speed of ghost
    private int[] boardData; // board data (where the walls dots etc. are)
    
    private Timer timer; // timer used to repaint every 40 ms
	   
    private final int validSpeeds[] = {1, 2, 3, 4, 6, 8}; // speeds for the ghost
    
    JFrame frame; // jFrame
    
    Pacman pac; // new pacman object
    
    private ArrayList<Objects> Dots = new ArrayList<Objects>(); // arraylist for keeping the dots
    
    private final int levelData[] = { // data for the level, 0 = wall, 1 = left wall, 2 = top wall, 4 = right wall, 8 = bottom wall, 16 = dots
        	19, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22,
            17, 16, 16, 16, 16, 24, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            25, 24, 24, 24, 28, 0, 17, 16, 16, 16, 16, 16, 16, 16, 20,
            0,  0,  0,  0,  0,  0, 17, 16, 16, 16, 16, 16, 16, 16, 20,
            19, 18, 18, 18, 18, 18, 16, 16, 16, 16, 24, 24, 24, 24, 20,
            17, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0,  0,  0,   0, 21,
            17, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0,  0,  0,   0, 21,
            17, 16, 16, 16, 24, 16, 16, 16, 16, 20, 0,  0,  0,   0, 21,
            17, 16, 16, 20, 0, 17, 16, 16, 16, 16, 18, 18, 18, 18, 20,
            17, 24, 24, 28, 0, 25, 24, 24, 16, 16, 16, 16, 16, 16, 20,
            21, 0, 0,  0,  0,  0,  0,   0, 17, 16, 16, 16, 16, 16, 20,
            17, 18, 18, 22, 0, 19, 18, 18, 16, 16, 16, 16, 16, 16, 20,
            17, 16, 16, 20, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            17, 16, 16, 20, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            25, 24, 24, 24, 26, 24, 24, 24, 24, 24, 24, 24, 24, 24, 28
            };
    
    public Board () {
    	
    	
    	frame = new JFrame("Pacman"); // new JFrame
    	
    	JPanel panel = new JPanel(); // new panel
    	
    	BoxLayout boxlayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(boxlayout);
		
		// intitializing variables
		boardData = new int[NUM_BLOCKS * NUM_BLOCKS];
        d = new Dimension(400, 400);
        ghostX = new int[MAX_GHOSTS];
        ghost2X = new int[MAX_GHOSTS];
        ghostY = new int[MAX_GHOSTS];
        ghost2Y = new int[MAX_GHOSTS];
        ghostSpeed = new int[MAX_GHOSTS];
        x = new int[4];
        y = new int[4];
        
        timer = new Timer(40, this); // starts timer
        timer.start();
    
        JPanel drawPanel = new JPanel() {
        	
			public void paint (Graphics g) {
				
				g.setColor(Color.BLACK);
		        g.fillRect(0, 0, d.width, d.height); // draws the background
		       		      
				drawBoard(g); // draws the board (walls, dots, etc.)
				drawScore(g); // draws scoreboard
				drawLives(g); // draws lives
				
				if (inGame) {
					
					playGame(g);
					
				} else if (lives == 0) {
						
					drawGameOver(g); // if lost, draw game over
						
				} else if (!inGame) {
					
					drawStartScreen(g); // if game has not started yet, draw start screen
					
				}
				
				if (userWin) {
					 
					drawWinScreen(g); // if user got all the dots, draw win screen
					
				}
				
			
			}
			
			
		};
	
		drawPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT)); // size for the draw panel
		
		panel.add(drawPanel);
		
		// setting up the frame
		frame.add(panel);
		frame.setSize(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setFocusable(true);
		frame.setVisible(true);

		frame.addKeyListener(this); // adds a key listener
    	
    }

	private void setupGame() {
    	
		// setup the level for each time you restart the game
    	lives = 3;
        score = 0;
        NUM_GHOSTS = 6;
        currentSpeed = 3;
        setupLevel();
    	
    }
	
	 private void setupLevel() {

		 for (int i = 0; i < NUM_BLOCKS * NUM_BLOCKS; i++) {
			 
			 boardData[i] = levelData[i]; // now board data stores the level data for the game
	
	     }

	        continueLevel(); // runs continue level
	        
	    }
    
    public void playGame (Graphics g) {
    	   	 
    	if (isDead) { // first checks if you got hit by a ghost
    		
    		loseLife(); // lose a life
    		
    	} else if (userWin) { // check if you won
    		
    		win(); // win
    		
    	} else {
    	
    		movePacman(); // then move pacman, draw pacman, and move ghosts
    		drawPacman(g); 
    		moveGhosts(g);
    		
    	}
    	
    }
    
    // start screen
    private void drawStartScreen (Graphics g) {
    	
    	g.setFont(new Font("Arial", Font.BOLD, 14));
    	g.setColor(Color.YELLOW);
    	g.drawString("Press SPACE to Begin", SCREEN_SIZE/2-80, SCREEN_SIZE/2);
    	
    }
    
    // draws the scoreboard
    private void drawScore (Graphics g) {
    	
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.setColor(Color.YELLOW);
        String s = "Score: " + score;
        g.drawString(s, SCREEN_SIZE / 2 + 96, SCREEN_SIZE + 16);

    }
    
    // draws how many lives you have left
    private void drawLives (Graphics g) {
    	
    	g.setFont(new Font("Arial", Font.BOLD, 14));
    	g.setColor(Color.YELLOW);
    	String life = "Lives: " + lives;
    	g.drawString(life, SCREEN_SIZE/2, SCREEN_SIZE+16);
    	
    }
    
    // draw game over
    private void drawGameOver (Graphics g) {
    	
    	g.setFont(new Font("Arial", Font.BOLD, 30));
    	g.setColor(Color.YELLOW);
    	g.drawString("GAME OVER", SCREEN_SIZE/2-80, SCREEN_SIZE/2);
    	
    }
    
    // draws the win screen
    private void drawWinScreen (Graphics g) {
    	
    	g.setFont(new Font("Arial", Font.BOLD, 30));
    	g.setColor(Color.YELLOW);
    	g.drawString("YOU WIN!", SCREEN_SIZE/2-80, SCREEN_SIZE/2);
    	
    }
    
    // move ghosts method
    private void moveGhosts(Graphics g) {

        int loc;
        int count;

        for (int i = 0; i < 4; i++) {
        	
            if (ghostX[i] % BLOCK_SIZE == 0 && ghostY[i] % BLOCK_SIZE == 0) {
            	
                loc = ghostX[i] / BLOCK_SIZE + NUM_BLOCKS * (int) (ghostY[i] / BLOCK_SIZE); // sets the initial location of the ghosts

                count = 0;

                if ((boardData[loc] & 1) == 0 && ghost2X[i] != 1) { // if the ghost is not next to the left wall (it would be 0)
                	
                    x[count] = -1;
                    y[count] = 0;
                    count++;
                    
                }

                if ((boardData[loc] & 2) == 0 && ghost2Y[i] != 1) { // if ghost is not next to the top wall
                	
                    x[count] = 0;
                    y[count] = -1;
                    count++;
                    
                }

                if ((boardData[loc] & 4) == 0 && ghost2X[i] != -1) { // same for the right wall
                	
                    x[count] = 1;
                    y[count] = 0;
                    count++;
                    
                }

                if ((boardData[loc] & 8) == 0 && ghost2Y[i] != -1) { // same for the bottom wall
                	
                    x[count] = 0;
                    y[count] = 1;
                    count++;
                    
                }
                //System.out.println(boardData[loc]&15);
                if (count == 0) {
                	
                    if ((boardData[loc] & 15) == 15) {
                    	
                        ghost2X[i] = 0;
                        ghost2Y[i] = 0;
                        
                    } else {
                    	
                        ghost2X[i] = -ghost2X[i];
                        ghost2Y[i] = -ghost2Y[i];
                        
                    }

                } else {

                    count = (int) (Math.random() * count);

                    if (count > 3) {
                    	
                        count = 3;
                        
                    }

                    ghost2X[i] = x[count]; // changes the directions of the ghosts
                    ghost2Y[i] = y[count];
                    
                }

            }

            ghostX[i] += (ghost2X[i] * ghostSpeed[i]); // moves the ghosts
            ghostY[i] += (ghost2Y[i] * ghostSpeed[i]);
            
            drawGhost(g, ghostX[i] + 1, ghostY[i] + 1);

            if (pacmanX > (ghostX[i] - 12) && pacmanX < (ghostX[i] + 12)
                    && pacmanY > (ghostY[i] - 12) && pacmanY < (ghostY[i] + 12)
                    && inGame) { // if the pacman runs into a ghost

                isDead = true;
                
            }
            
            // the ghosts move by checking if there are obstacles. For example, if there are no obstacles to the right of the ghost
            // and the ghost is not moving to the left, the ghost will move to the right.
            
        }
    }
    
    private void drawGhost(Graphics g, int x, int y) {
	   
	    Ghost ghost = new Ghost(x+MARGIN_X, y+MARGIN_Y, SIZE, SIZE, Color.RED); // create new ghost object and draws it
	    ghost.draw(g);
	    	
    }
    
    private void loseLife () {

    	lives--; // loses a life

        if (lives == 0) {
        	
            inGame = false; // if life == 0, game over
            
        }

        continueLevel(); // continue the level
        
    }
    
    private void win () {
   
    	inGame = false; // leave the game
    	
    	try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	// wait three seconds
    
    	userWin = false; 
    	
    	continueLevel();
    	
    }
	
    public void drawBoard (Graphics g) {
    	
    	int i = 0;
		
		for (int y = 0; y < SCREEN_SIZE; y+=BLOCK_SIZE) {
			
			for (int x = 0; x < SCREEN_SIZE; x+=BLOCK_SIZE) {
				
				  g.setColor(new Color(0,72,251));
	                
	                if ((levelData[i] == 0)) { // if it is a inside wall
	                	
	                	g.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE); // draws the wall
	                	
	                 }

	                if ((boardData[i] & 1) != 0) {  // left wall
	                	
	                    g.drawLine(x, y, x, y + BLOCK_SIZE - 1);
	       
	                }

	                if ((boardData[i] & 2) != 0) { // top wall
	                	
	                    g.drawLine(x, y, x + BLOCK_SIZE - 1, y);
	                    
	                }

	                if ((boardData[i] & 4) != 0) { // right wall
	                	
	                    g.drawLine(x + BLOCK_SIZE - 1, y, x + BLOCK_SIZE - 1,
	                            y + BLOCK_SIZE - 1);
	                    
	                }

	                if ((boardData[i] & 8) != 0) { // bottom wall
	                	
	                    g.drawLine(x, y + BLOCK_SIZE - 1, x + BLOCK_SIZE - 1,
	                            y + BLOCK_SIZE - 1);
	                    
	                }

	                if ((boardData[i] & 16) != 0) {  // dot
	                    
	                	Dots.add(new Dot((x+10), (y+10), 6, 6, Color.ORANGE)); // creates a new dot object and draws it
	                	Dots.get(Dots.size()-1).draw(g);
	                	          	
	               }

	                i++;
				
			}
			
		}
    	
    }
    
    private void movePacman() { // move pacman


        int pos;

        if (pacmanX % BLOCK_SIZE == 0 && pacmanY % BLOCK_SIZE == 0) {
        	
            pos = pacmanX / BLOCK_SIZE + NUM_BLOCKS * (int) (pacmanY / BLOCK_SIZE); // location of pacman

            if (boardData[pos] >= 16) { // if it is a dot
            	
                boardData[pos] = boardData[pos] & 15; // gets rid of the dot
                Dots.remove(Dots.size()-1); // removes a dot
                score++; // adds to the score
                
            }

            if (userX != 0 || userY != 0) { // if the user is moving
            	
                if (!((userX == 1 && userY == 0 && boardData[pos] == 4) // if user is pressing the right key and there is a wall there
                        || (userX == -1 && userY == 0 && boardData[pos] == 1) // if user is pressing the left key and there is a wall there
                        || (userX == 0 && userY == 1 && boardData[pos] == 8) // if user is pressing the down key and there is wall there
                        || (userX == 0 && userY == -1 && boardData[pos] == 2))) { // if user is pressing the up key and there is a wall there
                	
                    pacman2X = userX; // pacman x = the user x
                    pacmand2Y = userY; // pacman y = the user y
                    
                }
                
            }
            
            // checks if the pacman is moving against the wall
            if ((pacman2X == 1 && pacmand2Y == 0 && (boardData[pos] & 4) != 0) // if the pacman is against the right wall (or any of the inside walls( 0 means pacman is not touching anyting))
                    || (pacman2X == -1 && pacmand2Y == 0 && (boardData[pos] & 1) != 0 ) // if the pacman is against the left wall
                    || (pacman2X == 0 && pacmand2Y == 1 && (boardData[pos] & 8) != 0) // if the pacman is against the bottom wall
                    || (pacman2X == 0 && pacmand2Y == -1 && (boardData[pos] & 2) != 0)) { // if the pacman is against the top wall
            	
                pacman2X = 0; // (then pacman cannot move)
                pacmand2Y = 0;
                
            }
            
        } 
        
        pacmanX += PACMAN_SPEED * pacman2X;
        pacmanY += PACMAN_SPEED * pacmand2Y;
        
        if (score == WIN_SCORE || Dots.isEmpty()) { // when the score is the max/when there are no more dots you win
			
			userWin = true;
			
		}
        
    }
    
    public void drawPacman (Graphics g) {
    	
    	Pacman pac = new Pacman(pacmanX+MARGIN_X, pacmanY+MARGIN_Y, SIZE, SIZE, Color.YELLOW); // creates a new pacman object and draws it
    	
    	pac.draw(g);
    	
    }
    
    private void continueLevel() {

    	int x = 1;
        int randomSpeed;

        for (int i = 0; i < NUM_GHOSTS; i++) {

            ghostY[i] = 4 * BLOCK_SIZE; // resets ghost start position
            ghostX[i] = 4 * BLOCK_SIZE;
            ghost2Y[i] = 0;
            ghost2X[i] = x;
            x = -x;
            
            randomSpeed = (int) (Math.random() * (currentSpeed + 1));

            if (randomSpeed > currentSpeed) {
            	
                randomSpeed = currentSpeed;
                
            }

           ghostSpeed[i] = validSpeeds[randomSpeed]; // each time continue level is called the ghosts get a new speed
           
        }

        pacmanX = 7 * BLOCK_SIZE;  //resets start position
        pacmanY = 11 * BLOCK_SIZE;
        pacman2X = 0;	//reset direction
        pacmand2Y = 0;
        userX = 0;		// reset direction controls
        userY = 0;
        isDead = false;
        
        // this is called when the pacman loses a life so everything is reset
        
    }

	@Override
	public void actionPerformed(ActionEvent e) { // each timer tick
		
		frame.getContentPane().repaint(); // repaints every time tick (40 ms)
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	// checks for key pressed
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		int key = e.getKeyCode();

        if (inGame) {
        	
            if (key == KeyEvent.VK_LEFT) {
            
                userX = -1;
                userY = 0;
                
            } else if (key == KeyEvent.VK_RIGHT) {
            
                userX = 1;
                userY = 0;
                
            } else if (key == KeyEvent.VK_UP) {
      
                userX = 0;
                userY = -1;
                
            } else if (key == KeyEvent.VK_DOWN) {
            	
                userX = 0;
                userY = 1;
                
            } else if (key == KeyEvent.VK_ESCAPE && timer.isRunning()) {
            	
                inGame = false; // closes the game
                
            }
            
        } else if (key == KeyEvent.VK_SPACE) {
            	
            	//System.out.println("H");
            	setupGame(); // starts the game once you press SPACE
                inGame = true;
                
            }
        } 
                
	@Override
	public void keyReleased(KeyEvent e) {
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Board();
		
	}

	
}
