import java.awt.*;
import javax.swing.*;

public class PacMan extends JPanel {

    private int rowCount = 21;
    private int columnCount = 19;
    private int tileSize = 32;
    private int boardWidth = columnCount*tileSize;
    private int boardHeight = rowCount * tileSize;

    PacMan(){
        setPreferredSize(new Dimension(boardWidth,boardHeight));
        setBackground(Color.BLACK);
    }
    class Block{
        int x;
        int y;
        int width;
        int height;
        Image image;
        int startX;
        int startY;

        Block(Image image, int x, int y, int width, int height){
            this.image = image;
            this.x= x;
            this.y = y;
            this.width=width;
            this.height = height;
            this.startX = x;
            this.startY = y;
        }
    }

}