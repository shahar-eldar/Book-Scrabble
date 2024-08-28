package test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer {
    private int port;
    private volatile boolean stop;
    private ClientHandler clientHandler; 
    private ServerSocket serverSocket; // Define serverSocket as a field
    private Thread serverThread; // Define serverThread as a field

    public MyServer(int port, ClientHandler clientHandler) {
        this.port = port;
        this.clientHandler = clientHandler;
        this.stop = false;
    }

    public void start() {
        serverThread = new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                while (!stop) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        if (stop) {
                            clientSocket.close();
                            break;
                        }
                        clientHandler.handleClient(clientSocket.getInputStream(), clientSocket.getOutputStream());
                        clientSocket.close(); // no need
                    } catch (IOException e) {
                        if (!stop) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (IOException e) {
                if (!stop) {
                    e.printStackTrace();
                }
            } finally {
                close(); // Ensure resources are cleaned up
            }
        });
        serverThread.start();
    }

    public void close() {
        stop = true; // Signal to stop the server loop

        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close(); // Close the ServerSocket to stop accept()
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (serverThread != null && serverThread.isAlive()) {
            try {
                serverThread.join(1); // Wait for the server thread to finish
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt(); // Restore interrupted status
            }
        }

        clientHandler.close(); // Close the client handler resources
    }
}
