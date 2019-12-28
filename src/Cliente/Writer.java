package Cliente;

// Imports
import java.io.BufferedWriter;
import java.io.IOException;

public class Writer implements Runnable {

    // Variáveis de Instância
    private Menu menu;
    private BufferedWriter output;

    public Writer(Menu m, BufferedWriter o) {
        output = o;
        menu = m;
    }

    // Está constantemente a ler a "choice" do user  da classe "Menu" e ...
    public void run() {
        int choice;
        menu.show();
        while (((choice = menu.choice()) != -1)) {
            try {
                parse(choice);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //... dependendo da escolha, executa a ação correspondente
    private void parse (Integer choice) throws Exception {
        switch (menu.getState()) {
            case NOTLOGGED:
                if (choice == 0) 
                    System.exit(0);
                if (choice == 1) 
                    System.out.println("login");
                    login_signup(1);
                if (choice == 2) 
                    System.out.println("signup");
                    login_signup(2);
                break;
            case LOGGED:
                break;
            case REGISTERING:
                break;
            case SEARCHING:
                break;
            case UPLOADING:
                break;
            case DOWNLOADING:
                break;
        }
    }

    // No caso do login/signup, recebemos 
    private void login_signup(int i) throws IOException {
        String username = menu.lerDadosUser("Username: ");
        String password = menu.lerDadosUser("Password: ");
        String query;

        if (i == 1)
            query = "LOGIN"+"-"+username+","+password;
        else 
            query = "REGISTER"+"-"+username+","+password;

        output.write(query);
        output.newLine();
        output.flush();
    }
}
