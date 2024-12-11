//package craig;
//
//public class Map {
//    Tile[][] tiles = new Tile[50][50];
//
//    public Map() {
//        for (int i = 0; i < tiles.length; i++) {
//            for (int j = 0; j < tiles[i].length; j++) {
//                try {
//                    tiles[i][j] = Tile.WALL.clone();
//                    tiles[i][j].setPos(i*50,j*50);
//                } catch (CloneNotSupportedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }
//    }
//
//    public void draw(Player player) {
//
//        for (int i = player.x -10; i < player.x+10; i++) {
//            for (int j = player.y-10; j < player.y+10; j++) {
//                tiles[i][j].draw();
//            }
//        }
//    }
//}