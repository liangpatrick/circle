package Agents;
import Environment.*;
import Environment.Graph;

import java.util.*;

import static Environment.Predator.getPath;
import static java.util.Comparator.comparingDouble;
import static java.util.Comparator.comparingInt;

public class CompleteInformation {
    public static String agentOne(ArrayList<ArrayList<Graph.Node>> maze){
//        initializes all player positions
        Agent agent = new Agent();
        Prey prey = new Prey(agent);
        Predator predator = new Predator(agent);

        int count = 0;

//        will return only when Agent dies or succeeds
        while(true){
//            hung
//            if (count == 30)
//                return "hung";
//            creates arraylists of neighbors, predator distances, and prey distances
            ArrayList<Graph.Node> neighbors = maze.get(agent.getCell());
            ArrayList<Integer> predatorDistances = new ArrayList<>();
            ArrayList<Integer> preyDistances = new ArrayList<>();
//            adds distances to predator/prey from all neighbors
            for(int x = 0; x < neighbors.size(); x++){
                List<Graph.Node> predatorList = searchPred(neighbors.get(x).getCell(), predator.getCell(), maze);
                List<Graph.Node> preyList = searchPrey(neighbors.get(x).getCell(), prey.getCell(), maze);
                predatorDistances.add(x, predatorList.size());
                preyDistances.add(x, preyList.size());

            }
//            stores distance of agent to predator/prey in a named variable
            int agentToPrey = preyDistances.get(0);
            int agentToPredator = predatorDistances.get(0);
//            used to store arraylist of possible ties
            HashMap<Integer,ArrayList<Integer>> moves = new HashMap<Integer,ArrayList<Integer>>();
//            initializes the arraylists
            for(int x = 0; x < 7; x++)
                moves.put(x, new ArrayList<>());
//            want to start iterating through neighbors and compare to agent position
            for(int x = 1; x < predatorDistances.size(); x++){
                int currPredator = predatorDistances.get(x);
                int currPrey = preyDistances.get(x);
//                logical statements provided in the writeup
                if(currPrey < agentToPrey && currPredator > agentToPredator){
                    moves.get(0).add(neighbors.get(x).getCell());
                } else if (currPrey < agentToPrey && currPredator == agentToPredator) {
                    moves.get(1).add(neighbors.get(x).getCell());
                } else if (currPrey == agentToPrey && currPredator > agentToPredator){
                    moves.get(2).add(neighbors.get(x).getCell());
                } else if (currPrey == agentToPrey && currPredator == agentToPredator){
                    moves.get(3).add(neighbors.get(x).getCell());
                } else if (currPredator > agentToPredator){
                    moves.get(4).add(neighbors.get(x).getCell());
                } else if (currPredator == agentToPredator){
                    moves.get(5).add(neighbors.get(x).getCell());
                } else {
                    moves.get(6).add(agent.getCell());
                }



            }
//          looks for the first non-zero sized list out of all possible moves
            ArrayList<Integer> random = new ArrayList<>();
            for(int x = 0; x < 7; x++){
                if (moves.get(x).size() > 0){
                    random = moves.get(x);
                    break;
                }
            }
//            randomly choose neighbor for ties
             int rand = new Random().nextInt(random.size());
            agent.setCell(random.get(rand));
//            win
            if(agent.getCell() == prey.getCell()){
                return "true";
            }
            if(agent.getCell() == predator.getCell()){
                return "false";
            }
//          prey move
            prey.setCell(Prey.choosesNeighbors(prey.getCell(), maze));
//            win
            if(agent.getCell() == prey.getCell()){
                return "true";
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
                return "false";
            }

            count++;

        }
    }

//  look at Prediction notes!!!!!!!! OR SIMULATE
    public static String agentTwo(ArrayList<ArrayList<Graph.Node>> maze) {
//        initializes all player positions
        Agent agent = new Agent();
        Prey prey = new Prey(agent);
        Predator predator = new Predator(agent);

        int counter = 0;

//        will return only when Agent dies or succeeds
        while(true){
//            hung
            if (counter == 100)
                return "hung";
//            creates arraylists of neighbors, predator distances, and prey distances
            ArrayList<Graph.Node> neighbors = maze.get(agent.getCell());
            ArrayList<Graph.Node> preyNeighbors = maze.get(prey.getCell());

            int cell = bestCell(neighbors, predator, preyNeighbors, maze);


            agent.setCell(cell);
//            win
            if(agent.getCell() == prey.getCell()){
                return "true";
            }
//          prey move
            prey.setCell(Prey.choosesNeighbors(prey.getCell(), maze));
//            win
            if(agent.getCell() == prey.getCell()){
                return "true";
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
                return "false";
            }

            counter++;

        }

    }


//    utility function
    public static int bestCell(ArrayList<Graph.Node> neighbors, Predator predator, ArrayList<Graph.Node> preyNeighbors, ArrayList<ArrayList<Graph.Node>> maze){


        ArrayList<Double> utilities = new ArrayList<>();
//        PriorityQueue<Graph.Node> preyDistances = new PriorityQueue<>(comparingDouble(Graph.Node::getDistance));
//        PriorityQueue<Graph.Node> predatorDistances = new PriorityQueue<>(comparingDouble(Graph.Node::getDistance));

        ArrayList<Double> preyDistances = new ArrayList<>();
        ArrayList<Integer> predatorDistances = new ArrayList<>();
//        collects all preyDistances and predatorDistances
        for(int x = 0; x < neighbors.size(); x++) {
            double aveDistance = 0.0;
//          list of average distances from each possible agent to cluster of prey neighbors
            for (int y = 0; y < preyNeighbors.size(); y++) {
                int currDistance = searchPrey(neighbors.get(x).getCell(), preyNeighbors.get(y).getCell(), maze).size();
                aveDistance += currDistance;


            }
            aveDistance /= preyNeighbors.size();
//            preyDistances.add(new Graph.Node(neighbors.get(x).getCell(), aveDistance));
            preyDistances.add(aveDistance);
            List<Graph.Node> predatorList = searchPred(neighbors.get(x).getCell(), predator.getCell(), maze);
//            predatorDistances.add(new Graph.Node(neighbors.get(x).getCell(),predatorList.size()));
            predatorDistances.add(predatorList.size());
//            System.out.println(predatorList.size() + ", " + aveDistance);
            utilities.add(predatorList.size() - aveDistance);
        }
        double weightPrey   =    1.0/2;
        double weightPredator = -1.0/2;
        for(int x = 0; x < neighbors.size(); x++){
            if(Collections.min(preyDistances) == preyDistances.get(x))
                utilities.set(x, utilities.get(x) + 75*(weightPrey/preyDistances.size()));
            if(Collections.min(predatorDistances) == predatorDistances.get(x))
                utilities.set(x, utilities.get(x) + 100*(weightPredator/predatorDistances.size()));
        }
        if(Collections.max(utilities) > 0)
            return neighbors.get(utilities.indexOf(Collections.max(utilities))).getCell();
//        else if(Collections.max(utilities) == 0)
//            return neighbors.get(0).getCell();
        else
            return neighbors.get(predatorDistances.indexOf(Collections.max(predatorDistances))).getCell();


//        return -1;
    }




//    double agentToPrey = preyDistances.get(0);
//        int agentToPredator = predatorDistances.get(0);
//        HashMap<Integer,ArrayList<Integer>> moves = new HashMap<Integer,ArrayList<Integer>>();
////            initializes the arraylists
//        for(int x = 0; x < 7; x++)
//            moves.put(x, new ArrayList<>());
////            want to start iterating through neighbors and compare to agent position
//        for(int x = 1; x < predatorDistances.size(); x++){
//            int currPredator = predatorDistances.get(x);
//            double currPrey = preyDistances.get(x);
////                logical statements provided in the writeup
//            if(currPrey < agentToPrey && currPredator > agentToPredator){
//                moves.get(0).add(neighbors.get(x).getCell());
//            } else if (currPrey < agentToPrey && currPredator == agentToPredator) {
//                moves.get(1).add(neighbors.get(x).getCell());
//            } else if (currPrey == agentToPrey && currPredator > agentToPredator){
//                moves.get(2).add(neighbors.get(x).getCell());
//            } else if (currPrey == agentToPrey && currPredator == agentToPredator){
//                moves.get(3).add(neighbors.get(x).getCell());
//            } else if (currPredator > agentToPredator){
//                moves.get(4).add(neighbors.get(x).getCell());
//            } else if (currPredator == agentToPredator){
//                moves.get(5).add(neighbors.get(x).getCell());
//            } else {
//                moves.get(6).add(neighbors.get(0).getCell());
//            }
//
//
//
//        }
////          looks for the first non-zero sized list out of all possible moves
//        ArrayList<Integer> random = new ArrayList<>();
//        for(int x = 0; x < 7; x++){
//            if (moves.get(x).size() > 0){
//                random = moves.get(x);
//                break;
//            }
//        }
////            randomly choose neighbor for ties
//        int rand = new Random().nextInt(random.size());
//
//
//
////        compare utilities of all neighbor cells














//    public static String agentTwo(ArrayList<ArrayList<Graph.Node>> maze){
//        //        initializes all player positions
//        Agent agent = new Agent();
//        Prey prey = new Prey(agent);
//        Predator predator = new Predator(agent);
//
//        int counter = 0;
//
////        will return only when Agent dies or succeeds
//        while(true){
////            hung
////            if (counter == 30)
////                return "hung";
////            creates arraylists of neighbors, predator distances, and prey distances
//            ArrayList<Graph.Node> neighbors = maze.get(agent.getCell());
//            ArrayList<Integer> predatorDistances = new ArrayList<>();
//            ArrayList<Integer> preyDistances = new ArrayList<>();
////            adds distances to predator/prey from all neighbors
//            HashSet<Graph.Node> network = network(prey.getCell(), maze, 1);
//            // convert HashSet to an array
//            Graph.Node[] arrayNumbers = network.toArray(new Graph.Node[network.size()]);
//            for(int x = 0; x < neighbors.size(); x++){
//                int randomPrey = arrayNumbers[new Random().nextInt(network.size())].getCell();
//
////                int count = 0;
////                PriorityQueue<Integer> temp = new PriorityQueue<>();
////                int size = 0;
////                for(Graph.Node preyNet: network) {
////                    if(count == randomPrey) {
//////                        List<Graph.Node> preyList = searchPrey(neighbors.get(x).getCell(), preyNet.getCell(), maze);
//////                        temp.add(preyList.size());
////                        size = searchPrey(neighbors.get(x).getCell(), preyNet.getCell(), maze).size();
////                        break;
////                    }
////                    count++;
////                }
//                List<Graph.Node> predatorList = searchPred(neighbors.get(x).getCell(), predator.getCell(), maze);
//                predatorDistances.add(x, predatorList.size());
//                preyDistances.add(x, searchPrey(neighbors.get(x).getCell(), randomPrey, maze).size());
//            }
////            stores distance of agent to predator/prey in a named variable
//            int agentToPrey = preyDistances.get(0);
//            int agentToPredator = predatorDistances.get(0);
////            used to store arraylist of possible ties
//            HashMap<Integer,ArrayList<Integer>> moves = new HashMap<Integer,ArrayList<Integer>>();
////            initializes the arraylists
//            for(int x = 0; x < 7; x++)
//                moves.put(x, new ArrayList<>());
////            want to start iterating through neighbors and compare to agent position
//            for(int x = 1; x < predatorDistances.size(); x++){
//                int currPredator = predatorDistances.get(x);
//                int currPrey = preyDistances.get(x);
////                logical statements provided in the writeup
//                if(currPrey < agentToPrey && currPredator > agentToPredator){
//                    moves.get(0).add(neighbors.get(x).getCell());
//                } else if (currPrey < agentToPrey && currPredator == agentToPredator) {
//                    moves.get(1).add(neighbors.get(x).getCell());
//                } else if (currPrey == agentToPrey && currPredator > agentToPredator){
//                    moves.get(2).add(neighbors.get(x).getCell());
//                } else if (currPrey == agentToPrey && currPredator == agentToPredator){
//                    moves.get(3).add(neighbors.get(x).getCell());
//                } else if (currPredator > agentToPredator){
//                    moves.get(4).add(neighbors.get(x).getCell());
//                } else if (currPredator == agentToPredator){
//                    moves.get(5).add(neighbors.get(x).getCell());
//                } else {
//                    moves.get(6).add(agent.getCell());
//                }
//
//
//
//            }
////          looks for the first non-zero sized list out of all possible moves
//            ArrayList<Integer> random = new ArrayList<>();
//            for(int x = 0; x < 7; x++){
//                if (moves.get(x).size() > 0){
//                    random = moves.get(x);
//                    break;
//                }
//            }
////            randomly choose neighbor for ties
//            int rand = new Random().nextInt(random.size());
//            agent.setCell(random.get(rand));
////            win
//            if(agent.getCell() == prey.getCell()){
//                return "true";
//            }
////          prey move
//            prey.setCell(Prey.choosesNeighbors(prey.getCell(), maze));
////            win
//            if(agent.getCell() == prey.getCell()){
//                return "true";
//            }
////            pred move
//            ArrayList<Graph.Node> predatorNeighbors = maze.get(predator.getCell());
//            ArrayList<Integer> agentDistances = new ArrayList<>();
//
//            for(int x = 1; x < predatorNeighbors.size(); x++){
//                List<Graph.Node> agentList = Predator.bfs(predatorNeighbors.get(x).getCell(), agent, maze);
//                agentDistances.add(agentList.size());
//            }
////            randomly choose neighbor for ties
//            int min = Collections.min(agentDistances);
//
//            ArrayList<Integer> indices = new ArrayList<>();
//            for(int x = 0; x < agentDistances.size(); x++){
//                if (agentDistances.get(x) == min){
//                    indices.add(x);
//                }
//            }
//            int randInt = new Random().nextInt(indices.size());
//            predator.setCell(predatorNeighbors.get(indices.get(randInt)+1).getCell());
////            dead
//            if(agent.getCell() == predator.getCell()){
//                return "false";
//            }
//
//            counter++;
//
//        }
//    }

