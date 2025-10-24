import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static Set<ClientHandler> clientHandlers = new HashSet<>();
    private static Map<String, ClientHandler> clientsMap = new HashMap<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(2974)) {
            System.out.println("Serwer dziala na porcie 2974");

            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                new Thread(clientHandler).start();
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
                while (true) {
                    username = in.readLine();
                    if (username == null) return;

                    synchronized (clientsMap) {
                        if (clientsMap.containsKey(username)) {
                            out.println("NICK_TAKEN");
                        } else {
                            clientsMap.put(username, this);
                            clientHandlers.add(this);
                            out.println("NICK_OK");
                            break;
                        }
                    }
                }

                sendUserList();
                sendToAll(username + " dołączył do czatu.");

                String message;
                while ((message = in.readLine()) != null) {
                    if (message.startsWith("/pv ")) {
                        handlePrivateMessage(message);
                    } else {
                        sendToAll(username + ": " + message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (username != null) {
                        clientsMap.remove(username);
                        clientHandlers.remove(this);
                        sendToAll(username + " opuścił czat.");
                        sendUserList();
                    }
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void handlePrivateMessage(String message) {
            String pvCommand = message.substring(4).trim();
            String regex = "['\"]([^'\"]+)['\"]\\s+(.+)";
            java.util.regex.Matcher matcher = java.util.regex.Pattern.compile(regex).matcher(pvCommand);

            if (matcher.matches()) {
                String recipient = matcher.group(1).trim();
                String msg = matcher.group(2).trim();

                if (recipient.equals(username)) {
                    out.println("Nie możesz wysyłać wiadomości prywatnych do samego siebie!");
                    return;
                }

                if (clientsMap.containsKey(recipient)) {
                    sendPrivateMessage(recipient, msg);
                } else {
                    out.println("Użytkownik '" + recipient + "' nie jest dostępny.");
                }
            } else {
                out.println("Niepoprawny format. Użyj: /pv 'nazwa' wiadomość");
            }
        }

        private void sendToAll(String message) {
            synchronized (clientHandlers) {
                for (ClientHandler client : clientHandlers) {
                    client.out.println(message);
                }
            }
        }

        private void sendPrivateMessage(String recipient, String message) {
            ClientHandler recipientHandler = clientsMap.get(recipient);
            if (recipientHandler != null) {
                recipientHandler.out.println("[PV od " + username + "]: " + message);
                out.println("[PV do " + recipient + "]: " + message);
            }
        }

        private void sendUserList() {
            StringBuilder userList = new StringBuilder("Dostępni użytkownicy:\n");
            for (String client : clientsMap.keySet()) {
                userList.append(client).append("\n");
            }
            for (ClientHandler client : clientHandlers) {
                client.out.println(userList.toString());
            }
        }
    }
}
