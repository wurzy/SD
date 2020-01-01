package Cloud;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Queue {

    class Pedido{
        int id;      //id da musica
        String user; //login name do user

        Pedido (int id, String user) {
            this.id = id;
            this.user = user;
        }

        int getID(){return id;}
        String getUser() {return user;}
    }

    private ReentrantLock lock = new ReentrantLock();
    private Condition notFull = lock.newCondition();
    private Condition notEmpty = lock.newCondition();
    private ArrayList<Pedido> pedidos;
    private int MAX;

    public Queue(int MAX){
        this.pedidos = new ArrayList<>();
        this.MAX = MAX;
    }

    public void put(int id, String user) throws Exception{
        lock.lock();
        while(this.pedidos.size()>=MAX) {
            notFull.await();    //espero que seja removido
        }
        this.pedidos.add(new Pedido(id,user));
        notEmpty.signal();      //digo que ja tem 1 elemento
        lock.unlock();
    }

    public Pedido get() throws Exception{
        Pedido p;
        lock.lock();
        while(this.pedidos.size()==0) {
            notEmpty.await();      //espero pelo put
        }
        p = this.pedidos.get(0);
        this.pedidos.remove(0);
        notFull.signal();         //digo que removi 1 elemento
        lock.unlock();
        return p;
    }


}