    public static HashSet<Graph.Node> network(int origin, ArrayList<ArrayList<Graph.Node>> maze, int reach){
        HashSet<Graph.Node> network = new HashSet<>();
        Queue<Graph.Node> fringe = new LinkedList<>();
        HashSet<Integer> visited = new HashSet();
        fringe.add(new Graph.Node(origin, 0));
        network.add(new Graph.Node(origin));
        visited.add(origin);
        while(!fringe.isEmpty()){
//        for(int x = 0; x < reach*maze.get(prey.getCell()).size(); x++) {
//          use poll instead of remove so no errors are thrown
            Graph.Node curr = fringe.poll();
            int ind = curr.getCell();
            if (curr.level == reach + 1){
                return network;
            }
//          if arrived at destination
//            if (ind == pred.getCell()) {
//                List<Graph.Node> path = new ArrayList<>();;
//                getPath(curr, path);
//                return path;
//            }
//          checks all neighbors to see if they are eligible to be added to the fringe
            List<Graph.Node> edges = maze.get(ind).subList(1, maze.get(ind).size());
            for(Graph.Node n: edges)
            {
                if(!visited.contains(n.getCell()) && curr.level + 1 <= reach) {
                    visited.add(n.getCell());
                    fringe.add(new Graph.Node(n.getCell(), curr.level + 1));
                    network.add(new Graph.Node(n.getCell()));
                }
            }

        }
        return network;

    }

