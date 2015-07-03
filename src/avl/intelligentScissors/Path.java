package avl.intelligentScissors;

import java.awt.Point;
import java.util.ArrayList;

public final class Path {

    protected final int m, n;
    protected final int tileSize = 128;
    private final ArrayList<ArrayList<Point[][]>> xTiles;

    public Path(int m, int n) {
        this.m = m;
        this.n = n;
        int numOfXtiles = (int) Math.ceil((float) m / tileSize);
        int numOfYtiles = (int) Math.ceil((float) n / tileSize);
        xTiles = new ArrayList<>();
        for (int x = 0; x < numOfXtiles; x++) {
            ArrayList<Point[][]> temp = new ArrayList<>();
            for (int y = 0; y < numOfYtiles; y++) {
                temp.add(null);
            }
            xTiles.add(temp);
        }
    }

    public int getM() {
        return m;
    }

    public int getN() {
        return n;
    }

    public int getTileSize() {
        return tileSize;
    }

    public boolean isInBounds(Point p) {
        return (p.x >= 0) && (p.x < m) && (p.y >= 0) && (p.y < n);
    }

    private Point[][] newTile() {
        Point[][] tile = new Point[tileSize][tileSize];
        for (int ix = 0; ix < tileSize; ix++) {
            for (int iy = 0; iy < tileSize; iy++) {
                tile[ix][iy] = new Point();
            }
        }
        return tile;
    }

    public void set(Point src, Point dest) {
        int xIdx = (int) Math.floor((float) src.x / tileSize);
        int yIdx = (int) Math.floor((float) src.y / tileSize);
        ArrayList<Point[][]> yTiles = xTiles.get(xIdx);
        Point tile[][] = yTiles.get(yIdx);
        if (tile == null) {
            tile = newTile();
            yTiles.set(yIdx, tile);
        }
        tile[src.x - xIdx * tileSize][src.y - yIdx * tileSize] = dest;
    }

    public Point get(Point p) {
        int xIdx = (int) Math.floor((float) p.x / tileSize);
        int yIdx = (int) Math.floor((float) p.y / tileSize);
        ArrayList<Point[][]> yTiles = xTiles.get(xIdx);
        Point tile[][] = yTiles.get(yIdx);
        if (tile == null) {
            tile = newTile();
            yTiles.set(yIdx, tile);
        }
        return tile[p.x - xIdx * tileSize][p.y - yIdx * tileSize];
    }

    public boolean isactive(Point p) {
        if (xTiles == null) {
            return false;
        }
        if (p.x >= xTiles.size()) {
            return false;
        }
        ArrayList<Point[][]> yTiles = xTiles.get(p.x);
        if (yTiles == null) {
            return false;
        }
        if (p.y >= yTiles.size()) {
            return false;
        }
        Point tile[][] = yTiles.get(p.y);
        if (tile == null) {
            return false;
        } else {
            return true;
        }
    }

    public void inc(Point p) {
        int xIdx = (int) Math.floor((float) p.x / tileSize);
        int yIdx = (int) Math.floor((float) p.y / tileSize);
        ArrayList<Point[][]> yTiles = xTiles.get(xIdx);
        Point tile[][] = yTiles.get(yIdx);
        if (tile == null) {
            tile = newTile();
            yTiles.set(yIdx, tile);
        }
        tile[p.x - xIdx * tileSize][p.y - yIdx * tileSize].x += 1;
    }
}
