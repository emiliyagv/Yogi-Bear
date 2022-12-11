/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import model.ID;

public class Database {
    private final String tableName = "highscore";
    private final Connection conn;
    private final HashMap<ID, Integer> highScores;

    public Database(){
        Connection c = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql://localhost/yogibear?"
                    + "serverTimezone=UTC&user=student&password=asd123");
        } catch (Exception ex) {
            System.out.println("No connection");
        }
        this.conn = c;
        highScores = new HashMap<>();
        loadHighScores();
    }

    public boolean storeHighScore(ID id, int newScore){
        return mergeHighScores(id, newScore, newScore > 0);
    }

    public ArrayList<HighScore> getHighScores(){
        ArrayList<HighScore> scores = new ArrayList<>();
        for (ID id : highScores.keySet()){
            HighScore h = new HighScore(id, highScores.get(id));
            scores.add(h);

        }
        return scores;
    }

    private void loadHighScores(){
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
            while (rs.next()){
                String diff = rs.getString("Difficulty");
                int level = rs.getInt("GameLevel");
                int steps = rs.getInt("Steps");
                ID id = new ID(diff, level);
                mergeHighScores(id, steps, false);
            }
        } catch (Exception e){ System.out.println("loadHighScores error: " + e.getMessage());}
    }


    private boolean mergeHighScores(ID id, int score, boolean store) {
        boolean doUpdate = true;
        System.out.println(score);
        if (highScores.containsKey(id)){
            System.out.println("Another hello");
            System.out.println("after" + score);
            int oldScore = highScores.get(id);
            System.out.println("old" + oldScore);
            doUpdate = ((score < oldScore && score != 0) || oldScore == 0);
        }
        if (doUpdate){
            System.out.println("hello");
            System.out.println("before" + score);
            highScores.remove(id);
            highScores.put(id, score);
            System.out.println("highsc" + highScores.get(id));
            if (store) return storeToDatabase(id, score) > 0;
            return true;
        }

        return false;
    }

    private int storeToDatabase(ID id, int score) {
        try (Statement stmt = conn.createStatement()) {
            String s = "INSERT INTO " + tableName +
                    " (Difficulty, GameLevel, Steps) " +
                    "VALUES('" + id.difficulty + "'," + id.level +
                    "," + score +
                    ") ON DUPLICATE KEY UPDATE Steps=" + score;
            return stmt.executeUpdate(s);
        } catch (Exception e) {
            System.out.println("storeToDatabase error");
        }
        return 0;
    }

    
}
