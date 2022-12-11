package model;

import java.io.Console;
import java.util.ArrayList;
import java.util.Random;

public class GameLevel {

    public final ID gameid;
    private int randnum;
    public final int rows, cols;
    public final LevelElements[][] level;
    public Position  player = new Position(0, 0);
    public Position ranger = new Position(0, 0);
    private int numbaskets, numcollected, numSteps;
    private int        numlife = 5;


    public GameLevel(ArrayList<String> gameLevelRows , ID gameid) {
        int cols1;
        rows = gameLevelRows.size();
        cols1 = 0;
        randnum = 0;
        for(String row : gameLevelRows){ if(row.length() > cols1){
            cols1 = row.length();}}

        cols = cols1;
        this.gameid = gameid;
        level = new LevelElements[rows][cols];
        numbaskets = 0;
        numcollected = 0;
        numSteps = 0;



        for (int i = 0; i < rows; i++){
            String s = gameLevelRows.get(i);
            for (int j = 0; j < s.length(); j++){
                switch (s.charAt(j)){
                    case '@': player = new Position(j, i);
                        level[i][j] = LevelElements.EMPTY; break;
                    case '#': level[i][j] = LevelElements.MOUNTAIN; break;
                    case '.': level[i][j] = LevelElements.BASKET;
                        numbaskets++;
                    break;
                    case '$': level[i][j] = LevelElements.RANGER;
                    ranger = new Position(j, i);
                        break;
                    case '*': level[i][j] = LevelElements.TREE;
                        break;
                    default:  level[i][j] = LevelElements.EMPTY; break;
                }
            }
            for (int j = s.length(); j < cols; j++){
                level[i][j] = LevelElements.EMPTY;
            }
        }
    }
    public GameLevel(GameLevel gl) {
        gameid = gl.gameid;
        rows = gl.rows;
        cols = gl.cols;
        numbaskets= gl.numbaskets;

        numSteps = gl.numSteps;
        level = new LevelElements[rows][cols];
        ranger = new Position(gl.ranger.x, gl.ranger.y);
        player = new Position(gl.player.x, gl.player.y);
        for (int i = 0; i < rows; i++){
            System.arraycopy(gl.level[i], 0, level[i], 0, cols);
        }
    }

    public int getNumbaskets() {
        return numbaskets;
    }

    public int getNumcollected() {
        return numcollected;
    }

    public int getNumSteps() {
        
        return numSteps;
    }
    public int getlife(){
        return numlife;
    }
    
    
    public void moveranger(){
        Random rand = new Random();
        int rand_int1 = randnum;
         randnum = rand.nextInt(4);    
        Position c  = ranger;
         if(randnum != rand_int1){
        switch(rand_int1){
            case 0: if(ispositionvalid(ranger.translate(Direction.UP)) && isGrass(ranger.translate(Direction.UP))) c = ranger.translate(Direction.UP);
            break;
            case 1: if(ispositionvalid(ranger.translate(Direction.DOWN)) && isGrass(ranger.translate(Direction.DOWN))) c = ranger.translate(Direction.DOWN);
            break;
            case 2: if(ispositionvalid(ranger.translate(Direction.LEFT)) && isGrass(ranger.translate(Direction.LEFT))) c = ranger.translate(Direction.LEFT);
            break;
            case 3: if(ispositionvalid(ranger.translate(Direction.RIGHT)) && isGrass(ranger.translate(Direction.RIGHT))) c = ranger.translate(Direction.RIGHT);
            break;
            
        }
        if(!c.equals(ranger) && !c.equals(player)){
       

        level[ranger.y][ranger.x] = LevelElements.EMPTY;
        level[c.y][c.x] = LevelElements.RANGER;
        ranger = c;
         }
        
        if(isRangeraround(player)){ numlife--;}
    }
    }
    

    public boolean ispositionvalid(Position p){
        return (p.x >= 0 && p.y >= 0 && p.x < cols && p.y < rows);
    }
    

    public boolean isGrass(Position p){
        if (!ispositionvalid(p)) return false;
        LevelElements le = level[p.y][p.x];
        return (le == LevelElements.EMPTY);
    }

    boolean isBasket(Position p) {
        if (!ispositionvalid(p)) return false;
        LevelElements le = level[p.y][p.x];
        return (le == LevelElements.BASKET);
    }


    boolean checkpositions(Position p, Direction d){
        Position c = p.translate(d);
        if (!ispositionvalid(c) ) return false;
        LevelElements le = level[c.y][c.x];
        return (le == LevelElements.RANGER);
    }

    boolean Lifeatzero(){
        return numlife == 0;
    }

    boolean isRangeraround(Position p) {
        return checkpositions(p, Direction.LEFT) || checkpositions(p, Direction.RIGHT)
                || checkpositions(p, Direction.UP) || checkpositions(p, Direction.DOWN);

    }


    public boolean moveYogi(Direction d) {
        Position current = player;
        Position next = current.translate(d);
        if (numcollected < numbaskets && !Lifeatzero()) {
            if(isGrass(next) || isBasket(next)) {
                if (isGrass(next)) {
             
                    player = next;
                    numSteps++;
                } else{

                    player = next;
                    numcollected++;
                    numSteps++;
                    level[next.y][next.x] = LevelElements.EMPTY;

                }

                
                return true;
            }else {
                return false;
            }
        }
            return false;
        }

}
