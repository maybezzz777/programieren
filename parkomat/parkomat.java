import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class parkomat extends JFrame {
    private JPanel paymentPanel, mainPanel, ticketPanel;
    private JTextField regInput, amountInput;
    private JTextArea ticketOutput;
    private JComboBox<String> paymentSelector;
    private static final double RATE_PER_HOUR = 4.0;
    private String selectedPayment = "Monety";

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
        UIManager.put("ComboBox.selectionBackground", new Color(70, 70, 70));
        UIManager.put("ComboBox.selectionForeground", Color.WHITE);
        UIManager.put("TextArea.background", new Color(40, 40, 40));
        UIManager.put("TextArea.foreground", Color.WHITE);
        UIManager.put("OptionPane.background", new Color(40, 40, 40));
        UIManager.put("Panel.background", new Color(40, 40, 40));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
    }

    private void initPaymentPanel() {
        paymentPanel = new JPanel(new BorderLayout(10, 10));

        JLabel title = new JLabel("Wybierz formę płatności", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        paymentPanel.add(title, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new GridLayout(1, 3, 20, 20));
        buttons.setBackground(new Color(30, 30, 30));

        String[] methods = {"Monety", "Banknoty", "Karta"};
        for (String m : methods) {
            JButton btn = new JButton(m);
            btn.setFont(new Font("Arial", Font.BOLD, 22));
            btn.setFocusPainted(false);
            btn.addActionListener(e -> {
                selectedPayment = m;
                showPanel("main");
            });
            buttons.add(btn);
        }

        paymentPanel.add(buttons, BorderLayout.CENTER);
    }

    private void initMainPanel() {
        mainPanel = new JPanel(new BorderLayout(10, 10));

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topBar.setBackground(new Color(30, 30, 30));
        JButton backBtn = new JButton("Cofnij");
        backBtn.setFocusPainted(false);
        backBtn.setFont(new Font("Arial", Font.BOLD, 16));
        backBtn.setBackground(new Color(60, 60, 60));
        backBtn.setForeground(Color.WHITE);
        backBtn.setMargin(new Insets(8, 16, 8, 16));
        backBtn.addActionListener(e -> showPanel("payment"));
        topBar.add(backBtn);
        mainPanel.add(topBar, BorderLayout.NORTH);

        JPanel inputs = new JPanel(new GridLayout(3, 2, 10, 15));
        inputs.setBackground(new Color(30, 30, 30));

        JLabel formLabel = new JLabel("Forma płatności:");
        formLabel.setFont(new Font("Arial", Font.BOLD, 16));
        inputs.add(formLabel);

        paymentSelector = new JComboBox<>(new String[]{"Monety", "Banknoty", "Karta"});
        paymentSelector.setSelectedItem(selectedPayment);
        paymentSelector.setEnabled(false);
        inputs.add(paymentSelector);

        JLabel regLabel = new JLabel("Numer rejestracyjny:");
        regLabel.setFont(new Font("Arial", Font.BOLD, 16));
        inputs.add(regLabel);

        regInput = new JTextField();
        regInput.setFont(new Font("Consolas", Font.BOLD, 18));
        regInput.setCaretColor(Color.WHITE);
        inputs.add(regInput);

        JLabel kwotaLabel = new JLabel("Kwota (zł):");
        kwotaLabel.setFont(new Font("Arial", Font.BOLD, 16));
        inputs.add(kwotaLabel);

        amountInput = new JTextField();
        amountInput.setFont(new Font("Consolas", Font.BOLD, 18));
        amountInput.setCaretColor(Color.WHITE);
        inputs.add(amountInput);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        left.setBackground(new Color(30, 30, 30));
        left.add(inputs);
        mainPanel.add(left, BorderLayout.WEST);

        JPanel center = new JPanel(new BorderLayout(15, 10));
        center.setBackground(new Color(30, 30, 30));
        center.add(createKeyboardPanel(), BorderLayout.CENTER);

        JPanel pricePanel = new JPanel(new BorderLayout());
        pricePanel.setPreferredSize(new Dimension(220, 200));
        pricePanel.setBackground(new Color(45, 45, 45));
        JLabel priceLabel = new JLabel("<html><center><b>4 zł / godzina</b><br>1 zł = 15 minut</center></html>", SwingConstants.CENTER);
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        priceLabel.setForeground(Color.WHITE);
        pricePanel.add(priceLabel, BorderLayout.CENTER);
        center.add(pricePanel, BorderLayout.EAST);

        mainPanel.add(center, BorderLayout.CENTER);

        JButton printBtn = new JButton("Drukuj bilet");
        printBtn.setFont(new Font("Arial", Font.BOLD, 20));
        printBtn.setBackground(new Color(0, 180, 0));
        printBtn.setForeground(Color.WHITE);
        printBtn.setFocusPainted(false);
        printBtn.addActionListener(e -> validateAndGenerateTicket());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(new Color(30, 30, 30));
        bottom.add(printBtn);
        mainPanel.add(bottom, BorderLayout.SOUTH);
    }

    private JPanel createKeyboardPanel() {
        JPanel keyboard = new JPanel();
        keyboard.setBackground(new Color(30, 30, 30));
        keyboard.setLayout(new GridLayout(5, 1, 5, 5));

        String[] rows = {
                "1 2 3 4 5 6 7 8 9 0",
                "Q W E R T Y U I O P",
                "A S D F G H J K L ←",
                "Z X C V B N M ."
        };

        for (String row : rows) {
            JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
            rowPanel.setBackground(new Color(30, 30, 30));
            for (String key : row.split(" ")) {
                JButton btn = new JButton(key);
                btn.setFont(new Font("Arial", Font.BOLD, 16));
                btn.setPreferredSize(new Dimension(55, 55));
                btn.setBackground(new Color(60, 60, 60));
                btn.setForeground(Color.WHITE);
                btn.setFocusPainted(false);
                btn.addActionListener(e -> handleKeyPress(key));
                rowPanel.add(btn);
            }
            keyboard.add(rowPanel);
        }
        return keyboard;
    }

    private void handleKeyPress(String key) {
        if (key.equals("←")) {
            if (regInput.isFocusOwner()) {
                String text = regInput.getText();
                if (!text.isEmpty()) regInput.setText(text.substring(0, text.length() - 1));
            } else if (amountInput.isFocusOwner()) {
                String text = amountInput.getText();
                if (!text.isEmpty()) amountInput.setText(text.substring(0, text.length() - 1));
            }
        } else if (key.equals(".")) {
            if (amountInput.isFocusOwner())
                amountInput.setText(amountInput.getText() + ".");
        } else if (key.matches("[A-Z0-9]")) {
            if (regInput.isFocusOwner() && regInput.getText().length() < 7) {
                regInput.setText(regInput.getText() + key);
            } else if (amountInput.isFocusOwner()) {
                amountInput.setText(amountInput.getText() + key);
            }
        }
    }

    private void initTicketPanel() {
        ticketPanel = new JPanel(new BorderLayout(10, 10));
        ticketPanel.setBackground(new Color(30, 30, 30));

        JLabel title = new JLabel("Bilet parkingowy", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        ticketPanel.add(title, BorderLayout.NORTH);

        ticketOutput = new JTextArea();
        ticketOutput.setFont(new Font("Monospaced", Font.PLAIN, 18));
        ticketOutput.setEditable(false);
        ticketOutput.setBackground(new Color(45, 45, 45));
        ticketOutput.setForeground(Color.WHITE);
        ticketOutput.setMargin(new Insets(10, 10, 10, 10));
        ticketPanel.add(new JScrollPane(ticketOutput), BorderLayout.CENTER);

        JButton back = new JButton("Powrót do początku");
        back.setFont(new Font("Arial", Font.BOLD, 18));
        back.setBackground(new Color(60, 60, 60));
        back.setForeground(Color.WHITE);
        back.setFocusPainted(false);
        back.addActionListener(e -> showPanel("payment"));
        JPanel bottom = new JPanel();
        bottom.setBackground(new Color(30, 30, 30));
        bottom.add(back);
        ticketPanel.add(bottom, BorderLayout.SOUTH);
    }

    private void validateAndGenerateTicket() {
        paymentSelector.setSelectedItem(selectedPayment);

        String reg = regInput.getText().trim();
        if (reg.isEmpty()) {
            showError("Wpisz numer rejestracyjny");
            return;
        }

        String input = amountInput.getText().trim();
        if (input.isEmpty()) {
            showError("Podaj kwotę");
            return;
        }

        double paid;
        try {
            paid = Double.parseDouble(input);
        } catch (NumberFormatException e) {
            showError("Podaj poprawną liczbę");
            return;
        }

        int minutes = (int) ((paid / RATE_PER_HOUR) * 60);
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, minutes);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");

        String ticketText =
                "==============================\n" +
                        "       BILET PARKINGOWY\n" +
                        "==============================\n" +
                        "Rejestracja: " + reg + "\n" +
                        "Forma płatności: " + selectedPayment + "\n" +
                        String.format("Kwota: %.2f zł\n", paid) +
                        "Czas: " + minutes + " minut\n" +
                        "Start: " + sdf.format(now) + "\n" +
                        "Ważny do: " + sdf.format(cal.getTime()) + "\n" +
                        "==============================\n";

        ticketOutput.setText(ticketText);
        showPanel("ticket");
    }

    private void showError(String msg) {
        UIManager.put("OptionPane.background", new Color(45, 45, 45));
        UIManager.put("Panel.background", new Color(45, 45, 45));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        JOptionPane.showMessageDialog(this, msg, "Błąd", JOptionPane.ERROR_MESSAGE);
    }

    private void showPanel(String name) {
        CardLayout cl = (CardLayout) getContentPane().getLayout();
        cl.show(getContentPane(), name);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new parkomat().setVisible(true));
    }
}
