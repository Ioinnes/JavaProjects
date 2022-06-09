import auxiliary.Connection;
import auxiliary.ConsoleHelper;
import auxiliary.Message;
import auxiliary.MessageType;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {

    private static Map<String, Connection> connectionMap = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        ConsoleHelper.writeMessage("Введите порт сервера:");
        int port = ConsoleHelper.readInt();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            ConsoleHelper.writeMessage("Чат сервер запущен.");
            while (true) {
                Socket socket = serverSocket.accept();
                new Handler(socket).start();
            }
        } catch (Exception e) {
            ConsoleHelper.writeMessage("Произошла ошибка при запуске или работе сервера.");
        }
    }

    private static class Handler extends Thread {
        private Socket socket;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            String newUser = null;
            ConsoleHelper.writeMessage("Установлено новое соединение с " + socket.getRemoteSocketAddress());
            try (Connection connection = new Connection(socket)) {

                newUser = serverHandshake(connection);
                sendBroadcastMessage(new Message(MessageType.USER_ADDED, newUser));
                notifyUsers(connection, newUser);
                serverMainLoop(connection, newUser);

            } catch (IOException | ClassNotFoundException e) {
                ConsoleHelper.writeMessage("Ошибка при обмене данными с " + socket.getRemoteSocketAddress());
            } finally {
                if (newUser != null) {
                    connectionMap.remove(newUser);
                    sendBroadcastMessage(new Message(MessageType.USER_REMOVED, newUser));
                }
            }

        }

        private String serverHandshake(Connection connection) throws IOException, ClassNotFoundException {
            while (true) {
                connection.send(new Message(MessageType.NAME_REQUEST));
                Message request = connection.receive();
                if (request.getType() == MessageType.USER_NAME &&
                        request.getData() != null &&
                        !connectionMap.containsKey(request.getData()) &&
                        !request.getData().equals("")) {
                    connectionMap.put(request.getData(), connection);
                    connection.send(new Message(MessageType.NAME_ACCEPTED));
                    return request.getData();
                }
            }
        }

        private void notifyUsers(Connection connection, String userName) throws IOException {
            for (Map.Entry<String, Connection> client : connectionMap.entrySet()) {
                if (!client.getKey().equals(userName))
                    connection.send(new Message(MessageType.USER_ADDED, client.getKey()));

            }
        }

        private void serverMainLoop(Connection connection, String userName) throws IOException, ClassNotFoundException {
            while (true) {
                Message message = connection.receive();
                if (message.getType() == MessageType.TEXT)
                    sendBroadcastMessage(new Message(MessageType.TEXT, userName + ": " + message.getData()));
                else {
                    ConsoleHelper.writeMessage("Ошибка: принятое сообщение не является текстом");
                }
            }
        }
    }

    public static void sendBroadcastMessage(Message message) {
        for (Map.Entry<String, Connection> client : connectionMap.entrySet()) {
            try {
                client.getValue().send(message);
            } catch (IOException e) {
                ConsoleHelper.writeMessage("Не удалось отправить сообщение");
            }
        }
    }
    
}
