package NonAgents;

import java.util.ArrayList;
import java.util.Random;

public class Prey {
//    randomly chooses between itself and its neighbors
    public static Graph.Node choosesNeighbors(int cell, ArrayList<ArrayList<Graph.Node>> maze){
        int randInt = new Random().nextInt(maze.get(cell).size());
        return maze.get(cell).get(randInt);
    }
}
