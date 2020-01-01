package Cloud;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import Servidor.Worker;

public class SoundCloud {
    private ReentrantLock lock;

    private HashMap<String,String> utilizadores;
    private HashMap<String,Worker> usersLogged;
    private HashMap<Integer,Musica> musicas;
    private int lastAdded = 0; // so para saber o ultimo ID, mais rapido

    //private Queue queue;
    //private int MAXDOWNLOADS = 1;
    //private ReentrantLock downloads;
    //private HashMap<String,Integer> num_downloads;
    //private Condition tooMany;


    public SoundCloud(){
        this.utilizadores = new HashMap<>();
        this.musicas = new HashMap<>();
        this.lock = new ReentrantLock();
        //this.downloads = new ReentrantLock();
        //this.tooMany = downloads.newCondition();
        this.usersLogged = new HashMap<>();
        //this.num_downloads = new HashMap<>();
    }

    public void insertLogged(String user, Worker wkr) {
        System.out.println("Inserimos user: " +user);
        this.usersLogged.put(user,wkr);

    }

    public void removeLogged(String user) {
        System.out.println("Removemos user: " +user);
        this.usersLogged.remove(user);
    }

    public boolean isLogged(String user) {
        boolean b;
        lock.lock();
        b = this.usersLogged.containsKey(user);
        lock.unlock();
        return b;
    }

    public boolean login(String user,String pw){
        boolean bool = false;
        lock.lock();
        if(this.utilizadores.containsKey(user)){
            if (this.utilizadores.get(user).equals(pw)) {
                bool = true;
            }
        }
        lock.unlock();
        return bool;
    }

    public boolean register(String user, String pw) {
        System.out.println("tou no register da soundcloud");
        lock.lock();
        if(this.utilizadores.containsKey(user)) {
            lock.unlock();
            return false;
        }
        this.utilizadores.put(user,pw);
        lock.unlock();
        return true;
    }

    public int addMusica(Musica m){
        int i;
        lock.lock();
        this.lastAdded++;
        m.setId(lastAdded);
        i = lastAdded;
        this.musicas.put(lastAdded,m);

        // Vai notificar todos os users que estão logged in
        Iterator it = this.usersLogged.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Worker w = (Worker)pair.getValue();
            String s = (String)pair.getKey();
            System.out.println("User "+s+" vai ser notificado.");
            w.notifica("*** A música "+m.getNome()+" do artista "+m.getArtista()+" foi uploaded! ***");
            //it.remove();
        }

        lock.unlock();
        return i;
    }

    public String getMusicaString(int id){
        String s = null;
        lock.lock();
        if(this.musicas.containsKey(id)) {
            s = this.musicas.get(id).toString();
        }
        lock.unlock();
        return s;
    }

    public void allowMusica(int id){
        lock.lock();
        /*
        try{
            System.out.println("Started sleep");
            Thread.sleep(10000);
            System.out.println("Stopped sleep");
        }
        catch(Exception e) {
            System.out.println("Erro no sleep");
        }
        */
        this.musicas.get(id).allowDownload();
        lock.unlock();
    }

    public boolean downloadMusica(int id) throws Exception{
        boolean b = false;
        lock.lock();
        if(this.musicas.containsKey(id)){
            System.out.println("Estou a ver se e possivel");
            this.musicas.get(id).available();
            System.out.println("E possivel");
            //verificar se ele nao passou o limite
            b = true;
            this.musicas.get(id).downloaded();
        }
        lock.unlock();
        return b;
    }

    //public void addDownload(String user, int id){}

    public HashMap<Integer,Musica> getAllMusicas(){
        lock.lock();
        HashMap<Integer,Musica> musicas = this.musicas;
        lock.unlock();
        return musicas;
    }

    public Set<Musica> filterTag(ArrayList<String> tags){
        lock.lock();
        Set<Musica> ret = new TreeSet<>();
        for(String tag: tags){
            for(Musica m : musicas.values()){
                if(m.containsTag(tag)) {
                    ret.add(m);
                }
            }
        }
        lock.unlock();
        return ret;
    }

    /*
    public void addToQueue(int id, String user) throws Exception{
        lock.lock();
        //this.num_downloads.put(user,)
        //this.queue.put(id,user);
        lock.unlock();
    }
    */
}
