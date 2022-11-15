package Agents;

import Environment.*;

import java.util.*;

import static Agents.CompleteInformation.searchPred;
import static Agents.CompleteInformation.searchPrey;
import static Environment.Predator.bfs;

public class FixedFaultyCombinedPartialInformation {
//    static values to access and update values easier
    static double[] preyBelief = new double[50];
    static double[] predatorBelief = new double[50];
    static double[][] preyTransMatrix = new double[50][50];
    static double[][] predatorTransMatrix = new double[50][50];
    static double[][] predatorRandTransMatrix = new double[50][50];

    public static Result agentSevenFaultyFixed(ArrayList<ArrayList<Graph.Node>> maze){
//        initializes all player positions
        Agent agent = new Agent();
        Prey prey = new Prey(agent);
        Predator predator = new Predator(agent);
//        initialize belief vector and transition matrix
        preyInitTransMatrix(maze);
        initRandTransMatrix(maze);
        preyInitialBelief(agent.getCell());
        predatorInitialBelief(predator.getCell());
        int count = 0;
        double preySurveyRate = 0;
        double predatorSurveyRate = 0;
//        will return only when Agent dies or succeeds
        while(true){
//            hung
            if (count == 5000)
                return new Result(false, false, false,false, preySurveyRate/(double)count, predatorSurveyRate/(double)count, count);
            ArrayList<Graph.Node> neighbors = maze.get(agent.getCell());
            ArrayList<Integer> predatorDistances = new ArrayList<>();
            ArrayList<Integer> preyDistances = new ArrayList<>();
//            random survey
            if (count == 5000)
                return new Result(false, false, false,false, preySurveyRate/((double)count + 1), predatorSurveyRate/((double)count + 1), count);
//            creates arraylists of neighbors, predator distances, and prey distances
//            random survey
            if(predatorMaxBelief() < 1) {
                int surveyedNode = predatorRandomSurvey(agent, maze);
//                10% chance its faulty
                int prob = new Random().nextInt(10)+1;
                if(prob == 10) {
//                    if predator is already found, then won't be faulty
                    if(predatorBelief[surveyedNode] < 1) {
                        predatorBayes(false, surveyedNode);
                        predatorBelief = predatorNormalize(predatorBelief);

                    }else {
                        predatorSurveyRate++;
                        predatorBayes(true, predator.getCell());
                    }

                    if(preyBelief[surveyedNode] < 1) {
                        preyBayes(false, surveyedNode);
                        preyNormalize();

                    }else {
                        preySurveyRate++;
                        preyBayes(true, prey.getCell());

                    }


                }
                else {
                    if (predator.getCell() == surveyedNode) {
                        predatorSurveyRate++;
                        predatorBayes(true, predator.getCell());
                    } else {
                        predatorBayes(false, surveyedNode);
                        predatorBelief = predatorNormalize(predatorBelief);
                    }

                    if (prey.getCell() == surveyedNode) {
                        preySurveyRate++;
                        preyBayes(true, prey.getCell());
                    } else {
                        preyBayes(false, surveyedNode);
                        preyNormalize();
                    }

                }
            } else {
                int surveyedNode = preyRandomSurvey();
//                10% chance its faulty

                int prob = new Random().nextInt(10)+1;
                if(prob == 10) {
//                    if predator is already found, then won't be faulty
                    if(predatorBelief[surveyedNode] < 1) {
                        predatorBayes(false, surveyedNode);
                        predatorBelief = predatorNormalize(predatorBelief);
                    }else {
                        predatorSurveyRate++;
                        predatorBayes(true, predator.getCell());
                    }

                    if(preyBelief[surveyedNode] < 1) {
                        preyBayes(false, surveyedNode);
                        preyNormalize();

                    }else {
                        preySurveyRate++;
                        preyBayes(true, prey.getCell());
                    }


                }
                else {
                    if (prey.getCell() == surveyedNode) {
                        preySurveyRate++;
                        preyBayes(true, prey.getCell());
                    } else {
                        preyBayes(false, surveyedNode);
                        preyNormalize();
                    }

                    if (predator.getCell() == surveyedNode) {
                        predatorSurveyRate++;
                        predatorBayes(true, predator.getCell());
                    } else {
                        predatorBayes(false, surveyedNode);
                        predatorBelief = predatorNormalize(predatorBelief);
                    }

                }
            }
//          gets most probabilistic values
            int predatorCell = predatorRandomSurvey(agent, maze);
            int preyCell = preyRandomSurvey();
//            adds distances to predator/prey from all neighbors
            for(int x = 0; x < neighbors.size(); x++){
                List<Graph.Node> predatorList = searchPred(neighbors.get(x).getCell(), predatorCell, maze);
                List<Graph.Node> preyList = searchPrey(neighbors.get(x).getCell(), preyCell, maze);
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
                return new Result(false, true, false, false,preySurveyRate/((double)count + 1), predatorSurveyRate/((double)count + 1), 0);
            }
//            dead
            else if(agent.getCell() == predator.getCell()) {
                return new Result(false, false, true, false,preySurveyRate/((double)count + 1), predatorSurveyRate/((double)count + 1), 0);
            }
//          belief updates
            predatorBayes(false,  agent.getCell());
            predatorBelief = predatorNormalize(predatorBelief);
            preyBayes(false,  agent.getCell());
            preyNormalize();

//          prey move
            prey.setCell(Prey.choosesNeighbors(prey.getCell(), maze));

//            win
            if(agent.getCell() == prey.getCell()){
                return new Result(false, false, false, true,preySurveyRate/((double)count + 1), predatorSurveyRate/((double)count + 1), 0);
            }
            preyMatmul();
//            preyNormalize();

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
                return new Result(true, false, false, false,preySurveyRate/((double)count + 1), predatorSurveyRate/((double)count + 1), 0);
            }
//          belief distribution
            updateTransMatrix(agent, maze);

            double[] temp1 = predatorBelief.clone();
            double[] temp2 = predatorBelief.clone();
            temp1 = predatorMatmul(temp1);
            for(int x = 0; x < temp1.length; x++)
                temp1[x] *= .6;

            temp2 = matmulRand(temp2);
            for(int x = 0; x < temp2.length; x++)
                temp2[x] *= .4;

            for(int x = 0; x < predatorBelief.length; x++)
                predatorBelief[x] = temp1[x] + temp2[x];



            count++;



        }

    }

    public static Result agentEightFaultyFixed(ArrayList<ArrayList<Graph.Node>> maze){
//        initializes all player positions
        Agent agent = new Agent();
        Prey prey = new Prey(agent);
        Predator predator = new Predator(agent);
//        initialize belief vector and transition matrix
        preyInitTransMatrix(maze);
        initRandTransMatrix(maze);
        preyInitialBelief(agent.getCell());
        predatorInitialBelief(predator.getCell());
        int count = 0;
        double preySurveyRate = 0;
        double predatorSurveyRate = 0;
//        will return only when Agent dies or succeeds
        while(true){
//            hung
            if (count == 5000)
                return new Result(false, false, false,false, preySurveyRate/((double)count + 1), predatorSurveyRate/((double)count + 1), count);
//            creates arraylists of neighbors, predator distances, and prey distances
            ArrayList<Graph.Node> neighbors = maze.get(agent.getCell());
//            random survey
            if(predatorMaxBelief() < 1) {
                int surveyedNode = predatorRandomSurvey(agent, maze);
//                10% chance its faulty
                int prob = new Random().nextInt(10)+1;
                if(prob == 10) {
//                    if predator is already found, then won't be faulty
                    if(predatorBelief[surveyedNode] < 1) {
                        predatorBayes(false, surveyedNode);
                        predatorBelief = predatorNormalize(predatorBelief);

                    }else {
                        predatorSurveyRate++;
                        predatorBayes(true, predator.getCell());
                    }

                    if(preyBelief[surveyedNode] < 1) {
                        preyBayes(false, surveyedNode);
                        preyNormalize();

                    }else {
                        preySurveyRate++;
                        preyBayes(true, prey.getCell());

                    }


                }
                else {
                    if (predator.getCell() == surveyedNode) {
                        predatorSurveyRate++;
                        predatorBayes(true, predator.getCell());
                    } else {
                        predatorBayes(false, surveyedNode);
                        predatorBelief = predatorNormalize(predatorBelief);
                    }

                    if (prey.getCell() == surveyedNode) {
                        preySurveyRate++;
                        preyBayes(true, prey.getCell());
                    } else {
                        preyBayes(false, surveyedNode);
                        preyNormalize();
                    }

                }
            } else {
                int surveyedNode = preyRandomSurvey();
//                10% chance its faulty

                int prob = new Random().nextInt(10)+1;
                if(prob == 10) {
//                    if predator is already found, then won't be faulty
                    if(predatorBelief[surveyedNode] < 1) {
                        predatorBayes(false, surveyedNode);
                        predatorBelief = predatorNormalize(predatorBelief);
                    }else {
                        predatorSurveyRate++;
                        predatorBayes(true, predator.getCell());
                    }

                    if(preyBelief[surveyedNode] < 1) {
                        preyBayes(false, surveyedNode);
                        preyNormalize();

                    }else {
                        preySurveyRate++;
                        preyBayes(true, prey.getCell());
                    }


                }
                else {
                    if (prey.getCell() == surveyedNode) {
                        preySurveyRate++;
                        preyBayes(true, prey.getCell());
                    } else {
                        preyBayes(false, surveyedNode);
                        preyNormalize();
                    }

                    if (predator.getCell() == surveyedNode) {
                        predatorSurveyRate++;
                        predatorBayes(true, predator.getCell());
                    } else {
                        predatorBayes(false, surveyedNode);
                        predatorBelief = predatorNormalize(predatorBelief);
                    }

                }
            }


            ArrayList<Graph.Node> preyNeighbors = maze.get(prey.getCell());

//            calls utility function
            int cell = bestCell(neighbors, predatorRandomSurvey(agent, maze), preyNeighbors, maze);
            agent.setCell(cell);


//            win
            if(agent.getCell() == prey.getCell()){
                return new Result(false, true, false, false,preySurveyRate/((double)count + 1), predatorSurveyRate/((double)count + 1), 0);
            }
            else if(agent.getCell() == predator.getCell()){
                return new Result(false, false, true, false,preySurveyRate/((double)count + 1), predatorSurveyRate/((double)count + 1), 0);
            }
//            belief updates
            predatorBayes(false,  agent.getCell());
            preyBayes(false,  agent.getCell());
            predatorBelief = predatorNormalize(predatorBelief);
            preyNormalize();

//          prey move
            prey.setCell(Prey.choosesNeighbors(prey.getCell(), maze));
//            win
            if(agent.getCell() == prey.getCell()){
                return new Result(false, false, false, true,preySurveyRate/((double)count + 1), predatorSurveyRate/((double)count + 1), 0);
            }
            preyMatmul();
//            preyNormalize();


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
                return new Result(true, false, false, false,preySurveyRate/((double)count + 1), predatorSurveyRate/((double)count + 1), 0);
            }
//          belief distribution
            updateTransMatrix(agent, maze);

            double[] temp1 = predatorBelief.clone();
            double[] temp2 = predatorBelief.clone();
            temp1 = predatorMatmul(temp1);
            for(int x = 0; x < temp1.length; x++)
                temp1[x] *= .6;

            temp2 = matmulRand(temp2);
            for(int x = 0; x < temp2.length; x++)
                temp2[x] *= .4;

            for(int x = 0; x < predatorBelief.length; x++)
                predatorBelief[x] = temp1[x] + temp2[x];

//            predatorBelief = predatorNormalize(predatorBelief);


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
                utilities.set(x, utilities.get(x) + 100 * (weightPredator/predatorDistances.size()));
        }

