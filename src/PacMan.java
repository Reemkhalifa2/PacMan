
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import java.util.stream.Gatherer;
import javax.swing.*;


public class PacMan extends JPanel implements ActionListener, KeyListener {


    class Block{
        int x;
        int y;
        int width;
        int height;
        Image image;
        int startX;
        int startY;
        char direction = 'U';
        int velocityX = 0;
        int velocityY = 0;





        Block(Image image, int x, int y, int width, int height){
            this.image = image;
            this.x= x;
            this.y = y;
            this.width=width;
            this.height = height;
            this.startX = x;
            this.startY = y;
        }
        void updateDirection(char direction){
            this.direction = direction;
            updateVelocity();

            int nextX = this.x + this.velocityX;
            int nextY = this.y + this.velocityY;

            for (Block wall : walls){
                if(nextX < wall.x + wall.width &&
                        nextX + this.width > wall.x &&
                        nextY < wall.y + wall.height &&
                        nextY + this.height > wall.y){
                    return; // stop if wall ahead
                }
            }

            this.x = nextX;
            this.y = nextY;
        }
        void updateVelocity(){
            if(this.direction == 'U'){
                this.velocityX = 0;
                this.velocityY = -tileSize/4;
            }else if(this.direction == 'D'){
                this.velocityX = 0;
                this.velocityY = tileSize/4;
            }else if(this.direction == 'L'){
                this.velocityX = -tileSize/4;
                this.velocityY=0;
            }else if(this.direction == 'R'){
                this.velocityX = tileSize/4;
                this.velocityY=0;
            }
        }

        void reset(){
            this.x = this.startX;
            this.y = this.startY;
        }



    }
    private int rowCount = 21;
    private int columnCount = 19;
    private int tileSize = 32;
    private int boardWidth = columnCount*tileSize;
    private int boardHeight = rowCount * tileSize;

    private Image wallImage ;
    private Image blueGhostImage ;
    private Image orangeGhostImage ;
    private Image pinkGhostImage ;
    private Image redGhostImage ;

    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanLeftImage;
    private Image pacmanRightImage;
    //X = wall, O = skip, P = pac man, ' ' = food
    //Ghosts: b = blue, o = orange, p = pink, r = red
    private String[] tileMap = {
            "XXXXXXXXXXXXXXXXXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X                 X",
            "X XX X XXXXX X XX X",
            "X    X       X    X",
            "XXXX XXXX XXXX XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXrXX X XXXX",
            "X       bpo       X",
            "XXXX X XXXXX X XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXXXX X XXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X  X     P     X  X",
            "XX X X XXXXX X X XX",
            "X    X   X   X    X",
            "X XXXXXX X XXXXXX X",
            "X                 X",
            "XXXXXXXXXXXXXXXXXXX"
    };

    HashSet<Block> walls;
    HashSet<Block> foods;
    HashSet<Block> ghosts;
    Timer gameLoop;
    char [] directions = {'u', 'D', 'L', 'R'};
    Random random = new Random();

    int score = 0;
    int lives = 3;
    boolean gameOver = false;



    Block packman;



