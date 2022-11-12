import Agents.*;
import Environment.*;

import java.util.*;

public class Main {
    public static void runOne(){
        int success = 0;
        int hung = 0;
        double fail = 0;
        long total = System.nanoTime();
        for(int graphs = 1; graphs <= 100; graphs++) {
            ArrayList<ArrayList<Graph.Node>> maze = Graph.buildGraph();
            for (int runs = 1; runs <= 30; runs++) {
                String result = CompleteInformation.agentOne(maze);
                if (result.equals("true"))
                    success++;
                else if (result.equals("hung"))
                    hung++;
                else
                    fail++;
            }
        }
        long endTime = System.nanoTime();
        long duration = (endTime - total)/(long)Math.pow(10,9);
        System.out.println(duration);
        System.out.println(hung);
        System.out.println("Hung out of Loss: " + hung/(fail+hung));
        System.out.println("A1: " + success/3000.0);
    }
    public static void runTwo(){
        double aveSuccess = 0;
        int hung = 0;
        double fail = 0;
        long total = System.nanoTime();
        for(int graphs = 1; graphs <= 100; graphs++) {
            int success = 0;
            ArrayList<ArrayList<Graph.Node>> maze = Graph.buildGraph();
            for (int runs = 1; runs <= 30; runs++) {
                String result = CompleteInformation.agentTwo(maze);
                if (result.equals("true"))
                    success++;
                else if (result.equals("hung"))
                    hung++;
                else
                    fail++;
            }
            aveSuccess += success;
        }

        long endTime = System.nanoTime();
        long duration = (endTime - total)/(long)Math.pow(10,9);
        System.out.println(duration);
        System.out.println(hung);
        System.out.println("Hung out of Loss: " + hung/(fail+hung));
        System.out.println("A2: " + (aveSuccess)/3000.0);
    }
    public static void runThree(){
        double aveSuccess = 0;
        int hung = 0;
        double fail = 0;
        long total = System.nanoTime();
        for(int graphs = 1; graphs <= 100; graphs++) {
            int success = 0;
            ArrayList<ArrayList<Graph.Node>> maze = Graph.buildGraph();
            for (int runs = 1; runs <= 30; runs++) {
                String result = PartialPrey.agentThree(maze);
                if (result.equals("true"))
                    success++;
                else if (result.equals("hung"))
                    hung++;
                else
                    fail++;
            }
            aveSuccess += success;
        }

        long endTime = System.nanoTime();
        long duration = (endTime - total)/(long)Math.pow(10,9);
        System.out.println(duration);
        System.out.println(hung);
        System.out.println("Hung out of Loss: " + hung/(fail+hung));
        System.out.println("A3: " + (aveSuccess)/3000.0);
//        System.out.println();
    }
    public static void runFour(){
        double aveSuccess = 0;
        int hung = 0;
        double fail = 0;
        long total = System.nanoTime();
        for(int graphs = 1; graphs <= 100; graphs++) {
            int success = 0;
            ArrayList<ArrayList<Graph.Node>> maze = Graph.buildGraph();
            for (int runs = 1; runs <= 30; runs++) {
                String result = PartialPrey.agentFour(maze);
                if (result.equals("true"))
                    success++;
                else if (result.equals("hung"))
                    hung++;
                else
                    fail++;
            }
            aveSuccess += success;
        }

        long endTime = System.nanoTime();
        long duration = (endTime - total)/(long)Math.pow(10,9);
        System.out.println(duration);
        System.out.println(hung);
        System.out.println("Hung out of Loss: " + hung/(fail+hung));
        System.out.println("A4: " + (aveSuccess)/3000.0);
//        System.out.println();
    }
    public static void runFive(){
        double aveSuccess = 0;
        int hung = 0;
        double fail = 0;
        long total = System.nanoTime();
        for(int graphs = 1; graphs <= 100; graphs++) {
            int success = 0;
            ArrayList<ArrayList<Graph.Node>> maze = Graph.buildGraph();
            for (int runs = 1; runs <= 30; runs++) {
                String result = PartialPredator.agentFive(maze);
                if (result.equals("true"))
                    success++;
                else if (result.equals("hung"))
                    hung++;
                else
                    fail++;
            }
            aveSuccess += success;
        }

        long endTime = System.nanoTime();
        long duration = (endTime - total)/(long)Math.pow(10,9);
        System.out.println(duration);
        System.out.println(hung);
        System.out.println("Hung out of Loss: " + hung/(fail+hung));
        System.out.println("A5: " + (aveSuccess)/3000.0);
//        System.out.println();
    }
    public static void runSix(){
        double aveSuccess = 0;
        int hung = 0;
        double fail = 0;
        long total = System.nanoTime();
        for(int graphs = 1; graphs <= 100; graphs++) {
            int success = 0;
            ArrayList<ArrayList<Graph.Node>> maze = Graph.buildGraph();
            for (int runs = 1; runs <= 30; runs++) {
                String result = PartialPredator.agentSix(maze);
                if (result.equals("true"))
                    success++;
                else if (result.equals("hung"))
                    hung++;
                else
                    fail++;
            }
            aveSuccess += success;
        }

        long endTime = System.nanoTime();
        long duration = (endTime - total)/(long)Math.pow(10,9);
        System.out.println(duration);
        System.out.println(hung);
        System.out.println("Hung out of Loss: " + hung/(fail+hung));
        System.out.println("A6: " + (aveSuccess)/3000.0);
//        System.out.println();
    }
    public static void runSeven(){
        double aveSuccess = 0;
        int hung = 0;
        double fail = 0;
        long total = System.nanoTime();
        int averageStep = 0;
        for(int graphs = 1; graphs <= 100; graphs++) {
            int success = 0;
            ArrayList<ArrayList<Graph.Node>> maze = Graph.buildGraph();
            for (int runs = 1; runs <= 30; runs++) {
                String result = CombinedPartialInformation.agentSeven(maze);
                if (result.contains("true")) {
                    success++;
                    averageStep += Integer.parseInt(result.substring(5));
//                    System.out.println(averageStep);
                }
                else if (result.contains("hung"))
                    hung++;
                else
                    fail++;
            }
            aveSuccess += success;
        }

        long endTime = System.nanoTime();
        long duration = (endTime - total)/(long)Math.pow(10,9);
        System.out.println(duration);
        System.out.println(hung);
//        System.out.println(averageStep);
//        System.out.println("Average step: " + averageStep/aveSuccess);
        System.out.println("Hung out of Loss: " + hung/(fail+hung));
        System.out.println("A7: " + (aveSuccess)/3000.0);
    }
    public static void runSevenFaulty(){
        double aveSuccess = 0;
        int hung = 0;
        double fail = 0;
        long total = System.nanoTime();
        int averageStep = 0;
        for(int graphs = 1; graphs <= 100; graphs++) {
            int success = 0;
            ArrayList<ArrayList<Graph.Node>> maze = Graph.buildGraph();
            for (int runs = 1; runs <= 30; runs++) {
                String result = FaultyCombinedPartialInformation.agentSevenFaulty(maze);
                if (result.contains("true")) {
                    success++;
                    averageStep += Integer.parseInt(result.substring(5));
//                    System.out.println(averageStep);
                }
                else if (result.contains("hung"))
                    hung++;
                else
                    fail++;
            }
            aveSuccess += success;
        }

        long endTime = System.nanoTime();
        long duration = (endTime - total)/(long)Math.pow(10,9);
        System.out.println(duration);
        System.out.println(hung);
//        System.out.println(averageStep);
//        System.out.println("Average step: " + averageStep/aveSuccess);
        System.out.println("Hung out of Loss: " + hung/(fail+hung));
        System.out.println("A7 Faulty: " + (aveSuccess)/3000.0);
    }
    public static void runSevenFaultyFixed(){
        double aveSuccess = 0;
        int hung = 0;
        double fail = 0;
        long total = System.nanoTime();
        int averageStep = 0;
        for(int graphs = 1; graphs <= 100; graphs++) {
            int success = 0;
            ArrayList<ArrayList<Graph.Node>> maze = Graph.buildGraph();
            for (int runs = 1; runs <= 30; runs++) {
                String result = FixedFaultyCombinedPartialInformation.agentSevenFaultyFixed(maze);
                if (result.contains("true")) {
                    success++;
                    averageStep += Integer.parseInt(result.substring(5));
//                    System.out.println(averageStep);
                }
                else if (result.contains("hung"))
                    hung++;
                else
                    fail++;
            }
            aveSuccess += success;
        }

        long endTime = System.nanoTime();
        long duration = (endTime - total)/(long)Math.pow(10,9);
        System.out.println(duration);
        System.out.println(hung);
//        System.out.println(averageStep);
//        System.out.println("Average step: " + averageStep/aveSuccess);
        System.out.println("Hung out of Loss: " + hung/(fail+hung));
        System.out.println("A7 Faulty Fixed: " + (aveSuccess)/3000.0);
    }
    public static void runEight(){
        double aveSuccess = 0;
        int hung = 0;
        double fail = 0;
        long total = System.nanoTime();
        for(int graphs = 1; graphs <= 100; graphs++) {
            int success = 0;
            ArrayList<ArrayList<Graph.Node>> maze = Graph.buildGraph();
            for (int runs = 1; runs <= 30; runs++) {
                String result = CombinedPartialInformation.agentEight(maze);
                if (result.equals("true"))
                    success++;
                else if (result.equals("hung"))
                    hung++;
                else
                    fail++;
            }
            aveSuccess += success;
        }

        long endTime = System.nanoTime();
        long duration = (endTime - total)/(long)Math.pow(10,9);
        System.out.println(duration);
        System.out.println(hung);
        System.out.println("Hung out of Loss: " + hung/(fail+hung));
        System.out.println("A8: " + (aveSuccess)/3000.0);
    }
    public static void runEightFaulty(){
        double aveSuccess = 0;
        int hung = 0;
        double fail = 0;
        long total = System.nanoTime();
        for(int graphs = 1; graphs <= 100; graphs++) {
            int success = 0;
            ArrayList<ArrayList<Graph.Node>> maze = Graph.buildGraph();
            for (int runs = 1; runs <= 30; runs++) {
                String result = FaultyCombinedPartialInformation.agentEightFaulty(maze);
                if (result.equals("true"))
                    success++;
                else if (result.equals("hung"))
                    hung++;
                else
                    fail++;
            }
            aveSuccess += success;
        }

        long endTime = System.nanoTime();
        long duration = (endTime - total)/(long)Math.pow(10,9);
        System.out.println(duration);
        System.out.println(hung);
        System.out.println("Hung out of Loss: " + hung/(fail+hung));
        System.out.println("A8 Faulty: " + (aveSuccess)/3000.0);
    }
    public static void runEightFaultyFixed(){
        double aveSuccess = 0;
        int hung = 0;
        double fail = 0;
        long total = System.nanoTime();
        for(int graphs = 1; graphs <= 100; graphs++) {
            int success = 0;
            ArrayList<ArrayList<Graph.Node>> maze = Graph.buildGraph();
            for (int runs = 1; runs <= 30; runs++) {
                String result = FixedFaultyCombinedPartialInformation.agentEightFaultyFixed(maze);
                if (result.equals("true"))
                    success++;
                else if (result.equals("hung"))
                    hung++;
                else
                    fail++;
            }
            aveSuccess += success;
        }

        long endTime = System.nanoTime();
        long duration = (endTime - total)/(long)Math.pow(10,9);
        System.out.println(duration);
        System.out.println(hung);
        System.out.println("Hung out of Loss: " + hung/(fail+hung));
        System.out.println("A8 Faulty Fixed: " + (aveSuccess)/3000.0);
    }
    public static void runTest(){
        double aveSuccess = 0;
        int hung = 0;
        double fail = 0;
        long total = System.nanoTime();
        for(int graphs = 1; graphs <= 100; graphs++) {
            int success = 0;
            ArrayList<ArrayList<Graph.Node>> maze = Graph.buildGraph();
            for (int runs = 1; runs <= 30; runs++) {
                String result = TestAgent.test(maze);
                if (result.equals("true"))
                    success++;
                else if (result.equals("hung"))
                    hung++;
                else
                    fail++;
            }
            aveSuccess += success;
        }

        long endTime = System.nanoTime();
        long duration = (endTime - total)/(long)Math.pow(10,9);
        System.out.println(duration);
        System.out.println(hung);
        System.out.println("Hung out of Loss: " + hung/(fail+hung));
        System.out.println("Test: " + (aveSuccess)/3000.0);
    }

