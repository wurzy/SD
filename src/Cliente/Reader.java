package Cliente;

// Imports
import java.io.BufferedReader;
import java.io.IOException;

import Cliente.Menu.State;

public class Reader implements Runnable{
    //private static final String RESET = "\u001B[0m";

    // Variáveis de Instância
    private Menu menu;
    private BufferedReader input;

    Reader(Menu menu, BufferedReader input) {
        this.input = input;
        this.menu = menu;
    }

    // Está constantemente a ler do "BufferedReader" e ...
    public void run() {
        System.out.println("runReader");
        try {
            while (input.readLine() != null) {
                parseResponse(input.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ... dependendo do input (resposta do "Server"), muda o "state" da classe "Menu"
    private synchronized void parseResponse(String response){
        //String[] info = response.split(" ", 2);
        switch (/*info[0].toUpperCase()*/response) {
            case("DENIED"):
                System.out.println("Username/Password inválidas" /*+ RESET*/);
                menu.show();
                break;
            case ("LOGGEDIN"):
                menu.setState(State.LOGGED);
                menu.show();
                break;
            case("SIGNEDUP"):
                menu.setState(State.NOTLOGGED);
                menu.show();
                break;
            default:
                menu.show();
        }
    }
}
