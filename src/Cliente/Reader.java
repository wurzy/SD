package Cliente;

// Imports
import java.io.*;
import java.net.Socket;
import java.nio.file.*;
import Cliente.Menu.State;

public class Reader implements Runnable{

    // Variáveis de Instância
    //Windows: private final String temp2 = "C:\\Users\\User\\AppData\\Local\\Temp\\SoundCloud\\cliente\\";
    private final String temp2 = "/tmp/SoundCloud/cliente";
    private final int MAXSIZE = 500*1024;
    private Menu menu;
    private BufferedReader input;
    private Socket sock;

    // Construtor por Parâmetros
    Reader (Menu menu, BufferedReader input,Socket s) {
        this.input = input;
        this.menu = menu;
        this.sock = s;
    }

    // Está constantemente a ler do input do Cliente (BufferedReader input) (à espera do output do Servidor)
    public void run() {
        String s;
        try {
            while ((s = input.readLine()) != null) 
                parseResponse(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Dependendo da resposta do Server, muda o estado da classe Menu
    private synchronized void parseResponse(String response){
        switch (response) {
            case("DENIED_1"):
                System.out.println("Username/Password inválidos!");
                menu.show();
                break;
            case("DENIED_2"):
                System.out.println("Username já existe!");
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
            case("UPLOADING"):
                menu.setState(State.UPLOADING);
                menu.show();
                break;
            case("DOWNLOADING"):
                menu.setState(State.DOWNLOADING);
                recebeFicheiro(sock,"1");
                menu.show();
            default:
                menu.show();
        }
    }

    // Cria uma diretoria temporária
    private void createTempDirectory(){
        Path path2 = Paths.get(temp2);
        if (!Files.exists(path2)) {
            try {
                Files.createDirectories(path2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Download
    private void recebeFicheiro(Socket sock, String fileName) {
        createTempDirectory();
        try {
            int bytesRead;
            //Scanner sc = new Scanner(System.in);
            System.out.println("Written");
            InputStream in = sock.getInputStream();

            DataInputStream clientData = new DataInputStream(in);

            fileName = clientData.readUTF();
            System.out.println(fileName);
            OutputStream output = new FileOutputStream((temp2 + fileName + ".mp3"));
            long size = clientData.readLong();
            System.out.println(size);
            byte[] buffer = new byte[MAXSIZE];

            while (size > 0 && (bytesRead = clientData.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                output.write(buffer, 0, bytesRead);
                size -= bytesRead;
            }

            output.close();
            //in.close();

            System.out.println("File "+fileName+" received from Server.");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
