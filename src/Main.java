import Agents.CompleteInformation;
import Environment.*;

import java.util.ArrayList;

public class Main {
    public static void main (String args[]) {
        ArrayList<ArrayList<Graph.Node>> maze = Graph.buildGraph();
//        Graph.printGraph(maze);

        int success = 0;
//        int hung = 0;
//        double fail = 0;
        long total = System.nanoTime();
        for(int x = 1; x <= 3000; x++){
//            long startTime = System.nanoTime();
            String result = CompleteInformation.agentOne(Graph.buildGraph());
            if(result.equals("true"))
                success++;
//            else if(result.equals("hung"))
//                hung++;
//            else
//                fail++;


//            long endTime = System.nanoTime();
//            long duration = (endTime - startTime)/(long)Math.pow(10,9);
//            System.out.println(x + "; " + success + "; " +duration);
        }
        long endTime = System.nanoTime();
        long duration = (endTime - total)/(long)Math.pow(10,9);
        System.out.println(duration);
//        System.out.println(hung);
//        System.out.println("Hung out of Loss: " + hung/fail);
        System.out.println(success/3000.0);



//        Agent agent = new Agent();
//        Prey prey = new Prey(agent);
//        System.out.println(agent.getCell());
//        System.out.println(prey.getCell());
////        System.out.println(maze.get(prey.getCell()));
////        prey.setCell(Prey.choosesNeighbors(prey.getCell(), Graph.buildGraph()));
////        System.out.println(prey.getCell());
//
//        CompleteInformation.searchPrey(agent.getCell(), prey, maze);



//        Agent agent = new Agent();
//        Predator pred = new Predator(agent);
//        System.out.println(agent.getCell());
//        System.out.println(pred.getCell());
////        System.out.println(maze.get(prey.getCell()));
////        prey.setCell(Prey.choosesNeighbors(prey.getCell(), Graph.buildGraph()));
////        System.out.println(prey.getCell());
//        Predator.bfs(pred.getCell(), agent, maze);
////        CompleteInformation.searchPred(agent.getCell(), pred, maze);
    }

}
