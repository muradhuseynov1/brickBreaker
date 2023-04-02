package brickBreaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePlay extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private int score = 0;

    private int totalBricks = 21;

    private Timer timer;
    private int delay = 8;

    private int playerX = 310;

    private int ballPositionX = 120;
    private int ballPositionY = 350;
    private int ballXDirection = -1;
    private int ballYDirection = -2;

    private MapGenerator map;

    public GamePlay() {
        map = new MapGenerator(3, 7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics g) {
        // background
        g.setColor(Color.darkGray);
        g.fillRect(1, 1, 692, 592);

        // map
        map.draw((Graphics2D)g);

        // borders
        g.setColor(Color.orange);
        g.fillRect(0,0,3,592);
        g.fillRect(0,0,692,3);
        g.fillRect(691,0,3,592);

        // scores
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("" + score, 590, 30);

        // paddle
        g.setColor(Color.red);
        g.fillRect(playerX,550,100,8);

        // ball
        g.setColor(Color.blue);
        g.fillRect(ballPositionX, ballPositionY,20,20);

        if(totalBricks <= 0 ){
            play = false;
            ballXDirection = 0;
            ballYDirection = 0;
            g.setColor(Color.cyan);
            g.setFont(new Font("monospace", Font.BOLD, 30));
            g.drawString("YOU WON!", 190, 300);

            g.setFont(new Font("monospace", Font.BOLD, 20));
            g.drawString("PRESS ENTER TO RESTART!", 230, 350);
        }

        if(ballPositionY > 570) {
            play = false;
            ballXDirection = 0;
            ballYDirection = 0;
            g.setColor(Color.cyan);
            g.setFont(new Font("monospace", Font.BOLD, 30));
            g.drawString("GAME OVER!", 190, 300);

            g.setFont(new Font("monospace", Font.BOLD, 20));
            g.drawString("PRESS ENTER TO RESTART!", 230, 350);
        }

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if(play) {
            if(new Rectangle(ballPositionX, ballPositionY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
                ballYDirection = -ballYDirection;
            }

            A: for(int i=0; i<map.map.length; i++) {
                for(int j=0; j<map.map[0].length; j++) {
                    if(map.map[i][j] > 0) {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballPositionX, ballPositionY, 20, 20);
                        Rectangle brickRect = rect;

                        if(ballRect.intersects(brickRect)) {
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score+=5;

                            if(ballPositionX + 19 <= brickRect.x || ballPositionX +1 >= brickRect.x + brickRect.width) {
                                ballXDirection = -ballXDirection;
                            } else {
                                ballYDirection = -ballYDirection;
                            }

                            break A;
                        }
                    }
                }
            }

            ballPositionX += ballXDirection;
            ballPositionY += ballYDirection;
            if(ballPositionX < 0) {
                ballXDirection = -ballXDirection;
            }
            if(ballPositionY < 0) {
                ballYDirection = -ballYDirection;
            }
            if(ballPositionX > 670) {
                ballXDirection = -ballXDirection;
            }
        }

        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) { }
    @Override
    public void keyReleased(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if(playerX >= 600) {
                playerX = 600;
            } else {
                moveRight();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            if(playerX < 10) {
                playerX = 10;
            } else {
                moveLeft();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
            if(!play) {
                play = true;
                ballPositionX = 120;
                ballPositionY = 350;
                ballXDirection = -1;
                ballYDirection = -2;
                playerX = 310;
                score = 0;
                totalBricks = 21;
                map = new MapGenerator(3, 7);

                repaint();
            }
        }
    }

    public void moveRight() {
        play = true;
        playerX += 20;
    }
    public void moveLeft() {
        play = true;
        playerX -= 20;
    }
}
