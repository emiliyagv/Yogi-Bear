package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JPanel;
import javax.swing.Timer;

import model.LevelElements;
import model.Position;
import resource.ResourceLoader;
import model.Game;
import static model.LevelElements.RANGER;

public class GameBoard extends JPanel {

    Game game;
    private final Image mountain, tree, basket, ranger, empty, yogibear;

    private double scale;
    private int scaled_size;
    private final int square_size = 50;


    public GameBoard(Game game) throws IOException {

         this.game = game;
         scale = 1.5;
         scaled_size = (int)scale * square_size;
         yogibear = ResourceLoader.loadImage("resource/yogibear.png");
         mountain =  ResourceLoader.loadImage("resource/mountain.png");
         tree = ResourceLoader.loadImage("resource/tree.png");
         basket = ResourceLoader.loadImage("resource/basket.png");
         ranger = ResourceLoader.loadImage("resource/ranger.png");
         empty = ResourceLoader.loadImage("resource/empty.png");




    }

    public boolean refresh(){
       // if (!game.isLevelLoaded()) return false;
        Dimension dim = new Dimension(game.cols() * scaled_size, game.rows() * scaled_size);
        setPreferredSize(dim);
        setMaximumSize(dim);
        setSize(dim);
        repaint();
        return true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (!game.isLevelLoaded()) return;
        Graphics2D gr = (Graphics2D)g;
        int width = game.cols();
        int height = game.rows();
        Position p = game.positionofplayer();
        Position r = game.positionofranger();
        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                Image img = null;
                LevelElements le = game.items(y, x);
                switch (le){
                    case MOUNTAIN: img = mountain; break;
                    case TREE: img = tree; break;
                    case BASKET: img = basket; break;                   
                    case EMPTY: img = empty; break;
                }
                if (r.x == x && r.y == y) img = ranger; 
                if (p.x == x && p.y == y) img = yogibear;
                if (img == null) continue;
                gr.drawImage(img, x * scaled_size, y * scaled_size, scaled_size, scaled_size, null);
            }
        }
    }
    
    

}
