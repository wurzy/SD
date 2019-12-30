package Cliente;

// Imports
import java.io.*;
import java.net.Socket;
import Cliente.Menu.State;

public class Writer implements Runnable {

    // Variáveis de Instância
    private final int MAXSIZE = 500; // 500kb max
    private Menu menu;
    private BufferedWriter output;
    private Socket sk;

    // Construtor por Parâmetros
    public Writer(Menu m, BufferedWriter o, Socket s) {
        this.output = o;
        this.menu = m;
        this.sk = s;
    }

    // Está constantemente a ler a escolha do user (menu.choice())
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

    // Dependendo da escolha, executa a ação correspondente do menu atual
    private void parse(Integer choice) throws Exception {
        switch (menu.getState()) {
        case NOTLOGGED:
            if (choice == 0) 
                leave();
            if (choice == 1)
                login_signup(1);
            if (choice == 2)
                login_signup(2);
            break;
        case LOGGED:
            if (choice == 0) 
                logout();
            if (choice == 1)
                upload(sk);
            if (choice == 2)
                download();
            if (choice == 3)
                searching();
            if (choice == 4)
                //notificacoes();
            break;
        case REGISTERING:
            //menu.show();
            break;
        case SEARCHING:
            //searching();
            break;
        case UPLOADING:
            //menu.show();
            break;
        case DOWNLOADING:
            //menu.show();
            break;
        }
    }

    // Login / Signup
    private void login_signup(int i) throws IOException {
        String username = menu.lerDadosUser("Username: ");
        String password = menu.lerDadosUser("Password: ");
        String query;

        if (i == 1)
            query = "LOGIN" + "-" + username + "," + password;
        else
            query = "REGISTER" + "-" + username + "," + password;

        output.write(query);
        output.newLine();
        output.flush();
    }

    // Logout
    private void logout() throws IOException{
        String userAtual = menu.getUser();
        output.write("LOGOUT-"+userAtual); 
        output.newLine();
        output.flush();
        menu.setState(State.NOTLOGGED);
        menu.show(); 
    }

    // Download
    private void download() {
        try {
            String nrFile = menu.lerDadosUser("Nº File: ");
            output.write("DOWNLOAD-" + nrFile);
            output.newLine();
            output.flush();
        } catch (IOException e) {
            System.err.println("File does not exist!");
        }
    }

    // Upload
    private void upload(Socket s){
        try {
            String nome = menu.lerDadosUser("Nome: ");
            String artista = menu.lerDadosUser("Artista: ");
            String ano = menu.lerDadosUser("Ano: ");
            String tags = menu.lerTags();
            String file = menu.lerDadosUser("Filename: ");
            String fileName = "./effects/" + file + ".mp3";     // Linux
            //String fileName = ".\\effects\\" + file + ".mp3"; // Windows
            String query = nome+","+artista+","+ano+","+tags;

            File myFile = new File(fileName);
            long filesize = myFile.length();
            int stop = 0;

            //byte[] mybytearray = new byte[(int) myFile.length()];
            byte[] mybytearray = new byte[MAXSIZE];
            output.write("UPLOAD-");
            output.newLine();
            output.flush();
            FileInputStream fis = new FileInputStream(myFile);
            BufferedInputStream bis = new BufferedInputStream(fis);
            //bis.read(mybytearray, 0, mybytearray.length);

            OutputStream os = s.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            DataInputStream dis = new DataInputStream(bis);
            dos.writeUTF(query);
            dos.writeLong(filesize);
           /* do{
                stop+=dis.read(mybytearray, offset, (int)Math.min(mybytearray.length, filesize-stop));
                dos.write(mybytearray, offset, mybytearray.length);
                dos.flush();
                //stop+=mybytearray.length;
                System.out.println("Im currently at: " + stop + "/" + filesize);
            }while(stop<filesize);
            */
           //System.out.println("Filesize inicial: " + filesize);
            while (filesize > 0 && (stop = dis.read(mybytearray, 0, (int) Math.min(MAXSIZE, filesize))) != -1) {
                dos.write(mybytearray, 0, stop);
                System.out.println("Im currently at: " + filesize);
                filesize -= stop;
            }
            dos.flush();
            //Sending file name and file size to the server
            System.out.println("File " + fileName + " sent to Server.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void leave(){
        try{
            output.write("LEAVE-");
            output.newLine();
            output.flush();
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }

    private void searching(){
        String tags = menu.tags();
        String query = "SEARCH-"+tags;
        try {
            output.write(query);
            output.newLine();
            output.flush();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

}
