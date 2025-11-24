import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 This class contains a small parser to extract integer pairs from a simple JSON-like file.
 It is intentionally minimal so it works without external libraries.
*/
public class DynamicObstacles {

    public static class Obstacle {
        public final String id;
        public final List<int[]> positions;
        public Obstacle(String id, List<int[]> positions) {
            this.id = id;
            this.positions = positions;
        }
        public int[] positionAt(int t) {
            if (positions.isEmpty()) return null;
            return positions.get(t % positions.size());
        }
    }

    private final List<Obstacle> obstacles;

    public DynamicObstacles(List<Obstacle> obs) { this.obstacles = obs; }

    public static DynamicObstacles fromFile(String path) {
        try {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(path))) {
                String line;
                while ((line = br.readLine()) != null) sb.append(line).append("\n");
            }
            String text = sb.toString();
            // find all occurrences of [number,number]
            Pattern p = Pattern.compile("\\[\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\]");
            Matcher m = p.matcher(text);
            List<int[]> all = new ArrayList<>();
            while (m.find()) {
                int a = Integer.parseInt(m.group(1));
                int b = Integer.parseInt(m.group(2));
                all.add(new int[]{a,b});
            }
            // If none found, return empty obstacles
            if (all.isEmpty()) return new DynamicObstacles(Collections.emptyList());
            // For simplicity: treat each contiguous block (separated by non-overlapping positions per obstacle) as one obstacle.
            // Here we will split the list into two obstacles if size >=4, else single.
            List<Obstacle> obs = new ArrayList<>();
            if (all.size() <= 4) {
                obs.add(new Obstacle("o1", all));
            } else {
                // split into two for demo
                List<int[]> p1 = new ArrayList<>();
                List<int[]> p2 = new ArrayList<>();
                for (int i = 0; i < all.size(); i++) {
                    if (i % 2 == 0) p1.add(all.get(i));
                    else p2.add(all.get(i));
                }
                obs.add(new Obstacle("o1", p1));
                obs.add(new Obstacle("o2", p2));
            }
            return new DynamicObstacles(obs);
        } catch (Exception e) {
            return new DynamicObstacles(Collections.emptyList());
        }
    }

    public Set<String> occupiedPositionsAt(int t) {
        Set<String> s = new HashSet<>();
        for (Obstacle o : obstacles) {
            int[] p = o.positionAt(t);
            if (p != null) s.add(p[0] + "," + p[1]);
        }
        return s;
    }
}