    PacMan(){
        setPreferredSize(new Dimension(boardWidth,boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        //load images
        wallImage = new ImageIcon(getClass().getResource("./wall.png")).getImage();
        blueGhostImage = new ImageIcon(getClass().getResource("./blueGhost.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("./redGhost.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("./pinkGhost.png")).getImage();
        orangeGhostImage = new ImageIcon(getClass().getResource("./orangeGhost.png")).getImage();

        pacmanUpImage = new ImageIcon(getClass().getResource("./pacmanUp.png")).getImage();
        pacmanLeftImage = new ImageIcon(getClass().getResource("./pacmanLeft.png")).getImage();
        pacmanRightImage = new ImageIcon(getClass().getResource("./pacmanRight.png")).getImage();
        pacmanDownImage = new ImageIcon(getClass().getResource("./pacmanDown.png")).getImage();

        loadMap();
        for(Block ghost : ghosts){
            char newDirecrio = directions[random.nextInt(4)];
            ghost.updateDirection(newDirecrio);
        }
        gameLoop = new Timer(50, this);
        gameLoop.start();
    }

    public void loadMap(){
        walls = new HashSet<Block>();
        foods = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        for(int r=0; r<rowCount; r++){
            for ( int c =0; c< columnCount; c++){
                String row = tileMap[r];
                char tileMapChar = row.charAt(c);

                int x= c*tileSize;
                int y = r * tileSize;

                if(tileMapChar == 'X'){//block wall
                    Block wall = new Block(wallImage,x,y,tileSize, tileSize);
                    walls.add(wall);
                }else if(tileMapChar == 'b'){//blue ghost
                    Block ghost = new Block(blueGhostImage,x,y,tileSize,tileSize);
                    ghosts.add(ghost);
                }else if(tileMapChar == 'o'){//orange ghost
                    Block ghost = new Block(orangeGhostImage,x,y,tileSize,tileSize);
                    ghosts.add(ghost);
                }else if(tileMapChar == 'p'){//pink ghost
                    Block ghost = new Block(pinkGhostImage,x,y,tileSize,tileSize);
                    ghosts.add(ghost);
                }else if(tileMapChar == 'r'){//red ghost
                    Block ghost = new Block(redGhostImage,x,y,tileSize,tileSize);
                    ghosts.add(ghost);
                }else if(tileMapChar == 'P'){//Pack Man
                    packman = new Block(pacmanRightImage,x,y,tileSize,tileSize);
                }else if(tileMapChar == ' '){//food
                    Block food = new Block(null, x+4, y+14, 4,4);
                    foods.add(food);
                }

            }
        }


    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);

    }
    public void draw(Graphics g){
        g.drawImage(packman.image, packman.x, packman.y,packman.width, packman.height, null);
        for(Block ghost : ghosts){
            g.drawImage(ghost.image, ghost.x, ghost.y,ghost.width, ghost.height, null);
        }
        for(Block wall : walls){
            g.drawImage(wall.image, wall.x, wall.y,wall.width, wall.height, null);
        }
        g.setColor(Color.WHITE);
        for(Block food : foods){
            g.fillRect( food.x, food.y,food.width, food.height);
        }
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        if(gameOver){
            g.drawString("Game Over" + String.valueOf(score), tileSize/2, tileSize/2);
        }else{
            g.drawString("x" + String.valueOf(lives), tileSize/2, tileSize/2);
        }
    }

    public void move(){
        packman.x += packman.velocityX;
        packman.y += packman.velocityY;

        //check wall collision
        for (Block wall: walls){
            if(collision(packman,wall)){
                packman.x -= packman.velocityX;
                packman.y -= packman.velocityY;
                break;
            }
        }//check ghosts collision
        for (Block ghost: ghosts){
            if(collision(ghost, packman)){
                lives -=1;
                if(lives ==0){
                    gameOver = true;
                    return;
                }
                resetPosition();
            }

            if(ghost.direction != 'U' && ghost.direction !='D' ){
                ghost.updateDirection('U');
            }
                ghost.x += ghost.velocityX;
                ghost.y += ghost.velocityY;
                for(Block wall : walls){
                    if(collision(ghost,wall)){
                        ghost.x -= ghost.velocityX;
                        ghost.y -= ghost.velocityY;
                        char newDirection = directions[random.nextInt(4)];
                        ghost.updateDirection(newDirection);
                    }
                }


        }

        //check food collision
        Block foodEaten = null;
        for(Block food : foods){
            if(collision(packman, food)){
                foodEaten = food;
                score += 10;
            }
        }
        foods.remove(foodEaten);



    }
    public boolean collision(Block a, Block b){
        return a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver){
            gameLoop.stop();
        }


    }
    public void resetPosition(){
        packman.reset();
        packman.velocityX= 0;
        packman.velocityY = 0;
        for(Block ghost : ghosts){
            ghost.reset();
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        if (gameOver){
            loadMap();
            resetPosition();
            lives = 3;
            score = 0;
            gameOver = false;
            gameLoop.start();
        }

//        System.out.println("KeyEvent:  "+ e.getKeyCode());
        if(e.getKeyCode() == KeyEvent.VK_UP){
            packman.updateDirection('U');
        }else if(e.getKeyCode() == KeyEvent.VK_DOWN){
            packman.updateDirection('D');
        }else if(e.getKeyCode() == KeyEvent.VK_LEFT){
            packman.updateDirection('L');
        }else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            packman.updateDirection('R');
        }

        if(packman.direction == 'U'){
            packman.image = pacmanUpImage;
        }else if(packman.direction == 'D'){
            packman.image = pacmanDownImage;
        }else if(packman.direction == 'R'){
            packman.image = pacmanRightImage;
        }else if(packman.direction == 'L'){
            packman.image = pacmanLeftImage;
        }
    }

}
