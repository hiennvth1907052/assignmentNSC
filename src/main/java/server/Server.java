package server;

import client.ClientHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class Server {
    private final int port = 8088;
    private ServerSocket serverSocket;
    private ArrayList<Socket> clientList;

    public Server() {
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);
        } catch (IOException e)
        {
            System.out.println(e.getStackTrace());
        }
        clientList = new ArrayList<Socket>();
    }

    public void startServer() throws IOException {
        System.out.println("Accepting clients...");
        while(true)
        {
            Socket client = serverSocket.accept();
            clientList.add(client);
            System.out.println("New client accepted..." + client.getRemoteSocketAddress());
            System.out.println("Total users: " + clientList.size());
            ClientHandler handler = new ClientHandler(client,this);
            Thread thread = new Thread(handler);
            thread.start();
        }
    }

    public synchronized void sendChatMessageToAll(String msg) throws IOException {
        for(Iterator<Socket> it = clientList.iterator(); it.hasNext();)
        {
            Socket client = it.next();
            if( !client.isClosed() )
            {
                PrintWriter pw = new PrintWriter(client.getOutputStream());
                pw.println(msg);
                pw.flush();
            }
        }
    }
    public static void main(String[] args) throws IOException {
        new Server().startServer();
    }
}
