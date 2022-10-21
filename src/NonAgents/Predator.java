package NonAgents;

import java.util.*;

public class Predator {
    public static List<Graph.Node> bfs(int start, ArrayList<ArrayList<Graph.Node>> maze){
//      fringe to store cells that need to be visited
        Queue<Graph.Node> fringe = new LinkedList<>();
//      originally used a 2d array but space complexity is too huge, so it is now a set.
//        boolean[][] visited = new boolean[maze.length][maze[0].length];
        HashSet<Integer> visited = new HashSet();
//      used to verify if end source has been achieved

//      add beginning cell to fringe and visited
        fringe.add(new Graph.Node(start, true, false, 0));
        visited.add(start);
        while(!fringe.isEmpty()) {
//          use poll instead of remove so no errors are thrown
            Graph.Node curr = fringe.poll();
//          indX and indY hold current positions, didn't want to keep using curr.x or curr.y for laziness sakes
            int ind = curr.cell;
//            System.out.println(indX + ", " + indY + " start");
//          if arrived at destination
            if (maze.get(ind).get(0).agent) {
//                System.out.println("finish");
                List<Graph.Node> path = new ArrayList<>();;
                getPath(curr, path);
//                System.out.println("here");
                return path;
            }
//          checks all neighbors to see if they are eligible to be added to the fringe
            List<Graph.Node> edges = maze.get(ind).subList(1, maze.get(ind).size());
            for(Graph.Node n: edges)
            {
                visited.add(n.cell);
                fringe.add(new Graph.Node(n.cell, curr));

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
