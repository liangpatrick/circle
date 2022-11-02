package Agents;

import Environment.Agent;
import Environment.Graph;
import Environment.Predator;
import Environment.Prey;

import java.util.*;

import static Agents.CompleteInformation.searchPred;
import static Agents.CompleteInformation.searchPrey;
import static Agents.PartialPrey.matmul;
import static Environment.Predator.getPath;

public class PartialPredator {
    static double[] belief = new double[50];
    static double[][] transMatrix = new double[50][50];
    public static String agentFive(ArrayList<ArrayList<Graph.Node>> maze){
//        initializes all player positions
        Agent agent = new Agent();
        Prey prey = new Prey(agent);
        Predator predator = new Predator(agent);
        updateTransMatrix(maze);
        initialBelief(agent.getCell());

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
//            random survey
            int surveyedNode = randomSurvey();
            if(prey.getCell() == surveyedNode){
                bayes(true, prey.getCell(), agent);
            } else {
                bayes(false, surveyedNode, agent);
                matmul();
            }
//            matmul();

//            adds distances to predator/prey from all neighbors
            for(int x = 0; x < neighbors.size(); x++){
                List<Graph.Node> predatorList = searchPred(neighbors.get(x).getCell(), predator.getCell(), maze);
                List<Graph.Node> preyList = searchPrey(neighbors.get(x).getCell(), maxIndex(maxBelief()), maze);
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
//            matmul();
            bayes(false,  agent.getCell(), agent);
            matmul();
//            System.out.println(beliefSum(belief));
//          prey move

            prey.setCell(Prey.choosesNeighbors(prey.getCell(), maze));
//            win
            if(agent.getCell() == prey.getCell()){
                return "true";
            }
//            bayes(false, agent.getCell(), agent);
//            matmul();
//            System.out.println(beliefSum(belief));


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


//            return "false";

        }

//        return "false";
    }

    //    updates belief when new node is surveyed; Phrases: found, moving, not found
    public static void bayes(boolean found, int cell, Agent agent){

        if (found){
            for (int x = 0; x < belief.length; x++) {
                if (cell == x) {
                    belief[x] = 1.0;
                } else {
                    belief[x] = 0.0;
                }

            }

        } else {
            double removedProbability = belief[cell];
            for (int x = 0; x < belief.length; x++) {
                if(x == agent.getCell() || x == cell){
                    belief[x] = 0;
                }  else {
                    belief[x] /= (1-removedProbability);
                }

            }


        }
//        else if(found.equals("not found")){
//            for(int x = 0; x < belief.length; x++){
//                if(x == agent.getCell()){
//                    belief[x] = 0;
//                } else if(cell == x){
//                    belief[x] = 0;
//                } else {
//                    belief[x] /= size;
//                }
//            }
//        }

    }

//    return denominator by counting non-zero cells

    public static int maxIndex(double value){
        for(int x = 0; x < belief.length; x++){
            if(value == belief[x])
                return x;
        }
        return -1;
    }

    public static double maxBelief(){
        return Arrays.stream(belief).max().getAsDouble();
    }



    //    initializes 1/49 for every non-agent cell
    public static void initialBelief(int agentCell){
        for(int x = 0; x < belief.length; x++){
            if(x != agentCell) {
                belief[x] = 1.0 / (49);
            }
            else {
                belief[x] = 0.0;
            }
//            System.out.println(belief[x]);

        }
    }


    //    returns random cell that has the highest likelihood of being prey
    public static int randomSurvey(){
        ArrayList<Integer> indices = new ArrayList<>();
        for(int x = 0; x < belief.length; x++){
//            stores all indices that are have a probability of having prey
            if(belief[x] > 0){
                indices.add(x);
            }
        }
//        randomly chooses index from arraylist
        int randInt = new Random().nextInt(indices.size());
        return indices.get(randInt);
    }

    //    never changes
    public static void updateTransMatrix(ArrayList<ArrayList<Graph.Node>> maze){
        for(int x = 0; x < maze.size(); x++){
            for(int y = 0; y < maze.get(x).size(); y++){
//                transMatrix[x][y] = 1.0/(maze.get(x).size());

//                SOMETHING ELSE; CHECK NOTES;




            }
        }
    }

    public static List<Graph.Node> bfs(int start, Agent agent, ArrayList<ArrayList<Graph.Node>> maze){
//      fringe to store cells that need to be visited
        Queue<Graph.Node> fringe = new LinkedList<>();
        HashSet<Integer> visited = new HashSet();

//      add beginning cell to fringe and visited
        fringe.add(new Graph.Node(start,null));
        visited.add(start);
        while(!fringe.isEmpty()) {
//          use poll instead of remove so no errors are thrown
            Graph.Node curr = fringe.poll();
            int ind = curr.getCell();
//          if arrived at destination
            if (agent.getCell() == ind) {
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
