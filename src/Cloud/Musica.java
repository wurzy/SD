package Cloud;

import java.util.ArrayList;

public class Musica implements Comparable<Musica> {
    private int id;
    private String nome;
    private String artista;
    private int ano;
    private ArrayList<String> tags;
    private int downloads;

    public Musica(String nome, String artista, int ano, ArrayList<String> tags){
        this.id = -1;
        this.nome = nome;
        this.artista = artista;
        this.tags = tags;
        this.downloads = 0;
        this.ano = ano;
    }

    public synchronized void setId(int id) {
        this.id = id;
    }

    public synchronized int getAno(){
        return this.ano;
    }

    public synchronized int getId(){
        return this.id;
    }

    public synchronized String getNome(){
        return this.nome;
    }

    public synchronized String getArtista(){
        return this.artista;
    }

    public synchronized ArrayList<String> getTags(){
        return this.tags;
    }

    public synchronized int getDownloads(){
        return this.downloads;
    }

    public synchronized void downloaded(){
        this.downloads++;
    }

    public synchronized String toString(){
        return this.id + " - "
                + this.nome + ", "
                + this.artista
                + " (" + this.ano + ") "
                +this.tags.toString()
                + " {" + this.downloads + "}";
    }

    public int compareTo(Musica m) {
        return Integer.compare(this.id,m.getId());
    }

    public synchronized boolean containsTag(String s){
        return this.tags.stream().anyMatch(tag -> tag.equals(s));
    }

}
