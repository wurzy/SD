package Servidor;

import java.util.Scanner;

public class w {

    public static void main(String[] args) {
        String tag;
        Scanner sc = new Scanner(System.in);
        String tags = "";
        while(!(tag=sc.nextLine()).equals("q")){
            tags+=tag + ",";
        }
        tags = tags.substring(0,tags.length()-1);
        System.out.println(tags);
    }
}
