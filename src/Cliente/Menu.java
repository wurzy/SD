package Cliente;
import java.util.Scanner;

public class Menu {
    private static final String CLEAR = "\u001b[2J\u001b[H";
    private static final String RESET = "\u001B[0m";

    public enum State {
        UNAUTH,
        AUTH,
        REGISTERING,
        SEARCHING,
        UPLOADING,
        DOWNLOADING;
    }

    private State state;
    private Scanner input;
    private String username;
    private String password;

    public Menu(){
        input = new Scanner(System.in);
        this.state = State.AUTH;
    }

    public void show() {
        switch (state) {
            case UNAUTH:
                System.out.println(CLEAR+"+----------------- MENU INICIAL -----------------+\n" +
                        "| 1 - LOG-IN                                     |\n" +
                        "| 2 - REGISTAR                                   |\n" +
                        "| 0 - SAIR                                       |\n" +
                        "+------------------------------------------------+\n"+RESET);
                break;
            case AUTH:
                System.out.println(CLEAR+"+----------------- MENU CLIENTE ------------------+\n" +
                        "| 1 - UPLOAD                                      |\n" +
                        "| 2 - DOWNLOAD                                    |\n" +
                        "| 3 - PROCURAR                                    |\n" +
                        "| 0 - LOGOUT                                      |\n" +
                        "+ ------------------------------------------------+\n"+RESET);
                break;
            case REGISTERING:
                System.out.println(CLEAR+"+------------------- REGISTAR --------------------+\n" +
                        "|                                                 |\n" +
                        "|            A registar cliente....               |\n" +
                        "|                                                 |\n" +
                        "+ ------------------------------------------------+\n"+RESET);
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

    public Integer choice() {
        int choice;
        try {
            choice = Integer.parseInt(input.nextLine());
            switch(state){
                case UNAUTH:
                    while(choice<0 || choice >2) {
                        System.out.print("Opção: ");
                        choice = Integer.parseInt(input.nextLine());
                    }
                    break;
                case AUTH:
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

    public String readString(String question) {
        System.out.println(question);
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
