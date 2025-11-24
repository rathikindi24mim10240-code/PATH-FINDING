import java.util.*;

public class SearchAlgorithms {

    public static SearchResult bfs(Grid grid, Cell start, Cell goal, boolean diagonals) {
        long startTime = System.currentTimeMillis();
        Queue<Cell> q = new LinkedList<>();
        Map<Cell, Cell> parent = new HashMap<>();
        Set<Cell> visited = new HashSet<>();
        q.add(start);
        visited.add(start);
        boolean found = false;
        int nodesExpanded = 0;
        while (!q.isEmpty()) {
            Cell cur = q.poll();
            nodesExpanded++;
            if (cur.equals(goal)) { found = true; break; }
            for (Cell nb : grid.neighbors(cur, diagonals)) {
                if (!visited.contains(nb)) {
                    visited.add(nb);
                    parent.put(nb, cur);
                    q.add(nb);
                }
            }
        }
        List<Cell> path = reconstructPath(parent, start, goal, found);
        long endTime = System.currentTimeMillis();
        Map<String,Object> stats = new HashMap<>();
        stats.put("nodes_expanded", nodesExpanded);
        stats.put("time_ms", endTime - startTime);
        stats.put("path_cost", pathCost(path));
        return new SearchResult(path, stats);
    }

    public static SearchResult ucs(Grid grid, Cell start, Cell goal, boolean diagonals) {
        long startTime = System.currentTimeMillis();
        Comparator<Node> comp = Comparator.comparingDouble(n -> n.cost);
        PriorityQueue<Node> pq = new PriorityQueue<>(comp);
        Map<Cell, Double> bestCost = new HashMap<>();
        Map<Cell, Cell> parent = new HashMap<>();
        pq.add(new Node(start, 0.0));
        bestCost.put(start, 0.0);
        int nodesExpanded = 0;
        boolean found = false;
        while (!pq.isEmpty()) {
            Node cur = pq.poll();
            nodesExpanded++;
            if (cur.cell.equals(goal)) { found = true; break; }
            if (cur.cost > bestCost.getOrDefault(cur.cell, Double.POSITIVE_INFINITY)) continue;
            for (Cell nb : grid.neighbors(cur.cell, diagonals)) {
                double nc = cur.cost + Math.max(1, nb.cost); // treat cost >=1; if cost is 1 it's unit but allow other costs
                if (nc < bestCost.getOrDefault(nb, Double.POSITIVE_INFINITY)) {
                    bestCost.put(nb, nc);
                    parent.put(nb, cur.cell);
                    pq.add(new Node(nb, nc));
                }
            }
        }
        List<Cell> path = reconstructPath(parent, start, goal, found);
        long endTime = System.currentTimeMillis();
        Map<String,Object> stats = new HashMap<>();
        stats.put("nodes_expanded", nodesExpanded);
        stats.put("time_ms", endTime - startTime);
        stats.put("path_cost", pathCost(path));
        return new SearchResult(path, stats);
    }

    public static SearchResult aStar(Grid grid, Cell start, Cell goal, boolean diagonals, Heuristics.Type heuristic) {
        long startTime = System.currentTimeMillis();
        class ANode {
            Cell cell; double g; double f;
            ANode(Cell c, double g, double f) { this.cell = c; this.g = g; this.f = f; }
        }
        Comparator<ANode> comp = Comparator.comparingDouble(n -> n.f);
        PriorityQueue<ANode> open = new PriorityQueue<>(comp);
        Map<Cell, Double> gScore = new HashMap<>();
        Map<Cell, Cell> parent = new HashMap<>();
        gScore.put(start, 0.0);
        double f0 = Heuristics.estimate(start, goal, heuristic);
        open.add(new ANode(start, 0.0, f0));
        int nodesExpanded = 0;
        boolean found = false;
        while (!open.isEmpty()) {
            ANode cur = open.poll();
            nodesExpanded++;
            if (cur.cell.equals(goal)) { found = true; break; }
            if (cur.f - Heuristics.estimate(cur.cell, goal, heuristic) > gScore.getOrDefault(cur.cell, Double.POSITIVE_INFINITY)) {
                // outdated node
                continue;
            }
            for (Cell nb : grid.neighbors(cur.cell, diagonals)) {
                double tentativeG = cur.g + Math.max(1, nb.cost);
                if (tentativeG < gScore.getOrDefault(nb, Double.POSITIVE_INFINITY)) {
                    parent.put(nb, cur.cell);
                    gScore.put(nb, tentativeG);
                    double f = tentativeG + Heuristics.estimate(nb, goal, heuristic);
                    open.add(new ANode(nb, tentativeG, f));
                }
            }
        }
        List<Cell> path = reconstructPath(parent, start, goal, found);
        long endTime = System.currentTimeMillis();
        Map<String,Object> stats = new HashMap<>();
        stats.put("nodes_expanded", nodesExpanded);
        stats.put("time_ms", endTime - startTime);
        stats.put("path_cost", pathCost(path));
        return new SearchResult(path, stats);
    }

    private static List<Cell> reconstructPath(Map<Cell, Cell> parent, Cell start, Cell goal, boolean found) {
        List<Cell> path = new ArrayList<>();
        if (!found) return path;
        Cell cur = goal;
        while (cur != null && !cur.equals(start)) {
            path.add(cur);
            cur = parent.get(cur);
        }
        if (start != null) path.add(start);
        Collections.reverse(path);
        return path;
    }

    private static int pathCost(List<Cell> path) {
        if (path == null || path.isEmpty()) return 0;
        // cost is number of steps or sum of cell costs (simple: steps-1)
        return Math.max(0, path.size() - 1);
    }

    // small helper node for UCS comparator
    private static class Node {
        final Cell cell;
        final double cost;
        Node(Cell cell, double cost) { this.cell = cell; this.cost = cost; }
    }
}
