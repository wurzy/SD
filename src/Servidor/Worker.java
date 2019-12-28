package Servidor;
import Cloud.Musica;
import Cloud.SoundCloud;

import java.io.*;
import java.lang.reflect.Array;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Worker implements Runnable {
    private BufferedReader in;
    private PrintWriter out;
   // private DataInputStream clientData;
    private Socket s;

    private SoundCloud app;
    private final String temp = "C:\\Users\\User\\AppData\\Local\\Temp\\SoundCloud\\servidor\\";
    private final String temp2 = "C:\\Users\\User\\AppData\\Local\\Temp\\SoundCloud\\cliente\\";
    private final int MAXSIZE = 500*1024; // 500kb max

    public Worker(Socket s, SoundCloud sc) throws IOException {
        this.in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        this.out = new PrintWriter(s.getOutputStream());
        this.s = s;
        //this.clientData = new DataInputStream(s.getInputStream());
        this.app = sc;
    }

    public void run() {
        String input;
        while(true) {
            try {
                //byte[] mybytearray = new byte[MAXSIZE+50];
                //byte[] comando = Arrays.copyOfRange(mybytearray,0,20);
                System.out.println("Estou a espera de input agora");
                input=in.readLine();
                //InputStream is = s.getInputStream();
                //int bytesRead = is.read(mybytearray, 0, mybytearray.length);
                if(input == null || input.equals("q")) {
                    s.shutdownInput();
                    s.shutdownOutput();
                    //clientData.close();
                    s.close();
                    break;
                }
                System.out.println(input);
                parse(input);
                //upload(input,s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void parse(String input){
        String[] partes = input.split("-",2);
        switch(partes[0]){
            case "LOGIN":
                login(partes[1]);
                break;
            case "REGISTER":
                register(partes[1]);
                break;
            case "UPLOAD":
                //out.println("ok");
                //out.flush();
                receiveFile(partes[1]);
                break;
            case "SEARCH":
                search(partes[1]);
                break;
            default:
                //ArrayList<String> list = new ArrayList<>();
                //Collections.addAll(list,"pop","wow","rap");
                //Musica musica = new Musica("Amazing","Kanye West",2007,list);
                //app.addMusica(musica);
                //System.out.println(app.getAllMusicas().toString());
                //out.println(app.getAllMusicas().toString());
                out.println("Invalido");
                out.flush();

                break;
        }
    }
    private void login(String s){
        String[] auth = s.split(",");
        boolean b = app.login(auth[0],auth[1]);
        if(b) {
            System.out.println("Ok login");
            out.println("LOGGEDIN");
        }
        else{
            System.out.println("Not ok login");
            out.println("DENIED");
        }
        //out.println(b);
        out.flush();
    }

    private void register(String s){
        String[] reg = s.split(",");
        boolean br = app.register(reg[0],reg[1]);
        //System.out.println("Not ok reg");
        if(!br) {
            System.out.println("Not ok reg");
            out.println("DENIED");
        }
        else {
            System.out.println("Ok reg");
            out.println("SIGNEDUP");
        }
        //out.println(br);
        out.flush();
    }

    private void search(String s) {
        ArrayList<String> search = new ArrayList<>();
        String[] tags = s.split(",");
        for(String tag: tags){
            search.add(tag);
        }
        Set<Musica> musicas = app.filterTag(search);
        out.println(musicas);
        out.flush();
    }

    private void createTempDirectory(){
        Path path1 = Paths.get(temp);
        Path path2 = Paths.get(temp2);
        if (!Files.exists(path1)) {
            try {
                Files.createDirectories(path1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!Files.exists(path2)) {
            try {
                Files.createDirectories(path2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        out.println("Existe o path");
        out.flush();
    }

    private void receiveFile(String input){
        //System.out.println("tou no receive");
        createTempDirectory();
        try {
            int bytesRead;
            DataInputStream clientData = new DataInputStream(s.getInputStream());

            String fileName = clientData.readUTF();
            //fileName = clientData.readUTF();

            System.out.println(fileName + " foi o nome");
            String partes[] = fileName.split(",",4);
            String tags[] = partes[3].split("%");
            ArrayList<String> tagsA = new ArrayList<>();
            for (String tag : tags) {
                tagsA.add(tag);
            }

            int id = app.addMusica(new Musica(partes[0],partes[1],Integer.valueOf(partes[2]),tagsA));

            String nome = temp+id+".mp3";

            OutputStream output = new FileOutputStream(nome);
            long size = clientData.readLong();
            //System.out.println(size + " foi o size");
            byte[] buffer = new byte[MAXSIZE];
            while (size > 0 && (bytesRead = clientData.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                output.write(buffer, 0, bytesRead);
                size -= bytesRead;
            }

            output.close();
            //clientData.close();
            System.out.println("Cheguei ao fim");
            out.println("Uploaded");
            out.flush();
            //System.out.println("File "+fileName+" received from client.");
            System.out.println(app.getAllMusicas());
        } catch (IOException ex) {
            System.err.println("Client error. Connection closed.");
        }
    }

/*
    private void sendFile(String fileName){
        try {
            //handle file read
            File myFile = new File(fileName);
            byte[] mybytearray = new byte[(int) myFile.length()];

            FileInputStream fis = new FileInputStream(myFile);
            BufferedInputStream bis = new BufferedInputStream(fis);
            //bis.read(mybytearray, 0, mybytearray.length);

            DataInputStream dis = new DataInputStream(bis);
            dis.readFully(mybytearray, 0, mybytearray.length);

            //handle file send over socket
            OutputStream os = s.getOutputStream();

            //Sending file name and file size to the server
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeUTF(myFile.getName());
            dos.writeLong(mybytearray.length);
            dos.write(mybytearray, 0, mybytearray.length);
            dos.flush();
            System.out.println("File "+fileName+" sent to client.");
        } catch (Exception e) {
            System.err.println("File does not exist!");
        }
    }
    */

}
