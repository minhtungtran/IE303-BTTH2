import java.awt.*;
import java.util.Random;
import javax.swing.*;

public class Pipe {
    private int x, topHeight;
    private final int WIDTH = 52, GAP = 150, SPEED = 3;
    private Image topPipeImage, bottomPipeImage;

    public Pipe(int startX) {
        x = startX;
        topHeight = new Random().nextInt(180) + 120;
        topPipeImage = new ImageIcon("toppipe.png").getImage();
        bottomPipeImage = new ImageIcon("bottompipe.png").getImage();
    }

    public void move() {
        x -= SPEED;
    }

    public boolean isOutOfScreen() {
        return x + WIDTH < 0;
    }

    public boolean collision(int birdX, int birdY, int birdW, int birdH) {
        return (birdX + birdW > x && birdX < x + WIDTH &&
                (birdY < topHeight || birdY + birdH > topHeight + GAP));
    }

    public void draw(Graphics g) {
        g.drawImage(topPipeImage, x, topHeight - 320, WIDTH, 320, null);
        g.drawImage(bottomPipeImage, x, topHeight + GAP, WIDTH, 320, null);
    }

    public int getX() { return x; }
}