    public static List<Integer> findShortestPair(ArrayList<Graph.Node> agentNeighbors, HashSet<Graph.Node> preyNetwork, ArrayList<ArrayList<Graph.Node>> maze){
        List<Integer> shortest = null;
        int smallestPath = Integer.MAX_VALUE;
        for(Graph.Node prey: preyNetwork){

            for(int x = 0; x < agentNeighbors.size(); x++){
                int size = searchPrey(agentNeighbors.get(x).getCell(), prey.getCell(), maze).size();
                if( smallestPath > size) {
                    smallestPath = size;
                    shortest = List.of(agentNeighbors.get(x).getCell(), prey.getCell());
                }
            }

        }

        return shortest;
    }


    public static List<Graph.Node> searchPred(int start, int pred, ArrayList<ArrayList<Graph.Node>> maze){
//      fringe to store cells that need to be visited
        Queue<Graph.Node> fringe = new LinkedList<>();
//        hashset of visited for o(1) lookup
        HashSet<Integer> visited = new HashSet();

//      add beginning cell to fringe and visited
        fringe.add(new Graph.Node(start, null));
        visited.add(start);
        while(!fringe.isEmpty()) {
//          use poll instead of remove so no errors are thrown
            Graph.Node curr = fringe.poll();
            int ind = curr.getCell();
//          if arrived at destination
            if (ind == pred) {
                List<Graph.Node> path = new ArrayList<>();;
                getPath(curr, path);
                return path;
            }
//          checks all neighbors to see if they are eligible to be added to the fringe
            List<Graph.Node> edges = maze.get(ind).subList(1, maze.get(ind).size());
            for(Graph.Node n: edges)
            {
                if(!visited.contains(n.getCell())) {
                    visited.add(n.getCell());
                    fringe.add(new Graph.Node(n.getCell(), curr));
                }
            }

        }
//        System.out.println("No path");
        return null;
    }
    public static List<Graph.Node> searchPrey(int start, int prey, ArrayList<ArrayList<Graph.Node>> maze){
//      fringe to store cells that need to be visited
        Queue<Graph.Node> fringe = new LinkedList<>();
        HashSet<Integer> visited = new HashSet();
//      add beginning cell to fringe and visited
        fringe.add(new Graph.Node(start, null));
        visited.add(start);
        while(!fringe.isEmpty()) {
//          use poll instead of remove so no errors are thrown
            Graph.Node curr = fringe.poll();
            int ind = curr.getCell();
//          if arrived at destination
            if (ind == prey) {
                List<Graph.Node> path = new ArrayList<>();;
                getPath(curr, path);
                return path;
            }
//          checks all neighbors to see if they are eligible to be added to the fringe
            List<Graph.Node> edges = maze.get(ind).subList(1, maze.get(ind).size());
            for(Graph.Node n: edges)
            {
                if(!visited.contains(n.getCell())) {
                    visited.add(n.getCell());
                    fringe.add(new Graph.Node(n.getCell(), curr));
                }
            }

        }
        return null;
    }





}
