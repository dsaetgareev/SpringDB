package ru.dinis.db.objects;


/**
 * Create by dinis of 06.01.18.
 */
public class MP3 {

    private int id;

    private String name;

    private Author author;

    public MP3() {

    }

    public MP3(String name, Author author) {
        this.name = name;
        this.author = author;
    }

    public String getName() {
        return name;
    }



    public void setName(String name) {
        this.name = name;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s", this.id, this.name, this.author);
    }
}
