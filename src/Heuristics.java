public class Heuristics {
    public enum Type { MANHATTAN, EUCLIDEAN }

    public static double estimate(Cell a, Cell b, Type type) {
        int dx = Math.abs(a.x - b.x);
        int dy = Math.abs(a.y - b.y);
        switch (type) {
            case EUCLIDEAN:
                return Math.hypot(dx, dy);
            case MANHATTAN:
            default:
                return dx + dy;
        }
    }
}
