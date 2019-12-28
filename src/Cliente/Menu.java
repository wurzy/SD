package Cliente;

// Imports
import java.util.Scanner;

public class Menu {
    //private static final String CLEAR = "\u001b[2J\u001b[H";
    //private static final String RESET = "\u001B[0m";

    // Estados possíveis
    public enum State {
        NOTLOGGED,
        LOGGED,
        REGISTERING,
        SEARCHING,
        UPLOADING,
        DOWNLOADING;
    }

    // Variáveis de Instância
    private State state;
    private Scanner input;
    //private String username;
    //private String password;

    // Construtor (inicia o Menu no 1º menu e state, NOTLOGGED)
    public Menu(){
        input = new Scanner(System.in);
        this.state = State.NOTLOGGED;
    }

    // Mostra um específico menu de acordo com o "state" atual
    public void show() {
        switch (state) {
            case NOTLOGGED:
                System.out.println(/*CLEAR +*/"+----------------- MENU INICIAL -----------------+\n" +
                        "| 1 - LOG-IN                                     |\n" +
                        "| 2 - REGISTAR                                   |\n" +
                        "| 0 - SAIR                                       |\n" +
                        "+------------------------------------------------+\n"/*+ RESET*/);
                break;
            case LOGGED:
                System.out.println(/*CLEAR +*/"+----------------- MENU CLIENTE ------------------+\n" +
                        "| 1 - UPLOAD                                      |\n" +
                        "| 2 - DOWNLOAD                                    |\n" +
                        "| 3 - PROCURAR                                    |\n" +
                        "| 4 - NOTIFICAÇOES                                |\n" +
                        "| 0 - LOGOUT                                      |\n" +
                        "+ ------------------------------------------------+\n"/*+ RESET*/);
                break;
            case REGISTERING:
                System.out.println(/*CLEAR +*/"+------------------- REGISTAR --------------------+\n" +
                        "|                                                 |\n" +
                        "|            A registar cliente....               |\n" +
                        "|                                                 |\n" +
                        "+ ------------------------------------------------+\n"/*+ RESET*/);
                break;
            case SEARCHING: 
                break;
            case UPLOADING:
                break;
            case DOWNLOADING:
                break;
        }
        System.out.print("Opção: ");
    }

    // Regista a escolha do utilizador, se esta for válida (se não for válida, volta a pedir)
    public Integer choice() {
        int choice;
        try {
            choice = Integer.parseInt(input.nextLine());
            switch(state){
                case NOTLOGGED:
                    while(choice<0 || choice >2) {
                        System.out.print("Opção: ");
                        choice = Integer.parseInt(input.nextLine());
                    }
                    break;
                case LOGGED:
                    while(choice<0 || choice >3){
                        System.out.print("Opção: ");
                        choice = Integer.parseInt(input.nextLine());
                    }
                    break;
                default:
                    System.out.println("This is my final messsage... goodbye");
            }
        }
        catch(Exception e) {
            choice = -1;
        }
        return choice;
    }

    public String lerDadosUser(String pedido) {
        System.out.println(pedido);
        return input.nextLine();
    }

    // Getters e Setters
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}