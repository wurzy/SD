package Cliente;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws Exception{
        Socket socket = new Socket("127.0.0.1",12345);
        String input;
        
        Scanner sc = new Scanner(System.in);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        
        try{
            Socket s = new Socket("localhost",12345);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            Menu menu = new Menu();
            Writer writer = new Writer(menu,bw);
            Reader reader = new Reader(menu,br);

            Thread t_reader = new Thread(reader);
            Thread t_writer = new Thread(writer);

            t_reader.start();
            t_writer.start();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

        // Para podermos dar quit do programa a qualquer momento
        while(!(input=sc.nextLine()).equals("q")) {
            out.println(input);
            out.flush();
            System.out.println("Server Sent: " + in.readLine());
        }
        
        socket.shutdownOutput();
        socket.shutdownInput();
        socket.close();
    }
}
