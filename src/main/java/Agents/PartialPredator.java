package Agents;

import Environment.*;


import java.util.*;

import static Agents.CompleteInformation.searchPred;
import static Agents.CompleteInformation.searchPrey;
import static Environment.Predator.bfs;

public class PartialPredator {
    static double[] belief = new double[50];
    static double[][] transMatrix = new double[50][50];
    static double[][] randTransMatrix = new double[50][50];
    public static Result agentFive(ArrayList<ArrayList<Graph.Node>> maze){
//        initializes all player positions
        Agent agent = new Agent();
        Prey prey = new Prey(agent);
        Predator predator = new Predator(agent);
        initialBelief(predator.getCell());
        initRandTransMatrix(maze);
        int count = 0;
        double surveyRate = 0;
//        will return only when Agent dies or succeeds
        while(true){
//            hung
            if (count == 5000)
                return new Result(false, false, false,false, surveyRate/(double)count, count);
//            creates arraylists of neighbors, predator distances, and prey distances
            ArrayList<Graph.Node> neighbors = maze.get(agent.getCell());
            ArrayList<Integer> predatorDistances = new ArrayList<>();
            ArrayList<Integer> preyDistances = new ArrayList<>();
//            random survey
            int surveyedNode = randomSurvey(agent, maze);


            if(predator.getCell() == surveyedNode){
                surveyRate++;
                updateProbability(true, predator.getCell());
            } else {
                updateProbability(false, surveyedNode);
            }
//            belief updates
            belief = normalize(belief);

            int predatorCell = randomSurvey(agent,maze);

//            adds distances to predator/prey from all neighbors
            for(int x = 0; x < neighbors.size(); x++){
                List<Graph.Node> predatorList = searchPred(neighbors.get(x).getCell(), predatorCell, maze);
                List<Graph.Node> preyList = searchPrey(neighbors.get(x).getCell(), prey.getCell(), maze);
                predatorDistances.add(x, predatorList.size());
                preyDistances.add(x, preyList.size());

            }
//            stores distance of agent to predator/prey in a named variable
            int agentToPrey = preyDistances.get(0);
            int agentToPredator = predatorDistances.get(0);
//            used to store arraylist of possible ties
            HashMap<Integer,ArrayList<Integer>> moves = new HashMap<>();
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
                return new Result(false, true, false,false, surveyRate/((double)count+1), 0);
            }
//            dead
            else if(agent.getCell() == predator.getCell())
                return new Result(false, false, true,false, surveyRate/((double)count+1), 0);
//          belief update
            updateProbability(false,  agent.getCell());
            belief  = normalize(belief);


//          prey move
            prey.setCell(Prey.choosesNeighbors(prey.getCell(), maze));
//            win
            if(agent.getCell() == prey.getCell()){
                return new Result(false, false, false,true, surveyRate/((double)count+1), 0);
            }

//            pred move
            List<Graph.Node> predatorNeighbors = maze.get(predator.getCell()).subList(1, maze.get(predator.getCell()).size());
            ArrayList<Integer> distances = new ArrayList<>();

            for(int x = 0; x < predatorNeighbors.size(); x++){
                List<Graph.Node> agentList = Predator.bfs(predatorNeighbors.get(x).getCell(), agent, maze);
                distances.add(agentList.size());
            }
//            randomly choose neighbor for ties
            int min = Collections.min(distances);

            ArrayList<Integer> indices = new ArrayList<>();
            for(int x = 0; x < distances.size(); x++){
                if (distances.get(x) == min){
                    indices.add(x);
                }
            }
            int randInt = new Random().nextInt(indices.size());

//             if prob is <= 6 then it chooses shortest path. Else, chooses randomly
            int prob = new Random().nextInt(10)+1;
            if(prob <= 6) {
                predator.setCell(predatorNeighbors.get(indices.get(randInt)).getCell());

            }
            else {
                predator.setCell(Predator.choosesNeighbors(predator.getCell(), maze));
            }
//            dead
            if(agent.getCell() == predator.getCell()){
                return new Result(true, false, false,false, surveyRate/((double)count+1), 0);
            }
//            belief distribution
            updateTransMatrix(agent, maze);

            double[] temp1 = belief.clone();
            double[] temp2 = belief.clone();

            temp1 = matmul(temp1);
            for(int x = 0; x < temp1.length; x++){
                temp1[x]*=.6;
            }


            temp2 = matmulRand(temp2);
            for(int x = 0; x < temp2.length; x++){
                temp2[x]*=.4;
            }

            for(int x = 0; x < belief.length; x++)
                belief[x] = temp1[x] + temp2[x];
//            System.out.println(beliefSum(belief));
//            belief = normalize(belief);


            count++;


        }
    }


    public static Result agentSix(ArrayList<ArrayList<Graph.Node>> maze){
//        initializes all player positions
        Agent agent = new Agent();
        Prey prey = new Prey(agent);
        Predator predator = new Predator(agent);
        initialBelief(predator.getCell());
        initRandTransMatrix(maze);
//        for
        int count = 0;
        double surveyRate = 0;
//        will return only when Agent dies or succeeds
        while(true){
//            hung
            if (count == 5000)
                return new Result(false, false, false,false, surveyRate/(double)count, count);
//            creates arraylists of neighbors
            ArrayList<Graph.Node> neighbors = maze.get(agent.getCell());

//            random survey
            int surveyedNode = randomSurvey(agent, maze);
            if(predator.getCell() == surveyedNode){
                surveyRate++;
                updateProbability(true, predator.getCell());
            } else {
                updateProbability(false, surveyedNode);
            }
//          belief updates
            belief = normalize(belief);
            ArrayList<Graph.Node> preyNeighbors = maze.get(prey.getCell());

//            calls utility function
            int cell = bestCell(neighbors, randomSurvey(agent,maze), preyNeighbors, maze);
            agent.setCell(cell);

//            win
            if(agent.getCell() == prey.getCell()){
                return new Result(false, true, false,false, surveyRate/((double)count+1), 0);
            }
//            dead
            else if(agent.getCell() == predator.getCell())
                return new Result(false, false, true,false, surveyRate/((double)count+1), 0);
            updateProbability(false,  agent.getCell());
//            updates belief
            belief = normalize(belief);
//          prey move

            prey.setCell(Prey.choosesNeighbors(prey.getCell(), maze));
//            win
            if(agent.getCell() == prey.getCell()){
                return new Result(false, false, false,true, surveyRate/((double)count+1), 0);
            }


//            pred move
            List<Graph.Node> predatorNeighbors = maze.get(predator.getCell()).subList(1, maze.get(predator.getCell()).size());
            ArrayList<Integer> distances = new ArrayList<>();

            for(int x = 0; x < predatorNeighbors.size(); x++){
                List<Graph.Node> agentList = Predator.bfs(predatorNeighbors.get(x).getCell(), agent, maze);
                distances.add(agentList.size());
            }
//            randomly choose neighbor for ties
            int min = Collections.min(distances);

            ArrayList<Integer> indices = new ArrayList<>();
            for(int x = 0; x < distances.size(); x++){
                if (distances.get(x) == min){
                    indices.add(x);
                }
            }
            int randInt = new Random().nextInt(indices.size());

//             if prob is <= 6 then it chooses shortest path. Else, chooses randomly
            int prob = new Random().nextInt(10)+1;
            if(prob <= 6)
                predator.setCell(predatorNeighbors.get(indices.get(randInt)).getCell());
            else
                predator.setCell(Predator.choosesNeighbors(predator.getCell(), maze));
//            dead
            if(agent.getCell() == predator.getCell()){
                return new Result(true, false, false,false, surveyRate/((double)count+1), 0);
            }
//          belief distribution
            updateTransMatrix(agent, maze);

            double[] temp1 = belief.clone();
            double[] temp2 = belief.clone();
            temp1 = matmul(temp1);
            for(int x = 0; x < temp1.length; x++)
                temp1[x] *= .6;
//            temp1 = normalize(temp1);

            temp2 = matmulRand(temp2);
            for(int x = 0; x < temp2.length; x++)
                temp2[x] *= .4;
//            temp2 = normalize(temp2);

            for(int x = 0; x < belief.length; x++)
                belief[x] = temp1[x] + temp2[x];
//            belief = matmul(belief);

            belief = normalize(belief);



            count++;



        }

    }

    public static int bestCell(ArrayList<Graph.Node> neighbors, int predator, ArrayList<Graph.Node> preyNeighbors, ArrayList<ArrayList<Graph.Node>> maze){

//      stores utility of all agent cells
        ArrayList<Double> utilities = new ArrayList<>();
        ArrayList<Double> preyDistances = new ArrayList<>();
        ArrayList<Integer> predatorDistances = new ArrayList<>();
//        collects all preyDistances and predatorDistances
        for(int x = 0; x < neighbors.size(); x++) {
            double aveDistance = 0.0;
//          gets of average distances from each possible agent to cluster of prey neighbors
            for (int y = 0; y < preyNeighbors.size(); y++) {
                int currDistance = searchPrey(neighbors.get(x).getCell(), preyNeighbors.get(y).getCell(), maze).size();
                aveDistance += currDistance;


            }
//            stores average distances
            aveDistance /= preyNeighbors.size();
            preyDistances.add(aveDistance);
            List<Graph.Node> predatorList = searchPred(neighbors.get(x).getCell(), predator, maze);
            predatorDistances.add(predatorList.size());
//            stores initial utility values
            utilities.add(predatorList.size() - aveDistance);
        }
//        arbitrary number
        double weightPrey   =    1.0/2;
        double weightPredator = -1.0/2;
//        updates utility of cell depending  on whether current cell has closest distance to predator or closest distance to prey
        for(int x = 0; x < neighbors.size(); x++){
            if(Collections.min(preyDistances) == preyDistances.get(x))
                utilities.set(x, utilities.get(x) + 75 * (weightPrey/preyDistances.size()));
            if(Collections.min(predatorDistances) == predatorDistances.get(x))
                utilities.set(x, utilities.get(x) + 75 * (weightPredator/predatorDistances.size()));
        }

//        Two options: 1) move towards cell with highest utility when all greatest utility is positive2) move away from predator
        if(Collections.max(utilities) > 0)
            return neighbors.get(utilities.indexOf(Collections.max(utilities))).getCell();
        else
            return neighbors.get(predatorDistances.indexOf(Collections.max(predatorDistances))).getCell();
    }

    //    updates belief when new node is surveyed;
    public static void updateProbability(boolean found, int cell){
//        if node surveyed contains prey
        if (found){
//            System.out.println("here");
            for (int x = 0; x < belief.length; x++) {
                if (cell == x) {
                    belief[x] = 1.0;
                } else {
                    belief[x] = 0.0;
                }

            }

        } else {
//            update all probabilities based on removal of current probability
            double removedProbability = belief[cell];
            for (int x = 0; x < belief.length; x++) {
                if(x == cell){
                    belief[x] = 0;
                }  else {

                    belief[x] /= (1.0-removedProbability);
                }
            }
        }
    }

    //    initializes 1 for predator location because we already know it
    public static void initialBelief(int predatorCell){
        for(int x = 0; x < belief.length; x++){
            if(x == predatorCell) {
                belief[x] = 1.0;
            }
            else {
                belief[x] = 0.0;
            }

        }
    }

    public static void initRandTransMatrix(ArrayList<ArrayList<Graph.Node>> maze){
        for(int x = 0; x < 50; x++){
            for(int y = 0; y < 50; y++){
                randTransMatrix[x][y] = 0;
            }
        }
        for(int x = 0; x < maze.size(); x++){
            for(int y = 0; y < maze.get(x).size(); y++){
//                updates neighbors with .4/number of neighbors
                if(maze.get(x).get(0).getCell() !=  maze.get(x).get(y).getCell())
                    randTransMatrix[maze.get(x).get(0).getCell()][maze.get(x).get(y).getCell()] = (1.0/(maze.get(x).size()-1));
                else
                    randTransMatrix[maze.get(x).get(0).getCell()][maze.get(x).get(y).getCell()] = 0.0;
            }
        }
    }

