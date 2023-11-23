import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private static final String serverAddress = "localhost";
    private static final int serverPort = 8080;

    public static void main(String[] args) {
        try(Socket socket = new Socket(serverAddress, serverPort)){
            System.out.println("Connected to server...");

            BufferedReader inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter outputWriter = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Enter your name: ");
            outputWriter.println(consoleReader.readLine());

            Thread inputThread = new Thread(() -> {
                try{
                    String serverMessage;
                    while((serverMessage = inputReader.readLine()) != null){
                        System.out.println(serverMessage);
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            });
            inputThread.start();
            System.out.println("You can now type your messages. \nType 'exit' to leave the chat...");

            String userInput;
            while((userInput = consoleReader.readLine()) != null){
                outputWriter.println(userInput);
                if (userInput.equalsIgnoreCase("exit")){
                    break;
                }
            }
            inputThread.join();
        }catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
    }
}
