package Cliente;

// Imports
import java.util.Scanner;

public class Menu {

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
    private String user;

    // Construtor vazio (inicia o Menu no 1º menu e state, NOTLOGGED)
    public Menu(){
        input = new Scanner(System.in);
        this.state = State.NOTLOGGED;
    }

    // Mostra um específico menu de acordo com o "state" atual
    public void show() {
        switch (state) {
            case NOTLOGGED:
                //clearScreen();
                System.out.println("State:" + this.state);
                System.out.println("+----------------- MENU INICIAL -----------------+\n" +
                        "| 1 - LOG-IN                                     |\n" +
                        "| 2 - REGISTAR                                   |\n" +
                        "| 0 - SAIR                                       |\n" +
                        "+------------------------------------------------+\n");
                break;
            case LOGGED:
                //clearScreen();
                System.out.println("State:" + this.state);
                System.out.println("+----------------- MENU CLIENTE ------------------+\n" +
                        "| 1 - UPLOAD                                      |\n" +
                        "| 2 - DOWNLOAD                                    |\n" +
                        "| 3 - PROCURAR                                    |\n" +
                        "| 0 - LOGOUT                                      |\n" +
                        "+ ------------------------------------------------+\n");
                break;
            case REGISTERING:
                System.out.println("State:" + this.state);
                System.out.println("+------------------- REGISTAR --------------------+\n" +
                        "|                                                 |\n" +
                        "|            A registar cliente....               |\n" +
                        "|                                                 |\n" +
                        "+ ------------------------------------------------+\n");
                break;
            case SEARCHING:
                System.out.println("State:" + this.state);
                break;
            case UPLOADING:
                System.out.println("State:" + this.state);
                System.out.println("+-------------------------------------------------+\n" +
                        "|                                                 |\n" +
                        "|                   Uploading....                 |\n" +
                        "|                                                 |\n" +
                        "+-------------------------------------------------+\n");
                break;
            case DOWNLOADING:
                System.out.println("State:" + this.state);
                System.out.println("+------------------- REGISTAR --------------------+\n" +
                        "|                                                 |\n" +
                        "|                  Downloading....                |\n" +
                        "|                                                 |\n" +
                        "+ ------------------------------------------------+\n");
                break;
        }
        System.out.println("State:" + this.state);
        System.out.print("Opção: ");
    }

    // Regista a escolha do utilizador, se esta for válida (se não for válida, volta a pedir)
    public Integer choice() {
        int choice = -1;
        while((choice = choice2()) == -1) {
            System.out.println("Insira opção válida!");
            System.out.print("Opção: ");
        }
        return choice;
    }

    private Integer choice2() {
        int choice = -1;
        try {
            choice = Integer.parseInt(input.nextLine());
            switch(state){
                case NOTLOGGED:
                    while(choice<0 || choice >2) {
                    System.out.println("Insira opção válida!");
                    System.out.print("Opção: ");
                    choice = Integer.parseInt(input.nextLine());
                }
                break;
                case LOGGED:
                    while(choice<0 || choice >3){
                    System.out.println("Insira opção válida!");
                    System.out.print("Opção: ");
                    choice = Integer.parseInt(input.nextLine());
                }
                break;
                default:
                    System.out.println("This is my final messsage... goodbye");
            }
        }
        catch(NumberFormatException e) {
            choice = -1;
        }
        return choice;
    }

    public String lerDadosUser(String pedido) {
        System.out.println(pedido);
        return input.nextLine();
    }

    public String lerTags() {
        String listTags = "";
        System.out.println("Tag: ");
        String tag = input.nextLine();
        listTags = listTags+tag;
        System.out.println("Deseja inserir mais tags? S/N");
        String sn = input.nextLine();
        while (sn.compareTo("S") == 0) {
            System.out.println("Tag: ");
            tag = input.nextLine();
            listTags = listTags+"%"+tag;
            System.out.println("Deseja inserir mais tags? S/N");
            sn = input.nextLine();
        }
        System.out.println(listTags);
        return listTags;
    }

    // Limpa a consola
    public void clearScreen() {
        System.out.println("\033[H\033[2J");
        System.out.flush();
    }

    public String tags(){
        String tag;
        String tags = "";
        System.out.println("Tag (q=Exit): ");
        while(!(tag=input.nextLine()).equals("q")){
            tags+=tag + ",";
        }
        tags = tags.substring(0,tags.length()-1);
        return tags;
    }

    public String getConfirm(){
        System.out.println("Clique numa tecla para confirmar.");
        System.out.flush();
        String s = input.nextLine();
        return s;
    }


	public void notificaUser(String string) {
        System.out.println(string);
    }
    
    // Getters e Setters
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}