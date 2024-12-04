package craig;

import nl.saxion.app.SaxionApp;

public class Tile {
    public static final Tile WALL = new Tile("shadow-labyrinth/Sandbox/resources/images/map/lightWall.png");
    public static final int WALLNUM = 0;

    private String image;
    private int x;
    private int y;

    public Tile(String image) {
        this.image = image;
        x=0;
        y=0;
    }
    public void setPos(int x, int y){
        this.x=x;
        this.y=y;
    }

    @Override
    protected Tile clone() throws CloneNotSupportedException {
        return (Tile)super.clone();
    }

    public void draw(){
        SaxionApp.drawImage(image,x,y);
    }
    public int isOnMe(Player p){
        return 0;
    }
}
