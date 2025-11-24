import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Grid {
    private final int width;
    private final int height;
    private final int[][] cells; // stores costs, -1 blocked

    public Grid(int width, int height, int[][] cells) {
        this.width = width;
        this.height = height;
        this.cells = cells;
    }

    public static Grid fromFile(String path) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String first = br.readLine();
            if (first == null) throw new Exception("Empty map file");
            StringTokenizer st = new StringTokenizer(first);
            int w = Integer.parseInt(st.nextToken());
            int h = Integer.parseInt(st.nextToken());
            int[][] arr = new int[h][w];
            for (int r = 0; r < h; r++) {
                String line = br.readLine();
                if (line == null) throw new Exception("Unexpected EOF reading map");
                st = new StringTokenizer(line);
                for (int c = 0; c < w; c++) {
                    arr[r][c] = Integer.parseInt(st.nextToken());
                }
            }
            return new Grid(w, h, arr);
        }
    }

    public boolean inBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public Cell getCell(int x, int y) {
        if (!inBounds(x,y)) return null;
        return new Cell(x, y, cells[y][x]);
    }

    /**
     * Generate 4-neighbors or 8-neighbors depending on diagonals flag.
     */
    public List<Cell> neighbors(Cell cell, boolean diagonals) {
        List<Cell> n = new ArrayList<>();
        int[][] deltas = diagonals
                ? new int[][]{{1,0},{-1,0},{0,1},{0,-1},{1,1},{1,-1},{-1,1},{-1,-1}}
                : new int[][]{{1,0},{-1,0},{0,1},{0,-1}};
        for (int[] d : deltas) {
            int nx = cell.x + d[0];
            int ny = cell.y + d[1];
            if (inBounds(nx, ny) && cells[ny][nx] != -1) {
                n.add(new Cell(nx, ny, cells[ny][nx]));
            }
        }
        return n;
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
