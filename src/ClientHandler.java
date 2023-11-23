import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
public class ClientHandler implements Runnable{
    private final Socket clientSocket;
    private String clientName;
    private BufferedReader reader;
    private PrintWriter writer;
    public ClientHandler(Socket socket){
        this.clientSocket = socket;
        try {
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);
            this.clientName = reader.readLine();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public String getClientName() {
        return clientName;
    }
    public void sendMessage(String message){
        writer.println(message);
    }

    @Override
    public void run() {
        String clientMessage;
        try {
            System.out.println("Connected from " + clientSocket.getInetAddress().getHostAddress());
            while((clientMessage = reader.readLine()) != null){
                if (clientMessage.equalsIgnoreCase("exit")){
                    break;
                }
                Server.broadcastMessage(clientMessage, this);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                reader.close();
                writer.close();
                clientSocket.close();
            }catch (IOException e){
                e.printStackTrace();
            }
            Server.removeClient(this);
            Server.broadcastMessage(clientName + " has left the chat.", this);
        }
    }
}
