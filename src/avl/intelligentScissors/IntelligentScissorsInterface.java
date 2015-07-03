package avl.intelligentScissors;

import java.awt.Rectangle;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;

public interface IntelligentScissorsInterface {
    public BufferedImage getImage(Rectangle window);
    public void roiFinished(Path2D.Double path);
    public void updated();
}
