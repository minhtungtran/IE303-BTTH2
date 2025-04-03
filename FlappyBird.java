import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    private final int WIDTH = 360, HEIGHT = 640;
    private Image background, birdImage;
    private Timer timer;
    private int birdY, birdVelocity;
    private boolean gameOver, gameStarted;
    private ArrayList<Pipe> pipes;
    private int score, highScore;

    public FlappyBird() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        addKeyListener(this);
        loadImages();
        timer = new Timer(20, this);
        timer.start();
        initializeGame();
    }

    private void loadImages() {
        background = new ImageIcon("flappybirdbg.png").getImage();
        birdImage = new ImageIcon("flappybird.png").getImage();
    }

    private void initializeGame() {
        birdY = HEIGHT / 2;
        birdVelocity = 0;
        gameOver = false;
        gameStarted = false;
        score = 0;
        highScore = loadHighScore();
        pipes = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            pipes.add(new Pipe(WIDTH + i * 150));
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, WIDTH, HEIGHT, null);
        g.drawImage(birdImage, WIDTH / 4, birdY, 34, 24, null);
        for (Pipe pipe : pipes) pipe.draw(g);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Score: " + score, 20, 40);
        g.drawString("High Score: " + highScore, 200, 40);

        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 32));
            g.drawString("Game Over!", WIDTH / 4, HEIGHT / 2);
            g.drawString("Press R to Restart", WIDTH / 6, HEIGHT / 2 + 40);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (!gameOver && gameStarted) {
            birdVelocity += 1;
            birdY += birdVelocity;

            for (int i = 0; i < pipes.size(); i++) {
                Pipe pipe = pipes.get(i);
                pipe.move();
                if (pipe.isOutOfScreen()) {
                    pipes.remove(i);
                    pipes.add(new Pipe(WIDTH));
                    score++;
                    if (score > highScore) saveHighScore(score);
                }
                if (pipe.collision(WIDTH / 4, birdY, 34, 24)) gameOver = true;
            }
            if (birdY > HEIGHT - 50 || birdY < 0) gameOver = true;
        }
        repaint();
    }

    public void keyPressed(KeyEvent e) {
        if ((e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER) && !gameOver) {
            birdVelocity = -10;
            gameStarted = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_R && gameOver) {
            initializeGame();
        }
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}

    private int loadHighScore() {
        try (BufferedReader reader = new BufferedReader(new FileReader("highscore.txt"))) {
            return Integer.parseInt(reader.readLine());
        } catch (Exception e) {
            return 0;
        }
    }

    private void saveHighScore(int newScore) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("highscore.txt"))) {
            writer.write(String.valueOf(newScore));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
