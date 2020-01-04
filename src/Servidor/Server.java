package Servidor;

// Imports
import Cloud.*;
import java.net.*;
import java.util.*;

public class Server {
    public static void main(String[] args) throws Exception{

        ServerSocket sSocket = new ServerSocket(12345);
        SoundCloud sc = new SoundCloud();

        while(true){
            Socket clSock = sSocket.accept();
            Worker worker = new Worker(clSock,sc);
            System.out.println("CONNECTED: " + clSock.getRemoteSocketAddress());
            Thread parallelClient = new Thread(worker);
            parallelClient.start();
        }
    }
}