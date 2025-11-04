import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class parkomat extends JFrame {
    private JPanel paymentPanel, mainPanel, ticketPanel;
    private JTextField regInput, manualAmountField;
    private JTextArea ticketOutput;
    private JComboBox<String> paymentSelector;
    private JComboBox<String> timeSelector;
    private JLabel costLabel, statusLabel;
    private JButton printBtn; // przechowujemy referencję dla wygody
    private static final double RATE_PER_HOUR = 4.0;
    private String selectedPayment = "Monety";
    private final DecimalFormat df = new DecimalFormat("0.00");

    public parkomat() {
        setTitle("Symulator Parkometru");
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
        UIManager.put("TextArea.background", new Color(40, 40, 40));
        UIManager.put("TextArea.foreground", Color.WHITE);
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
        for (int i = 0; i < methods.length; i++) {
            String m = methods[i];
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
            gbc.gridy = i;
            center.add(btn, gbc);
        }
        paymentPanel.add(center, BorderLayout.CENTER);
    }

    private void initMainPanel() {
        mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(25, 25, 25));

        // Górny pasek
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topBar.setBackground(new Color(25, 25, 25));
        JButton backBtn = new JButton("Cofnij");
        backBtn.setFont(new Font("Arial", Font.BOLD, 16));
        backBtn.setBackground(new Color(70, 70, 70));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.addActionListener(e -> showPanel("payment"));
        topBar.add(backBtn);
        mainPanel.add(topBar, BorderLayout.NORTH);

        // Lewy panel (formularz)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(35, 35, 35));
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 80), 1),
                "Dane kierowcy", 0, 0,
                new Font("Arial", Font.BOLD, 18),
                Color.WHITE));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.anchor = GridBagConstraints.WEST;

        Dimension inputSize = new Dimension(180, 30);

        // Forma płatności (zablokowana, wyszarzona)
        c.gridx = 0; c.gridy = 0;
        JLabel payLabel = new JLabel("Forma płatności:");
        payLabel.setForeground(Color.WHITE);
        formPanel.add(payLabel, c);
        c.gridx = 1;
        paymentSelector = new JComboBox<>(new String[]{"Monety", "Banknoty", "Karta"});
        paymentSelector.setEnabled(false); // nie można zmieniać
        paymentSelector.setPreferredSize(inputSize);
        paymentSelector.setBackground(new Color(60, 60, 60));
        paymentSelector.setForeground(new Color(160, 160, 160)); // wyszarzony tekst
        formPanel.add(paymentSelector, c);

        // Numer rejestracyjny
        c.gridx = 0; c.gridy = 1;
        JLabel regLabel = new JLabel("Numer rejestracyjny:");
        regLabel.setForeground(Color.WHITE);
        formPanel.add(regLabel, c);
        c.gridx = 1;
        regInput = new JTextField();
        regInput.setFont(new Font("Consolas", Font.BOLD, 18));
        regInput.setPreferredSize(inputSize);
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

        // Czas parkowania
        c.gridx = 0; c.gridy = 2;
        JLabel timeLabel = new JLabel("Czas parkowania:");
        timeLabel.setForeground(Color.WHITE);
        formPanel.add(timeLabel, c);
        c.gridx = 1;
        String[] times = {"0.5h", "1h", "2h", "3h", "4h"};
        timeSelector = new JComboBox<>(times);
        timeSelector.setPreferredSize(inputSize);
        timeSelector.addActionListener(e -> updateCostLabel());
        formPanel.add(timeSelector, c);

        // Kwota wrzucona (pusta na start)
        c.gridx = 0; c.gridy = 3;
        JLabel amountLabel = new JLabel("Kwota wrzucona:");
        amountLabel.setForeground(Color.WHITE);
        formPanel.add(amountLabel, c);
        c.gridx = 1;
        manualAmountField = new JTextField();
        manualAmountField.setFont(new Font("Consolas", Font.BOLD, 16));
        manualAmountField.setPreferredSize(inputSize);
        manualAmountField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updatePrintButtonState();
            }
        });
        formPanel.add(manualAmountField, c);

        // Pasek dodatkowych informacji (u dołu lewego panelu) - zostawimy miejsce
        c.gridx = 0; c.gridy = 4; c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        JPanel spacer = new JPanel();
        spacer.setOpaque(false);
        spacer.setPreferredSize(new Dimension(10, 10));
        formPanel.add(spacer, c);

        mainPanel.add(formPanel, BorderLayout.WEST);

        // Prawy panel (podsumowanie)
        JPanel summaryPanel = new JPanel(new BorderLayout(10, 10));
        summaryPanel.setBackground(new Color(35, 35, 35));
        summaryPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 80), 1),
                "Podsumowanie", 0, 0,
                new Font("Arial", Font.BOLD, 18),
                Color.WHITE));

        JPanel infoBox = new JPanel(new GridLayout(3, 1, 10, 10));
        infoBox.setBackground(new Color(35, 35, 35));

        costLabel = new JLabel("Cena: 0.00 zł", SwingConstants.CENTER);
        costLabel.setFont(new Font("Arial", Font.BOLD, 22));
        costLabel.setForeground(new Color(180, 255, 180));

        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        statusLabel.setForeground(Color.ORANGE);

        printBtn = new JButton("Drukuj bilet"); // bez emotki
        printBtn.setFont(new Font("Arial", Font.BOLD, 20));
        printBtn.setBackground(new Color(0, 160, 0));
        printBtn.setForeground(Color.WHITE);
        printBtn.setFocusPainted(false);
        printBtn.addActionListener(e -> validateAndGenerateTicket());

        infoBox.add(costLabel);
        infoBox.add(statusLabel);
        infoBox.add(printBtn);

        summaryPanel.add(infoBox, BorderLayout.CENTER);
        mainPanel.add(summaryPanel, BorderLayout.CENTER);

        // Dolny panel (klawiatura)
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(new Color(25,25,25));
        bottom.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        bottom.add(createKeyboardPanel(), BorderLayout.CENTER);
        mainPanel.add(bottom, BorderLayout.SOUTH);

        updateCostLabel();
    }

    private JPanel createKeyboardPanel() {
        JPanel keyboard = new JPanel(new GridLayout(4, 1, 5, 5));
        keyboard.setBackground(new Color(30, 30, 30));
        String[] rows = {
                "1 2 3 4 5 6 7 8 9 0",
                "Q W E R T Y U I O P",
                "A S D F G H J K L ←",
                "Z X C V B N M"
        };
        for (String row : rows) {
            JPanel r = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
            r.setBackground(new Color(30,30,30));
            for (String key : row.split(" ")) {
                JButton b = new JButton(key);
                b.setPreferredSize(new Dimension(55, 55));
                b.setBackground(new Color(60,60,60));
                b.setForeground(Color.WHITE);
                b.setFocusPainted(false);
                b.addActionListener(e -> handleKeyPress(key));
                r.add(b);
            }
            keyboard.add(r);
        }
        return keyboard;
    }

    private void handleKeyPress(String key) {
        if (key.equals("←")) {
            String t = regInput.getText();
            if (!t.isEmpty()) regInput.setText(t.substring(0, t.length()-1));
        } else if (key.matches("[A-Z0-9]") && regInput.getText().length() < 7) {
            regInput.setText(regInput.getText() + key);
        }
    }

    private void initTicketPanel() {
        ticketPanel = new JPanel(new BorderLayout(10, 10));
        ticketPanel.setBackground(new Color(30,30,30));
        JLabel title = new JLabel("Bilet parkingowy", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        ticketPanel.add(title, BorderLayout.NORTH);

        ticketOutput = new JTextArea();
        ticketOutput.setFont(new Font("Monospaced", Font.PLAIN, 18));
        ticketOutput.setBackground(new Color(45,45,45));
        ticketOutput.setForeground(Color.WHITE);
        ticketOutput.setEditable(false);
        ticketPanel.add(new JScrollPane(ticketOutput), BorderLayout.CENTER);

        JButton back = new JButton("Powrót do początku");
        back.setBackground(new Color(70,70,70));
        back.setForeground(Color.WHITE);
        back.setFocusPainted(false);
        back.addActionListener(e -> showPanel("payment"));
        ticketPanel.add(back, BorderLayout.SOUTH);
    }

    private void updateCostLabel() {
        double hours = parseHoursFromCombo((String) timeSelector.getSelectedItem());
        double cost = hours * RATE_PER_HOUR;
        costLabel.setText("Cena: " + df.format(cost) + " zł");
        updatePrintButtonState();
    }

    private double parseHoursFromCombo(String s) {
        // obsługa wartości typu 0.5h, 1h
        return Double.parseDouble(s.replace("h", ""));
    }

    private void updateUiForPayment() {
        paymentSelector.setSelectedItem(selectedPayment);
        // jeśli karta wybrana → pole kwoty nieaktywne
        manualAmountField.setEnabled(!selectedPayment.equals("Karta"));
        manualAmountField.setBackground(selectedPayment.equals("Karta") ? new Color(50,50,50) : new Color(45,45,45));
        updatePrintButtonState();
    }

    private void updatePrintButtonState() {
        if (printBtn == null) return;
        double hours = parseHoursFromCombo((String) timeSelector.getSelectedItem());
        double cost = hours * RATE_PER_HOUR;

        if (selectedPayment.equals("Karta")) {
            printBtn.setEnabled(true);
            statusLabel.setText("Płatność kartą — brak reszty");
            statusLabel.setForeground(new Color(200, 255, 200));
        } else {
            double inserted;
            try { inserted = manualAmountField.getText().trim().isEmpty() ? 0.0
                    : Double.parseDouble(manualAmountField.getText().replace(",", ".")); }
            catch (Exception ex) { inserted = 0; }
            if (inserted >= cost && inserted > 0) {
                printBtn.setEnabled(true);
                statusLabel.setText("Wystarczy — reszta: " + df.format(inserted - cost) + " zł");
                statusLabel.setForeground(new Color(200, 255, 200));
            } else {
                printBtn.setEnabled(false);
                statusLabel.setText("Brakuje: " + df.format(Math.max(0, cost - inserted)) + " zł");
                statusLabel.setForeground(Color.ORANGE);
            }
        }
    }

    private void validateAndGenerateTicket() {
        String reg = regInput.getText().trim();
        if (reg.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Wpisz numer rejestracyjny", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double hours = parseHoursFromCombo((String) timeSelector.getSelectedItem());
        double cost = hours * RATE_PER_HOUR;

        double inserted = 0;
        if (!selectedPayment.equals("Karta")) {
            try { inserted = manualAmountField.getText().trim().isEmpty() ? 0.0
                    : Double.parseDouble(manualAmountField.getText().replace(",", ".")); }
            catch (Exception ex) { inserted = 0; }
            if (inserted < cost || inserted == 0) {
                JOptionPane.showMessageDialog(this, "Za mało pieniędzy!", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        double change = selectedPayment.equals("Karta") ? 0 : Math.max(0, inserted - cost);

        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, (int)(hours*60));
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");

        ticketOutput.setText(
            "==============================\n" +
            "       BILET PARKINGOWY\n" +
            "==============================\n" +
            "Rejestracja: " + reg + "\n" +
            "Forma płatności: " + selectedPayment + "\n" +
            "Czas: " + (int)(hours*60) + " minut\n" +
            "Do zapłaty: " + df.format(cost) + " zł\n" +
            (selectedPayment.equals("Karta") ? "Płatność kartą — autoryzacja OK\n"
                                            : "Wrzucono: " + df.format(inserted) + " zł\nReszta: " + df.format(change) + " zł\n") +
            "Start: " + sdf.format(now) + "\n" +
            "Ważny do: " + sdf.format(cal.getTime()) + "\n" +
            "==============================\n"
        );
        showPanel("ticket");
    }

    private void showPanel(String name) {
        CardLayout cl = (CardLayout) getContentPane().getLayout();
        cl.show(getContentPane(), name);
        if (name.equals("main")) updateUiForPayment();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new parkomat().setVisible(true));
    }
}
