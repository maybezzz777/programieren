import javax.swing.*;
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
    private JTextArea messageArea;
    private JList<String> userList;
    private JTextField messageField;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new Client().start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void start() throws IOException {
        socket = new Socket("localhost", 12345);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        JFrame frame = new JFrame("Czat");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        String login = JOptionPane.showInputDialog(frame, "Podaj swoje imię:");
        if (login != null && !login.trim().isEmpty()) {
            username = login;
            out.println(username);
        }

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        frame.add(new JScrollPane(messageArea), BorderLayout.CENTER);

        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        frame.add(new JScrollPane(userList), BorderLayout.EAST);

        messageField = new JTextField();
        messageField.addActionListener(this::sendMessage);
        frame.add(messageField, BorderLayout.SOUTH);

        frame.setVisible(true);

        new Thread(this::readMessages).start();
    }

    private void readMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                if (message.startsWith("Dostępni użytkownicy:")) {
                    updateUserList(message); 
                } else {
                    messageArea.append(message + "\n");  
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(ActionEvent e) {
        String message = messageField.getText();
        if (message.startsWith("/private")) {
            out.println(message); 
        } else {
            out.println(message); 
        }
        messageField.setText(""); 
    }

    private void updateUserList(String message) {
        String[] users = message.split("\n");
        userListModel.clear();
        for (String user : users) {
            if (!user.equals("Dostępni użytkownicy:")) {
                userListModel.addElement(user);
            }
        }
    }
}
