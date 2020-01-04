package Cliente;

// Imports
import java.io.*;
import java.net.Socket;
public class Client {

    // Classe que cria um leitor e um escritor para um dado cliente
    public static void main(String[] args) throws Exception{
        Socket socket = new Socket("127.0.0.1",12345);
        
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        Menu menu = new Menu();
        Writer writer = new Writer(menu,bw,socket);
        Reader reader = new Reader(menu,in,socket);

        Thread t_reader = new Thread(reader);
        Thread t_writer = new Thread(writer);

        t_reader.start();
        t_writer.start();
    }
}
