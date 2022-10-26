package Agents;
import Environment.*;
import Environment.Graph;

import java.lang.reflect.Array;
import java.util.*;

public class CompleteInformation {
    public static boolean agentOne(ArrayList<ArrayList<Graph.Node>> maze){
        Agent agent = new Agent();
//        System.out.println(agent.getCell());
        Prey prey = new Prey(agent);
//        System.out.println(prey.getCell());
        Predator predator = new Predator(agent);
        int count = 0;
        while(agent.getCell() != prey.getCell()){
//            System.out.println("Agent: " + agent.getCell() + "; Prey: " + prey.getCell() + "; Pred: " + predator.getCell());
            if(count == 99)
                return false;
            ArrayList<Graph.Node> neighbors = maze.get(agent.getCell());
            ArrayList<Integer> predatorDistances = new ArrayList<>();
            ArrayList<Integer> preyDistances = new ArrayList<>();
            predatorDistances.add(0, searchPred(agent.getCell(),predator, maze).size());
            preyDistances.add(0, searchPrey(agent.getCell(), prey, maze).size());
            int agentToPrey = preyDistances.get(0);
            int agentToPredator = predatorDistances.get(0);
            for(int x = 1; x < neighbors.size(); x++){
                List<Graph.Node> predatorList = searchPred(neighbors.get(x).getCell(), predator, maze);
                List<Graph.Node> preyList = searchPrey(neighbors.get(x).getCell(), prey, maze);
                predatorDistances.add(x, predatorList.size());
                preyDistances.add(x, preyList.size());
//                System.out.print(x + " ");

            }
//            System.out.println();
//            agent.setCell(neighbors.get(preyDistances.indexOf(Collections.min(preyDistances))).getCell());
            HashMap<Integer,ArrayList<Integer>> moves = new HashMap<Integer,ArrayList<Integer>>();
            for(int x = 0; x < 7; x++)
                moves.put(x, new ArrayList<>());
//            System.out.println(predatorDistances);
//            System.out.println(preyDistances);
            for(int x = 1; x < predatorDistances.size(); x++){
                int currPredator = predatorDistances.get(x);
                int currPrey = preyDistances.get(x);
                if(currPrey < agentToPrey && currPredator > agentToPredator){
//                    agent.setCell(neighbors.get(x).getCell());
                    moves.get(0).add(neighbors.get(x).getCell());
                } else if (currPrey < agentToPrey && currPredator == agentToPredator) {
//                    agent.setCell(neighbors.get(x).getCell());
                    moves.get(1).add(neighbors.get(x).getCell());
                } else if (currPrey == agentToPrey && currPredator > agentToPredator){
                    moves.get(2).add(neighbors.get(x).getCell());
//                    agent.setCell(neighbors.get(x).getCell());
                } else if (currPrey == agentToPrey && currPredator == agentToPredator){
//                    agent.setCell(neighbors.get(x).getCell());
                    moves.get(3).add(neighbors.get(x).getCell());
                } else if (currPredator > agentToPredator){
//                    agent.setCell(neighbors.get(x).getCell());
                    moves.get(4).add(neighbors.get(x).getCell());
                } else if (currPredator == agentToPredator){
//                    agent.setCell(neighbors.get(x).getCell());
                    moves.get(5).add(neighbors.get(x).getCell());
                } else {
                    moves.get(6).add(agent.getCell());
                }



            }
            for(int x = 0; x < 7; x++){
//                System.out.println(x + ": " + moves.get(x));
            }
//            System.out.println(agent.getCell());
            ArrayList<Integer> random = new ArrayList<>();
            for(int x = 0; x < 7; x++){
                if (moves.get(x).size() > 0){
                    random = moves.get(x);
                    break;
                }
            }
//            System.out.println(random);
            //            randomly choose neighbor for ties
             int rand = new Random().nextInt(random.size());
            agent.setCell(random.get(rand));
//            System.out.println(agent.getCell());
//            if(true)
//                return false;






//          prey move
            prey.setCell(Prey.choosesNeighbors(prey.getCell(), maze));
//            win
            if(agent.getCell() == prey.getCell()){
                return true;
            }
//            pred move
            ArrayList<Graph.Node> predatorNeighbors = maze.get(predator.getCell());
            ArrayList<Integer> agentDistances = new ArrayList<>();

            for(int x = 1; x < predatorNeighbors.size(); x++){
                List<Graph.Node> agentList = Predator.bfs(predatorNeighbors.get(x).getCell(), agent, maze);
                agentDistances.add(agentList.size());
            }
//            randomly choose neighbor for ties
            int min = Collections.min(agentDistances);
            ArrayList<Integer> indices = new ArrayList<>();
            for(int x = 0; x < agentDistances.size(); x++){
                if (agentDistances.get(x) == min){
                    indices.add(x);
                }
            }
            int randInt = new Random().nextInt(indices.size());
            predator.setCell(predatorNeighbors.get(indices.get(randInt)+1).getCell());
//            dead
            if(agent.getCell() == predator.getCell()){
                return false;
            }
            count++;

        }
//        win??
        return true;
    }
    public static List<Graph.Node> searchPred(int start, Predator pred, ArrayList<ArrayList<Graph.Node>> maze){
//      fringe to store cells that need to be visited
        Queue<Graph.Node> fringe = new LinkedList<>();
//      originally used a 2d array but space complexity is too huge, so it is now a set.
//        boolean[][] visited = new boolean[maze.length][maze[0].length];
        HashSet<Integer> visited = new HashSet();
//      used to verify if end source has been achieved

//      add beginning cell to fringe and visited
        fringe.add(new Graph.Node(start, 0));
        visited.add(start);
        while(!fringe.isEmpty()) {
//          use poll instead of remove so no errors are thrown
            Graph.Node curr = fringe.poll();
//          indX and indY hold current positions, didn't want to keep using curr.x or curr.y for laziness sakes
            int ind = curr.getCell();
//          if arrived at destination
            if (ind == pred.getCell()) {
//                System.out.println(ind);
                List<Graph.Node> path = new ArrayList<>();;
                getPath(curr, path);
                return path;
            }
//          checks all neighbors to see if they are eligible to be added to the fringe
            List<Graph.Node> edges = maze.get(ind).subList(1, maze.get(ind).size());
            for(Graph.Node n: edges)
            {
                visited.add(n.getCell());
                fringe.add(new Graph.Node(n.getCell(), curr));

            }

        }
//        System.out.println("No path");
        return null;
    }
    public static List<Graph.Node> searchPrey(int start, Prey prey, ArrayList<ArrayList<Graph.Node>> maze){
//      fringe to store cells that need to be visited
        Queue<Graph.Node> fringe = new LinkedList<>();
//      originally used a 2d array but space complexity is too huge, so it is now a set.
//        boolean[][] visited = new boolean[maze.length][maze[0].length];
        HashSet<Integer> visited = new HashSet();
//      used to verify if end source has been achieved

//      add beginning cell to fringe and visited
        fringe.add(new Graph.Node(start, 0));
        visited.add(start);
        while(!fringe.isEmpty()) {
//          use poll instead of remove so no errors are thrown
            Graph.Node curr = fringe.poll();
//          indX and indY hold current positions, didn't want to keep using curr.x or curr.y for laziness sakes
            int ind = curr.getCell();
//          if arrived at destination
            if (ind == prey.getCell()) {
//                System.out.println(ind);
                List<Graph.Node> path = new ArrayList<>();;
                getPath(curr, path);
                return path;
            }
//          checks all neighbors to see if they are eligible to be added to the fringe
            List<Graph.Node> edges = maze.get(ind).subList(1, maze.get(ind).size());
            for(Graph.Node n: edges)
            {
                visited.add(n.getCell());
                fringe.add(new Graph.Node(n.getCell(), curr));

            }

        }
//        System.out.println("No path");
        return null;
    }
    private static void getPath(Graph.Node node, List<Graph.Node> path){
        if (node != null){
            getPath(node.prev, path);
            path.add(node);

        }
    }





}
