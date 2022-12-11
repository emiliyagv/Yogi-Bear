package main;

import model.Direction;
import model.Game;
import model.ID;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import javax.swing.*;


public class Main extends JFrame {

    private Timer t;
  private GameBoard gameBoard;
    private final Game game;
   private final JLabel gameStateLabel; 
   
    private long et;
    private long elapset;
    private long st;
    private double ets;
    private String timestring ;
    private final JLabel gametimer; 



  public Main(){
      game = new Game();
      et= 0;
      elapset = 0;
      st = 0;
      ets = 0;
      timestring = "";
      setTitle("Yogi Bear");
      setSize(600, 600);
      setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      URL url = Main.class.getClassLoader().getResource("resource/gamepic.png");
      setIconImage(Toolkit.getDefaultToolkit().getImage(url));

      JMenuBar menubar = new JMenuBar();
      JMenu menu = new JMenu("Menu");
      JMenu levels = new JMenu("Levels");
         createGameLevelMenuItems(levels);

     JMenuItem menuHighScores = new JMenuItem(new AbstractAction("Score table") {
            @Override
            public void actionPerformed(ActionEvent e) {
                new HighScoreWindow(game.getHighScores(), Main.this);
            }
        });
      JMenuItem exit = new JMenuItem(new AbstractAction("Exit") {
          @Override
          public void actionPerformed(ActionEvent e) {
              System.exit(0);
          }
      });

      menu.add(levels);
      menu.addSeparator();
      menu.add(menuHighScores);
      menu.add(exit);
      menubar.add(menu);
      setJMenuBar(menubar);
          setLayout(new BorderLayout(0, 10));
          gameStateLabel = new JLabel("label");
          gametimer = new JLabel("label");
          add(gameStateLabel, BorderLayout.NORTH);
          add(gametimer, BorderLayout.SOUTH);
          try {
              add(gameBoard = new GameBoard(game), BorderLayout.CENTER);
          } catch (IOException e) {

          }
          
          
        
 

      addKeyListener(new KeyAdapter() {
          @Override
          public void keyPressed(KeyEvent ke) {
              super.keyPressed(ke);
              if (!game.isLevelLoaded()) return;
              int kc = ke.getKeyCode();
              Direction d = null;
              switch (kc){
                  case KeyEvent.VK_LEFT:  d = Direction.LEFT; break;
                  case KeyEvent.VK_RIGHT: d = Direction.RIGHT; break;
                  case KeyEvent.VK_UP:    d = Direction.UP; break;
                  case KeyEvent.VK_DOWN:  d = Direction.DOWN; break;
                  case KeyEvent.VK_ESCAPE: game.loadGame(game.getgameid());
              }
             refreshGameStateLabel();
              gameBoard.repaint();
           
                  if (d != null && game.step(d)) {
                      if (game.getlevelnumbaskets() == game.getlevelnumcollected()) {
                          String msg = "Congratulation!";
                          if (game.isBetterHighScore()){
                              msg += " You've improved your performance so far!";
                          }
                               t.stop();
                          JOptionPane.showMessageDialog(Main.this, msg, "Congratulation!", JOptionPane.INFORMATION_MESSAGE);
                            stopgame();
                      }
                  
              }
          }
      });


 

      pack();
      setResizable(false);
      setLocationRelativeTo(null);
      game.loadGame(new ID("EASY", 1));
      
      gameBoard.refresh();
  
      pack();
      
      
      Timer timer = new Timer(500, new ActionListener() {
          @Override
         public void actionPerformed(ActionEvent ae) {
        if(game.stopgame()){
            t.stop();
       JOptionPane.showMessageDialog(Main.this, "Game Over!", "Game Over!", JOptionPane.INFORMATION_MESSAGE);
             stopgame();
              }
              
          game.moveranger();
          refreshGameStateLabel();
           gameBoard.repaint();
          }
      });
      
 
     timer.start();
refreshGameStateLabel();
        startTimer();
     setVisible(true);
     
            



  }
  
      private void createGameLevelMenuItems(JMenu menu){
        for (String s : game.getDifficulties()){
            JMenu difficultyMenu = new JMenu(s);
            menu.add(difficultyMenu);
            for (Integer i : game.getLevelsOfDifficulty(s)){
                JMenuItem item = new JMenuItem(new AbstractAction("Level-" + i) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        game.loadGame(new ID(s, i));
                        gameBoard.refresh();
                        pack();
                    }
                });
                difficultyMenu.add(item);
            }
        }
    }
     private void refreshGameStateLabel(){
           String s = "Steps: " + game.getNumberSteps();
            s += ", Collected baskets: " + game.getlevelnumcollected() + "/" + game.getlevelnumbaskets();
            s += "           Life Level: " + game.getlifelevel();
            gameStateLabel.setText(s);
           

  }
     
            
     public void startTimer() {
        st = System.currentTimeMillis();
        t = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                elapset = System.currentTimeMillis() - st;
                ets = (double) elapset / 1000; 
                timestring = "Elapsed time:" + ets + " s";
               gametimer.setText(timestring);
            }
        });
        t.start();
    }
        
     public void resetTimer() {
        st = System.currentTimeMillis();
        t.restart();
    }

    private void stopgame(){
        
        et = System.currentTimeMillis();
        game.loadGame(game.getgameid());
        refreshGameStateLabel();
        gametimer.setText(timestring);
        resetTimer();
    }


    public static void main(String[] args) {
       Main mainwindow = new Main();
       mainwindow.setVisible(true);
    }
}