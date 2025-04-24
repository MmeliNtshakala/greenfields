package za.co.wethinkcode.robots.world;

public class World{
    private static int[] sizeOfWorld;

    public World(){
      sizeOfWorld = new int[2];
      sizeOfWorld[0] = 100;
      sizeOfWorld[1] = 100;
    }

    public int[] getSizeOfWorld(){
      return sizeOfWorld;
    }
}