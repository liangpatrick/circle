package Agents;
import Environment.*;
import Environment.Graph;

import java.util.*;

public class CompleteInformation {
    public static boolean agentOne(ArrayList<ArrayList<Graph.Node>> maze){
        Agent agent = new Agent();
        Prey prey = new Prey(agent);
        Predator pred = new Predator(agent);

        while(maze.get(agent.getCell()).get(0).getCell() != prey.getCell()){
            ArrayList<Graph.Node> neighbors = maze.get(agent.getCell());
            ArrayList<Integer> predDistances = new ArrayList<>();
            ArrayList<Integer> preyDistances = new ArrayList<>();
            predDistances.set(0, searchPred(0,pred, maze).size());
            preyDistances.set(0, searchPrey(0, prey, maze).size());
            for(int x = 1; x < neighbors.size(); x++){
                List<Graph.Node> predList = searchPred(x, pred, maze);
                List<Graph.Node> preyList = searchPrey(x, prey, maze);
                predDistances.add(x, predList.size());
                preyDistances.add(x, preyList.size());
            }
            for(int x = 1; x < predDistances.size(); x++){
                int currPred = predDistances.get(x);
                int currPrey = predDistances.get(x);

                if(preyDistances.get(0) > currPrey && predDistances.get(0) < currPred){
                    agent.setCell(neighbors.get(x).getCell());
                    break;
                } else if (preyDistances.get(0) > currPrey && predDistances.get(0) == currPred) {
                    agent.setCell(neighbors.get(x).getCell());
                    break;
                } else if (preyDistances.get(0) == currPrey && predDistances.get(0) < currPred){
                    agent.setCell(neighbors.get(x).getCell());
                    break;
                } else if (preyDistances.get(0) == currPrey && predDistances.get(0) <= currPred){
                    agent.setCell(neighbors.get(x).getCell());
                    break;
                } else if (predDistances.get(0) < currPred){
                    agent.setCell(neighbors.get(x).getCell());
                    break;
                } else if (predDistances.get(0) == currPred){
                    agent.setCell(neighbors.get(x).getCell());
                    break;
                }
            }
//          prey move
            prey.setCell(Prey.choosesNeighbors(prey.getCell(), maze));
//            win
            if(agent.getCell() == prey.getCell()){
                return true;
            }
//            pred move
            ArrayList<Graph.Node> predNeighbors = maze.get(pred.getCell());
            ArrayList<Integer> agentDistances = new ArrayList<>();

            for(int x = 1; x < predNeighbors.size(); x++){
                List<Graph.Node> agentList = Predator.bfs(predNeighbors.get(x).getCell(), agent, maze);
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
            pred.setCell(predNeighbors.get(indices.get(randInt)+1).getCell());
//            dead
            if(agent.getCell() == pred.getCell()){
                return false;
            }

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
            if (maze.get(ind).get(0).getCell() == pred.getCell()) {
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
            if (maze.get(ind).get(0).getCell() == prey.getCell()) {
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
