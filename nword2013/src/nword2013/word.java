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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class word extends javax.swing.JFrame {

    public word() {
        initComponents();
        ustawMenuPlik();

        jTextArea1.setFont(new Font("Arial", Font.PLAIN, 12));
        jTextArea1.setLineWrap(false);
        jTextArea1.setWrapStyleWord(true);
    }

    private void ustawMenuPlik() {
        JMenuItem menuOtworz = new JMenuItem("Otworz");
        menuOtworz.addActionListener(evt -> {
            JFileChooser fc = new JFileChooser();
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    jTextArea1.read(br, null);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Blad odczytu pliku", "Blad", JOptionPane.ERROR_MESSAGE);
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
                    jTextArea1.write(bw);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Blad zapisu pliku", "Blad", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        jMenu1.add(menuZapisz);

        JMenuItem menuZakoncz = new JMenuItem("Zakoncz");
        menuZakoncz.addActionListener(evt -> System.exit(0));
        jMenu1.add(menuZakoncz);
    }

    @SuppressWarnings("unchecked")
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
        jScrollPane1 = new JScrollPane();
        jTextArea1 = new JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(600, 600));
        setPreferredSize(new java.awt.Dimension(1000, 600));

        jPanel1.setBackground(new java.awt.Color(54, 52, 52));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));

        jComboBox1.setBackground(new java.awt.Color(51, 51, 51));
        jComboBox1.setForeground(new java.awt.Color(255, 255, 255));
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Arial", "Comic Sans MS", "Century", "Consolas"}));
        jComboBox1.setFocusable(false);
        jComboBox1.setPreferredSize(new java.awt.Dimension(100, 30));
        jComboBox1.addActionListener(evt -> {
            String fontName = (String) jComboBox1.getSelectedItem();
            Font current = jTextArea1.getFont();
            jTextArea1.setFont(new Font(fontName, current.getStyle(), current.getSize()));
        });
        jPanel1.add(jComboBox1);

        jComboBox2.setBackground(new java.awt.Color(51, 51, 51));
        jComboBox2.setForeground(new java.awt.Color(255, 255, 255));
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"10", "12", "14", "16", "18", "20", "22", "24", "26", "28", "30", "40", "50", "60", "70"}));
        jComboBox2.setFocusable(false);
        jComboBox2.setPreferredSize(new java.awt.Dimension(50, 30));
        jComboBox2.addActionListener(evt -> {
            int size = Integer.parseInt((String) jComboBox2.getSelectedItem());
            Font current = jTextArea1.getFont();
            jTextArea1.setFont(new Font(current.getFontName(), current.getStyle(), size));
        });
        jPanel1.add(jComboBox2);

        jButton1.setBackground(new java.awt.Color(51, 51, 51));
        jButton1.setFont(new java.awt.Font("Segoe UI", Font.ITALIC, 12));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("I");
        jButton1.setFocusable(false);
        jButton1.setPreferredSize(new java.awt.Dimension(45, 30));
        jButton1.addActionListener(evt -> {
            Font current = jTextArea1.getFont();
            int style = current.getStyle() ^ Font.ITALIC;
            jTextArea1.setFont(current.deriveFont(style));
        });
        jPanel1.add(jButton1);

        jButton2.setBackground(new java.awt.Color(51, 51, 51));
        jButton2.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 12));
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("B");
        jButton2.setFocusable(false);
        jButton2.setPreferredSize(new java.awt.Dimension(45, 30));
        jButton2.addActionListener(evt -> {
            Font currentFont = jTextArea1.getFont();
            int style = currentFont.getStyle() ^ Font.BOLD;
            jTextArea1.setFont(currentFont.deriveFont(style));
        });
        jPanel1.add(jButton2);

        jButton4.setBackground(new java.awt.Color(51, 51, 51));
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Kolor czcionki");
        jButton4.setFocusable(false);
        jButton4.setPreferredSize(new java.awt.Dimension(115, 30));
        jButton4.addActionListener(evt -> {
            Color newColor = JColorChooser.showDialog(this, "Wybierz kolor czcionki", jTextArea1.getForeground());
            if (newColor != null) {
                jTextArea1.setForeground(newColor);
                jTextField1.setBackground(newColor);
            }
        });
        jPanel1.add(jButton4);

        jTextField1.setEditable(false);
        jTextField1.setBackground(new java.awt.Color(0, 0, 0));
        jTextField1.setPreferredSize(new java.awt.Dimension(30, 30));
        jPanel1.add(jTextField1);

        jButton3.setBackground(new java.awt.Color(51, 51, 51));
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Kolor tla");
        jButton3.setFocusable(false);
        jButton3.setPreferredSize(new java.awt.Dimension(115, 30));
        jButton3.addActionListener(evt -> {
            Color newColor = JColorChooser.showDialog(this, "Wybierz kolor tła", jTextArea1.getBackground());
            if (newColor != null) {
                jTextArea1.setBackground(newColor);
                jTextField2.setBackground(newColor);
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
        jCheckBox1.addActionListener(evt -> {
            boolean wrap = jCheckBox1.isSelected();
            jTextArea1.setLineWrap(wrap);
            jScrollPane1.setHorizontalScrollBarPolicy(wrap ? JScrollPane.HORIZONTAL_SCROLLBAR_NEVER : JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        });
        jPanel1.add(jCheckBox1);

        jButton5.setBackground(new java.awt.Color(51, 51, 51));
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Kopiuj");
        jButton5.setFocusable(false);
        jButton5.setPreferredSize(new java.awt.Dimension(72, 31));
        jButton5.addActionListener(evt -> jTextArea1.copy());
        jPanel1.add(jButton5);

        jButton6.setBackground(new java.awt.Color(51, 51, 51));
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("Wklej");
        jButton6.setFocusable(false);
        jButton6.setPreferredSize(new java.awt.Dimension(72, 31));
        jButton6.addActionListener(evt -> jTextArea1.paste());
        jPanel1.add(jButton6);

        jButton7.setBackground(new java.awt.Color(51, 51, 51));
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText("Wytnij");
        jButton7.setFocusable(false);
        jButton7.setPreferredSize(new java.awt.Dimension(72, 31));
        jButton7.addActionListener(evt -> jTextArea1.cut());
        jPanel1.add(jButton7);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

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
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new word().setVisible(true));
    }

    private javax.swing.JButton jButton1, jButton2, jButton3, jButton4, jButton5, jButton6, jButton7;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox<String> jComboBox1, jComboBox2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1, jTextField2;
    private JTextArea jTextArea1;
}
