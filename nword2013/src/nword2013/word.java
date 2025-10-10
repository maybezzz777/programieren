package nword2013;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;


public class word extends javax.swing.JFrame {

 
    public word() {
        initComponents();
        ustawMenuPlik();
    }
    
    private void ustawMenuPlik() {
    JMenuItem menuOtworz = new JMenuItem("Otwórz");
    menuOtworz.addActionListener(evt -> {
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                jEditorPane1.read(br, null);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Błąd odczytu pliku", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    });
    jMenu1.add(menuOtworz);

    JMenuItem menuZapisz = new JMenuItem("Zapisz");
    menuZapisz.addActionListener(evt -> {
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                jEditorPane1.write(bw);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Błąd zapisu pliku", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    });
    jMenu1.add(menuZapisz);

    JMenuItem menuZakoncz = new JMenuItem("Zakończ");
    menuZakoncz.addActionListener(evt -> System.exit(0));
    jMenu1.add(menuZakoncz);
}
    
    class StyledViewFactory implements javax.swing.text.ViewFactory {
    @Override
    public javax.swing.text.View create(javax.swing.text.Element elem) {
        String kind = elem.getName();
        if (kind != null) {
            if (kind.equals(javax.swing.text.AbstractDocument.ContentElementName)) {
                return new javax.swing.text.WrappedPlainView(elem);
            } else if (kind.equals(javax.swing.text.AbstractDocument.ParagraphElementName)) {
                return new javax.swing.text.ParagraphView(elem);
            }
        }
        return new javax.swing.text.LabelView(elem);
    }
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jTextField2 = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(600, 600));
        setPreferredSize(new java.awt.Dimension(1000, 600));

        jPanel1.setBackground(new java.awt.Color(54, 52, 52));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));

        jComboBox1.setBackground(new java.awt.Color(51, 51, 51));
        jComboBox1.setForeground(new java.awt.Color(255, 255, 255));
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Arial", "Comic sans MS", "Century", "Consolas" }));
        jComboBox1.setFocusable(false);
        jComboBox1.setPreferredSize(new java.awt.Dimension(100, 30));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        jPanel1.add(jComboBox1);

        jComboBox2.setBackground(new java.awt.Color(51, 51, 51));
        jComboBox2.setForeground(new java.awt.Color(255, 255, 255));
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "10", "12", "14", "16", "18", "20", "22", "24", "26", "28", "30", "40", "50", "60", "70" }));
        jComboBox2.setFocusable(false);
        jComboBox2.setPreferredSize(new java.awt.Dimension(50, 30));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });
        jPanel1.add(jComboBox2);

        jButton1.setBackground(new java.awt.Color(51, 51, 51));
        jButton1.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("I");
        jButton1.setFocusable(false);
        jButton1.setPreferredSize(new java.awt.Dimension(45, 30));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);

        jButton2.setBackground(new java.awt.Color(51, 51, 51));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("B");
        jButton2.setFocusable(false);
        jButton2.setPreferredSize(new java.awt.Dimension(45, 30));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2);

        jButton4.setBackground(new java.awt.Color(51, 51, 51));
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Kolor czcionki");
        jButton4.setFocusable(false);
        jButton4.setPreferredSize(new java.awt.Dimension(115, 30));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton4);

        jTextField1.setEditable(false);
        jTextField1.setBackground(new java.awt.Color(0, 0, 0));
        jTextField1.setPreferredSize(new java.awt.Dimension(30, 30));
        jPanel1.add(jTextField1);

        jButton3.setBackground(new java.awt.Color(51, 51, 51));
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Kolor tła");
        jButton3.setFocusable(false);
        jButton3.setPreferredSize(new java.awt.Dimension(115, 30));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3);

        jTextField2.setEditable(false);
        jTextField2.setBackground(new java.awt.Color(255, 255, 255));
        jTextField2.setForeground(new java.awt.Color(255, 255, 255));
        jTextField2.setPreferredSize(new java.awt.Dimension(30, 30));
        jPanel1.add(jTextField2);

        jCheckBox1.setBackground(new java.awt.Color(51, 51, 51));
        jCheckBox1.setForeground(new java.awt.Color(255, 255, 255));
        jCheckBox1.setText("Zawijaj tekst");
        jCheckBox1.setFocusable(false);
        jCheckBox1.setPreferredSize(new java.awt.Dimension(100, 30));
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        jPanel1.add(jCheckBox1);

        jButton5.setBackground(new java.awt.Color(51, 51, 51));
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Kopiuj");
        jButton5.setFocusable(false);
        jButton5.setPreferredSize(new java.awt.Dimension(72, 31));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton5);

        jButton6.setBackground(new java.awt.Color(51, 51, 51));
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("Wklej");
        jButton6.setFocusable(false);
        jButton6.setPreferredSize(new java.awt.Dimension(72, 31));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton6);

        jButton7.setBackground(new java.awt.Color(51, 51, 51));
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText("Wytnij");
        jButton7.setFocusable(false);
        jButton7.setPreferredSize(new java.awt.Dimension(72, 31));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton7);

        jEditorPane1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jEditorPane1.setPreferredSize(new java.awt.Dimension(30, 30));
        jScrollPane1.setViewportView(jEditorPane1);

        jMenu1.setText("Plik");
        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 987, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Font currentFont = jEditorPane1.getFont();
        int style = currentFont.getStyle() ^ Font.ITALIC;
        jEditorPane1.setFont(currentFont.deriveFont(style));
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        Color newColor = JColorChooser.showDialog(this, "Wybierz kolor tła", jEditorPane1.getBackground());
        if (newColor != null) {
            jEditorPane1.setBackground(newColor);
            jTextField2.setBackground(newColor); 
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        Color newColor = JColorChooser.showDialog(this, "Wybierz kolor czcionki", jEditorPane1.getForeground());
        if (newColor != null) {
            jEditorPane1.setForeground(newColor);
            jTextField1.setBackground(newColor); 
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
         jEditorPane1.copy();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        jEditorPane1.cut();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        jEditorPane1.paste();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        boolean wrap = jCheckBox1.isSelected();
    if (wrap) {
        jEditorPane1.setEditorKit(new javax.swing.text.StyledEditorKit() {
            @Override
            public javax.swing.text.ViewFactory getViewFactory() {
                return new StyledViewFactory();
            }
        });
    } else {
        // wersja bez zawijania (tekst w jednej linii)
        jEditorPane1.setEditorKit(new javax.swing.text.StyledEditorKit());
    }
    jEditorPane1.repaint();
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        String fontName = (String) jComboBox1.getSelectedItem();
        Font current = jEditorPane1.getFont();
        jEditorPane1.setFont(new Font(fontName, current.getStyle(), current.getSize()));
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        int size = Integer.parseInt((String) jComboBox2.getSelectedItem());
        Font current = jEditorPane1.getFont();
        jEditorPane1.setFont(new Font(current.getFontName(), current.getStyle(), size));
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
       Font currentFont = jEditorPane1.getFont();
       int style = currentFont.getStyle() ^ Font.BOLD; 
       jEditorPane1.setFont(currentFont.deriveFont(style));
    }//GEN-LAST:event_jButton2ActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new word().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
