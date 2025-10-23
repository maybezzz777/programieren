import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class Client {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String username;
    private DefaultListModel<String> userListModel;
    private JTextPane messageArea;
    private JList<String> userList;
    private JTextField messageField;
    private JTextField nameField;
    private JButton loginButton;

    private final Color bgColor = new Color(25, 25, 25);
    private final Color panelColor = new Color(40, 40, 40);
    private final Color textColor = new Color(230, 230, 230);
    private final Color accentColor = new Color(0, 120, 215);
    private final Color joinColor = new Color(0, 180, 0);
    private final Color leaveColor = new Color(200, 60, 60);
    private final Color pvColor = new Color(80, 160, 255);

    private JFrame frame;
    private JPanel loginPanel;
    private JPanel chatPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new Client().initUI();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void initUI() throws IOException {
        frame = new JFrame("Czat");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 650);
        frame.getContentPane().setBackground(bgColor);
        frame.setLayout(new CardLayout());

        socket = new Socket("localhost", 12345);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        createLoginPanel();
        frame.add(loginPanel, "login");
        frame.setVisible(true);
    }

    private void createLoginPanel() {
        loginPanel = new JPanel();
        loginPanel.setBackground(bgColor);
        loginPanel.setLayout(new GridBagLayout());

        JLabel title = new JLabel("Witaj w czacie!");
        title.setForeground(textColor);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));

        JLabel prompt = new JLabel("Podaj swój nick:");
        prompt.setForeground(textColor);
        prompt.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        nameField = new JTextField(15);
        nameField.setBackground(panelColor);
        nameField.setForeground(textColor);
        nameField.setCaretColor(textColor);
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        nameField.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        loginButton = new JButton("Dołącz");
        loginButton.setBackground(accentColor);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(e -> attemptLogin());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        loginPanel.add(title, gbc);
        gbc.gridy++;
        loginPanel.add(prompt, gbc);
        gbc.gridy++;
        loginPanel.add(nameField, gbc);
        gbc.gridy++;
        loginPanel.add(loginButton, gbc);
    }

    private void attemptLogin() {
        try {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Nick nie może być pusty!");
                return;
            }

            out.println(name);
            String response = in.readLine();
            if ("NICK_TAKEN".equals(response)) {
                JOptionPane.showMessageDialog(frame, "Ten nick jest już zajęty. Wybierz inny.");
            } else if ("NICK_OK".equals(response)) {
                username = name;
                showChatPanel();
                new Thread(this::readMessages).start();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Błąd połączenia z serwerem.");
        }
    }

    private void showChatPanel() {
        chatPanel = new JPanel(new BorderLayout(15, 15));
        chatPanel.setBackground(bgColor);

        messageArea = new JTextPane();
        messageArea.setEditable(false);
        messageArea.setBackground(panelColor);
        messageArea.setForeground(textColor);
        messageArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageArea.setMargin(new Insets(10, 10, 10, 10));
        messageArea.setEditorKit(new javax.swing.text.StyledEditorKit()); // zawijanie tekstu

        JScrollPane chatScroll = new JScrollPane(messageArea);
        chatScroll.setBorder(null);
        chatScroll.getViewport().setBackground(panelColor);
        chatPanel.add(chatScroll, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.setBackground(panelColor);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel usersLabel = new JLabel("Aktywni użytkownicy");
        usersLabel.setForeground(textColor);
        usersLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        usersLabel.setHorizontalAlignment(SwingConstants.CENTER);
        rightPanel.add(usersLabel, BorderLayout.NORTH);

        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        userList.setBackground(new Color(45, 45, 45));
        userList.setForeground(textColor);
        userList.setSelectionBackground(accentColor);
        userList.setSelectionForeground(Color.WHITE);
        userList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        userList.setBorder(null);

        JScrollPane userScroll = new JScrollPane(userList);
        userScroll.setPreferredSize(new Dimension(180, 0));
        userScroll.setBorder(null);
        rightPanel.add(userScroll, BorderLayout.CENTER);
        chatPanel.add(rightPanel, BorderLayout.EAST);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(panelColor);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        messageField = new JTextField();
        messageField.setBackground(new Color(50, 50, 50));
        messageField.setForeground(textColor);
        messageField.setCaretColor(textColor);
        messageField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageField.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JButton sendButton = new JButton("Wyślij");
        sendButton.setBackground(accentColor);
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        sendButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sendButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);
        chatPanel.add(bottomPanel, BorderLayout.SOUTH);

        ActionListener sendAction = e -> {
            String message = messageField.getText().trim();
            if (!message.isEmpty()) {
                out.println(message);
                messageField.setText("");
            }
        };
        sendButton.addActionListener(sendAction);
        messageField.addActionListener(sendAction);

        userList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selectedUser = userList.getSelectedValue();
                    if (selectedUser != null && !selectedUser.equals(username)) {
                        messageField.setText("/pv " + selectedUser + " ");
                        messageField.requestFocus();
                    }
                }
            }
        });

        frame.getContentPane().removeAll();
        frame.add(chatPanel);
        frame.revalidate();
        frame.repaint();
    }

    private void readMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                if (message.startsWith("Dostępni użytkownicy:")) {
                    StringBuilder fullList = new StringBuilder(message + "\n");
                    String line;
                    while (in.ready() && (line = in.readLine()) != null && !line.isEmpty()) {
                        fullList.append(line).append("\n");
                    }
                    updateUserList(fullList.toString());
                } else {
                    final String msg = message;
                    SwingUtilities.invokeLater(() -> appendColoredMessage(msg));
                }
            }
        } catch (IOException e) {
            SwingUtilities.invokeLater(() ->
                appendColoredMessage("Utracono połączenie z serwerem.")
            );
        }
    }

    private void appendColoredMessage(String msg) {
    StyledDocument doc = messageArea.getStyledDocument();
    SimpleAttributeSet style = new SimpleAttributeSet();

    StyleConstants.setForeground(style, textColor);

    if (msg.contains("dołączył do czatu")) {
        StyleConstants.setForeground(style, joinColor);
    } else if (msg.contains("opuścił czat")) {
        StyleConstants.setForeground(style, leaveColor); 
    } else if (msg.toLowerCase().startsWith("[pv") || msg.toLowerCase().contains("(priv)")) {
        StyleConstants.setForeground(style, pvColor);
    }

    try {
        doc.insertString(doc.getLength(), msg + "\n", style);
        messageArea.setCaretPosition(doc.getLength());
    } catch (Exception e) {
        e.printStackTrace();
    }
}


    private void updateUserList(String message) {
        SwingUtilities.invokeLater(() -> {
            userListModel.clear();
            String[] lines = message.split("\n");
            for (String user : lines) {
                if (!user.equals("Dostępni użytkownicy:") && !user.trim().isEmpty()) {
                    userListModel.addElement(user.trim());
                }
            }
            userList.setCellRenderer(new UserListRenderer(username));
        });
    }

    private static class UserListRenderer extends DefaultListCellRenderer {
        private final String currentUser;

        public UserListRenderer(String currentUser) {
            this.currentUser = currentUser;
        }

        @Override
        public Component getListCellRendererComponent(
                JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setOpaque(true);
            String user = value.toString();
            if (user.equals(currentUser)) {
                label.setBackground(new Color(70, 70, 70));
                label.setForeground(new Color(0, 200, 255));
                label.setFont(label.getFont().deriveFont(Font.BOLD));
            } else {
                label.setBackground(new Color(45, 45, 45));
                label.setForeground(Color.WHITE);
            }
            return label;
        }
    }
}
