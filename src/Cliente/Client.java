package Cliente;

// Imports
import java.io.*;
import java.net.Socket;
public class Client {

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
        //menu.show();
        
        //while(!(input=sc.nextLine()).equals("q")) {
            //out.println(input);
            //enviarFicheiro(".\\effects\\bruh.mp3",out);
            //out.println("UPLOAD-");
            //out.println("filename");
            //out.flush();
            //enviarFicheiro(socket,"Amazing,Kanye,1999,ola%ola%ola%ola.mp3");
            //System.out.println("Server Sent: " + in.readLine());
        //}
        
        //socket.shutdownOutput();
        //socket.shutdownInput();
        //socket.close();
        /*
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
/*
    private static void enviarFicheiro(Socket s, String campos){
            try {
                //System.err.print("Enter file name: ");
                String fileName = ".\\effects\\bruh.mp3"; // substituir aqui eventualmente

                File myFile = new File(fileName);
                byte[] mybytearray = new byte[(int) myFile.length()];

                FileInputStream fis = new FileInputStream(myFile);
                BufferedInputStream bis = new BufferedInputStream(fis);
                //bis.read(mybytearray, 0, mybytearray.length);

                DataInputStream dis = new DataInputStream(bis);
                dis.readFully(mybytearray, 0, mybytearray.length);

                OutputStream os = s.getOutputStream();

                //Sending file name and file size to the server
                DataOutputStream dos = new DataOutputStream(os);
                //dos.writeUTF(myFile.getName());
                dos.writeUTF(campos);
                dos.writeLong(mybytearray.length);
                dos.write(mybytearray, 0, mybytearray.length);
                dos.flush();
                System.out.println("File "+fileName+" sent to Server.");
            } catch (Exception e) {
                System.err.println("File does not exist!");
            }
            */
    }


}
