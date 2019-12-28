package Cliente;

// Imports
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import Cliente.Menu.State;

public class Reader implements Runnable{
    //private static final String RESET = "\u001B[0m";

    // Variáveis de Instância
    private final String temp2 = "C:\\Users\\User\\AppData\\Local\\Temp\\SoundCloud\\cliente\\";
    private final int MAXSIZE = 500*1024;
    private Menu menu;
    private BufferedReader input;
    private Socket sock;

    Reader(Menu menu, BufferedReader input,Socket s) {
        this.input = input;
        this.menu = menu;
        this.sock = s;
    }

    // Está constantemente a ler do "BufferedReader" e ...
    public void run() {
        String s;
        System.out.println("runReader");
        try {
            while ((s= input.readLine()) != null) {
                //s = input.readLine();
                parseResponse(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ... dependendo do input (resposta do "Server"), muda o "state" da classe "Menu"
    private synchronized void parseResponse(String response){
        //String[] info = response.split(" ", 2);
        switch (/*info[0].toUpperCase()*/response) {
            case("DENIED"):
                System.out.println("Username/Password inválidas" /*+ RESET*/);
                menu.show();
                break;
            case ("LOGGEDIN"):
                menu.setState(State.LOGGED);
                menu.show();
                break;
            case("SIGNEDUP"):
                System.out.println("Hello world");
                menu.setState(State.NOTLOGGED);
                menu.show();
                break;
            case("DOWNLOADING"):
                System.out.println("Downloading");
                menu.setState(State.DOWNLOADING);
                recebeFicheiro(sock,"1");
                menu.show();
            default:
                //System.out.println("I fucked up");
                //menu.show();
        }
    }
    private void createTempDirectory(){
        Path path2 = Paths.get(temp2);
        if (!Files.exists(path2)) {
            try {
                Files.createDirectories(path2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void recebeFicheiro(Socket sock, String fileName) {
        createTempDirectory();
        try {
            int bytesRead;
            //Scanner sc = new Scanner(System.in);
            System.out.println("Written");
            InputStream in = sock.getInputStream();

            DataInputStream clientData = new DataInputStream(in);

            fileName = clientData.readUTF();
            System.out.println(fileName);
            OutputStream output = new FileOutputStream((temp2 + fileName + ".mp3"));
            long size = clientData.readLong();
            System.out.println(size);
            byte[] buffer = new byte[MAXSIZE];

            while (size > 0 && (bytesRead = clientData.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                output.write(buffer, 0, bytesRead);
                size -= bytesRead;
            }

            output.close();
            //in.close();

            System.out.println("File "+fileName+" received from Server.");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
