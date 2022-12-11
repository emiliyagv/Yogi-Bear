/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import database.HighScore;

public class HighScoresTableModel extends AbstractTableModel{
    private final ArrayList<HighScore> highScores;
    private final String[] colName = new String[]{ "Difficulty Level", "GameLevel", "Steps" };
    
    public HighScoresTableModel(ArrayList<HighScore> highScores){
        this.highScores = highScores;
    }

    @Override
    public int getRowCount() { return highScores.size(); }

    @Override
    public int getColumnCount() { return 3; }

    @Override
    public Object getValueAt(int r, int c) {
        HighScore h = highScores.get(r);
        if      (c == 0) return h.difficulty;
        else if (c == 1) return h.level;
        return (h.steps == 0) ? "" : h.steps;
    }

    @Override
    public String getColumnName(int i) { return colName[i]; }    
    
}
