package Agents;

import Environment.Agent;
import Environment.Graph;
import Environment.Predator;
import Environment.Prey;

import java.util.*;

import static Agents.CompleteInformation.searchPred;
import static Agents.CompleteInformation.searchPrey;

public class PartialPrey {
    static double[] belief = new double[50];
    static double[][] transMatrix = new double[50][50];
    public static String agentThree(ArrayList<ArrayList<Graph.Node>> maze){
//        initializes all player positions
        Agent agent = new Agent();
        Prey prey = new Prey(agent);
        Predator predator = new Predator(agent);
        initTransMatrix(maze);
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
            }
            matmul();

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
            bayes(false,  agent.getCell(), agent);
            matmul();
//            System.out.println(beliefSum(belief));
//          prey move

            prey.setCell(Prey.choosesNeighbors(prey.getCell(), maze));
//            win
            if(agent.getCell() == prey.getCell()){
                return "true";
            }
            bayes(false, agent.getCell(), agent);
            matmul();
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

//    sums up belief for normalization and error checking
    public static double beliefSum(double[] array) {
        return Arrays.stream(array).sum();
    }
//    never changes
    public static void initTransMatrix(ArrayList<ArrayList<Graph.Node>> maze){
        for(int x = 0; x < maze.size(); x++){
            for(int y = 0; y < maze.get(x).size(); y++){
                transMatrix[maze.get(x).get(0).getCell()][maze.get(x).get(y).getCell()] = 1.0/(maze.get(x).size());
            }
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

//    updates belief after no new info
    public static void matmul(){
        double[] arr = belief.clone();
        for(int x = 0; x < 50; x++){
            belief[x] = dotProduct(x, arr);
        }
//        normalization
        double sum = beliefSum(belief);
        for(int x = 0; x < 50; x++){
            belief[x] /= sum;

        }

    }
//    dot product to update belief with transMatrix
    public static double dotProduct(int row, double[] temp) {
        double sum = 0;
        for (int x = 0; x < 50; x++) {
            sum += temp[x] * transMatrix[row][x];

        }

        return sum;

    }
}