import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        // Defaults
        String mapPath = "maps/small.txt";
        String dynamicPath = "maps/dynamic.json";
        String algo = "astar";
        String startStr = "0,0";
        String goalStr = "4,4";
        boolean diag = false;
        String logFile = "runlog.txt";
        Heuristics.Type heuristic = Heuristics.Type.MANHATTAN;

        for (int i=0;i<args.length;i++) {
            switch (args[i]) {
                case "--map": mapPath = args[++i]; break;
                case "--dynamic": dynamicPath = args[++i]; break;
                case "--algo": algo = args[++i].toLowerCase(); break;
                case "--start": startStr = args[++i]; break;
                case "--goal": goalStr = args[++i]; break;
                case "--diag": diag = Boolean.parseBoolean(args[++i]); break;
                case "--log": logFile = args[++i]; break;
                case "--heuristic":
                    heuristic = Heuristics.Type.valueOf(args[++i].toUpperCase());
                    break;
                default: break;
            }
        }

        Grid grid = Grid.fromFile(mapPath);
        DynamicObstacles dyn = DynamicObstacles.fromFile(dynamicPath);

        String[] sParts = startStr.split(",");
        String[] gParts = goalStr.split(",");
        Cell start = new Cell(Integer.parseInt(sParts[0]), Integer.parseInt(sParts[1]), 1);
        Cell goal = new Cell(Integer.parseInt(gParts[0]), Integer.parseInt(gParts[1]), 1);

        SearchResult result;
        switch (algo) {
            case "bfs":
                result = SearchAlgorithms.bfs(grid, start, goal, diag);
                break;
            case "ucs":
                result = SearchAlgorithms.ucs(grid, start, goal, diag);
                break;
            case "astar":
            default:
                result = SearchAlgorithms.aStar(grid, start, goal, diag, heuristic);
                break;
        }

        Utils.printResult(result);
        Utils.logResult(logFile, result);

        try {
            Visualizer.saveVisualization(grid, result.path, "visualization.png");
            System.out.println("Saved visualization.png");
        } catch (Exception e) {
            System.out.println("Visualization failed: " + e.getMessage());
        }
    }
}
