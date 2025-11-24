import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

public class Visualizer {

    public static void saveVisualization(Grid grid, List<Cell> path, String filename) throws Exception {
        int cellSize = 40;
        int w = grid.getWidth() * cellSize;
        int h = grid.getHeight() * cellSize;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0,0,w,h);

        // draw grid cells
        for (int y=0;y<grid.getHeight();y++) {
            for (int x=0;x<grid.getWidth();x++) {
                int cx = x * cellSize;
                int cy = y * cellSize;
                Cell c = grid.getCell(x,y);
                g.setColor(c.isBlocked() ? Color.DARK_GRAY : Color.LIGHT_GRAY);
                g.fillRect(cx, cy, cellSize, cellSize);
                g.setColor(Color.BLACK);
                g.drawRect(cx, cy, cellSize, cellSize);
            }
        }

        // draw path
        if (path != null && !path.isEmpty()) {
            g.setStroke(new BasicStroke(4));
            g.setColor(Color.RED);
            for (int i=0; i<path.size()-1; i++) {
                Cell a = path.get(i);
                Cell b = path.get(i+1);
                int ax = a.x * cellSize + cellSize/2;
                int ay = a.y * cellSize + cellSize/2;
                int bx = b.x * cellSize + cellSize/2;
                int by = b.y * cellSize + cellSize/2;
                g.drawLine(ax, ay, bx, by);
            }
            // start and goal
            Cell s = path.get(0);
            Cell gcell = path.get(path.size()-1);
            g.setColor(Color.GREEN);
            g.fillOval(s.x*cellSize + cellSize/4, s.y*cellSize + cellSize/4, cellSize/2, cellSize/2);
            g.setColor(Color.BLUE);
            g.fillOval(gcell.x*cellSize + cellSize/4, gcell.y*cellSize + cellSize/4, cellSize/2, cellSize/2);
        }

        ImageIO.write(img, "PNG", new File(filename));
        g.dispose();
    }
}
