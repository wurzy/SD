package Servidor;
import Cloud.Musica;
import Cloud.SoundCloud;

import java.io.*;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.*;

public class Worker implements Runnable {
    private BufferedReader in;
    private PrintWriter out;
    private Socket s;

    private SoundCloud app;

    public Worker(Socket s, SoundCloud sc) throws IOException {
        this.in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        this.out = new PrintWriter(s.getOutputStream());
        this.s = s;
        this.app = sc;
    }

    public void run() {
        String input;
        int i = 0;
        while(true) {
            try {
                input=in.readLine();
                if(input == null || input.equals("q")) {
                    s.shutdownInput();
                    s.shutdownOutput();
                    s.close();
                    break;
                }
                parse(input,i);
                i++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void parse(String input,int i){
        String[] partes = input.split("-",2);
        switch(partes[0]){
            case "LOGIN":
                login(partes[1]);
                break;
            case "REGISTER":
                register(partes[1]);
                break;
            case "UPLOAD":
                upload(partes[1]);
                break;
            case "SEARCH":
                search(partes[1]);
                break;
            default:
                ArrayList<String> list = new ArrayList<>();
                Collections.addAll(list,"pop","wow","rap");
                Musica musica = new Musica("Amazing","Kanye West"+i,2007,list);
                app.addMusica(musica);
                System.out.println(app.getAllMusicas().toString());
                out.println(app.getAllMusicas().toString());
                out.flush();

                break;
        }
    }
    private void login(String s){
        String[] auth = s.split(",");
        boolean b = app.login(auth[0],auth[1]);
        out.println(b);
        out.flush();
    }

    private void register(String s){
        String[] reg = s.split(",");
        boolean br = app.register(reg[0],reg[1]);
        out.println(br);
        out.flush();
    }

    private void upload(String s){

        String[] metadados = s.split("%"); // antes do % ta o resto dos dados, depois array de bytes
       // byte[] bytes =

        /*
        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list,"pop","wow","rap");
        Musica musica = new Musica("Amazing","Kanye West"+i,2007,list);
        //out.println(musica.toString());
        app.addMusica(musica);
        //out.println("Nova Iteracao");
        System.out.println(app.getAllMusicas().toString());
        out.println(app.getAllMusicas().toString());
        out.flush();
        */
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

}
