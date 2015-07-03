package avl.intelligentScissors;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.jai.JAI;
import javax.media.jai.KernelJAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.operator.FormatDescriptor;

public class IntelligentScissors extends MouseAdapter {

    PlanarImage imgFloat;

    private Dist dist;
    private Path path;

    Point lastPoint = new Point();

    private Point start_node;
    private int imageWidth, imageHeight, yLast, xLast;

    private Path2D.Double fixedROI = null;
    private Path2D.Double dynamicROI = null;
    private Point startPoint;

    private boolean approximateCosInv = true, demo = false;
    private IntelligentScissorsWeights weights = new IntelligentScissorsWeights();
    private final IntelligentScissorsInterface intelligentScissorsInterface;

    private final static float mask5x5[] = {0.009345855f, 0.046901669f, 0.074026778f, 0.046901669f, 0.009345855f,
        0.046901669f, 0.093097717f, -0.076360062f, 0.093097717f, 0.046901669f,
        0.074026778f, -0.076360062f, -0.77565455f, -0.076360062f, 0.074026778f,
        0.046901669f, 0.093097717f, -0.076360062f, 0.093097717f, 0.046901669f,
        0.009345855f, 0.046901669f, 0.074026778f, 0.046901669f, 0.009345855f};

    private final static float mask9x9[] = {5.59E-05f, 0.000232978f, 0.00091235f, 0.002088718f, 0.002736173f, 0.002088718f, 0.00091235f, 0.000232978f, 5.59E-05f,
        0.000232978f, 0.001588481f, 0.005942293f, 0.011815447f, 0.014364959f, 0.011815447f, 0.005942293f, 0.001588481f, 0.000232978f,
        0.00091235f, 0.005942293f, 0.017000664f, 0.01994336f, 0.01491824f, 0.01994336f, 0.017000664f, 0.005942293f, 0.00091235f,
        0.002088718f, 0.011815447f, 0.01994336f, -0.023389196f, -0.070777208f, -0.023389196f, 0.01994336f, 0.011815447f, 0.002088718f,
        0.002736173f, 0.014364959f, 0.01491824f, -0.070777208f, -0.15347332f, -0.070777208f, 0.01491824f, 0.014364959f, 0.002736173f,
        0.002088718f, 0.011815447f, 0.01994336f, -0.023389196f, -0.070777208f, -0.023389196f, 0.01994336f, 0.011815447f, 0.002088718f,
        0.00091235f, 0.005942293f, 0.017000664f, 0.01994336f, 0.01491824f, 0.01994336f, 0.017000664f, 0.005942293f, 0.00091235f,
        0.000232978f, 0.001588481f, 0.005942293f, 0.011815447f, 0.014364959f, 0.011815447f, 0.005942293f, 0.001588481f, 0.000232978f,
        5.59E-05f, 0.000232978f, 0.00091235f, 0.002088718f, 0.002736173f, 0.002088718f, 0.00091235f, 0.000232978f, 5.59E-05f};

    private final KernelJAI vert = KernelJAI.GRADIENT_MASK_SOBEL_VERTICAL;
    private final KernelJAI horz = KernelJAI.GRADIENT_MASK_SOBEL_HORIZONTAL;

    private final int PADDING = 5, COST_DIM = 100;
    private final int PADDED_DIM = COST_DIM + 2 * PADDING;
    private float costM[][][] = new float[COST_DIM][COST_DIM][8];

    private ZeroCrossGen zeroCross5x5Runnable = new ZeroCrossGen(new KernelJAI(5, 5, mask5x5));
    private ZeroCrossGen zeroCross9x9Runnable = new ZeroCrossGen(new KernelJAI(9, 9, mask9x9));

    public IntelligentScissorsWeights getWeights() {
        return weights;
    }

    private class ZeroCrossGen implements Callable<byte[][]> {

        final KernelJAI gaborKernel;

        public ZeroCrossGen(KernelJAI gaborKernel) {
            this.gaborKernel = gaborKernel;
        }