//        Two options: 1) move towards cell with highest utility when all greatest utility is positive2) move away from predator
        if(Collections.max(utilities) > 0)
            return neighbors.get(utilities.indexOf(Collections.max(utilities))).getCell();
        else
            return neighbors.get(predatorDistances.indexOf(Collections.max(predatorDistances))).getCell();
    }

    //    PREY METHODS
    //    updates belief when new node is surveyed
    public static void preyBayes(boolean found, int cell){
//        if node surveyed contains prey
        if (found){
            for (int x = 0; x < preyBelief.length; x++) {
                if (cell == x) {
                    preyBelief[x] = 1.0;
                } else {
                    preyBelief[x] = 0.0;
                }

            }

        } else {
//            update all probabilities based on removal of current probability

                preyBelief[cell]*=.1;

                preyNormalize();

        }
    }


    //  returns greatest probability in belief;
    public static double preyMaxBelief(){
        return Arrays.stream(preyBelief).max().getAsDouble();
    }

    //    initializes 1/49 for every non-agent cell
    public static void preyInitialBelief(int agentCell){
        for(int x = 0; x < preyBelief.length; x++){
            if(x != agentCell) {
                preyBelief[x] = 1.0 / (49);
            }
            else {
                preyBelief[x] = 0.0;
            }

        }
    }

    //    sums up belief for normalization and error checking
    public static double beliefSum(double[] array) {
        return Arrays.stream(array).sum();
    }
    //    never changes
    public static void preyInitTransMatrix(ArrayList<ArrayList<Graph.Node>> maze){
        for(int x = 0; x < 50; x++){
            for(int y = 0; y < 50; y++){
                preyTransMatrix[x][y] = 0;
            }
        }
        for(int x = 0; x < maze.size(); x++){
            for(int y = 0; y < maze.get(x).size(); y++){
                preyTransMatrix[maze.get(x).get(0).getCell()][maze.get(x).get(y).getCell()] = 1.0/(maze.get(x).size());
            }
        }
    }

    //    returns random cell that has the highest likelihood of being prey
    public static int preyRandomSurvey(){
        ArrayList<Integer> indices = new ArrayList<>();
        double max = preyMaxBelief();
        for(int x = 0; x < preyBelief.length; x++){
//            stores all indices that are have a probability of having prey
            if(preyBelief[x] == max){
                indices.add(x);
            }
        }
        if(indices.size() == 1)
            return indices.get(0);
//        randomly chooses index from arraylist
        int randInt = new Random().nextInt(indices.size());
        return indices.get(randInt);
    }

    //    updates belief after no new info
    public static void preyMatmul(){
        double[] arr = preyBelief.clone();
        for(int x = 0; x < 50; x++){
            preyBelief[x] = preyDotProduct(x, arr);
        }
//        normalization


    }

    //    dot product to update belief with transMatrix
    public static double preyDotProduct(int row, double[] temp) {
        double sum = 0;
        for (int x = 0; x < 50; x++) {
            sum += preyTransMatrix[x][row] * temp[x];

        }

        return sum;

    }

    //    normalizes values
    public static void preyNormalize(){
        double sum = beliefSum(preyBelief);
        for(int x = 0; x < 50; x++){
            preyBelief[x] /= sum;
        }
    }





