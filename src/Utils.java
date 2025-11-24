import java.io.FileWriter;
import java.util.List;
import java.util.Map;

public class Utils {

    public static void logResult(String filename, SearchResult res) {
        try {
            try (FileWriter fw = new FileWriter(filename)) {
                fw.write("Path: " + res.path + "\n");
                fw.write("Stats: " + res.stats + "\n");
            }
            System.out.println("Saved log to " + filename);
        } catch (Exception e) {
            System.err.println("Failed to save log: " + e.getMessage());
        }
    }

    public static void printResult(SearchResult res) {
        System.out.println("Path: " + res.path);
        System.out.println("Stats: " + res.stats);
    }
}
