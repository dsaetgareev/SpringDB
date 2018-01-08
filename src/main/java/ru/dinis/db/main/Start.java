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
        mp3.setName("soldat");
        mp3.setAuthor("Lube");

        SQLiteDao sqLiteDao = (SQLiteDao) context.getBean("sqliteDao");
//        sqLiteDao.insert(mp3);
//        sqLiteDao.allShow();
        System.out.println(sqLiteDao.getMP3Count());
//        sqLiteDao.showList(sqLiteDao.getMP3ListByAuthor("Lube"));
        System.out.println(sqLiteDao.getStat());
    }

}
