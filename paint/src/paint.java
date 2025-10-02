import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JColorChooser;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class paint extends javax.swing.JFrame {

    Color brushColor = Color.BLACK;
    int old_mouse_x = 0;
    int old_mouse_y = 0;
    boolean draw_figure = false;
    int first_mouse_x = 0;
    int first_mouse_y = 0;
    BufferedImage canvas;
    Graphics2D g2;
    
    ArrayList<Point> polygonPoints = new ArrayList<>();
    boolean drawingPolygon = false;
    Point currentMousePos = null;
        

    public class CanvasPanel extends javax.swing.JPanel {
        private BufferedImage canvas;

        public void setCanvas(BufferedImage canvas) {
            this.canvas = canvas;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (canvas != null) {
                g.drawImage(canvas, 0, 0, null);
            }
        }
    }

    public paint() {
        initComponents();


        canvas = new BufferedImage(jPanel2.getWidth(), jPanel2.getHeight(), BufferedImage.TYPE_INT_ARGB);
        g2 = canvas.createGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        g2.setColor(brushColor);
        ((CanvasPanel) jPanel2).setCanvas(canvas);

        ((CanvasPanel) jPanel2).setCanvas(canvas);

        jPanel2.addComponentListener(new java.awt.event.ComponentAdapter() {
        @Override
        public void componentResized(java.awt.event.ComponentEvent e) {
            int newWidth = jPanel2.getWidth();
            int newHeight = jPanel2.getHeight();


            if (newWidth > canvas.getWidth() || newHeight > canvas.getHeight()) {
                BufferedImage newCanvas = new BufferedImage(
                    Math.max(newWidth, canvas.getWidth()),
                    Math.max(newHeight, canvas.getHeight()),
                    BufferedImage.TYPE_INT_ARGB
            );

            Graphics2D g = newCanvas.createGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, newCanvas.getWidth(), newCanvas.getHeight());
            g.drawImage(canvas, 0, 0, null); 
            g.dispose();

            canvas = newCanvas;
            g2 = canvas.createGraphics();
            g2.setColor(brushColor);
            ((CanvasPanel) jPanel2).setCanvas(canvas);
        }

        jPanel2.repaint();
        }
        });
    }
    
    private void saveCanvasToFile() {
    if (canvas == null || canvas.getWidth() <= 0 || canvas.getHeight() <= 0) {
        JOptionPane.showMessageDialog(this, "Brak obrazu do zapisania.", "Błąd", JOptionPane.ERROR_MESSAGE);
        return;
    }

    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Zapisz obraz");

    javax.swing.filechooser.FileNameExtensionFilter pngFilter = new javax.swing.filechooser.FileNameExtensionFilter("PNG Images", "png");
    javax.swing.filechooser.FileNameExtensionFilter jpgFilter = new javax.swing.filechooser.FileNameExtensionFilter("JPEG Images", "jpg", "jpeg");
    fileChooser.addChoosableFileFilter(pngFilter);
    fileChooser.addChoosableFileFilter(jpgFilter);
    fileChooser.setFileFilter(pngFilter);

    int result = fileChooser.showSaveDialog(this);
    if (result == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();
        String selectedExt = "png";

        javax.swing.filechooser.FileFilter chosen = fileChooser.getFileFilter();
        if (chosen == jpgFilter) selectedExt = "jpg";
        else if (chosen == pngFilter) selectedExt = "png";

        String fileName = file.getName().toLowerCase();
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            selectedExt = "jpg";
        } else if (fileName.endsWith(".png")) {
            selectedExt = "png";
        } else {
            file = new File(file.getAbsolutePath() + "." + selectedExt);
        }

        if (file.exists()) {
            int ok = JOptionPane.showConfirmDialog(this,
                    "Plik istnieje. Nadpisać?", "Potwierdź nadpisanie",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (ok != JOptionPane.YES_OPTION) return;
        }

        try {
            BufferedImage imageToSave = canvas;
            if ("jpg".equalsIgnoreCase(selectedExt)) {
                BufferedImage rgb = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D gg = rgb.createGraphics();
                gg.setColor(Color.WHITE);
                gg.fillRect(0, 0, rgb.getWidth(), rgb.getHeight());
                gg.drawImage(canvas, 0, 0, null);
                gg.dispose();
                imageToSave = rgb;
            }

            boolean ok = ImageIO.write(imageToSave, selectedExt, file);
            if (!ok) {
                throw new IOException("Nieobsługiwany format zapisu: " + selectedExt);
            }
            JOptionPane.showMessageDialog(this, "Obraz zapisany jako " + file.getAbsolutePath(),"Zapisano", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Błąd przy zapisie pliku!\n" + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }
}
    
    private void loadCanvasFromFile() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Wczytaj obraz");

    javax.swing.filechooser.FileNameExtensionFilter pngFilter = new javax.swing.filechooser.FileNameExtensionFilter("PNG Images", "png");
    javax.swing.filechooser.FileNameExtensionFilter jpgFilter = new javax.swing.filechooser.FileNameExtensionFilter("JPEG Images", "jpg", "jpeg");
    fileChooser.addChoosableFileFilter(pngFilter);
    fileChooser.addChoosableFileFilter(jpgFilter);
    fileChooser.setFileFilter(pngFilter);

    int result = fileChooser.showOpenDialog(this);
    if (result == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();
        try {
            BufferedImage loadedImage = ImageIO.read(file);
            if (loadedImage == null) {
                JOptionPane.showMessageDialog(this, "Nie można wczytać pliku jako obraz!", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int w = Math.max(jPanel2.getWidth(), loadedImage.getWidth());
            int h = Math.max(jPanel2.getHeight(), loadedImage.getHeight());
            canvas = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            g2 = canvas.createGraphics();

            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
            g2.drawImage(loadedImage, 0, 0, null);
            g2.setColor(brushColor);

            ((CanvasPanel) jPanel2).setCanvas(canvas);
            jPanel2.repaint();

        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Błąd przy wczytywaniu pliku!", "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }
}

    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel2 = jPanel2 = new CanvasPanel(); ;

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Paint");
        setBackground(new java.awt.Color(102, 102, 102));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMinimumSize(new java.awt.Dimension(800, 600));

        jPanel1.setBackground(new java.awt.Color(71, 71, 71));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setPreferredSize(new java.awt.Dimension(1007, 84));

        jComboBox1.setBackground(new java.awt.Color(45, 45, 45));
        jComboBox1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jComboBox1.setForeground(new java.awt.Color(255, 255, 255));
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ołówek", "Prostokąt", "Linia", "Okrąg", "Wielokąt" }));
        jComboBox1.setToolTipText("");
        jComboBox1.setBorder(null);
        jComboBox1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jComboBox1.setFocusable(false);
        jComboBox1.setPreferredSize(new java.awt.Dimension(110, 25));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        jPanel1.add(jComboBox1);

        jComboBox2.setBackground(new java.awt.Color(45, 45, 45));
        jComboBox2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jComboBox2.setForeground(new java.awt.Color(255, 255, 255));
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1px", "2px", "3px", "4px" }));
        jComboBox2.setBorder(null);
        jComboBox2.setFocusable(false);
        jComboBox2.setPreferredSize(new java.awt.Dimension(110, 25));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });
        jPanel1.add(jComboBox2);

        jButton2.setBackground(new java.awt.Color(45, 45, 45));
        jButton2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Kolor");
        jButton2.setBorder(null);
        jButton2.setFocusable(false);
        jButton2.setPreferredSize(new java.awt.Dimension(110, 25));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2);

        jTextField1.setEditable(false);
        jTextField1.setBackground(new java.awt.Color(0, 0, 0));
        jTextField1.setBorder(null);
        jTextField1.setFocusable(false);
        jTextField1.setPreferredSize(new java.awt.Dimension(50, 25));
        jPanel1.add(jTextField1);

        jButton1.setBackground(new java.awt.Color(45, 45, 45));
        jButton1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Wyczyść");
        jButton1.setBorder(null);
        jButton1.setFocusable(false);
        jButton1.setPreferredSize(new java.awt.Dimension(120, 25));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);

        jButton4.setBackground(new java.awt.Color(45, 45, 45));
        jButton4.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Zapisz");
        jButton4.setBorder(null);
        jButton4.setFocusable(false);
        jButton4.setPreferredSize(new java.awt.Dimension(110, 25));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton4);

        jButton3.setBackground(new java.awt.Color(45, 45, 45));
        jButton3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Wczytaj");
        jButton3.setBorder(null);
        jButton3.setFocusable(false);
        jButton3.setPreferredSize(new java.awt.Dimension(110, 25));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setFocusable(false);
        jPanel2.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jPanel2MouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jPanel2MouseMoved(evt);
            }
        });
        jPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel2MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel2MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jPanel2MouseReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 556, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        draw_figure = false;
        g2.setColor(brushColor); 
        g2.setStroke(new BasicStroke(jComboBox2.getSelectedIndex() + 1));
        jPanel2.repaint();
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        //nigga
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        brushColor = JColorChooser.showDialog(null, "Wybierz kolor", brushColor);
        jTextField1.setBackground(brushColor);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jPanel2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MousePressed
       if (jComboBox1.getSelectedIndex() > 0 && !draw_figure) {
            draw_figure = true;
            first_mouse_x = evt.getX();
            first_mouse_y = evt.getY();
        }
        old_mouse_x = evt.getX();
        old_mouse_y = evt.getY();
    }//GEN-LAST:event_jPanel2MousePressed

    private void jPanel2MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseDragged
          int tool = jComboBox1.getSelectedIndex();

    if (tool == 0) { 
        g2.setColor(brushColor);
        g2.setStroke(new BasicStroke(jComboBox2.getSelectedIndex() + 1));
        g2.drawLine(old_mouse_x, old_mouse_y, evt.getX(), evt.getY());
        old_mouse_x = evt.getX();
        old_mouse_y = evt.getY();
        jPanel2.repaint();
    } 
    else if (tool >= 1 && tool <= 3 && draw_figure) {
        BufferedImage tempCanvas = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D gTemp = tempCanvas.createGraphics();
        gTemp.drawImage(canvas, 0, 0, null);

        int a = Math.min(first_mouse_x, evt.getX());
        int b = Math.min(first_mouse_y, evt.getY());
        int width = Math.abs(evt.getX() - first_mouse_x);
        int height = Math.abs(evt.getY() - first_mouse_y);

        gTemp.setColor(brushColor);
        gTemp.setStroke(new BasicStroke(jComboBox2.getSelectedIndex() + 1));

        if (tool == 1) {
            gTemp.drawRect(a, b, width, height);
        } else if (tool == 2) {
            gTemp.drawLine(first_mouse_x, first_mouse_y, evt.getX(), evt.getY());
        } else if (tool == 3) {
            gTemp.drawOval(a, b, width, height);
        }

        gTemp.dispose();
        ((CanvasPanel) jPanel2).setCanvas(tempCanvas);
        jPanel2.repaint();
    }
    else if (tool == 4 && drawingPolygon && !polygonPoints.isEmpty()) {
        BufferedImage tempCanvas = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D gTemp = tempCanvas.createGraphics();
        gTemp.drawImage(canvas, 0, 0, null);

        gTemp.setColor(brushColor);
        gTemp.setStroke(new BasicStroke(jComboBox2.getSelectedIndex() + 1));

        for (int i = 0; i < polygonPoints.size() - 1; i++) {
            Point p1 = polygonPoints.get(i);
            Point p2 = polygonPoints.get(i + 1);
            gTemp.drawLine(p1.x, p1.y, p2.x, p2.y);
        }

        Point last = polygonPoints.get(polygonPoints.size() - 1);
        gTemp.drawLine(last.x, last.y, evt.getX(), evt.getY());

        gTemp.dispose();
        ((CanvasPanel) jPanel2).setCanvas(tempCanvas);
        jPanel2.repaint();
    }
    }//GEN-LAST:event_jPanel2MouseDragged

    private void jPanel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseClicked
        int tool = jComboBox1.getSelectedIndex();

        if (tool == 4) {
            Point clicked = new Point(evt.getX(), evt.getY());

            if (!drawingPolygon) {
                drawingPolygon = true;
                polygonPoints.clear();
                polygonPoints.add(clicked);
            } else {
                Point first = polygonPoints.get(0);
                if (clicked.distance(first) < 10 && polygonPoints.size() > 2) {
                    g2.setColor(brushColor);
                    g2.setStroke(new BasicStroke(jComboBox2.getSelectedIndex() + 1));
                    int[] xPoints = polygonPoints.stream().mapToInt(p -> p.x).toArray();
                    int[] yPoints = polygonPoints.stream().mapToInt(p -> p.y).toArray();
                    g2.drawPolygon(xPoints, yPoints, polygonPoints.size());
                    polygonPoints.clear();
                    drawingPolygon = false;
                    ((CanvasPanel) jPanel2).setCanvas(canvas);
                    jPanel2.repaint();
                    return;
                } else {
                    polygonPoints.add(clicked);
                }
            }
        }
    }//GEN-LAST:event_jPanel2MouseClicked

    private void jPanel2MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseMoved
    int tool = jComboBox1.getSelectedIndex();

    if (tool == 4 && drawingPolygon && !polygonPoints.isEmpty()) {
        currentMousePos = evt.getPoint();

        BufferedImage tempCanvas = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D gTemp = tempCanvas.createGraphics();
        gTemp.drawImage(canvas, 0, 0, null);

        gTemp.setColor(brushColor);
        gTemp.setStroke(new BasicStroke(jComboBox2.getSelectedIndex() + 1));

        for (int i = 0; i < polygonPoints.size() - 1; i++) {
            Point p1 = polygonPoints.get(i);
            Point p2 = polygonPoints.get(i + 1);
            gTemp.drawLine(p1.x, p1.y, p2.x, p2.y);
        }

        Point last = polygonPoints.get(polygonPoints.size() - 1);
        gTemp.drawLine(last.x, last.y, currentMousePos.x, currentMousePos.y);

        gTemp.dispose();
        ((CanvasPanel) jPanel2).setCanvas(tempCanvas);
        jPanel2.repaint();
    }
    }//GEN-LAST:event_jPanel2MouseMoved

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        g2.setColor(brushColor); 
        jPanel2.repaint();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jPanel2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseReleased
        if (draw_figure && jComboBox1.getSelectedIndex() > 0) {
        int a = Math.min(first_mouse_x, evt.getX());
        int b = Math.min(first_mouse_y, evt.getY());
        int width = Math.abs(evt.getX() - first_mouse_x);
        int height = Math.abs(evt.getY() - first_mouse_y);

        g2.setColor(brushColor);
        g2.setStroke(new BasicStroke(jComboBox2.getSelectedIndex() + 1));

        if (jComboBox1.getSelectedIndex() == 1) {
            g2.drawRect(a, b, width, height);
        } else if (jComboBox1.getSelectedIndex() == 2) {
            g2.drawLine(first_mouse_x, first_mouse_y, evt.getX(), evt.getY());
        } else if (jComboBox1.getSelectedIndex() == 3) {
            g2.drawOval(a, b, width, height);
        }
        draw_figure = false;
        ((CanvasPanel) jPanel2).setCanvas(canvas);
        jPanel2.repaint();
    }
    }//GEN-LAST:event_jPanel2MouseReleased

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        saveCanvasToFile();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
       loadCanvasFromFile();
    }//GEN-LAST:event_jButton3ActionPerformed
  
      
    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new paint().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}

