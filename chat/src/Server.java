import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static Set<ClientHandler> clientHandlers = new HashSet<>();
    private static Map<String, ClientHandler> clientsMap = new HashMap<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Serwer uruchomiony na porcie 12345...");

            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String username;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                this.out = new PrintWriter(socket.getOutputStream(), true);
                this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                username = in.readLine();
                clientsMap.put(username, this);
                clientHandlers.add(this);

                sendToAll(username + " dołączył do czatu.");

                String message;
                while ((message = in.readLine()) != null) {
                    if (message.startsWith("/private")) {
                        String[] parts = message.split(" ", 3);
                        if (parts.length == 3 && clientsMap.containsKey(parts[1])) {
                            sendPrivateMessage(parts[1], parts[2]);
                        } else {
                            out.println("Błędny format wiadomości prywatnej.");
                        }
                    } else {
                        sendToAll(username + ": " + message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientsMap.remove(username);
                    clientHandlers.remove(this);
                    socket.close();
                    sendToAll(username + " opuścił czat.");
                    sendUserList();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void sendToAll(String message) {
            for (ClientHandler clientHandler : clientHandlers) {
                clientHandler.out.println(message);
            }
        }

        private void sendPrivateMessage(String recipient, String message) {
            ClientHandler recipientHandler = clientsMap.get(recipient);
            if (recipientHandler != null) {
                recipientHandler.out.println("[Prywatna wiadomość od " + username + "]: " + message);
            }
        }

        private void sendUserList() {
            StringBuilder userList = new StringBuilder("Dostępni użytkownicy:\n");
            for (String client : clientsMap.keySet()) {
                userList.append(client).append("\n");
            }
            out.println(userList.toString());
        }
    }
}
