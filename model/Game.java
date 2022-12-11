package model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;
import resource.ResourceLoader;
import database.Database;
import database.HighScore;

public class Game {
    private final HashMap<String, HashMap<Integer, GameLevel>> gameLevels;
    private GameLevel gameLevel = null;
    private final Database database;
    private boolean isBetterHighScore = false;
        


    public Game() {
        gameLevels = new HashMap<>();
         database = new Database();
        readLevels();
    }


  
    public void loadGame(ID gameID){
        gameLevel = new GameLevel(gameLevels.get(gameID.difficulty).get(gameID.level));
                isBetterHighScore = false;
    }

    public boolean stopgame(){
        return gameLevel.Lifeatzero();
    }

    public boolean step(Direction d){
        boolean stepped = (gameLevel.moveYogi(d));
            if (stepped && !gameLevel.Lifeatzero() && gameLevel.getNumcollected() == gameLevel.getNumbaskets()){
            ID id = gameLevel.gameid;
            int steps = gameLevel.getNumSteps();
            isBetterHighScore = database.storeHighScore(id, steps);
        }
        return stepped;
        
    }

    public Collection<String> getDifficulties(){ return gameLevels.keySet(); }
    
    public Collection<Integer> getLevelsOfDifficulty(String difficulty){
        if (!gameLevels.containsKey(difficulty)) return null;
        return gameLevels.get(difficulty).keySet();
    }
    
    private void readLevels(){
        InputStream is;
        is = ResourceLoader.loadResource("resource/levels.txt");

        try (Scanner sc = new Scanner(is)){
            String line = readNextLine(sc);
            ArrayList<String> gameLevelRows = new ArrayList<>();

            while (!line.isEmpty()){

                ID id = readGameID(line);
                if (id == null) return;



                gameLevelRows.clear();
                line = readNextLine(sc);
                while (!line.isEmpty() && line.trim().charAt(0) != ';'){
                    gameLevelRows.add(line);
                    line = readNextLine(sc);
                }
                addNewGameLevel(new GameLevel(gameLevelRows, id));
            }

        } catch (Exception e){
            System.out.println("Ajaj");
        }

    }

    private void addNewGameLevel(GameLevel gameLevel){
        HashMap<Integer, GameLevel> levelsOfDifficulty;
        if (gameLevels.containsKey(gameLevel.gameid.difficulty)){
            levelsOfDifficulty = gameLevels.get(gameLevel.gameid.difficulty);
            levelsOfDifficulty.put(gameLevel.gameid.level, gameLevel);
        } else {
            levelsOfDifficulty = new HashMap<>();
            levelsOfDifficulty.put(gameLevel.gameid.level, gameLevel);
            gameLevels.put(gameLevel.gameid.difficulty, levelsOfDifficulty);
        }
        database.storeHighScore(gameLevel.gameid, 0);

    }

    private String readNextLine(Scanner sc){
        String line = "";
        while (sc.hasNextLine() && line.trim().isEmpty()){
            line = sc.nextLine();
        }

        return line;
    }

    private ID readGameID(String line){
        line = line.trim();
        if (line.isEmpty() || line.charAt(0) != ';') return null;
        Scanner s = new Scanner(line);
        s.next(); // skip ';'
        if (!s.hasNext()) return null;
        String difficulty = s.next().toUpperCase();
        if (!s.hasNextInt()) return null;
        int id = s.nextInt();
        return new ID(difficulty, id);
    }

    public boolean isLevelLoaded(){ return gameLevel != null; }
    public int rows(){ return gameLevel.rows; }
    public int cols(){ return gameLevel.cols; }

    public LevelElements[][] levelboard(){return gameLevel.level;}
    public LevelElements items(int y, int x) {return  gameLevel.level[y][x]; }

    public void moveranger(){gameLevel.moveranger();}
    public  Position positionofplayer() {return gameLevel.player;}
    public  Position positionofranger() {return gameLevel.ranger;}
    public boolean isBetterHighScore(){ return isBetterHighScore; }


    public  ID getgameid() {return gameLevel.gameid;}
    public int getlevelnumbaskets(){ return (gameLevel != null) ? gameLevel.getNumbaskets() : 0; }
    public int getlevelnumcollected(){ return (gameLevel != null) ? gameLevel.getNumcollected() : 0; }
    public int getNumberSteps(){ return (gameLevel != null) ? gameLevel.getNumSteps(): 0; }
   public int getlifelevel(){return gameLevel.getlife();} 
    public ArrayList<HighScore> getHighScores() {
           
        return database.getHighScores();
    }


}
