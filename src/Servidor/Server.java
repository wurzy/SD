package Servidor;

// Imports
import Cloud.*;
import java.net.*;
import java.util.*;

public class Server {
    public static void main(String[] args) throws Exception{
        ServerSocket sSocket = new ServerSocket(12345);
        SoundCloud sc = new SoundCloud();
        ArrayList<String> list = new ArrayList<>();

        Collections.addAll(list,"pop","wow","rap");
        Musica musica1 = new Musica("Amazing","Kanye West",2007,list);

        list = new ArrayList<>();
        Collections.addAll(list,"nada","lll");
        Musica musica2 = new Musica("Amazing","Kanye West",2007,list);

        list = new ArrayList<>();
        Collections.addAll(list,"m","wow","rap");
        Musica musica3 = new Musica("Amazing","Kanye West",2007,list);

        list = new ArrayList<>();
        Collections.addAll(list,"w","wow","rap");
        Musica musica4 = new Musica("Amazing","Kanye West",2007,list);

        sc.addMusica(musica1);
        sc.allowMusica(musica1.getId());
        sc.addMusica(musica2);
        sc.allowMusica(musica2.getId());
        sc.addMusica(musica3);
        sc.allowMusica(musica3.getId());
        sc.addMusica(musica4);
        sc.allowMusica(musica4.getId());

        while(true){
            Socket clSock = sSocket.accept();

            Worker worker = new Worker(clSock,sc);

            System.out.println("Assigning new thread to new client");

            Thread parallelClient = new Thread(worker);
            parallelClient.start();
        }
    }
}