    public static void runAll(){
        int[] success = new int[13];
        int[] hung    = new int[13];
        int[] fail    = new int[13];

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

                result = PartialPrey.agentFour(maze);
                if (result.equals("true"))
                    success[4]++;
                else if (result.equals("hung"))
                    hung[4]++;
                else
                    fail[4]++;

                result = PartialPredator.agentFive(maze);
                if (result.equals("true"))
                    success[5]++;
                else if (result.equals("hung"))
                    hung[5]++;
                else
                    fail[5]++;

                result = PartialPredator.agentSix(maze);
                if (result.equals("true"))
                    success[6]++;
                else if (result.equals("hung"))
                    hung[6]++;
                else
                    fail[6]++;

                result = CombinedPartialInformation.agentSeven(maze);
                if (result.contains("true"))
                    success[7]++;
                else if (result.equals("hung"))
                    hung[7]++;
                else
                    fail[7]++;

                result = CombinedPartialInformation.agentEight(maze);
                if (result.equals("true"))
                    success[8]++;
                else if (result.equals("hung"))
                    hung[8]++;
                else
                    fail[8]++;

                result = FaultyCombinedPartialInformation.agentSevenFaulty(maze);
                if (result.contains("true"))
                    success[9]++;
                else if (result.equals("hung"))
                    hung[9]++;
                else
                    fail[9]++;

                result = FaultyCombinedPartialInformation.agentEightFaulty(maze);
                if (result.equals("true"))
                    success[10]++;
                else if (result.equals("hung"))
                    hung[10]++;
                else
                    fail[10]++;

                result = FixedFaultyCombinedPartialInformation.agentSevenFaultyFixed(maze);
                if (result.contains("true"))
                    success[11]++;
                else if (result.equals("hung"))
                    hung[11]++;
                else
                    fail[11]++;

                result = FixedFaultyCombinedPartialInformation.agentEightFaultyFixed(maze);
                if (result.equals("true"))
                    success[12]++;
                else if (result.equals("hung"))
                    hung[12]++;
                else
                    fail[12]++;

            }
            long endTime = System.nanoTime();
            long duration = (endTime - total)/(long)Math.pow(10,9);
            System.out.println("Iter: " + graphs + "; Time:" + duration);

        }
        long endTime = System.nanoTime();
        long duration = (endTime - total)/(long)Math.pow(10,9);
        System.out.println(duration);
        for(int x = 1; x < success.length; x++){
            System.out.println(hung[x]);
            System.out.println("Hung out of Loss: " + (double)hung[x]/((double)fail[x]+hung[x]));
            if(x < 9) {
                System.out.println("A" + x + ": " + (success[x]) / 3000.0);
            }
            else if(x == 9){
                System.out.println("A7 Faulty: " + (success[x])/3000.0);
            }
            else if(x == 10){
                System.out.println("A8 Faulty: " + (success[x])/3000.0);
            }
            else if(x == 11){
                System.out.println("A7 Faulty Fixed: " + (success[x])/3000.0);
            }
            else if(x == 12){
                System.out.println("A8 Faulty Fixed: " + (success[x])/3000.0);

            }
        }



    }


    public static void main (String args[]) {
//        ArrayList<ArrayList<Graph.Node>> maze = Graph.buildGraph();
//        runOne();
//        runTwo();
//        runThree();
//        runFour();
//        runFive();
//        runSix();
//        runSeven();
//        runEight();
//        runSevenFaulty();
//        runEightFaulty();
//        runSevenFaultyFixed();
//        runEightFaultyFixed();

//        runTest();
        runAll();





    }


}
