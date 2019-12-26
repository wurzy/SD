package Cliente;

public class Client {

    public static void main(String[] args){
        Menu menu = new Menu();
        System.out.println(menu.getState());
        menu.show();
        menu.choice();
    }
}
