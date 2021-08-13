package client;

import server.Server;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable {
    private Socket client;
    private Server server;
    private Scanner inputStream;

    public ClientHandler(Socket client, Server server) {
        this.client = client;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            inputStream = new Scanner(client.getInputStream());
            while(true)
            {
                if(!inputStream.hasNext())
                    return;
                String chatLine = inputStream.nextLine();
                System.out.println(client.getRemoteSocketAddress() + " said: " + chatLine);
                server.sendChatMessageToAll(chatLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
