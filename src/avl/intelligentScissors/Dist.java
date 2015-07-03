package avl.intelligentScissors;

import java.util.ArrayList;

public final class Dist {

    private final int tileSize = 128;
    private final ArrayList<ArrayList<float[][]>> xTiles;
    private int m,n;

    public Dist(int m, int n, int rowIdx, int colIdx) {
        this.m = m;
        this.n = n;
        int numOfXtiles = (int) Math.ceil((float) m / tileSize);
        int numOfYtiles = (int) Math.ceil((float) n / tileSize);
        xTiles = new ArrayList<>();
        for (int x = 0; x < numOfXtiles; x++) {
            ArrayList<float[][]> temp = new ArrayList<>();
            for (int y = 0; y < numOfYtiles; y++) {
                temp.add(null);
            }
            xTiles.add(temp);
        }
        set(rowIdx, colIdx, 0);
    }

    private float[][] newTile() {
        float[][] tile = new float[tileSize][tileSize];
        for (int ix = 0; ix < tileSize; ix++) {
            for (int iy = 0; iy < tileSize; iy++) {
                tile[ix][iy] = Float.MAX_VALUE;
            }
        }
        return tile;
    }

    public void set(int x, int y, float v) {
        int xIdx = (int) Math.floor((float) x / tileSize);
        int yIdx = (int) Math.floor((float) y / tileSize);
        ArrayList<float[][]> yTiles = xTiles.get(xIdx);
        float tile[][] = yTiles.get(yIdx);
        if (tile == null) {
            tile = newTile();
            yTiles.set(yIdx, tile);
        }
        tile[x - xIdx * tileSize][y - yIdx * tileSize] = v;
    }

    public float get(int x, int y) {
        int xIdx = (int) Math.floor((float) x / tileSize);
        int yIdx = (int) Math.floor((float) y / tileSize);
        ArrayList<float[][]> yTiles = xTiles.get(xIdx);
        float tile[][] = yTiles.get(yIdx);
        if (tile == null) {
            tile = newTile();
            yTiles.set(yIdx, tile);
        }
        if ((x <= 0) || (y <= 0) || (x>=m-1) || (y>=n-1)){
            return Float.MAX_VALUE;
        }
        return tile[x - xIdx * tileSize][y - yIdx * tileSize];
    }

}
