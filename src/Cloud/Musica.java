package Cloud;

import java.util.ArrayList;

public class Musica {
    private int id;
    private String nome;
    private String artista;
    private String genero;
    private ArrayList<String> tags;

    public Musica(int id, String nome, String artista, String genero, ArrayList<String> tags){
        this.id = id;
        this.nome = nome;
        this.artista = artista;
        this.genero = genero;
        this.tags = tags;
    }


}
