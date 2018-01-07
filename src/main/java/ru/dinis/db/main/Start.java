package ru.dinis.db.main;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.dinis.db.impl.SQLiteDao;
import ru.dinis.db.objects.MP3;

/**
 * Create by dinis of 07.01.18.
 */
public class Start {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");

        MP3 mp3 = new MP3();
        mp3.setName("tuman");
        mp3.setAuthor("Gazmanov");

        SQLiteDao sqLiteDao = (SQLiteDao) context.getBean("sqliteDao");
        sqLiteDao.insert(mp3);
    }

}
