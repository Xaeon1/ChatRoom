import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.List;

public class Server {
    private static final int port = 8080;
    private static List<ClientHandler> clients = new LinkedList<>();
    public static void main(String[] args) {
        try(ServerSocket server = new ServerSocket(port)){
            while(true){
                ClientHandler clientHandler = new ClientHandler(server.accept());
                System.out.println(clientHandler.getClientName() + " has joined the chatroom...");
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        }catch (IOException  e){
            e.printStackTrace();
        }
    }
    public static void broadcastMessage(String message, ClientHandler sender){
        for (ClientHandler client : clients) {
            if (client != sender){
                client.sendMessage(sender.getClientName() + ": " + message);
            }
        }
    }
    public static void removeClient(ClientHandler client){
        clients.remove(client);
    }
}
