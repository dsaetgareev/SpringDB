package ru.dinis.db.objects;


/**
 * Create by dinis of 06.01.18.
 */
public class MP3 {

    private int id;

    private String name;

    private String author;

    public MP3() {

    }

    public MP3(String name, String author) {
        this.name = name;
        this.author = author;
    }

    public String getName() {
        return name;
    }



    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
