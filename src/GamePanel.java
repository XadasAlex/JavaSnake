import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = SCREEN_WIDTH*SCREEN_HEIGHT/UNIT_SIZE;
    static final int DELAY = 75;
    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];
    int bodyParts = 3;
    int points = 0;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        spawnApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (!running) {
            gameOver(g);
            return;
        }

        for(int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++) {
            g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
            g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
        }

        g.setColor(Color.red);
        g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

        for(int i = 0; i < bodyParts; i++) {
            if(i == 0) {
                g.setColor(Color.green);
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            } else {
              g.setColor(new Color(45,180, 0));
              g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }
        }
    }
    public void spawnApple() {
        appleX = random.nextInt(SCREEN_WIDTH/UNIT_SIZE);
        appleX = appleX * UNIT_SIZE;
        appleY = random.nextInt(SCREEN_HEIGHT/UNIT_SIZE);
        appleY = appleY*UNIT_SIZE;
    }
    public void move() {
        for(int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
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
        if (x[0] == appleX && y[0] == appleY) {
            spawnApple();
            bodyParts++;
        }
    }

    public void checkCollisions() {
        int headX = x[0];
        int headY = y[0];

        if (x[0] < 0 || y[0] < 0 || headX > SCREEN_WIDTH || headY > SCREEN_HEIGHT) {
            running = false;
        }

        for (int i = 1; i < bodyParts; i++) {
            if (headX == x[i] && headY == y[i]) {
                running = false;
                break;
            }
        }

    }

    public void gameOver(Graphics g)
    {
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
        g.setColor(Color.white);
        g.setFont(new Font("Ink Free", Font.BOLD, 45));
        String score = "Your Score: " + (bodyParts - 3);
        g.drawString(score, (SCREEN_WIDTH - metrics.stringWidth(score))/2, SCREEN_HEIGHT / 2 + 100);
    }
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(running) {
            move();
            checkApple();
            checkCollisions();
        }

        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                // fallthrough: vk_left || vk_a (either a key or arrow key left)
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_A:
                    if (direction != 'R') direction = 'L';
                    break;
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_D:
                    if (direction != 'L') direction = 'R';
                    break;
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S:
                    if (direction != 'U') direction = 'D';
                    break;
                case KeyEvent.VK_UP:
                case KeyEvent.VK_W:
                    if (direction != 'D') direction = 'U';
                    break;


            }
        }
    }
}