        @Override
        public byte[][] call() throws Exception {
            PlanarImage g5 = JAI.create("Convolve", imgFloat, gaborKernel).createInstance();
            float temp[] = new float[PADDED_DIM * PADDED_DIM];
            g5.getData().getSamples(0, 0, PADDED_DIM, PADDED_DIM, 0, temp);

            float gaborResult[][] = new float[PADDED_DIM][PADDED_DIM];
            int idx = 0;
            for (int i = 0; i < PADDED_DIM; i++) {
                for (int j = 0; j < PADDED_DIM; j++) {
                    gaborResult[j][i] = temp[idx++];
                }
            }
            
            double sum = 0;
            for (int i = 0; i < PADDED_DIM; i++) {
                for (int j = 0; j < PADDED_DIM; j++) {
                    sum += Math.abs(gaborResult[i][j]);
                }
            }
            double meanAbs = sum / (PADDED_DIM * PADDED_DIM);
            double thresh = 0.75 * meanAbs;

            byte cross[][] = new byte[PADDED_DIM][PADDED_DIM];
            for (int i = 1; i < PADDED_DIM - 1; i++) {
                for (int j = 1; j < PADDED_DIM - 1; j++) {
                    final int rOff[] = {i - 1, i, i + 1, i + 1, i + 1, i, i - 1, i - 1};
                    final int cOff[] = {j - 1, j - 1, j - 1, j, j + 1, j + 1, j + 1, j};
                    for (int K = 0; K < 4; K++) {
                        float v51 = gaborResult[rOff[K]][cOff[K]];
                        float v52 = gaborResult[rOff[K + 4]][cOff[K + 4]];
                        if ((Math.abs(v51) > thresh) && (Math.abs(v52) > thresh) && ((v51 * v52) < 0)) {
                            cross[i][j] = 0;
                        } else {
                            cross[i][j] = 1;
                        }
                    }
                }
            }
            return cross;
        }
    }

    private class CostMaxGen implements Runnable {

        private final int K, cOff, rOff;
        private final float minG, maxG;
        private final boolean isApproxCosineInvserse;
        private final float gradData[][], dxData[][], dyData[][];
        private final byte g5Cross[][], g9Cross[][];

        public CostMaxGen(int K, int cOff, int rOff, float minG, float maxG, boolean isApproxCosineInvserse, float[][] gradData, float[][] dxData, float[][] dyData, byte[][] g5Cross, byte[][] g9Cross ) {
            this.K = K;
            this.cOff = cOff;
            this.rOff = rOff;
            this.minG = minG;
            this.maxG = maxG;
            this.isApproxCosineInvserse = isApproxCosineInvserse;
            this.gradData = gradData;
            this.dxData = dxData;
            this.dyData = dyData;
            this.g5Cross = g5Cross;
            this.g9Cross = g9Cross;
        }

        @Override
        public void run() {
            for (int i = PADDING; i < PADDED_DIM - PADDING; i++) {
                for (int j = PADDING; j < PADDED_DIM - PADDING; j++) {
                    //Copy matrices into memory
                    float L0 = cOff;
                    float L1 = rOff;
                    float factor = 1;
                    if (K % 2 == 0) {
                        L0 *= 0.70710678118f;
                        L1 *= 0.70710678118f;
                        factor = 1.41421356f; // 1/sqrt(2)
                    }

                    float temp1 = (dyData[i][j] * L1 - dxData[i][j] * L0);
                    float temp2 = (dyData[cOff + i][rOff + j] * L1 - dxData[cOff + i][rOff + j] * L0);
                    temp1 = Math.abs(temp1);
                    temp2 = Math.abs(temp2);
                    float fd;
                    if (isApproxCosineInvserse) {
                        fd = (float) ((acosApprox(temp1) + acosApprox(temp2)) / Math.PI);
                    } else {
                        fd = (float) ((Math.acos(temp1) + Math.acos(temp2)) / Math.PI);
                    }
                    float fg = (1f - ((gradData[cOff + i][rOff + j] - minG) / (maxG - minG))) * factor;
                    float fz = (float) (0.45 * g5Cross[cOff + i][rOff + j] + 0.55 * g9Cross[cOff + i][rOff + j]) * factor;
                    double temp = weights.g * fg + weights.d * fd + weights.z * fz;
                    temp = Math.max(temp, 0.00001f);
                    costM[i - PADDING][j - PADDING][K] = (float) temp;
                }
            }
        }
    }

