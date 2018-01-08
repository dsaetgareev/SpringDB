package ru.dinis.db.main;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.dinis.db.interfaces.MP3Dao;
import ru.dinis.db.objects.MP3;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by dinis of 07.01.18.
 */
public class Start {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        MP3Dao mp3Dao = (MP3Dao) context.getBean("sqliteDao");
        MP3 mp3 = mp3Dao.getMP3ById(1);
        mp3.setName("rebata");

        MP3 mp31 = mp3Dao.getMP3ById(5);
        mp31.setAuthor("lube");

        List<MP3> list = new ArrayList<MP3>();
        list.add(mp3);
        list.add(mp31);

        mp3Dao.updateById(list);
        mp3Dao.allShow();
    }

}
