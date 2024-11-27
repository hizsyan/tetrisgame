package tetrisgame;

public class TetroShape {
    private Coord[] relative;
    private boolean[][] absolute;

    public TetroShape(int i){
        int[][][] shapes = 
        {
            {{0,0},{0,1},{1,0},{1,1}},
            {{0,0},{0,1},{0,2},{0,3}},
            {{0,0},{1,0},{-1,1},{0,1}},
            {{0,0},{0,1},{0,2},{1,2}},
            {{0,0},{0,1},{0,2},{-1,2}},
            {{0,0},{1,0},{2,0},{1,1}},
            {{0,0},{1,0},{1,1},{1,2}}
        };
        this.relative = new Coord[4];
        for(int j = 0; j<4;j++){
            try{
                this.relative[j] = new Coord(shapes[i][j]);
            }
            catch(Exception e){}
        }
    }

    private Coord calcTopLeft(){
        for(int row = 0; row<4;row++){
            for(int col = 0; col<4;col++){
                if(this.absolute[row][col]){
                    return new Coord(row, col);
                }
            }
        }
        return new Coord(-1,-1);
    }

    private void calcRelative(){
        Coord topleft = calcTopLeft();
        for(int row = 0; row<4;row++){
            for(int col = 0; col < 4; col++){
                Coord current = new Coord(row,col);
                if(absolute[row][col] && !topleft.equals(current)){
                    this.relative[this.relative.length-1] = current;
                }
            }
        }
    }

    public void rotateClock(){
        Coord[] newcoords = new Coord[3];
        for(int curr = 0; curr<4;curr++){
            Coord c = this.relative[curr];
            Coord make = new Coord(-c.getY(), c.getX());
            newcoords[0] = make;
        }
    }

    public Coord[] getRelative(){
        return relative;
    }

    public boolean[][] getAbsolute(){
        return absolute;
    }

}
