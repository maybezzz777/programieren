import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class parkomat extends JFrame {
    private JPanel paymentPanel, mainPanel, ticketPanel;
    private JTextField regInput, manualAmountField;
    private JLabel amountLabel;
    private JTextArea ticketOutput;
    private JComboBox<String> paymentSelector, timeSelector;
    private JLabel costLabel, statusLabel;
    private JButton printBtn;
    private static final double RATE_PER_HOUR = 4.0;
    private String selectedPayment = "Monety";
    private final DecimalFormat df = new DecimalFormat("0.00");
    private JTextField activeField;

    public parkomat() {
        setTitle("Parkomat");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 600);
        setLocationRelativeTo(null);
        setLayout(new CardLayout());
        setDarkTheme();
        initPaymentPanel();
        initMainPanel();
        initTicketPanel();
        add(paymentPanel, "payment");
        add(mainPanel, "main");
        add(ticketPanel, "ticket");
        showPanel("payment");
    }

    private void setDarkTheme() {
        UIManager.put("Panel.background", new Color(30, 30, 30));
        UIManager.put("Label.foreground", Color.WHITE);
        UIManager.put("Button.background", new Color(60, 60, 60));
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("TextField.background", new Color(45, 45, 45));
        UIManager.put("TextField.foreground", Color.WHITE);
        UIManager.put("ComboBox.background", new Color(45, 45, 45));
        UIManager.put("ComboBox.foreground", Color.WHITE);
        UIManager.put("ComboBox.disabledBackground", new Color(45, 45, 45));
        UIManager.put("ComboBox.disabledForeground", new Color(160, 160, 160));
    }

    private void initPaymentPanel() {
        paymentPanel = new JPanel(new BorderLayout(10, 10));
        paymentPanel.setBackground(new Color(30, 30, 30));
        JLabel title = new JLabel("Wybierz formę płatności", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        paymentPanel.add(title, BorderLayout.NORTH);
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(new Color(30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        String[] methods = {"Monety", "Banknoty", "Karta"};
        for (String m : methods) {
            JButton btn = new JButton(m);
            btn.setFont(new Font("Arial", Font.BOLD, 26));
            btn.setPreferredSize(new Dimension(300, 80));
            btn.setFocusPainted(false);
            btn.setBackground(new Color(60, 60, 60));
            btn.setForeground(Color.WHITE);
            btn.addActionListener(e -> {
                selectedPayment = m;
                if (paymentSelector != null) paymentSelector.setSelectedItem(selectedPayment);
                showPanel("main");
                updateUiForPayment();
            });
            gbc.gridy++;
            center.add(btn, gbc);
        }
        paymentPanel.add(center, BorderLayout.CENTER);
    }

    private void initMainPanel() {
        mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(25, 25, 25));
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topBar.setBackground(new Color(25, 25, 25));
        JButton backBtn = new JButton("Cofnij");
        backBtn.setFont(new Font("Arial", Font.BOLD, 16));
        backBtn.setBackground(new Color(70, 70, 70));
        backBtn.setForeground(Color.WHITE);
        backBtn.addActionListener(e -> showPanel("payment"));
        topBar.add(backBtn);
        mainPanel.add(topBar, BorderLayout.NORTH);
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(35, 35, 35));
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 80), 1),
                "Dane kierowcy", 0, 0,
                new Font("Arial", Font.BOLD, 18), Color.WHITE));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.anchor = GridBagConstraints.WEST;
        Dimension inputSize = new Dimension(200, 30);

        c.gridx = 0; c.gridy = 0;
        formPanel.add(new JLabel("Forma płatności:"), c);
        c.gridx = 1;
        paymentSelector = new JComboBox<>(new String[]{"Monety", "Banknoty", "Karta"});
        paymentSelector.setPreferredSize(inputSize);
        paymentSelector.setEnabled(false);
        paymentSelector.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton("▼");
                button.setBackground(new Color(45, 45, 45));
                button.setForeground(new Color(160, 160, 160));
                button.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70)));
                return button;
            }
        });
        paymentSelector.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                lbl.setOpaque(true);
                lbl.setBackground(new Color(45, 45, 45));
                lbl.setForeground(new Color(180, 180, 180));
                return lbl;
            }
        });
        paymentSelector.setBackground(new Color(45, 45, 45));
        paymentSelector.setForeground(new Color(180, 180, 180));
        formPanel.add(paymentSelector, c);

        c.gridx = 0; c.gridy = 1;
        formPanel.add(new JLabel("Numer rejestracyjny:"), c);
        c.gridx = 1;
        regInput = new JTextField();
        regInput.setFont(new Font("Consolas", Font.BOLD, 18));
        regInput.setPreferredSize(inputSize);
        regInput.setCaretColor(Color.WHITE);
        regInput.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { activeField = regInput; }
        });
        regInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char ch = Character.toUpperCase(e.getKeyChar());
                if (!Character.isLetterOrDigit(ch) || regInput.getText().length() >= 7)
                    e.consume();
                else e.setKeyChar(ch);
            }
        });
        formPanel.add(regInput, c);

        c.gridx = 0; c.gridy = 2;
        formPanel.add(new JLabel("Czas parkowania:"), c);
        c.gridx = 1;
        String[] times = {"0.5h", "1h", "2h", "3h", "4h", "6h", "12h", "24h"};
        timeSelector = new JComboBox<>(times);
