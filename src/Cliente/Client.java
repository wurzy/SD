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

        while(!(input=sc.nextLine()).equals("q")) {
            out.println(input);
            out.flush();
            System.out.println("Server Sent: " + in.readLine());
        }

        socket.shutdownOutput();
        socket.shutdownInput();
        socket.close();
        /*
        try{

            Socket s = new Socket("localhost",12345);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            Menu menu = new Menu();
            Stub stub = new Stub(menu,bw);
            //ClientReader cr = new ClientReader(br, menu);

            //Thread reader = new Thread(cr);
            Thread stubs = new Thread(stub);

            //reader.start();
            stubs.start();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        */

    }
}
