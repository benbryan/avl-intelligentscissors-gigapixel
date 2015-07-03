package avl.demo;

import avl.intelligentScissors.IntelligentScissors;
import avl.intelligentScissors.IntelligentScissorsInterface;
import avl.intelligentScissors.IntelligentScissorsPrompt;
import avl.intelligentScissors.IntelligentScissorsWeights;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SpringLayout;

public class Demo extends javax.swing.JFrame {
    
    public Demo() {
        initComponents();
        BufferedImage image = getImage();
        final ArrayList<Path2D.Double> paths = new ArrayList<>(); 
        IntelligentScissors intelligentScissors = new IntelligentScissors(new IntelligentScissorsInterface() {
            @Override
            public BufferedImage getImage(Rectangle r) {
                if ((r.x<0) || (r.y<0) || ((r.x+r.width)>=image.getWidth()) || ((r.y+r.height)>=image.getHeight())){
                    BufferedImage temp = new BufferedImage(r.width, r.height, BufferedImage.TYPE_3BYTE_BGR);
                    temp.getGraphics().drawImage(image, -r.x, -r.y, null);
                    return temp;
                } else {
                    return image.getSubimage(r.x, r.y, r.width, r.height);
                }
            }
            @Override
            public void roiFinished(Path2D.Double path) {
                paths.add(path);
            }

            @Override
            public void updated() {
                repaint();
            }
        });

        intelligentScissors.setApproximateCosInv(true);
        intelligentScissors.setWeights(new IntelligentScissorsWeights());
        intelligentScissors.setImageSize(image.getWidth(),image.getHeight());
                      
        JPanel imagePanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.drawImage(image,0, 0, null);
                drawCreatedPaths(g2d);
                intelligentScissors.paint(g2d);
            }
            private void drawCreatedPaths(Graphics2D g2d){
                g2d = (Graphics2D) g2d.create();
                for (Path2D.Double path:paths){
                    g2d.setColor(Color.red);
                    g2d.setStroke(new BasicStroke(5));
                    g2d.draw(path);
                    g2d.setColor(Color.white);
                    g2d.setStroke(new BasicStroke(1));
                    g2d.draw(path);
                } 
            }
        };
        
        imagePanel.setVisible(true);        
        imagePanel.addMouseListener(intelligentScissors);
        imagePanel.addMouseMotionListener(intelligentScissors);

        JToggleButton demoButton = new JToggleButton(new AbstractAction("Demo") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JToggleButton button = (JToggleButton) e.getSource();
                boolean demoEnable = button.isSelected();
                intelligentScissors.setDemo(demoEnable);
            }
        });
        
        JButton optionsButton = new JButton(new AbstractAction("Options") {
            @Override
            public void actionPerformed(ActionEvent e) {
                IntelligentScissorsPrompt prompt = new IntelligentScissorsPrompt(null, Dialog.ModalityType.APPLICATION_MODAL);
                prompt.setWeights(intelligentScissors.getWeights());
                prompt.setApproximateCosInv(intelligentScissors.getApproximateCosInv());
                prompt.setVisible(true);
                intelligentScissors.setWeights(prompt.getWeights());
                intelligentScissors.setApproximateCosInv(prompt.isApproximateCosInv());
            }
        });
        
        add(imagePanel);
        add(demoButton);
        add(optionsButton);
        
//        imagePanel.setSize(new Dimension(image.getWidth(), image.getHeight()));
        imagePanel.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
//        demoButton.setSize(100, 20);
        Container contentPane = getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);        
        
        layout.putConstraint(SpringLayout.WEST, imagePanel, 5, SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.WEST, demoButton, 5, SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.WEST, optionsButton, 5, SpringLayout.WEST, contentPane);

        layout.putConstraint(SpringLayout.NORTH, imagePanel, 5, SpringLayout.NORTH, contentPane);
        layout.putConstraint( SpringLayout.NORTH, demoButton, 5, SpringLayout.SOUTH, imagePanel);      
        layout.putConstraint( SpringLayout.NORTH, optionsButton, 5, SpringLayout.SOUTH, imagePanel);      
        layout.putConstraint(SpringLayout.WEST, optionsButton, 5, SpringLayout.EAST,  demoButton);
        
        layout.putConstraint(SpringLayout.SOUTH, contentPane, 5, SpringLayout.SOUTH, demoButton);
        layout.putConstraint(SpringLayout.SOUTH, contentPane, 5, SpringLayout.SOUTH, optionsButton);
        
        layout.putConstraint( SpringLayout.EAST, contentPane, 5, SpringLayout.EAST, imagePanel);      

        pack();
        
//        imagePanel.setSize(new Dimension(image.getWidth() + getMinimumSize().width, image.getHeight() + getMinimumSize().height));
//        demoButton.setSize(100, 20);
        
//        pack();
    }
    
    private BufferedImage getImage(){
        try {
            InputStream stream = getClass().getResourceAsStream("AVL.jpg");
            if (stream == null){
                System.err.println("Could not find target image");
                System.exit(-1);
            }
            BufferedImage image = ImageIO.read(stream);
            if (image == null){
                System.err.println("Could not read target image");
                System.exit(-2);
            }
            return image;
        } catch (IOException ex) {
            System.err.println("Failed to load the demo image");
            Logger.getLogger(Demo.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 655, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 486, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Demo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Demo().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
