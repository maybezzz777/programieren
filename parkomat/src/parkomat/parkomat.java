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
        setSize(900, 550);
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
        UIManager.put("Panel.background", new Color(35, 35, 35));
        UIManager.put("Label.foreground", Color.WHITE);
        UIManager.put("Button.background", new Color(60, 60, 60));
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("TextField.background", new Color(45, 45, 45));
        UIManager.put("TextField.foreground", Color.WHITE);
        UIManager.put("TextArea.background", new Color(40, 40, 40));
        UIManager.put("TextArea.foreground", Color.WHITE);
    }

    private void initPaymentPanel() {
        paymentPanel = new JPanel(new BorderLayout(10, 10));

        JLabel title = new JLabel("Wybierz formę płatności:", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        paymentPanel.add(title, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new GridLayout(3, 1, 15, 15));
        JButton[] payButtons = {
                new JButton("Monety"),
                new JButton("Banknoty"),
                new JButton("Karta")
        };
        for (JButton b : payButtons) {
            b.setFont(new Font("Arial", Font.BOLD, 22));
            b.addActionListener(e -> {
                selectedPayment = b.getText();
                showPanel("main");
            });
            buttons.add(b);
        }
        paymentPanel.add(buttons, BorderLayout.CENTER);
    }

    private void initMainPanel() {
        mainPanel = new JPanel(new BorderLayout(10, 10));

        JPanel inputs = new JPanel(new GridLayout(3, 2, 8, 8));
        inputs.add(new JLabel("Forma płatności:"));
        paymentSelector = new JComboBox<>(new String[]{"Monety", "Banknoty", "Karta"});
        paymentSelector.setEnabled(false);
        inputs.add(paymentSelector);

        inputs.add(new JLabel("Numer rejestracyjny:"));
        regInput = new JTextField();
        regInput.setFont(new Font("Consolas", Font.BOLD, 18));
        regInput.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isLetterOrDigit(c) || regInput.getText().length() >= 7) {
                    e.consume();
                    return;
                }
                e.setKeyChar(Character.toUpperCase(c));
            }
        });
        inputs.add(regInput);

        inputs.add(new JLabel("Kwota (zł):"));
        amountInput = new JTextField();
        amountInput.setFont(new Font("Consolas", Font.BOLD, 18));
        inputs.add(amountInput);
        mainPanel.add(inputs, BorderLayout.NORTH);

        // Główna część z klawiaturą i cennikiem
        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.add(createKeyboardPanel(), BorderLayout.CENTER);

        JPanel pricePanel = new JPanel(new BorderLayout());
        pricePanel.setPreferredSize(new Dimension(180, 150));
        pricePanel.setBorder(BorderFactory.createTitledBorder("Cennik"));
        JLabel priceLabel = new JLabel("<html><center>4 zł / godzina<br>1 zł = 15 minut</center></html>", SwingConstants.CENTER);
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        pricePanel.add(priceLabel, BorderLayout.CENTER);
        center.add(pricePanel, BorderLayout.EAST);

        mainPanel.add(center, BorderLayout.CENTER);

        JButton print = new JButton("Drukuj bilet");
        print.setFont(new Font("Arial", Font.BOLD, 20));
        print.addActionListener(e -> validateAndGenerateTicket());
        mainPanel.add(print, BorderLayout.SOUTH);
    }

    private JPanel createKeyboardPanel() {
        JPanel keyboard = new JPanel();
        keyboard.setLayout(new GridLayout(5, 1, 5, 5));

        String[] rows = {
                "1 2 3 4 5 6 7 8 9 0",
                "Q W E R T Y U I O P",
                "A S D F G H J K L",
                "Z X C V B N M",
                "←"
        };

        for (String row : rows) {
            JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
            for (String key : row.split(" ")) {
                JButton btn = new JButton(key);
                btn.setFont(new Font("Arial", Font.BOLD, 18));
                btn.setPreferredSize(new Dimension(60, 60));
                btn.addActionListener(e -> handleKeyPress(key));
                rowPanel.add(btn);
            }
            keyboard.add(rowPanel);
        }

        return keyboard;
    }

    private void handleKeyPress(String key) {
        if (key.equals("←")) {
            String text = regInput.getText();
            if (!text.isEmpty()) regInput.setText(text.substring(0, text.length() - 1));
        } else if (key.equals("CLEAR")) {
            regInput.setText("");
        } else if (key.matches("[A-Z0-9]") && regInput.getText().length() < 7) {
            regInput.setText(regInput.getText() + key);
        }
    }

    private void initTicketPanel() {
        ticketPanel = new JPanel(new BorderLayout(10, 10));

        JLabel title = new JLabel("Bilet parkingowy", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        ticketPanel.add(title, BorderLayout.NORTH);

        ticketOutput = new JTextArea();
        ticketOutput.setFont(new Font("Monospaced", Font.PLAIN, 18));
        ticketOutput.setEditable(false);
        ticketOutput.setBackground(new Color(45, 45, 45));

        JScrollPane scrollPane = new JScrollPane(ticketOutput);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        ticketPanel.add(scrollPane, BorderLayout.CENTER);

        JButton back = new JButton("Powrót do początku");
        back.setFont(new Font("Arial", Font.BOLD, 18));
        back.addActionListener(e -> showPanel("payment"));
        ticketPanel.add(back, BorderLayout.SOUTH);
    }

    private void validateAndGenerateTicket() {
        paymentSelector.setSelectedItem(selectedPayment);

        String reg = regInput.getText().trim();
        if (reg.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Wpisz numer rejestracyjny");
            return;
        }

        String input = amountInput.getText().trim();
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Podaj kwotę");
            return;
        }

        double paid;
        try {
            paid = Double.parseDouble(input);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Podaj poprawną liczbę");
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

    private void showPanel(String name) {
        CardLayout cl = (CardLayout) getContentPane().getLayout();
        cl.show(getContentPane(), name);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new parkomat().setVisible(true));
    }
}