    public IntelligentScissors(IntelligentScissorsInterface intelligentScissorsInterface) {
        this.intelligentScissorsInterface = intelligentScissorsInterface;
    }

    public void setDemo(boolean demo) {
        this.demo = demo;
    }

    public void setApproximateCosInv(boolean approximateCosInv) {
        if (fixedROI == null) {
            this.approximateCosInv = approximateCosInv;
        }
    }

    public boolean getApproximateCosInv() {
        return this.approximateCosInv;
    }

    public void setWeights(IntelligentScissorsWeights weights) {
        if (dynamicROI == null) {
            this.weights = weights;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point point = adjustPoint(e.getPoint());
        int x = point.x;
        int y = point.y;
        if (fixedROI != null) {
            appendDynamicToFixed();
            dynamicROI = null;
            addPoint(fixedROI, x, y);
        }
        intelligentScissorsInterface.updated();
    }

    private void appendDynamicToFixed() {
        if (dynamicROI == null) {
            return;
        }
        PathIterator iter = dynamicROI.getPathIterator(new AffineTransform());
        ArrayList<float[]> pts = new ArrayList<>();
        while (!iter.isDone()) {
            float pt[] = new float[6];
            iter.currentSegment(pt);
            pts.add(pt);
            iter.next();
        }
        Collections.reverse(pts);
        pts.stream().forEach((pt) -> {
            addPoint(fixedROI, pt[0], pt[1]);
        });
    }

    private Point adjustPoint(Point p) {
        int x = p.x;
        int y = p.y;
        if (x <= 1) {
            x = 1;
        }
        if (x >= imageWidth - 2) {
            x = imageWidth - 2;
        }
        if (y <= 1) {
            y = 1;
        }
        if (y >= imageHeight - 2) {
            y = imageHeight - 2;
        }
        return new Point(x, y);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Point point = adjustPoint(e.getPoint());
        lastPoint = point;
        if (fixedROI != null) {
            Path2D.Double dynamicPathTemp = new Path2D.Double();
            Point[] pointPath = dijkstraMain(point.x, point.y, false);
            if (pointPath == null) {
                return;
            }
            for (Point p : pointPath) {
                addPoint(dynamicPathTemp, p.x, p.y);
            }
            dynamicROI = dynamicPathTemp;
        }
        intelligentScissorsInterface.updated();
    }

    public boolean isActive() {
        return fixedROI != null;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Point point = adjustPoint(e.getPoint());
        int x = point.x;
        int y = point.y;
        if (fixedROI == null) {
            fixedROI = new Path2D.Double();
            startPoint = new Point(x, y);
            dijkstraMain(x, y, true);
        } else if (e.getClickCount() > 1) {
            dynamicROI = null;
            closeROI();
        } else if (new Point(x, y).distance(startPoint) < 5) {
            appendDynamicToFixed();
            dynamicROI = null;
            closeROI();
        } else {
            appendDynamicToFixed();
            dijkstraMain(x, y, true);
        }
        intelligentScissorsInterface.updated();
    }

    public void setImageSize(int M, int N) {
        this.imageWidth = M;
        this.imageHeight = N;
        fixedROI = null;
        dynamicROI = null;
    }

    private void costMatrix(int row_index, int col_index) {
        Rectangle r = new Rectangle(row_index - COST_DIM / 2 - PADDING, col_index - COST_DIM / 2 - PADDING, PADDED_DIM, PADDED_DIM);
        BufferedImage img = intelligentScissorsInterface.getImage(r);
        if (img == null){
            return;
        }
        img = rgb2gray(img);
        imgFloat = FormatDescriptor.create(img, DataBuffer.TYPE_FLOAT, null).createInstance();

        PlanarImage dx = JAI.create("Convolve", imgFloat, horz).createInstance();
        PlanarImage dy = JAI.create("Convolve", imgFloat, vert).createInstance();
        PlanarImage grad = JAI.create("GradientMagnitude", imgFloat, horz, vert);

        float[] tempGrad = new float[PADDED_DIM * PADDED_DIM];
        float[] tempDyData = new float[PADDED_DIM * PADDED_DIM];
        float[] tempDxData = new float[PADDED_DIM * PADDED_DIM];
        grad.getData().getSamples(0, 0, PADDED_DIM, PADDED_DIM, 0, tempGrad);
        dx.getData().getSamples(0, 0, PADDED_DIM, PADDED_DIM, 0, tempDyData);
        dy.getData().getSamples(0, 0, PADDED_DIM, PADDED_DIM, 0, tempDxData);

        final float gradData[][] = new float[PADDED_DIM][PADDED_DIM];
        final float dxData[][] = new float[PADDED_DIM][PADDED_DIM];
        final float dyData[][] = new float[PADDED_DIM][PADDED_DIM];

        int idx = 0;
        for (int i = 0; i < gradData.length; i++) {
            for (int j = 0; j < gradData[0].length; j++) {
                gradData[j][i] = tempGrad[idx];
                dxData[j][i] = tempDxData[idx];
                dyData[j][i] = tempDyData[idx];
                idx++;
            }
        }

        float minG = Float.MAX_VALUE, maxG = Float.MIN_VALUE;
        for (int i = 2; i < PADDED_DIM - 2; i++) {
            for (int j = 2; j < PADDED_DIM - 2; j++) {
                float s = gradData[i][j];
                if (s < minG) {
                    minG = s;
                }
                if (s > maxG) {
                    maxG = s;
                }
            }
        }

        for (int i = 0; i < gradData.length; i++) {
            for (int j = 0; j < gradData[0].length; j++) {
                if (gradData[i][j] < 0.00001) {
                    dxData[i][j] = 0;
                    dyData[i][j] = 0;
                } else {
                    dxData[i][j] /= gradData[i][j];
                    dyData[i][j] /= gradData[i][j];
                }
            }
        }
        try {
            ExecutorService threadPool = Executors.newFixedThreadPool(2);
            Future<byte[][]> zCross5Result = threadPool.submit(zeroCross5x5Runnable);
            Future<byte[][]> zCross9Result = threadPool.submit(zeroCross9x9Runnable);
            threadPool.shutdown();
            
            byte g5Cross[][] = zCross5Result.get(2, TimeUnit.SECONDS);
            byte g9Cross[][] = zCross9Result.get(2, TimeUnit.SECONDS);
            
            threadPool = Executors.newFixedThreadPool(8);
            final int cOff[] = {-1, 0, 1, 1, 1, 0, -1, -1};
            final int rOff[] = {-1, -1, -1, 0, 1, 1, 1, 0};
            for (int K = 0; K < 8; K++) {
                threadPool.submit(new CostMaxGen(K, cOff[K], rOff[K], minG, maxG, approximateCosInv, gradData, dxData, dyData, g5Cross, g9Cross));
            }
            threadPool.shutdown();
            threadPool.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            System.out.println("Failed to generate cost matrix");
            Logger.getLogger(IntelligentScissors.class.getName()).log(Level.WARNING, null, ex);
        } catch (ExecutionException | TimeoutException ex) {
            Logger.getLogger(IntelligentScissors.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private double acosApprox(double x) {
        return (-0.69813170079773212 * x * x - 0.87266462599716477) * x + 1.5707963267948966;
    }

    private Point[] dijkstraMain(int row_index, int col_index, boolean firstRun) {

        Point path_array[];
        if (firstRun) {
            start_node = new Point(row_index, col_index);
            path = new Path(imageWidth, imageHeight);
            dist = new Dist(imageWidth, imageHeight, row_index, col_index);
        }

        updateDist(row_index, col_index, firstRun);

        if (!firstRun) {
            if (((yLast == 0) && (xLast == 0))) {
                return null;
            } else {
                path_array = forcePath(row_index, col_index, firstRun);
                if (path_array == null) {//fail
                    return forcePath(row_index, col_index, firstRun);
                }
                yLast = col_index;
                xLast = row_index;
                return path_array;
            }
        }
        yLast = col_index;
        xLast = row_index;
        return null;
    }

    private Point[] forcePath(final int xCurrent, final int yCurrent, final boolean firstRun) {
        Point freePoint = new Point(xCurrent, yCurrent);
        Point[] path_array = new Point[5000];
        path_array[0] = freePoint;
        int path_array_index = 1;
        Point new_node = (Point) freePoint.clone();
        int attempt = 0;
        while (!new_node.equals(start_node)) {
            if ((path_array_index >= (50000))) {
                return null;
            }

            //If a path was not found, incrementally update distance matrix with points between start and finish node
            if (new_node.equals(new Point(0, 0))) {
                if (attempt == 1) {
                    return null;
                }
                int numelNewNodes = (int) Math.ceil(2 * new Point(xLast, yLast).distance(new Point(xCurrent, yCurrent)) / (Math.sqrt(2) * COST_DIM)) - 1;
                if (numelNewNodes == 0) {
                    return null;
                }
                for (int i = 0; i < numelNewNodes + 1; i++) {
                    int x = (int) (xLast + (double) (xCurrent - xLast) * (i / (double) numelNewNodes));
                    int y = (int) (yLast + (double) (yCurrent - yLast) * (i / (double) numelNewNodes));
                    updateDist(x, y, firstRun);
                }
                path_array_index = 1;
                new_node = freePoint;
                attempt++;
            }

            if (path_array_index >= path_array.length - 1) {
                Point temp[] = new Point[path_array.length * 2];
                System.arraycopy(path_array, 0, temp, 0, path_array.length);
                path_array = temp;
                path_array[0] = freePoint;
            }

            if (path_array_index > 1024) {
                Point temp = path.get(new_node);
                for (int i = 1; i < 5; i++) {
                    if (path_array[path_array_index - i].equals(temp)) {
                        path.inc(new_node);
                        System.out.println("inc");
                    }
                }

            }
            new_node = path.get(new_node);
            path_array[path_array_index++] = new_node;
        }

        //Create final output with only significant elements
        Point[] temp = new Point[path_array_index];
        System.arraycopy(path_array, 0, temp, 0, path_array_index);
        //This does need to be here
        return temp;
    }

    private void updateDist(final int row_index, final int col_index, final boolean firstRun) {
        final int bound = COST_DIM / 2;
        costMatrix(row_index, col_index);
        if (firstRun == true) {
            for (int W = 0; W < bound; W++) {
                clockWise(W, col_index, row_index);
            }
        }
        for (int W = bound - 1; W >= 0; W--) {
            clockWise(W, col_index, row_index);

        }
        for (int W = 0; W < bound; W++) {
            clockWise(W, col_index, row_index);

        }
        if (firstRun == false) {
            for (int W = bound - 1; W >= 0; W--) {
                clockWise(W, col_index, row_index);
            }
        }
        makePath(row_index, col_index);
    }

    private void clockWise(int W, int col_index, int row_index) {
        int I, J;
        J = col_index + W;
        for (I = row_index - W; I < row_index + W; I++) { //Right Side, Down
            makeNeigh(I, J, row_index, col_index);
        }
        for (J = col_index + W; J > col_index - W; J--) { //Bottom Side, To Left
            makeNeigh(I, J, row_index, col_index);
        }
        for (I = row_index + W; I > row_index - W; I--) { //Left Side, Up
            makeNeigh(I, J, row_index, col_index);
        }
        for (J = col_index - W; J < col_index + W; J++) { //Top, To Right
            makeNeigh(I, J, row_index, col_index);
        }
    }

    private void makeNeigh(final int i, final int j, final int row_index, final int col_index) {
        if ((i < PADDING) || (j < PADDING) || (i >= (imageWidth - PADDING)) || (j >= (imageHeight - PADDING))) {
            return;
        }
        final int rOff[] = {i - 1, i, i + 1, i + 1, i + 1, i, i - 1, i - 1};
        final int cOff[] = {j - 1, j - 1, j - 1, j, j + 1, j + 1, j + 1, j};
        for (int K = 0; K < 8; K++) {
            float temp = costM[i - row_index + COST_DIM / 2][j - col_index + COST_DIM / 2][K] + dist.get(i, j);
            if (temp < dist.get(rOff[K], cOff[K])) {
                dist.set(rOff[K], +cOff[K], temp);
            }
        }
    }

    private void makePath(int row_index, int col_index) {
        int j, i, K, Kl;
        float min, temp;
        final int bounds = COST_DIM / 2 + PADDING;
        for (j = col_index - bounds; j < col_index + bounds; j++) {
            for (i = row_index - bounds; i < row_index + bounds; i++) {
                if ((i <= 0) || (j <= 0) || (i >= (imageWidth - 1)) || (j >= (imageHeight - 1))) {
                    continue;
                }
                final int rOff[] = {i - 1, i, i + 1, i + 1, i + 1, i, i - 1, i - 1};
                final int cOff[] = {j - 1, j - 1, j - 1, j, j + 1, j + 1, j + 1, j};

                min = Float.MAX_VALUE - 1;
                Kl = -1;
                for (K = 0; K < 8; K++) {

                    temp = dist.get(rOff[K], cOff[K]);
                    if (temp < min) {
                        min = temp;
                        Kl = K;
                    }
                }
                if (Kl >= 0) {
                    path.set(new Point(i, j), new Point(rOff[Kl], cOff[Kl]));
                }
            }
        }
    }

    private BufferedImage rgb2gray(BufferedImage in) {
        BufferedImage out = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = out.getGraphics();
        g.drawImage(in, 0, 0, null);
        g.dispose();
        return out;
    }

    private void closeROI() {
        intelligentScissorsInterface.roiFinished(fixedROI);
        fixedROI = null;
        dynamicROI = null;
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if (demo) {
            if ((fixedROI != null) && (dynamicROI != null)) {
                g.drawRect(lastPoint.x - path.tileSize / 2, lastPoint.y - path.tileSize / 2, path.tileSize, path.tileSize);
            }
        }
        if (fixedROI != null) {
            paintROI(fixedROI, g2d, Color.GREEN);
        }
        if (dynamicROI != null) {
            paintROI(dynamicROI, g2d, Color.GREEN);
        }

        if (demo) {
            if ((fixedROI != null) && (dynamicROI != null)) {
                if (path != null) {
                    for (int x = 0; x < (int) Math.ceil((float) path.m / path.tileSize); x++) {
                        for (int y = 0; y < (int) Math.ceil((float) path.n / path.tileSize); y++) {
                            if (path.isactive(new Point(x, y))) {
                                g.drawRect((int) (x * path.getTileSize()), (int) (y * path.getTileSize()), (int) (path.tileSize), (int) (path.tileSize));
                            }
                        }
                    }
                }
            }
        }
    }

    public void paintROI(Path2D.Double path, Graphics2D g, Color color) {
        g = (Graphics2D) g.create();
        if (path == null) {
            return;
        }

        float zFactor = (float) (1 / g.getTransform().getScaleX());
        g.setColor(color);
        g.setStroke(new BasicStroke(5 * zFactor));
        g.draw(path);

        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(1 * zFactor));
        g.draw(path);

        g.setStroke(new BasicStroke(3 * zFactor));

        // Fill the new roi
        g.setColor(Color.green);
        Color c = g.getColor();
        g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 30));
        g.fill(path);

        g.setColor(Color.red);

    }

    public void addPoint(Path2D.Double path, double x, double y) {
        if (path == null) {
            return;
        }
        if (path.getCurrentPoint() == null) {
            path.moveTo(x, y);
        } else {
            path.lineTo(x, y);
        }
    }

}
