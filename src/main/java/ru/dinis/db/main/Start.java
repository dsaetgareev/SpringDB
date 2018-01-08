package ru.dinis.db.main;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.dinis.db.impl.SQLiteDao;
import ru.dinis.db.interfaces.MP3Dao;
import ru.dinis.db.objects.MP3;

/**
 * Create by dinis of 07.01.18.
 */
public class Start {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        MP3Dao mp3Dao = (MP3Dao) context.getBean("sqliteDao");
        MP3 mp3 = new MP3("tututu", "nusha");
        mp3Dao.insert(mp3);
        mp3Dao.allShow();
    }

}
