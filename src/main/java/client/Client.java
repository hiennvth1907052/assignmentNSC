package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client implements Runnable{
    private Socket link;
    private PrintWriter outputStream;
    private Scanner inputStream;
    private int port = 8088;

    public Client() throws IOException {
        initialize();
    }
    private void initialize() throws IOException {
        Scanner keyboard = new Scanner(System.in);
        InetAddress host = null;
        try {
            host = InetAddress.getByName(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            System.out.println("Host not found");
        }
        System.out.println(host.getHostAddress() + " says: ");
        link = null;
        try {
            link = new Socket(host, port);
            link.setReuseAddress(true);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("not found");
        }
        inputStream = new Scanner(link.getInputStream());
        outputStream = new PrintWriter(link.getOutputStream());

        Thread t = new Thread(this);
        t.start();
        while (keyboard.hasNextLine()) {
            String msg = keyboard.nextLine();
            outputStream.println(host.getHostAddress() + " says: " + msg);
            outputStream.flush();
        }
    }

    public static void main(String[] args) throws Exception {
        new Client();
    }

    @Override
    public void run() {
        while (true) {
            if (inputStream.hasNextLine())
                System.out.println(inputStream.nextLine());
        }
    }
}
