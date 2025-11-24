public class Cell {
    public final int x;
    public final int y;
    public final int cost; // -1 = blocked, otherwise traversal cost (1 default)

    public Cell(int x, int y, int cost) {
        this.x = x;
        this.y = y;
        this.cost = cost;
    }

    public boolean isBlocked() {
        return cost == -1;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Cell)) return false;
        Cell c = (Cell) o;
        return this.x == c.x && this.y == c.y;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(x) * 31 + Integer.hashCode(y);
    }
}