timeSelector.setPreferredSize(inputSize);

timeSelector.setBackground(new Color(45, 45, 45));
timeSelector.setForeground(Color.WHITE);
timeSelector.setFocusable(false);

timeSelector.setUI(new BasicComboBoxUI() {
    @Override
    protected JButton createArrowButton() {
        JButton button = new JButton("▼");
        button.setBackground(new Color(45, 45, 45));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70)));
        button.setFocusable(false);
        return button;
    }
});

timeSelector.setRenderer(new DefaultListCellRenderer() {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        lbl.setOpaque(true);
        if (isSelected) {
            lbl.setBackground(new Color(70, 70, 70)); 
            lbl.setForeground(Color.WHITE);
        } else {
            lbl.setBackground(new Color(45, 45, 45)); 
            lbl.setForeground(new Color(200, 200, 200));
        }
        return lbl;
    }
});

timeSelector.addActionListener(e -> updateCostLabel());
formPanel.add(timeSelector, c);


        c.gridx = 0; c.gridy = 3;
        amountLabel = new JLabel("Kwota wrzucona:");
        formPanel.add(amountLabel, c);
        c.gridx = 1;
        manualAmountField = new JTextField();
        manualAmountField.setFont(new Font("Consolas", Font.BOLD, 16));
        manualAmountField.setPreferredSize(inputSize);
        manualAmountField.setCaretColor(Color.WHITE);
        manualAmountField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { activeField = manualAmountField; }
        });

        manualAmountField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char ch = e.getKeyChar();
                String text = manualAmountField.getText();
                if (Character.isDigit(ch)) return;
                if ((ch == '.' || ch == ',') && !text.contains(".") && !text.contains(",")) return;
                e.consume();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                updatePrintButtonState();
            }
        });

        formPanel.add(manualAmountField, c);

        mainPanel.add(formPanel, BorderLayout.WEST);
        JPanel summary = new JPanel(new BorderLayout(10, 10));
        summary.setBackground(new Color(35, 35, 35));
        summary.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 80), 1),
                "Podsumowanie", 0, 0,
                new Font("Arial", Font.BOLD, 18), Color.WHITE));
        costLabel = new JLabel("Cena: 0,00 zł", SwingConstants.CENTER);
        costLabel.setFont(new Font("Arial", Font.BOLD, 22));
        costLabel.setForeground(new Color(180, 255, 180));
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        statusLabel.setForeground(Color.ORANGE);
        printBtn = new JButton("Drukuj bilet");
        printBtn.setFont(new Font("Arial", Font.BOLD, 20));
        printBtn.setBackground(new Color(0, 160, 0));
        printBtn.setForeground(Color.WHITE);
        printBtn.addActionListener(e -> validateAndGenerateTicket());
        JPanel infoBox = new JPanel(new GridLayout(3, 1, 10, 10));
        infoBox.setBackground(new Color(35, 35, 35));
        infoBox.add(costLabel);
        infoBox.add(statusLabel);
        infoBox.add(printBtn);
        summary.add(infoBox, BorderLayout.CENTER);
        mainPanel.add(summary, BorderLayout.CENTER);
        mainPanel.add(createKeyboardPanel(), BorderLayout.SOUTH);
        updateCostLabel();
    }

    private JPanel createKeyboardPanel() {
        JPanel keyboard = new JPanel(new GridLayout(4, 1, 5, 5));
        keyboard.setBackground(new Color(30, 30, 30));
        String[] rows = {
                "1 2 3 4 5 6 7 8 9 0",
                "Q W E R T Y U I O P",
                "A S D F G H J K L ←",
                "Z X C V B N M ."
        };
        for (String row : rows) {
            JPanel r = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
            r.setBackground(new Color(30, 30, 30));
            for (String key : row.split(" ")) {
                JButton b = new JButton(key);
                b.setPreferredSize(new Dimension(55, 55));
                b.setBackground(new Color(60, 60, 60));
                b.setForeground(Color.WHITE);
                b.addActionListener(e -> handleKeyPress(key));
                r.add(b);
            }
            keyboard.add(r);
        }
        return keyboard;
    }

    private void handleKeyPress(String key) {
        if (activeField == null) return;
        if (key.equals("←")) {
            String t = activeField.getText();
            if (!t.isEmpty()) activeField.setText(t.substring(0, t.length() - 1));
        } else {
            if (activeField == regInput) {
                if (key.matches("[A-Z0-9]") && regInput.getText().length() < 7)
                    regInput.setText(regInput.getText() + key);
            } else if (activeField == manualAmountField) {
                if (key.matches("[0-9\\.,]")) {
                    String text = manualAmountField.getText();
                    if ((key.equals(".") || key.equals(",")) &&
                            (text.contains(".") || text.contains(","))) return;
                    manualAmountField.setText(text + key);
                }
            }
        }
        updatePrintButtonState();
    }

    private void initTicketPanel() {
        ticketPanel = new JPanel(new BorderLayout(10, 10));
        ticketPanel.setBackground(new Color(30, 30, 30));
        JLabel title = new JLabel("Bilet parkingowy", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        ticketPanel.add(title, BorderLayout.NORTH);
        ticketOutput = new JTextArea();
        ticketOutput.setEditable(false);
        ticketOutput.setBackground(new Color(40, 40, 40));
        ticketOutput.setForeground(Color.WHITE);
        ticketOutput.setFont(new Font("Monospaced", Font.PLAIN, 18));
        ticketPanel.add(new JScrollPane(ticketOutput), BorderLayout.CENTER);

        JButton back = new JButton("Powrót");
        back.setFont(new Font("Arial", Font.BOLD, 22));
        back.setPreferredSize(new Dimension(200, 70));
        back.setBackground(new Color(90, 90, 90));
        back.setForeground(Color.WHITE);
        back.addActionListener(e -> {
            regInput.setText("");
            manualAmountField.setText("");
            timeSelector.setSelectedIndex(0);
            statusLabel.setText(" ");
            costLabel.setText("Cena: 0,00 zł");
            selectedPayment = "Monety";
            paymentSelector.setSelectedItem(selectedPayment);
            updateUiForPayment();
            showPanel("payment");
        });
        JPanel bottom = new JPanel();
        bottom.setBackground(new Color(30, 30, 30));
        bottom.add(back);
        ticketPanel.add(bottom, BorderLayout.SOUTH);
    }

    private void updateCostLabel() {
        double hours = Double.parseDouble(((String) timeSelector.getSelectedItem()).replace("h", ""));
        double cost = hours * RATE_PER_HOUR;
        costLabel.setText("Cena: " + df.format(cost) + " zł");
        updatePrintButtonState();
    }

    private void updateUiForPayment() {
        paymentSelector.setSelectedItem(selectedPayment);
        boolean showAmount = !selectedPayment.equals("Karta");
        manualAmountField.setVisible(showAmount);
        amountLabel.setVisible(showAmount);
        updatePrintButtonState();
    }

    private void updatePrintButtonState() {
        double hours = Double.parseDouble(((String) timeSelector.getSelectedItem()).replace("h", ""));
        double cost = hours * RATE_PER_HOUR;
        if (selectedPayment.equals("Karta")) {
            printBtn.setEnabled(true);
            statusLabel.setText("Płatność kartą");
            return;
        }
        double inserted;
        try {
            inserted = manualAmountField.getText().isEmpty() ? 0.0 :
                    Double.parseDouble(manualAmountField.getText().replace(",", "."));
        } catch (Exception e) {
            inserted = 0;
        }
        if (inserted >= cost) {
    printBtn.setEnabled(true);
    double change = inserted - cost;
    if (change > 0.0) {
        statusLabel.setText("Reszta: " + df.format(change) + " zł");
    } else {
        statusLabel.setText("");
    }
} else {
    printBtn.setEnabled(false);
    statusLabel.setText("Brakuje: " + df.format(cost - inserted) + " zł");
}

    }

    private void validateAndGenerateTicket() {
    String reg = regInput.getText().trim();
    if (reg.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Podaj numer rejestracyjny!", "Błąd", JOptionPane.ERROR_MESSAGE);
        return;
    }

    double hours = Double.parseDouble(((String) timeSelector.getSelectedItem()).replace("h", ""));
    double cost = hours * RATE_PER_HOUR;
    double inserted = 0;

    if (!selectedPayment.equals("Karta")) {
        try {
            inserted = manualAmountField.getText().isEmpty() ? 0.0 :
                    Double.parseDouble(manualAmountField.getText().replace(",", "."));
        } catch (Exception e) {
            inserted = 0;
        }
        if (inserted < cost) {
            JOptionPane.showMessageDialog(this, "Za mało pieniędzy!", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    double change = selectedPayment.equals("Karta") ? 0 : Math.max(0, inserted - cost);

    Calendar cal = Calendar.getInstance();
    Date now = cal.getTime();
    cal.add(Calendar.MINUTE, (int) (hours * 60));
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");

    StringBuilder ticket = new StringBuilder();
    ticket.append("\n")
          .append(" BILET PARKINGOWY\n")
          .append("\n")
          .append(" Numer rejestracyjny: ").append(reg).append("\n")
          .append(" Płatność: ").append(selectedPayment).append("\n")
          .append(" Czas: ").append(hours).append("h\n")
          .append(" Cena: ").append(df.format(cost)).append(" zł\n");

    if (selectedPayment.equals("Karta")) {
    } else {
        ticket.append(" Wrzucono: ").append(df.format(inserted)).append(" zł\n");
        if (change > 0) {
            ticket.append(" Reszta: ").append(df.format(change)).append(" zł\n");
        }
    }

    ticket.append(" Start: ").append(sdf.format(now)).append("\n")
          .append(" Ważny do: ").append(sdf.format(cal.getTime())).append("\n");

    ticketOutput.setText(ticket.toString());
    showPanel("ticket");
}


    private void showPanel(String name) {
        CardLayout cl = (CardLayout) getContentPane().getLayout();
        cl.show(getContentPane(), name);
        if (name.equals("main")) updateUiForPayment();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(()-> new parkomat().setVisible(true));
    }
}
