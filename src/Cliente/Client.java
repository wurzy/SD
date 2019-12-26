package Cliente;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client {

    public static void main(String[] args){
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
    }
}
