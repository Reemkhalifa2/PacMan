import javax.swing.JFrame;// GUI widget toolkit for Java,
public class Main {
    public static void main(String[] args) {
        int rowCount = 21;
        int columnCount = 19;
        int tileSize = 32;
        int boardWidth = columnCount*tileSize;
        int boardHeight = rowCount * tileSize;

        JFrame frame = new JFrame("Pac Man");//Create a window
        frame.setSize(boardWidth,boardHeight);
        frame.setLocationRelativeTo(null);//center
        frame.setResizable(false);//can not change the size of the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
        PacMan pacmanGame = new PacMan();
        frame.add(pacmanGame);
        frame.pack();
        frame.setVisible(true);//make the window visible



    }
}