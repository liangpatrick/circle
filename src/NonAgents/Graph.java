package NonAgents;
import org.apache.commons.lang3.builder.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;

public class Graph {
    int cell;
    Node node;
    static class Node{
        Node prev;

        boolean prey;
        boolean predator;
        boolean agent;
        int preyDistance;
        int predatorDistance;
        int agentDistance;
        int cell;
//        Agent constructor
        public Node(int cell, boolean prey, boolean predator, boolean agent, int predatorDistance, int preyDistance){
            this.cell = cell;
            this.prey = prey;
            this.predator = predator;
            this.agent = agent;
            this.predatorDistance = predatorDistance;
            this.preyDistance = preyDistance;

        }
//        Predator constructor
        public Node(int cell, boolean predator, boolean agent, int agentDistance){
            this.cell = cell;
            this.predator = predator;
            this.agent = agent;
            this.agentDistance = agentDistance;


        }
//      used for equals comparisons
        public Node(int cell){
            this.cell = cell;
        }
//      used for bfs
        public Node(int cell, Node prev){
            this.cell = cell;
            this.prev = prev;
        }


        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
                    // if deriving: appendSuper(super.hashCode()).
                            append(cell).
//                    append(age).
                    toHashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Node))
                return false;
            if (obj == this)
                return true;

            Node rhs = (Node) obj;
            return new EqualsBuilder().
                    // if deriving: appendSuper(super.equals(obj)).
                            append(cell, rhs.cell).
//                    append(age, rhs.age).
                    isEquals();
        }

        @Override
        public String toString(){
//            return ("Cell: " + cell + "; Prey: " + prey + "; Predator: " + predator + "; Agent: " + agent + "; Predator Distance: " + predatorDistance + "; Prey Distance: " + preyDistance);
            return ("Cell: " + cell + "; Agent: " + agent);


//            return ("Cell: " + cell);
        }

    }

    public Graph(int cell, Node node){
        this.cell = cell;
        this.node = node;
    }
//  wrapper method which generates a graph that satisfies conditions

    public static ArrayList<ArrayList<Node>> buildGraph(){
        return addRandomEdges(buildSkeletonGraph());
    }

//  creates initial undirected graph
    public static ArrayList<ArrayList<Node>> buildSkeletonGraph(){
        ArrayList<ArrayList<Node>> graph = new ArrayList<>();
        for(int x = 0; x < 50; x++){
            ArrayList<Node> temp = new ArrayList<>();
            temp.add(new Node(x, false, false, false, -1, -1));
//            codes the wrap around
            if (x==0){
                temp.add(new Node(x + 1, false, false, false, -1, -1));
                temp.add(new Node(49, false, false, false, -1, -1));
            } else if (x == 49) {
                temp.add(new Node(0, false, false, true, -1, -1));
                temp.add(new Node(x-1, false, false, false, -1, -1));
            } else {
//                creates bidirectional edge
                temp.add(new Node(x + 1, false, false, false, -1, -1));
                temp.add(new Node(x - 1, false, false, false, -1, -1));
            }
            graph.add(temp);
        }
        graph.get(0).get(0).agent = true;
        return graph;

    }
//  adds random edges
    static ArrayList<ArrayList<Node>> addRandomEdges(ArrayList<ArrayList<Node>> skeleton){
        ArrayList<Node> full = new ArrayList<>();
        for(ArrayList<Node> arr: skeleton) {
            full.add(arr.get(0));
        }
        while(!full.isEmpty()){
            int index = new Random().nextInt(full.size());

            Node currNode = full.get(index);
            int value = currNode.cell;
            int range = new Random().nextInt(5) + 1;
            int sign = new Random().nextInt(2);
//            negative if 1 and positive is 0
            sign = sign == 1 ? -1 : 1;

            int newEdge = value + range * sign;

//            wrapAround
            newEdge = newEdge < 0 ? 50 + newEdge : newEdge;
            newEdge = newEdge > 49 ? newEdge - 50 : newEdge;
            Node newNode = full.contains(new Node(newEdge)) ? full.get(full.indexOf(new Node(newEdge))) : null;
            int count = 0;

            while(newNode == null || !full.contains(newNode)){
                range = new Random().nextInt(5) + 1;
                sign = new Random().nextInt(2);
                sign = sign == 1 ? -1 : 1;
                newEdge = value + range* sign;
                newEdge = newEdge < 0 ? 50 + newEdge : newEdge;
                newEdge = newEdge > 49 ? newEdge - 50 : newEdge;
                newNode = full.contains(new Node(newEdge)) ? full.get(full.indexOf(new Node(newEdge))) : null;
                count += 1;
//                abritray limit to exhaust all possibilities that no possible edge
                if (count > 50){
                    return skeleton;
                }
            }

//            int fullNewEdgeIndex = full.indexOf(newEdge);
            skeleton.get(currNode.cell).add(new Node(newEdge, false, false, false, -1, -1));
            skeleton.get(newNode.cell).add(new Node(value, false, false, false, -1, -1));
            full.remove(currNode);
            full.remove(newNode);

        }
        return skeleton;
    }

    public static void printGraph(ArrayList<ArrayList<Node>> graph){
         for(ArrayList<Node> key: graph) {

//             System.out.print(key + ": " );
//             graph.get(key.cell).forEach(entry -> {
//                 System.out.print(entry + "; ");
//             });
             System.out.println(key.toString());
             System.out.println();
         }
    }


}
