import java.util.List;
import java.util.Map;

public class SearchResult {
    public final List<Cell> path;
    public final Map<String, Object> stats;

    public SearchResult(List<Cell> path, Map<String, Object> stats) {
        this.path = path;
        this.stats = stats;
    }
}
