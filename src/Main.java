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

    public static void runAll(){
        int[] success = new int[5];
        int[] hung = new int[5];
        int[] fail = new int[5];

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
            }

        }
        long endTime = System.nanoTime();
        long duration = (endTime - total)/(long)Math.pow(10,9);
        System.out.println(duration);
        for(int x = 1; x < success.length; x++){
            System.out.println(hung[x]);
            System.out.println("Hung out of Loss: " + (double)hung[x]/((double)fail[x]+hung[x]));
            System.out.println("A" +x + ": " + (success[x])/3000.0);
        }


    }


    public static void main (String args[]) {
        ArrayList<ArrayList<Graph.Node>> maze = Graph.buildGraph();

//        runOne();
//        runTwo();
//        runThree();
        runAll();



        String result = CompleteInformation.agentTwo(maze);
        if (result.equals("true"))
            System.out.println("success");






    }

}
