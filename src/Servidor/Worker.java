package Servidor;

// Imports
import Cloud.*;
import java.io.*;
import java.net.Socket;
import java.nio.file.*;
import java.util.*;

public class Worker implements Runnable {

    // Variáveis de Instância
    //private final String temp = "C:\\Users\\User\\AppData\\Local\\Temp\\SoundCloud\\servidor\\"; //Windows
    private final String temp = "/tmp/SoundCloud/servidor/";     // Linux
    private final int MAXSIZE = 500*1024; // 500kb max
    private BufferedReader in;
    private PrintWriter out;
    private Socket s;
    private SoundCloud app;
    // private DataInputStream clientData;

    // Construtor
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void parse(String input){
        String[] partes = input.split("-",2);
        System.out.println("Entrei no parse :)");
        switch(partes[0]){
            case "LOGIN":
                System.out.println("Entrei no login :)");
                login(partes[1]);
                break;
            case "LOGOUT":
                System.out.println("Entrei no logout :)");
                logout(partes[1]);
                break;
            case "REGISTER":
                System.out.println("Entrei no reg :)");
                register(partes[1]);
                break;
            case "UPLOAD":
                System.out.println("Entrei no upl:)");
                receiveFile(partes[1]);
                break;
            case "DOWNLOAD":
                System.out.println("Entrei no dl :)");
                sendFile(partes[1]);
                break;
            case "SEARCH":
                System.out.println("Entrei no search:)");
                search(partes[1]);
                break;
            case "LEAVE":
                leave();
                break;
            default:
                System.out.println("Entrei no careices :)");
                break;
        }
    }

    // Login: vai mandar para o "Client" se o login feito foi um sucesso ou não (username/password certas ou não)
    private void login(String s){
        String[] auth = s.split(",");
        boolean b = app.login(auth[0],auth[1]);
        if(b) {
            out.println("LOGGEDIN-"+auth[0]);
            this.app.insertLogged(auth[0],this);
        } else
            out.println("DENIED_1-");
        out.flush();
    }

     // Logout
    private void logout(String s){
        this.app.removeLogged(s);
    }

    // Signup: vai mandar para o "Client" se o signup feito foi um sucesso ou não (username já existia ou não)
    private void register(String s){
        String[] reg = s.split(",");
        boolean br = app.register(reg[0],reg[1]);
        if(br)
             out.println("SIGNEDUP-");
        else 
             out.println("DENIED_2-");  
        out.flush();
    }

    // Notificar users de Uploads
    public void notifica(String s) {
        out.println("NOTIFICA-"+s);
        out.flush();
    }

    // Search
    private void search(String s) {
        ArrayList<String> search = new ArrayList<>();
        String[] tags = s.split(",");
        for(String tag: tags){
            search.add(tag);
        }
        Set<Musica> musicas = app.filterTag(search);
        Iterator<Musica> it = musicas.iterator();
        out.println("SEARCHING-");
        out.flush();
        while(it.hasNext()) {
            Musica m = it.next();
            System.out.println(m);
            out.println(m.toString() + " {" +m.getDownloads()+"}");
        }
       // musicas.stream()
       //         .forEach(
        //                musica->out.println(
        //                        (musica.toString())+ " {" + musica.getDownloads()+"}")
       //         );
        //out.println(musicas);
        out.println("end");
        out.flush();
        System.out.println("done searching");
    }

    private void createTempDirectory(){
        Path path1 = Paths.get(temp);
        //Path path2 = Paths.get(temp2);
        if (!Files.exists(path1)) {
            try {
                Files.createDirectories(path1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //out.println("Existe o path");
        //out.flush();
    }

    // Upload
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
            out.println("UPLOADING-");
            out.flush();
            //System.out.println("File "+fileName+" received from client.");
            System.out.println(app.getAllMusicas());

        } catch (IOException ex) {
            System.err.println("Client error. Connection closed.");
        }
    }

    // Download
    private void sendFile(String input){
        createTempDirectory();
        System.out.println("sending");
        try {
            //handle file read
            out.println("DOWNLOADING-");
            out.flush();
            System.out.println(temp + input + ".mp3");
            File myFile = new File(temp+input+".mp3");
            //File myFile = new File(temp+ "5.mp3"); // para ja fica estatico :)
            byte[] mybytearray = new byte[(int) myFile.length()];
            System.out.println(myFile.length());

            FileInputStream fis = new FileInputStream(myFile);
            BufferedInputStream bis = new BufferedInputStream(fis);
            //bis.read(mybytearray, 0, mybytearray.length);

            DataInputStream dis = new DataInputStream(bis);
            dis.readFully(mybytearray, 0, mybytearray.length);

            //handle file send over socket
            OutputStream os = s.getOutputStream();

            //Sending file name and file size to the server
            DataOutputStream dos = new DataOutputStream(os);

            System.out.println("Filename: " + app.getMusicaString(Integer.valueOf(input)));
            dos.writeUTF(app.getMusicaString(Integer.valueOf(input)));
            dos.writeLong(mybytearray.length);
            dos.write(mybytearray, 0, mybytearray.length);
            app.downloadMusica(Integer.valueOf(input));
            dos.flush();
            System.out.println("File "+input+" sent to client.");
        } catch (Exception e) {

            //System.err.println("File does not exist!");
            e.printStackTrace();
        }
    }

    private void leave(){
        System.out.println("Trying to leave");
        out.println("LEAVE-");
        out.flush();
    }


}
