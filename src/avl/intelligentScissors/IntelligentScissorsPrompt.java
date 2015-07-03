package avl.intelligentScissors;

import avl.intelligentScissors.IntelligentScissorsWeights;

public class IntelligentScissorsPrompt extends javax.swing.JDialog {

    private IntelligentScissorsWeights weights = new IntelligentScissorsWeights();

    public IntelligentScissorsWeights getWeights() {
        return weights;
    }
    
    public void setWeights(IntelligentScissorsWeights weights) {
        this.weights = weights;
        updateDisplay();
    }
    
    public IntelligentScissorsPrompt(java.awt.Window parent, ModalityType modalityType) {
        super(parent, modalityType);
        initComponents();
        updateDisplay();
    }

    public boolean isApproximateCosInv(){
        return jCheckBoxApproximateCosInv.isSelected();
    }
    
    public void setApproximateCosInv(boolean approximateCosInv) {
        jCheckBoxApproximateCosInv.setSelected(approximateCosInv);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupLock = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jSliderWd = new javax.swing.JSlider();
        jSliderWg = new javax.swing.JSlider();
        jSliderWz = new javax.swing.JSlider();
        jTextFieldWz = new javax.swing.JTextField();
        jTextFieldWg = new javax.swing.JTextField();
        jTextFieldWd = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jCheckBoxApproximateCosInv = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Intelligent Scissors Options");
        setResizable(false);

        jSliderWd.setOrientation(javax.swing.JSlider.VERTICAL);
        jSliderWd.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSliderWdStateChanged(evt);
            }
        });

        jSliderWg.setOrientation(javax.swing.JSlider.VERTICAL);
        jSliderWg.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSliderWgStateChanged(evt);
            }
        });

        jSliderWz.setOrientation(javax.swing.JSlider.VERTICAL);
        jSliderWz.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSliderWzStateChanged(evt);
            }
        });

        jTextFieldWz.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldWzFocusLost(evt);
            }
        });

        jTextFieldWg.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldWgFocusLost(evt);
            }
        });

        jTextFieldWd.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldWdFocusLost(evt);
            }
        });

        jLabel3.setText("w_d");

        jLabel2.setText("w_g");

        jLabel1.setText("w_z");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextFieldWz, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSliderWz, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextFieldWg, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSliderWg, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextFieldWd, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSliderWd, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldWd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSliderWd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldWg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSliderWg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldWz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSliderWz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jCheckBoxApproximateCosInv.setSelected(true);
        jCheckBoxApproximateCosInv.setText("Approximate inverse cosine");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(4, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jCheckBoxApproximateCosInv)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jCheckBoxApproximateCosInv)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldWzFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldWzFocusLost
        float temp = Float.parseFloat(jTextFieldWd.getText());
        weights.z = clamp(temp);
        updateDisplay();
    }//GEN-LAST:event_jTextFieldWzFocusLost

    private void jSliderWzStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSliderWzStateChanged
        float temp = (float)jSliderWz.getValue()/100;
        weights.z = clamp(temp);
        updateDisplay();
    }//GEN-LAST:event_jSliderWzStateChanged

    private void jSliderWgStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSliderWgStateChanged
        float temp = (float)jSliderWg.getValue()/100;
        weights.g = clamp(temp);
        updateDisplay();    }//GEN-LAST:event_jSliderWgStateChanged

    private void jSliderWdStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSliderWdStateChanged
        float temp = (float)jSliderWd.getValue()/100;
        weights.d = clamp(temp);
        updateDisplay();
    }//GEN-LAST:event_jSliderWdStateChanged

    private void jTextFieldWgFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldWgFocusLost
        float temp = Float.parseFloat(jTextFieldWg.getText());
        weights.g = clamp(temp);
        updateDisplay();
    }//GEN-LAST:event_jTextFieldWgFocusLost

    private void jTextFieldWdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldWdFocusLost
        float temp = Float.parseFloat(jTextFieldWd.getText());
        weights.d = clamp(temp);
        updateDisplay();
    }//GEN-LAST:event_jTextFieldWdFocusLost

    private float clamp(float in){
        in = Math.max(in,0);
        return Math.min(in, 1);
    }
        
    private void updateDisplay(){
        jTextFieldWz.setText(String.format("%.2f", weights.z));
        jTextFieldWd.setText(String.format("%.2f", weights.d));
        jTextFieldWg.setText(String.format("%.2f", weights.g));
        jSliderWz.setValue((int) (weights.z*100));
        jSliderWd.setValue((int) (weights.d*100));
        jSliderWg.setValue((int) (weights.g*100));
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupLock;
    private javax.swing.JCheckBox jCheckBoxApproximateCosInv;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSlider jSliderWd;
    private javax.swing.JSlider jSliderWg;
    private javax.swing.JSlider jSliderWz;
    private javax.swing.JTextField jTextFieldWd;
    private javax.swing.JTextField jTextFieldWg;
    private javax.swing.JTextField jTextFieldWz;
    // End of variables declaration//GEN-END:variables


}
