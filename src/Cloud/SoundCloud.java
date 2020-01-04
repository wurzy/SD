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

    private int MAXDOWNLOADS = 5;
    private int num_downloads = 0;
    private Condition tooMany;


    public SoundCloud(){
        this.utilizadores = new HashMap<>();
        this.musicas = new HashMap<>();
        this.lock = new ReentrantLock();
        this.tooMany = lock.newCondition();
        this.usersLogged = new HashMap<>();
    }

    public void insertLogged(String user, Worker wkr) {
        this.usersLogged.put(user,wkr);

    }

    public void removeLogged(String user) {
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
            w.notifica("*** A música "+m.getNome()+" do artista "+m.getArtista()+" foi uploaded! ***");
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
        this.musicas.get(id).allowDownload();
        lock.unlock();
    }

    public boolean downloadMusica(int id) throws Exception{
        boolean b = false;
        lock.lock();
        if(this.musicas.containsKey(id)){
            this.musicas.get(id).available();
            num_downloads++;
            while(num_downloads > MAXDOWNLOADS) {
                tooMany.await();
            }
            b = true;
            this.musicas.get(id).downloaded();
        }
        lock.unlock();
        return b;
    }

    public void finished(){
        lock.lock();
        num_downloads--;
        tooMany.signalAll();
        lock.unlock();
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
}
