import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Parkomat extends JFrame {

    private JTextField rejestracjaField, kwotaField;
    private JTextArea wynikArea;
    private JButton drukujButton;
    private final double cenaZaGodzine = 1.0; // stała cena za godzinę
    private final DateTimeFormatter czasFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public Parkomat() {
        setTitle("Symulator Parkometru");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10,10));

        // Panel górny z danymi
        JPanel gornyPanel = new JPanel(new GridBagLayout());
        gornyPanel.setBorder(BorderFactory.createTitledBorder("Dane parkowania"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.anchor = GridBagConstraints.WEST;

        // Cena (nieedytowalne)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gornyPanel.add(new JLabel("Cena za godzinę (PLN):"), gbc);

        JTextField cenaField = new JTextField(String.format("%.2f", cenaZaGodzine),6);
        cenaField.setEditable(false);
        gbc.gridx = 1;
        gornyPanel.add(cenaField, gbc);

        // Numer rejestracyjny
        gbc.gridx = 0;
        gbc.gridy = 1;
        gornyPanel.add(new JLabel("Numer rejestracyjny:"), gbc);

        rejestracjaField = new JTextField(10);
        gbc.gridx = 1;
        gornyPanel.add(rejestracjaField, gbc);

        // Kwota wpłacona
        gbc.gridx = 0;
        gbc.gridy = 2;
        gornyPanel.add(new JLabel("Kwota wpłacona (PLN):"), gbc);

        kwotaField = new JTextField(10);
        gbc.gridx = 1;
        gornyPanel.add(kwotaField, gbc);

        add(gornyPanel, BorderLayout.NORTH);

        // Pole tekstowe na bilet
        wynikArea = new JTextArea(12, 35);
        wynikArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        wynikArea.setEditable(false);
        wynikArea.setBorder(BorderFactory.createTitledBorder("Bilet parkingowy"));
        JScrollPane scrollPane = new JScrollPane(wynikArea);
        add(scrollPane, BorderLayout.CENTER);

        // Przycisk
        drukujButton = new JButton("Drukuj bilet");
        JPanel dolnyPanel = new JPanel();
        dolnyPanel.add(drukujButton);
        add(dolnyPanel, BorderLayout.SOUTH);

        drukujButton.addActionListener(e -> drukujBilet());

        setVisible(true);
    }

    private void drukujBilet() {
        wynikArea.setText("");
        String rejestracja = rejestracjaField.getText().trim();

        if (rejestracja.isEmpty()) {
            wynikArea.setText("Błąd: Proszę podać numer rejestracyjny pojazdu.");
            return;
        }

        try {
            double wplata = Double.parseDouble(kwotaField.getText().replace(',', '.'));

            if (wplata <= 0) {
                wynikArea.setText("Błąd: Kwota wpłacona musi być większa niż 0.");
                return;
            }

            LocalTime start = LocalTime.now();

            // Oblicz ile godzin można kupić za podaną kwotę (z dokładnością do minut)
            double maxCzas = wplata / cenaZaGodzine; // w godzinach

            int minuty = (int) Math.floor(maxCzas * 60);

            if (minuty == 0) {
                wynikArea.setText("Wpłacona kwota jest zbyt niska na zakup minimalnego czasu parkowania.");
                return;
            }

            LocalTime koniec = start.plusMinutes(minuty);

            double koszt = ((double) minuty / 60) * cenaZaGodzine;
            double reszta = wplata - koszt;

            // Budowanie biletu
            StringBuilder bilet = new StringBuilder();
            bilet.append("======================================\n");
            bilet.append("           BILET PARKINGOWY           \n");
            bilet.append("======================================\n");
            bilet.append(String.format("Rejestracja: %-25s\n", rejestracja));
            bilet.append(String.format("Godzina rozpoczęcia: %s\n", start.format(czasFormatter)));
            bilet.append(String.format("Godzina zakończenia:  %s\n", koniec.format(czasFormatter)));
            bilet.append(String.format("Cena za godzinę:     %.2f PLN\n", cenaZaGodzine));
            bilet.append(String.format("Kwota wpłacona:      %.2f PLN\n", wplata));
            bilet.append(String.format("Koszt parkowania:    %.2f PLN\n", koszt));
            bilet.append(String.format("Reszta:              %.2f PLN\n", reszta));
            bilet.append("======================================\n");
            bilet.append("   Dziękujemy za skorzystanie!       \n");
            bilet.append("======================================");

            wynikArea.setText(bilet.toString());

        } catch (NumberFormatException ex) {
            wynikArea.setText("Błąd: Proszę podać poprawną kwotę wpłaconą.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Parkomat());
    }
}
