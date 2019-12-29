package Cloud;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import Servidor.Worker;

public class SoundCloud {
    private HashMap<String,String> utilizadores;
    private HashMap<Integer,Musica> musicas;
    private int lastAdded = 0; // so para saber o ultimo ID
    private ReentrantLock lock;

    private HashMap<String,Worker> usersLogged;

    public SoundCloud(){
        this.utilizadores = new HashMap<>();
        this.musicas = new HashMap<>();
        this.lock = new ReentrantLock();
        this.usersLogged = new HashMap<>();
    }

    public void insertLogged(String user, Worker wkr) {
        System.out.println("Inserimos user: " +user);
        this.usersLogged.put(user,wkr);

    }

    public void removeLogged(String user) {
        System.out.println("Removemos user: " +user);
        this.usersLogged.remove(user);
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
        lock.lock();
        String s = this.musicas.get(id).toString();
        lock.unlock();
        return s;
    }

    public boolean downloadMusica(int id){
        boolean b = false;
        lock.lock();
        if(this.musicas.containsKey(id)){
            b = true;
            this.musicas.get(id).downloaded();
        }
        lock.unlock();
        return b;
    }

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
}
