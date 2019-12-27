package Cliente;

import java.io.BufferedWriter;

public class Stub implements Runnable {
    private Menu menu;
    private BufferedWriter output;

    public Stub(Menu m, BufferedWriter o) {
        output = o;
        menu = m;
    }

    @Override
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

    private void parse (Integer choice) throws Exception {
        switch (menu.getState()) {
            case UNAUTH:
                if (choice==1) {
                    //login();
                }
                if (choice==2) {
                    //signup();
                }
                if (choice==0) {
                    System.exit(0);
                }
                break;
            case AUTH:
                if (choice==1) {
                    //sendMessage("QUEUE");
                }
                if (choice==2) {
                    //sendMessage("INFO");
                }
                if (choice==0) {
                    //sendMessage("LOGOUT");
                }
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

}