//    updates trans matrix after avery agent move
    public static void updateTransMatrix(Agent agent, ArrayList<ArrayList<Graph.Node>> maze){
        for(int x = 0; x < maze.size(); x++){
//            if (x == agent.getCell())
//                continue;
            List<Graph.Node> neighbors = maze.get(x).subList(1, maze.get(x).size());
            ArrayList<Integer> distances = new ArrayList<>();
//            accumaltes all distancesfor current list of neighbors to agent
            for(int y = 0; y < neighbors.size(); y++){
                List<Graph.Node> predatorDistance = Predator.bfs(neighbors.get(y).getCell(), agent, maze);
                distances.add(predatorDistance.size());
            }

//            updates transmatrix with probability of predator/cell moving to next cell towards agent.
            int minimum = Collections.min(distances);
            int count = countMin(distances, minimum);

//            updates current shortest path neighbors with .6/count
            for(int y = 0; y < 50; y++){
                transMatrix[x][y] = 0.0;
                if(neighbors.contains(new Graph.Node(y)) && minimum == distances.get(neighbors.indexOf(new Graph.Node(y)))) {
                    transMatrix[x][y] = (1.0/ count);

                }

            }


        }

    }

    //    returns random cell that has the highest likelihood of being prey
    public static int randomSurvey(Agent agent, ArrayList<ArrayList<Graph.Node>> maze){
        ArrayList<Integer> indices = new ArrayList<>();
        double biggestProb = maxBelief();
        for(int x = 0; x < belief.length; x++){
//            stores all indices that are have a probability of having prey
            if(belief[x] == biggestProb){
                indices.add(x);
            }
        }
        if(indices.size() == 1)
            return indices.get(0);
        int minimum = Integer.MAX_VALUE;
//        int closestIndex = -1;
        for(Integer cell: indices){
            int path = bfs(cell, agent, maze).size();
            if(minimum > path){
//                closestIndex = cell;
                minimum = path;
            }
        }

        ArrayList<Integer> minDist = new ArrayList<>();
        for(Integer cell: indices){
            int path = bfs(cell, agent, maze).size();
            if(minimum == path){
                minimum = path;
                minDist.add(cell);
            }
        }
        if(minDist.size() == 1)
            return minDist.get(0);
        int rand = new Random().nextInt(minDist.size());

        return minDist.get(rand);
    }

    //  returns greatest probability in belief
    public static double maxBelief(){
        return Arrays.stream(belief).max().getAsDouble();
    }

