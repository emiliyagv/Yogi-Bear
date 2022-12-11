package model;

public enum LevelElements {
    RANGER('$'), TREE('*'), BASKET('.'), MOUNTAIN('#'), EMPTY(' ');
    LevelElements(char rep){ representation = rep; }
    public final char representation;
}