//  PREDATOR METHODS:


    //    updates belief when new node is surveyed;
    public static void predatorBayes(boolean found, int cell){
//        if node surveyed contains prey
        if (found){
//            System.out.println("here");
            for (int x = 0; x < predatorBelief.length; x++) {
                if (cell == x) {
                    predatorBelief[x] = 1.0;
                } else {
                    predatorBelief[x] = 0.0;
                }

            }

        } else {
//            update all probabilities based on removal of current probability
//            double removedProbability = predatorBelief[cell];
                predatorBelief[cell]*=.1;
                predatorBelief = predatorNormalize(predatorBelief);

        }
    }

    //    initializes 1 for predator location because we already know it
    public static void predatorInitialBelief(int predatorCell){
        for(int x = 0; x < predatorBelief.length; x++){
            if(x == predatorCell) {
                predatorBelief[x] = 1.0;
            }
            else {
                predatorBelief[x] = 0.0;
            }
//            System.out.println(belief[x]);

        }
    }


    public static void initRandTransMatrix(ArrayList<ArrayList<Graph.Node>> maze){
        for(int x = 0; x < 50; x++){
            for(int y = 0; y < 50; y++){
                predatorRandTransMatrix[x][y] = 0;
            }
        }
        for(int x = 0; x < maze.size(); x++){
            for(int y = 0; y < maze.get(x).size(); y++){
                if(maze.get(x).get(0).getCell() !=  maze.get(x).get(y).getCell())
                    predatorRandTransMatrix[maze.get(x).get(0).getCell()][maze.get(x).get(y).getCell()] = (1.0/(maze.get(x).size()-1));
                else
                    predatorRandTransMatrix[maze.get(x).get(0).getCell()][maze.get(x).get(y).getCell()] = 0.0;
            }
        }
    }



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

            for(int y = 0; y < 50; y++){
//                if (y > 0)
//                    transMatrix[x][y] = .4/(neighbors.size()-1);
                predatorTransMatrix[x][y] = 0.0;
                if(neighbors.contains(new Graph.Node(y)) && minimum == distances.get(neighbors.indexOf(new Graph.Node(y)))) {
                    predatorTransMatrix[x][y] = (1.0/ count);

                }

            }


        }

    }

    //    returns random cell that has the highest likelihood of being prey
    public static int predatorRandomSurvey(Agent agent, ArrayList<ArrayList<Graph.Node>> maze){
        ArrayList<Integer> indices = new ArrayList<>();
        double biggestProb = predatorMaxBelief();
        for(int x = 0; x < predatorBelief.length; x++){
//            stores all indices that are have a probability of having prey
            if(predatorBelief[x] == biggestProb){
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



    //  returns greatest probability in belief;
    public static double predatorMaxBelief(){
        return Arrays.stream(predatorBelief).max().getAsDouble();
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
    public static double[] predatorMatmul(double[] belief){
        double[] arr = belief.clone();
        for(int x = 0; x < 50; x++){
            belief[x] = predatorDotProduct(x, arr);

        }
        return belief;

    }

    //    dot product to update belief with transMatrix
    public static double predatorDotProduct(int row, double[] temp) {
        double sum = 0;
        for (int x = 0; x < 50; x++) {
            sum += predatorTransMatrix[x][row] * temp[x];

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
            sum += predatorRandTransMatrix[x][row] * temp[x];
        }

        return sum;

    }


    //    normalizes values
    public static double[] predatorNormalize(double[] belief){
        double sum = beliefSum(belief);
        for(int x = 0; x < 50; x++){
            belief[x] /= sum;
        }
        return belief;
    }




}