//    counts how many minimum distances there are
    public static int countMin(ArrayList<Integer> distances, int minimum){
        int count = 0;
        for(int z = 0; z < distances.size(); z++){
            if(distances.get(z) == minimum){
                count++;
            }

        }
        return count;
    }

    //    updates belief after no new info
    public static double[] matmul(double[] belief){
        double[] arr = belief.clone();
        for(int x = 0; x < 50; x++){
            belief[x] = dotProduct(x, arr);

        }
        return belief;

    }

    //    dot product to update belief with transMatrix
    public static double dotProduct(int row, double[] temp) {
        double sum = 0;
        for (int x = 0; x < 50; x++) {
            sum += transMatrix[x][row] * temp[x];
        }

        return sum;

    }

    //    updates belief after no new info
    public static double[] matmulRand(double[] belief){
        double[] arr = belief.clone();
        for(int x = 0; x < 50; x++){
            belief[x] = dotProductRand(x, arr);

        }
        return belief;


    }

    //    dot product to update belief with transMatrix
    public static double dotProductRand(int row, double[] temp) {
        double sum = 0;
        for (int x = 0; x < 50; x++) {
            sum += randTransMatrix[x][row] * temp[x];
        }

        return sum;

    }


    //    normalizes values
    public static double[] normalize(double[] belief){
        double sum = beliefSum(belief);
        for(int x = 0; x < 50; x++){
            belief[x] /= sum;
        }
        return belief;
    }

    public static double beliefSum(double[] array) {
        return Arrays.stream(array).sum();
    }




}
