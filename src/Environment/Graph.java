package Environment;
import org.apache.commons.lang3.builder.*;
import java.util.ArrayList;
import java.util.Random;

public class Graph {
    int cell;
    Node node;
    public static class Node{
        public Node prev;


        int agentDistance;
        int cell;


//        Predator constructor
        public Node(int cell,int agentDistance){
            this.cell = cell;
            this.agentDistance = agentDistance;


        }

        public Node(int cell){
            this.cell = cell;
        }
//      used for bfs
        public Node(int cell, Node prev){
            this.cell = cell;
            this.prev = prev;
        }


        public int getCell(){
            return cell;
        }

        public void setCell(int cell){
            this.cell = cell;
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
            return ("Cell: " + cell);
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
            temp.add(new Node(x));
//            codes the wrap around
            if (x==0){
                temp.add(new Node(x + 1));
                temp.add(new Node(49));
            } else if (x == 49) {
                temp.add(new Node(0));
                temp.add(new Node(x-1));
            } else {
//                creates bidirectional edge
                temp.add(new Node(x + 1));
                temp.add(new Node(x - 1));

            }
            graph.add(temp);
        }
        return graph;

    }

    static boolean contains(ArrayList<Node> skeleton, Node node){
        for(int x = 0; x < skeleton.size(); x++){
            if (skeleton.get(x).getCell() == node.getCell()){
                return true;
            }
        }
        return false;

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
            int value = currNode.getCell();
            int range = new Random().nextInt(5) + 1;
            int sign = new Random().nextInt(2);
//            negative if 1 and positive is 0
            sign = sign == 1 ? -1 : 1;

            int newEdge = value + range * sign;

//            wrapAround
            Node newNode = null;
            int count = 0;
            while(newNode == null){
                range = new Random().nextInt(5) + 1;
                sign = new Random().nextInt(2);
                sign = sign == 1 ? -1 : 1;
                newEdge = value + range* sign;
                newEdge = newEdge < 0 ? 50 + newEdge : newEdge;
                newEdge = newEdge > 49 ? newEdge - 50 : newEdge;

                newNode = full.contains(new Node(newEdge)) ? full.get(full.indexOf(new Node(newEdge))) : null;


                if (newNode != null && contains(skeleton.get(currNode.getCell()), newNode)){
                    newNode = null;
                    continue;
                }
                count += 1;
//                abritray limit to exhaust all possibilities that no possible edge
                if (count > 50){

                    return skeleton;
                }
            }


            if(contains(skeleton.get(currNode.getCell()), newNode)){
                return null;
            }
            skeleton.get(currNode.getCell()).add(new Node(newEdge));
            skeleton.get(newNode.getCell()).add(new Node(value));
            full.remove(full.indexOf(currNode));
            full.remove(full.indexOf(newNode));

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
