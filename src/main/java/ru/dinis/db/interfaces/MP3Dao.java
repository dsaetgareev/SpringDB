package ru.dinis.db.interfaces;

import ru.dinis.db.objects.MP3;

import java.util.List;

/**
 * Create by dinis of 06.01.18.
 */
public interface MP3Dao {

    void insert(MP3 mp3);

    void insert(List<MP3> mp3List);

    void delete(int id);

    void delete(MP3 mp3);

    MP3 getMP3ById(int id);

    List<MP3> getMP3ListByName(String name);

    List<MP3> getMP3ListByAuthor(String author);
}
