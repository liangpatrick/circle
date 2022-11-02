import Agents.CompleteInformation;
import Agents.PartialPrey;
import Environment.*;

import java.util.*;

public class Main {
    public static void runOne(){
        int success = 0;
        int hung = 0;
        double fail = 0;
        long total = System.nanoTime();
        for(int x = 1; x <= 3000; x++){
//            long startTime = System.nanoTime();
            String result = CompleteInformation.agentOne(Graph.buildGraph());
            if(result.equals("true"))
                success++;
            else if(result.equals("hung"))
                hung++;
            else
                fail++;


//            long endTime = System.nanoTime();
//            long duration = (endTime - startTime)/(long)Math.pow(10,9);
//            System.out.println(x + "; " + success + "; " +duration);
        }
//        long endTime = System.nanoTime();
//        long duration = (endTime - total)/(long)Math.pow(10,9);
//        System.out.println(duration);
//        System.out.println(hung);
//        System.out.println("Hung out of Loss: " + hung/fail);
        System.out.println("A1: " + success/3000.0);
    }
    public static void runTwo(){
        int success = 0;
        int hung = 0;
        double fail = 0;
        long total = System.nanoTime();
        for(int x = 1; x <= 3000; x++){
            long startTime = System.nanoTime();
            String result = CompleteInformation.agentTwo(Graph.buildGraph());
            if(result.equals("true"))
                success++;
            else if(result.equals("hung"))
                hung++;
            else
                fail++;


//            long endTime = System.nanoTime();
//            long duration = (endTime - startTime)/(long)Math.pow(10,9);
//            System.out.println(x + "; " + success + "; " +duration);
        }
//        long endTime = System.nanoTime();
//        long duration = (endTime - total)/(long)Math.pow(10,9);
//        System.out.println(duration);
//        System.out.println(hung);
//        System.out.println("Hung out of Loss: " + hung/fail);
        System.out.println("A2: " + success/3000.0);
    }
    public static void runThree(){
        int success = 0;
        int hung = 0;
        double fail = 0;
        long total = System.nanoTime();
        for(int x = 1; x <= 3000; x++){
            long startTime = System.nanoTime();
            String result = PartialPrey.agentThree(Graph.buildGraph());
            if(result.equals("true"))
                success++;
            else if(result.equals("hung"))
                hung++;
            else
                fail++;


//            long endTime = System.nanoTime();
//            long duration = (endTime - startTime)/(long)Math.pow(10,9);
//            System.out.println(x + "; " + success + "; " +duration);
        }
        long endTime = System.nanoTime();
        long duration = (endTime - total)/(long)Math.pow(10,9);
        System.out.println(duration);
//        System.out.println(hung);
//        System.out.println("Hung out of Loss: " + hung/fail);
        System.out.println("A3: " + success/3000.0);
    }

    public static void runAll(){
        int[] success = new int[10];
        int[] hung = new int[10];
        int[] fail = new int[10];

        long total = System.nanoTime();

        for(int graphs = 1; graphs <= 100; graphs++){
            ArrayList<ArrayList<Graph.Node>> maze = Graph.buildGraph();
            for(int runs = 1; runs <= 30; runs++) {
                String result = CompleteInformation.agentOne(maze);
                if (result.equals("true"))
                    success[1]++;
                else if (result.equals("hung"))
                    hung[1]++;
                else
                    fail[1]++;
                result = CompleteInformation.agentTwo(maze);
                if (result.equals("true"))
                    success[2]++;
                else if (result.equals("hung"))
                    hung[2]++;
                else
                    fail[2]++;
                result = PartialPrey.agentThree(maze);
                if (result.equals("true"))
                    success[3]++;
                else if (result.equals("hung"))
                    hung[3]++;
                else
                    fail[3]++;
//                result = PartialPrey.agentFour(maze);
//                if (result.equals("true"))
//                    success[3]++;
//                else if (result.equals("hung"))
//                    hung[3]++;
//                else
//                    fail[3]++;
            }

        }
        long endTime = System.nanoTime();
        long duration = (endTime - total)/(long)Math.pow(10,9);
        System.out.println(duration);


    }


    public static void main (String args[]) {
        ArrayList<ArrayList<Graph.Node>> maze = Graph.buildGraph();
//        Graph.printGraph(maze);
//        long total = System.nanoTime();
//        for(int x = 0; x < 100; x++) {
//            runOne();
//            runTwo();
//        }
//
//        long endTime = System.nanoTime();
//        long duration = (endTime - total)/(long)Math.pow(10,9);
//        System.out.println(duration);

        runThree();

//        String result = PartialPrey.agentThree(maze);
//        if(result.equals("true"))
//            System.out.println("success");




//        Agent agent = new Agent();
//        Prey prey = new Prey(agent);
//        HashSet<Graph.Node> network = CompleteInformation.network(prey.getCell(), maze, 2);
//        // convert HashSet to an array
//        Graph.Node[] arrayNumbers = network.toArray(new Graph.Node[network.size()]);
//
//        // generate a random number
//
//        // this will generate a random number between 0 and
//        // HashSet.size - 1
//            int randomPrey = arrayNumbers[new Random().nextInt(network.size())].getCell();
//        System.out.println(network);
//        System.out.println(randomPrey);
//        Agent agent = new Agent();
//        Prey prey = new Prey(agent);
//
//        System.out.println(prey.getCell());
//        int reach = 2;
//        HashSet<Graph.Node> network = new HashSet<>();
//        ArrayList<Graph.Node> start = maze.get(prey.getCell());
//        network.add(start.get(0));
//        for(int x = 1; x < start.size(); x++){
//            ArrayList<Graph.Node> temp = maze.get(start.get(x).getCell());
//            network.add(start.get(x));
//
//            for(int y = 1; y < temp.size(); y++){
//                network.add(temp.get(y));
//            }
//
//        }
//        System.out.println(network);

//        System.out.println("No path");













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
