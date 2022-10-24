package Environment;

import java.util.ArrayList;
import java.util.Random;

public class Prey {
    int cell;
    public Prey(Agent agent){
        int prey = new Random().nextInt(49) + 1;
        while(prey == agent.getCell()){
            prey = new Random().nextInt(49) + 1;
        }
        this.cell = prey;
    }

    public int getCell(){
        return cell;
    }
    public void setCell(int cell){
        this.cell = cell;
    }

//    randomly chooses between itself and its neighbors
    public static int choosesNeighbors(int cell, ArrayList<ArrayList<Graph.Node>> maze){
        int newCell = new Random().nextInt(maze.get(cell).size());
        return newCell;
    }
}