package model;

public class Position {
    public int x, y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }    
    
    public Position translate(Direction d){
        return new Position(x + d.x, y + d.y);
    }
     @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Position other = (Position) obj;
        if (this.x != other.x || this.y != other.y) {
            return false;
        }
       
        return true;
    }
    
}
